package com.final_project_clinic.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class UserDTOTest {

    private final ObjectMapper objectMapper;

    public UserDTOTest() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testNoArgsConstructor() {
        UserDTO userDTO = new UserDTO();
        assertNotNull(userDTO);
        assertNull(userDTO.getId());
        assertNull(userDTO.getFullName());
        assertNull(userDTO.getEmail());
        assertNull(userDTO.getRole());
        assertNull(userDTO.getCreatedTime());
        assertNull(userDTO.getUpdatedTime());
        assertNull(userDTO.getCreatedBy());
        assertNull(userDTO.getUpdatedBy());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        String fullName = "John Doe";
        String email = "john.doe@example.com";
        String role = "USER";
        LocalDateTime createdTime = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedTime = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "admin";

        UserDTO userDTO = new UserDTO(id, fullName, email, role, createdTime, updatedTime, createdBy, updatedBy);

        assertAll("UserDTO All Args Constructor",
                () -> assertEquals(id, userDTO.getId()),
                () -> assertEquals(fullName, userDTO.getFullName()),
                () -> assertEquals(email, userDTO.getEmail()),
                () -> assertEquals(role, userDTO.getRole()),
                () -> assertEquals(createdTime, userDTO.getCreatedTime()),
                () -> assertEquals(updatedTime, userDTO.getUpdatedTime()),
                () -> assertEquals(createdBy, userDTO.getCreatedBy()),
                () -> assertEquals(updatedBy, userDTO.getUpdatedBy())
        );
    }

    @Test
    void testSettersAndGetters() {
        UserDTO userDTO = new UserDTO();

        UUID id = UUID.randomUUID();
        String fullName = "Jane Smith";
        String email = "jane.smith@example.com";
        String role = "ADMIN";
        LocalDateTime createdTime = LocalDateTime.now().minusDays(2);
        LocalDateTime updatedTime = LocalDateTime.now().minusHours(1);
        String createdBy = "system";
        String updatedBy = "system";

        userDTO.setId(id);
        userDTO.setFullName(fullName);
        userDTO.setEmail(email);
        userDTO.setRole(role);
        userDTO.setCreatedTime(createdTime);
        userDTO.setUpdatedTime(updatedTime);
        userDTO.setCreatedBy(createdBy);
        userDTO.setUpdatedBy(updatedBy);

        assertAll("UserDTO Setters and Getters",
                () -> assertEquals(id, userDTO.getId()),
                () -> assertEquals(fullName, userDTO.getFullName()),
                () -> assertEquals(email, userDTO.getEmail()),
                () -> assertEquals(role, userDTO.getRole()),
                () -> assertEquals(createdTime, userDTO.getCreatedTime()),
                () -> assertEquals(updatedTime, userDTO.getUpdatedTime()),
                () -> assertEquals(createdBy, userDTO.getCreatedBy()),
                () -> assertEquals(updatedBy, userDTO.getUpdatedBy())
        );
    }

    @Test
    void testJsonSerialization() throws Exception {
        UUID id = UUID.randomUUID();
        String fullName = "John Doe";
        String email = "john.doe@example.com";
        String role = "USER";
        LocalDateTime createdTime = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedTime = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "admin";

        UserDTO userDTO = new UserDTO(id, fullName, email, role, createdTime, updatedTime, createdBy, updatedBy);

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(userDTO);
        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"" + id.toString() + "\""));
        assertTrue(json.contains("\"fullName\":\"" + fullName + "\""));
        assertTrue(json.contains("\"email\":\"" + email + "\""));
        assertTrue(json.contains("\"role\":\"" + role + "\""));
        assertFalse(json.contains("\"createdTime\":\"" + createdTime.toString() + "\""));
        assertFalse(json.contains("\"updatedTime\":\"" + updatedTime.toString() + "\""));
        assertTrue(json.contains("\"createdBy\":\"" + createdBy + "\""));
        assertTrue(json.contains("\"updatedBy\":\"" + updatedBy + "\""));
    }

    @Test
    void testJsonDeserialization() throws Exception {
        String json = "{\"id\":\"e2f8f41e-87b8-4f3b-b4b1-8e383cbb6d2a\",\"fullName\":\"John Doe\",\"email\":\"john.doe@example.com\",\"role\":\"USER\",\"createdTime\":\"2024-09-18T10:15:30\",\"updatedTime\":\"2024-09-19T10:15:30\",\"createdBy\":\"admin\",\"updatedBy\":\"admin\"}";

        UserDTO userDTO = objectMapper.readValue(json, UserDTO.class);

        assertNotNull(userDTO);
        assertEquals(UUID.fromString("e2f8f41e-87b8-4f3b-b4b1-8e383cbb6d2a"), userDTO.getId());
        assertEquals("John Doe", userDTO.getFullName());
        assertEquals("john.doe@example.com", userDTO.getEmail());
        assertEquals("USER", userDTO.getRole());
        assertEquals(LocalDateTime.of(2024, 9, 18, 10, 15, 30), userDTO.getCreatedTime());
        assertEquals(LocalDateTime.of(2024, 9, 19, 10, 15, 30), userDTO.getUpdatedTime());
        assertEquals("admin", userDTO.getCreatedBy());
        assertEquals("admin", userDTO.getUpdatedBy());
    }
}
