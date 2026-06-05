/**
 * Cópia do contrato SOAP (lado cliente) consumido pelo helios-rest.
 *
 * <p>Mesmo namespace do serviço (helios-soap). Mantido aqui para o cliente
 * (un)marshalling sem acoplar os dois módulos por dependência binária.</p>
 */
@XmlSchema(
        namespace = "http://helios.fiap.com.br/soap/diagnostico",
        elementFormDefault = XmlNsForm.QUALIFIED)
package br.com.fiap.helios.rest.soap.contract;

import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
