package com.tai.chef.salefood.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "sale_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Column(name = "user_name")
    private String username;

    @NotBlank
    @Email
    @Column(name = "user_email")
    private String email;

    @NotBlank
    @Column(name = "user_phone")
    private String phone;

    @NotBlank
    @Column(name = "user_address")
    private String address;

    @NotBlank
    @Column(name = "user_secret")
    private String userSecret;

    @NotBlank
    @Column(name = "user_password")
    private String password;

    @Column(name = "user_balance")
    private BigDecimal balance;

    @Column(name = "user_locked")
    private Boolean userLocked;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
