package com.final_project_clinic.authentication.service;

import com.final_project_clinic.authentication.data.model.User;
import com.final_project_clinic.authentication.data.repository.PatientRepository;
import com.final_project_clinic.authentication.data.repository.UserRepository;
import com.final_project_clinic.authentication.dto.LoginRequestDTO;
import com.final_project_clinic.authentication.dto.LoginResponseDTO;
import com.final_project_clinic.authentication.dto.ProfileResponseDTO;
import com.final_project_clinic.authentication.dto.RegisterRequestDTO;
import com.final_project_clinic.authentication.dto.RegisterResponseDTO;
import com.final_project_clinic.authentication.exception.AuthException;
import com.final_project_clinic.authentication.exception.DuplicateEmailException;
import com.final_project_clinic.authentication.exception.DuplicateNikException;
import com.final_project_clinic.authentication.service.impl.AuthServiceImpl;
import com.final_project_clinic.authentication.utils.JwtUtils;
import com.final_project_clinic.authentication.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() throws AuthException {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("email@gmail.com", "password");
        User user = new User();
        user.setEmail("email@gmail.com");
        user.setPassword(PasswordUtils.hashPassword("password"));
        user.setId(UUID.randomUUID());
        user.setRole("PATIENT");

        // Mock behavior
        when(userRepository.findUserByEmail(loginRequest.getEmail())).thenReturn(user);
        when(jwtUtils.generateToken(user.getEmail(), user.getId(), user.getRole())).thenReturn("token");

        // Act
        LoginResponseDTO response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("token", response.getToken());
    }

    @Test
    void testLoginFailure() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("email@gmail.com", "wrongPassword");

        // Mock behavior
        when(userRepository.findUserByEmail(loginRequest.getEmail())).thenReturn(null);

        // Act & Assert
        assertThrows(AuthException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testRegister() {
        // Arrange
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(3741123412343321L, "Username", "email@gmail.com", "Password123!", null);
        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setFullName(registerRequest.getFullName());
        newUser.setPassword(PasswordUtils.hashPassword(registerRequest.getPassword()));
        newUser.setRole("PATIENT");
        newUser.setCreatedTime(LocalDateTime.now());
        newUser.setCreatedBy(registerRequest.getEmail());

        // Mock behavior
        when(userRepository.findUserByEmail(registerRequest.getEmail())).thenReturn(null);
        when(patientRepository.findPatientByNik(registerRequest.getNik())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        RegisterResponseDTO response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Registered Successfully", response.getMessage());
        assertEquals(registerRequest.getFullName(), response.getFullName());
    }

    @Test
    void testRegisterDuplicateEmail() {
        // Arrange
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(3741123412343321L, "Username", "email@gmail.com", "Password123!", null);
        User existingUser = new User();
        existingUser.setEmail(registerRequest.getEmail());

        // Mock behavior
        when(userRepository.findUserByEmail(registerRequest.getEmail())).thenReturn(existingUser);

        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> authService.register(registerRequest));
    }

    @Test
    void testGetProfile() {
        // Arrange
        String jwtToken = "someToken";
        String email = "email@gmail.com";
        User user = new User();
        user.setFullName("Username");
        user.setEmail(email);
        user.setRole("PATIENT");
        user.setCreatedBy(email);
        user.setCreatedTime(LocalDateTime.now());

        // Mock behavior
        when(jwtUtils.extractEmail(jwtToken)).thenReturn(email);
        when(userRepository.findUserByEmail(email)).thenReturn(user);

        // Act
        ProfileResponseDTO profile = authService.getProfile(jwtToken);

        // Assert
        assertNotNull(profile);
        assertEquals("Username", profile.getFullName());
    }
}
