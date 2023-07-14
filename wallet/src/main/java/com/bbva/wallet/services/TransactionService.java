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
import com.bbva.wallet.dtos.PaymentDto;
import com.bbva.wallet.dtos.ResponsePaymentDto;
import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.exceptions.*;
import com.bbva.wallet.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import com.bbva.wallet.dtos.DepositDTO;

import java.time.LocalDateTime;
import java.util.Optional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

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

        if (recipientAccount.getUserId().getId().equals(authenticatedUser.getId()))
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
        accountRepository.save(sourceAccount);
        transactionRepository.save(transactionPayment);


        Transaction transactionIncome = Transaction.builder()
                        .amount(amount)
                        .description("Recibiste dinero")
                        .account(recipientAccount)
                        .type(TransactionType.INCOME)
                        .build();

        Double newBalanceRecipientAccount = recipientAccount.getBalance() + amount;
        recipientAccount.setBalance(newBalanceRecipientAccount);
        accountRepository.save(recipientAccount);
        transactionRepository.save(transactionIncome);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transactionPayment);
        transactions.add(transactionIncome);
        return transactions;
    }

    public ResponsePaymentDto pay(PaymentDto paymentDto) {
        User authenticatedUser = ExtractUser.extract();
        Double amount = paymentDto.getAmount();
        Long paymentAccountId = paymentDto.getId();
        Currencies paymentCurrency = paymentDto.getCurrency();
        Account paymentAccount = accountRepository.findById(paymentAccountId).filter(account -> account.getCurrency() == paymentCurrency)
                .orElseThrow(() -> new ExceptionAccountNotFound());

        if (paymentAccount.getUserId().isSoftDelete()) {
            throw new ExceptionUserNotFound();
        }

        if(!paymentAccount.getUserId().getId().equals(authenticatedUser.getId())){
            throw new ExceptionAccountNotFound();
        }

        if (paymentAccount.getBalance() < amount) {
            throw new ExceptionInsufficientBalance();
        }

        if (paymentCurrency != paymentAccount.getCurrency()) {
            throw new ExceptionMismatchCurrencies();
        }

        Transaction transactionPayment = Transaction.builder()
                .amount(amount)
                .description("Pagaste")
                .account(paymentAccount)
                .type(TransactionType.PAYMENT)
                .build();

        Double newBalancePaymentAccount = paymentAccount.getBalance() - amount;
        paymentAccount.setBalance(newBalancePaymentAccount);

        ResponsePaymentDto responsePayment = new ResponsePaymentDto();
        responsePayment.setUpdatedAccount(accountRepository.save(paymentAccount));
        responsePayment.setTransactionPayment(transactionRepository.save(transactionPayment));
        return responsePayment;
    }
    public Transaction deposit(DepositDTO deposit){
        Currencies currency = deposit.currency();
        Double amount = deposit.amount();

        User authenticatedUser = userService.findById(ExtractUser.extract().getId())
                .orElseThrow(() -> new ExceptionUserNotFound());

        Optional<Account> optionalAccount = authenticatedUser.hasThisCurrencyAccount(currency);

        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();

            Transaction transaction = Transaction.builder()
                    .amount(amount)
                    .type(TransactionType.DEPOSIT)
                    .account(account)
                    .transactionDate(LocalDateTime.now())
                    .build();

            transactionRepository.save(transaction);
            accountService.updateDepositBalance(account, amount);

            return transaction;

        } else throw new ExceptionAccountCurrenyNotFound();

    }
}
