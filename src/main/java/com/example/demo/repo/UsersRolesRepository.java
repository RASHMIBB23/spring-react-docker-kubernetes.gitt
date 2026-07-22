package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.data.UserRoleId;
import com.example.demo.data.Users_Roles;

public interface UsersRolesRepository extends JpaRepository<Users_Roles, UserRoleId> {
    
}
