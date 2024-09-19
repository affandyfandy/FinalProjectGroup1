package com.final_project_clinic.authentication.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterResponseDTOTest {

    @Test
    void testDefaultConstructor() {
        // Given
        RegisterResponseDTO dto = new RegisterResponseDTO();

        // Then
        assertEquals("Registered Successfully", dto.getMessage());
        assertNull(dto.getNik());
        assertNull(dto.getFullName());
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
        assertNull(dto.getRole());
    }

    @Test
    void testParameterizedConstructorAndGetters() {
        // Given
        Long nik = 123456789L;
        String fullName = "John Doe";
        String email = "john.doe@example.com";
        String password = "SecurePass123!";
        String role = "User";

        // When
        RegisterResponseDTO dto = new RegisterResponseDTO("Registration Successful", nik, fullName, email, password, role);

        // Then
        assertEquals("Registration Successful", dto.getMessage());
        assertEquals(nik, dto.getNik());
        assertEquals(fullName, dto.getFullName());
        assertEquals(email, dto.getEmail());
        assertEquals(password, dto.getPassword());
        assertEquals(role, dto.getRole());
    }

    @Test
    void testSetters() {
        // Given
        RegisterResponseDTO dto = new RegisterResponseDTO();
        Long nik = 987654321L;
        String fullName = "Jane Doe";
        String email = "jane.doe@example.com";
        String password = "NewSecurePass456!";
        String role = "Admin";

        // When
        dto.setNik(nik);
        dto.setFullName(fullName);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRole(role);

        // Then
        assertEquals(nik, dto.getNik());
        assertEquals(fullName, dto.getFullName());
        assertEquals(email, dto.getEmail());
        assertEquals(password, dto.getPassword());
        assertEquals(role, dto.getRole());
    }

    @Test
    void testToString() {
        // Given
        RegisterResponseDTO dto = new RegisterResponseDTO("Registration Successful", 123456789L, "John Doe", "john.doe@example.com", "SecurePass123!", "User");

        // When
        String expectedString = "RegisterResponseDTO(message=Registration Successful, nik=123456789, fullName=John Doe, email=john.doe@example.com, password=SecurePass123!, role=User)";
        String actualString = dto.toString();

        // Then
        assertEquals(expectedString, actualString, "toString() should return the expected string representation");
    }
}
