package com.bbva.wallet.entities;

import com.bbva.wallet.enums.Currencies;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Collection;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter@Getter
@SQLDelete(sql = "UPDATE users SET soft_delete = true WHERE id=?")
@Entity
@Table(name = "users")
public class User implements UserDetails,Serializable {

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

    @JsonIgnore
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Account> accountList;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role roleId;

    @JsonIgnore
    @Column(name = "soft_delete", nullable = false ,columnDefinition = "boolean default false")
    private boolean softDelete;

    @JsonIgnore
    @Column(name = "creation_date",nullable = false)
    private LocalDateTime creationDate;

    @JsonIgnore
    @Column(name = "update_date",nullable = false)
    private LocalDateTime updateDate;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
        this.updateDate = this.creationDate;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.roleId.getName().name()));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return !this.softDelete;
    }

    @PreRemove
    public void deleteUser(){
        this.softDelete = true;
    }

    public Optional<Account> hasThisCurrencyAccount(Currencies currency) {
        return this.accountList.stream()
                .filter(acc-> acc.getCurrency().equals(currency))
                .findFirst();
    }

}
