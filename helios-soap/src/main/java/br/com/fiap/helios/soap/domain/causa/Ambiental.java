package br.com.fiap.helios.soap.domain.causa;

import br.com.fiap.helios.soap.domain.Acao;
import br.com.fiap.helios.soap.domain.Severidade;

import java.util.List;

/** Queda explicada pelo ambiente (sol baixo / frio) — comportamento normal, sem ação. */
public final class Ambiental extends Causa {

    public Ambiental(double confianca, List<String> evidencias) {
        super(confianca, evidencias);
    }

    @Override
    public String nome() {
        return "AMBIENTAL";
    }

    @Override
    public Severidade severidade() {
        return Severidade.BAIXA;
    }

    @Override
    public Acao acaoRecomendada() {
        return Acao.NENHUMA;
    }
}
