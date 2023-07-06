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

        List<Account> listaDeCuentas = accountRepository.findAllByUserId(authenticatedUser);
        System.out.println(listaDeCuentas);
        Account savedAccount = null;
        if(listaDeCuentas.size() == 0 || listaDeCuentas.stream().noneMatch(existingAccount -> existingAccount.getCurrency().equals(currency))){
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
        //Utilizar handleExceptions
        System.out.println("YA HAY CUENTA CREADA");
    }
        return savedAccount;


//        List<Account> existingAccounts = authenticatedUser.getAccounts().
//                stream()
//                .filter(existingAccount->existingAccount.getCurrency().equals(currency))
//                .toList();
//
//        Account savedAccount = null;
//
//        if(existingAccounts.size()==0) {
//            Account account = new Account();
//            account.setCurrency(currency);
//            if (currency.toString().equals("ARS")) {
//                account.setTransactionLimit(300000.0);
//            } else {
//                account.setTransactionLimit(1000.0);
//            }
//            account.setBalance(0.0);
//            account.setUserId(authenticatedUser);
//            savedAccount = accountRepository.save(account);
//
//
//        }else {
//            //Utilizar handleExceptions
//            System.out.println("YA HAY CUENTA CREADA");
//        }
//        return savedAccount;


    }


}
