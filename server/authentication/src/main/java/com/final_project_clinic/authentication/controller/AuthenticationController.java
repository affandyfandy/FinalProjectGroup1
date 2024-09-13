package com.final_project_clinic.authentication.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.final_project_clinic.authentication.utils.JwtUtils;
import com.final_project_clinic.authentication.utils.PasswordUtils;
import com.final_project_clinic.authentication.data.model.User;
import com.final_project_clinic.authentication.dto.LoginRequestDTO;
import com.final_project_clinic.authentication.dto.LoginResponseDTO;
import com.final_project_clinic.authentication.dto.RegisterRequestDTO;
import com.final_project_clinic.authentication.exception.AuthException;
import com.final_project_clinic.authentication.exception.DuplicateEmailException;
import com.final_project_clinic.authentication.service.AuthService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    private final JwtUtils jwtUtil;
    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService, JwtUtils jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(@RequestBody LoginRequestDTO authRequest) {
        User user = authService.findByEmail(authRequest.getEmail()); // Custom method to find user by username
        System.out.println("Data " + user);
        if (user == null) {
            throw new AuthException("User not found, Please input a valid email");
        }

        if (!PasswordUtils.verifyPassword(authRequest.getPassword(), user.getPassword())) {
            throw new AuthException("User not found, Please input a valid email");
        }
        // Authenticate the user first
        authService.login(authRequest.getEmail(), authRequest.getPassword());

        // Generate JWT token
        final String jwt = jwtUtil.generateToken(authRequest.getEmail(), user.getId(),user.getRole());

        // Return the token as a response
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO request) {
        try {
            User newUser = new User();
            newUser.setId(UUID.randomUUID());
            newUser.setFull_name(request.getFull_name());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(request.getPassword());
            String role = (request.getRole() != null && !request.getRole().isEmpty()) ? request.getRole() : "Patient";
            newUser.setRole(role);
            // Register the new user
            authService.register(newUser);

            return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new DuplicateEmailException("Email already exists: " + request.getEmail());
            }
            throw e; // Rethrow other exceptions to be handled globally
        }
    }

}
