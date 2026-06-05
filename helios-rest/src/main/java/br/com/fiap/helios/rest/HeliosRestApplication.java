package br.com.fiap.helios.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API REST do HÉLIOS.
 *
 * <p>Expõe o CRUD de {@code PainelSolar}, persiste em H2 e orquestra o loop
 * perceber → diagnosticar → agir: recebe telemetria, consulta a API externa de
 * ambiente, chama o Web Service SOAP de diagnóstico e gera alertas/comandos.</p>
 */
@SpringBootApplication
public class HeliosRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeliosRestApplication.class, args);
    }
}
