package com.bbva.wallet.entities;
import com.bbva.wallet.enums.Currencies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Random;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
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

    @ManyToOne
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
    @Column(nullable = false ,columnDefinition = "boolean default false")
    private Boolean softDelete;

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
