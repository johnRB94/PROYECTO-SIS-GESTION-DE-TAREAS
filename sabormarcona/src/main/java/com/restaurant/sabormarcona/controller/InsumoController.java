package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.exception.ResourceNotFoundException;
import com.restaurant.sabormarcona.model.Insumo;
import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.service.InsumoService;
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
@RequestMapping("/insumos")
@RequiredArgsConstructor
@Slf4j
public class InsumoController {

    private final InsumoService insumoService;

    private boolean verificarAutenticacion(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            log.warn("Usuario no autenticado intentando acceder a /insumos");
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para acceder");
            return false;
        }
        return true;
    }

    // LISTAR TODOS LOS INSUMOS
    @GetMapping
    public String listarInsumos(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("GET /insumos - Listando todos los insumos");

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            List<Insumo> insumos = insumoService.obtenerTodosLosInsumos();
            List<Insumo> insumosBajoStock = insumoService.obtenerInsumosConBajoStock(10);

            model.addAttribute("insumos", insumos);
            model.addAttribute("insumosBajoStock", insumosBajoStock);
            model.addAttribute("totalInsumos", insumos.size());
            model.addAttribute("cantidadBajoStock", insumosBajoStock.size());

            return "vista/insumo";

        } catch (Exception e) {
            log.error("Error al listar insumos", e);
            model.addAttribute("mensaje", "Error al cargar los insumos");
            model.addAttribute("tipoMensaje", "danger");
            return "vista/insumo";
        }
    }

    // FORMULARIO NUEVO INSUMO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoInsumo(Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("GET /insumos/nuevo - Mostrar formulario de nuevo insumo");

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        model.addAttribute("insumo", new Insumo());
        model.addAttribute("accion", "Crear");
        return "vista/insumo-formulario"; 
    }

    // CREAR INSUMO
    @PostMapping
    public String crearInsumo(@Valid @ModelAttribute("insumo") Insumo insumo,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("POST /insumos - Creando nuevo insumo: {}", insumo.getNombre());

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al crear insumo: {}", result.getAllErrors());
            model.addAttribute("accion", "Crear");
            return "vista/insumo-formulario";
        }

        try {
            Insumo insumoGuardado = insumoService.agregarInsumo(insumo);
            log.info("Insumo creado exitosamente con ID: {}", insumoGuardado.getId());

            redirectAttributes.addFlashAttribute("mensaje", "Insumo creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/insumos";

        } catch (Exception e) {
            log.error("Error al crear insumo", e);
            model.addAttribute("mensaje", "Error al crear el insumo: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            model.addAttribute("accion", "Crear");
            return "vista/insumo-formulario";
        }
    }

    // FORMULARIO EDITAR INSUMO
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarInsumo(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("GET /insumos/{}/editar - Mostrar formulario de edición", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            Insumo insumo = insumoService.obtenerInsumoPorId(id);
            model.addAttribute("insumo", insumo);
            model.addAttribute("accion", "Editar");
            return "vista/insumo-formulario";

        } catch (ResourceNotFoundException e) {
            log.warn("Insumo no encontrado para editar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Insumo no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/insumos";
        }
    }

    // ACTUALIZAR INSUMO
    @PostMapping("/{id}/editar")
    public String actualizarInsumo(@PathVariable Long id,
            @Valid @ModelAttribute("insumo") Insumo insumo,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("POST /insumos/{}/editar - Actualizando insumo", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al actualizar insumo: {}", result.getAllErrors());
            model.addAttribute("accion", "Editar");
            return "vista/insumo-formulario";
        }

        try {
            insumo.setId(id);
            insumoService.actualizarInsumo(insumo);
            log.info("Insumo actualizado exitosamente: {}", id);

            redirectAttributes.addFlashAttribute("mensaje", "Insumo actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/insumos";

        } catch (ResourceNotFoundException e) {
            log.warn("Insumo no encontrado al actualizar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Insumo no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/insumos";
        } catch (Exception e) {
            log.error("Error al actualizar insumo", e);
            model.addAttribute("mensaje", "Error al actualizar el insumo: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            model.addAttribute("accion", "Editar");
            return "vista/insumo-formulario";
        }
    }

    // ELIMINAR INSUMO
    @PostMapping("/{id}/eliminar")
    public String eliminarInsumo(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("POST /insumos/{}/eliminar - Eliminando insumo", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            insumoService.eliminarInsumo(id);
            log.info("Insumo eliminado exitosamente: {}", id);

            redirectAttributes.addFlashAttribute("mensaje", "Insumo eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (ResourceNotFoundException e) {
            log.warn("Insumo no encontrado al eliminar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Insumo no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (Exception e) {
            log.error("Error al eliminar insumo", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el insumo");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/insumos";
    }

    // OPERACIONES ADICIONALES

    @PostMapping("/{id}/actualizar-stock")
    public String actualizarStock(@PathVariable Long id,
            @RequestParam Integer cantidad,
            @RequestParam String operacion,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("POST /insumos/{}/actualizar-stock - Operación: {}, Cantidad: {}", id, operacion, cantidad);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            if ("agregar".equals(operacion)) {
                insumoService.incrementarStock(id, cantidad);
                redirectAttributes.addFlashAttribute("mensaje", "Stock incrementado exitosamente");
            } else if ("restar".equals(operacion)) {
                insumoService.decrementarStock(id, cantidad);
                redirectAttributes.addFlashAttribute("mensaje", "Stock decrementado exitosamente");
            }

            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al actualizar stock", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el stock");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/insumos";
    }
}
