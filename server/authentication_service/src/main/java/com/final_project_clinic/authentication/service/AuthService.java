package com.final_project_clinic.authentication.service;

import com.final_project_clinic.authentication.dto.LoginRequestDTO;
import com.final_project_clinic.authentication.dto.LoginResponseDTO;
import com.final_project_clinic.authentication.dto.ProfileResponseDTO;
import com.final_project_clinic.authentication.dto.RegisterRequestDTO;
import com.final_project_clinic.authentication.dto.RegisterResponseDTO;
import com.final_project_clinic.authentication.exception.AuthException;

public interface AuthService {

    // Authenticate user based on given user data
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws AuthException;

    // Save the user to the database
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);

    ProfileResponseDTO getProfile(String jwtToken);
}
