package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Usuario;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class InicioController {

    @GetMapping("/")
    public String mostrarInicio(Model model, HttpSession session) {
        log.debug("GET / - Mostrando página de inicio");

        // Verificar si ya está autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            log.info("Usuario ya autenticado, redirigiendo a principal");
            return "redirect:/principal";
        }

        // Verificar parámetros de error o logout
        String error = (String) model.asMap().get("error");
        String logout = (String) model.asMap().get("logout");

        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }

        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada exitosamente");
        }

        return "vista/inicio";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.debug("GET /logout - Cerrando sesión");

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            log.info("Usuario {} cerró sesión", usuario.getUsername());
        }

        session.invalidate();
        SecurityContextHolder.clearContext();

        return "redirect:/?logout=true";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Model model) {
        log.warn("Acceso denegado - Usuario intentó acceder a recurso sin permisos");
        model.addAttribute("error", "No tienes permisos para acceder a este recurso");
        return "vista/acceso-denegado";
    }
}