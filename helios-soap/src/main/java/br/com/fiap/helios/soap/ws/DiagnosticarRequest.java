package br.com.fiap.helios.soap.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/** Entrada da operação {@code diagnosticar}: os sinais a serem cruzados. */
@XmlRootElement(name = "diagnosticarRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "ativoId", "valorEnergia", "esperadoEnergia", "grauSujeira",
        "quedaGradual", "pontoQuente", "solBaixo", "frio"
})
public class DiagnosticarRequest {

    private String ativoId;
    private double valorEnergia;
    private double esperadoEnergia;
    private double grauSujeira;
    private boolean quedaGradual;
    private boolean pontoQuente;
    private boolean solBaixo;
    private boolean frio;

    public String getAtivoId() { return ativoId; }
    public void setAtivoId(String ativoId) { this.ativoId = ativoId; }

    public double getValorEnergia() { return valorEnergia; }
    public void setValorEnergia(double valorEnergia) { this.valorEnergia = valorEnergia; }

    public double getEsperadoEnergia() { return esperadoEnergia; }
    public void setEsperadoEnergia(double esperadoEnergia) { this.esperadoEnergia = esperadoEnergia; }

    public double getGrauSujeira() { return grauSujeira; }
    public void setGrauSujeira(double grauSujeira) { this.grauSujeira = grauSujeira; }

    public boolean isQuedaGradual() { return quedaGradual; }
    public void setQuedaGradual(boolean quedaGradual) { this.quedaGradual = quedaGradual; }

    public boolean isPontoQuente() { return pontoQuente; }
    public void setPontoQuente(boolean pontoQuente) { this.pontoQuente = pontoQuente; }

    public boolean isSolBaixo() { return solBaixo; }
    public void setSolBaixo(boolean solBaixo) { this.solBaixo = solBaixo; }

    public boolean isFrio() { return frio; }
    public void setFrio(boolean frio) { this.frio = frio; }
}
