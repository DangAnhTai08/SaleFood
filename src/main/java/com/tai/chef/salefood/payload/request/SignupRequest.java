package com.tai.chef.salefood.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Email
    private String phone;

    @NotBlank
    @Email
    private String address;

    private Set<String> role;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;
}
