package br.com.fiap.helios.soap.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import br.com.fiap.helios.soap.ws.ConsultarHistoricoRequest;
import br.com.fiap.helios.soap.ws.ConsultarHistoricoResponse;
import br.com.fiap.helios.soap.ws.DiagnosticarRequest;
import br.com.fiap.helios.soap.ws.DiagnosticarResponse;
import br.com.fiap.helios.soap.ws.DiagnosticoType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Configuração do Web Service SOAP (contract-first).
 *
 * <p>O servlet do Spring-WS responde em {@code /ws/*}. O WSDL é gerado a partir
 * do {@code diagnostico.xsd} e publicado em {@code /ws/diagnostico.wsdl}.</p>
 */
@EnableWs
@Configuration
public class WebServiceConfig {

    public static final String NAMESPACE = "http://helios.fiap.com.br/soap/diagnostico";

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    /** Bean nomeado "diagnostico" → WSDL publicado em /ws/diagnostico.wsdl. */
    @Bean(name = "diagnostico")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema diagnosticoSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("DiagnosticoPort");
        definition.setLocationUri("/ws");
        definition.setTargetNamespace(NAMESPACE);
        definition.setSchema(diagnosticoSchema);
        return definition;
    }

    @Bean
    public XsdSchema diagnosticoSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/diagnostico.xsd"));
    }

    /** Marshaller único (request + response) baseado nas classes JAXB do contrato. */
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(
                DiagnosticarRequest.class,
                DiagnosticarResponse.class,
                ConsultarHistoricoRequest.class,
                ConsultarHistoricoResponse.class,
                DiagnosticoType.class);
        return marshaller;
    }
}
