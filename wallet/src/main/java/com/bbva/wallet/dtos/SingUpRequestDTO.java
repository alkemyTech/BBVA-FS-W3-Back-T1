package com.bbva.wallet.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SingUpRequestDTO(@NotBlank String firstName, @NotBlank String lastName
        , @NotBlank@Email String email, @NotBlank String password) {
}

