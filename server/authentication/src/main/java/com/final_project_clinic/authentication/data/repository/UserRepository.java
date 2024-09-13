package com.final_project_clinic.authentication.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.final_project_clinic.authentication.data.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Find User based on his/her email
    User findByEmail(String email);
}
