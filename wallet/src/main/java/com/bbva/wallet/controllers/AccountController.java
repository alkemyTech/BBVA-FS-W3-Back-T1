package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.AccountCbuRequestDTO;
import com.bbva.wallet.dtos.CreateAccountCurrencyRequestDTO;
import com.bbva.wallet.dtos.AccountBalanceResponseDTO;
import com.bbva.wallet.dtos.AccountUpdateRequestDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.hateoas.AccountModel;
import com.bbva.wallet.hateoas.GenericModelAssembler;
import com.bbva.wallet.hateoas.UserModel;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.ExtractUser;
import com.bbva.wallet.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = @Content)
})
@Tag(name = "Accounts")
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    private GenericModelAssembler<Account,AccountModel> genericModelAssembler;
    public AccountController() {
        this.genericModelAssembler = new GenericModelAssembler<>(AccountController.class, AccountModel.class);
    }

    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Busca una cuenta por cbu",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = Account.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @GetMapping("/cbu/{cbu}")
    public ResponseEntity<Response> getByCbu(@PathVariable String cbu){
        Response response = new Response<>();
        response.setData(accountService.getByCbu(cbu));
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Crea una cuenta",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = Account.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Response> saveAccount(@RequestBody CreateAccountCurrencyRequestDTO currenciesDto){
        Response<Account> response = new Response<>();
        User user = ExtractUser.extract();
        response.setData(accountService.createAccount(currenciesDto.getCurrency(),user));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            description = "Endpoint accesible a admins",
            summary = "Traer cuentas por id de usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = Account.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<Response> getUserAccounts(@PathVariable Long userId ){
        Response<List<Account>> response = new Response<>();
        response.setData(userService.getUserAccounts(userId));
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Endpoint accesible a usuario autenticado",
            summary = "Trae las cuentas, transacciones y plazo fijos del usuario autenticado",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = AccountBalanceResponseDTO.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @GetMapping("/balance")
    public ResponseEntity<Response> getUserBalance(){
        Response<AccountBalanceResponseDTO> response = new Response<>();
        response.setData(accountService.getBalance());
        return ResponseEntity.ok(response);
    }


    @Operation(
            description = "Endpoint accesible a usuario autenticado dueño de la cuenta",
            summary = "Actualiza el límite de la cuenta",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = Account.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @RequestMapping(value = "/{id}",method = RequestMethod.PATCH)
    public ResponseEntity<Response> updateAccount(@PathVariable Long id, Authentication authentication, @RequestBody AccountUpdateRequestDTO dto){
        User user= (User) authentication.getPrincipal();
        Response<Account> response = new Response<>();
        response.setData(accountService.updateAccount(id,user,dto.transactionLimit()));
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Endpoint accesible a admin",
            summary = "Trae de a 10 cuentas por página",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = Account.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Response> getAll(@RequestParam(required = false) Optional<Integer> page) {
        Response response = new Response<>();
        CollectionModel<AccountModel> collectionModel;
        Slice<Account> pagedEntity;
        if (page.isPresent()) {
             pagedEntity = accountService.getTen(page.get());
        }
        else {
            pagedEntity = accountService.getTen(0);
        }
        collectionModel = genericModelAssembler.toCollectionModel(pagedEntity);
        response.setData(collectionModel);
        return ResponseEntity.ok(response);
    }
}

