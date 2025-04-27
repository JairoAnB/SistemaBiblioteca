# Sistema de Gestión de Préstamos de Libros 📚

## 👋 ¡Bienvenido!

Este proyecto implementa un **sistema de préstamos de libros** con una arquitectura moderna basada en **microservicios**.

Este sistema se compone de tres microservicios principales que trabajan juntos de forma independiente pero coordinada:

1. **Loan Service (Servicio de Préstamos)**: Gestiona todo lo relacionado con los préstamos de libros. Permite registrar, consultar, actualizar y eliminar préstamos de los usuarios.
2. **Book Service (Servicio de Libros)**: Lleva el control de los libros disponibles en el sistema, su título, autor, ISBN y stock.
3. **User Service (Servicio de Usuarios)**: Gestiona los usuarios que pueden realizar préstamos. Aquí se registra la información personal de cada usuario, como su nombre, apellido y correo electrónico.

### ¿Por qué este sistema?

- **Facilidad de uso**: Los usuarios pueden pedir libros prestados, devolverlos, ver el historial de préstamos y mucho más, todo a través de simples solicitudes a las APIs.
- **Microservicios**: Los tres servicios son independientes, lo que permite escalabilidad y flexibilidad. Si quieres agregar más funcionalidades o cambiar algo, ¡puedes hacerlo sin afectar a los demás servicios!

## 🚀 Tecnologías usadas

Este sistema está desarrollado con las siguientes tecnologías que lo hacen rápido, eficiente y fácil de mantener:

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![JPA](https://img.shields.io/badge/JPA-Hibernate-lightgrey)
![REST API](https://img.shields.io/badge/REST%20API-RESTful-red)

- **Java 17**: El lenguaje de programación que da vida a todo el sistema.
- **Spring Boot**: Framework para desarrollar aplicaciones rápidas y eficientes en Java, usado en cada microservicio.
- **PostgreSQL**: Base de datos potente y confiable para almacenar los datos de usuarios, libros y préstamos.
- **JPA / Hibernate**: Usados para gestionar la persistencia de datos de manera eficiente y sin tener que escribir SQL manualmente.
- **REST API**: La comunicación entre microservicios y con el frontend se realiza mediante APIs RESTful.

## 🛠️ Servicios del Sistema

### **Loan Service (Servicio de Préstamos)**

El **Loan Service** es donde ocurre toda la magia del préstamo de libros. Permite:

- **Consultar todos los préstamos**: ¿Quieres saber quién tiene qué libro? Puedes hacer una solicitud para ver todos los préstamos.
- **Consultar préstamo por ID**: Si necesitas detalles sobre un préstamo específico, solo pasa el ID y tendrás toda la información.
- **Crear un préstamo**: Cuando un usuario decide pedir un libro, puedes registrar un nuevo préstamo.
- **Actualizar un préstamo**: Si algo cambia en un préstamo, como la fecha de retorno o la información del libro, puedes actualizarlo.
- **Eliminar un préstamo**: Cuando un préstamo ya no es necesario o el libro es devuelto, puedes eliminarlo.

Endpoints:
- `GET /api/prestamos` – Obtiene todos los préstamos.
- `GET /api/prestamos/{id}` – Obtiene un préstamo por su ID.
- `POST /api/prestamos/create` – Crea un nuevo préstamo.
- `PUT /api/prestamos/update/{id}` – Actualiza un préstamo.
- `DELETE /api/prestamos/delete/{id}` – Elimina un préstamo.

### **Book Service (Servicio de Libros)**

El **Book Service** gestiona los libros disponibles en el sistema:

- **Consultar todos los libros**: Puedes ver todos los libros disponibles en el sistema.
- **Consultar libro por ID**: Si necesitas información de un libro en particular, solo debes pasar el ID.
- **Agregar un nuevo libro**: Puedes agregar nuevos libros al sistema.
- **Actualizar un libro**: Cambia detalles de un libro como el título, autor o el stock disponible.
- **Eliminar un libro**: Si un libro ya no está disponible, puedes eliminarlo del sistema.

Endpoints:
- `GET /api/books` – Obtiene todos los libros.
- `GET /api/books/{id}` – Obtiene un libro por su ID.
- `POST /api/books/create` – Crea un nuevo libro.
- `PUT /api/books/update/{id}` – Actualiza un libro.
- `PUT /api/books/updateStock/{id}/{stock}`- Actualiza el stock del libro.
- `DELETE /api/books/delete/{id}` – Elimina un libro.

### **User Service (Servicio de Usuarios)**

El **User Service** es donde se gestionan los usuarios del sistema. Este servicio te permite:

- **Consultar todos los usuarios**: Ver una lista de todos los usuarios registrados.
- **Consultar usuario por ID**: Si quieres detalles sobre un usuario, puedes obtenerlos pasando el ID del usuario.
- **Registrar un nuevo usuario**: Crea un nuevo usuario en el sistema.
- **Actualizar la información de un usuario**: Si un usuario cambia su correo o datos personales, puedes actualizar su información.
- **Eliminar un usuario**: Si un usuario ya no es necesario, puedes eliminarlo del sistema.

Endpoints:
- `GET /api/users` – Obtiene todos los usuarios.
- `GET /api/users/{id}` – Obtiene un usuario por su ID.
- `POST /api/users/create` – Crea un nuevo usuario.
- `PUT /api/users/update/{id}` – Actualiza un usuario.
- `DELETE /api/users/delete/{id}` – Elimina un usuario.

---

## 🛠️ Cómo Poblar las Tablas

Para que el sistema funcione adecuadamente, puedes usar los siguientes comandos `INSERT` para poblar las tablas con datos de ejemplo.

### **Usuarios (User Service)**

```sql
-- Insertar usuarios
INSERT INTO usuario (nombre, apellido, email, fecha_alta) VALUES
('Juan', 'Pérez', 'juan.perez@email.com', '2023-01-01'),
('Ana', 'García', 'ana.garcia@email.com', '2023-02-15'),
('Carlos', 'López', 'carlos.lopez@email.com', '2023-03-20');
```
### **Libros (Book Service)**
```
-- Insertar libros
INSERT INTO book (titulo, autor_name, isbn, stock) VALUES
('La sombra del viento', 'Carlos Ruiz Zafón', '9788401362802', 5),
('Cien años de soledad', 'Gabriel García Márquez', '9780307474728', 3),
('1984', 'George Orwell', '9780451524935', 4);
```
### **Prestamos (loan Service)**
```
-- Insertar préstamos
INSERT INTO prestamo (libro_id, usuario_id, monto, fecha_prestamo) VALUES
(1, 1, 500, '2023-04-01'),
(2, 2, 600, '2023-04-15'),
(3, 3, 450, '2023-04-20');
```
## 📡 Cómo Probar la API

Si quieres interactuar con el sistema, te recomiendo usar **Postman** o cualquier cliente HTTP. Aquí te dejo una explicacion de los pasos basicos:

- Asegurate que PostgreSQL este configurado y funcionando.
- Lanza los servicios de los microservicios (Loan service, book service y user Service)
- Usa Postman para probar las rutas que menciono anteriormente.
