# ✨Repositorio Backend Portofolio

Repositorio que guarda backend de mi portofolio, desarrollado para gestionar y exponer mis proyectos/habilidades de manera dinámica.

El objetivo fue construir un software robusto que soporte funcionalidades basicas (GET, POST, PUT, DELETE) implementando seguridad y pruebas (unitarias y de integracion) para testeo con datos limites e interesantes, ademas de reduccion y reutilizacion de codigo reduntante, entre demas cosas.

Desplegada como *Web Service* en hosting [![Render](https://img.shields.io/badge/Render-46E3B7?style=flat&logo=render&logoColor=white)](https://render.com/) 

---
## 🔗 Repositorios Relacionados

- **Frontend:** [ProyEK-FrontEnd](https://github.com/ElianKunze13/ProyEK-FRONTEND-.git)

---

## 🛠️ Herramientas implementadas

### Lenguajes
- **Java JDK v17** - Lenguaje principal
- **HTML** - Para definir estructura de mails
- **CSS** - Para estetica de html

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
- **CORS**

### Base de Datos
*   [![MySQL](https://img.shields.io/badge/MySQL-9.3.0-4479A1?style=flat&logo=mysql&logoColor=white)](https://www.mysql.com/) - Se cambio a PostgreSQL para desplegar 

### Otros
*  [![UptimeRobot](https://img.shields.io/badge/UptimeRobot-Monitoring-7B3CE4?style=flat&logo=uptimerobot&logoColor=white)](https://uptimerobot.com/) - Mantiene activo backend, reduciendo carga de componentes
* [![ImageKit](https://img.shields.io/badge/ImageKit-CloudStorage-1A8CFF?style=flat&logo=imagekit&logoColor=white)](https://imagekit.io/dashboard) - Almacenamiento para imagenes
  
---

## 📂 Estructura del Proyecto
La arquitectura sigue el patrón **MVC (Modelo-Vista-Controlador)** y una clara separación en capas:
```
backend-portofolio/
├── 📁src/
│   ├── 📁main/
│   │   ├── 📁java/
│   │   │   └── 📁com/portofolio/
│   │   │       ├── 📁authsecurity/
│   │   │       ├── 📁config/
│   │   │       ├── 📁controllers/
│   │   │       ├── 📁dto/
│   │   │       ├── 📁enums/
│   │   │       ├── 📁mapper/
│   │   │       ├── 📁models/
│   │   │       ├── 📁repository/
│   │   │       └── 📁service/
│   │   │            └── 📁impl/
│   │   └── 📁resources/
│   │       └── 📄application.properties
│   │
│   └── 📁test/
│   
├── 📄pom.xml
└── 📄README.md
```

---

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java JDK 11 o superior
- IDE (recomendado): IntelliJ IDEA / Eclipse / VS Code
- Maven 3.6+
- Base de datos MySQL

**IMPORTANTE**
*Para probar api en dispositivo personal se debe definir variables de entorno nuevas/locales en application.properties, caso contrario dara error al ejecutar programa

---

## 📌 Convenciones utilizadas

- **Mensajes de commit** → Se sigue la convención *Conventional Commits*
- **Nombres de ramas** → `feature/nombre-funcionalidad`, `fix/descripcion-bug`
- **Código** → Se aplican principios SOLID y patrones de diseño

---


## 📄 Licencia

Este proyecto usa una licencia MIT. Tiene fines educativos y demostracion profesional del uso de herramientas.

