package com.final_project_clinic.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DuplicatePhoneNumberExceptionTest {

    @Test
    public void testDuplicatePhoneNumberExceptionMessage() {
        String expectedMessage = "Duplicate phone number found";
        DuplicatePhoneNumberException exception = new DuplicatePhoneNumberException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
