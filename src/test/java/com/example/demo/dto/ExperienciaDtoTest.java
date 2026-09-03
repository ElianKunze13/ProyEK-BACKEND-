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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        List<TecnologiaUsada> expectedTecnologias = List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA);

        // Crear lista de imágenes para prueba
        List<ImagenDto> imagenes = Arrays.asList(
                ImagenDto.builder()
                        .id(1)
                        .url("proyecto-principal.jpg")
                        .alt("Captura principal del proyecto")
                        .build(),
                ImagenDto.builder()
                        .id(2)
                        .url("proyecto-detalle.jpg")
                        .alt("Detalle del proyecto")
                        .build()
        );

        // Act - Ejecutar la acción a probar
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(expectedId)
                .titulo(expectedTitulo)
                .fechaInicioProyecto(expectedFechaInicio)
                .fechaFinProyecto(expectedFechaFin)
                .descripcion(expectedDescripcion)
                .link(expectedLink)
                .tipoExperiencia(expectedTipo)
                .tecnologiasUsadas(expectedTecnologias)
                .imagenes(imagenes) // 🔥 Cambiado a lista de imágenes
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
        assertNotNull(experienciaDto.getTecnologiasUsadas(), "La lista de tecnologías no debería ser nula");
        assertTrue(experienciaDto.getTecnologiasUsadas().containsAll(expectedTecnologias),
                "Las tecnologías usadas deberían coincidir");
        assertNotNull(experienciaDto.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, experienciaDto.getImagenes().size(), "Debería tener 2 imágenes");
        assertEquals("proyecto-principal.jpg", experienciaDto.getImagenes().get(0).getUrl(),
                "La URL de la primera imagen debería coincidir");
        assertEquals("proyecto-detalle.jpg", experienciaDto.getImagenes().get(1).getUrl(),
                "La URL de la segunda imagen debería coincidir");

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
        assertNull(experienciaDto.getTecnologiasUsadas(), "Las tecnologías usadas deberían ser nulas por defecto");
        assertNull(experienciaDto.getImagenes(), "La lista de imágenes debería ser nula por defecto");
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
        List<TecnologiaUsada> tecnologias = List.of(TecnologiaUsada.REACT, TecnologiaUsada.TYPESCRIPT);

        List<ImagenDto> imagenes = Arrays.asList(
                ImagenDto.builder()
                        .id(1)
                        .url("ecommerce-home.jpg")
                        .alt("Página principal de la tienda")
                        .build(),
                ImagenDto.builder()
                        .id(2)
                        .url("ecommerce-checkout.jpg")
                        .alt("Proceso de checkout")
                        .build(),
                ImagenDto.builder()
                        .id(3)
                        .url("ecommerce-admin.jpg")
                        .alt("Panel de administración")
                        .build()
        );

        // Act
        ExperienciaDto experienciaDto = new ExperienciaDto(
                id, titulo, fechaInicio, fechaFin, descripcion,
                link, imagenes, tipo, tecnologias // 🔥 Cambiado a lista de imágenes
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
        assertNotNull(experienciaDto.getTecnologiasUsadas(), "La lista de tecnologías no debería ser nula");
        assertTrue(experienciaDto.getTecnologiasUsadas().containsAll(tecnologias),
                "Las tecnologías usadas deberían coincidir");
        assertNotNull(experienciaDto.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(3, experienciaDto.getImagenes().size(), "Debería tener 3 imágenes");
        assertEquals("ecommerce-home.jpg", experienciaDto.getImagenes().get(0).getUrl());
        assertEquals("ecommerce-checkout.jpg", experienciaDto.getImagenes().get(1).getUrl());
        assertEquals("ecommerce-admin.jpg", experienciaDto.getImagenes().get(2).getUrl());
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(null) // 🔥 Lista de imágenes nula (opcional)
                .build();

        // Assert
        assertNotNull(experienciaDto, "El objeto ExperienciaDto no debería ser nulo");
        assertNull(experienciaDto.getImagenes(), "La lista de imágenes debería ser nula");

        // Validar que no hay violaciones (imagenes es opcional)
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con lista de imágenes nula");
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(Collections.emptyList()) // Lista vacía
                .build();

        // Act & Assert
        assertNotNull(experienciaDto, "El objeto ExperienciaDto no debería ser nulo");
        assertNull(experienciaDto.getFechaFinProyecto(), "La fecha de fin debería ser nula para proyectos en curso");

        // Validar que no hay violaciones (fechaFinProyecto es opcional)
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con fecha de fin nula");
    }

    @Test
    @DisplayName("Debería permitir lista vacía de tecnologías")
    void shouldAllowEmptyTecnologiasUsadas() {
        // Arrange - Experiencia con lista vacía de tecnologías
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto sin tecnologías")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-tecnologias")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of()) // Lista vacía
                .imagenes(Collections.emptyList())
                .build();

        // Act & Assert
        assertNotNull(experienciaDto, "El objeto ExperienciaDto no debería ser nulo");
        assertNotNull(experienciaDto.getTecnologiasUsadas(), "La lista de tecnologías no debería ser nula");
        assertTrue(experienciaDto.getTecnologiasUsadas().isEmpty(), "La lista de tecnologías debería estar vacía");
    }

    @Test
    @DisplayName("Debería permitir lista vacía de imágenes")
    void shouldAllowEmptyImagenesList() {
        // Arrange - Experiencia con lista vacía de imágenes
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto sin imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(Collections.emptyList()) // Lista vacía de imágenes
                .build();

        // Act & Assert
        assertNotNull(experienciaDto, "El objeto ExperienciaDto no debería ser nulo");
        assertNotNull(experienciaDto.getImagenes(), "La lista de imágenes no debería ser nula");
        assertTrue(experienciaDto.getImagenes().isEmpty(), "La lista de imágenes debería estar vacía");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - No debería tener violaciones para campos válidos")
    void validation_ShouldNotHaveViolations_WhenAllFieldsAreValid() {
        // Arrange - ExperienciaDto con todos los campos válidos
        List<ImagenDto> imagenes = List.of(
                ImagenDto.builder().url("test.jpg").alt("Test image").build()
        );

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(imagenes)
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para tipo de experiencia nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoExperiencia")),
                "Debe haber violación específica para el campo tipoExperiencia");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando tecnologiasUsadas es nulo")
    void validation_ShouldHaveViolations_WhenTecnologiasUsadasIsNull() {
        // Arrange - Tecnologías usadas nulas
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(null)
                .imagenes(Collections.emptyList())
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para tecnologías usadas nulas");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tecnologiasUsadas")),
                "Debe haber violación específica para el campo tecnologiasUsadas");
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
                    .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                    .imagenes(Collections.emptyList())
                    .build();

            // Assert
            assertNotNull(experienciaDto, "La ExperienciaDto no debería ser nula para tipo: " + tipo);
            assertEquals(tipo, experienciaDto.getTipoExperiencia(), "Debería manejar correctamente el tipo: " + tipo);
        }
    }

    @Test
    @DisplayName("Debe manejar todas las tecnologías del enum en lista")
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
                    .tecnologiasUsadas(List.of(tecnologia))
                    .imagenes(Collections.emptyList())
                    .build();

            // Assert
            assertNotNull(experienciaDto, "La ExperienciaDto no debería ser nula para tecnología: " + tecnologia);
            assertTrue(experienciaDto.getTecnologiasUsadas().contains(tecnologia),
                    "Debería manejar correctamente la tecnología: " + tecnologia);
        }
    }

    @Test
    @DisplayName("Debe manejar múltiples tecnologías en la lista")
    void shouldHandleMultipleTecnologiasUsada() {
        // Arrange
        List<TecnologiaUsada> tecnologiasMultiples = Arrays.asList(
                TecnologiaUsada.JAVA,
                TecnologiaUsada.SPRINGBOOT,
                TecnologiaUsada.REACT,
                TecnologiaUsada.TYPESCRIPT
        );

        // Act
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto con múltiples tecnologías")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologiasMultiples)
                .imagenes(Collections.emptyList())
                .build();

        // Assert
        assertNotNull(experienciaDto, "La ExperienciaDto no debería ser nula");
        assertNotNull(experienciaDto.getTecnologiasUsadas(), "La lista de tecnologías no debería ser nula");
        assertEquals(4, experienciaDto.getTecnologiasUsadas().size(), "Debería tener 4 tecnologías");
        assertTrue(experienciaDto.getTecnologiasUsadas().containsAll(tecnologiasMultiples),
                "Debería contener todas las tecnologías");
    }

    // ==================== TESTS DE RELACIONES ====================

    @Test
    @DisplayName("Debe manejar relación con lista de ImagenDto correctamente")
    void shouldHandleRelationshipWithImagenDtoListCorrectly() {
        // Arrange
        List<ImagenDto> imagenes = Arrays.asList(
                ImagenDto.builder()
                        .id(1)
                        .url("imagen-1.jpg")
                        .alt("Primera imagen del proyecto")
                        .build(),
                ImagenDto.builder()
                        .id(2)
                        .url("imagen-2.jpg")
                        .alt("Segunda imagen del proyecto")
                        .build()
        );

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Con Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-con-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(imagenes)
                .build();

        // Act & Assert
        assertNotNull(experienciaDto.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, experienciaDto.getImagenes().size(), "Debería tener 2 imágenes");
        assertEquals(1, experienciaDto.getImagenes().get(0).getId(), "El ID de la primera imagen debería coincidir");
        assertEquals("imagen-1.jpg", experienciaDto.getImagenes().get(0).getUrl(),
                "La URL de la primera imagen debería coincidir");
        assertEquals("Primera imagen del proyecto", experienciaDto.getImagenes().get(0).getAlt(),
                "El alt de la primera imagen debería coincidir");
        assertEquals(2, experienciaDto.getImagenes().get(1).getId(), "El ID de la segunda imagen debería coincidir");
        assertEquals("imagen-2.jpg", experienciaDto.getImagenes().get(1).getUrl(),
                "La URL de la segunda imagen debería coincidir");
        assertEquals("Segunda imagen del proyecto", experienciaDto.getImagenes().get(1).getAlt(),
                "El alt de la segunda imagen debería coincidir");
    }

    @Test
    @DisplayName("Debe manejar creación de ExperienciaDto sin imágenes")
    void shouldHandleExperienciaDtoWithoutImagenes() {
        // Arrange - ExperienciaDto sin imágenes
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Sin Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(null)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertNotNull(experienciaDto, "La ExperienciaDto no debería ser nula");
        assertNull(experienciaDto.getImagenes(), "La lista de imágenes debería ser nula");
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debe manejar lista de imágenes con elementos nulos")
    void shouldHandleImagenesListWithNullElements() {
        // Arrange - Lista de imágenes con elementos nulos
        List<ImagenDto> imagenesConNull = Arrays.asList(
                ImagenDto.builder().id(1).url("img1.jpg").alt("Imagen 1").build(),
                null,
                ImagenDto.builder().id(3).url("img3.jpg").alt("Imagen 3").build()
        );

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto con imágenes nulas")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(imagenesConNull)
                .build();

        // Act & Assert
        assertNotNull(experienciaDto, "La ExperienciaDto no debería ser nula");
        assertNotNull(experienciaDto.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(3, experienciaDto.getImagenes().size(), "Debería tener 3 elementos");
        assertNull(experienciaDto.getImagenes().get(1), "El segundo elemento debería ser nulo");
        assertNotNull(experienciaDto.getImagenes().get(0), "El primer elemento no debería ser nulo");
        assertNotNull(experienciaDto.getImagenes().get(2), "El tercer elemento no debería ser nulo");
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(Collections.emptyList())
                .build();

        // Crear nuevas imágenes
        List<ImagenDto> nuevasImagenes = Arrays.asList(
                ImagenDto.builder()
                        .id(2)
                        .url("nuevo-proyecto-1.jpg")
                        .alt("Nuevo proyecto imagen 1")
                        .build(),
                ImagenDto.builder()
                        .id(3)
                        .url("nuevo-proyecto-2.jpg")
                        .alt("Nuevo proyecto imagen 2")
                        .build()
        );

        List<TecnologiaUsada> nuevasTecnologias = List.of(TecnologiaUsada.REACT, TecnologiaUsada.TYPESCRIPT);

        // Act
        experienciaDto.setId(2);
        experienciaDto.setTitulo("Título Actualizado");
        experienciaDto.setFechaInicioProyecto(LocalDate.of(2024, 2, 1));
        experienciaDto.setFechaFinProyecto(LocalDate.of(2024, 7, 1));
        experienciaDto.setDescripcion("Descripción actualizada con más de 5 caracteres");
        experienciaDto.setLink("https://github.com/usuario/actualizado");
        experienciaDto.setTipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE);
        experienciaDto.setTecnologiasUsadas(nuevasTecnologias);
        experienciaDto.setImagenes(nuevasImagenes);

        // Assert
        assertEquals(2, experienciaDto.getId(), "El ID debería estar actualizado");
        assertEquals("Título Actualizado", experienciaDto.getTitulo(), "El título debería estar actualizado");
        assertEquals(LocalDate.of(2024, 2, 1), experienciaDto.getFechaInicioProyecto(),
                "La fecha de inicio debería estar actualizada");
        assertEquals(LocalDate.of(2024, 7, 1), experienciaDto.getFechaFinProyecto(),
                "La fecha de fin debería estar actualizada");
        assertEquals("Descripción actualizada con más de 5 caracteres", experienciaDto.getDescripcion(),
                "La descripción debería estar actualizada");
        assertEquals("https://github.com/usuario/actualizado", experienciaDto.getLink(),
                "El link debería estar actualizado");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE, experienciaDto.getTipoExperiencia(),
                "El tipo de experiencia debería estar actualizado");
        assertNotNull(experienciaDto.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(2, experienciaDto.getTecnologiasUsadas().size(), "Debería tener 2 tecnologías");
        assertTrue(experienciaDto.getTecnologiasUsadas().containsAll(nuevasTecnologias),
                "Las tecnologías usadas deberían estar actualizadas");
        assertNotNull(experienciaDto.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, experienciaDto.getImagenes().size(), "Debería tener 2 imágenes");
        assertEquals("nuevo-proyecto-1.jpg", experienciaDto.getImagenes().get(0).getUrl(),
                "La URL de la primera imagen debería estar actualizada");
        assertEquals("nuevo-proyecto-2.jpg", experienciaDto.getImagenes().get(1).getUrl(),
                "La URL de la segunda imagen debería estar actualizada");
    }

    @Test
    @DisplayName("Debería actualizar lista de imágenes a null")
    void shouldUpdateImagenesListToNull() {
        // Arrange
        List<ImagenDto> imagenesIniciales = List.of(
                ImagenDto.builder().id(1).url("img1.jpg").alt("Imagen 1").build()
        );

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(imagenesIniciales)
                .build();

        // Act
        experienciaDto.setImagenes(null);

        // Assert
        assertNull(experienciaDto.getImagenes(), "La lista de imágenes debería ser nula");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos ExperienciaDto con los mismos valores")
    void shouldBeEqualWhenComparingTwoExperienciaDtosWithSameValues() {
        // Arrange
        List<ImagenDto> imagenes = Arrays.asList(
                ImagenDto.builder()
                        .id(1)
                        .url("proyecto.jpg")
                        .alt("Captura del proyecto")
                        .build(),
                ImagenDto.builder()
                        .id(2)
                        .url("proyecto-detalle.jpg")
                        .alt("Detalle del proyecto")
                        .build()
        );

        List<TecnologiaUsada> tecnologias = List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA);

        ExperienciaDto experienciaDto1 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologias)
                .imagenes(imagenes)
                .build();

        ExperienciaDto experienciaDto2 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologias)
                .imagenes(imagenes)
                .build();

        // Act & Assert
        assertEquals(experienciaDto1, experienciaDto2, "Los objetos ExperienciaDto deberían ser iguales");
        assertEquals(experienciaDto1.hashCode(), experienciaDto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos ExperienciaDto con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoExperienciaDtosWithDifferentValues() {
        // Arrange
        List<ImagenDto> imagenes1 = List.of(
                ImagenDto.builder().id(1).url("img1.jpg").alt("Imagen 1").build()
        );

        List<ImagenDto> imagenes2 = List.of(
                ImagenDto.builder().id(2).url("img2.jpg").alt("Imagen 2").build()
        );

        ExperienciaDto experienciaDto1 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto A")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto A")
                .link("https://github.com/usuario/proyecto-a")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(imagenes1)
                .build();

        ExperienciaDto experienciaDto2 = ExperienciaDto.builder()
                .id(2)
                .titulo("Proyecto B")
                .fechaInicioProyecto(LocalDate.of(2024, 2, 1))
                .fechaFinProyecto(LocalDate.of(2024, 7, 1))
                .descripcion("Descripción del proyecto B")
                .link("https://github.com/usuario/proyecto-b")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON))
                .imagenes(imagenes2)
                .build();

        // Act & Assert
        assertNotEquals(experienciaDto1, experienciaDto2, "Los objetos ExperienciaDto no deberían ser iguales");
        assertNotEquals(experienciaDto1.hashCode(), experienciaDto2.hashCode(),
                "Los hashCodes no deberían ser iguales");
    }

    @Test
    @DisplayName("Dos ExperienciaDto con diferentes listas de imágenes no deberían ser iguales")
    void shouldNotBeEqual_WhenImagenesListsAreDifferent() {
        // Arrange
        List<ImagenDto> imagenes1 = Arrays.asList(
                ImagenDto.builder().id(1).url("img1.jpg").alt("Imagen 1").build(),
                ImagenDto.builder().id(2).url("img2.jpg").alt("Imagen 2").build()
        );

        List<ImagenDto> imagenes2 = List.of(
                ImagenDto.builder().id(1).url("img1.jpg").alt("Imagen 1").build()
        );

        ExperienciaDto dto1 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://test.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(imagenes1)
                .build();

        ExperienciaDto dto2 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://test.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(imagenes2)
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales con diferentes listas de imágenes");
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
                .build();

        // Act - Validar título mínimo
        Set<ConstraintViolation<ExperienciaDto>> violationsMinimo = validator.validate(experienciaDtoMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para título exactamente de 3 caracteres");
        assertEquals(3, experienciaDtoMinimo.getTitulo().length(),
                "El título debería tener exactamente 3 caracteres");

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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
                .build();

        // Act - Validar título máximo
        Set<ConstraintViolation<ExperienciaDto>> violationsMaximo = validator.validate(experienciaDtoMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para título exactamente de 145 caracteres");
        assertEquals(145, experienciaDtoMaximo.getTitulo().length(),
                "El título debería tener exactamente 145 caracteres");
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
                .build();

        // Act - Validar texto mínimo
        Set<ConstraintViolation<ExperienciaDto>> violationsMinimo = validator.validate(experienciaDtoMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(),
                "No debería haber violaciones para descripción exactamente de 5 caracteres");
        assertEquals(5, experienciaDtoMinimo.getDescripcion().length(),
                "La descripción debería tener exactamente 5 caracteres");

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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
                .build();

        // Act - Validar texto máximo
        Set<ConstraintViolation<ExperienciaDto>> violationsMaximo = validator.validate(experienciaDtoMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(),
                "No debería haber violaciones para link exactamente de 300 caracteres");
        assertEquals(300, experienciaDtoMaximo.getLink().length(),
                "El link debería tener exactamente 300 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en campos de texto")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Texto con caracteres especiales y acentos
        String tituloEspecial = "Proyecto con áéíóú y ñ: gestión de usuarios";
        String descripcionEspecial = "Descripción con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";
        String linkEspecial = "https://github.com/usuario/proyecto-con-acentos-y-caracteres-especiales";

        List<ImagenDto> imagenes = List.of(
                ImagenDto.builder()
                        .url("imagen-con-acentos-áéíóú.jpg")
                        .alt("Imagen con acentos")
                        .build()
        );

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo(tituloEspecial)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(descripcionEspecial)
                .link(linkEspecial)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(imagenes)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(tituloEspecial, experienciaDto.getTitulo(),
                "El título con caracteres especiales debería mantenerse");
        assertEquals(descripcionEspecial, experienciaDto.getDescripcion(),
                "La descripción con caracteres especiales debería mantenerse");
        assertEquals(linkEspecial, experienciaDto.getLink(),
                "El link con caracteres especiales debería mantenerse");
        assertEquals("imagen-con-acentos-áéíóú.jpg", experienciaDto.getImagenes().get(0).getUrl(),
                "La URL de la imagen con acentos debería mantenerse");
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList())
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para fechas válidas");
        assertEquals(fechaInicioPasado, experienciaDto.getFechaInicioProyecto(),
                "La fecha de inicio debería mantenerse");
        assertEquals(fechaFinReciente, experienciaDto.getFechaFinProyecto(),
                "La fecha de fin debería mantenerse");
    }

    @Test
    @DisplayName("Validación - Debe manejar lista de imágenes con URLs vacías")
    void validation_ShouldHandleImagenesWithEmptyUrls() {
        // Arrange - Imágenes con URLs vacías (no hay validación en el DTO para esto)
        List<ImagenDto> imagenes = Arrays.asList(
                ImagenDto.builder().url("").alt("URL vacía").build(),
                ImagenDto.builder().url("   ").alt("URL con espacios").build()
        );

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto con URLs vacías")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(imagenes)
                .build();

        // Act
        Set<ConstraintViolation<ExperienciaDto>> violations = validator.validate(experienciaDto);

        // Assert - Nota: No hay validación específica para URLs de imágenes en el DTO
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
        assertEquals("", experienciaDto.getImagenes().get(0).getUrl(), "La URL vacía debería mantenerse");
        assertEquals("   ", experienciaDto.getImagenes().get(1).getUrl(), "La URL con espacios debería mantenerse");
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
        List<ImagenDto> imagenes = List.of(
                ImagenDto.builder().id(1).url("test.jpg").alt("Test image").build()
        );

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(imagenes)
                .build();

        // Act
        String toStringResult = experienciaDto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Proyecto Test"), "toString debería contener el título");
        assertTrue(toStringResult.contains("PROYECTO_PERSONAL"), "toString debería contener el tipo de experiencia");
        assertTrue(toStringResult.contains("SPRINGBOOT"), "toString debería contener la tecnología usada");
        assertTrue(toStringResult.contains("imagenes"), "toString debería contener el campo imagenes");
        assertTrue(toStringResult.contains("test.jpg"), "toString debería contener la URL de la imagen");
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

    @Test
    @DisplayName("toString debería mostrar lista de imágenes correctamente")
    void toString_ShouldShowImagenesListCorrectly() {
        // Arrange
        List<ImagenDto> imagenes = Arrays.asList(
                ImagenDto.builder().id(1).url("img1.jpg").alt("Imagen 1").build(),
                ImagenDto.builder().id(2).url("img2.jpg").alt("Imagen 2").build()
        );

        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto con imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(imagenes)
                .build();

        // Act
        String toStringResult = experienciaDto.toString();

        // Assert
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("img1.jpg"), "toString debería contener la URL de la primera imagen");
        assertTrue(toStringResult.contains("img2.jpg"), "toString debería contener la URL de la segunda imagen");
        assertTrue(toStringResult.contains("Imagen 1"), "toString debería contener el alt de la primera imagen");
        assertTrue(toStringResult.contains("Imagen 2"), "toString debería contener el alt de la segunda imagen");
    }
}