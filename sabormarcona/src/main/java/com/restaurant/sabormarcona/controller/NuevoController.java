package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NuevoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        if (!model.containsAttribute("usuario")) {
             model.addAttribute("usuario", new Usuario());
        }
        return "vista/nuevo"; 
    }

    @PostMapping("/nuevo")
    public String registrarEmpleado(@ModelAttribute("usuario") Usuario usuario,
                                    @RequestParam("confirmarContrasena") String confirmarContrasena, 
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        // Validaciones mejoradas
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty() ||
            usuario.getRol() == null || usuario.getRol().trim().isEmpty() ||
            usuario.getUsername() == null || usuario.getUsername().trim().isEmpty() || 
            usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            
            model.addAttribute("error", "Todos los campos son obligatorios.");
            return "vista/nuevo";
        }
        
        if (!usuario.getPassword().equals(confirmarContrasena)) {
             model.addAttribute("error", "Las contraseñas no coinciden.");
             return "vista/nuevo";
        }
        
        if (usuarioService.existeUsuario(usuario.getUsername())) {
             model.addAttribute("error", "El nombre de usuario '" + usuario.getUsername() + "' ya existe.");
             return "vista/nuevo";
        }

        // Ya no seteamos rol ni nombre, vienen del formulario
        usuarioService.agregarUsuario(usuario);

        redirectAttributes.addFlashAttribute("registroExitoso", true);
        redirectAttributes.addFlashAttribute("mensajeExito", "¡Empleado '" + usuario.getNombre() + "' registrado con éxito!");
        
        return "redirect:/nuevo"; 
    }
}