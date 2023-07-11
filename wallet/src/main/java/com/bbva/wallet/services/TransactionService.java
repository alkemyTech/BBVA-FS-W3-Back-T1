package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.exceptions.*;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.utils.ExtractUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Transactional
@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> sendMoney(TransactionDto transactionDto, Currencies currency) {
        User authenticatedUser = ExtractUser.extract();
        Long recipientAccountId = transactionDto.getId();
        Double amount = transactionDto.getAmount();
        Account recipientAccount = accountRepository.findById(recipientAccountId).orElseThrow(() -> new ExceptionAccountNotFound());
        Account sourceAccount = authenticatedUser.getAccountList().stream().filter(account -> account.getCurrency() == currency).findAny().orElseThrow(() -> new ExceptionAccountNotFound());

        if(authenticatedUser.isSoftDelete())
        {throw new ExceptionUserNotFound();}

        if(currency != recipientAccount.getCurrency())
        {throw new ExceptionMismatchCurrencies();}

        if (recipientAccount.getUserId().equals(authenticatedUser))
        {throw new ExceptionTransactionNotAllowed("No se puede enviar dinero a uno mismo");}

        if (sourceAccount.getBalance() < amount)
        {throw new ExceptionInsufficientBalance();}

        if (sourceAccount.getTransactionLimit()<amount)
        {throw new ExceptionExceedTransactionLimit();}


                Transaction transactionPayment = Transaction.builder()
                        .amount(amount)
                        .description("Enviaste dinero")
                        .account(sourceAccount)
                        .type(TransactionType.PAYMENT)
                        .build();

                Double newBalanceSourceAccount = sourceAccount.getBalance() - amount;
                sourceAccount.setBalance(newBalanceSourceAccount);
                transactionRepository.save(transactionPayment);


                Transaction transactionIncome = Transaction.builder()
                        .amount(amount)
                        .description("Recibiste dinero")
                        .account(recipientAccount)
                        .type(TransactionType.INCOME)
                        .build();
                Double newBalanceRecipientAccount = recipientAccount.getBalance() + amount;
                recipientAccount.setBalance(newBalanceRecipientAccount);
                transactionRepository.save(transactionIncome);

                List<Transaction> transactions = new ArrayList<>();
                transactions.add(transactionPayment);
                transactions.add(transactionIncome);
                return transactions;





    }

}

