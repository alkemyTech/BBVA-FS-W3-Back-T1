package com.bbva.wallet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    @Schema(description = "ID de la cuenta destino", example = "31")
    private Long id;

    @Schema(description = "Monto a enviar", example = "2000")
    @Positive
    private Double amount;

}
