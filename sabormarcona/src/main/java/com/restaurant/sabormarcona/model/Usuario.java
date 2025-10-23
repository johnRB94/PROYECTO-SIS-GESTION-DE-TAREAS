package com.restaurant.sabormarcona.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // <-- ¡IMPORTA ESTO!
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

// ***** LA SOLUCIÓN ESTÁ EN ESTA LÍNEA *****
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String nombre;
    private String rol;
    private boolean activo = true;

    // --- El resto de tu clase permanece exactamente igual ---

    @Transient
    public String getIniciales() {
        if (nombre == null || nombre.trim().isEmpty()) return "";
        String[] partes = nombre.split(" ");
        StringBuilder iniciales = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isEmpty()) iniciales.append(parte.charAt(0));
        }
        return iniciales.toString().toUpperCase();
    }

    @Transient
    public String getColorBadge() {
        if (rol == null) return "bg-secondary";
        switch (rol.toLowerCase()) {
            case "chef principal": return "bg-success";
            case "mesera":
            case "mesero": return "bg-warning";
            case "cocinero": return "bg-info";
            case "bartender": return "bg-primary";
            default: return "bg-secondary";
        }
    }

    public Usuario() {}

    public Usuario(String username, String password, String nombre, String rol) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = true;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}