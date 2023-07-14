package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currencies;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountCurrencyRequestDTO {
    private Currencies currency;
}
