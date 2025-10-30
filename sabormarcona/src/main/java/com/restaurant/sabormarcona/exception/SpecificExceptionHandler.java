package com.restaurant.sabormarcona.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class SpecificExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, 
                                                  RedirectAttributes redirectAttributes) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
        redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        return "redirect:/";
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateResourceException(DuplicateResourceException ex, 
                                                   RedirectAttributes redirectAttributes) {
        log.error("Recurso duplicado: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
        redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        return "redirect:/";
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errores.put(error.getField(), error.getDefaultMessage())
        );
        
        log.error("Errores de validación: {}", errores);
        model.addAttribute("errores", errores);
        model.addAttribute("mensaje", "Error de validación en el formulario");
        return "error/validacion";
    }
}
