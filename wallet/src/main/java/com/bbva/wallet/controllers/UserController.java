package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<Response> getAll(@RequestParam(required = false) Optional<Integer> page) {
        Response response = new Response<>();

        if (page.isPresent()) {
            response.setData(userService.getTen(page.get()));
        }
        else {
            response.setData(userService.getAll());
        }
        return ResponseEntity.ok(response);
    }


}
