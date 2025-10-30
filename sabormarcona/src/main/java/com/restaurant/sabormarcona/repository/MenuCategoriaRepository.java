package com.restaurant.sabormarcona.repository;

import com.restaurant.sabormarcona.model.MenuCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCategoriaRepository extends JpaRepository<MenuCategoria, Long> {
    
    Optional<MenuCategoria> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    List<MenuCategoria> findAllByOrderByNombreAsc();
    
    @Query("SELECT c FROM MenuCategoria c LEFT JOIN FETCH c.items WHERE c.id = :id")
    Optional<MenuCategoria> findByIdWithItems(Long id);
    
    @Query("SELECT c FROM MenuCategoria c WHERE SIZE(c.items) > 0")
    List<MenuCategoria> findCategoriasConItems();
}
