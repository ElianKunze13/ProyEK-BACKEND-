package com.example.demo.model;

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
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Experiencia
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class ExperienciaTest {

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
    @DisplayName("Debería crear Experiencia con valores válidos - Caso feliz")
    void shouldCreateExperienciaWithValidValues() {
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
        Imagen imagen = Imagen.builder()
                .url("proyecto.jpg")
                .alt("Captura del proyecto")
                .build();

        // Act - Ejecutar la acción a probar
        Experiencia experiencia = Experiencia.builder()
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
        assertNotNull(experiencia, "El objeto Experiencia no debería ser nulo");
        assertEquals(expectedId, experiencia.getId(), "El ID debería coincidir");
        assertEquals(expectedTitulo, experiencia.getTitulo(), "El título debería coincidir");
        assertEquals(expectedFechaInicio, experiencia.getFechaInicioProyecto(), "La fecha de inicio debería coincidir");
        assertEquals(expectedFechaFin, experiencia.getFechaFinProyecto(), "La fecha de fin debería coincidir");
        assertEquals(expectedDescripcion, experiencia.getDescripcion(), "La descripción debería coincidir");
        assertEquals(expectedLink, experiencia.getLink(), "El link debería coincidir");
        assertEquals(expectedTipo, experiencia.getTipoExperiencia(), "El tipo de experiencia debería coincidir");
        assertEquals(expectedTecnologia, experiencia.getTecnologiaUsada(), "La tecnología usada debería coincidir");
        assertNotNull(experiencia.getImagen(), "La imagen no debería ser nula");

        // Validar con Bean Validation
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear Experiencia con constructor por defecto")
    void shouldCreateExperienciaWithDefaultConstructor() {
        // Arrange & Act
        Experiencia experiencia = new Experiencia();

        // Assert
        assertNotNull(experiencia, "El objeto Experiencia no debería ser nulo");
        assertNull(experiencia.getId(), "El ID debería ser nulo por defecto");
        assertNull(experiencia.getTitulo(), "El título debería ser nulo por defecto");
        assertNull(experiencia.getFechaInicioProyecto(), "La fecha de inicio debería ser nula por defecto");
        assertNull(experiencia.getFechaFinProyecto(), "La fecha de fin debería ser nula por defecto");
        assertNull(experiencia.getDescripcion(), "La descripción debería ser nula por defecto");
        assertNull(experiencia.getLink(), "El link debería ser nulo por defecto");
        assertNull(experiencia.getTipoExperiencia(), "El tipo de experiencia debería ser nulo por defecto");
        assertNull(experiencia.getTecnologiaUsada(), "La tecnología usada debería ser nula por defecto");
        assertNull(experiencia.getImagen(), "La imagen debería ser nula por defecto");
        assertNull(experiencia.getUsuario(), "El usuario debería ser nulo por defecto");
    }

    @Test
    @DisplayName("Debería crear Experiencia con constructor con todos los argumentos")
    void shouldCreateExperienciaWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String titulo = "App de E-commerce";
        LocalDate fechaInicio = LocalDate.of(2024, 2, 1);
        LocalDate fechaFin = LocalDate.of(2024, 8, 15);
        String descripcion = "Desarrollo de tienda online con carrito de compras y pasarela de pago";
        String link = "https://github.com/usuario/ecommerce";
        TipoExperiencia tipo = TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO;
        TecnologiaUsada tecnologia = TecnologiaUsada.REACT;
        Imagen imagen = Imagen.builder().url("ecommerce.jpg").alt("Tienda online").build();
        Usuario usuario = Usuario.builder().id(1).nombre("Usuario Test").build();

        // Act
        Experiencia experiencia = new Experiencia(
                id, titulo, fechaInicio, fechaFin, descripcion,
                link, tipo, tecnologia, imagen, usuario
        );

        // Assert
        assertNotNull(experiencia, "El objeto Experiencia no debería ser nulo");
        assertEquals(id, experiencia.getId(), "El ID debería coincidir");
        assertEquals(titulo, experiencia.getTitulo(), "El título debería coincidir");
        assertEquals(fechaInicio, experiencia.getFechaInicioProyecto(), "La fecha de inicio debería coincidir");
        assertEquals(fechaFin, experiencia.getFechaFinProyecto(), "La fecha de fin debería coincidir");
        assertEquals(descripcion, experiencia.getDescripcion(), "La descripción debería coincidir");
        assertEquals(link, experiencia.getLink(), "El link debería coincidir");
        assertEquals(tipo, experiencia.getTipoExperiencia(), "El tipo de experiencia debería coincidir");
        assertEquals(tecnologia, experiencia.getTecnologiaUsada(), "La tecnología usada debería coincidir");
        assertNotNull(experiencia.getImagen(), "La imagen no debería ser nula");
        assertNotNull(experiencia.getUsuario(), "El usuario no debería ser nulo");
    }

    @Test
    @DisplayName("Debería permitir fechaFinProyecto nula (proyecto en curso)")
    void shouldAllowNullFechaFinProyecto() {
        // Arrange - Experiencia con fecha de fin nula (proyecto en curso)
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto en Desarrollo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null) // Proyecto en curso
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act & Assert
        assertNotNull(experiencia, "El objeto Experiencia no debería ser nulo");
        assertNull(experiencia.getFechaFinProyecto(), "La fecha de fin debería ser nula para proyectos en curso");

        // Validar que no hay violaciones (fechaFinProyecto es opcional)
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con fecha de fin nula");
    }

    @Test
    @DisplayName("Debería permitir imagen nula (campo opcional)")
    void shouldAllowNullImagen() {
        // Arrange - Experiencia sin imagen
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(null)
                .build();

        // Act & Assert
        assertNotNull(experiencia, "El objeto Experiencia no debería ser nulo");
        assertNull(experiencia.getImagen(), "La imagen debería ser nula");

        // Validar que no hay violaciones (imagen es opcional)
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con imagen nula");
    }

    @Test
    @DisplayName("Debería permitir usuario nulo (campo opcional)")
    void shouldAllowNullUsuario() {
        // Arrange - Experiencia sin usuario asociado
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Independiente")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 2, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-independiente")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVASCRIPT)
                .usuario(null)
                .build();

        // Act & Assert
        assertNotNull(experiencia, "El objeto Experiencia no debería ser nulo");
        assertNull(experiencia.getUsuario(), "El usuario debería ser nulo");

        // Validar que no hay violaciones (usuario es opcional)
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con usuario nulo");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        Experiencia experiencia = Experiencia.builder()
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
        Imagen nuevaImagen = Imagen.builder()
                .url("nuevo-proyecto.jpg")
                .alt("Nuevo proyecto")
                .build();

        Usuario nuevoUsuario = Usuario.builder()
                .id(2)
                .nombre("Nuevo Usuario")
                .build();

        // Act
        experiencia.setId(2);
        experiencia.setTitulo("Título Actualizado");
        experiencia.setFechaInicioProyecto(LocalDate.of(2024, 2, 1));
        experiencia.setFechaFinProyecto(LocalDate.of(2024, 7, 1));
        experiencia.setDescripcion("Descripción actualizada con más de 5 caracteres");
        experiencia.setLink("https://github.com/usuario/actualizado");
        experiencia.setTipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE);
        experiencia.setTecnologiaUsada(TecnologiaUsada.REACT);
        experiencia.setImagen(nuevaImagen);
        experiencia.setUsuario(nuevoUsuario);

        // Assert
        assertEquals(2, experiencia.getId(), "El ID debería estar actualizado");
        assertEquals("Título Actualizado", experiencia.getTitulo(), "El título debería estar actualizado");
        assertEquals(LocalDate.of(2024, 2, 1), experiencia.getFechaInicioProyecto(), "La fecha de inicio debería estar actualizada");
        assertEquals(LocalDate.of(2024, 7, 1), experiencia.getFechaFinProyecto(), "La fecha de fin debería estar actualizada");
        assertEquals("Descripción actualizada con más de 5 caracteres", experiencia.getDescripcion(), "La descripción debería estar actualizada");
        assertEquals("https://github.com/usuario/actualizado", experiencia.getLink(), "El link debería estar actualizado");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE, experiencia.getTipoExperiencia(), "El tipo de experiencia debería estar actualizado");
        assertEquals(TecnologiaUsada.REACT, experiencia.getTecnologiaUsada(), "La tecnología usada debería estar actualizada");
        assertNotNull(experiencia.getImagen(), "La imagen no debería ser nula");
        assertEquals("nuevo-proyecto.jpg", experiencia.getImagen().getUrl(), "La URL de la imagen debería estar actualizada");
        assertNotNull(experiencia.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(2, experiencia.getUsuario().getId(), "El ID del usuario debería estar actualizado");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos Experiencia con los mismos valores")
    void shouldBeEqualWhenComparingTwoExperienciasWithSameValues() {
        // Arrange
        Experiencia experiencia1 = Experiencia.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        Experiencia experiencia2 = Experiencia.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act & Assert
        assertEquals(experiencia1, experiencia2, "Los objetos Experiencia deberían ser iguales");
        assertEquals(experiencia1.hashCode(), experiencia2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos Experiencia con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoExperienciasWithDifferentValues() {
        // Arrange
        Experiencia experiencia1 = Experiencia.builder()
                .id(1)
                .titulo("Proyecto A")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto A")
                .link("https://github.com/usuario/proyecto-a")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        Experiencia experiencia2 = Experiencia.builder()
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
        assertNotEquals(experiencia1, experiencia2, "Los objetos Experiencia no deberían ser iguales");
        assertNotEquals(experiencia1.hashCode(), experiencia2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando título es nulo")
    void validation_ShouldHaveViolations_WhenTituloIsNull() {
        // Arrange - Título nulo
        Experiencia experiencia = Experiencia.builder()
                .titulo(null)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para título nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debe haber violación específica para el campo título");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando descripción está vacía o es nula")
    void validation_ShouldHaveViolations_WhenDescripcionIsNullOrEmpty(String descripcion) {
        // Arrange - Descripción vacía o nula
        Experiencia experiencia = Experiencia.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(descripcion)
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción vacía o nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando descripción es muy corta")
    void validation_ShouldHaveViolations_WhenDescripcionIsTooShort() {
        // Arrange - Descripción muy corta (menos de 5 caracteres)
        Experiencia experiencia = Experiencia.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("1234")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción demasiado corta");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando link está vacío o es nulo")
    void validation_ShouldHaveViolations_WhenLinkIsNullOrEmpty(String link) {
        // Arrange - Link vacío o nulo
        Experiencia experiencia = Experiencia.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link(link)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para link vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("link")),
                "Debe haber violación específica para el campo link");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando link es muy corto")
    void validation_ShouldHaveViolations_WhenLinkIsTooShort() {
        // Arrange - Link muy corto (menos de 5 caracteres)
        Experiencia experiencia = Experiencia.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("http")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para link demasiado corto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("link")),
                "Debe haber violación específica para el campo link");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando fechaInicioProyecto es nula")
    void validation_ShouldHaveViolations_WhenFechaInicioProyectoIsNull() {
        // Arrange - Fecha de inicio nula
        Experiencia experiencia = Experiencia.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(null)
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para fecha de inicio nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fechaInicioProyecto")),
                "Debe haber violación específica para el campo fechaInicioProyecto");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando tipoExperiencia es nulo")
    void validation_ShouldHaveViolations_WhenTipoExperienciaIsNull() {
        // Arrange - Tipo de experiencia nulo
        Experiencia experiencia = Experiencia.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(null)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para tipo de experiencia nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoExperiencia")),
                "Debe haber violación específica para el campo tipoExperiencia");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando tecnologiaUsada es nulo")
    void validation_ShouldHaveViolations_WhenTecnologiaUsadaIsNull() {
        // Arrange - Tecnología usada nula
        Experiencia experiencia = Experiencia.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(null)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para tecnología usada nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tecnologiaUsada")),
                "Debe haber violación específica para el campo tecnologiaUsada");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "ab"})
    @DisplayName("Validación - Debe tener violaciones cuando título es muy corto")
    void validation_ShouldHaveViolations_WhenTituloIsTooShort(String titulo) {
        // Arrange - Título muy corto (menos de 3 caracteres)
        Experiencia experiencia = Experiencia.builder()
                .titulo(titulo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para título demasiado corto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debe haber violación específica para el campo titulo");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        Experiencia experiencia = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            experiencia.getTitulo(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Validación - Debe manejar títulos en el límite de longitud (3 y 145 caracteres)")
    void validation_ShouldHandleTituloAtLengthBoundaries() {
        // Arrange - Título exactamente de 3 caracteres (mínimo)
        String tituloMinimo = "ABC";
        Experiencia experienciaMinimo = Experiencia.builder()
                .titulo(tituloMinimo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act - Validar título mínimo
        Set<ConstraintViolation<Experiencia>> violationsMinimo = validator.validate(experienciaMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para título exactamente de 3 caracteres");
        assertEquals(3, experienciaMinimo.getTitulo().length(), "El título debería tener exactamente 3 caracteres");

        // Arrange - Título exactamente de 145 caracteres (máximo)
        String tituloMaximo = "A".repeat(145);
        Experiencia experienciaMaximo = Experiencia.builder()
                .titulo(tituloMaximo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act - Validar título máximo
        Set<ConstraintViolation<Experiencia>> violationsMaximo = validator.validate(experienciaMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para título exactamente de 145 caracteres");
        assertEquals(145, experienciaMaximo.getTitulo().length(), "El título debería tener exactamente 145 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar descripción y link en límites de longitud (5 y 300 caracteres)")
    void validation_ShouldHandleDescripcionAndLinkAtLengthBoundaries() {
        // Arrange - Texto exactamente de 5 caracteres (mínimo)
        String textoMinimo = "12345";
        Experiencia experienciaMinimo = Experiencia.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(textoMinimo)
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act - Validar texto mínimo
        Set<ConstraintViolation<Experiencia>> violationsMinimo = validator.validate(experienciaMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para descripción exactamente de 5 caracteres");
        assertEquals(5, experienciaMinimo.getDescripcion().length(), "La descripción debería tener exactamente 5 caracteres");

        // Arrange - Texto exactamente de 300 caracteres (máximo)
        String textoMaximo = "A".repeat(300);
        Experiencia experienciaMaximo = Experiencia.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link(textoMaximo)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act - Validar texto máximo
        Set<ConstraintViolation<Experiencia>> violationsMaximo = validator.validate(experienciaMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para link exactamente de 300 caracteres");
        assertEquals(300, experienciaMaximo.getLink().length(), "El link debería tener exactamente 300 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en campos de texto")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Texto con caracteres especiales y acentos
        String tituloEspecial = "Proyecto con áéíóú y ñ: gestión de usuarios";
        String descripcionEspecial = "Descripción con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";
        String linkEspecial = "https://github.com/usuario/proyecto-con-acentos-y-caracteres-especiales";

        Experiencia experiencia = Experiencia.builder()
                .titulo(tituloEspecial)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(descripcionEspecial)
                .link(linkEspecial)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(tituloEspecial, experiencia.getTitulo(), "El título con caracteres especiales debería mantenerse");
        assertEquals(descripcionEspecial, experiencia.getDescripcion(), "La descripción con caracteres especiales debería mantenerse");
        assertEquals(linkEspecial, experiencia.getLink(), "El link con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Validación - Debe manejar todos los tipos de experiencia del enum")
    void validation_ShouldHandleAllTiposExperiencia() {
        // Arrange - Probar todos los tipos de experiencia
        TipoExperiencia[] tipos = TipoExperiencia.values();

        for (TipoExperiencia tipo : tipos) {
            // Act
            Experiencia experiencia = Experiencia.builder()
                    .titulo("Proyecto Test para " + tipo.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/test")
                    .tipoExperiencia(tipo)
                    .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                    .build();

            // Assert
            assertNotNull(experiencia, "La Experiencia no debería ser nula para tipo: " + tipo);
            assertEquals(tipo, experiencia.getTipoExperiencia(), "Debería manejar correctamente el tipo: " + tipo);
        }
    }

    @Test
    @DisplayName("Validación - Debe manejar todas las tecnologías del enum")
    void validation_ShouldHandleAllTecnologiasUsada() {
        // Arrange - Probar todas las tecnologías
        TecnologiaUsada[] tecnologias = TecnologiaUsada.values();

        for (TecnologiaUsada tecnologia : tecnologias) {
            // Act
            Experiencia experiencia = Experiencia.builder()
                    .titulo("Proyecto con " + tecnologia.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/test")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiaUsada(tecnologia)
                    .build();

            // Assert
            assertNotNull(experiencia, "La Experiencia no debería ser nula para tecnología: " + tecnologia);
            assertEquals(tecnologia, experiencia.getTecnologiaUsada(), "Debería manejar correctamente la tecnología: " + tecnologia);
        }
    }

    @Test
    @DisplayName("Validación - Debe aceptar fechas con diferentes valores")
    void validation_ShouldAcceptDifferentDateValues() {
        // Arrange - Fechas con diferentes valores
        LocalDate fechaInicioPasado = LocalDate.of(2020, 1, 1);
        LocalDate fechaFinReciente = LocalDate.of(2024, 12, 31);

        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto con fechas variadas")
                .fechaInicioProyecto(fechaInicioPasado)
                .fechaFinProyecto(fechaFinReciente)
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act
        Set<ConstraintViolation<Experiencia>> violations = validator.validate(experiencia);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para fechas válidas");
        assertEquals(fechaInicioPasado, experiencia.getFechaInicioProyecto(), "La fecha de inicio debería mantenerse");
        assertEquals(fechaFinReciente, experiencia.getFechaFinProyecto(), "La fecha de fin debería mantenerse");
    }

    @Test
    @DisplayName("Validación - Debe aceptar fechaFinProyecto igual o posterior a fechaInicio")
    void validation_ShouldAcceptFechaFinProyectoEqualOrAfterFechaInicio() {
        // Arrange - Fecha fin igual a fecha inicio
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFinIgual = LocalDate.of(2024, 1, 1);

        Experiencia experienciaIgual = Experiencia.builder()
                .titulo("Proyecto con fecha fin igual a inicio")
                .fechaInicioProyecto(fechaInicio)
                .fechaFinProyecto(fechaFinIgual)
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act & Assert
        Set<ConstraintViolation<Experiencia>> violationsIgual = validator.validate(experienciaIgual);
        assertTrue(violationsIgual.isEmpty(), "No debería haber violaciones para fecha fin igual a fecha inicio");

        // Arrange - Fecha fin posterior a fecha inicio
        LocalDate fechaFinPosterior = LocalDate.of(2024, 6, 1);

        Experiencia experienciaPosterior = Experiencia.builder()
                .titulo("Proyecto con fecha fin posterior a inicio")
                .fechaInicioProyecto(fechaInicio)
                .fechaFinProyecto(fechaFinPosterior)
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // Act & Assert
        Set<ConstraintViolation<Experiencia>> violationsPosterior = validator.validate(experienciaPosterior);
        assertTrue(violationsPosterior.isEmpty(), "No debería haber violaciones para fecha fin posterior a fecha inicio");
    }
}