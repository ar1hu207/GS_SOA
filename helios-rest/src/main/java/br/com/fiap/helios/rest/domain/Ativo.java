package br.com.fiap.helios.rest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Ativo gerenciado pelo HÉLIOS.
 *
 * <p><b>Abstração + herança:</b> reúne o que todo ativo tem (id, código, nome,
 * estado). Cada ativo concreto informa seu {@link #tipo()} por polimorfismo.</p>
 */
@MappedSuperclass
public abstract class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false)
    private boolean ativo = true;

    protected Ativo() {
    }

    /** Tipo do ativo (polimórfico). Ex.: "PAINEL_SOLAR". */
    public abstract String tipo();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
