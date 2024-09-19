package com.final_project_clinic.authentication.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

class LoginRequestDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidLoginRequestDTO_thenNoConstraintViolations() {
        LoginRequestDTO dto = new LoginRequestDTO("test@example.com", "ValidP@ssw0rd123");
        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "There should be no constraint violations");
    }

    @Test
    void whenInvalidEmail_thenConstraintViolation() {
        LoginRequestDTO dto = new LoginRequestDTO("invalid-email", "ValidP@ssw0rd123");
        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

//    @Test
//    void whenPasswordViolations_thenConstraintViolation() {
//        // Test case 1: Short password
//        LoginRequestDTO shortPasswordDTO = new LoginRequestDTO("test@example.com", "short");
//        Set<ConstraintViolation<LoginRequestDTO>> shortPasswordViolations = validator.validate(shortPasswordDTO);
//
//        // Ensure there are two violations: one for length and one for complexity
//        assertEquals(2, shortPasswordViolations.size(), "There should be two constraint violations for short password");
//
//        // Collect violation messages for short password
//        Set<String> shortPasswordViolationMessages = shortPasswordViolations.stream()
//                .map(ConstraintViolation::getMessage)
//                .collect(Collectors.toSet());
//
//        // Check that expected messages for short password are present
//        assertTrue(shortPasswordViolationMessages.contains("Password must be at least 12 characters long"));
//        assertTrue(shortPasswordViolationMessages.contains("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"));
//
//        // Test case 2: Invalid password (missing complexity)
//        LoginRequestDTO invalidPasswordDTO = new LoginRequestDTO("test@example.com", "abcdefghij");
//        Set<ConstraintViolation<LoginRequestDTO>> invalidPasswordViolations = validator.validate(invalidPasswordDTO);
//
//        // Ensure there are two violations: one for length and one for complexity
//        assertEquals(2, invalidPasswordViolations.size(), "There should be two constraint violations for invalid password");
//
//        // Collect violation messages for invalid password
//        Set<String> invalidPasswordViolationMessages = invalidPasswordViolations.stream()
//                .map(ConstraintViolation::getMessage)
//                .collect(Collectors.toSet());
//
//        // Check that expected messages for invalid password are present
//        assertTrue(invalidPasswordViolationMessages.contains("Password must be at least 12 characters long"));
//        assertTrue(invalidPasswordViolationMessages.contains("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"));
//    }



    @Test
    void whenWeakPassword_thenConstraintViolation() {
        LoginRequestDTO dto = new LoginRequestDTO("test@example.com", "weakpassword");
        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character", violations.iterator().next().getMessage());
    }
}
