package com.example.demo.dto;

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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase UsuarioDto
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class UsuarioDtoTest {

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
    @DisplayName("Debería crear UsuarioDto con valores válidos - Caso feliz")
    void shouldCreateUsuarioDtoWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedNombre = "Juan Pérez";
        String expectedUsername = "juan.perez@email.com";
        String expectedPassword = "password123";
        String expectedIntroduccion = "Desarrollador Full Stack con 5 años de experiencia";
        String expectedDescripcion = "Apasionado por la tecnología y el desarrollo de software.";
        Role expectedRol = Role.USER;
        ImagenDto expectedFotoPerfil = ImagenDto.builder()
                .id(1)
                .url("perfil.jpg")
                .alt("Foto de perfil")
                .build();
        ImagenDto expectedFotoPortada = ImagenDto.builder()
                .id(2)
                .url("portada.jpg")
                .alt("Foto de portada")
                .build();

        // Act - Ejecutar la acción a probar
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .id(expectedId)
                .nombre(expectedNombre)
                .username(expectedUsername)
                .password(expectedPassword)
                .introduccion(expectedIntroduccion)
                .descripcion(expectedDescripcion)
                .rol(expectedRol)
                .fotoPerfil(expectedFotoPerfil)
                .fotoPortada(expectedFotoPortada)
                .active(true)
                .build();

        // Assert - Verificar resultados
        assertNotNull(usuarioDto, "El objeto UsuarioDto no debería ser nulo");
        assertEquals(expectedId, usuarioDto.getId(), "El ID debería coincidir");
        assertEquals(expectedNombre, usuarioDto.getNombre(), "El nombre debería coincidir");
        assertEquals(expectedUsername, usuarioDto.getUsername(), "El username debería coincidir");
        assertEquals(expectedPassword, usuarioDto.getPassword(), "La contraseña debería coincidir");
        assertEquals(expectedIntroduccion, usuarioDto.getIntroduccion(), "La introducción debería coincidir");
        assertEquals(expectedDescripcion, usuarioDto.getDescripcion(), "La descripción debería coincidir");
        assertEquals(expectedRol, usuarioDto.getRol(), "El rol debería coincidir");
        assertNotNull(usuarioDto.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertEquals(expectedFotoPerfil.getUrl(), usuarioDto.getFotoPerfil().getUrl(), "La URL de la foto de perfil debería coincidir");
        assertNotNull(usuarioDto.getFotoPortada(), "La foto de portada no debería ser nula");
        assertEquals(expectedFotoPortada.getUrl(), usuarioDto.getFotoPortada().getUrl(), "La URL de la foto de portada debería coincidir");
        assertTrue(usuarioDto.isActive(), "El usuario debería estar activo");

        // Validar con Bean Validation
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear UsuarioDto con constructor por defecto")
    void shouldCreateUsuarioDtoWithDefaultConstructor() {
        // Arrange & Act
        UsuarioDto usuarioDto = new UsuarioDto();

        // Assert
        assertNotNull(usuarioDto, "El objeto UsuarioDto no debería ser nulo");
        assertNull(usuarioDto.getId(), "El ID debería ser nulo por defecto");
        assertNull(usuarioDto.getNombre(), "El nombre debería ser nulo por defecto");
        assertNull(usuarioDto.getUsername(), "El username debería ser nulo por defecto");
        assertNull(usuarioDto.getPassword(), "La contraseña debería ser nula por defecto");
        assertNull(usuarioDto.getIntroduccion(), "La introducción debería ser nula por defecto");
        assertNull(usuarioDto.getDescripcion(), "La descripción debería ser nula por defecto");
        assertNull(usuarioDto.getRol(), "El rol debería ser nulo por defecto");
        assertNull(usuarioDto.getFotoPerfil(), "La foto de perfil debería ser nula por defecto");
        assertNull(usuarioDto.getFotoPortada(), "La foto de portada debería ser nula por defecto");
        assertTrue(usuarioDto.isActive(), "El active debería ser true por defecto");
    }

    @Test
    @DisplayName("Debería crear UsuarioDto con constructor con todos los argumentos")
    void shouldCreateUsuarioDtoWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String nombre = "María García";
        String username = "maria.garcia@email.com";
        String password = "password456";
        String introduccion = "Desarrolladora Frontend con 3 años de experiencia";
        String descripcion = "Especializada en Angular y React.";
        Role rol = Role.ADMIN;
        ImagenDto fotoPerfil = ImagenDto.builder()
                .id(1)
                .url("perfil.jpg")
                .alt("Foto de perfil")
                .build();
        ImagenDto fotoPortada = ImagenDto.builder()
                .id(2)
                .url("portada.jpg")
                .alt("Foto de portada")
                .build();
        boolean active = true;

        // Act
        UsuarioDto usuarioDto = new UsuarioDto(id, nombre, username, password, introduccion, descripcion, rol, fotoPerfil, fotoPortada, active);

        // Assert
        assertNotNull(usuarioDto, "El objeto UsuarioDto no debería ser nulo");
        assertEquals(id, usuarioDto.getId(), "El ID debería coincidir");
        assertEquals(nombre, usuarioDto.getNombre(), "El nombre debería coincidir");
        assertEquals(username, usuarioDto.getUsername(), "El username debería coincidir");
        assertEquals(password, usuarioDto.getPassword(), "La contraseña debería coincidir");
        assertEquals(introduccion, usuarioDto.getIntroduccion(), "La introducción debería coincidir");
        assertEquals(descripcion, usuarioDto.getDescripcion(), "La descripción debería coincidir");
        assertEquals(rol, usuarioDto.getRol(), "El rol debería coincidir");
        assertNotNull(usuarioDto.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertNotNull(usuarioDto.getFotoPortada(), "La foto de portada no debería ser nula");
        assertTrue(usuarioDto.isActive(), "El usuario debería estar activo");
    }

    @Test
    @DisplayName("Debería permitir valores nulos en campos opcionales - Imágenes")
    void shouldAllowNullValuesInOptionalFields() {
        // Arrange & Act - UsuarioDto sin imágenes
        UsuarioDto usuarioDto = UsuarioDto.builder()
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
        assertNotNull(usuarioDto, "El objeto UsuarioDto no debería ser nulo");
        assertNull(usuarioDto.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(usuarioDto.getFotoPortada(), "La foto de portada debería ser nula");

        // Validar que no hay violaciones (ya que las imágenes son opcionales)
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con imágenes nulas");
    }

    // ==================== TESTS DE VALIDACIONES ====================

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando nombre es nulo")
    void validation_ShouldHaveViolations_WhenNombreIsNull() {
        // Arrange - Nombre nulo
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre(null)
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

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
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Nombre Válido")
                .username(username)
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para username vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")),
                "Debe haber violación específica para el campo username");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando username no es un email válido")
    void validation_ShouldHaveViolations_WhenUsernameIsInvalidEmail() {
        // Arrange - Username con formato de email inválido
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Nombre Válido")
                .username("correoinvalido")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")),
                "Debe haber violación específica para el campo username");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Validación - Debe tener violaciones cuando password está vacío o es nulo")
    void validation_ShouldHaveViolations_WhenPasswordIsNullOrEmpty(String password) {
        // Arrange - Password vacío
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password(password)
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para password vacío o nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")),
                "Debe haber violación específica para el campo password");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ab", "12", "A"})
    @DisplayName("Validación - Debe tener violaciones cuando nombre es muy corto (menos de 3 caracteres)")
    void validation_ShouldHaveViolations_WhenNombreIsTooShort(String nombre) {
        // Arrange - Nombre muy corto (menos de 3 caracteres)
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre(nombre)
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre demasiado corto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Un nombre muy largo que supera los 145 caracteres es inválido para este test porque excede el límite máximo permitido"})
    @DisplayName("Validación - Debe tener violaciones cuando nombre es muy largo (más de 145 caracteres)")
    void validation_ShouldHaveViolations_WhenNombreIsTooLong(String nombreLargo) {
        // Arrange - Nombre demasiado largo
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre(nombreLargo)
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre demasiado largo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debe haber violación específica para el campo nombre");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n", "1234"})
    @DisplayName("Validación - Debe tener violaciones cuando introducción es muy corta o está vacía")
    void validation_ShouldHaveViolations_WhenIntroduccionIsTooShort(String introduccion) {
        // Arrange - Introducción muy corta (menos de 5 caracteres)
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion(introduccion)
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

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
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion(descripcion)
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para descripción vacía o muy corta");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debe haber violación específica para el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Debe tener violaciones cuando rol es nulo")
    void validation_ShouldHaveViolations_WhenRolIsNull() {
        // Arrange - Rol nulo
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(null)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertFalse(violations.isEmpty(), "Debe haber violaciones para rol nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("rol")),
                "Debe haber violación específica para el campo rol");
    }

    @Test
    @DisplayName("Validación - Debe aceptar introducciones y descripciones largas")
    void validation_ShouldAcceptLongIntroduccionAndDescripcion() {
        // Arrange - Texto largo válido
        String textoLargo = "desarrollador full stack con mas de 5 caracteres y mucha experiencia en el desarrollo de aplicaciones empresariales";
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion(textoLargo)
                .descripcion(textoLargo)
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para textos largos válidos");
    }

    // ==================== TESTS DE ENUM ====================

    @Test
    @DisplayName("Debe manejar todos los roles del enum Role")
    void shouldHandleAllRoles() {
        // Arrange - Probar todos los roles
        Role[] roles = Role.values();

        for (Role rol : roles) {
            // Act
            UsuarioDto usuarioDto = UsuarioDto.builder()
                    .nombre("Usuario Test")
                    .username("test@email.com")
                    .password("pass123")
                    .introduccion("Introducción válida con más de 5 caracteres")
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .rol(rol)
                    .build();

            // Assert
            assertNotNull(usuarioDto, "El UsuarioDto no debería ser nulo para rol: " + rol);
            assertEquals(rol, usuarioDto.getRol(), "Debería manejar correctamente el rol: " + rol);
        }
    }

    // ==================== TESTS DE RELACIONES ====================

    @Test
    @DisplayName("Debe manejar relación con ImagenDto correctamente")
    void shouldHandleRelationshipWithImagenDtoCorrectly() {
        // Arrange
        ImagenDto fotoPerfil = ImagenDto.builder()
                .id(1)
                .url("perfil.jpg")
                .alt("Foto de perfil del usuario")
                .build();

        ImagenDto fotoPortada = ImagenDto.builder()
                .id(2)
                .url("portada.jpg")
                .alt("Foto de portada del usuario")
                .build();

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Usuario Con Imágenes")
                .username("imagenes@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .fotoPerfil(fotoPerfil)
                .fotoPortada(fotoPortada)
                .build();

        // Act & Assert
        assertNotNull(usuarioDto.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertEquals("perfil.jpg", usuarioDto.getFotoPerfil().getUrl(), "La URL de la foto de perfil debería coincidir");
        assertEquals("Foto de perfil del usuario", usuarioDto.getFotoPerfil().getAlt(), "El alt de la foto de perfil debería coincidir");

        assertNotNull(usuarioDto.getFotoPortada(), "La foto de portada no debería ser nula");
        assertEquals("portada.jpg", usuarioDto.getFotoPortada().getUrl(), "La URL de la foto de portada debería coincidir");
        assertEquals("Foto de portada del usuario", usuarioDto.getFotoPortada().getAlt(), "El alt de la foto de portada debería coincidir");
    }

    @Test
    @DisplayName("Debe manejar creación de UsuarioDto con solo foto de perfil")
    void shouldHandleUsuarioDtoWithOnlyProfilePicture() {
        // Arrange - UsuarioDto solo con foto de perfil
        ImagenDto fotoPerfil = ImagenDto.builder()
                .id(1)
                .url("perfil.jpg")
                .alt("Foto de perfil")
                .build();

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Usuario Solo Perfil")
                .username("solo.perfil@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .fotoPerfil(fotoPerfil)
                .fotoPortada(null)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertNotNull(usuarioDto, "El UsuarioDto no debería ser nulo");
        assertNotNull(usuarioDto.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertNull(usuarioDto.getFotoPortada(), "La foto de portada debería ser nula");
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debe manejar creación de UsuarioDto con solo foto de portada")
    void shouldHandleUsuarioDtoWithOnlyCoverPicture() {
        // Arrange - UsuarioDto solo con foto de portada
        ImagenDto fotoPortada = ImagenDto.builder()
                .id(2)
                .url("portada.jpg")
                .alt("Foto de portada")
                .build();

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Usuario Solo Portada")
                .username("solo.portada@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(fotoPortada)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertNotNull(usuarioDto, "El UsuarioDto no debería ser nulo");
        assertNull(usuarioDto.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNotNull(usuarioDto.getFotoPortada(), "La foto de portada no debería ser nula");
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos UsuarioDto con los mismos valores")
    void shouldBeEqualWhenComparingTwoUsuarioDtosWithSameValues() {
        // Arrange
        ImagenDto fotoPerfil = ImagenDto.builder()
                .id(1)
                .url("perfil.jpg")
                .alt("Foto de perfil")
                .build();

        UsuarioDto usuarioDto1 = UsuarioDto.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .fotoPerfil(fotoPerfil)
                .active(true)
                .build();

        UsuarioDto usuarioDto2 = UsuarioDto.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .fotoPerfil(fotoPerfil)
                .active(true)
                .build();

        // Act & Assert
        assertEquals(usuarioDto1, usuarioDto2, "Los objetos UsuarioDto deberían ser iguales");
        assertEquals(usuarioDto1.hashCode(), usuarioDto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos UsuarioDto con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoUsuarioDtosWithDifferentValues() {
        // Arrange
        UsuarioDto usuarioDto1 = UsuarioDto.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .active(true)
                .build();

        UsuarioDto usuarioDto2 = UsuarioDto.builder()
                .id(2)
                .nombre("María García")
                .username("maria@email.com")
                .password("pass456")
                .introduccion("Otra intro")
                .descripcion("Otra desc")
                .rol(Role.ADMIN)
                .active(false)
                .build();

        // Act & Assert
        assertNotEquals(usuarioDto1, usuarioDto2, "Los objetos UsuarioDto no deberían ser iguales");
        assertNotEquals(usuarioDto1.hashCode(), usuarioDto2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .id(1)
                .nombre("Nombre Inicial")
                .username("inicial@email.com")
                .password("pass123")
                .introduccion("Intro inicial")
                .descripcion("Desc inicial")
                .rol(Role.USER)
                .active(true)
                .build();

        ImagenDto nuevaFotoPerfil = ImagenDto.builder()
                .id(3)
                .url("nuevo-perfil.jpg")
                .alt("Nueva foto de perfil")
                .build();

        ImagenDto nuevaFotoPortada = ImagenDto.builder()
                .id(4)
                .url("nuevo-portada.jpg")
                .alt("Nueva foto de portada")
                .build();

        // Act
        usuarioDto.setId(2);
        usuarioDto.setNombre("Nombre Actualizado");
        usuarioDto.setUsername("actualizado@email.com");
        usuarioDto.setPassword("pass456");
        usuarioDto.setIntroduccion("Intro actualizada con más de 5 caracteres");
        usuarioDto.setDescripcion("Desc actualizada con más de 5 caracteres");
        usuarioDto.setRol(Role.ADMIN);
        usuarioDto.setFotoPerfil(nuevaFotoPerfil);
        usuarioDto.setFotoPortada(nuevaFotoPortada);
        usuarioDto.setActive(false);

        // Assert
        assertEquals(2, usuarioDto.getId(), "El ID debería estar actualizado");
        assertEquals("Nombre Actualizado", usuarioDto.getNombre(), "El nombre debería estar actualizado");
        assertEquals("actualizado@email.com", usuarioDto.getUsername(), "El username debería estar actualizado");
        assertEquals("pass456", usuarioDto.getPassword(), "La contraseña debería estar actualizada");
        assertEquals("Intro actualizada con más de 5 caracteres", usuarioDto.getIntroduccion(), "La introducción debería estar actualizada");
        assertEquals("Desc actualizada con más de 5 caracteres", usuarioDto.getDescripcion(), "La descripción debería estar actualizada");
        assertEquals(Role.ADMIN, usuarioDto.getRol(), "El rol debería estar actualizado");
        assertNotNull(usuarioDto.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertEquals("nuevo-perfil.jpg", usuarioDto.getFotoPerfil().getUrl(), "La URL de la foto de perfil debería estar actualizada");
        assertNotNull(usuarioDto.getFotoPortada(), "La foto de portada no debería ser nula");
        assertEquals("nuevo-portada.jpg", usuarioDto.getFotoPortada().getUrl(), "La URL de la foto de portada debería estar actualizada");
        assertFalse(usuarioDto.isActive(), "El usuario debería estar inactivo");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        UsuarioDto usuarioDto = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            usuarioDto.getNombre(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Validación - Debe manejar nombres en el límite de longitud (3 y 145 caracteres)")
    void validation_ShouldHandleNombreAtLengthBoundaries() {
        // Arrange - Nombre exactamente de 3 caracteres (mínimo)
        String nombreMinimo = "ABC";
        UsuarioDto usuarioDtoMinimo = UsuarioDto.builder()
                .nombre(nombreMinimo)
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act - Validar nombre mínimo
        Set<ConstraintViolation<UsuarioDto>> violationsMinimo = validator.validate(usuarioDtoMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para nombre exactamente de 3 caracteres");
        assertEquals(3, usuarioDtoMinimo.getNombre().length(), "El nombre debería tener exactamente 3 caracteres");

        // Arrange - Nombre exactamente de 145 caracteres (máximo)
        String nombreMaximo = "A".repeat(145);
        UsuarioDto usuarioDtoMaximo = UsuarioDto.builder()
                .nombre(nombreMaximo)
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        // Act - Validar nombre máximo
        Set<ConstraintViolation<UsuarioDto>> violationsMaximo = validator.validate(usuarioDtoMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para nombre exactamente de 145 caracteres");
        assertEquals(145, usuarioDtoMaximo.getNombre().length(), "El nombre debería tener exactamente 145 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar introducción y descripción en límites de longitud (5 y 500 caracteres)")
    void validation_ShouldHandleIntroduccionAndDescripcionAtLengthBoundaries() {
        // Arrange - Texto exactamente de 5 caracteres (mínimo)
        String textoMinimo = "12345";
        UsuarioDto usuarioDtoMinimo = UsuarioDto.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion(textoMinimo)
                .descripcion(textoMinimo)
                .rol(Role.USER)
                .build();

        // Act - Validar texto mínimo
        Set<ConstraintViolation<UsuarioDto>> violationsMinimo = validator.validate(usuarioDtoMinimo);

        // Assert
        assertTrue(violationsMinimo.isEmpty(), "No debería haber violaciones para texto exactamente de 5 caracteres");
        assertEquals(5, usuarioDtoMinimo.getIntroduccion().length(), "La introducción debería tener exactamente 5 caracteres");
        assertEquals(5, usuarioDtoMinimo.getDescripcion().length(), "La descripción debería tener exactamente 5 caracteres");

        // Arrange - Texto exactamente de 500 caracteres (máximo)
        String textoMaximo = "A".repeat(500);
        UsuarioDto usuarioDtoMaximo = UsuarioDto.builder()
                .nombre("Nombre Válido")
                .username("test@email.com")
                .password("pass123")
                .introduccion(textoMaximo)
                .descripcion(textoMaximo)
                .rol(Role.USER)
                .build();

        // Act - Validar texto máximo
        Set<ConstraintViolation<UsuarioDto>> violationsMaximo = validator.validate(usuarioDtoMaximo);

        // Assert
        assertTrue(violationsMaximo.isEmpty(), "No debería haber violaciones para texto exactamente de 500 caracteres");
        assertEquals(500, usuarioDtoMaximo.getIntroduccion().length(), "La introducción debería tener exactamente 500 caracteres");
        assertEquals(500, usuarioDtoMaximo.getDescripcion().length(), "La descripción debería tener exactamente 500 caracteres");
    }

    @Test
    @DisplayName("Validación - Debe manejar caracteres especiales y acentos en campos de texto")
    void validation_ShouldHandleSpecialCharactersAndAccents() {
        // Arrange - Texto con caracteres especiales y acentos
        String nombreEspecial = "José María ñáéíóú ÜÑ";
        String introduccionEspecial = "Introducción con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";
        String descripcionEspecial = "Descripción con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre(nombreEspecial)
                .username("test@email.com")
                .password("pass123")
                .introduccion(introduccionEspecial)
                .descripcion(descripcionEspecial)
                .rol(Role.USER)
                .build();

        // Act
        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(usuarioDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para caracteres especiales");
        assertEquals(nombreEspecial, usuarioDto.getNombre(), "El nombre con caracteres especiales debería mantenerse");
        assertEquals(introduccionEspecial, usuarioDto.getIntroduccion(), "La introducción con caracteres especiales debería mantenerse");
        assertEquals(descripcionEspecial, usuarioDto.getDescripcion(), "La descripción con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("toString debería incluir información relevante del UsuarioDto")
    void toString_ShouldIncludeRelevantInformation() {
        // Arrange
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .active(true)
                .build();

        // Act
        String toStringResult = usuarioDto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Juan Pérez"), "toString debería contener el nombre");
        assertTrue(toStringResult.contains("juan@email.com"), "toString debería contener el username");
        assertTrue(toStringResult.contains("USER"), "toString debería contener el rol");
        assertTrue(toStringResult.contains("active"), "toString debería contener el campo active");
    }

    @Test
    @DisplayName("Debe manejar el campo active correctamente con valores true y false")
    void shouldHandleActiveFieldCorrectly() {
        // Arrange & Act - UsuarioDto activo
        UsuarioDto usuarioDtoActivo = UsuarioDto.builder()
                .nombre("Usuario Activo")
                .username("activo@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .active(true)
                .build();

        // Assert
        assertTrue(usuarioDtoActivo.isActive(), "El usuario debería estar activo");

        // Arrange & Act - UsuarioDto inactivo
        UsuarioDto usuarioDtoInactivo = UsuarioDto.builder()
                .nombre("Usuario Inactivo")
                .username("inactivo@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .active(false)
                .build();

        // Assert
        assertFalse(usuarioDtoInactivo.isActive(), "El usuario debería estar inactivo");
    }
}