package com.bbva.wallet.controllers;

import com.bbva.wallet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserAccounts(@PathVariable Long userId ){
        //CAMBIAR POR RESPONSE Y AGREGAR SEGURIDAD PARA ADMINS UNICAMENTE
        return ResponseEntity.ok().body(userService.getUserAccounts(userId));
    }
}
