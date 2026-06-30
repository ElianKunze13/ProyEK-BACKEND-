package com.example.demo.mapper;

import com.example.demo.dto.MensajeDto;
import com.example.demo.model.Mensaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase MensajeMapper
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Usa MapStruct para el mapeo entre Mensaje y MensajeDto
 */
@ExtendWith(MockitoExtension.class)
class MensajeMapperTest {

    private MensajeMapper mensajeMapper;

    private Mensaje mensajeValido;
    private MensajeDto mensajeDtoValido;
    private Mensaje mensajeSinMensaje;
    private MensajeDto mensajeDtoSinMensaje;
    private Mensaje mensajeConFechaManual;
    private MensajeDto mensajeDtoConFechaManual;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        // Obtener la instancia del mapper generado por MapStruct
        mensajeMapper = Mappers.getMapper(MensajeMapper.class);

        LocalDate fechaActual = LocalDate.now();

        // Crear mensaje válido
        mensajeValido = Mensaje.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan.perez@email.com")
                .mensaje("Este es un mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaActual)
                .build();

        // Crear MensajeDto válido
        mensajeDtoValido = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan.perez@email.com")
                .mensaje("Este es un mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaActual)
                .build();

        // Crear mensaje sin contenido (mensaje nulo)
        mensajeSinMensaje = Mensaje.builder()
                .id(2)
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .fechaCreacion(fechaActual)
                .build();

        // Crear MensajeDto sin contenido (mensaje nulo)
        mensajeDtoSinMensaje = MensajeDto.builder()
                .id(2)
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .fechaCreacion(fechaActual)
                .build();

        // Crear mensaje con fecha manual
        LocalDate fechaManual = LocalDate.of(2024, 1, 1);
        mensajeConFechaManual = Mensaje.builder()
                .id(3)
                .nombreUsuario("María García")
                .email("maria.garcia@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaManual)
                .build();

        // Crear MensajeDto con fecha manual
        mensajeDtoConFechaManual = MensajeDto.builder()
                .id(3)
                .nombreUsuario("María García")
                .email("maria.garcia@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaManual)
                .build();
    }

    // ==================== TESTS TO DTO ====================

    @Test
    @DisplayName("toMensajeDto - Debería mapear Mensaje a MensajeDto correctamente")
    void toMensajeDto_ShouldMapMensajeToMensajeDtoCorrectly() {
        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(mensajeValido.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals(mensajeValido.getEmail(), resultado.getEmail(), "El email debería coincidir");
        assertEquals(mensajeValido.getMensaje(), resultado.getMensaje(), "El mensaje debería coincidir");
        assertEquals(mensajeValido.getFechaCreacion(), resultado.getFechaCreacion(),
                "La fecha de creación debería coincidir");
    }

    @Test
    @DisplayName("toMensajeDto - Debería mapear Mensaje con valores nulos a MensajeDto")
    void toMensajeDto_ShouldMapMensajeWithNullValuesToMensajeDto() {
        // Arrange - Mensaje con campos nulos
        Mensaje mensajeNulo = Mensaje.builder()
                .id(null)
                .nombreUsuario(null)
                .email(null)
                .mensaje(null)
                .fechaCreacion(null)
                .build();

        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeNulo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertNull(resultado.getNombreUsuario(), "El nombre de usuario debería ser nulo");
        assertNull(resultado.getEmail(), "El email debería ser nulo");
        assertNull(resultado.getMensaje(), "El mensaje debería ser nulo");
        assertNull(resultado.getFechaCreacion(), "La fecha de creación debería ser nula");
    }

    @Test
    @DisplayName("toMensajeDto - Debería mapear Mensaje sin mensaje (campo opcional)")
    void toMensajeDto_ShouldMapMensajeWithoutMessage() {
        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeSinMensaje);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeSinMensaje.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(mensajeSinMensaje.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals(mensajeSinMensaje.getEmail(), resultado.getEmail(), "El email debería coincidir");
        assertNull(resultado.getMensaje(), "El mensaje debería ser nulo");
        assertEquals(mensajeSinMensaje.getFechaCreacion(), resultado.getFechaCreacion(),
                "La fecha de creación debería coincidir");
    }

    @Test
    @DisplayName("toMensajeDto - Debería mapear Mensaje con fecha manual")
    void toMensajeDto_ShouldMapMensajeWithManualDate() {
        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeConFechaManual);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeConFechaManual.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(mensajeConFechaManual.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals(mensajeConFechaManual.getEmail(), resultado.getEmail(), "El email debería coincidir");
        assertEquals(mensajeConFechaManual.getMensaje(), resultado.getMensaje(), "El mensaje debería coincidir");
        assertEquals(LocalDate.of(2024, 1, 1), resultado.getFechaCreacion(),
                "La fecha de creación debería ser la manual");
    }

    @Test
    @DisplayName("toMensajeDto - Debería manejar Mensaje nulo")
    void toMensajeDto_ShouldHandleNullMensaje() {
        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando el mensaje es nulo");
    }

    // ==================== TESTS TO ENTITY ====================

    @Test
    @DisplayName("toMensaje - Debería mapear MensajeDto a Mensaje correctamente")
    void toMensaje_ShouldMapMensajeDtoToMensajeCorrectly() {
        // Act
        Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeDtoValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(mensajeDtoValido.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals(mensajeDtoValido.getEmail(), resultado.getEmail(), "El email debería coincidir");
        assertEquals(mensajeDtoValido.getMensaje(), resultado.getMensaje(), "El mensaje debería coincidir");
        assertEquals(mensajeDtoValido.getFechaCreacion(), resultado.getFechaCreacion(),
                "La fecha de creación debería coincidir");
    }

    @Test
    @DisplayName("toMensaje - Debería mapear MensajeDto con valores nulos a Mensaje")
    void toMensaje_ShouldMapMensajeDtoWithNullValuesToMensaje() {
        // Arrange - MensajeDto con campos nulos
        MensajeDto mensajeDtoNulo = MensajeDto.builder()
                .id(null)
                .nombreUsuario(null)
                .email(null)
                .mensaje(null)
                .fechaCreacion(null)
                .build();

        // Act
        Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoNulo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertNull(resultado.getNombreUsuario(), "El nombre de usuario debería ser nulo");
        assertNull(resultado.getEmail(), "El email debería ser nulo");
        assertNull(resultado.getMensaje(), "El mensaje debería ser nulo");
        assertNull(resultado.getFechaCreacion(), "La fecha de creación debería ser nula");
    }

    @Test
    @DisplayName("toMensaje - Debería mapear MensajeDto sin mensaje (campo opcional)")
    void toMensaje_ShouldMapMensajeDtoWithoutMessage() {
        // Act
        Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoSinMensaje);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeDtoSinMensaje.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(mensajeDtoSinMensaje.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals(mensajeDtoSinMensaje.getEmail(), resultado.getEmail(), "El email debería coincidir");
        assertNull(resultado.getMensaje(), "El mensaje debería ser nulo");
        assertEquals(mensajeDtoSinMensaje.getFechaCreacion(), resultado.getFechaCreacion(),
                "La fecha de creación debería coincidir");
    }

    @Test
    @DisplayName("toMensaje - Debería mapear MensajeDto con fecha manual")
    void toMensaje_ShouldMapMensajeDtoWithManualDate() {
        // Act
        Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoConFechaManual);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeDtoConFechaManual.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(mensajeDtoConFechaManual.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals(mensajeDtoConFechaManual.getEmail(), resultado.getEmail(), "El email debería coincidir");
        assertEquals(mensajeDtoConFechaManual.getMensaje(), resultado.getMensaje(), "El mensaje debería coincidir");
        assertEquals(LocalDate.of(2024, 1, 1), resultado.getFechaCreacion(),
                "La fecha de creación debería ser la manual");
    }

    @Test
    @DisplayName("toMensaje - Debería manejar MensajeDto nulo")
    void toMensaje_ShouldHandleNullMensajeDto() {
        // Act
        Mensaje resultado = mensajeMapper.toMensaje(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando el MensajeDto es nulo");
    }

    // ==================== TESTS DE CONSISTENCIA ====================

    @Test
    @DisplayName("toMensajeDto y toMensaje - Deberían ser consistentes (round-trip)")
    void toMensajeDtoAndToMensaje_ShouldBeConsistent() {
        // Act - Convertir de Mensaje a MensajeDto y luego de vuelta a Mensaje
        MensajeDto dto = mensajeMapper.toMensajeDto(mensajeValido);
        Mensaje mensajeConvertido = mensajeMapper.toMensaje(dto);

        // Assert - Verificar que el round-trip mantiene los datos
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(mensajeConvertido, "El mensaje convertido no debería ser nulo");
        assertEquals(mensajeValido.getId(), mensajeConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(mensajeValido.getNombreUsuario(), mensajeConvertido.getNombreUsuario(),
                "El nombre de usuario debería ser el mismo");
        assertEquals(mensajeValido.getEmail(), mensajeConvertido.getEmail(), "El email debería ser el mismo");
        assertEquals(mensajeValido.getMensaje(), mensajeConvertido.getMensaje(), "El mensaje debería ser el mismo");
        assertEquals(mensajeValido.getFechaCreacion(), mensajeConvertido.getFechaCreacion(),
                "La fecha de creación debería ser la misma");
    }

    @Test
    @DisplayName("toMensajeDto y toMensaje - Deberían ser consistentes con objetos nulos")
    void toMensajeDtoAndToMensaje_ShouldBeConsistentWithNullObjects() {
        // Act - Convertir de MensajeDto a Mensaje y luego de vuelta a MensajeDto
        Mensaje mensaje = mensajeMapper.toMensaje(mensajeDtoValido);
        MensajeDto dtoConvertido = mensajeMapper.toMensajeDto(mensaje);

        // Assert
        assertNotNull(mensaje, "El mensaje no debería ser nulo");
        assertNotNull(dtoConvertido, "El DTO convertido no debería ser nulo");
        assertEquals(mensajeDtoValido.getId(), dtoConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(mensajeDtoValido.getNombreUsuario(), dtoConvertido.getNombreUsuario(),
                "El nombre de usuario debería ser el mismo");
        assertEquals(mensajeDtoValido.getEmail(), dtoConvertido.getEmail(), "El email debería ser el mismo");
        assertEquals(mensajeDtoValido.getMensaje(), dtoConvertido.getMensaje(), "El mensaje debería ser el mismo");
        assertEquals(mensajeDtoValido.getFechaCreacion(), dtoConvertido.getFechaCreacion(),
                "La fecha de creación debería ser la misma");
    }

    @Test
    @DisplayName("toMensajeDto y toMensaje - Deberían ser consistentes con campos vacíos")
    void toMensajeDtoAndToMensaje_ShouldBeConsistentWithEmptyFields() {
        // Arrange - Mensaje con campos vacíos
        Mensaje mensajeVacio = Mensaje.builder()
                .id(10)
                .nombreUsuario("")
                .email("")
                .mensaje("")
                .fechaCreacion(LocalDate.now())
                .build();

        // Act - Convertir de Mensaje a MensajeDto y luego de vuelta a Mensaje
        MensajeDto dto = mensajeMapper.toMensajeDto(mensajeVacio);
        Mensaje mensajeConvertido = mensajeMapper.toMensaje(dto);

        // Assert
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(mensajeConvertido, "El mensaje convertido no debería ser nulo");
        assertEquals(mensajeVacio.getId(), mensajeConvertido.getId(), "El ID debería ser el mismo");
        assertEquals("", mensajeConvertido.getNombreUsuario(), "El nombre de usuario vacío debería mantenerse");
        assertEquals("", mensajeConvertido.getEmail(), "El email vacío debería mantenerse");
        assertEquals("", mensajeConvertido.getMensaje(), "El mensaje vacío debería mantenerse");
    }

    @Test
    @DisplayName("toMensajeDto y toMensaje - Deberían ser consistentes sin mensaje")
    void toMensajeDtoAndToMensaje_ShouldBeConsistentWithoutMessage() {
        // Act - Convertir de Mensaje a MensajeDto y luego de vuelta a Mensaje
        MensajeDto dto = mensajeMapper.toMensajeDto(mensajeSinMensaje);
        Mensaje mensajeConvertido = mensajeMapper.toMensaje(dto);

        // Assert
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(mensajeConvertido, "El mensaje convertido no debería ser nulo");
        assertEquals(mensajeSinMensaje.getId(), mensajeConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(mensajeSinMensaje.getNombreUsuario(), mensajeConvertido.getNombreUsuario(),
                "El nombre de usuario debería ser el mismo");
        assertEquals(mensajeSinMensaje.getEmail(), mensajeConvertido.getEmail(), "El email debería ser el mismo");
        assertNull(mensajeConvertido.getMensaje(), "El mensaje debería ser nulo");
    }

    // ==================== TESTS CON CARACTERES ESPECIALES ====================

    @Test
    @DisplayName("toMensajeDto - Debería mapear Mensaje con caracteres especiales")
    void toMensajeDto_ShouldMapMensajeWithSpecialCharacters() {
        // Arrange
        String nombreEspecial = "José María ñáéíóú ÜÑ";
        String emailEspecial = "josé.maria@email.com";
        String mensajeEspecial = "Mensaje con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";

        Mensaje mensajeEspecialChars = Mensaje.builder()
                .id(5)
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .fechaCreacion(LocalDate.now())
                .build();

        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeEspecialChars);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreEspecial, resultado.getNombreUsuario(),
                "El nombre con caracteres especiales debería mantenerse");
        assertEquals(emailEspecial, resultado.getEmail(),
                "El email con caracteres especiales debería mantenerse");
        assertEquals(mensajeEspecial, resultado.getMensaje(),
                "El mensaje con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("toMensaje - Debería mapear MensajeDto con caracteres especiales")
    void toMensaje_ShouldMapMensajeDtoWithSpecialCharacters() {
        // Arrange
        String nombreEspecial = "María José ñáéíóú ÜÑ";
        String emailEspecial = "maria.josé@email.com";
        String mensajeEspecial = "Mensaje DTO con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";

        MensajeDto mensajeDtoEspecial = MensajeDto.builder()
                .id(6)
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .fechaCreacion(LocalDate.now())
                .build();

        // Act
        Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreEspecial, resultado.getNombreUsuario(),
                "El nombre con caracteres especiales debería mantenerse");
        assertEquals(emailEspecial, resultado.getEmail(),
                "El email con caracteres especiales debería mantenerse");
        assertEquals(mensajeEspecial, resultado.getMensaje(),
                "El mensaje con caracteres especiales debería mantenerse");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("toMensajeDto - Debería manejar ID negativo")
    void toMensajeDto_ShouldHandleNegativeId() {
        // Arrange
        Mensaje mensajeIdNegativo = Mensaje.builder()
                .id(-1)
                .nombreUsuario("Usuario Negativo")
                .email("negativo@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeIdNegativo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(-1, resultado.getId(), "El ID negativo debería mantenerse");
    }

    @Test
    @DisplayName("toMensaje - Debería manejar ID negativo")
    void toMensaje_ShouldHandleNegativeId() {
        // Arrange
        MensajeDto mensajeDtoIdNegativo = MensajeDto.builder()
                .id(-2)
                .nombreUsuario("DTO Negativo")
                .email("dto.negativo@email.com")
                .mensaje("Mensaje DTO de prueba con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        // Act
        Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoIdNegativo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(-2, resultado.getId(), "El ID negativo debería mantenerse");
    }

    @Test
    @DisplayName("toMensajeDto - Debería manejar email muy largo")
    void toMensajeDto_ShouldHandleVeryLongEmail() {
        // Arrange
        String emailLargo = "a".repeat(90) + "@email.com"; // 100 caracteres
        Mensaje mensajeEmailLargo = Mensaje.builder()
                .id(7)
                .nombreUsuario("Usuario Email Largo")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeEmailLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(emailLargo, resultado.getEmail(), "El email largo debería mantenerse");
        assertEquals(100, resultado.getEmail().length(), "El email debería tener exactamente 100 caracteres");
    }

    @Test
    @DisplayName("toMensaje - Debería manejar email muy largo")
    void toMensaje_ShouldHandleVeryLongEmail() {
        // Arrange
        String emailLargo = "a".repeat(90) + "@email.com"; // 100 caracteres
        MensajeDto mensajeDtoEmailLargo = MensajeDto.builder()
                .id(8)
                .nombreUsuario("DTO Email Largo")
                .email(emailLargo)
                .mensaje("Mensaje DTO de prueba con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        // Act
        Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoEmailLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(emailLargo, resultado.getEmail(), "El email largo debería mantenerse");
        assertEquals(100, resultado.getEmail().length(), "El email debería tener exactamente 100 caracteres");
    }

    @Test
    @DisplayName("toMensajeDto - Debería manejar mensaje muy largo")
    void toMensajeDto_ShouldHandleVeryLongMessage() {
        // Arrange
        String mensajeLargo = "a".repeat(1000);
        Mensaje mensajeMensajeLargo = Mensaje.builder()
                .id(9)
                .nombreUsuario("Usuario Mensaje Largo")
                .email("test@email.com")
                .mensaje(mensajeLargo)
                .fechaCreacion(LocalDate.now())
                .build();

        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeMensajeLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeLargo, resultado.getMensaje(), "El mensaje largo debería mantenerse");
        assertEquals(1000, resultado.getMensaje().length(), "El mensaje debería tener exactamente 1000 caracteres");
    }

    @Test
    @DisplayName("toMensaje - Debería manejar mensaje muy largo")
    void toMensaje_ShouldHandleVeryLongMessage() {
        // Arrange
        String mensajeLargo = "a".repeat(1000);
        MensajeDto mensajeDtoMensajeLargo = MensajeDto.builder()
                .id(10)
                .nombreUsuario("DTO Mensaje Largo")
                .email("test@email.com")
                .mensaje(mensajeLargo)
                .fechaCreacion(LocalDate.now())
                .build();

        // Act
        Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoMensajeLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeLargo, resultado.getMensaje(), "El mensaje largo debería mantenerse");
        assertEquals(1000, resultado.getMensaje().length(), "El mensaje debería tener exactamente 1000 caracteres");
    }

    // ==================== TESTS DE VERIFICACIÓN DE INSTANCIA ====================

    @Test
    @DisplayName("INSTANCE - Debería obtener una instancia válida de MensajeMapper")
    void instance_ShouldReturnValidMensajeMapperInstance() {
        // Arrange & Act
        MensajeMapper mapper = MensajeMapper.INSTANCE;

        // Assert
        assertNotNull(mapper, "La instancia de MensajeMapper no debería ser nula");
        assertNotNull(mapper.toMensajeDto(mensajeValido), "El mapper debería poder mapear");
        assertNotNull(mapper.toMensaje(mensajeDtoValido), "El mapper debería poder mapear");
    }

    @Test
    @DisplayName("Mappers.getMapper - Debería obtener una instancia válida de MensajeMapper")
    void getMapper_ShouldReturnValidMensajeMapperInstance() {
        // Arrange & Act
        MensajeMapper mapper = Mappers.getMapper(MensajeMapper.class);

        // Assert
        assertNotNull(mapper, "El mapper no debería ser nulo");
        assertNotNull(mapper.toMensajeDto(mensajeValido), "El mapper debería poder mapear");
        assertNotNull(mapper.toMensaje(mensajeDtoValido), "El mapper debería poder mapear");
    }

    // ==================== TESTS DE COMPARACIÓN ====================

    @Test
    @DisplayName("toMensajeDto - Debería mantener todos los valores del objeto original")
    void toMensajeDto_ShouldMaintainAllValuesFromOriginalObject() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        Mensaje mensajeOriginal = Mensaje.builder()
                .id(11)
                .nombreUsuario("Usuario Original")
                .email("original@email.com")
                .mensaje("Mensaje original con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Act
        MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeOriginal);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeOriginal.getId(), resultado.getId(), "El ID debería ser el mismo");
        assertEquals(mensajeOriginal.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería ser el mismo");
        assertEquals(mensajeOriginal.getEmail(), resultado.getEmail(), "El email debería ser el mismo");
        assertEquals(mensajeOriginal.getMensaje(), resultado.getMensaje(), "El mensaje debería ser el mismo");
        assertEquals(mensajeOriginal.getFechaCreacion(), resultado.getFechaCreacion(),
                "La fecha de creación debería ser la misma");

        // Verificar que los objetos son diferentes instancias pero con mismos valores
        assertNotSame(mensajeOriginal, resultado, "Deberían ser objetos diferentes");
    }

    @Test
    @DisplayName("toMensaje - Debería mantener todos los valores del DTO original")
    void toMensaje_ShouldMaintainAllValuesFromOriginalDto() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        MensajeDto mensajeDtoOriginal = MensajeDto.builder()
                .id(12)
                .nombreUsuario("DTO Original")
                .email("dto.original@email.com")
                .mensaje("Mensaje DTO original con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Act
        Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoOriginal);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeDtoOriginal.getId(), resultado.getId(), "El ID debería ser el mismo");
        assertEquals(mensajeDtoOriginal.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería ser el mismo");
        assertEquals(mensajeDtoOriginal.getEmail(), resultado.getEmail(), "El email debería ser el mismo");
        assertEquals(mensajeDtoOriginal.getMensaje(), resultado.getMensaje(), "El mensaje debería ser el mismo");
        assertEquals(mensajeDtoOriginal.getFechaCreacion(), resultado.getFechaCreacion(),
                "La fecha de creación debería ser la misma");

        // Verificar que los objetos son diferentes instancias pero con mismos valores
        assertNotSame(mensajeDtoOriginal, resultado, "Deberían ser objetos diferentes");
    }

    // ==================== TESTS DE MÚLTIPLES INSTANCIAS ====================

    @Test
    @DisplayName("toMensajeDto - Debería mapear múltiples mensajes correctamente")
    void toMensajeDto_ShouldMapMultipleMensajesCorrectly() {
        // Arrange - Crear múltiples mensajes
        Mensaje[] mensajes = new Mensaje[5];
        for (int i = 0; i < mensajes.length; i++) {
            mensajes[i] = Mensaje.builder()
                    .id(i + 1)
                    .nombreUsuario("Usuario " + i)
                    .email("usuario" + i + "@email.com")
                    .mensaje("Mensaje " + i + " con más de 10 caracteres.")
                    .fechaCreacion(LocalDate.now())
                    .build();
        }

        // Act - Mapear todos los mensajes
        MensajeDto[] resultados = new MensajeDto[mensajes.length];
        for (int i = 0; i < mensajes.length; i++) {
            resultados[i] = mensajeMapper.toMensajeDto(mensajes[i]);
        }

        // Assert - Verificar que cada DTO tiene los valores correctos
        for (int i = 0; i < resultados.length; i++) {
            assertNotNull(resultados[i], "El DTO " + i + " no debería ser nulo");
            assertEquals(mensajes[i].getId(), resultados[i].getId(), "El ID de " + i + " debería coincidir");
            assertEquals(mensajes[i].getNombreUsuario(), resultados[i].getNombreUsuario(),
                    "El nombre de usuario de " + i + " debería coincidir");
            assertEquals(mensajes[i].getEmail(), resultados[i].getEmail(),
                    "El email de " + i + " debería coincidir");
            assertEquals(mensajes[i].getMensaje(), resultados[i].getMensaje(),
                    "El mensaje de " + i + " debería coincidir");

            // Verificar que los DTOs son diferentes entre sí
            for (int j = i + 1; j < resultados.length; j++) {
                assertNotEquals(resultados[i], resultados[j],
                        "Los DTOs " + i + " y " + j + " no deberían ser iguales");
            }
        }
    }

    @Test
    @DisplayName("toMensaje - Debería mapear múltiples DTOs correctamente")
    void toMensaje_ShouldMapMultipleDtosCorrectly() {
        // Arrange - Crear múltiples DTOs
        MensajeDto[] dtos = new MensajeDto[5];
        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = MensajeDto.builder()
                    .id(i + 10)
                    .nombreUsuario("DTO " + i)
                    .email("dto" + i + "@email.com")
                    .mensaje("Mensaje DTO " + i + " con más de 10 caracteres.")
                    .fechaCreacion(LocalDate.now())
                    .build();
        }

        // Act - Mapear todos los DTOs
        Mensaje[] resultados = new Mensaje[dtos.length];
        for (int i = 0; i < dtos.length; i++) {
            resultados[i] = mensajeMapper.toMensaje(dtos[i]);
        }

        // Assert - Verificar que cada mensaje tiene los valores correctos
        for (int i = 0; i < resultados.length; i++) {
            assertNotNull(resultados[i], "El mensaje " + i + " no debería ser nulo");
            assertEquals(dtos[i].getId(), resultados[i].getId(), "El ID de " + i + " debería coincidir");
            assertEquals(dtos[i].getNombreUsuario(), resultados[i].getNombreUsuario(),
                    "El nombre de usuario de " + i + " debería coincidir");
            assertEquals(dtos[i].getEmail(), resultados[i].getEmail(),
                    "El email de " + i + " debería coincidir");
            assertEquals(dtos[i].getMensaje(), resultados[i].getMensaje(),
                    "El mensaje de " + i + " debería coincidir");

            // Verificar que los mensajes son diferentes entre sí
            for (int j = i + 1; j < resultados.length; j++) {
                assertNotEquals(resultados[i], resultados[j],
                        "Los mensajes " + i + " y " + j + " no deberían ser iguales");
            }
        }
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("toMensajeDto - Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void toMensajeDto_ShouldThrowNullPointerException_WhenAccessingNullObject() {
        // Arrange
        Mensaje mensajeNulo = null;

        // Act & Assert - El mapper maneja null devolviendo null
        assertDoesNotThrow(() -> {
            MensajeDto resultado = mensajeMapper.toMensajeDto(mensajeNulo);
            assertNull(resultado, "Debería devolver null para mensaje nulo");
        }, "El mapper debería manejar null sin lanzar excepción");

        // Test de acceso a método en objeto nulo
        assertThrows(NullPointerException.class, () -> {
            Mensaje mensaje = null;
            mensaje.getNombreUsuario(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    @Test
    @DisplayName("toMensaje - Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void toMensaje_ShouldThrowNullPointerException_WhenAccessingNullObject() {
        // Arrange
        MensajeDto mensajeDtoNulo = null;

        // Act & Assert - El mapper maneja null devolviendo null
        assertDoesNotThrow(() -> {
            Mensaje resultado = mensajeMapper.toMensaje(mensajeDtoNulo);
            assertNull(resultado, "Debería devolver null para MensajeDto nulo");
        }, "El mapper debería manejar null sin lanzar excepción");

        // Test de acceso a método en objeto nulo
        assertThrows(NullPointerException.class, () -> {
            MensajeDto dto = null;
            dto.getNombreUsuario(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TEST DE IGUALDAD ====================

    @Test
    @DisplayName("toMensajeDto - Debería mantener igualdad de contenido")
    void toMensajeDto_ShouldMaintainContentEquality() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        Mensaje mensaje1 = Mensaje.builder()
                .id(20)
                .nombreUsuario("Usuario Igual")
                .email("igual@email.com")
                .mensaje("Mensaje igual con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        Mensaje mensaje2 = Mensaje.builder()
                .id(20)
                .nombreUsuario("Usuario Igual")
                .email("igual@email.com")
                .mensaje("Mensaje igual con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Act
        MensajeDto dto1 = mensajeMapper.toMensajeDto(mensaje1);
        MensajeDto dto2 = mensajeMapper.toMensajeDto(mensaje2);

        // Assert
        assertEquals(dto1, dto2, "Los DTOs deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("toMensaje - Debería mantener igualdad de contenido")
    void toMensaje_ShouldMaintainContentEquality() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        MensajeDto dto1 = MensajeDto.builder()
                .id(21)
                .nombreUsuario("DTO Igual")
                .email("dto.igual@email.com")
                .mensaje("Mensaje DTO igual con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        MensajeDto dto2 = MensajeDto.builder()
                .id(21)
                .nombreUsuario("DTO Igual")
                .email("dto.igual@email.com")
                .mensaje("Mensaje DTO igual con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Act
        Mensaje mensaje1 = mensajeMapper.toMensaje(dto1);
        Mensaje mensaje2 = mensajeMapper.toMensaje(dto2);

        // Assert
        assertEquals(mensaje1, mensaje2, "Los mensajes deberían ser iguales");
        assertEquals(mensaje1.hashCode(), mensaje2.hashCode(), "Los hashCodes deberían ser iguales");
    }
}