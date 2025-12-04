# Guía de Contribución - Sabormarcona

## 🎯 Código de Conducta

Este proyecto adhiere a un código de conducta que todos los contribuidores deben seguir.

### Nuestros Compromisos

- Ser respetuoso y profesional
- Dar y recibir crítica constructiva
- Fomentar un ambiente inclusivo
- Reportar comportamiento inapropiado

## 🚀 Cómo Contribuir

### Reportar Bugs

#### Antes de reportar:
1. Verifica si el bug ya fue reportado en [Issues](https://github.com/tu-usuario/sabormarcona/issues)
2. Intenta reproducirlo en la versión más reciente

#### Al reportar, incluye:
- Versión del proyecto
- Sistema operativo y versión
- Pasos para reproducir
- Comportamiento esperado vs actual
- Logs relevantes
- Screenshots si aplica

**Template:**

```markdown
**Descripción:**
Describe claramente el problema.

**Pasos para reproducir:**
1. Ir a...
2. Hacer clic en...
3. Observar el error

**Resultado esperado:**
Debería...

**Resultado actual:**
Ocurre...

**Entorno:**
- OS: Windows 11
- Java: 21
- MySQL: 8.0
```

### Sugerir Mejoras

#### Antes de sugerir:
1. Verifica si ya existe una sugerencia similar

#### Proporciona:
- Descripción clara del enhancement
- Justificación y casos de uso
- Posibles implementaciones (opcional)

### Contribuir Código

#### 1. Fork el Proyecto

```bash
# En GitHub, haz clic en "Fork"
git clone https://github.com/tu-usuario/sabormarcona.git
cd sabormarcona
```

#### 2. Crear Rama Feature

```bash
# Actualizar main
git checkout main
git pull origin main

# Crear nueva rama
git checkout -b feature/descripcion-corta

# Ejemplos válidos:
# feature/agregar-notificaciones
# bugfix/corregir-login
# docs/actualizar-readme
```

#### 3. Hacer Cambios

```bash
# Editar archivos
# Probar localmente
./mvnw spring-boot:run

# Compilar y verificar
./mvnw clean package
```

#### 4. Commit Semántico

```bash
# Formato: <tipo>(<scope>): <descripción>

# Tipos permitidos:
# feat: nueva funcionalidad
# fix: corrección de bug
# docs: cambios en documentación
# style: formato de código
# refactor: refactorización sin cambios funcionales
# perf: mejora de performance
# test: cambios en tests
# chore: tareas de mantenimiento

# Ejemplos:
git commit -m "feat(tareas): agregar filtro por estado"
git commit -m "fix(auth): corregir validación de token JWT"
git commit -m "docs(readme): actualizar instrucciones de instalación"
```

#### 5. Push a tu Fork

```bash
git push origin feature/descripcion-corta
```

#### 6. Crear Pull Request

1. Abre GitHub
2. Verás un botón "Compare & pull request"
3. Completa la descripción con:
   - Qué cambios hiciste
   - Por qué son necesarios
   - Cómo se probaron
   - Issues relacionados (cierra con `Closes #123`)

**Template de PR:**

```markdown
## Descripción
Describe brevemente los cambios.

## Tipo de Cambio
- [ ] Bug fix (corrección que no rompe funcionalidad)
- [ ] Feature (nueva funcionalidad)
- [ ] Breaking change (cambio importante)

## Cómo se Probó
- [ ] Test unitario agregado
- [ ] Probado localmente
- [ ] Probado en navegador

## Checklist
- [ ] Código sigue estilo del proyecto
- [ ] Documentación actualizada
- [ ] No hay errores de compilación
- [ ] Tests pasan

## Screenshots (si aplica)
[Inserta imágenes]

## Issues Relacionados
Closes #123
```

## 📋 Estándares de Código

### Java

```java
// ✅ BUENO
@Service
@RequiredArgsConstructor
@Slf4j
public class TareaService {
    
    private final TareaRepository tareaRepository;
    
    public void crearTarea(TareaDTO dto) {
        log.debug("Creando tarea: {}", dto.getNombre());
        // código...
    }
}

// ❌ MALO
public class TareaService {
    TareaRepository tareaRepository;
    
    public void crearTarea(TareaDTO dto) {
        System.out.println("Creating task"); // Usar log
    }
}
```

### Principios

- **SOLID:** Aplicar principios SOLID
- **DRY:** No repetir código
- **KISS:** Mantener simple
- **Clean Code:** Nombres descriptivos, funciones pequeñas

### Nombrado

```java
// Clases
public class UsuarioService { }
public class CustomUserDetails { }

// Métodos
public void crearUsuario(UsuarioDTO dto) { }
private boolean validarCredenciales(String user, String pass) { }

// Variables
private long jwtExpirationMs;
private String jwtSecret;
```

### Documentación

```java
/**
 * Crear una nueva tarea en el sistema.
 * 
 * @param dto Objeto con datos de la tarea
 * @return ID de la tarea creada
 * @throws IllegalArgumentException si los datos son inválidos
 */
public Long crearTarea(TareaDTO dto) {
    // ...
}
```

### Logging

```java
// ✅ CORRECTO
log.debug("Verificando usuario: {}", username);
log.info("Usuario {} autenticado", username);
log.warn("Intento de acceso fallido para usuario: {}", username);
log.error("Error al guardar tarea", exception);

// ❌ INCORRECTO
System.out.println("Debug message");
System.err.println("Error");
```

## 🧪 Testing

### Crear Tests

```bash
# Tests en src/test/java
# Nombrado: NombreClaseTest.java

# Ejemplo estructura
src/test/java/com/restaurant/sabormarcona/
  └── service/
      └── TareaServiceTest.java
```

### Ejecutar Tests

```bash
# Todos
./mvnw test

# Específico
./mvnw test -Dtest=TareaServiceTest

# Con cobertura
./mvnw test jacoco:report
```

### Ejemplo de Test

```java
@SpringBootTest
public class UsuarioServiceTest {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Test
    public void testCrearUsuario() {
        // Arrange
        UsuarioDTO dto = new UsuarioDTO("test", "pass", "test@email.com");
        
        // Act
        Usuario resultado = usuarioService.crearUsuario(dto);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("test", resultado.getUsername());
    }
}
```

## 📚 Convenciones de Rama

```
main              - Producción estable
develop           - Desarrollo integrado
feature/*         - Nuevas funcionalidades
bugfix/*          - Correcciones
hotfix/*          - Fixes urgentes
release/*         - Preparación de release
```

## 🔍 Revisión de Código

### Lo que revisaremos:

- ✅ Corrección funcional
- ✅ Tests incluidos
- ✅ Documentación
- ✅ Estilo de código
- ✅ Performance
- ✅ Seguridad

### Proceso:

1. Mínimo 1 revisión requerida
2. Cambios solicitados deben ser incorporados
3. Aprobación = merge automático
4. Rechazo con motivos = notificación al autor

## 📦 Proceso de Release

1. Crear rama `release/vX.Y.Z`
2. Actualizar versión en `pom.xml`
3. Actualizar `CHANGELOG.md`
4. Crear PR para revisión
5. Merge a `main` y tag `vX.Y.Z`
6. Merge de vuelta a `develop`

## 📧 Preguntas

- **Ayuda General:** Crear Discussion en GitHub
- **Bugs:** Crear Issue
- **Requests:** Crear Issue con label "enhancement"

## ✨ Gracias por Contribuir

Tu contribución es valiosa para el proyecto. ¡Apreciamos tu tiempo y esfuerzo!

---

**Última actualización:** Diciembre 3, 2025
