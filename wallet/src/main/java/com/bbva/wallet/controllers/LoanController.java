package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.LoanDto;
import com.bbva.wallet.dtos.LoanRequestBodyDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.services.LoanService;
import com.bbva.wallet.utils.Response;
import com.bbva.wallet.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "Loan")
@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Operation(
            description = "Endpoint accesible a usuarios autenticados",
            summary = "Simula un préstamo",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = LoanDto.class), mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping("/simulate")
    public ResponseEntity<Response>simulateLoan(@RequestBody LoanRequestBodyDto loanRequestBodyDto){
        Response<LoanDto> response = new Response<>();
        response.setData(loanService.simulateLoan(loanRequestBodyDto.getAmount(), loanRequestBodyDto.getMonths()));
        return ResponseEntity.ok(response);
    }
}
