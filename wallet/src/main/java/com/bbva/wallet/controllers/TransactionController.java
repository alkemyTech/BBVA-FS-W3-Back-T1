package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.*;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.hateoas.GenericModelAssembler;
import com.bbva.wallet.hateoas.TransactionModel;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.bbva.wallet.enums.Currencies;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    private  GenericModelAssembler<Transaction,TransactionModel> genericModelAssembler;

    @PatchMapping("/{id}")
    public ResponseEntity<Response>editTransaction(@PathVariable Long id, @RequestBody TransactionDescriptionDto transactionDescriptionDto){
        Response <Transaction> response = new Response<>();
        response.setData(transactionService.editTransaction(id, transactionDescriptionDto.getDescription()));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN') || #userId == authentication.principal.id")
    @GetMapping("/{userId}")
    public ResponseEntity<Response> getUserTransactions(@RequestParam(required = false) Optional<Integer> page, @PathVariable Long userId){
        Response response = new Response<>();
        CollectionModel<TransactionModel> collectionModel;
        if(page.isPresent()){
            Slice<Transaction> pagedEntity = transactionService.getTen(page.get(), userId);
            collectionModel = genericModelAssembler.toCollectionModel(pagedEntity);
            response.setData(collectionModel);
        }
        else{
            response.setData(transactionService.getUserTransactions(userId));
        }
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
        response.setData(transactionService.pay(paymentDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody @Valid DepositDTO depositDTO){

        return ResponseEntity.ok(transactionService.deposit(depositDTO));
    }

}
