package com.bbva.wallet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter@Getter
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role roleId;
    @JsonIgnore
    @Column(name = "soft_delete", nullable = false ,columnDefinition = "boolean default false")
    private boolean softDelete;
    @JsonIgnore
    @Column(name = "creation_date",nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date",nullable = false)
    private LocalDateTime updateDate;

    @JsonIgnore
    @OneToMany (mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Account> accounts;


    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
        this.updateDate = this.creationDate;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

}
