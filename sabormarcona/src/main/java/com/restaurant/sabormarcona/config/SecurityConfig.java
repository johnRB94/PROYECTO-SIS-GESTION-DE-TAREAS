package com.restaurant.sabormarcona.config;

import com.restaurant.sabormarcona.dao.CustomUserDetailsService;
import com.restaurant.sabormarcona.security.JwtAuthenticationFilter;
import com.restaurant.sabormarcona.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
        private final JwtTokenProvider jwtTokenProvider;
        private final CustomUserDetailsService customUserDetailsService;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
                return new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService)
                        throws Exception {
                http
                                .securityMatcher("/api/**") // Solo para /api/**
                                .userDetailsService(userDetailsService)
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        @Bean
        public SecurityFilterChain webSecurityFilterChain(HttpSecurity http, UserDetailsService userDetailsService)
                        throws Exception {
                http
                                .userDetailsService(userDetailsService)
                                .authorizeHttpRequests(authorize -> authorize
                                                // Recursos públicos
                                                .requestMatchers("/", "/login", "/logout", "/css/**", "/js/**",
                                                                "/img/**", "/error")
                                                .permitAll()
                                                .requestMatchers("/usuarios/nuevo").permitAll()
                                                .requestMatchers("/debug/**").permitAll()

                                                // Auth-info accesible para todos (autenticados y anónimos)
                                                .requestMatchers("/auth-info").permitAll()

                                                // Admin tiene acceso a todo
                                                .requestMatchers("/principal").authenticated() // Solo autenticados
                                                .requestMatchers("/menu/**", "/usuarios/**")
                                                .hasAnyRole("ADMIN", "USER1", "USER2")
                                                .requestMatchers("/nuevo").hasRole("ADMIN")

                                                // USER1 tiene acceso a: tareas, incidencias, gestión menú, gestión
                                                // insumos
                                                .requestMatchers("/tareas/**").hasAnyRole("ADMIN", "USER1")
                                                .requestMatchers("/incidencias/**")
                                                .hasAnyRole("ADMIN", "USER1", "USER2")
                                                .requestMatchers("/menu/**").hasAnyRole("ADMIN", "USER1", "USER2")
                                                .requestMatchers("/insumos/**").hasAnyRole("ADMIN", "USER1")

                                                // Cualquier otra petición requiere autenticación
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/")
                                                .loginProcessingUrl("/login")
                                                .successHandler(customAuthenticationSuccessHandler)
                                                .failureUrl("/?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/acceso-denegado"))
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }
}
