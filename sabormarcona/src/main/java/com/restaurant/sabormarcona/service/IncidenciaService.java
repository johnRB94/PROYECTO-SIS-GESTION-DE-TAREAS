package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.exception.ResourceNotFoundException;
import com.restaurant.sabormarcona.model.Incidencia;
import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.repository.IncidenciaRepository;
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
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final UsuarioService usuarioService;

    // CREATE
    public Incidencia guardarNuevaIncidencia(Incidencia nuevaIncidencia) {
        log.debug("Guardando nueva incidencia: {}", nuevaIncidencia.getTitulo());

        if (nuevaIncidencia.getTrabajador() == null) {
            log.debug("No se especificó trabajador, asignando usuario por defecto (ID: 1)");
            try {
                Usuario usuarioPorDefecto = usuarioService.findById(1L);
                nuevaIncidencia.setTrabajador(usuarioPorDefecto);
            } catch (ResourceNotFoundException e) {
                log.warn("No se pudo asignar trabajador por defecto: {}", e.getMessage());
            }
        }

        Incidencia incidenciaGuardada = incidenciaRepository.save(nuevaIncidencia);
        log.info("Incidencia creada exitosamente con ID: {}", incidenciaGuardada.getId());
        return incidenciaGuardada;
    }

    // READ
    @Transactional(readOnly = true)
    public List<Incidencia> obtenerTodasLasIncidencias() {
        log.debug("Obteniendo todas las incidencias");
        return incidenciaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Incidencia obtenerIncidenciaPorId(Long id) {
        log.debug("Buscando incidencia con ID: {}", id);
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Incidencia no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Incidencia no encontrada con ID: " + id);
                });
    }

    @Transactional(readOnly = true)
    public List<Incidencia> obtenerIncidenciasPorEstado(String estado) {
        log.debug("Obteniendo incidencias con estado: {}", estado);
        return incidenciaRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Incidencia> obtenerIncidenciasPorTrabajador(Usuario trabajador) {
        log.debug("Obteniendo incidencias del trabajador: {}", trabajador.getNombre());
        return incidenciaRepository.findByTrabajador(trabajador);
    }

    // UPDATE
    public Incidencia modificarIncidencia(Incidencia incidenciaModificada) {
        log.debug("Modificando incidencia con ID: {}", incidenciaModificada.getId());

        Incidencia incidenciaExistente = obtenerIncidenciaPorId(incidenciaModificada.getId());

        incidenciaExistente.setTitulo(incidenciaModificada.getTitulo());
        incidenciaExistente.setDescripcion(incidenciaModificada.getDescripcion());
        incidenciaExistente.setPrioridad(incidenciaModificada.getPrioridad());
        incidenciaExistente.setFechaLimite(incidenciaModificada.getFechaLimite());
        incidenciaExistente.setEstado(incidenciaModificada.getEstado());

        if (incidenciaModificada.getTrabajador() != null) {
            incidenciaExistente.setTrabajador(incidenciaModificada.getTrabajador());
        }

        Incidencia incidenciaActualizada = incidenciaRepository.save(incidenciaExistente);
        log.info("Incidencia actualizada exitosamente con ID: {}", incidenciaActualizada.getId());
        return incidenciaActualizada;
    }

    public Incidencia cambiarEstado(Long id, String nuevoEstado) {
        log.debug("Cambiando estado de incidencia ID: {} a {}", id, nuevoEstado);

        Incidencia incidencia = obtenerIncidenciaPorId(id);
        incidencia.setEstado(nuevoEstado);

        return incidenciaRepository.save(incidencia);
    }

    // DELETE
    public void eliminarIncidencia(Long id) {
        log.debug("Eliminando incidencia con ID: {}", id);

        if (!incidenciaRepository.existsById(id)) {
            log.warn("Intento de eliminar incidencia inexistente con ID: {}", id);
            throw new ResourceNotFoundException("Incidencia no encontrada con ID: " + id);
        }

        incidenciaRepository.deleteById(id);
        log.info("Incidencia eliminada exitosamente con ID: {}", id);
    }

    // MÉTODOS ADICIONALES
    @Transactional(readOnly = true)
    public List<Incidencia> obtenerIncidenciasVencidas() {
        LocalDateTime ahora = LocalDateTime.now();
        log.debug("Obteniendo incidencias vencidas antes de {}", ahora);
        return incidenciaRepository.findIncidenciasVencidas(ahora);
    }

    @Transactional(readOnly = true)
    public List<Incidencia> obtenerIncidenciasPorPrioridad(String prioridad) {
        log.debug("Obteniendo incidencias con prioridad: {}", prioridad);
        return incidenciaRepository.findByPrioridad(prioridad);
    }
}
