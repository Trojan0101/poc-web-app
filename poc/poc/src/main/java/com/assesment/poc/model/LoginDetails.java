package com.assesment.poc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "login_details")
public class LoginDetails {

    @Id
    @NotBlank
    private String email;
    @Column(name = "is_logged_in")
    private boolean isLoggedIn;

    public LoginDetails() {
    }

    public LoginDetails(String email, boolean isLoggedIn) {
        this.email = email;
        this.isLoggedIn = isLoggedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
