package com.final_project_clinic.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.final_project_clinic.authentication.utils.JwtUtils;
import com.final_project_clinic.authentication.dto.LoginRequestDTO;
import com.final_project_clinic.authentication.dto.LoginResponseDTO;
import com.final_project_clinic.authentication.dto.ProfileResponseDTO;
import com.final_project_clinic.authentication.dto.RegisterRequestDTO;
import com.final_project_clinic.authentication.dto.RegisterResponseDTO;
import com.final_project_clinic.authentication.service.AuthService;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/authentication")
@Validated
public class AuthenticationController {

    private final JwtUtils jwtUtil;
    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService, JwtUtils jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody @Valid LoginRequestDTO authRequest) {
        // Call the single service method to handle login and token generation
        LoginResponseDTO response = authService.login(authRequest);

        // Return JWT token in the response
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody @Valid RegisterRequestDTO request) {
        // Call the register method in the service
        RegisterResponseDTO response = authService.register(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseDTO> getProfile(@RequestHeader("Authorization") String token) {
        // Strip "Bearer " prefix from token
        String jwtToken = token.replace("Bearer ", "");
        ProfileResponseDTO profile = authService.getProfile(jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }
}
