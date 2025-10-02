package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Usuario agregarUsuario(Usuario nuevoUsuario) {
        
        return usuarioRepository.save(nuevoUsuario);
    }
    public Optional<Usuario> findById(Long id) {
    return usuarioRepository.findById(id);
}
    public boolean existeUsuario(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public Optional<Usuario> autenticar(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // La comparación de contraseñas se mejorará con Spring Security
            if (password.equals(usuario.getPassword()) && usuario.isActivo()) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
}