package com.example.demo.dto;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoConocimiento;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ConocimientoDto
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Nota: ConocimientoDto no tiene anotaciones de validación (@NotNull, @Size, etc.)
 * por lo que las pruebas de validación se enfocan en la creación y comportamiento
 * de los objetos, no en validaciones de Bean Validation.
 */
class ConocimientoDtoTest {

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
    @DisplayName("Debería crear ConocimientoDto con valores válidos - Caso feliz")
    void shouldCreateConocimientoDtoWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedNombre = "Java";
        Nivel expectedNivel = Nivel.AVANZADO;
        TipoConocimiento expectedTipo = TipoConocimiento.BACKEND;
        ImagenDto expectedImagen = ImagenDto.builder()
                .id(1)
                .url("java-logo.png")
                .alt("Logo de Java")
                .build();

        // Act - Ejecutar la acción a probar
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(expectedId)
                .nombre(expectedNombre)
                .nivel(expectedNivel)
                .tipoConocimiento(expectedTipo)
                .imagen(expectedImagen)
                .build();

        // Assert - Verificar resultados
        assertNotNull(conocimientoDto, "El objeto ConocimientoDto no debería ser nulo");
        assertEquals(expectedId, conocimientoDto.getId(), "El ID debería coincidir");
        assertEquals(expectedNombre, conocimientoDto.getNombre(), "El nombre debería coincidir");
        assertEquals(expectedNivel, conocimientoDto.getNivel(), "El nivel debería coincidir");
        assertEquals(expectedTipo, conocimientoDto.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNotNull(conocimientoDto.getImagen(), "La imagen no debería ser nula");
        assertEquals(expectedImagen.getUrl(), conocimientoDto.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals(expectedImagen.getAlt(), conocimientoDto.getImagen().getAlt(), "El alt de la imagen debería coincidir");

        // Validar con Bean Validation (no hay anotaciones, pero se valida por consistencia)
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear ConocimientoDto con constructor por defecto")
    void shouldCreateConocimientoDtoWithDefaultConstructor() {
        // Arrange & Act
        ConocimientoDto conocimientoDto = new ConocimientoDto();

        // Assert
        assertNotNull(conocimientoDto, "El objeto ConocimientoDto no debería ser nulo");
        assertNull(conocimientoDto.getId(), "El ID debería ser nulo por defecto");
        assertNull(conocimientoDto.getNombre(), "El nombre debería ser nulo por defecto");
        assertNull(conocimientoDto.getNivel(), "El nivel debería ser nulo por defecto");
        assertNull(conocimientoDto.getTipoConocimiento(), "El tipo de conocimiento debería ser nulo por defecto");
        assertNull(conocimientoDto.getImagen(), "La imagen debería ser nula por defecto");
    }

    @Test
    @DisplayName("Debería crear ConocimientoDto con constructor con todos los argumentos")
    void shouldCreateConocimientoDtoWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String nombre = "Spring Boot";
        Nivel nivel = Nivel.INTERMEDIO;
        TipoConocimiento tipo = TipoConocimiento.BACKEND;
        ImagenDto imagen = ImagenDto.builder()
                .id(2)
                .url("spring-logo.png")
                .alt("Logo de Spring Boot")
                .build();

        // Act
        ConocimientoDto conocimientoDto = new ConocimientoDto(id, nombre, nivel, tipo, imagen);

        // Assert
        assertNotNull(conocimientoDto, "El objeto ConocimientoDto no debería ser nulo");
        assertEquals(id, conocimientoDto.getId(), "El ID debería coincidir");
        assertEquals(nombre, conocimientoDto.getNombre(), "El nombre debería coincidir");
        assertEquals(nivel, conocimientoDto.getNivel(), "El nivel debería coincidir");
        assertEquals(tipo, conocimientoDto.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNotNull(conocimientoDto.getImagen(), "La imagen no debería ser nula");
    }

    @Test
    @DisplayName("Debería permitir valores nulos en campos opcionales - Imagen")
    void shouldAllowNullValuesInOptionalFields() {
        // Arrange & Act - ConocimientoDto sin imagen
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre("Docker")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(null)
                .build();

        // Assert
        assertNotNull(conocimientoDto, "El objeto ConocimientoDto no debería ser nulo");
        assertNull(conocimientoDto.getImagen(), "La imagen debería ser nula");

        // Validar que no hay violaciones (ya que la imagen es opcional)
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con imagen nula");
    }

    @Test
    @DisplayName("Debería permitir valores nulos en campos opcionales - Todos los campos")
    void shouldAllowNullValuesInAllOptionalFields() {
        // Arrange & Act - ConocimientoDto con todos los campos opcionales nulos
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(null)
                .nombre(null)
                .nivel(null)
                .tipoConocimiento(null)
                .imagen(null)
                .build();

        // Assert
        assertNotNull(conocimientoDto, "El objeto ConocimientoDto no debería ser nulo");
        assertNull(conocimientoDto.getId(), "El ID debería ser nulo");
        assertNull(conocimientoDto.getNombre(), "El nombre debería ser nulo");
        assertNull(conocimientoDto.getNivel(), "El nivel debería ser nulo");
        assertNull(conocimientoDto.getTipoConocimiento(), "El tipo de conocimiento debería ser nulo");
        assertNull(conocimientoDto.getImagen(), "La imagen debería ser nula");

        // Validar que no hay violaciones (todos los campos son opcionales)
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con todos los campos nulos");
    }

    // ==================== TESTS DE ENUM ====================

    @Test
    @DisplayName("Debe manejar todos los niveles del enum Nivel")
    void shouldHandleAllNiveles() {
        // Arrange - Probar todos los niveles
        Nivel[] niveles = Nivel.values();

        for (Nivel nivel : niveles) {
            // Act
            ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                    .id(1)
                    .nombre("Test")
                    .nivel(nivel)
                    .tipoConocimiento(TipoConocimiento.BACKEND)
                    .imagen(null)
                    .build();

            // Assert
            assertNotNull(conocimientoDto, "El ConocimientoDto no debería ser nulo para nivel: " + nivel);
            assertEquals(nivel, conocimientoDto.getNivel(), "Debería manejar correctamente el nivel: " + nivel);
        }
    }

    @Test
    @DisplayName("Debe manejar todos los tipos del enum TipoConocimiento")
    void shouldHandleAllTipoConocimiento() {
        // Arrange - Probar todos los tipos de conocimiento
        TipoConocimiento[] tipos = TipoConocimiento.values();

        for (TipoConocimiento tipo : tipos) {
            // Act
            ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                    .id(1)
                    .nombre("Test")
                    .nivel(Nivel.INTERMEDIO)
                    .tipoConocimiento(tipo)
                    .imagen(null)
                    .build();

            // Assert
            assertNotNull(conocimientoDto, "El ConocimientoDto no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, conocimientoDto.getTipoConocimiento(), "Debería manejar correctamente el tipo: " + tipo);
        }
    }

    @ParameterizedTest
    @EnumSource(Nivel.class)
    @DisplayName("Debe manejar cada nivel con diferentes combinaciones de tipo")
    void shouldHandleEachNivelWithDifferentTipo(Nivel nivel) {
        // Arrange
        for (TipoConocimiento tipo : TipoConocimiento.values()) {
            // Act
            ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                    .id(1)
                    .nombre("Test " + nivel + " " + tipo)
                    .nivel(nivel)
                    .tipoConocimiento(tipo)
                    .imagen(null)
                    .build();

            // Assert
            assertNotNull(conocimientoDto, "El objeto no debería ser nulo");
            assertEquals(nivel, conocimientoDto.getNivel(), "El nivel debería coincidir");
            assertEquals(tipo, conocimientoDto.getTipoConocimiento(), "El tipo debería coincidir");
        }
    }

    // ==================== TESTS DE RELACIONES ====================

    @Test
    @DisplayName("Debe manejar relación con ImagenDto correctamente")
    void shouldHandleRelationshipWithImagenDtoCorrectly() {
        // Arrange
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("python-logo.png")
                .alt("Logo de Python")
                .build();

        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre("Python")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertNotNull(conocimientoDto.getImagen(), "La imagen no debería ser nula");
        assertEquals("python-logo.png", conocimientoDto.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Logo de Python", conocimientoDto.getImagen().getAlt(), "El alt de la imagen debería coincidir");
        assertEquals(1, conocimientoDto.getImagen().getId(), "El ID de la imagen debería coincidir");
    }

    @Test
    @DisplayName("Debe manejar creación de ConocimientoDto con imagen nula")
    void shouldHandleConocimientoDtoWithNullImage() {
        // Arrange - ConocimientoDto sin imagen
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre("React")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        // Act
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);

        // Assert
        assertNotNull(conocimientoDto, "El ConocimientoDto no debería ser nulo");
        assertNull(conocimientoDto.getImagen(), "La imagen debería ser nula");
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debe manejar creación de ConocimientoDto con imagen con todos los campos")
    void shouldHandleConocimientoDtoWithFullImage() {
        // Arrange
        ImagenDto imagenCompleta = ImagenDto.builder()
                .id(5)
                .url("full-image.png")
                .alt("Imagen completa con descripción")
                .build();

        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(2)
                .nombre("Angular")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(imagenCompleta)
                .build();

        // Act
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);

        // Assert
        assertNotNull(conocimientoDto, "El ConocimientoDto no debería ser nulo");
        assertNotNull(conocimientoDto.getImagen(), "La imagen no debería ser nula");
        assertEquals(5, conocimientoDto.getImagen().getId(), "El ID de la imagen debería ser 5");
        assertEquals("full-image.png", conocimientoDto.getImagen().getUrl(), "La URL debería coincidir");
        assertEquals("Imagen completa con descripción", conocimientoDto.getImagen().getAlt(), "El alt debería coincidir");
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .id(1)
                        .url("old-logo.png")
                        .alt("Logo antiguo")
                        .build())
                .build();

        ImagenDto nuevaImagen = ImagenDto.builder()
                .id(2)
                .url("new-logo.png")
                .alt("Logo nuevo")
                .build();

        // Act
        conocimientoDto.setId(2);
        conocimientoDto.setNombre("Java 17");
        conocimientoDto.setNivel(Nivel.INTERMEDIO);
        conocimientoDto.setTipoConocimiento(TipoConocimiento.OTROS);
        conocimientoDto.setImagen(nuevaImagen);

        // Assert
        assertEquals(2, conocimientoDto.getId(), "El ID debería estar actualizado");
        assertEquals("Java 17", conocimientoDto.getNombre(), "El nombre debería estar actualizado");
        assertEquals(Nivel.INTERMEDIO, conocimientoDto.getNivel(), "El nivel debería estar actualizado");
        assertEquals(TipoConocimiento.OTROS, conocimientoDto.getTipoConocimiento(), "El tipo debería estar actualizado");
        assertNotNull(conocimientoDto.getImagen(), "La imagen no debería ser nula");
        assertEquals("new-logo.png", conocimientoDto.getImagen().getUrl(), "La URL de la imagen debería estar actualizada");
        assertEquals("Logo nuevo", conocimientoDto.getImagen().getAlt(), "El alt de la imagen debería estar actualizado");
    }

    @Test
    @DisplayName("Debería permitir setear imagen a null")
    void shouldAllowSettingImageToNull() {
        // Arrange
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre("Docker")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(ImagenDto.builder()
                        .id(1)
                        .url("docker-logo.png")
                        .alt("Logo de Docker")
                        .build())
                .build();

        // Act
        conocimientoDto.setImagen(null);

        // Assert
        assertNull(conocimientoDto.getImagen(), "La imagen debería ser nula después de setear a null");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        ConocimientoDto conocimientoDto = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            conocimientoDto.getNombre(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos ConocimientoDto con los mismos valores")
    void shouldBeEqualWhenComparingTwoConocimientoDtosWithSameValues() {
        // Arrange
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("logo.png")
                .alt("Logo")
                .build();

        ConocimientoDto dto1 = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        ConocimientoDto dto2 = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos ConocimientoDto deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos ConocimientoDto con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoConocimientoDtosWithDifferentValues() {
        // Arrange
        ConocimientoDto dto1 = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .build();

        ConocimientoDto dto2 = ConocimientoDto.builder()
                .id(2)
                .nombre("Python")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos ConocimientoDto no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos ConocimientoDto con imagen nula")
    void shouldBeEqualWhenComparingTwoConocimientoDtosWithNullImage() {
        // Arrange
        ConocimientoDto dto1 = ConocimientoDto.builder()
                .id(1)
                .nombre("React")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        ConocimientoDto dto2 = ConocimientoDto.builder()
                .id(1)
                .nombre("React")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos ConocimientoDto deberían ser iguales con imagen nula");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes deberían ser iguales con imagen nula");
    }

    @Test
    @DisplayName("No debería ser igual cuando las imágenes son diferentes")
    void shouldNotBeEqualWhenImagesAreDifferent() {
        // Arrange
        ImagenDto imagen1 = ImagenDto.builder()
                .id(1)
                .url("image1.png")
                .alt("Imagen 1")
                .build();

        ImagenDto imagen2 = ImagenDto.builder()
                .id(2)
                .url("image2.png")
                .alt("Imagen 2")
                .build();

        ConocimientoDto dto1 = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen1)
                .build();

        ConocimientoDto dto2 = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen2)
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales cuando las imágenes son diferentes");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes no deberían ser iguales cuando las imágenes son diferentes");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debe manejar nombres con caracteres especiales y acentos")
    void shouldHandleSpecialCharactersAndAccents() {
        // Arrange
        String nombreEspecial = "José María ñáéíóú ÜÑ";
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre(nombreEspecial)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(null)
                .build();

        // Act
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(nombreEspecial, conocimientoDto.getNombre(), "El nombre con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Debe manejar nombres muy largos")
    void shouldHandleVeryLongNames() {
        // Arrange
        String nombreLargo = "a".repeat(1000);
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre(nombreLargo)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(null)
                .build();

        // Act
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombres largos");
        assertEquals(nombreLargo, conocimientoDto.getNombre(), "El nombre largo debería mantenerse");
    }

    @Test
    @DisplayName("Debe manejar nombres vacíos")
    void shouldHandleEmptyNames() {
        // Arrange
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre("")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(null)
                .build();

        // Act
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre vacío");
        assertEquals("", conocimientoDto.getNombre(), "El nombre vacío debería mantenerse");
    }

    @Test
    @DisplayName("Debe manejar nombres con espacios en blanco")
    void shouldHandleNamesWithWhitespace() {
        // Arrange
        String nombreConEspacios = "   Java   ";
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre(nombreConEspacios)
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .build();

        // Act
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para nombre con espacios");
        assertEquals(nombreConEspacios, conocimientoDto.getNombre(), "El nombre con espacios debería mantenerse");
    }

    // ==================== TESTS DE TOSTRING ====================

    @Test
    @DisplayName("toString debería incluir información relevante del ConocimientoDto")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .id(1)
                        .url("java-logo.png")
                        .alt("Logo de Java")
                        .build())
                .build();

        // Act
        String toStringResult = conocimientoDto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Java"), "toString debería contener el nombre");
        assertTrue(toStringResult.contains("AVANZADO"), "toString debería contener el nivel");
        assertTrue(toStringResult.contains("BACKEND"), "toString debería contener el tipo de conocimiento");
    }

    @Test
    @DisplayName("toString debería manejar valores nulos correctamente")
    void toString_ShouldHandleNullValues() {
        // Arrange
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(null)
                .nombre(null)
                .nivel(null)
                .tipoConocimiento(null)
                .imagen(null)
                .build();

        // Act
        String toStringResult = conocimientoDto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("null"), "toString debería contener 'null' para campos nulos");
    }

    // ==================== TESTS DE BUILDER ====================

    @Test
    @DisplayName("Builder debería crear objetos con diferentes combinaciones de campos")
    void builder_ShouldCreateObjectsWithDifferentFieldCombinations() {
        // Test 1: Solo con campos obligatorios (todos son opcionales en este caso)
        ConocimientoDto dto1 = ConocimientoDto.builder()
                .nombre("Test")
                .build();
        assertNotNull(dto1, "El objeto no debería ser nulo");
        assertEquals("Test", dto1.getNombre(), "El nombre debería coincidir");
        assertNull(dto1.getId(), "El ID debería ser nulo");
        assertNull(dto1.getNivel(), "El nivel debería ser nulo");
        assertNull(dto1.getTipoConocimiento(), "El tipo debería ser nulo");
        assertNull(dto1.getImagen(), "La imagen debería ser nula");

        // Test 2: Con todos los campos
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("logo.png")
                .alt("Logo")
                .build();

        ConocimientoDto dto2 = ConocimientoDto.builder()
                .id(2)
                .nombre("Spring")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();
        assertNotNull(dto2, "El objeto no debería ser nulo");
        assertEquals(2, dto2.getId(), "El ID debería ser 2");
        assertEquals("Spring", dto2.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.ALTO, dto2.getNivel(), "El nivel debería ser ALTO");
        assertEquals(TipoConocimiento.BACKEND, dto2.getTipoConocimiento(), "El tipo debería ser BACKEND");
        assertNotNull(dto2.getImagen(), "La imagen no debería ser nula");

        // Test 3: Solo con ID y nombre
        ConocimientoDto dto3 = ConocimientoDto.builder()
                .id(3)
                .nombre("Docker")
                .build();
        assertNotNull(dto3, "El objeto no debería ser nulo");
        assertEquals(3, dto3.getId(), "El ID debería ser 3");
        assertEquals("Docker", dto3.getNombre(), "El nombre debería coincidir");
        assertNull(dto3.getNivel(), "El nivel debería ser nulo");
        assertNull(dto3.getTipoConocimiento(), "El tipo debería ser nulo");
        assertNull(dto3.getImagen(), "La imagen debería ser nula");
    }

    // ==================== TESTS DE VALIDACIÓN (aunque no hay anotaciones) ====================

    @Test
    @DisplayName("Validación - No debería tener violaciones incluso con valores nulos")
    void validation_ShouldNotHaveViolations_WithNullValues() {
        // Arrange - Todos los campos nulos
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(null)
                .nombre(null)
                .nivel(null)
                .tipoConocimiento(null)
                .imagen(null)
                .build();

        // Act
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para valores nulos");
    }

    @Test
    @DisplayName("Validación - No debería tener violaciones incluso con valores vacíos")
    void validation_ShouldNotHaveViolations_WithEmptyValues() {
        // Arrange - Valores vacíos
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(0)
                .nombre("")
                .nivel(null)
                .tipoConocimiento(null)
                .imagen(ImagenDto.builder()
                        .id(null)
                        .url("")
                        .alt("")
                        .build())
                .build();

        // Act
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para valores vacíos");
    }

    @Test
    @DisplayName("Validación - No debería tener violaciones para valores en límites extremos")
    void validation_ShouldNotHaveViolations_WithExtremeValues() {
        // Arrange - Valores extremos
        String nombreLargo = "a".repeat(10000);
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .id(Integer.MAX_VALUE)
                .nombre(nombreLargo)
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.IA)
                .imagen(ImagenDto.builder()
                        .id(Integer.MAX_VALUE)
                        .url("https://example.com/" + "a".repeat(1000) + ".png")
                        .alt("a".repeat(1000))
                        .build())
                .build();

        // Act
        Set<ConstraintViolation<ConocimientoDto>> violations = validator.validate(conocimientoDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para valores extremos");
    }
}