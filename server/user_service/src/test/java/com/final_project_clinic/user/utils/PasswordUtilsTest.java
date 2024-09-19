package com.final_project_clinic.user.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilsTest {

    @Test
    void testHashPassword() {
        String plainPassword = "mySecretPassword";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);

        // Assert that the hashed password is not null and not equal to the plain password
        assertNotNull(hashedPassword);
        assertNotEquals(plainPassword, hashedPassword);

        // Verify that the hashed password can be verified
        assertTrue(PasswordUtils.verifyPassword(plainPassword, hashedPassword), "The hashed password should be verified successfully.");
    }

    @Test
    void testVerifyPassword() {
        String plainPassword = "mySecretPassword";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);

        // Verify that the password matches the hashed password
        assertTrue(PasswordUtils.verifyPassword(plainPassword, hashedPassword));

        // Verify that a different password does not match
        assertFalse(PasswordUtils.verifyPassword("wrongPassword", hashedPassword));
    }
}
