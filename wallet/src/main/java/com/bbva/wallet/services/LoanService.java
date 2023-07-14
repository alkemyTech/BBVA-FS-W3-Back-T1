package com.bbva.wallet.services;

import com.bbva.wallet.dtos.LoanDto;
import com.bbva.wallet.exceptions.ExceptionAmountNotAllowed;
import com.bbva.wallet.exceptions.ExceptionMonthNotExist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanService {
    @Value("${montlhy.interest.rate}")
    private Double monthlyInterestRate;
    public LoanDto simulateLoan(double amount, Integer month){
        if(amount <= 0.0){
            throw new ExceptionAmountNotAllowed();
        }
        if(month<=0){
            throw new ExceptionMonthNotExist();
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = currentDate.plusMonths(month);

        double monthlyInterest = amount*(monthlyInterestRate/100);
        double totalAmountDue = amount + (monthlyInterest * month);
        double monthlyPayment = (amount/month) + monthlyInterest;

        LoanDto loanDto = new LoanDto();
        loanDto.setMonthlyInterestRate(monthlyInterestRate);
        loanDto.setMonthlyPayment(monthlyPayment);
        loanDto.setTotalAmountDue(totalAmountDue);
        loanDto.setExpirationDate(expirationDate);

        return loanDto;
    }
}
