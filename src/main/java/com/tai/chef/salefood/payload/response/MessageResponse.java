package com.tai.chef.salefood.payload.response;

import lombok.Data;

@Data
public class MessageResponse {
    private String message;
    private String login;
    private String token;
    private boolean error;

    public MessageResponse(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    public MessageResponse(String message, String login, boolean error) {
        this.login = login;
        this.message = message;
        this.error = error;
    }
}
