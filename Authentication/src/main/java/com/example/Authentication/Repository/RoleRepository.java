package com.example.Authentication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Authentication.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}