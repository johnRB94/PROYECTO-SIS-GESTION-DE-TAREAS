package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Endpoint para obtener token JWT
     * POST /api/auth/login
     * Body: {
     * "username": "admin",
     * "password": "admin123"
     * }
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.generateToken(authentication);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("username", username);
            response.put("message", "Login exitoso");

            log.info("Usuario {} autenticado exitosamente", username);
            return response;
        } catch (Exception e) {
            log.error("Error de autenticación para usuario {}: {}", username, e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Credenciales inválidas");
            error.put("message", "El nombre de usuario o contraseña es incorrecto");
            return error;
        }
    }

    /**
     * Endpoint para obtener token a partir del username
     * Útil para generar tokens sin autenticación (con validación adicional)
     */
    @PostMapping("/token")
    public Map<String, Object> getToken(@RequestBody Map<String, String> request) {
        String username = request.get("username");

        try {
            String token = tokenProvider.generateTokenFromUsername(username);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("username", username);
            return response;
        } catch (Exception e) {
            log.error("Error generando token: {}", e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al generar token");
            return error;
        }
    }

    /**
     * Endpoint para validar un token
     * POST /api/auth/validate
     * Body: { "token": "eyJhbGciOiJIUzUxMiJ9..." }
     */
    @PostMapping("/validate")
    public Map<String, Object> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        boolean isValid = tokenProvider.validateToken(token);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);

        if (isValid) {
            String username = tokenProvider.getUsernameFromToken(token);
            response.put("username", username);
            response.put("expiresAt", tokenProvider.getExpirationDateFromToken(token));
        }

        return response;
    }
}
