package br.com.fiap.helios.soap.domain.causa;

import br.com.fiap.helios.soap.domain.Acao;
import br.com.fiap.helios.soap.domain.Severidade;

import java.util.List;

/** Não houve queda significativa de energia — tudo normal. */
public final class SemFalha extends Causa {

    public SemFalha(double confianca, List<String> evidencias) {
        super(confianca, evidencias);
    }

    @Override
    public String nome() {
        return "SEM_FALHA";
    }

    @Override
    public Severidade severidade() {
        return Severidade.NENHUMA;
    }

    @Override
    public Acao acaoRecomendada() {
        return Acao.NENHUMA;
    }
}
