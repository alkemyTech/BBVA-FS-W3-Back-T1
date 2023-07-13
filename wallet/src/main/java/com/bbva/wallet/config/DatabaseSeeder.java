package com.bbva.wallet.config;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import com.bbva.wallet.enums.EnumRole;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Value("${develop.seeder}")
    private Boolean loadDataBase;

    @Override
    public void run(String... args) throws Exception {
        if (loadDataBase){
            Role roleUser = roleRepository.findByName(EnumRole.USER).orElseGet(() -> roleRepository.save(new Role(EnumRole.USER)));
            Role roleAdmin = roleRepository.findByName(EnumRole.ADMIN).orElseGet(() -> roleRepository.save(new Role(EnumRole.ADMIN)));

            accountRepository.deleteAll();

            userRepository.deleteAll();

            transactionRepository.deleteAll();

            //admin sin cuentas
            User adminSinCuentas = new User();
            makeUser(adminSinCuentas,"adminSinCuenta@example.com",roleAdmin);
            adminSinCuentas = saveUser(adminSinCuentas);

            //admin solo con cuenta en pesos
            User adminCuentaEnPesos = new User();
            makeUser(adminCuentaEnPesos,"adminCuentaEnPesos@example.com",roleAdmin);
            adminCuentaEnPesos = saveUser(adminCuentaEnPesos);
            Account savedAccount= accountService.createAccount(Currencies.ARS,adminCuentaEnPesos);


            //admin solo con cuenta en dolares
            User adminCuentaEnDolares = new User();
            makeUser(adminCuentaEnDolares,"adminCuentaEnDolares@example.com",roleAdmin);
            adminCuentaEnDolares = saveUser(adminCuentaEnDolares);
            savedAccount= accountService.createAccount(Currencies.USD,adminCuentaEnDolares);

            //admin con cuenta en pesos con balance en 100.000$
            User adminPesosBalance100mil = new User();
            makeUser(adminPesosBalance100mil,"adminPesosBalance100mil@example.com",roleAdmin);
            adminPesosBalance100mil= saveUser(adminPesosBalance100mil);
            savedAccount= accountService.createAccount(Currencies.USD,adminPesosBalance100mil);
            savedAccount.setBalance(100_000.0);
            accountRepository.save(savedAccount);

            //admin con cuenta en dolares con balance en 10.000$
            User adminDolares10mil = new User();
            makeUser(adminDolares10mil,"adminDolares10mil@example.com",roleAdmin);
            adminDolares10mil= saveUser(adminDolares10mil);
            savedAccount= accountService.createAccount(Currencies.USD,adminDolares10mil);
            savedAccount.setBalance(10000.0);
            accountRepository.save(savedAccount);

            // Usuario sin cuentas
            User userSinCuentas = new User();
            makeUser(userSinCuentas, "userSinCuenta@example.com", roleUser);
            userSinCuentas = saveUser(userSinCuentas);

            // Usuario con cuenta en pesos
            User userCuentaEnPesos = new User();
            makeUser(userCuentaEnPesos, "userCuentaEnPesos@example.com", roleUser);
            userCuentaEnPesos = saveUser(userCuentaEnPesos);
            savedAccount = accountService.createAccount(Currencies.ARS, userCuentaEnPesos);

            // Usuario con cuenta en dólares
            User userCuentaEnDolares = new User();
            makeUser(userCuentaEnDolares, "userCuentaEnDolares@example.com", roleUser);
            userCuentaEnDolares = saveUser(userCuentaEnDolares);
            savedAccount = accountService.createAccount(Currencies.USD, userCuentaEnDolares);

            // Usuario con cuenta en pesos con balance de 100.000$
            User userPesosBalance100mil = new User();
            makeUser(userPesosBalance100mil, "userPesosBalance100mil@example.com", roleUser);
            userPesosBalance100mil = saveUser(userPesosBalance100mil);
            savedAccount = accountService.createAccount(Currencies.USD, userPesosBalance100mil);
            savedAccount.setBalance(100_000.0);
            accountRepository.save(savedAccount);

            // Usuario con cuenta en dólares con balance de 10.000$
            User userDolares10mil = new User();
            makeUser(userDolares10mil, "userDolares10mil@example.com", roleUser);
            userDolares10mil = saveUser(userDolares10mil);
            savedAccount = accountService.createAccount(Currencies.USD, userDolares10mil);
            savedAccount.setBalance(10_000.0);
            accountRepository.save(savedAccount);


            // Crear 5 usuarios administradores
            for (int i = 0; i < 5; i++) {
                User admin = new User();
                makeUser(admin,"admin" + i +"@example.com",roleAdmin);
                userRepository.findByEmail(admin.getEmail()).ifPresent(oldUser -> {admin.setId(oldUser.getId());});
                userRepository.save(admin);
                User savedadmin = saveUser(admin);
                accountService.createAccount(Currencies.USD, savedadmin);
                accountService.createAccount(Currencies.ARS, savedadmin);
            }

            // Crear 5 usuarios regulares
            for (int i = 0; i < 5; i++) {
                User regularUser = new User();
                makeUser(regularUser,"user" + i + "@example.com",roleUser);
                User savedRegularUser = saveUser(regularUser);
                accountService.createAccount(Currencies.USD, savedRegularUser);
                accountService.createAccount(Currencies.ARS, savedRegularUser);
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