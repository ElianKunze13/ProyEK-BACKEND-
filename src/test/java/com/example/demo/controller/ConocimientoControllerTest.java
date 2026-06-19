package com.example.demo.controller;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoConocimiento;
import com.example.demo.service.ConocimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase ConocimientoController
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class ConocimientoControllerTest {

    @Mock
    private ConocimientoService conocimientoService;

    @InjectMocks
    private ConocimientoController conocimientoController;

    private ConocimientoDto conocimientoDtoValido;
    private ConocimientoDto conocimientoDtoFrontend;
    private ConocimientoDto conocimientoDtoBackend;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("java-logo.png")
                .alt("Logo de Java")
                .build();

        conocimientoDtoValido = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        conocimientoDtoFrontend = ConocimientoDto.builder()
                .id(2)
                .nombre("React")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(ImagenDto.builder()
                        .id(2)
                        .url("react-logo.png")
                        .alt("Logo de React")
                        .build())
                .build();

        conocimientoDtoBackend = ConocimientoDto.builder()
                .id(3)
                .nombre("Spring Boot")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .id(3)
                        .url("spring-logo.png")
                        .alt("Logo de Spring Boot")
                        .build())
                .build();
    }

    // ==================== TESTS GET /todos/educaciones ====================

    @Test
    @DisplayName("GET /todos/educaciones - Debería retornar lista de todos los conocimientos")
    void getAllConocimientos_ShouldReturnAllConocimientos() {
        // Arrange
        List<ConocimientoDto> conocimientosEsperados = Arrays.asList(
                conocimientoDtoValido,
                conocimientoDtoFrontend,
                conocimientoDtoBackend
        );
        when(conocimientoService.getAllConocimientos()).thenReturn(conocimientosEsperados);

        // Act
        ResponseEntity<List<ConocimientoDto>> response = conocimientoController.getAllConocimientos();

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(3, response.getBody().size(), "Debería haber 3 conocimientos");
        assertEquals("Java", response.getBody().get(0).getNombre(), "El primer conocimiento debería ser Java");
        assertEquals("React", response.getBody().get(1).getNombre(), "El segundo conocimiento debería ser React");
        assertEquals("Spring Boot", response.getBody().get(2).getNombre(), "El tercer conocimiento debería ser Spring Boot");

        verify(conocimientoService, times(1)).getAllConocimientos();
    }

    @Test
    @DisplayName("GET /todos/educaciones - Debería retornar lista vacía cuando no hay conocimientos")
    void getAllConocimientos_ShouldReturnEmptyList_WhenNoConocimientos() {
        // Arrange
        when(conocimientoService.getAllConocimientos()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<ConocimientoDto>> response = conocimientoController.getAllConocimientos();

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertTrue(response.getBody().isEmpty(), "La lista debería estar vacía");

        verify(conocimientoService, times(1)).getAllConocimientos();
    }

    @Test
    @DisplayName("GET /todos/educaciones - Debería manejar excepción del servicio")
    void getAllConocimientos_ShouldHandleServiceException() {
        // Arrange
        when(conocimientoService.getAllConocimientos())
                .thenThrow(new RuntimeException("Error al obtener conocimientos"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            conocimientoController.getAllConocimientos();
        }, "Debería propagar la excepción del servicio");

        verify(conocimientoService, times(1)).getAllConocimientos();
    }

    // ==================== TESTS GET /conocimientos/tipo/{tipo} ====================

    @ParameterizedTest
    @EnumSource(TipoConocimiento.class)
    @DisplayName("GET /conocimientos/tipo/{tipo} - Debería retornar conocimientos filtrados por tipo")
    void getConocimientosByTipo_ShouldReturnConocimientosByTipo(TipoConocimiento tipo) {
        // Arrange
        List<ConocimientoDto> conocimientosFiltrados;

        if (tipo == TipoConocimiento.FRONTEND) {
            conocimientosFiltrados = Collections.singletonList(conocimientoDtoFrontend);
        } else if (tipo == TipoConocimiento.BACKEND) {
            conocimientosFiltrados = Arrays.asList(conocimientoDtoValido, conocimientoDtoBackend);
        } else {
            conocimientosFiltrados = Collections.emptyList();
        }

        when(conocimientoService.filtrarPorTipo(tipo)).thenReturn(conocimientosFiltrados);

        // Act
        ResponseEntity<List<ConocimientoDto>> response = conocimientoController.getConocimientosByTipo(tipo);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(conocimientosFiltrados.size(), response.getBody().size(),
                "El tamaño de la lista debería coincidir");

        verify(conocimientoService, times(1)).filtrarPorTipo(tipo);
    }

    @ParameterizedTest
    @EnumSource(TipoConocimiento.class)
    @DisplayName("GET /conocimientos/tipo/{tipo} - Debería retornar lista vacía cuando no hay conocimientos del tipo")
    void getConocimientosByTipo_ShouldReturnEmptyList_WhenNoConocimientosOfType(TipoConocimiento tipo) {
        // Arrange
        when(conocimientoService.filtrarPorTipo(tipo)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<ConocimientoDto>> response = conocimientoController.getConocimientosByTipo(tipo);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertTrue(response.getBody().isEmpty(), "La lista debería estar vacía");

        verify(conocimientoService, times(1)).filtrarPorTipo(tipo);
    }

    @Test
    @DisplayName("GET /conocimientos/tipo/{tipo} - Debería manejar tipo nulo - Test negativo")
    void getConocimientosByTipo_ShouldHandleNullTipo() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            conocimientoController.getConocimientosByTipo(null);
        }, "Debería lanzar NullPointerException cuando el tipo es nulo");

        verify(conocimientoService, never()).filtrarPorTipo(any());
    }

    @Test
    @DisplayName("GET /conocimientos/tipo/{tipo} - Debería manejar excepción del servicio")
    void getConocimientosByTipo_ShouldHandleServiceException() {
        // Arrange
        TipoConocimiento tipo = TipoConocimiento.FRONTEND;
        when(conocimientoService.filtrarPorTipo(tipo))
                .thenThrow(new RuntimeException("Error al filtrar conocimientos"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            conocimientoController.getConocimientosByTipo(tipo);
        }, "Debería propagar la excepción del servicio");

        verify(conocimientoService, times(1)).filtrarPorTipo(tipo);
    }

    // ==================== TESTS GET /conocimientos/filtrar ====================

    @ParameterizedTest
    @EnumSource(TipoConocimiento.class)
    @DisplayName("GET /conocimientos/filtrar - Debería retornar conocimientos filtrados por parámetro")
    void getConocimientosByTipoParam_ShouldReturnConocimientosByTipoParam(TipoConocimiento tipo) {
        // Arrange
        List<ConocimientoDto> conocimientosFiltrados = Collections.singletonList(conocimientoDtoValido);
        when(conocimientoService.filtrarPorTipo(tipo)).thenReturn(conocimientosFiltrados);

        // Act
        ResponseEntity<List<ConocimientoDto>> response = conocimientoController.getConocimientosByTipoParam(tipo);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(1, response.getBody().size(), "Debería haber 1 conocimiento");

        verify(conocimientoService, times(1)).filtrarPorTipo(tipo);
    }

    @Test
    @DisplayName("GET /conocimientos/filtrar - Debería manejar tipo nulo")
    void getConocimientosByTipoParam_ShouldHandleNullTipo() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            conocimientoController.getConocimientosByTipoParam(null);
        }, "Debería lanzar NullPointerException cuando el tipo es nulo");

        verify(conocimientoService, never()).filtrarPorTipo(any());
    }

    @Test
    @DisplayName("GET /conocimientos/filtrar - Debería manejar excepción del servicio")
    void getConocimientosByTipoParam_ShouldHandleServiceException() {
        // Arrange
        TipoConocimiento tipo = TipoConocimiento.BACKEND;
        when(conocimientoService.filtrarPorTipo(tipo))
                .thenThrow(new RuntimeException("Error al filtrar conocimientos"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            conocimientoController.getConocimientosByTipoParam(tipo);
        }, "Debería propagar la excepción del servicio");

        verify(conocimientoService, times(1)).filtrarPorTipo(tipo);
    }

    // ==================== TESTS POST /auth/guardar/conocimiento ====================

    @Test
    @DisplayName("POST /auth/guardar/conocimiento - Debería crear conocimiento y retornar 201 Created")
    void saveConocimiento_ShouldCreateConocimientoAndReturnCreated() {
        // Arrange
        ConocimientoDto conocimientoDtoCrear = ConocimientoDto.builder()
                .nombre("Python")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .url("python-logo.png")
                        .alt("Logo de Python")
                        .build())
                .build();

        ConocimientoDto conocimientoDtoCreado = ConocimientoDto.builder()
                .id(4)
                .nombre("Python")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .id(4)
                        .url("python-logo.png")
                        .alt("Logo de Python")
                        .build())
                .build();

        when(conocimientoService.saveConocimiento(any(ConocimientoDto.class))).thenReturn(conocimientoDtoCreado);

        // Act
        ResponseEntity<ConocimientoDto> response = conocimientoController.saveConocimiento(conocimientoDtoCrear);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "El status code debería ser 201 Created");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(4, response.getBody().getId(), "El ID generado debería ser 4");
        assertEquals("Python", response.getBody().getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.INTERMEDIO, response.getBody().getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.BACKEND, response.getBody().getTipoConocimiento(), "El tipo debería coincidir");

        verify(conocimientoService, times(1)).saveConocimiento(conocimientoDtoCrear);
    }

    @Test
    @DisplayName("POST /auth/guardar/conocimiento - Debería crear conocimiento sin imagen")
    void saveConocimiento_ShouldCreateConocimientoWithoutImage() {
        // Arrange
        ConocimientoDto conocimientoDtoSinImagen = ConocimientoDto.builder()
                .nombre("Docker")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(null)
                .build();

        when(conocimientoService.saveConocimiento(any(ConocimientoDto.class)))
                .thenReturn(conocimientoDtoSinImagen);

        // Act
        ResponseEntity<ConocimientoDto> response = conocimientoController.saveConocimiento(conocimientoDtoSinImagen);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "El status code debería ser 201 Created");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertNull(response.getBody().getImagen(), "La imagen debería ser nula");

        verify(conocimientoService, times(1)).saveConocimiento(conocimientoDtoSinImagen);
    }

    @Test
    @DisplayName("POST /auth/guardar/conocimiento - Debería manejar conocimiento nulo - Test negativo")
    void saveConocimiento_ShouldHandleNullConocimiento() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            conocimientoController.saveConocimiento(null);
        }, "Debería lanzar NullPointerException cuando el conocimiento es nulo");

        verify(conocimientoService, never()).saveConocimiento(any());
    }

    @Test
    @DisplayName("POST /auth/guardar/conocimiento - Debería manejar excepción del servicio")
    void saveConocimiento_ShouldHandleServiceException() {
        // Arrange
        when(conocimientoService.saveConocimiento(any(ConocimientoDto.class)))
                .thenThrow(new RuntimeException("Error al crear conocimiento"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            conocimientoController.saveConocimiento(conocimientoDtoValido);
        }, "Debería propagar la excepción del servicio");

        verify(conocimientoService, times(1)).saveConocimiento(conocimientoDtoValido);
    }

    // ==================== TESTS PUT /auth/modificar/conocimiento/{id} ====================

    @Test
    @DisplayName("PUT /auth/modificar/conocimiento/{id} - Debería actualizar conocimiento y retornar 200 OK")
    void updateConocimiento_ShouldUpdateConocimientoAndReturnOk() {
        // Arrange
        Integer id = 1;
        ConocimientoDto conocimientoDtoActualizado = ConocimientoDto.builder()
                .id(id)
                .nombre("Java 17")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .id(1)
                        .url("java17-logo.png")
                        .alt("Logo de Java 17")
                        .build())
                .build();

        when(conocimientoService.actualizarConocimientoPorId(eq(id), any(ConocimientoDto.class)))
                .thenReturn(conocimientoDtoActualizado);

        // Act
        ResponseEntity<ConocimientoDto> response = conocimientoController.updateConocimiento(id, conocimientoDtoActualizado);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(id, response.getBody().getId(), "El ID debería coincidir");
        assertEquals("Java 17", response.getBody().getNombre(), "El nombre debería estar actualizado");
        assertEquals(Nivel.AVANZADO, response.getBody().getNivel(), "El nivel debería ser AVANZADO");

        verify(conocimientoService, times(1)).actualizarConocimientoPorId(eq(id), any(ConocimientoDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/conocimiento/{id} - Debería retornar 404 cuando el ID no existe")
    void updateConocimiento_ShouldReturnNotFound_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(conocimientoService.actualizarConocimientoPorId(eq(id), any(ConocimientoDto.class)))
                .thenThrow(new RuntimeException("Conocimiento no encontrado"));

        // Act
        ResponseEntity<ConocimientoDto> response = conocimientoController.updateConocimiento(id, conocimientoDtoValido);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "El status code debería ser 404 Not Found");
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo");

        verify(conocimientoService, times(1)).actualizarConocimientoPorId(eq(id), any(ConocimientoDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/conocimiento/{id} - Debería manejar ID nulo - Test negativo")
    void updateConocimiento_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            conocimientoController.updateConocimiento(id, conocimientoDtoValido);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(conocimientoService, never()).actualizarConocimientoPorId(any(), any());
    }

    @Test
    @DisplayName("PUT /auth/modificar/conocimiento/{id} - Debería manejar conocimiento nulo - Test negativo")
    void updateConocimiento_ShouldHandleNullConocimientoDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            conocimientoController.updateConocimiento(id, null);
        }, "Debería lanzar NullPointerException cuando el conocimiento es nulo");

        verify(conocimientoService, never()).actualizarConocimientoPorId(any(), any());
    }

    @Test
    @DisplayName("PUT /auth/modificar/conocimiento/{id} - Debería manejar ID negativo")
    void updateConocimiento_ShouldHandleNegativeId() {
        // Arrange
        Integer id = -1;
        when(conocimientoService.actualizarConocimientoPorId(eq(id), any(ConocimientoDto.class)))
                .thenThrow(new RuntimeException("Conocimiento no encontrado"));

        // Act
        ResponseEntity<ConocimientoDto> response = conocimientoController.updateConocimiento(id, conocimientoDtoValido);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(conocimientoService, times(1)).actualizarConocimientoPorId(eq(id), any(ConocimientoDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/conocimiento/{id} - Debería manejar excepción diferente a RuntimeException")
    void updateConocimiento_ShouldHandleOtherException() {
        // Arrange
        Integer id = 1;
        when(conocimientoService.actualizarConocimientoPorId(eq(id), any(ConocimientoDto.class)))
                .thenThrow(new IllegalArgumentException("Error de validación"));

        // Act
        ResponseEntity<ConocimientoDto> response = conocimientoController.updateConocimiento(id, conocimientoDtoValido);

        // Assert - El catch solo captura RuntimeException, por lo que IllegalArgumentException se propaga
        assertThrows(IllegalArgumentException.class, () -> {
            conocimientoController.updateConocimiento(id, conocimientoDtoValido);
        });

        verify(conocimientoService, times(1)).actualizarConocimientoPorId(eq(id), any(ConocimientoDto.class));
    }

    // ==================== TESTS DELETE /borrar/conocimiento/{id} ====================

    @Test
    @DisplayName("DELETE /borrar/conocimiento/{id} - Debería eliminar conocimiento y retornar 204 No Content")
    void deleteConocimientoById_ShouldDeleteConocimientoAndReturnNoContent() {
        // Arrange
        Integer id = 1;
        doNothing().when(conocimientoService).deleteConocimientoById(id);

        // Act
        ResponseEntity<Void> response = conocimientoController.deleteConocimientoById(id);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "El status code debería ser 204 No Content");
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo");

        verify(conocimientoService, times(1)).deleteConocimientoById(id);
    }

    @Test
    @DisplayName("DELETE /borrar/conocimiento/{id} - Debería manejar ID nulo - Test negativo")
    void deleteConocimientoById_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            conocimientoController.deleteConocimientoById(id);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(conocimientoService, never()).deleteConocimientoById(any());
    }

    @Test
    @DisplayName("DELETE /borrar/conocimiento/{id} - Debería manejar ID negativo")
    void deleteConocimientoById_ShouldHandleNegativeId() {
        // Arrange
        Integer id = -1;
        doNothing().when(conocimientoService).deleteConocimientoById(id);

        // Act
        ResponseEntity<Void> response = conocimientoController.deleteConocimientoById(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(conocimientoService, times(1)).deleteConocimientoById(id);
    }

    @Test
    @DisplayName("DELETE /borrar/conocimiento/{id} - Debería manejar ID no existente")
    void deleteConocimientoById_ShouldHandleNonExistentId() {
        // Arrange
        Integer id = 999;
        doThrow(new RuntimeException("Conocimiento no encontrado"))
                .when(conocimientoService).deleteConocimientoById(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            conocimientoController.deleteConocimientoById(id);
        }, "Debería propagar la excepción del servicio");

        verify(conocimientoService, times(1)).deleteConocimientoById(id);
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("GET /conocimientos/tipo/{tipo} - Debería manejar todos los tipos de conocimiento")
    void getConocimientosByTipo_ShouldHandleAllTipoConocimiento() {
        // Arrange
        for (TipoConocimiento tipo : TipoConocimiento.values()) {
            when(conocimientoService.filtrarPorTipo(tipo))
                    .thenReturn(Collections.singletonList(conocimientoDtoValido));

            // Act
            ResponseEntity<List<ConocimientoDto>> response = conocimientoController.getConocimientosByTipo(tipo);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isEmpty());
        }

        verify(conocimientoService, times(TipoConocimiento.values().length))
                .filtrarPorTipo(any(TipoConocimiento.class));
    }

    @Test
    @DisplayName("GET /conocimientos/filtrar - Debería manejar todos los tipos de conocimiento")
    void getConocimientosByTipoParam_ShouldHandleAllTipoConocimiento() {
        // Arrange
        for (TipoConocimiento tipo : TipoConocimiento.values()) {
            when(conocimientoService.filtrarPorTipo(tipo))
                    .thenReturn(Collections.singletonList(conocimientoDtoValido));

            // Act
            ResponseEntity<List<ConocimientoDto>> response = conocimientoController.getConocimientosByTipoParam(tipo);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isEmpty());
        }

        verify(conocimientoService, times(TipoConocimiento.values().length))
                .filtrarPorTipo(any(TipoConocimiento.class));
    }

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void shouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        doNothing().when(conocimientoService).deleteConocimientoById(idMax);

        // Act
        ResponseEntity<Void> response = conocimientoController.deleteConocimientoById(idMax);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(conocimientoService, times(1)).deleteConocimientoById(idMax);
    }

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MIN_VALUE)")
    void shouldHandleMinIntegerId() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        doNothing().when(conocimientoService).deleteConocimientoById(idMin);

        // Act
        ResponseEntity<Void> response = conocimientoController.deleteConocimientoById(idMin);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(conocimientoService, times(1)).deleteConocimientoById(idMin);
    }

    @Test
    @DisplayName("ConocimientoDto - Debería crear objeto con builder correctamente")
    void conocimientoDtoBuilder_ShouldCreateCorrectly() {
        // Arrange & Act
        ImagenDto imagen = ImagenDto.builder()
                .id(5)
                .url("test-logo.png")
                .alt("Logo de prueba")
                .build();

        ConocimientoDto dto = ConocimientoDto.builder()
                .id(10)
                .nombre("Test Knowledge")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.TESTING)
                .imagen(imagen)
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(10, dto.getId(), "El ID debería ser 10");
        assertEquals("Test Knowledge", dto.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.INTERMEDIO, dto.getNivel(), "El nivel debería ser INTERMEDIO");
        assertEquals(TipoConocimiento.TESTING, dto.getTipoConocimiento(), "El tipo debería ser TESTING");
        assertNotNull(dto.getImagen(), "La imagen no debería ser nula");
        assertEquals("test-logo.png", dto.getImagen().getUrl(), "La URL de la imagen debería coincidir");
    }

    @Test
    @DisplayName("ConocimientoDto - Debería permitir valores nulos en campos opcionales")
    void conocimientoDto_ShouldAllowNullValues() {
        // Arrange & Act
        ConocimientoDto dto = ConocimientoDto.builder()
                .id(1)
                .nombre("Test")
                .nivel(null)
                .tipoConocimiento(null)
                .imagen(null)
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(1, dto.getId(), "El ID debería ser 1");
        assertEquals("Test", dto.getNombre(), "El nombre debería coincidir");
        assertNull(dto.getNivel(), "El nivel debería ser nulo");
        assertNull(dto.getTipoConocimiento(), "El tipo debería ser nulo");
        assertNull(dto.getImagen(), "La imagen debería ser nula");
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con el mock
        verifyNoInteractions(conocimientoService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas")
    void verifyExpectedInteractions() {
        // Arrange
        Integer id = 1;
        when(conocimientoService.getAllConocimientos()).thenReturn(Collections.emptyList());

        // Act
        conocimientoController.getAllConocimientos();

        // Assert - Verificar interacciones exactas
        verify(conocimientoService, times(1)).getAllConocimientos();
        verify(conocimientoService, never()).filtrarPorTipo(any());
        verify(conocimientoService, never()).saveConocimiento(any());
        verify(conocimientoService, never()).actualizarConocimientoPorId(any(), any());
        verify(conocimientoService, never()).deleteConocimientoById(any());
    }

    // ==================== TESTS DE COMPARACIÓN ====================

    @Test
    @DisplayName("Comparar objeto ConocimientoDto - Debería ser igual cuando los valores son iguales")
    void conocimientoDtoEquals_ShouldBeEqual_WhenSameValues() {
        // Arrange
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("logo.png")
                .alt("Logo")
                .build();

        ConocimientoDto dto1 = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        ConocimientoDto dto2 = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode deberían ser iguales");
        assertTrue(dto1.equals(dto2), "equals() debería retornar true");
    }

    @Test
    @DisplayName("Comparar objeto ConocimientoDto - No debería ser igual cuando los valores son diferentes")
    void conocimientoDtoEquals_ShouldNotBeEqual_WhenDifferentValues() {
        // Arrange
        ConocimientoDto dto1 = ConocimientoDto.builder()
                .id(1)
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        ConocimientoDto dto2 = ConocimientoDto.builder()
                .id(2)
                .nombre("Python")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode no deberían ser iguales");
        assertFalse(dto1.equals(dto2), "equals() debería retornar false");
    }

    @Test
    @DisplayName("toString de ConocimientoDto debería incluir información relevante")
    void conocimientoDtoToString_ShouldIncludeRelevantInformation() {
        // Arrange & Act
        String toStringResult = conocimientoDtoValido.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Java"), "toString debería contener el nombre");
        assertTrue(toStringResult.contains("AVANZADO"), "toString debería contener el nivel");
        assertTrue(toStringResult.contains("BACKEND"), "toString debería contener el tipo");
    }
}