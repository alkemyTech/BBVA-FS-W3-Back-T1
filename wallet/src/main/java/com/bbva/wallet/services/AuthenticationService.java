package com.bbva.wallet.services;

import com.bbva.wallet.dtos.SingUpRequestDTO;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public User singUp(SingUpRequestDTO singUpRequestDTO){
        Role userRole = roleRepository.findByName(EnumRole.USER).orElseThrow(() -> new IllegalStateException("El rol USER no existe"));
        User user = User.builder()
                .email(singUpRequestDTO.email())
                .firstName(singUpRequestDTO.firstName())
                .lastName(singUpRequestDTO.lastName())
                .roleId(userRole)
                .password(passwordEncoder.encode(singUpRequestDTO.password()))
                .build();

        User savedUser = userService.save(user);

        //agregar creacion de cuentas

        return  savedUser;
    }
}
