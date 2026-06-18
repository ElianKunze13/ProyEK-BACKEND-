package com.example.demo.model;

import com.example.demo.dto.ImagenDto;
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
 * Pruebas unitarias para la clase Usuario
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class UsuarioTest {

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
    @DisplayName("Debería crear Usuario con valores válidos - Caso feliz")
    void shouldCreateUsuarioWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedNombre = "Juan Pérez";
        String expectedUsername = "juan.perez@email.com";
        String expectedPassword = "password123";
        String expectedIntroduccion = "Desarrollador Full Stack con 5 años de experiencia";
        String expectedDescripcion = "Apasionado por la tecnología y el desarrollo de software.";
        Role expectedRol = Role.USER;

        // Act - Ejecutar la acción a probar
        Usuario usuario = Usuario.builder()
                .id(expectedId)
                .nombre(expectedNombre)
                .username(expectedUsername)
                .password(expectedPassword)
                .introduccion(expectedIntroduccion)
                .descripcion(expectedDescripcion)
                .rol(expectedRol)
                .active(true)
                .build();

        // Assert - Verificar resultados
        assertNotNull(usuario, "El objeto Usuario no debería ser nulo");
        assertEquals(expectedId, usuario.getId(), "El ID debería coincidir");
        assertEquals(expectedNombre, usuario.getNombre(), "El nombre debería coincidir");
        assertEquals(expectedUsername, usuario.getUsername(), "El username debería coincidir");
        assertEquals(expectedPassword, usuario.getPassword(), "La contraseña debería coincidir");
        assertEquals(expectedIntroduccion, usuario.getIntroduccion(), "La introducción debería coincidir");
        assertEquals(expectedDescripcion, usuario.getDescripcion(), "La descripción debería coincidir");
        assertEquals(expectedRol, usuario.getRol(), "El rol debería coincidir");
        assertTrue(usuario.isActive(), "El usuario debería estar activo");

        // Validar con Bean Validation
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear Usuario con constructor por defecto")
    void shouldCreateUsuarioWithDefaultConstructor() {
        // Arrange & Act
        Usuario usuario = new Usuario();

        // Assert
        assertNotNull(usuario, "El objeto Usuario no debería ser nulo");
        assertNull(usuario.getId(), "El ID debería ser nulo por defecto");
        assertNull(usuario.getNombre(), "El nombre debería ser nulo por defecto");
        assertNull(usuario.getUsername(), "El username debería ser nulo por defecto");
        assertNull(usuario.getPassword(), "La contraseña debería ser nula por defecto");
        assertNull(usuario.getIntroduccion(), "La introducción debería ser nula por defecto");
        assertNull(usuario.getDescripcion(), "La descripción debería ser nula por defecto");
        assertNull(usuario.getRol(), "El rol debería ser nulo por defecto");
        assertNull(usuario.getFotoPerfil(), "La foto de perfil debería ser nula por defecto");
        assertNull(usuario.getFotoPortada(), "La foto de portada debería ser nula por defecto");
    }

    @Test
    @DisplayName("Debería crear Usuario con constructor con todos los argumentos")
    void shouldCreateUsuarioWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String nombre = "María García";
        String username = "maria.garcia@email.com";
        String password = "password456";
        String introduccion = "Desarrolladora Frontend con 3 años de experiencia";
        String descripcion = "Especializada en Angular y React.";
        Role rol = Role.ADMIN;
        Imagen fotoPerfil = Imagen.builder().url("perfil.jpg").alt("Foto de perfil").build();
        Imagen fotoPortada = Imagen.builder().url("portada.jpg").alt("Foto de portada").build();

        // Act
        Usuario usuario = new Usuario(id, nombre, username, password, introduccion, descripcion, rol, fotoPerfil, fotoPortada, true);

        // Assert
        assertNotNull(usuario, "El objeto Usuario no debería ser nulo");
        assertEquals(id, usuario.getId(), "El ID debería coincidir");
        assertEquals(nombre, usuario.getNombre(), "El nombre debería coincidir");
        assertEquals(username, usuario.getUsername(), "El username debería coincidir");
        assertEquals(password, usuario.getPassword(), "La contraseña debería coincidir");
        assertEquals(introduccion, usuario.getIntroduccion(), "La introducción debería coincidir");
        assertEquals(descripcion, usuario.getDescripcion(), "La descripción debería coincidir");
        assertEquals(rol, usuario.getRol(), "El rol debería coincidir");
        assertNotNull(usuario.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertNotNull(usuario.getFotoPortada(), "La foto de portada no debería ser nula");
        assertTrue(usuario.isActive(), "El usuario debería estar activo");
    }

    @Test
    @DisplayName("Debería permitir valores nulos en campos opcionales - Imágenes")
    void shouldAllowNullValuesInOptionalFields() {
        // Arrange & Act - Usuario sin imágenes
        Usuario usuario = Usuario.builder()
                .nombre("Carlos López")
                .username("carlos@email.com")
                .password("password789")
                .introduccion("Introducción válida para prueba")
                .descripcion("Descripción válida para prueba")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .build();

        // Assert
        assertNotNull(usuario, "El objeto Usuario no debería ser nulo");
        assertNull(usuario.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(usuario.getFotoPortada(), "La foto de portada debería ser nula");

        // Validar que no hay violaciones (ya que las imágenes son opcionales)
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con imágenes nulas");
    }

    // ==================== TESTS DE MÉTODOS DE UserDetails ====================

    @Test
    @DisplayName("Debería retornar authorities correctamente basado en el rol")
    void shouldReturnAuthoritiesBasedOnRole() {
        // Arrange
        Usuario usuarioAdmin = Usuario.builder()
                .nombre("Admin User")
                .username("admin@email.com")
                .password("admin123")
                .introduccion("Introducción para admin")
                .descripcion("Descripción para admin")
                .rol(Role.ADMIN)
                .build();

        Usuario usuarioUser = Usuario.builder()
                .nombre("Regular User")
                .username("user@email.com")
                .password("user123")
                .introduccion("Introducción para user")
                .descripcion("Descripción para user")
                .rol(Role.USER)
                .build();

        // Act & Assert
        assertEquals(1, usuarioAdmin.getAuthorities().size(), "Admin debe tener 1 authority");
        assertTrue(usuarioAdmin.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN")), "Admin debe tener authority ADMIN");
        assertEquals(1, usuarioUser.getAuthorities().size(), "User debe tener 1 authority");
        assertTrue(usuarioUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER")), "User debe tener authority USER");
    }

    @Test
    @DisplayName("Debería retornar true en métodos de cuenta cuando active es true")
    void shouldReturnTrueForAccountMethodsWhenActiveIsTrue() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Activo")
                .username("activo@email.com")
                .password("active123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .active(true)
                .build();

        // Act & Assert
        assertTrue(usuario.isAccountNonExpired(), "La cuenta no debería expirar");
        assertTrue(usuario.isAccountNonLocked(), "La cuenta no debería estar bloqueada");
        assertTrue(usuario.isCredentialsNonExpired(), "Las credenciales no deberían expirar");
        assertTrue(usuario.isEnabled(), "La cuenta debería estar habilitada");
    }

    @Test
    @DisplayName("Debería retornar false en métodos de cuenta cuando active es false")
    void shouldReturnFalseForAccountMethodsWhenActiveIsFalse() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Inactivo")
                .username("inactivo@email.com")
                .password("inactive123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .active(false)
                .build();

        // Act & Assert
        assertFalse(usuario.isAccountNonExpired(), "La cuenta debería expirar");
        assertFalse(usuario.isAccountNonLocked(), "La cuenta debería estar bloqueada");
        assertFalse(usuario.isCredentialsNonExpired(), "Las credenciales deberían expirar");
        assertFalse(usuario.isEnabled(), "La cuenta no debería estar habilitada");
    }

    @Test
    @DisplayName("getEmail debería retornar el mismo valor que getUsername")
    void shouldReturnSameEmailAsUsername() {
        // Arrange
        String expectedEmail = "test@email.com";
        Usuario usuario = Usuario.builder()
                .nombre("Test User")
                .username(expectedEmail)
                .password("test123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .build();

        // Act & Assert
        assertEquals(expectedEmail, usuario.getEmail(), "getEmail debería devolver el mismo valor que getUsername");
        assertEquals(usuario.getUsername(), usuario.getEmail(), "getUsername y getEmail deberían ser iguales");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos Usuario con los mismos valores")
    void shouldBeEqualWhenComparingTwoUsuariosWithSameValues() {
        // Arrange
        Usuario usuario1 = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .build();

        Usuario usuario2 = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .build();

        // Act & Assert
        assertEquals(usuario1, usuario2, "Los objetos Usuario deberían ser iguales");
        assertEquals(usuario1.hashCode(), usuario2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos Usuario con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoUsuariosWithDifferentValues() {
        // Arrange
        Usuario usuario1 = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .build();

        Usuario usuario2 = Usuario.builder()
                .id(2)
                .nombre("María García")
                .username("maria@email.com")
                .password("pass456")
                .introduccion("Otra intro")
                .descripcion("Otra desc")
                .rol(Role.ADMIN)
                .build();

        // Act & Assert
        assertNotEquals(usuario1, usuario2, "Los objetos Usuario no deberían ser iguales");
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando nombre es nulo")
    void validation_ShouldHaveViolations_WhenNombreIsNull() {
        // Arrange - Nombre nulo
        Usuario usuario = Usuario.builder()
                .nombre(null)
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando username está vacío o es nulo")
    void validation_ShouldHaveViolations_WhenUsernameIsNullOrEmpty(String username) {
        // Arrange - Username vacío
        Usuario usuario = Usuario.builder()
                .nombre("Nombre Válido")
                .username(username)
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para username vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")),
                "Debe haber violación específica para el campo username");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando username no es un email válido")
    void validation_ShouldHaveViolations_WhenUsernameIsInvalidEmail() {
        // Arrange - Username con formato de email inválido
        Usuario usuario = Usuario.builder()
                .nombre("Nombre Válido")
                .username("correoinvalido")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")),
                "Debe haber violación específica para el campo username");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ab", "12", "A"})
    @DisplayName("Validación - Debe tener violaciones cuando nombre es muy corto")
    void validation_ShouldHaveViolations_WhenNombreIsTooShort(String nombre) {
        // Arrange - Nombre muy corto (menos de 3 caracteres)
        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre demasiado corto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando password está vacío o es nulo")
    void validation_ShouldHaveViolations_WhenPasswordIsNullOrEmpty(String password) {
        // Arrange - Password vacío
        Usuario usuario = Usuario.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password(password)
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para password vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")),
                "Debe haber violación específica para el campo password");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n", "1234"})
    @DisplayName("Validación - Debe tener violaciones cuando introducción es muy corta o está vacía")
    void validation_ShouldHaveViolations_WhenIntroduccionIsTooShort(String introduccion) {
        // Arrange - Introducción muy corta (menos de 5 caracteres)
        Usuario usuario = Usuario.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion(introduccion)
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para introducción vacía o muy corta");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("introduccion")),
                "Debe haber violación específica para el campo introduccion");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n", "Hola"})
    @DisplayName("Validación - Debe tener violaciones cuando descripción es muy corta o está vacía")
    void validation_ShouldHaveViolations_WhenDescripcionIsTooShort(String descripcion) {
        // Arrange - Descripción muy corta (menos de 5 caracteres)
        Usuario usuario = Usuario.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion(descripcion)
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción vacía o muy corta");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando rol es nulo")
    void validation_ShouldHaveViolations_WhenRolIsNull() {
        // Arrange - Rol nulo
        Usuario usuario = Usuario.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(null)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para rol nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("rol")),
                "Debe haber violación específica para el campo rol");
    }

    @ParameterizedTest
    @ValueSource(strings = {"desarrollador full stack con mas de 5 caracteres", "una descripcion muy larga que supera los 5 caracteres"})
    @DisplayName("Validación - Debe aceptar introducciones y descripciones largas")
    void validation_ShouldAcceptLongIntroduccionAndDescripcion(String textoLargo) {
        // Arrange - Texto largo válido
        Usuario usuario = Usuario.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion(textoLargo)
                .descripcion(textoLargo)
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para textos largos válidos");
    }

    // ==================== TESTS DE MÉTODOS DE Source ====================

    @Test
    @DisplayName("Debería retornar cadena vacía en getSystemId cuando no se ha establecido")
    void shouldReturnEmptyStringForSystemIdWhenNotSet() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Test User")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .build();

        // Act & Assert
        assertEquals("", usuario.getSystemId(), "getSystemId debería retornar cadena vacía por defecto");
    }

    @Test
    @DisplayName("setSystemId no debería lanzar excepción")
    void shouldNotThrowExceptionWhenSettingSystemId() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Test User")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .build();

        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> usuario.setSystemId("http://example.com"),
                "setSystemId no debería lanzar excepción");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .id(1)
                .nombre("Nombre Inicial")
                .username("inicial@email.com")
                .password("pass123")
                .introduccion("Intro inicial")
                .descripcion("Desc inicial")
                .rol(Role.USER)
                .active(true)
                .build();

        // Act
        usuario.setId(2);
        usuario.setNombre("Nombre Actualizado");
        usuario.setUsername("actualizado@email.com");
        usuario.setPassword("pass456");
        usuario.setIntroduccion("Intro actualizada con más de 5 caracteres");
        usuario.setDescripcion("Desc actualizada con más de 5 caracteres");
        usuario.setRol(Role.ADMIN);
        usuario.setActive(false);

        // Assert
        assertEquals(2, usuario.getId(), "El ID debería estar actualizado");
        assertEquals("Nombre Actualizado", usuario.getNombre(), "El nombre debería estar actualizado");
        assertEquals("actualizado@email.com", usuario.getUsername(), "El username debería estar actualizado");
        assertEquals("pass456", usuario.getPassword(), "La contraseña debería estar actualizada");
        assertEquals("Intro actualizada con más de 5 caracteres", usuario.getIntroduccion(), "La introducción debería estar actualizada");
        assertEquals("Desc actualizada con más de 5 caracteres", usuario.getDescripcion(), "La descripción debería estar actualizada");
        assertEquals(Role.ADMIN, usuario.getRol(), "El rol debería estar actualizado");
        assertFalse(usuario.isActive(), "El usuario debería estar inactivo");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        Usuario usuario = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            usuario.getNombre(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Validación - Debe manejar nombres en el límite de longitud (3 y 145 caracteres)")
    void validation_ShouldHandleNombreAtLengthBoundaries() {
        // Arrange - Nombre exactamente de 3 caracteres (mínimo)
        String nombreMinimo = "ABC";
        Usuario usuarioMinimo = Usuario.builder()
                .nombre(nombreMinimo)
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act - Validar nombre mínimo
        Set<ConstraintViolation<Usuario>> violationsMinimo = validator.validate(usuarioMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para nombre exactamente de 3 caracteres");
        assertEquals(3, usuarioMinimo.getNombre().length(), "El nombre debería tener exactamente 3 caracteres");

        // Arrange - Nombre exactamente de 145 caracteres (máximo)
        String nombreMaximo = "A".repeat(145);
        Usuario usuarioMaximo = Usuario.builder()
                .nombre(nombreMaximo)
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act - Validar nombre máximo
        Set<ConstraintViolation<Usuario>> violationsMaximo = validator.validate(usuarioMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para nombre exactamente de 145 caracteres");
        assertEquals(145, usuarioMaximo.getNombre().length(), "El nombre debería tener exactamente 145 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar introducción y descripción en límites de longitud (5 y 500 caracteres)")
    void validation_ShouldHandleIntroduccionAndDescripcionAtLengthBoundaries() {
        // Arrange - Texto exactamente de 5 caracteres (mínimo)
        String textoMinimo = "12345";
        Usuario usuarioMinimo = Usuario.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion(textoMinimo)
                .descripcion(textoMinimo)
                .rol(Role.USER)
                .build();

        // Act - Validar texto mínimo
        Set<ConstraintViolation<Usuario>> violationsMinimo = validator.validate(usuarioMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para texto exactamente de 5 caracteres");
        assertEquals(5, usuarioMinimo.getIntroduccion().length(), "La introducción debería tener exactamente 5 caracteres");
        assertEquals(5, usuarioMinimo.getDescripcion().length(), "La descripción debería tener exactamente 5 caracteres");

        // Arrange - Texto exactamente de 500 caracteres (máximo)
        String textoMaximo = "A".repeat(500);
        Usuario usuarioMaximo = Usuario.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion(textoMaximo)
                .descripcion(textoMaximo)
                .rol(Role.USER)
                .build();

        // Act - Validar texto máximo
        Set<ConstraintViolation<Usuario>> violationsMaximo = validator.validate(usuarioMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para texto exactamente de 500 caracteres");
        assertEquals(500, usuarioMaximo.getIntroduccion().length(), "La introducción debería tener exactamente 500 caracteres");
        assertEquals(500, usuarioMaximo.getDescripcion().length(), "La descripción debería tener exactamente 500 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en campos de texto")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Texto con caracteres especiales y acentos
        String nombreEspecial = "José María ñáéíóú ÜÑ";
        String introduccionEspecial = "Introducción con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";
        String descripcionEspecial = "Descripción con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";

        Usuario usuario = Usuario.builder()
                .nombre(nombreEspecial)
                .username("test@email.com")
                .password("pass123")
                .introduccion(introduccionEspecial)
                .descripcion(descripcionEspecial)
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(nombreEspecial, usuario.getNombre(), "El nombre con caracteres especiales debería mantenerse");
        assertEquals(introduccionEspecial, usuario.getIntroduccion(), "La introducción con caracteres especiales debería mantenerse");
        assertEquals(descripcionEspecial, usuario.getDescripcion(), "La descripción con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Validación - Debe manejar todos los roles del enum")
    void validation_ShouldHandleAllRoles() {
        // Arrange - Probar todos los roles
        Role[] roles = Role.values();

        for (Role rol : roles) {
            // Act
            Usuario usuario = Usuario.builder()
                    .nombre("Nombre Válido")
                    .username("test@email.com")
                    .password("pass123")
                    .introduccion("Introducción válida con más de 5 caracteres")
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .rol(rol)
                    .build();

            // Assert
            assertNotNull(usuario, "El Usuario no debería ser nulo para rol: " + rol);
            assertEquals(rol, usuario.getRol(), "Debería manejar correctamente el rol: " + rol);
            assertEquals(rol.name(), usuario.getAuthorities().iterator().next().getAuthority(),
                    "El authority debería coincidir con el nombre del rol");
        }
    }
}