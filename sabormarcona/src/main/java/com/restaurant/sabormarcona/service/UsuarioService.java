package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Usuario;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final List<Usuario> usuarios = new ArrayList<>();
    private long nextId = 1;

    public UsuarioService() {
        inicializarUsuarios();
    }

    private void inicializarUsuarios() {
        Usuario u1 = new Usuario("admin", "admin123", "Administrador", "ADMIN");
        u1.setId(nextId++);
        usuarios.add(u1);
        
        Usuario u2 = new Usuario("carlos", "carlos123", "Carlos García", "EMPLEADO");
        u2.setId(nextId++);
        usuarios.add(u2);
        
        Usuario u3 = new Usuario("mariana", "mariana123", "Mariana Rojas", "ADMIN");
        u3.setId(nextId++);
        usuarios.add(u3);
    }

    public Usuario agregarUsuario(Usuario nuevoUsuario) {
        nuevoUsuario.setId(nextId++);
        nuevoUsuario.setRol("EMPLEADO");
        nuevoUsuario.setNombre("Empleado Nuevo - " + nuevoUsuario.getUsername());
        usuarios.add(nuevoUsuario);
        return nuevoUsuario;
    }
    
    // Método de utilidad para verificar si el usuario ya existe
    public boolean existeUsuario(String username) {
        return usuarios.stream()
                .anyMatch(usuario -> usuario.getUsername().equalsIgnoreCase(username));
    }
    
    public Optional<Usuario> autenticar(String username, String password) {
        return usuarios.stream()
            .filter(usuario -> usuario.getUsername().equals(username)
                && usuario.getPassword().equals(password)
                && usuario.isActivo())
            .findFirst();
    }
    
    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }
}