package com.final_project_clinic.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DuplicateNikExceptionTest {

    @Test
    public void testDuplicateNikExceptionMessage() {
        String expectedMessage = "Duplicate NIK found";
        DuplicateNikException exception = new DuplicateNikException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
