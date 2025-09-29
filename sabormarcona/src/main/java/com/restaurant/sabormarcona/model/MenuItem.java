package com.restaurant.sabormarcona.model;

import java.math.BigDecimal;

public class MenuItem {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String descripcion;
    private MenuCategory categoria;
    private boolean disponible;

    public MenuItem() {}

    public MenuItem(Long id, String nombre, BigDecimal precio, String descripcion, MenuCategory categoria, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public MenuCategory getCategoria() { return categoria; }
    public void setCategoria(MenuCategory categoria) { this.categoria = categoria; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}