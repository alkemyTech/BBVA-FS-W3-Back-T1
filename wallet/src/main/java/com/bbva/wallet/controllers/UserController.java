package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.Response;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN') || #id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> removeUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.removeUser(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUser(@PathVariable("id") Long id, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Response<User> response = new Response<>();
        response.setData(userService.getUser(user.getId()));
        return ResponseEntity.ok(response);
    }
}
