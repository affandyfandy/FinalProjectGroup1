package com.final_project_clinic.authentication.service.impl;

import com.final_project_clinic.authentication.data.model.Patient;
import com.final_project_clinic.authentication.data.model.Role;
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
import com.final_project_clinic.authentication.utils.JwtUtils;
import com.final_project_clinic.authentication.utils.PasswordUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

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
        String email = "test@example.com";
        String password = "password123";
        LoginRequestDTO loginRequest = new LoginRequestDTO(email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(PasswordUtils.hashPassword(password)); // Assuming password is hashed in the DB
        user.setId(UUID.randomUUID());
        user.setRole(Role.PATIENT.toString());

        when(userRepository.findUserByEmail(email)).thenReturn(user);
        when(jwtUtils.generateToken(email, user.getId(), user.getRole())).thenReturn("token");

        // Act
        LoginResponseDTO response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("token", response.getToken());
        verify(userRepository).findUserByEmail(email);
        verify(jwtUtils).generateToken(email, user.getId(), user.getRole());
    }

    @Test
    void testLoginUserNotFound() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("unknown@example.com", "password123");

        // Mock behavior
        when(userRepository.findUserByEmail(anyString())).thenReturn(null);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.login(loginRequest));
        assertEquals("User not found, Invalid email or password", exception.getMessage());
    }

    @Test
    void testLoginInvalidPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongPassword";
        LoginRequestDTO loginRequest = new LoginRequestDTO(email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(PasswordUtils.hashPassword("correctPassword"));

        when(userRepository.findUserByEmail(email)).thenReturn(user);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.login(loginRequest));
        assertEquals("User not found, Invalid email or password", exception.getMessage());
    }

    @Test
    void testRegisterSuccess() {
        // Arrange
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(1234567890L, "John Doe", "john@example.com", "Password123!", "PATIENT");
        User existingUser = null; // No existing user
        Patient existingPatient = null; // No existing patient

        when(userRepository.findUserByEmail(registerRequest.getEmail())).thenReturn(existingUser);
        when(patientRepository.findPatientByNik(registerRequest.getNik())).thenReturn(existingPatient);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return saved user

        // Act
        RegisterResponseDTO response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Registered Successfully", response.getMessage());
        assertEquals(registerRequest.getEmail(), response.getEmail());
        verify(patientRepository).save(any(Patient.class)); // Ensure patient is saved
    }

    @Test
    void testRegisterDuplicateEmail() {
        // Arrange
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(1234567890L, "John Doe", "john@example.com", "Password123!", "PATIENT");
        User existingUser = new User();
        existingUser.setEmail(registerRequest.getEmail());

        when(userRepository.findUserByEmail(registerRequest.getEmail())).thenReturn(existingUser);

        // Act & Assert
        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> authService.register(registerRequest));
        assertEquals("Email already exists: " + registerRequest.getEmail(), exception.getMessage());
    }

    @Test
    void testRegisterDuplicateNik() {
        // Arrange
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(1234567890L, "John Doe", "john@example.com", "Password123!", "PATIENT");
        User existingUser = null; // No existing user
        Patient existingPatient = new Patient();
        existingPatient.setNik(registerRequest.getNik());

        when(userRepository.findUserByEmail(registerRequest.getEmail())).thenReturn(existingUser);
        when(patientRepository.findPatientByNik(registerRequest.getNik())).thenReturn(existingPatient);

        // Act & Assert
        DuplicateNikException exception = assertThrows(DuplicateNikException.class, () -> authService.register(registerRequest));
        assertEquals("NIK already exists: " + registerRequest.getNik(), exception.getMessage());
    }

    @Test
    void testGetProfileSuccess() {
        // Arrange
        String jwtToken = "validToken";
        String email = "john@example.com";
        User user = new User();
        user.setFullName("John Doe");
        user.setEmail(email);
        user.setRole(Role.PATIENT.toString());
        user.setCreatedBy("admin");
        user.setCreatedTime(LocalDateTime.now());

        when(jwtUtils.extractEmail(jwtToken)).thenReturn(email);
        when(userRepository.findUserByEmail(email)).thenReturn(user);

        // Act
        ProfileResponseDTO profile = authService.getProfile(jwtToken);

        // Assert
        assertNotNull(profile);
        assertEquals("John Doe", profile.getFullName());
        assertEquals(email, profile.getEmail());
        assertEquals(Role.PATIENT.toString(), profile.getRole());
    }

    @Test
    void testGetProfileUserNotFound() {
        // Arrange
        String jwtToken = "invalidToken";
        when(jwtUtils.extractEmail(jwtToken)).thenReturn("unknown@example.com");
        when(userRepository.findUserByEmail("unknown@example.com")).thenReturn(null);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.getProfile(jwtToken));
        assertEquals("User not found, Invalid email or password", exception.getMessage());
    }
}
