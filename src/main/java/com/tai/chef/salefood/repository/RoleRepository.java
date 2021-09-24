package com.tai.chef.salefood.repository;

import com.tai.chef.salefood.models.ERole;
import com.tai.chef.salefood.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
