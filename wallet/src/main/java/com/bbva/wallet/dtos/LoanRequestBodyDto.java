package com.bbva.wallet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoanRequestBodyDto {
    @Schema(description = "Plazo en meses (Mayor a 0)", example = "12")
    private Integer months;
    @Schema(description = "Cantidad de dinero (Mayor a 0)", example = "10000")
    private Double amount;
}
