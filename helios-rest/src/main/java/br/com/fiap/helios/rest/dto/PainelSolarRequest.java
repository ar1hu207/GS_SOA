package br.com.fiap.helios.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Dados de entrada para criar/atualizar um painel solar. */
@Schema(description = "Dados para cadastro ou atualização de um painel solar")
public record PainelSolarRequest(

        @Schema(example = "PAINEL-A")
        @NotBlank(message = "codigo é obrigatório")
        @Size(max = 60, message = "codigo deve ter no máximo 60 caracteres")
        String codigo,

        @Schema(example = "Painel A - Base Sul")
        @NotBlank(message = "nome é obrigatório")
        @Size(max = 120, message = "nome deve ter no máximo 120 caracteres")
        String nome,

        @Schema(example = "210.0", description = "Potência nominal em watts")
        @Positive(message = "potenciaNominalW deve ser maior que zero")
        double potenciaNominalW,

        @Schema(example = "-23.5")
        Double latitude,

        @Schema(example = "-46.6")
        Double longitude,

        @Schema(example = "true", description = "Se ausente, assume true")
        Boolean ativo
) {
}
