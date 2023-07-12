package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePaymentDto {
    private Account updatedAccount;
    private Transaction transactionPayment;
}
