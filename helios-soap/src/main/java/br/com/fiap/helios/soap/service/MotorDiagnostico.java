package br.com.fiap.helios.soap.service;

import br.com.fiap.helios.soap.domain.Diagnostico;
import br.com.fiap.helios.soap.domain.EntradaDiagnostico;
import br.com.fiap.helios.soap.domain.causa.Ambiental;
import br.com.fiap.helios.soap.domain.causa.Causa;
import br.com.fiap.helios.soap.domain.causa.DanoFisico;
import br.com.fiap.helios.soap.domain.causa.FalhaEletrica;
import br.com.fiap.helios.soap.domain.causa.SemFalha;
import br.com.fiap.helios.soap.domain.causa.Sombra;
import br.com.fiap.helios.soap.domain.causa.Sujeira;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Cérebro do HÉLIOS: classifica a causa da queda de energia cruzando os sinais
 * (fusão de sensores), seguindo a matriz de diagnóstico do projeto.
 *
 * <p>A ordem das regras importa — vai do mais conclusivo ao mais ambíguo.</p>
 */
@Service
public class MotorDiagnostico {

    /** Grau mínimo de cobertura para considerar sujeira acionável. */
    private static final double LIMIAR_SUJEIRA = 0.15;
    /** Abaixo disso o painel é considerado limpo. */
    private static final double LIMIAR_LIMPO = 0.05;

    public Diagnostico diagnosticar(EntradaDiagnostico in) {
        validar(in);

        final List<String> evidencias = new ArrayList<>();
        evidencias.add("queda_" + fmt(in.queda()));

        final Causa causa;
        if (!in.houveQueda()) {
            causa = new SemFalha(0.95, List.of("sem_queda_significativa"));
        } else if (in.pontoQuente()) {
            // Dano físico é CONCLUSIVO: tem prioridade e nunca é mascarado pelo ambiente,
            // senão um painel trincado num dia frio/nublado seria silenciado como AMBIENTAL.
            evidencias.add("ponto_quente");
            causa = new DanoFisico(0.85, evidencias);
        } else if (in.solBaixo() || in.frio()) {
            if (in.solBaixo()) evidencias.add("sol_baixo");
            if (in.frio()) evidencias.add("frio");
            causa = new Ambiental(0.80, evidencias);
        } else if (in.grauSujeira() >= LIMIAR_SUJEIRA && in.quedaGradual()) {
            evidencias.add("cobertura_" + fmt(in.grauSujeira()));
            evidencias.add("queda_gradual");
            evidencias.add("ambiente_normal");
            double confianca = Math.clamp(0.60 + in.grauSujeira() * 0.40, 0.60, 0.98);
            causa = new Sujeira(in.grauSujeira(), confianca, evidencias);
        } else if (in.grauSujeira() < LIMIAR_LIMPO) {
            // Diagnóstico de EXCLUSÃO: só após descartar dano e ambiente. Painel limpo,
            // ambiente normal, mas energia caiu → suspeita de falha elétrica.
            evidencias.add("painel_limpo");
            evidencias.add("ambiente_normal");
            causa = new FalhaEletrica(0.70, evidencias);
        } else {
            evidencias.add("queda_subita");
            causa = new Sombra(0.60, evidencias);
        }

        return new Diagnostico(in.ativoId(), causa, LocalDateTime.now());
    }

    private void validar(EntradaDiagnostico in) {
        if (in.ativoId() == null || in.ativoId().isBlank()) {
            throw new DiagnosticoInvalidoException("ativoId é obrigatório.");
        }
        if (in.esperadoEnergia() <= 0) {
            throw new DiagnosticoInvalidoException("esperadoEnergia deve ser maior que zero.");
        }
        if (in.grauSujeira() < 0 || in.grauSujeira() > 1) {
            throw new DiagnosticoInvalidoException("grauSujeira deve estar entre 0 e 1.");
        }
    }

    private static String fmt(double v) {
        return String.format(Locale.US, "%.2f", v);
    }
}
