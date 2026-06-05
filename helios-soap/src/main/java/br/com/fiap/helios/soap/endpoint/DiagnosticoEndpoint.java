package br.com.fiap.helios.soap.endpoint;

import br.com.fiap.helios.soap.config.WebServiceConfig;
import br.com.fiap.helios.soap.domain.Diagnostico;
import br.com.fiap.helios.soap.domain.EntradaDiagnostico;
import br.com.fiap.helios.soap.domain.causa.Causa;
import br.com.fiap.helios.soap.service.HistoricoDiagnostico;
import br.com.fiap.helios.soap.service.MotorDiagnostico;
import br.com.fiap.helios.soap.ws.ConsultarHistoricoRequest;
import br.com.fiap.helios.soap.ws.ConsultarHistoricoResponse;
import br.com.fiap.helios.soap.ws.DiagnosticarRequest;
import br.com.fiap.helios.soap.ws.DiagnosticarResponse;
import br.com.fiap.helios.soap.ws.DiagnosticoType;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * Endpoint SOAP do serviço de Diagnóstico.
 *
 * <ul>
 *   <li>{@code diagnosticar} — processamento: cruza os sinais e devolve a causa.</li>
 *   <li>{@code consultarHistorico} — consulta: diagnósticos já feitos para um painel.</li>
 * </ul>
 */
@Endpoint
public class DiagnosticoEndpoint {

    private final MotorDiagnostico motor;
    private final HistoricoDiagnostico historico;

    public DiagnosticoEndpoint(MotorDiagnostico motor, HistoricoDiagnostico historico) {
        this.motor = motor;
        this.historico = historico;
    }

    @PayloadRoot(namespace = WebServiceConfig.NAMESPACE, localPart = "diagnosticarRequest")
    @ResponsePayload
    public DiagnosticarResponse diagnosticar(@RequestPayload DiagnosticarRequest request) {
        EntradaDiagnostico entrada = new EntradaDiagnostico(
                request.getAtivoId(),
                request.getValorEnergia(),
                request.getEsperadoEnergia(),
                request.getGrauSujeira(),
                request.isQuedaGradual(),
                request.isPontoQuente(),
                request.isSolBaixo(),
                request.isFrio());

        Diagnostico diagnostico = motor.diagnosticar(entrada);
        historico.registrar(diagnostico);

        DiagnosticarResponse response = new DiagnosticarResponse();
        response.setDiagnostico(toType(diagnostico));
        return response;
    }

    @PayloadRoot(namespace = WebServiceConfig.NAMESPACE, localPart = "consultarHistoricoRequest")
    @ResponsePayload
    public ConsultarHistoricoResponse consultarHistorico(@RequestPayload ConsultarHistoricoRequest request) {
        List<Diagnostico> diagnosticos = historico.consultar(request.getAtivoId());

        ConsultarHistoricoResponse response = new ConsultarHistoricoResponse();
        for (Diagnostico d : diagnosticos) {
            response.getDiagnostico().add(toType(d));
        }
        return response;
    }

    /** Converte o resultado de domínio para o tipo do contrato SOAP. */
    private DiagnosticoType toType(Diagnostico diagnostico) {
        Causa causa = diagnostico.getCausa();
        DiagnosticoType tipo = new DiagnosticoType();
        tipo.setAtivoId(diagnostico.getAtivoId());
        tipo.setCausa(causa.nome());
        tipo.setConfianca(causa.confianca());
        tipo.setSeveridade(causa.severidade().name());
        tipo.setAcaoRecomendada(causa.acaoRecomendada().name());
        tipo.setIntensidadeVibracao(causa.intensidadeVibracao());
        tipo.getEvidencia().addAll(causa.evidencias());
        tipo.setTimestamp(diagnostico.getTimestamp().toString());
        return tipo;
    }
}
