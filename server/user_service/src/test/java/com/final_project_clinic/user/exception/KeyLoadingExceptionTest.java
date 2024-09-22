package com.final_project_clinic.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyLoadingExceptionTest {

    @Test
    void testKeyLoadingExceptionMessageAndCause() {
        String expectedMessage = "Error loading key";
        Throwable cause = new RuntimeException("Cause of the error");
        KeyLoadingException exception = new KeyLoadingException(expectedMessage, cause);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
