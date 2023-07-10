package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fixedTerm")
public class FixedTermController {

    @PostMapping
    public ResponseEntity<Response> createFixedTerm(){
        Response<FixedTermDeposit> response = new Response<>();
        return ResponseEntity.ok(response);
    }
}
