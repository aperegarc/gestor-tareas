# Documentación API Gestor de Tareas

Esta documentación describe todos los endpoints disponibles en la API del Gestor de Tareas.

## Base URL
```
http://localhost:8080
```

## Autenticación

Todas las rutas (excepto registro y login) requieren autenticación mediante JWT Token.
El token debe ser incluido en el header de la siguiente manera:

```
Authorization: Bearer <tu_token_jwt>
```

### 1. Registro de Usuario
```http
POST /api/auth/register
Content-Type: application/json

{
    "name": "Usuario Ejemplo",
    "email": "usuario@ejemplo.com",
    "password": "tuContraseña123"
}
```

**Respuesta exitosa (201 Created):**
```json
{
    "message": "Registro de usuario exitoso.",
    "token": null
}
```

### 2. Inicio de Sesión
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "usuario@ejemplo.com",
    "password": "tuContraseña123"
}
```

**Respuesta exitosa (200 OK):**
```json
{
    "message": "Inicio de sesión exitoso.",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Gestión de Tareas

### 1. Obtener todas las tareas
```http
GET /api/tasks
Authorization: Bearer <tu_token_jwt>
```

**Respuesta exitosa (200 OK):**
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

### 2. Crear nueva tarea
```http
POST /api/tasks
Authorization: Bearer <tu_token_jwt>
Content-Type: application/json

{
    "description": "Nueva tarea de ejemplo",
    "completed": false
}
```

**Respuesta exitosa (201 Created):**
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

### 3. Obtener una tarea específica
```http
GET /api/tasks/{id}
Authorization: Bearer <tu_token_jwt>
```

**Respuesta exitosa (200 OK):**
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

### 4. Actualizar una tarea
```http
PUT /api/tasks/{id}
Authorization: Bearer <tu_token_jwt>
Content-Type: application/json

{
    "description": "Tarea actualizada",
    "completed": true
}
```

**Respuesta exitosa (200 OK):**
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

### 5. Eliminar una tarea
```http
DELETE /api/tasks/{id}
Authorization: Bearer <tu_token_jwt>
```

**Respuesta exitosa (204 No Content)**

## Códigos de Estado

- `200 OK`: Petición exitosa
- `201 Created`: Recurso creado exitosamente
- `204 No Content`: Petición exitosa sin contenido de respuesta
- `400 Bad Request`: Error en la petición (datos inválidos)
- `401 Unauthorized`: No autenticado o token inválido
- `403 Forbidden`: No autorizado para acceder al recurso
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

## Ejemplo de Flujo Completo en Postman

1. Primero, registra un nuevo usuario:
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
    "name": "Usuario Test",
    "email": "test@ejemplo.com",
    "password": "password123"
}
```

2. Inicia sesión para obtener el token:
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "email": "test@ejemplo.com",
    "password": "password123"
}
```

3. Copia el token recibido y úsalo en las siguientes peticiones en el header:
```
Authorization: Bearer <token_recibido>
```

4. Crea una nueva tarea:
```http
POST http://localhost:8080/api/tasks
Authorization: Bearer <token_recibido>
Content-Type: application/json

{
    "description": "Mi primera tarea",
    "completed": false
}
```

5. Obtén la lista de tareas:
```http
GET http://localhost:8080/api/tasks
Authorization: Bearer <token_recibido>
```

## Notas Importantes

1. El token JWT tiene una validez de 10 horas.
2. Todas las peticiones a `/api/tasks/**` requieren autenticación.
3. Los usuarios solo pueden ver y modificar sus propias tareas.
4. Las contraseñas se almacenan encriptadas usando BCrypt.
5. El token debe ser incluido en todas las peticiones (excepto login y registro) con el prefijo "Bearer ".

## Errores Comunes

1. **Token Inválido o Expirado**
```json
{
    "error": "Token inválido o expirado",
    "status": 401
}
```

2. **Credenciales Incorrectas**
```json
{
    "message": "Credenciales incorrectas.",
    "token": null
}
```

3. **Email ya registrado**
```json
{
    "message": "El email ya está registrado.",
    "token": null
}
```

4. **Tarea no encontrada**
```json
{
    "error": "Tarea no encontrada",
    "status": 404
}
```