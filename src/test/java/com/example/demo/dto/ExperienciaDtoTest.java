package com.example.demo.dto;

import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ExperienciaDto
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class ExperienciaDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // ==================== TESTS DE CREACIÓN Y CONSTRUCTORES ====================

    @Test
    @DisplayName("Debería crear ExperienciaDto con valores válidos - Caso feliz")
    void shouldCreateExperienciaDtoWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedTitulo = "Sistema de Gestión de Usuarios";
        LocalDate expectedFechaInicio = LocalDate.of(2024, 1, 15);
        LocalDate expectedFechaFin = LocalDate.of(2024, 6, 30);
        String expectedDescripcion = "Desarrollo de API REST con Spring Boot y JWT para gestión de usuarios";
        String expectedLink = "https://github.com/usuario/proyecto";
        TipoExperiencia expectedTipo = TipoExperiencia.PROYECTO_PERSONAL;
        TecnologiaUsada expectedTecnologia = TecnologiaUsada.SPRINGBOOT;

        // Crear imagen para prueba
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("proyecto.jpg")
                .alt("Captura del proyecto")
                .build();

        // Act - Ejecutar la acción a probar
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(expectedId)
                .titulo(expectedTitulo)
                .fechaInicioProyecto(expectedFechaInicio)
                .fechaFinProyecto(expectedFechaFin)
                .descripcion(expectedDescripcion)
                .link(expectedLink)
                .tipoExperiencia(expectedTipo)
                .tecnologiaUsada(expectedTecnologia)
                .imagen(imagen)
                .build();

        // Assert - Verificar resultados
        assertNotNull(experienciaDto, "El objeto ExperienciaDto no debería ser nulo");
        assertEquals(expectedId, experienciaDto.getId(), "El ID debería coincidir");
        assertEquals(expectedTitulo, experienciaDto.getTitulo(), "El título debería coincidir");
        assertEquals(expectedFechaInicio, experienciaDto.getFechaInicioProyecto(), "La fecha de inicio debería coincidir");
        assertEquals(expectedFechaFin, experienciaDto.getFechaFinProyecto(), "La fecha de fin debería coincidir");
        assertEquals(expectedDescripcion, experienciaDto.getDescripcion(), "La descripción debería coincidir");
        assertEquals(expectedLink, experienciaDto.getLink(), "El link debería coincidir");
        assertEquals(expectedTipo, experienciaDto.getTipoExperiencia(), "El tipo de experiencia debería coincidir");
        assertEquals(expectedTecnologia, experienciaDto.getTecnologiaUsada(), "La tecnología usada debería coincidir");
        assertNotNull(experienciaDto.getImagen(), "La imagen no debería ser nula");
        assertEquals("proyecto.jpg", experienciaDto.getImagen().getUrl(), "La URL de la imagen debería coincidir");

        // Validar con Bean Validation
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear ExperienciaDto con constructor por defecto")
    void shouldCreateExperienciaDtoWithDefaultConstructor() {
        // Arrange & Act
        ExperienciaDto experienciaDto = new ExperienciaDto();

        // Assert
        assertNotNull(experienciaDto, "El objeto ExperienciaDto no debería ser nulo");
        assertNull(experienciaDto.getId(), "El ID debería ser nulo por defecto");
        assertNull(experienciaDto.getTitulo(), "El título debería ser nulo por defecto");
        assertNull(experienciaDto.getFechaInicioProyecto(), "La fecha de inicio debería ser nula por defecto");
        assertNull(experienciaDto.getFechaFinProyecto(), "La fecha de fin debería ser nula por defecto");
        assertNull(experienciaDto.getDescripcion(), "La descripción debería ser nula por defecto");
        assertNull(experienciaDto.getLink(), "El link debería ser nulo por defecto");
        assertNull(experienciaDto.getTipoExperiencia(), "El tipo de experiencia debería ser nulo por defecto");
        assertNull(experienciaDto.getTecnologiaUsada(), "La tecnología usada debería ser nula por defecto");
        assertNull(experienciaDto.getImagen(), "La imagen debería ser nula por defecto");
    }

    @Test
    @DisplayName("Debería crear ExperienciaDto con constructor con todos los argumentos")
    void shouldCreateExperienciaDtoWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String titulo = "App de E-commerce";
        LocalDate fechaInicio = LocalDate.of(2024, 2, 1);
        LocalDate fechaFin = LocalDate.of(2024, 8, 15);
        String descripcion = "Desarrollo de tienda online con carrito de compras y pasarela de pago";
        String link = "https://github.com/usuario/ecommerce";
        TipoExperiencia tipo = TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO;
        TecnologiaUsada tecnologia = TecnologiaUsada.REACT;
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("ecommerce.jpg")
                .alt("Tienda online")
                .build();

        // Act
        ExperienciaDto experienciaDto = new ExperienciaDto(
                id, titulo, fechaInicio, fechaFin, descripcion,
                link, imagen, tipo, tecnologia
        );

        // Assert
        assertNotNull(experienciaDto, "El objeto ExperienciaDto no debería ser nulo");
        assertEquals(id, experienciaDto.getId(), "El ID debería coincidir");
        assertEquals(titulo, experienciaDto.getTitulo(), "El título debería coincidir");
        assertEquals(fechaInicio, experienciaDto.getFechaInicioProyecto(), "La fecha de inicio debería coincidir");
        assertEquals(fechaFin, experienciaDto.getFechaFinProyecto(), "La fecha de fin debería coincidir");
        assertEquals(descripcion, experienciaDto.getDescripcion(), "La descripción debería coincidir");
        assertEquals(link, experienciaDto.getLink(), "El link debería coincidir");
        assertEquals(tipo, experienciaDto.getTipoExperiencia(), "El tipo de experiencia debería coincidir");
        assertEquals(tecnologia, experienciaDto.getTecnologiaUsada(), "La tecnología usada debería coincidir");
        assertNotNull(experienciaDto.getImagen(), "La imagen no debería ser nula");
        assertEquals("ecommerce.jpg", experienciaDto.getImagen().getUrl(), "La URL de la imagen debería coincidir");
    }

    @Test
    @DisplayName("Debería permitir valores nulos en campos opcionales")
    void shouldAllowNullValuesInOptionalFields() {
        // Arrange & Act - ExperienciaDto sin campos opcionales
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .build();

        // Assert
        assertNotNull(experienciaDto, "El objeto ExperienciaDto no debería ser nulo");
        assertNull(experienciaDto.getImagen(), "La imagen debería ser nula");

        // Validar que no hay violaciones (imagen es opcional)
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con imagen nula");
    }

    @Test
    @DisplayName("Debería permitir fechaFinProyecto nula (proyecto en curso)")
    void shouldAllowNullFechaFinProyecto() {
        // Arrange - Experiencia con fecha de fin nula (proyecto en curso)
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto en Desarrollo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null) // Proyecto en curso
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act & Assert
        assertNotNull(experienciaDto, "El objeto ExperienciaDto no debería ser nulo");
        assertNull(experienciaDto.getFechaFinProyecto(), "La fecha de fin debería ser nula para proyectos en curso");

        // Validar que no hay violaciones (fechaFinProyecto es opcional)
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con fecha de fin nula");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - No debería tener violaciones para campos válidos")
    void validation_ShouldNotHaveViolations_WhenAllFieldsAreValid() {
        // Arrange - ExperienciaDto con todos los campos válidos
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para campos válidos");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando título es nulo")
    void validation_ShouldHaveViolations_WhenTituloIsNull() {
        // Arrange - Título nulo
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo(null)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para título nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debe haber violación específica para el campo título");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando título es muy corto")
    void validation_ShouldHaveViolations_WhenTituloIsTooShort() {
        // Arrange - Título muy corto (menos de 3 caracteres)
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("AB")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para título demasiado corto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debe haber violación específica para el campo título");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando título es muy largo")
    void validation_ShouldHaveViolations_WhenTituloIsTooLong() {
        // Arrange - Título muy largo
        String tituloLargo = "A".repeat(146); // Más de 145 caracteres
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo(tituloLargo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para título demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debe haber violación específica para el campo título");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando descripción es nula")
    void validation_ShouldHaveViolations_WhenDescripcionIsNull() {
        // Arrange - Descripción nula
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(null)
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando descripción es muy corta")
    void validation_ShouldHaveViolations_WhenDescripcionIsTooShort() {
        // Arrange - Descripción muy corta (menos de 5 caracteres)
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("1234")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción demasiado corta");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando descripción es muy larga")
    void validation_ShouldHaveViolations_WhenDescripcionIsTooLong() {
        // Arrange - Descripción muy larga
        String descripcionLarga = "A".repeat(301); // Más de 300 caracteres
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(descripcionLarga)
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción demasiado larga");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando link es nulo")
    void validation_ShouldHaveViolations_WhenLinkIsNull() {
        // Arrange - Link nulo
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link(null)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para link nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("link")),
                "Debe haber violación específica para el campo link");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando link es muy corto")
    void validation_ShouldHaveViolations_WhenLinkIsTooShort() {
        // Arrange - Link muy corto (menos de 5 caracteres)
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("http")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para link demasiado corto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("link")),
                "Debe haber violación específica para el campo link");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando link es muy largo")
    void validation_ShouldHaveViolations_WhenLinkIsTooLong() {
        // Arrange - Link muy largo
        String linkLargo = "A".repeat(301); // Más de 300 caracteres
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link(linkLargo)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para link demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("link")),
                "Debe haber violación específica para el campo link");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando fechaInicioProyecto es nula")
    void validation_ShouldHaveViolations_WhenFechaInicioProyectoIsNull() {
        // Arrange - Fecha de inicio nula
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(null)
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para fecha de inicio nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fechaInicioProyecto")),
                "Debe haber violación específica para el campo fechaInicioProyecto");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando tipoExperiencia es nulo")
    void validation_ShouldHaveViolations_WhenTipoExperienciaIsNull() {
        // Arrange - Tipo de experiencia nulo
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(null)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para tipo de experiencia nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoExperiencia")),
                "Debe haber violación específica para el campo tipoExperiencia");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando tecnologiaUsada es nulo")
    void validation_ShouldHaveViolations_WhenTecnologiaUsadaIsNull() {
        // Arrange - Tecnología usada nula
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(null)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para tecnología usada nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tecnologiaUsada")),
                "Debe haber violación específica para el campo tecnologiaUsada");
    }

    // ==================== TESTS DE ENUMS ====================

    @Test
    @DisplayName("Debe manejar todos los tipos de experiencia del enum")
    void shouldHandleAllTiposExperiencia() {
        // Arrange - Probar todos los tipos de experiencia
        TipoExperiencia[] tipos = TipoExperiencia.values();

        for (TipoExperiencia tipo : tipos) {
            // Act
            ExperienciaDto experienciaDto = ExperienciaDto.builder()
                    .id(1)
                    .titulo("Proyecto Test para " + tipo.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/test")
                    .tipoExperiencia(tipo)
                    .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                    .build();

            // Assert
            assertNotNull(experienciaDto, "La ExperienciaDto no debería ser nula para tipo: " + tipo);
            assertEquals(tipo, experienciaDto.getTipoExperiencia(), "Debería manejar correctamente el tipo: " + tipo);
        }
    }

    @Test
    @DisplayName("Debe manejar todas las tecnologías del enum")
    void shouldHandleAllTecnologiasUsada() {
        // Arrange - Probar todas las tecnologías
        TecnologiaUsada[] tecnologias = TecnologiaUsada.values();

        for (TecnologiaUsada tecnologia : tecnologias) {
            // Act
            ExperienciaDto experienciaDto = ExperienciaDto.builder()
                    .id(1)
                    .titulo("Proyecto con " + tecnologia.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/test")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiaUsada(tecnologia)
                    .build();

            // Assert
            assertNotNull(experienciaDto, "La ExperienciaDto no debería ser nula para tecnología: " + tecnologia);
            assertEquals(tecnologia, experienciaDto.getTecnologiaUsada(), "Debería manejar correctamente la tecnología: " + tecnologia);
        }
    }

    // ==================== TESTS DE RELACIONES ====================

    @Test
    @DisplayName("Debe manejar relación con ImagenDto correctamente")
    void shouldHandleRelationshipWithImagenDtoCorrectly() {
        // Arrange
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("proyecto.jpg")
                .alt("Captura del proyecto")
                .build();

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Con Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-con-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertNotNull(experienciaDto.getImagen(), "La imagen no debería ser nula");
        assertEquals(1, experienciaDto.getImagen().getId(), "El ID de la imagen debería coincidir");
        assertEquals("proyecto.jpg", experienciaDto.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Captura del proyecto", experienciaDto.getImagen().getAlt(), "El alt de la imagen debería coincidir");
    }

    @Test
    @DisplayName("Debe manejar creación de ExperienciaDto sin imagen")
    void shouldHandleExperienciaDtoWithoutImagen() {
        // Arrange - ExperienciaDto sin imagen
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertNotNull(experienciaDto, "La ExperienciaDto no debería ser nula");
        assertNull(experienciaDto.getImagen(), "La imagen debería ser nula");
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Inicial")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción inicial válida")
                .link("https://github.com/usuario/inicial")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Crear nueva imagen
        ImagenDto nuevaImagen = ImagenDto.builder()
                .id(2)
                .url("nuevo-proyecto.jpg")
                .alt("Nuevo proyecto")
                .build();

        // Act
        experienciaDto.setId(2);
        experienciaDto.setTitulo("Título Actualizado");
        experienciaDto.setFechaInicioProyecto(LocalDate.of(2024, 2, 1));
        experienciaDto.setFechaFinProyecto(LocalDate.of(2024, 7, 1));
        experienciaDto.setDescripcion("Descripción actualizada con más de 5 caracteres");
        experienciaDto.setLink("https://github.com/usuario/actualizado");
        experienciaDto.setTipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE);
        experienciaDto.setTecnologiaUsada(TecnologiaUsada.REACT);
        experienciaDto.setImagen(nuevaImagen);

        // Assert
        assertEquals(2, experienciaDto.getId(), "El ID debería estar actualizado");
        assertEquals("Título Actualizado", experienciaDto.getTitulo(), "El título debería estar actualizado");
        assertEquals(LocalDate.of(2024, 2, 1), experienciaDto.getFechaInicioProyecto(), "La fecha de inicio debería estar actualizada");
        assertEquals(LocalDate.of(2024, 7, 1), experienciaDto.getFechaFinProyecto(), "La fecha de fin debería estar actualizada");
        assertEquals("Descripción actualizada con más de 5 caracteres", experienciaDto.getDescripcion(), "La descripción debería estar actualizada");
        assertEquals("https://github.com/usuario/actualizado", experienciaDto.getLink(), "El link debería estar actualizado");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE, experienciaDto.getTipoExperiencia(), "El tipo de experiencia debería estar actualizado");
        assertEquals(TecnologiaUsada.REACT, experienciaDto.getTecnologiaUsada(), "La tecnología usada debería estar actualizada");
        assertNotNull(experienciaDto.getImagen(), "La imagen no debería ser nula");
        assertEquals("nuevo-proyecto.jpg", experienciaDto.getImagen().getUrl(), "La URL de la imagen debería estar actualizada");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos ExperienciaDto con los mismos valores")
    void shouldBeEqualWhenComparingTwoExperienciaDtosWithSameValues() {
        // Arrange
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("proyecto.jpg")
                .alt("Captura del proyecto")
                .build();

        ExperienciaDto experienciaDto1 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .build();

        ExperienciaDto experienciaDto2 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertEquals(experienciaDto1, experienciaDto2, "Los objetos ExperienciaDto deberían ser iguales");
        assertEquals(experienciaDto1.hashCode(), experienciaDto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos ExperienciaDto con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoExperienciaDtosWithDifferentValues() {
        // Arrange
        ExperienciaDto experienciaDto1 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto A")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto A")
                .link("https://github.com/usuario/proyecto-a")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        ExperienciaDto experienciaDto2 = ExperienciaDto.builder()
                .id(2)
                .titulo("Proyecto B")
                .fechaInicioProyecto(LocalDate.of(2024, 2, 1))
                .fechaFinProyecto(LocalDate.of(2024, 7, 1))
                .descripcion("Descripción del proyecto B")
                .link("https://github.com/usuario/proyecto-b")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .build();

        // Act & Assert
        assertNotEquals(experienciaDto1, experienciaDto2, "Los objetos ExperienciaDto no deberían ser iguales");
        assertNotEquals(experienciaDto1.hashCode(), experienciaDto2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Validación - Debe manejar títulos en el límite de longitud (3 y 145 caracteres)")
    void validation_ShouldHandleTituloAtLengthBoundaries() {
        // Arrange - Título exactamente de 3 caracteres (mínimo)
        String tituloMinimo = "ABC";
        ExperienciaDto experienciaDtoMinimo = ExperienciaDto.builder()
                .id(1)
                .titulo(tituloMinimo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act - Validar título mínimo
        Set<ConstraintViolation<ExperienciaDto>> violationsMinimo = validator.validate(experienciaDtoMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para título exactamente de 3 caracteres");
        assertEquals(3, experienciaDtoMinimo.getTitulo().length(), "El título debería tener exactamente 3 caracteres");

        // Arrange - Título exactamente de 145 caracteres (máximo)
        String tituloMaximo = "A".repeat(145);
        ExperienciaDto experienciaDtoMaximo = ExperienciaDto.builder()
                .id(1)
                .titulo(tituloMaximo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act - Validar título máximo
        Set<ConstraintViolation<ExperienciaDto>> violationsMaximo = validator.validate(experienciaDtoMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para título exactamente de 145 caracteres");
        assertEquals(145, experienciaDtoMaximo.getTitulo().length(), "El título debería tener exactamente 145 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar descripción y link en límites de longitud (5 y 300 caracteres)")
    void validation_ShouldHandleDescripcionAndLinkAtLengthBoundaries() {
        // Arrange - Texto exactamente de 5 caracteres (mínimo)
        String textoMinimo = "12345";
        ExperienciaDto experienciaDtoMinimo = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(textoMinimo)
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act - Validar texto mínimo
        Set<ConstraintViolation<ExperienciaDto>> violationsMinimo = validator.validate(experienciaDtoMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para descripción exactamente de 5 caracteres");
        assertEquals(5, experienciaDtoMinimo.getDescripcion().length(), "La descripción debería tener exactamente 5 caracteres");

        // Arrange - Texto exactamente de 300 caracteres (máximo)
        String textoMaximo = "A".repeat(300);
        ExperienciaDto experienciaDtoMaximo = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link(textoMaximo)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act - Validar texto máximo
        Set<ConstraintViolation<ExperienciaDto>> violationsMaximo = validator.validate(experienciaDtoMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para link exactamente de 300 caracteres");
        assertEquals(300, experienciaDtoMaximo.getLink().length(), "El link debería tener exactamente 300 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en campos de texto")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Texto con caracteres especiales y acentos
        String tituloEspecial = "Proyecto con áéíóú y ñ: gestión de usuarios";
        String descripcionEspecial = "Descripción con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";
        String linkEspecial = "https://github.com/usuario/proyecto-con-acentos-y-caracteres-especiales";

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo(tituloEspecial)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(descripcionEspecial)
                .link(linkEspecial)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(tituloEspecial, experienciaDto.getTitulo(), "El título con caracteres especiales debería mantenerse");
        assertEquals(descripcionEspecial, experienciaDto.getDescripcion(), "La descripción con caracteres especiales debería mantenerse");
        assertEquals(linkEspecial, experienciaDto.getLink(), "El link con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Validación - Debe aceptar fechas con diferentes valores")
    void validation_ShouldAcceptDifferentDateValues() {
        // Arrange - Fechas con diferentes valores
        LocalDate fechaInicioPasado = LocalDate.of(2020, 1, 1);
        LocalDate fechaFinReciente = LocalDate.of(2024, 12, 31);

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto con fechas variadas")
                .fechaInicioProyecto(fechaInicioPasado)
                .fechaFinProyecto(fechaFinReciente)
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para fechas válidas");
        assertEquals(fechaInicioPasado, experienciaDto.getFechaInicioProyecto(), "La fecha de inicio debería mantenerse");
        assertEquals(fechaFinReciente, experienciaDto.getFechaFinProyecto(), "La fecha de fin debería mantenerse");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        ExperienciaDto experienciaDto = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            experienciaDto.getTitulo(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE toString ====================

    @Test
    @DisplayName("toString debería incluir información relevante del ExperienciaDto")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        String toStringResult = experienciaDto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Proyecto Test"), "toString debería contener el título");
        assertTrue(toStringResult.contains("PROYECTO_PERSONAL"), "toString debería contener el tipo de experiencia");
        assertTrue(toStringResult.contains("SPRINGBOOT"), "toString debería contener la tecnología usada");
    }

    @Test
    @DisplayName("toString no debería lanzar excepción cuando hay campos nulos")
    void toString_ShouldNotThrowException_WhenFieldsAreNull() {
        // Arrange - ExperienciaDto con campos nulos
        ExperienciaDto experienciaDto = new ExperienciaDto();

        // Act & Assert
        assertDoesNotThrow(() -> {
            String result = experienciaDto.toString();
            assertNotNull(result, "toString no debería ser nulo");
        }, "toString no debería lanzar excepción con campos nulos");
    }
}