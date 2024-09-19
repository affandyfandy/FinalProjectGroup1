package com.final_project_clinic.user.config.filter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class JwtKeyConfigTest {
    @Autowired
    private JwtKeyConfig jwtKeyConfig;

//    @Test
//    void testPublicKeyLoading() {
//        assertDoesNotThrow(() -> {
//            PublicKey publicKey = jwtKeyConfig.publicKey();
//            assertNotNull(publicKey);
//            assertEquals("RSA", publicKey.getAlgorithm());
//        });
//    }
//
//    @Test
//    void testPrivateKeyLoading() {
//        assertDoesNotThrow(() -> {
//            PrivateKey privateKey = jwtKeyConfig.privateKey();
//            assertNotNull(privateKey);
//            assertEquals("RSA", privateKey.getAlgorithm());
//        });
//    }
//
//    @Test
//    void testJwtDecoderCreation() {
//        assertDoesNotThrow(() -> {
//            JwtDecoder jwtDecoder = jwtKeyConfig.jwtDecoder(jwtKeyConfig.publicKey());
//            assertNotNull(jwtDecoder);
//        });
//    }
}