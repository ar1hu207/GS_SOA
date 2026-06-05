package br.com.fiap.helios.soap.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

/** Saída da operação {@code consultarHistorico}: diagnósticos já realizados para o painel. */
@XmlRootElement(name = "consultarHistoricoResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"diagnostico"})
public class ConsultarHistoricoResponse {

    @XmlElement(name = "diagnostico")
    private List<DiagnosticoType> diagnostico = new ArrayList<>();

    public List<DiagnosticoType> getDiagnostico() { return diagnostico; }
    public void setDiagnostico(List<DiagnosticoType> diagnostico) { this.diagnostico = diagnostico; }
}
