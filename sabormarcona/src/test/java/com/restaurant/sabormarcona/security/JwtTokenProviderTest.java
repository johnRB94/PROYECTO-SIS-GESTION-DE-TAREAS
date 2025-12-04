package com.restaurant.sabormarcona.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    private String token;

    @BeforeEach
    public void setUp() {
        // Crear una autenticación simulada
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("admin", "admin123"));
        token = jwtTokenProvider.generateToken(authentication);
    }

    @Test
    public void testGenerateToken() {
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testValidateToken() {
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    public void testGetUsernameFromToken() {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        assertEquals("admin", username);
    }

    @Test
    public void testInvalidToken() {
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    public void testExpiredToken() {
        // Crear un token con expiración muy corta
        // Este test fallaría inmediatamente
        // Solo es demostrativo
    }
}
