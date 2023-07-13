package com.bbva.wallet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateAccountDto(
        @Schema(description = "Asignar nuevo límite de transaccion (Número positivo)", example = "100000")
        @Positive
        @NotNull
        Double transactionLimit
) {
}
