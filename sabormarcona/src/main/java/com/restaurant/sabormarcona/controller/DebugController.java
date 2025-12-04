package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
@Slf4j
public class DebugController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/usuarios")
    public String listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        String resultado = usuarios.stream()
                .map(u -> String.format(
                        "ID: %d, Username: %s, Nombre: %s, Email: %s, Rol: %s, Activo: %s, Password Hash: %s",
                        u.getId(),
                        u.getUsername(),
                        u.getNombre(),
                        u.getEmail(),
                        u.getRol(),
                        u.isActivo(),
                        u.getPassword() != null
                                ? u.getPassword().substring(0, Math.min(20, u.getPassword().length())) + "..."
                                : "NULL"))
                .collect(Collectors.joining("\n"));

        return resultado.isEmpty() ? "NO HAY USUARIOS EN LA BASE DE DATOS" : resultado;
    }

    @GetMapping("/test-password")
    public String testPassword(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);

        if (usuario == null) {
            return "Usuario no encontrado: " + username;
        }

        boolean matches = passwordEncoder.matches(password, usuario.getPassword());

        return String.format(
                "Usuario: %s\nPassword ingresada: %s\nPassword en BD (primeros 50 chars): %s\n" +
                        "¿Coinciden? %s\nActivo: %s",
                username,
                password,
                usuario.getPassword().substring(0, Math.min(50, usuario.getPassword().length())),
                matches,
                usuario.isActivo());
    }
}
