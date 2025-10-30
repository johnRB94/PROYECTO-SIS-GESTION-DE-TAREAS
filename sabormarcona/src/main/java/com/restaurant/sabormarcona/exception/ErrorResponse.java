package com.restaurant.sabormarcona.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String mensaje;
    private String detalles;
    private int codigo;
    
    public ErrorResponse(String mensaje, String detalles) {
        this.mensaje = mensaje;
        this.detalles = detalles;
        this.codigo = 500;
    }
}
