package com.restaurant.sabormarcona.model;

public class Insumo {
    private Long id;
    private String nombre;
    private String tipo;
    private Integer stock;
    private String unidad;

    public Insumo() {}


    public Insumo(Long id, String nombre, String tipo, Integer stock, String unidad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.stock = stock;
        this.unidad = unidad;
    }

    // Constructor 
    public Insumo(String nombre, String tipo, Integer stock, String unidad) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.stock = stock;
        this.unidad = unidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
}
