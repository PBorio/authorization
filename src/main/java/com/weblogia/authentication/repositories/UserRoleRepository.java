package com.weblogia.authentication.repositories;

import com.weblogia.authentication.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(String roleName);
}
