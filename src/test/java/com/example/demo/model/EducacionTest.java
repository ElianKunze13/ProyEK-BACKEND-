package com.example.demo.model;

import com.example.demo.enums.TipoEducacion;
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
 * Pruebas unitarias para la clase Educacion
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class EducacionTest {

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
    @DisplayName("Debería crear Educacion con valores válidos - Caso feliz")
    void shouldCreateEducacionWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedTitulo = "Ingeniería en Sistemas";
        LocalDate expectedFechaObtencion = LocalDate.of(2020, 6, 15);
        String expectedDescripcion = "Carrera universitaria con enfoque en desarrollo de software y arquitectura de sistemas.";
        TipoEducacion expectedTipoEducacion = TipoEducacion.FORMAL;
        Imagen expectedImagen = Imagen.builder()
                .id(1)
                .url("certificado.jpg")
                .alt("Certificado de título")
                .build();

        // Act - Ejecutar la acción a probar
        Educacion educacion = Educacion.builder()
                .id(expectedId)
                .titulo(expectedTitulo)
                .fechaObtencion(expectedFechaObtencion)
                .descripcion(expectedDescripcion)
                .tipoEducacion(expectedTipoEducacion)
                .imagen(expectedImagen)
                .build();

        // Assert - Verificar resultados
        assertNotNull(educacion, "El objeto Educacion no debería ser nulo");
        assertEquals(expectedId, educacion.getId(), "El ID debería coincidir");
        assertEquals(expectedTitulo, educacion.getTitulo(), "El título debería coincidir");
        assertEquals(expectedFechaObtencion, educacion.getFechaObtencion(),
                "La fecha de obtención debería coincidir");
        assertEquals(expectedDescripcion, educacion.getDescripcion(),
                "La descripción debería coincidir");
        assertEquals(expectedTipoEducacion, educacion.getTipoEducacion(),
                "El tipo de educación debería coincidir");
        assertNotNull(educacion.getImagen(), "La imagen no debería ser nula");
        assertEquals(expectedImagen.getUrl(), educacion.getImagen().getUrl(),
                "La URL de la imagen debería coincidir");

        // Validar con Bean Validation
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear Educacion con constructor por defecto")
    void shouldCreateEducacionWithDefaultConstructor() {
        // Arrange & Act
        Educacion educacion = new Educacion();

        // Assert
        assertNotNull(educacion, "El objeto Educacion no debería ser nulo");
        assertNull(educacion.getId(), "El ID debería ser nulo por defecto");
        assertNull(educacion.getTitulo(), "El título debería ser nulo por defecto");
        assertNull(educacion.getFechaObtencion(), "La fecha de obtención debería ser nula por defecto");
        assertNull(educacion.getDescripcion(), "La descripción debería ser nula por defecto");
        assertNull(educacion.getTipoEducacion(), "El tipo de educación debería ser nulo por defecto");
        assertNull(educacion.getImagen(), "La imagen debería ser nula por defecto");
        assertNull(educacion.getUsuario(), "El usuario debería ser nulo por defecto");
    }

    @Test
    @DisplayName("Debería crear Educacion con constructor con todos los argumentos")
    void shouldCreateEducacionWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String titulo = "Curso de Spring Boot";
        LocalDate fechaObtencion = LocalDate.of(2021, 8, 20);
        String descripcion = "Curso avanzado de Spring Boot con enfoque en microservicios.";
        TipoEducacion tipoEducacion = TipoEducacion.INFORMAL_CURSO;
        Imagen imagen = Imagen.builder()
                .id(2)
                .url("certificado-curso.jpg")
                .alt("Certificado del curso")
                .build();
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .build();

        // Act
        Educacion educacion = new Educacion(id, titulo, fechaObtencion, descripcion,
                tipoEducacion, imagen, usuario);

        // Assert
        assertNotNull(educacion, "El objeto Educacion no debería ser nulo");
        assertEquals(id, educacion.getId(), "El ID debería coincidir");
        assertEquals(titulo, educacion.getTitulo(), "El título debería coincidir");
        assertEquals(fechaObtencion, educacion.getFechaObtencion(),
                "La fecha de obtención debería coincidir");
        assertEquals(descripcion, educacion.getDescripcion(),
                "La descripción debería coincidir");
        assertEquals(tipoEducacion, educacion.getTipoEducacion(),
                "El tipo de educación debería coincidir");
        assertNotNull(educacion.getImagen(), "La imagen no debería ser nula");
        assertNotNull(educacion.getUsuario(), "El usuario no debería ser nulo");
    }

    @Test
    @DisplayName("Debería permitir valores nulos en campos opcionales - Imagen y Usuario")
    void shouldAllowNullValuesInOptionalFields() {
        // Arrange & Act - Educacion sin imagen y sin usuario
        Educacion educacion = Educacion.builder()
                .titulo("Autodidacta en Java")
                .fechaObtencion(LocalDate.now())
                .descripcion("Aprendizaje autónomo de Java a través de documentación y práctica.")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .imagen(null)
                .usuario(null)
                .build();

        // Assert
        assertNotNull(educacion, "El objeto Educacion no debería ser nulo");
        assertNull(educacion.getImagen(), "La imagen debería ser nula");
        assertNull(educacion.getUsuario(), "El usuario debería ser nulo");

        // Validar que no hay violaciones (ya que imagen y usuario son opcionales)
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con imagen y usuario nulos");
    }

    @Test
    @DisplayName("Debería crear Educacion con todos los tipos de educación disponibles")
    void shouldCreateEducacionWithAllTiposEducacion() {
        // Arrange - Probar todos los tipos de educación
        TipoEducacion[] tipos = TipoEducacion.values();

        for (TipoEducacion tipo : tipos) {
            // Act
            Educacion educacion = Educacion.builder()
                    .titulo("Educación tipo: " + tipo)
                    .fechaObtencion(LocalDate.now())
                    .descripcion("Descripción válida para tipo: " + tipo)
                    .tipoEducacion(tipo)
                    .build();

            // Assert
            assertNotNull(educacion, "El objeto Educacion no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, educacion.getTipoEducacion(),
                    "El tipo de educación debería ser: " + tipo);

            // Validar con Bean Validation
            Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);
            assertTrue(violations.isEmpty(),
                    "No debería haber violaciones para tipo: " + tipo);
        }
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando título es nulo")
    void validation_ShouldHaveViolations_WhenTituloIsNull() {
        // Arrange - Título nulo
        Educacion educacion = Educacion.builder()
                .titulo(null)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para título nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debe haber violación específica para el campo titulo");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando título está vacío o es nulo")
    void validation_ShouldHaveViolations_WhenTituloIsNullOrEmpty(String titulo) {
        // Arrange - Título vacío
        Educacion educacion = Educacion.builder()
                .titulo(titulo)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para título vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debe haber violación específica para el campo titulo");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ab", "12", "A"})
    @DisplayName("Validación - Debe tener violaciones cuando título es muy corto (menos de 3 caracteres)")
    void validation_ShouldHaveViolations_WhenTituloIsTooShort(String titulo) {
        // Arrange - Título muy corto (menos de 3 caracteres)
        Educacion educacion = Educacion.builder()
                .titulo(titulo)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para título demasiado corto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debe haber violación específica para el campo titulo");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Un título muy largo que supera los 145 caracteres es inválido para este test porque excede el límite máximo permitido"})
    @DisplayName("Validación - Debe tener violaciones cuando título es muy largo (más de 145 caracteres)")
    void validation_ShouldHaveViolations_WhenTituloIsTooLong(String tituloLargo) {
        // Arrange - Título demasiado largo
        Educacion educacion = Educacion.builder()
                .titulo(tituloLargo)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para título demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debe haber violación específica para el campo titulo");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando fechaObtencion es nula")
    void validation_ShouldHaveViolations_WhenFechaObtencionIsNull() {
        // Arrange - Fecha de obtención nula
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(null)
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para fechaObtencion nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fechaObtencion")),
                "Debe haber violación específica para el campo fechaObtencion");
    }

    @Test
    @DisplayName("Validación - Debe aceptar fechaObtencion en el pasado")
    void validation_ShouldAcceptFechaObtencionInPast() {
        // Arrange - Fecha en el pasado
        LocalDate fechaPasada = LocalDate.of(2020, 1, 1);
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(fechaPasada)
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para fecha en el pasado");
        assertEquals(fechaPasada, educacion.getFechaObtencion(),
                "La fecha de obtención debería ser la establecida");
    }

    @Test
    @DisplayName("Validación - Debe aceptar fechaObtencion actual")
    void validation_ShouldAcceptFechaObtencionCurrentDate() {
        // Arrange - Fecha actual
        LocalDate fechaActual = LocalDate.now();
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(fechaActual)
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para fecha actual");
        assertEquals(fechaActual, educacion.getFechaObtencion(),
                "La fecha de obtención debería ser la actual");
    }

    @Test
    @DisplayName("Validación - Debe aceptar fechaObtencion en el futuro")
    void validation_ShouldAcceptFechaObtencionFuture() {
        // Arrange - Fecha en el futuro
        LocalDate fechaFutura = LocalDate.of(2026, 12, 31);
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(fechaFutura)
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para fecha futura");
        assertEquals(fechaFutura, educacion.getFechaObtencion(),
                "La fecha de obtención debería ser la establecida");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando descripción está vacía o es nula")
    void validation_ShouldHaveViolations_WhenDescripcionIsNullOrEmpty(String descripcion) {
        // Arrange - Descripción vacía
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcion)
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción vacía o nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hola", "1234", "  "})
    @DisplayName("Validación - Debe tener violaciones cuando descripción es muy corta (menos de 5 caracteres)")
    void validation_ShouldHaveViolations_WhenDescripcionIsTooShort(String descripcion) {
        // Arrange - Descripción muy corta (menos de 5 caracteres)
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcion)
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción demasiado corta");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando descripción es muy larga (más de 300 caracteres)")
    void validation_ShouldHaveViolations_WhenDescripcionIsTooLong() {
        // Arrange - Descripción demasiado larga
        String descripcionLarga = "A".repeat(301);
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionLarga)
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción demasiado larga");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando tipoEducacion es nulo")
    void validation_ShouldHaveViolations_WhenTipoEducacionIsNull() {
        // Arrange - Tipo de educación nulo
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(null)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para tipoEducacion nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoEducacion")),
                "Debe haber violación específica para el campo tipoEducacion");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Descripción válida con más de 5 caracteres",
            "Una descripción muy larga que supera los 5 caracteres pero no excede los 300 caracteres"})
    @DisplayName("Validación - Debe aceptar descripciones largas válidas")
    void validation_ShouldAcceptLongDescripcion(String descripcionLarga) {
        // Arrange - Descripción larga válida
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionLarga)
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para descripción larga válida");
        assertEquals(descripcionLarga, educacion.getDescripcion(),
                "La descripción debería mantenerse");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos Educacion con los mismos valores")
    void shouldBeEqualWhenComparingTwoEducacionesWithSameValues() {
        // Arrange
        LocalDate fecha = LocalDate.of(2020, 6, 15);

        Educacion educacion1 = Educacion.builder()
                .id(1)
                .titulo("Ingeniería en Sistemas")
                .fechaObtencion(fecha)
                .descripcion("Carrera universitaria")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        Educacion educacion2 = Educacion.builder()
                .id(1)
                .titulo("Ingeniería en Sistemas")
                .fechaObtencion(fecha)
                .descripcion("Carrera universitaria")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act & Assert
        assertEquals(educacion1, educacion2, "Los objetos Educacion deberían ser iguales");
        assertEquals(educacion1.hashCode(), educacion2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos Educacion con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoEducacionesWithDifferentValues() {
        // Arrange
        Educacion educacion1 = Educacion.builder()
                .id(1)
                .titulo("Ingeniería en Sistemas")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Carrera universitaria")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        Educacion educacion2 = Educacion.builder()
                .id(2)
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.of(2021, 8, 20))
                .descripcion("Curso avanzado")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // Act & Assert
        assertNotEquals(educacion1, educacion2, "Los objetos Educacion no deberían ser iguales");
        assertNotEquals(educacion1.hashCode(), educacion2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        Educacion educacion = Educacion.builder()
                .id(1)
                .titulo("Título Inicial")
                .fechaObtencion(LocalDate.of(2020, 1, 1))
                .descripcion("Descripción inicial")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        Imagen nuevaImagen = Imagen.builder()
                .id(3)
                .url("nuevo-certificado.jpg")
                .alt("Nuevo certificado")
                .build();

        Usuario nuevoUsuario = Usuario.builder()
                .id(2)
                .nombre("María García")
                .build();

        // Act
        educacion.setId(2);
        educacion.setTitulo("Título Actualizado");
        educacion.setFechaObtencion(LocalDate.of(2021, 6, 15));
        educacion.setDescripcion("Descripción actualizada con más de 5 caracteres");
        educacion.setTipoEducacion(TipoEducacion.INFORMAL_CURSO);
        educacion.setImagen(nuevaImagen);
        educacion.setUsuario(nuevoUsuario);

        // Assert
        assertEquals(2, educacion.getId(), "El ID debería estar actualizado");
        assertEquals("Título Actualizado", educacion.getTitulo(), "El título debería estar actualizado");
        assertEquals(LocalDate.of(2021, 6, 15), educacion.getFechaObtencion(),
                "La fecha de obtención debería estar actualizada");
        assertEquals("Descripción actualizada con más de 5 caracteres", educacion.getDescripcion(),
                "La descripción debería estar actualizada");
        assertEquals(TipoEducacion.INFORMAL_CURSO, educacion.getTipoEducacion(),
                "El tipo de educación debería estar actualizado");
        assertNotNull(educacion.getImagen(), "La imagen no debería ser nula");
        assertEquals("nuevo-certificado.jpg", educacion.getImagen().getUrl(),
                "La URL de la imagen debería estar actualizada");
        assertNotNull(educacion.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(2, educacion.getUsuario().getId(), "El ID del usuario debería estar actualizado");
    }

    @Test
    @DisplayName("Debería permitir establecer imagen a null")
    void shouldAllowSettingImagenToNull() {
        // Arrange
        Educacion educacion = Educacion.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(Imagen.builder()
                        .id(1)
                        .url("certificado.jpg")
                        .alt("Certificado")
                        .build())
                .build();

        // Act
        educacion.setImagen(null);

        // Assert
        assertNull(educacion.getImagen(), "La imagen debería ser nula");

        // Validar que no hay violaciones (imagen es opcional)
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con imagen nula");
    }

    @Test
    @DisplayName("Debería permitir establecer usuario a null")
    void shouldAllowSettingUsuarioToNull() {
        // Arrange
        Educacion educacion = Educacion.builder()
                .id(1)
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(Usuario.builder()
                        .id(1)
                        .nombre("Juan Pérez")
                        .build())
                .build();

        // Act
        educacion.setUsuario(null);

        // Assert
        assertNull(educacion.getUsuario(), "El usuario debería ser nulo");

        // Validar que no hay violaciones (usuario es opcional)
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con usuario nulo");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        Educacion educacion = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            educacion.getTitulo(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en imagen nula - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullImagen() {
        // Arrange
        Educacion educacion = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(null)
                .build();

        // Act & Assert - Test negativo
        assertThrows(NullPointerException.class, () -> {
            educacion.getImagen().getUrl(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en imagen nula");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Validación - Debe manejar títulos en el límite de longitud (3 y 145 caracteres)")
    void validation_ShouldHandleTituloAtLengthBoundaries() {
        // Arrange - Título exactamente de 3 caracteres (mínimo)
        String tituloMinimo = "ABC";
        Educacion educacionMinimo = Educacion.builder()
                .titulo(tituloMinimo)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act - Validar título mínimo
        Set<ConstraintViolation<Educacion>> violationsMinimo = validator.validate(educacionMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(),
                "No debería haber violaciones para título exactamente de 3 caracteres");
        assertEquals(3, educacionMinimo.getTitulo().length(),
                "El título debería tener exactamente 3 caracteres");

        // Arrange - Título exactamente de 145 caracteres (máximo)
        String tituloMaximo = "A".repeat(145);
        Educacion educacionMaximo = Educacion.builder()
                .titulo(tituloMaximo)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida con más de 5 caracteres")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act - Validar título máximo
        Set<ConstraintViolation<Educacion>> violationsMaximo = validator.validate(educacionMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(),
                "No debería haber violaciones para título exactamente de 145 caracteres");
        assertEquals(145, educacionMaximo.getTitulo().length(),
                "El título debería tener exactamente 145 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar descripción en límites de longitud (5 y 300 caracteres)")
    void validation_ShouldHandleDescripcionAtLengthBoundaries() {
        // Arrange - Descripción exactamente de 5 caracteres (mínimo)
        String descripcionMinimo = "12345";
        Educacion educacionMinimo = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionMinimo)
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act - Validar descripción mínima
        Set<ConstraintViolation<Educacion>> violationsMinimo = validator.validate(educacionMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(),
                "No debería haber violaciones para descripción exactamente de 5 caracteres");
        assertEquals(5, educacionMinimo.getDescripcion().length(),
                "La descripción debería tener exactamente 5 caracteres");

        // Arrange - Descripción exactamente de 300 caracteres (máximo)
        String descripcionMaximo = "A".repeat(300);
        Educacion educacionMaximo = Educacion.builder()
                .titulo("Título Válido")
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionMaximo)
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act - Validar descripción máxima
        Set<ConstraintViolation<Educacion>> violationsMaximo = validator.validate(educacionMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(),
                "No debería haber violaciones para descripción exactamente de 300 caracteres");
        assertEquals(300, educacionMaximo.getDescripcion().length(),
                "La descripción debería tener exactamente 300 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en campos de texto")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Texto con caracteres especiales y acentos
        String tituloEspecial = "Ingeniería en Sistemas - 2024";
        String descripcionEspecial = "Curso avanzado de programación con Java, Spring Boot y Angular - ¡Éxito!";

        Educacion educacion = Educacion.builder()
                .titulo(tituloEspecial)
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionEspecial)
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        Set<ConstraintViolation<Educacion>> violations = validator.validate(educacion);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(tituloEspecial, educacion.getTitulo(),
                "El título con caracteres especiales debería mantenerse");
        assertEquals(descripcionEspecial, educacion.getDescripcion(),
                "La descripción con caracteres especiales debería mantenerse");
    }

    // ==================== TESTS DE RELACIONES ====================

    @Test
    @DisplayName("Debe manejar relación con Imagen correctamente")
    void shouldHandleRelationshipWithImagenCorrectly() {
        // Arrange
        Imagen imagen = Imagen.builder()
                .id(1)
                .url("certificado.jpg")
                .alt("Certificado de educación")
                .build();

        Educacion educacion = Educacion.builder()
                .id(1)
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.now())
                .descripcion("Curso completo de Java desde cero hasta avanzado.")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertNotNull(educacion.getImagen(), "La imagen no debería ser nula");
        assertEquals("certificado.jpg", educacion.getImagen().getUrl(),
                "La URL de la imagen debería coincidir");
        assertEquals("Certificado de educación", educacion.getImagen().getAlt(),
                "El alt de la imagen debería coincidir");
    }

    @Test
    @DisplayName("Debe manejar relación con Usuario correctamente")
    void shouldHandleRelationshipWithUsuarioCorrectly() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .build();

        Educacion educacion = Educacion.builder()
                .id(1)
                .titulo("Ingeniería en Sistemas")
                .fechaObtencion(LocalDate.now())
                .descripcion("Carrera universitaria completa.")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(usuario)
                .build();

        // Act & Assert
        assertNotNull(educacion.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(1, educacion.getUsuario().getId(), "El ID del usuario debería coincidir");
        assertEquals("Juan Pérez", educacion.getUsuario().getNombre(),
                "El nombre del usuario debería coincidir");
        assertEquals("juan@email.com", educacion.getUsuario().getUsername(),
                "El username del usuario debería coincidir");
    }

    @Test
    @DisplayName("Debe manejar relación con Imagen y Usuario simultáneamente")
    void shouldHandleRelationshipWithImagenAndUsuarioSimultaneously() {
        // Arrange
        Imagen imagen = Imagen.builder()
                .id(1)
                .url("certificado.jpg")
                .alt("Certificado")
                .build();

        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .build();

        Educacion educacion = Educacion.builder()
                .id(1)
                .titulo("Curso de Spring Boot")
                .fechaObtencion(LocalDate.now())
                .descripcion("Curso avanzado de Spring Boot.")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act & Assert
        assertNotNull(educacion.getImagen(), "La imagen no debería ser nula");
        assertNotNull(educacion.getUsuario(), "El usuario no debería ser nulo");
        assertEquals("certificado.jpg", educacion.getImagen().getUrl(),
                "La URL de la imagen debería ser correcta");
        assertEquals("Juan Pérez", educacion.getUsuario().getNombre(),
                "El nombre del usuario debería ser correcto");
    }

    // ==================== TESTS DE toString ====================

    @Test
    @DisplayName("toString debería incluir información relevante de Educacion")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        Educacion educacion = Educacion.builder()
                .id(1)
                .titulo("Ingeniería en Sistemas")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Carrera universitaria")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        String toStringResult = educacion.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Ingeniería en Sistemas"),
                "toString debería contener el título");
        assertTrue(toStringResult.contains("2020-06-15"),
                "toString debería contener la fecha de obtención");
        assertTrue(toStringResult.contains("Carrera universitaria"),
                "toString debería contener la descripción");
        assertTrue(toStringResult.contains("FORMAL"),
                "toString debería contener el tipo de educación");
    }

    // ==================== TESTS DE FECHAS ====================

    @Test
    @DisplayName("Debería manejar fechas correctamente")
    void shouldHandleDatesCorrectly() {
        // Arrange
        LocalDate fechaEsperada = LocalDate.of(2023, 12, 25);
        Educacion educacion = Educacion.builder()
                .titulo("Curso de Navidad")
                .fechaObtencion(fechaEsperada)
                .descripcion("Curso especial de Navidad")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // Act & Assert
        assertEquals(fechaEsperada, educacion.getFechaObtencion(),
                "La fecha de obtención debería ser la esperada");
        assertEquals(2023, educacion.getFechaObtencion().getYear(),
                "El año debería ser 2023");
        assertEquals(12, educacion.getFechaObtencion().getMonthValue(),
                "El mes debería ser diciembre");
        assertEquals(25, educacion.getFechaObtencion().getDayOfMonth(),
                "El día debería ser 25");
    }

    @Test
    @DisplayName("Debería manejar fechas con diferentes formatos")
    void shouldHandleDatesWithDifferentFormats() {
        // Arrange
        LocalDate fecha1 = LocalDate.of(2020, 1, 1);
        LocalDate fecha2 = LocalDate.of(2021, 12, 31);

        Educacion educacion1 = Educacion.builder()
                .titulo("Educación 1")
                .fechaObtencion(fecha1)
                .descripcion("Descripción 1")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        Educacion educacion2 = Educacion.builder()
                .titulo("Educación 2")
                .fechaObtencion(fecha2)
                .descripcion("Descripción 2")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // Act & Assert
        assertEquals(fecha1, educacion1.getFechaObtencion(),
                "La primera fecha debería ser 2020-01-01");
        assertEquals(fecha2, educacion2.getFechaObtencion(),
                "La segunda fecha debería ser 2021-12-31");
        assertTrue(educacion1.getFechaObtencion().isBefore(educacion2.getFechaObtencion()),
                "La primera fecha debería ser anterior a la segunda");
    }

    // ==================== TESTS DE MÚLTIPLES INSTANCIAS ====================

    @Test
    @DisplayName("Debería crear múltiples instancias de Educacion independientes")
    void shouldCreateMultipleIndependentEducacionInstances() {
        // Arrange
        Educacion educacion1 = Educacion.builder()
                .id(1)
                .titulo("Ingeniería en Sistemas")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Carrera universitaria")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        Educacion educacion2 = Educacion.builder()
                .id(2)
                .titulo("Curso de Angular")
                .fechaObtencion(LocalDate.of(2021, 8, 20))
                .descripcion("Curso avanzado de Angular")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // Act & Assert
        assertNotSame(educacion1, educacion2, "Las instancias deberían ser diferentes");
        assertNotEquals(educacion1.getId(), educacion2.getId(), "Los IDs deberían ser diferentes");
        assertNotEquals(educacion1.getTitulo(), educacion2.getTitulo(),
                "Los títulos deberían ser diferentes");
        assertNotEquals(educacion1.getFechaObtencion(), educacion2.getFechaObtencion(),
                "Las fechas deberían ser diferentes");
        assertNotEquals(educacion1.getTipoEducacion(), educacion2.getTipoEducacion(),
                "Los tipos de educación deberían ser diferentes");
    }
}