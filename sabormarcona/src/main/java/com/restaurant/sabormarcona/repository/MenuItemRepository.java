package com.restaurant.sabormarcona.repository;

import com.restaurant.sabormarcona.model.MenuItem;
import com.restaurant.sabormarcona.model.MenuCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByCategoria(MenuCategoria categoria);
    
    List<MenuItem> findByDisponible(boolean disponible);
    
    List<MenuItem> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);
    
    List<MenuItem> findByNombreContainingIgnoreCase(String nombre);
    
    List<MenuItem> findAllByOrderByPrecioAsc();
    
    long countByCategoria(MenuCategoria categoria);
    
    long countByDisponible(boolean disponible);
    
    @Query("SELECT m FROM MenuItem m WHERE m.categoria.id = :categoriaId AND m.disponible = true")
    List<MenuItem> findItemsDisponiblesPorCategoria(@Param("categoriaId") Long categoriaId);
    
    @Query("SELECT m FROM MenuItem m LEFT JOIN FETCH m.categoria WHERE m.id = :id")
    MenuItem findByIdWithCategoria(@Param("id") Long id);
    
    @Query("SELECT m.categoria.nombre, COUNT(m), AVG(m.precio) FROM MenuItem m GROUP BY m.categoria.nombre")
    List<Object[]> obtenerEstadisticasPorCategoria();
    
    @Modifying
    @Query("UPDATE MenuItem m SET m.disponible = :disponible WHERE m.categoria.id = :categoriaId")
    int cambiarDisponibilidadPorCategoria(@Param("categoriaId") Long categoriaId, 
                                          @Param("disponible") boolean disponible);
}
