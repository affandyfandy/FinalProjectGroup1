package com.final_project_clinic.authentication.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProfileResponseDTOTest {

    @Test
    void testDefaultConstructor() {
        // Given
        ProfileResponseDTO dto = new ProfileResponseDTO();

        // Then
        assertNull(dto.getFullName());
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
        assertNull(dto.getRole());
        assertNull(dto.getCreatedBy());
        assertNull(dto.getCreatedTime());
        assertNull(dto.getUpdatedBy());
        assertNull(dto.getUpdatedTime());
    }

    @Test
    void testParameterizedConstructorAndGetters() {
        // Given
        String fullName = "John Doe";
        String email = "john.doe@gmail.com";
        String password = "SecurePass123!";
        String role = "Admin";
        String createdBy = "system";
        LocalDateTime createdTime = LocalDateTime.now();
        String updatedBy = "admin";
        LocalDateTime updatedTime = LocalDateTime.now();

        // When
        ProfileResponseDTO dto = new ProfileResponseDTO(fullName, email, password, role, createdBy, createdTime, updatedBy, updatedTime);

        // Then
        assertEquals(fullName, dto.getFullName());
        assertEquals(email, dto.getEmail());
        assertEquals(password, dto.getPassword());
        assertEquals(role, dto.getRole());
        assertEquals(createdBy, dto.getCreatedBy());
        assertEquals(createdTime, dto.getCreatedTime());
        assertEquals(updatedBy, dto.getUpdatedBy());
        assertEquals(updatedTime, dto.getUpdatedTime());
    }

    @Test
    void testSetters() {
        // Given
        ProfileResponseDTO dto = new ProfileResponseDTO();
        String fullName = "Jane Doe";
        String email = "jane.doe@gmail.com";
        String password = "NewSecurePass456!";
        String role = "User";
        String createdBy = "admin";
        LocalDateTime createdTime = LocalDateTime.now();
        String updatedBy = "admin";
        LocalDateTime updatedTime = LocalDateTime.now();

        // When
        dto.setFullName(fullName);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRole(role);
        dto.setCreatedBy(createdBy);
        dto.setCreatedTime(createdTime);
        dto.setUpdatedBy(updatedBy);
        dto.setUpdatedTime(updatedTime);

        // Then
        assertEquals(fullName, dto.getFullName());
        assertEquals(email, dto.getEmail());
        assertEquals(password, dto.getPassword());
        assertEquals(role, dto.getRole());
        assertEquals(createdBy, dto.getCreatedBy());
        assertEquals(createdTime, dto.getCreatedTime());
        assertEquals(updatedBy, dto.getUpdatedBy());
        assertEquals(updatedTime, dto.getUpdatedTime());
    }

    @Test
    void testEquals() {
        // Given
        ProfileResponseDTO dto1 = new ProfileResponseDTO("John Doe", "john.doe@gmail.com", "SecurePass123!", "Admin", "system", LocalDateTime.now(), "admin", LocalDateTime.now());
        ProfileResponseDTO dto2 = new ProfileResponseDTO("John Doe", "john.doe@gmail.com", "SecurePass123!", "Admin", "system", LocalDateTime.now(), "admin", LocalDateTime.now());
        ProfileResponseDTO dto3 = new ProfileResponseDTO("Jane Doe", "jane.doe@gmail.com", "DifferentPass123!", "User", "admin", LocalDateTime.now(), "admin", LocalDateTime.now());

        // Then
        assertEquals(dto1, dto2, "DTOs with same values should be equal");
        assertNotEquals(dto1, dto3, "DTOs with different values should not be equal");
        assertNotEquals(dto1, null, "DTO should not be equal to null");
    }

    @Test
    void testHashCode() {
        // Given
        ProfileResponseDTO dto1 = new ProfileResponseDTO("John Doe", "john.doe@gmail.com", "SecurePass123!", "Admin", "system", LocalDateTime.now(), "admin", LocalDateTime.now());
        ProfileResponseDTO dto2 = new ProfileResponseDTO("John Doe", "john.doe@gmail.com", "SecurePass123!", "Admin", "system", LocalDateTime.now(), "admin", LocalDateTime.now());

        // Then
        assertEquals(dto1.hashCode(), dto2.hashCode(), "DTOs with same values should have the same hash code");
    }

    @Test
    void testToString() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        ProfileResponseDTO dto = new ProfileResponseDTO("John Doe", "john.doe@example.com", "SecurePass123!", "Admin", "system", now, "admin", now);

        // When
        String expectedString = "ProfileResponseDTO(fullName=John Doe, email=john.doe@example.com, password=SecurePass123!, role=Admin, createdBy=system, createdTime=" + now + ", updatedBy=admin, updatedTime=" + now + ")";
        String actualString = dto.toString();

        // Then
        assertEquals(expectedString, actualString, "toString() should return the expected string representation");
    }
}
