package com.example.demo.controller;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import com.example.demo.service.ExperienciaService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase ExperienciaController
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class ExperienciaControllerTest {

    @Mock
    private ExperienciaService experienciaService;

    @InjectMocks
    private ExperienciaController experienciaController;

    private Validator validator;

    private ExperienciaDto experienciaDtoValida;
    private ImagenDto imagenDto;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        imagenDto = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Imagen del proyecto")
                .build();

        experienciaDtoValida = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Full Stack")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de una aplicación web completa con Spring Boot y React")
                .link("https://github.com/usuario/proyecto")
                .imagen(imagenDto)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();
    }

    // ==================== TESTS GET /todas/experiencias ====================

    @Test
    @DisplayName("GET /todas/experiencias - Debería retornar lista de experiencias cuando existen")
    void getAllExperiencias_ShouldReturnListOfExperiencias_WhenExists() {
        // Arrange - Configurar comportamiento del mock
        ExperienciaDto experienciaDto2 = ExperienciaDto.builder()
                .id(2)
                .titulo("Proyecto Angular")
                .fechaInicioProyecto(LocalDate.of(2024, 2, 1))
                .fechaFinProyecto(LocalDate.of(2024, 5, 31))
                .descripcion("Aplicación frontend con Angular 17")
                .link("https://github.com/usuario/angular-project")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.ANGULAR)
                .build();

        List<ExperienciaDto> listaExperiencias = Arrays.asList(experienciaDtoValida, experienciaDto2);
        when(experienciaService.getAllExperiencias()).thenReturn(listaExperiencias);

        // Act - Ejecutar el método del controlador
        ResponseEntity<List<ExperienciaDto>> response = experienciaController.getAllExperiencias();

        // Assert - Verificar resultados
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(2, response.getBody().size(), "Debería retornar 2 experiencias");
        assertEquals("Proyecto Full Stack", response.getBody().get(0).getTitulo());
        assertEquals("Proyecto Angular", response.getBody().get(1).getTitulo());

        // Verificar que se llamó al servicio exactamente una vez
        verify(experienciaService, times(1)).getAllExperiencias();
    }

    @Test
    @DisplayName("GET /todas/experiencias - Debería retornar lista vacía cuando no hay experiencias")
    void getAllExperiencias_ShouldReturnEmptyList_WhenNoExperienciasExist() {
        // Arrange
        when(experienciaService.getAllExperiencias()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<ExperienciaDto>> response = experienciaController.getAllExperiencias();

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "El cuerpo no debería ser nulo");
        assertTrue(response.getBody().isEmpty(), "La lista debería estar vacía");

        verify(experienciaService, times(1)).getAllExperiencias();
    }

    @Test
    @DisplayName("GET /todas/experiencias - Debería manejar excepción del servicio")
    void getAllExperiencias_ShouldHandleServiceException() {
        // Arrange
        when(experienciaService.getAllExperiencias())
                .thenThrow(new RuntimeException("Error al obtener experiencias"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            experienciaController.getAllExperiencias();
        }, "Debería propagar la excepción del servicio");

        verify(experienciaService, times(1)).getAllExperiencias();
    }

    // ==================== TESTS POST /auth/guardar/experiencia ====================

    @Test
    @DisplayName("POST /auth/guardar/experiencia - Debería crear experiencia y retornar 201 Created")
    void saveExperiencia_ShouldCreateExperienciaAndReturnCreated() {
        // Arrange
        ExperienciaDto experienciaDtoCrear = ExperienciaDto.builder()
                .titulo("Nuevo Proyecto")
                .fechaInicioProyecto(LocalDate.of(2024, 7, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción del nuevo proyecto con más de 5 caracteres")
                .link("https://github.com/usuario/nuevo-proyecto")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiaUsada(TecnologiaUsada.DJANGO)
                .imagen(imagenDto)
                .build();

        ExperienciaDto experienciaDtoCreada = ExperienciaDto.builder()
                .id(2)
                .titulo("Nuevo Proyecto")
                .fechaInicioProyecto(LocalDate.of(2024, 7, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción del nuevo proyecto con más de 5 caracteres")
                .link("https://github.com/usuario/nuevo-proyecto")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiaUsada(TecnologiaUsada.DJANGO)
                .imagen(imagenDto)
                .build();

        when(experienciaService.saveExperiencia(any(ExperienciaDto.class))).thenReturn(experienciaDtoCreada);

        // Act
        ResponseEntity<ExperienciaDto> response = experienciaController.saveExperiencia(experienciaDtoCrear);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "El status code debería ser 201 Created");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(2, response.getBody().getId(), "El ID generado debería ser 2");
        assertEquals("Nuevo Proyecto", response.getBody().getTitulo());
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO, response.getBody().getTipoExperiencia());
        assertEquals(TecnologiaUsada.DJANGO, response.getBody().getTecnologiaUsada());

        verify(experienciaService, times(1)).saveExperiencia(experienciaDtoCrear);
    }

    @Test
    @DisplayName("POST /auth/guardar/experiencia - Debería manejar experiencia con datos mínimos válidos")
    void saveExperiencia_ShouldHandleMinimalValidData() {
        // Arrange
        ExperienciaDto experienciaDtoMinima = ExperienciaDto.builder()
                .titulo("Proyecto Mínimo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 2, 1))
                .descripcion("Descripción mínima válida de más de 5 caracteres")
                .link("https://minimo.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        when(experienciaService.saveExperiencia(any(ExperienciaDto.class))).thenReturn(experienciaDtoMinima);

        // Act
        ResponseEntity<ExperienciaDto> response = experienciaController.saveExperiencia(experienciaDtoMinima);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(experienciaService, times(1)).saveExperiencia(experienciaDtoMinima);
    }

    @Test
    @DisplayName("POST /auth/guardar/experiencia - Debería manejar experiencia sin imagen")
    void saveExperiencia_ShouldHandleExperienciaWithoutImage() {
        // Arrange
        ExperienciaDto experienciaDtoSinImagen = ExperienciaDto.builder()
                .titulo("Proyecto sin imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 1))
                .descripcion("Descripción válida de más de 5 caracteres para el proyecto sin imagen")
                .link("https://sin-imagen.com")
                .tipoExperiencia(TipoExperiencia.APORTE_CODIGO_ABIERTO)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(null)
                .build();

        when(experienciaService.saveExperiencia(any(ExperienciaDto.class))).thenReturn(experienciaDtoSinImagen);

        // Act
        ResponseEntity<ExperienciaDto> response = experienciaController.saveExperiencia(experienciaDtoSinImagen);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getImagen(), "La imagen debería ser nula");

        verify(experienciaService, times(1)).saveExperiencia(experienciaDtoSinImagen);
    }

    @Test
    @DisplayName("POST /auth/guardar/experiencia - Debería manejar experiencia nula - Test negativo")
    void saveExperiencia_ShouldHandleNullExperiencia() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            experienciaController.saveExperiencia(null);
        }, "Debería lanzar NullPointerException cuando la experiencia es nula");

        verify(experienciaService, never()).saveExperiencia(any());
    }

    @Test
    @DisplayName("POST /auth/guardar/experiencia - Debería manejar excepción del servicio")
    void saveExperiencia_ShouldHandleServiceException() {
        // Arrange
        when(experienciaService.saveExperiencia(any(ExperienciaDto.class)))
                .thenThrow(new RuntimeException("Error al guardar experiencia"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            experienciaController.saveExperiencia(experienciaDtoValida);
        }, "Debería propagar la excepción del servicio");

        verify(experienciaService, times(1)).saveExperiencia(experienciaDtoValida);
    }

    // ==================== TESTS PUT /auth/modificar/experiencia/{id} ====================

    @Test
    @DisplayName("PUT /auth/modificar/experiencia/{id} - Debería actualizar experiencia y retornar 200 OK")
    void updateExperiencia_ShouldUpdateExperienciaAndReturnOk() {
        // Arrange
        Integer id = 1;
        ExperienciaDto experienciaDtoActualizada = ExperienciaDto.builder()
                .id(id)
                .titulo("Proyecto Full Stack Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 8, 31))
                .descripcion("Descripción actualizada del proyecto con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-actualizado")
                .tipoExperiencia(TipoExperiencia.PRACTICA_PROFESIONAL)
                .tecnologiaUsada(TecnologiaUsada.REACT)
                .imagen(imagenDto)
                .build();

        when(experienciaService.actualizarExperienciaPorId(eq(id), any(ExperienciaDto.class)))
                .thenReturn(experienciaDtoActualizada);

        // Act
        ResponseEntity<ExperienciaDto> response = experienciaController.updateExperiencia(id, experienciaDtoActualizada);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El status code debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(id, response.getBody().getId(), "El ID debería coincidir");
        assertEquals("Proyecto Full Stack Actualizado", response.getBody().getTitulo());
        assertEquals(TipoExperiencia.PRACTICA_PROFESIONAL, response.getBody().getTipoExperiencia());
        assertEquals(TecnologiaUsada.REACT, response.getBody().getTecnologiaUsada());

        verify(experienciaService, times(1)).actualizarExperienciaPorId(eq(id), any(ExperienciaDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/experiencia/{id} - Debería retornar 404 cuando el ID no existe")
    void updateExperiencia_ShouldReturnNotFound_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(experienciaService.actualizarExperienciaPorId(eq(id), any(ExperienciaDto.class)))
                .thenThrow(new RuntimeException("Experiencia no encontrada"));

        // Act
        ResponseEntity<ExperienciaDto> response = experienciaController.updateExperiencia(id, experienciaDtoValida);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "El status code debería ser 404 Not Found");
        assertNull(response.getBody(), "El cuerpo debería ser nulo cuando no se encuentra la experiencia");

        verify(experienciaService, times(1)).actualizarExperienciaPorId(eq(id), any(ExperienciaDto.class));
    }

    @Test
    @DisplayName("PUT /auth/modificar/experiencia/{id} - Debería manejar ID nulo - Test negativo")
    void updateExperiencia_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            experienciaController.updateExperiencia(id, experienciaDtoValida);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(experienciaService, never()).actualizarExperienciaPorId(any(), any());
    }

    @Test
    @DisplayName("PUT /auth/modificar/experiencia/{id} - Debería manejar experiencia nula - Test negativo")
    void updateExperiencia_ShouldHandleNullExperienciaDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            experienciaController.updateExperiencia(id, null);
        }, "Debería lanzar NullPointerException cuando la experiencia es nula");

        verify(experienciaService, never()).actualizarExperienciaPorId(any(), any());
    }

    @Test
    @DisplayName("PUT /auth/modificar/experiencia/{id} - Debería manejar ID con formato inválido")
    void updateExperiencia_ShouldHandleInvalidIdFormat() {
        // Arrange
        Integer id = -1;
        when(experienciaService.actualizarExperienciaPorId(eq(id), any(ExperienciaDto.class)))
                .thenThrow(new RuntimeException("Experiencia no encontrada"));

        // Act
        ResponseEntity<ExperienciaDto> response = experienciaController.updateExperiencia(id, experienciaDtoValida);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(experienciaService, times(1)).actualizarExperienciaPorId(eq(id), any(ExperienciaDto.class));
    }

    // ==================== TESTS DELETE /borrar/experiencia/{id} ====================

    @Test
    @DisplayName("DELETE /borrar/experiencia/{id} - Debería eliminar experiencia y retornar 204 No Content")
    void deleteExperienciaById_ShouldDeleteExperienciaAndReturnNoContent() {
        // Arrange
        Integer id = 1;
        doNothing().when(experienciaService).deleteExperienciaPorId(id);

        // Act
        ResponseEntity<Void> response = experienciaController.deleteExperienciaById(id);

        // Assert
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "El status code debería ser 204 No Content");
        assertNull(response.getBody(), "El cuerpo debería ser nulo");

        verify(experienciaService, times(1)).deleteExperienciaPorId(id);
    }

    @Test
    @DisplayName("DELETE /borrar/experiencia/{id} - Debería manejar ID nulo - Test negativo")
    void deleteExperienciaById_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            experienciaController.deleteExperienciaById(id);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(experienciaService, never()).deleteExperienciaPorId(any());
    }

    @Test
    @DisplayName("DELETE /borrar/experiencia/{id} - Debería manejar ID no existente")
    void deleteExperienciaById_ShouldHandleNonExistentId() {
        // Arrange
        Integer id = 999;
        doThrow(new RuntimeException("Experiencia no encontrada")).when(experienciaService).deleteExperienciaPorId(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            experienciaController.deleteExperienciaById(id);
        }, "Debería propagar la excepción del servicio");

        verify(experienciaService, times(1)).deleteExperienciaPorId(id);
    }

    @Test
    @DisplayName("DELETE /borrar/experiencia/{id} - Debería manejar ID con formato inválido")
    void deleteExperienciaById_ShouldHandleInvalidIdFormat() {
        // Arrange
        Integer id = -1;
        doThrow(new RuntimeException("Error al eliminar")).when(experienciaService).deleteExperienciaPorId(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            experienciaController.deleteExperienciaById(id);
        });

        verify(experienciaService, times(1)).deleteExperienciaPorId(id);
    }

    // ==================== TESTS DE VALIDACIÓN ====================

    @Test
    @DisplayName("Validación - ExperienciaDto con todos los campos válidos debería pasar la validación")
    void validation_ShouldPass_WhenAllFieldsAreValid() {
        // Act
        var violations = validator.validate(experienciaDtoValida);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Validación - Título con menos de 3 caracteres debería fallar")
    void validation_ShouldFail_WhenTituloIsTooShort() {
        // Arrange
        ExperienciaDto dtoInvalido = ExperienciaDto.builder()
                .titulo("AB") // Menos de 3 caracteres
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://valid.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act
        var violations = validator.validate(dtoInvalido);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debería haber una violación en el campo titulo");
    }

    @Test
    @DisplayName("Validación - Título con más de 145 caracteres debería fallar")
    void validation_ShouldFail_WhenTituloIsTooLong() {
        // Arrange
        String tituloLargo = "A".repeat(146); // Más de 145 caracteres
        ExperienciaDto dtoInvalido = ExperienciaDto.builder()
                .titulo(tituloLargo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://valid.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act
        var violations = validator.validate(dtoInvalido);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")),
                "Debería haber una violación en el campo titulo");
    }

    @Test
    @DisplayName("Validación - Descripción con menos de 5 caracteres debería fallar")
    void validation_ShouldFail_WhenDescripcionIsTooShort() {
        // Arrange
        ExperienciaDto dtoInvalido = ExperienciaDto.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desc") // Menos de 5 caracteres
                .link("https://valid.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act
        var violations = validator.validate(dtoInvalido);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debería haber una violación en el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Descripción con más de 300 caracteres debería fallar")
    void validation_ShouldFail_WhenDescripcionIsTooLong() {
        // Arrange
        String descripcionLarga = "A".repeat(301); // Más de 300 caracteres
        ExperienciaDto dtoInvalido = ExperienciaDto.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion(descripcionLarga)
                .link("https://valid.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act
        var violations = validator.validate(dtoInvalido);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")),
                "Debería haber una violación en el campo descripcion");
    }

    @Test
    @DisplayName("Validación - Link con menos de 5 caracteres debería fallar")
    void validation_ShouldFail_WhenLinkIsTooShort() {
        // Arrange
        ExperienciaDto dtoInvalido = ExperienciaDto.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("htt") // Menos de 5 caracteres
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act
        var violations = validator.validate(dtoInvalido);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("link")),
                "Debería haber una violación en el campo link");
    }

    @Test
    @DisplayName("Validación - Link con más de 300 caracteres debería fallar")
    void validation_ShouldFail_WhenLinkIsTooLong() {
        // Arrange
        String linkLargo = "A".repeat(301); // Más de 300 caracteres
        ExperienciaDto dtoInvalido = ExperienciaDto.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link(linkLargo)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act
        var violations = validator.validate(dtoInvalido);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("link")),
                "Debería haber una violación en el campo link");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void shouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        doNothing().when(experienciaService).deleteExperienciaPorId(idMax);

        // Act
        ResponseEntity<Void> response = experienciaController.deleteExperienciaById(idMax);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(experienciaService, times(1)).deleteExperienciaPorId(idMax);
    }

    @Test
    @DisplayName("Debería manejar ID en el límite de Integer (Integer.MIN_VALUE)")
    void shouldHandleMinIntegerId() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        doThrow(new RuntimeException("Error al eliminar")).when(experienciaService).deleteExperienciaPorId(idMin);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            experienciaController.deleteExperienciaById(idMin);
        });

        verify(experienciaService, times(1)).deleteExperienciaPorId(idMin);
    }

    @Test
    @DisplayName("FechaInicioProyecto y FechaFinProyecto - Fecha inicio después de fecha fin")
    void validation_ShouldHandleInvalidDateRange() {
        // Arrange
        ExperienciaDto dtoConFechasInvalidas = ExperienciaDto.builder()
                .titulo("Título Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 6, 30)) // Fecha inicio después
                .fechaFinProyecto(LocalDate.of(2024, 1, 1))    // Fecha fin antes
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://valid.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act - Nota: La validación de fechas no está en el DTO, pero podemos verificar que no hay violaciones
        var violations = validator.validate(dtoConFechasInvalidas);

        // Assert - Las fechas no tienen validaciones específicas en el DTO
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación para las fechas");
    }

    @Test
    @DisplayName("TipoExperiencia - Todos los valores del enum deberían ser válidos")
    void validation_ShouldAcceptAllTipoExperienciaValues() {
        // Arrange & Act
        for (TipoExperiencia tipo : TipoExperiencia.values()) {
            ExperienciaDto dto = ExperienciaDto.builder()
                    .titulo("Título Válido")
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://valid.com")
                    .tipoExperiencia(tipo)
                    .tecnologiaUsada(TecnologiaUsada.JAVA)
                    .build();

            // Assert
            var violations = validator.validate(dto);
            assertTrue(violations.isEmpty(), "El tipo " + tipo + " debería ser válido");
        }
    }

    @Test
    @DisplayName("TecnologiaUsada - Todos los valores del enum deberían ser válidos")
    void validation_ShouldAcceptAllTecnologiaUsadaValues() {
        // Arrange & Act
        for (TecnologiaUsada tecnologia : TecnologiaUsada.values()) {
            ExperienciaDto dto = ExperienciaDto.builder()
                    .titulo("Título Válido")
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://valid.com")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiaUsada(tecnologia)
                    .build();

            // Assert
            var violations = validator.validate(dto);
            assertTrue(violations.isEmpty(), "La tecnología " + tecnologia + " debería ser válida");
        }
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con el mock
        verifyNoInteractions(experienciaService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas")
    void verifyExpectedInteractions() {
        // Arrange
        Integer id = 1;
        when(experienciaService.getAllExperiencias()).thenReturn(Collections.emptyList());

        // Act
        experienciaController.getAllExperiencias();

        // Assert - Verificar interacciones exactas
        verify(experienciaService, times(1)).getAllExperiencias();
        verify(experienciaService, never()).saveExperiencia(any());
        verify(experienciaService, never()).actualizarExperienciaPorId(any(), any());
        verify(experienciaService, never()).deleteExperienciaPorId(any());
    }

    // ==================== TESTS DE COMPARACIÓN DE OBJETOS ====================

    @Test
    @DisplayName("Comparar objeto ExperienciaDto - Debería ser igual cuando los valores son iguales")
    void experienciaDtoEquals_ShouldBeEqual_WhenSameValues() {
        // Arrange
        ExperienciaDto dto1 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción del proyecto de prueba con más de 5 caracteres")
                .link("https://test.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        ExperienciaDto dto2 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción del proyecto de prueba con más de 5 caracteres")
                .link("https://test.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode deberían ser iguales");
        assertTrue(dto1.equals(dto2), "equals() debería retornar true");
    }

    @Test
    @DisplayName("Comparar objeto ExperienciaDto - No debería ser igual cuando los valores son diferentes")
    void experienciaDtoEquals_ShouldNotBeEqual_WhenDifferentValues() {
        // Arrange
        ExperienciaDto dto1 = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción del proyecto de prueba con más de 5 caracteres")
                .link("https://test.com")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        ExperienciaDto dto2 = ExperienciaDto.builder()
                .id(2)
                .titulo("Otro Proyecto")
                .fechaInicioProyecto(LocalDate.of(2024, 2, 1))
                .fechaFinProyecto(LocalDate.of(2024, 7, 31))
                .descripcion("Otra descripción del proyecto con más de 5 caracteres")
                .link("https://otro.com")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiaUsada(TecnologiaUsada.REACT)
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode no deberían ser iguales");
        assertFalse(dto1.equals(dto2), "equals() debería retornar false");
    }

    @Test
    @DisplayName("toString de ExperienciaDto debería incluir información relevante")
    void experienciaDtoToString_ShouldIncludeRelevantInformation() {
        // Arrange & Act
        String toStringResult = experienciaDtoValida.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Proyecto Full Stack"), "toString debería contener el título");
        assertTrue(toStringResult.contains("SPRINGBOOT"), "toString debería contener la tecnología usada");
        assertTrue(toStringResult.contains("PROYECTO_PERSONAL"), "toString debería contener el tipo de experiencia");
        assertTrue(toStringResult.contains("link"), "toString debería contener el campo link");
    }

    @Test
    @DisplayName("Builder de ExperienciaDto debería crear objetos correctamente")
    void experienciaDtoBuilder_ShouldCreateCorrectly() {
        // Arrange & Act
        ExperienciaDto dto = ExperienciaDto.builder()
                .id(10)
                .titulo("Builder Test")
                .fechaInicioProyecto(LocalDate.of(2024, 3, 1))
                .fechaFinProyecto(LocalDate.of(2024, 9, 1))
                .descripcion("Descripción del builder con más de 5 caracteres")
                .link("https://builder.com")
                .tipoExperiencia(TipoExperiencia.APORTE_CODIGO_ABIERTO)
                .tecnologiaUsada(TecnologiaUsada.TYPESCRIPT)
                .imagen(imagenDto)
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(10, dto.getId(), "El ID debería ser 10");
        assertEquals("Builder Test", dto.getTitulo(), "El título debería coincidir");
        assertEquals(LocalDate.of(2024, 3, 1), dto.getFechaInicioProyecto());
        assertEquals(LocalDate.of(2024, 9, 1), dto.getFechaFinProyecto());
        assertEquals("Descripción del builder con más de 5 caracteres", dto.getDescripcion());
        assertEquals("https://builder.com", dto.getLink());
        assertEquals(TipoExperiencia.APORTE_CODIGO_ABIERTO, dto.getTipoExperiencia());
        assertEquals(TecnologiaUsada.TYPESCRIPT, dto.getTecnologiaUsada());
        assertNotNull(dto.getImagen(), "La imagen no debería ser nula");
        assertEquals(1, dto.getImagen().getId());
    }
}