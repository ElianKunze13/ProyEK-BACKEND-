package com.example.demo.service;

import com.example.demo.dto.ImagenDto;
import com.example.demo.mapper.ImagenMapper;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.service.Impl.ImagenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase ImagenServiceImpl
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class ImagenServiceImplTest {

    @Mock
    private ImagenRepository imagenRepository;

    @Mock
    private ImagenMapper imagenMapper;

    @InjectMocks
    private ImagenServiceImpl imagenService;

    private Imagen imagenValida;
    private ImagenDto imagenDtoValido;
    private Imagen imagenGuardada;
    private ImagenDto imagenDtoGuardado;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear imagen válida
        imagenValida = Imagen.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Imagen de prueba")
                .build();

        // Crear ImagenDto válido
        imagenDtoValido = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Imagen de prueba")
                .build();

        // Crear imagen guardada (con ID generado)
        imagenGuardada = Imagen.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Imagen de prueba")
                .build();

        // Crear ImagenDto guardado
        imagenDtoGuardado = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Imagen de prueba")
                .build();
    }

    // ==================== TESTS SAVE IMAGEN ====================

    @Test
    @DisplayName("saveImagen - Debería guardar una imagen correctamente - Caso feliz")
    void saveImagen_ShouldSaveImagenCorrectly() {
        // Arrange
        when(imagenMapper.toImagen(imagenDtoValido)).thenReturn(imagenValida);
        when(imagenRepository.save(imagenValida)).thenReturn(imagenGuardada);
        when(imagenMapper.toImagenDto(imagenGuardada)).thenReturn(imagenDtoGuardado);

        // Act
        ImagenDto resultado = imagenService.saveImagen(imagenDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(imagenDtoValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(imagenDtoValido.getUrl(), resultado.getUrl(), "La URL debería coincidir");
        assertEquals(imagenDtoValido.getAlt(), resultado.getAlt(), "El texto alternativo debería coincidir");

        // Verificar interacciones
        verify(imagenMapper, times(1)).toImagen(imagenDtoValido);
        verify(imagenRepository, times(1)).save(imagenValida);
        verify(imagenMapper, times(1)).toImagenDto(imagenGuardada);
    }

    @Test
    @DisplayName("saveImagen - Debería guardar una imagen sin ID (nuevo registro)")
    void saveImagen_ShouldSaveImagenWithoutId() {
        // Arrange - ImagenDto sin ID (nuevo registro)
        ImagenDto imagenDtoSinId = ImagenDto.builder()
                .url("https://ejemplo.com/nueva-imagen.jpg")
                .alt("Nueva imagen sin ID")
                .build();

        Imagen imagenSinId = Imagen.builder()
                .url("https://ejemplo.com/nueva-imagen.jpg")
                .alt("Nueva imagen sin ID")
                .build();

        Imagen imagenConIdGenerado = Imagen.builder()
                .id(2)
                .url("https://ejemplo.com/nueva-imagen.jpg")
                .alt("Nueva imagen sin ID")
                .build();

        ImagenDto imagenDtoConIdGenerado = ImagenDto.builder()
                .id(2)
                .url("https://ejemplo.com/nueva-imagen.jpg")
                .alt("Nueva imagen sin ID")
                .build();

        when(imagenMapper.toImagen(imagenDtoSinId)).thenReturn(imagenSinId);
        when(imagenRepository.save(imagenSinId)).thenReturn(imagenConIdGenerado);
        when(imagenMapper.toImagenDto(imagenConIdGenerado)).thenReturn(imagenDtoConIdGenerado);

        // Act
        ImagenDto resultado = imagenService.saveImagen(imagenDtoSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.getId(), "El ID generado debería ser 2");
        assertEquals(imagenDtoSinId.getUrl(), resultado.getUrl(), "La URL debería coincidir");
        assertEquals(imagenDtoSinId.getAlt(), resultado.getAlt(), "El texto alternativo debería coincidir");

        verify(imagenMapper, times(1)).toImagen(imagenDtoSinId);
        verify(imagenRepository, times(1)).save(imagenSinId);
        verify(imagenMapper, times(1)).toImagenDto(imagenConIdGenerado);
    }

    @Test
    @DisplayName("saveImagen - Debería guardar una imagen con campos vacíos")
    void saveImagen_ShouldSaveImagenWithEmptyFields() {
        // Arrange
        ImagenDto imagenDtoVacio = ImagenDto.builder()
                .url("")
                .alt("")
                .build();

        Imagen imagenVacia = Imagen.builder()
                .url("")
                .alt("")
                .build();

        Imagen imagenGuardadaVacia = Imagen.builder()
                .id(3)
                .url("")
                .alt("")
                .build();

        ImagenDto imagenDtoGuardadoVacio = ImagenDto.builder()
                .id(3)
                .url("")
                .alt("")
                .build();

        when(imagenMapper.toImagen(imagenDtoVacio)).thenReturn(imagenVacia);
        when(imagenRepository.save(imagenVacia)).thenReturn(imagenGuardadaVacia);
        when(imagenMapper.toImagenDto(imagenGuardadaVacia)).thenReturn(imagenDtoGuardadoVacio);

        // Act
        ImagenDto resultado = imagenService.saveImagen(imagenDtoVacio);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getUrl(), "La URL vacía debería mantenerse");
        assertEquals("", resultado.getAlt(), "El alt vacío debería mantenerse");

        verify(imagenMapper, times(1)).toImagen(imagenDtoVacio);
        verify(imagenRepository, times(1)).save(imagenVacia);
        verify(imagenMapper, times(1)).toImagenDto(imagenGuardadaVacia);
    }

    @Test
    @DisplayName("saveImagen - Debería guardar una imagen con caracteres especiales")
    void saveImagen_ShouldSaveImagenWithSpecialCharacters() {
        // Arrange
        String urlEspecial = "https://ejemplo.com/imagen-con-espacios%20y-simbolos!@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~.jpg";
        String altEspecial = "Texto alternativo con acentos: áéíóú ñü y caracteres especiales !@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~";

        ImagenDto imagenDtoEspecial = ImagenDto.builder()
                .id(4)
                .url(urlEspecial)
                .alt(altEspecial)
                .build();

        Imagen imagenEspecial = Imagen.builder()
                .id(4)
                .url(urlEspecial)
                .alt(altEspecial)
                .build();

        when(imagenMapper.toImagen(imagenDtoEspecial)).thenReturn(imagenEspecial);
        when(imagenRepository.save(imagenEspecial)).thenReturn(imagenEspecial);
        when(imagenMapper.toImagenDto(imagenEspecial)).thenReturn(imagenDtoEspecial);

        // Act
        ImagenDto resultado = imagenService.saveImagen(imagenDtoEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(urlEspecial, resultado.getUrl(), "La URL con caracteres especiales debería mantenerse");
        assertEquals(altEspecial, resultado.getAlt(), "El alt con caracteres especiales debería mantenerse");

        verify(imagenMapper, times(1)).toImagen(imagenDtoEspecial);
        verify(imagenRepository, times(1)).save(imagenEspecial);
        verify(imagenMapper, times(1)).toImagenDto(imagenEspecial);
    }

    @Test
    @DisplayName("saveImagen - Debería manejar DTO nulo - Test negativo")
    void saveImagen_ShouldHandleNullDto() {
        // Arrange
        when(imagenMapper.toImagen(null)).thenThrow(new NullPointerException("ImagenDto no puede ser nulo"));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            imagenService.saveImagen(null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(imagenMapper, times(1)).toImagen(null);
        verify(imagenRepository, never()).save(any());
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("saveImagen - Debería propagar DataIntegrityViolationException del repositorio")
    void saveImagen_ShouldPropagateDataIntegrityViolationException() {
        // Arrange
        when(imagenMapper.toImagen(imagenDtoValido)).thenReturn(imagenValida);
        when(imagenRepository.save(imagenValida)).thenThrow(new DataIntegrityViolationException("Violación de integridad de datos"));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            imagenService.saveImagen(imagenDtoValido);
        }, "Debería propagar DataIntegrityViolationException");

        verify(imagenMapper, times(1)).toImagen(imagenDtoValido);
        verify(imagenRepository, times(1)).save(imagenValida);
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("saveImagen - Debería propagar RuntimeException del repositorio")
    void saveImagen_ShouldPropagateRuntimeException() {
        // Arrange
        when(imagenMapper.toImagen(imagenDtoValido)).thenReturn(imagenValida);
        when(imagenRepository.save(imagenValida)).thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imagenService.saveImagen(imagenDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error de base de datos", exception.getMessage(), "El mensaje de excepción debería ser el mismo");

        verify(imagenMapper, times(1)).toImagen(imagenDtoValido);
        verify(imagenRepository, times(1)).save(imagenValida);
        verify(imagenMapper, never()).toImagenDto(any());
    }

    // ==================== TESTS DELETE IMAGEN ====================

    @Test
    @DisplayName("deleteImagenPorId - Debería eliminar una imagen por ID correctamente")
    void deleteImagenPorId_ShouldDeleteImagenCorrectly() {
        // Arrange
        Integer id = 1;
        doNothing().when(imagenRepository).deleteById(id);

        // Act
        imagenService.deleteImagenPorId(id);

        // Assert
        verify(imagenRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteImagenPorId - Debería manejar ID nulo")
    void deleteImagenPorId_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            imagenService.deleteImagenPorId(id);
        }, "Debería lanzar IllegalArgumentException cuando el ID es nulo");

        verify(imagenRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteImagenPorId - No debería lanzar excepción al eliminar ID inexistente")
    void deleteImagenPorId_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        doNothing().when(imagenRepository).deleteById(id);

        // Act & Assert
        assertDoesNotThrow(() -> {
            imagenService.deleteImagenPorId(id);
        }, "No debería lanzar excepción al eliminar un ID que no existe");

        verify(imagenRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteImagenPorId - Debería propagar RuntimeException del repositorio")
    void deleteImagenPorId_ShouldPropagateRuntimeException() {
        // Arrange
        Integer id = 1;
        doThrow(new RuntimeException("Error al eliminar")).when(imagenRepository).deleteById(id);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imagenService.deleteImagenPorId(id);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al eliminar", exception.getMessage(), "El mensaje de excepción debería ser el mismo");

        verify(imagenRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteImagenPorId - Debería manejar ID con valor negativo")
    void deleteImagenPorId_ShouldHandleNegativeId() {
        // Arrange
        Integer idNegativo = -1;
        doNothing().when(imagenRepository).deleteById(idNegativo);

        // Act
        imagenService.deleteImagenPorId(idNegativo);

        // Assert
        verify(imagenRepository, times(1)).deleteById(idNegativo);
    }

    @Test
    @DisplayName("deleteImagenPorId - Debería manejar ID con valor cero")
    void deleteImagenPorId_ShouldHandleZeroId() {
        // Arrange
        Integer idCero = 0;
        doNothing().when(imagenRepository).deleteById(idCero);

        // Act
        imagenService.deleteImagenPorId(idCero);

        // Assert
        verify(imagenRepository, times(1)).deleteById(idCero);
    }

    @Test
    @DisplayName("deleteImagenPorId - Debería manejar ID con Integer.MAX_VALUE")
    void deleteImagenPorId_ShouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        doNothing().when(imagenRepository).deleteById(idMax);

        // Act
        imagenService.deleteImagenPorId(idMax);

        // Assert
        verify(imagenRepository, times(1)).deleteById(idMax);
    }

    // ==================== TESTS GET ALL IMAGENES ====================

    @Test
    @DisplayName("getAllImagen - Debería retornar todas las imágenes")
    void getAllImagen_ShouldReturnAllImagenes() {
        // Arrange
        Imagen imagen1 = Imagen.builder()
                .id(1)
                .url("https://ejemplo.com/imagen1.jpg")
                .alt("Imagen 1")
                .build();

        Imagen imagen2 = Imagen.builder()
                .id(2)
                .url("https://ejemplo.com/imagen2.jpg")
                .alt("Imagen 2")
                .build();

        ImagenDto imagenDto1 = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen1.jpg")
                .alt("Imagen 1")
                .build();

        ImagenDto imagenDto2 = ImagenDto.builder()
                .id(2)
                .url("https://ejemplo.com/imagen2.jpg")
                .alt("Imagen 2")
                .build();

        List<Imagen> imagenes = Arrays.asList(imagen1, imagen2);
        when(imagenRepository.findAll()).thenReturn(imagenes);
        when(imagenMapper.toImagenDto(imagen1)).thenReturn(imagenDto1);
        when(imagenMapper.toImagenDto(imagen2)).thenReturn(imagenDto2);

        // Act
        List<ImagenDto> resultado = imagenService.getAllImagen();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.size(), "Debería haber 2 imágenes");
        assertEquals(imagen1.getId(), resultado.get(0).getId(), "El ID de la primera imagen debería coincidir");
        assertEquals(imagen2.getId(), resultado.get(1).getId(), "El ID de la segunda imagen debería coincidir");
        assertEquals(imagen1.getUrl(), resultado.get(0).getUrl(), "La URL de la primera imagen debería coincidir");
        assertEquals(imagen2.getUrl(), resultado.get(1).getUrl(), "La URL de la segunda imagen debería coincidir");

        verify(imagenRepository, times(1)).findAll();
        verify(imagenMapper, times(1)).toImagenDto(imagen1);
        verify(imagenMapper, times(1)).toImagenDto(imagen2);
    }

    @Test
    @DisplayName("getAllImagen - Debería retornar lista vacía cuando no hay imágenes")
    void getAllImagen_ShouldReturnEmptyList_WhenNoImagenes() {
        // Arrange
        when(imagenRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ImagenDto> resultado = imagenService.getAllImagen();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");

        verify(imagenRepository, times(1)).findAll();
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("getAllImagen - Debería retornar lista con una imagen")
    void getAllImagen_ShouldReturnListWithOneImagen() {
        // Arrange
        Imagen imagen = Imagen.builder()
                .id(1)
                .url("https://ejemplo.com/imagen-unica.jpg")
                .alt("Imagen única")
                .build();

        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen-unica.jpg")
                .alt("Imagen única")
                .build();

        List<Imagen> imagenes = Collections.singletonList(imagen);
        when(imagenRepository.findAll()).thenReturn(imagenes);
        when(imagenMapper.toImagenDto(imagen)).thenReturn(imagenDto);

        // Act
        List<ImagenDto> resultado = imagenService.getAllImagen();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1, resultado.size(), "Debería haber 1 imagen");
        assertEquals(imagen.getId(), resultado.get(0).getId(), "El ID debería coincidir");
        assertEquals(imagen.getUrl(), resultado.get(0).getUrl(), "La URL debería coincidir");

        verify(imagenRepository, times(1)).findAll();
        verify(imagenMapper, times(1)).toImagenDto(imagen);
    }

    @Test
    @DisplayName("getAllImagen - Debería manejar imágenes con caracteres especiales")
    void getAllImagen_ShouldHandleImagenesWithSpecialCharacters() {
        // Arrange
        String urlEspecial = "https://ejemplo.com/imagen-con-espacios%20y-simbolos!@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~.jpg";
        String altEspecial = "Texto alternativo con acentos: áéíóú ñü y caracteres especiales !@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~";

        Imagen imagen = Imagen.builder()
                .id(1)
                .url(urlEspecial)
                .alt(altEspecial)
                .build();

        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url(urlEspecial)
                .alt(altEspecial)
                .build();

        List<Imagen> imagenes = Collections.singletonList(imagen);
        when(imagenRepository.findAll()).thenReturn(imagenes);
        when(imagenMapper.toImagenDto(imagen)).thenReturn(imagenDto);

        // Act
        List<ImagenDto> resultado = imagenService.getAllImagen();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1, resultado.size(), "Debería haber 1 imagen");
        assertEquals(urlEspecial, resultado.get(0).getUrl(), "La URL con caracteres especiales debería mantenerse");
        assertEquals(altEspecial, resultado.get(0).getAlt(), "El alt con caracteres especiales debería mantenerse");

        verify(imagenRepository, times(1)).findAll();
        verify(imagenMapper, times(1)).toImagenDto(imagen);
    }

    @Test
    @DisplayName("getAllImagen - Debería propagar RuntimeException del repositorio")
    void getAllImagen_ShouldPropagateRuntimeException() {
        // Arrange
        when(imagenRepository.findAll()).thenThrow(new RuntimeException("Error al obtener imágenes"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imagenService.getAllImagen();
        }, "Debería propagar RuntimeException");

        assertEquals("Error al obtener imágenes", exception.getMessage(), "El mensaje de excepción debería ser el mismo");

        verify(imagenRepository, times(1)).findAll();
        verify(imagenMapper, never()).toImagenDto(any());
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("saveImagen - Debería manejar URL extremadamente larga")
    void saveImagen_ShouldHandleVeryLongUrl() {
        // Arrange
        String longUrl = "https://ejemplo.com/" + "a".repeat(500) + ".jpg";
        ImagenDto imagenDtoLarga = ImagenDto.builder()
                .url(longUrl)
                .alt("Imagen con URL larga")
                .build();

        Imagen imagenLarga = Imagen.builder()
                .url(longUrl)
                .alt("Imagen con URL larga")
                .build();

        when(imagenMapper.toImagen(imagenDtoLarga)).thenReturn(imagenLarga);
        when(imagenRepository.save(imagenLarga)).thenReturn(imagenLarga);
        when(imagenMapper.toImagenDto(imagenLarga)).thenReturn(imagenDtoLarga);

        // Act
        ImagenDto resultado = imagenService.saveImagen(imagenDtoLarga);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(longUrl, resultado.getUrl(), "La URL larga debería mantenerse");

        verify(imagenMapper, times(1)).toImagen(imagenDtoLarga);
        verify(imagenRepository, times(1)).save(imagenLarga);
        verify(imagenMapper, times(1)).toImagenDto(imagenLarga);
    }

    @Test
    @DisplayName("saveImagen - Debería manejar alt extremadamente largo")
    void saveImagen_ShouldHandleVeryLongAlt() {
        // Arrange
        String longAlt = "Texto alternativo muy largo " + "b".repeat(500);
        ImagenDto imagenDtoAltLargo = ImagenDto.builder()
                .url("https://ejemplo.com/imagen.jpg")
                .alt(longAlt)
                .build();

        Imagen imagenAltLargo = Imagen.builder()
                .url("https://ejemplo.com/imagen.jpg")
                .alt(longAlt)
                .build();

        when(imagenMapper.toImagen(imagenDtoAltLargo)).thenReturn(imagenAltLargo);
        when(imagenRepository.save(imagenAltLargo)).thenReturn(imagenAltLargo);
        when(imagenMapper.toImagenDto(imagenAltLargo)).thenReturn(imagenDtoAltLargo);

        // Act
        ImagenDto resultado = imagenService.saveImagen(imagenDtoAltLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(longAlt, resultado.getAlt(), "El alt largo debería mantenerse");

        verify(imagenMapper, times(1)).toImagen(imagenDtoAltLargo);
        verify(imagenRepository, times(1)).save(imagenAltLargo);
        verify(imagenMapper, times(1)).toImagenDto(imagenAltLargo);
    }

    @Test
    @DisplayName("getAllImagen - Debería manejar lista con múltiples imágenes (10+)")
    void getAllImagen_ShouldHandleMultipleImagenes() {
        // Arrange - Crear 10 imágenes
        List<Imagen> imagenes = new java.util.ArrayList<>();
        List<ImagenDto> imagenDtos = new java.util.ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Imagen imagen = Imagen.builder()
                    .id(i)
                    .url("https://ejemplo.com/imagen-" + i + ".jpg")
                    .alt("Imagen " + i)
                    .build();
            imagenes.add(imagen);

            ImagenDto imagenDto = ImagenDto.builder()
                    .id(i)
                    .url("https://ejemplo.com/imagen-" + i + ".jpg")
                    .alt("Imagen " + i)
                    .build();
            imagenDtos.add(imagenDto);

            when(imagenMapper.toImagenDto(imagen)).thenReturn(imagenDto);
        }

        when(imagenRepository.findAll()).thenReturn(imagenes);

        // Act
        List<ImagenDto> resultado = imagenService.getAllImagen();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(10, resultado.size(), "Debería haber 10 imágenes");

        for (int i = 0; i < 10; i++) {
            assertEquals(i + 1, resultado.get(i).getId(), "El ID de la imagen " + (i + 1) + " debería coincidir");
            assertEquals("https://ejemplo.com/imagen-" + (i + 1) + ".jpg", resultado.get(i).getUrl(),
                    "La URL de la imagen " + (i + 1) + " debería coincidir");
        }

        verify(imagenRepository, times(1)).findAll();
        verify(imagenMapper, times(10)).toImagenDto(any(Imagen.class));
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con los mocks
        verifyNoInteractions(imagenRepository);
        verifyNoInteractions(imagenMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en saveImagen")
    void verifyExpectedInteractionsInSaveImagen() {
        // Arrange
        when(imagenMapper.toImagen(imagenDtoValido)).thenReturn(imagenValida);
        when(imagenRepository.save(imagenValida)).thenReturn(imagenGuardada);
        when(imagenMapper.toImagenDto(imagenGuardada)).thenReturn(imagenDtoGuardado);

        // Act
        imagenService.saveImagen(imagenDtoValido);

        // Assert
        verify(imagenMapper, times(1)).toImagen(imagenDtoValido);
        verify(imagenRepository, times(1)).save(imagenValida);
        verify(imagenMapper, times(1)).toImagenDto(imagenGuardada);
        verifyNoMoreInteractions(imagenRepository);
        verifyNoMoreInteractions(imagenMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en deleteImagenPorId")
    void verifyExpectedInteractionsInDeleteImagenPorId() {
        // Arrange
        Integer id = 1;
        doNothing().when(imagenRepository).deleteById(id);

        // Act
        imagenService.deleteImagenPorId(id);

        // Assert
        verify(imagenRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(imagenRepository);
        verifyNoInteractions(imagenMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en getAllImagen")
    void verifyExpectedInteractionsInGetAllImagen() {
        // Arrange
        List<Imagen> imagenes = Collections.singletonList(imagenValida);
        when(imagenRepository.findAll()).thenReturn(imagenes);
        when(imagenMapper.toImagenDto(imagenValida)).thenReturn(imagenDtoValido);

        // Act
        imagenService.getAllImagen();

        // Assert
        verify(imagenRepository, times(1)).findAll();
        verify(imagenMapper, times(1)).toImagenDto(imagenValida);
        verifyNoMoreInteractions(imagenRepository);
        verifyNoMoreInteractions(imagenMapper);
    }

    // ==================== TESTS DE INTEGRACIÓN DE OPERACIONES ====================

    @Test
    @DisplayName("Operaciones CRUD completas - Guardar, Listar y Eliminar")
    void completeCrudOperations_ShouldWorkCorrectly() {
        // 1. Guardar imagen
        when(imagenMapper.toImagen(imagenDtoValido)).thenReturn(imagenValida);
        when(imagenRepository.save(imagenValida)).thenReturn(imagenGuardada);
        when(imagenMapper.toImagenDto(imagenGuardada)).thenReturn(imagenDtoGuardado);

        ImagenDto resultadoGuardado = imagenService.saveImagen(imagenDtoValido);
        assertNotNull(resultadoGuardado, "La imagen guardada no debería ser nula");
        assertEquals(1, resultadoGuardado.getId(), "El ID de la imagen guardada debería ser 1");

        // 2. Listar todas las imágenes
        List<Imagen> imagenes = Collections.singletonList(imagenGuardada);
        when(imagenRepository.findAll()).thenReturn(imagenes);
        when(imagenMapper.toImagenDto(imagenGuardada)).thenReturn(imagenDtoGuardado);

        List<ImagenDto> resultadoLista = imagenService.getAllImagen();
        assertNotNull(resultadoLista, "La lista no debería ser nula");
        assertEquals(1, resultadoLista.size(), "Debería haber 1 imagen");
        assertEquals(1, resultadoLista.get(0).getId(), "El ID de la imagen en la lista debería ser 1");

        // 3. Eliminar imagen
        doNothing().when(imagenRepository).deleteById(1);
        imagenService.deleteImagenPorId(1);

        // Verificar interacciones
        verify(imagenMapper, times(1)).toImagen(imagenDtoValido);
        verify(imagenRepository, times(1)).save(imagenValida);
        verify(imagenMapper, times(1)).toImagenDto(imagenGuardada);
        verify(imagenRepository, times(1)).findAll();
        verify(imagenRepository, times(1)).deleteById(1);
        verify(imagenMapper, times(2)).toImagenDto(imagenGuardada);
    }

    @Test
    @DisplayName("Operaciones CRUD completas - Guardar múltiples imágenes y listar")
    void completeCrudOperations_MultipleImagenes() {
        // Crear imágenes
        Imagen imagen1 = Imagen.builder().id(1).url("url1").alt("alt1").build();
        Imagen imagen2 = Imagen.builder().id(2).url("url2").alt("alt2").build();
        ImagenDto dto1 = ImagenDto.builder().id(1).url("url1").alt("alt1").build();
        ImagenDto dto2 = ImagenDto.builder().id(2).url("url2").alt("alt2").build();

        // Guardar imagen 1
        when(imagenMapper.toImagen(any(ImagenDto.class))).thenReturn(imagen1);
        when(imagenRepository.save(imagen1)).thenReturn(imagen1);
        when(imagenMapper.toImagenDto(imagen1)).thenReturn(dto1);
        imagenService.saveImagen(dto1);

        // Guardar imagen 2
        when(imagenMapper.toImagen(any(ImagenDto.class))).thenReturn(imagen2);
        when(imagenRepository.save(imagen2)).thenReturn(imagen2);
        when(imagenMapper.toImagenDto(imagen2)).thenReturn(dto2);
        imagenService.saveImagen(dto2);

        // Listar todas
        List<Imagen> imagenes = Arrays.asList(imagen1, imagen2);
        when(imagenRepository.findAll()).thenReturn(imagenes);
        when(imagenMapper.toImagenDto(imagen1)).thenReturn(dto1);
        when(imagenMapper.toImagenDto(imagen2)).thenReturn(dto2);

        List<ImagenDto> resultado = imagenService.getAllImagen();

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula");
        assertEquals(2, resultado.size(), "Debería haber 2 imágenes");
        assertEquals(1, resultado.get(0).getId(), "El ID de la primera imagen debería ser 1");
        assertEquals(2, resultado.get(1).getId(), "El ID de la segunda imagen debería ser 2");

        // Eliminar imagen 1
        doNothing().when(imagenRepository).deleteById(1);
        imagenService.deleteImagenPorId(1);

        verify(imagenRepository, times(1)).deleteById(1);
        verify(imagenRepository, times(2)).save(any(Imagen.class));
        verify(imagenRepository, times(1)).findAll();
        verify(imagenMapper, times(2)).toImagen(any(ImagenDto.class));
        verify(imagenMapper, times(4)).toImagenDto(any(Imagen.class));
    }

    // ==================== TEST DE COMPORTAMIENTO CON NULL ====================

    @Test
    @DisplayName("saveImagen - Debería manejar DTO con todos los campos nulos")
    void saveImagen_ShouldHandleDtoWithAllNullFields() {
        // Arrange
        ImagenDto imagenDtoTodoNulo = ImagenDto.builder()
                .id(null)
                .url(null)
                .alt(null)
                .build();

        Imagen imagenTodoNula = Imagen.builder()
                .id(null)
                .url(null)
                .alt(null)
                .build();

        Imagen imagenGuardadaNula = Imagen.builder()
                .id(5)
                .url(null)
                .alt(null)
                .build();

        ImagenDto imagenDtoGuardadoNulo = ImagenDto.builder()
                .id(5)
                .url(null)
                .alt(null)
                .build();

        when(imagenMapper.toImagen(imagenDtoTodoNulo)).thenReturn(imagenTodoNula);
        when(imagenRepository.save(imagenTodoNula)).thenReturn(imagenGuardadaNula);
        when(imagenMapper.toImagenDto(imagenGuardadaNula)).thenReturn(imagenDtoGuardadoNulo);

        // Act
        ImagenDto resultado = imagenService.saveImagen(imagenDtoTodoNulo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(5, resultado.getId(), "El ID generado debería ser 5");
        assertNull(resultado.getUrl(), "La URL debería ser nula");
        assertNull(resultado.getAlt(), "El alt debería ser nulo");

        verify(imagenMapper, times(1)).toImagen(imagenDtoTodoNulo);
        verify(imagenRepository, times(1)).save(imagenTodoNula);
        verify(imagenMapper, times(1)).toImagenDto(imagenGuardadaNula);
    }
}