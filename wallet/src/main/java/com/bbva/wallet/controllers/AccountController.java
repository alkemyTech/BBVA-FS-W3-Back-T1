package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.CurrenciesDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> saveAccount(@RequestBody CurrenciesDto currenciesDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(currenciesDto));
    }

}
