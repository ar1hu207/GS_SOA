package br.com.fiap.helios.rest.dto;

import br.com.fiap.helios.rest.domain.Alerta;

import java.time.LocalDateTime;

/** Representação de saída de um alerta. */
public record AlertaResponse(
        Long id,
        String ativoId,
        String severidade,
        String tipo,
        String mensagem,
        boolean resolvido,
        LocalDateTime criadoEm
) {

    public static AlertaResponse de(Alerta a) {
        return new AlertaResponse(
                a.getId(),
                a.getPainel().getCodigo(),
                a.getSeveridade(),
                a.getTipo(),
                a.getMensagem(),
                a.isResolvido(),
                a.getCriadoEm());
    }
}
