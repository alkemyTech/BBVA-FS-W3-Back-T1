package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionDepositRequestDTO;
import com.bbva.wallet.dtos.TransactionPaymentRequestDTO;
import com.bbva.wallet.dtos.TransactionPaymentResponseDTO;
import com.bbva.wallet.dtos.TransactionSendMoneyRequestDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.exceptions.*;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.ExtractUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.*;

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

    public Transaction getTransaction(Long id) {

        return transactionRepository.findById(id)
                .orElseThrow(ExceptionTransactionNotExist::new);
    }

    public Transaction editTransaction(Long id, String description) {

        User authenticatedUser = userRepository.findById(ExtractUser.extract().getId())
                .orElseThrow(ExceptionUserNotAuthenticated::new);

        Transaction transactionToEdit = transactionRepository.findById(id)
                .orElseThrow(ExceptionTransactionNotExist::new);

        if (transactionToEdit.getAccount().getUserId().getId().equals(authenticatedUser.getId())) {
            transactionToEdit.setDescription(description);
            return transactionRepository.save(transactionToEdit);
        } else {
            throw new ExceptionUserNotAuthenticated();
        }
    }

    public List<Transaction> getUserTransactions(Long userId) {
        User userTransactions = userRepository.findById(userId).orElseThrow(() -> new ExceptionUserNotFound());

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

        if (transactions.isEmpty()) {
            throw new ExceptionUserWithNoTransactions();
        }
        return transactions;
    }

    public List<Transaction> sendMoney(TransactionSendMoneyRequestDTO transactionDto, Currencies currency, User user) {
        Long recipientAccountId = transactionDto.getId();
        Double amount = transactionDto.getAmount();
        Account recipientAccount = accountRepository.findById(recipientAccountId).orElseThrow(() -> new ExceptionAccountNotFound());
        Account sourceAccount = accountRepository.findAll().stream()
                .filter(account -> account.getCurrency() == currency && account.getUserId()
                        .getId().equals(user.getId()))
                .findAny().orElseThrow(() -> new ExceptionAccountNotFound("El usuario no tiene una cuenta de este tipo de moneda"));

        if (user.isSoftDelete()) {
            throw new ExceptionUserNotFound();
        }

        if (currency != recipientAccount.getCurrency()) {
            throw new ExceptionMismatchCurrencies();
        }

        if (recipientAccount.getUserId().getId().equals(user.getId())) {
            throw new ExceptionTransactionNotAllowed("No se puede enviar dinero a uno mismo");
        }

        if (sourceAccount.getBalance() < amount) {
            throw new ExceptionInsufficientBalance();
        }

        if (sourceAccount.getTransactionLimit() < amount) {
            throw new ExceptionExceedTransactionLimit();
        }

        Double newBalanceSourceAccount = sourceAccount.getBalance() - amount;

        Transaction transactionPayment = Transaction.builder()
                .amount(amount)
                .description("Enviaste dinero")
                .account(sourceAccount)
                .type(TransactionType.PAYMENT)
                .accountBalance(newBalanceSourceAccount)
                .build();


        sourceAccount.setBalance(newBalanceSourceAccount);
        accountRepository.save(sourceAccount);
        transactionRepository.save(transactionPayment);

        Double newBalanceRecipientAccount = recipientAccount.getBalance() + amount;

        Transaction transactionIncome = Transaction.builder()
                .amount(amount)
                .description("Recibiste dinero")
                .account(recipientAccount)
                .type(TransactionType.INCOME)
                .accountBalance(newBalanceRecipientAccount)
                .build();


        recipientAccount.setBalance(newBalanceRecipientAccount);
        accountRepository.save(recipientAccount);
        transactionRepository.save(transactionIncome);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transactionPayment);
        transactions.add(transactionIncome);
        return transactions;
    }

    public TransactionPaymentResponseDTO pay(TransactionPaymentRequestDTO paymentDto, User authenticatedUser) {
        Double amount = paymentDto.getAmount();
        Long paymentAccountId = paymentDto.getId();
        Currencies paymentCurrency = paymentDto.getCurrency();
        String description = paymentDto.getDescription();

        if(description.isEmpty()){
            description = "Pagaste";
        }

        Account paymentAccount = accountRepository.findById(paymentAccountId)
                .orElseThrow(() -> new ExceptionAccountNotFound());

        if (paymentAccount.getUserId().isSoftDelete()) {
            throw new ExceptionUserNotFound();
        }

        if (!paymentAccount.getUserId().getId().equals(authenticatedUser.getId())) {
            throw new ExceptionAccountNotFound();
        }

        if (paymentCurrency != paymentAccount.getCurrency()) {
            throw new ExceptionMismatchCurrencies();
        }

        if (paymentAccount.getBalance() < amount) {
            throw new ExceptionInsufficientBalance();
        }

        Double newBalancePaymentAccount = paymentAccount.getBalance() - amount;

        Transaction transactionPayment = Transaction.builder()
                .amount(amount)
                .description(description)
                .account(paymentAccount)
                .accountBalance(newBalancePaymentAccount)
                .type(TransactionType.SERVICEPAYMENT)

                .build();
        paymentAccount.setBalance(newBalancePaymentAccount);

        TransactionPaymentResponseDTO responsePayment = new TransactionPaymentResponseDTO();
        responsePayment.setUpdatedAccount(accountRepository.save(paymentAccount));
        responsePayment.setTransactionPayment(transactionRepository.save(transactionPayment));
        return responsePayment;
    }

    public Transaction deposit(TransactionDepositRequestDTO deposit, User user) {
        Currencies currency = deposit.currency();
        Double amount = deposit.amount();
        String description = deposit.description();

        User authenticatedUser = userService.findById(user.getId())
                .orElseThrow(() -> new ExceptionUserNotFound());

        Optional<Account> optionalAccount = authenticatedUser.hasThisCurrencyAccount(currency);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            Transaction transaction = Transaction.builder()
                    .amount(amount)
                    .type(TransactionType.DEPOSIT)
                    .account(account)
                    .transactionDate(LocalDateTime.now())
                    .description(deposit.description())
                    .accountBalance(account.getBalance() + amount)
                    .build();

            transactionRepository.save(transaction);

            accountService.updateDepositBalance(account, amount);
            return transaction;

        } else throw new ExceptionAccountCurrencyNotFound();
    }

    public Page<Transaction> getTen(Integer page, Long id, TransactionType transactionType, Sort.Direction sortDirection, Currencies currencies) {
        User user = userRepository.findById(id).orElseThrow(() -> new ExceptionUserNotFound());
        Account account = null;
        if (currencies != null) {
            account = user.getAccountList().stream().filter(ac -> ac.getCurrency()==currencies && !ac.isSoftDelete())
                    .findFirst().orElseThrow(ExceptionAccountCurrencyNotFound::new);
        }

        Pageable pageable = PageRequest.of(page, 10, sortDirection, "transactionDate");

        // Continue with transaction retrieval based on the provided filters
        if (transactionType != null && currencies != null) {
            return transactionRepository.findByAccount_UserId_IdAndTypeAndAccount_Id(id, transactionType, account.getId(), pageable);
        } else if (transactionType != null) {
            return transactionRepository.findByAccount_UserId_IdAndType(id, transactionType, pageable);
        } else if (currencies != null) {
            return transactionRepository.findByAccount_UserId_IdAndAccount_Id(id, account.getId(), pageable);
        } else {
            return transactionRepository.findByAccount_UserId_Id(id, pageable);
        }
    }
}
