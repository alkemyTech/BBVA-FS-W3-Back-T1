package com.bbva.wallet.entities;
import com.bbva.wallet.enums.Currencies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@SQLDelete(sql = "UPDATE accounts SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
@Table (name = "accounts")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currencies currency;

    @NotNull
    private Double transactionLimit;

    @NotNull
    @Column(columnDefinition = "double default 0.0")
    private Double balance;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userId;

    @NotNull
    @NotBlank
    @Column (unique = true)
    private String cbu;

    @CreationTimestamp
    @Column
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @Column
    private LocalDateTime updateDate;

    @JsonIgnore
    @Column(name = "soft_delete", nullable = false ,columnDefinition = "boolean default false")
    private boolean softDelete;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> transaction;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FixedTermDeposit> fixedTermDeposits;

    private static final int CBU_LENGTH = 22;


    @PrePersist
    protected void onCreate(){
        generateCBU();
    }
    public void generateCBU() {
        Random random = new Random();
        StringBuilder cbuBuilder = new StringBuilder();

        for (int i = 0; i < CBU_LENGTH; i++) {
            int digit = random.nextInt(10);
            cbuBuilder.append(digit);
        }

        this.cbu = cbuBuilder.toString();
    }
}
