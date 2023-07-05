package com.bbva.wallet.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.bbva.wallet.enums.TransactionType;





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
    @CreationTimestamp
    private LocalDateTime transactionDate;

}
