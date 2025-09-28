package com.restaurant.sabormarcona.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {
    private Long id;
    private String titulo;
    private String descripcion;
    private String trabajador;
    private String rol;
    private String prioridad;
    private LocalDateTime fechaLimite;
    private String estado;
    
    // Constructor adicional para facilitar la creación
    public Tarea(String titulo, String descripcion, String trabajador, 
                 String rol, String prioridad, LocalDateTime fechaLimite) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.trabajador = trabajador;
        this.rol = rol;
        this.prioridad = prioridad;
        this.fechaLimite = fechaLimite;
        this.estado = "Pendiente";
    }
}