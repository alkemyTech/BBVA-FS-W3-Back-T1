package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.BalanceDto;
import com.bbva.wallet.dtos.CurrenciesDto;
import com.bbva.wallet.dtos.UpdateAccountDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.Response;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Response> saveAccount(@RequestBody CurrenciesDto currenciesDto){
        Response<Account> response = new Response<>();
        response.setData(accountService.createAccount(currenciesDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<Response> getUserAccounts(@PathVariable Long userId ){
        Response<List<Account>> response = new Response<>();
        response.setData(userService.getUserAccounts(userId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance")
    public ResponseEntity<Response> getUserBalance(){
        Response<BalanceDto> response = new Response<>();
        response.setData(accountService.getBalance());
        return ResponseEntity.ok(response);
    }


    @RequestMapping(value = "/{id}",method = RequestMethod.PATCH)
    public ResponseEntity<Response> updateAccount(@PathVariable Long id, Authentication authentication, @RequestBody UpdateAccountDto dto){
        User user= (User) authentication.getPrincipal();
        Response<Account> response = new Response<>();
        response.setData(accountService.updateAccount(id,user,dto.transactionLimit()));
        return ResponseEntity.ok(response);
    }
}

