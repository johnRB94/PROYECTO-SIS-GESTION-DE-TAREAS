package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Incidencia;
import com.restaurant.sabormarcona.service.IncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/incidencias")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    /**
     * Muestra la tabla principal de incidencias.
     * Mapea a src/main/resources/templates/incidencia.html
     */
    @GetMapping
    public String listarIncidencias(Model model) {
        model.addAttribute("incidencias", incidenciaService.obtenerTodasLasIncidencias());
        return "vista/incidencia"; 
    }
    
    /**
     * Endpoint REST para obtener una incidencia por ID. 
     * Usado por el JavaScript del modal de edición (incidencia.js).
     * Retorna un objeto JSON.
     */
    @GetMapping("/obtener/{id}")
    @ResponseBody
    public Optional<Incidencia> obtenerIncidenciaJson(@PathVariable Long id) {
        return incidenciaService.obtenerIncidenciaPorId(id);
    }
    
    /**
     * PROCESAR REGISTRO DE NUEVA INCIDENCIA.
     * Recibe los datos del formulario del Modal de Registro.
     */
    @PostMapping("/registrar")
    public String registrarIncidencia(@ModelAttribute Incidencia incidencia, RedirectAttributes redirectAttributes) {
        try {
            if (incidencia.getTitulo() == null || incidencia.getTitulo().isEmpty()) {
                 throw new IllegalArgumentException("El título de la incidencia es obligatorio.");
            }
            
            incidenciaService.guardarNuevaIncidencia(incidencia);
            redirectAttributes.addFlashAttribute("mensaje", "Incidencia registrada exitosamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            // Captura errores de binding (como fechas) o del servicio.
            redirectAttributes.addFlashAttribute("mensaje", "Error al registrar la incidencia: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/incidencias";
    }

    /**
     * Procesar la actualización de la incidencia (desde el Modal de Edición).
     * El objeto 'incidencia' debe incluir el ID.
     */
    @PostMapping("/actualizar")
    public String actualizarIncidencia(@ModelAttribute Incidencia incidencia, RedirectAttributes redirectAttributes) {
        try {
            Incidencia incidenciaActualizada = incidenciaService.modificarIncidencia(incidencia);

            if (incidenciaActualizada != null) {
                redirectAttributes.addFlashAttribute("mensaje", "Incidencia actualizada exitosamente.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                 redirectAttributes.addFlashAttribute("mensaje", "Error: No se encontró la incidencia para actualizar.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar la incidencia: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/incidencias";
    }
    
    /**
     * Procesar la eliminación de la incidencia.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarIncidencia(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            incidenciaService.eliminarIncidencia(id);
            redirectAttributes.addFlashAttribute("mensaje", "Incidencia eliminada exitosamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la incidencia: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/incidencias";
    }
}