package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.*;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.hateoas.GenericModelAssembler;
import com.bbva.wallet.hateoas.TransactionModel;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.exceptions.ExceptionUserNotAuthenticated;

import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.ExtractUser;
import com.bbva.wallet.utils.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Slice;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/transactions")

public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    private  GenericModelAssembler<Transaction,TransactionModel> genericModelAssembler;

    public TransactionController() {
        this.genericModelAssembler = new GenericModelAssembler<>(TransactionController.class, TransactionModel.class);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Response>editTransaction(@PathVariable Long id, @RequestBody TransactionPatchDescriptionRequestDTO transactionDescriptionDto){
        Response <Transaction> response = new Response<>();
        response.setData(transactionService.editTransaction(id, transactionDescriptionDto.getDescription()));
        return ResponseEntity.ok(response);
    }

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

    @PreAuthorize("hasAuthority('ADMIN') || #userId == authentication.principal.id")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Response> getUserTransactions(@RequestParam(required = false) Optional<Integer> page, @PathVariable Long userId){
        Response response = new Response<>();
        CollectionModel<TransactionModel> collectionModel;
        Slice<Transaction> pagedEntity;
        if(page.isPresent()){
            pagedEntity= transactionService.getTen(page.get(), userId);
        }
        else{
            pagedEntity= transactionService.getTen(0, userId);
        }

        collectionModel = genericModelAssembler.toCollectionModel(pagedEntity);
        response.setData(collectionModel);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendArs")
    public ResponseEntity<Response> sendPesos(@Valid @RequestBody TransactionSendMoneyRequestDTO transactionDto) {
        Response<List<Transaction>> response = new Response<>();
        User authenticatedUser = ExtractUser.extract();
        response.setData(transactionService.sendMoney(transactionDto,Currencies.ARS,authenticatedUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<Response> sendDollars(@Valid @RequestBody TransactionSendMoneyRequestDTO transactionDto) {
        Response<List<Transaction>> response = new Response<>();
        User authenticatedUser = ExtractUser.extract();
        response.setData(transactionService.sendMoney(transactionDto,Currencies.USD,authenticatedUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/payment")
    public ResponseEntity<Response> pay(@Valid @RequestBody TransactionPaymentRequestDTO paymentDto){
        Response<TransactionPaymentResponseDTO> response = new Response<>();
        User authenticatedUser = ExtractUser.extract();
        response.setData(transactionService.pay(paymentDto,authenticatedUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody @Valid TransactionDepositRequestDTO depositDTO){
        User authenticatedUser = ExtractUser.extract();
        return ResponseEntity.ok(transactionService.deposit(depositDTO,authenticatedUser));
    }

}
