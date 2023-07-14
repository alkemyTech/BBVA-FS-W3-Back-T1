package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.UpdateUserDto;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.hateoas.GenericModelAssembler;
import com.bbva.wallet.hateoas.UserModel;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = @Content)
})

@Tag(name = "Users")
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

    @Operation(
            description = "Endpoint accesible a administradores",
            summary = "Elimina un usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PreAuthorize("hasAuthority('ADMIN') || #id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> removeUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.removeUser(id));
    }

    @Operation(
            description = "Endpoint accesible a administradores",
            summary = "Obtiene todos los usuarios registrados",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Response> getAll(@RequestParam(required = false) Optional<Integer> page) {
        Response response = new Response<>();
        CollectionModel<UserModel> collectionModel;
        Slice<User> pagedEntity;
        if (page.isPresent()) {
            pagedEntity = userService.getTen(page.get());
        }
        else {
            pagedEntity = userService.getTen(0);
        }
        collectionModel = genericModelAssembler.toCollectionModel(pagedEntity);
        response.setData(collectionModel);
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Endpoint accesible a usuario autenticado",
            summary = "Actualiza nombre, apellido y contraseña del usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PreAuthorize("#id == authentication.principal.id")
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Response> updateUser(@PathVariable("id") Long id,@RequestBody @Valid UpdateUserDto dto, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Response<User> response = new Response<>();
        response.setData(userService.updateUser(dto,user));
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Endpoint accesible a usuario autenticado",
            summary = "Obtiene la información del usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUser(@PathVariable("id") Long id, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Response<User> response = new Response<>();
        response.setData(userService.getUser(user.getId()));
        return ResponseEntity.ok(response);
    }
}
