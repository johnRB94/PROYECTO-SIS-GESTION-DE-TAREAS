package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Tarea;
import com.restaurant.sabormarcona.model.TaskStatus;
import com.restaurant.sabormarcona.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareaService {
    
<<<<<<< HEAD
    @Autowired
    private TareaRepository tareaRepository;

=======
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
    
>>>>>>> 7b7abd34a689173cbdf0d53c7baf558f1903d74e
    public List<Tarea> obtenerTodasLasTareas() {
        return tareaRepository.findAll();
    }
    
    public Optional<Tarea> obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id);
    }
    
    public Tarea agregarTarea(Tarea tarea) {
        if (tarea.getEstado() == null) {
            tarea.setEstado(TaskStatus.PENDIENTE);
        }
<<<<<<< HEAD
        return tareaRepository.save(tarea);
=======
        tareas.add(0, tarea); 
        return tarea;
>>>>>>> 7b7abd34a689173cbdf0d53c7baf558f1903d74e
    }
    
    public boolean eliminarTarea(Long id) {
        if (tareaRepository.existsById(id)) {
            tareaRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Tarea modificarTarea(Tarea tareaModificada) {
<<<<<<< HEAD
        if (tareaRepository.existsById(tareaModificada.getId())) {
            return tareaRepository.save(tareaModificada);
        }
        return null;
=======
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
>>>>>>> 7b7abd34a689173cbdf0d53c7baf558f1903d74e
    }
    
    public long contarTareasPendientes() {
        return tareaRepository.countByEstado(TaskStatus.PENDIENTE);
    }
}