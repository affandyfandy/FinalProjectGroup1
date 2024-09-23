package com.final_project_clinic.doctor.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidPhoneNumberExceptionTest {

    @Test
    void testDuplicatePhoneNumberExceptionMessage() {
        String expectedMessage = "Invalid phone number. The phone number must start with +62 and contain 9 to 13 digits.";
        InvalidPhoneNumberException exception = new InvalidPhoneNumberException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
