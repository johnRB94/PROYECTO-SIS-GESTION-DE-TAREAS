package com.restaurant.sabormarcona.repository;

import com.restaurant.sabormarcona.model.Tarea;
import com.restaurant.sabormarcona.model.TaskStatus;
import com.restaurant.sabormarcona.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

       List<Tarea> findByEstado(TaskStatus estado);

       List<Tarea> findByTrabajadorAsignado(Usuario trabajador);

       List<Tarea> findByPrioridad(String prioridad);

       List<Tarea> findByFechaLimiteBefore(LocalDateTime fecha);

       List<Tarea> findByFechaLimiteAfter(LocalDateTime fecha);

       List<Tarea> findByEstadoAndPrioridad(TaskStatus estado, String prioridad);

       List<Tarea> findByEstadoOrPrioridad(TaskStatus estado, String prioridad);

       List<Tarea> findByTituloContainingIgnoreCase(String titulo);

       List<Tarea> findByDescripcionContainingIgnoreCase(String descripcion);

       List<Tarea> findAllByOrderByFechaLimiteAsc();

       List<Tarea> findAllByOrderByPrioridadDesc();

       long countByEstado(TaskStatus estado);

       long countByPrioridad(String prioridad);

       long countByTrabajadorAsignado(Usuario trabajador);

       boolean existsByTitulo(String titulo);

       boolean existsByTrabajadorAsignadoAndEstado(Usuario trabajador, TaskStatus estado);

       @Query("SELECT t FROM Tarea t WHERE t.trabajadorAsignado.id = :trabajadorId AND t.estado = :estado")
       List<Tarea> findByTrabajadorAndEstado(@Param("trabajadorId") Long trabajadorId,
                     @Param("estado") TaskStatus estado);

       @Query("SELECT t FROM Tarea t WHERE t.estado = com.restaurant.sabormarcona.model.TaskStatus.PENDIENTE AND t.fechaLimite BETWEEN :inicio AND :fin")
       List<Tarea> findTareasPendientesProximas(@Param("inicio") LocalDateTime inicio,
                     @Param("fin") LocalDateTime fin);

       @Query("SELECT t FROM Tarea t WHERE " +
                     "(:estado IS NULL OR t.estado = :estado) AND " +
                     "(:prioridad IS NULL OR t.prioridad = :prioridad) AND " +
                     "(:trabajadorId IS NULL OR t.trabajadorAsignado.id = :trabajadorId)")
       List<Tarea> buscarTareasPorCriterios(@Param("estado") TaskStatus estado,
                     @Param("prioridad") String prioridad,
                     @Param("trabajadorId") Long trabajadorId);

       @Query("SELECT t FROM Tarea t WHERE t.fechaLimite < :fecha AND t.estado <> com.restaurant.sabormarcona.model.TaskStatus.COMPLETADA")
       List<Tarea> findTareasVencidas(@Param("fecha") LocalDateTime fecha);

       @Query("SELECT t FROM Tarea t LEFT JOIN FETCH t.trabajadorAsignado WHERE t.id = :id")
       Tarea findByIdWithTrabajador(@Param("id") Long id);

       @Query("SELECT DISTINCT t FROM Tarea t LEFT JOIN FETCH t.trabajadorAsignado")
       List<Tarea> findAllWithTrabajadores();

       @Query("SELECT t.prioridad, COUNT(t) FROM Tarea t GROUP BY t.prioridad")
       List<Object[]> contarTareasPorPrioridad();

       @Query("SELECT t.estado, COUNT(t) FROM Tarea t GROUP BY t.estado")
       List<Object[]> contarTareasPorEstado();

       @Query("SELECT t FROM Tarea t WHERE " +
                     "LOWER(t.titulo) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
                     "LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
       List<Tarea> buscarTareasPorTexto(@Param("texto") String texto);

       @Query(value = "SELECT * FROM tareas WHERE prioridad = :prioridad AND estado = :estado", nativeQuery = true)
       List<Tarea> findTareasByPrioridadAndEstadoNative(@Param("prioridad") String prioridad,
                     @Param("estado") String estado);

       @Query(value = "SELECT * FROM tareas WHERE fecha_limite >= DATE_SUB(NOW(), INTERVAL 1 MONTH)", nativeQuery = true)
       List<Tarea> findTareasDelUltimoMes();

       @Query(value = "SELECT * FROM tareas WHERE " +
                     "titulo LIKE CONCAT('%', :texto, '%') OR " +
                     "descripcion LIKE CONCAT('%', :texto, '%')", nativeQuery = true)
       List<Tarea> buscarTareasPorTextoNativo(@Param("texto") String texto);

       @Modifying
       @Query("UPDATE Tarea t SET t.estado = :nuevoEstado WHERE t.estado = :estadoAnterior")
       int actualizarEstadoMasivo(@Param("estadoAnterior") TaskStatus estadoAnterior,
                     @Param("nuevoEstado") TaskStatus nuevoEstado);

       @Modifying
       @Query("UPDATE Tarea t SET t.prioridad = 'ALTA' WHERE t.fechaLimite < :fecha AND t.estado = com.restaurant.sabormarcona.model.TaskStatus.PENDIENTE")
       int actualizarPrioridadTareasVencidas(@Param("fecha") LocalDateTime fecha);

       @Modifying
       @Query("DELETE FROM Tarea t WHERE t.estado = com.restaurant.sabormarcona.model.TaskStatus.COMPLETADA AND t.fechaLimite < :fecha")
       int eliminarTareasCompletadasAntiguas(@Param("fecha") LocalDateTime fecha);

       @Modifying
       @Query("UPDATE Tarea t SET t.trabajadorAsignado = :nuevoTrabajador WHERE t.trabajadorAsignado.id = :trabajadorAnteriorId")
       int reasignarTareas(@Param("trabajadorAnteriorId") Long trabajadorAnteriorId,
                     @Param("nuevoTrabajador") Usuario nuevoTrabajador);
}
