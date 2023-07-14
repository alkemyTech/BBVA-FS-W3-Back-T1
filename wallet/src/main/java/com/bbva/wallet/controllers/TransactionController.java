package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.TransactionDescriptionDto;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.ExtractUser;
import com.bbva.wallet.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bbva.wallet.dtos.PaymentDto;
import com.bbva.wallet.dtos.ResponsePaymentDto;
import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.enums.Currencies;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.bbva.wallet.dtos.DepositDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PatchMapping("/{id}")
    public ResponseEntity<Response>editTransaction(@PathVariable Long id, @RequestBody TransactionDescriptionDto transactionDescriptionDto){
        Response <Transaction> response = new Response<>();
        response.setData(transactionService.editTransaction(id, transactionDescriptionDto.getDescription()));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN') || #userId == authentication.principal.id")
    @GetMapping("/{userId}")
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
