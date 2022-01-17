package com.tai.chef.salefood.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OTPRequest {
    @NotBlank
    private String totp;

    @NotBlank
    private String login;

    @NotBlank
    private String token;

    @NotBlank
    private Double price;
}
