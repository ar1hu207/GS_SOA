package br.com.fiap.helios.rest.dto;

import java.util.List;

/**
 * Resultado completo do loop para uma leitura: o que foi medido, o ambiente
 * consultado, o diagnóstico do serviço SOAP e a ação tomada (comando/alerta).
 */
public record TelemetriaResponse(
        Long painelId,
        String ativoId,
        Long leituraId,
        AmbienteResumo ambiente,
        DiagnosticoResumo diagnostico,
        ComandoVibracaoResumo comando,
        AlertaResumo alerta
) {

    public record AmbienteResumo(boolean solBaixo, boolean frio, String fonte) {
    }

    public record DiagnosticoResumo(
            String causa,
            double confianca,
            String severidade,
            String acaoRecomendada,
            Double intensidadeVibracao,
            List<String> evidencias,
            String timestamp
    ) {
    }

    public record ComandoVibracaoResumo(
            String atuadorId,
            String acao,
            double intensidade,
            int duracaoSeg
    ) {
    }

    public record AlertaResumo(
            Long id,
            String severidade,
            String tipo,
            String mensagem,
            boolean resolvido
    ) {
    }
}
