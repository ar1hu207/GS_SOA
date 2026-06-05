package br.com.fiap.helios.soap.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

/** Tipo reutilizado nas respostas {@code diagnosticarResponse} e {@code consultarHistoricoResponse}. */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "diagnosticoType", propOrder = {
        "ativoId", "causa", "confianca", "severidade",
        "acaoRecomendada", "intensidadeVibracao", "evidencia", "timestamp"
})
public class DiagnosticoType {

    private String ativoId;
    private String causa;
    private double confianca;
    private String severidade;
    private String acaoRecomendada;
    private Double intensidadeVibracao;

    @XmlElement(name = "evidencia")
    private List<String> evidencia = new ArrayList<>();

    private String timestamp;

    public String getAtivoId() { return ativoId; }
    public void setAtivoId(String ativoId) { this.ativoId = ativoId; }

    public String getCausa() { return causa; }
    public void setCausa(String causa) { this.causa = causa; }

    public double getConfianca() { return confianca; }
    public void setConfianca(double confianca) { this.confianca = confianca; }

    public String getSeveridade() { return severidade; }
    public void setSeveridade(String severidade) { this.severidade = severidade; }

    public String getAcaoRecomendada() { return acaoRecomendada; }
    public void setAcaoRecomendada(String acaoRecomendada) { this.acaoRecomendada = acaoRecomendada; }

    public Double getIntensidadeVibracao() { return intensidadeVibracao; }
    public void setIntensidadeVibracao(Double intensidadeVibracao) { this.intensidadeVibracao = intensidadeVibracao; }

    public List<String> getEvidencia() { return evidencia; }
    public void setEvidencia(List<String> evidencia) { this.evidencia = evidencia; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
