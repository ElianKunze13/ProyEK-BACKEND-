package com.example.demo.controller;

import com.example.demo.dto.EducacionDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TipoEducacion;
import com.example.demo.service.EducacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase EducacionController
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class EducacionControllerTest {

    @Mock
    private EducacionService educacionService;

    @InjectMocks
    private EducacionController educacionController;

    private EducacionDto educacionDtoValido;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        ImagenDto imagen = ImagenDto.builder()
                .id(1)
                .url("titulo.jpg")
                .alt("Imagen del título")
                .build();

        educacionDtoValido = EducacionDto.builder()
                .id(1)
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática con especialización en software")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .build();
    }

    // ==================== TESTS GET /todas/educaciones ====================

    @Test
    @DisplayName("GET /todas/educaciones - Debería retornar lista de educaciones cuando existen")
    void getAllEducaciones_ShouldReturnListOfEducaciones_WhenExist() {
        // Arrange
        EducacionDto educacionDto2 = EducacionDto.builder()
                .id(2)
                .titulo("Máster en Inteligencia Artificial")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Máster oficial en IA y Machine Learning")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        List<EducacionDto> educacionesEsperadas = Arrays.asList(educacionDtoValido, educacionDto2);
        when(educacionService.getAllEducacion()).thenReturn(educacionesEsperadas);

        // Act
        ResponseEntity<List<EducacionDto>> response = educacionController.getAllEducaciones();

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(2, response.getBody().size(), "La lista debería tener 2 elementos");
        assertEquals(educacionesEsperadas, response.getBody(), "La lista debería coincidir");

        // Verificar que se llamó al servicio exactamente una vez
        verify(educacionService, times(1)).getAllEducacion();
    }

    @Test
    @DisplayName("GET /todas/educaciones - Debería retornar lista vacía cuando no hay educaciones")
    void getAllEducaciones_ShouldReturnEmptyList_WhenNoEducacionesExist() {
        // Arrange
        when(educacionService.getAllEducacion()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<EducacionDto>> response = educacionController.getAllEducaciones();

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertTrue(response.getBody().isEmpty(), "La lista debería estar vacía");

        // Verificar que se llamó al servicio exactamente una vez
        verify(educacionService, times(1)).getAllEducacion();
    }

    @Test
    @DisplayName("GET /todas/educaciones - Debería retornar lista con una educación")
    void getAllEducaciones_ShouldReturnListWithOneEducacion() {
        // Arrange
        List<EducacionDto> educacionesEsperadas = Collections.singletonList(educacionDtoValido);
        when(educacionService.getAllEducacion()).thenReturn(educacionesEsperadas);

        // Act
        ResponseEntity<List<EducacionDto>> response = educacionController.getAllEducaciones();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(educacionDtoValido, response.getBody().get(0));

        verify(educacionService, times(1)).getAllEducacion();
    }

    @Test
    @DisplayName("GET /todas/educaciones - Debería manejar excepción del servicio")
    void getAllEducaciones_ShouldHandleServiceException() {
        // Arrange
        when(educacionService.getAllEducacion())
                .thenThrow(new RuntimeException("Error al obtener educaciones"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            educacionController.getAllEducaciones();
        }, "Debería propagar la excepción del servicio");

        verify(educacionService, times(1)).getAllEducacion();
    }

    // ==================== TESTS POST /auth/guardar/educacion ====================

    @Test
    @DisplayName("POST /auth/guardar/educacion - Debería crear educación y retornar 201 Created")
    void saveEducacion_ShouldCreateEducacionAndReturnCreated() {
        // Arrange
        EducacionDto educacionDtoCrear = EducacionDto.builder()
                .titulo("Curso de Spring Boot")
                .fechaObtencion(LocalDate.of(2023, 5, 20))
                .descripcion("Curso avanzado de Spring Boot")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        EducacionDto educacionDtoCreado = EducacionDto.builder()
                .id(3)
                .titulo("Curso de Spring Boot")
                .fechaObtencion(LocalDate.of(2023, 5, 20))
                .descripcion("Curso avanzado de Spring Boot")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        when(educacionService.saveEducacion(any(EducacionDto.class))).thenReturn(educacionDtoCreado);

        // Act
        ResponseEntity<EducacionDto> response = educacionController.saveEducacion(educacionDtoCrear);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "El status code debería ser 201 Created");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(3, response.getBody().getId(), "El ID generado debería ser 3");
        assertEquals("Curso de Spring Boot", response.getBody().getTitulo(), "El título debería coincidir");
        assertEquals(TipoEducacion.INFORMAL_CURSO, response.getBody().getTipoEducacion(), "El tipo debería coincidir");

        // Verificar que se llamó al servicio exactamente una vez
        verify(educacionService, times(1)).saveEducacion(educacionDtoCrear);
    }

    @Test
    @DisplayName("POST /auth/guardar/educacion - Debería manejar creación con datos mínimos válidos")
    void saveEducacion_ShouldHandleMinimalValidData() {
        // Arrange
        EducacionDto educacionDtoMinimo = EducacionDto.builder()
                .titulo("Educación Básica")
                .fechaObtencion(LocalDate.now())
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        when(educacionService.saveEducacion(any(EducacionDto.class))).thenReturn(educacionDtoMinimo);

        // Act
        ResponseEntity<EducacionDto> response = educacionController.saveEducacion(educacionDtoMinimo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Educación Básica", response.getBody().getTitulo());

        verify(educacionService, times(1)).saveEducacion(educacionDtoMinimo);
    }

    @Test
    @DisplayName("POST /auth/guardar/educacion - Debería manejar educación sin imagen")
    void saveEducacion_ShouldHandleEducacionWithoutImage() {
        // Arrange
        EducacionDto educacionDtoSinImagen = EducacionDto.builder()
                .titulo("Curso Online")
                .fechaObtencion(LocalDate.now())
                .descripcion("Curso de Java desde cero")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .imagen(null)
                .build();

        when(educacionService.saveEducacion(any(EducacionDto.class))).thenReturn(educacionDtoSinImagen);

        // Act
        ResponseEntity<EducacionDto> response = educacionController.saveEducacion(educacionDtoSinImagen);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getImagen(), "La imagen debería ser nula");

        verify(educacionService, times(1)).saveEducacion(educacionDtoSinImagen);
    }


    @Test
    @DisplayName("POST /auth/guardar/educacion - Debería manejar educación con todos los tipos de educación")
    @ParameterizedTest
    @EnumSource(TipoEducacion.class)
    void saveEducacion_ShouldHandleAllTipoEducacionValues(TipoEducacion tipo) {
        // Arrange
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo("Educación de tipo " + tipo.name())
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción para " + tipo.name())
                .tipoEducacion(tipo)
                .build();

        when(educacionService.saveEducacion(any(EducacionDto.class))).thenReturn(educacionDto);

        // Act
        ResponseEntity<EducacionDto> response = educacionController.saveEducacion(educacionDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tipo, response.getBody().getTipoEducacion(), "El tipo de educación debería coincidir");

        verify(educacionService, times(1)).saveEducacion(educacionDto);
    }

    @Test
    @DisplayName("POST /auth/guardar/educacion - Debería manejar educación nula - Test negativo")
    void saveEducacion_ShouldHandleNullEducacion() {
        // Act & Assert - Verificar que se lanza NullPointerException
        assertThrows(NullPointerException.class, () -> {
            educacionController.saveEducacion(null);
        }, "Debería lanzar NullPointerException cuando la educación es nula");

        // Verificar que nunca se llamó al servicio
        verify(educacionService, never()).saveEducacion(any());
    }

    @Test
    @DisplayName("POST /auth/guardar/educacion - Debería manejar excepción del servicio")
    void saveEducacion_ShouldHandleServiceException() {
        // Arrange
        when(educacionService.saveEducacion(any(EducacionDto.class)))
                .thenThrow(new RuntimeException("Error al guardar educación"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            educacionController.saveEducacion(educacionDtoValido);
        }, "Debería propagar la excepción del servicio");

        verify(educacionService, times(1)).saveEducacion(educacionDtoValido);
    }

    // ==================== TESTS PUT /auth/modificar/educacion/{id} ====================

    @Test
    @DisplayName("PUT /auth/modificar/educacion/{id} - Debería actualizar educación y retornar 200 OK")
    void updateEducacion_ShouldUpdateEducacionAndReturnOk() {
        // Arrange
        Integer id = 1;
        EducacionDto educacionDtoActualizado = EducacionDto.builder()
                .id(id)
                .titulo("Ingeniería Informática Actualizado")
                .fechaObtencion(LocalDate.of(2021, 6, 15))
                .descripcion("Descripción actualizada con más detalles")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        when(educacionService.actualizarEducacionPorId(eq(id), any(EducacionDto.class)))
                .thenReturn(educacionDtoActualizado);

        // Act
        ResponseEntity<EducacionDto> response = educacionController.updateEducacion(id, educacionDtoActualizado);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(id, response.getBody().getId(), "El ID debería coincidir");
        assertEquals("Ingeniería Informática Actualizado", response.getBody().getTitulo(), "El título debería estar actualizado");
        assertEquals(TipoEducacion.FORMAL, response.getBody().getTipoEducacion(), "El tipo debería coincidir");

        // Verificar que se llamó al servicio exactamente una vez
        verify(educacionService, times(1)).actualizarEducacionPorId(eq(id), any(EducacionDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/educacion/{id} - Debería retornar 404 cuando la educación no existe")
    void updateEducacion_ShouldReturnNotFound_WhenEducacionDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(educacionService.actualizarEducacionPorId(eq(id), any(EducacionDto.class)))
                .thenThrow(new RuntimeException("Educación no encontrada"));

        // Act
        ResponseEntity<EducacionDto> response = educacionController.updateEducacion(id, educacionDtoValido);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "El status code debería ser 404 Not Found");
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo");

        // Verificar que se llamó al servicio exactamente una vez
        verify(educacionService, times(1)).actualizarEducacionPorId(eq(id), any(EducacionDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/educacion/{id} - Debería manejar actualización con ID diferente al del DTO")
    void updateEducacion_ShouldHandleDifferentIdInDto() {
        // Arrange
        Integer idPath = 1;
        EducacionDto educacionDtoConIdDiferente = EducacionDto.builder()
                .id(2)  // ID diferente al de la ruta
                .titulo("Título con ID diferente")
                .fechaObtencion(LocalDate.now())
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        EducacionDto educacionDtoRetornado = EducacionDto.builder()
                .id(1)  // El servicio podría usar el ID de la ruta
                .titulo("Título con ID diferente")
                .fechaObtencion(LocalDate.now())
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        when(educacionService.actualizarEducacionPorId(eq(idPath), any(EducacionDto.class)))
                .thenReturn(educacionDtoRetornado);

        // Act
        ResponseEntity<EducacionDto> response = educacionController.updateEducacion(idPath, educacionDtoConIdDiferente);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId(), "El ID debería ser el de la ruta");

        verify(educacionService, times(1)).actualizarEducacionPorId(eq(idPath), any(EducacionDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/educacion/{id} - Debería manejar ID nulo - Test negativo")
    void updateEducacion_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            educacionController.updateEducacion(id, educacionDtoValido);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(educacionService, never()).actualizarEducacionPorId(any(), any());
    }

    @Test
    @DisplayName("PUT /auth/modificar/educacion/{id} - Debería manejar educación nula - Test negativo")
    void updateEducacion_ShouldHandleNullEducacionDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            educacionController.updateEducacion(id, null);
        }, "Debería lanzar NullPointerException cuando la educación es nula");

        verify(educacionService, never()).actualizarEducacionPorId(any(), any());
    }

    @Test
    @DisplayName("PUT /auth/modificar/educacion/{id} - Debería manejar excepción del servicio no relacionada con NotFound")
    void updateEducacion_ShouldHandleOtherServiceException() {
        // Arrange
        Integer id = 1;
        when(educacionService.actualizarEducacionPorId(eq(id), any(EducacionDto.class)))
                .thenThrow(new RuntimeException("Error inesperado en el servicio"));

        // Act
        ResponseEntity<EducacionDto> response = educacionController.updateEducacion(id, educacionDtoValido);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "El controlador captura cualquier RuntimeException como 404");
        assertNull(response.getBody());

        verify(educacionService, times(1)).actualizarEducacionPorId(eq(id), any(EducacionDto.class));
    }

    // ==================== TESTS DELETE /borrar/educacion/{id} ====================

    @Test
    @DisplayName("DELETE /borrar/educacion/{id} - Debería eliminar educación y retornar 204 No Content")
    void deleteEducacionById_ShouldDeleteEducacionAndReturnNoContent() {
        // Arrange
        Integer id = 1;
        doNothing().when(educacionService).deleteEducacionPorId(id);

        // Act
        ResponseEntity<Void> response = educacionController.deleteEducacionById(id);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "El status code debería ser 204 No Content");
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo");

        // Verificar que se llamó al servicio exactamente una vez
        verify(educacionService, times(1)).deleteEducacionPorId(id);
    }

    @Test
    @DisplayName("DELETE /borrar/educacion/{id} - Debería manejar eliminación de ID no existente")
    void deleteEducacionById_ShouldHandleNonExistentId() {
        // Arrange
        Integer id = 999;
        doNothing().when(educacionService).deleteEducacionPorId(id);

        // Act
        ResponseEntity<Void> response = educacionController.deleteEducacionById(id);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "El status code debería ser 204 No Content");
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo");

        // Verificar que se llamó al servicio exactamente una vez
        verify(educacionService, times(1)).deleteEducacionPorId(id);
    }

    @Test
    @DisplayName("DELETE /borrar/educacion/{id} - Debería manejar ID nulo - Test negativo")
    void deleteEducacionById_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            educacionController.deleteEducacionById(id);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        // Verificar que nunca se llamó al servicio
        verify(educacionService, never()).deleteEducacionPorId(anyInt());
    }

    @Test
    @DisplayName("DELETE /borrar/educacion/{id} - Debería manejar ID negativo")
    void deleteEducacionById_ShouldHandleNegativeId() {
        // Arrange
        Integer id = -1;
        doNothing().when(educacionService).deleteEducacionPorId(id);

        // Act
        ResponseEntity<Void> response = educacionController.deleteEducacionById(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(educacionService, times(1)).deleteEducacionPorId(id);
    }

    @Test
    @DisplayName("DELETE /borrar/educacion/{id} - Debería manejar excepción del servicio")
    void deleteEducacionById_ShouldHandleServiceException() {
        // Arrange
        Integer id = 1;
        doThrow(new RuntimeException("Error al eliminar educación")).when(educacionService).deleteEducacionPorId(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            educacionController.deleteEducacionById(id);
        }, "Debería propagar la excepción del servicio");

        verify(educacionService, times(1)).deleteEducacionPorId(id);
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void shouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        doNothing().when(educacionService).deleteEducacionPorId(idMax);

        // Act
        ResponseEntity<Void> response = educacionController.deleteEducacionById(idMax);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(educacionService, times(1)).deleteEducacionPorId(idMax);
    }

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MIN_VALUE)")
    void shouldHandleMinIntegerId() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        doNothing().when(educacionService).deleteEducacionPorId(idMin);

        // Act
        ResponseEntity<Void> response = educacionController.deleteEducacionById(idMin);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(educacionService, times(1)).deleteEducacionPorId(idMin);
    }

    @Test
    @DisplayName("Debería manejar fechas extremas en educación")
    void shouldHandleExtremeDates() {
        // Arrange
        LocalDate fechaAntigua = LocalDate.of(1900, 1, 1);
        EducacionDto educacionDtoAntiguo = EducacionDto.builder()
                .titulo("Educación antigua")
                .fechaObtencion(fechaAntigua)
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        when(educacionService.saveEducacion(any(EducacionDto.class))).thenReturn(educacionDtoAntiguo);

        // Act
        ResponseEntity<EducacionDto> response = educacionController.saveEducacion(educacionDtoAntiguo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(fechaAntigua, response.getBody().getFechaObtencion());

        verify(educacionService, times(1)).saveEducacion(educacionDtoAntiguo);
    }

    @Test
    @DisplayName("Debería manejar títulos y descripciones muy largos")
    void shouldHandleVeryLongTitlesAndDescriptions() {
        // Arrange
        String tituloLargo = "T".repeat(500);
        String descripcionLarga = "D".repeat(1000);
        EducacionDto educacionDtoLargo = EducacionDto.builder()
                .titulo(tituloLargo)
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionLarga)
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .build();

        when(educacionService.saveEducacion(any(EducacionDto.class))).thenReturn(educacionDtoLargo);

        // Act
        ResponseEntity<EducacionDto> response = educacionController.saveEducacion(educacionDtoLargo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tituloLargo, response.getBody().getTitulo());
        assertEquals(descripcionLarga, response.getBody().getDescripcion());

        verify(educacionService, times(1)).saveEducacion(educacionDtoLargo);
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con el mock
        verifyNoInteractions(educacionService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas")
    void verifyExpectedInteractions() {
        // Arrange
        when(educacionService.getAllEducacion()).thenReturn(Collections.emptyList());

        // Act
        educacionController.getAllEducaciones();

        // Assert - Verificar interacciones exactas
        verify(educacionService, times(1)).getAllEducacion();
        verify(educacionService, never()).saveEducacion(any());
        verify(educacionService, never()).actualizarEducacionPorId(any(), any());
        verify(educacionService, never()).deleteEducacionPorId(any());
    }

    // ==================== TESTS DE COMPARACIÓN DE OBJETOS ====================

    @Test
    @DisplayName("Comparar objeto EducacionDto - Debería ser igual cuando los valores son iguales")
    void educacionDtoEquals_ShouldBeEqual_WhenSameValues() {
        // Arrange
        ImagenDto imagen = ImagenDto.builder().id(1).url("img.jpg").alt("Alt").build();
        LocalDate fecha = LocalDate.of(2020, 6, 15);

        EducacionDto dto1 = EducacionDto.builder()
                .id(1)
                .titulo("Ingeniería")
                .fechaObtencion(fecha)
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .build();

        EducacionDto dto2 = EducacionDto.builder()
                .id(1)
                .titulo("Ingeniería")
                .fechaObtencion(fecha)
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode deberían ser iguales");
        assertTrue(dto1.equals(dto2), "equals() debería retornar true");
    }

    @Test
    @DisplayName("Comparar objeto EducacionDto - No debería ser igual cuando los valores son diferentes")
    void educacionDtoEquals_ShouldNotBeEqual_WhenDifferentValues() {
        // Arrange
        EducacionDto dto1 = EducacionDto.builder()
                .id(1)
                .titulo("Ingeniería")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Descripción A")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        EducacionDto dto2 = EducacionDto.builder()
                .id(2)
                .titulo("Máster")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Descripción B")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode no deberían ser iguales");
        assertFalse(dto1.equals(dto2), "equals() debería retornar false");
    }

    @Test
    @DisplayName("toString de EducacionDto debería incluir información relevante")
    void educacionDtoToString_ShouldIncludeRelevantInformation() {
        // Arrange & Act
        String toStringResult = educacionDtoValido.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Ingeniería Informática"), "toString debería contener el título");
        assertTrue(toStringResult.contains("2020-06-15"), "toString debería contener la fecha");
        assertTrue(toStringResult.contains("FORMAL"), "toString debería contener el tipo");
        assertTrue(toStringResult.contains("descripcion"), "toString debería contener el campo descripcion");
    }

    @Test
    @DisplayName("Builder de EducacionDto debería crear objetos correctamente")
    void educacionDtoBuilder_ShouldCreateCorrectly() {
        // Arrange & Act
        LocalDate fecha = LocalDate.of(2023, 10, 15);
        ImagenDto imagen = ImagenDto.builder().id(5).url("test.jpg").alt("Test").build();

        EducacionDto dto = EducacionDto.builder()
                .id(10)
                .titulo("Curso de Testing")
                .fechaObtencion(fecha)
                .descripcion("Curso de pruebas unitarias con JUnit")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .imagen(imagen)
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(10, dto.getId(), "El ID debería ser 10");
        assertEquals("Curso de Testing", dto.getTitulo(), "El título debería coincidir");
        assertEquals(fecha, dto.getFechaObtencion(), "La fecha debería coincidir");
        assertEquals("Curso de pruebas unitarias con JUnit", dto.getDescripcion(), "La descripción debería coincidir");
        assertEquals(TipoEducacion.INFORMAL_CURSO, dto.getTipoEducacion(), "El tipo debería ser INFORMAL_CURSO");
        assertNotNull(dto.getImagen(), "La imagen no debería ser nula");
        assertEquals("test.jpg", dto.getImagen().getUrl(), "La URL de la imagen debería coincidir");
    }

    @Test
    @DisplayName("EducacionDto debería aceptar valores nulos en campos opcionales")
    void educacionDto_ShouldAcceptNullValuesInOptionalFields() {
        // Arrange & Act
        EducacionDto dto = EducacionDto.builder()
                .id(1)
                .titulo("Título")
                .fechaObtencion(LocalDate.now())
                .descripcion(null)
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(null)
                .build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getDescripcion(), "La descripción debería ser nula");
        assertNull(dto.getImagen(), "La imagen debería ser nula");
    }
}