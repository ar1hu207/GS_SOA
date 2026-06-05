package br.com.fiap.helios.soap.domain;

/**
 * Conjunto de sinais que entram no diagnóstico (fusão de sensores).
 *
 * @param ativoId         identificação do painel
 * @param valorEnergia    energia gerada agora (W)
 * @param esperadoEnergia energia que deveria gerar (W)
 * @param grauSujeira     cobertura de poeira estimada pela visão (0..1)
 * @param quedaGradual    a queda veio devagar (sintoma típico de sujeira)
 * @param pontoQuente     há ponto quente (sintoma de trinca/dano)
 * @param solBaixo        irradiância baixa (ambiente)
 * @param frio            temperatura baixa (ambiente)
 */
public record EntradaDiagnostico(
        String ativoId,
        double valorEnergia,
        double esperadoEnergia,
        double grauSujeira,
        boolean quedaGradual,
        boolean pontoQuente,
        boolean solBaixo,
        boolean frio
) {

    /** Limite a partir do qual consideramos que houve queda relevante (5%). */
    public static final double LIMIAR_QUEDA = 0.05;

    /** Fração de perda em relação ao esperado (0..1). */
    public double queda() {
        if (esperadoEnergia <= 0) {
            return 0;
        }
        return (esperadoEnergia - valorEnergia) / esperadoEnergia;
    }

    public boolean houveQueda() {
        return queda() >= LIMIAR_QUEDA;
    }
}
