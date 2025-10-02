package com.restaurant.sabormarcona.model;

import java.time.LocalDateTime;

public class Tarea {
    private Long id;
    private String titulo;
    private String descripcion;
    private String trabajador;
    private String rol;
    private String prioridad;
    private LocalDateTime fechaLimite;
    private String estado;

    public Tarea() {
    }

    public Tarea(Long id, String titulo, String descripcion, String trabajador,
                 String rol, String prioridad, LocalDateTime fechaLimite, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.trabajador = trabajador;
        this.rol = rol;
        this.prioridad = prioridad;
        this.fechaLimite = fechaLimite;
        this.estado = estado;
    }

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

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTrabajador() { return trabajador; }
    public void setTrabajador(String trabajador) { this.trabajador = trabajador; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}