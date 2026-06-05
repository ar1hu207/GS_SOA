/**
 * Tipos JAXB do contrato SOAP de diagnóstico.
 *
 * <p>O namespace e a qualificação dos elementos são definidos aqui para casar
 * exatamente com o {@code diagnostico.xsd} (elementFormDefault="qualified").</p>
 */
@XmlSchema(
        namespace = "http://helios.fiap.com.br/soap/diagnostico",
        elementFormDefault = XmlNsForm.QUALIFIED)
package br.com.fiap.helios.soap.ws;

import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
