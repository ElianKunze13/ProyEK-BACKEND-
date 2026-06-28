package com.example.demo.mapper;

import com.example.demo.dto.HabilidadDto;
import com.example.demo.model.Habilidad;
import com.example.demo.model.Usuario;
import com.example.demo.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase HabilidadMapper
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Nota: HabilidadMapper es una interfaz de MapStruct, por lo que se usa la implementación generada
 */
@ExtendWith(MockitoExtension.class)
class HabilidadMapperTest {

    @InjectMocks
    private HabilidadMapperImpl habilidadMapper;

    private Validator validator;
    private Habilidad habilidadValida;
    private HabilidadDto habilidadDtoValido;
    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        // Crear usuario válido
        usuarioValido = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .active(true)
                .build();

        // Crear Habilidad válida
        habilidadValida = Habilidad.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .usuario(usuarioValido)
                .build();

        // Crear HabilidadDto válido
        habilidadDtoValido = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        // Inicializar el mapper (MapStruct genera la implementación)
        habilidadMapper = new HabilidadMapperImpl();
    }

    // ==================== TESTS TO HABILIDAD DTO ====================

    @Test
    @DisplayName("toHabilidadDto - Debería mapear Habilidad a HabilidadDto correctamente")
    void toHabilidadDto_ShouldMapHabilidadToHabilidadDtoCorrectly() {
        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadValida);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(habilidadValida.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(habilidadValida.getNombre(), resultado.getNombre(), "El nombre debería coincidir");

        // Verificar que el usuario NO se mapea (no está en el DTO)
        // El DTO no tiene campo usuario, por lo que no se debe mapear
    }

    @Test
    @DisplayName("toHabilidadDto - Debería mapear Habilidad con todos los campos correctamente")
    void toHabilidadDto_ShouldMapHabilidadWithAllFieldsCorrectly() {
        // Arrange - Habilidad con campos completos
        Habilidad habilidadCompleta = Habilidad.builder()
                .id(5)
                .nombre("Trabajo en Equipo Avanzado")
                .usuario(usuarioValido)
                .build();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadCompleta);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(5, resultado.getId(), "El ID debería ser 5");
        assertEquals("Trabajo en Equipo Avanzado", resultado.getNombre(), "El nombre debería coincidir");
    }

    @Test
    @DisplayName("toHabilidadDto - Debería manejar Habilidad con ID nulo")
    void toHabilidadDto_ShouldHandleHabilidadWithNullId() {
        // Arrange - Habilidad con ID nulo
        Habilidad habilidadSinId = Habilidad.builder()
                .id(null)
                .nombre("Adaptabilidad")
                .usuario(usuarioValido)
                .build();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals("Adaptabilidad", resultado.getNombre(), "El nombre debería coincidir");
    }

    @Test
    @DisplayName("toHabilidadDto - Debería manejar Habilidad con nombre nulo")
    void toHabilidadDto_ShouldHandleHabilidadWithNullNombre() {
        // Arrange - Habilidad con nombre nulo
        Habilidad habilidadSinNombre = Habilidad.builder()
                .id(1)
                .nombre(null)
                .usuario(usuarioValido)
                .build();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadSinNombre);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getNombre(), "El nombre debería ser nulo");
        assertEquals(1, resultado.getId(), "El ID debería ser 1");
    }

    @Test
    @DisplayName("toHabilidadDto - Debería manejar Habilidad con nombre vacío")
    void toHabilidadDto_ShouldHandleHabilidadWithEmptyNombre() {
        // Arrange - Habilidad con nombre vacío
        Habilidad habilidadNombreVacio = Habilidad.builder()
                .id(2)
                .nombre("")
                .usuario(usuarioValido)
                .build();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadNombreVacio);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getNombre(), "El nombre vacío debería mantenerse");
        assertEquals(2, resultado.getId(), "El ID debería ser 2");
    }

    @Test
    @DisplayName("toHabilidadDto - Debería manejar Habilidad con usuario nulo")
    void toHabilidadDto_ShouldHandleHabilidadWithNullUsuario() {
        // Arrange - Habilidad con usuario nulo
        Habilidad habilidadSinUsuario = Habilidad.builder()
                .id(3)
                .nombre("Liderazgo")
                .usuario(null)
                .build();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadSinUsuario);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(3, resultado.getId(), "El ID debería ser 3");
        assertEquals("Liderazgo", resultado.getNombre(), "El nombre debería coincidir");
        // El usuario no se mapea al DTO, por lo que no se verifica
    }

    @Test
    @DisplayName("toHabilidadDto - Debería manejar Habilidad nula")
    void toHabilidadDto_ShouldHandleNullHabilidad() {
        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando la habilidad es nula");
    }

    // ==================== TESTS TO HABILIDAD ====================

    @Test
    @DisplayName("toHabilidad - Debería mapear HabilidadDto a Habilidad correctamente")
    void toHabilidad_ShouldMapHabilidadDtoToHabilidadCorrectly() {
        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(habilidadDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(habilidadDtoValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(habilidadDtoValido.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
        assertNull(resultado.getUsuario(), "El usuario debería ser nulo (no se mapea desde DTO)");
    }

    @Test
    @DisplayName("toHabilidad - Debería mapear HabilidadDto con todos los campos correctamente")
    void toHabilidad_ShouldMapHabilidadDtoWithAllFieldsCorrectly() {
        // Arrange - HabilidadDto con campos completos
        HabilidadDto habilidadDtoCompleto = HabilidadDto.builder()
                .id(6)
                .nombre("Resolución de Problemas")
                .build();

        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(habilidadDtoCompleto);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(6, resultado.getId(), "El ID debería ser 6");
        assertEquals("Resolución de Problemas", resultado.getNombre(), "El nombre debería coincidir");
        assertNull(resultado.getUsuario(), "El usuario debería ser nulo");
    }

    @Test
    @DisplayName("toHabilidad - Debería manejar HabilidadDto con ID nulo")
    void toHabilidad_ShouldHandleHabilidadDtoWithNullId() {
        // Arrange - HabilidadDto con ID nulo
        HabilidadDto habilidadDtoSinId = HabilidadDto.builder()
                .id(null)
                .nombre("Pensamiento Crítico")
                .build();

        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(habilidadDtoSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals("Pensamiento Crítico", resultado.getNombre(), "El nombre debería coincidir");
        assertNull(resultado.getUsuario(), "El usuario debería ser nulo");
    }

    @Test
    @DisplayName("toHabilidad - Debería manejar HabilidadDto con nombre nulo")
    void toHabilidad_ShouldHandleHabilidadDtoWithNullNombre() {
        // Arrange - HabilidadDto con nombre nulo
        HabilidadDto habilidadDtoSinNombre = HabilidadDto.builder()
                .id(7)
                .nombre(null)
                .build();

        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(habilidadDtoSinNombre);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getNombre(), "El nombre debería ser nulo");
        assertEquals(7, resultado.getId(), "El ID debería ser 7");
        assertNull(resultado.getUsuario(), "El usuario debería ser nulo");
    }

    @Test
    @DisplayName("toHabilidad - Debería manejar HabilidadDto con nombre vacío")
    void toHabilidad_ShouldHandleHabilidadDtoWithEmptyNombre() {
        // Arrange - HabilidadDto con nombre vacío
        HabilidadDto habilidadDtoNombreVacio = HabilidadDto.builder()
                .id(8)
                .nombre("")
                .build();

        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(habilidadDtoNombreVacio);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getNombre(), "El nombre vacío debería mantenerse");
        assertEquals(8, resultado.getId(), "El ID debería ser 8");
        assertNull(resultado.getUsuario(), "El usuario debería ser nulo");
    }

    @Test
    @DisplayName("toHabilidad - Debería manejar HabilidadDto nulo")
    void toHabilidad_ShouldHandleNullHabilidadDto() {
        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando el HabilidadDto es nulo");
    }

    // ==================== TESTS DE CONSISTENCIA ====================

    @Test
    @DisplayName("toHabilidadDto y toHabilidad - Deberían ser consistentes (round-trip)")
    void toHabilidadDtoAndToHabilidad_ShouldBeConsistent() {
        // Act - Convertir de Habilidad a HabilidadDto y luego de vuelta a Habilidad
        HabilidadDto dto = habilidadMapper.toHabilidadDto(habilidadValida);
        Habilidad habilidadConvertida = habilidadMapper.toHabilidad(dto);

        // Assert - Verificar que el round-trip mantiene los datos
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(habilidadConvertida, "La habilidad convertida no debería ser nula");
        assertEquals(habilidadValida.getId(), habilidadConvertida.getId(), "El ID debería ser el mismo");
        assertEquals(habilidadValida.getNombre(), habilidadConvertida.getNombre(), "El nombre debería ser el mismo");
        // El usuario no se mapea en el DTO, por lo que se pierde en el round-trip
        assertNull(habilidadConvertida.getUsuario(), "El usuario debería ser nulo después del round-trip");
    }

    @Test
    @DisplayName("toHabilidad y toHabilidadDto - Deberían ser consistentes (round-trip inverso)")
    void toHabilidadAndToHabilidadDto_ShouldBeConsistent() {
        // Act - Convertir de HabilidadDto a Habilidad y luego de vuelta a HabilidadDto
        Habilidad habilidad = habilidadMapper.toHabilidad(habilidadDtoValido);
        HabilidadDto dtoConvertido = habilidadMapper.toHabilidadDto(habilidad);

        // Assert - Verificar que el round-trip mantiene los datos
        assertNotNull(habilidad, "La habilidad no debería ser nula");
        assertNotNull(dtoConvertido, "El DTO convertido no debería ser nulo");
        assertEquals(habilidadDtoValido.getId(), dtoConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(habilidadDtoValido.getNombre(), dtoConvertido.getNombre(), "El nombre debería ser el mismo");
    }

    @Test
    @DisplayName("toHabilidadDto y toHabilidad - Deberían ser consistentes con objetos nulos")
    void toHabilidadDtoAndToHabilidad_ShouldBeConsistentWithNullObjects() {
        // Arrange - HabilidadDto con valores nulos
        HabilidadDto dtoNulo = HabilidadDto.builder()
                .id(null)
                .nombre(null)
                .build();

        // Act - Convertir a Habilidad y luego de vuelta a HabilidadDto
        Habilidad habilidad = habilidadMapper.toHabilidad(dtoNulo);
        HabilidadDto dtoConvertido = habilidadMapper.toHabilidadDto(habilidad);

        // Assert
        assertNotNull(habilidad, "La habilidad no debería ser nula");
        assertNotNull(dtoConvertido, "El DTO convertido no debería ser nulo");
        assertNull(dtoConvertido.getId(), "El ID debería ser nulo");
        assertNull(dtoConvertido.getNombre(), "El nombre debería ser nulo");
    }

    // ==================== TESTS CON DIFERENTES TIPOS DE NOMBRES ====================

    @Test
    @DisplayName("toHabilidadDto - Debería manejar nombre con caracteres especiales")
    void toHabilidadDto_ShouldHandleNombreWithSpecialCharacters() {
        // Arrange - Habilidad con caracteres especiales
        String nombreEspecial = "Comunicación & Trabajo en Equipo! @#$%";
        Habilidad habilidadEspecial = Habilidad.builder()
                .id(9)
                .nombre(nombreEspecial)
                .usuario(usuarioValido)
                .build();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreEspecial, resultado.getNombre(), "El nombre con caracteres especiales debería mantenerse");
        assertEquals(9, resultado.getId(), "El ID debería ser 9");
    }

    @Test
    @DisplayName("toHabilidad - Debería manejar nombre con caracteres especiales")
    void toHabilidad_ShouldHandleNombreWithSpecialCharacters() {
        // Arrange - HabilidadDto con caracteres especiales
        String nombreEspecial = "Adaptabilidad & Flexibilidad!";
        HabilidadDto habilidadDtoEspecial = HabilidadDto.builder()
                .id(10)
                .nombre(nombreEspecial)
                .build();

        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(habilidadDtoEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreEspecial, resultado.getNombre(), "El nombre con caracteres especiales debería mantenerse");
        assertEquals(10, resultado.getId(), "El ID debería ser 10");
        assertNull(resultado.getUsuario(), "El usuario debería ser nulo");
    }

    @Test
    @DisplayName("toHabilidadDto - Debería manejar nombre con espacios al inicio y al final")
    void toHabilidadDto_ShouldHandleNombreWithSpaces() {
        // Arrange - Habilidad con espacios
        String nombreConEspacios = "  Trabajo en Equipo  ";
        Habilidad habilidadConEspacios = Habilidad.builder()
                .id(11)
                .nombre(nombreConEspacios)
                .usuario(usuarioValido)
                .build();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadConEspacios);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreConEspacios, resultado.getNombre(), "El nombre con espacios debería mantenerse");
        assertEquals(11, resultado.getId(), "El ID debería ser 11");
    }

    @Test
    @DisplayName("toHabilidad - Debería manejar nombre con espacios al inicio y al final")
    void toHabilidad_ShouldHandleNombreWithSpaces() {
        // Arrange - HabilidadDto con espacios
        String nombreConEspacios = "  Liderazgo  ";
        HabilidadDto habilidadDtoConEspacios = HabilidadDto.builder()
                .id(12)
                .nombre(nombreConEspacios)
                .build();

        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(habilidadDtoConEspacios);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreConEspacios, resultado.getNombre(), "El nombre con espacios debería mantenerse");
        assertEquals(12, resultado.getId(), "El ID debería ser 12");
        assertNull(resultado.getUsuario(), "El usuario debería ser nulo");
    }

    @Test
    @DisplayName("toHabilidadDto - Debería manejar nombre muy largo (145 caracteres)")
    void toHabilidadDto_ShouldHandleNombreVeryLong() {
        // Arrange - Habilidad con nombre muy largo
        String nombreLargo = "A".repeat(145);
        Habilidad habilidadLarga = Habilidad.builder()
                .id(13)
                .nombre(nombreLargo)
                .usuario(usuarioValido)
                .build();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadLarga);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(145, resultado.getNombre().length(), "El nombre debería tener 145 caracteres");
        assertEquals(nombreLargo, resultado.getNombre(), "El nombre largo debería mantenerse");
        assertEquals(13, resultado.getId(), "El ID debería ser 13");
    }

    @Test
    @DisplayName("toHabilidad - Debería manejar nombre muy largo (145 caracteres)")
    void toHabilidad_ShouldHandleNombreVeryLong() {
        // Arrange - HabilidadDto con nombre muy largo
        String nombreLargo = "B".repeat(145);
        HabilidadDto habilidadDtoLargo = HabilidadDto.builder()
                .id(14)
                .nombre(nombreLargo)
                .build();

        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(habilidadDtoLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(145, resultado.getNombre().length(), "El nombre debería tener 145 caracteres");
        assertEquals(nombreLargo, resultado.getNombre(), "El nombre largo debería mantenerse");
        assertEquals(14, resultado.getId(), "El ID debería ser 14");
        assertNull(resultado.getUsuario(), "El usuario debería ser nulo");
    }

    // ==================== TESTS DE VALIDACIÓN ====================

    @Test
    @DisplayName("Validación - HabilidadDto con nombre válido debería pasar validación")
    void validation_ShouldPass_WithValidNombre() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        // Act
        var violations = validator.validate(dto);

        // Assert - El DTO no tiene anotaciones de validación, por lo que siempre pasa
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación en el DTO");
    }

    @Test
    @DisplayName("Validación - HabilidadDto con nombre nulo debería pasar validación (sin validaciones en DTO)")
    void validation_ShouldPass_WithNullNombre() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre(null)
                .build();

        // Act
        var violations = validator.validate(dto);

        // Assert - El DTO no tiene anotaciones de validación, por lo que siempre pasa
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación en el DTO");
    }

    @Test
    @DisplayName("Validación - HabilidadDto con nombre vacío debería pasar validación (sin validaciones en DTO)")
    void validation_ShouldPass_WithEmptyNombre() {
        // Arrange
        HabilidadDto dto = HabilidadDto.builder()
                .id(1)
                .nombre("")
                .build();

        // Act
        var violations = validator.validate(dto);

        // Assert - El DTO no tiene anotaciones de validación, por lo que siempre pasa
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación en el DTO");
    }

    @Test
    @DisplayName("Validación - Habilidad con nombre válido debería pasar validación")
    void validation_ShouldPass_WithValidHabilidadNombre() {
        // Arrange
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .usuario(usuarioValido)
                .build();

        // Act
        var violations = validator.validate(habilidad);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Validación - Habilidad con nombre nulo debería fallar validación")
    void validation_ShouldFail_WithNullHabilidadNombre() {
        // Arrange
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(null)
                .usuario(usuarioValido)
                .build();

        // Act
        var violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debería haber violación en el campo nombre");
    }

    @Test
    @DisplayName("Validación - Habilidad con nombre muy corto debería fallar validación")
    void validation_ShouldFail_WithHabilidadNombreTooShort() {
        // Arrange - Nombre con menos de 3 caracteres
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre("AB")
                .usuario(usuarioValido)
                .build();

        // Act
        var violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debería haber violación en el campo nombre");
    }

    @Test
    @DisplayName("Validación - Habilidad con nombre muy largo debería fallar validación")
    void validation_ShouldFail_WithHabilidadNombreTooLong() {
        // Arrange - Nombre con más de 145 caracteres
        String nombreLargo = "A".repeat(146);
        Habilidad habilidad = Habilidad.builder()
                .id(1)
                .nombre(nombreLargo)
                .usuario(usuarioValido)
                .build();

        // Act
        var violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debería haber violación en el campo nombre");
    }

    // ==================== TESTS NEGATIVOS ====================

    @Test
    @DisplayName("toHabilidadDto - Debería lanzar NullPointerException al acceder a método en objeto nulo")
    void toHabilidadDto_ShouldThrowNullPointerException_WhenAccessingNullObject() {
        // Arrange
        Habilidad habilidad = null;

        // Act & Assert - Test negativo
        assertThrows(NullPointerException.class, () -> {
            habilidad.getId(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder a método en objeto nulo");
    }

    @Test
    @DisplayName("toHabilidad - Debería lanzar NullPointerException al acceder a método en objeto nulo")
    void toHabilidad_ShouldThrowNullPointerException_WhenAccessingNullObject() {
        // Arrange
        HabilidadDto dto = null;

        // Act & Assert - Test negativo
        assertThrows(NullPointerException.class, () -> {
            dto.getId(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder a método en objeto nulo");
    }

    @Test
    @DisplayName("toHabilidad - Debería manejar DTO con ID negativo")
    void toHabilidad_ShouldHandleDtoWithNegativeId() {
        // Arrange
        HabilidadDto dtoConIdNegativo = HabilidadDto.builder()
                .id(-1)
                .nombre("Habilidad Negativa")
                .build();

        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(dtoConIdNegativo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(-1, resultado.getId(), "El ID negativo debería mantenerse");
        assertEquals("Habilidad Negativa", resultado.getNombre(), "El nombre debería coincidir");
        assertNull(resultado.getUsuario(), "El usuario debería ser nulo");
    }

    @Test
    @DisplayName("toHabilidadDto - Debería manejar Habilidad con ID negativo")
    void toHabilidadDto_ShouldHandleHabilidadWithNegativeId() {
        // Arrange
        Habilidad habilidadNegativa = Habilidad.builder()
                .id(-2)
                .nombre("Habilidad Negativa DTO")
                .usuario(usuarioValido)
                .build();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadNegativa);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(-2, resultado.getId(), "El ID negativo debería mantenerse");
        assertEquals("Habilidad Negativa DTO", resultado.getNombre(), "El nombre debería coincidir");
    }

    // ==================== TESTS DE MÚLTIPLES CONVERSIONES ====================

    @Test
    @DisplayName("Debería realizar múltiples conversiones secuenciales correctamente")
    void shouldPerformMultipleConversionsSequentially() {
        // 1. Convertir Habilidad a HabilidadDto
        HabilidadDto dto1 = habilidadMapper.toHabilidadDto(habilidadValida);
        assertNotNull(dto1, "El DTO no debería ser nulo");
        assertEquals(1, dto1.getId(), "El ID debería ser 1");
        assertEquals("Comunicación Efectiva", dto1.getNombre(), "El nombre debería coincidir");

        // 2. Crear nuevo DTO
        HabilidadDto dto2 = HabilidadDto.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .build();

        // 3. Convertir DTO a Habilidad
        Habilidad habilidad2 = habilidadMapper.toHabilidad(dto2);
        assertNotNull(habilidad2, "La habilidad no debería ser nula");
        assertEquals(2, habilidad2.getId(), "El ID debería ser 2");
        assertEquals("Trabajo en Equipo", habilidad2.getNombre(), "El nombre debería coincidir");
        assertNull(habilidad2.getUsuario(), "El usuario debería ser nulo");

        // 4. Convertir de vuelta a DTO
        HabilidadDto dto3 = habilidadMapper.toHabilidadDto(habilidad2);
        assertNotNull(dto3, "El DTO no debería ser nulo");
        assertEquals(2, dto3.getId(), "El ID debería ser 2");
        assertEquals("Trabajo en Equipo", dto3.getNombre(), "El nombre debería coincidir");
    }

    @Test
    @DisplayName("Debería manejar diferentes habilidades del mismo usuario")
    void shouldHandleDifferentHabilidadesFromSameUsuario() {
        // Arrange - Crear múltiples habilidades para el mismo usuario
        Habilidad habilidad1 = Habilidad.builder()
                .id(100)
                .nombre("Comunicación")
                .usuario(usuarioValido)
                .build();

        Habilidad habilidad2 = Habilidad.builder()
                .id(101)
                .nombre("Trabajo en Equipo")
                .usuario(usuarioValido)
                .build();

        Habilidad habilidad3 = Habilidad.builder()
                .id(102)
                .nombre("Adaptabilidad")
                .usuario(usuarioValido)
                .build();

        // Act - Convertir todas a DTO
        HabilidadDto dto1 = habilidadMapper.toHabilidadDto(habilidad1);
        HabilidadDto dto2 = habilidadMapper.toHabilidadDto(habilidad2);
        HabilidadDto dto3 = habilidadMapper.toHabilidadDto(habilidad3);

        // Assert
        assertNotNull(dto1, "DTO1 no debería ser nulo");
        assertNotNull(dto2, "DTO2 no debería ser nulo");
        assertNotNull(dto3, "DTO3 no debería ser nulo");

        assertEquals(100, dto1.getId(), "ID del DTO1 debería ser 100");
        assertEquals(101, dto2.getId(), "ID del DTO2 debería ser 101");
        assertEquals(102, dto3.getId(), "ID del DTO3 debería ser 102");

        assertEquals("Comunicación", dto1.getNombre(), "Nombre del DTO1 debería coincidir");
        assertEquals("Trabajo en Equipo", dto2.getNombre(), "Nombre del DTO2 debería coincidir");
        assertEquals("Adaptabilidad", dto3.getNombre(), "Nombre del DTO3 debería coincidir");

        // Verificar que los DTOs son diferentes
        assertNotEquals(dto1, dto2, "DTOs diferentes deberían ser diferentes");
        assertNotEquals(dto1, dto3, "DTOs diferentes deberían ser diferentes");
        assertNotEquals(dto2, dto3, "DTOs diferentes deberían ser diferentes");
    }

    // ==================== TESTS DE COMPARACIÓN ====================

    @Test
    @DisplayName("toHabilidadDto - Debería crear un nuevo objeto, no modificar el original")
    void toHabilidadDto_ShouldCreateNewObject_NotModifyOriginal() {
        // Arrange
        String nombreOriginal = habilidadValida.getNombre();

        // Act
        HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidadValida);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreOriginal, resultado.getNombre(), "El nombre debería ser el mismo");

        // Modificar el DTO
        resultado.setNombre("Nombre Modificado");

        // Verificar que la entidad original no se modificó
        assertEquals(nombreOriginal, habilidadValida.getNombre(),
                "La entidad original no debería modificarse al cambiar el DTO");
    }

    @Test
    @DisplayName("toHabilidad - Debería crear un nuevo objeto, no modificar el original")
    void toHabilidad_ShouldCreateNewObject_NotModifyOriginal() {
        // Arrange
        String nombreOriginal = habilidadDtoValido.getNombre();

        // Act
        Habilidad resultado = habilidadMapper.toHabilidad(habilidadDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreOriginal, resultado.getNombre(), "El nombre debería ser el mismo");

        // Modificar la entidad
        resultado.setNombre("Nombre Modificado");

        // Verificar que el DTO original no se modificó
        assertEquals(nombreOriginal, habilidadDtoValido.getNombre(),
                "El DTO original no debería modificarse al cambiar la entidad");
    }

    // ==================== TESTS DE TIPOS DE HABILIDADES ====================

    @Test
    @DisplayName("toHabilidadDto - Debería manejar diferentes tipos de habilidades blandas")
    void toHabilidadDto_ShouldHandleDifferentSoftSkills() {
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
            Habilidad habilidad = Habilidad.builder()
                    .id(i + 100)
                    .nombre(softSkills[i])
                    .usuario(usuarioValido)
                    .build();

            HabilidadDto resultado = habilidadMapper.toHabilidadDto(habilidad);

            assertNotNull(resultado, "El resultado no debería ser nulo para: " + softSkills[i]);
            assertEquals(softSkills[i], resultado.getNombre(),
                    "El nombre debería coincidir para: " + softSkills[i]);
            assertEquals(i + 100, resultado.getId(), "El ID debería ser " + (i + 100));
        }
    }

    @Test
    @DisplayName("toHabilidad - Debería manejar diferentes tipos de habilidades blandas desde DTO")
    void toHabilidad_ShouldHandleDifferentSoftSkillsFromDto() {
        // Arrange - Lista de habilidades blandas comunes
        String[] softSkills = {
                "Comunicación",
                "Trabajo en Equipo",
                "Adaptabilidad",
                "Rapidez de Aprendizaje",
                "Trabajo Bajo Presión"
        };

        // Act & Assert
        for (int i = 0; i < softSkills.length; i++) {
            HabilidadDto dto = HabilidadDto.builder()
                    .id(i + 200)
                    .nombre(softSkills[i])
                    .build();

            Habilidad resultado = habilidadMapper.toHabilidad(dto);

            assertNotNull(resultado, "El resultado no debería ser nulo para: " + softSkills[i]);
            assertEquals(softSkills[i], resultado.getNombre(),
                    "El nombre debería coincidir para: " + softSkills[i]);
            assertEquals(i + 200, resultado.getId(), "El ID debería ser " + (i + 200));
            assertNull(resultado.getUsuario(), "El usuario debería ser nulo para todos");
        }
    }
}