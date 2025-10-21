package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

<<<<<<< HEAD
    @Autowired
    private UsuarioRepository usuarioRepository;
=======
    private final List<Usuario> usuarios = new ArrayList<>();
    private long nextId = 1;
>>>>>>> 7b7abd34a689173cbdf0d53c7baf558f1903d74e


<<<<<<< HEAD
    public Usuario agregarUsuario(Usuario nuevoUsuario) {
        
        return usuarioRepository.save(nuevoUsuario);
=======
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
>>>>>>> 7b7abd34a689173cbdf0d53c7baf558f1903d74e
    }
    public Optional<Usuario> findById(Long id) {
    return usuarioRepository.findById(id);
}
    public boolean existeUsuario(String username) {
        return usuarioRepository.existsByUsername(username);
    }
<<<<<<< HEAD

=======
    
>>>>>>> 7b7abd34a689173cbdf0d53c7baf558f1903d74e
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