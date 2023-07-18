package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currencies;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionPaymentRequestDTO {

    private Long id;

    @Positive(message = "El monto a pagar debe ser mayor a cero.")
    private Double amount;

    private Currencies currency;

}
