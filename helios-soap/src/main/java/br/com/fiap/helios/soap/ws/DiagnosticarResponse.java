package br.com.fiap.helios.soap.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/** Saída da operação {@code diagnosticar}: a causa classificada. */
@XmlRootElement(name = "diagnosticarResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"diagnostico"})
public class DiagnosticarResponse {

    private DiagnosticoType diagnostico;

    public DiagnosticoType getDiagnostico() { return diagnostico; }
    public void setDiagnostico(DiagnosticoType diagnostico) { this.diagnostico = diagnostico; }
}
