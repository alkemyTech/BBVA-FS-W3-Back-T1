package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.TransactionDescriptionDto;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
