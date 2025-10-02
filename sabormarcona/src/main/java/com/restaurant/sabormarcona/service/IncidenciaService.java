package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Incidencia;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncidenciaService {

    private static final List<Incidencia> incidencias = new ArrayList<>();
    private static Long nextId = 1L;

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
    
    public Incidencia guardarNuevaIncidencia(Incidencia nuevaIncidencia) {
        nuevaIncidencia.setId(nextId++); 
        
        if (nuevaIncidencia.getTrabajador() == null || nuevaIncidencia.getTrabajador().isEmpty()) {
             nuevaIncidencia.setTrabajador("Recién Registrado");
        }
        if (nuevaIncidencia.getRol() == null || nuevaIncidencia.getRol().isEmpty()) {
             nuevaIncidencia.setRol("General");
        }
               
        incidencias.add(nuevaIncidencia);
        return nuevaIncidencia;
    }

    public Incidencia modificarIncidencia(Incidencia incidenciaModificada) {
        return obtenerIncidenciaPorId(incidenciaModificada.getId())
            .map(incidenciaOriginal -> {
                incidenciaOriginal.setTitulo(incidenciaModificada.getTitulo());
                incidenciaOriginal.setDescripcion(incidenciaModificada.getDescripcion());
                incidenciaOriginal.setPrioridad(incidenciaModificada.getPrioridad());
                incidenciaOriginal.setFechaLimite(incidenciaModificada.getFechaLimite());
                incidenciaOriginal.setEstado(incidenciaModificada.getEstado());
                
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