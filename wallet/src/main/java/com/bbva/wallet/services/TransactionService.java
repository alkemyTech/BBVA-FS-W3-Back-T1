package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.ExtractUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getUserTransactions(Long userId){
        User userTransactions = userRepository.findById(userId).orElseThrow(()->new ExceptionUserNotFound());

        List<Account> userAccounts = userTransactions.getAccountList();

        Optional<Account> arsAccount = userAccounts.stream().filter(account ->
                account.getCurrency() == Currencies.ARS && !account.isSoftDelete()).findFirst();

        Optional<Account> usdAccount = userAccounts.stream().filter(account ->
                account.getCurrency() == Currencies.USD && !account.isSoftDelete()).findFirst();

        List<Transaction> arsTransactions = arsAccount.isPresent() ?
                arsAccount.get().getTransaction() : Collections.emptyList();

        List<Transaction> usdTransactions = usdAccount.isPresent() ?
                usdAccount.get().getTransaction() : Collections.emptyList();

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(arsTransactions);
        transactions.addAll(usdTransactions);

        return transactions;
    }

}
