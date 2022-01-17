package com.tai.chef.salefood.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users_roles")
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "user_id")
    private Integer userId;
}