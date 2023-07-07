package com.bbva.wallet.services;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.ExceptionUserAlreadyExist;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ExceptionUserNotFound());
    }

    public User save(User user){
        userRepository.findByEmail(user.getEmail())
                .ifPresent(present -> {
                    throw new ExceptionUserAlreadyExist();
                });
        return userRepository.save(user);
    }
}
