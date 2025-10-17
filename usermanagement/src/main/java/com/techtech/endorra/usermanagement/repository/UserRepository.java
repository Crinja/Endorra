package com.techtech.endorra.usermanagement.repository;

import com.techtech.endorra.usermanagement.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> 
{
    
}
