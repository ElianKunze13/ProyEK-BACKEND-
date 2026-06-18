package com.example.demo.model;

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
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Conocimiento
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class ConocimientoTest {

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
    @DisplayName("Debería crear Conocimiento con valores válidos - Caso feliz")
    void shouldCreateConocimientoWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedNombre = "Spring Boot";
        Nivel expectedNivel = Nivel.ALTO;
        TipoConocimiento expectedTipoConocimiento = TipoConocimiento.BACKEND;
        Imagen expectedImagen = Imagen.builder()
                .url("spring-boot-icon.png")
                .alt("Spring Boot Logo")
                .build();
        Usuario expectedUsuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("password123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        // Act - Ejecutar la acción a probar
        Conocimiento conocimiento = Conocimiento.builder()
                .id(expectedId)
                .nombre(expectedNombre)
                .nivel(expectedNivel)
                .tipoConocimiento(expectedTipoConocimiento)
                .imagen(expectedImagen)
                .usuario(expectedUsuario)
                .build();

        // Assert - Verificar resultados
        assertNotNull(conocimiento, "El objeto Conocimiento no debería ser nulo");
        assertEquals(expectedId, conocimiento.getId(), "El ID debería coincidir");
        assertEquals(expectedNombre, conocimiento.getNombre(), "El nombre debería coincidir");
        assertEquals(expectedNivel, conocimiento.getNivel(), "El nivel debería coincidir");
        assertEquals(expectedTipoConocimiento, conocimiento.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNotNull(conocimiento.getImagen(), "La imagen no debería ser nula");
        assertEquals(expectedImagen.getUrl(), conocimiento.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertNotNull(conocimiento.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(expectedUsuario.getId(), conocimiento.getUsuario().getId(), "El ID del usuario debería coincidir");

        // Validar con Bean Validation
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear Conocimiento con constructor por defecto")
    void shouldCreateConocimientoWithDefaultConstructor() {
        // Arrange & Act
        Conocimiento conocimiento = new Conocimiento();

        // Assert
        assertNotNull(conocimiento, "El objeto Conocimiento no debería ser nulo");
        assertNull(conocimiento.getId(), "El ID debería ser nulo por defecto");
        assertNull(conocimiento.getNombre(), "El nombre debería ser nulo por defecto");
        assertNull(conocimiento.getNivel(), "El nivel debería ser nulo por defecto");
        assertNull(conocimiento.getTipoConocimiento(), "El tipo de conocimiento debería ser nulo por defecto");
        assertNull(conocimiento.getImagen(), "La imagen debería ser nula por defecto");
        assertNull(conocimiento.getUsuario(), "El usuario debería ser nulo por defecto");
    }

    @Test
    @DisplayName("Debería crear Conocimiento con constructor con todos los argumentos")
    void shouldCreateConocimientoWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String nombre = "Angular";
        Nivel nivel = Nivel.AVANZADO;
        TipoConocimiento tipoConocimiento = TipoConocimiento.FRONTEND;
        Imagen imagen = Imagen.builder()
                .url("angular-icon.png")
                .alt("Angular Logo")
                .build();
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("María García")
                .username("maria@email.com")
                .password("password456")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(com.example.demo.enums.Role.ADMIN)
                .build();

        // Act
        Conocimiento conocimiento = new Conocimiento(id, nombre, nivel, tipoConocimiento, imagen, usuario);

        // Assert
        assertNotNull(conocimiento, "El objeto Conocimiento no debería ser nulo");
        assertEquals(id, conocimiento.getId(), "El ID debería coincidir");
        assertEquals(nombre, conocimiento.getNombre(), "El nombre debería coincidir");
        assertEquals(nivel, conocimiento.getNivel(), "El nivel debería coincidir");
        assertEquals(tipoConocimiento, conocimiento.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNotNull(conocimiento.getImagen(), "La imagen no debería ser nula");
        assertNotNull(conocimiento.getUsuario(), "El usuario no debería ser nulo");
    }

    @Test
    @DisplayName("Debería permitir valores nulos en campos opcionales - Imagen y Usuario")
    void shouldAllowNullValuesInOptionalFields() {
        // Arrange & Act - Conocimiento sin imagen y sin usuario
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .usuario(null)
                .build();

        // Assert
        assertNotNull(conocimiento, "El objeto Conocimiento no debería ser nulo");
        assertNull(conocimiento.getImagen(), "La imagen debería ser nula");
        assertNull(conocimiento.getUsuario(), "El usuario debería ser nulo");

        // Validar que no hay violaciones (ya que imagen y usuario son opcionales)
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con imagen y usuario nulos");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando nombre es nulo")
    void validation_ShouldHaveViolations_WhenNombreIsNull() {
        // Arrange - Nombre nulo
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre(null)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // Act
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);

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
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre(nombre)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // Act
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ab", "12", "A"})
    @DisplayName("Validación - Debe tener violaciones cuando nombre es muy corto (menos de 3 caracteres)")
    void validation_ShouldHaveViolations_WhenNombreIsTooShort(String nombre) {
        // Arrange - Nombre muy corto (menos de 3 caracteres)
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre(nombre)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // Act
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre demasiado corto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Un nombre muy largo que supera los 145 caracteres es inválido para este test porque excede el límite máximo permitido por la anotación Size en la entidad Conocimiento"})
    @DisplayName("Validación - Debe tener violaciones cuando nombre es muy largo (más de 145 caracteres)")
    void validation_ShouldHaveViolations_WhenNombreIsTooLong(String nombreLargo) {
        // Arrange - Nombre demasiado largo
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre(nombreLargo)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // Act
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando nivel es nulo")
    void validation_ShouldHaveViolations_WhenNivelIsNull() {
        // Arrange - Nivel nulo
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("JavaScript")
                .nivel(null)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        // Act
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nivel nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nivel")),
                "Debe haber violación específica para el campo nivel");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando tipoConocimiento es nulo")
    void validation_ShouldHaveViolations_WhenTipoConocimientoIsNull() {
        // Arrange - TipoConocimiento nulo
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Python")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(null)
                .build();

        // Act
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para tipoConocimiento nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoConocimiento")),
                "Debe haber violación específica para el campo tipoConocimiento");
    }

    // ==================== TESTS DE ENUMS ====================

    @Test
    @DisplayName("Debe manejar todos los niveles del enum Nivel")
    void shouldHandleAllNiveles() {
        // Arrange - Probar todos los niveles
        Nivel[] niveles = Nivel.values();

        for (Nivel nivel : niveles) {
            // Act
            Conocimiento conocimiento = Conocimiento.builder()
                    .nombre("Tecnología Test")
                    .nivel(nivel)
                    .tipoConocimiento(TipoConocimiento.OTROS)
                    .build();

            // Assert
            assertNotNull(conocimiento, "El Conocimiento no debería ser nulo para nivel: " + nivel);
            assertEquals(nivel, conocimiento.getNivel(), "Debería manejar correctamente el nivel: " + nivel);
        }
    }

    @Test
    @DisplayName("Debe manejar todos los tipos de conocimiento del enum TipoConocimiento")
    void shouldHandleAllTipoConocimientos() {
        // Arrange - Probar todos los tipos de conocimiento
        TipoConocimiento[] tipos = TipoConocimiento.values();

        for (TipoConocimiento tipo : tipos) {
            // Act
            Conocimiento conocimiento = Conocimiento.builder()
                    .nombre("Tecnología Test")
                    .nivel(Nivel.INTERMEDIO)
                    .tipoConocimiento(tipo)
                    .build();

            // Assert
            assertNotNull(conocimiento, "El Conocimiento no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, conocimiento.getTipoConocimiento(), "Debería manejar correctamente el tipo: " + tipo);
        }
    }

    // ==================== TESTS DE RELACIONES ====================

    @Test
    @DisplayName("Debe manejar relación OneToOne con Imagen correctamente")
    void shouldHandleOneToOneWithImagenCorrectly() {
        // Arrange
        Imagen imagen = Imagen.builder()
                .url("test-icon.png")
                .alt("Test Icon")
                .build();

        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Node.js")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertNotNull(conocimiento.getImagen(), "La imagen no debería ser nula");
        assertEquals("test-icon.png", conocimiento.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Test Icon", conocimiento.getImagen().getAlt(), "El alt de la imagen debería coincidir");

        // Verificar que la relación bidireccional se mantiene
        // Nota: Imagen tiene @OneToOne(mappedBy = "imagen") para la relación con Conocimiento
        assertNotNull(imagen.getConocimiento(), "El conocimiento en la imagen no debería ser nulo");
        assertEquals(conocimiento, imagen.getConocimiento(), "La relación bidireccional debería ser consistente");
    }

    @Test
    @DisplayName("Debe manejar relación ManyToOne con Usuario correctamente")
    void shouldHandleManyToOneWithUsuarioCorrectly() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Usuario Test")
                .username("test@email.com")
                .password("password123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .usuario(usuario)
                .build();

        // Act & Assert
        assertNotNull(conocimiento.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(1, conocimiento.getUsuario().getId(), "El ID del usuario debería coincidir");
        assertEquals("Usuario Test", conocimiento.getUsuario().getNombre(), "El nombre del usuario debería coincidir");
    }

    @Test
    @DisplayName("Debe manejar la creación de Conocimiento con imagen embebida sin usuario")
    void shouldHandleConocimientoWithEmbeddedImagenWithoutUsuario() {
        // Arrange - Conocimiento solo con imagen, sin usuario
        Imagen imagen = Imagen.builder()
                .url("test-img.png")
                .alt("Test Image")
                .build();

        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Docker")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(imagen)
                .usuario(null)
                .build();

        // Act
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);

        // Assert
        assertNotNull(conocimiento, "El Conocimiento no debería ser nulo");
        assertNotNull(conocimiento.getImagen(), "La imagen no debería ser nula");
        assertNull(conocimiento.getUsuario(), "El usuario debería ser nulo");
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos Conocimiento con los mismos valores")
    void shouldBeEqualWhenComparingTwoConocimientosWithSameValues() {
        // Arrange
        Imagen imagen = Imagen.builder()
                .url("icon.png")
                .alt("Icon")
                .build();

        Conocimiento conocimiento1 = Conocimiento.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        Conocimiento conocimiento2 = Conocimiento.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertEquals(conocimiento1, conocimiento2, "Los objetos Conocimiento deberían ser iguales");
        assertEquals(conocimiento1.hashCode(), conocimiento2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos Conocimiento con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoConocimientosWithDifferentValues() {
        // Arrange
        Conocimiento conocimiento1 = Conocimiento.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        Conocimiento conocimiento2 = Conocimiento.builder()
                .id(2)
                .nombre("Python")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // Act & Assert
        assertNotEquals(conocimiento1, conocimiento2, "Los objetos Conocimiento no deberían ser iguales");
        assertNotEquals(conocimiento1.hashCode(), conocimiento2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        Conocimiento conocimiento = Conocimiento.builder()
                .id(1)
                .nombre("Nombre Inicial")
                .nivel(Nivel.PRINCIPIANTE_BASICO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .build();

        Imagen nuevaImagen = Imagen.builder()
                .url("nueva-imagen.png")
                .alt("Nueva Imagen")
                .build();

        Usuario nuevoUsuario = Usuario.builder()
                .id(2)
                .nombre("Nuevo Usuario")
                .username("nuevo@email.com")
                .password("password789")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(com.example.demo.enums.Role.ADMIN)
                .build();

        // Act
        conocimiento.setId(2);
        conocimiento.setNombre("Nombre Actualizado");
        conocimiento.setNivel(Nivel.AVANZADO);
        conocimiento.setTipoConocimiento(TipoConocimiento.FRONTEND);
        conocimiento.setImagen(nuevaImagen);
        conocimiento.setUsuario(nuevoUsuario);

        // Assert
        assertEquals(2, conocimiento.getId(), "El ID debería estar actualizado");
        assertEquals("Nombre Actualizado", conocimiento.getNombre(), "El nombre debería estar actualizado");
        assertEquals(Nivel.AVANZADO, conocimiento.getNivel(), "El nivel debería estar actualizado");
        assertEquals(TipoConocimiento.FRONTEND, conocimiento.getTipoConocimiento(), "El tipo de conocimiento debería estar actualizado");
        assertNotNull(conocimiento.getImagen(), "La imagen no debería ser nula");
        assertEquals("nueva-imagen.png", conocimiento.getImagen().getUrl(), "La URL de la imagen debería estar actualizada");
        assertNotNull(conocimiento.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(2, conocimiento.getUsuario().getId(), "El ID del usuario debería estar actualizado");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        Conocimiento conocimiento = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            conocimiento.getNombre(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Validación - Debe manejar nombres en el límite de longitud (3 y 145 caracteres)")
    void validation_ShouldHandleNombreAtLengthBoundaries() {
        // Arrange - Nombre exactamente de 3 caracteres (mínimo)
        String nombreMinimo = "ABC";
        Conocimiento conocimientoMinimo = Conocimiento.builder()
                .nombre(nombreMinimo)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .build();

        // Act - Validar nombre mínimo
        Set<ConstraintViolation<Conocimiento>> violationsMinimo = validator.validate(conocimientoMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para nombre exactamente de 3 caracteres");
        assertEquals(3, conocimientoMinimo.getNombre().length(), "El nombre debería tener exactamente 3 caracteres");

        // Arrange - Nombre exactamente de 145 caracteres (máximo)
        String nombreMaximo = "A".repeat(145);
        Conocimiento conocimientoMaximo = Conocimiento.builder()
                .nombre(nombreMaximo)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .build();

        // Act - Validar nombre máximo
        Set<ConstraintViolation<Conocimiento>> violationsMaximo = validator.validate(conocimientoMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para nombre exactamente de 145 caracteres");
        assertEquals(145, conocimientoMaximo.getNombre().length(), "El nombre debería tener exactamente 145 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en el nombre")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Nombre con caracteres especiales y acentos
        String nombreEspecial = "José María ñáéíóú ÜÑ y caracteres !@#$%^&*()";

        Conocimiento conocimiento = Conocimiento.builder()
                .nombre(nombreEspecial)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .build();

        // Act
        Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(nombreEspecial, conocimiento.getNombre(), "El nombre con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Debe manejar la creación de Conocimiento con combinaciones de enums")
    void shouldHandleAllCombinationsOfEnums() {
        // Arrange - Probar todas las combinaciones de nivel y tipo
        Nivel[] niveles = Nivel.values();
        TipoConocimiento[] tipos = TipoConocimiento.values();

        for (Nivel nivel : niveles) {
            for (TipoConocimiento tipo : tipos) {
                // Act
                Conocimiento conocimiento = Conocimiento.builder()
                        .nombre("Test " + nivel + " " + tipo)
                        .nivel(nivel)
                        .tipoConocimiento(tipo)
                        .build();

                // Assert
                assertNotNull(conocimiento, "El Conocimiento no debería ser nulo para nivel: " + nivel + " y tipo: " + tipo);
                assertEquals(nivel, conocimiento.getNivel(), "El nivel debería ser: " + nivel);
                assertEquals(tipo, conocimiento.getTipoConocimiento(), "El tipo debería ser: " + tipo);

                // Validar que no hay violaciones
                Set<ConstraintViolation<Conocimiento>> violations = validator.validate(conocimiento);
                assertTrue(violations.isEmpty(), "No debería haber violaciones para nivel: " + nivel + " y tipo: " + tipo);
            }
        }
    }

    @Test
    @DisplayName("Debe manejar Conocimiento con imagen sin URL (caso borde)")
    void shouldHandleConocimientoWithImagenWithoutUrl() {
        // Arrange - Imagen sin URL (viola @NonNull en Imagen)
        Imagen imagenSinUrl = Imagen.builder()
                .url(null) // Esto debería lanzar NullPointerException debido a @NonNull
                .alt("Imagen sin URL")
                .build();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            Conocimiento.builder()
                    .nombre("Test")
                    .nivel(Nivel.INTERMEDIO)
                    .tipoConocimiento(TipoConocimiento.OTROS)
                    .imagen(imagenSinUrl)
                    .build();
        }, "Debería lanzar NullPointerException al crear Conocimiento con imagen sin URL");
    }

    @Test
    @DisplayName("Debe manejar Conocimiento con imagen sin alt (caso borde)")
    void shouldHandleConocimientoWithImagenWithoutAlt() {
        // Arrange - Imagen sin alt (viola @NonNull en Imagen)
        Imagen imagenSinAlt = Imagen.builder()
                .url("test.png")
                .alt(null) // Esto debería lanzar NullPointerException debido a @NonNull
                .build();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            Conocimiento.builder()
                    .nombre("Test")
                    .nivel(Nivel.INTERMEDIO)
                    .tipoConocimiento(TipoConocimiento.OTROS)
                    .imagen(imagenSinAlt)
                    .build();
        }, "Debería lanzar NullPointerException al crear Conocimiento con imagen sin alt");
    }

    @Test
    @DisplayName("toString debería incluir información relevante del Conocimiento")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        Conocimiento conocimiento = Conocimiento.builder()
                .id(1)
                .nombre("Spring Boot")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // Act
        String toStringResult = conocimiento.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Spring Boot"), "toString debería contener el nombre");
        assertTrue(toStringResult.contains("ALTO"), "toString debería contener el nivel");
        assertTrue(toStringResult.contains("BACKEND"), "toString debería contener el tipo de conocimiento");
    }
}