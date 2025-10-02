package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Tarea;
import com.restaurant.sabormarcona.service.TareaService;
import com.restaurant.sabormarcona.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/tareas")
public class TareaController {
    
    @Autowired
    private TareaService tareaService;
    
    @Autowired
    private PrincipalService trabajadorService;
    
    @GetMapping
    public String listarTareas(Model model) {
        model.addAttribute("tareas", tareaService.obtenerTodasLasTareas());
        model.addAttribute("trabajadores", trabajadorService.obtenerTodosTrabajadores());
        model.addAttribute("totalTareas", tareaService.obtenerTodasLasTareas().size());
        model.addAttribute("tareasPendientes", tareaService.contarTareasPendientes());
        return "vista/tarea"; 
    }

    @PostMapping("/agregar")
    public String agregarTarea(@ModelAttribute Tarea tarea,
                              @RequestParam("fechaLimiteStr") String fechaLimiteStr,
                              RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime fechaLimite = LocalDateTime.parse(fechaLimiteStr);
            tarea.setFechaLimite(fechaLimite);

            tareaService.agregarTarea(tarea);
            redirectAttributes.addFlashAttribute("mensaje", "Tarea agregada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al agregar la tarea: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/tareas"; 
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (tareaService.eliminarTarea(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Tarea eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "info");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "No se pudo eliminar la tarea");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/tareas"; 
    }

    @GetMapping("/obtener/{id}")
    @ResponseBody
    public Tarea obtenerTareaJson(@PathVariable Long id) {
        return tareaService.obtenerTareaPorId(id).orElse(null);
    }

    @PostMapping("/actualizar")
    public String actualizarTarea(@ModelAttribute Tarea tarea,
                                @RequestParam("fechaLimiteStr") String fechaLimiteStr,
                                RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime fechaLimite = LocalDateTime.parse(fechaLimiteStr);
            tarea.setFechaLimite(fechaLimite);
            
            Tarea tareaActualizada = tareaService.modificarTarea(tarea);
            
            if (tareaActualizada != null) {
                redirectAttributes.addFlashAttribute("mensaje", "Tarea actualizada exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Error: No se encontró la tarea para actualizar.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar la tarea: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/tareas";
    }
}
