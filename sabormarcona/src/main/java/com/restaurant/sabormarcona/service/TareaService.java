package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Tarea;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TareaService {
    
    private final List<Tarea> tareas = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(1);
    
    public TareaService() {
        inicializarDatosPrueba();
    }
    
    private void inicializarDatosPrueba() {
        agregarTarea(new Tarea("Preparar Ceviche Mixto", 
                              "Preparar 5 porciones de ceviche mixto para el almuerzo", 
                              "Carlos García", "Chef Principal", "Alta", 
                              LocalDateTime.now().plusHours(2)));
                              
        agregarTarea(new Tarea("Atender Mesa 5", 
                              "Tomar orden y servir a la familia en mesa 5", 
                              "Mariana Rojas", "Mesera", "Media", 
                              LocalDateTime.now().plusMinutes(30)));
                              
        agregarTarea(new Tarea("Limpiar Área de Cocina", 
                              "Limpieza profunda del área de preparación", 
                              "Carlos García", "Chef Principal", "Baja", 
                              LocalDateTime.now().plusHours(4)));
    }
    
    public List<Tarea> obtenerTodasLasTareas() {
        return new ArrayList<>(tareas);
    }
    
    public Optional<Tarea> obtenerTareaPorId(Long id) {
        return tareas.stream()
                .filter(tarea -> tarea.getId().equals(id))
                .findFirst();
    }
    
    public Tarea agregarTarea(Tarea tarea) {
        tarea.setId(contadorId.getAndIncrement());
        if (tarea.getEstado() == null) {
            tarea.setEstado("Pendiente");
        }
        tareas.add(0, tarea); 
        return tarea;
    }
    
    public boolean eliminarTarea(Long id) {
        return tareas.removeIf(tarea -> tarea.getId().equals(id));
    }
    
    public Tarea modificarTarea(Tarea tareaModificada) {
        Optional<Tarea> optionalTarea = obtenerTareaPorId(tareaModificada.getId());
        
        if (optionalTarea.isPresent()) {
            Tarea tareaOriginal = optionalTarea.get();
            
            tareaOriginal.setTitulo(tareaModificada.getTitulo());
            tareaOriginal.setDescripcion(tareaModificada.getDescripcion());
            tareaOriginal.setTrabajador(tareaModificada.getTrabajador());
            tareaOriginal.setRol(tareaModificada.getRol());
            tareaOriginal.setPrioridad(tareaModificada.getPrioridad());
            tareaOriginal.setFechaLimite(tareaModificada.getFechaLimite());
            tareaOriginal.setEstado(tareaModificada.getEstado());
            
            return tareaOriginal; 
        }
        return null; 
    }
    public List<Tarea> obtenerTareasPorPrioridad(String prioridad) {
        return tareas.stream()
                .filter(tarea -> tarea.getPrioridad().equalsIgnoreCase(prioridad))
                .toList();
    }
    
    public long contarTareasPendientes() {
        return tareas.stream()
                .filter(tarea -> "Pendiente".equals(tarea.getEstado()))
                .count();
    }

}
