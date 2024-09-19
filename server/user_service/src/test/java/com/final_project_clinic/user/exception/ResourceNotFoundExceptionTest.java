package com.final_project_clinic.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testResourceNotFoundExceptionMessage() {
        String expectedMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
