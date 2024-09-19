package com.final_project_clinic.authentication.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateEmailExceptionTest {

    @Test
    public void testDuplicateEmailExceptionTest() {
        String expectedMessage = "Duplicate Email found";
        DuplicateEmailException exception = new DuplicateEmailException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}