package com.final_project_clinic.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicateNikExceptionTest {

    @Test
    void testDuplicateNikExceptionMessage() {
        String expectedMessage = "Duplicate NIK found";
        DuplicateNikException exception = new DuplicateNikException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
