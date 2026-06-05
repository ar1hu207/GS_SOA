package br.com.fiap.helios.soap.domain.causa;

import br.com.fiap.helios.soap.domain.Acao;
import br.com.fiap.helios.soap.domain.Severidade;

import java.util.List;

/**
 * Sujeira (poeira) cobrindo o painel — a única causa que aciona a limpeza.
 * A vibração é <b>modulada</b> conforme o grau de sujeira.
 */
public final class Sujeira extends Causa {

    private final double grauSujeira;

    public Sujeira(double grauSujeira, double confianca, List<String> evidencias) {
        super(confianca, evidencias);
        this.grauSujeira = grauSujeira;
    }

    @Override
    public String nome() {
        return "SUJEIRA";
    }

    @Override
    public Severidade severidade() {
        return Severidade.ALTA;
    }

    @Override
    public Acao acaoRecomendada() {
        return Acao.VIBRAR;
    }

    /** Vibração proporcional à sujeira, limitada a [0.3, 1.0] (não é liga-desliga). */
    @Override
    public Double intensidadeVibracao() {
        return Math.clamp(grauSujeira, 0.3, 1.0);
    }

    public double grauSujeira() {
        return grauSujeira;
    }
}
