package com.bbva.wallet.services;

import com.bbva.wallet.dtos.CreateFixedTermDto;
import com.bbva.wallet.dtos.OutSimulateFixedTermDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.FixedTermDepositsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Transactional
public class FixedTermService {

//    private final FixedTermDepositsRepository fixedTermDepositsRepository;
//    private final AccountRepository accountRepository;

    private static final double INTEREST_PER_DAY = 0.002;

//    public FixedTermDeposit createFixedTermDeposit(CreateFixedTermDto dto, User user){
//        //verificar que un usuario tenga cuentas asociadas con la currency
//        Predicate<Account> compareCurrencies = account -> account.getCurrency().equals(Currencies.ARS);
//        Account account = user.getAccountList().stream().filter(compareCurrencies).findFirst().orElseThrow(ExceptionAccountCurrenyNotFound::new);
//        if (account.getBalance() < dto.amount()){
//            throw new ExceptionInsufficientBalance();
//        }
//        account.setBalance(account.getBalance() - dto.amount());
//        accountRepository.save(account);
//
//        FixedTermDeposit savedFixedTerm = fixedTermDepositsRepository.save(makeFixedTerm(account,dto));
//
//        return savedFixedTerm;
//    }

    public OutSimulateFixedTermDto simulateFixedTerm(CreateFixedTermDto dto){
        FixedTermDeposit fix = makeFixedTerm(null,dto);
        return new OutSimulateFixedTermDto(Timestamp.valueOf(LocalDateTime.now()),fix.getClosingDate(),
                fix.getAmount(),fix.getInterest(),fix.getAmount()+fix.getInterest());
    }

    private FixedTermDeposit makeFixedTerm(Account account,CreateFixedTermDto dto) {

        Timestamp closingDate = Timestamp.valueOf(LocalDateTime.now().plusDays(dto.cantDias()));
        Double interest = dto.amount() * INTEREST_PER_DAY * dto.cantDias();
        var newFixedTerm = FixedTermDeposit.builder()
                .account(account)
                .amount(dto.amount())
                .interest(interest)
                .closingDate(closingDate)
                .build();

        return newFixedTerm;
    }
}