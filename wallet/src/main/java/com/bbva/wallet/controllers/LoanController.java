package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.LoanDto;
import com.bbva.wallet.dtos.LoanRequestBodyDto;
import com.bbva.wallet.services.LoanService;
import com.bbva.wallet.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Response>simulateLoan(@RequestBody LoanRequestBodyDto loanRequestBodyDto){
        Response<LoanDto> response = new Response<>();
        response.setData(loanService.simulateLoan(loanRequestBodyDto.getAmount(), loanRequestBodyDto.getMonths()));
        return ResponseEntity.ok(response);
    }
}
