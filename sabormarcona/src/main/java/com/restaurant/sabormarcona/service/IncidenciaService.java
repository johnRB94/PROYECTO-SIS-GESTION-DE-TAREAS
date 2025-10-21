package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Incidencia;
import com.restaurant.sabormarcona.repository.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;
    
    @Autowired
    private UsuarioService usuarioService; // Para buscar usuarios

    public List<Incidencia> obtenerTodasLasIncidencias() {
        return incidenciaRepository.findAll();
    }

    public Optional<Incidencia> obtenerIncidenciaPorId(Long id) {
        return incidenciaRepository.findById(id);
    }
    
    public Incidencia guardarNuevaIncidencia(Incidencia nuevaIncidencia) {
        // Asignamos un usuario por defecto si no se especifica uno
        if (nuevaIncidencia.getTrabajador() == null) {
             nuevaIncidencia.setTrabajador(usuarioService.findById(1L).orElse(null)); // Asigna al admin por defecto
        }
        return incidenciaRepository.save(nuevaIncidencia);
    }

    public Incidencia modificarIncidencia(Incidencia incidenciaModificada) {
        // Aseguramos que el trabajador se mantenga si no se envía uno nuevo
        Incidencia original = obtenerIncidenciaPorId(incidenciaModificada.getId()).orElse(null);
        if (original != null) {
            incidenciaModificada.setTrabajador(original.getTrabajador());
        }
        return incidenciaRepository.save(incidenciaModificada);
    }

    public void eliminarIncidencia(Long id) {
        incidenciaRepository.deleteById(id);
    }
}