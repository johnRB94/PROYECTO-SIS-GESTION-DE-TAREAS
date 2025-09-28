package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Trabajador;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrabajadorService {
    
    private final List<Trabajador> trabajadores = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(1);
    
    public TrabajadorService() {
        inicializarTrabajadores();
    }
    
    private void inicializarTrabajadores() {
        agregarTrabajador(new Trabajador("Carlos García", "Chef Principal"));
        agregarTrabajador(new Trabajador("Mariana Rojas", "Mesera"));
        agregarTrabajador(new Trabajador("Pedro Sánchez", "Cocinero"));
        agregarTrabajador(new Trabajador("Ana López", "Bartender"));
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