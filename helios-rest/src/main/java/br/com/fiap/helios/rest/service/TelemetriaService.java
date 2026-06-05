package br.com.fiap.helios.rest.service;

import br.com.fiap.helios.rest.domain.Alerta;
import br.com.fiap.helios.rest.domain.Diagnostico;
import br.com.fiap.helios.rest.domain.Leitura;
import br.com.fiap.helios.rest.domain.PainelSolar;
import br.com.fiap.helios.rest.dto.LeituraRequest;
import br.com.fiap.helios.rest.dto.TelemetriaResponse;
import br.com.fiap.helios.rest.dto.TelemetriaResponse.AlertaResumo;
import br.com.fiap.helios.rest.dto.TelemetriaResponse.AmbienteResumo;
import br.com.fiap.helios.rest.dto.TelemetriaResponse.ComandoVibracaoResumo;
import br.com.fiap.helios.rest.dto.TelemetriaResponse.DiagnosticoResumo;
import br.com.fiap.helios.rest.external.Ambiente;
import br.com.fiap.helios.rest.external.AmbienteService;
import br.com.fiap.helios.rest.repository.AlertaRepository;
import br.com.fiap.helios.rest.repository.DiagnosticoRepository;
import br.com.fiap.helios.rest.repository.LeituraRepository;
import br.com.fiap.helios.rest.soap.DiagnosticoSoapClient;
import br.com.fiap.helios.rest.soap.contract.DiagnosticarRequest;
import br.com.fiap.helios.rest.soap.contract.DiagnosticoType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Orquestra o loop do HÉLIOS: recebe a telemetria, consulta o ambiente externo,
 * pede o diagnóstico ao Web Service SOAP, persiste tudo e decide a ação.
 *
 * <p>É o ponto onde os serviços distribuídos se integram (REST → externo → SOAP).</p>
 */
@Service
public class TelemetriaService {

    private final PainelSolarService painelService;
    private final AmbienteService ambienteService;
    private final DiagnosticoSoapClient soapClient;
    private final LeituraRepository leituraRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final AlertaRepository alertaRepository;

    public TelemetriaService(PainelSolarService painelService,
                             AmbienteService ambienteService,
                             DiagnosticoSoapClient soapClient,
                             LeituraRepository leituraRepository,
                             DiagnosticoRepository diagnosticoRepository,
                             AlertaRepository alertaRepository) {
        this.painelService = painelService;
        this.ambienteService = ambienteService;
        this.soapClient = soapClient;
        this.leituraRepository = leituraRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.alertaRepository = alertaRepository;
    }

    @Transactional
    public TelemetriaResponse processar(Long painelId, LeituraRequest req) {
        PainelSolar painel = painelService.buscarPorId(painelId);
        LocalDateTime agora = LocalDateTime.now();

        // 1) Ambiente (consumo de serviço externo, com fallback simulado)
        Ambiente ambiente = ambienteService.consultar(painel.getLatitude(), painel.getLongitude());

        // 2) Persiste a leitura
        Leitura leitura = new Leitura();
        leitura.setPainel(painel);
        leitura.setValorEnergia(req.valorEnergia());
        leitura.setEsperadoEnergia(painel.getPotenciaNominalW());
        leitura.setGrauSujeira(req.grauSujeira());
        leitura.setQuedaGradual(req.quedaGradual());
        leitura.setPontoQuente(req.pontoQuente());
        leitura.setCriadoEm(agora);
        leitura = leituraRepository.save(leitura);

        // 3) Diagnóstico via Web Service SOAP
        DiagnosticarRequest soapReq = new DiagnosticarRequest();
        soapReq.setAtivoId(painel.getCodigo());
        soapReq.setValorEnergia(req.valorEnergia());
        soapReq.setEsperadoEnergia(painel.getPotenciaNominalW());
        soapReq.setGrauSujeira(req.grauSujeira());
        soapReq.setQuedaGradual(req.quedaGradual());
        soapReq.setPontoQuente(req.pontoQuente());
        soapReq.setSolBaixo(ambiente.solBaixo());
        soapReq.setFrio(ambiente.frio());
        DiagnosticoType resultado = soapClient.diagnosticar(soapReq);

        // 4) Persiste o diagnóstico
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setPainel(painel);
        diagnostico.setCausa(resultado.getCausa());
        diagnostico.setConfianca(resultado.getConfianca());
        diagnostico.setSeveridade(resultado.getSeveridade());
        diagnostico.setAcaoRecomendada(resultado.getAcaoRecomendada());
        diagnostico.setIntensidadeVibracao(resultado.getIntensidadeVibracao());
        diagnostico.setEvidencias(List.copyOf(resultado.getEvidencia()));
        diagnostico.setCriadoEm(agora);
        diagnosticoRepository.save(diagnostico);

        // 5) Decide a ação: comando de vibração (só sujeira) e/ou alerta
        double perdaPct = perdaPercentual(req.valorEnergia(), painel.getPotenciaNominalW());
        ComandoVibracaoResumo comando = montarComando(painel, resultado);
        AlertaResumo alerta = montarAlerta(painel, resultado, perdaPct, agora);

        return new TelemetriaResponse(
                painel.getId(),
                painel.getCodigo(),
                leitura.getId(),
                new AmbienteResumo(ambiente.solBaixo(), ambiente.frio(), ambiente.fonte()),
                new DiagnosticoResumo(
                        resultado.getCausa(),
                        resultado.getConfianca(),
                        resultado.getSeveridade(),
                        resultado.getAcaoRecomendada(),
                        resultado.getIntensidadeVibracao(),
                        List.copyOf(resultado.getEvidencia()),
                        resultado.getTimestamp()),
                comando,
                alerta);
    }

    private ComandoVibracaoResumo montarComando(PainelSolar painel, DiagnosticoType resultado) {
        if (!"VIBRAR".equals(resultado.getAcaoRecomendada()) || resultado.getIntensidadeVibracao() == null) {
            return null;
        }
        double intensidade = resultado.getIntensidadeVibracao();
        int duracao = (int) Math.round(intensidade * 12);
        return new ComandoVibracaoResumo("VIB-" + painel.getCodigo(), "VIBRAR", intensidade, duracao);
    }

    private AlertaResumo montarAlerta(PainelSolar painel, DiagnosticoType resultado,
                                      double perdaPct, LocalDateTime agora) {
        // Só gera alerta para causas de severidade ALTA (sujeira, dano, falha).
        if (!"ALTA".equals(resultado.getSeveridade())) {
            return null;
        }
        Alerta alerta = new Alerta();
        alerta.setPainel(painel);
        alerta.setSeveridade(resultado.getSeveridade());
        alerta.setTipo(resultado.getCausa() + "_DETECTADA");
        alerta.setMensagem(String.format(
                "Perda de %.0f%% — causa %s. Ação recomendada: %s.",
                perdaPct, resultado.getCausa(), resultado.getAcaoRecomendada()));
        alerta.setResolvido(false);
        alerta.setCriadoEm(agora);
        alerta = alertaRepository.save(alerta);
        return new AlertaResumo(alerta.getId(), alerta.getSeveridade(),
                alerta.getTipo(), alerta.getMensagem(), alerta.isResolvido());
    }

    private double perdaPercentual(double valor, double esperado) {
        if (esperado <= 0) {
            return 0;
        }
        return Math.max(0, (esperado - valor) / esperado) * 100.0;
    }
}
