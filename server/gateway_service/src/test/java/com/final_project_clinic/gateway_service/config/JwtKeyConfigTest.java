package com.final_project_clinic.gateway_service.config;

import com.final_project_clinic.gateway_service.exception.KeyLoadingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtKeyConfigTest {

    private JwtKeyConfig jwtKeyConfig;

    @BeforeEach
    void setUp() {
        jwtKeyConfig = new JwtKeyConfig();
    }

    @Test
    void testPublicKey() throws Exception {
        PublicKey publicKey = jwtKeyConfig.publicKey();
        assertNotNull(publicKey);
        assertTrue(publicKey instanceof java.security.interfaces.RSAPublicKey);
    }

    @Test
    void testPrivateKey() throws Exception {
        PrivateKey privateKey = jwtKeyConfig.privateKey();
        assertNotNull(privateKey);
        assertTrue(privateKey instanceof java.security.interfaces.RSAPrivateKey);
    }

    @Test
    void testJwtDecoder() throws Exception {
        PublicKey publicKey = jwtKeyConfig.publicKey();
        JwtDecoder jwtDecoder = jwtKeyConfig.jwtDecoder(publicKey);
        assertNotNull(jwtDecoder);
    }
}
