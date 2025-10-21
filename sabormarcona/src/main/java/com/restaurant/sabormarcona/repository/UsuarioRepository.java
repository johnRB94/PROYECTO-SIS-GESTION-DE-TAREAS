package com.restaurant.sabormarcona.repository;

import com.restaurant.sabormarcona.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring Data JPA creará la consulta automáticamente a partir del nombre del método.
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
}