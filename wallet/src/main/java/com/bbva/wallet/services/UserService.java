package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<Account> getUserAccounts(Long id){
        //CAMBIAR CUANDO HAYA LAS CUSTOM EXCEPTION
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("usuario no existe"));

        return user.getAccountList();
    }
}
