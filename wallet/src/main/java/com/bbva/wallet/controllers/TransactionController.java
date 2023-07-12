package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.exceptions.ExceptionUserNotAuthenticated;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.ExtractUser;
import com.bbva.wallet.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/transactions")
    public class TransactionController {
        @Autowired
        private TransactionService transactionService;
        @Autowired
        private UserRepository userRepository;

        @GetMapping("/{id}")
        public ResponseEntity<Response> getTransaction(@PathVariable Long id){
            User authenticatedUser = userRepository.findById(ExtractUser.extract().getId())
                    .orElseThrow(ExceptionUserNotFound::new);

            Response<Transaction> response = new Response<>();
            response.setData(transactionService.getTransaction(id, authenticatedUser));
            return ResponseEntity.ok(response);
        }

}
