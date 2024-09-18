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
import com.final_project_clinic.authentication.service.AuthService;
import com.final_project_clinic.authentication.utils.JwtUtils;
import com.final_project_clinic.authentication.utils.PasswordUtils;
import com.final_project_clinic.authentication.exception.AuthException;
import com.final_project_clinic.authentication.exception.DuplicateEmailException;
import com.final_project_clinic.authentication.exception.DuplicateNikException;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    final JwtUtils jwtUtils;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PatientRepository patientRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Patient findPatientByNik(Long nik) {
        return patientRepository.findPatientByNik(nik);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws AuthException {
        // Find the user by email
        User user = userRepository.findUserByEmail(loginRequestDTO.getEmail());
        if (user == null) {
            throw new AuthException("User not found, Invalid email or password");
        }

        // Verify the password
        if (!PasswordUtils.verifyPassword(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new AuthException("User not found, Invalid email or password");
        }

        // Generate JWT token if authentication succeeds
        String token = jwtUtils.generateToken(user.getEmail(), user.getId(), user.getRole());

        // Return the response containing the JWT token
        return new LoginResponseDTO(token);
    }

    @Override
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        // Check if the email already exists
        User existingUser = userRepository.findUserByEmail(registerRequestDTO.getEmail());
        if (existingUser != null) {
            throw new DuplicateEmailException("Email already exists: " + registerRequestDTO.getEmail());
        }

        // Check if the NIK already exists
        Patient existingPatient = patientRepository.findPatientByNik(registerRequestDTO.getNik());
        if (existingPatient != null) {
            throw new DuplicateNikException("NIK already exists: " + registerRequestDTO.getNik());
        }

        // Create new User object
        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setFull_name(registerRequestDTO.getFull_name());
        user.setPassword(PasswordUtils.hashPassword(registerRequestDTO.getPassword())); // Hash the password
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        user.setCreatedBy(registerRequestDTO.getEmail());

        // Set the role (default to 'PATIENT' if no role is provided)
        String role = (registerRequestDTO.getRole() != null && !registerRequestDTO.getRole().isEmpty())
                ? registerRequestDTO.getRole()
                : Role.PATIENT.toString();
        user.setRole(role);

        // Save the user
        User savedUser = userRepository.save(user);

        // If the user is an ADMIN, skip creating the patient record
        if ("ADMIN".equals(savedUser.getRole())) {
            return new RegisterResponseDTO(
                    "Registered Successfully",
                    null,
                    savedUser.getFull_name(),
                    savedUser.getEmail(),
                    null,
                    savedUser.getRole());
        }

        // Create Patient object and set the NIK
        Patient patient = new Patient();
        patient.setNik(registerRequestDTO.getNik()); // Set the NIK from the request DTO
        patient.setUser_id(savedUser.getId());
        patient.setCreatedBy(savedUser.getEmail());
        patient.setCreatedTime(LocalDateTime.now());

        // Save the patient
        patientRepository.save(patient);
        return new RegisterResponseDTO(
                "Registered Successfully",
                patient.getNik(),
                savedUser.getFull_name(),
                savedUser.getEmail(),
                null, // Don't include the password in the response for security reasons
                savedUser.getRole());
    }

    @Override
    public ProfileResponseDTO getProfile(String jwtToken) {
        // Extract user ID from the token
        String email = jwtUtils.extractEmail(jwtToken);

        // Fetch user from database by ID
        User user = userRepository.findUserByEmail(email);

        // Return profile data as ProfileResponseDTO
        return new ProfileResponseDTO(
                user.getFull_name(),
                user.getEmail(),
                null, // We don't return password for security reasons
                user.getRole(),
                user.getCreatedBy(),
                user.getCreatedTime(),
                user.getUpdatedBy(),
                user.getUpdatedTime());
    }

}
