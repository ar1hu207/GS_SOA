package br.com.fiap.helios.rest.exception;

/** Lançada quando um recurso solicitado não existe (resulta em HTTP 404). */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
