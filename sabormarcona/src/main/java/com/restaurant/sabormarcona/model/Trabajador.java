package com.restaurant.sabormarcona.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trabajador {
    private Long id;
    private String nombre;
    private String rol;
    private String iniciales;
    private String colorBadge;
    
    public Trabajador(String nombre, String rol) {
        this.nombre = nombre;
        this.rol = rol;
        this.iniciales = generarIniciales(nombre);
        this.colorBadge = asignarColor(rol);
    }
    
    private String generarIniciales(String nombre) {
        String[] partes = nombre.split(" ");
        StringBuilder iniciales = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isEmpty()) {
                iniciales.append(parte.charAt(0));
            }
        }
        return iniciales.toString().toUpperCase();
    }
    
    private String asignarColor(String rol) {
        switch (rol.toLowerCase()) {
            case "chef principal": return "bg-success";
            case "mesera": case "mesero": return "bg-warning";
            case "cocinero": return "bg-info";
            case "bartender": return "bg-primary";
            default: return "bg-secondary";
        }
    }
}