package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currencies;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    private Long id;

    @Positive
    private Double amount;

    private Currencies currency;

}
