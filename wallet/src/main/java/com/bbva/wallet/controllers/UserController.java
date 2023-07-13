package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.UpdateUserDto;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.hateoas.GenericModelAssembler;
import com.bbva.wallet.hateoas.UserModel;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.Response;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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


    @PreAuthorize("#id == authentication.principal.id")
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Response> updateUser(@PathVariable("id") Long id,@RequestBody @Valid UpdateUserDto dto, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Response<User> response = new Response<>();
        response.setData(userService.updateUser(dto,user));
        return ResponseEntity.ok(response);
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
