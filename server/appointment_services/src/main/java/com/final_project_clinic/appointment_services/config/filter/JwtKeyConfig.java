package com.final_project_clinic.appointment_services.config.filter;

import com.final_project_clinic.appointment_services.exception.KeyLoadingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

// @Configuration
// public class JwtKeyConfig {

//     @Bean
//     public PublicKey publicKey() throws Exception {
//         String publicKeyPEM = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("keys/public.pem").toURI())));
//         publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
//                 .replace("-----END PUBLIC KEY-----", "")
//                 .replaceAll("\\s", "");

//         byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
//         X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
//         KeyFactory keyFactory = KeyFactory.getInstance("RSA");

//         return keyFactory.generatePublic(keySpec);
//     }
// }

@Configuration
public class JwtKeyConfig {

    @Bean
    public PublicKey publicKey() throws KeyLoadingException {
        try {
            // Load public key from PEM file
            ClassPathResource resource = new ClassPathResource("keys/public.pem");
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] keyBytes = inputStream.readAllBytes();
                String publicKeyPEM = new String(keyBytes)
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s+", "");
                byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePublic(keySpec);
            }
        } catch (Exception e) {
            throw new KeyLoadingException("Failed to load the public key", e);
        }
    }

    @Bean
    public PrivateKey privateKey() throws KeyLoadingException {
        try {
            // Load private key from PEM file
            ClassPathResource resource = new ClassPathResource("keys/private.pem");
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] keyBytes = inputStream.readAllBytes();
                String privateKeyPEM = new String(keyBytes)
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s+", "");
                byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePrivate(keySpec);
            }
        } catch (Exception e) {
            throw new KeyLoadingException("Failed to load the private key", e);
        }
    }
}
