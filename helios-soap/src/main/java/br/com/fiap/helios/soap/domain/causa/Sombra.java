package br.com.fiap.helios.soap.domain.causa;

import br.com.fiap.helios.soap.domain.Acao;
import br.com.fiap.helios.soap.domain.Severidade;

import java.util.List;

/** Sombra parcial/temporária — não age, espera passar. */
public final class Sombra extends Causa {

    public Sombra(double confianca, List<String> evidencias) {
        super(confianca, evidencias);
    }

    @Override
    public String nome() {
        return "SOMBRA";
    }

    @Override
    public Severidade severidade() {
        return Severidade.BAIXA;
    }

    @Override
    public Acao acaoRecomendada() {
        return Acao.AGUARDAR;
    }
}
