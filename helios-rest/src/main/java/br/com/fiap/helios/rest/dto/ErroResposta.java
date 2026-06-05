package br.com.fiap.helios.rest.dto;

import java.time.LocalDateTime;
import java.util.Map;

/** Corpo padrão de erro retornado pela API (JSON). */
public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos
) {

    public static ErroResposta de(int status, String erro, String mensagem, String caminho) {
        return new ErroResposta(LocalDateTime.now(), status, erro, mensagem, caminho, null);
    }

    public static ErroResposta deCampos(int status, String erro, String mensagem,
                                        String caminho, Map<String, String> campos) {
        return new ErroResposta(LocalDateTime.now(), status, erro, mensagem, caminho, campos);
    }
}
