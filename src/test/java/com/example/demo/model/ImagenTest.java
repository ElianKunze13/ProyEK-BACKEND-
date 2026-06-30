package com.example.demo.model;

import com.example.demo.enums.Role;
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
 * Pruebas unitarias para la clase Imagen
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class ImagenTest {

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
    @DisplayName("Debería crear Imagen con valores válidos - Caso feliz")
    void shouldCreateImagenWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedUrl = "https://ejemplo.com/imagen.jpg";
        String expectedAlt = "Descripción de la imagen";

        // Act - Ejecutar la acción a probar
        Imagen imagen = Imagen.builder()
                .id(expectedId)
                .url(expectedUrl)
                .alt(expectedAlt)
                .build();

        // Assert - Verificar resultados
        assertNotNull(imagen, "El objeto Imagen no debería ser nulo");
        assertEquals(expectedId, imagen.getId(), "El ID debería coincidir");
        assertEquals(expectedUrl, imagen.getUrl(), "La URL debería coincidir");
        assertEquals(expectedAlt, imagen.getAlt(), "El texto alternativo debería coincidir");

        // Validar con Bean Validation
        Set<ConstraintViolation<Imagen>> violations = validator.validate(imagen);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear Imagen con constructor por defecto")
    void shouldCreateImagenWithDefaultConstructor() {
        // Arrange & Act
        Imagen imagen = new Imagen();

        // Assert
        assertNotNull(imagen, "El objeto Imagen no debería ser nulo");
        assertNull(imagen.getId(), "El ID debería ser nulo por defecto");
        assertNull(imagen.getUrl(), "La URL debería ser nula por defecto");
        assertNull(imagen.getAlt(), "El texto alternativo debería ser nulo por defecto");
        assertNull(imagen.getConocimiento(), "El conocimiento debería ser nulo por defecto");
        assertNull(imagen.getEducacion(), "La educación debería ser nula por defecto");
        assertNull(imagen.getExperiencia(), "La experiencia debería ser nula por defecto");
        assertNull(imagen.getUsuario(), "El usuario debería ser nulo por defecto");
    }

    @Test
    @DisplayName("Debería crear Imagen con constructor con todos los argumentos")
    void shouldCreateImagenWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String url = "https://ejemplo.com/foto.jpg";
        String alt = "Foto de perfil";

        // Crear objetos relacionados para la prueba
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Java")
                .build();

        Educacion educacion = Educacion.builder()
                .titulo("Ingeniería")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .build();

        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto")
                .fechaInicioProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/proyecto")
                .build();

        Usuario usuario = Usuario.builder()
                .nombre("Usuario Test")
                .username("test@email.com")
                .password("password123")
                .introduccion("Introducción válida para prueba")
                .descripcion("Descripción válida para prueba")
                .rol(Role.USER)
                .build();

        // Act
        Imagen imagen = new Imagen(id, url, alt, conocimiento, educacion, experiencia, usuario);

        // Assert
        assertNotNull(imagen, "El objeto Imagen no debería ser nulo");
        assertEquals(id, imagen.getId(), "El ID debería coincidir");
        assertEquals(url, imagen.getUrl(), "La URL debería coincidir");
        assertEquals(alt, imagen.getAlt(), "El texto alternativo debería coincidir");
        assertNotNull(imagen.getConocimiento(), "El conocimiento no debería ser nulo");
        assertNotNull(imagen.getEducacion(), "La educación no debería ser nula");
        assertNotNull(imagen.getExperiencia(), "La experiencia no debería ser nula");
        assertNotNull(imagen.getUsuario(), "El usuario no debería ser nulo");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando url es nula")
    void validation_ShouldHaveViolations_WhenUrlIsNull() {
        // Arrange - URL nula
        Imagen imagen = Imagen.builder()
                .url(null)
                .alt("Texto alternativo válido")
                .build();

        // Act
        Set<ConstraintViolation<Imagen>> violations = validator.validate(imagen);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para url nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("url")),
                "Debe haber violación específica para el campo url");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando url está vacía o es nula")
    void validation_ShouldHaveViolations_WhenUrlIsNullOrEmpty(String url) {
        // Arrange - URL vacía
        Imagen imagen = Imagen.builder()
                .url(url)
                .alt("Texto alternativo válido")
                .build();

        // Act
        Set<ConstraintViolation<Imagen>> violations = validator.validate(imagen);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para url vacía o nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("url")),
                "Debe haber violación específica para el campo url");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando alt es nulo")
    void validation_ShouldHaveViolations_WhenAltIsNull() {
        // Arrange - Alt nulo
        Imagen imagen = Imagen.builder()
                .url("https://ejemplo.com/imagen.jpg")
                .alt(null)
                .build();

        // Act
        Set<ConstraintViolation<Imagen>> violations = validator.validate(imagen);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para alt nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("alt")),
                "Debe haber violación específica para el campo alt");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando alt está vacío o es nulo")
    void validation_ShouldHaveViolations_WhenAltIsNullOrEmpty(String alt) {
        // Arrange - Alt vacío
        Imagen imagen = Imagen.builder()
                .url("https://ejemplo.com/imagen.jpg")
                .alt(alt)
                .build();

        // Act
        Set<ConstraintViolation<Imagen>> violations = validator.validate(imagen);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para alt vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("alt")),
                "Debe haber violación específica para el campo alt");
    }

    @Test
    @DisplayName("Validación - Debe aceptar URLs con diferentes formatos válidos")
    void validation_ShouldAcceptDifferentUrlFormats() {
        // Arrange - URLs con diferentes formatos
        String[] urlsValidas = {
                "https://ejemplo.com/imagen.jpg",
                "http://ejemplo.com/imagen.png",
                "https://subdominio.ejemplo.com/imagen.gif",
                "https://ejemplo.com/ruta/con/espacios%20codificados.jpg",
                "https://ejemplo.com/imagen-con-guiones.jpg",
                "https://ejemplo.com/imagen_con_guiones_bajos.jpg"
        };

        for (String url : urlsValidas) {
            // Act
            Imagen imagen = Imagen.builder()
                    .url(url)
                    .alt("Texto alternativo válido")
                    .build();

            // Assert
            Set<ConstraintViolation<Imagen>> violations = validator.validate(imagen);
            assertTrue(violations.isEmpty(), "No debería haber violaciones para URL válida: " + url);
            assertEquals(url, imagen.getUrl(), "La URL debería mantenerse igual");
        }
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos Imagen con los mismos valores")
    void shouldBeEqualWhenComparingTwoImagenesWithSameValues() {
        // Arrange
        Imagen imagen1 = Imagen.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Texto alternativo")
                .build();

        Imagen imagen2 = Imagen.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Texto alternativo")
                .build();

        // Act & Assert
        assertEquals(imagen1, imagen2, "Los objetos Imagen deberían ser iguales");
        assertEquals(imagen1.hashCode(), imagen2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos Imagen con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoImagenesWithDifferentValues() {
        // Arrange
        Imagen imagen1 = Imagen.builder()
                .id(1)
                .url("https://ejemplo.com/imagen1.jpg")
                .alt("Texto alternativo 1")
                .build();

        Imagen imagen2 = Imagen.builder()
                .id(2)
                .url("https://ejemplo.com/imagen2.jpg")
                .alt("Texto alternativo 2")
                .build();

        // Act & Assert
        assertNotEquals(imagen1, imagen2, "Los objetos Imagen no deberían ser iguales");
        assertNotEquals(imagen1.hashCode(), imagen2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        Imagen imagen = Imagen.builder()
                .id(1)
                .url("https://ejemplo.com/inicial.jpg")
                .alt("Texto inicial")
                .build();

        // Act
        imagen.setId(2);
        imagen.setUrl("https://ejemplo.com/actualizada.jpg");
        imagen.setAlt("Texto actualizado");

        // Crear objetos relacionados para pruebas de setters
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Spring Boot")
                .build();
        Educacion educacion = Educacion.builder()
                .titulo("Curso Avanzado")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .build();
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Actualizado")
                .fechaInicioProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/proyecto-actualizado")
                .build();
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Actualizado")
                .username("actualizado@email.com")
                .password("password123")
                .introduccion("Introducción actualizada")
                .descripcion("Descripción actualizada")
                .rol(Role.ADMIN)
                .build();

        imagen.setConocimiento(conocimiento);
        imagen.setEducacion(educacion);
        imagen.setExperiencia(experiencia);
        imagen.setUsuario(usuario);

        // Assert
        assertEquals(2, imagen.getId(), "El ID debería estar actualizado");
        assertEquals("https://ejemplo.com/actualizada.jpg", imagen.getUrl(), "La URL debería estar actualizada");
        assertEquals("Texto actualizado", imagen.getAlt(), "El texto alternativo debería estar actualizado");
        assertNotNull(imagen.getConocimiento(), "El conocimiento no debería ser nulo");
        assertNotNull(imagen.getEducacion(), "La educación no debería ser nula");
        assertNotNull(imagen.getExperiencia(), "La experiencia no debería ser nula");
        assertNotNull(imagen.getUsuario(), "El usuario no debería ser nulo");
    }

    // ==================== TESTS DE RELACIONES ====================

    @Test
    @DisplayName("Debería permitir establecer relaciones con otras entidades")
    void shouldAllowSettingRelationshipsWithOtherEntities() {
        // Arrange - Crear imagen
        Imagen imagen = Imagen.builder()
                .url("https://ejemplo.com/imagen-relacionada.jpg")
                .alt("Imagen relacionada")
                .build();

        // Crear entidades relacionadas
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Java")
                .build();

        Educacion educacion = Educacion.builder()
                .titulo("Máster en Informática")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .build();

        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Relacionado")
                .fechaInicioProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/proyecto-relacionado")
                .build();

        Usuario usuario = Usuario.builder()
                .nombre("Usuario Relacionado")
                .username("relacionado@email.com")
                .password("password123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .build();

        // Act - Establecer relaciones
        imagen.setConocimiento(conocimiento);
        imagen.setEducacion(educacion);
        imagen.setExperiencia(experiencia);
        imagen.setUsuario(usuario);

        // Assert
        assertNotNull(imagen.getConocimiento(), "La relación con conocimiento debería establecerse");
        assertEquals("Java", imagen.getConocimiento().getNombre(), "El nombre del conocimiento debería coincidir");
        assertNotNull(imagen.getEducacion(), "La relación con educación debería establecerse");
        assertEquals("Máster en Informática", imagen.getEducacion().getTitulo(), "El título de educación debería coincidir");
        assertNotNull(imagen.getExperiencia(), "La relación con experiencia debería establecerse");
        assertEquals("Proyecto Relacionado", imagen.getExperiencia().getTitulo(), "El título de experiencia debería coincidir");
        assertNotNull(imagen.getUsuario(), "La relación con usuario debería establecerse");
        assertEquals("Usuario Relacionado", imagen.getUsuario().getNombre(), "El nombre del usuario debería coincidir");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        Imagen imagen = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            imagen.getUrl(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debería manejar URLs y textos alternativos con caracteres especiales")
    void shouldHandleSpecialCharactersInUrlAndAlt() {
        // Arrange - URL y alt con caracteres especiales
        String urlEspecial = "https://ejemplo.com/imagen-con-espacios%20y-simbolos!@#.jpg";
        String altEspecial = "Texto alternativo con acentos: áéíóú ñü y caracteres especiales !@#$%^&*()";

        Imagen imagen = Imagen.builder()
                .url(urlEspecial)
                .alt(altEspecial)
                .build();

        // Act
        Set<ConstraintViolation<Imagen>> violations = validator.validate(imagen);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(urlEspecial, imagen.getUrl(), "La URL con caracteres especiales debería mantenerse");
        assertEquals(altEspecial, imagen.getAlt(), "El alt con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Debería manejar URLs muy largas y textos alternativos largos")
    void shouldHandleVeryLongUrlsAndAlt() {
        // Arrange - URL larga (más de 200 caracteres)
        String urlLarga = "https://ejemplo.com/" + "a".repeat(200) + ".jpg";
        String altLargo = "Texto alternativo muy largo " + "b".repeat(300);

        Imagen imagen = Imagen.builder()
                .url(urlLarga)
                .alt(altLargo)
                .build();

        // Act
        Set<ConstraintViolation<Imagen>> violations = validator.validate(imagen);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para URLs y textos largos");
        assertEquals(urlLarga, imagen.getUrl(), "La URL larga debería mantenerse");
        assertEquals(altLargo, imagen.getAlt(), "El texto alt largo debería mantenerse");
    }

    @Test
    @DisplayName("Debería permitir múltiples relaciones con la misma imagen")
    void shouldAllowMultipleRelationshipsWithSameImage() {
        // Arrange - Crear una imagen
        Imagen imagen = Imagen.builder()
                .url("https://ejemplo.com/imagen-compartida.jpg")
                .alt("Imagen compartida entre relaciones")
                .build();

        // Act - Establecer múltiples relaciones (en realidad, en la base de datos se maneja con diferentes tablas)
        // En el modelo actual, una imagen puede tener relaciones con diferentes entidades
        // aunque en la práctica, una imagen solo debería pertenecer a una entidad a la vez

        // Crear y establecer diferentes relaciones
        Conocimiento conocimiento = new Conocimiento();
        conocimiento.setNombre("JavaScript");
        imagen.setConocimiento(conocimiento);

        Educacion educacion = new Educacion();
        educacion.setTitulo("Bootcamp JavaScript");
        educacion.setFechaObtencion(LocalDate.now());
        educacion.setDescripcion("Descripción válida");
        imagen.setEducacion(educacion);

        Experiencia experiencia = new Experiencia();
        experiencia.setTitulo("Proyecto JavaScript");
        experiencia.setFechaInicioProyecto(LocalDate.now());
        experiencia.setDescripcion("Descripción válida");
        experiencia.setLink("https://github.com/proyecto-js");
        imagen.setExperiencia(experiencia);

        // Assert - Verificar que se pueden establecer múltiples relaciones
        assertNotNull(imagen.getConocimiento(), "Debería poder establecer relación con conocimiento");
        assertNotNull(imagen.getEducacion(), "Debería poder establecer relación con educación");
        assertNotNull(imagen.getExperiencia(), "Debería poder establecer relación con experiencia");

        // Verificar que los valores se mantienen
        assertEquals("JavaScript", imagen.getConocimiento().getNombre(), "El nombre del conocimiento debería coincidir");
        assertEquals("Bootcamp JavaScript", imagen.getEducacion().getTitulo(), "El título de educación debería coincidir");
        assertEquals("Proyecto JavaScript", imagen.getExperiencia().getTitulo(), "El título de experiencia debería coincidir");
    }

    @Test
    @DisplayName("Debería permitir null en relaciones opcionales")
    void shouldAllowNullInOptionalRelationships() {
        // Arrange - Crear imagen sin relaciones
        Imagen imagen = Imagen.builder()
                .url("https://ejemplo.com/imagen-sin-relaciones.jpg")
                .alt("Imagen sin relaciones")
                .conocimiento(null)
                .educacion(null)
                .experiencia(null)
                .usuario(null)
                .build();

        // Act & Assert
        assertNotNull(imagen, "El objeto Imagen no debería ser nulo");
        assertNull(imagen.getConocimiento(), "El conocimiento debería ser nulo");
        assertNull(imagen.getEducacion(), "La educación debería ser nula");
        assertNull(imagen.getExperiencia(), "La experiencia debería ser nula");
        assertNull(imagen.getUsuario(), "El usuario debería ser nulo");

        // Validar que no hay violaciones (las relaciones son opcionales)
        Set<ConstraintViolation<Imagen>> violations = validator.validate(imagen);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con relaciones nulas");
    }

    @Test
    @DisplayName("toString - Debería incluir campos principales")
    void toString_ShouldIncludeMainFields() {
        // Arrange
        Imagen imagen = Imagen.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Texto alternativo")
                .build();

        // Act
        String toStringResult = imagen.toString();

        // Assert
        assertNotNull(toStringResult, "El resultado de toString no debería ser nulo");
        assertTrue(toStringResult.contains("id=1"), "toString debería incluir el ID");
        assertTrue(toStringResult.contains("url=https://ejemplo.com/imagen.jpg"), "toString debería incluir la URL");
        assertTrue(toStringResult.contains("alt=Texto alternativo"), "toString debería incluir el texto alternativo");
    }
}