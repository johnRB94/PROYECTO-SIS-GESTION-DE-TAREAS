package com.restaurant.sabormarcona.config;

import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.dao.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();

        // Obtener el usuario autenticado
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Usuario usuario = userDetails.getUsuario();

        // Guardar información en la sesión
        session.setAttribute("usuarioLogueado", usuario);
        session.setAttribute("nombreUsuario", usuario.getNombre());
        session.setAttribute("rolUsuario", usuario.getRol());
        session.setAttribute("usuarioActual", usuario);

        log.info("Usuario {} autenticado exitosamente con rol {}", usuario.getUsername(), usuario.getRol());

        // Redirigir a principal
        response.sendRedirect(request.getContextPath() + "/principal");
    }
}