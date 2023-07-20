package com.bbva.wallet.dtos;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionSendMoneyRequestDTO {
    private Long id;
    @Positive(message = "El monto a enviar debe ser mayor a cero.")
    private Double amount;

}