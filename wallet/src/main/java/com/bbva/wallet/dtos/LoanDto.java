package com.bbva.wallet.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LoanDto {
    private double monthlyPayment;
    private double totalAmountDue;
    private double monthlyInterestRate;
    private LocalDate expirationDate;

}
