package com.restaurant.sabormarcona.model;

import java.math.BigDecimal;

public class Menu {
    private Long id;
    private String nombre;

    // Constructor por defecto
    public Menu() {
    }

    // Constructor con todos los campos
    public Menu(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Clase interna para representar los ítems del menú
    public static class MenuItem {
        private Long id;
        private String nombre;
        private BigDecimal precio;
        private String descripcion;
        private Menu categoria;
        private boolean disponible;

        // Constructor por defecto
        public MenuItem() {
        }

        // Constructor con todos los campos
        public MenuItem(Long id, String nombre, BigDecimal precio, String descripcion, Menu categoria, boolean disponible) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.descripcion = descripcion;
            this.categoria = categoria;
            this.disponible = disponible;
        }

        // Getters y Setters de MenuItem
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public BigDecimal getPrecio() { return precio; }
        public void setPrecio(BigDecimal precio) { this.precio = precio; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public Menu getCategoria() { return categoria; }
        public void setCategoria(Menu categoria) { this.categoria = categoria; }

        public boolean isDisponible() { return disponible; }
        public void setDisponible(boolean disponible) { this.disponible = disponible; }
    }
}