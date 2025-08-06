# 🎬 Mini Sistema de Cine - Spring Boot + Seguridad

Sistema web tipo Netflix donde los usuarios pueden ver trailers, registrarse, iniciar sesión y acceder a películas según su rol. Admin puede gestionar contenido.

## ✅ Funcionalidades principales:
- Registro e inicio de sesión con encriptación (BCrypt)
- Roles de acceso (USER, ADMIN)
- Gestión de películas (ver, agregar, editar, eliminar)
- Ver trailer (público)
- Ver película (solo usuarios registrados)
- Panel de administración (`/admin`)
- Recuperación de contraseña (`/forgot-password`, `/reset-password`)
- Diseño moderno con recursos estáticos (CSS, JS, Images)

## 🔐 Seguridad
Implementación de Spring Security con:
- `UserDetailsService` personalizado
- `PasswordEncoder` con BCrypt
- Filtros de seguridad personalizados (`SecurityFilterChain`)
- Páginas públicas, privadas y de administración

## 🚀 Tecnologías utilizadas

- Java 17+
- Spring Boot
- Spring Security
- Thymeleaf
- Bootstrap / HTML / JS
- PostgreSQL
- Spring Web
- JPA / Hibernate

## 📂 Estructura de paquetes:
- `controller`
- `entity`
- `repository`
- `service`
- `security`
- `exception`
- `init` (poblado de datos)

## 🚀 Para ejecutarlo:
1. Clona el proyecto
2. Configura tu DB en `application.properties`
3. Ejecuta `MiniSistemaCineApplication.java`
4. Accede en el navegador a: [http://localhost:8080](http://localhost:8080)

---

## 👨‍💻 Autor

**Cristian Huarcaya Pumahualcca**  
Desarrollador Backend en Java  
[LinkedIn](https://www.linkedin.com/in/christian-huarcaya-pumahualcca) | [GitHub](https://github.com/ChristianHuarcaya)



