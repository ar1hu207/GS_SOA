package br.com.fiap.helios.soap.domain;

import br.com.fiap.helios.soap.domain.causa.Causa;

import java.time.LocalDateTime;

/** Resultado do diagnóstico: a causa identificada para um painel, com data/hora. */
public class Diagnostico {

    private final String ativoId;
    private final Causa causa;
    private final LocalDateTime timestamp;

    public Diagnostico(String ativoId, Causa causa, LocalDateTime timestamp) {
        this.ativoId = ativoId;
        this.causa = causa;
        this.timestamp = timestamp;
    }

    public String getAtivoId() { return ativoId; }
    public Causa getCausa() { return causa; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
