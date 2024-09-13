package com.final_project_clinic.authentication.service;

import com.final_project_clinic.authentication.data.model.User;
import com.final_project_clinic.authentication.exception.AuthException;

public interface AuthService {

    // Find user by its email
    User findByEmail(String email);

    // Authenticate user based on given user data
    void login(String email, String password) throws AuthException;

    // Save the user to the database
    void register(User user);
}
