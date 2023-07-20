package com.bbva.wallet.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionPatchDescriptionRequestDTO {
    private String description;
}
