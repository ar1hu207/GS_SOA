package br.com.fiap.helios.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;

/** Telemetria enviada para um painel (dispara o loop de diagnóstico). */
@Schema(description = "Leitura de telemetria de um painel")
public record LeituraRequest(

        @Schema(example = "142.0", description = "Energia gerada agora (W)")
        @PositiveOrZero(message = "valorEnergia não pode ser negativo")
        double valorEnergia,

        @Schema(example = "0.47", description = "Grau de sujeira estimado pela visão (0..1)")
        @DecimalMin(value = "0.0", message = "grauSujeira deve ser >= 0")
        @DecimalMax(value = "1.0", message = "grauSujeira deve ser <= 1")
        double grauSujeira,

        @Schema(example = "true", description = "A queda foi gradual (típico de sujeira)")
        boolean quedaGradual,

        @Schema(example = "false", description = "Há ponto quente (típico de trinca/dano)")
        boolean pontoQuente
) {
}
