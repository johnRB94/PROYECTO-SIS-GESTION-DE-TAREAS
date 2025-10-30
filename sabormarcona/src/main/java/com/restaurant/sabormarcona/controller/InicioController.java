package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class InicioController {

    private final UsuarioService usuarioService;

    @GetMapping("/")
    public String mostrarInicio(Model model, HttpSession session) {
        log.debug("GET / - Mostrando página de inicio");

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            log.info("Usuario {} ya autenticado, redirigiendo a principal", usuarioLogueado.getUsername());
            return "redirect:/principal";
        }

        return "vista/inicio";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        Model model) {

        log.debug("POST /login - Intento de login para usuario: {}", username);

        try {
            // Validar username
            if (username == null || username.trim().isEmpty()) {
                log.warn("Username vacío");
                model.addAttribute("error", "El usuario es obligatorio");
                return "vista/inicio";
            }

            // Validar password
            if (password == null || password.trim().isEmpty()) {
                log.warn("Password vacío");
                model.addAttribute("error", "La contraseña es obligatoria");
                model.addAttribute("username", username);
                return "vista/inicio";
            }

            // Intentar autenticar
            Optional<Usuario> usuarioOpt = usuarioService.autenticar(username, password);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                // Guardar en sesión
                session.setAttribute("usuarioLogueado", usuario);
                session.setAttribute("nombreUsuario", usuario.getNombre());
                session.setAttribute("rolUsuario", usuario.getRol());

                log.info("Login exitoso para: {} (ID: {})", username, usuario.getId());

                redirectAttributes.addFlashAttribute("mensaje", "¡Bienvenido, " + usuario.getNombre() + "!");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
                return "redirect:/principal";

            } else {
                // Credenciales incorrectas - Modal permanece abierto
                log.warn("Credenciales incorrectas para: {}", username);
                model.addAttribute("error", "Usuario o contraseña incorrectos");
                model.addAttribute("username", username);
                return "vista/inicio";
            }

        } catch (Exception e) {
            log.error("Error en login para usuario {}: {}", username, e.getMessage(), e);
            model.addAttribute("error", "Error interno del servidor. Intente nuevamente.");
            model.addAttribute("username", username);
            return "vista/inicio";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("GET /logout - Cerrando sesión");

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            log.info("Usuario {} cerró sesión", usuario.getUsername());
        }

        session.invalidate();

        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada exitosamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "info");
        return "redirect:/";
    }
}
