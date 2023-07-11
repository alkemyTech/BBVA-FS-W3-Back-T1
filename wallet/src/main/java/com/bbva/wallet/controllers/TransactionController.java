package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasAuthority('ADMIN') || #userId == authentication.principal.id")
    @GetMapping("/{userId}")
    public ResponseEntity<Response> getUserTransactions(@PathVariable Long userId){
        Response <List<Transaction>> response = new Response<>();
        response.setData(transactionService.getUserTransactions(userId));
        return ResponseEntity.ok(response);
    }
}
