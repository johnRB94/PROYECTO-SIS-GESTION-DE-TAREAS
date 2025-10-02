package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Principal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PrincipalService {
    
    private final List<Principal> trabajadores = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(1);
    
    public PrincipalService() {
        inicializarTrabajadores();
    }
    
    private void inicializarTrabajadores() {
        agregarTrabajador(new Principal("Carlos García", "Chef Principal"));
        agregarTrabajador(new Principal("Mariana Rojas", "Mesera"));
        agregarTrabajador(new Principal("Pedro Sánchez", "Cocinero"));
        agregarTrabajador(new Principal("Ana López", "Bartender"));
    }
    
    public List<Principal> obtenerTodosTrabajadores() {
        return new ArrayList<>(trabajadores);
    }
    
    public Principal agregarTrabajador(Principal trabajador) {
        trabajador.setId(contadorId.getAndIncrement());
        trabajadores.add(trabajador);
        return trabajador;
    }
}