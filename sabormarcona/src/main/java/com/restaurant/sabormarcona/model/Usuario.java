package com.restaurant.sabormarcona.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password", "tareas", "incidencias"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El username no puede estar vacío.")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotEmpty(message = "La contraseña no puede estar vacía.")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Email(message = "El email debe ser válido.")
    @NotEmpty(message = "El email no puede estar vacío.")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "rol", length = 50)
    private String rol;

    @Column(name = "activo")
    @Builder.Default
    private boolean activo = true;

    @OneToMany(mappedBy = "trabajadorAsignado", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Tarea> tareas = new ArrayList<>();

    @OneToMany(mappedBy = "trabajador", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Incidencia> incidencias = new ArrayList<>();

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
}
