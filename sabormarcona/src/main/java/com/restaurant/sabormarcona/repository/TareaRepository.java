package com.restaurant.sabormarcona.repository;

import com.restaurant.sabormarcona.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // Aquí podrías añadir métodos de búsqueda personalizados si los necesitas.
    long countByEstado(com.restaurant.sabormarcona.model.TaskStatus estado);
}