package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthenticationResponse;
import com.bbva.wallet.dtos.SingInRequestDTO;
import com.bbva.wallet.dtos.SingUpRequestDTO;
import com.bbva.wallet.services.AuthenticationService;
import com.bbva.wallet.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = @Content)
@Tag(name = "Authentications")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            description = "Endpoint sin restricciones",
            summary = "Crea un nuevo usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Response> signup(@RequestBody @Valid SingUpRequestDTO request) {
        Response<JwtAuthenticationResponse> response = new Response<>();
        response.setData(authenticationService.singUp(request));
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Endpoint sin restricciones",
            summary = "Logea un usuario creado",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping("login")
    public ResponseEntity<Response> signin(@RequestBody @Valid SingInRequestDTO request) {
        Response<JwtAuthenticationResponse> response = new Response<>();
        response.setData(authenticationService.singIn(request));
        return ResponseEntity.ok(response);
    }
}
