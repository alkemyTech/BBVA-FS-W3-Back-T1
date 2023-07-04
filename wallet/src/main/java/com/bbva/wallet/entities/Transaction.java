package com.bbva.wallet.entities;

import com.bbva.wallet.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@Builder
@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;

    @NotNull
    private LocalDateTime transactionDate;









}
