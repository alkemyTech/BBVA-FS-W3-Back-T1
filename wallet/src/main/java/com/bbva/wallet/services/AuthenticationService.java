package com.bbva.wallet.services;

import com.bbva.wallet.dtos.SingUpRequestDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;

    public User singUp(SingUpRequestDTO singUpRequestDTO){
        Role userRole = roleRepository.findByName(EnumRole.USER).orElseThrow(() -> new IllegalStateException("El rol USER no existe"));
        User user = User.builder()
                .email(singUpRequestDTO.email())
                .firstName(singUpRequestDTO.firstName())
                .lastName(singUpRequestDTO.lastName())
                .roleId(userRole)
                .password(passwordEncoder.encode(singUpRequestDTO.password()))
                .build();

        User savedUser = saveUserWithAccounts(user);
        return  savedUser;
    }


    private User saveUserWithAccounts(User user) {
        User savedUser = userService.save(user);

        Account arsAccount = Account.builder().currency(Currencies.ARS)
                .transactionLimit(300000.0)
                .userId(savedUser)
                .balance(0.0)
                .softDelete(false)
                .build();
        accountRepository.save(arsAccount);

        Account usdAccount = Account.builder().currency(Currencies.USD)
                .transactionLimit(1000.0)
                .userId(savedUser)
                .balance(0.0)
                .softDelete(false)
                .build();
        accountRepository.save(usdAccount);

        return savedUser;
    }
}
