package com.bbva.wallet.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SingInRequestDTO(@NotBlank @Email String email, @NotBlank String password) {
}
