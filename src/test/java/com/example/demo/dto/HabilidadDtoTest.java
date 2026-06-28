package com.example.demo.dto;

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
 * Pruebas unitarias para la clase HabilidadDto
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Nota: HabilidadDto actualmente no tiene anotaciones de validación (@NotNull, @Size, etc.)
 * por lo que las validaciones siempre pasarán. Las pruebas de validación están incluidas
 * para cuando se agreguen anotaciones en el futuro.
 */
class HabilidadDtoTest {

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
    @DisplayName("Debería crear HabilidadDto con valores válidos - Caso feliz")
    void shouldCreateHabilidadDtoWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedNombre = "Comunicación Efectiva";

        // Act - Ejecutar la acción a probar
        HabilidadDto habilidadDto = HabilidadDto.builder()
                .id(expectedId)
                .nombre(expectedNombre)
                .build();

        // Assert - Verificar resultados
        assertNotNull(habilidadDto, "El objeto HabilidadDto no debería ser nulo");
        assertEquals(expectedId, habilidadDto.getId(), "El ID debería coincidir");
        assertEquals(expectedNombre, habilidadDto.getNombre(), "El nombre debería coincidir");

        // Validar con Bean Validation
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(habilidadDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear HabilidadDto con constructor por defecto")
    void shouldCreateHabilidadDtoWithDefaultConstructor() {
        // Arrange & Act
        HabilidadDto habilidadDto = new HabilidadDto();

        // Assert
        assertNotNull(habilidadDto, "El objeto HabilidadDto no debería ser nulo");
        assertNull(habilidadDto.getId(), "El ID debería ser nulo por defecto");
        assertNull(habilidadDto.getNombre(), "El nombre debería ser nulo por defecto");
    }

    @Test
    @DisplayName("Debería crear HabilidadDto con constructor con todos los argumentos")
    void shouldCreateHabilidadDtoWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String nombre = "Trabajo en Equipo";

        // Act
        HabilidadDto habilidadDto = new HabilidadDto(id, nombre);

        // Assert
        assertNotNull(habilidadDto, "El objeto HabilidadDto no debería ser nulo");
        assertEquals(id, habilidadDto.getId(), "El ID debería coincidir");
        assertEquals(nombre, habilidadDto.getNombre(), "El nombre debería coincidir");
    }

    @Test
    @DisplayName("Debería crear HabilidadDto con ID nulo")
    void shouldCreateHabilidadDtoWithNullId() {
        // Arrange & Act
        HabilidadDto habilidadDto = HabilidadDto.builder()
                .id(null)
                .nombre("Adaptabilidad")
                .build();

        // Assert
        assertNotNull(habilidadDto, "El objeto HabilidadDto no debería ser nulo");
        assertNull(habilidadDto.getId(), "El ID debería ser nulo");
        assertEquals("Adaptabilidad", habilidadDto.getNombre(), "El nombre debería coincidir");

        // Validar
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(habilidadDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear HabilidadDto con nombre nulo")
    void shouldCreateHabilidadDtoWithNullNombre() {
        // Arrange & Act
        HabilidadDto habilidadDto = HabilidadDto.builder()
                .id(1)
                .nombre(null)
                .build();

        // Assert
        assertNotNull(habilidadDto, "El objeto HabilidadDto no debería ser nulo");
        assertEquals(1, habilidadDto.getId(), "El ID debería ser 1");
        assertNull(habilidadDto.getNombre(), "El nombre debería ser nulo");

        // Validar
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(habilidadDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear HabilidadDto con nombre vacío")
    void shouldCreateHabilidadDtoWithEmptyNombre() {
        // Arrange & Act
        HabilidadDto habilidadDto = HabilidadDto.builder()
                .id(1)
                .nombre("")
                .build();

        // Assert
        assertNotNull(habilidadDto, "El objeto HabilidadDto no debería ser nulo");
        assertEquals(1, habilidadDto.getId(), "El ID debería ser 1");
        assertEquals("", habilidadDto.getNombre(), "El nombre vacío debería mantenerse");

        // Validar
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(habilidadDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear HabilidadDto sin ID")
    void shouldCreateHabilidadDtoWithoutId() {
        // Arrange & Act
        HabilidadDto habilidadDto = HabilidadDto.builder()
                .nombre("Liderazgo")
                .build();

        // Assert
        assertNotNull(habilidadDto, "El objeto HabilidadDto no debería ser nulo");
        assertNull(habilidadDto.getId(), "El ID debería ser nulo");
        assertEquals("Liderazgo", habilidadDto.getNombre(), "El nombre debería coincidir");
    }

    @Test
    @DisplayName("Debería crear HabilidadDto sin nombre")
    void shouldCreateHabilidadDtoWithoutNombre() {
        // Arrange & Act
        HabilidadDto habilidadDto = HabilidadDto.builder()
                .id(1)
                .build();

        // Assert
        assertNotNull(habilidadDto, "El objeto HabilidadDto no debería ser nulo");
        assertEquals(1, habilidadDto.getId(), "El ID debería ser 1");
        assertNull(habilidadDto.getNombre(), "El nombre debería ser nulo");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - HabilidadDto con todos los campos válidos debería pasar la validación")
    void validation_ShouldPass_WhenAllFieldsAreValid() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert - El DTO no tiene anotaciones de validación, siempre pasa
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Validación - HabilidadDto con ID nulo debería pasar la validación")
    void validation_ShouldPass_WithNullId() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(null)
                .nombre("Trabajo en Equipo")
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para ID nulo");
    }

    @Test
    @DisplayName("Validación - HabilidadDto con nombre nulo debería pasar la validación")
    void validation_ShouldPass_WithNullNombre() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(null)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre nulo");
    }

    @Test
    @DisplayName("Validación - HabilidadDto con nombre vacío debería pasar la validación")
    void validation_ShouldPass_WithEmptyNombre() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre("")
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre vacío");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ab", "A", "12"})
    @DisplayName("Validación - HabilidadDto con nombre muy corto debería pasar la validación (sin validaciones en DTO)")
    void validation_ShouldPass_WithNombreTooShort(String nombreCorto) {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(nombreCorto)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert - El DTO no tiene validación de tamaño, por lo que siempre pasa
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre corto");
    }

    @Test
    @DisplayName("Validación - HabilidadDto con nombre muy largo (más de 145 caracteres) debería pasar la validación")
    void validation_ShouldPass_WithNombreTooLong() {
        // Arrange
        String nombreLargo = "A".repeat(200);
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(nombreLargo)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert - El DTO no tiene validación de tamaño, por lo que siempre pasa
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre largo");
    }

    @Test
    @DisplayName("Validación - HabilidadDto con nombre de 145 caracteres exactamente debería pasar")
    void validation_ShouldPass_WithNombreExactly145Characters() {
        // Arrange
        String nombreExacto = "A".repeat(145);
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(nombreExacto)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones");
        assertEquals(145, dto.getNombre().length(), "El nombre debería tener exactamente 145 caracteres");
    }

    @Test
    @DisplayName("Validación - HabilidadDto con nombre de 3 caracteres exactamente debería pasar")
    void validation_ShouldPass_WithNombreExactly3Characters() {
        // Arrange
        String nombreExacto = "ABC";
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(nombreExacto)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones");
        assertEquals(3, dto.getNombre().length(), "El nombre debería tener exactamente 3 caracteres");
    }

    // ==================== TESTS DE CARACTERES ESPECIALES ====================

    @Test
    @DisplayName("Debería manejar nombre con caracteres especiales")
    void shouldHandleNombreWithSpecialCharacters() {
        // Arrange
        String nombreEspecial = "Comunicación & Trabajo en Equipo! @#$%^&*()";
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(nombreEspecial)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(nombreEspecial, dto.getNombre(), "El nombre con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Debería manejar nombre con espacios al inicio y al final")
    void shouldHandleNombreWithLeadingAndTrailingSpaces() {
        // Arrange
        String nombreConEspacios = "  Comunicación Efectiva  ";
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(nombreConEspacios)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre con espacios");
        assertEquals(nombreConEspacios, dto.getNombre(), "El nombre con espacios debería mantenerse");
    }

    @Test
    @DisplayName("Debería manejar nombre con acentos y caracteres Unicode")
    void shouldHandleNombreWithAccentsAndUnicode() {
        // Arrange
        String nombreUnicode = "José María Comunicación Ñáéíóú ü";
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(nombreUnicode)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres Unicode");
        assertEquals(nombreUnicode, dto.getNombre(), "El nombre con caracteres Unicode debería mantenerse");
    }

    @Test
    @DisplayName("Debería manejar nombre con caracteres numéricos")
    void shouldHandleNombreWithNumericCharacters() {
        // Arrange
        String nombreConNumeros = "Habilidad 2024";
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(nombreConNumeros)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre con números");
        assertEquals(nombreConNumeros, dto.getNombre(), "El nombre con números debería mantenerse");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos HabilidadDto con los mismos valores")
    void shouldBeEqualWhenComparingTwoHabilidadDtosWithSameValues() {
        // Arrange
        HabilidadDto dto1 = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        HabilidadDto dto2 = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos HabilidadDto deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos HabilidadDto con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoHabilidadDtosWithDifferentValues() {
        // Arrange
        HabilidadDto dto1 = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        HabilidadDto dto2 = HabilidadDto.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos HabilidadDto no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan con objeto de diferente clase")
    void shouldNotBeEqualWhenComparingWithDifferentClass() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        String differentObject = "String de prueba";

        // Act & Assert
        assertNotEquals(dto, differentObject, "No debería ser igual a objeto de diferente clase");
        assertNotEquals(dto, null, "No debería ser igual a null");
    }

    // ==================== TESTS DE SETTERS ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        HabilidadDto dto = new HabilidadDto();

        // Act
        dto.setId(10);
        dto.setNombre("Nuevo Nombre de Habilidad");

        // Assert
        assertEquals(10, dto.getId(), "El ID debería estar actualizado");
        assertEquals("Nuevo Nombre de Habilidad", dto.getNombre(), "El nombre debería estar actualizado");
    }

    @Test
    @DisplayName("Debería permitir establecer ID nulo mediante setter")
    void shouldAllowSettingNullId() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación")
                .build();

        // Act
        dto.setId(null);

        // Assert
        assertNull(dto.getId(), "El ID debería ser nulo después de settear null");
    }

    @Test
    @DisplayName("Debería permitir establecer nombre nulo mediante setter")
    void shouldAllowSettingNullNombre() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación")
                .build();

        // Act
        dto.setNombre(null);

        // Assert
        assertNull(dto.getNombre(), "El nombre debería ser nulo después de settear null");
    }

    @Test
    @DisplayName("Debería permitir establecer nombre vacío mediante setter")
    void shouldAllowSettingEmptyNombre() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación")
                .build();

        // Act
        dto.setNombre("");

        // Assert
        assertEquals("", dto.getNombre(), "El nombre vacío debería poder establecerse");
    }

    // ==================== TESTS DE TO_STRING ====================

    @Test
    @DisplayName("toString debería incluir información relevante")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        // Act
        String toStringResult = dto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Comunicación Efectiva"), "toString debería contener el nombre");
    }

    @Test
    @DisplayName("toString debería manejar valores nulos")
    void toString_ShouldHandleNullValues() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(null)
                .nombre(null)
                .build();

        // Act
        String toStringResult = dto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("null"), "toString debería mostrar null para valores nulos");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void shouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        HabilidadDto dto = HabilidadDto.builder()
                .id(idMax)
                .nombre("Habilidad con ID máximo")
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para ID máximo");
        assertEquals(idMax, dto.getId(), "El ID debería ser Integer.MAX_VALUE");
    }

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MIN_VALUE)")
    void shouldHandleMinIntegerId() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        HabilidadDto dto = HabilidadDto.builder()
                .id(idMin)
                .nombre("Habilidad con ID mínimo")
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para ID mínimo");
        assertEquals(idMin, dto.getId(), "El ID debería ser Integer.MIN_VALUE");
    }

    @Test
    @DisplayName("Debería manejar ID negativo")
    void shouldHandleNegativeId() {
        // Arrange
        Integer idNegativo = -1;
        HabilidadDto dto = HabilidadDto.builder()
                .id(idNegativo)
                .nombre("Habilidad con ID negativo")
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para ID negativo");
        assertEquals(idNegativo, dto.getId(), "El ID negativo debería mantenerse");
    }

    @Test
    @DisplayName("Debería manejar nombre con 500 caracteres")
    void shouldHandleNombreWith500Characters() {
        // Arrange
        String nombreLargo = "A".repeat(500);
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(nombreLargo)
                .build();

        // Act
        Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre muy largo");
        assertEquals(500, dto.getNombre().length(), "El nombre debería tener 500 caracteres");
    }

    // ==================== TESTS DE BUILDER ====================

    @Test
    @DisplayName("Builder debería crear objetos correctamente con todos los campos")
    void builder_ShouldCreateObjectCorrectlyWithAllFields() {
        // Arrange & Act
        HabilidadDto dto = HabilidadDto.builder()
                .id(5)
                .nombre("Creatividad")
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(5, dto.getId(), "El ID debería ser 5");
        assertEquals("Creatividad", dto.getNombre(), "El nombre debería ser 'Creatividad'");
    }

    @Test
    @DisplayName("Builder debería crear objeto con solo ID")
    void builder_ShouldCreateObjectWithOnlyId() {
        // Arrange & Act
        HabilidadDto dto = HabilidadDto.builder()
                .id(7)
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(7, dto.getId(), "El ID debería ser 7");
        assertNull(dto.getNombre(), "El nombre debería ser nulo");
    }

    @Test
    @DisplayName("Builder debería crear objeto con solo nombre")
    void builder_ShouldCreateObjectWithOnlyNombre() {
        // Arrange & Act
        HabilidadDto dto = HabilidadDto.builder()
                .nombre("Pensamiento Crítico")
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertNull(dto.getId(), "El ID debería ser nulo");
        assertEquals("Pensamiento Crítico", dto.getNombre(), "El nombre debería ser 'Pensamiento Crítico'");
    }

    @Test
    @DisplayName("Builder debería crear objeto vacío")
    void builder_ShouldCreateEmptyObject() {
        // Arrange & Act
        HabilidadDto dto = HabilidadDto.builder().build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertNull(dto.getId(), "El ID debería ser nulo");
        assertNull(dto.getNombre(), "El nombre debería ser nulo");
    }

    // ==================== TESTS DE TIPOS DE HABILIDADES ====================

    @Test
    @DisplayName("Debería manejar diferentes tipos de habilidades blandas")
    void shouldHandleDifferentSoftSkills() {
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
        for (int i = 0; i < softSkills.length; i++) {
            HabilidadDto dto = HabilidadDto.builder()
                    .id(i + 100)
                    .nombre(softSkills[i])
                    .build();

            assertNotNull(dto, "El DTO no debería ser nulo para: " + softSkills[i]);
            assertEquals(softSkills[i], dto.getNombre(), "El nombre debería coincidir para: " + softSkills[i]);
            assertEquals(i + 100, dto.getId(), "El ID debería ser " + (i + 100));

            // Validar
            Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);
            assertTrue(violations.isEmpty(), "No debería haber violaciones para: " + softSkills[i]);
        }
    }

    @Test
    @DisplayName("Debería manejar habilidades con nombres compuestos")
    void shouldHandleCompoundNames() {
        // Arrange
        String[] compoundNames = {
                "Desarrollo de Software",
                "Gestión de Proyectos",
                "Análisis de Datos",
                "Inteligencia Artificial",
                "Machine Learning",
                "Deep Learning",
                "Procesamiento de Lenguaje Natural",
                "Visión por Computadora",
                "Robótica y Automatización",
                "Ciberseguridad y Redes"
        };

        // Act & Assert
        for (int i = 0; i < compoundNames.length; i++) {
            HabilidadDto dto = HabilidadDto.builder()
                    .id(i + 200)
                    .nombre(compoundNames[i])
                    .build();

            assertNotNull(dto, "El DTO no debería ser nulo para: " + compoundNames[i]);
            assertEquals(compoundNames[i], dto.getNombre(), "El nombre debería coincidir para: " + compoundNames[i]);

            Set<ConstraintViolation<HabilidadDto>> violations = validator.validate(dto);
            assertTrue(violations.isEmpty(), "No debería haber violaciones para: " + compoundNames[i]);
        }
    }

    // ==================== TESTS DE MÚLTIPLES INSTANCIAS ====================

    @Test
    @DisplayName("Debería crear múltiples instancias de HabilidadDto independientes")
    void shouldCreateMultipleIndependentInstances() {
        // Arrange & Act
        HabilidadDto dto1 = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación")
                .build();

        HabilidadDto dto2 = HabilidadDto.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .build();

        HabilidadDto dto3 = HabilidadDto.builder()
                .id(3)
                .nombre("Adaptabilidad")
                .build();

        // Assert
        assertNotNull(dto1, "DTO1 no debería ser nulo");
        assertNotNull(dto2, "DTO2 no debería ser nulo");
        assertNotNull(dto3, "DTO3 no debería ser nulo");

        assertEquals(1, dto1.getId(), "ID del DTO1 debería ser 1");
        assertEquals(2, dto2.getId(), "ID del DTO2 debería ser 2");
        assertEquals(3, dto3.getId(), "ID del DTO3 debería ser 3");

        assertEquals("Comunicación", dto1.getNombre(), "Nombre del DTO1 debería ser 'Comunicación'");
        assertEquals("Trabajo en Equipo", dto2.getNombre(), "Nombre del DTO2 debería ser 'Trabajo en Equipo'");
        assertEquals("Adaptabilidad", dto3.getNombre(), "Nombre del DTO3 debería ser 'Adaptabilidad'");

        // Verificar que son diferentes instancias
        assertNotEquals(dto1, dto2, "DTO1 y DTO2 deberían ser diferentes");
        assertNotEquals(dto1, dto3, "DTO1 y DTO3 deberían ser diferentes");
        assertNotEquals(dto2, dto3, "DTO2 y DTO3 deberían ser diferentes");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        HabilidadDto dto = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            dto.getNombre(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE ENCADENAMIENTO DE MÉTODOS ====================

    @Test
    @DisplayName("Debería permitir encadenamiento de métodos (builder fluent API)")
    void shouldAllowMethodChaining() {
        // Arrange & Act
        HabilidadDto dto = HabilidadDto.builder()
                .id(50)
                .nombre("Pensamiento Estratégico")
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(50, dto.getId());
        assertEquals("Pensamiento Estratégico", dto.getNombre());
    }

    @Test
    @DisplayName("Los setters deberían permitir encadenamiento (si se implementa)")
    void shouldAllowSetterChaining() {
        // Arrange
        HabilidadDto dto = new HabilidadDto();

        // Act
        dto.setId(60);
        dto.setNombre("Visión Estratégica");

        // Assert
        assertEquals(60, dto.getId());
        assertEquals("Visión Estratégica", dto.getNombre());
    }

    // ==================== TESTS DE CONSTRUCTORES ====================

    @Test
    @DisplayName("Constructor con parámetros debería funcionar correctamente")
    void parameterizedConstructor_ShouldWorkCorrectly() {
        // Arrange
        Integer id = 30;
        String nombre = "Innovación";

        // Act
        HabilidadDto dto = new HabilidadDto(id, nombre);

        // Assert
        assertEquals(id, dto.getId(), "El ID debería ser el pasado al constructor");
        assertEquals(nombre, dto.getNombre(), "El nombre debería ser el pasado al constructor");
    }

    @Test
    @DisplayName("Constructor con parámetros nulos debería funcionar correctamente")
    void parameterizedConstructor_ShouldWorkWithNullValues() {
        // Arrange & Act
        HabilidadDto dto = new HabilidadDto(null, null);

        // Assert
        assertNull(dto.getId(), "El ID debería ser nulo");
        assertNull(dto.getNombre(), "El nombre debería ser nulo");
    }

    @Test
    @DisplayName("Constructor por defecto debería funcionar correctamente")
    void defaultConstructor_ShouldWorkCorrectly() {
        // Arrange & Act
        HabilidadDto dto = new HabilidadDto();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertNull(dto.getId(), "El ID debería ser nulo");
        assertNull(dto.getNombre(), "El nombre debería ser nulo");
    }
}