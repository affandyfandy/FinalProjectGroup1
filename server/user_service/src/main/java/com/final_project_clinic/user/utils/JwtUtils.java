package com.final_project_clinic.user.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.final_project_clinic.user.dto.TokenDTO;
import java.security.PublicKey;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtils {

    private final PublicKey publicKey;
    private static final Logger utilsLogger = LoggerFactory.getLogger(JwtUtils.class);

    @Autowired
    public JwtUtils(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    // Extract all claims from the token
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            utilsLogger.warn("Token has expired");

            throw e; // Re-throw to handle it in validateToken
        } catch (Exception e) {
            utilsLogger.error("Token has expired");
            throw e; // Re-throw to handle it in validateToken
        }
    }

    // Validate the token by checking its expiration date
    public boolean validateToken(String token) {
        Claims claims = extractAllClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    // Extract the username (email) from the token
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Optionally, extract other claims such as the user ID or role
    public String extractUserId(String token) {
        return (String) extractAllClaims(token).get("id");
    }

    public String extractUserRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }

    public TokenDTO extractTokenDetails(String token) {
        Claims claims = extractAllClaims(token);
        String id = (String) claims.get("id");
        String role = (String) claims.get("role");
        String email = claims.getSubject(); // email is typically stored as the subject

        return new TokenDTO(id, role, email);
    }
}
