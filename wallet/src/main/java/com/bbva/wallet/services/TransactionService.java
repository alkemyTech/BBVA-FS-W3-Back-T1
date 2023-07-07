package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.utils.ExtractUser;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    public void sendMoney(TransactionDto transactionDto) {
       User authenticatedUser = ExtractUser.extract();




    }
}
