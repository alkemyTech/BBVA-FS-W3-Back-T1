package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.TransactionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Slice<Transaction> findByAccount_UserId_Id(Long userId, Pageable pageable);
    Slice<Transaction> findByAccount_UserId_IdAndType(Long userId, TransactionType transactionType, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM transactions", nativeQuery = true)
    void deleteAll();
}
