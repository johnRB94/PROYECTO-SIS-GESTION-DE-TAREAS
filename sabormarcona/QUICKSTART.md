# Guía de Instalación Rápida - Sabormarcona

## ⚡ Instalación en 5 Minutos

### Opción 1: Docker Compose (Recomendado)

#### Requisitos
- Docker y Docker Compose instalados

#### Pasos

```bash
# 1. Clonar repositorio
git clone https://github.com/tu-usuario/sabormarcona.git
cd sabormarcona

# 2. Iniciar servicios
docker-compose up -d

# 3. Esperar a que inicie (30-40 segundos)
sleep 40

# 4. Abrir navegador
# http://localhost:8080/sabormarcona/

# Credenciales
# Username: admin
# Password: admin123
```

#### Detener

```bash
docker-compose down
```

#### Ver Logs

```bash
docker-compose logs -f app
```

---

### Opción 2: Instalación Local (Manual)

#### Requisitos
- Java 21+
- Maven 3.9+
- MySQL 5.7+

#### Pasos

```bash
# 1. Clonar repositorio
git clone https://github.com/tu-usuario/sabormarcona.git
cd sabormarcona

# 2. Crear base de datos
mysql -u root -p << EOF
CREATE DATABASE sabormarcona_db CHARACTER SET utf8mb4;
EOF

# 3. Configurar aplicación
# Editar: src/main/resources/application.properties
# Actualizar:
# - spring.datasource.username
# - spring.datasource.password

# 4. Compilar
./mvnw clean package

# 5. Ejecutar
./mvnw spring-boot:run
# O
java -jar target/sabormarcona-0.0.1-SNAPSHOT.jar

# 6. Abrir navegador
# http://localhost:8080/sabormarcona/
```

---

### Opción 3: IDE (VS Code / IntelliJ)

#### VS Code

```bash
# 1. Instalar extensiones
# - Extension Pack for Java (Microsoft)
# - Spring Boot Extension Pack (Microsoft)

# 2. Abrir proyecto
code .

# 3. Click en SabormarconaApplication.java
# 4. Click en "Run" (botón verde)
```

#### IntelliJ IDEA

```bash
# 1. File → Open → Seleccionar carpeta sabormarcona
# 2. Right-click en SabormarconaApplication.java
# 3. Run 'SabormarconaApplication'
```

---

## ✅ Verificar Instalación

### Comprobar que funciona

```bash
# Verificar aplicación
curl http://localhost:8080/sabormarcona/

# Verificar salud
curl http://localhost:8080/sabormarcona/actuator/health

# Probar login
curl -X POST http://localhost:8080/sabormarcona/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

---

## 🐛 Problemas Comunes

### "Connection refused"

**Solución:**
```bash
# Verificar MySQL corriendo
sudo systemctl status mysql
# o
docker ps | grep mysql
```

### "Port 8080 already in use"

```bash
# Cambiar puerto en application.properties
server.port=8081

# O matar proceso
sudo lsof -ti:8080 | xargs kill -9
```

### "Maven not found"

```bash
# Usar Maven wrapper
./mvnw (Linux/Mac)
mvnw.cmd (Windows)
```

### Base de datos vacía después del login

```bash
# Ejecutar SQL manualmente
mysql -u root sabormarcona_db < schema.sql
```

---

## 🎯 Próximos Pasos

1. ✅ Abrir http://localhost:8080/sabormarcona/
2. ✅ Login con admin/admin123
3. ✅ Explorar dashboard
4. ✅ Crear tareas de prueba
5. ✅ Leer [README.md](README.md) completo

---

## 📚 Documentación

- [README.md](README.md) - Documentación completa
- [JWT_README.md](JWT_README.md) - Autenticación JWT
- [CONTRIBUTING.md](CONTRIBUTING.md) - Guía de contribución

---

**Versión:** 0.0.1
**Última actualización:** Diciembre 3, 2025
