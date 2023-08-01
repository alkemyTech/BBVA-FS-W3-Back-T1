package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.Transaction;

public record createFixedTermDepositResponseDTO (FixedTermDeposit savedFixedTerm, Transaction transactionOfFixedTerm) {
}
