package com.tai.chef.salefood.payload.response;

import com.tai.chef.salefood.security.services.UserDetailsImpl;
import lombok.Data;

import java.util.List;

@Data
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private final List<String> roles;
    private boolean error;

    public AuthResponse(String accessToken,
                        UserDetailsImpl userDetail,
                        List<String> roles,
                        boolean error
    ) {
        this.token = accessToken;
        this.username = userDetail.getUsername();
        this.email = userDetail.getEmail();
        this.roles = roles;
        this.error = error;
    }
}
