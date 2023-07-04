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
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @NotNull
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


}
