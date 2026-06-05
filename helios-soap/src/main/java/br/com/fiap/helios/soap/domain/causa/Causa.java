package br.com.fiap.helios.soap.domain.causa;

import br.com.fiap.helios.soap.domain.Acao;
import br.com.fiap.helios.soap.domain.Severidade;

import java.util.List;

/**
 * Causa diagnosticada para a queda de geração de um painel.
 *
 * <p><b>Abstração + herança:</b> define o contrato comum; cada causa concreta
 * (sujeira, sombra, dano, etc.) decide, por <b>polimorfismo</b>, sua severidade,
 * a ação recomendada e se há (e quanto de) vibração.</p>
 */
public abstract class Causa {

    private final double confianca;
    private final List<String> evidencias;

    protected Causa(double confianca, List<String> evidencias) {
        this.confianca = confianca;
        this.evidencias = List.copyOf(evidencias);
    }

    /** Nome canônico da causa (ex.: "SUJEIRA"), usado no contrato de dados. */
    public abstract String nome();

    /** Gravidade desta causa. */
    public abstract Severidade severidade();

    /** O que o HÉLIOS deve fazer diante desta causa. */
    public abstract Acao acaoRecomendada();

    /**
     * Intensidade de vibração (0..1) quando a ação é limpar.
     * Por padrão {@code null} — só {@link Sujeira} modula vibração.
     */
    public Double intensidadeVibracao() {
        return null;
    }

    public double confianca() {
        return confianca;
    }

    public List<String> evidencias() {
        return evidencias;
    }
}
