package com.bbva.wallet.services;

import com.bbva.wallet.dtos.JwtAuthenticationResponse;
import com.bbva.wallet.dtos.SingInRequestDTO;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse singUp(SingUpRequestDTO singUpRequestDTO){
        Role userRole = roleRepository.findByName(EnumRole.USER)
                .orElseGet(() -> roleRepository.save(new Role(EnumRole.USER)));
        Optional<User> oldUser = userService.findDeletedUser(singUpRequestDTO.email());
        User savedUser;
        if(oldUser.isEmpty()) {
            savedUser = User.builder()
                    .email(singUpRequestDTO.email())
                    .firstName(singUpRequestDTO.firstName())
                    .lastName(singUpRequestDTO.lastName())
                    .roleId(userRole)
                    .password(passwordEncoder.encode(singUpRequestDTO.password()))
                    .build();
        } else {
            savedUser = oldUser.get();
            savedUser.setFirstName(singUpRequestDTO.firstName());
            savedUser.setLastName(singUpRequestDTO.lastName());
            savedUser.setRoleId(userRole);
            savedUser.setPassword(passwordEncoder.encode(singUpRequestDTO.password()));
        }
        savedUser = saveUserWithAccounts(savedUser);

        String jwt = jwtService.generateToken(savedUser);

        return JwtAuthenticationResponse.builder().token(jwt).user(savedUser).build();
    }

    public JwtAuthenticationResponse singIn(SingInRequestDTO request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(),request.password()));
        User user = userService.findByEmail(request.email());
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).user(user).build();
    }


    private User saveUserWithAccounts(User user) {
        User savedUser = userService.save(user);

        Account arsAccount = Account.builder().currency(Currencies.ARS)
                .transactionLimit(300000.0)
                .userId(savedUser)
                .balance(0.0)
                .build();
        accountRepository.save(arsAccount);

        Account usdAccount = Account.builder().currency(Currencies.USD)
                .transactionLimit(1000.0)
                .userId(savedUser)
                .balance(0.0)
                .build();
        accountRepository.save(usdAccount);

        return savedUser;
    }
}
