package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User findByFirstName(String name);
}
