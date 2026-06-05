package br.com.fiap.helios.soap.domain.causa;

import br.com.fiap.helios.soap.domain.Acao;
import br.com.fiap.helios.soap.domain.Severidade;

import java.util.List;

/** Painel limpo, ambiente normal, mas energia caiu → suspeita de falha elétrica. */
public final class FalhaEletrica extends Causa {

    public FalhaEletrica(double confianca, List<String> evidencias) {
        super(confianca, evidencias);
    }

    @Override
    public String nome() {
        return "FALHA_ELETRICA";
    }

    @Override
    public Severidade severidade() {
        return Severidade.ALTA;
    }

    @Override
    public Acao acaoRecomendada() {
        return Acao.ALERTAR_HUMANO;
    }
}
