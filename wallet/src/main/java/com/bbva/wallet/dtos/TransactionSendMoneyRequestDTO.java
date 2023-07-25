package com.bbva.wallet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionSendMoneyRequestDTO {
    @Schema(description = "ID de la cuenta destino", example = "18")
    private Long id;

    @Schema(description = "Monto a enviar", example = "2000")
    @Positive(message = "El monto a enviar debe ser mayor a cero.")
    private Double amount;

}
