package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.MenuCategory;
import com.restaurant.sabormarcona.model.MenuItem;
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
    public MenuController(MenuService menuService) { this.menuService = menuService; }

    // LISTADO + FILTRO
    @GetMapping
    public String listado(@RequestParam(value = "categoryId", required = false) Long categoryId,
                        Model model,
                        HttpSession session,
                        HttpServletRequest request) {
        List<MenuCategory> categorias = menuService.findAllCategories();
        List<MenuItem> items = (categoryId == null) ?
                menuService.findAllItems() : menuService.findItemsByCategory(categoryId);

        model.addAttribute("categorias", categorias);
        model.addAttribute("items", items);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("currentUri", request.getRequestURI()); // <-- agrega esto
        return "vista/menu";
    }

    // FORM AGREGAR (GET)
    @GetMapping("/add")
    public String formAgregar(Model model) {
        model.addAttribute("categorias", menuService.findAllCategories());
        return "vista/menu_form"; // templates/menu_form.html
    }

    // AGREGAR (POST)
    @PostMapping("/add")
    public String agregar(@RequestParam String nombre,
                          @RequestParam String precio,
                          @RequestParam(required = false) String descripcion,
                          @RequestParam Long categoriaId,
                          @RequestParam(defaultValue = "false") boolean disponible,
                          RedirectAttributes ra) {
        // Validación simple 
        if (nombre == null || nombre.trim().isEmpty()) {
            ra.addFlashAttribute("mensaje", "El nombre es obligatorio");
            ra.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/menu/add";
        }
        BigDecimal p;
        try { p = new BigDecimal(precio); }
        catch(Exception ex) {
            ra.addFlashAttribute("mensaje", "Precio inválido");
            ra.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/menu/add";
        }
        Optional<MenuCategory> cat = menuService.findCategoryById(categoriaId);
        if (cat.isEmpty()) {
            ra.addFlashAttribute("mensaje", "Categoría no encontrada");
            ra.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/menu/add";
        }
        menuService.addItem(nombre.trim(), p, (descripcion==null? "" : descripcion.trim()), cat.get(), disponible);
        ra.addFlashAttribute("mensaje", "Platillo agregado");
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/menu";
    }

    // ELIMINAR ÍTEM (simulado)
    @PostMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        if (menuService.removeItem(id)) {
            ra.addFlashAttribute("mensaje", "Platillo eliminado");
            ra.addFlashAttribute("tipoMensaje", "info");
        } else {
            ra.addFlashAttribute("mensaje", "No se encontró el platillo");
            ra.addFlashAttribute("tipoMensaje", "warning");
        }
        return "redirect:/menu";
    }

    // GESTIONAR CATEGORÍAS (TIPOS)
    @GetMapping("/categories")
    public String categorias(Model model) {
        model.addAttribute("categorias", menuService.findAllCategories());
        return "vista/menu_categories"; // templates/menu_categories.html
    }

    @PostMapping("/categories/add")
    public String addCategoria(@RequestParam String nombre, RedirectAttributes ra) {
        if (nombre == null || nombre.trim().isEmpty()) {
            ra.addFlashAttribute("mensaje", "El nombre de la categoría es obligatorio");
            ra.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/menu/categories";
        }
        menuService.addCategory(nombre.trim());
        ra.addFlashAttribute("mensaje", "Categoría agregada");
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/menu/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String delCategoria(@PathVariable Long id, RedirectAttributes ra) {
        if (menuService.deleteCategory(id)) {
            ra.addFlashAttribute("mensaje", "Categoría eliminada");
            ra.addFlashAttribute("tipoMensaje", "info");
        } else {
            ra.addFlashAttribute("mensaje", "No se pudo eliminar la categoría");
            ra.addFlashAttribute("tipoMensaje", "warning");
        }
        return "redirect:/menu/categories";
    }
}