package com.bbva.wallet.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoanDto {
    private Double monthlyInstallment;
    private Double totalAmountDue;
    private Double MonthlyInterestRate;

}
