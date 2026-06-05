package br.com.fiap.helios.rest.soap;

import br.com.fiap.helios.rest.exception.RequisicaoInvalidaException;
import br.com.fiap.helios.rest.exception.ServicoIndisponivelException;
import br.com.fiap.helios.rest.soap.contract.DiagnosticarRequest;
import br.com.fiap.helios.rest.soap.contract.DiagnosticarResponse;
import br.com.fiap.helios.rest.soap.contract.DiagnosticoType;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

/**
 * Cliente do Web Service SOAP de diagnóstico.
 *
 * <p>É aqui que a API REST se integra ao serviço SOAP. O mapeamento de falhas
 * distingue corretamente:</p>
 * <ul>
 *   <li><b>SOAP Fault de cliente</b> (validação) → {@link RequisicaoInvalidaException} (HTTP 400);</li>
 *   <li><b>indisponibilidade/timeout</b> (I/O) → {@link ServicoIndisponivelException} (HTTP 503).</li>
 * </ul>
 */
@Component
public class DiagnosticoSoapClient {

    private final WebServiceTemplate template;

    public DiagnosticoSoapClient(WebServiceTemplate diagnosticoWebServiceTemplate) {
        this.template = diagnosticoWebServiceTemplate;
    }

    public DiagnosticoType diagnosticar(DiagnosticarRequest request) {
        try {
            DiagnosticarResponse response =
                    (DiagnosticarResponse) template.marshalSendAndReceive(request);
            if (response == null || response.getDiagnostico() == null) {
                throw new ServicoIndisponivelException(
                        "Resposta vazia do serviço SOAP de diagnóstico.");
            }
            return response.getDiagnostico();
        } catch (SoapFaultClientException e) {
            // Falha de validação reportada pelo SOAP: é erro do cliente, não indisponibilidade.
            throw new RequisicaoInvalidaException(
                    "Diagnóstico rejeitou os dados enviados: " + e.getFaultStringOrReason());
        } catch (WebServiceIOException e) {
            throw new ServicoIndisponivelException(
                    "Serviço SOAP de diagnóstico indisponível.");
        } catch (ServicoIndisponivelException | RequisicaoInvalidaException e) {
            throw e;
        } catch (Exception e) {
            throw new ServicoIndisponivelException(
                    "Falha ao consultar o serviço SOAP de diagnóstico.");
        }
    }
}
