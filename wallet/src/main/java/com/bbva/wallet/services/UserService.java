package com.bbva.wallet.services;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public User removeUser (Long id){
        User userToDelete = userRepository.findById(id).filter(user -> !user.isSoftDelete()).orElseThrow(ExceptionUserNotFound::new);
        userRepository.delete(userToDelete);

        return userToDelete;


    }

    public Optional<User> findDeletedUser(String email){
        return userRepository.findSoftDeletedUser(email);
    }

    public List<User> getAll() {
        return userRepository.findAllActive();
    }

    public Slice<User> getTen(Integer page) {
        return userRepository.findSliceByPage(
                PageRequest.of(page, 10));
    }

}
