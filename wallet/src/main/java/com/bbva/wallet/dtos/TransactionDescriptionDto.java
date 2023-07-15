package com.bbva.wallet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDescriptionDto {
    @Schema(description = "Nueva descripción para la transacción", example = "Pago de servicio")
    private String description;
}
