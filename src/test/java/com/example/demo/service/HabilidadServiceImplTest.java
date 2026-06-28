package com.example.demo.service;

import com.example.demo.dto.HabilidadDto;
import com.example.demo.mapper.HabilidadMapper;
import com.example.demo.model.Habilidad;
import com.example.demo.model.Usuario;
import com.example.demo.repository.HabilidadRepository;
import com.example.demo.service.Impl.HabilidadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase HabilidadServiceImpl
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class HabilidadServiceImplTest {

    @Mock
    private HabilidadRepository habilidadRepository;

    @Mock
    private HabilidadMapper habilidadMapper;

    @InjectMocks
    private HabilidadServiceImpl habilidadService;

    private HabilidadDto habilidadDtoValido;
    private Habilidad habilidadValida;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("password123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(com.example.demo.enums.Role.USER)
                .build();

        habilidadDtoValido = HabilidadDto.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .build();

        habilidadValida = Habilidad.builder()
                .id(1)
                .nombre("Comunicación Efectiva")
                .usuario(usuario)
                .build();
    }

    // ==================== TESTS SAVE HABILIDAD ====================

    @Test
    @DisplayName("saveHabilidad - Debería guardar habilidad correctamente y retornar HabilidadDto")
    void saveHabilidad_ShouldSaveHabilidadAndReturnHabilidadDto() {
        // Arrange - Configurar comportamiento del mock
        when(habilidadMapper.toHabilidad(any(HabilidadDto.class))).thenReturn(habilidadValida);
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadValida);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoValido);

        // Act - Ejecutar el método del servicio
        HabilidadDto resultado = habilidadService.saveHabilidad(habilidadDtoValido);

        // Assert - Verificar resultados
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(habilidadDtoValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(habilidadDtoValido.getNombre(), resultado.getNombre(), "El nombre debería coincidir");

        // Verificar que se llamaron los métodos exactamente una vez
        verify(habilidadMapper, times(1)).toHabilidad(any(HabilidadDto.class));
        verify(habilidadRepository, times(1)).save(any(Habilidad.class));
        verify(habilidadMapper, times(1)).toHabilidadDto(any(Habilidad.class));
    }

    @Test
    @DisplayName("saveHabilidad - Debería guardar habilidad sin ID correctamente")
    void saveHabilidad_ShouldSaveHabilidadWithoutId() {
        // Arrange
        HabilidadDto habilidadDtoSinId = HabilidadDto.builder()
                .nombre("Trabajo en Equipo")
                .build();

        Habilidad habilidadSinId = Habilidad.builder()
                .nombre("Trabajo en Equipo")
                .usuario(usuario)
                .build();

        Habilidad habilidadGuardada = Habilidad.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .usuario(usuario)
                .build();

        HabilidadDto habilidadDtoGuardada = HabilidadDto.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .build();

        when(habilidadMapper.toHabilidad(any(HabilidadDto.class))).thenReturn(habilidadSinId);
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadGuardada);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoGuardada);

        // Act
        HabilidadDto resultado = habilidadService.saveHabilidad(habilidadDtoSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.getId(), "El ID debería ser 2 (generado)");
        assertEquals("Trabajo en Equipo", resultado.getNombre(), "El nombre debería coincidir");

        verify(habilidadMapper, times(1)).toHabilidad(any(HabilidadDto.class));
        verify(habilidadRepository, times(1)).save(any(Habilidad.class));
        verify(habilidadMapper, times(1)).toHabilidadDto(any(Habilidad.class));
    }

    @Test
    @DisplayName("saveHabilidad - Debería manejar habilidad con nombre en blanco")
    void saveHabilidad_ShouldHandleHabilidadWithBlankNombre() {
        // Arrange
        HabilidadDto habilidadDtoConNombreVacio = HabilidadDto.builder()
                .nombre("")
                .build();

        Habilidad habilidadConNombreVacio = Habilidad.builder()
                .nombre("")
                .usuario(usuario)
                .build();

        when(habilidadMapper.toHabilidad(any(HabilidadDto.class))).thenReturn(habilidadConNombreVacio);
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadConNombreVacio);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoConNombreVacio);

        // Act
        HabilidadDto resultado = habilidadService.saveHabilidad(habilidadDtoConNombreVacio);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getNombre(), "El nombre vacío debería mantenerse");

        verify(habilidadRepository, times(1)).save(any(Habilidad.class));
    }

    @Test
    @DisplayName("saveHabilidad - Debería manejar excepción del repositorio")
    void saveHabilidad_ShouldHandleRepositoryException() {
        // Arrange
        when(habilidadMapper.toHabilidad(any(HabilidadDto.class))).thenReturn(habilidadValida);
        when(habilidadRepository.save(any(Habilidad.class)))
                .thenThrow(new RuntimeException("Error al guardar habilidad"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadService.saveHabilidad(habilidadDtoValido);
        }, "Debería propagar la excepción del repositorio");

        verify(habilidadMapper, times(1)).toHabilidad(any(HabilidadDto.class));
        verify(habilidadRepository, times(1)).save(any(Habilidad.class));
        verify(habilidadMapper, never()).toHabilidadDto(any(Habilidad.class));
    }

    @Test
    @DisplayName("saveHabilidad - Debería manejar habilidadDto nulo - Test negativo")
    void saveHabilidad_ShouldHandleNullHabilidadDto() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            habilidadService.saveHabilidad(null);
        }, "Debería lanzar NullPointerException cuando habilidadDto es nulo");

        verify(habilidadMapper, never()).toHabilidad(any());
        verify(habilidadRepository, never()).save(any());
    }

    // ==================== TESTS ACTUALIZAR HABILIDAD ====================

    @Test
    @DisplayName("actualizarHabilidadPorId - Debería actualizar habilidad existente y retornar HabilidadDto")
    void actualizarHabilidadPorId_ShouldUpdateExistingHabilidadAndReturnHabilidadDto() {
        // Arrange
        Integer id = 1;
        HabilidadDto habilidadDtoActualizado = HabilidadDto.builder()
                .id(id)
                .nombre("Comunicación Avanzada")
                .build();

        Habilidad habilidadExistente = Habilidad.builder()
                .id(id)
                .nombre("Comunicación Efectiva")
                .usuario(usuario)
                .build();

        Habilidad habilidadActualizada = Habilidad.builder()
                .id(id)
                .nombre("Comunicación Avanzada")
                .usuario(usuario)
                .build();

        when(habilidadRepository.findById(id)).thenReturn(Optional.of(habilidadExistente));
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadActualizada);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoActualizado);

        // Act
        HabilidadDto resultado = habilidadService.actualizarHabilidadPorId(id, habilidadDtoActualizado);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(id, resultado.getId(), "El ID debería coincidir");
        assertEquals("Comunicación Avanzada", resultado.getNombre(), "El nombre debería estar actualizado");

        // Verificar que se llamaron los métodos exactamente una vez
        verify(habilidadRepository, times(1)).findById(id);
        verify(habilidadRepository, times(1)).save(any(Habilidad.class));
        verify(habilidadMapper, times(1)).toHabilidadDto(any(Habilidad.class));
    }

    @Test
    @DisplayName("actualizarHabilidadPorId - Debería retornar null cuando la habilidad no existe")
    void actualizarHabilidadPorId_ShouldReturnNull_WhenHabilidadDoesNotExist() {
        // Arrange
        Integer id = 999;
        HabilidadDto habilidadDtoActualizar = HabilidadDto.builder()
                .id(id)
                .nombre("Habilidad Nueva")
                .build();

        when(habilidadRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        HabilidadDto resultado = habilidadService.actualizarHabilidadPorId(id, habilidadDtoActualizar);

        // Assert
        assertNull(resultado, "El resultado debería ser null cuando la habilidad no existe");

        verify(habilidadRepository, times(1)).findById(id);
        verify(habilidadRepository, never()).save(any(Habilidad.class));
        verify(habilidadMapper, never()).toHabilidadDto(any(Habilidad.class));
    }

    @Test
    @DisplayName("actualizarHabilidadPorId - Debería actualizar solo el nombre de la habilidad")
    void actualizarHabilidadPorId_ShouldUpdateOnlyNombre() {
        // Arrange
        Integer id = 1;
        String nuevoNombre = "Adaptabilidad";

        HabilidadDto habilidadDtoActualizado = HabilidadDto.builder()
                .id(id)
                .nombre(nuevoNombre)
                .build();

        Habilidad habilidadExistente = Habilidad.builder()
                .id(id)
                .nombre("Comunicación Efectiva")
                .usuario(usuario)
                .build();

        when(habilidadRepository.findById(id)).thenReturn(Optional.of(habilidadExistente));
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadExistente);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoActualizado);

        // Act
        HabilidadDto resultado = habilidadService.actualizarHabilidadPorId(id, habilidadDtoActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(nuevoNombre, resultado.getNombre(), "El nombre debería estar actualizado");

        // Verificar que el setter fue llamado
        assertEquals(nuevoNombre, habilidadExistente.getNombre(), "El nombre en la entidad debería estar actualizado");
        verify(habilidadRepository, times(1)).save(habilidadExistente);
    }

    @Test
    @DisplayName("actualizarHabilidadPorId - Debería manejar ID nulo - Test negativo")
    void actualizarHabilidadPorId_ShouldHandleNullId() {
        // Arrange
        Integer id = null;
        HabilidadDto habilidadDto = HabilidadDto.builder()
                .nombre("Habilidad Test")
                .build();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            habilidadService.actualizarHabilidadPorId(id, habilidadDto);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(habilidadRepository, never()).findById(any());
        verify(habilidadRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarHabilidadPorId - Debería manejar habilidadDto nulo - Test negativo")
    void actualizarHabilidadPorId_ShouldHandleNullHabilidadDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            habilidadService.actualizarHabilidadPorId(id, null);
        }, "Debería lanzar NullPointerException cuando habilidadDto es nulo");

        verify(habilidadRepository, never()).findById(any());
        verify(habilidadRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarHabilidadPorId - Debería manejar excepción del repositorio al buscar")
    void actualizarHabilidadPorId_ShouldHandleRepositoryFindException() {
        // Arrange
        Integer id = 1;
        when(habilidadRepository.findById(id)).thenThrow(new RuntimeException("Error al buscar habilidad"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadService.actualizarHabilidadPorId(id, habilidadDtoValido);
        }, "Debería propagar la excepción del repositorio");

        verify(habilidadRepository, times(1)).findById(id);
        verify(habilidadRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarHabilidadPorId - Debería manejar excepción del repositorio al guardar")
    void actualizarHabilidadPorId_ShouldHandleRepositorySaveException() {
        // Arrange
        Integer id = 1;
        when(habilidadRepository.findById(id)).thenReturn(Optional.of(habilidadValida));
        when(habilidadRepository.save(any(Habilidad.class)))
                .thenThrow(new RuntimeException("Error al guardar habilidad"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadService.actualizarHabilidadPorId(id, habilidadDtoValido);
        }, "Debería propagar la excepción del repositorio");

        verify(habilidadRepository, times(1)).findById(id);
        verify(habilidadRepository, times(1)).save(any(Habilidad.class));
    }

    // ==================== TESTS DELETE HABILIDAD ====================

    @Test
    @DisplayName("deleteHabilidadPorId - Debería eliminar habilidad correctamente")
    void deleteHabilidadPorId_ShouldDeleteHabilidad() {
        // Arrange
        Integer id = 1;
        doNothing().when(habilidadRepository).deleteById(id);

        // Act
        habilidadService.deleteHabilidadPorId(id);

        // Assert - Verificar que se llamó al repositorio exactamente una vez
        verify(habilidadRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteHabilidadPorId - Debería manejar ID no existente")
    void deleteHabilidadPorId_ShouldHandleNonExistentId() {
        // Arrange
        Integer id = 999;
        doThrow(new RuntimeException("Habilidad no encontrada")).when(habilidadRepository).deleteById(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadService.deleteHabilidadPorId(id);
        }, "Debería propagar la excepción del repositorio");

        verify(habilidadRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteHabilidadPorId - Debería manejar ID nulo - Test negativo")
    void deleteHabilidadPorId_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            habilidadService.deleteHabilidadPorId(id);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(habilidadRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteHabilidadPorId - Debería manejar ID negativo")
    void deleteHabilidadPorId_ShouldHandleNegativeId() {
        // Arrange
        Integer id = -1;
        doThrow(new RuntimeException("ID inválido")).when(habilidadRepository).deleteById(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadService.deleteHabilidadPorId(id);
        }, "Debería propagar la excepción del repositorio");

        verify(habilidadRepository, times(1)).deleteById(id);
    }

    // ==================== TESTS GET ALL HABILIDADES ====================

    @Test
    @DisplayName("getAllHabilidad - Debería retornar lista de habilidades cuando existen")
    void getAllHabilidad_ShouldReturnListOfHabilidad_WhenExists() {
        // Arrange
        Habilidad habilidad2 = Habilidad.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .usuario(usuario)
                .build();

        HabilidadDto habilidadDto2 = HabilidadDto.builder()
                .id(2)
                .nombre("Trabajo en Equipo")
                .build();

        List<Habilidad> listaHabilidades = Arrays.asList(habilidadValida, habilidad2);
        List<HabilidadDto> listaHabilidadesDto = Arrays.asList(habilidadDtoValido, habilidadDto2);

        when(habilidadRepository.findAll()).thenReturn(listaHabilidades);
        when(habilidadMapper.toHabilidadDto(habilidadValida)).thenReturn(habilidadDtoValido);
        when(habilidadMapper.toHabilidadDto(habilidad2)).thenReturn(habilidadDto2);

        // Act
        List<HabilidadDto> resultado = habilidadService.getAllHabilidad();

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula");
        assertEquals(2, resultado.size(), "Debería retornar 2 habilidades");
        assertEquals("Comunicación Efectiva", resultado.get(0).getNombre(), "El nombre de la primera habilidad debería coincidir");
        assertEquals("Trabajo en Equipo", resultado.get(1).getNombre(), "El nombre de la segunda habilidad debería coincidir");

        verify(habilidadRepository, times(1)).findAll();
        verify(habilidadMapper, times(2)).toHabilidadDto(any(Habilidad.class));
    }

    @Test
    @DisplayName("getAllHabilidad - Debería retornar lista vacía cuando no hay habilidades")
    void getAllHabilidad_ShouldReturnEmptyList_WhenNoHabilidadExist() {
        // Arrange
        when(habilidadRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<HabilidadDto> resultado = habilidadService.getAllHabilidad();

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");

        verify(habilidadRepository, times(1)).findAll();
        verify(habilidadMapper, never()).toHabilidadDto(any(Habilidad.class));
    }

    @Test
    @DisplayName("getAllHabilidad - Debería manejar excepción del repositorio")
    void getAllHabilidad_ShouldHandleRepositoryException() {
        // Arrange
        when(habilidadRepository.findAll()).thenThrow(new RuntimeException("Error al obtener habilidades"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadService.getAllHabilidad();
        }, "Debería propagar la excepción del repositorio");

        verify(habilidadRepository, times(1)).findAll();
        verify(habilidadMapper, never()).toHabilidadDto(any(Habilidad.class));
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("saveHabilidad - Debería manejar nombre con caracteres especiales")
    void saveHabilidad_ShouldHandleNombreWithSpecialCharacters() {
        // Arrange
        String nombreEspecial = "Comunicación & Trabajo en Equipo!";
        HabilidadDto habilidadDtoEspecial = HabilidadDto.builder()
                .nombre(nombreEspecial)
                .build();

        Habilidad habilidadEspecial = Habilidad.builder()
                .nombre(nombreEspecial)
                .usuario(usuario)
                .build();

        when(habilidadMapper.toHabilidad(any(HabilidadDto.class))).thenReturn(habilidadEspecial);
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadEspecial);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoEspecial);

        // Act
        HabilidadDto resultado = habilidadService.saveHabilidad(habilidadDtoEspecial);

        // Assert
        assertNotNull(resultado);
        assertEquals(nombreEspecial, resultado.getNombre(), "El nombre con caracteres especiales debería mantenerse");

        verify(habilidadRepository, times(1)).save(any(Habilidad.class));
    }

    @Test
    @DisplayName("saveHabilidad - Debería manejar nombre con espacios al inicio y al final")
    void saveHabilidad_ShouldHandleNombreWithSpaces() {
        // Arrange
        String nombreConEspacios = "  Comunicación  ";
        HabilidadDto habilidadDtoConEspacios = HabilidadDto.builder()
                .nombre(nombreConEspacios)
                .build();

        Habilidad habilidadConEspacios = Habilidad.builder()
                .nombre(nombreConEspacios)
                .usuario(usuario)
                .build();

        when(habilidadMapper.toHabilidad(any(HabilidadDto.class))).thenReturn(habilidadConEspacios);
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadConEspacios);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoConEspacios);

        // Act
        HabilidadDto resultado = habilidadService.saveHabilidad(habilidadDtoConEspacios);

        // Assert
        assertNotNull(resultado);
        assertEquals(nombreConEspacios, resultado.getNombre(), "El nombre con espacios debería mantenerse");

        verify(habilidadRepository, times(1)).save(any(Habilidad.class));
    }

    @Test
    @DisplayName("actualizarHabilidadPorId - Debería manejar ID en límite de Integer")
    void actualizarHabilidadPorId_ShouldHandleIntegerBoundaryValues() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        when(habilidadRepository.findById(idMax)).thenReturn(Optional.empty());

        // Act
        HabilidadDto resultado = habilidadService.actualizarHabilidadPorId(idMax, habilidadDtoValido);

        // Assert
        assertNull(resultado, "Debería retornar null para ID no existente");

        verify(habilidadRepository, times(1)).findById(idMax);
        verify(habilidadRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteHabilidadPorId - Debería manejar ID en límite de Integer")
    void deleteHabilidadPorId_ShouldHandleIntegerBoundaryValues() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        doThrow(new RuntimeException("Error al eliminar")).when(habilidadRepository).deleteById(idMin);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            habilidadService.deleteHabilidadPorId(idMin);
        }, "Debería propagar la excepción del repositorio");

        verify(habilidadRepository, times(1)).deleteById(idMin);
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con los mocks
        verifyNoInteractions(habilidadRepository);
        verifyNoInteractions(habilidadMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas")
    void verifyExpectedInteractions() {
        // Arrange
        when(habilidadRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        habilidadService.getAllHabilidad();

        // Assert - Verificar interacciones exactas
        verify(habilidadRepository, times(1)).findAll();
        verify(habilidadRepository, never()).save(any());
        verify(habilidadRepository, never()).findById(any());
        verify(habilidadRepository, never()).deleteById(any());
        verify(habilidadMapper, never()).toHabilidadDto(any(Habilidad.class));
    }

    @Test
    @DisplayName("Verificar interacciones secuenciales en saveHabilidad")
    void verifySequentialInteractionsInSaveHabilidad() {
        // Arrange
        when(habilidadMapper.toHabilidad(any(HabilidadDto.class))).thenReturn(habilidadValida);
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadValida);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoValido);

        // Act
        habilidadService.saveHabilidad(habilidadDtoValido);

        // Assert - Verificar orden de interacciones
        var inOrder = inOrder(habilidadMapper, habilidadRepository);
        inOrder.verify(habilidadMapper).toHabilidad(any(HabilidadDto.class));
        inOrder.verify(habilidadRepository).save(any(Habilidad.class));
        inOrder.verify(habilidadMapper).toHabilidadDto(any(Habilidad.class));
    }

    @Test
    @DisplayName("Verificar interacciones secuenciales en actualizarHabilidadPorId")
    void verifySequentialInteractionsInActualizarHabilidadPorId() {
        // Arrange
        Integer id = 1;
        when(habilidadRepository.findById(id)).thenReturn(Optional.of(habilidadValida));
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadValida);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoValido);

        // Act
        habilidadService.actualizarHabilidadPorId(id, habilidadDtoValido);

        // Assert - Verificar orden de interacciones
        var inOrder = inOrder(habilidadRepository, habilidadMapper);
        inOrder.verify(habilidadRepository).findById(id);
        inOrder.verify(habilidadRepository).save(any(Habilidad.class));
        inOrder.verify(habilidadMapper).toHabilidadDto(any(Habilidad.class));
    }

    // ==================== TESTS DE MÚLTIPLES OPERACIONES ====================

    @Test
    @DisplayName("Debería realizar operaciones CRUD completas correctamente")
    void shouldPerformCompleteCRUDOperations() {
        // Arrange
        // 1. Guardar
        when(habilidadMapper.toHabilidad(any(HabilidadDto.class))).thenReturn(habilidadValida);
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadValida);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoValido);

        // 2. Obtener todos
        when(habilidadRepository.findAll()).thenReturn(Collections.singletonList(habilidadValida));

        // 3. Actualizar
        Integer id = 1;
        HabilidadDto habilidadDtoActualizado = HabilidadDto.builder()
                .id(id)
                .nombre("Habilidad Actualizada")
                .build();
        when(habilidadRepository.findById(id)).thenReturn(Optional.of(habilidadValida));
        when(habilidadRepository.save(any(Habilidad.class))).thenReturn(habilidadValida);
        when(habilidadMapper.toHabilidadDto(any(Habilidad.class))).thenReturn(habilidadDtoActualizado);

        // Act & Assert - 1. Guardar
        HabilidadDto saved = habilidadService.saveHabilidad(habilidadDtoValido);
        assertNotNull(saved);
        assertEquals("Comunicación Efectiva", saved.getNombre());

        // Act & Assert - 2. Obtener todos
        List<HabilidadDto> all = habilidadService.getAllHabilidad();
        assertNotNull(all);
        assertEquals(1, all.size());

        // Act & Assert - 3. Actualizar
        HabilidadDto updated = habilidadService.actualizarHabilidadPorId(id, habilidadDtoActualizado);
        assertNotNull(updated);
        assertEquals("Habilidad Actualizada", updated.getNombre());

        // Act & Assert - 4. Eliminar
        doNothing().when(habilidadRepository).deleteById(id);
        habilidadService.deleteHabilidadPorId(id);
        verify(habilidadRepository, times(1)).deleteById(id);

        // Verificar todas las interacciones
        verify(habilidadMapper, times(2)).toHabilidad(any(HabilidadDto.class));
        verify(habilidadRepository, times(4)).save(any(Habilidad.class));
        verify(habilidadRepository, times(1)).findAll();
        verify(habilidadRepository, times(1)).findById(id);
        verify(habilidadMapper, times(3)).toHabilidadDto(any(Habilidad.class));
    }
}