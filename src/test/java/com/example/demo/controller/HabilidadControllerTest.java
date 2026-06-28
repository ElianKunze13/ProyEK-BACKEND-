package com.example.demo.controller;

import com.example.demo.dto.HabilidadDto;
import com.example.demo.service.HabilidadService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase HabilidadController
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class HabilidadControllerTest {

    @Mock
    private HabilidadService habilidadService;

    @InjectMocks
    private HabilidadController habilidadController;

    private HabilidadDto habilidadDtoValido;
    private HabilidadDto habilidadDtoValido2;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        habilidadDtoValido = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        habilidadDtoValido2 = HabilidadDto.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .build();
    }

    // ==================== TESTS GET /todas/habilidades ====================

    @Test
    @DisplayName("GET /todas/habilidades - Debería retornar lista de habilidades cuando existen")
    void getAllHabilidades_ShouldReturnListOfHabilidades_WhenExists() {
        // Arrange - Configurar comportamiento del mock
        List<HabilidadDto> listaHabilidades = Arrays.asList(habilidadDtoValido, habilidadDtoValido2);
        when(habilidadService.getAllHabilidad()).thenReturn(listaHabilidades);

        // Act - Ejecutar el método del controlador
        ResponseEntity<List<HabilidadDto>> response = habilidadController.getAllHabilidades();

        // Assert - Verificar resultados
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(2, response.getBody().size(), "Debería retornar 2 habilidades");
        assertEquals("Comunicación Efectiva", response.getBody().get(0).getNombre(), "El nombre de la primera habilidad debería coincidir");
        assertEquals("Trabajo en Equipo", response.getBody().get(1).getNombre(), "El nombre de la segunda habilidad debería coincidir");

        // Verificar que se llamó al servicio exactamente una vez
        verify(habilidadService, times(1)).getAllHabilidad();
    }

    @Test
    @DisplayName("GET /todas/habilidades - Debería retornar lista vacía cuando no hay habilidades")
    void getAllHabilidades_ShouldReturnEmptyList_WhenNoHabilidadesExist() {
        // Arrange
        when(habilidadService.getAllHabilidad()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<HabilidadDto>> response = habilidadController.getAllHabilidades();

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "El cuerpo no debería ser nulo");
        assertTrue(response.getBody().isEmpty(), "La lista debería estar vacía");

        verify(habilidadService, times(1)).getAllHabilidad();
    }

    @Test
    @DisplayName("GET /todas/habilidades - Debería manejar excepción del servicio")
    void getAllHabilidades_ShouldHandleServiceException() {
        // Arrange
        when(habilidadService.getAllHabilidad())
                .thenThrow(new RuntimeException("Error al obtener habilidades"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadController.getAllHabilidades();
        }, "Debería propagar la excepción del servicio");

        verify(habilidadService, times(1)).getAllHabilidad();
    }

    // ==================== TESTS POST /auth/guardar/habilidad ====================

    @Test
    @DisplayName("POST /auth/guardar/habilidad - Debería crear habilidad y retornar 201 Created")
    void saveHabilidad_ShouldCreateHabilidadAndReturnCreated() {
        // Arrange
        HabilidadDto habilidadDtoCrear = HabilidadDto.builder()
                .nombre("Adaptabilidad")
                .build();

        HabilidadDto habilidadDtoCreada = HabilidadDto.builder()
                .id(3)
                .nombre("Adaptabilidad")
                .build();

        when(habilidadService.saveHabilidad(any(HabilidadDto.class))).thenReturn(habilidadDtoCreada);

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.saveHabilidad(habilidadDtoCrear);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "El status code debería ser 201 Created");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(3, response.getBody().getId(), "El ID generado debería ser 3");
        assertEquals("Adaptabilidad", response.getBody().getNombre(), "El nombre debería coincidir");

        // Verificar que se llamó al servicio exactamente una vez
        verify(habilidadService, times(1)).saveHabilidad(any(HabilidadDto.class));
    }

    @Test
    @DisplayName("POST /auth/guardar/habilidad - Debería manejar creación con nombre válido")
    void saveHabilidad_ShouldHandleValidNombre() {
        // Arrange
        HabilidadDto habilidadDtoCrear = HabilidadDto.builder()
                .nombre("Liderazgo")
                .build();

        when(habilidadService.saveHabilidad(any(HabilidadDto.class))).thenReturn(habilidadDtoCrear);

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.saveHabilidad(habilidadDtoCrear);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Liderazgo", response.getBody().getNombre());

        verify(habilidadService, times(1)).saveHabilidad(any(HabilidadDto.class));
    }

    @Test
    @DisplayName("POST /auth/guardar/habilidad - Debería manejar habilidad con nombre vacío")
    void saveHabilidad_ShouldHandleHabilidadWithEmptyNombre() {
        // Arrange
        HabilidadDto habilidadDtoVacio = HabilidadDto.builder()
                .nombre("")
                .build();

        when(habilidadService.saveHabilidad(any(HabilidadDto.class))).thenReturn(habilidadDtoVacio);

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.saveHabilidad(habilidadDtoVacio);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("", response.getBody().getNombre());

        verify(habilidadService, times(1)).saveHabilidad(any(HabilidadDto.class));
    }

    @Test
    @DisplayName("POST /auth/guardar/habilidad - Debería manejar habilidad con nombre nulo")
    void saveHabilidad_ShouldHandleHabilidadWithNullNombre() {
        // Arrange
        HabilidadDto habilidadDtoNulo = HabilidadDto.builder()
                .nombre(null)
                .build();

        when(habilidadService.saveHabilidad(any(HabilidadDto.class))).thenReturn(habilidadDtoNulo);

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.saveHabilidad(habilidadDtoNulo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getNombre());

        verify(habilidadService, times(1)).saveHabilidad(any(HabilidadDto.class));
    }

    @Test
    @DisplayName("POST /auth/guardar/habilidad - Debería manejar habilidad nula - Test negativo")
    void saveHabilidad_ShouldHandleNullHabilidad() {
        // Act & Assert - Verificar que se lanza NullPointerException
        assertThrows(NullPointerException.class, () -> {
            habilidadController.saveHabilidad(null);
        }, "Debería lanzar NullPointerException cuando la habilidad es nula");

        // Verificar que nunca se llamó al servicio
        verify(habilidadService, never()).saveHabilidad(any());
    }

    @Test
    @DisplayName("POST /auth/guardar/habilidad - Debería manejar excepción del servicio")
    void saveHabilidad_ShouldHandleServiceException() {
        // Arrange
        when(habilidadService.saveHabilidad(any(HabilidadDto.class)))
                .thenThrow(new RuntimeException("Error al guardar habilidad"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadController.saveHabilidad(habilidadDtoValido);
        }, "Debería propagar la excepción del servicio");

        verify(habilidadService, times(1)).saveHabilidad(any(HabilidadDto.class));
    }

    // ==================== TESTS PUT /auth/modificar/habilidad ====================

    @Test
    @DisplayName("PUT /auth/modificar/habilidad - Debería actualizar habilidad y retornar 200 OK")
    void updateHabilidad_ShouldUpdateHabilidadAndReturnOk() {
        // Arrange
        Integer id = 1;
        HabilidadDto habilidadDtoActualizado = HabilidadDto.builder()
                .id(id)
                .nombre("Comunicación Avanzada")
                .build();

        when(habilidadService.actualizarHabilidadPorId(eq(id), any(HabilidadDto.class)))
                .thenReturn(habilidadDtoActualizado);

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.updateExperiencia(id, habilidadDtoActualizado);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(id, response.getBody().getId(), "El ID debería coincidir");
        assertEquals("Comunicación Avanzada", response.getBody().getNombre(), "El nombre debería estar actualizado");

        // Verificar que se llamó al servicio exactamente una vez
        verify(habilidadService, times(1)).actualizarHabilidadPorId(eq(id), any(HabilidadDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/habilidad - Debería retornar 404 cuando el ID no existe")
    void updateHabilidad_ShouldReturnNotFound_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(habilidadService.actualizarHabilidadPorId(eq(id), any(HabilidadDto.class)))
                .thenThrow(new RuntimeException("Habilidad no encontrada"));

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.updateExperiencia(id, habilidadDtoValido);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "El status code debería ser 404 Not Found");
        assertNull(response.getBody(), "El cuerpo debería ser nulo cuando no se encuentra la habilidad");

        verify(habilidadService, times(1)).actualizarHabilidadPorId(eq(id), any(HabilidadDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/habilidad - Debería manejar ID nulo - Test negativo")
    void updateHabilidad_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            habilidadController.updateExperiencia(id, habilidadDtoValido);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(habilidadService, never()).actualizarHabilidadPorId(any(), any());
    }

    @Test
    @DisplayName("PUT /auth/modificar/habilidad - Debería manejar habilidad nula - Test negativo")
    void updateHabilidad_ShouldHandleNullHabilidadDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            habilidadController.updateExperiencia(id, null);
        }, "Debería lanzar NullPointerException cuando la habilidad es nula");

        verify(habilidadService, never()).actualizarHabilidadPorId(any(), any());
    }

    @Test
    @DisplayName("PUT /auth/modificar/habilidad - Debería manejar ID con formato inválido")
    void updateHabilidad_ShouldHandleInvalidIdFormat() {
        // Arrange
        Integer id = -1;
        when(habilidadService.actualizarHabilidadPorId(eq(id), any(HabilidadDto.class)))
                .thenThrow(new RuntimeException("Habilidad no encontrada"));

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.updateExperiencia(id, habilidadDtoValido);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(habilidadService, times(1)).actualizarHabilidadPorId(eq(id), any(HabilidadDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/habilidad - Debería manejar actualización con nombre vacío")
    void updateHabilidad_ShouldHandleUpdateWithEmptyNombre() {
        // Arrange
        Integer id = 1;
        HabilidadDto habilidadDtoVacio = HabilidadDto.builder()
                .id(id)
                .nombre("")
                .build();

        when(habilidadService.actualizarHabilidadPorId(eq(id), any(HabilidadDto.class)))
                .thenReturn(habilidadDtoVacio);

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.updateExperiencia(id, habilidadDtoVacio);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("", response.getBody().getNombre());

        verify(habilidadService, times(1)).actualizarHabilidadPorId(eq(id), any(HabilidadDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/habilidad - Debería manejar excepción del servicio")
    void updateHabilidad_ShouldHandleServiceException() {
        // Arrange
        Integer id = 1;
        when(habilidadService.actualizarHabilidadPorId(eq(id), any(HabilidadDto.class)))
                .thenThrow(new RuntimeException("Error al actualizar habilidad"));

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.updateExperiencia(id, habilidadDtoValido);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(habilidadService, times(1)).actualizarHabilidadPorId(eq(id), any(HabilidadDto.class));
    }

    // ==================== TESTS DELETE /borrar/habilidad/{id} ====================

    @Test
    @DisplayName("DELETE /borrar/habilidad/{id} - Debería eliminar habilidad y retornar 204 No Content")
    void deleteHabilidadById_ShouldDeleteHabilidadAndReturnNoContent() {
        // Arrange
        Integer id = 1;
        doNothing().when(habilidadService).deleteHabilidadPorId(id);

        // Act
        ResponseEntity<Void> response = habilidadController.deleteHabilidadById(id);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "El status code debería ser 204 No Content");
        assertNull(response.getBody(), "El cuerpo debería ser nulo");

        verify(habilidadService, times(1)).deleteHabilidadPorId(id);
    }

    @Test
    @DisplayName("DELETE /borrar/habilidad/{id} - Debería manejar ID nulo - Test negativo")
    void deleteHabilidadById_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            habilidadController.deleteHabilidadById(id);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(habilidadService, never()).deleteHabilidadPorId(any());
    }

    @Test
    @DisplayName("DELETE /borrar/habilidad/{id} - Debería manejar ID no existente")
    void deleteHabilidadById_ShouldHandleNonExistentId() {
        // Arrange
        Integer id = 999;
        doThrow(new RuntimeException("Habilidad no encontrada")).when(habilidadService).deleteHabilidadPorId(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadController.deleteHabilidadById(id);
        }, "Debería propagar la excepción del servicio");

        verify(habilidadService, times(1)).deleteHabilidadPorId(id);
    }

    @Test
    @DisplayName("DELETE /borrar/habilidad/{id} - Debería manejar ID con formato inválido")
    void deleteHabilidadById_ShouldHandleInvalidIdFormat() {
        // Arrange
        Integer id = -1;
        doThrow(new RuntimeException("Error al eliminar")).when(habilidadService).deleteHabilidadPorId(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadController.deleteHabilidadById(id);
        });

        verify(habilidadService, times(1)).deleteHabilidadPorId(id);
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void shouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        doNothing().when(habilidadService).deleteHabilidadPorId(idMax);

        // Act
        ResponseEntity<Void> response = habilidadController.deleteHabilidadById(idMax);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(habilidadService, times(1)).deleteHabilidadPorId(idMax);
    }

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MIN_VALUE)")
    void shouldHandleMinIntegerId() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        doThrow(new RuntimeException("Error al eliminar")).when(habilidadService).deleteHabilidadPorId(idMin);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadController.deleteHabilidadById(idMin);
        });

        verify(habilidadService, times(1)).deleteHabilidadPorId(idMin);
    }

    @Test
    @DisplayName("GET /todas/habilidades - Debería manejar lista con una sola habilidad")
    void getAllHabilidades_ShouldHandleSingleHabilidad() {
        // Arrange
        List<HabilidadDto> listaUnica = Collections.singletonList(habilidadDtoValido);
        when(habilidadService.getAllHabilidad()).thenReturn(listaUnica);

        // Act
        ResponseEntity<List<HabilidadDto>> response = habilidadController.getAllHabilidades();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Comunicación Efectiva", response.getBody().get(0).getNombre());

        verify(habilidadService, times(1)).getAllHabilidad();
    }

    @Test
    @DisplayName("POST /auth/guardar/habilidad - Debería manejar nombre con caracteres especiales")
    void saveHabilidad_ShouldHandleNombreWithSpecialCharacters() {
        // Arrange
        String nombreEspecial = "Comunicación & Trabajo en Equipo! @#$%";
        HabilidadDto habilidadDtoEspecial = HabilidadDto.builder()
                .nombre(nombreEspecial)
                .build();

        when(habilidadService.saveHabilidad(any(HabilidadDto.class))).thenReturn(habilidadDtoEspecial);

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.saveHabilidad(habilidadDtoEspecial);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(nombreEspecial, response.getBody().getNombre());

        verify(habilidadService, times(1)).saveHabilidad(any(HabilidadDto.class));
    }

    @Test
    @DisplayName("POST /auth/guardar/habilidad - Debería manejar nombre muy largo")
    void saveHabilidad_ShouldHandleNombreVeryLong() {
        // Arrange
        String nombreLargo = "A".repeat(145);
        HabilidadDto habilidadDtoLargo = HabilidadDto.builder()
                .nombre(nombreLargo)
                .build();

        when(habilidadService.saveHabilidad(any(HabilidadDto.class))).thenReturn(habilidadDtoLargo);

        // Act
        ResponseEntity<HabilidadDto> response = habilidadController.saveHabilidad(habilidadDtoLargo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(nombreLargo, response.getBody().getNombre());
        assertEquals(145, response.getBody().getNombre().length());

        verify(habilidadService, times(1)).saveHabilidad(any(HabilidadDto.class));
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con el mock
        verifyNoInteractions(habilidadService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas")
    void verifyExpectedInteractions() {
        // Arrange
        when(habilidadService.getAllHabilidad()).thenReturn(Collections.emptyList());

        // Act
        habilidadController.getAllHabilidades();

        // Assert - Verificar interacciones exactas
        verify(habilidadService, times(1)).getAllHabilidad();
        verify(habilidadService, never()).saveHabilidad(any());
        verify(habilidadService, never()).actualizarHabilidadPorId(any(), any());
        verify(habilidadService, never()).deleteHabilidadPorId(any());
    }

    @Test
    @DisplayName("Verificar interacciones secuenciales en saveHabilidad")
    void verifySequentialInteractionsInSaveHabilidad() {
        // Arrange
        when(habilidadService.saveHabilidad(any(HabilidadDto.class))).thenReturn(habilidadDtoValido);

        // Act
        habilidadController.saveHabilidad(habilidadDtoValido);

        // Assert - Verificar que se llamó al servicio
        verify(habilidadService, times(1)).saveHabilidad(any(HabilidadDto.class));
    }

    @Test
    @DisplayName("Verificar interacciones secuenciales en updateHabilidad")
    void verifySequentialInteractionsInUpdateHabilidad() {
        // Arrange
        Integer id = 1;
        when(habilidadService.actualizarHabilidadPorId(eq(id), any(HabilidadDto.class)))
                .thenReturn(habilidadDtoValido);

        // Act
        habilidadController.updateExperiencia(id, habilidadDtoValido);

        // Assert - Verificar que se llamó al servicio
        verify(habilidadService, times(1)).actualizarHabilidadPorId(eq(id), any(HabilidadDto.class));
    }

    // ==================== TESTS DE COMPARACIÓN DE OBJETOS ====================

    @Test
    @DisplayName("Comparar objeto HabilidadDto - Debería ser igual cuando los valores son iguales")
    void habilidadDtoEquals_ShouldBeEqual_WhenSameValues() {
        // Arrange
        HabilidadDto dto1 = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        HabilidadDto dto2 = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode deberían ser iguales");
        assertTrue(dto1.equals(dto2), "equals() debería retornar true");
    }

    @Test
    @DisplayName("Comparar objeto HabilidadDto - No debería ser igual cuando los valores son diferentes")
    void habilidadDtoEquals_ShouldNotBeEqual_WhenDifferentValues() {
        // Arrange
        HabilidadDto dto1 = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        HabilidadDto dto2 = HabilidadDto.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode no deberían ser iguales");
        assertFalse(dto1.equals(dto2), "equals() debería retornar false");
    }

    @Test
    @DisplayName("toString de HabilidadDto debería incluir información relevante")
    void habilidadDtoToString_ShouldIncludeRelevantInformation() {
        // Arrange & Act
        String toStringResult = habilidadDtoValido.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Comunicación Efectiva"), "toString debería contener el nombre");
    }

    @Test
    @DisplayName("Builder de HabilidadDto debería crear objetos correctamente")
    void habilidadDtoBuilder_ShouldCreateCorrectly() {
        // Arrange & Act
        HabilidadDto dto = HabilidadDto.builder()
                .id(10)
                .nombre("Pensamiento Crítico")
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(10, dto.getId(), "El ID debería ser 10");
        assertEquals("Pensamiento Crítico", dto.getNombre(), "El nombre debería coincidir");
    }

    // ==================== TESTS DE MÚLTIPLES OPERACIONES ====================

    @Test
    @DisplayName("Debería realizar operaciones CRUD completas correctamente")
    void shouldPerformCompleteCRUDOperations() {
        // 1. GET - Obtener todas las habilidades
        when(habilidadService.getAllHabilidad()).thenReturn(Collections.singletonList(habilidadDtoValido));
        ResponseEntity<List<HabilidadDto>> getAllResponse = habilidadController.getAllHabilidades();
        assertNotNull(getAllResponse);
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertEquals(1, getAllResponse.getBody().size());

        // 2. POST - Crear una nueva habilidad
        HabilidadDto nuevaHabilidad = HabilidadDto.builder()
                .nombre("Nueva Habilidad")
                .build();
        HabilidadDto habilidadCreada = HabilidadDto.builder()
                .id(3)
                .nombre("Nueva Habilidad")
                .build();
        when(habilidadService.saveHabilidad(any(HabilidadDto.class))).thenReturn(habilidadCreada);
        ResponseEntity<HabilidadDto> postResponse = habilidadController.saveHabilidad(nuevaHabilidad);
        assertNotNull(postResponse);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertEquals("Nueva Habilidad", postResponse.getBody().getNombre());

        // 3. PUT - Actualizar una habilidad
        Integer id = 1;
        HabilidadDto habilidadActualizada = HabilidadDto.builder()
                .id(id)
                .nombre("Habilidad Actualizada")
                .build();
        when(habilidadService.actualizarHabilidadPorId(eq(id), any(HabilidadDto.class)))
                .thenReturn(habilidadActualizada);
        ResponseEntity<HabilidadDto> putResponse = habilidadController.updateExperiencia(id, habilidadActualizada);
        assertNotNull(putResponse);
        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertEquals("Habilidad Actualizada", putResponse.getBody().getNombre());

        // 4. DELETE - Eliminar una habilidad
        doNothing().when(habilidadService).deleteHabilidadPorId(id);
        ResponseEntity<Void> deleteResponse = habilidadController.deleteHabilidadById(id);
        assertNotNull(deleteResponse);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Verificar todas las interacciones
        verify(habilidadService, times(1)).getAllHabilidad();
        verify(habilidadService, times(1)).saveHabilidad(any(HabilidadDto.class));
        verify(habilidadService, times(1)).actualizarHabilidadPorId(eq(id), any(HabilidadDto.class));
        verify(habilidadService, times(1)).deleteHabilidadPorId(id);
    }

    // ==================== TESTS CON DIFERENTES NOMBRES DE HABILIDADES ====================

    @Test
    @DisplayName("POST /auth/guardar/habilidad - Debería manejar diferentes nombres de habilidades blandas")
    void saveHabilidad_ShouldHandleDifferentSoftSkills() {
        // Arrange
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
                "Gestión del Tiempo"
        };

        // Act & Assert
        for (String skill : softSkills) {
            HabilidadDto dto = HabilidadDto.builder()
                    .nombre(skill)
                    .build();

            when(habilidadService.saveHabilidad(any(HabilidadDto.class))).thenReturn(dto);

            ResponseEntity<HabilidadDto> response = habilidadController.saveHabilidad(dto);

            assertNotNull(response);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(skill, response.getBody().getNombre());
        }

        verify(habilidadService, times(softSkills.length)).saveHabilidad(any(HabilidadDto.class));
    }
}