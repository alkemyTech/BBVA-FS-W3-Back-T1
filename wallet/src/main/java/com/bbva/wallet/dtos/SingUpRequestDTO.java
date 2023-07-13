package com.bbva.wallet.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SingUpRequestDTO(
        @Schema(description = "Nombre de usuario", example = "NombrePrueba")
        @NotBlank
        String firstName,
        @Schema(description = "Apellido de usuario", example = "ApellidoPrueba")
        @NotBlank
        String lastName,
        @Schema(description = "Email de usuario", example = "UsuarioDePrueba@example.com")
        @NotBlank
        @Email
        String email,
        @Schema(description = "Contraseña del usuario", example = "123")
        @NotBlank
        String password) {
}

