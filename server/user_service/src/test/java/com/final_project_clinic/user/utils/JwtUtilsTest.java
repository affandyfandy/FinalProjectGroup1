package com.final_project_clinic.user.utils;

import com.final_project_clinic.user.dto.TokenDTO;
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
                .setSubject("email1d21321@gmail.com")
                .claim("id", "1c46a1dd-41e1-4775-bdcf-6d4c6d898779")
                .claim("role", "SUPERADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // Expires in 10 minutes
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();

        // Create an expired token with a shorter expiration time
        expiredToken = Jwts.builder()
                .setSubject("email1d21321@gmail.com")
                .claim("id", "1c46a1dd-41e1-4775-bdcf-6d4c6d898779")
                .claim("role", "SUPERADMIN")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 10)) // Issued 10 minutes ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 5)) // Expired 5 minutes ago
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }


    @Test
    void testValidateToken_ValidToken_ReturnsTrue() {
        assertTrue(jwtUtils.validateToken(validToken));
    }

    @Test
    void testExtractEmail_ValidToken_ReturnsEmail() {
        assertEquals("email1d21321@gmail.com", jwtUtils.extractEmail(validToken));
    }

    @Test
    void testExtractUserId_ValidToken_ReturnsUserId() {
        assertEquals("1c46a1dd-41e1-4775-bdcf-6d4c6d898779", jwtUtils.extractUserId(validToken));
    }

    @Test
    void testExtractUserRole_ValidToken_ReturnsUserRole() {
        assertEquals("SUPERADMIN", jwtUtils.extractUserRole(validToken));
    }

    @Test
    void testExtractTokenDetails_ValidToken_ReturnsTokenDTO() {
        TokenDTO tokenDTO = jwtUtils.extractTokenDetails(validToken);
        assertEquals("1c46a1dd-41e1-4775-bdcf-6d4c6d898779", tokenDTO.getId());
        assertEquals("SUPERADMIN", tokenDTO.getRole());
        assertEquals("email1d21321@gmail.com", tokenDTO.getEmail());
    }

    @Test
    void testExtractAllClaims_ValidToken_ReturnsClaims() {
        Claims claims = jwtUtils.extractAllClaims(validToken);
        assertNotNull(claims);
        assertEquals("email1d21321@gmail.com", claims.getSubject());
        assertEquals("1c46a1dd-41e1-4775-bdcf-6d4c6d898779", claims.get("id"));
        assertEquals("SUPERADMIN", claims.get("role"));
    }

    @Test
    void testExtractAllClaims_InvalidToken_ThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            jwtUtils.extractAllClaims("invalidToken");
        });
    }

    @Test
    void testExtractAllClaims_ExpiredToken_ThrowsException() {
        assertThrows(ExpiredJwtException.class, () -> {
            jwtUtils.extractAllClaims(expiredToken);
        });
    }
}
