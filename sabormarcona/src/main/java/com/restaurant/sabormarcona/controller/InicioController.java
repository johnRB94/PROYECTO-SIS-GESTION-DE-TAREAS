package com.restaurant.sabormarcona.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class InicioController {

    @GetMapping("/")
    public String mostrarInicio(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model,
            HttpSession session) {
        log.debug("GET / - Mostrando página de inicio");

        // Verificar si ya está autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            log.info("Usuario ya autenticado, redirigiendo a principal");
            return "redirect:/principal";
        }

        // Manejar parámetros de error o logout
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }

        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada exitosamente");
        }

        return "vista/inicio";
    }

    @GetMapping("/auth-info")
    @ResponseBody
    public Map<String, Object> getAuthInfo(Authentication authentication) {
        Map<String, Object> authInfo = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {

            String username = authentication.getName();
            String roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));

            authInfo.put("authenticated", true);
            authInfo.put("username", username);
            authInfo.put("roles", roles);
            authInfo.put("timestamp", System.currentTimeMillis());

            log.info("Auth info solicitada para: {} | Roles: {}", username, roles);
        } else {
            authInfo.put("authenticated", false);
            authInfo.put("username", "anonymous");
            authInfo.put("roles", "NONE");
            authInfo.put("timestamp", System.currentTimeMillis());

            log.debug("Solicitud de auth info sin autenticación");
        }

        return authInfo;
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Model model) {
        log.warn("Acceso denegado - Usuario intentó acceder a recurso sin permisos");
        model.addAttribute("error", "No tienes permisos para acceder a este recurso");
        return "vista/acceso-denegado";
    }
}
