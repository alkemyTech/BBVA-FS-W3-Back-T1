package com.bbva.wallet.services;

import com.bbva.wallet.dtos.CreateFixedTermDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.ExceptionAccountCurrenyNotFound;
import com.bbva.wallet.exceptions.ExceptionInsufficientBalance;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.FixedTermDepositsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class FixedTermService {

    private final FixedTermDepositsRepository fixedTermDepositsRepository;
    private final AccountRepository accountRepository;

    public FixedTermDeposit createFixedTermDeposit(CreateFixedTermDto dto, User user){
        //verificar que un usuario tenga cuentas asociadas con la currency
        Predicate<Account> compareCurrencies = account -> account.getCurrency().equals(dto.currencies());
        Account account = user.getAccountList().stream().filter(compareCurrencies).findFirst().orElseThrow(ExceptionAccountCurrenyNotFound::new);

        FixedTermDeposit savedFixedTerm = makeFixedTerm(account,dto);

        return savedFixedTerm;
    }

    private FixedTermDeposit makeFixedTerm(Account account,CreateFixedTermDto dto) {
        if (account.getBalance() < dto.amount()){
            throw new ExceptionInsufficientBalance();
        }
        Timestamp closingDate = Timestamp.valueOf(LocalDateTime.now().plusDays(dto.cantDias()));
        Double interest = dto.amount() * 0.02 * dto.cantDias();
        var newFixedTerm = FixedTermDeposit.builder()
                .account(account)
                .amount(dto.amount())
                .interest(interest)
                .closingDate(closingDate)
                .build();
        account.setBalance(account.getBalance() - dto.amount());
        accountRepository.save(account);
        return fixedTermDepositsRepository.save(newFixedTerm);
    }
}
