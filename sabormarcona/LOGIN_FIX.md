# Solución: Problemas de Login y Acceso a Principal

## Problema Identificado

El `SecurityFilterChain` con `SessionCreationPolicy.STATELESS` estaba deshabilitando las sesiones HTTP para TODOS los endpoints, incluyendo los del formulario de login. Esto impedía que el usuario accediera a `/principal` después de iniciar sesión.

## Solución Implementada

Se crearon dos `SecurityFilterChain` separados:

### 1. **Para Endpoints REST API (/api/**)**
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http, ...)
    .securityMatcher("/api/**")
    .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .addFilterBefore(jwtAuthenticationFilter(), ...)
```
- Usa **JWT tokens** (stateless)
- Requiere header `Authorization: Bearer <token>`
- Sin sesiones HTTP

### 2. **Para Aplicación Web (/principal, /tareas, etc.)**
```java
@Bean
public SecurityFilterChain webSecurityFilterChain(HttpSecurity http, ...)
    .formLogin(...)
    .sessionManagement() // Usa sesiones normales
```
- Usa **login por formulario** (stateful con sesiones)
- Crea `JSESSIONID` cookie
- Almacena usuario en sesión

## Flujo de Login Correcto

### Opción 1: Login por Formulario (Web)

```
1. Usuario accede a http://localhost:8080/sabormarcona/
2. Completa modal: username="admin", password="admin123"
3. Envía POST a /login
4. Spring valida credenciales
5. Ejecuta CustomAuthenticationSuccessHandler
6. Crea sesión y almacena usuario
7. Redirige a /principal
8. Usuario ve dashboard
```

### Opción 2: Login por API JWT (REST)

```
1. Enviar POST /api/auth/login
   {
     "username": "admin",
     "password": "admin123"
   }

2. Respuesta:
   {
     "token": "eyJhbGciOiJIUzUxMiJ9...",
     "type": "Bearer",
     "username": "admin"
   }

3. Usar token en peticiones futuras:
   GET /api/usuarios
   Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## Configuración Actual

### SecurityConfig.java
- **Dos FilterChains independientes**:
  - `/api/**` → JWT stateless
  - Otros → Sesiones stateful
  
- **Roles permitidos**:
  - `/principal`: ADMIN, USER1, USER2
  - `/tareas/**`: ADMIN, USER1
  - `/incidencias/**`: ADMIN, USER1, USER2
  - `/menu/**`: ADMIN, USER1, USER2
  - `/insumos/**`: ADMIN, USER1

### Credenciales por Defecto
- **Username**: admin
- **Password**: admin123
- **Rol**: admin
- Se crea automáticamente al iniciar la aplicación

## Pruebas Recomendadas

### Test 1: Login por Formulario
1. Abre http://localhost:8080/sabormarcona/
2. Haz clic en "Inicio"
3. Ingresa: admin / admin123
4. Deberías ver el dashboard en `/principal`

### Test 2: Login por API
```bash
curl -X POST http://localhost:8080/sabormarcona/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Test 3: Validar Token
```bash
curl -X POST http://localhost:8080/sabormarcona/api/auth/validate \
  -H "Content-Type: application/json" \
  -d '{"token":"<tu-token-aqui>"}'
```

## Cambios Realizados

1. **Eliminada** configuración `SessionCreationPolicy.STATELESS` global
2. **Creado** segundo `SecurityFilterChain` con nombre `webSecurityFilterChain`
3. **Agregado** `@Bean` con `securityMatcher("/api/**")` solo para JWT
4. **Mantenido** login por formulario con sesiones normales

## Archivos Modificados

- `src/main/java/com/restaurant/sabormarcona/config/SecurityConfig.java`

## Próximos Pasos Opcionales

1. Agregar refresh tokens a JWT
2. Implementar logout para JWT
3. Agregar roles/claims al token JWT
4. Configurar HTTPS en producción
5. Usar contraseñas desde variables de entorno (no hardcoded)
