package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Tarea;
import com.restaurant.sabormarcona.model.Usuario; // Asegúrate de importar Usuario
import com.restaurant.sabormarcona.service.TareaService;
import com.restaurant.sabormarcona.service.UsuarioService; // Cambiamos PrincipalService por UsuarioService
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
    private UsuarioService usuarioService; // Inyectamos el servicio de usuarios
    
    @GetMapping
    public String listarTareas(Model model) {
        model.addAttribute("tareas", tareaService.obtenerTodasLasTareas());
        model.addAttribute("trabajadores", usuarioService.obtenerTodos()); // Obtenemos todos los usuarios
        model.addAttribute("totalTareas", tareaService.obtenerTodasLasTareas().size());
        model.addAttribute("tareasPendientes", tareaService.contarTareasPendientes());
        return "vista/tarea"; 
    }

    @PostMapping("/agregar")
    public String agregarTarea(@ModelAttribute Tarea tarea,
                              @RequestParam("fechaLimiteStr") String fechaLimiteStr,
                              @RequestParam("trabajadorId") Long trabajadorId, // Recibimos el ID del trabajador
                              RedirectAttributes redirectAttributes) {
        try {
            // Buscamos el usuario (trabajador) en la base de datos
            Usuario trabajador = usuarioService.findById(trabajadorId)
            .orElseThrow(() -> new IllegalArgumentException("Trabajador no válido Id:" + trabajadorId));

            // Asignamos el objeto Usuario completo a la tarea
            tarea.setTrabajadorAsignado(trabajador);

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
                                @RequestParam("trabajadorId") Long trabajadorId, // También recibimos el ID al actualizar
                                RedirectAttributes redirectAttributes) {
        try {
            // Buscamos y asignamos el trabajador
            Usuario trabajador = usuarioService.findById(trabajadorId)
    .orElseThrow(() -> new IllegalArgumentException("Trabajador no válido Id:" + trabajadorId));
            tarea.setTrabajadorAsignado(trabajador);

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