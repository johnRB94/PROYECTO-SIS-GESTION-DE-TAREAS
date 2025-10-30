package com.restaurant.sabormarcona.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "insumos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Min(value = 0, message = "El stock no puede ser negativo.")
    @Column(name = "stock")
    @Builder.Default
    private Integer stock = 0;

    @Column(name = "unidad", length = 20)
    private String unidad;
}
