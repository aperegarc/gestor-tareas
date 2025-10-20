# Gestor de Tareas - API REST

Esta API permite gestionar tareas personales asociadas a usuarios mediante autenticación JWT.  
Desarrollada en Java 17 con Spring Boot 3, Spring Security, Spring Data JPA y H2 Database.

---

## 1. Descripción General

La aplicación permite a los usuarios:

- Registrarse e iniciar sesión.
- Crear, consultar, actualizar y eliminar tareas personales.
- Mantener sesiones seguras mediante tokens JWT.
- Trabajar con una base de datos en memoria (H2) ideal para desarrollo o pruebas.

---

## 2. Tecnologías Utilizadas

- Java 17+
- Spring Boot 3
- Spring Security
- Spring Data JPA
- H2 Database
- JWT (io.jsonwebtoken)
- Maven

---

## 3. Instalación y Ejecución

### Requisitos previos

- Java 17 o superior
- Maven 3.9+

### Pasos de instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/tuusuario/gestor-tareas.git
cd gestor-tareas
```

2. Compilar y ejecutar la aplicación:
```bash
mvn clean install
mvn spring-boot:run
```

3. Acceder a la aplicación:
- API Base URL: `http://localhost:8080`
- Consola H2: `http://localhost:8080/h2-console`

4. Configuración por defecto de H2:
```
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password:
```

---

## 4. Autenticación

Todas las rutas protegidas requieren un JWT válido.  
En cada petición se debe enviar en el header:
```
Authorization: Bearer <tu_token_jwt>
```

---

## 5. Endpoints de Autenticación

### 5.1 Registro de usuario
```http
POST /api/auth/register
Content-Type: application/json

{
    "name": "Usuario Ejemplo",
    "email": "usuario@ejemplo.com",
    "password": "tuContraseña123"
}
```

**Respuesta (201 Created):**
```json
{
    "message": "Registro de usuario exitoso.",
    "token": null
}
```

---

### 5.2 Inicio de sesión
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "usuario@ejemplo.com",
    "password": "tuContraseña123"
}
```

**Respuesta (200 OK):**
```json
{
    "message": "Inicio de sesión exitoso.",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## 6. Endpoints de Tareas

Todas las rutas `/api/tasks/**` requieren autenticación JWT.

### 6.1 Obtener todas las tareas
```http
GET /api/tasks
Authorization: Bearer <tu_token_jwt>
```

**Respuesta (200 OK):**
```json
[
    {
        "id": 1,
        "description": "Ejemplo de tarea",
        "completed": false,
        "user": {
            "id": 1,
            "email": "usuario@ejemplo.com"
        }
    }
]
```

---

### 6.2 Crear una nueva tarea
```http
POST /api/tasks
Authorization: Bearer <tu_token_jwt>
Content-Type: application/json

{
    "description": "Nueva tarea de ejemplo",
    "completed": false
}
```

**Respuesta (201 Created):**
```json
{
    "id": 2,
    "description": "Nueva tarea de ejemplo",
    "completed": false,
    "user": {
        "id": 1,
        "email": "usuario@ejemplo.com"
    }
}
```

---

### 6.3 Obtener una tarea específica
```http
GET /api/tasks/{id}
Authorization: Bearer <tu_token_jwt>
```

**Respuesta (200 OK):**
```json
{
    "id": 1,
    "description": "Ejemplo de tarea",
    "completed": false,
    "user": {
        "id": 1,
        "email": "usuario@ejemplo.com"
    }
}
```

---

### 6.4 Actualizar una tarea
```http
PUT /api/tasks/{id}
Authorization: Bearer <tu_token_jwt>
Content-Type: application/json

{
    "description": "Tarea actualizada",
    "completed": true
}
```

**Respuesta (200 OK):**
```json
{
    "id": 1,
    "description": "Tarea actualizada",
    "completed": true,
    "user": {
        "id": 1,
        "email": "usuario@ejemplo.com"
    }
}
```

---

### 6.5 Eliminar una tarea
```http
DELETE /api/tasks/{id}
Authorization: Bearer <tu_token_jwt>
```

**Respuesta (204 No Content)**

---

## 7. Ejemplo de Flujo Completo

1. Registrar usuario:
```http
POST http://localhost:8080/api/auth/register
{
    "name": "Usuario Test",
    "email": "test@ejemplo.com",
    "password": "password123"
}
```

2. Iniciar sesión y obtener token:
```http
POST http://localhost:8080/api/auth/login
{
    "email": "test@ejemplo.com",
    "password": "password123"
}
```

3. Copiar el token recibido y añadirlo en las siguientes peticiones:
```
Authorization: Bearer <token_recibido>
```

4. Crear tarea:
```http
POST http://localhost:8080/api/tasks
{
    "description": "Mi primera tarea",
    "completed": false
}
```

5. Listar tareas:
```http
GET http://localhost:8080/api/tasks
```

---

## 8. Códigos de Estado

| Código | Descripción |
|---------|-------------|
| 200 | Petición exitosa |
| 201 | Recurso creado correctamente |
| 204 | Sin contenido |
| 400 | Datos inválidos |
| 401 | No autenticado o token inválido |
| 403 | Acceso no autorizado |
| 404 | Recurso no encontrado |
| 500 | Error interno del servidor |

---

## 9. Manejo de Errores

### Token inválido o expirado
```json
{
    "error": "Token inválido o expirado",
    "status": 401
}
```

### Credenciales incorrectas
```json
{
    "message": "Credenciales incorrectas.",
    "token": null
}
```

### Email ya registrado
```json
{
    "message": "El email ya está registrado.",
    "token": null
}
```

### Tarea no encontrada
```json
{
    "error": "Tarea no encontrada",
    "status": 404
}
```

---

## 10. Notas

- El token JWT expira después de 10 horas.
- Las contraseñas se almacenan cifradas con BCrypt.
- Cada usuario solo puede acceder a sus propias tareas.
- La base de datos H2 se reinicia al reiniciar la aplicación.
- Ideal para desarrollo o pruebas. Para producción, se recomienda una base de datos persistente (MySQL, PostgreSQL, etc).

---
