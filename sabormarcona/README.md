# Sabormarcona - Sistema de Gestión de Tareas

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-5.7%2B-blue)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-blue)](https://maven.apache.org/)

Sistema integral de gestión de tareas, incidencias e insumos para restaurantes, desarrollado con Spring Boot 3, Thymeleaf y MySQL.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Uso](#uso)
- [Autenticación JWT](#autenticación-jwt)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [API REST](#api-rest)
- [Despliegue](#despliegue)
- [Troubleshooting](#troubleshooting)

## ✨ Características

### Gestión de Tareas
- ✅ Crear, editar y eliminar tareas
- ✅ Estados: Pendiente, En Progreso, Completada
- ✅ Asignación a usuarios
- ✅ Historial de cambios

### Gestión de Incidencias
- ✅ Reportar y seguir incidencias
- ✅ Prioridades: Baja, Media, Alta, Crítica
- ✅ Asignación a responsables
- ✅ Notificaciones

### Gestión de Menú
- ✅ CRUD de platos
- ✅ Categorías y precios
- ✅ Descripciones e ingredientes

### Gestión de Insumos
- ✅ Inventario de ingredientes
- ✅ Control de stock
- ✅ Proveedores
- ✅ Alertas de bajo stock

### Seguridad
- 🔐 Autenticación con BCrypt
- 🔐 Autorización basada en roles (ADMIN, USER1, USER2)
- 🔐 JWT para API REST
- 🔐 CSRF protection
- 🔐 Sesiones seguras

## 🔧 Requisitos Previos

### Software
- **Java 21+** - [Descargar](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.9+** - [Descargar](https://maven.apache.org/download.cgi)
- **MySQL 5.7+** - [Descargar](https://www.mysql.com/downloads/)
- **Git** - [Descargar](https://git-scm.com/)

### Hardware Mínimo
- CPU: Dual-core 2.0 GHz
- RAM: 2 GB
- Disco: 500 MB disponibles

## 📥 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/sabormarcona.git
cd sabormarcona
```

### 2. Configurar Base de Datos

#### Crear Base de Datos MySQL

```sql
CREATE DATABASE sabormarcona_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'sabormarcona_user'@'localhost' IDENTIFIED BY 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON sabormarcona_db.* TO 'sabormarcona_user'@'localhost';
FLUSH PRIVILEGES;
```

#### O usar base de datos sin contraseña (solo desarrollo)

```sql
CREATE DATABASE sabormarcona_db;
```

### 3. Actualizar Configuración

Editar `src/main/resources/application.properties`:

```properties
# Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/sabormarcona_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=tu_contraseña

# JWT (cambiar en producción)
app.jwt-secret=MiClaveSecretaMuyLargaParaJWTAlMenos32CaracteresParaSeguridadTotal
app.jwt-expiration-ms=86400000
```

### 4. Compilar el Proyecto

```bash
# Linux/Mac
./mvnw clean install

# Windows
mvnw.cmd clean install
```

### 5. Ejecutar la Aplicación

```bash
# Modo desarrollo
./mvnw spring-boot:run

# Compilar JAR
./mvnw package

# Ejecutar JAR
java -jar target/sabormarcona-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: **http://localhost:8080/sabormarcona**

## ⚙️ Configuración

### Propiedades de Aplicación

```properties
# Servidor
server.servlet.context-path=/sabormarcona
server.port=8080

# Base de Datos
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.com.restaurant.sabormarcona=DEBUG
logging.level.org.springframework.security=DEBUG

# JWT
app.jwt-secret=<tu-clave-secreta-larga>
app.jwt-expiration-ms=86400000
```

### Variables de Entorno (Producción)

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://db-host:3306/sabormarcona_db
export SPRING_DATASOURCE_USERNAME=user
export SPRING_DATASOURCE_PASSWORD=password
export APP_JWT_SECRET=tu-clave-super-segura-de-32-caracteres-minimo
export APP_JWT_EXPIRATION_MS=86400000
```

## 🚀 Uso

### Credenciales por Defecto

| Campo | Valor |
|-------|-------|
| **Username** | admin |
| **Password** | admin123 |
| **Rol** | ADMIN |

**⚠️ Cambiar contraseña en producción**

### Acceso a la Aplicación

1. Abre http://localhost:8080/sabormarcona/
2. Haz clic en **"Inicio"**
3. Ingresa credenciales
4. Accede al dashboard

### Navegación Principal

- **Principal** - Dashboard con estadísticas
- **Empleado Nuevo** - Crear nuevos usuarios
- **Tareas** - Gestión de tareas
- **Incidencias** - Reporte de problemas
- **Gestión de Menú** - Administrar platos
- **Gestión de Insumos** - Control de inventario
- **Usuarios** - Administración de acceso

## 🔑 Autenticación JWT

### Login por API

```bash
curl -X POST http://localhost:8080/sabormarcona/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Respuesta:**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "admin",
  "message": "Login exitoso"
}
```

### Usar Token en Peticiones

```bash
curl -X GET http://localhost:8080/sabormarcona/api/usuarios \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

### Validar Token

```bash
curl -X POST http://localhost:8080/sabormarcona/api/auth/validate \
  -H "Content-Type: application/json" \
  -d '{"token": "tu-token-aqui"}'
```

## 📁 Estructura del Proyecto

```
sabormarcona/
├── src/
│   ├── main/
│   │   ├── java/com/restaurant/sabormarcona/
│   │   │   ├── config/              # Configuraciones (Security, Properties)
│   │   │   ├── controller/          # Controladores REST y Web
│   │   │   ├── dao/                 # UserDetails y servicios de autenticación
│   │   │   ├── exception/           # Excepciones personalizadas
│   │   │   ├── model/               # Entidades JPA
│   │   │   ├── repository/          # Interfaces de repositorio
│   │   │   ├── security/            # JWT (JwtTokenProvider, JwtAuthenticationFilter)
│   │   │   ├── service/             # Lógica de negocio
│   │   │   └── SabormarconaApplication.java
│   │   ├── resources/
│   │   │   ├── templates/           # Vistas Thymeleaf
│   │   │   ├── static/              # CSS, JS, imágenes
│   │   │   └── application.properties
│   │   └── META-INF/
│   │       └── spring-configuration-metadata.json
│   └── test/
│       └── java/                    # Tests unitarios
├── pom.xml                          # Dependencias Maven
├── mvnw y mvnw.cmd                  # Maven Wrapper
├── README.md                        # Este archivo
├── JWT_README.md                    # Documentación JWT
└── LOGIN_FIX.md                     # Notas de configuración de seguridad
```

## 🔌 API REST

### Endpoints de Autenticación

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|----------------|
| POST | `/api/auth/login` | Obtener token JWT | ❌ No |
| POST | `/api/auth/token` | Generar token por username | ❌ No |
| POST | `/api/auth/validate` | Validar token | ❌ No |

### Endpoints Protegidos

| Método | Endpoint | Rol Requerido |
|--------|----------|---------------|
| GET | `/api/usuarios` | ADMIN, USER1, USER2 |
| POST | `/api/tareas` | ADMIN, USER1 |
| GET | `/api/incidencias` | ADMIN, USER1, USER2 |
| POST | `/api/menu` | ADMIN, USER1, USER2 |
| PUT | `/api/insumos` | ADMIN, USER1 |

## 🌐 Despliegue

### Despliegue Local (Desarrollo)

```bash
./mvnw spring-boot:run
```

### Despliegue en Servidor

#### 1. Preparar JAR

```bash
./mvnw clean package
```

#### 2. Copiar a Servidor

```bash
scp target/sabormarcona-0.0.1-SNAPSHOT.jar user@server:/opt/sabormarcona/
```

#### 3. Crear Servicio Systemd (Linux)

```ini
# /etc/systemd/system/sabormarcona.service
[Unit]
Description=Sabormarcona Application
After=network.target

[Service]
Type=simple
User=sabormarcona
WorkingDirectory=/opt/sabormarcona
ExecStart=/usr/bin/java -jar sabormarcona-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

#### 4. Iniciar Servicio

```bash
sudo systemctl daemon-reload
sudo systemctl start sabormarcona
sudo systemctl enable sabormarcona
```

### Despliegue con Docker

#### 1. Crear Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

#### 2. Construir Imagen

```bash
docker build -t sabormarcona:latest .
```

#### 3. Ejecutar Contenedor

```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-host:3306/sabormarcona_db \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=password \
  --name sabormarcona \
  sabormarcona:latest
```

### Despliegue con Docker Compose

```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: sabormarcona_db
      MYSQL_ROOT_PASSWORD: root_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/sabormarcona_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root_password
      APP_JWT_SECRET: clave-super-segura-en-produccion
    depends_on:
      - mysql

volumes:
  mysql_data:
```

#### Iniciar con Docker Compose

```bash
docker-compose up -d
```

## 🔒 Seguridad en Producción

### Checklist de Seguridad

- [ ] Cambiar contraseña admin por defecto
- [ ] Usar HTTPS/SSL (generar certificados)
- [ ] Cambiar `app.jwt-secret` por clave larga y aleatoria
- [ ] Configurar CORS si es necesario
- [ ] Habilitar CSRF protection (comentada en dev)
- [ ] Usar variables de entorno para credenciales
- [ ] Configurar firewall (solo puertos 80, 443, 3306)
- [ ] Backups automáticos de base de datos
- [ ] Monitoreo de logs y errores
- [ ] Rate limiting en APIs

### Configurar HTTPS

```properties
# application-prod.properties
server.ssl.key-store=classpath:keystore.jks
server.ssl.key-store-password=changeit
server.ssl.key-store-type=JKS
```

## 🐛 Troubleshooting

### Error: "Usuario no autenticado"

**Causa:** Las sesiones están deshabilitadas o token JWT inválido.

**Solución:**
```bash
# Limpiar cookies/sesiones
# Usar credenciales válidas (admin/admin123)
# O generar nuevo token JWT
```

### Error: "Connection refused" en Base de Datos

```bash
# Verificar MySQL está corriendo
sudo systemctl status mysql

# Verificar credenciales en application.properties
# Verificar puerto 3306 está abierto
```

### Error: "Permiso denegado" en /principal

**Causa:** El rol del usuario no tiene permisos.

**Solución:** Asignar rol correcto al usuario (ADMIN, USER1 o USER2)

### Aplicación Lenta

```bash
# Verificar logs
tail -f logs/sabormarcona.log

# Optimizar queries
# Aumentar heap memory: -Xmx2048m
```

## 📊 Monitoreo

### Endpoints de Salud

```bash
curl http://localhost:8080/sabormarcona/actuator/health
```

### Ver Logs

```bash
# Linux
tail -f /var/log/sabormarcona/app.log

# Windows (si usa fichero)
Get-Content app.log -Tail 50 -Wait
```

## 📚 Documentación Adicional

- [JWT_README.md](JWT_README.md) - Detalles de autenticación JWT
- [LOGIN_FIX.md](LOGIN_FIX.md) - Configuración de seguridad
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)

## 🤝 Contribuir

1. Fork el proyecto
2. Crea rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre Pull Request

## 📄 Licencia

Este proyecto está bajo licencia MIT. Ver archivo [LICENSE](LICENSE) para más detalles.

## ✉️ Contacto

- **Autor:** Tu Nombre
- **Email:** tu.email@example.com
- **GitHub:** [@tu-usuario](https://github.com/tu-usuario)
- **Reportar Issues:** [GitHub Issues](https://github.com/tu-usuario/sabormarcona/issues)

---

**Última actualización:** Diciembre 3, 2025

**Versión:** 0.0.1-SNAPSHOT
