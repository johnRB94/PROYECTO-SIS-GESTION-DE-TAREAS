package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class InicioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        
        System.out.println("Login attempt - Usuario: " + username + ", Password: " + password);
        
        try {
            Optional<Usuario> usuario = usuarioService.autenticar(username, password);
            
            if (usuario.isPresent()) {
                session.setAttribute("usuarioLogueado", usuario.get());
                System.out.println("Login exitoso para: " + username);
                return "redirect:/principal";
            } else {
                System.out.println("Credenciales incorrectas para: " + username);
                redirectAttributes.addFlashAttribute("error", "Usuario o contraseña incorrectos");
                return "redirect:/";
            }
        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error interno del servidor");
            return "redirect:/";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}