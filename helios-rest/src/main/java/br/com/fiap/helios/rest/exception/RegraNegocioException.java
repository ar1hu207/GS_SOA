package br.com.fiap.helios.rest.exception;

/** Lançada quando uma regra de negócio é violada (resulta em HTTP 409). */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
