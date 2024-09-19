package com.final_project_clinic.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class UserSaveDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserSaveDTO() {
        UserSaveDTO userSaveDTO = new UserSaveDTO(
                "John Doe",
                "john.doe@example.com",
                "ValidPassw0rd!",
                "USER"
        );

        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);
        assertTrue(violations.isEmpty(), "Expected no constraint violations");
    }

    @Test
    void testInvalidFullName() {
        UserSaveDTO userSaveDTO = new UserSaveDTO(
                "John123",
                "john.doe@example.com",
                "ValidPassw0rd!",
                "USER"
        );

        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);
        assertFalse(violations.isEmpty(), "Expected constraint violations for invalid full name");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Name can only contain letters and spaces")));
    }

    @Test
    void testInvalidEmail() {
        UserSaveDTO userSaveDTO = new UserSaveDTO(
                "John Doe",
                "invalid-email",
                "ValidPassw0rd!",
                "USER"
        );

        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);
        assertFalse(violations.isEmpty(), "Expected constraint violations for invalid email");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    void testInvalidPassword() {
        UserSaveDTO userSaveDTO = new UserSaveDTO(
                "John Doe",
                "john.doe@example.com",
                "short",
                "USER"
        );

        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);
        assertFalse(violations.isEmpty(), "Expected constraint violations for invalid password");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password must be at least 12 characters long")));
    }

    @Test
    void testPasswordWithoutSpecialCharacter() {
        UserSaveDTO userSaveDTO = new UserSaveDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "USER"
        );

        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);
        assertFalse(violations.isEmpty(), "Expected constraint violations for password missing special character");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")));
    }

    @Test
    void testEmptyFullName() {
        UserSaveDTO userSaveDTO = new UserSaveDTO(
                "",
                "john.doe@example.com",
                "ValidPassw0rd!",
                "USER"
        );

        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);
        assertFalse(violations.isEmpty(), "Expected constraint violations for empty full name");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Fullname is mandatory")));
    }

    @Test
    void testEmptyEmail() {
        UserSaveDTO userSaveDTO = new UserSaveDTO(
                "John Doe",
                "",
                "ValidPassw0rd!",
                "USER"
        );

        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);
        assertFalse(violations.isEmpty(), "Expected constraint violations for empty email");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email is mandatory")));
    }

    @Test
    void testEmptyPassword() {
        UserSaveDTO userSaveDTO = new UserSaveDTO(
                "John Doe",
                "john.doe@example.com",
                "",
                "USER"
        );

        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);
        assertFalse(violations.isEmpty(), "Expected constraint violations for empty password");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password is required")));
    }
}
