package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermCreateRequestDTO;
import com.bbva.wallet.dtos.FixedTermSimulateResponseDTO;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.FixedTermService;
import com.bbva.wallet.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = @Content)
})
@Tag(name = "Fixed Terms")
@RequiredArgsConstructor
@RestController
@RequestMapping("/fixedTerm")
public class FixedTermController {

    private final FixedTermService fixedTermService;

    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Crea un plazo fijo",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = FixedTermDeposit.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Response> createFixedTerm(@RequestBody @Valid FixedTermCreateRequestDTO dto, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        Response response = new Response<>();
        response.setData(fixedTermService.createFixedTermDeposit(dto,user));
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Simula un plazo fijo",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = FixedTermSimulateResponseDTO.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping("/simulate")
    public ResponseEntity<Response> simulateFixedTerm(@RequestBody @Valid FixedTermCreateRequestDTO dto){
        Response<FixedTermSimulateResponseDTO> response = new Response<>();
        response.setData(fixedTermService.simulateFixedTerm(dto));
        return ResponseEntity.ok(response);
    }
}