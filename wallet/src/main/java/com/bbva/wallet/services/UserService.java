package com.bbva.wallet.services;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.stereotype.Service;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.exceptions.ExceptionUserAlreadyExist;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<Account> getUserAccounts(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ExceptionUserNotFound());

        return user
                .getAccountList().stream()
                .filter(account -> !account.isSoftDelete())
                .collect(Collectors.toList());
    }

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

    public void removeUser (Long id){
        Optional<User> userToDelete = userRepository.findById(id);

        if (userToDelete.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new ExceptionUserNotFound();
        }

    }

    public List<User> getAll() {
        return userRepository.findAll();

    }
}
