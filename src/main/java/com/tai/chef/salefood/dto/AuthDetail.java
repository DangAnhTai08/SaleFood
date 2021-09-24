package com.tai.chef.salefood.dto;

import com.tai.chef.salefood.security.services.UserDetailsImpl;
import lombok.Data;

@Data
public class AuthDetail {
    private String username;
    private Long userId;
    private String email;

    public AuthDetail(UserDetailsImpl userDetail) {
        this.username = userDetail.getUsername();
        this.userId = userDetail.getId();
        this.email = userDetail.getEmail();
    }
}
