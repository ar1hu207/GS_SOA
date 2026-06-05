package br.com.fiap.helios.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada do Web Service SOAP do HÉLIOS.
 *
 * <p>Responsável pelo motor de diagnóstico: recebe energia, grau de sujeira e
 * ambiente e classifica a causa da queda de geração (SUJEIRA, SOMBRA, DANO,
 * AMBIENTAL, FALHA). O contrato é exposto via WSDL (contract-first).</p>
 */
@SpringBootApplication
public class HeliosSoapApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeliosSoapApplication.class, args);
    }
}
