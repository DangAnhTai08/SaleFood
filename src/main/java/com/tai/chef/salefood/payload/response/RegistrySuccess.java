package com.tai.chef.salefood.payload.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegistrySuccess {
    private String message;
    private String login;
    private String token;
}
