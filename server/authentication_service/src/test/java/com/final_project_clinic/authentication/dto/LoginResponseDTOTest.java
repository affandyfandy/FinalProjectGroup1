package com.final_project_clinic.authentication.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginResponseDTOTest {

    @Test
    void testLoginResponseDTOConstructorAndGetter() {
        // Given
        String token = "sampleToken123";

        // When
        LoginResponseDTO responseDTO = new LoginResponseDTO(token);

        // Then
        assertEquals(token, responseDTO.getToken(), "Token should match the provided value");
    }
}
