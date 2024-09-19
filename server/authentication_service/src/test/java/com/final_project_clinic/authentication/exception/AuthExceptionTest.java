package com.final_project_clinic.authentication.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthExceptionTest {

    @Test
    void testExceptionMessage() {
        // Given
        String errorMessage = "Authentication failed";

        // When
        AuthException exception = new AuthException(errorMessage);

        // Then
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
    }

    @Test
    void testExceptionInheritance() {
        // Given
        String errorMessage = "Authentication failed";

        // When
        AuthException exception = new AuthException(errorMessage);

        // Then
        assertTrue(exception instanceof RuntimeException, "AuthException should be an instance of RuntimeException");
    }
}
