package com.bbva.wallet.services;

import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.exceptions.ExceptionTransactionNotExist;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction getTransaction(Long id){

        return transactionRepository.findById(id)
                .orElseThrow(ExceptionTransactionNotExist::new);

    }
}