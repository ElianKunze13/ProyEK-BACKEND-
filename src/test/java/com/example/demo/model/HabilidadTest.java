package com.example.demo.model;

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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Habilidad
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class HabilidadTest {

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
    @DisplayName("Debería crear Habilidad con valores válidos - Caso feliz")
    void shouldCreateHabilidadWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedNombre = "Comunicación Efectiva";
        Usuario expectedUsuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("password123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        // Act - Ejecutar la acción a probar
        Habilidad habilidad = Habilidad.builder()
                .id(expectedId)
                .nombre(expectedNombre)
                .usuario(expectedUsuario)
                .build();

        // Assert - Verificar resultados
        assertNotNull(habilidad, "El objeto Habilidad no debería ser nulo");
        assertEquals(expectedId, habilidad.getId(), "El ID debería coincidir");
        assertEquals(expectedNombre, habilidad.getNombre(), "El nombre debería coincidir");
        assertEquals(expectedUsuario, habilidad.getUsuario(), "El usuario debería coincidir");
        assertNotNull(habilidad.getUsuario(), "El usuario no debería ser nulo");
        assertEquals("Juan Pérez", habilidad.getUsuario().getNombre(), "El nombre del usuario debería coincidir");

        // Validar con Bean Validation
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear Habilidad con constructor por defecto")
    void shouldCreateHabilidadWithDefaultConstructor() {
        // Arrange & Act
        Habilidad habilidad = new Habilidad();

        // Assert
        assertNotNull(habilidad, "El objeto Habilidad no debería ser nulo");
        assertNull(habilidad.getId(), "El ID debería ser nulo por defecto");
        assertNull(habilidad.getNombre(), "El nombre debería ser nulo por defecto");
        assertNull(habilidad.getUsuario(), "El usuario debería ser nulo por defecto");
    }

    @Test
    @DisplayName("Debería crear Habilidad con constructor con todos los argumentos")
    void shouldCreateHabilidadWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String nombre = "Trabajo en Equipo";
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("María García")
                .username("maria@email.com")
                .password("password456")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.ADMIN)
                .build();

        // Act
        Habilidad habilidad = new Habilidad(id, nombre, usuario);

        // Assert
        assertNotNull(habilidad, "El objeto Habilidad no debería ser nulo");
        assertEquals(id, habilidad.getId(), "El ID debería coincidir");
        assertEquals(nombre, habilidad.getNombre(), "El nombre debería coincidir");
        assertEquals(usuario, habilidad.getUsuario(), "El usuario debería coincidir");
    }

    @Test
    @DisplayName("Debería permitir usuario nulo (relación opcional)")
    void shouldAllowNullUsuario() {
        // Arrange & Act - Habilidad sin usuario
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre("Adaptabilidad")
                .usuario(null)
                .build();

        // Assert
        assertNotNull(habilidad, "El objeto Habilidad no debería ser nulo");
        assertNull(habilidad.getUsuario(), "El usuario debería ser nulo");
        assertEquals("Adaptabilidad", habilidad.getNombre(), "El nombre debería ser correcto");

        // Validar que no hay violaciones (usuario es opcional)
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con usuario nulo");
    }

    @Test
    @DisplayName("Debería crear Habilidad solo con nombre obligatorio")
    void shouldCreateHabilidadWithOnlyRequiredFields() {
        // Arrange & Act
        String nombre = "Rapidez de Aprendizaje";
        Habilidad habilidad = Habilidad.builder()
                .nombre(nombre)
                .build();

        // Assert
        assertNotNull(habilidad, "El objeto Habilidad no debería ser nulo");
        assertNull(habilidad.getId(), "El ID debería ser nulo");
        assertEquals(nombre, habilidad.getNombre(), "El nombre debería coincidir");
        assertNull(habilidad.getUsuario(), "El usuario debería ser nulo");

        // Validar
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);
        assertTrue(violations.isEmpty(), "No debería haber violaciones");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando nombre es nulo")
    void validation_ShouldHaveViolations_WhenNombreIsNull() {
        // Arrange - Nombre nulo
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(null)
                .usuario(new Usuario())
                .build();

        // Act
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando nombre está vacío o es nulo")
    void validation_ShouldHaveViolations_WhenNombreIsNullOrEmpty(String nombre) {
        // Arrange - Nombre vacío
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(nombre)
                .usuario(new Usuario())
                .build();

        // Act
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ab", "12", "A"})
    @DisplayName("Validación - Debe tener violaciones cuando nombre es muy corto (menos de 3 caracteres)")
    void validation_ShouldHaveViolations_WhenNombreIsTooShort(String nombreCorto) {
        // Arrange - Nombre muy corto (menos de 3 caracteres)
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(nombreCorto)
                .usuario(new Usuario())
                .build();

        // Act
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre demasiado corto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando nombre es muy largo (más de 145 caracteres)")
    void validation_ShouldHaveViolations_WhenNombreIsTooLong() {
        // Arrange - Nombre muy largo (más de 145 caracteres)
        String nombreLargo = "A".repeat(146); // 146 caracteres (excede el máximo de 145)
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(nombreLargo)
                .usuario(new Usuario())
                .build();

        // Act
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @Test
    @DisplayName("Validación - Debe aceptar nombre exactamente de 3 caracteres (límite mínimo)")
    void validation_ShouldAcceptNombreExactlyMinLength() {
        // Arrange - Nombre exactamente de 3 caracteres (mínimo)
        String nombreMinimo = "ABC";
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(nombreMinimo)
                .usuario(new Usuario())
                .build();

        // Act
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre exactamente de 3 caracteres");
        assertEquals(3, habilidad.getNombre().length(), "El nombre debería tener exactamente 3 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe aceptar nombre exactamente de 145 caracteres (límite máximo)")
    void validation_ShouldAcceptNombreExactlyMaxLength() {
        // Arrange - Nombre exactamente de 145 caracteres (máximo)
        String nombreMaximo = "A".repeat(145);
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(nombreMaximo)
                .usuario(new Usuario())
                .build();

        // Act
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre exactamente de 145 caracteres");
        assertEquals(145, habilidad.getNombre().length(), "El nombre debería tener exactamente 145 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en el nombre")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Nombre con caracteres especiales y acentos
        String nombreEspecial = "José María Comunicación y Ñáéíóú";
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(nombreEspecial)
                .usuario(new Usuario())
                .build();

        // Act
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(nombreEspecial, habilidad.getNombre(), "El nombre con caracteres especiales debería mantenerse");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos Habilidad con los mismos valores")
    void shouldBeEqualWhenComparingTwoHabilidadWithSameValues() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        Habilidad habilidad1 = Habilidad.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .usuario(usuario)
                .build();

        Habilidad habilidad2 = Habilidad.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .usuario(usuario)
                .build();

        // Act & Assert
        assertEquals(habilidad1, habilidad2, "Los objetos Habilidad deberían ser iguales");
        assertEquals(habilidad1.hashCode(), habilidad2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos Habilidad con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoHabilidadWithDifferentValues() {
        // Arrange
        Usuario usuario1 = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        Usuario usuario2 = Usuario.builder()
                .id(2)
                .nombre("María García")
                .username("maria@email.com")
                .password("pass456")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.ADMIN)
                .build();

        Habilidad habilidad1 = Habilidad.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .usuario(usuario1)
                .build();

        Habilidad habilidad2 = Habilidad.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .usuario(usuario2)
                .build();

        // Act & Assert
        assertNotEquals(habilidad1, habilidad2, "Los objetos Habilidad no deberían ser iguales");
        assertNotEquals(habilidad1.hashCode(), habilidad2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan con objeto de diferente clase")
    void shouldNotBeEqualWhenComparingWithDifferentClass() {
        // Arrange
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        String differentObject = "String de prueba";

        // Act & Assert
        assertNotEquals(habilidad, differentObject, "No debería ser igual a objeto de diferente clase");
        assertNotEquals(habilidad, null, "No debería ser igual a null");
    }

    // ==================== TESTS DE SETTERS ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        Habilidad habilidad = new Habilidad();
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Usuario Test")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        // Act
        habilidad.setId(10);
        habilidad.setNombre("Nuevo Nombre de Habilidad");
        habilidad.setUsuario(usuario);

        // Assert
        assertEquals(10, habilidad.getId(), "El ID debería estar actualizado");
        assertEquals("Nuevo Nombre de Habilidad", habilidad.getNombre(), "El nombre debería estar actualizado");
        assertEquals(usuario, habilidad.getUsuario(), "El usuario debería estar actualizado");
        assertEquals("Usuario Test", habilidad.getUsuario().getNombre(), "El nombre del usuario debería coincidir");
    }

    @Test
    @DisplayName("Debería permitir establecer usuario nulo mediante setter")
    void shouldAllowSettingNullUsuario() {
        // Arrange
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre("Comunicación")
                .usuario(new Usuario())
                .build();

        // Act
        habilidad.setUsuario(null);

        // Assert
        assertNull(habilidad.getUsuario(), "El usuario debería ser nulo después de settear null");
    }

    // ==================== TESTS DE TO_STRING ====================

    @Test
    @DisplayName("toString debería incluir información relevante")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .usuario(usuario)
                .build();

        // Act
        String toStringResult = habilidad.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Comunicación Efectiva"), "toString debería contener el nombre");
        assertTrue(toStringResult.contains("usuario"), "toString debería contener el campo usuario");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debería manejar nombres con espacios al inicio y al final")
    void shouldHandleNombreWithLeadingAndTrailingSpaces() {
        // Arrange
        String nombreConEspacios = "  Comunicación Efectiva  ";
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(nombreConEspacios)
                .usuario(new Usuario())
                .build();

        // Act
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre con espacios");
        assertEquals(nombreConEspacios, habilidad.getNombre(), "El nombre con espacios debería mantenerse");
    }

    @Test
    @DisplayName("Debería manejar nombres con caracteres numéricos")
    void shouldHandleNombreWithNumericCharacters() {
        // Arrange
        String nombreConNumeros = "Habilidad 2024";
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(nombreConNumeros)
                .usuario(new Usuario())
                .build();

        // Act
        Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre con números");
        assertEquals(nombreConNumeros, habilidad.getNombre(), "El nombre con números debería mantenerse");
    }

    @Test
    @DisplayName("Debería manejar todos los tipos de nombres de habilidades blandas")
    void shouldHandleAllTypesOfSoftSkills() {
        // Arrange - Lista de habilidades blandas comunes
        String[] softSkills = {
                "Comunicación",
                "Trabajo en Equipo",
                "Adaptabilidad",
                "Rapidez de Aprendizaje",
                "Trabajo Bajo Presión",
                "Liderazgo",
                "Resolución de Problemas",
                "Pensamiento Crítico",
                "Creatividad",
                "Gestión del Tiempo",
                "Empatía",
                "Inteligencia Emocional",
                "Negociación",
                "Toma de Decisiones",
                "Flexibilidad"
        };

        // Act & Assert
        for (String skill : softSkills) {
            Habilidad habilidad = Habilidad.builder()
                    .id(1)
                    .nombre(skill)
                    .usuario(new Usuario())
                    .build();

            Set<ConstraintViolation<Habilidad>> violations = validator.validate(habilidad);
            assertTrue(violations.isEmpty(), "No debería haber violaciones para la habilidad: " + skill);
            assertEquals(skill, habilidad.getNombre(), "El nombre debería coincidir para: " + skill);
        }
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        Habilidad habilidad = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            habilidad.getNombre(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    @Test
    @DisplayName("Debería lanzar NullPointerException al construir con nombre nulo en builder")
    void shouldThrowExceptionWhenBuildingWithNullNombre() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            Habilidad.builder()
                    .id(1)
                    .nombre(null) // Esto lanzará NPE por @NonNull
                    .build();
        }, "Debería lanzar NullPointerException al construir con nombre nulo");
    }

    // ==================== TESTS DE ENCADENAMIENTO DE MÉTODOS ====================

    @Test
    @DisplayName("Debería permitir encadenamiento de métodos (builder fluent API)")
    void shouldAllowMethodChaining() {
        // Arrange & Act
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Usuario Test")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        Habilidad habilidad = Habilidad.builder()
                .id(5)
                .nombre("Pensamiento Crítico")
                .usuario(usuario)
                .build();

        // Assert
        assertNotNull(habilidad);
        assertEquals(5, habilidad.getId());
        assertEquals("Pensamiento Crítico", habilidad.getNombre());
        assertNotNull(habilidad.getUsuario());
    }

    @Test
    @DisplayName("Debería ser posible crear múltiples instancias de Habilidad para el mismo usuario")
    void shouldCreateMultipleHabilidadInstancesForSameUsuario() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        // Act
        Habilidad habilidad1 = Habilidad.builder()
                .id(1)
                .nombre("Comunicación")
                .usuario(usuario)
                .build();

        Habilidad habilidad2 = Habilidad.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .usuario(usuario)
                .build();

        Habilidad habilidad3 = Habilidad.builder()
                .id(3)
                .nombre("Adaptabilidad")
                .usuario(usuario)
                .build();

        // Assert
        assertNotNull(habilidad1);
        assertNotNull(habilidad2);
        assertNotNull(habilidad3);
        assertEquals(usuario, habilidad1.getUsuario());
        assertEquals(usuario, habilidad2.getUsuario());
        assertEquals(usuario, habilidad3.getUsuario());
        assertNotEquals(habilidad1, habilidad2, "Deberían ser diferentes instancias");
        assertNotEquals(habilidad1.getId(), habilidad2.getId(), "Los IDs deberían ser diferentes");
    }
}