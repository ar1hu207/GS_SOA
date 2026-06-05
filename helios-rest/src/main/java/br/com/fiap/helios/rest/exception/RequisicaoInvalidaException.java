package br.com.fiap.helios.rest.exception;

/** Lançada quando a requisição é inválida do ponto de vista de negócio (HTTP 400). */
public class RequisicaoInvalidaException extends RuntimeException {

    public RequisicaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
