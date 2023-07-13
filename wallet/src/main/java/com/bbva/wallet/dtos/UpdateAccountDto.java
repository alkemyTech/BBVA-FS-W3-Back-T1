package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateAccountDto(@Positive@NotNull Double transactionLimit) {
}
