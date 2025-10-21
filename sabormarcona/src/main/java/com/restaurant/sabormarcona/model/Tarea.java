package com.restaurant.sabormarcona.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    
    // --- CAMPOS ANTIGUOS ELIMINADOS ---
    // private String trabajador; (Reemplazado por la relación)
    // private String rol; (Reemplazado por la relación)

    private String prioridad;

    @NotNull(message = "La fecha límite es obligatoria.")
    @Future(message = "La fecha límite debe ser en el futuro.")
    private LocalDateTime fechaLimite;

<<<<<<< HEAD
    @Enumerated(EnumType.STRING)
    private TaskStatus estado;

    // --- NUEVA RELACIÓN CON USUARIO ---
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: Carga el usuario solo cuando se necesita
    @JoinColumn(name = "usuario_id") // La columna en la tabla 'tareas' que guardará el ID del usuario
    private Usuario trabajadorAsignado;


    // Constructores, Getters y Setters
    public Tarea() {}
=======
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
>>>>>>> 7b7abd34a689173cbdf0d53c7baf558f1903d74e

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