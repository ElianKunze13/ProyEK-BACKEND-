# ✨Repositorio Backend Portofolio

Este repositorio contiene el backend de mi portofolio digital, desarrollado para gestionar y exponer mis proyectos y habilidades de manera dinámica. Es un proyecto que busca demostrar el uso de buenas prácticas en el desarrollo web aprendidas en la tecnicatura.

El objetivo de su realizacion fue el de construir un software robusto que soporte funcionalidades basicas (GET, POST, PUT, DELETE) implementando seguridad y pruebas (unitarias y de integracion) para testeo de posibles escenarios con datos limites e interesantes reduccion y reutilizacion de codigo reduntante, entre demas cosas.


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
*   ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.3-6DB33F?style=flat&logo=spring-boot&logoColor=white) - Framework para desarrollo de aplicaciones Java


### Dependencias
*   [![Maven](https://img.shields.io/badge/Maven-3.9.x-C71A36?style=flat&logo=apache-maven&logoColor=white)](https://maven.apache.org/) - Gestión de dependencias y construcción del proyecto
*   ![JUnit](https://img.shields.io/badge/JUnit-5.10.0-green.svg) - estructurar/ejecutar test **(No implementado aun)**
*   ![Mockito](https://img.shields.io/badge/Mockito-0.0.0-orange.svg) - Framework para estructurar/ejecutar test **(No implementado aun)**
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

