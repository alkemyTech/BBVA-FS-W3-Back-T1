package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.*;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.exceptions.ExceptionUserNotAuthenticated;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.ExtractUser;
import com.bbva.wallet.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
    public class TransactionController {
        @Autowired
        private TransactionService transactionService;

        @GetMapping("/{id}")
        public ResponseEntity<Response> getTransaction(@PathVariable Long id){
            User authenticatedUser = ExtractUser.extract();
            Transaction transaction = transactionService.getTransaction(id);
            Response<Transaction> response = new Response<>();
            if(transaction.getAccount().getUserId().getId().equals(authenticatedUser.getId()) ||
                    authenticatedUser.getRoleId().getName().equals(EnumRole.ADMIN)){
                response.setData(transaction);
                return ResponseEntity.ok(response);
            }else throw new ExceptionUserNotAuthenticated();
        }

    @PatchMapping("/{id}")
    public ResponseEntity<Response>editTransaction(@PathVariable Long id, @RequestBody TransactionDescriptionDto transactionDescriptionDto){
        Response <Transaction> response = new Response<>();
        response.setData(transactionService.editTransaction(id, transactionDescriptionDto.getDescription()));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN') || #userId == authentication.principal.id")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Response> getUserTransactions(@PathVariable Long userId){
        Response <List<Transaction>> response = new Response<>();
        response.setData(transactionService.getUserTransactions(userId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendArs")
    public ResponseEntity<Response> sendPesos(@Valid @RequestBody TransactionDto transactionDto) {
        Response<List<Transaction>> response = new Response<>();
        response.setData(transactionService.sendMoney(transactionDto,Currencies.ARS));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<Response> sendDollars(@Valid @RequestBody TransactionDto transactionDto) {
        Response<List<Transaction>> response = new Response<>();
        response.setData(transactionService.sendMoney(transactionDto,Currencies.USD));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/payment")
    public ResponseEntity<Response> pay(@Valid @RequestBody PaymentDto paymentDto){
        Response<ResponsePaymentDto> response = new Response<>();
        User authenticatedUser = ExtractUser.extract();
        response.setData(transactionService.pay(paymentDto,authenticatedUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody @Valid DepositDTO depositDTO){

        return ResponseEntity.ok(transactionService.deposit(depositDTO));
    }

}
