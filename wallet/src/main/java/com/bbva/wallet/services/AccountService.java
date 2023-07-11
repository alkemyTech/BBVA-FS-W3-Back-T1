package com.bbva.wallet.services;

import com.bbva.wallet.dtos.CurrenciesDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.exceptions.ExceptionAccountAlreadyExist;
import com.bbva.wallet.exceptions.ExceptionAccountNotFound;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.CurrencyLimit;
import com.bbva.wallet.utils.ExtractUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;


    public Account createAccount(CurrenciesDto currenciesDto ){

        Currencies currency = currenciesDto.getCurrency();
        User authenticatedUser = userRepository.findById(ExtractUser.extract().getId())
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
}
