// src/main/java/com/restaurant/sabormarcona/service/UsuarioService.java
package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Usuario;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    private final List<Usuario> usuarios = new ArrayList<>();
    
    public UsuarioService() {
        inicializarUsuarios();
    }
    
    private void inicializarUsuarios() {
        usuarios.add(new Usuario("admin", "admin123", "Administrador", "ADMIN"));
        usuarios.add(new Usuario("carlos", "chef123", "Carlos García", "CHEF"));
        usuarios.add(new Usuario("mariana", "mesera123", "Mariana Rojas", "MESERO"));
        usuarios.add(new Usuario("empleado", "emp123", "Empleado General", "EMPLEADO"));
    }
    
    public Optional<Usuario> autenticar(String username, String password) {
        System.out.println("Intentando autenticar: " + username + " con password: " + password);
        
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