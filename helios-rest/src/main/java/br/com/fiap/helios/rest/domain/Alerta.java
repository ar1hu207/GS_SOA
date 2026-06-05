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

/** Alerta gerado a partir de um diagnóstico que exige atenção. */
@Entity
@Table(name = "alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "painel_id")
    private PainelSolar painel;

    @Column(nullable = false, length = 20)
    private String severidade;

    @Column(nullable = false, length = 40)
    private String tipo;

    @Column(nullable = false, length = 240)
    private String mensagem;

    @Column(nullable = false)
    private boolean resolvido;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Alerta() {
    }

    public Long getId() { return id; }

    public PainelSolar getPainel() { return painel; }
    public void setPainel(PainelSolar painel) { this.painel = painel; }

    public String getSeveridade() { return severidade; }
    public void setSeveridade(String severidade) { this.severidade = severidade; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public boolean isResolvido() { return resolvido; }
    public void setResolvido(boolean resolvido) { this.resolvido = resolvido; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
