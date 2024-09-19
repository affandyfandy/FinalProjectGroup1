package com.final_project_clinic.authentication.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KeyLoadingExceptionTest {

    @Test
    public void testKeyLoadingExceptionMessageAndCause() {
        String expectedMessage = "Error loading key";
        Throwable cause = new RuntimeException("Cause of the error");
        KeyLoadingException exception = new KeyLoadingException(expectedMessage, cause);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
