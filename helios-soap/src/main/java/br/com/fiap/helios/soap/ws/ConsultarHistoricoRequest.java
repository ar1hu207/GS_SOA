package br.com.fiap.helios.soap.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/** Entrada da operação {@code consultarHistorico}: identifica o painel. */
@XmlRootElement(name = "consultarHistoricoRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"ativoId"})
public class ConsultarHistoricoRequest {

    private String ativoId;

    public String getAtivoId() { return ativoId; }
    public void setAtivoId(String ativoId) { this.ativoId = ativoId; }
}
