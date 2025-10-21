package com.restaurant.sabormarcona.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El título no puede estar vacío.")
    private String titulo;

    private String descripcion;
    private String prioridad;

    @NotNull(message = "La fecha límite es obligatoria.")
    @Future(message = "La fecha límite debe ser en el futuro.")
    private LocalDateTime fechaLimite;

    @Enumerated(EnumType.STRING)
    private TaskStatus estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario trabajadorAsignado;

    public Tarea() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }
    public TaskStatus getEstado() { return estado; }
    public void setEstado(TaskStatus estado) { this.estado = estado; }
    public Usuario getTrabajadorAsignado() { return trabajadorAsignado; }
    public void setTrabajadorAsignado(Usuario trabajadorAsignado) { this.trabajadorAsignado = trabajadorAsignado; }
}