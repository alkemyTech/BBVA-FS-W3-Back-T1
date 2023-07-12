package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.Response;
import com.bbva.wallet.hateos.UserModel;
import com.bbva.wallet.hateos.GenericModelAssembler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.CollectionModel;
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

    private  GenericModelAssembler<User,UserModel> genericModelAssembler;

    public UserController() {
        this.genericModelAssembler = new GenericModelAssembler<>(UserController.class, UserModel.class);
    }

    @PreAuthorize("hasAuthority('ADMIN') || #id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> removeUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.removeUser(id));
    }

    @GetMapping
    public ResponseEntity<Response> getAll(@RequestParam(required = false) Optional<Integer> page) {
        Response response = new Response<>();
        CollectionModel<UserModel> collectionModel;
        if (page.isPresent()) {
            Slice<User> pagedEntity = userService.getTen(page.get());
            collectionModel = genericModelAssembler.toCollectionModel(pagedEntity);
            response.setData(collectionModel);
        }
        else {
            response.setData(userService.getAll());
        }
        return ResponseEntity.ok(response);
    }


}
