package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.TransactionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    private Account account = new Account().builder().id(1L).currency(Currencies.ARS).softDelete(false).balance(100.0).build();
    @Test
    public void findByIdTransaction() {
        var newAccount = this.entityManager.merge(Account.builder()
                .id(1L)
                .currency(Currencies.ARS)
                .transactionLimit(300_000.0)
                .softDelete(false)
                .balance(100.0)
                .build());

        this.transactionRepository.save(Transaction.builder()
                .amount(10.0)
                .type(TransactionType.INCOME)
                .account(newAccount)
                .build());

        Optional<Transaction> transaction = this.transactionRepository.findById(1L);

        assertTrue(transaction.isPresent());
        assertEquals(Long.valueOf(1L), transaction.get().getId());
    }

    @Test
    public void findByIdTransactionEmpty() {
        Optional<Transaction> transaction = this.transactionRepository.findById(1L);

        assertTrue(transaction.isEmpty());
    }
}
