package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.exception.DuplicateResourceException;
import com.restaurant.sabormarcona.exception.ResourceNotFoundException;
import com.restaurant.sabormarcona.model.Usuario;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    private boolean verificarAutenticacion(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            log.warn("Usuario no autenticado");
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión");
            return false;
        }
        return true;
    }

    // LISTAR TODOS LOS USUARIOS
    @GetMapping
    public String listarUsuarios(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("GET /usuarios - Listando todos los usuarios");

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            List<Usuario> usuarios = usuarioService.obtenerTodos();
            model.addAttribute("usuarios", usuarios);
            return "vista/usuario";
        } catch (Exception e) {
            log.error("Error al listar usuarios", e);
            model.addAttribute("mensaje", "Error al cargar los usuarios");
            model.addAttribute("tipoMensaje", "danger");
            return "error/error";
        }
    }

    // VER DETALLE DE UN USUARIO
    @GetMapping("/{id}")
    public String verDetalleUsuario(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("GET /usuarios/{} - Ver detalle de usuario", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            Usuario usuario = usuarioService.findById(id);
            model.addAttribute("usuario", usuario);
            return "vista/usuario-detalle";
        } catch (ResourceNotFoundException e) {
            log.warn("Usuario no encontrado: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/usuarios";
        }
    }

    // CREATE - MOSTRAR FORMULARIO (GET /usuarios/nuevo)
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        log.info("=== GET /usuarios/nuevo - Mostrando formulario ===");

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        model.addAttribute("usuario", new Usuario());
        return "vista/nuevo"; // Retorna templates/vista/nuevo.html
    }

    // CREATE - PROCESAR FORMULARIO (POST /usuarios/nuevo)
    @PostMapping("/nuevo")
    public String crearUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            @RequestParam(required = false) String confirmarContrasena,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        log.info("=== POST /usuarios/nuevo - Creando usuario: {} ===", usuario.getUsername());

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación: {}", result.getAllErrors());
            model.addAttribute("error", "Por favor corrija los errores en el formulario");
            return "vista/nuevo";
        }

        if (confirmarContrasena != null && !usuario.getPassword().equals(confirmarContrasena)) {
            log.warn("Las contraseñas no coinciden");
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("usuario", usuario);
            return "vista/nuevo";
        }

        try {
            usuario.setActivo(true);

            Usuario usuarioGuardado = usuarioService.agregarUsuario(usuario);

            log.info("Usuario creado exitosamente: {} (ID: {})",
                    usuarioGuardado.getUsername(), usuarioGuardado.getId());

            redirectAttributes.addFlashAttribute("registroExitoso", true);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Empleado " + usuarioGuardado.getNombre() + " registrado exitosamente");

            return "redirect:/usuarios/nuevo";

        } catch (DuplicateResourceException e) {
            log.warn("Usuario duplicado: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "vista/nuevo";
        } catch (Exception e) {
            log.error("Error al crear usuario: ", e);
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            model.addAttribute("usuario", usuario);
            return "vista/nuevo";
        }
    }

    // UPDATE - MOSTRAR FORMULARIO DE EDICIÓN
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("GET /usuarios/{}/editar - Mostrar formulario de edición", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            Usuario usuario = usuarioService.findById(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("accion", "Editar");
            return "vista/usuario-formulario";
        } catch (ResourceNotFoundException e) {
            log.warn("Usuario no encontrado para editar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/usuarios";
        }
    }

    // UPDATE - PROCESAR FORMULARIO DE EDICIÓN
    @PostMapping("/{id}/editar")
    public String actualizarUsuario(@PathVariable Long id,
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("POST /usuarios/{}/editar - Actualizando usuario", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al actualizar usuario: {}", result.getAllErrors());
            model.addAttribute("accion", "Editar");
            return "vista/usuario-formulario";
        }

        try {
            usuarioService.actualizarUsuario(id, usuario);
            log.info("Usuario actualizado exitosamente: {}", id);

            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/usuarios";

        } catch (ResourceNotFoundException e) {
            log.warn("Usuario no encontrado al actualizar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/usuarios";
        } catch (DuplicateResourceException e) {
            log.warn("Datos duplicados al actualizar usuario: {}", e.getMessage());
            model.addAttribute("mensaje", e.getMessage());
            model.addAttribute("tipoMensaje", "warning");
            model.addAttribute("accion", "Editar");
            return "vista/usuario-formulario";
        } catch (Exception e) {
            log.error("Error al actualizar usuario", e);
            model.addAttribute("mensaje", "Error al actualizar el usuario");
            model.addAttribute("tipoMensaje", "danger");
            model.addAttribute("accion", "Editar");
            return "vista/usuario-formulario";
        }
    }

    // DELETE - ELIMINAR USUARIO
    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("POST /usuarios/{}/eliminar - Eliminando usuario", id);

        if (!verificarAutenticacion(session, redirectAttributes)) {
            return "redirect:/";
        }

        try {
            usuarioService.eliminarUsuario(id);
            log.info("Usuario eliminado exitosamente: {}", id);

            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (ResourceNotFoundException e) {
            log.warn("Usuario no encontrado al eliminar: {}", id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (Exception e) {
            log.error("Error al eliminar usuario", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el usuario");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/usuarios";
    }
}
