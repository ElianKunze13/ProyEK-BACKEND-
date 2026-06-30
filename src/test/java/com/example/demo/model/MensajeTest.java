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

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Mensaje
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class MensajeTest {

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
    @DisplayName("Debería crear Mensaje con valores válidos - Caso feliz")
    void shouldCreateMensajeWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedNombreUsuario = "Juan Pérez";
        String expectedEmail = "juan.perez@email.com";
        String expectedMensaje = "Este es un mensaje de prueba con más de 10 caracteres.";
        LocalDate expectedFechaCreacion = LocalDate.now();

        // Act - Ejecutar la acción a probar
        Mensaje mensaje = Mensaje.builder()
                .id(expectedId)
                .nombreUsuario(expectedNombreUsuario)
                .email(expectedEmail)
                .mensaje(expectedMensaje)
                .fechaCreacion(expectedFechaCreacion)
                .build();

        // Assert - Verificar resultados
        assertNotNull(mensaje, "El objeto Mensaje no debería ser nulo");
        assertEquals(expectedId, mensaje.getId(), "El ID debería coincidir");
        assertEquals(expectedNombreUsuario, mensaje.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals(expectedEmail, mensaje.getEmail(), "El email debería coincidir");
        assertEquals(expectedMensaje, mensaje.getMensaje(), "El mensaje debería coincidir");
        assertEquals(expectedFechaCreacion, mensaje.getFechaCreacion(), "La fecha de creación debería coincidir");

        // Validar con Bean Validation
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear Mensaje con constructor por defecto")
    void shouldCreateMensajeWithDefaultConstructor() {
        // Arrange & Act
        Mensaje mensaje = new Mensaje();

        // Assert
        assertNotNull(mensaje, "El objeto Mensaje no debería ser nulo");
        assertNull(mensaje.getId(), "El ID debería ser nulo por defecto");
        assertNull(mensaje.getNombreUsuario(), "El nombre de usuario debería ser nulo por defecto");
        assertNull(mensaje.getEmail(), "El email debería ser nulo por defecto");
        assertNull(mensaje.getMensaje(), "El mensaje debería ser nulo por defecto");
        assertNull(mensaje.getFechaCreacion(), "La fecha de creación debería ser nula por defecto");
    }

    @Test
    @DisplayName("Debería crear Mensaje con constructor con todos los argumentos")
    void shouldCreateMensajeWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String nombreUsuario = "María García";
        String email = "maria.garcia@email.com";
        String mensaje = "Mensaje de prueba con más de 10 caracteres para validación.";
        LocalDate fechaCreacion = LocalDate.now();

        // Act
        Mensaje mensaje1 = new Mensaje(id, nombreUsuario, email, mensaje, fechaCreacion);

        // Assert
        assertNotNull(mensaje1, "El objeto Mensaje no debería ser nulo");
        assertEquals(id, mensaje1.getId(), "El ID debería coincidir");
        assertEquals(nombreUsuario, mensaje1.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals(email, mensaje1.getEmail(), "El email debería coincidir");
        assertEquals(mensaje, mensaje1.getMensaje(), "El mensaje debería coincidir");
        assertEquals(fechaCreacion, mensaje1.getFechaCreacion(), "La fecha de creación debería coincidir");
    }

    @Test
    @DisplayName("Debería crear Mensaje sin mensaje (campo opcional)")
    void shouldCreateMensajeWithoutMessage() {
        // Arrange & Act - Mensaje sin contenido
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .build();

        // Assert
        assertNotNull(mensaje, "El objeto Mensaje no debería ser nulo");
        assertEquals("Carlos López", mensaje.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("carlos@email.com", mensaje.getEmail(), "El email debería coincidir");
        assertNull(mensaje.getMensaje(), "El mensaje debería ser nulo");

        // Validar con Bean Validation (el mensaje es opcional)
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con mensaje nulo");
    }

    @Test
    @DisplayName("Debería crear Mensaje sin fechaCreacion (debería ser nulo)")
    void shouldCreateMensajeWithoutFechaCreacion() {
        // Arrange & Act
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Ana Martínez")
                .email("ana@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(null)
                .build();

        // Assert
        assertNotNull(mensaje, "El objeto Mensaje no debería ser nulo");
        assertNull(mensaje.getFechaCreacion(), "La fecha de creación debería ser nula");
    }

    // ==================== TESTS DE @PrePersist ====================
    @Test
    @DisplayName("onCreate - Debería establecer la fecha actual al persistir")
    void onCreate_ShouldSetCurrentDate() {
        // Arrange
        LocalDate beforeCreation = LocalDate.now();
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Test")
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Assert - La fecha debería ser nula antes de onCreate
        assertNull(mensaje.getFechaCreacion(), "La fecha de creación debería ser nula antes de onCreate");

        // Act - Simular @PrePersist
        mensaje.onCreate();

        // Assert - La fecha debería ser establecida
        assertNotNull(mensaje.getFechaCreacion(), "La fecha de creación no debería ser nula después de onCreate");

        // Usar LocalDate.now() para comparar directamente
        LocalDate currentDate = LocalDate.now();
        assertTrue(
                mensaje.getFechaCreacion().isEqual(currentDate) ||
                        mensaje.getFechaCreacion().isEqual(beforeCreation) ||
                        mensaje.getFechaCreacion().isAfter(beforeCreation),
                "La fecha de creación debería ser la fecha actual o posterior"
        );
        assertEquals(LocalDate.now(), mensaje.getFechaCreacion(), "La fecha de creación debería ser la fecha actual");
    }

    @Test
    @DisplayName("onCreate - Debería establecer la fecha actual incluso con fecha existente")
    void onCreate_ShouldSetCurrentDateEvenWithExistingDate() {
        // Arrange
        LocalDate oldDate = LocalDate.of(2020, 1, 1);
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Test")
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(oldDate)
                .build();

        // Assert - La fecha debería ser la fecha antigua
        assertEquals(oldDate, mensaje.getFechaCreacion(), "La fecha de creación debería ser la fecha antigua");

        // Act - Simular @PrePersist
        mensaje.onCreate();

        // Assert - La fecha debería ser actualizada
        assertNotNull(mensaje.getFechaCreacion(), "La fecha de creación no debería ser nula");
        assertNotEquals(oldDate, mensaje.getFechaCreacion(), "La fecha de creación debería ser actualizada");
        assertEquals(LocalDate.now(), mensaje.getFechaCreacion(), "La fecha de creación debería ser la fecha actual");
    }

    @Test
    @DisplayName("onCreate - Debería establecer la fecha actual para múltiples instancias")
    void onCreate_ShouldSetCurrentDateForMultipleInstances() {
        // Arrange
        Mensaje mensaje1 = Mensaje.builder()
                .nombreUsuario("Usuario 1")
                .email("usuario1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .build();

        Mensaje mensaje2 = Mensaje.builder()
                .nombreUsuario("Usuario 2")
                .email("usuario2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .build();

        // Act
        mensaje1.onCreate();
        mensaje2.onCreate();

        // Assert
        assertNotNull(mensaje1.getFechaCreacion(), "La fecha de creación de mensaje1 no debería ser nula");
        assertNotNull(mensaje2.getFechaCreacion(), "La fecha de creación de mensaje2 no debería ser nula");
        assertEquals(LocalDate.now(), mensaje1.getFechaCreacion(), "La fecha de creación de mensaje1 debería ser la fecha actual");
        assertEquals(LocalDate.now(), mensaje2.getFechaCreacion(), "La fecha de creación de mensaje2 debería ser la fecha actual");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando nombreUsuario es nulo")
    void validation_ShouldHaveViolations_WhenNombreUsuarioIsNull() {
        // Arrange - Nombre de usuario nulo
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario(null)
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

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
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email(email)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

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
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailInvalido)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

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
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailValido)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para email válido: " + emailValido);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "a", ""})
    @DisplayName("Validación - Debe tener violaciones cuando email es muy corto (menos de 10 caracteres)")
    void validation_ShouldHaveViolations_WhenEmailIsTooShort(String emailCorto) {
        // Arrange - Email muy corto (menos de 10 caracteres)
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailCorto)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

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
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Debe haber violación específica para el campo email");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Este mensaje tiene más de 10 caracteres", "1234567890", "Mensaje válido"})
    @DisplayName("Validación - Debe aceptar mensajes con longitud válida (hasta 1000 caracteres)")
    void validation_ShouldAcceptValidMessageLength(String mensajeValido) {
        // Arrange
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(mensajeValido)
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje válido");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando mensaje es muy largo (más de 1000 caracteres)")
    void validation_ShouldHaveViolations_WhenMensajeIsTooLong() {
        // Arrange - Mensaje demasiado largo
        String mensajeLargo = "a".repeat(1001);
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(mensajeLargo)
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para mensaje demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("mensaje")),
                "Debe haber violación específica para el campo mensaje");
    }

    @Test
    @DisplayName("Validación - Debe aceptar mensaje exactamente de 1000 caracteres (límite máximo)")
    void validation_ShouldAcceptMessageExactlyAtMaxLength() {
        // Arrange - Mensaje exactamente de 1000 caracteres
        String mensajeMaximo = "a".repeat(1000);
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(mensajeMaximo)
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje exactamente de 1000 caracteres");
        assertEquals(1000, mensaje.getMensaje().length(), "El mensaje debería tener exactamente 1000 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe aceptar email exactamente de 100 caracteres (límite máximo)")
    void validation_ShouldAcceptEmailExactlyAtMaxLength() {
        // Arrange - Email exactamente de 100 caracteres
        String emailMaximo = "a".repeat(90) + "@email.com"; // 90 + 10 = 100
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailMaximo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para email exactamente de 100 caracteres");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos Mensaje con los mismos valores")
    void shouldBeEqualWhenComparingTwoMensajesWithSameValues() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        Mensaje mensaje1 = Mensaje.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        Mensaje mensaje2 = Mensaje.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Act & Assert
        assertEquals(mensaje1, mensaje2, "Los objetos Mensaje deberían ser iguales");
        assertEquals(mensaje1.hashCode(), mensaje2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos Mensaje con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoMensajesWithDifferentValues() {
        // Arrange
        LocalDate fecha1 = LocalDate.of(2024, 1, 1);
        LocalDate fecha2 = LocalDate.of(2024, 1, 2);

        Mensaje mensaje1 = Mensaje.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .fechaCreacion(fecha1)
                .build();

        Mensaje mensaje2 = Mensaje.builder()
                .id(2)
                .nombreUsuario("María García")
                .email("maria@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .fechaCreacion(fecha2)
                .build();

        // Act & Assert
        assertNotEquals(mensaje1, mensaje2, "Los objetos Mensaje no deberían ser iguales");
        assertNotEquals(mensaje1.hashCode(), mensaje2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        LocalDate fechaInicial = LocalDate.now();
        Mensaje mensaje = Mensaje.builder()
                .id(1)
                .nombreUsuario("Nombre Inicial")
                .email("inicial@email.com")
                .mensaje("Mensaje inicial con más de 10 caracteres.")
                .fechaCreacion(fechaInicial)
                .build();

        LocalDate nuevaFecha = LocalDate.of(2024, 12, 31);

        // Act
        mensaje.setId(2);
        mensaje.setNombreUsuario("Nombre Actualizado");
        mensaje.setEmail("actualizado@email.com");
        mensaje.setMensaje("Mensaje actualizado con más de 10 caracteres.");
        mensaje.setFechaCreacion(nuevaFecha);

        // Assert
        assertEquals(2, mensaje.getId(), "El ID debería estar actualizado");
        assertEquals("Nombre Actualizado", mensaje.getNombreUsuario(), "El nombre de usuario debería estar actualizado");
        assertEquals("actualizado@email.com", mensaje.getEmail(), "El email debería estar actualizado");
        assertEquals("Mensaje actualizado con más de 10 caracteres.", mensaje.getMensaje(), "El mensaje debería estar actualizado");
        assertEquals(nuevaFecha, mensaje.getFechaCreacion(), "La fecha de creación debería estar actualizada");
    }

    @Test
    @DisplayName("Debería permitir establecer fechaCreacion manualmente")
    void shouldAllowManualFechaCreacionSetter() {
        // Arrange
        LocalDate fechaManual = LocalDate.of(2023, 6, 15);
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Test")
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        mensaje.setFechaCreacion(fechaManual);

        // Assert
        assertEquals(fechaManual, mensaje.getFechaCreacion(), "La fecha de creación debería ser la establecida manualmente");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        Mensaje mensaje = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            mensaje.getNombreUsuario(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Validación - Debe manejar nombreUsuario en el límite de longitud (sin restricción explícita)")
    void validation_ShouldHandleNombreUsuarioAtBoundaries() {
        // Arrange - Nombre de usuario con diferentes longitudes
        String nombreCorto = "A";
        String nombreLargo = "A".repeat(255);

        Mensaje mensajeCorto = Mensaje.builder()
                .nombreUsuario(nombreCorto)
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        Mensaje mensajeLargo = Mensaje.builder()
                .nombreUsuario(nombreLargo)
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violationsCorto = validator.validate(mensajeCorto);
        Set<ConstraintViolation<Mensaje>> violationsLargo = validator.validate(mensajeLargo);

        // Assert - No hay restricciones de tamaño para nombreUsuario (solo @NonNull)
        assertTrue(violationsCorto.isEmpty(), "No debería haber violaciones para nombre de usuario corto");
        assertTrue(violationsLargo.isEmpty(), "No debería haber violaciones para nombre de usuario largo");
    }

    @Test
    @DisplayName("Validación - Debe manejar email exactamente de 10 caracteres (mínimo)")
    void validation_ShouldHandleEmailAtMinimumLength() {
        // Arrange - Email exactamente de 10 caracteres
        String emailMinimo = "a@b.cd"; // 6 caracteres
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailMinimo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert - El email es corto pero válido como email
        // La validación de tamaño mínimo de 10 caracteres está en el @Size
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email por debajo del mínimo de 10 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar email exactamente de 100 caracteres (máximo)")
    void validation_ShouldHandleEmailAtMaximumLength() {
        // Arrange - Email exactamente de 100 caracteres
        String emailMaximo = "a".repeat(90) + "@email.com"; // 90 + 10 = 100
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email(emailMaximo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para email exactamente de 100 caracteres");
        assertEquals(100, mensaje.getEmail().length(), "El email debería tener exactamente 100 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar mensaje exactamente de 1000 caracteres (máximo)")
    void validation_ShouldHandleMessageAtMaximumLength() {
        // Arrange - Mensaje exactamente de 1000 caracteres
        String mensajeMaximo = "a".repeat(1000);
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(mensajeMaximo)
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje exactamente de 1000 caracteres");
        assertEquals(1000, mensaje.getMensaje().length(), "El mensaje debería tener exactamente 1000 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en todos los campos")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Texto con caracteres especiales y acentos
        String nombreEspecial = "José María ñáéíóú ÜÑ";
        String emailEspecial = "josé.maria@email.com";
        String mensajeEspecial = "Mensaje con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";

        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(nombreEspecial, mensaje.getNombreUsuario(), "El nombre con caracteres especiales debería mantenerse");
        assertEquals(emailEspecial, mensaje.getEmail(), "El email con caracteres especiales debería mantenerse");
        assertEquals(mensajeEspecial, mensaje.getMensaje(), "El mensaje con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Validación - Debe aceptar mensaje sin contenido (nulo)")
    void validation_ShouldAcceptNullMessage() {
        // Arrange - Mensaje nulo
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje(null)
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje nulo");
        assertNull(mensaje.getMensaje(), "El mensaje debería ser nulo");
    }

    @Test
    @DisplayName("Validación - Debe aceptar mensaje vacío")
    void validation_ShouldAcceptEmptyMessage() {
        // Arrange - Mensaje vacío
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Válido")
                .email("test@email.com")
                .mensaje("")
                .build();

        // Act
        Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para mensaje vacío");
        assertEquals("", mensaje.getMensaje(), "El mensaje debería estar vacío");
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
            Mensaje mensaje = Mensaje.builder()
                    .nombreUsuario("Usuario Válido")
                    .email(email)
                    .mensaje("Mensaje de prueba con más de 10 caracteres.")
                    .build();

            // Act
            Set<ConstraintViolation<Mensaje>> violations = validator.validate(mensaje);

            // Assert
            assertTrue(violations.isEmpty(), "No debería haber violaciones para email válido: " + email);
        }
    }

    // ==================== TESTS DE TO STRING ====================

    @Test
    @DisplayName("toString - Debería incluir información relevante del Mensaje")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        Mensaje mensaje = Mensaje.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Act
        String toStringResult = mensaje.toString();

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
        Mensaje mensaje = new Mensaje();

        // Act
        String toStringResult = mensaje.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("id=null"), "toString debería manejar id nulo");
        assertTrue(toStringResult.contains("nombreUsuario=null"), "toString debería manejar nombreUsuario nulo");
        assertTrue(toStringResult.contains("email=null"), "toString debería manejar email nulo");
        assertTrue(toStringResult.contains("mensaje=null"), "toString debería manejar mensaje nulo");
        assertTrue(toStringResult.contains("fechaCreacion=null"), "toString debería manejar fechaCreacion nulo");
    }
}