package com.restaurant.sabormarcona.dao;

import com.restaurant.sabormarcona.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertir el rol del usuario a un GrantedAuthority
        String role = usuario.getRol();
        if (role == null || role.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Asegurarse de que el rol tenga el prefijo ROLE_
        String roleWithPrefix = role.toUpperCase().startsWith("ROLE_") ? role.toUpperCase()
                : "ROLE_" + role.toUpperCase();

        return Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.isActivo();
    }
}