package br.com.fiap.helios.rest.external;

/**
 * Condições de ambiente que influenciam a geração de energia.
 *
 * @param solBaixo irradiância baixa (sol fraco / noite / muita nuvem)
 * @param frio     temperatura baixa
 * @param fonte    origem do dado ("openweathermap", "nasa" ou "simulado")
 */
public record Ambiente(boolean solBaixo, boolean frio, String fonte) {

    public static Ambiente simulado() {
        return new Ambiente(false, false, "simulado");
    }
}
