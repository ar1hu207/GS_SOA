package br.com.fiap.helios.rest.exception;

/** Lançada quando um serviço dependente (ex.: SOAP) falha (resulta em HTTP 503). */
public class ServicoIndisponivelException extends RuntimeException {

    public ServicoIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
