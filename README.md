# ✨Repositorio Backend Portofolio

Este repositorio contiene el backend de mi portofolio digital, desarrollado para gestionar y exponer mis proyectos y habilidades de manera dinámica. Es un proyecto personal que aplica buenas prácticas de desarrollo web, arquitectura REST y control de versiones.

El objetivo es construir un software robusto que soporte todas las funcionalidades definidas, haciendo uso de reglas de nomenclatura para definicion de nombres, pruebas/test de escenarios con datos limites, reduccion y reutilizacion de codigo reduntante, entre demas cosas aprendidas en la tecnicatura.


---
## 🔗 Repositorios Relacionados

- **Frontend:** [ProyEK-FrontEnd](https://github.com/ElianKunze13/ProyEK-FRONTEND-.git)

---

## 🛠️ Herramientas implementadas

### Lenguajes
- **Java JDK v17** - Lenguaje principal del backend
- **HTML** - Definicion de estructura de mails
- **CSS** - Para modificar estetica de html

### Frameworks y Librerías
*   [![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.3-6DB33F?style=flat&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot) - Framework para desarrollo de aplicaciones Java


### Dependencias
*   [![Maven](https://img.shields.io/badge/Maven-3.9.x-C71A36?style=flat&logo=apache-maven&logoColor=white)](https://maven.apache.org/) - Gestión de dependencias y construcción del proyecto
*   [![Project Lombok](https://img.shields.io/badge/Lombok-1.18.30-4B9BBE?style=flat)](https://projectlombok.org/) - Reducción de código 

### Seguridad y Testeo
- **Swagger**
- **Postman**
- **JWT**

  
### Base de Datos
*   [![MySQL](https://img.shields.io/badge/MySQL-9.3.0-4479A1?style=flat&logo=mysql&logoColor=white)](https://www.mysql.com/) - base de datos usada


## 📂 Estructura del Proyecto
La arquitectura sigue el patrón **MVC (Modelo-Vista-Controlador)** y una clara separación en capas:
```
backend-portofolio/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/portofolio/
│   │   │       ├── controllers/
│   │   │       ├── models/
│   │   │       ├── services/
│   │   │       └── repositories/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

---

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java JDK 11 o superior
- IDE (recomendado): IntelliJ IDEA / Eclipse / VS Code
- Maven 3.6+
- Base de datos MySQL


## 📌 Convenciones utilizadas

- **Mensajes de commit** → Se sigue la convención *Conventional Commits*
- **Nombres de ramas** → `feature/nombre-funcionalidad`, `fix/descripcion-bug`
- **Código** → Se aplican principios SOLID y patrones de diseño

---


## 📄 Licencia

Este proyecto usa una licencia MIT. Tiene fines educativos y demostracion profesional del uso de herramientas.

