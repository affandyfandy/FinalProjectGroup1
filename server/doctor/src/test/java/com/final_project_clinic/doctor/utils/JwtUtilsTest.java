package com.final_project_clinic.doctor.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private PublicKey publicKey;
    private KeyPair keyPair;
    private String validToken;
    private String expiredToken;

    @BeforeEach
    void setUp() {
        // Generate a real RSA key pair
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        publicKey = keyPair.getPublic();
        jwtUtils = new JwtUtils(publicKey);

        // Create a valid token using the private key
        validToken = Jwts.builder()
                .setSubject("admin2@gmail.com")
                .claim("id", "2d54a1cf-32s4-5432-dgtd-7d5s9p892368")
                .claim("role", "SUPERADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();

        // Create an expired token with a shorter expiration time
        expiredToken = Jwts.builder()
                .setSubject("admin2@gmail.com")
                .claim("id", "2d54a1cf-32s4-5432-dgtd-7d5s9p892368")
                .claim("role", "SUPERADMIN")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 10))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 5))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Test
    void testExtractEmail_ValidToken_ReturnsEmail() {
        String email = jwtUtils.extractEmail(validToken);
        assertEquals("admin2@gmail.com", email);
    }

    @Test
    void testExtractRole_ValidToken_ReturnsRole() {
        String role = jwtUtils.extractRole(validToken);
        assertEquals("SUPERADMIN", role);
    }

    @Test
    void testExtractAllClaims_ValidToken_ReturnsClaims() {
        Claims claims = jwtUtils.extractAllClaims(validToken);
        assertNotNull(claims);
        assertEquals("admin2@gmail.com", claims.getSubject());
        assertEquals("2d54a1cf-32s4-5432-dgtd-7d5s9p892368", claims.get("id"));
        assertEquals("SUPERADMIN", claims.get("role"));
    }

    @Test
    void testExtractAllClaims_InvalidToken_ThrowsException() {
        assertThrows(RuntimeException.class, () -> jwtUtils.extractAllClaims("invalidToken"));
    }

    @Test
    void testExtractAllClaims_ExpiredToken_ThrowsException() {
        assertThrows(ExpiredJwtException.class, () -> jwtUtils.extractAllClaims(expiredToken));
    }

}
