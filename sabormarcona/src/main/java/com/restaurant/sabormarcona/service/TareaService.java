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
    
    @Autowired
    private TareaRepository tareaRepository;

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
        return tareaRepository.save(tarea);
    }
    
    public boolean eliminarTarea(Long id) {
        if (tareaRepository.existsById(id)) {
            tareaRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Tarea modificarTarea(Tarea tareaModificada) {
        if (tareaRepository.existsById(tareaModificada.getId())) {
            return tareaRepository.save(tareaModificada);
        }
        return null;
    }
    
    public long contarTareasPendientes() {
        return tareaRepository.countByEstado(TaskStatus.PENDIENTE);
    }
}