package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.ExceptionTransactionNotExist;
import com.bbva.wallet.exceptions.ExceptionUserNotAuthenticated;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.ExtractUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public Transaction editTransaction(Long id, String description){

        User authenticatedUser = userRepository.findById(ExtractUser.extract().getId())
                .orElseThrow(ExceptionUserNotAuthenticated::new);

        Transaction transactionToEdit = transactionRepository.findById(id)
                .orElseThrow(ExceptionTransactionNotExist::new);

        if(transactionToEdit.getAccount().getUserId().getId().equals(authenticatedUser.getId())){
            transactionToEdit.setDescription(description);
            return transactionRepository.save(transactionToEdit);
        } else {
            throw new ExceptionUserNotAuthenticated();
        }
    }
}
