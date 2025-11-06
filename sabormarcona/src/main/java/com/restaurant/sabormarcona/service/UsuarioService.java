package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.exception.DuplicateResourceException;
import com.restaurant.sabormarcona.exception.ResourceNotFoundException;
import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // CREATE
    public Usuario agregarUsuario(Usuario nuevoUsuario) {
        log.debug("Agregando nuevo usuario: {}", nuevoUsuario.getUsername());

        if (usuarioRepository.existsByUsername(nuevoUsuario.getUsername())) {
            log.warn("Intento de crear usuario duplicado: {}", nuevoUsuario.getUsername());
            throw new DuplicateResourceException(
                    "El nombre de usuario '" + nuevoUsuario.getUsername() + "' ya existe");
        }

        if (usuarioRepository.existsByEmail(nuevoUsuario.getEmail())) {
            log.warn("Intento de crear usuario con email duplicado: {}", nuevoUsuario.getEmail());
            throw new DuplicateResourceException(
                    "El email '" + nuevoUsuario.getEmail() + "' ya está registrado");
        }

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        log.info("Usuario creado exitosamente con ID: {}", usuarioGuardado.getId());
        return usuarioGuardado;
    }

    // READ
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        log.debug("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        log.debug("Buscando usuario con ID: {}", id);
        if (id == null) {
            log.warn("ID de usuario es null");
            throw new IllegalArgumentException("El ID del usuario no puede ser null");
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
                });
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(String username) {
        log.debug("Buscando usuario con username: {}", username);
        return usuarioRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        log.debug("Obteniendo usuarios con rol: {}", rol);
        return usuarioRepository.findByRol(rol);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivos() {
        log.debug("Obteniendo usuarios activos");
        return usuarioRepository.findByActivo(true);
    }

    // UPDATE
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        log.debug("Actualizando usuario con ID: {}", id);

        if (id == null) {
            log.warn("ID de usuario es null al intentar actualizar");
            throw new IllegalArgumentException("El ID del usuario no puede ser null");
        }

        Usuario usuarioExistente = findById(id);

        if (!usuarioExistente.getUsername().equals(usuarioActualizado.getUsername())) {
            if (usuarioRepository.existsByUsername(usuarioActualizado.getUsername())) {
                throw new DuplicateResourceException(
                        "El username '" + usuarioActualizado.getUsername() + "' ya existe");
            }
        }

        if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
                throw new DuplicateResourceException(
                        "El email '" + usuarioActualizado.getEmail() + "' ya está registrado");
            }
        }

        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setRol(usuarioActualizado.getRol());
        usuarioExistente.setActivo(usuarioActualizado.isActivo());

        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
        }

        Usuario usuarioActualizadoDB = usuarioRepository.save(usuarioExistente);
        log.info("Usuario actualizado exitosamente con ID: {}", usuarioActualizadoDB.getId());
        return usuarioActualizadoDB;
    }

    public Usuario cambiarEstado(Long id, boolean activo) {
        log.debug("Cambiando estado de usuario ID: {} a {}", id, activo);

        if (id == null) {
            log.warn("ID de usuario es null al intentar cambiar estado");
            throw new IllegalArgumentException("El ID del usuario no puede ser null");
        }

        Usuario usuario = findById(id);
        usuario.setActivo(activo);

        return usuarioRepository.save(usuario);
    }

    // DELETE
    public void eliminarUsuario(Long id) {
        log.debug("Eliminando usuario con ID: {}", id);

        if (id == null) {
            log.warn("ID de usuario es null al intentar eliminar");
            throw new IllegalArgumentException("El ID del usuario no puede ser null");
        }

        if (!usuarioRepository.existsById(id)) {
            log.warn("Intento de eliminar usuario inexistente con ID: {}", id);
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }

        usuarioRepository.deleteById(id);
        log.info("Usuario eliminado exitosamente con ID: {}", id);
    }

    // AUTENTICACIÓN
    @Transactional(readOnly = true)
    public Optional<Usuario> autenticar(String username, String password) {
        log.debug("Intento de autenticación para usuario: {}", username);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (password.equals(usuario.getPassword()) && usuario.isActivo()) {
                log.info("Autenticación exitosa para usuario: {}", username);
                return Optional.of(usuario);
            }

            log.warn("Credenciales inválidas o usuario inactivo para: {}", username);
        } else {
            log.warn("Usuario no encontrado: {}", username);
        }

        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public boolean existeUsuario(String username) {
        return usuarioRepository.existsByUsername(username);
    }
}
