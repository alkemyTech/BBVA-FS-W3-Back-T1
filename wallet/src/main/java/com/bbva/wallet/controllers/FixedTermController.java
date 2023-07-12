package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.CreateFixedTermDto;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.FixedTermService;
import com.bbva.wallet.utils.Response;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public ResponseEntity<Response> createFixedTerm(@RequestBody @Valid CreateFixedTermDto dto, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        Response<FixedTermDeposit> response = new Response<>();
        response.setData(fixedTermService.createFixedTermDeposit(dto,user));
        return ResponseEntity.ok(response);
    }
}
