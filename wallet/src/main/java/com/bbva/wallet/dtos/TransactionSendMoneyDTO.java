package com.bbva.wallet.dtos;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionSendMoneyDTO {
    private Long id;
    @Positive
    private Double amount;

}
