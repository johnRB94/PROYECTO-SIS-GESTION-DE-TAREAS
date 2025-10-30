package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.TaskStatus;
import com.restaurant.sabormarcona.model.Usuario;
import com.restaurant.sabormarcona.service.IncidenciaService;
import com.restaurant.sabormarcona.service.InsumoService;
import com.restaurant.sabormarcona.service.TareaService;
import com.restaurant.sabormarcona.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PrincipalController {

    private final TareaService tareaService;
    private final UsuarioService usuarioService;
    private final IncidenciaService incidenciaService;
    private final InsumoService insumoService;

    @GetMapping("/principal")
    public String mostrarPrincipal(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("GET /principal - Mostrando página principal");

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            log.warn("Usuario no autenticado intentando acceder a /principal");
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para acceder");
            return "redirect:/";
        }

        try {
            log.debug("Cargando estadísticas para usuario: {}", usuario.getUsername());

            long totalTareas = tareaService.obtenerTodasLasTareas().size();
            long tareasPendientes = tareaService.contarTareasPorEstado(TaskStatus.PENDIENTE);
            long tareasEnProgreso = tareaService.contarTareasPorEstado(TaskStatus.EN_PROGRESO);
            long tareasCompletadas = tareaService.contarTareasPorEstado(TaskStatus.COMPLETADA);

            model.addAttribute("totalTareas", totalTareas);
            model.addAttribute("tareasPendientes", tareasPendientes);
            model.addAttribute("tareasEnProgreso", tareasEnProgreso);
            model.addAttribute("tareasCompletadas", tareasCompletadas);

            long totalUsuarios = usuarioService.obtenerTodos().size();
            long usuariosActivos = usuarioService.obtenerUsuariosActivos().size();

            model.addAttribute("totalUsuarios", totalUsuarios);
            model.addAttribute("usuariosActivos", usuariosActivos);

            long totalIncidencias = incidenciaService.obtenerTodasLasIncidencias().size();
            model.addAttribute("totalIncidencias", totalIncidencias);

            long totalInsumos = insumoService.obtenerTodosLosInsumos().size();
            long insumosBajoStock = insumoService.obtenerInsumosConBajoStock(10).size();

            model.addAttribute("totalInsumos", totalInsumos);
            model.addAttribute("insumosBajoStock", insumosBajoStock);

            model.addAttribute("tareasPróximas", tareaService.obtenerTareasPendientesProximas());

            model.addAttribute("usuarioActual", usuario);

            log.info("Dashboard cargado exitosamente para usuario: {}", usuario.getUsername());

            return "vista/principal";

        } catch (Exception e) {
            log.error("Error al cargar la página principal para usuario: {}", usuario.getUsername(), e);
            model.addAttribute("mensaje", "Error al cargar el dashboard");
            model.addAttribute("tipoMensaje", "danger");
            return "error/error";
        }
    }
}
