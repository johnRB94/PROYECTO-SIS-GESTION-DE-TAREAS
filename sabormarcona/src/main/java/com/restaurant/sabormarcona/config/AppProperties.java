package com.restaurant.sabormarcona.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    /**
     * Clave secreta para firmar tokens JWT
     * Mínimo 32 caracteres para HS512
     */
    private String jwtSecret = "MiClaveSecretaMuyLargaParaJWTAlMenos32CaracteresParaSeguridadTotal";

    /**
     * Tiempo de expiración del token JWT en milisegundos
     * Por defecto: 86400000 ms = 24 horas
     */
    private Long jwtExpirationMs = 86400000L;
}
