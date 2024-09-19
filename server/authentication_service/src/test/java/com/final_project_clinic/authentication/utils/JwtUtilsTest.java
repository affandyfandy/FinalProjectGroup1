package com.final_project_clinic.authentication.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private JwtUtils jwtUtils;

    private String email;
    private UUID id;
    private String role;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        email = "test@example.com";
        id = UUID.randomUUID();
        role = "USER";

        // Load real RSA keys
        privateKey = loadPrivateKey("keys/private.pem");
        publicKey = loadPublicKey("keys/public.pem");
        jwtUtils = new JwtUtils(privateKey, publicKey);

        // Generate a token for testing
        token = jwtUtils.generateToken(email, id, role);
    }

    private PrivateKey loadPrivateKey(String filename) throws Exception {
        ClassPathResource resource = new ClassPathResource(filename);
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] keyBytes = inputStream.readAllBytes();
            String keyPEM = new String(keyBytes)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decoded = Base64.getDecoder().decode(keyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }

    private PublicKey loadPublicKey(String filename) throws Exception {
        ClassPathResource resource = new ClassPathResource(filename);
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] keyBytes = inputStream.readAllBytes();
            String keyPEM = new String(keyBytes)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decoded = Base64.getDecoder().decode(keyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        }
    }

    @Test
    void testGenerateToken() {
        assertNotNull(token);
    }

    @Test
    void testExtractEmail() {
        String extractedEmail = jwtUtils.extractEmail(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testExtractClaim() {
        Claims claims = jwtUtils.extractAllClaims(token);

        assertEquals(email, claims.get("email"));
        assertEquals(id.toString(), claims.get("id").toString());
        assertEquals(role, claims.get("role"));
    }

    @Test
    void testTokenExpiration() {
        Claims claims = jwtUtils.extractAllClaims(token);

        Date expiration = claims.getExpiration();
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testExtractAllClaims() {
        Claims claims = jwtUtils.extractAllClaims(token);
        assertEquals(email, claims.get("email"));
        assertEquals(id.toString(), claims.get("id").toString());
        assertEquals(role, claims.get("role"));
    }
}
