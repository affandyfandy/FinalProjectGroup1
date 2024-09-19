package com.final_project_clinic.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TokenDTOTest {

    @Test
    void testNoArgsConstructor() {
        TokenDTO tokenDTO = new TokenDTO();
        assertNotNull(tokenDTO);
        assertNull(tokenDTO.getId());
        assertNull(tokenDTO.getRole());
        assertNull(tokenDTO.getEmail());
    }

    @Test
    void testAllArgsConstructor() {
        String id = "12345";
        String role = "ADMIN";
        String email = "test@example.com";

        TokenDTO tokenDTO = new TokenDTO(id, role, email);

        assertAll("TokenDTO All Args Constructor",
                () -> assertEquals(id, tokenDTO.getId()),
                () -> assertEquals(role, tokenDTO.getRole()),
                () -> assertEquals(email, tokenDTO.getEmail())
        );
    }

    @Test
    void testSettersAndGetters() {
        TokenDTO tokenDTO = new TokenDTO();

        String id = "67890";
        String role = "USER";
        String email = "user@example.com";

        tokenDTO.setId(id);
        tokenDTO.setRole(role);
        tokenDTO.setEmail(email);

        assertAll("TokenDTO Setters and Getters",
                () -> assertEquals(id, tokenDTO.getId()),
                () -> assertEquals(role, tokenDTO.getRole()),
                () -> assertEquals(email, tokenDTO.getEmail())
        );
    }
}
