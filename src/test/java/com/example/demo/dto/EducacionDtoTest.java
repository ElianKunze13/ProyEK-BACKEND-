package com.example.demo.dto;

import com.example.demo.enums.TipoEducacion;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase EducacionDto
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class EducacionDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Configuración del validador para pruebas de Bean Validation
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // ==================== TESTS DE CREACIÓN Y CONSTRUCTORES ====================

    @Test
    @DisplayName("Debería crear EducacionDto con valores válidos - Caso feliz")
    void shouldCreateEducacionDtoWithValidValues() {
        // Arrange
        Integer expectedId = 1;
        String expectedTitulo = "Ingeniería Informática";
        LocalDate expectedFechaObtencion = LocalDate.of(2020, 6, 15);
        String expectedDescripcion = "Grado en Ingeniería Informática con especialización en software";
        TipoEducacion expectedTipo = TipoEducacion.FORMAL;
        ImagenDto expectedImagen = ImagenDto.builder()
                .id(10)
                .url("titulo.jpg")
                .alt("Imagen del título")
                .build();

        // Act
        EducacionDto educacionDto = EducacionDto.builder()
                .id(expectedId)
                .titulo(expectedTitulo)
                .fechaObtencion(expectedFechaObtencion)
                .descripcion(expectedDescripcion)
                .tipoEducacion(expectedTipo)
                .imagen(expectedImagen)
                .build();

        // Assert
        assertNotNull(educacionDto, "El objeto EducacionDto no debería ser nulo");
        assertEquals(expectedId, educacionDto.getId(), "El ID debería coincidir");
        assertEquals(expectedTitulo, educacionDto.getTitulo(), "El título debería coincidir");
        assertEquals(expectedFechaObtencion, educacionDto.getFechaObtencion(), "La fecha de obtención debería coincidir");
        assertEquals(expectedDescripcion, educacionDto.getDescripcion(), "La descripción debería coincidir");
        assertEquals(expectedTipo, educacionDto.getTipoEducacion(), "El tipo de educación debería coincidir");
        assertNotNull(educacionDto.getImagen(), "La imagen no debería ser nula");
        assertEquals(expectedImagen.getUrl(), educacionDto.getImagen().getUrl(), "La URL de la imagen debería coincidir");

        // Validación con Bean Validation (sin restricciones, no debe haber violaciones)
        Set<ConstraintViolation<EducacionDto>> violations = validator.validate(educacionDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear EducacionDto con el constructor por defecto")
    void shouldCreateEducacionDtoWithDefaultConstructor() {
        // Act
        EducacionDto educacionDto = new EducacionDto();

        // Assert
        assertNotNull(educacionDto, "El objeto EducacionDto no debería ser nulo");
        assertNull(educacionDto.getId(), "El ID debería ser nulo por defecto");
        assertNull(educacionDto.getTitulo(), "El título debería ser nulo por defecto");
        assertNull(educacionDto.getFechaObtencion(), "La fecha de obtención debería ser nula por defecto");
        assertNull(educacionDto.getDescripcion(), "La descripción debería ser nula por defecto");
        assertNull(educacionDto.getTipoEducacion(), "El tipo de educación debería ser nulo por defecto");
        assertNull(educacionDto.getImagen(), "La imagen debería ser nula por defecto");
    }

    @Test
    @DisplayName("Debería crear EducacionDto con el constructor con todos los argumentos")
    void shouldCreateEducacionDtoWithAllArgsConstructor() {
        // Arrange
        Integer id = 2;
        String titulo = "Máster en Inteligencia Artificial";
        LocalDate fechaObtencion = LocalDate.of(2022, 9, 30);
        String descripcion = "Máster oficial en IA y Machine Learning";
        TipoEducacion tipo = TipoEducacion.FORMAL;
        ImagenDto imagen = ImagenDto.builder()
                .id(20)
                .url("master.jpg")
                .alt("Máster IA")
                .build();

        // Act
        EducacionDto educacionDto = new EducacionDto(id, titulo, fechaObtencion, descripcion, tipo, imagen);

        // Assert
        assertNotNull(educacionDto);
        assertEquals(id, educacionDto.getId());
        assertEquals(titulo, educacionDto.getTitulo());
        assertEquals(fechaObtencion, educacionDto.getFechaObtencion());
        assertEquals(descripcion, educacionDto.getDescripcion());
        assertEquals(tipo, educacionDto.getTipoEducacion());
        assertEquals(imagen, educacionDto.getImagen());
    }

    @Test
    @DisplayName("Debería permitir valores nulos en campos opcionales (imagen, descripción, etc.)")
    void shouldAllowNullValuesInOptionalFields() {
        // Arrange & Act - EducacionDto sin imagen y sin descripción
        EducacionDto educacionDto = EducacionDto.builder()
                .id(3)
                .titulo("Curso de Spring Boot")
                .fechaObtencion(LocalDate.now())
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .descripcion(null)
                .imagen(null)
                .build();

        // Assert
        assertNull(educacionDto.getDescripcion(), "La descripción debería ser nula");
        assertNull(educacionDto.getImagen(), "La imagen debería ser nula");

        // Validación: sin restricciones, no debe haber violaciones
        Set<ConstraintViolation<EducacionDto>> violations = validator.validate(educacionDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con valores nulos");
    }

    // ==================== TESTS DE VALIDACIONES (si existieran) ====================
    // Nota: EducacionDto actualmente no tiene anotaciones de validación.
    // Estos tests demuestran que no hay violaciones para cualquier valor, incluso nulos.
    // Si en el futuro se añaden restricciones, estos tests deberían actualizarse.

    @Test
    @DisplayName("Validación - No debe haber violaciones para cualquier objeto (sin restricciones)")
    void validation_ShouldHaveNoViolationsForAnyObject() {
        // Arrange - Objeto con todos los campos nulos
        EducacionDto educacionDto = new EducacionDto();

        // Act
        Set<ConstraintViolation<EducacionDto>> violations = validator.validate(educacionDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones porque no hay restricciones");
    }

    @Test
    @DisplayName("Validación - No debe haber violaciones para objetos con valores extremos")
    void validation_ShouldHaveNoViolationsForExtremeValues() {
        // Arrange - Título muy largo (simulación de posible @Size)
        String tituloLargo = "A".repeat(1000);
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo(tituloLargo)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .build();

        // Act
        Set<ConstraintViolation<EducacionDto>> violations = validator.validate(educacionDto);

        // Assert - Sin restricciones, no hay violaciones
        assertTrue(violations.isEmpty(), "No debería haber violaciones aunque el título sea muy largo");
    }

    // ==================== TESTS DE ENUM ====================

    @Test
    @DisplayName("Debe manejar todos los valores del enum TipoEducacion")
    void shouldHandleAllTipoEducacionValues() {
        // Probar cada valor del enum
        for (TipoEducacion tipo : TipoEducacion.values()) {
            // Act
            EducacionDto educacionDto = EducacionDto.builder()
                    .titulo("Educación " + tipo.name())
                    .fechaObtencion(LocalDate.now())
                    .descripcion("Descripción para " + tipo.name())
                    .tipoEducacion(tipo)
                    .build();

            // Assert
            assertNotNull(educacionDto);
            assertEquals(tipo, educacionDto.getTipoEducacion(), "El tipo de educación debería ser " + tipo);
        }
    }

    @ParameterizedTest
    @EnumSource(TipoEducacion.class)
    @DisplayName("Debería aceptar cualquier valor de TipoEducacion en la construcción")
    void shouldAcceptAnyTipoEducacion(TipoEducacion tipo) {
        // Act
        EducacionDto educacionDto = EducacionDto.builder()
                .tipoEducacion(tipo)
                .build();

        // Assert
        assertEquals(tipo, educacionDto.getTipoEducacion());
    }

    // ==================== TESTS DE RELACIONES ====================

    @Test
    @DisplayName("Debe manejar la relación con ImagenDto correctamente")
    void shouldHandleRelationshipWithImagenDtoCorrectly() {
        // Arrange
        ImagenDto imagen = ImagenDto.builder()
                .id(99)
                .url("certificado.jpg")
                .alt("Certificado de educación")
                .build();

        EducacionDto educacionDto = EducacionDto.builder()
                .id(5)
                .titulo("Certificación AWS")
                .fechaObtencion(LocalDate.of(2023, 1, 10))
                .descripcion("Certificación de arquitecto de soluciones")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertNotNull(educacionDto.getImagen(), "La imagen no debería ser nula");
        assertEquals("certificado.jpg", educacionDto.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Certificado de educación", educacionDto.getImagen().getAlt(), "El alt de la imagen debería coincidir");
    }

    @Test
    @DisplayName("Debe manejar EducacionDto sin imagen asociada")
    void shouldHandleEducacionDtoWithoutImage() {
        // Arrange
        EducacionDto educacionDto = EducacionDto.builder()
                .id(6)
                .titulo("Curso online")
                .fechaObtencion(LocalDate.now())
                .descripcion("Curso de Java")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .imagen(null)
                .build();

        // Act
        Set<ConstraintViolation<EducacionDto>> violations = validator.validate(educacionDto);

        // Assert
        assertNull(educacionDto.getImagen(), "La imagen debería ser nula");
        assertTrue(violations.isEmpty(), "No debería haber violaciones sin imagen");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        EducacionDto educacionDto = EducacionDto.builder()
                .id(1)
                .titulo("Título inicial")
                .fechaObtencion(LocalDate.of(2020, 1, 1))
                .descripcion("Descripción inicial")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        ImagenDto nuevaImagen = ImagenDto.builder()
                .id(50)
                .url("nueva.jpg")
                .alt("Nueva imagen")
                .build();

        LocalDate nuevaFecha = LocalDate.of(2025, 12, 31);

        // Act
        educacionDto.setId(2);
        educacionDto.setTitulo("Nuevo título");
        educacionDto.setFechaObtencion(nuevaFecha);
        educacionDto.setDescripcion("Nueva descripción");
        educacionDto.setTipoEducacion(TipoEducacion.INFORMAL_CURSO);
        educacionDto.setImagen(nuevaImagen);

        // Assert
        assertEquals(2, educacionDto.getId());
        assertEquals("Nuevo título", educacionDto.getTitulo());
        assertEquals(nuevaFecha, educacionDto.getFechaObtencion());
        assertEquals("Nueva descripción", educacionDto.getDescripcion());
        assertEquals(TipoEducacion.INFORMAL_CURSO, educacionDto.getTipoEducacion());
        assertNotNull(educacionDto.getImagen());
        assertEquals("nueva.jpg", educacionDto.getImagen().getUrl());
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos EducacionDto con los mismos valores")
    void shouldBeEqualWhenComparingTwoEducacionDtosWithSameValues() {
        // Arrange
        ImagenDto imagen = ImagenDto.builder().id(1).url("img.jpg").alt("Alt").build();
        EducacionDto dto1 = EducacionDto.builder()
                .id(1)
                .titulo("Título")
                .fechaObtencion(LocalDate.of(2020, 1, 1))
                .descripcion("Desc")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .build();

        EducacionDto dto2 = EducacionDto.builder()
                .id(1)
                .titulo("Título")
                .fechaObtencion(LocalDate.of(2020, 1, 1))
                .descripcion("Desc")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos EducacionDto con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoEducacionDtosWithDifferentValues() {
        // Arrange
        EducacionDto dto1 = EducacionDto.builder()
                .id(1)
                .titulo("Título A")
                .fechaObtencion(LocalDate.of(2020, 1, 1))
                .descripcion("Desc A")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        EducacionDto dto2 = EducacionDto.builder()
                .id(2)
                .titulo("Título B")
                .fechaObtencion(LocalDate.of(2021, 2, 2))
                .descripcion("Desc B")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE toString ====================

    @Test
    @DisplayName("toString debería incluir información relevante del EducacionDto")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        EducacionDto educacionDto = EducacionDto.builder()
                .id(7)
                .titulo("Doctorado")
                .fechaObtencion(LocalDate.of(2024, 5, 20))
                .descripcion("Doctorado en Ciencias")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Act
        String toStringResult = educacionDto.toString();

        // Assert
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("7"), "toString debe contener el ID");
        assertTrue(toStringResult.contains("Doctorado"), "toString debe contener el título");
        assertTrue(toStringResult.contains("2024-05-20"), "toString debe contener la fecha");
        assertTrue(toStringResult.contains("FORMAL"), "toString debe contener el tipo");
        assertTrue(toStringResult.contains("descripcion"), "toString debe contener el campo descripcion");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debe manejar fechas extremas (pasado y futuro)")
    void shouldHandleExtremeDates() {
        // Arrange & Act - Fecha muy antigua
        LocalDate fechaAntigua = LocalDate.of(1900, 1, 1);
        EducacionDto dtoAntiguo = EducacionDto.builder()
                .titulo("Educación antigua")
                .fechaObtencion(fechaAntigua)
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // Assert
        assertEquals(fechaAntigua, dtoAntiguo.getFechaObtencion());

        // Arrange & Act - Fecha futura
        LocalDate fechaFutura = LocalDate.of(2099, 12, 31);
        EducacionDto dtoFuturo = EducacionDto.builder()
                .titulo("Educación futura")
                .fechaObtencion(fechaFutura)
                .tipoEducacion(TipoEducacion.OTROS)
                .build();

        assertEquals(fechaFutura, dtoFuturo.getFechaObtencion());

        // Validación: sin restricciones, no debe haber violaciones
        Set<ConstraintViolation<EducacionDto>> violations1 = validator.validate(dtoAntiguo);
        Set<ConstraintViolation<EducacionDto>> violations2 = validator.validate(dtoFuturo);
        assertTrue(violations1.isEmpty());
        assertTrue(violations2.isEmpty());
    }

    @Test
    @DisplayName("Debe manejar títulos y descripciones de longitud variable")
    void shouldHandleVariableLengthStrings() {
        // Arrange - Título y descripción muy largos
        String tituloLargo = "T".repeat(1000);
        String descripcionLarga = "D".repeat(2000);
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo(tituloLargo)
                .descripcion(descripcionLarga)
                .fechaObtencion(LocalDate.now())
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .build();

        // Act
        Set<ConstraintViolation<EducacionDto>> violations = validator.validate(educacionDto);

        // Assert - Sin restricciones, no hay violaciones
        assertTrue(violations.isEmpty());
        assertEquals(tituloLargo, educacionDto.getTitulo());
        assertEquals(descripcionLarga, educacionDto.getDescripcion());
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        EducacionDto educacionDto = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            educacionDto.getTitulo(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }
}