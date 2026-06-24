package com.example.demo.service;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoConocimiento;
import com.example.demo.mapper.ConocimientoMapper;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ConocimientoRepository;
import com.example.demo.service.Impl.ConocimientoServiceImpl;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase ConocimientoServiceImpl
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class ConocimientoServiceImplTest {

    @Mock
    private ConocimientoRepository conocimientoRepository;

    @Mock
    private ConocimientoMapper conocimientoMapper;

    @InjectMocks
    private ConocimientoServiceImpl conocimientoService;

    private Conocimiento conocimientoValido;
    private ConocimientoDto conocimientoDtoValido;
    private Imagen imagen;
    private ImagenDto imagenDto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear usuario
        usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack")
                .descripcion("Apasionado por la tecnología")
                .active(true)
                .build();

        // Crear imagen
        imagen = Imagen.builder()
                .id(1)
                .url("angular.jpg")
                .alt("Logo de Angular")
                .build();

        // Crear imagen DTO
        imagenDto = ImagenDto.builder()
                .id(1)
                .url("angular.jpg")
                .alt("Logo de Angular")
                .build();

        // Crear conocimiento válido
        conocimientoValido = Conocimiento.builder()
                .id(1)
                .nombre("Angular")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Crear conocimiento DTO válido
        conocimientoDtoValido = ConocimientoDto.builder()
                .id(1)
                .nombre("Angular")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(imagenDto)
                .build();

        // Establecer la relación bidireccional en la imagen
        if (imagen != null) {
            imagen.setConocimiento(conocimientoValido);
        }
    }

    // ==================== TESTS SAVE CONOCIMIENTO ====================

    @Test
    @DisplayName("saveConocimiento - Debería guardar un conocimiento correctamente")
    void saveConocimiento_ShouldSaveConocimientoCorrectly() {
        // Arrange
        ConocimientoDto conocimientoDtoCrear = ConocimientoDto.builder()
                .nombre("Spring Boot")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .url("springboot.jpg")
                        .alt("Logo de Spring Boot")
                        .build())
                .build();

        Conocimiento conocimientoParaGuardar = Conocimiento.builder()
                .nombre("Spring Boot")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(Imagen.builder()
                        .url("springboot.jpg")
                        .alt("Logo de Spring Boot")
                        .build())
                .build();

        Conocimiento conocimientoGuardado = Conocimiento.builder()
                .id(2)
                .nombre("Spring Boot")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(Imagen.builder()
                        .id(2)
                        .url("springboot.jpg")
                        .alt("Logo de Spring Boot")
                        .build())
                .build();

        ConocimientoDto conocimientoDtoGuardado = ConocimientoDto.builder()
                .id(2)
                .nombre("Spring Boot")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .id(2)
                        .url("springboot.jpg")
                        .alt("Logo de Spring Boot")
                        .build())
                .build();

        when(conocimientoMapper.toConocimiento(conocimientoDtoCrear)).thenReturn(conocimientoParaGuardar);
        when(conocimientoRepository.save(conocimientoParaGuardar)).thenReturn(conocimientoGuardado);
        when(conocimientoMapper.toConocimientoDto(conocimientoGuardado)).thenReturn(conocimientoDtoGuardado);

        // Act
        ConocimientoDto resultado = conocimientoService.saveConocimiento(conocimientoDtoCrear);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.getId(), "El ID generado debería ser 2");
        assertEquals("Spring Boot", resultado.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.INTERMEDIO, resultado.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.BACKEND, resultado.getTipoConocimiento(),
                "El tipo de conocimiento debería coincidir");
        assertNotNull(resultado.getImagen(), "La imagen no debería ser nula");
        assertEquals("springboot.jpg", resultado.getImagen().getUrl(),
                "La URL de la imagen debería coincidir");

        // Verificar que se estableció la relación bidireccional
        verify(conocimientoMapper, times(1)).toConocimiento(conocimientoDtoCrear);
        verify(conocimientoRepository, times(1)).save(conocimientoParaGuardar);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoGuardado);
    }

    @Test
    @DisplayName("saveConocimiento - Debería guardar un conocimiento sin imagen")
    void saveConocimiento_ShouldSaveConocimientoWithoutImagen() {
        // Arrange
        ConocimientoDto conocimientoDtoSinImagen = ConocimientoDto.builder()
                .nombre("JavaScript")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        Conocimiento conocimientoSinImagen = Conocimiento.builder()
                .nombre("JavaScript")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        Conocimiento conocimientoGuardado = Conocimiento.builder()
                .id(3)
                .nombre("JavaScript")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        ConocimientoDto conocimientoDtoGuardado = ConocimientoDto.builder()
                .id(3)
                .nombre("JavaScript")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        when(conocimientoMapper.toConocimiento(conocimientoDtoSinImagen)).thenReturn(conocimientoSinImagen);
        when(conocimientoRepository.save(conocimientoSinImagen)).thenReturn(conocimientoGuardado);
        when(conocimientoMapper.toConocimientoDto(conocimientoGuardado)).thenReturn(conocimientoDtoGuardado);

        // Act
        ConocimientoDto resultado = conocimientoService.saveConocimiento(conocimientoDtoSinImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(3, resultado.getId(), "El ID generado debería ser 3");
        assertEquals("JavaScript", resultado.getNombre(), "El nombre debería coincidir");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");

        // Verificar que no se intentó establecer relación de imagen
        verify(conocimientoMapper, times(1)).toConocimiento(conocimientoDtoSinImagen);
        verify(conocimientoRepository, times(1)).save(conocimientoSinImagen);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoGuardado);
    }

    @Test
    @DisplayName("saveConocimiento - Debería establecer relación bidireccional con imagen")
    void saveConocimiento_ShouldSetBidirectionalRelationshipWithImagen() {
        // Arrange
        ConocimientoDto conocimientoDtoConImagen = ConocimientoDto.builder()
                .nombre("React")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(ImagenDto.builder()
                        .url("react.jpg")
                        .alt("Logo de React")
                        .build())
                .build();

        Conocimiento conocimientoConImagen = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(Imagen.builder()
                        .url("react.jpg")
                        .alt("Logo de React")
                        .build())
                .build();

        when(conocimientoMapper.toConocimiento(conocimientoDtoConImagen)).thenReturn(conocimientoConImagen);
        when(conocimientoRepository.save(conocimientoConImagen)).thenReturn(conocimientoConImagen);
        when(conocimientoMapper.toConocimientoDto(conocimientoConImagen)).thenReturn(conocimientoDtoConImagen);

        // Act
        conocimientoService.saveConocimiento(conocimientoDtoConImagen);

        // Assert - Verificar que se estableció la relación bidireccional
        assertNotNull(conocimientoConImagen.getImagen(), "La imagen no debería ser nula");
        assertEquals(conocimientoConImagen, conocimientoConImagen.getImagen().getConocimiento(),
                "La relación bidireccional debería estar establecida");

        verify(conocimientoMapper, times(1)).toConocimiento(conocimientoDtoConImagen);
        verify(conocimientoRepository, times(1)).save(conocimientoConImagen);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoConImagen);
    }

    @Test
    @DisplayName("saveConocimiento - Debería manejar DTO nulo - Test negativo")
    void saveConocimiento_ShouldHandleNullDto() {
        // Arrange
        when(conocimientoMapper.toConocimiento(null)).thenThrow(new NullPointerException("ConocimientoDto no puede ser nulo"));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            conocimientoService.saveConocimiento(null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(conocimientoMapper, times(1)).toConocimiento(null);
        verify(conocimientoRepository, never()).save(any());
    }

    @Test
    @DisplayName("saveConocimiento - Debería propagar RuntimeException del repositorio")
    void saveConocimiento_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        when(conocimientoMapper.toConocimiento(conocimientoDtoValido)).thenReturn(conocimientoValido);
        when(conocimientoRepository.save(conocimientoValido)).thenThrow(new RuntimeException("Error al guardar"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conocimientoService.saveConocimiento(conocimientoDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al guardar", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(conocimientoMapper, times(1)).toConocimiento(conocimientoDtoValido);
        verify(conocimientoRepository, times(1)).save(conocimientoValido);
        verify(conocimientoMapper, never()).toConocimientoDto(any());
    }

    // ==================== TESTS DELETE CONOCIMIENTO ====================

    @Test
    @DisplayName("deleteConocimientoById - Debería eliminar un conocimiento correctamente")
    void deleteConocimientoById_ShouldDeleteConocimientoCorrectly() {
        // Arrange
        Integer id = 1;
        doNothing().when(conocimientoRepository).deleteById(id);

        // Act
        conocimientoService.deleteConocimientoById(id);

        // Assert
        verify(conocimientoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteConocimientoById - No debería lanzar excepción al eliminar ID existente")
    void deleteConocimientoById_ShouldNotThrowException_WhenIdExists() {
        // Arrange
        Integer id = 1;
        doNothing().when(conocimientoRepository).deleteById(id);

        // Act & Assert
        assertDoesNotThrow(() -> {
            conocimientoService.deleteConocimientoById(id);
        }, "No debería lanzar excepción al eliminar un ID existente");

        verify(conocimientoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteConocimientoById - Debería manejar ID nulo - Test negativo")
    void deleteConocimientoById_ShouldHandleNullId() {
        // Arrange
        Integer id = null;
        doThrow(new IllegalArgumentException("ID no puede ser nulo")).when(conocimientoRepository).deleteById(id);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            conocimientoService.deleteConocimientoById(id);
        }, "Debería lanzar IllegalArgumentException cuando el ID es nulo");

        verify(conocimientoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteConocimientoById - Debería manejar ID no existente")
    void deleteConocimientoById_ShouldHandleNonExistentId() {
        // Arrange
        Integer id = 999;
        doThrow(new RuntimeException("Conocimiento no encontrado con id " + id))
                .when(conocimientoRepository).deleteById(id);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conocimientoService.deleteConocimientoById(id);
        }, "Debería lanzar RuntimeException");

        assertTrue(exception.getMessage().contains("Conocimiento no encontrado"),
                "El mensaje de excepción debería indicar que no se encontró");

        verify(conocimientoRepository, times(1)).deleteById(id);
    }

    // ==================== TESTS GET ALL CONOCIMIENTOS ====================

    @Test
    @DisplayName("getAllConocimientos - Debería retornar lista de conocimientos")
    void getAllConocimientos_ShouldReturnListOfConocimientos() {
        // Arrange
        List<Conocimiento> conocimientos = Arrays.asList(
                conocimientoValido,
                Conocimiento.builder()
                        .id(2)
                        .nombre("Spring Boot")
                        .nivel(Nivel.INTERMEDIO)
                        .tipoConocimiento(TipoConocimiento.BACKEND)
                        .build()
        );

        List<ConocimientoDto> conocimientosDto = Arrays.asList(
                conocimientoDtoValido,
                ConocimientoDto.builder()
                        .id(2)
                        .nombre("Spring Boot")
                        .nivel(Nivel.INTERMEDIO)
                        .tipoConocimiento(TipoConocimiento.BACKEND)
                        .build()
        );

        when(conocimientoRepository.findAll()).thenReturn(conocimientos);
        when(conocimientoMapper.toConocimientoDto(conocimientos.get(0)))
                .thenReturn(conocimientosDto.get(0));
        when(conocimientoMapper.toConocimientoDto(conocimientos.get(1)))
                .thenReturn(conocimientosDto.get(1));

        // Act
        List<ConocimientoDto> resultado = conocimientoService.getAllConocimientos();

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula");
        assertEquals(2, resultado.size(), "Debería haber 2 conocimientos");
        assertEquals("Angular", resultado.get(0).getNombre(),
                "El primer conocimiento debería ser Angular");
        assertEquals("Spring Boot", resultado.get(1).getNombre(),
                "El segundo conocimiento debería ser Spring Boot");

        verify(conocimientoRepository, times(1)).findAll();
        verify(conocimientoMapper, times(2)).toConocimientoDto(any(Conocimiento.class));
    }

    @Test
    @DisplayName("getAllConocimientos - Debería retornar lista vacía cuando no hay conocimientos")
    void getAllConocimientos_ShouldReturnEmptyList_WhenNoConocimientos() {
        // Arrange
        when(conocimientoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<ConocimientoDto> resultado = conocimientoService.getAllConocimientos();

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");

        verify(conocimientoRepository, times(1)).findAll();
        verify(conocimientoMapper, never()).toConocimientoDto(any());
    }

    @Test
    @DisplayName("getAllConocimientos - Debería propagar RuntimeException del repositorio")
    void getAllConocimientos_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        when(conocimientoRepository.findAll()).thenThrow(new RuntimeException("Error al obtener conocimientos"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conocimientoService.getAllConocimientos();
        }, "Debería propagar RuntimeException");

        assertEquals("Error al obtener conocimientos", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(conocimientoRepository, times(1)).findAll();
        verify(conocimientoMapper, never()).toConocimientoDto(any());
    }

    // ==================== TESTS FILTRAR POR TIPO ====================

    @Test
    @DisplayName("filtrarPorTipo - Debería filtrar conocimientos por tipo FRONTEND")
    void filtrarPorTipo_ShouldFilterFrontendConocimientos() {
        // Arrange
        TipoConocimiento tipo = TipoConocimiento.FRONTEND;
        List<Conocimiento> conocimientos = Arrays.asList(
                conocimientoValido,
                Conocimiento.builder()
                        .id(2)
                        .nombre("React")
                        .nivel(Nivel.INTERMEDIO)
                        .tipoConocimiento(TipoConocimiento.FRONTEND)
                        .build()
        );

        List<ConocimientoDto> conocimientosDto = Arrays.asList(
                conocimientoDtoValido,
                ConocimientoDto.builder()
                        .id(2)
                        .nombre("React")
                        .nivel(Nivel.INTERMEDIO)
                        .tipoConocimiento(TipoConocimiento.FRONTEND)
                        .build()
        );

        when(conocimientoRepository.findByTipoConocimiento(tipo)).thenReturn(conocimientos);
        when(conocimientoMapper.toConocimientoDto(conocimientos.get(0)))
                .thenReturn(conocimientosDto.get(0));
        when(conocimientoMapper.toConocimientoDto(conocimientos.get(1)))
                .thenReturn(conocimientosDto.get(1));

        // Act
        List<ConocimientoDto> resultado = conocimientoService.filtrarPorTipo(tipo);

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula");
        assertEquals(2, resultado.size(), "Debería haber 2 conocimientos FRONTEND");
        assertTrue(resultado.stream()
                        .allMatch(dto -> dto.getTipoConocimiento() == TipoConocimiento.FRONTEND),
                "Todos los conocimientos deberían ser de tipo FRONTEND");

        verify(conocimientoRepository, times(1)).findByTipoConocimiento(tipo);
        verify(conocimientoMapper, times(2)).toConocimientoDto(any(Conocimiento.class));
    }

    @ParameterizedTest
    @EnumSource(TipoConocimiento.class)
    @DisplayName("filtrarPorTipo - Debería filtrar por todos los tipos de conocimiento")
    void filtrarPorTipo_ShouldFilterByAllTiposConocimiento(TipoConocimiento tipo) {
        // Arrange
        List<Conocimiento> conocimientos = Arrays.asList(
                Conocimiento.builder()
                        .id(1)
                        .nombre("Conocimiento " + tipo)
                        .nivel(Nivel.INTERMEDIO)
                        .tipoConocimiento(tipo)
                        .build()
        );

        List<ConocimientoDto> conocimientosDto = Arrays.asList(
                ConocimientoDto.builder()
                        .id(1)
                        .nombre("Conocimiento " + tipo)
                        .nivel(Nivel.INTERMEDIO)
                        .tipoConocimiento(tipo)
                        .build()
        );

        when(conocimientoRepository.findByTipoConocimiento(tipo)).thenReturn(conocimientos);
        when(conocimientoMapper.toConocimientoDto(conocimientos.get(0)))
                .thenReturn(conocimientosDto.get(0));

        // Act
        List<ConocimientoDto> resultado = conocimientoService.filtrarPorTipo(tipo);

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula para tipo: " + tipo);
        assertFalse(resultado.isEmpty(), "La lista no debería estar vacía para tipo: " + tipo);
        assertEquals(tipo, resultado.get(0).getTipoConocimiento(),
                "El tipo debería ser: " + tipo);

        verify(conocimientoRepository, times(1)).findByTipoConocimiento(tipo);
    }

    @Test
    @DisplayName("filtrarPorTipo - Debería retornar lista vacía cuando no hay conocimientos del tipo")
    void filtrarPorTipo_ShouldReturnEmptyList_WhenNoConocimientosOfType() {
        // Arrange
        TipoConocimiento tipo = TipoConocimiento.BACKEND;
        when(conocimientoRepository.findByTipoConocimiento(tipo)).thenReturn(new ArrayList<>());

        // Act
        List<ConocimientoDto> resultado = conocimientoService.filtrarPorTipo(tipo);

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");

        verify(conocimientoRepository, times(1)).findByTipoConocimiento(tipo);
        verify(conocimientoMapper, never()).toConocimientoDto(any());
    }

    @Test
    @DisplayName("filtrarPorTipo - Debería manejar tipo nulo")
    void filtrarPorTipo_ShouldHandleNullTipo() {
        // Arrange
        TipoConocimiento tipo = null;
        when(conocimientoRepository.findByTipoConocimiento(tipo)).thenReturn(new ArrayList<>());

        // Act
        List<ConocimientoDto> resultado = conocimientoService.filtrarPorTipo(tipo);

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");

        verify(conocimientoRepository, times(1)).findByTipoConocimiento(tipo);
        verify(conocimientoMapper, never()).toConocimientoDto(any());
    }

    // ==================== TESTS ACTUALIZAR CONOCIMIENTO ====================

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería actualizar un conocimiento existente correctamente")
    void actualizarConocimientoPorId_ShouldUpdateConocimientoCorrectly() {
        // Arrange
        Integer id = 1;
        ConocimientoDto conocimientoDtoActualizado = ConocimientoDto.builder()
                .id(id)
                .nombre("Angular 15")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(ImagenDto.builder()
                        .url("angular15.jpg")
                        .alt("Logo de Angular 15")
                        .build())
                .build();

        Conocimiento conocimientoExistente = conocimientoValido;
        Conocimiento conocimientoActualizado = Conocimiento.builder()
                .id(id)
                .nombre("Angular 15")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(Imagen.builder()
                        .url("angular15.jpg")
                        .alt("Logo de Angular 15")
                        .build())
                .build();

        when(conocimientoRepository.findById(id)).thenReturn(Optional.of(conocimientoExistente));
        when(conocimientoRepository.save(conocimientoExistente)).thenReturn(conocimientoActualizado);
        when(conocimientoMapper.toConocimientoDto(conocimientoActualizado))
                .thenReturn(conocimientoDtoActualizado);

        // Act
        ConocimientoDto resultado = conocimientoService.actualizarConocimientoPorId(id, conocimientoDtoActualizado);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(id, resultado.getId(), "El ID debería coincidir");
        assertEquals("Angular 15", resultado.getNombre(), "El nombre debería estar actualizado");
        assertEquals(Nivel.ALTO, resultado.getNivel(), "El nivel debería estar actualizado");
        assertEquals(TipoConocimiento.FRONTEND, resultado.getTipoConocimiento(),
                "El tipo de conocimiento debería mantenerse");
        assertNotNull(resultado.getImagen(), "La imagen no debería ser nula");
        assertEquals("angular15.jpg", resultado.getImagen().getUrl(),
                "La URL de la imagen debería estar actualizada");

        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, times(1)).save(conocimientoExistente);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoActualizado);
    }

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería actualizar conocimiento sin imagen")
    void actualizarConocimientoPorId_ShouldUpdateConocimientoWithoutImagen() {
        // Arrange
        Integer id = 1;
        ConocimientoDto conocimientoDtoSinImagen = ConocimientoDto.builder()
                .id(id)
                .nombre("Angular Actualizado")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        Conocimiento conocimientoExistente = conocimientoValido;
        conocimientoExistente.setImagen(null); // Eliminar imagen existente

        when(conocimientoRepository.findById(id)).thenReturn(Optional.of(conocimientoExistente));
        when(conocimientoRepository.save(conocimientoExistente)).thenReturn(conocimientoExistente);
        when(conocimientoMapper.toConocimientoDto(conocimientoExistente))
                .thenReturn(conocimientoDtoSinImagen);

        // Act
        ConocimientoDto resultado = conocimientoService.actualizarConocimientoPorId(id, conocimientoDtoSinImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");

        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, times(1)).save(conocimientoExistente);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoExistente);
    }

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería crear nueva imagen cuando no existe")
    void actualizarConocimientoPorId_ShouldCreateNewImagen_WhenNotExists() {
        // Arrange
        Integer id = 1;
        Conocimiento conocimientoSinImagen = Conocimiento.builder()
                .id(id)
                .nombre("Java")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .usuario(usuario)
                .build();

        ConocimientoDto conocimientoDtoConImagen = ConocimientoDto.builder()
                .id(id)
                .nombre("Java")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .url("java.jpg")
                        .alt("Logo de Java")
                        .build())
                .build();

        when(conocimientoRepository.findById(id)).thenReturn(Optional.of(conocimientoSinImagen));
        when(conocimientoRepository.save(conocimientoSinImagen)).thenReturn(conocimientoSinImagen);
        when(conocimientoMapper.toConocimientoDto(conocimientoSinImagen))
                .thenReturn(conocimientoDtoConImagen);

        // Act
        ConocimientoDto resultado = conocimientoService.actualizarConocimientoPorId(id, conocimientoDtoConImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(conocimientoSinImagen.getImagen(), "La imagen debería ser creada");
        assertEquals("java.jpg", conocimientoSinImagen.getImagen().getUrl(),
                "La URL de la imagen debería ser la correcta");
        assertEquals(conocimientoSinImagen, conocimientoSinImagen.getImagen().getConocimiento(),
                "La relación bidireccional debería estar establecida");

        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, times(1)).save(conocimientoSinImagen);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoSinImagen);
    }

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería actualizar imagen existente")
    void actualizarConocimientoPorId_ShouldUpdateExistingImagen() {
        // Arrange
        Integer id = 1;
        Conocimiento conocimientoExistente = conocimientoValido;
        Imagen imagenExistente = conocimientoExistente.getImagen();

        ConocimientoDto conocimientoDtoConImagenActualizada = ConocimientoDto.builder()
                .id(id)
                .nombre("Angular")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(ImagenDto.builder()
                        .url("angular-nuevo.jpg")
                        .alt("Logo de Angular actualizado")
                        .build())
                .build();

        when(conocimientoRepository.findById(id)).thenReturn(Optional.of(conocimientoExistente));
        when(conocimientoRepository.save(conocimientoExistente)).thenReturn(conocimientoExistente);
        when(conocimientoMapper.toConocimientoDto(conocimientoExistente))
                .thenReturn(conocimientoDtoConImagenActualizada);

        // Act
        ConocimientoDto resultado = conocimientoService.actualizarConocimientoPorId(id,
                conocimientoDtoConImagenActualizada);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(conocimientoExistente.getImagen(), "La imagen no debería ser nula");
        assertEquals("angular-nuevo.jpg", conocimientoExistente.getImagen().getUrl(),
                "La URL de la imagen debería estar actualizada");
        assertEquals("Logo de Angular actualizado", conocimientoExistente.getImagen().getAlt(),
                "El alt de la imagen debería estar actualizado");

        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, times(1)).save(conocimientoExistente);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoExistente);
    }

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería eliminar imagen cuando se envía null")
    void actualizarConocimientoPorId_ShouldRemoveImagen_WhenNullSent() {
        // Arrange
        Integer id = 1;
        Conocimiento conocimientoExistente = conocimientoValido;
        Integer idImagen = conocimientoExistente.getImagen().getId();

        ConocimientoDto conocimientoDtoSinImagen = ConocimientoDto.builder()
                .id(id)
                .nombre("Angular")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        when(conocimientoRepository.findById(id)).thenReturn(Optional.of(conocimientoExistente));
        when(conocimientoRepository.save(conocimientoExistente)).thenReturn(conocimientoExistente);
        when(conocimientoMapper.toConocimientoDto(conocimientoExistente))
                .thenReturn(conocimientoDtoSinImagen);

        // Act
        ConocimientoDto resultado = conocimientoService.actualizarConocimientoPorId(id, conocimientoDtoSinImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(conocimientoExistente.getImagen(), "La imagen debería ser eliminada");

        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, times(1)).save(conocimientoExistente);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoExistente);
    }

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería lanzar RuntimeException cuando el ID no existe")
    void actualizarConocimientoPorId_ShouldThrowRuntimeException_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(conocimientoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conocimientoService.actualizarConocimientoPorId(id, conocimientoDtoValido);
        }, "Debería lanzar RuntimeException");

        assertTrue(exception.getMessage().contains("Conocimiento no encontrado con id " + id),
                "El mensaje de excepción debería indicar que el conocimiento no fue encontrado");

        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, never()).save(any());
        verify(conocimientoMapper, never()).toConocimientoDto(any());
    }

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería actualizar solo los campos permitidos")
    void actualizarConocimientoPorId_ShouldUpdateOnlyAllowedFields() {
        // Arrange
        Integer id = 1;
        ConocimientoDto conocimientoDtoActualizado = ConocimientoDto.builder()
                .id(id)
                .nombre("Nuevo Nombre")
                .nivel(Nivel.PRINCIPIANTE_BASICO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(null)
                .build();

        when(conocimientoRepository.findById(id)).thenReturn(Optional.of(conocimientoValido));
        when(conocimientoRepository.save(conocimientoValido)).thenReturn(conocimientoValido);
        when(conocimientoMapper.toConocimientoDto(conocimientoValido))
                .thenReturn(conocimientoDtoActualizado);

        // Act
        conocimientoService.actualizarConocimientoPorId(id, conocimientoDtoActualizado);

        // Assert - Verificar que los setters fueron llamados con los valores correctos
        assertEquals("Nuevo Nombre", conocimientoValido.getNombre(),
                "El nombre debería estar actualizado");
        assertEquals(Nivel.PRINCIPIANTE_BASICO, conocimientoValido.getNivel(),
                "El nivel debería estar actualizado");
        assertEquals(TipoConocimiento.OTROS, conocimientoValido.getTipoConocimiento(),
                "El tipo de conocimiento debería estar actualizado");

        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, times(1)).save(conocimientoValido);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoValido);
    }

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería manejar ID nulo - Test negativo")
    void actualizarConocimientoPorId_ShouldHandleNullId() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            conocimientoService.actualizarConocimientoPorId(null, conocimientoDtoValido);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(conocimientoRepository, never()).findById(any());
        verify(conocimientoRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería manejar DTO nulo - Test negativo")
    void actualizarConocimientoPorId_ShouldHandleNullDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            conocimientoService.actualizarConocimientoPorId(id, null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(conocimientoRepository, never()).findById(any());
        verify(conocimientoRepository, never()).save(any());
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void actualizarConocimientoPorId_ShouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        when(conocimientoRepository.findById(idMax)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            conocimientoService.actualizarConocimientoPorId(idMax, conocimientoDtoValido);
        }, "Debería lanzar RuntimeException con ID máximo");

        verify(conocimientoRepository, times(1)).findById(idMax);
        verify(conocimientoRepository, never()).save(any());
    }

    @Test
    @DisplayName("saveConocimiento - Debería manejar nombre con caracteres especiales")
    void saveConocimiento_ShouldHandleNombreWithSpecialCharacters() {
        // Arrange
        ConocimientoDto conocimientoDtoEspecial = ConocimientoDto.builder()
                .nombre("C# - .NET Core (Framework 8)")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .build();

        Conocimiento conocimientoEspecial = Conocimiento.builder()
                .nombre("C# - .NET Core (Framework 8)")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .build();

        when(conocimientoMapper.toConocimiento(conocimientoDtoEspecial)).thenReturn(conocimientoEspecial);
        when(conocimientoRepository.save(conocimientoEspecial)).thenReturn(conocimientoEspecial);
        when(conocimientoMapper.toConocimientoDto(conocimientoEspecial))
                .thenReturn(conocimientoDtoEspecial);

        // Act
        ConocimientoDto resultado = conocimientoService.saveConocimiento(conocimientoDtoEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("C# - .NET Core (Framework 8)", resultado.getNombre(),
                "El nombre con caracteres especiales debería mantenerse");

        verify(conocimientoMapper, times(1)).toConocimiento(conocimientoDtoEspecial);
        verify(conocimientoRepository, times(1)).save(conocimientoEspecial);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoEspecial);
    }

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería manejar nombre con acentos y ñ")
    void actualizarConocimientoPorId_ShouldHandleNombreWithAccentsAndEnie() {
        // Arrange
        Integer id = 1;
        ConocimientoDto conocimientoDtoAcentuado = ConocimientoDto.builder()
                .id(id)
                .nombre("Java Spring Boot - Configuración avanzada")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .build();

        when(conocimientoRepository.findById(id)).thenReturn(Optional.of(conocimientoValido));
        when(conocimientoRepository.save(conocimientoValido)).thenReturn(conocimientoValido);
        when(conocimientoMapper.toConocimientoDto(conocimientoValido))
                .thenReturn(conocimientoDtoAcentuado);

        // Act
        ConocimientoDto resultado = conocimientoService.actualizarConocimientoPorId(id,
                conocimientoDtoAcentuado);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("Java Spring Boot - Configuración avanzada", conocimientoValido.getNombre(),
                "El nombre con acentos debería mantenerse");

        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, times(1)).save(conocimientoValido);
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con los mocks
        verifyNoInteractions(conocimientoRepository);
        verifyNoInteractions(conocimientoMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en saveConocimiento")
    void verifyExpectedInteractionsInSaveConocimiento() {
        // Arrange
        when(conocimientoMapper.toConocimiento(conocimientoDtoValido)).thenReturn(conocimientoValido);
        when(conocimientoRepository.save(conocimientoValido)).thenReturn(conocimientoValido);
        when(conocimientoMapper.toConocimientoDto(conocimientoValido)).thenReturn(conocimientoDtoValido);

        // Act
        conocimientoService.saveConocimiento(conocimientoDtoValido);

        // Assert
        verify(conocimientoMapper, times(1)).toConocimiento(conocimientoDtoValido);
        verify(conocimientoRepository, times(1)).save(conocimientoValido);
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoValido);
        verifyNoMoreInteractions(conocimientoRepository);
        verifyNoMoreInteractions(conocimientoMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en getAllConocimientos")
    void verifyExpectedInteractionsInGetAllConocimientos() {
        // Arrange
        when(conocimientoRepository.findAll()).thenReturn(Arrays.asList(conocimientoValido));
        when(conocimientoMapper.toConocimientoDto(conocimientoValido)).thenReturn(conocimientoDtoValido);

        // Act
        conocimientoService.getAllConocimientos();

        // Assert
        verify(conocimientoRepository, times(1)).findAll();
        verify(conocimientoMapper, times(1)).toConocimientoDto(conocimientoValido);
        verifyNoMoreInteractions(conocimientoRepository);
        verifyNoMoreInteractions(conocimientoMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en deleteConocimientoById")
    void verifyExpectedInteractionsInDeleteConocimientoById() {
        // Arrange
        Integer id = 1;
        doNothing().when(conocimientoRepository).deleteById(id);

        // Act
        conocimientoService.deleteConocimientoById(id);

        // Assert
        verify(conocimientoRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(conocimientoRepository);
        verifyNoInteractions(conocimientoMapper);
    }

    // ==================== TESTS DE EXCEPCIONES ESPECÍFICAS ====================

    @Test
    @DisplayName("actualizarConocimientoPorId - Debería propagar RuntimeException del repositorio al actualizar")
    void actualizarConocimientoPorId_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        Integer id = 1;
        when(conocimientoRepository.findById(id)).thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conocimientoService.actualizarConocimientoPorId(id, conocimientoDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error de base de datos", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, never()).save(any());
        verify(conocimientoMapper, never()).toConocimientoDto(any());
    }

    @Test
    @DisplayName("filtrarPorTipo - Debería propagar RuntimeException del repositorio")
    void filtrarPorTipo_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        TipoConocimiento tipo = TipoConocimiento.FRONTEND;
        when(conocimientoRepository.findByTipoConocimiento(tipo))
                .thenThrow(new RuntimeException("Error al filtrar"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conocimientoService.filtrarPorTipo(tipo);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al filtrar", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(conocimientoRepository, times(1)).findByTipoConocimiento(tipo);
        verify(conocimientoMapper, never()).toConocimientoDto(any());
    }

    // ==================== TESTS DE CASOS DE USO REALISTAS ====================

    @Test
    @DisplayName("Debe realizar operaciones CRUD completas secuencialmente")
    void shouldPerformCompleteCrudOperationsSequentially() {
        // Arrange - Guardar
        ConocimientoDto conocimientoDtoCrear = ConocimientoDto.builder()
                .nombre("JUnit")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.TESTING)
                .build();

        Conocimiento conocimientoParaGuardar = new Conocimiento();
        Conocimiento conocimientoGuardado = Conocimiento.builder()
                .id(1)
                .nombre("JUnit")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.TESTING)
                .build();

        when(conocimientoMapper.toConocimiento(conocimientoDtoCrear)).thenReturn(conocimientoParaGuardar);
        when(conocimientoRepository.save(conocimientoParaGuardar)).thenReturn(conocimientoGuardado);
        when(conocimientoMapper.toConocimientoDto(conocimientoGuardado))
                .thenReturn(conocimientoDtoCrear);

        // 1. Crear
        ConocimientoDto creado = conocimientoService.saveConocimiento(conocimientoDtoCrear);
        assertNotNull(creado, "El conocimiento creado no debería ser nulo");

        // 2. Obtener todos
        when(conocimientoRepository.findAll()).thenReturn(Arrays.asList(conocimientoGuardado));
        when(conocimientoMapper.toConocimientoDto(conocimientoGuardado))
                .thenReturn(conocimientoDtoCrear);

        List<ConocimientoDto> lista = conocimientoService.getAllConocimientos();
        assertNotNull(lista, "La lista no debería ser nula");
        assertFalse(lista.isEmpty(), "La lista no debería estar vacía");

        // 3. Actualizar
        Integer id = 1;
        ConocimientoDto conocimientoDtoActualizado = ConocimientoDto.builder()
                .id(id)
                .nombre("JUnit 5")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.TESTING)
                .build();

        when(conocimientoRepository.findById(id)).thenReturn(Optional.of(conocimientoGuardado));
        when(conocimientoRepository.save(conocimientoGuardado)).thenReturn(conocimientoGuardado);
        when(conocimientoMapper.toConocimientoDto(conocimientoGuardado))
                .thenReturn(conocimientoDtoActualizado);

        ConocimientoDto actualizado = conocimientoService.actualizarConocimientoPorId(id,
                conocimientoDtoActualizado);
        assertNotNull(actualizado, "El conocimiento actualizado no debería ser nulo");
        assertEquals("JUnit 5", actualizado.getNombre(),
                "El nombre debería estar actualizado");

        // 4. Eliminar
        doNothing().when(conocimientoRepository).deleteById(id);
        conocimientoService.deleteConocimientoById(id);

        // Verificar interacciones
        verify(conocimientoMapper, times(2)).toConocimiento(conocimientoDtoCrear);
        verify(conocimientoRepository, times(2)).save(any(Conocimiento.class));
        verify(conocimientoRepository, times(1)).findAll();
        verify(conocimientoRepository, times(1)).findById(id);
        verify(conocimientoRepository, times(1)).deleteById(id);
        verify(conocimientoMapper, times(4)).toConocimientoDto(any(Conocimiento.class));
    }

    @Test
    @DisplayName("Debe manejar correctamente la actualización de múltiples campos")
    void shouldHandleMultipleFieldUpdatesCorrectly() {
        // Arrange
        Integer id = 1;
        ConocimientoDto conocimientoDtoActualizado = ConocimientoDto.builder()
                .id(id)
                .nombre("Spring Data JPA")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(ImagenDto.builder()
                        .url("spring-data-jpa.jpg")
                        .alt("Logo de Spring Data JPA")
                        .build())
                .build();

        when(conocimientoRepository.findById(id)).thenReturn(Optional.of(conocimientoValido));
        when(conocimientoRepository.save(conocimientoValido)).thenReturn(conocimientoValido);
        when(conocimientoMapper.toConocimientoDto(conocimientoValido))
                .thenReturn(conocimientoDtoActualizado);

        // Act
        ConocimientoDto resultado = conocimientoService.actualizarConocimientoPorId(id,
                conocimientoDtoActualizado);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("Spring Data JPA", conocimientoValido.getNombre(),
                "El nombre debería estar actualizado");
        assertEquals(Nivel.ALTO, conocimientoValido.getNivel(),
                "El nivel debería estar actualizado");
        assertEquals(TipoConocimiento.BACKEND, conocimientoValido.getTipoConocimiento(),
                "El tipo de conocimiento debería estar actualizado");
        assertNotNull(conocimientoValido.getImagen(), "La imagen no debería ser nula");
        assertEquals("spring-data-jpa.jpg", conocimientoValido.getImagen().getUrl(),
                "La URL de la imagen debería estar actualizada");
    }
}