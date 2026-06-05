package br.com.fiap.helios.rest.soap.contract;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/** Espelho cliente de {@code diagnosticarResponse}. */
@XmlRootElement(name = "diagnosticarResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"diagnostico"})
public class DiagnosticarResponse {

    private DiagnosticoType diagnostico;

    public DiagnosticoType getDiagnostico() { return diagnostico; }
    public void setDiagnostico(DiagnosticoType diagnostico) { this.diagnostico = diagnostico; }
}
