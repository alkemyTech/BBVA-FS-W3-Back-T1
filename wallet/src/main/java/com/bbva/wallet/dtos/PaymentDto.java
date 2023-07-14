package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currencies;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {
    @Schema(description = "ID de la cuenta que realiza el pago", example = "20")
    private Long id;

    @Schema(description = "Monto a pagar", example = "200")
    @Positive
    private Double amount;

    @Schema(description = "Moneda del pago", example = "ARS")
    private Currencies currency;

}
