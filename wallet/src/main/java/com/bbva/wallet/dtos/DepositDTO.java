package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currencies;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DepositDTO(

        @Schema(description = "Tipo de moneda del depósito", example = "ARS")
        @NotNull Currencies currency,

        @Schema(description = "Monto a depositar", example = "30000")
        @NotNull
        @Positive(message = "El monto a depositar debe ser mayor a cero.")
        Double amount,

        @Schema(description = "Descripción opcional de la transacción", example = "Cobro por ventas de julio")
        String description) {

}
