package br.com.fiap.helios.soap.service;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 * Entrada de diagnóstico inválida. Mapeada para um SOAP Fault do tipo Client,
 * para que o consumidor (SoapUI, REST) receba um erro claro em vez de stack trace.
 */
@SoapFault(faultCode = FaultCode.CLIENT)
public class DiagnosticoInvalidoException extends RuntimeException {

    public DiagnosticoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
