package com.final_project_clinic.authentication.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        // Optional: Cleanup resources if needed
    }

    @Test
    void whenValidDatathenNoConstraintViolations() {
        RegisterRequestDTO dto = new RegisterRequestDTO(3174123412341234L, "John Doe", "john.doe@example.com", "SecurePass123!", "User");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "There should be no constraint violations for valid data");
    }

    @Test
    void whenNullNIKthenConstraintViolation() {
        RegisterRequestDTO dto = new RegisterRequestDTO(null, "John Doe", "john.doe@example.com", "SecurePass123!", "User");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "There should be one constraint violation for NIK");
        assertEquals("NIK cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    void whenInvalidFullNamethenConstraintViolation() {
        RegisterRequestDTO dto = new RegisterRequestDTO(3174123412341234L, "John123", "john.doe@example.com", "SecurePass123!", "User");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "There should be one constraint violation for fullName");
        assertEquals("Name can only contain letters and spaces", violations.iterator().next().getMessage());
    }

    @Test
    void whenBlankEmailthenConstraintViolation() {
        RegisterRequestDTO dto = new RegisterRequestDTO(3174123412341234L, "John Doe", "", "SecurePass123!", "User");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "There should be one constraint violation for email");
        assertEquals("Email is mandatory", violations.iterator().next().getMessage());
    }

    @Test
    void whenInvalidEmailthenConstraintViolation() {
        RegisterRequestDTO dto = new RegisterRequestDTO(3174123412341234L, "John Doe", "invalid-email", "SecurePass123!", "User");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "There should be one constraint violation for email");
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    @Test
    void whenShortPasswordthenConstraintViolation() {
        RegisterRequestDTO dto = new RegisterRequestDTO(123456789L, "John Doe", "john.doe@example.com", "short", "User");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertEquals(2, violations.size(), "There should be two constraint violations for short password");

        // Collect the violation messages
        Set<String> violationMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        // Check that both expected messages are present
        assertTrue(violationMessages.contains("Password must be at least 12 characters long"));
        assertTrue(violationMessages.contains("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"));
    }

    @Test
    void whenInvalidPasswordthenConstraintViolation() {
        RegisterRequestDTO dto = new RegisterRequestDTO(123456789L, "John Doe", "john.doe@example.com", "Password123`", "User");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size(), "There should be two constraint violations for short password");

        // Collect the violation message
        String violationMessage = violations.iterator().next().getMessage();

        // Check the expected message for password complexity
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character", violationMessage);
    }

    @Test
    void whenValidRolethenNoConstraintViolations() {
        RegisterRequestDTO dto = new RegisterRequestDTO(123456789L, "John Doe", "john.doe@example.com", "SecurePass123!", "Admin");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "There should be no constraint violations for valid role");
    }
}
