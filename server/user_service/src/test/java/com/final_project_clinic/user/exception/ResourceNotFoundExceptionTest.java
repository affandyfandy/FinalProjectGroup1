package com.final_project_clinic.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceNotFoundExceptionTest {

    @Test
    void testResourceNotFoundExceptionMessage() {
        String expectedMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
