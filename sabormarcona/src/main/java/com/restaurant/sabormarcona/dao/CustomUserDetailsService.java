package com.restaurant.sabormarcona.dao;

import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Intentando cargar usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        if (!usuario.isActivo()) {
            log.warn("Usuario {} está inactivo", username);
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        log.info("Usuario {} cargado exitosamente con rol {}", username, usuario.getRol());
        return new CustomUserDetails(usuario);
    }
}