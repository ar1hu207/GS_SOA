package br.com.fiap.helios.rest.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Diagnóstico persistido (resultado vindo do Web Service SOAP). */
@Entity
@Table(name = "diagnostico")
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "painel_id")
    private PainelSolar painel;

    @Column(nullable = false, length = 30)
    private String causa;

    @Column(nullable = false)
    private double confianca;

    @Column(nullable = false, length = 20)
    private String severidade;

    @Column(name = "acao_recomendada", nullable = false, length = 30)
    private String acaoRecomendada;

    /** Nulo quando a causa não aciona vibração (só SUJEIRA preenche). */
    @Column(name = "intensidade_vibracao")
    private Double intensidadeVibracao;

    @ElementCollection
    @CollectionTable(name = "diagnostico_evidencia", joinColumns = @JoinColumn(name = "diagnostico_id"))
    @Column(name = "evidencia")
    private List<String> evidencias = new ArrayList<>();

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Diagnostico() {
    }

    public Long getId() { return id; }

    public PainelSolar getPainel() { return painel; }
    public void setPainel(PainelSolar painel) { this.painel = painel; }

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

    public List<String> getEvidencias() { return evidencias; }
    public void setEvidencias(List<String> evidencias) { this.evidencias = evidencias; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
