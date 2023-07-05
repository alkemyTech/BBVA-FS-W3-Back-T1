package com.bbva.wallet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fixed_term_deposits")
public class FixedTermDeposit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private Double interest;

    @CreationTimestamp
    private Timestamp creationDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp closingDate;
}
