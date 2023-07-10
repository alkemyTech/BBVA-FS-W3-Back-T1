package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currencies;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateFixedTermDto(@NotNull Currencies currencies, @Positive @NotNull Double amount, @NotNull @Min(30) Integer cantDias) {
}
