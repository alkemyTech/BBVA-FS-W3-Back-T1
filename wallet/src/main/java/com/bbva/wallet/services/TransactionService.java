package com.bbva.wallet.services;

import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.exceptions.ExceptionTransactionNotExist;
import com.bbva.wallet.exceptions.ExceptionUserNotAuthenticated;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public Transaction getTransaction(Long id, User authenticatedUser){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(ExceptionTransactionNotExist::new);

        if(transaction.getAccount().getUserId().getId().equals(authenticatedUser.getId()) || authenticatedUser.getRoleId().getName().equals(EnumRole.ADMIN)){;
            return transaction;
        } else {
            throw new ExceptionUserNotAuthenticated();
        }
    }

}
