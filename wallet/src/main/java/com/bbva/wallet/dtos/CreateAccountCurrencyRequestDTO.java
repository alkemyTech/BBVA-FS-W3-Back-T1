package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currencies;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountCurrencyRequestDTO {
    @Schema(description = "Tipo de moneda", example = "USD")
    private Currencies currency;
}
