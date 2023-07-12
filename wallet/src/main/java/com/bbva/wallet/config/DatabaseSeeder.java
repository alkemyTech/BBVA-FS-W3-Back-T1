package com.bbva.wallet.config;

import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private final AccountService accountService;

    @Value("${develop.seeder}")
    private Boolean crearUsuarios;

    @Override
    public void run(String... args) throws Exception {
        if (crearUsuarios){
            Role roleUser = roleRepository.findByName(EnumRole.USER).orElseGet(() -> roleRepository.save(new Role(EnumRole.USER)));
            Role roleAdmin = roleRepository.findByName(EnumRole.ADMIN).orElseGet(() -> roleRepository.save(new Role(EnumRole.ADMIN)));


            // Crear 10 usuarios administradores
            //admin sin cuentas
            User adminSinCuentas = new User();
            makeUser(adminSinCuentas,"adminSinCuenta@example.com",roleAdmin);
            adminSinCuentas = saveUser(adminSinCuentas);

            //admin solo con cuenta en pesos
            User adminCuentaEnPesos = new User();
            makeUser(adminCuentaEnPesos,"adminCuentaEnPesos@example.com",roleAdmin);
            adminCuentaEnPesos = saveUser(adminCuentaEnPesos);
            accountService.createAccount(Currencies.ARS,adminCuentaEnPesos);

            //admin solo con cuenta en dolares
            User adminCuentaEnDolares = new User();
            makeUser(adminCuentaEnDolares,"adminCuentaEnDolares@example.com",roleAdmin);
            adminCuentaEnDolares = saveUser(adminCuentaEnDolares);
            accountService.createAccount(Currencies.USD,adminCuentaEnDolares);

            //admin con cuenta en pesos con balance en 100.000$
            User adminPesosBalance100mil = new User();
            makeUser(adminPesosBalance100mil,"adminPesosBalance100mil@example.com",roleAdmin);
            adminPesosBalance100mil= saveUser(adminPesosBalance100mil);
            accountService.createAccount(Currencies.USD,adminPesosBalance100mil).setBalance(100000.0);

            //admin con cuenta en dolares con balance en 10.000$
            User adminDolares10mil = new User();
            makeUser(adminDolares10mil,"adminDolares10mil@example.com",roleAdmin);
            adminDolares10mil= saveUser(adminDolares10mil);
            accountService.createAccount(Currencies.USD,adminDolares10mil).setBalance(10000.0);

            for (int i = 0; i < 10; i++) {
                User admin = new User();
                makeUser(admin,"admin" + i +"@example.com",roleUser);
                userRepository.findByEmail(admin.getEmail()).ifPresent(oldUser -> {admin.setId(oldUser.getId());});
                userRepository.save(admin);
                saveUser(admin);
            }

            // Crear 10 usuarios regulares
            for (int i = 0; i < 10; i++) {
                User regularUser = new User();
                makeUser(regularUser,"user" + i + "@example.com",roleUser);
                saveUser(regularUser);
            }
        }
    }

    private void makeUser(User user,String email,Role role){
        user.setFirstName("FistName" );
        user.setLastName("LastName");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123"));
        user.setRoleId(role);
        user.setSoftDelete(false);
        user.setUpdateDate(LocalDateTime.now());
        user.setCreationDate(LocalDateTime.now());
    }

    private User saveUser(User user){
        userRepository.findByEmail(user.getEmail()).ifPresent(oldUser -> {user.setId(oldUser.getId());});
        return userRepository.save(user);
    }
}