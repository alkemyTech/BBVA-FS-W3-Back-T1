package com.bbva.wallet.services;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.exceptions.ExceptionUnauthorizedUser;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.ExtractUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public User save(User user) {

        User userExist = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(userExist != null && !userExist.isSoftDelete()){
            throw new ExceptionUserAlreadyExist();
        }
        user.setSoftDelete(false);

        return userRepository.save(user);
    }

    public ResponseEntity<?> removeUser (Long id){
        User loggedUser = ExtractUser.extract();

        if ("ADMIN".equals(loggedUser.getRoleId().getName().name()) || id.equals(loggedUser.getId())){
            User userToDelete = userRepository.findById(id).orElseThrow(ExceptionUserNotFound::new);
            userRepository.delete(userToDelete);
            return ResponseEntity.ok(userToDelete);
        } else {
            throw new ExceptionUnauthorizedUser();
        }

    }

    public Optional<User> findDeletedUser(String email){
        return userRepository.findSoftDeletedUser(email);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

}
