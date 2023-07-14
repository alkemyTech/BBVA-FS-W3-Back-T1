package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.LoanDto;
import com.bbva.wallet.dtos.LoanRequestBodyDto;
import com.bbva.wallet.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;
    @PostMapping("/simulate")
    public LoanDto simulateLoan(@RequestBody LoanRequestBodyDto loanRequestBodyDto){
        return loanService.simulateLoan(loanRequestBodyDto.getAmount(), loanRequestBodyDto.getMonths());
    }
}
