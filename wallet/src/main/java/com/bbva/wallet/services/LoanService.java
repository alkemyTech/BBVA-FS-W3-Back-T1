package com.bbva.wallet.services;

import com.bbva.wallet.dtos.LoanDto;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    private final Double monthlyInterestRate = 60.0;
    public LoanDto simulateLoan(Double amount, Integer month){

        Double monthlyInstallment = amount*(monthlyInterestRate/100);

        LoanDto loanDto = new LoanDto();
        loanDto.setMonthlyInterestRate(monthlyInterestRate);
        loanDto.setMonthlyInstallment(monthlyInstallment);
        loanDto.setTotalAmountDue(amount+(monthlyInstallment*month));
        return loanDto;
    }
}
