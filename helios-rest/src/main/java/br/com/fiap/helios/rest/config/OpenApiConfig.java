package br.com.fiap.helios.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Metadados da documentação OpenAPI/Swagger da API REST. */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI heliosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("HÉLIOS — API REST")
                .version("1.0.0")
                .description("""
                        API REST do HÉLIOS (Global Solution 2026.1 · FIAP · tema Space Connect).
                        Gerencia painéis solares e orquestra o loop perceber → diagnosticar → agir,
                        integrando-se ao Web Service SOAP de diagnóstico e a uma API externa de ambiente."""));
    }
}
