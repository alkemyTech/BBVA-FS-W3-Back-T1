package com.bbva.wallet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FixedTermCreateRequestDto(
        @Schema(description = "Monto de dinero a colocar en el plazo fijo", example = "1000.0")
        @Positive
        @NotNull
        Double amount,

        @Schema(description = "Cantidad mínima de días para el plazo fijo (mínimo 30)", example = "30")
        @NotNull
        @Min(value = 30, message = "El plazo fijo debe ser de al menos 30 días")
        Integer cantDias
) { }