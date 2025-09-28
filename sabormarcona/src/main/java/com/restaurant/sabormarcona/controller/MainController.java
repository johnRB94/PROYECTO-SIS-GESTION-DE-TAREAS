package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("titulo", "Sabor de Marcona");
        return "index";
    }
    
    @GetMapping("/principal")
    public String principal(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuario == null) {
            return "redirect:/";
        }
        
        model.addAttribute("usuario", usuario.getNombre());
        model.addAttribute("rol", usuario.getRol());
        model.addAttribute("username", usuario.getUsername());
        return "principal";
    }
}