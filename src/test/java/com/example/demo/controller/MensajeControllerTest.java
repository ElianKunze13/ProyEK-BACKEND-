package com.example.demo.controller;

import com.example.demo.dto.MensajeDto;
import com.example.demo.service.MensajeService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase MensajeController
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class MensajeControllerTest {

    @Mock
    private MensajeService mensajeService;

    @InjectMocks
    private MensajeController mensajeController;

    private MensajeDto mensajeDtoValido;
    private MensajeDto mensajeDtoCreado;
    private MensajeDto mensajeDtoSinMensaje;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        LocalDate fechaActual = LocalDate.now();

        mensajeDtoValido = MensajeDto.builder()
                .nombreUsuario("Juan Pérez")
                .email("juan.perez@email.com")
                .mensaje("Este es un mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaActual)
                .build();

        mensajeDtoCreado = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan.perez@email.com")
                .mensaje("Este es un mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaActual)
                .build();

        mensajeDtoSinMensaje = MensajeDto.builder()
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .fechaCreacion(fechaActual)
                .build();
    }

    // ==================== TESTS POST /guardar/contacto ====================

    @Test
    @DisplayName("POST /guardar/contacto - Debería enviar mensaje y retornar 200 OK con éxito")
    void enviarMensajeContacto_ShouldReturnOk_WhenMessageSentSuccessfully() {
        // Arrange
        when(mensajeService.enviarMensaje(any(MensajeDto.class))).thenReturn(mensajeDtoCreado);

        // Act
        ResponseEntity<?> response = mensajeController.enviarMensajeContacto(mensajeDtoValido);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");

        // Verificar que el cuerpo contiene el mapa con los campos esperados
        assertTrue(response.getBody() instanceof Map, "El cuerpo debería ser un Map");
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertEquals("success", responseBody.get("estado"), "El estado debería ser 'success'");
        assertEquals("Mensaje enviado exitosamente. Te contactaré pronto.",
                responseBody.get("mensaje"), "El mensaje debería ser el esperado");
        assertNotNull(responseBody.get("data"), "El data no debería ser nulo");
        assertTrue(responseBody.get("data") instanceof MensajeDto, "data debería ser un MensajeDto");

        MensajeDto data = (MensajeDto) responseBody.get("data");
        assertEquals(1, data.getId(), "El ID del mensaje creado debería ser 1");
        assertEquals("Juan Pérez", data.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("juan.perez@email.com", data.getEmail(), "El email debería coincidir");

        // Verificar que se llamó al servicio exactamente una vez
        verify(mensajeService, times(1)).enviarMensaje(mensajeDtoValido);
    }

    @Test
    @DisplayName("POST /guardar/contacto - Debería enviar mensaje sin contenido (mensaje nulo)")
    void enviarMensajeContacto_ShouldSendMessageWithoutContent() {
        // Arrange
        MensajeDto mensajeDtoSinContenido = MensajeDto.builder()
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .build();

        MensajeDto mensajeDtoCreadoSinMensaje = MensajeDto.builder()
                .id(2)
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .fechaCreacion(LocalDate.now())
                .build();

        when(mensajeService.enviarMensaje(any(MensajeDto.class))).thenReturn(mensajeDtoCreadoSinMensaje);

        // Act
        ResponseEntity<?> response = mensajeController.enviarMensajeContacto(mensajeDtoSinContenido);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("success", responseBody.get("estado"));

        MensajeDto data = (MensajeDto) responseBody.get("data");
        assertNull(data.getMensaje(), "El mensaje debería ser nulo");

        verify(mensajeService, times(1)).enviarMensaje(mensajeDtoSinContenido);
    }

    @Test
    @DisplayName("POST /guardar/contacto - Debería manejar excepción del servicio y retornar 400 Bad Request")
    void enviarMensajeContacto_ShouldReturnBadRequest_WhenServiceThrowsException() {
        // Arrange
        when(mensajeService.enviarMensaje(any(MensajeDto.class)))
                .thenThrow(new RuntimeException("Error al enviar mensaje"));

        // Act
        ResponseEntity<?> response = mensajeController.enviarMensajeContacto(mensajeDtoValido);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "El status code debería ser 400 Bad Request");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("error", responseBody.get("estado"), "El estado debería ser 'error'");
        assertEquals("Error al enviar el mensaje. Por favor intenta nuevamente.",
                responseBody.get("mensaje"), "El mensaje de error debería ser el esperado");

        verify(mensajeService, times(1)).enviarMensaje(mensajeDtoValido);
    }

    @Test
    @DisplayName("POST /guardar/contacto - Debería manejar mensaje con caracteres especiales")
    void enviarMensajeContacto_ShouldHandleSpecialCharacters() {
        // Arrange
        String nombreEspecial = "José María ñáéíóú ÜÑ";
        String emailEspecial = "josé.maria@email.com";
        String mensajeEspecial = "Mensaje con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";

        MensajeDto mensajeDtoEspecial = MensajeDto.builder()
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .build();

        MensajeDto mensajeDtoCreadoEspecial = MensajeDto.builder()
                .id(3)
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .fechaCreacion(LocalDate.now())
                .build();

        when(mensajeService.enviarMensaje(any(MensajeDto.class))).thenReturn(mensajeDtoCreadoEspecial);

        // Act
        ResponseEntity<?> response = mensajeController.enviarMensajeContacto(mensajeDtoEspecial);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        MensajeDto data = (MensajeDto) responseBody.get("data");
        assertEquals(nombreEspecial, data.getNombreUsuario(), "El nombre con caracteres especiales debería mantenerse");
        assertEquals(emailEspecial, data.getEmail(), "El email con caracteres especiales debería mantenerse");
        assertEquals(mensajeEspecial, data.getMensaje(), "El mensaje con caracteres especiales debería mantenerse");

        verify(mensajeService, times(1)).enviarMensaje(mensajeDtoEspecial);
    }

    @Test
    @DisplayName("POST /guardar/contacto - Debería manejar mensaje nulo - Test negativo")
    void enviarMensajeContacto_ShouldHandleNullMensajeDto() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            mensajeController.enviarMensajeContacto(null);
        }, "Debería lanzar excepción cuando el MensajeDto es nulo");

        verify(mensajeService, never()).enviarMensaje(any());
    }

    @Test
    @DisplayName("POST /guardar/contacto - Debería manejar email largo (100 caracteres)")
    void enviarMensajeContacto_ShouldHandleLongEmail() {
        // Arrange
        String emailLargo = "a".repeat(90) + "@email.com"; // 100 caracteres
        MensajeDto mensajeDtoEmailLargo = MensajeDto.builder()
                .nombreUsuario("Usuario Email Largo")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        MensajeDto mensajeDtoCreadoEmailLargo = MensajeDto.builder()
                .id(4)
                .nombreUsuario("Usuario Email Largo")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        when(mensajeService.enviarMensaje(any(MensajeDto.class))).thenReturn(mensajeDtoCreadoEmailLargo);

        // Act
        ResponseEntity<?> response = mensajeController.enviarMensajeContacto(mensajeDtoEmailLargo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        MensajeDto data = (MensajeDto) responseBody.get("data");
        assertEquals(emailLargo, data.getEmail(), "El email largo debería mantenerse");
        assertEquals(100, data.getEmail().length(), "El email debería tener exactamente 100 caracteres");

        verify(mensajeService, times(1)).enviarMensaje(mensajeDtoEmailLargo);
    }

    // ==================== TESTS GET /allMensajes ====================

    @Test
    @DisplayName("GET /allMensajes - Debería retornar todos los mensajes")
    void getAllMensajes_ShouldReturnAllMensajes() {
        // Arrange
        MensajeDto mensaje1 = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Usuario 1")
                .email("usuario1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        MensajeDto mensaje2 = MensajeDto.builder()
                .id(2)
                .nombreUsuario("Usuario 2")
                .email("usuario2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        List<MensajeDto> mensajes = Arrays.asList(mensaje1, mensaje2);

        when(mensajeService.getAllMensajes()).thenReturn(mensajes);

        // Act
        ResponseEntity<List<MensajeDto>> response = mensajeController.getAllMensajes();

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(2, response.getBody().size(), "Debería haber 2 mensajes");
        assertEquals(1, response.getBody().get(0).getId(), "El ID del primer mensaje debería ser 1");
        assertEquals(2, response.getBody().get(1).getId(), "El ID del segundo mensaje debería ser 2");
        assertEquals("Usuario 1", response.getBody().get(0).getNombreUsuario(),
                "El nombre de usuario del primer mensaje debería coincidir");
        assertEquals("Usuario 2", response.getBody().get(1).getNombreUsuario(),
                "El nombre de usuario del segundo mensaje debería coincidir");

        verify(mensajeService, times(1)).getAllMensajes();
    }

    @Test
    @DisplayName("GET /allMensajes - Debería retornar lista vacía cuando no hay mensajes")
    void getAllMensajes_ShouldReturnEmptyList_WhenNoMensajes() {
        // Arrange
        when(mensajeService.getAllMensajes()).thenReturn(List.of());

        // Act
        ResponseEntity<List<MensajeDto>> response = mensajeController.getAllMensajes();

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertTrue(response.getBody().isEmpty(), "La lista debería estar vacía");

        verify(mensajeService, times(1)).getAllMensajes();
    }

    @Test
    @DisplayName("GET /allMensajes - Debería manejar excepción del servicio")
    void getAllMensajes_ShouldHandleServiceException() {
        // Arrange
        when(mensajeService.getAllMensajes()).thenThrow(new RuntimeException("Error al obtener mensajes"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            mensajeController.getAllMensajes();
        }, "Debería propagar la excepción del servicio");

        verify(mensajeService, times(1)).getAllMensajes();
    }

    // ==================== TESTS DELETE /borrar/mensajes/{id} ====================

    @Test
    @DisplayName("DELETE /borrar/mensajes/{id} - Debería eliminar mensaje y retornar 204 No Content")
    void deleteMensaje_ShouldReturnNoContent_WhenMessageDeleted() {
        // Arrange
        Integer id = 1;
        doNothing().when(mensajeService).deleteMensaje(id);

        // Act
        ResponseEntity<Void> response = mensajeController.deleteMensaje(id);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(),
                "El status code debería ser 204 No Content");
        assertNull(response.getBody(), "El cuerpo debería ser nulo");

        verify(mensajeService, times(1)).deleteMensaje(id);
    }

    @Test
    @DisplayName("DELETE /borrar/mensajes/{id} - Debería manejar ID nulo - Test negativo")
    void deleteMensaje_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(Exception.class, () -> {
            mensajeController.deleteMensaje(id);
        }, "Debería lanzar excepción cuando el ID es nulo");

        verify(mensajeService, never()).deleteMensaje(anyInt());
    }

    @Test
    @DisplayName("DELETE /borrar/mensajes/{id} - Debería manejar ID no existente")
    void deleteMensaje_ShouldHandleNonExistentId() {
        // Arrange
        Integer id = 999;
        doThrow(new RuntimeException("Mensaje no encontrado")).when(mensajeService).deleteMensaje(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            mensajeController.deleteMensaje(id);
        }, "Debería propagar la excepción del servicio");

        verify(mensajeService, times(1)).deleteMensaje(id);
    }

    @Test
    @DisplayName("DELETE /borrar/mensajes/{id} - Debería manejar ID negativo")
    void deleteMensaje_ShouldHandleNegativeId() {
        // Arrange
        Integer idNegativo = -1;
        doNothing().when(mensajeService).deleteMensaje(idNegativo);

        // Act
        ResponseEntity<Void> response = mensajeController.deleteMensaje(idNegativo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(mensajeService, times(1)).deleteMensaje(idNegativo);
    }

    // ==================== TESTS GET /obtenerMensaje/{id} ====================

    @Test
    @DisplayName("GET /obtenerMensaje/{id} - Debería retornar mensaje cuando existe el ID")
    void getMensajeById_ShouldReturnMensaje_WhenIdExists() {
        // Arrange
        Integer id = 1;
        when(mensajeService.getMensajeById(id)).thenReturn(mensajeDtoCreado);

        // Act
        ResponseEntity<MensajeDto> response = mensajeController.getMensajeById(id);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(id, response.getBody().getId(), "El ID debería coincidir");
        assertEquals("Juan Pérez", response.getBody().getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals("juan.perez@email.com", response.getBody().getEmail(),
                "El email debería coincidir");
        assertNotNull(response.getBody().getFechaCreacion(), "La fecha de creación no debería ser nula");

        verify(mensajeService, times(1)).getMensajeById(id);
    }

    @Test
    @DisplayName("GET /obtenerMensaje/{id} - Debería retornar 404 Not Found cuando el ID no existe")
    void getMensajeById_ShouldReturnNotFound_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(mensajeService.getMensajeById(id)).thenReturn(null);

        // Act
        ResponseEntity<MensajeDto> response = mensajeController.getMensajeById(id);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "El status code debería ser 404 Not Found");
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo");

        verify(mensajeService, times(1)).getMensajeById(id);
    }

    @Test
    @DisplayName("GET /obtenerMensaje/{id} - Debería manejar ID nulo - Test negativo")
    void getMensajeById_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(Exception.class, () -> {
            mensajeController.getMensajeById(id);
        }, "Debería lanzar excepción cuando el ID es nulo");

        verify(mensajeService, never()).getMensajeById(anyInt());
    }

    @Test
    @DisplayName("GET /obtenerMensaje/{id} - Debería manejar ID negativo")
    void getMensajeById_ShouldHandleNegativeId() {
        // Arrange
        Integer idNegativo = -1;
        when(mensajeService.getMensajeById(idNegativo)).thenReturn(null);

        // Act
        ResponseEntity<MensajeDto> response = mensajeController.getMensajeById(idNegativo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(mensajeService, times(1)).getMensajeById(idNegativo);
    }

    @Test
    @DisplayName("GET /obtenerMensaje/{id} - Debería manejar excepción del servicio")
    void getMensajeById_ShouldHandleServiceException() {
        // Arrange
        Integer id = 1;
        when(mensajeService.getMensajeById(id)).thenThrow(new RuntimeException("Error al obtener mensaje"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            mensajeController.getMensajeById(id);
        }, "Debería propagar la excepción del servicio");

        verify(mensajeService, times(1)).getMensajeById(id);
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("GET /obtenerMensaje/{id} - Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void getMensajeById_ShouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        when(mensajeService.getMensajeById(idMax)).thenReturn(null);

        // Act
        ResponseEntity<MensajeDto> response = mensajeController.getMensajeById(idMax);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(mensajeService, times(1)).getMensajeById(idMax);
    }

    @Test
    @DisplayName("GET /obtenerMensaje/{id} - Debería manejar ID en el límite de Integer (Integer.MIN_VALUE)")
    void getMensajeById_ShouldHandleMinIntegerId() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        when(mensajeService.getMensajeById(idMin)).thenReturn(null);

        // Act
        ResponseEntity<MensajeDto> response = mensajeController.getMensajeById(idMin);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(mensajeService, times(1)).getMensajeById(idMin);
    }

    @Test
    @DisplayName("POST /guardar/contacto - Debería manejar mensaje con email vacío (validación fallará)")
    void enviarMensajeContacto_ShouldHandleEmptyEmail() {
        // Arrange
        MensajeDto mensajeDtoEmailVacio = MensajeDto.builder()
                .nombreUsuario("Usuario Test")
                .email("")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        when(mensajeService.enviarMensaje(any(MensajeDto.class)))
                .thenThrow(new RuntimeException("Email no puede estar vacío"));

        // Act
        ResponseEntity<?> response = mensajeController.enviarMensajeContacto(mensajeDtoEmailVacio);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("error", responseBody.get("estado"));

        verify(mensajeService, times(1)).enviarMensaje(mensajeDtoEmailVacio);
    }

    @Test
    @DisplayName("POST /guardar/contacto - Debería manejar mensaje con nombre de usuario vacío")
    void enviarMensajeContacto_ShouldHandleEmptyNombreUsuario() {
        // Arrange
        MensajeDto mensajeDtoNombreVacio = MensajeDto.builder()
                .nombreUsuario("")
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        when(mensajeService.enviarMensaje(any(MensajeDto.class)))
                .thenThrow(new RuntimeException("Nombre de usuario no puede estar vacío"));

        // Act
        ResponseEntity<?> response = mensajeController.enviarMensajeContacto(mensajeDtoNombreVacio);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(mensajeService, times(1)).enviarMensaje(mensajeDtoNombreVacio);
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con el mock
        verifyNoInteractions(mensajeService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en enviarMensajeContacto")
    void verifyExpectedInteractionsInEnviarMensajeContacto() {
        // Arrange
        when(mensajeService.enviarMensaje(any(MensajeDto.class))).thenReturn(mensajeDtoCreado);

        // Act
        mensajeController.enviarMensajeContacto(mensajeDtoValido);

        // Assert - Verificar interacciones exactas
        verify(mensajeService, times(1)).enviarMensaje(mensajeDtoValido);
        verify(mensajeService, never()).getAllMensajes();
        verify(mensajeService, never()).deleteMensaje(anyInt());
        verify(mensajeService, never()).getMensajeById(anyInt());
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en getAllMensajes")
    void verifyExpectedInteractionsInGetAllMensajes() {
        // Arrange
        when(mensajeService.getAllMensajes()).thenReturn(List.of());

        // Act
        mensajeController.getAllMensajes();

        // Assert
        verify(mensajeService, times(1)).getAllMensajes();
        verify(mensajeService, never()).enviarMensaje(any());
        verify(mensajeService, never()).deleteMensaje(anyInt());
        verify(mensajeService, never()).getMensajeById(anyInt());
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en deleteMensaje")
    void verifyExpectedInteractionsInDeleteMensaje() {
        // Arrange
        Integer id = 1;
        doNothing().when(mensajeService).deleteMensaje(id);

        // Act
        mensajeController.deleteMensaje(id);

        // Assert
        verify(mensajeService, times(1)).deleteMensaje(id);
        verify(mensajeService, never()).enviarMensaje(any());
        verify(mensajeService, never()).getAllMensajes();
        verify(mensajeService, never()).getMensajeById(anyInt());
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en getMensajeById")
    void verifyExpectedInteractionsInGetMensajeById() {
        // Arrange
        Integer id = 1;
        when(mensajeService.getMensajeById(id)).thenReturn(mensajeDtoCreado);

        // Act
        mensajeController.getMensajeById(id);

        // Assert
        verify(mensajeService, times(1)).getMensajeById(id);
        verify(mensajeService, never()).enviarMensaje(any());
        verify(mensajeService, never()).getAllMensajes();
        verify(mensajeService, never()).deleteMensaje(anyInt());
    }

    // ==================== TESTS DE VALIDACIÓN DE MENSAJE DTO ====================

    @Test
    @DisplayName("Comparar objeto MensajeDto - Debería ser igual cuando los valores son iguales")
    void mensajeDtoEquals_ShouldBeEqual_WhenSameValues() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        MensajeDto dto1 = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        MensajeDto dto2 = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode deberían ser iguales");
        assertTrue(dto1.equals(dto2), "equals() debería retornar true");
    }

    @Test
    @DisplayName("Comparar objeto MensajeDto - No debería ser igual cuando los valores son diferentes")
    void mensajeDtoEquals_ShouldNotBeEqual_WhenDifferentValues() {
        // Arrange
        LocalDate fecha1 = LocalDate.of(2024, 1, 1);
        LocalDate fecha2 = LocalDate.of(2024, 1, 2);

        MensajeDto dto1 = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .fechaCreacion(fecha1)
                .build();

        MensajeDto dto2 = MensajeDto.builder()
                .id(2)
                .nombreUsuario("María García")
                .email("maria@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .fechaCreacion(fecha2)
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode no deberían ser iguales");
        assertFalse(dto1.equals(dto2), "equals() debería retornar false");
    }

    @Test
    @DisplayName("toString de MensajeDto debería incluir información relevante")
    void mensajeDtoToString_ShouldIncludeRelevantInformation() {
        // Arrange & Act
        String toStringResult = mensajeDtoValido.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("Juan Pérez"), "toString debería contener el nombre de usuario");
        assertTrue(toStringResult.contains("juan.perez@email.com"), "toString debería contener el email");
        assertTrue(toStringResult.contains("Este es un mensaje de prueba"),
                "toString debería contener el mensaje");
        assertTrue(toStringResult.contains("fechaCreacion"), "toString debería contener la fecha de creación");
    }

    @Test
    @DisplayName("Builder de MensajeDto debería crear objetos correctamente")
    void mensajeDtoBuilder_ShouldCreateCorrectly() {
        // Arrange & Act
        LocalDate fecha = LocalDate.now();
        MensajeDto dto = MensajeDto.builder()
                .id(10)
                .nombreUsuario("Test Builder")
                .email("builder@email.com")
                .mensaje("Mensaje del builder con más de 10 caracteres.")
                .fechaCreacion(fecha)
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(10, dto.getId(), "El ID debería ser 10");
        assertEquals("Test Builder", dto.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("builder@email.com", dto.getEmail(), "El email debería coincidir");
        assertEquals("Mensaje del builder con más de 10 caracteres.", dto.getMensaje(),
                "El mensaje debería coincidir");
        assertEquals(fecha, dto.getFechaCreacion(), "La fecha de creación debería coincidir");
    }

    @Test
    @DisplayName("MensajeDto - Debería permitir construir con campos nulos")
    void mensajeDtoBuilder_ShouldAllowNullFields() {
        // Arrange & Act
        MensajeDto dto = MensajeDto.builder()
                .nombreUsuario("Usuario Test")
                .email("test@email.com")
                .mensaje(null)
                .fechaCreacion(null)
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals("Usuario Test", dto.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("test@email.com", dto.getEmail(), "El email debería coincidir");
        assertNull(dto.getMensaje(), "El mensaje debería ser nulo");
        assertNull(dto.getFechaCreacion(), "La fecha de creación debería ser nula");
    }
}