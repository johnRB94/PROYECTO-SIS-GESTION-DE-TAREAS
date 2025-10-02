package com.restaurant.sabormarcona.controller;

import com.restaurant.sabormarcona.model.Insumo;
import com.restaurant.sabormarcona.service.InsumoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid; 

@Controller
@RequestMapping("/insumo")
public class InsumoController {
    
    private final InsumoService insumoService;

    public InsumoController (InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @GetMapping
    public String listarInsumos (Model model) {
        model.addAttribute ("insumos", insumoService.obtenerTodosLosInsumos ());
        model.addAttribute ("insumo", new Insumo ()); 
        return "vista/insumo";
    }

    @PostMapping("/agregar")
    public String agregarInsumo (@Valid @ModelAttribute("insumo") Insumo insumo,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors ()) {
            model.addAttribute ("insumos", insumoService.obtenerTodosLosInsumos ());
            return "vista/insumo";
        }
        insumoService.agregarInsumo (insumo);
        redirectAttributes.addFlashAttribute ("success", "Insumo agregado correctamente.");
        return "redirect:/insumo";
    }

    @GetMapping("/editar/{id}")
    public String editarInsumo (@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return insumoService.obtenerInsumoPorId (id)
            .map (insumo -> {
                model.addAttribute ("insumo", insumo);
                model.addAttribute ("insumos", insumoService.obtenerTodosLosInsumos ());
                return "vista/insumo";
            })
            .orElseGet (() -> {
                redirectAttributes.addFlashAttribute ("error", "El insumo no existe.");
                return "redirect:/insumo";
            });
    }

    @PostMapping("/editar")
    public String actualizarInsumo (@Valid @ModelAttribute("insumo") Insumo insumo,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors ()) {
            model.addAttribute ("insumos", insumoService.obtenerTodosLosInsumos ());
            return "vista/insumo";
        }
        boolean actualizado = insumoService.actualizarInsumo (insumo);
        if (actualizado) {
            redirectAttributes.addFlashAttribute ("success", "Insumo actualizado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute ("error", "No se pudo actualizar el insumo.");
        }
        return "redirect:/insumo";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarInsumo (@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean eliminado = insumoService.eliminarInsumo (id);
        if (eliminado) {
            redirectAttributes.addFlashAttribute ("success", "Insumo eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute ("error", "El insumo no existe.");
        }
        return "redirect:/insumo";
    }
}