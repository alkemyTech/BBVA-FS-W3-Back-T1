package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.BalanceDto;
import com.bbva.wallet.dtos.CurrenciesDto;
import com.bbva.wallet.dtos.UpdateAccountDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = @Content)
})
@Tag(name = "Accounts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    private final UserService userService;

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
    public ResponseEntity<Response> saveAccount(@RequestBody CurrenciesDto currenciesDto){
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
                                    @Content(schema = @Schema(implementation = BalanceDto.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @GetMapping("/balance")
    public ResponseEntity<Response> getUserBalance(){
        Response<BalanceDto> response = new Response<>();
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
    public ResponseEntity<Response> updateAccount(@PathVariable Long id, Authentication authentication, @RequestBody UpdateAccountDto dto){
        User user= (User) authentication.getPrincipal();
        Response<Account> response = new Response<>();
        response.setData(accountService.updateAccount(id,user,dto.transactionLimit()));
        return ResponseEntity.ok(response);
    }
}

