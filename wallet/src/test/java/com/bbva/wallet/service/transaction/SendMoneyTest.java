package com.bbva.wallet.service.transaction;

import com.bbva.wallet.dtos.TransactionSendMoneyRequestDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.services.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SendMoneyTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private User authenticatedUser = new User().builder().id(1L).build();

    private Account sourceAccount = new Account().builder().id(1L).userId(authenticatedUser).softDelete(false).build();
    private Account recipientAccount = new Account().builder().id(2L).userId(new User().builder().id(2L).build()).softDelete(false).build();
    @Test
    public void goodResponse200(){

        sourceAccount.setCurrency(Currencies.ARS);
        sourceAccount.setTransactionLimit(1000.0);
        sourceAccount.setBalance(101.0);

        recipientAccount.setCurrency(Currencies.ARS);

        TransactionSendMoneyRequestDTO dto =
                new TransactionSendMoneyRequestDTO(recipientAccount.getId(), 100.0);

        when(accountRepository.findById(Long.valueOf(2L))).thenReturn((Optional.of(recipientAccount)));
        when(accountRepository.findAll()).thenReturn(List.of(sourceAccount));

        List<Transaction> response = transactionService.sendMoney(dto,Currencies.ARS,authenticatedUser);


        verify(accountRepository, times(1)).save(sourceAccount);
        verify(accountRepository, times(1)).save(recipientAccount);
        verify(transactionRepository, times(2)).save(new Transaction());
    }
}
