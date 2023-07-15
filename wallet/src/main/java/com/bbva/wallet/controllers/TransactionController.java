package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.*;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.hateoas.GenericModelAssembler;
import com.bbva.wallet.hateoas.TransactionModel;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.exceptions.ExceptionUserNotAuthenticated;

import com.bbva.wallet.hateoas.UserModel;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.ExtractUser;
import com.bbva.wallet.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = @Content)
})

@Tag(name = "Transactions")
@RestController
@RequestMapping("/transactions")
    public class TransactionController {
        @Autowired
        private TransactionService transactionService;
    private  GenericModelAssembler<Transaction,TransactionModel> genericModelAssembler;

    public TransactionController() {
        this.genericModelAssembler = new GenericModelAssembler<>(TransactionController.class, TransactionModel.class);
    }

    @Operation(
            description = "Endpoint accesible a usuarios autenticados(Si le pertenece la transaccion), o ADMIN (Todas las transacciones)",
            summary = "Obtiene la transacción por id",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = Transaction.class), mediaType = "application/json")
                            }
                    )
            }
    )
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

    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Modifica la descripción de una transacción",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = Transaction.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Response>editTransaction(@PathVariable Long id, @RequestBody TransactionDescriptionDto transactionDescriptionDto){
        Response <Transaction> response = new Response<>();
        response.setData(transactionService.editTransaction(id, transactionDescriptionDto.getDescription()));
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Endpoint accesible a administradores y al usuario dueño de la cuenta",
            summary = "Obtiene las transacciones de un usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
                            }
                    )
            }
    )
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

    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Realiza un envío de dinero en pesos",
            responses ={
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = {
                                    @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping("/sendArs")
    public ResponseEntity<Response> sendPesos(@Valid @RequestBody TransactionDto transactionDto) {
        Response<List<Transaction>> response = new Response<>();
        response.setData(transactionService.sendMoney(transactionDto,Currencies.ARS));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Realiza un envío de dinero en dólares",
            responses ={
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = {
                                    @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping("/sendUsd")
    public ResponseEntity<Response> sendDollars(@Valid @RequestBody TransactionDto transactionDto) {
        Response<List<Transaction>> response = new Response<>();
        response.setData(transactionService.sendMoney(transactionDto,Currencies.USD));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Realiza un pago",
            responses ={
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = {
                                    @Content(schema = @Schema(implementation = ResponsePaymentDto.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping("/payment")
    public ResponseEntity<Response> pay(@Valid @RequestBody PaymentDto paymentDto){
        Response<ResponsePaymentDto> response = new Response<>();
        User authenticatedUser = ExtractUser.extract();
        response.setData(transactionService.pay(paymentDto,authenticatedUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Realiza un depósito en su cuenta",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = Transaction.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody @Valid DepositDTO depositDTO){

        return ResponseEntity.ok(transactionService.deposit(depositDTO));
    }

}
