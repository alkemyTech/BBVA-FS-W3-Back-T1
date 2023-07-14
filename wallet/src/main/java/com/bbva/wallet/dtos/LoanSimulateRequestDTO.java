package com.bbva.wallet.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoanSimulateRequestDTO {
    private Integer months;
    private Double amount;
}
