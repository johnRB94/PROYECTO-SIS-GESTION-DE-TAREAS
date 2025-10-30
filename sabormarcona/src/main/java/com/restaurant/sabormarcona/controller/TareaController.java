package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.exception.ResourceNotFoundException;
import com.restaurant.sabormarcona.model.Tarea;
import com.restaurant.sabormarcona.model.TaskStatus;
import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.service.TareaService;
import com.restaurant.sabormarcona.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tareas")
@RequiredArgsConstructor
@Slf4j
public class TareaController {

    private final TareaService tareaService;
    private final UsuarioService usuarioService;

    private boolean verificarAutenticacion(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            log.warn("Usuario no autenticado intentando acceder a /tareas");
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para acceder");
            return false;
        }
        return true;
    }

    // LISTAR TODAS LAS TAREAS
    @GetMapping
    public String listarTareas(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        log.info("=== GET /tareas - Listando todas las tareas ===");

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            List<Tarea> tareas = tareaService.obtenerTodasLasTareas();
            log.info("Tareas obtenidas: {}", tareas.size());

            List<Usuario> trabajadores = usuarioService.obtenerUsuariosActivos();
            log.info("Trabajadores activos encontrados: {}", trabajadores.size());

            long pendientes = tareaService.contarTareasPorEstado(TaskStatus.PENDIENTE);
            long enProgreso = tareaService.contarTareasPorEstado(TaskStatus.EN_PROGRESO);
            long completadas = tareaService.contarTareasPorEstado(TaskStatus.COMPLETADA);

            model.addAttribute("tareas", tareas);
            model.addAttribute("trabajadores", trabajadores);
            model.addAttribute("totalTareas", tareas.size());
            model.addAttribute("pendientes", pendientes);
            model.addAttribute("enProgreso", enProgreso);
            model.addAttribute("completadas", completadas);

            log.info("Retornando vista: vista/tarea");
            return "vista/tarea";

        } catch (Exception e) {
            log.error("Error al listar tareas", e);
            model.addAttribute("mensaje", "Error al cargar las tareas");
            model.addAttribute("tipoMensaje", "danger");
            model.addAttribute("tareas", List.of());
            model.addAttribute("trabajadores", List.of());
            model.addAttribute("totalTareas", 0);
            return "vista/tarea";
        }
    }

    // PROCESAR FORMULARIO NUEVA TAREA
    @PostMapping
    public String crearTarea(@Valid @ModelAttribute("tarea") Tarea tarea,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("POST /tareas - Creando nueva tarea: {}", tarea.getTitulo());

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al crear tarea: {}", result.getAllErrors());

            String errorMessage = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Por favor completa todos los campos requeridos correctamente");

            List<Usuario> trabajadores = usuarioService.obtenerUsuariosActivos();

            model.addAttribute("tarea", tarea);
            model.addAttribute("mensaje", "Error de validación: " + errorMessage);
            model.addAttribute("tipoMensaje", "danger");
            model.addAttribute("tareas", tareaService.obtenerTodasLasTareas());
            model.addAttribute("trabajadores", trabajadores);

            return "vista/tarea";
        }

        if (tarea.getPrioridad() == null || tarea.getPrioridad().isEmpty()) {
            log.warn("Prioridad no seleccionada");

            List<Usuario> trabajadores = usuarioService.obtenerUsuariosActivos();
            model.addAttribute("tarea", tarea);
            model.addAttribute("mensaje", "Error de validación: Por favor selecciona una prioridad");
            model.addAttribute("tipoMensaje", "danger");
            model.addAttribute("tareas", tareaService.obtenerTodasLasTareas());
            model.addAttribute("trabajadores", trabajadores);

            return "vista/tarea";
        }

        try {
            if (tarea.getTrabajadorAsignado() != null && tarea.getTrabajadorAsignado().getId() != null) {
                Usuario trabajador = usuarioService.findById(tarea.getTrabajadorAsignado().getId());
                tarea.setTrabajadorAsignado(trabajador);
                log.info("Tarea asignada a trabajador: {}", trabajador.getNombre());
            }

            Tarea tareaGuardada = tareaService.agregarTarea(tarea);
            log.info("Tarea creada exitosamente con ID: {}", tareaGuardada.getId());

            redirectAttributes.addFlashAttribute("mensaje", "Tarea creada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/tareas";

        } catch (Exception e) {
            log.error("Error al crear tarea", e);

            List<Usuario> trabajadores = usuarioService.obtenerUsuariosActivos();
            model.addAttribute("tarea", tarea);
            model.addAttribute("mensaje", "Error al crear la tarea: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            model.addAttribute("tareas", tareaService.obtenerTodasLasTareas());
            model.addAttribute("trabajadores", trabajadores);

            return "vista/tarea";
        }
    }

    // PROCESAR FORMULARIO DE EDICIÓN
    @PostMapping("/{id}/editar")
    public String actualizarTarea(@PathVariable Long id,
            @Valid @ModelAttribute("tarea") Tarea tarea,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("POST /tareas/{}/editar - Actualizando tarea", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al actualizar tarea: {}", result.getAllErrors());
            List<Usuario> trabajadores = usuarioService.obtenerUsuariosActivos();
            model.addAttribute("estados", TaskStatus.values());
            model.addAttribute("trabajadores", trabajadores);
            model.addAttribute("accion", "Editar");
            return "vista/tarea-formulario";
        }

        try {
            tarea.setId(id);

            if (tarea.getTrabajadorAsignado() != null && tarea.getTrabajadorAsignado().getId() != null) {
                Usuario trabajador = usuarioService.findById(tarea.getTrabajadorAsignado().getId());
                tarea.setTrabajadorAsignado(trabajador);
            }

            tareaService.modificarTarea(tarea);
            log.info("Tarea actualizada exitosamente: {}", id);

            redirectAttributes.addFlashAttribute("mensaje", "Tarea actualizada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/tareas";

        } catch (ResourceNotFoundException e) {
            log.warn("Tarea no encontrada al actualizar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Tarea no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/tareas";
        } catch (Exception e) {
            log.error("Error al actualizar tarea", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/tareas";
        }
    }

    // ELIMINAR TAREA
    @PostMapping("/{id}/eliminar")
    public String eliminarTarea(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("POST /tareas/{}/eliminar - Eliminando tarea", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            tareaService.eliminarTarea(id);
            log.info("Tarea eliminada exitosamente: {}", id);

            redirectAttributes.addFlashAttribute("mensaje", "Tarea eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (ResourceNotFoundException e) {
            log.warn("Tarea no encontrada al eliminar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Tarea no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (Exception e) {
            log.error("Error al eliminar tarea", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/tareas";
    }

    // OBTENER DATOS DE TAREA (AJAX/JSON)
    @GetMapping("/{id}/datos")
    @ResponseBody
    public Map<String, Object> obtenerDatosTarea(@PathVariable Long id) {
        log.debug("GET /tareas/{}/datos - Obteniendo datos para editar", id);

        try {
            Tarea tarea = tareaService.obtenerTareaPorId(id);

            String fechaFormato = tarea.getFechaLimite() != null
                    ? tarea.getFechaLimite().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
                    : "";

            Map<String, Object> datos = new HashMap<>();
            datos.put("id", tarea.getId());
            datos.put("titulo", tarea.getTitulo());
            datos.put("descripcion", tarea.getDescripcion());
            datos.put("prioridad", tarea.getPrioridad());
            datos.put("fechaLimite", fechaFormato);
            datos.put("estado", tarea.getEstado());
            datos.put("trabajadorId",
                    tarea.getTrabajadorAsignado() != null ? tarea.getTrabajadorAsignado().getId() : null);

            return datos;
        } catch (Exception e) {
            log.error("Error obteniendo datos de tarea", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Tarea no encontrada");
            return error;
        }
    }

    // CAMBIAR ESTADO DE TAREA (AJAX)
    @PostMapping("/{id}/cambiar-estado")
    @ResponseBody
    public String cambiarEstado(@PathVariable Long id, @RequestParam String estado, HttpSession session) {
        log.debug("POST /tareas/{}/cambiar-estado - Cambiando estado a {}", id, estado);

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "error: no autenticado";
        }

        try {
            TaskStatus nuevoEstado = TaskStatus.valueOf(estado);
            tareaService.cambiarEstado(id, nuevoEstado);
            return "success";
        } catch (Exception e) {
            log.error("Error al cambiar estado de tarea", e);
            return "error";
        }
    }

    // FILTRAR TAREAS POR ESTADO
    @GetMapping("/filtrar")
    public String filtrarPorEstado(@RequestParam(required = false) String estado,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("GET /tareas/filtrar - Filtrando por estado: {}", estado);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            List<Tarea> tareas;
            if (estado != null && !estado.isEmpty()) {
                TaskStatus taskStatus = TaskStatus.valueOf(estado);
                tareas = tareaService.obtenerTareasPorEstado(taskStatus);
            } else {
                tareas = tareaService.obtenerTodasLasTareas();
            }

            List<Usuario> trabajadores = usuarioService.obtenerUsuariosActivos();

            model.addAttribute("tareas", tareas);
            model.addAttribute("trabajadores", trabajadores);
            model.addAttribute("totalTareas", tareas.size());
            model.addAttribute("estadoFiltro", estado);

            return "vista/tarea";

        } catch (Exception e) {
            log.error("Error al filtrar tareas", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al filtrar tareas");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/tareas";
        }
    }
}
