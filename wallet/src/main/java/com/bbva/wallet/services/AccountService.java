package com.bbva.wallet.services;

import com.bbva.wallet.dtos.CurrenciesDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.exceptions.ExceptionAccountAlreadyExist;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.CurrencyLimit;
import com.bbva.wallet.utils.ExtractUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;


    public Account createAccount(CurrenciesDto currenciesDto ){

        Currencies currency = currenciesDto.getCurrency();
        User authenticatedUser= ExtractUser.extract();

        if(authenticatedUser.isSoftDelete()){
            throw new ExceptionUserNotFound();
        }

        boolean accountExists = authenticatedUser.getAccounts().stream()
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


}
