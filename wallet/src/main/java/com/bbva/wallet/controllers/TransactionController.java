package com.bbva.wallet.controllers;


import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/sendArs")
    public void sendPesos(@RequestBody TransactionDto transactionDto) {
       transactionService.sendMoney(transactionDto);

    }

    @PostMapping("/sendUsd")
    public void sendDollars(@RequestBody TransactionDto transactionDto) {
        transactionService.sendMoney(transactionDto);

}
}