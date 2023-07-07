package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthenticationResponse;
import com.bbva.wallet.dtos.SingInRequestDTO;
import com.bbva.wallet.dtos.SingUpRequestDTO;
import com.bbva.wallet.services.AuthenticationService;
import com.bbva.wallet.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Response> signup(@RequestBody @Valid SingUpRequestDTO request) {
        Response<JwtAuthenticationResponse> response = new Response<>();
        response.setData(authenticationService.singUp(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("login")
    public ResponseEntity<Response> signin(@RequestBody @Valid SingInRequestDTO request) {
        Response<JwtAuthenticationResponse> response = new Response<>();
        response.setData(authenticationService.singIn(request));
        return ResponseEntity.ok(response);
    }
}
