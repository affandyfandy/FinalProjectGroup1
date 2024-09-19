package com.final_project_clinic.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class UserShowDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void testUserShowDTOConstructor() {
        UUID id = UUID.randomUUID();
        String fullName = "Jane Doe";
        String email = "jane.doe@example.com";
        String role = "ADMIN";
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now().plusHours(1);
        String createdBy = "admin";
        String updatedBy = "admin";

        UserShowDTO userShowDTO = new UserShowDTO(
                id,
                fullName,
                email,
                role,
                createdTime,
                updatedTime,
                createdBy,
                updatedBy
        );

        assertAll("UserShowDTO",
                () -> assertEquals(id, userShowDTO.getId()),
                () -> assertEquals(fullName, userShowDTO.getFullName()),
                () -> assertEquals(email, userShowDTO.getEmail()),
                () -> assertEquals(role, userShowDTO.getRole()),
                () -> assertEquals(createdTime, userShowDTO.getCreatedTime()),
                () -> assertEquals(updatedTime, userShowDTO.getUpdatedTime()),
                () -> assertEquals(createdBy, userShowDTO.getCreatedBy()),
                () -> assertEquals(updatedBy, userShowDTO.getUpdatedBy())
        );
    }

    @Test
    void testUserShowDTOSetters() {
        UserShowDTO userShowDTO = new UserShowDTO();

        UUID id = UUID.randomUUID();
        String fullName = "Jane Doe";
        String email = "jane.doe@example.com";
        String role = "ADMIN";
        LocalDateTime createdTime = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedTime = LocalDateTime.now().minusHours(1);
        String createdBy = "system";
        String updatedBy = "system";

        userShowDTO.setId(id);
        userShowDTO.setFullName(fullName);
        userShowDTO.setEmail(email);
        userShowDTO.setRole(role);
        userShowDTO.setCreatedTime(createdTime);
        userShowDTO.setUpdatedTime(updatedTime);
        userShowDTO.setCreatedBy(createdBy);
        userShowDTO.setUpdatedBy(updatedBy);

        assertAll("UserShowDTO Setters",
                () -> assertEquals(id, userShowDTO.getId()),
                () -> assertEquals(fullName, userShowDTO.getFullName()),
                () -> assertEquals(email, userShowDTO.getEmail()),
                () -> assertEquals(role, userShowDTO.getRole()),
                () -> assertEquals(createdTime, userShowDTO.getCreatedTime()),
                () -> assertEquals(updatedTime, userShowDTO.getUpdatedTime()),
                () -> assertEquals(createdBy, userShowDTO.getCreatedBy()),
                () -> assertEquals(updatedBy, userShowDTO.getUpdatedBy())
        );
    }

    @Test
    void testUserShowDTOJsonSerialization() throws Exception {
        UUID id = UUID.randomUUID();
        String fullName = "Jane Doe";
        String email = "jane.doe@example.com";
        String role = "ADMIN";
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now().plusHours(1);
        String createdBy = "admin";
        String updatedBy = "admin";

        UserShowDTO userShowDTO = new UserShowDTO(
                id,
                fullName,
                email,
                role,
                createdTime,
                updatedTime,
                createdBy,
                updatedBy
        );

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(userShowDTO);
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
    void testUserShowDTO() {
        UUID id = UUID.randomUUID();
        String fullName = "Jane Doe";
        String email = "jane.doe@example.com";
        String role = "ADMIN";
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now().plusHours(1);
        String createdBy = "admin";
        String updatedBy = "admin";

        UserShowDTO userShowDTO = new UserShowDTO(
                id,
                fullName,
                email,
                role,
                createdTime,
                updatedTime,
                createdBy,
                updatedBy
        );

        assertEquals(id, userShowDTO.getId());
        assertEquals(fullName, userShowDTO.getFullName());
        assertEquals(email, userShowDTO.getEmail());
        assertEquals(role, userShowDTO.getRole());
        assertEquals(createdTime, userShowDTO.getCreatedTime());
        assertEquals(updatedTime, userShowDTO.getUpdatedTime());
        assertEquals(createdBy, userShowDTO.getCreatedBy());
        assertEquals(updatedBy, userShowDTO.getUpdatedBy());
    }
}
