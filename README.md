# TinyTasks - CRUD Activity

**TinyTasks** es una microaplicación para gestionar tareas, desarrollada con **Spring Boot** y frontend nativo (**HTML + JavaScript + Bootstrap**). El proyecto implementa una arquitectura en capas con datos en memoria y pruebas unitarias completas con JUnit 5.

---

## 📋 Descripción

TinyTasks permite gestionar una lista básica de tareas con las siguientes operaciones:
- ✅ Listar todas las tareas
- ➕ Crear nuevas tareas
- 🔄 Alternar el estado (completada/pendiente)
- 🗑️ Eliminar tareas

El sistema **NO** usa base de datos: los datos viven en memoria usando estructuras de datos concurrentes.

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.5.7**
  - Spring Web
  - Spring Boot DevTools
  - Lombok
- **JUnit 5** y **Mockito** para pruebas unitarias
- **Maven** como gestor de dependencias

### Frontend
- **HTML5**
- **JavaScript (ES6+)**
- **Bootstrap 5.3**
- **Bootstrap Icons**

---

## 📁 Estructura del Proyecto

```
tinytask/
├── src/
│   ├── main/
│   │   ├── java/com/ssd/tinytask/
│   │   │   ├── config/
│   │   │   │   └── CorsConfig.java
│   │   │   ├── controller/
│   │   │   │   └── TodoController.java
│   │   │   ├── dto/
│   │   │   │   ├── CreateTodoRequest.java
│   │   │   │   └── ErrorResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── InvalidTodoException.java
│   │   │   │   └── TodoNotFoundException.java
│   │   │   ├── model/
│   │   │   │   └── Todo.java
│   │   │   ├── repository/
│   │   │   │   └── TodoRepository.java
│   │   │   ├── service/
│   │   │   │   └── TodoService.java
│   │   │   └── TinytaskApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   ├── app.js
│   │       │   └── style.css
│   │       └── application.properties
│   └── test/
│       └── java/com/ssd/tinytask/
│           ├── repository/
│           │   └── TodoRepositoryTest.java
│           └── service/
│               └── TodoServiceTest.java
├── pom.xml
└── README.md
```

---

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd Samuel-Serna-Tinytask
```

### 2. Compilar el proyecto
```bash
mvn clean install
```

### 3. Ejecutar las pruebas
```bash
mvn test
```

### 4. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

El backend estará disponible en: **http://localhost:8080**

### 5. Acceder al Frontend
Abre el archivo en tu navegador:
```
http://localhost:8080/index.html
```

O usa Live Server en VS Code para servir los archivos desde `src/main/resources/static/`

---

## 📡 API REST - Endpoints

### Base URL
```
http://localhost:8080/api/todos
```

### Endpoints Disponibles

#### 1. **GET /api/todos**
Obtiene todas las tareas.

**Response 200:**
```json
[
  {
    "id": 1,
    "title": "Learn Spring Boot",
    "done": false
  }
]
```

---

#### 2. **POST /api/todos**
Crea una nueva tarea.

**Request Body:**
```json
{
  "title": "Learn Spring Boot"
}
```

**Validaciones:**
- `title` es requerido
- `title` debe tener al menos 3 caracteres

**Response 201:**
```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "done": false
}
```

**Response 400 (Error):**
```json
{
  "error": "Title is required"
}
```

---

#### 3. **PUT /api/todos/{id}/toggle**
Alterna el estado de una tarea (done: true ↔ false).

**Response 200:**
```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "done": true
}
```

**Response 404:**
```json
{
  "error": "Not found"
}
```

---

#### 4. **DELETE /api/todos/{id}**
Elimina una tarea.

**Response 204:** No Content

**Response 404:**
```json
{
  "error": "Not found"
}
```

---

## ✅ Historias de Usuario

### HU-01: Listar tareas
**Como** usuario  
**Quiero** ver todas las tareas registradas  
**Para** saber qué tengo pendiente

**Criterios de aceptación:**
- ✅ El frontend ejecuta `GET /api/todos`
- ✅ Se muestra una lista con todas las tareas actuales
- ✅ Se muestran estadísticas (total y completadas)

---

### HU-02: Crear tarea
**Como** usuario  
**Quiero** crear una nueva tarea  
**Para** mantener mis pendientes organizados

**Criterios de aceptación:**
- ✅ Hay un campo de texto para ingresar el título
- ✅ Al presionar "Add", se ejecuta `POST /api/todos`
- ✅ La nueva tarea aparece en la lista
- ✅ Si el título es inválido, se muestra un mensaje de error

---

### HU-03: Alternar estado
**Como** usuario  
**Quiero** marcar o desmarcar una tarea como completada  
**Para** visualizar mi progreso

**Criterios de aceptación:**
- ✅ Al presionar "Complete/Undo", se ejecuta `PUT /api/todos/{id}/toggle`
- ✅ El estado visual cambia (tachado/normal)
- ✅ Si la tarea no existe, se muestra un error 404

---

### HU-04: Eliminar tarea
**Como** usuario  
**Quiero** eliminar una tarea que ya no necesito

**Criterios de aceptación:**
- ✅ Al presionar "Delete", se muestra confirmación
- ✅ Se ejecuta `DELETE /api/todos/{id}`
- ✅ La tarea desaparece de la lista
- ✅ Si el id no existe, se muestra error 404

---

### HU-05: Pruebas unitarias
**Como** desarrollador  
**Quiero** validar la lógica de negocio  
**Para** asegurar que el sistema funcione correctamente

**Criterios de aceptación:**
- ✅ Proyecto configurado con JUnit 5 y Mockito
- ✅ Pruebas del Repository pasan exitosamente
- ✅ Pruebas del Service pasan exitosamente
- ✅ Se cubren escenarios positivos y negativos

---

## 🧪 Pruebas Unitarias

### Ejecutar todas las pruebas
```bash
mvn test
```

### Cobertura de Pruebas

#### TodoRepositoryTest
- ✅ Generación de IDs autoincrementales únicos
- ✅ Guardar y recuperar por ID (positivo)
- ✅ Retornar Optional vacío cuando ID no existe (negativo)
- ✅ Encontrar todas las tareas
- ✅ Eliminar por ID (positivo)
- ✅ Retornar false al eliminar ID inexistente (negativo)
- ✅ Verificar existencia por ID
- ✅ Contar tareas
- ✅ Eliminar todas las tareas
- ✅ Actualizar tarea existente

#### TodoServiceTest
- ✅ Crear tarea con título válido (positivo)
- ✅ Lanzar excepción con título vacío (negativo)
- ✅ Lanzar excepción con título null (negativo)
- ✅ Lanzar excepción con título muy corto (negativo)
- ✅ Lanzar excepción con todo null (negativo)
- ✅ Establecer done=false al crear
- ✅ Alternar de false a true (positivo)
- ✅ Alternar de true a false (positivo)
- ✅ Retornar vacío al alternar ID inexistente (negativo)
- ✅ Eliminar tarea existente (positivo)
- ✅ Retornar false al eliminar ID inexistente (negativo)
- ✅ Obtener todas las tareas
- ✅ Obtener tarea por ID (positivo)
- ✅ Lanzar excepción al obtener ID inexistente (negativo)

---

## 🎨 Características del Frontend

### Interfaz de Usuario
- 🎨 Diseño moderno con Bootstrap 5
- 📱 Responsive (móvil, tablet, desktop)
- 🌈 Gradiente de fondo atractivo
- ✨ Animaciones suaves
- 🔔 Mensajes de error amigables

### Funcionalidades
- ➕ Agregar tareas con validación en tiempo real
- ✅ Marcar tareas como completadas (con tachado visual)
- ♻️ Desmarcar tareas completadas
- 🗑️ Eliminar tareas con confirmación
- 📊 Estadísticas en tiempo real (total y completadas)
- 🔄 Recarga automática de la lista
- 🎯 Estado vacío cuando no hay tareas

---

## 🔧 Configuración

### CORS
El backend está configurado para aceptar peticiones desde:
- `http://localhost:5500` (Live Server)
- `http://localhost:5501`
- `http://localhost:3000`
- `http://127.0.0.1:5500`
- Y variantes

### Puerto del Servidor
El servidor corre en el puerto **8080** por defecto. Se puede cambiar en `application.properties`:
```properties
server.port=8080
```

---

## 📝 Validaciones

### Backend
- Título requerido (no null, no vacío)
- Título mínimo 3 caracteres
- Manejo de errores con respuestas HTTP apropiadas

### Frontend
- Campo requerido
- Mínimo 3 caracteres
- Validación antes de enviar
- Confirmación antes de eliminar

---

## 🏗️ Arquitectura

### Capas del Backend

#### 1. **Controller Layer** (`TodoController`)
- Maneja las peticiones HTTP
- Valida entrada con `@Valid`
- Retorna respuestas apropiadas

#### 2. **Service Layer** (`TodoService`)
- Contiene la lógica de negocio
- Valida reglas de negocio
- Lanza excepciones cuando es necesario

#### 3. **Repository Layer** (`TodoRepository`)
- Gestiona el almacenamiento en memoria
- Usa `ConcurrentHashMap` para thread-safety
- Implementa operaciones CRUD básicas

#### 4. **Model Layer** (`Todo`)
- Representa la entidad de dominio
- Usa Lombok para reducir boilerplate

#### 5. **Exception Handling** (`GlobalExceptionHandler`)
- Captura excepciones globalmente
- Retorna respuestas de error consistentes
- Maneja validaciones de Bean Validation

---

## 🎯 Mejores Prácticas Implementadas

- ✅ Separación de responsabilidades (SRP)
- ✅ Inyección de dependencias con constructor
- ✅ Uso de DTOs para requests
- ✅ Manejo centralizado de excepciones
- ✅ Validación en múltiples capas
- ✅ Thread-safety en repositorio
- ✅ Pruebas unitarias con cobertura completa
- ✅ Código limpio y documentado
- ✅ Naming conventions en inglés
- ✅ Responses HTTP apropiados

---

## 🐛 Troubleshooting

### El backend no inicia
```bash
# Verificar que el puerto 8080 no esté en uso
netstat -ano | findstr :8080

# Cambiar el puerto en application.properties si es necesario
server.port=8081
```

### CORS errors en el frontend
- Verifica que el backend esté corriendo
- Confirma que la URL del frontend está en la lista de CORS permitidos
- Revisa que uses el puerto correcto en `app.js`

### Las pruebas fallan
```bash
# Limpiar y recompilar
mvn clean test

# Ver logs detallados
mvn test -X
```

---

## 📚 Recursos Adicionales

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Bootstrap Documentation](https://getbootstrap.com/docs/5.3/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

---

## 👨‍💻 Autor

**Samuel Serna**

---

## 📄 Licencia

Este proyecto es parte de una actividad académica para Crudzaso.

---

## 🎉 ¡Listo para usar!

1. Ejecuta el backend: `mvn spring-boot:run`
2. Abre el navegador: `http://localhost:8080/index.html`
3. ¡Comienza a gestionar tus tareas!

---

**¡Happy Coding! 🚀**
