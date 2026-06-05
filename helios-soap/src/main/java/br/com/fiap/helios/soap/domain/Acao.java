package br.com.fiap.helios.soap.domain;

/** Ação recomendada pelo diagnóstico (o que o HÉLIOS deve fazer). */
public enum Acao {
    NENHUMA,
    AGUARDAR,
    VIBRAR,
    ALERTA_MANUTENCAO,
    ALERTAR_HUMANO
}
