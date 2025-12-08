# JWT - Implementación en Sabormarcona

## ¿Qué se agregó?

Se implementó autenticación JWT (JSON Web Token) en el proyecto Spring Boot. Esto permite:

- Autenticación sin sesiones (Stateless)
- Tokens seguros para APIs REST
- Comunicación backend-frontend sin cookies

## Archivos Creados

### 1. `JwtTokenProvider.java` (security/)
Componente que maneja la generación y validación de tokens JWT.

**Métodos principales:**
- `generateToken(Authentication)` - Genera token a partir de autenticación
- `generateTokenFromUsername(String)` - Genera token directamente
- `validateToken(String)` - Valida si el token es válido
- `getUsernameFromToken(String)` - Extrae el username del token
- `getExpirationDateFromToken(String)` - Obtiene fecha de expiración

### 2. `JwtAuthenticationFilter.java` (security/)
Filtro de Spring Security que intercepta peticiones y valida tokens JWT.

**Funcionamiento:**
- Extrae el token del header `Authorization: Bearer <token>`
- Valida el token
- Crea un contexto de seguridad si es válido

### 3. `AuthController.java` (controller/)
Endpoints REST para autenticación JWT.

## Endpoints Disponibles

### 1. **POST /api/auth/login**
Obtiene un token JWT mediante credenciales.

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response (exitoso):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "admin",
  "message": "Login exitoso"
}
```

**Response (error):**
```json
{
  "error": "Credenciales inválidas",
  "message": "El nombre de usuario o contraseña es incorrecto"
}
```

### 2. **POST /api/auth/validate**
Valida si un token es válido.

**Request:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

**Response (válido):**
```json
{
  "valid": true,
  "username": "admin",
  "expiresAt": "2025-12-04T22:41:00Z"
}
```

### 3. **POST /api/auth/token**
Genera un token a partir del username (sin autenticación).

**Request:**
```json
{
  "username": "admin"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "admin"
}
```

## Cómo Usar

### Desde JavaScript/Frontend

```javascript
// 1. Obtener token
const response = await fetch('/sabormarcona/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    username: 'admin',
    password: 'admin123'
  })
});

const data = await response.json();
const token = data.token;

// 2. Usar token en peticiones posteriores
const response2 = await fetch('/sabormarcona/api/usuarios', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

### Desde cURL

```bash
# 1. Obtener token
curl -X POST http://localhost:8080/sabormarcona/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 2. Usar el token
curl -X GET http://localhost:8080/sabormarcona/api/usuarios \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

### Desde Postman

1. **Crear petición POST** a `http://localhost:8080/sabormarcona/api/auth/login`
2. **Body** (JSON):
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
3. **Send** - Copiar el `token` de la respuesta
4. **Para siguientes peticiones** - Header `Authorization`: `Bearer <token>`

## Configuración

En `application.properties`:

```properties
# Clave secreta para firmar tokens (cambiar en producción!)
app.jwtSecret=MiClaveSecretaMuyLargaParaJWTAlMenos32CaracteresParaSeguridadTotal

# Expiración del token en milisegundos (86400000 = 24 horas)
app.jwtExpirationMs=86400000
```

**⚠️ IMPORTANTE:** En producción, cambiar `jwtSecret` a una clave más segura.

## Cambios en SecurityConfig

Se agregaron:
- **SessionCreationPolicy.STATELESS** - No usa sesiones
- **JwtAuthenticationFilter** - Filtro de validación
- `/api/auth/**` permitido sin autenticación (es público)

## Flujo de Autenticación

```
1. Cliente envía POST /api/auth/login con credenciales
   ↓
2. AuthController autentica con AuthenticationManager
   ↓
3. Si es válido, genera token con JwtTokenProvider
   ↓
4. Cliente recibe token
   ↓
5. Cliente envía token en header: Authorization: Bearer <token>
   ↓
6. JwtAuthenticationFilter intercepta y valida token
   ↓
7. Si es válido, se crea SecurityContext y se procesa petición
```

## Seguridad

- Tokens firmados con HS512
- Expiración automática (24 horas por defecto)
- Validación en cada petición

## Próximos Pasos

1. Cambiar `jwtSecret` en producción
2. Implementar refresh tokens
3. Agregar roles/permisos al token (claims)
4. Configurar HTTPS en producción
