package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermCreateRequestDto;
import com.bbva.wallet.dtos.FixedTermSimulateResponseDTO;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.FixedTermService;
import com.bbva.wallet.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fixedTerm")
public class FixedTermController {

    private final FixedTermService fixedTermService;

    @PostMapping
    public ResponseEntity<Response> createFixedTerm(@RequestBody @Valid FixedTermCreateRequestDto dto, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        Response<FixedTermDeposit> response = new Response<>();
        response.setData(fixedTermService.createFixedTermDeposit(dto,user));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/simulate")
    public ResponseEntity<Response> simulateFixedTerm(@RequestBody @Valid FixedTermCreateRequestDto dto){
        Response<FixedTermSimulateResponseDTO> response = new Response<>();
        response.setData(fixedTermService.simulateFixedTerm(dto));
        return ResponseEntity.ok(response);
    }
}