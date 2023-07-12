package com.bbva.wallet.hateoas;

import com.bbva.wallet.entities.User;
import org.springframework.hateoas.RepresentationModel;
public class UserModel extends RepresentationModel<UserModel> {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    // Constructor

    public UserModel() {
    }
    public UserModel(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
