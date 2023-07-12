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
    @Autowired
    private UserModelAssembler userModelAssembler;

     @Autowired
     private PagedResourcesAssembler<User> pagedResourcesAssembler;

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
            Page<User> pagedEntity = userService.getTen(page.get());
            PagedModel<UserModel> pagedModel = pagedResourcesAssembler.toModel(pagedEntity,userModelAssembler);
            collectionModel = CollectionModel.of(pagedModel.getContent());
//            collectionModel.add(pagedModel.getLinks());
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
