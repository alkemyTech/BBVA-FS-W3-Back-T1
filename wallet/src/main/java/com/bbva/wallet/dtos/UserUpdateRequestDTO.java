package com.bbva.wallet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record UserUpdateRequestDTO(
        @Schema(description = "Nuevo nombre", example = "Carlos")
        @NotBlank String nombre,
        @Schema(description = "Nuevo apellido", example = "Rodriguez")
        @NotBlank String apellido,
        @Schema(description = "Nueva contraseña", example = "1234")
        Optional<String> contraseña
) {
}
