# Changelog

Todos los cambios importantes en este proyecto se documentan en este archivo.

El formato se basa en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [0.0.1-SNAPSHOT] - 2025-12-03

### Agregado

#### Autenticación y Seguridad
- ✨ Implementación completa de JWT (JSON Web Tokens)
- ✨ Autenticación basada en BCrypt
- ✨ Autorización por roles (ADMIN, USER1, USER2)
- ✨ Filtro de autenticación JWT con `JwtAuthenticationFilter`
- ✨ Generador de tokens con `JwtTokenProvider`
- ✨ Endpoints REST seguros en `/api/**`
- ✨ Validación de tokens automática

#### API REST
- ✨ Endpoint POST `/api/auth/login` - Obtener token
- ✨ Endpoint POST `/api/auth/token` - Generar token por username
- ✨ Endpoint POST `/api/auth/validate` - Validar token

#### Gestión de Tareas
- ✨ CRUD completo de tareas
- ✨ Estados de tarea: Pendiente, En Progreso, Completada
- ✨ Asignación a usuarios
- ✨ Filtrado por estado

#### Gestión de Incidencias
- ✨ Reporte de incidencias
- ✨ Niveles de prioridad
- ✨ Asignación a responsables
- ✨ Seguimiento de cambios

#### Gestión de Menú
- ✨ CRUD de platos
- ✨ Categorías de menú
- ✨ Precios y descripciones

#### Gestión de Insumos
- ✨ Control de inventario
- ✨ Gestión de stock
- ✨ Proveedores de insumos
- ✨ Alertas de bajo stock

#### Configuración y Inicialización
- ✨ Inicialización automática de usuario admin
- ✨ Encriptación automática de contraseñas en texto plano
- ✨ Configuración de JWT con `AppProperties`
- ✨ Metadata de propiedades de Spring Boot

#### Documentación
- ✨ README.md con instrucciones completas
- ✨ JWT_README.md con detalles de autenticación
- ✨ LOGIN_FIX.md con notas de seguridad
- ✨ CONTRIBUTING.md para contribuidores
- ✨ Este CHANGELOG

### Cambiado

- 🔄 SecurityConfig refactorizado con dos FilterChains
  - Uno para API JWT (stateless)
  - Uno para Web (sesiones)
- 🔄 JwtTokenProvider actualizado a JJWT 0.12.3 con API moderna
- 🔄 Propiedades JWT migradas a kebab-case

### Corregido

- 🐛 Problema de sesiones deshabilitadas globalmente
- 🐛 Acceso a `/principal` después de login
- 🐛 Anotaciones `@NonNull` faltantes
- 🐛 Deprecaciones en JJWT resueltas

### Eliminado

- ❌ Import innecesario de `SignatureAlgorithm`
- ❌ Configuración `SessionCreationPolicy.STATELESS` global

### Seguridad

- 🔐 Tokens JWT firmados con HS512
- 🔐 Expiración de tokens configurable
- 🔐 Validación automática en cada petición
- 🔐 Contraseñas hasheadas con BCrypt
- 🔐 CSRF protection habilitado

### Dependencias Principales

- Spring Boot 3.5.8
- Spring Security 6.2.14
- Spring Data JPA 3.2.5
- Hibernate 6.6.36
- MySQL Connector 8.3.0
- JJWT 0.12.3
- Lombok 1.18.30
- Thymeleaf 3.1.2
- Bootstrap 5.3.8

## Roadmap Futuro

### Próximas Versiones

- [ ] Implementar refresh tokens
- [ ] Agregar roles/claims al JWT
- [ ] Notificaciones por email
- [ ] Historial de auditoría
- [ ] Reportes avanzados
- [ ] Dashboard con gráficas
- [ ] Integración de pagos
- [ ] API en GraphQL
- [ ] Aplicación móvil
- [ ] Exportación a PDF/Excel

### Mejoras Planificadas

- [ ] Autenticación OAuth2/Google
- [ ] Two-Factor Authentication (2FA)
- [ ] Rate limiting
- [ ] Caché distribuida (Redis)
- [ ] Búsqueda ElasticSearch
- [ ] Microservicios
- [ ] Kubernetes deployment

---

## Convención de Versionado

Versiones en formato `MAJOR.MINOR.PATCH`:

- **MAJOR:** Cambios incompatibles (breaking changes)
- **MINOR:** Nuevas funcionalidades (backwards compatible)
- **PATCH:** Correcciones de bugs (backwards compatible)

Ejemplo: `1.2.3`

## Etiquetas

En los cambios usamos:
- ✨ Nueva funcionalidad (`feat:`)
- 🐛 Corrección de bug (`fix:`)
- 🔄 Cambio (`change:`)
- ❌ Eliminación (`remove:`)
- 📚 Documentación
- 🔐 Seguridad

---

**Última actualización:** Diciembre 3, 2025
