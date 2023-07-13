package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currencies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DepositDTO(
        @NotNull Currencies currency,
        @NotNull
        @Positive(message = "El monto a depositar debe ser mayor a cero.") Double amount,
        String description) {

}
