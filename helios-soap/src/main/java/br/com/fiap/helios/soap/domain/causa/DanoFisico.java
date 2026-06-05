package br.com.fiap.helios.soap.domain.causa;

import br.com.fiap.helios.soap.domain.Acao;
import br.com.fiap.helios.soap.domain.Severidade;

import java.util.List;

/**
 * Dano físico (trinca / ponto quente). <b>Não</b> vibra — vibrar poderia
 * piorar um painel já trincado. Abre alerta de manutenção.
 */
public final class DanoFisico extends Causa {

    public DanoFisico(double confianca, List<String> evidencias) {
        super(confianca, evidencias);
    }

    @Override
    public String nome() {
        return "DANO_FISICO";
    }

    @Override
    public Severidade severidade() {
        return Severidade.ALTA;
    }

    @Override
    public Acao acaoRecomendada() {
        return Acao.ALERTA_MANUTENCAO;
    }
}
