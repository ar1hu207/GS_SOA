package br.com.fiap.helios.rest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/** Leitura de telemetria de um painel (o gatilho do loop). */
@Entity
@Table(name = "leitura")
public class Leitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "painel_id")
    private PainelSolar painel;

    @Column(name = "valor_energia", nullable = false)
    private double valorEnergia;

    @Column(name = "esperado_energia", nullable = false)
    private double esperadoEnergia;

    @Column(name = "grau_sujeira", nullable = false)
    private double grauSujeira;

    @Column(name = "queda_gradual", nullable = false)
    private boolean quedaGradual;

    @Column(name = "ponto_quente", nullable = false)
    private boolean pontoQuente;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Leitura() {
    }

    public Long getId() { return id; }

    public PainelSolar getPainel() { return painel; }
    public void setPainel(PainelSolar painel) { this.painel = painel; }

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

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
