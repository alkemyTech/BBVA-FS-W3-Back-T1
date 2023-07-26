package com.bbva.wallet.service;

import com.bbva.wallet.dtos.TransactionPaymentRequestDTO;
import com.bbva.wallet.dtos.TransactionPaymentResponseDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.exceptions.ExceptionMismatchCurrencies;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.services.TransactionService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private User authenticatedUser;
    private Account paymentAccount;

    @Test
    public void pay_send_pesos_200(){
        authenticatedUser = new User();
        authenticatedUser.setId(1L);

        // Carga la cuenta antes de cada prueba
        paymentAccount = new Account();
        paymentAccount.setId(1L);
        paymentAccount.setCurrency(Currencies.ARS);
        paymentAccount.setTransactionLimit(1000.0);
        paymentAccount.setBalance(100.0);
        paymentAccount.setUserId(authenticatedUser);
        paymentAccount.setCbu("1234567890123456789012");
        paymentAccount.setCreationDate(LocalDateTime.now());
        paymentAccount.setUpdateDate(LocalDateTime.now());
        paymentAccount.setSoftDelete(false);
        paymentAccount.setTransaction(List.of());
        paymentAccount.setFixedTermDeposits(List.of());

        when(accountRepository.findById(any(Long.class))).thenReturn((Optional.of(paymentAccount)));
        when(accountRepository.save(any(Account.class))).thenReturn(paymentAccount);
        Transaction transaction = new Transaction();
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Crea una solicitud de pago válida
        TransactionPaymentRequestDTO paymentDto =
                new TransactionPaymentRequestDTO(1L,1.0,Currencies.ARS);

        TransactionPaymentResponseDTO response = transactionService.pay(paymentDto, authenticatedUser);

        assertNotNull(response);
        assertNotNull(response.getUpdatedAccount());
        assertEquals(Double.valueOf(99),paymentAccount.getBalance());
//        assertNotNull(response.getTransactionPayment());


        verify(accountRepository, times(1)).save(paymentAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void pay_send_dolares_200(){
        authenticatedUser = new User();
        authenticatedUser.setId(1L);

        // Carga la cuenta antes de cada prueba
        paymentAccount = new Account();
        paymentAccount.setId(1L);
        paymentAccount.setCurrency(Currencies.USD);
        paymentAccount.setTransactionLimit(1000.0);
        paymentAccount.setBalance(100.0);
        paymentAccount.setUserId(authenticatedUser);
        paymentAccount.setSoftDelete(false);

        when(accountRepository.findById(any(Long.class))).thenReturn((Optional.of(paymentAccount)));
        when(accountRepository.save(any(Account.class))).thenReturn(paymentAccount);

        // Crea una solicitud de pago válida
        TransactionPaymentRequestDTO paymentDto =
                new TransactionPaymentRequestDTO(1L,1.0,Currencies.USD);

        TransactionPaymentResponseDTO response = transactionService.pay(paymentDto, authenticatedUser);

        assertNotNull(response);
        assertNotNull(response.getUpdatedAccount());
        assertEquals(Double.valueOf(99),paymentAccount.getBalance());
//        assertNotNull(response.getTransactionPayment());


        verify(accountRepository, times(1)).save(paymentAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void pay_currency_missmach_400(){
        authenticatedUser = new User();
        authenticatedUser.setId(1L);

        // Carga la cuenta antes de cada prueba
        paymentAccount = new Account();
        paymentAccount.setId(1L);
        paymentAccount.setCurrency(Currencies.ARS);
        paymentAccount.setTransactionLimit(1000.0);
        paymentAccount.setBalance(100.0);
        paymentAccount.setUserId(authenticatedUser);
        paymentAccount.setSoftDelete(false);

        when(accountRepository.findById(any(Long.class))).thenReturn((Optional.of(paymentAccount)));
        when(accountRepository.save(any(Account.class))).thenReturn(paymentAccount);

        // Crea una solicitud de pago válida
        TransactionPaymentRequestDTO paymentDto =
                new TransactionPaymentRequestDTO(1L,1.0,Currencies.USD);

        var exception = assertThrows(ExceptionMismatchCurrencies.class, () -> {
            transactionService.pay(paymentDto, authenticatedUser);
        });
    }
}
