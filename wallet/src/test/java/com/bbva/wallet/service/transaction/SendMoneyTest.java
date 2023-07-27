package com.bbva.wallet.service.transaction;

import com.bbva.wallet.dtos.TransactionSendMoneyRequestDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.exceptions.*;
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

import static org.junit.Assert.*;
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
        recipientAccount.setBalance(0.0);

        TransactionSendMoneyRequestDTO dto =
                new TransactionSendMoneyRequestDTO(recipientAccount.getId(), 100.0);

        when(accountRepository.findById(recipientAccount.getId())).thenReturn((Optional.of(recipientAccount)));
        when(accountRepository.findAll()).thenReturn(List.of(sourceAccount));

        List<Transaction> response = transactionService.sendMoney(dto,Currencies.ARS,authenticatedUser);

        assertNotNull(response);
        assertEquals(2,response.size());
        assertTrue(response.stream().anyMatch(tr ->
                tr.getAccount().equals(sourceAccount) &&
                        tr.getAmount().equals(100.0) &&
                        tr.getType().equals(TransactionType.PAYMENT)));
        assertTrue(response.stream().anyMatch(tr ->
                tr.getAccount().equals(recipientAccount) &&
                        tr.getAmount().equals(100.0) &&
                        tr.getType().equals(TransactionType.INCOME)));
        assertEquals(Double.valueOf(1),sourceAccount.getBalance());
        assertEquals(Double.valueOf(100),recipientAccount.getBalance());

    }

    @Test
    public void RecipientAccountNotFound(){

        when(accountRepository.findById(recipientAccount.getId())).thenReturn((Optional.empty()));

        TransactionSendMoneyRequestDTO dto =
                new TransactionSendMoneyRequestDTO(recipientAccount.getId(), 100.0);

        assertThrows(ExceptionAccountNotFound.class, () -> {
            transactionService.sendMoney(dto, Currencies.ARS,authenticatedUser);
        });
    }

    @Test
    public void sourceAccountCurrencyMismach400(){


        when(accountRepository.findById(recipientAccount.getId())).thenReturn((Optional.of(recipientAccount)));
        when(accountRepository.findAll()).thenReturn(List.of(sourceAccount));

        TransactionSendMoneyRequestDTO dto =
                new TransactionSendMoneyRequestDTO(recipientAccount.getId(), 100.0);

        assertThrows(ExceptionAccountNotFound.class, () -> {
            transactionService.sendMoney(dto, Currencies.USD,authenticatedUser);
        });
    }

    @Test
    public void userSoftDeleteTrue400(){

        authenticatedUser.setSoftDelete(true);
        sourceAccount.setCurrency(Currencies.ARS);
        sourceAccount.setTransactionLimit(1000.0);
        sourceAccount.setBalance(101.0);

        recipientAccount.setCurrency(Currencies.ARS);
        recipientAccount.setBalance(0.0);

        when(accountRepository.findById(recipientAccount.getId())).thenReturn((Optional.of(recipientAccount)));
        when(accountRepository.findAll()).thenReturn(List.of(sourceAccount));

        TransactionSendMoneyRequestDTO dto =
                new TransactionSendMoneyRequestDTO(recipientAccount.getId(), 100.0);

        assertThrows(ExceptionUserNotFound.class, () -> {
            transactionService.sendMoney(dto, Currencies.ARS,authenticatedUser);
        });
    }

    @Test
    public void recipientAccountCurrencyMismach400(){

        sourceAccount.setCurrency(Currencies.ARS);
        sourceAccount.setTransactionLimit(1000.0);
        sourceAccount.setBalance(101.0);

        recipientAccount.setCurrency(Currencies.USD);
        recipientAccount.setBalance(0.0);

        when(accountRepository.findById(recipientAccount.getId())).thenReturn((Optional.of(recipientAccount)));
        when(accountRepository.findAll()).thenReturn(List.of(sourceAccount));

        TransactionSendMoneyRequestDTO dto =
                new TransactionSendMoneyRequestDTO(recipientAccount.getId(), 100.0);

        assertThrows(ExceptionMismatchCurrencies.class, () -> {
            transactionService.sendMoney(dto, Currencies.ARS,authenticatedUser);
        });
    }

    @Test
    public void SendToYourSelf400(){

        sourceAccount.setCurrency(Currencies.ARS);
        sourceAccount.setTransactionLimit(1000.0);
        sourceAccount.setBalance(101.0);

        when(accountRepository.findById(recipientAccount.getId())).thenReturn((Optional.of(sourceAccount)));
        when(accountRepository.findAll()).thenReturn(List.of(sourceAccount));

        TransactionSendMoneyRequestDTO dto =
                new TransactionSendMoneyRequestDTO(recipientAccount.getId(), 100.0);

        assertThrows(ExceptionTransactionNotAllowed.class, () -> {
            transactionService.sendMoney(dto, Currencies.ARS,authenticatedUser);
        });
    }

    @Test
    public void InsufficientBalance400(){
        sourceAccount.setCurrency(Currencies.ARS);
        sourceAccount.setTransactionLimit(1000.0);
        sourceAccount.setBalance(10.0);

        recipientAccount.setCurrency(Currencies.ARS);

        when(accountRepository.findById(recipientAccount.getId())).thenReturn((Optional.of(recipientAccount)));
        when(accountRepository.findAll()).thenReturn(List.of(sourceAccount));

        TransactionSendMoneyRequestDTO dto =
                new TransactionSendMoneyRequestDTO(recipientAccount.getId(), 11.0);

        assertThrows(ExceptionInsufficientBalance.class, () -> {
            transactionService.sendMoney(dto, Currencies.ARS,authenticatedUser);
        });
    }

    @Test
    public void TransactionLimitExceeded400(){
        sourceAccount.setCurrency(Currencies.ARS);
        sourceAccount.setTransactionLimit(100.0);
        sourceAccount.setBalance(1000.0);

        recipientAccount.setCurrency(Currencies.ARS);

        when(accountRepository.findById(recipientAccount.getId())).thenReturn((Optional.of(recipientAccount)));
        when(accountRepository.findAll()).thenReturn(List.of(sourceAccount));

        TransactionSendMoneyRequestDTO dto =
                new TransactionSendMoneyRequestDTO(recipientAccount.getId(), 101.0);

        assertThrows(ExceptionExceedTransactionLimit.class, () -> {
            transactionService.sendMoney(dto, Currencies.ARS,authenticatedUser);
        });
    }
}
