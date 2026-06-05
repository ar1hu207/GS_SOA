package br.com.fiap.helios.rest.dto;

import br.com.fiap.helios.rest.domain.PainelSolar;

/** Representação de saída (JSON) de um painel solar. */
public record PainelSolarResponse(
        Long id,
        String codigo,
        String nome,
        String tipo,
        double potenciaNominalW,
        Double latitude,
        Double longitude,
        boolean ativo
) {

    public static PainelSolarResponse de(PainelSolar p) {
        return new PainelSolarResponse(
                p.getId(),
                p.getCodigo(),
                p.getNome(),
                p.tipo(),
                p.getPotenciaNominalW(),
                p.getLatitude(),
                p.getLongitude(),
                p.isAtivo());
    }
}
