package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record UserUpdateRequestDTO(@NotBlank String nombre, @NotBlank String apellido, Optional<String> contraseña) {
}
