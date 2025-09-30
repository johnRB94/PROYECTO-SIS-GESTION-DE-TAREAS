package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Trabajador;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import com.restaurant.sabormarcona.service.UsuarioService;
import com.restaurant.sabormarcona.model.Usuario;

@Service
public class TrabajadorService {
    
    private final List<Trabajador> trabajadores = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(1);
    @Autowired
    private UsuarioService usuarioService;
    
    public TrabajadorService() {
        inicializarTrabajadores();
    }
    
    private void inicializarTrabajadores() {
        // Agregar trabajadores fijos
        agregarTrabajador(new Trabajador("Carlos García", "Chef Principal"));
        agregarTrabajador(new Trabajador("Mariana Rojas", "Mesera"));
        agregarTrabajador(new Trabajador("Pedro Sánchez", "Cocinero"));
        agregarTrabajador(new Trabajador("Ana López", "Bartender"));
        // Agregar trabajadores desde usuarios registrados
        if (usuarioService != null) {
            for (Usuario usuario : usuarioService.obtenerTodos()) {
                // Evitar duplicados por nombre
                boolean existe = trabajadores.stream().anyMatch(t -> t.getNombre().equalsIgnoreCase(usuario.getNombre()));
                if (!existe) {
                    String rol = usuario.getRol() != null ? usuario.getRol() : "Empleado";
                    agregarTrabajador(new Trabajador(usuario.getNombre(), rol));
                }
            }
        }
    }
    
    public List<Trabajador> obtenerTodosTrabajadores() {
        return new ArrayList<>(trabajadores);
    }
    
    public Trabajador agregarTrabajador(Trabajador trabajador) {
        trabajador.setId(contadorId.getAndIncrement());
        trabajadores.add(trabajador);
        return trabajador;
    }
}