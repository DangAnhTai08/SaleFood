package com.tai.chef.salefood.payload.response;

import lombok.Data;

@Data
public class MessageResponse {
    private String message;
    private String login;
    private String token;

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, String login) {
        this.login = login;
        this.message = message;
    }
}
