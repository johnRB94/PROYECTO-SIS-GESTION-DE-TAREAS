package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.exception.ResourceNotFoundException;
import com.restaurant.sabormarcona.model.Incidencia;
import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.service.IncidenciaService;
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

import java.util.List;

@Controller
@RequestMapping("/incidencias")
@RequiredArgsConstructor
@Slf4j
public class IncidenciaController {

    private final IncidenciaService incidenciaService;
    private final UsuarioService usuarioService;

    private boolean verificarAutenticacion(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            log.warn("Usuario no autenticado intentando acceder a /incidencias");
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para acceder");
            return false;
        }
        return true;
    }

    // LISTAR TODAS LAS INCIDENCIAS
    @GetMapping
    public String listarIncidencias(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("GET /incidencias - Listando todas las incidencias");

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            List<Incidencia> incidencias = incidenciaService.obtenerTodasLasIncidencias();
            List<Usuario> trabajadores = usuarioService.obtenerUsuariosActivos();

            model.addAttribute("incidencias", incidencias);
            model.addAttribute("trabajadores", trabajadores);
            model.addAttribute("totalIncidencias", incidencias.size());

            return "vista/incidencia";

        } catch (Exception e) {
            log.error("Error al listar incidencias", e);
            model.addAttribute("mensaje", "Error al cargar las incidencias");
            model.addAttribute("tipoMensaje", "danger");
            return "vista/incidencia";
        }
    }

    // VER DETALLE DE INCIDENCIA
    @GetMapping("/{id}")
    public String verDetalleIncidencia(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("GET /incidencias/{} - Ver detalle de incidencia", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            Incidencia incidencia = incidenciaService.obtenerIncidenciaPorId(id);
            model.addAttribute("incidencia", incidencia);
            return "vista/incidencia-detalle";
        } catch (ResourceNotFoundException e) {
            log.warn("Incidencia no encontrada: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Incidencia no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/incidencias";
        }
    }

    // FORMULARIO NUEVA INCIDENCIA
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaIncidencia(Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("GET /incidencias/nueva - Mostrar formulario de nueva incidencia");

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        model.addAttribute("incidencia", new Incidencia());
        model.addAttribute("trabajadores", usuarioService.obtenerUsuariosActivos());
        model.addAttribute("accion", "Crear");
        return "vista/incidencia-formulario";
    }

    // CREAR INCIDENCIA
    @PostMapping
    public String crearIncidencia(@Valid @ModelAttribute("incidencia") Incidencia incidencia,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("POST /incidencias - Creando nueva incidencia: {}", incidencia.getTitulo());

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al crear incidencia: {}", result.getAllErrors());
            model.addAttribute("trabajadores", usuarioService.obtenerUsuariosActivos());
            model.addAttribute("accion", "Crear");
            return "vista/incidencia-formulario";
        }

        try {
            // El trabajador asignado viene del formulario (seleccionado por el usuario)
            if (incidencia.getTrabajador() != null && incidencia.getTrabajador().getId() != null) {
                Usuario trabajadorAsignado = usuarioService.findById(incidencia.getTrabajador().getId());
                incidencia.setTrabajador(trabajadorAsignado);
            }

            Incidencia incidenciaGuardada = incidenciaService.guardarNuevaIncidencia(incidencia);
            log.info("Incidencia creada exitosamente con ID: {}", incidenciaGuardada.getId());

            redirectAttributes.addFlashAttribute("mensaje", "Incidencia creada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/incidencias";

        } catch (Exception e) {
            log.error("Error al crear incidencia", e);
            model.addAttribute("mensaje", "Error al crear la incidencia: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            model.addAttribute("trabajadores", usuarioService.obtenerUsuariosActivos());
            model.addAttribute("accion", "Crear");
            return "vista/incidencia-formulario";
        }
    }

    // FORMULARIO EDITAR INCIDENCIA
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarIncidencia(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("GET /incidencias/{}/editar - Mostrar formulario de edición", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            Incidencia incidencia = incidenciaService.obtenerIncidenciaPorId(id);
            model.addAttribute("incidencia", incidencia);
            model.addAttribute("trabajadores", usuarioService.obtenerUsuariosActivos());
            model.addAttribute("accion", "Editar");
            return "vista/incidencia-formulario";

        } catch (ResourceNotFoundException e) {
            log.warn("Incidencia no encontrada para editar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Incidencia no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/incidencias";
        }
    }

    // ACTUALIZAR INCIDENCIA
    @PostMapping("/{id}/editar")
    public String actualizarIncidencia(@PathVariable Long id,
            @Valid @ModelAttribute("incidencia") Incidencia incidencia,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("POST /incidencias/{}/editar - Actualizando incidencia", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al actualizar incidencia: {}", result.getAllErrors());
            model.addAttribute("trabajadores", usuarioService.obtenerUsuariosActivos());
            model.addAttribute("accion", "Editar");
            return "vista/incidencia-formulario";
        }

        try {
            incidencia.setId(id);

            // El trabajador asignado viene del formulario (seleccionado por el usuario)
            if (incidencia.getTrabajador() != null && incidencia.getTrabajador().getId() != null) {
                Usuario trabajadorAsignado = usuarioService.findById(incidencia.getTrabajador().getId());
                incidencia.setTrabajador(trabajadorAsignado);
            }

            incidenciaService.modificarIncidencia(incidencia);
            log.info("Incidencia actualizada exitosamente: {}", id);

            redirectAttributes.addFlashAttribute("mensaje", "Incidencia actualizada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/incidencias";

        } catch (ResourceNotFoundException e) {
            log.warn("Incidencia no encontrada al actualizar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Incidencia no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/incidencias";
        } catch (Exception e) {
            log.error("Error al actualizar incidencia", e);
            model.addAttribute("mensaje", "Error al actualizar la incidencia: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            model.addAttribute("trabajadores", usuarioService.obtenerUsuariosActivos());
            model.addAttribute("accion", "Editar");
            return "vista/incidencia-formulario";
        }
    }

    // ELIMINAR INCIDENCIA
    @PostMapping("/{id}/eliminar")
    public String eliminarIncidencia(@PathVariable Long id, HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("POST /incidencias/{}/eliminar - Eliminando incidencia", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            incidenciaService.eliminarIncidencia(id);
            log.info("Incidencia eliminada exitosamente: {}", id);

            redirectAttributes.addFlashAttribute("mensaje", "Incidencia eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (ResourceNotFoundException e) {
            log.warn("Incidencia no encontrada al eliminar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Incidencia no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (Exception e) {
            log.error("Error al eliminar incidencia", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la incidencia");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/incidencias";
    }
}
