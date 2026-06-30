# Auto Repair Shop API

## 1. Requisitos previos
Antes de levantar el proyecto, asegúrate de tener instalado:
- **JDK 21** o superior
- **PostgreSQL** instalado y corriendo localmente
- **Gradle** (el proyecto incluye el wrapper `gradlew`, no es necesario instalarlo aparte)
- Un cliente de base de datos como **pgAdmin** (recomendado para el paso de creación de admin)
- **Postman** (opcional, para probar los endpoints)

---

## 2. Crear la base de datos
Abre PostgreSQL y crea la base de datos:
```sql
CREATE DATABASE auto_repair_shop;
```

Las tablas se crean automáticamente al levantar la aplicación, gracias a `spring.jpa.hibernate.ddl-auto=update` — no es necesario correr ningún script de creación de esquema manualmente.

---

## 3. Configurar variables de entorno
El proyecto no usa valores hardcodeados en `application.properties`; todas las variables sensibles se inyectan por entorno. Debes configurar las siguientes:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_URL` | URL de conexión a PostgreSQL | `jdbc:postgresql://localhost:5432/auto_repair_shop` |
| `DB_USERNAME` | Usuario de la base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña del usuario | `tu_password` |
| `JWT_SECRET` | Clave secreta para firmar los JWT (mínimo 256 bits, codificada en Base64) | ver sección 3.1 |
| `JWT_EXPIRATION_MS` | Tiempo de expiración del access token, en milisegundos | `3600000` (1 hora) |
| `JWT_REFRESH_EXPIRATION_MS` | Tiempo de expiración del refresh token, en milisegundos | `604800000` (7 días) |

### 3.1 Generar un JWT_SECRET válido
El secreto debe ser una cadena Base64 de al menos 256 bits. Puedes generarlo así:
```bash
openssl rand -base64 32
```

### 3.2 Configurar las variables
**Configuración en el IDE (IntelliJ):**
Si usas IntelliJ IDEA, en la configuración de ejecución (Run Configuration) agrega las variables en la sección "Environment variables", separadas por punto y coma:

```
DB_URL=jdbc:postgresql://localhost:5432/auto_repair_shop;DB_USERNAME=postgres;DB_PASSWORD=tu_password;JWT_SECRET=tu_secreto;JWT_EXPIRATION_MS=3600000;JWT_REFRESH_EXPIRATION_MS=604800000
```

> **Nota:** sin estas variables configuradas, la aplicación no arrancará — `application.properties` las referencia directamente con `${VARIABLE}` y no tiene valores por defecto.

---

## 4. Levantar el proyecto
Con PostgreSQL corriendo y las variables de entorno configuradas
Si todo está correcto, la aplicación arranca en:

```
http://localhost:8000
```

Verás en consola los logs de Hibernate creando las tablas (`spring.jpa.show-sql=true` está activado para fines de depuración).

---

## 5. Crear el primer usuario administrador
El sistema no incluye un administrador por defecto — el primer ADMIN se crea manualmente en dos pasos:
### Paso 1 — Registrar un usuario normal
En Postman (o cualquier cliente HTTP), haz:

```
POST http://localhost:8000/api/auth/register
Content-Type: application/json
```

Body:

```json
{
  "name": "Admin",
  "email": "admin@test.com",
  "password": "password"
}
```

Esto crea el usuario con rol `CLIENT` por defecto (el rol asignado automáticamente en el registro público).

### Paso 2 — Promover el usuario a ADMIN
Conéctate a la base de datos con pgAdmin (o psql) y ejecuta:

```sql
UPDATE users
SET role = 'ADMIN',
    active = true
WHERE email = 'admin@test.com';
```

A partir de aquí, puedes hacer login con ese usuario (`POST /api/auth/login`) y usar el `accessToken` devuelto para acceder a los endpoints protegidos de ADMIN, incluyendo la creación de sucursales, mecánicos y demás usuarios del sistema.

---

## 6. Probar la API

El proyecto incluye una colección de Postman (`Auto_Repair_Shop_API_postman_collection.json`) que se encuentra en moodle en el apartado donde se envio el repositorio con todos los endpoints organizados por dominio, lista para importar.

1. Abre Postman → Import → selecciona el archivo `.json`
2. Configura una variable de entorno `baseUrl` con valor `http://localhost:8000`
3. Haz login primero y guarda el `accessToken` en una variable de entorno para usarlo en el resto de las llamadas

---
