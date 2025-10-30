package com.restaurant.sabormarcona.repository;

import com.restaurant.sabormarcona.model.Incidencia;
import com.restaurant.sabormarcona.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

       List<Incidencia> findByEstado(String estado);

       List<Incidencia> findByTrabajador(Usuario trabajador);

       List<Incidencia> findByPrioridad(String prioridad);

       List<Incidencia> findByEstadoAndPrioridad(String estado, String prioridad);

       List<Incidencia> findByTituloContainingIgnoreCase(String titulo);

       List<Incidencia> findByFechaLimiteBefore(LocalDateTime fecha);

       List<Incidencia> findByFechaLimiteAfter(LocalDateTime fecha);

       List<Incidencia> findAllByOrderByFechaLimiteAsc();

       long countByEstado(String estado);

       long countByPrioridad(String prioridad);

       long countByTrabajador(Usuario trabajador);

       boolean existsByTitulo(String titulo);

       boolean existsByTrabajadorAndEstado(Usuario trabajador, String estado);

       @Query("SELECT i FROM Incidencia i WHERE i.trabajador.id = :trabajadorId AND i.estado = :estado")
       List<Incidencia> findByTrabajadorIdAndEstado(@Param("trabajadorId") Long trabajadorId,
                     @Param("estado") String estado);

       @Query("SELECT i FROM Incidencia i WHERE i.fechaLimite < :fecha AND i.estado != 'RESUELTA'")
       List<Incidencia> findIncidenciasVencidas(@Param("fecha") LocalDateTime fecha);

       @Query("SELECT i FROM Incidencia i LEFT JOIN FETCH i.trabajador WHERE i.id = :id")
       Optional<Incidencia> findByIdWithTrabajador(@Param("id") Long id);

       @Query("SELECT DISTINCT i FROM Incidencia i LEFT JOIN FETCH i.trabajador")
       List<Incidencia> findAllWithTrabajadores();

       @Query("SELECT i FROM Incidencia i WHERE " +
                     "(:estado IS NULL OR i.estado = :estado) AND " +
                     "(:prioridad IS NULL OR i.prioridad = :prioridad) AND " +
                     "(:trabajadorId IS NULL OR i.trabajador.id = :trabajadorId)")
       List<Incidencia> buscarIncidenciasPorCriterios(@Param("estado") String estado,
                     @Param("prioridad") String prioridad,
                     @Param("trabajadorId") Long trabajadorId);

       @Query("SELECT i.estado, COUNT(i) FROM Incidencia i GROUP BY i.estado")
       List<Object[]> contarIncidenciasPorEstado();

       @Query("SELECT i.prioridad, COUNT(i) FROM Incidencia i GROUP BY i.prioridad")
       List<Object[]> contarIncidenciasPorPrioridad();

       @Query(value = "SELECT * FROM incidencias WHERE estado = :estado AND prioridad = 'ALTA'", nativeQuery = true)
       List<Incidencia> findIncidenciasAltaPrioridadPorEstado(@Param("estado") String estado);

       @Query(value = "SELECT * FROM incidencias WHERE fecha_limite BETWEEN :inicio AND :fin", nativeQuery = true)
       List<Incidencia> findIncidenciasEnRangoFechas(@Param("inicio") LocalDateTime inicio,
                     @Param("fin") LocalDateTime fin);

       @Modifying
       @Query("UPDATE Incidencia i SET i.estado = :nuevoEstado WHERE i.estado = :estadoAnterior")
       int actualizarEstadoMasivo(@Param("estadoAnterior") String estadoAnterior,
                     @Param("nuevoEstado") String nuevoEstado);

       @Modifying
       @Query("UPDATE Incidencia i SET i.prioridad = 'ALTA' WHERE i.fechaLimite < :fecha AND i.estado = 'PENDIENTE'")
       int actualizarPrioridadIncidenciasVencidas(@Param("fecha") LocalDateTime fecha);

       @Modifying
       @Query("DELETE FROM Incidencia i WHERE i.estado = 'RESUELTA' AND i.fechaLimite < :fecha")
       int eliminarIncidenciasResueltasAntiguas(@Param("fecha") LocalDateTime fecha);
}
