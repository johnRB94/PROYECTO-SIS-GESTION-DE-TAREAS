package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Menu;
import com.restaurant.sabormarcona.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;
    public MenuController (MenuService menuService) { this.menuService = menuService; }

    @GetMapping
    public String listado (@RequestParam( value = "categoryId", required = false) Long categoryId,
                           Model model,
                           HttpSession session,
                           HttpServletRequest request) {
        List<Menu> categorias = menuService.findAllCategories();
        // CORRECCIÓN: Se usa List<Menu.MenuItem>
        List<Menu.MenuItem> items = (categoryId == null) ?
                menuService.findAllItems() : menuService.findItemsByCategory(categoryId);
        model.addAttribute("categorias", categorias);
        model.addAttribute("items", items);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("currentUri", request.getRequestURI());
        return "vista/menu";
    }
    
    // AGREGAR ÍTEM (POST)
    @PostMapping("/add")
    public String agregar (@RequestParam String nombre,
                           @RequestParam String precio,
                           @RequestParam( required = false) String descripcion,
                           @RequestParam Long categoriaId,
                           @RequestParam( defaultValue = "false") boolean disponible,
                           RedirectAttributes ra) {
        // Validación simple
        if (nombre == null || nombre.trim().isEmpty()) {
            ra.addFlashAttribute("mensaje", "El nombre es obligatorio");
            ra.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/menu";
        }
        BigDecimal p;
        try { p = new BigDecimal(precio); }
        catch(Exception ex) {
            ra.addFlashAttribute("mensaje", "Precio inválido");
            ra.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/menu";
        }
        Optional<Menu> cat = menuService.findCategoryById(categoriaId);
        if (cat.isEmpty()) {
            ra.addFlashAttribute("mensaje", "Categoría no encontrada");
            ra.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/menu";
        }
        menuService.addItem(nombre.trim(), p, (descripcion==null? "" : descripcion.trim()), cat.get(), disponible);
        ra.addFlashAttribute("mensaje", "Platillo agregado");
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/menu";
    }

    // ELIMINAR ÍTEM (simulado)
    @PostMapping("/delete/{id}")
    public String eliminar (@PathVariable Long id, RedirectAttributes ra) {
        if (menuService.removeItem(id)) {
            ra.addFlashAttribute("mensaje", "Platillo eliminado");
            ra.addFlashAttribute("tipoMensaje", "info");
        } else {
            ra.addFlashAttribute("mensaje", "No se encontró el platillo");
            ra.addFlashAttribute("tipoMensaje", "warning");
        }
        return "redirect:/menu";
    }
    
    // GESTIONAR CATEGORÍAS (POST ADD)
    @PostMapping("/categories/add")
    public String addCategoria (@RequestParam String nombre, RedirectAttributes ra) {
        if (nombre == null || nombre.trim().isEmpty()) {
            ra.addFlashAttribute("mensaje", "El nombre de la categoría es obligatorio");
            ra.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/menu";
        }
        menuService.addCategory(nombre.trim());
        ra.addFlashAttribute("mensaje", "Categoría agregada");
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/menu";
    }

    // GESTIONAR CATEGORÍAS (POST DELETE)
    @PostMapping("/categories/delete/{id}")
    public String delCategoria (@PathVariable Long id, RedirectAttributes ra) {
        if (menuService.deleteCategory(id)) {
            ra.addFlashAttribute("mensaje", "Categoría eliminada");
            ra.addFlashAttribute("tipoMensaje", "info");
        } else {
            ra.addFlashAttribute("mensaje", "No se pudo eliminar la categoría");
            ra.addFlashAttribute("tipoMensaje", "warning");
        }
        return "redirect:/menu";
    }
}