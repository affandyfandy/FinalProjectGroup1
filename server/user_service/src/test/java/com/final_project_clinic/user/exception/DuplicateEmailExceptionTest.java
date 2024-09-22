package com.final_project_clinic.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicateEmailExceptionTest {

    @Test
    void testDuplicateNikExceptionMessage() {
        String expectedMessage = "Duplicate Email found";
        DuplicateEmailException exception = new DuplicateEmailException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
