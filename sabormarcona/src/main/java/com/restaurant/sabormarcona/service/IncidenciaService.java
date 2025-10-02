package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Incidencia;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncidenciaService {

    // 1. Array List Estático para simular la base de datos en memoria
    private static final List<Incidencia> incidencias = new ArrayList<>();
    private static Long nextId = 1L;

    // Bloque estático para inicializar los datos de ejemplo (simulando datos ingresados)
    static {
        incidencias.add(new Incidencia(nextId++, "Fallo en caja principal", "El POS no permite registrar ventas con tarjeta.", "Juan Pérez", "Cajero", "Alta", LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), "Pendiente"));
        incidencias.add(new Incidencia(nextId++, "Falta de stock en menú", "El chef reporta que no hay insumos para el plato del día.", "Ana García", "Supervisor", "Media", LocalDateTime.now().plusDays(3).withHour(15).withMinute(30), "En Progreso"));
        incidencias.add(new Incidencia(nextId++, "Limpieza de ventanas", "Solicitud de limpieza profunda de cristales del salón.", "Mariana Rpjas", "Mesera", "Baja", LocalDateTime.now().plusWeeks(1).withHour(9).withMinute(0), "Resuelta"));
    }

    public List<Incidencia> obtenerTodasLasIncidencias() {
        return incidencias;
    }

    public Optional<Incidencia> obtenerIncidenciaPorId(Long id) {
        return incidencias.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Guarda una nueva incidencia en el ArrayList.
     * Asigna un nuevo ID y valores por defecto (si faltan).
     */
    public Incidencia guardarNuevaIncidencia(Incidencia nuevaIncidencia) {
        // Asigna el siguiente ID
        nuevaIncidencia.setId(nextId++); 
        
        // Asignación de valores por defecto o iniciales para campos no incluidos en el formulario de registro.
        if (nuevaIncidencia.getTrabajador() == null || nuevaIncidencia.getTrabajador().isEmpty()) {
             nuevaIncidencia.setTrabajador("Recién Registrado");
        }
        if (nuevaIncidencia.getRol() == null || nuevaIncidencia.getRol().isEmpty()) {
             nuevaIncidencia.setRol("General");
        }
        // El estado y prioridad deben venir del formulario
        
        incidencias.add(nuevaIncidencia);
        return nuevaIncidencia;
    }

    /**
     * Actualiza la incidencia existente con los nuevos datos recibidos del modal de edición.
     */
    public Incidencia modificarIncidencia(Incidencia incidenciaModificada) {
        // Busca la incidencia original por ID
        return obtenerIncidenciaPorId(incidenciaModificada.getId())
            .map(incidenciaOriginal -> {
                // Actualiza todos los campos editables
                incidenciaOriginal.setTitulo(incidenciaModificada.getTitulo());
                incidenciaOriginal.setDescripcion(incidenciaModificada.getDescripcion());
                incidenciaOriginal.setPrioridad(incidenciaModificada.getPrioridad());
                incidenciaOriginal.setFechaLimite(incidenciaModificada.getFechaLimite());
                incidenciaOriginal.setEstado(incidenciaModificada.getEstado());
                
                // Mantiene el Trabajador y Rol (vienen de los campos ocultos del modal de edición para no perderlos)
                incidenciaOriginal.setTrabajador(incidenciaModificada.getTrabajador());
                incidenciaOriginal.setRol(incidenciaModificada.getRol());
                
                return incidenciaOriginal;
            })
            .orElse(null);
    }

    public void eliminarIncidencia(Long id) {
        incidencias.removeIf(i -> i.getId().equals(id));
    }
}