package com.restaurant.sabormarcona.model;

public class Principal {
    private Long id;
    private String nombre;
    private String rol;
    private String iniciales;
    private String colorBadge;

    public Principal() {
    }

    // Constructor completo (para pruebas o uso interno)
    public Principal(Long id, String nombre, String rol, String iniciales, String colorBadge) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.iniciales = iniciales;
        this.colorBadge = colorBadge;
    }

    // Constructor de lógica de negocio (genera iniciales y color)
    public Principal(String nombre, String rol) {
        this.nombre = nombre;
        this.rol = rol;
        this.iniciales = generarIniciales(nombre);
        this.colorBadge = asignarColor(rol);
    }
    
    // Lógica para generar iniciales
    private String generarIniciales(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "";
        }
        String[] partes = nombre.split(" ");
        StringBuilder iniciales = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isEmpty()) {
                iniciales.append(parte.charAt(0));
            }
        }
        return iniciales.toString().toUpperCase();
    }
    
    // Lógica para asignar color según el rol
    private String asignarColor(String rol) {
        if (rol == null) {
            return "bg-secondary";
        }
        switch (rol.toLowerCase()) {
            case "chef principal": return "bg-success";
            case "mesera": 
            case "mesero": return "bg-warning";
            case "cocinero": return "bg-info";
            case "bartender": return "bg-primary";
            default: return "bg-secondary";
        }
    }

    // Getters y Setters
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; this.iniciales = generarIniciales(nombre); }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; this.colorBadge = asignarColor(rol); }

    public String getIniciales() { return iniciales; }
    public void setIniciales(String iniciales) { this.iniciales = iniciales; }

    public String getColorBadge() { return colorBadge; }
    public void setColorBadge(String colorBadge) { this.colorBadge = colorBadge; }
}