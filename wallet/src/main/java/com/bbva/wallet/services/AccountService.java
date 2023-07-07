package com.bbva.wallet.services;

import com.bbva.wallet.dtos.CurrenciesDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
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
            //Utilizar handlerException para usuario eliminado
            System.out.println("Usuario eliminado");
            return null;
        }

        boolean accountExists = authenticatedUser.getAccounts().stream()
                .anyMatch(existingAccount -> existingAccount.getCurrency().equals(currency) && !existingAccount.isSoftDelete());

        if (accountExists) {
            System.out.println("YA HAY CUENTA CREADA");
            return null; // O maneja el caso de error de alguna manera específica
        }

        Account account = new Account();
        account.setCurrency(currency);
        account.setTransactionLimit(CurrencyLimit.getTransactionLimitForCurrency(currency));
        account.setBalance(0.0);
        account.setUserId(authenticatedUser);
        return accountRepository.save(account);
    }


}
