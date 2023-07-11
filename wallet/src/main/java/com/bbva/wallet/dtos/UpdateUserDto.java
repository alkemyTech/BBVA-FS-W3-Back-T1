package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserDto(@NotBlank String nombre,@NotBlank String apellido,@NotBlank String contraseña) {
}
