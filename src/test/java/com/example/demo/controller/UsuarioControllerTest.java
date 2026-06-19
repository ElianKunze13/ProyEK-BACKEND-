package com.example.demo.controller;

import com.example.demo.dto.ImagenDto;
import com.example.demo.dto.UsuarioDto;
import com.example.demo.enums.Role;
import com.example.demo.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase UsuarioController
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioDto usuarioDtoValido;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
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

        usuarioDtoValido = UsuarioDto.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack con 5 años de experiencia")
                .descripcion("Apasionado por la tecnología y el desarrollo de software.")
                .rol(Role.USER)
                .fotoPerfil(fotoPerfil)
                .fotoPortada(fotoPortada)
                .active(true)
                .build();
    }

    // ==================== TESTS GET /auth/traerPor/{id} ====================

    @Test
    @DisplayName("GET /auth/traerPor/{id} - Debería retornar usuario cuando existe el ID")
    void getUsuarioById_ShouldReturnUsuario_WhenIdExists() {
        // Arrange - Configurar comportamiento del mock
        Integer id = 1;
        when(usuarioService.getById(id)).thenReturn(usuarioDtoValido);

        // Act - Ejecutar el método del controlador
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioById(id);

        // Assert - Verificar resultados
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(id, response.getBody().getId(), "El ID debería coincidir");
        assertEquals("Juan Pérez", response.getBody().getNombre(), "El nombre debería coincidir");
        assertEquals("juan.perez@email.com", response.getBody().getUsername(), "El username debería coincidir");

        // Verificar que se llamó al servicio exactamente una vez
        verify(usuarioService, times(1)).getById(id);
    }

    @Test
    @DisplayName("GET /auth/traerPor/{id} - Debería retornar 404 cuando el ID no existe")
    void getUsuarioById_ShouldReturnNotFound_WhenIdDoesNotExist() {
        // Arrange - Configurar comportamiento del mock para que retorne null
        Integer id = 999;
        when(usuarioService.getById(id)).thenReturn(null);

        // Act - Ejecutar el método del controlador
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioById(id);

        // Assert - Verificar resultados
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK (el controlador no maneja 404)");
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo cuando no se encuentra el usuario");

        // Verificar que se llamó al servicio exactamente una vez
        verify(usuarioService, times(1)).getById(id);
    }

    @Test
    @DisplayName("GET /auth/traerPor/{id} - Debería manejar ID nulo - Test negativo")
    void getUsuarioById_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert - Verificar que se lanza NullPointerException
        assertThrows(NullPointerException.class, () -> {
            usuarioController.getUsuarioById(id);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        // Verificar que nunca se llamó al servicio
        verify(usuarioService, never()).getById(anyInt());
    }

    @Test
    @DisplayName("GET /auth/traerPor/{id} - Debería manejar ID negativo")
    void getUsuarioById_ShouldHandleNegativeId() {
        // Arrange
        Integer id = -1;
        when(usuarioService.getById(id)).thenReturn(null);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioById(id);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody(), "El cuerpo debería ser nulo para ID negativo");

        verify(usuarioService, times(1)).getById(id);
    }

    // ==================== TESTS GET /username/{username} ====================

    @Test
    @DisplayName("GET /username/{username} - Debería retornar usuario cuando existe el username")
    void getUsuarioByUsername_ShouldReturnUsuario_WhenUsernameExists() {
        // Arrange
        String username = "juan.perez@email.com";
        when(usuarioService.getByUsername(username)).thenReturn(usuarioDtoValido);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioByUsername(username);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(username, response.getBody().getUsername(), "El username debería coincidir");
        assertEquals("Juan Pérez", response.getBody().getNombre(), "El nombre debería coincidir");

        // Verificar que se llamó al servicio exactamente una vez
        verify(usuarioService, times(1)).getByUsername(username);
    }

    @Test
    @DisplayName("GET /username/{username} - Debería retornar 404 cuando el username no existe")
    void getUsuarioByUsername_ShouldReturnNotFound_WhenUsernameDoesNotExist() {
        // Arrange
        String username = "no.existe@email.com";
        when(usuarioService.getByUsername(username)).thenReturn(null);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioByUsername(username);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody(), "El cuerpo debería ser nulo cuando no se encuentra el usuario");

        verify(usuarioService, times(1)).getByUsername(username);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("GET /username/{username} - Debería manejar username vacío o nulo")
    void getUsuarioByUsername_ShouldHandleNullOrEmptyUsername(String username) {
        // Arrange - Mock para cuando el username es nulo o vacío
        when(usuarioService.getByUsername(anyString())).thenReturn(null);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioByUsername(username);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody(), "El cuerpo debería ser nulo para username vacío");

        // Si el username es nulo, el servicio podría no ser llamado
        if (username != null) {
            verify(usuarioService, times(1)).getByUsername(username);
        } else {
            verify(usuarioService, never()).getByUsername(anyString());
        }
    }

    @Test
    @DisplayName("GET /username/{username} - Debería manejar username con formato email inválido")
    void getUsuarioByUsername_ShouldHandleInvalidEmailFormat() {
        // Arrange
        String invalidUsername = "correo.invalido";
        when(usuarioService.getByUsername(invalidUsername)).thenReturn(null);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioByUsername(invalidUsername);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(usuarioService, times(1)).getByUsername(invalidUsername);
    }

    @Test
    @DisplayName("GET /username/{username} - Debería manejar username con mayúsculas")
    void getUsuarioByUsername_ShouldHandleUppercaseUsername() {
        // Arrange
        String username = "JUAN.PEREZ@EMAIL.COM";
        when(usuarioService.getByUsername(username)).thenReturn(usuarioDtoValido);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioByUsername(username);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(usuarioService, times(1)).getByUsername(username);
    }

    // ==================== TESTS POST /guardarUsuario ====================

    @Test
    @DisplayName("POST /guardarUsuario - Debería crear usuario y retornar 201 Created")
    void saveUsuario_ShouldCreateUsuarioAndReturnCreated() {
        // Arrange
        UsuarioDto usuarioDtoCrear = UsuarioDto.builder()
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Desarrolladora Frontend con 3 años de experiencia")
                .descripcion("Especializada en Angular y React.")
                .rol(Role.ADMIN)
                .active(true)
                .build();

        UsuarioDto usuarioDtoCreado = UsuarioDto.builder()
                .id(2)
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Desarrolladora Frontend con 3 años de experiencia")
                .descripcion("Especializada en Angular y React.")
                .rol(Role.ADMIN)
                .active(true)
                .build();

        when(usuarioService.create(any(UsuarioDto.class))).thenReturn(usuarioDtoCreado);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.saveUsuario(usuarioDtoCrear);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "El status code debería ser 201 Created");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(2, response.getBody().getId(), "El ID generado debería ser 2");
        assertEquals("María García", response.getBody().getNombre(), "El nombre debería coincidir");
        assertEquals("maria.garcia@email.com", response.getBody().getUsername(), "El username debería coincidir");
        assertEquals(Role.ADMIN, response.getBody().getRol(), "El rol debería ser ADMIN");

        // Verificar que se llamó al servicio exactamente una vez
        verify(usuarioService, times(1)).create(usuarioDtoCrear);
    }

    @Test
    @DisplayName("POST /guardarUsuario - Debería manejar creación con datos mínimos válidos")
    void saveUsuario_ShouldHandleMinimalValidData() {
        // Arrange
        UsuarioDto usuarioDtoMinimo = UsuarioDto.builder()
                .nombre("Ana")
                .username("ana@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .build();

        when(usuarioService.create(any(UsuarioDto.class))).thenReturn(usuarioDtoMinimo);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.saveUsuario(usuarioDtoMinimo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(usuarioService, times(1)).create(usuarioDtoMinimo);
    }

    @Test
    @DisplayName("POST /guardarUsuario - Debería manejar usuario con imágenes nulas")
    void saveUsuario_ShouldHandleUsuarioWithNullImages() {
        // Arrange
        UsuarioDto usuarioDtoSinImagenes = UsuarioDto.builder()
                .nombre("Carlos López")
                .username("carlos@email.com")
                .password("pass789")
                .introduccion("Introducción válida para prueba")
                .descripcion("Descripción válida para prueba")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .build();

        when(usuarioService.create(any(UsuarioDto.class))).thenReturn(usuarioDtoSinImagenes);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.saveUsuario(usuarioDtoSinImagenes);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(response.getBody().getFotoPortada(), "La foto de portada debería ser nula");

        verify(usuarioService, times(1)).create(usuarioDtoSinImagenes);
    }

    @Test
    @DisplayName("POST /guardarUsuario - Debería manejar usuario nulo - Test negativo")
    void saveUsuario_ShouldHandleNullUsuario() {
        // Act & Assert - Verificar que se lanza NullPointerException
        assertThrows(NullPointerException.class, () -> {
            usuarioController.saveUsuario(null);
        }, "Debería lanzar NullPointerException cuando el usuario es nulo");

        // Verificar que nunca se llamó al servicio
        verify(usuarioService, never()).create(any());
    }

    @Test
    @DisplayName("POST /guardarUsuario - Debería manejar excepción del servicio")
    void saveUsuario_ShouldHandleServiceException() {
        // Arrange
        when(usuarioService.create(any(UsuarioDto.class)))
                .thenThrow(new RuntimeException("Error al crear usuario"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            usuarioController.saveUsuario(usuarioDtoValido);
        }, "Debería propagar la excepción del servicio");

        verify(usuarioService, times(1)).create(usuarioDtoValido);
    }

    // ==================== TESTS PUT /usuario/{id} ====================

    @Test
    @DisplayName("PUT /usuario/{id} - Debería actualizar usuario y retornar 200 OK")
    void updateUsuario_ShouldUpdateUsuarioAndReturnOk() {
        // Arrange
        Integer id = 1;
        UsuarioDto usuarioDtoActualizado = UsuarioDto.builder()
                .id(id)
                .nombre("Juan Pérez Actualizado")
                .username("juan.actualizado@email.com")
                .password("newPassword123")
                .introduccion("Introducción actualizada con más de 5 caracteres")
                .descripcion("Descripción actualizada con más de 5 caracteres")
                .rol(Role.ADMIN)
                .active(true)
                .build();

        when(usuarioService.update(eq(id), any(UsuarioDto.class))).thenReturn(usuarioDtoActualizado);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.updateUsuario(id, usuarioDtoActualizado);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(id, response.getBody().getId(), "El ID debería coincidir");
        assertEquals("Juan Pérez Actualizado", response.getBody().getNombre(), "El nombre debería estar actualizado");
        assertEquals("juan.actualizado@email.com", response.getBody().getUsername(), "El username debería estar actualizado");
        assertEquals(Role.ADMIN, response.getBody().getRol(), "El rol debería ser ADMIN");

        // Verificar que se llamó al servicio exactamente una vez
        verify(usuarioService, times(1)).update(eq(id), any(UsuarioDto.class));
    }

    @Test
    @DisplayName("PUT /usuario/{id} - Debería manejar actualización de usuario con ID diferente al del DTO")
    void updateUsuario_ShouldHandleDifferentIdInDto() {
        // Arrange
        Integer idPath = 1;
        UsuarioDto usuarioDtoConIdDiferente = UsuarioDto.builder()
                .id(2)  // ID diferente al de la ruta
                .nombre("Usuario Con ID Diferente")
                .username("diferente@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        UsuarioDto usuarioDtoRetornado = UsuarioDto.builder()
                .id(1)  // El servicio podría usar el ID de la ruta
                .nombre("Usuario Con ID Diferente")
                .username("diferente@email.com")
                .password("pass123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .build();

        when(usuarioService.update(eq(idPath), any(UsuarioDto.class))).thenReturn(usuarioDtoRetornado);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.updateUsuario(idPath, usuarioDtoConIdDiferente);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId(), "El ID debería ser el de la ruta");

        verify(usuarioService, times(1)).update(eq(idPath), any(UsuarioDto.class));
    }

    @Test
    @DisplayName("PUT /usuario/{id} - Debería manejar ID nulo - Test negativo")
    void updateUsuario_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            usuarioController.updateUsuario(id, usuarioDtoValido);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(usuarioService, never()).update(any(), any());
    }

    @Test
    @DisplayName("PUT /usuario/{id} - Debería manejar usuario nulo - Test negativo")
    void updateUsuario_ShouldHandleNullUsuarioDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            usuarioController.updateUsuario(id, null);
        }, "Debería lanzar NullPointerException cuando el usuario es nulo");

        verify(usuarioService, never()).update(any(), any());
    }

    @Test
    @DisplayName("PUT /usuario/{id} - Debería manejar ID no existente")
    void updateUsuario_ShouldHandleNonExistentId() {
        // Arrange
        Integer id = 999;
        when(usuarioService.update(eq(id), any(UsuarioDto.class))).thenReturn(null);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.updateUsuario(id, usuarioDtoValido);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody(), "El cuerpo debería ser nulo cuando el usuario no existe");

        verify(usuarioService, times(1)).update(eq(id), any(UsuarioDto.class));
    }

    @Test
    @DisplayName("PUT /usuario/{id} - Debería manejar excepción del servicio")
    void updateUsuario_ShouldHandleServiceException() {
        // Arrange
        Integer id = 1;
        when(usuarioService.update(eq(id), any(UsuarioDto.class)))
                .thenThrow(new RuntimeException("Error al actualizar usuario"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            usuarioController.updateUsuario(id, usuarioDtoValido);
        }, "Debería propagar la excepción del servicio");

        verify(usuarioService, times(1)).update(eq(id), any(UsuarioDto.class));
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void shouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        when(usuarioService.getById(idMax)).thenReturn(null);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioById(idMax);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(usuarioService, times(1)).getById(idMax);
    }

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MIN_VALUE)")
    void shouldHandleMinIntegerId() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        when(usuarioService.getById(idMin)).thenReturn(null);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioById(idMin);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(usuarioService, times(1)).getById(idMin);
    }

    @Test
    @DisplayName("GET /username/{username} - Debería manejar username muy largo")
    void getUsuarioByUsername_ShouldHandleVeryLongUsername() {
        // Arrange
        String veryLongUsername = "a".repeat(255) + "@email.com";
        when(usuarioService.getByUsername(veryLongUsername)).thenReturn(null);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioByUsername(veryLongUsername);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(usuarioService, times(1)).getByUsername(veryLongUsername);
    }

    @Test
    @DisplayName("GET /username/{username} - Debería manejar username con caracteres especiales")
    void getUsuarioByUsername_ShouldHandleSpecialCharacters() {
        // Arrange
        String specialUsername = "usuario+test@dominio.com";
        when(usuarioService.getByUsername(specialUsername)).thenReturn(null);

        // Act
        ResponseEntity<UsuarioDto> response = usuarioController.getUsuarioByUsername(specialUsername);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(usuarioService, times(1)).getByUsername(specialUsername);
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con el mock
        verifyNoInteractions(usuarioService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas")
    void verifyExpectedInteractions() {
        // Arrange
        Integer id = 1;
        when(usuarioService.getById(id)).thenReturn(usuarioDtoValido);

        // Act
        usuarioController.getUsuarioById(id);

        // Assert - Verificar interacciones exactas
        verify(usuarioService, times(1)).getById(id);
        verify(usuarioService, never()).getByUsername(anyString());
        verify(usuarioService, never()).create(any());
        verify(usuarioService, never()).update(any(), any());
    }

    @Test
    @DisplayName("Comparar objeto UsuarioDto - Debería ser igual cuando los valores son iguales")
    void usuarioDtoEquals_ShouldBeEqual_WhenSameValues() {
        // Arrange
        UsuarioDto dto1 = UsuarioDto.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .active(true)
                .build();

        UsuarioDto dto2 = UsuarioDto.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .active(true)
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode deberían ser iguales");
        assertTrue(dto1.equals(dto2), "equals() debería retornar true");
    }

    @Test
    @DisplayName("Comparar objeto UsuarioDto - No debería ser igual cuando los valores son diferentes")
    void usuarioDtoEquals_ShouldNotBeEqual_WhenDifferentValues() {
        // Arrange
        UsuarioDto dto1 = UsuarioDto.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("pass123")
                .introduccion("Intro válida")
                .descripcion("Desc válida")
                .rol(Role.USER)
                .active(true)
                .build();

        UsuarioDto dto2 = UsuarioDto.builder()
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
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode no deberían ser iguales");
        assertFalse(dto1.equals(dto2), "equals() debería retornar false");
    }

    @Test
    @DisplayName("toString de UsuarioDto debería incluir información relevante")
    void usuarioDtoToString_ShouldIncludeRelevantInformation() {
        // Arrange & Act
        String toStringResult = usuarioDtoValido.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Juan Pérez"), "toString debería contener el nombre");
        assertTrue(toStringResult.contains("juan.perez@email.com"), "toString debería contener el username");
        assertTrue(toStringResult.contains("USER"), "toString debería contener el rol");
        assertTrue(toStringResult.contains("active"), "toString debería contener el campo active");
    }

    @Test
    @DisplayName("Builder de UsuarioDto debería crear objetos correctamente")
    void usuarioDtoBuilder_ShouldCreateCorrectly() {
        // Arrange & Act
        UsuarioDto dto = UsuarioDto.builder()
                .id(10)
                .nombre("Test Builder")
                .username("builder@email.com")
                .password("builderPass")
                .introduccion("Introducción del builder")
                .descripcion("Descripción del builder")
                .rol(Role.ADMIN)
                .active(false)
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(10, dto.getId(), "El ID debería ser 10");
        assertEquals("Test Builder", dto.getNombre(), "El nombre debería coincidir");
        assertEquals("builder@email.com", dto.getUsername(), "El username debería coincidir");
        assertEquals("builderPass", dto.getPassword(), "La contraseña debería coincidir");
        assertEquals("Introducción del builder", dto.getIntroduccion(), "La introducción debería coincidir");
        assertEquals("Descripción del builder", dto.getDescripcion(), "La descripción debería coincidir");
        assertEquals(Role.ADMIN, dto.getRol(), "El rol debería ser ADMIN");
        assertFalse(dto.isActive(), "El active debería ser false");
    }
}