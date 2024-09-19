package com.final_project_clinic.user.config.filter;

import com.final_project_clinic.user.exception.KeyLoadingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtKeyConfigTest {

    @InjectMocks
    private JwtKeyConfig jwtKeyConfig;

    @Mock
    private ClassPathResource publicKeyResource;

    @Mock
    private ClassPathResource privateKeyResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublicKeyLoadingSuccess() throws Exception {
        // Mock the resource input stream to return a valid public key PEM
        String publicKeyPEM = """
                -----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuC4...\n" +
                "-----END PUBLIC KEY-----
                """;
        InputStream publicKeyInputStream = mock(InputStream.class);
        when(publicKeyResource.getInputStream()).thenReturn(publicKeyInputStream);
        when(publicKeyInputStream.readAllBytes()).thenReturn(publicKeyPEM.getBytes());

        PublicKey publicKey = jwtKeyConfig.publicKey();

        assertNotNull(publicKey);
        assertTrue(publicKey instanceof RSAPublicKey);
    }

    @Test
    void testPrivateKeyLoadingSuccess() throws Exception {
        // Mock the resource input stream to return a valid private key PEM
        String privateKeyPEM = """
                -----BEGIN PRIVATE KEY-----\n" +
                "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBK...\n" +
                "-----END PRIVATE KEY-----
                """;
        InputStream privateKeyInputStream = mock(InputStream.class);
        when(privateKeyResource.getInputStream()).thenReturn(privateKeyInputStream);
        when(privateKeyInputStream.readAllBytes()).thenReturn(privateKeyPEM.getBytes());

        PrivateKey privateKey = jwtKeyConfig.privateKey();

        assertNotNull(privateKey);
    }
}
