package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.softDelete = true AND u.email = :email")
    Optional<User> findSoftDeletedUser(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.softDelete = false")
    List<User> findAllActive();

    @Query("SELECT u FROM User u WHERE u.softDelete = false ORDER BY u.id")
    Slice<User> findSliceByPage(PageRequest pageable);

    @Modifying
    @Query(value = "DELETE FROM users", nativeQuery = true)
    void deleteAll();
}
