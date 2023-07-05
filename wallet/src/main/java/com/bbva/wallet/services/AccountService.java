package com.bbva.wallet.services;

import com.bbva.wallet.dtos.CurrenciesDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(CurrenciesDto currenciesDto ){

        User authenticatedUser = new User();            //ExtractUser.extract();
        authenticatedUser.setFirstName("Diego");
        authenticatedUser.setLastName("Perez");
        authenticatedUser.setEmail("diego@diego.com");
        authenticatedUser.setPassword("123");
        authenticatedUser.setRoleId(new Role(EnumRole.USER));


        Currencies currency = currenciesDto.getCurrency();

        List<Account> cuentaExiste = authenticatedUser.getAccounts().
                stream()
                .filter(cuenta->cuenta.getCurrency().equals(currency))
                .toList();

        Account savedAccount = null;

        if(cuentaExiste.size()==0) {
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
    }
}
