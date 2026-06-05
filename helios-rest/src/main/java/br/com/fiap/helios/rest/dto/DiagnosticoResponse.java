package br.com.fiap.helios.rest.dto;

import br.com.fiap.helios.rest.domain.Diagnostico;

import java.time.LocalDateTime;
import java.util.List;

/** Representação de saída de um diagnóstico persistido. */
public record DiagnosticoResponse(
        Long id,
        String ativoId,
        String causa,
        double confianca,
        String severidade,
        String acaoRecomendada,
        Double intensidadeVibracao,
        List<String> evidencias,
        LocalDateTime criadoEm
) {

    public static DiagnosticoResponse de(Diagnostico d) {
        return new DiagnosticoResponse(
                d.getId(),
                d.getPainel().getCodigo(),
                d.getCausa(),
                d.getConfianca(),
                d.getSeveridade(),
                d.getAcaoRecomendada(),
                d.getIntensidadeVibracao(),
                List.copyOf(d.getEvidencias()),
                d.getCriadoEm());
    }
}
