package com.restaurant.sabormarcona.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "trabajador")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El título no puede estar vacío.")
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "prioridad", length = 20)
    private String prioridad;

    @Column(name = "fecha_limite")
    private LocalDateTime fechaLimite;

    @Column(name = "estado", length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajador_id")
    private Usuario trabajador;

    @Transient
    public String getRol() {
        return (this.trabajador != null) ? this.trabajador.getRol() : null;
    }
}
