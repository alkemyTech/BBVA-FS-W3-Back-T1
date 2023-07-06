package com.bbva.wallet.services;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {

        return userRepository.findAll();

    }


}
