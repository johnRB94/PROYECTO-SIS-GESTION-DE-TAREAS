package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsuarioInitializationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioInitializationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void initializePasswords() {
        log.debug("=== Verificando y encriptando contraseñas en texto plano ===");

        usuarioRepository.findAll().forEach(usuario -> {
            String password = usuario.getPassword();

            // Verificar si la contraseña no es un hash de BCrypt
            // Las contraseñas de BCrypt comienzan con $2a$, $2b$, $2y$
            if (password != null && !password.startsWith("$2")) {
                log.debug("Encriptando contraseña de usuario: {}", usuario.getUsername());
                String encryptedPassword = passwordEncoder.encode(password);
                usuario.setPassword(encryptedPassword);
                usuarioRepository.save(usuario);
                log.debug("Contraseña encriptada para: {}", usuario.getUsername());
            }
        });

        log.debug("=== Verificacion completada ===");

        // Verificar si el usuario admin existe y actualizar su contraseña
        var adminOpt = usuarioRepository.findByUsername("admin");
        if (adminOpt.isPresent()) {
            Usuario admin = adminOpt.get();
            log.debug("Usuario admin encontrado. Actualizando contraseña a admin123");
            String encryptedPassword = passwordEncoder.encode("admin123");
            admin.setPassword(encryptedPassword);
            usuarioRepository.save(admin);
            log.debug("Contraseña del admin actualizada: usa username=admin, password=admin123");
        } else {
            log.debug("Usuario admin NO existe - Creando usuario de prueba");

            Usuario newAdmin = new Usuario();
            newAdmin.setUsername("admin");
            newAdmin.setPassword(passwordEncoder.encode("admin123"));
            newAdmin.setNombre("Administrador");
            newAdmin.setEmail("admin@sabormarcona.com");
            newAdmin.setCargo("ADMINISTRADOR");
            newAdmin.setRol("admin");
            newAdmin.setActivo(true);

            Usuario savedAdmin = usuarioRepository.save(newAdmin);
            log.debug("Usuario de prueba creado: username=admin, password=admin123, id=" + savedAdmin.getId());
        }
    }
}
