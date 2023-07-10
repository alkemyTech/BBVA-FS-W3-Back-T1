package com.bbva.wallet.dtos.balanceDto;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.Transaction;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceDto {
   private Account accountArs;
   private Account accountUsd;
   private List<Transaction> historyArs;
   private List<Transaction> historyUsd;
   private List<FixedTermDeposit> fixedTerms;
}
