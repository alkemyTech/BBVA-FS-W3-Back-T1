package com.bbva.wallet.utils;

import com.bbva.wallet.enums.ErrorCodes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> {
    @Schema(description = "Nombre del error", example = "CUSTOM_ERROR")
    private List<ErrorCodes> errors;
    @Schema(description = "Descripción del error", example = "Descripción del error")
    private String message;
    private T data;

    public void addError(ErrorCodes error) {
        if (Objects.isNull(this.errors)) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
}
