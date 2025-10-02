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

    @GetMapping
    public String listarIncidencias(Model model) {
        model.addAttribute("incidencias", incidenciaService.obtenerTodasLasIncidencias());
        return "vista/incidencia"; 
    }
    
    @GetMapping("/obtener/{id}")
    @ResponseBody
    public Optional<Incidencia> obtenerIncidenciaJson(@PathVariable Long id) {
        return incidenciaService.obtenerIncidenciaPorId(id);
    }
    
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
            redirectAttributes.addFlashAttribute("mensaje", "Error al registrar la incidencia: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/incidencias";
    }

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