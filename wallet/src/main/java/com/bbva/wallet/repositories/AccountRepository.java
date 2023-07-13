package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE accounts", nativeQuery = true)
    void truncateTable();
}
