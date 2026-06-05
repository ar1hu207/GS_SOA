package br.com.fiap.helios.rest.soap;

import br.com.fiap.helios.rest.soap.contract.DiagnosticarRequest;
import br.com.fiap.helios.rest.soap.contract.DiagnosticarResponse;
import br.com.fiap.helios.rest.soap.contract.DiagnosticoType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

/** Configuração do cliente SOAP que consome o serviço de diagnóstico (helios-soap). */
@Configuration
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller diagnosticoMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(
                DiagnosticarRequest.class,
                DiagnosticarResponse.class,
                DiagnosticoType.class);
        return marshaller;
    }

    @Bean
    public WebServiceTemplate diagnosticoWebServiceTemplate(Jaxb2Marshaller diagnosticoMarshaller,
                                                            @Value("${helios.soap.url}") String soapUrl) {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(diagnosticoMarshaller);
        template.setUnmarshaller(diagnosticoMarshaller);
        template.setDefaultUri(soapUrl);
        return template;
    }
}
