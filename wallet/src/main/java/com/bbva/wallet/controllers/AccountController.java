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
import com.bbva.wallet.dtos.JwtAuthenticationResponse;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Account> saveAccount(@RequestBody CurrenciesDto currenciesDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(currenciesDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response> getUserAccounts(@PathVariable Long userId ){
        Response<List<Account>> response = new Response<>();
        response.setData(userService.getUserAccounts(userId));
        return ResponseEntity.ok(response);
    }
}

