package com.bbva.wallet.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AccountCbuRequestDTO (@NotBlank @Min(22) @Max(22) String cbu) {
}
