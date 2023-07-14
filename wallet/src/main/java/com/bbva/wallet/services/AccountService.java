package com.bbva.wallet.services;

import com.bbva.wallet.dtos.BalanceDto;
import com.bbva.wallet.dtos.CurrenciesDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.exceptions.ExceptionAccountAlreadyExist;
import com.bbva.wallet.exceptions.ExceptionAccountNotFound;
import com.bbva.wallet.exceptions.ExceptionUserAccountsNotFound;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.CurrencyLimit;
import com.bbva.wallet.utils.ExtractUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public Account createAccount(Currencies currency, User user ){

        User authenticatedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ExceptionUserNotFound());

        if(authenticatedUser.isSoftDelete()){
            throw new ExceptionUserNotFound();
        }

        boolean accountExists = authenticatedUser.getAccountList().stream()
                .anyMatch(existingAccount -> existingAccount.getCurrency().equals(currency) && !existingAccount.isSoftDelete());

        if (accountExists) {
            throw new ExceptionAccountAlreadyExist();
        }

        Account account = new Account();
        account.setCurrency(currency);
        account.setTransactionLimit(CurrencyLimit.getTransactionLimitForCurrency(currency));
        account.setBalance(0.0);
        account.setUserId(authenticatedUser);
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, User user, Double newTransactionLimit){
       Account account = accountRepository.findById(id).orElseThrow(ExceptionAccountNotFound::new);
       Boolean isUserAccount = user.getAccountList().stream().anyMatch(account1 -> account1.getId() == id);
       if (!isUserAccount) throw new ExceptionAccountNotFound("El usuario no tiene esta cuenta");
       account.setTransactionLimit(newTransactionLimit);
       return accountRepository.save(account);
    }
    public BalanceDto getBalance(){
        User authenticatedUser = userRepository.findById(ExtractUser.extract().getId())
                .orElseThrow(ExceptionUserNotFound::new);

        if(authenticatedUser.isSoftDelete()){
            throw new ExceptionUserNotFound();
        }

        Optional<Account> accountInArs = authenticatedUser.getAccountList().stream()
                .filter(account -> account.getCurrency()==Currencies.ARS && !account.isSoftDelete())
                .findFirst();

        Optional<Account> accountInUsd = authenticatedUser.getAccountList().stream()
                .filter(account -> account.getCurrency()==Currencies.USD && !account.isSoftDelete())
                .findFirst();

        if (accountInArs.isEmpty() && accountInUsd.isEmpty()){
            throw new ExceptionUserAccountsNotFound();
        }

        List<Transaction> historyTransactionsArs = accountInArs.isPresent() ? accountInArs.get().getTransaction() : null;
        List<Transaction> historyTransactionsUsd = accountInUsd.isPresent() ? accountInUsd.get().getTransaction() : null;

        List<FixedTermDeposit> fixedTermsAccount;
        if(accountInArs.isPresent())
             fixedTermsAccount = accountInArs.get().getFixedTermDeposits();
        else
            fixedTermsAccount = List.of();

        BalanceDto balanceResponse = new BalanceDto();
                balanceResponse.setAccountArs(accountInArs.orElse(null));
                balanceResponse.setAccountUsd(accountInUsd.orElse(null));
                balanceResponse.setHistoryArs(historyTransactionsArs);
                balanceResponse.setHistoryUsd(historyTransactionsUsd);
                balanceResponse.setFixedTerms(fixedTermsAccount);

        return balanceResponse;
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Slice<Account> getTen(Integer page) {
        return accountRepository.findAll(
                PageRequest.of(page, 10));
    }

    public void updateDepositBalance(Account account, Double amount) {
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }


}
