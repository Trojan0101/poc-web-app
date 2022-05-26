package com.assesment.poc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "login_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDetails {

    @Id
    @NotBlank
    private String email;
    @Column(name = "is_logged_in")
    private boolean isLoggedIn;

}
