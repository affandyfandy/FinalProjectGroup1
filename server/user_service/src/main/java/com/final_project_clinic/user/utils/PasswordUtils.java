package com.final_project_clinic.user.utils;

import com.password4j.Hash;
import com.password4j.Password;

public class PasswordUtils {

    private PasswordUtils() {
        throw new IllegalStateException("Utility class");
    }


    public static String hashPassword(String plainPassword) {
        Hash hash = Password.hash(plainPassword).withBCrypt();
        return hash.getResult();
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return Password.check(plainPassword, hashedPassword).withBCrypt();
    }
}
