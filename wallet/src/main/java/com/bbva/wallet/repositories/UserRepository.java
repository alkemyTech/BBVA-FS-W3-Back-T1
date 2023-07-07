package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.softDelete = true AND u.email = :email")
    Optional<User> findSoftDeletedUser(@Param("email") String email);
}
