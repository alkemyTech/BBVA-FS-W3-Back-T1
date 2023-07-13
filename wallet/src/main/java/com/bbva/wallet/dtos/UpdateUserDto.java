package com.bbva.wallet.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record UpdateUserDto(@NotBlank String nombre, @NotBlank String apellido, Optional<String> contraseña) {
}
