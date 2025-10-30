package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.exception.ResourceNotFoundException;
import com.restaurant.sabormarcona.model.Tarea;
import com.restaurant.sabormarcona.model.TaskStatus;
import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TareaService {

    private final TareaRepository tareaRepository;

    // CREATE
    public Tarea agregarTarea(Tarea tarea) {
        log.debug("Agregando nueva tarea: {}", tarea.getTitulo());

        if (tarea.getEstado() == null) {
            tarea.setEstado(TaskStatus.PENDIENTE);
        }

        Tarea tareaGuardada = tareaRepository.save(tarea);
        log.info("Tarea creada exitosamente con ID: {}", tareaGuardada.getId());
        return tareaGuardada;
    }

    // READ
    @Transactional(readOnly = true)
    public List<Tarea> obtenerTodasLasTareas() {
        log.debug("Obteniendo todas las tareas");
        return tareaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tarea obtenerTareaPorId(Long id) {
        log.debug("Buscando tarea con ID: {}", id);
        return tareaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tarea no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Tarea no encontrada con ID: " + id);
                });
    }

    @Transactional(readOnly = true)
    public List<Tarea> obtenerTareasPorEstado(TaskStatus estado) {
        log.debug("Obteniendo tareas con estado: {}", estado);
        return tareaRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Tarea> obtenerTareasPorTrabajador(Usuario trabajador) {
        log.debug("Obteniendo tareas del trabajador: {}", trabajador.getNombre());
        return tareaRepository.findByTrabajadorAsignado(trabajador);
    }

    // UPDATE
    public Tarea modificarTarea(Tarea tareaModificada) {
        log.debug("Modificando tarea con ID: {}", tareaModificada.getId());

        Tarea tareaExistente = obtenerTareaPorId(tareaModificada.getId());

        tareaExistente.setTitulo(tareaModificada.getTitulo());
        tareaExistente.setDescripcion(tareaModificada.getDescripcion());
        tareaExistente.setPrioridad(tareaModificada.getPrioridad());
        tareaExistente.setFechaLimite(tareaModificada.getFechaLimite());
        tareaExistente.setEstado(tareaModificada.getEstado());
        tareaExistente.setTrabajadorAsignado(tareaModificada.getTrabajadorAsignado());

        Tarea tareaActualizada = tareaRepository.save(tareaExistente);
        log.info("Tarea actualizada exitosamente con ID: {}", tareaActualizada.getId());
        return tareaActualizada;
    }

    public Tarea cambiarEstado(Long id, TaskStatus nuevoEstado) {
        log.debug("Cambiando estado de tarea ID: {} a {}", id, nuevoEstado);

        Tarea tarea = obtenerTareaPorId(id);
        tarea.setEstado(nuevoEstado);

        return tareaRepository.save(tarea);
    }

    // DELETE
    public void eliminarTarea(Long id) {
        log.debug("Eliminando tarea con ID: {}", id);

        if (!tareaRepository.existsById(id)) {
            log.warn("Intento de eliminar tarea inexistente con ID: {}", id);
            throw new ResourceNotFoundException("Tarea no encontrada con ID: " + id);
        }

        tareaRepository.deleteById(id);
        log.info("Tarea eliminada exitosamente con ID: {}", id);
    }

    // MÉTODOS ADICIONALES
    @Transactional(readOnly = true)
    public long contarTareasPendientes() {
        return tareaRepository.countByEstado(TaskStatus.PENDIENTE);
    }

    @Transactional(readOnly = true)
    public long contarTareasPorEstado(TaskStatus estado) {
        return tareaRepository.countByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Tarea> obtenerTareasPendientesProximas() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime tresDiasDespues = ahora.plusDays(3);
        log.debug("Obteniendo tareas pendientes entre {} y {}", ahora, tresDiasDespues);
        return tareaRepository.findTareasPendientesProximas(ahora, tresDiasDespues);
    }

    @Transactional(readOnly = true)
    public List<Tarea> obtenerTareasVencidas() {
        LocalDateTime ahora = LocalDateTime.now();
        log.debug("Obteniendo tareas vencidas antes de {}", ahora);
        return tareaRepository.findByFechaLimiteBefore(ahora);
    }
}
