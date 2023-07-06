package com.bbva.wallet.services;

import com.bbva.wallet.dtos.CurrenciesDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.UserRepository;
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
        //User authenticatedUser= ExtractUser.extract();    //Con este anda una vez agregado el JWT
        User authenticatedUser = userRepository.findByFirstName("prueba22");

        if(authenticatedUser.isSoftDelete()){
            //Utilizar handlerException para usuario eliminado
            System.out.println("Usuario eliminado");
            return null;
        }
        List<Account> existingAccounts = authenticatedUser.getAccounts().stream()
                .filter(existingAccount->existingAccount.getCurrency().equals(currency) && !existingAccount.isSoftDelete())
                .toList();
        Account savedAccount = null;
        if(existingAccounts.size() == 0){
            Account account = new Account();
            account.setCurrency(currency);
            if (currency.toString().equals("ARS")) {
                account.setTransactionLimit(300000.0);
            } else {
                account.setTransactionLimit(1000.0);
            }
            account.setBalance(0.0);
            account.setUserId(authenticatedUser);
            savedAccount = accountRepository.save(account);
        }else {
            //Utilizar handleExceptions para cuenta ya creada
            System.out.println("YA HAY CUENTA CREADA");
            return null;
        }
        return savedAccount;

    }


}
