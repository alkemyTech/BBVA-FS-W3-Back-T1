package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.exceptions.ExceptionTransactionNotExist;
import com.bbva.wallet.exceptions.ExceptionUserNotAuthenticated;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.ExtractUser;
import com.bbva.wallet.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
