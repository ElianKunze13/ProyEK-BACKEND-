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

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase MensajeDto
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class MensajeDtoTest {

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
    @DisplayName("Debería crear MensajeDto con valores válidos - Caso feliz")
    void shouldCreateMensajeDtoWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedNombreUsuario = "Juan Pérez";
        String expectedEmail = "juan.perez@email.com";
        String expectedMensaje = "Este es un mensaje de prueba con más de 10 caracteres.";
        LocalDate expectedFechaCreacion = LocalDate.now();

        // Act - Ejecutar la acción a probar
        MensajeDto mensajeDto = MensajeDto.builder()
                .id(expectedId)
                .nombreUsuario(expectedNombreUsuario)
                .email(expectedEmail)
                .mensaje(expectedMensaje)
                .fechaCreacion(expectedFechaCreacion)
                .build();

        // Assert - Verificar resultados
        assertNotNull(mensajeDto, "El objeto MensajeDto no debería ser nulo");
        assertEquals(expectedId, mensajeDto.getId(), "El ID debería coincidir");
        assertEquals(expectedNombreUsuario, mensajeDto.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals(expectedEmail, mensajeDto.getEmail(), "El email debería coincidir");
        assertEquals(expectedMensaje, mensajeDto.getMensaje(), "El mensaje debería coincidir");
        assertEquals(expectedFechaCreacion, mensajeDto.getFechaCreacion(), "La fecha de creación debería coincidir");

        // Validar con Bean Validation
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear MensajeDto con constructor por defecto")
    void shouldCreateMensajeDtoWithDefaultConstructor() {
        // Arrange & Act
        MensajeDto mensajeDto = new MensajeDto();

        // Assert
        assertNotNull(mensajeDto, "El objeto MensajeDto no debería ser nulo");
        assertNull(mensajeDto.getId(), "El ID debería ser nulo por defecto");
        assertNull(mensajeDto.getNombreUsuario(), "El nombre de usuario debería ser nulo por defecto");
        assertNull(mensajeDto.getEmail(), "El email debería ser nulo por defecto");
        assertNull(mensajeDto.getMensaje(), "El mensaje debería ser nulo por defecto");
        assertNull(mensajeDto.getFechaCreacion(), "La fecha de creación debería ser nula por defecto");
    }

    @Test
    @DisplayName("Debería crear MensajeDto con constructor con todos los argumentos")
    void shouldCreateMensajeDtoWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String nombreUsuario = "María García";
        String email = "maria.garcia@email.com";
        String mensaje = "Mensaje de prueba con más de 10 caracteres para validación.";
        LocalDate fechaCreacion = LocalDate.now();

        // Act
        MensajeDto mensajeDto = new MensajeDto(id, nombreUsuario, email, mensaje, fechaCreacion);

        // Assert
        assertNotNull(mensajeDto, "El objeto MensajeDto no debería ser nulo");
        assertEquals(id, mensajeDto.getId(), "El ID debería coincidir");
        assertEquals(nombreUsuario, mensajeDto.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals(email, mensajeDto.getEmail(), "El email debería coincidir");
        assertEquals(mensaje, mensajeDto.getMensaje(), "El mensaje debería coincidir");
        assertEquals(fechaCreacion, mensajeDto.getFechaCreacion(), "La fecha de creación debería coincidir");
    }

    @Test
    @DisplayName("Debería crear MensajeDto sin mensaje (campo opcional)")
    void shouldCreateMensajeDtoWithoutMessage() {
        // Arrange & Act - MensajeDto sin contenido
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .build();

        // Assert
        assertNotNull(mensajeDto, "El objeto MensajeDto no debería ser nulo");
        assertEquals("Carlos López", mensajeDto.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("carlos@email.com", mensajeDto.getEmail(), "El email debería coincidir");
        assertNull(mensajeDto.getMensaje(), "El mensaje debería ser nulo");

        // Validar con Bean Validation (el mensaje es opcional)
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con mensaje nulo");
    }

    @Test
    @DisplayName("Debería crear MensajeDto sin fechaCreacion (debería ser nulo)")
    void shouldCreateMensajeDtoWithoutFechaCreacion() {
        // Arrange & Act
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Ana Martínez")
                .email("ana@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(null)
                .build();

        // Assert
        assertNotNull(mensajeDto, "El objeto MensajeDto no debería ser nulo");
        assertNull(mensajeDto.getFechaCreacion(), "La fecha de creación debería ser nula");
    }

    // ==================== TESTS DE @PrePersist ====================

    @Test
    @DisplayName("onCreate - Debería establecer la fecha actual al persistir")
    void onCreate_ShouldSetCurrentDate() {
        // Arrange
        LocalDate beforeCreation = LocalDate.now();
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Test")
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Assert - La fecha debería ser nula antes de onCreate
        assertNull(mensajeDto.getFechaCreacion(), "La fecha de creación debería ser nula antes de onCreate");

        // Act - Simular @PrePersist
        mensajeDto.onCreate();

        // Assert - La fecha debería ser establecida
        assertNotNull(mensajeDto.getFechaCreacion(), "La fecha de creación no debería ser nula después de onCreate");

        // Comparar directamente con LocalDate.now()
        LocalDate currentDate = LocalDate.now();
        assertEquals(currentDate, mensajeDto.getFechaCreacion(),
                "La fecha de creación debería ser la fecha actual");

        // También verificar que es igual o posterior a beforeCreation
        assertTrue(
                mensajeDto.getFechaCreacion().isEqual(beforeCreation) ||
                        mensajeDto.getFechaCreacion().isAfter(beforeCreation),
                "La fecha de creación debería ser igual o posterior a la fecha antes de la creación"
        );
    }

    @Test
    @DisplayName("onCreate - Debería establecer la fecha actual incluso con fecha existente")
    void onCreate_ShouldSetCurrentDateEvenWithExistingDate() {
        // Arrange
        LocalDate oldDate = LocalDate.of(2020, 1, 1);
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Test")
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(oldDate)
                .build();

        // Assert - La fecha debería ser la fecha antigua
        assertEquals(oldDate, mensajeDto.getFechaCreacion(), "La fecha de creación debería ser la fecha antigua");

        // Act - Simular @PrePersist
        mensajeDto.onCreate();

        // Assert - La fecha debería ser actualizada
        assertNotNull(mensajeDto.getFechaCreacion(), "La fecha de creación no debería ser nula");
        assertNotEquals(oldDate, mensajeDto.getFechaCreacion(), "La fecha de creación debería ser actualizada");
        assertEquals(LocalDate.now(), mensajeDto.getFechaCreacion(), "La fecha de creación debería ser la fecha actual");
    }

    @Test
    @DisplayName("onCreate - Debería establecer la fecha actual para múltiples instancias")
    void onCreate_ShouldSetCurrentDateForMultipleInstances() {
        // Arrange
        MensajeDto mensajeDto1 = MensajeDto.builder()
                .nombreUsuario("Usuario 1")
                .email("usuario1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .build();

        MensajeDto mensajeDto2 = MensajeDto.builder()
                .nombreUsuario("Usuario 2")
                .email("usuario2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .build();

        // Act
        mensajeDto1.onCreate();
        mensajeDto2.onCreate();

        // Assert
        assertNotNull(mensajeDto1.getFechaCreacion(), "La fecha de creación de mensajeDto1 no debería ser nula");
        assertNotNull(mensajeDto2.getFechaCreacion(), "La fecha de creación de mensajeDto2 no debería ser nula");
        assertEquals(LocalDate.now(), mensajeDto1.getFechaCreacion(), "La fecha de creación de mensajeDto1 debería ser la fecha actual");
        assertEquals(LocalDate.now(), mensajeDto2.getFechaCreacion(), "La fecha de creación de mensajeDto2 debería ser la fecha actual");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando nombreUsuario es nulo")
    void validation_ShouldHaveViolations_WhenNombreUsuarioIsNull() {
        // Arrange - Nombre de usuario nulo
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario(null)
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombreUsuario nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombreUsuario")),
                "Debe haber violación específica para el campo nombreUsuario");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando email está vacío o es nulo")
    void validation_ShouldHaveViolations_WhenEmailIsNullOrEmpty(String email) {
        // Arrange - Email vacío
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email(email)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Debe haber violación específica para el campo email");
    }

    @ParameterizedTest
    @ValueSource(strings = {"correo", "correo@", "@dominio.com", "correo@dominio", "correo@dominio."})
    @DisplayName("Validación - Debe tener violaciones cuando email no es válido")
    void validation_ShouldHaveViolations_WhenEmailIsInvalid(String emailInvalido) {
        // Arrange - Email inválido
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailInvalido)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email inválido: " + emailInvalido);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Debe haber violación específica para el campo email");
    }

    @ParameterizedTest
    @ValueSource(strings = {"usuario@dominio.com", "usuario+test@dominio.com", "usuario.test@dominio.com"})
    @DisplayName("Validación - Debe aceptar emails válidos")
    void validation_ShouldAcceptValidEmails(String emailValido) {
        // Arrange - Email válido
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailValido)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para email válido: " + emailValido);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "a", ""})
    @DisplayName("Validación - Debe tener violaciones cuando email es muy corto (menos de 10 caracteres)")
    void validation_ShouldHaveViolations_WhenEmailIsTooShort(String emailCorto) {
        // Arrange - Email muy corto (menos de 10 caracteres)
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailCorto)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email demasiado corto: " + emailCorto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Debe haber violación específica para el campo email");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando email es muy largo (más de 100 caracteres)")
    void validation_ShouldHaveViolations_WhenEmailIsTooLong() {
        // Arrange - Email demasiado largo
        String emailLargo = "a".repeat(90) + "@email.com"; // Más de 100 caracteres
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Debe haber violación específica para el campo email");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Este mensaje tiene más de 10 caracteres", "1234567890", "Mensaje válido"})
    @DisplayName("Validación - Debe aceptar mensajes con longitud válida (hasta 300 caracteres)")
    void validation_ShouldAcceptValidMessageLength(String mensajeValido) {
        // Arrange
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(mensajeValido)
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje válido");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando mensaje es muy largo (más de 300 caracteres)")
    void validation_ShouldHaveViolations_WhenMensajeIsTooLong() {
        // Arrange - Mensaje demasiado largo (más de 300 caracteres)
        String mensajeLargo = "a".repeat(301);
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(mensajeLargo)
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para mensaje demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("mensaje")),
                "Debe haber violación específica para el campo mensaje");
    }

    @Test
    @DisplayName("Validación - Debe aceptar mensaje exactamente de 300 caracteres (límite máximo)")
    void validation_ShouldAcceptMessageExactlyAtMaxLength() {
        // Arrange - Mensaje exactamente de 300 caracteres
        String mensajeMaximo = "a".repeat(300);
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(mensajeMaximo)
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje exactamente de 300 caracteres");
        assertEquals(300, mensajeDto.getMensaje().length(), "El mensaje debería tener exactamente 300 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe aceptar email exactamente de 100 caracteres (límite máximo)")
    void validation_ShouldAcceptEmailExactlyAtMaxLength() {
        // Arrange - Email exactamente de 100 caracteres
        String emailMaximo = "a".repeat(90) + "@email.com"; // 90 + 10 = 100
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailMaximo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para email exactamente de 100 caracteres");
        assertEquals(100, mensajeDto.getEmail().length(), "El email debería tener exactamente 100 caracteres");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos MensajeDto con los mismos valores")
    void shouldBeEqualWhenComparingTwoMensajeDtosWithSameValues() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        MensajeDto mensajeDto1 = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        MensajeDto mensajeDto2 = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Act & Assert
        assertEquals(mensajeDto1, mensajeDto2, "Los objetos MensajeDto deberían ser iguales");
        assertEquals(mensajeDto1.hashCode(), mensajeDto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos MensajeDto con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoMensajeDtosWithDifferentValues() {
        // Arrange
        LocalDate fecha1 = LocalDate.of(2024, 1, 1);
        LocalDate fecha2 = LocalDate.of(2024, 1, 2);

        MensajeDto mensajeDto1 = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .fechaCreacion(fecha1)
                .build();

        MensajeDto mensajeDto2 = MensajeDto.builder()
                .id(2)
                .nombreUsuario("María García")
                .email("maria@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .fechaCreacion(fecha2)
                .build();

        // Act & Assert
        assertNotEquals(mensajeDto1, mensajeDto2, "Los objetos MensajeDto no deberían ser iguales");
        assertNotEquals(mensajeDto1.hashCode(), mensajeDto2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        LocalDate fechaInicial = LocalDate.now();
        MensajeDto mensajeDto = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Nombre Inicial")
                .email("inicial@email.com")
                .mensaje("Mensaje inicial con más de 10 caracteres.")
                .fechaCreacion(fechaInicial)
                .build();

        LocalDate nuevaFecha = LocalDate.of(2024, 12, 31);

        // Act
        mensajeDto.setId(2);
        mensajeDto.setNombreUsuario("Nombre Actualizado");
        mensajeDto.setEmail("actualizado@email.com");
        mensajeDto.setMensaje("Mensaje actualizado con más de 10 caracteres.");
        mensajeDto.setFechaCreacion(nuevaFecha);

        // Assert
        assertEquals(2, mensajeDto.getId(), "El ID debería estar actualizado");
        assertEquals("Nombre Actualizado", mensajeDto.getNombreUsuario(), "El nombre de usuario debería estar actualizado");
        assertEquals("actualizado@email.com", mensajeDto.getEmail(), "El email debería estar actualizado");
        assertEquals("Mensaje actualizado con más de 10 caracteres.", mensajeDto.getMensaje(), "El mensaje debería estar actualizado");
        assertEquals(nuevaFecha, mensajeDto.getFechaCreacion(), "La fecha de creación debería estar actualizada");
    }

    @Test
    @DisplayName("Debería permitir establecer fechaCreacion manualmente")
    void shouldAllowManualFechaCreacionSetter() {
        // Arrange
        LocalDate fechaManual = LocalDate.of(2023, 6, 15);
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Test")
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        mensajeDto.setFechaCreacion(fechaManual);

        // Assert
        assertEquals(fechaManual, mensajeDto.getFechaCreacion(), "La fecha de creación debería ser la establecida manualmente");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        MensajeDto mensajeDto = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            mensajeDto.getNombreUsuario(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Validación - Debe manejar nombreUsuario en el límite de longitud (sin restricción explícita)")
    void validation_ShouldHandleNombreUsuarioAtBoundaries() {
        // Arrange - Nombre de usuario con diferentes longitudes
        String nombreCorto = "A";
        String nombreLargo = "A".repeat(255);

        MensajeDto mensajeDtoCorto = MensajeDto.builder()
                .nombreUsuario(nombreCorto)
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        MensajeDto mensajeDtoLargo = MensajeDto.builder()
                .nombreUsuario(nombreLargo)
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violationsCorto = validator.validate(mensajeDtoCorto);
        Set<ConstraintViolation<MensajeDto>> violationsLargo = validator.validate(mensajeDtoLargo);

        // Assert - No hay restricciones de tamaño para nombreUsuario (solo @NonNull)
        assertTrue(violationsCorto.isEmpty(), "No debería haber violaciones para nombre de usuario corto");
        assertTrue(violationsLargo.isEmpty(), "No debería haber violaciones para nombre de usuario largo");
    }

    @Test
    @DisplayName("Validación - Debe manejar email exactamente de 10 caracteres (mínimo)")
    void validation_ShouldHandleEmailAtMinimumLength() {
        // Arrange - Email exactamente de 10 caracteres
        String emailMinimo = "a@b.cdefg"; // 10 caracteres
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailMinimo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert - El email tiene exactamente 10 caracteres, debería ser válido
        assertTrue(violations.isEmpty(), "No debería haber violaciones para email exactamente de 10 caracteres");
        assertEquals(10, mensajeDto.getEmail().length(), "El email debería tener exactamente 10 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar email exactamente de 100 caracteres (máximo)")
    void validation_ShouldHandleEmailAtMaximumLength() {
        // Arrange - Email exactamente de 100 caracteres
        String emailMaximo = "a".repeat(90) + "@email.com"; // 90 + 10 = 100
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailMaximo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para email exactamente de 100 caracteres");
        assertEquals(100, mensajeDto.getEmail().length(), "El email debería tener exactamente 100 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar mensaje exactamente de 300 caracteres (máximo)")
    void validation_ShouldHandleMessageAtMaximumLength() {
        // Arrange - Mensaje exactamente de 300 caracteres
        String mensajeMaximo = "a".repeat(300);
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(mensajeMaximo)
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje exactamente de 300 caracteres");
        assertEquals(300, mensajeDto.getMensaje().length(), "El mensaje debería tener exactamente 300 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar mensaje con menos de 10 caracteres (sin restricción mínima)")
    void validation_ShouldHandleMessageWithLessThanTenCharacters() {
        // Arrange - Mensaje con menos de 10 caracteres (no hay restricción mínima)
        String mensajeCorto = "Corto";
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(mensajeCorto)
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert - No debería haber violaciones porque no hay restricción de tamaño mínimo
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje corto");
        assertEquals(mensajeCorto, mensajeDto.getMensaje(), "El mensaje corto debería mantenerse");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en todos los campos")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Texto con caracteres especiales y acentos
        String nombreEspecial = "José María ñáéíóú ÜÑ";
        String emailEspecial = "josé.maria@email.com";
        String mensajeEspecial = "Mensaje con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";

        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(nombreEspecial, mensajeDto.getNombreUsuario(), "El nombre con caracteres especiales debería mantenerse");
        assertEquals(emailEspecial, mensajeDto.getEmail(), "El email con caracteres especiales debería mantenerse");
        assertEquals(mensajeEspecial, mensajeDto.getMensaje(), "El mensaje con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Validación - Debe aceptar mensaje sin contenido (nulo)")
    void validation_ShouldAcceptNullMessage() {
        // Arrange - Mensaje nulo
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(null)
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje nulo");
        assertNull(mensajeDto.getMensaje(), "El mensaje debería ser nulo");
    }

    @Test
    @DisplayName("Validación - Debe aceptar mensaje vacío")
    void validation_ShouldAcceptEmptyMessage() {
        // Arrange - Mensaje vacío
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje("")
                .build();

        // Act
        Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje vacío");
        assertEquals("", mensajeDto.getMensaje(), "El mensaje debería estar vacío");
    }

    @Test
    @DisplayName("Validation - Debe aceptar emails con diferentes formatos válidos")
    void validation_ShouldAcceptEmailsWithDifferentValidFormats() {
        // Arrange - Emails con diferentes formatos válidos
        String[] emailsValidos = {
                "usuario@dominio.com",
                "usuario+test@dominio.com",
                "usuario.test@dominio.com",
                "usuario@sub.dominio.com",
                "usuario@dominio.co.uk",
                "usuario123@dominio.com",
                "usuario_123@dominio.com",
                "usuario-123@dominio.com",
                "123@dominio.com"
        };

        for (String email : emailsValidos) {
            MensajeDto mensajeDto = MensajeDto.builder()
                    .nombreUsuario("Usuario Válido")
                    .email(email)
                    .mensaje("Mensaje de prueba con más de 10 caracteres.")
                    .build();

            // Act
            Set<ConstraintViolation<MensajeDto>> violations = validator.validate(mensajeDto);

            // Assert
            assertTrue(violations.isEmpty(), "No debería haber violaciones para email válido: " + email);
        }
    }

    // ==================== TESTS DE TO STRING ====================

    @Test
    @DisplayName("toString - Debería incluir información relevante del MensajeDto")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        MensajeDto mensajeDto = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Act
        String toStringResult = mensajeDto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("id=1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Juan Pérez"), "toString debería contener el nombre de usuario");
        assertTrue(toStringResult.contains("juan@email.com"), "toString debería contener el email");
        assertTrue(toStringResult.contains("Mensaje de prueba"), "toString debería contener el mensaje");
        assertTrue(toStringResult.contains("fechaCreacion"), "toString debería contener la fecha de creación");
    }

    @Test
    @DisplayName("toString - Debería manejar valores nulos correctamente")
    void toString_ShouldHandleNullValuesCorrectly() {
        // Arrange
        MensajeDto mensajeDto = new MensajeDto();

        // Act
        String toStringResult = mensajeDto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("id=null"), "toString debería manejar id nulo");
        assertTrue(toStringResult.contains("nombreUsuario=null"), "toString debería manejar nombreUsuario nulo");
        assertTrue(toStringResult.contains("email=null"), "toString debería manejar email nulo");
        assertTrue(toStringResult.contains("mensaje=null"), "toString debería manejar mensaje nulo");
        assertTrue(toStringResult.contains("fechaCreacion=null"), "toString debería manejar fechaCreacion nulo");
    }

    // ==================== TEST DE CONSTRUCTOR CON PARÁMETROS ====================

    @Test
    @DisplayName("Constructor con parámetros debería establecer todos los campos correctamente")
    void constructorWithParameters_ShouldSetAllFieldsCorrectly() {
        // Arrange
        Integer id = 5;
        String nombreUsuario = "Constructor Test";
        String email = "constructor@test.com";
        String mensaje = "Mensaje de prueba desde constructor con más de 10 caracteres.";
        LocalDate fecha = LocalDate.now();

        // Act
        MensajeDto mensajeDto = new MensajeDto(id, nombreUsuario, email, mensaje, fecha);

        // Assert
        assertEquals(id, mensajeDto.getId(), "El ID debería ser el proporcionado");
        assertEquals(nombreUsuario, mensajeDto.getNombreUsuario(), "El nombre de usuario debería ser el proporcionado");
        assertEquals(email, mensajeDto.getEmail(), "El email debería ser el proporcionado");
        assertEquals(mensaje, mensajeDto.getMensaje(), "El mensaje debería ser el proporcionado");
        assertEquals(fecha, mensajeDto.getFechaCreacion(), "La fecha de creación debería ser la proporcionada");
    }

    // ==================== TEST DE BUILDER CON CAMPOS PARCIALES ====================

    @Test
    @DisplayName("Builder - Debería permitir construcción parcial")
    void builder_ShouldAllowPartialConstruction() {
        // Act - Construir solo con nombre y email
        MensajeDto mensajeDto1 = MensajeDto.builder()
                .nombreUsuario("Usuario Parcial")
                .email("parcial@email.com")
                .build();

        // Assert
        assertNotNull(mensajeDto1, "El objeto no debería ser nulo");
        assertEquals("Usuario Parcial", mensajeDto1.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("parcial@email.com", mensajeDto1.getEmail(), "El email debería coincidir");
        assertNull(mensajeDto1.getId(), "El ID debería ser nulo");
        assertNull(mensajeDto1.getMensaje(), "El mensaje debería ser nulo");
        assertNull(mensajeDto1.getFechaCreacion(), "La fecha de creación debería ser nula");

        // Act - Construir solo con nombre y mensaje
        MensajeDto mensajeDto2 = MensajeDto.builder()
                .nombreUsuario("Usuario Mensaje")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Assert
        assertNotNull(mensajeDto2, "El objeto no debería ser nulo");
        assertEquals("Usuario Mensaje", mensajeDto2.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("Mensaje de prueba con más de 10 caracteres.", mensajeDto2.getMensaje(), "El mensaje debería coincidir");
        assertNull(mensajeDto2.getId(), "El ID debería ser nulo");
        assertNull(mensajeDto2.getEmail(), "El email debería ser nulo");
        assertNull(mensajeDto2.getFechaCreacion(), "La fecha de creación debería ser nula");
    }

    // ==================== TEST DE DIFERENCIA ENTRE DTO Y ENTIDAD ====================

    @Test
    @DisplayName("MensajeDto debería ser un DTO independiente de la entidad Mensaje")
    void mensajeDto_ShouldBeIndependentFromEntity() {
        // Arrange
        MensajeDto mensajeDto = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Usuario DTO")
                .email("dto@email.com")
                .mensaje("Mensaje desde DTO con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        // Assert - Verificar que es un objeto DTO independiente
        assertNotNull(mensajeDto, "El DTO no debería ser nulo");
        assertTrue(mensajeDto.getClass().getSimpleName().contains("Dto"),
                "Debería ser una clase DTO");

        // Verificar que tiene las anotaciones de Lombok
        assertNotNull(mensajeDto.getId(), "El ID no debería ser nulo");
        assertNotNull(mensajeDto.getNombreUsuario(), "El nombre de usuario no debería ser nulo");
        assertNotNull(mensajeDto.getEmail(), "El email no debería ser nulo");
    }
}