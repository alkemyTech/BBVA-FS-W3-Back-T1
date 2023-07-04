package com.bbva.wallet.entities;

import com.bbva.wallet.enums.EnumRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private EnumRole name;

    private String description;

    @Column(name = "creation_date",nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date",nullable = false)
    private LocalDateTime updateDate;

    public Role(EnumRole role) {
        this.name = role;
    }


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
