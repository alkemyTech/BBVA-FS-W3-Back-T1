package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.FixedTermDeposit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedTermDepositsRepository extends JpaRepository<FixedTermDeposit,Long> {

    @Query(value = "DELETE FROM fixed_term_deposits", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteAll();
}
