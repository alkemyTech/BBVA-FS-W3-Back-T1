package com.bbva.wallet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SingInRequestDTO(
        @Schema(description = "Email de usuario", example = "admin0@example.com")
        @NotBlank
        @Email
        String email,
        @Schema(description = "Email de usuario", example = "123")
        @NotBlank
        String password) {
}
