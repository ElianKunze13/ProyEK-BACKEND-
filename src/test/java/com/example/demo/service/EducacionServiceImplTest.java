package com.example.demo.service;

import com.example.demo.dto.EducacionDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TipoEducacion;
import com.example.demo.mapper.EducacionMapper;
import com.example.demo.model.Educacion;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EducacionRepository;
import com.example.demo.service.Impl.EducacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase EducacionServiceImpl
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class EducacionServiceImplTest {

    @Mock
    private EducacionRepository educacionRepository;

    @Mock
    private EducacionMapper educacionMapper;

    @InjectMocks
    private EducacionServiceImpl educacionService;

    private Educacion educacionValida;
    private EducacionDto educacionDtoValido;
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
                .build();

        // Crear imagen
        imagen = Imagen.builder()
                .id(1)
                .url("titulo.jpg")
                .alt("Imagen del título")
                .build();

        // Crear imagen DTO
        imagenDto = ImagenDto.builder()
                .id(1)
                .url("titulo.jpg")
                .alt("Imagen del título")
                .build();

        // Crear educación válida
        educacionValida = Educacion.builder()
                .id(1)
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática con especialización en software")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Crear EducacionDto válido
        educacionDtoValido = EducacionDto.builder()
                .id(1)
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática con especialización en software")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagenDto)
                .build();
    }

    // ==================== TESTS SAVE EDUCACION ====================

    @Test
    @DisplayName("saveEducacion - Debería guardar una educación correctamente")
    void saveEducacion_ShouldSaveEducacionCorrectly() {
        // Arrange
        EducacionDto educacionDtoCrear = EducacionDto.builder()
                .titulo("Máster en Inteligencia Artificial")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Máster oficial en IA y Machine Learning")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagenDto)
                .build();

        Educacion educacionParaGuardar = Educacion.builder()
                .titulo("Máster en Inteligencia Artificial")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Máster oficial en IA y Machine Learning")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        Educacion educacionGuardada = Educacion.builder()
                .id(2)
                .titulo("Máster en Inteligencia Artificial")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Máster oficial en IA y Machine Learning")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        EducacionDto educacionDtoCreado = EducacionDto.builder()
                .id(2)
                .titulo("Máster en Inteligencia Artificial")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Máster oficial en IA y Machine Learning")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagenDto)
                .build();

        when(educacionMapper.toEducacion(educacionDtoCrear)).thenReturn(educacionParaGuardar);
        when(educacionRepository.save(educacionParaGuardar)).thenReturn(educacionGuardada);
        when(educacionMapper.toEducacionDto(educacionGuardada)).thenReturn(educacionDtoCreado);

        // Act
        EducacionDto resultado = educacionService.saveEducacion(educacionDtoCrear);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.getId(), "El ID generado debería ser 2");
        assertEquals("Máster en Inteligencia Artificial", resultado.getTitulo(), "El título debería coincidir");
        assertEquals(TipoEducacion.FORMAL, resultado.getTipoEducacion(), "El tipo de educación debería coincidir");
        assertNotNull(resultado.getImagen(), "La imagen no debería ser nula");

        verify(educacionMapper, times(1)).toEducacion(educacionDtoCrear);
        verify(educacionRepository, times(1)).save(educacionParaGuardar);
        verify(educacionMapper, times(1)).toEducacionDto(educacionGuardada);
    }

    @Test
    @DisplayName("saveEducacion - Debería guardar una educación sin imagen")
    void saveEducacion_ShouldSaveEducacionWithoutImage() {
        // Arrange
        EducacionDto educacionDtoSinImagen = EducacionDto.builder()
                .titulo("Curso de Spring Boot")
                .fechaObtencion(LocalDate.of(2023, 5, 20))
                .descripcion("Curso avanzado de Spring Boot")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .imagen(null)
                .build();

        Educacion educacionSinImagen = Educacion.builder()
                .titulo("Curso de Spring Boot")
                .fechaObtencion(LocalDate.of(2023, 5, 20))
                .descripcion("Curso avanzado de Spring Boot")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .imagen(null)
                .usuario(usuario)
                .build();

        when(educacionMapper.toEducacion(educacionDtoSinImagen)).thenReturn(educacionSinImagen);
        when(educacionRepository.save(educacionSinImagen)).thenReturn(educacionSinImagen);
        when(educacionMapper.toEducacionDto(educacionSinImagen)).thenReturn(educacionDtoSinImagen);

        // Act
        EducacionDto resultado = educacionService.saveEducacion(educacionDtoSinImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");
        assertEquals("Curso de Spring Boot", resultado.getTitulo(), "El título debería coincidir");
        assertEquals(TipoEducacion.INFORMAL_CURSO, resultado.getTipoEducacion(), "El tipo debería coincidir");

        verify(educacionMapper, times(1)).toEducacion(educacionDtoSinImagen);
        verify(educacionRepository, times(1)).save(educacionSinImagen);
        verify(educacionMapper, times(1)).toEducacionDto(educacionSinImagen);
    }

    @Test
    @DisplayName("saveEducacion - Debería guardar educación con todos los tipos de educación")
    @ParameterizedTest
    @EnumSource(TipoEducacion.class)
    void saveEducacion_ShouldSaveEducacionWithAllTipoEducacion(TipoEducacion tipo) {
        // Arrange
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo("Educación " + tipo.name())
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción para " + tipo.name())
                .tipoEducacion(tipo)
                .build();

        Educacion educacion = Educacion.builder()
                .titulo("Educación " + tipo.name())
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción para " + tipo.name())
                .tipoEducacion(tipo)
                .usuario(usuario)
                .build();

        when(educacionMapper.toEducacion(educacionDto)).thenReturn(educacion);
        when(educacionRepository.save(educacion)).thenReturn(educacion);
        when(educacionMapper.toEducacionDto(educacion)).thenReturn(educacionDto);

        // Act
        EducacionDto resultado = educacionService.saveEducacion(educacionDto);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(tipo, resultado.getTipoEducacion(), "El tipo de educación debería coincidir");

        verify(educacionMapper, times(1)).toEducacion(educacionDto);
        verify(educacionRepository, times(1)).save(educacion);
        verify(educacionMapper, times(1)).toEducacionDto(educacion);
    }

    @Test
    @DisplayName("saveEducacion - Debería lanzar excepción cuando el DTO es nulo")
    void saveEducacion_ShouldThrowException_WhenDtoIsNull() {
        // Arrange
        when(educacionMapper.toEducacion(null)).thenThrow(new NullPointerException("EducacionDto no puede ser nulo"));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            educacionService.saveEducacion(null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(educacionMapper, times(1)).toEducacion(null);
        verify(educacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("saveEducacion - Debería propagar RuntimeException del repositorio")
    void saveEducacion_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        when(educacionMapper.toEducacion(educacionDtoValido)).thenReturn(educacionValida);
        when(educacionRepository.save(educacionValida)).thenThrow(new RuntimeException("Error al guardar educación"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            educacionService.saveEducacion(educacionDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al guardar educación", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(educacionMapper, times(1)).toEducacion(educacionDtoValido);
        verify(educacionRepository, times(1)).save(educacionValida);
    }

    // ==================== TESTS ACTUALIZAR EDUCACION ====================

    @Test
    @DisplayName("actualizarEducacionPorId - Debería actualizar una educación existente correctamente")
    void actualizarEducacionPorId_ShouldUpdateEducacionCorrectly() {
        // Arrange
        Integer id = 1;
        EducacionDto educacionDtoActualizado = EducacionDto.builder()
                .id(id)
                .titulo("Ingeniería Informática Actualizado")
                .fechaObtencion(LocalDate.of(2021, 6, 15))
                .descripcion("Descripción actualizada con más detalles")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .imagen(imagenDto)
                .build();

        when(educacionRepository.findById(id)).thenReturn(Optional.of(educacionValida));
        when(educacionRepository.save(any(Educacion.class))).thenReturn(educacionValida);
        when(educacionMapper.toEducacionDto(any(Educacion.class))).thenReturn(educacionDtoActualizado);

        // Act
        EducacionDto resultado = educacionService.actualizarEducacionPorId(id, educacionDtoActualizado);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(id, resultado.getId(), "El ID debería coincidir");
        assertEquals("Ingeniería Informática Actualizado", resultado.getTitulo(),
                "El título debería estar actualizado");
        assertEquals("Descripción actualizada con más detalles", resultado.getDescripcion(),
                "La descripción debería estar actualizada");
        assertEquals(TipoEducacion.INFORMAL_CURSO, resultado.getTipoEducacion(),
                "El tipo de educación debería estar actualizado");

        // Verificar que se actualizaron los campos
        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, times(1)).save(educacionValida);
        verify(educacionMapper, times(1)).toEducacionDto(educacionValida);
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería lanzar RuntimeException cuando el ID no existe")
    void actualizarEducacionPorId_ShouldThrowRuntimeException_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(educacionRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            educacionService.actualizarEducacionPorId(id, educacionDtoValido);
        }, "Debería lanzar RuntimeException");

        assertTrue(exception.getMessage().contains("Educación no encontrada con id " + id),
                "El mensaje de excepción debería indicar que la educación no fue encontrada");

        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, never()).save(any());
        verify(educacionMapper, never()).toEducacionDto(any());
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería actualizar solo los campos permitidos")
    void actualizarEducacionPorId_ShouldUpdateOnlyAllowedFields() {
        // Arrange
        Integer id = 1;
        EducacionDto educacionDtoActualizado = EducacionDto.builder()
                .id(id)
                .titulo("Nuevo Título")
                .fechaObtencion(LocalDate.of(2023, 12, 31))
                .descripcion("Nueva descripción con más detalles")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .imagen(imagenDto)
                .build();

        when(educacionRepository.findById(id)).thenReturn(Optional.of(educacionValida));
        when(educacionRepository.save(any(Educacion.class))).thenReturn(educacionValida);
        when(educacionMapper.toEducacionDto(any(Educacion.class))).thenReturn(educacionDtoActualizado);

        // Act
        educacionService.actualizarEducacionPorId(id, educacionDtoActualizado);

        // Assert - Verificar que los setters fueron llamados con los valores correctos
        assertEquals("Nuevo Título", educacionValida.getTitulo(), "El título debería estar actualizado");
        assertEquals(LocalDate.of(2023, 12, 31), educacionValida.getFechaObtencion(),
                "La fecha de obtención debería estar actualizada");
        assertEquals("Nueva descripción con más detalles", educacionValida.getDescripcion(),
                "La descripción debería estar actualizada");
        assertEquals(TipoEducacion.AUTODIDACTA, educacionValida.getTipoEducacion(),
                "El tipo de educación debería estar actualizado");

        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, times(1)).save(educacionValida);
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería actualizar la imagen cuando se proporciona y no existe")
    void actualizarEducacionPorId_ShouldCreateNewImage_WhenImageDoesNotExist() {
        // Arrange
        Integer id = 1;
        // Educación sin imagen
        Educacion educacionSinImagen = Educacion.builder()
                .id(1)
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(null)
                .usuario(usuario)
                .build();

        ImagenDto nuevaImagenDto = ImagenDto.builder()
                .url("nuevo-certificado.jpg")
                .alt("Nuevo certificado")
                .build();

        EducacionDto educacionDtoConImagen = EducacionDto.builder()
                .id(id)
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(nuevaImagenDto)
                .build();

        when(educacionRepository.findById(id)).thenReturn(Optional.of(educacionSinImagen));
        when(educacionRepository.save(any(Educacion.class))).thenReturn(educacionSinImagen);
        when(educacionMapper.toEducacionDto(any(Educacion.class))).thenReturn(educacionDtoConImagen);

        // Act
        educacionService.actualizarEducacionPorId(id, educacionDtoConImagen);

        // Assert - Verificar que se creó una nueva imagen
        assertNotNull(educacionSinImagen.getImagen(), "La imagen no debería ser nula");
        assertEquals("nuevo-certificado.jpg", educacionSinImagen.getImagen().getUrl(),
                "La URL de la imagen debería coincidir");
        assertEquals("Nuevo certificado", educacionSinImagen.getImagen().getAlt(),
                "El alt de la imagen debería coincidir");
        assertEquals(educacionSinImagen, educacionSinImagen.getImagen().getEducacion(),
                "La imagen debería estar asociada a la educación");

        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, times(1)).save(educacionSinImagen);
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería actualizar la imagen existente cuando se proporciona")
    void actualizarEducacionPorId_ShouldUpdateExistingImage_WhenImageExists() {
        // Arrange
        Integer id = 1;
        ImagenDto nuevaImagenDto = ImagenDto.builder()
                .url("certificado-actualizado.jpg")
                .alt("Certificado actualizado")
                .build();

        EducacionDto educacionDtoConImagen = EducacionDto.builder()
                .id(id)
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(nuevaImagenDto)
                .build();

        when(educacionRepository.findById(id)).thenReturn(Optional.of(educacionValida));
        when(educacionRepository.save(any(Educacion.class))).thenReturn(educacionValida);
        when(educacionMapper.toEducacionDto(any(Educacion.class))).thenReturn(educacionDtoConImagen);

        // Act
        educacionService.actualizarEducacionPorId(id, educacionDtoConImagen);

        // Assert - Verificar que se actualizó la imagen existente
        assertNotNull(educacionValida.getImagen(), "La imagen no debería ser nula");
        assertEquals("certificado-actualizado.jpg", educacionValida.getImagen().getUrl(),
                "La URL de la imagen debería estar actualizada");
        assertEquals("Certificado actualizado", educacionValida.getImagen().getAlt(),
                "El alt de la imagen debería estar actualizado");

        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, times(1)).save(educacionValida);
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería eliminar la imagen cuando se envía null y existía")
    void actualizarEducacionPorId_ShouldRemoveImage_WhenImageIsNullAndExists() {
        // Arrange
        Integer id = 1;
        EducacionDto educacionDtoSinImagen = EducacionDto.builder()
                .id(id)
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(null)
                .build();

        when(educacionRepository.findById(id)).thenReturn(Optional.of(educacionValida));
        when(educacionRepository.save(any(Educacion.class))).thenReturn(educacionValida);
        when(educacionMapper.toEducacionDto(any(Educacion.class))).thenReturn(educacionDtoSinImagen);

        // Act
        educacionService.actualizarEducacionPorId(id, educacionDtoSinImagen);

        // Assert - Verificar que se eliminó la imagen
        assertNull(educacionValida.getImagen(), "La imagen debería ser nula");

        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, times(1)).save(educacionValida);
    }

    @Test
    @DisplayName("actualizarEducacionPorId - No debería modificar la imagen cuando se envía null y no existía")
    void actualizarEducacionPorId_ShouldNotModifyImage_WhenImageIsNullAndDoesNotExist() {
        // Arrange
        Integer id = 1;
        // Educación sin imagen
        Educacion educacionSinImagen = Educacion.builder()
                .id(1)
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(null)
                .usuario(usuario)
                .build();

        EducacionDto educacionDtoSinImagen = EducacionDto.builder()
                .id(id)
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(null)
                .build();

        when(educacionRepository.findById(id)).thenReturn(Optional.of(educacionSinImagen));
        when(educacionRepository.save(any(Educacion.class))).thenReturn(educacionSinImagen);
        when(educacionMapper.toEducacionDto(any(Educacion.class))).thenReturn(educacionDtoSinImagen);

        // Act
        educacionService.actualizarEducacionPorId(id, educacionDtoSinImagen);

        // Assert - Verificar que la imagen sigue siendo nula
        assertNull(educacionSinImagen.getImagen(), "La imagen debería seguir siendo nula");

        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, times(1)).save(educacionSinImagen);
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería manejar ID nulo - Test negativo")
    void actualizarEducacionPorId_ShouldHandleNullId() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            educacionService.actualizarEducacionPorId(null, educacionDtoValido);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(educacionRepository, never()).findById(any());
        verify(educacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería manejar DTO nulo - Test negativo")
    void actualizarEducacionPorId_ShouldHandleNullDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            educacionService.actualizarEducacionPorId(id, null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(educacionRepository, never()).findById(any());
        verify(educacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería propagar RuntimeException del repositorio al buscar")
    void actualizarEducacionPorId_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        Integer id = 1;
        when(educacionRepository.findById(id)).thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            educacionService.actualizarEducacionPorId(id, educacionDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error de base de datos", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, never()).save(any());
    }

    // ==================== TESTS DELETE EDUCACION ====================

    @Test
    @DisplayName("deleteEducacionPorId - Debería eliminar una educación por ID")
    void deleteEducacionPorId_ShouldDeleteEducacion() {
        // Arrange
        Integer id = 1;
        doNothing().when(educacionRepository).deleteById(id);

        // Act
        educacionService.deleteEducacionPorId(id);

        // Assert
        verify(educacionRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteEducacionPorId - Debería manejar ID nulo - Test negativo")
    void deleteEducacionPorId_ShouldHandleNullId() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            educacionService.deleteEducacionPorId(null);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(educacionRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteEducacionPorId - Debería propagar RuntimeException del repositorio")
    void deleteEducacionPorId_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        Integer id = 1;
        doThrow(new RuntimeException("Error al eliminar educación")).when(educacionRepository).deleteById(id);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            educacionService.deleteEducacionPorId(id);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al eliminar educación", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(educacionRepository, times(1)).deleteById(id);
    }

    // ==================== TESTS GET ALL EDUCACION ====================

    @Test
    @DisplayName("getAllEducacion - Debería retornar lista de educaciones cuando existen")
    void getAllEducacion_ShouldReturnListOfEducaciones_WhenExist() {
        // Arrange
        Educacion educacion2 = Educacion.builder()
                .id(2)
                .titulo("Máster en Inteligencia Artificial")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Máster oficial en IA y Machine Learning")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        List<Educacion> educaciones = Arrays.asList(educacionValida, educacion2);
        List<EducacionDto> educacionesDto = Arrays.asList(educacionDtoValido,
                EducacionDto.builder()
                        .id(2)
                        .titulo("Máster en Inteligencia Artificial")
                        .fechaObtencion(LocalDate.of(2022, 9, 30))
                        .descripcion("Máster oficial en IA y Machine Learning")
                        .tipoEducacion(TipoEducacion.FORMAL)
                        .imagen(imagenDto)
                        .build());

        when(educacionRepository.findAll()).thenReturn(educaciones);
        when(educacionMapper.toEducacionDto(educacionValida)).thenReturn(educacionDtoValido);
        when(educacionMapper.toEducacionDto(educacion2)).thenReturn(educacionesDto.get(1));

        // Act
        List<EducacionDto> resultado = educacionService.getAllEducacion();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.size(), "La lista debería tener 2 elementos");
        assertEquals(educacionDtoValido, resultado.get(0), "El primer elemento debería coincidir");
        assertEquals(educacionesDto.get(1), resultado.get(1), "El segundo elemento debería coincidir");

        verify(educacionRepository, times(1)).findAll();
        verify(educacionMapper, times(1)).toEducacionDto(educacionValida);
        verify(educacionMapper, times(1)).toEducacionDto(educacion2);
    }

    @Test
    @DisplayName("getAllEducacion - Debería retornar lista vacía cuando no hay educaciones")
    void getAllEducacion_ShouldReturnEmptyList_WhenNoEducacionesExist() {
        // Arrange
        when(educacionRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<EducacionDto> resultado = educacionService.getAllEducacion();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");

        verify(educacionRepository, times(1)).findAll();
        verify(educacionMapper, never()).toEducacionDto(any());
    }

    @Test
    @DisplayName("getAllEducacion - Debería manejar educaciones con diferentes tipos")
    void getAllEducacion_ShouldHandleEducacionesWithDifferentTypes() {
        // Arrange
        Educacion educacionFormal = Educacion.builder()
                .id(1)
                .titulo("Carrera Universitaria")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Carrera de grado")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(usuario)
                .build();

        Educacion educacionCurso = Educacion.builder()
                .id(2)
                .titulo("Curso de Python")
                .fechaObtencion(LocalDate.of(2023, 1, 20))
                .descripcion("Curso intensivo")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .usuario(usuario)
                .build();

        Educacion educacionAutodidacta = Educacion.builder()
                .id(3)
                .titulo("Aprendizaje Autodidacta")
                .fechaObtencion(LocalDate.of(2024, 3, 10))
                .descripcion("Aprendizaje autodidacta")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .usuario(usuario)
                .build();

        List<Educacion> educaciones = Arrays.asList(educacionFormal, educacionCurso, educacionAutodidacta);

        when(educacionRepository.findAll()).thenReturn(educaciones);
        when(educacionMapper.toEducacionDto(any(Educacion.class))).thenAnswer(invocation -> {
            Educacion e = invocation.getArgument(0);
            return EducacionDto.builder()
                    .id(e.getId())
                    .titulo(e.getTitulo())
                    .fechaObtencion(e.getFechaObtencion())
                    .descripcion(e.getDescripcion())
                    .tipoEducacion(e.getTipoEducacion())
                    .build();
        });

        // Act
        List<EducacionDto> resultado = educacionService.getAllEducacion();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(3, resultado.size(), "Debería haber 3 educaciones");

        // Verificar que todos los tipos están presentes
        long countFormal = resultado.stream()
                .filter(e -> e.getTipoEducacion() == TipoEducacion.FORMAL)
                .count();
        long countCurso = resultado.stream()
                .filter(e -> e.getTipoEducacion() == TipoEducacion.INFORMAL_CURSO)
                .count();
        long countAutodidacta = resultado.stream()
                .filter(e -> e.getTipoEducacion() == TipoEducacion.AUTODIDACTA)
                .count();

        assertEquals(1, countFormal, "Debería haber 1 educación formal");
        assertEquals(1, countCurso, "Debería haber 1 educación de curso informal");
        assertEquals(1, countAutodidacta, "Debería haber 1 educación autodidacta");

        verify(educacionRepository, times(1)).findAll();
        verify(educacionMapper, times(3)).toEducacionDto(any(Educacion.class));
    }

    @Test
    @DisplayName("getAllEducacion - Debería propagar RuntimeException del repositorio")
    void getAllEducacion_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        when(educacionRepository.findAll()).thenThrow(new RuntimeException("Error al obtener educaciones"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            educacionService.getAllEducacion();
        }, "Debería propagar RuntimeException");

        assertEquals("Error al obtener educaciones", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(educacionRepository, times(1)).findAll();
        verify(educacionMapper, never()).toEducacionDto(any());
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("actualizarEducacionPorId - Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void actualizarEducacionPorId_ShouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        when(educacionRepository.findById(idMax)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            educacionService.actualizarEducacionPorId(idMax, educacionDtoValido);
        }, "Debería lanzar RuntimeException con ID máximo");

        verify(educacionRepository, times(1)).findById(idMax);
        verify(educacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería manejar ID en el límite de Integer (Integer.MIN_VALUE)")
    void actualizarEducacionPorId_ShouldHandleMinIntegerId() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        when(educacionRepository.findById(idMin)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            educacionService.actualizarEducacionPorId(idMin, educacionDtoValido);
        }, "Debería lanzar RuntimeException con ID mínimo");

        verify(educacionRepository, times(1)).findById(idMin);
        verify(educacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("saveEducacion - Debería manejar fechas extremas")
    void saveEducacion_ShouldHandleExtremeDates() {
        // Arrange
        LocalDate fechaAntigua = LocalDate.of(1900, 1, 1);
        EducacionDto educacionDtoAntiguo = EducacionDto.builder()
                .titulo("Educación Antigua")
                .fechaObtencion(fechaAntigua)
                .descripcion("Educación del año 1900")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        Educacion educacionAntigua = Educacion.builder()
                .titulo("Educación Antigua")
                .fechaObtencion(fechaAntigua)
                .descripcion("Educación del año 1900")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(usuario)
                .build();

        when(educacionMapper.toEducacion(educacionDtoAntiguo)).thenReturn(educacionAntigua);
        when(educacionRepository.save(educacionAntigua)).thenReturn(educacionAntigua);
        when(educacionMapper.toEducacionDto(educacionAntigua)).thenReturn(educacionDtoAntiguo);

        // Act
        EducacionDto resultado = educacionService.saveEducacion(educacionDtoAntiguo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(fechaAntigua, resultado.getFechaObtencion(), "La fecha antigua debería coincidir");

        verify(educacionMapper, times(1)).toEducacion(educacionDtoAntiguo);
        verify(educacionRepository, times(1)).save(educacionAntigua);
        verify(educacionMapper, times(1)).toEducacionDto(educacionAntigua);
    }

    @Test
    @DisplayName("saveEducacion - Debería manejar títulos y descripciones largos")
    void saveEducacion_ShouldHandleLongTitlesAndDescriptions() {
        // Arrange
        String tituloLargo = "T".repeat(140);
        String descripcionLarga = "D".repeat(295);

        EducacionDto educacionDtoLargo = EducacionDto.builder()
                .titulo(tituloLargo)
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionLarga)
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .build();

        Educacion educacionLarga = Educacion.builder()
                .titulo(tituloLargo)
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionLarga)
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .usuario(usuario)
                .build();

        when(educacionMapper.toEducacion(educacionDtoLargo)).thenReturn(educacionLarga);
        when(educacionRepository.save(educacionLarga)).thenReturn(educacionLarga);
        when(educacionMapper.toEducacionDto(educacionLarga)).thenReturn(educacionDtoLargo);

        // Act
        EducacionDto resultado = educacionService.saveEducacion(educacionDtoLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(tituloLargo, resultado.getTitulo(), "El título largo debería mantenerse");
        assertEquals(descripcionLarga, resultado.getDescripcion(), "La descripción larga debería mantenerse");

        verify(educacionMapper, times(1)).toEducacion(educacionDtoLargo);
        verify(educacionRepository, times(1)).save(educacionLarga);
        verify(educacionMapper, times(1)).toEducacionDto(educacionLarga);
    }

    @Test
    @DisplayName("saveEducacion - Debería manejar títulos con caracteres especiales")
    void saveEducacion_ShouldHandleTitlesWithSpecialCharacters() {
        // Arrange
        String tituloEspecial = "Educación: Cursos, Talleres & Seminarios (2024)";
        String descripcionEspecial = "Descripción con acentos: áéíóú ñ y caracteres especiales !@#$%^&*()";

        EducacionDto educacionDtoEspecial = EducacionDto.builder()
                .titulo(tituloEspecial)
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionEspecial)
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        Educacion educacionEspecial = Educacion.builder()
                .titulo(tituloEspecial)
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionEspecial)
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .usuario(usuario)
                .build();

        when(educacionMapper.toEducacion(educacionDtoEspecial)).thenReturn(educacionEspecial);
        when(educacionRepository.save(educacionEspecial)).thenReturn(educacionEspecial);
        when(educacionMapper.toEducacionDto(educacionEspecial)).thenReturn(educacionDtoEspecial);

        // Act
        EducacionDto resultado = educacionService.saveEducacion(educacionDtoEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(tituloEspecial, resultado.getTitulo(), "El título con caracteres especiales debería mantenerse");
        assertEquals(descripcionEspecial, resultado.getDescripcion(),
                "La descripción con caracteres especiales debería mantenerse");

        verify(educacionMapper, times(1)).toEducacion(educacionDtoEspecial);
        verify(educacionRepository, times(1)).save(educacionEspecial);
        verify(educacionMapper, times(1)).toEducacionDto(educacionEspecial);
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con los mocks
        verifyNoInteractions(educacionRepository);
        verifyNoInteractions(educacionMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en saveEducacion")
    void verifyExpectedInteractionsInSaveEducacion() {
        // Arrange
        when(educacionMapper.toEducacion(educacionDtoValido)).thenReturn(educacionValida);
        when(educacionRepository.save(educacionValida)).thenReturn(educacionValida);
        when(educacionMapper.toEducacionDto(educacionValida)).thenReturn(educacionDtoValido);

        // Act
        educacionService.saveEducacion(educacionDtoValido);

        // Assert
        verify(educacionMapper, times(1)).toEducacion(educacionDtoValido);
        verify(educacionRepository, times(1)).save(educacionValida);
        verify(educacionMapper, times(1)).toEducacionDto(educacionValida);
        verifyNoMoreInteractions(educacionRepository);
        verifyNoMoreInteractions(educacionMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en actualizarEducacionPorId")
    void verifyExpectedInteractionsInActualizarEducacionPorId() {
        // Arrange
        Integer id = 1;
        when(educacionRepository.findById(id)).thenReturn(Optional.of(educacionValida));
        when(educacionRepository.save(educacionValida)).thenReturn(educacionValida);
        when(educacionMapper.toEducacionDto(educacionValida)).thenReturn(educacionDtoValido);

        // Act
        educacionService.actualizarEducacionPorId(id, educacionDtoValido);

        // Assert
        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, times(1)).save(educacionValida);
        verify(educacionMapper, times(1)).toEducacionDto(educacionValida);
        verifyNoMoreInteractions(educacionRepository);
        verifyNoMoreInteractions(educacionMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en deleteEducacionPorId")
    void verifyExpectedInteractionsInDeleteEducacionPorId() {
        // Arrange
        Integer id = 1;
        doNothing().when(educacionRepository).deleteById(id);

        // Act
        educacionService.deleteEducacionPorId(id);

        // Assert
        verify(educacionRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(educacionRepository);
        verifyNoInteractions(educacionMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en getAllEducacion")
    void verifyExpectedInteractionsInGetAllEducacion() {
        // Arrange
        List<Educacion> educaciones = Collections.singletonList(educacionValida);
        when(educacionRepository.findAll()).thenReturn(educaciones);
        when(educacionMapper.toEducacionDto(educacionValida)).thenReturn(educacionDtoValido);

        // Act
        educacionService.getAllEducacion();

        // Assert
        verify(educacionRepository, times(1)).findAll();
        verify(educacionMapper, times(1)).toEducacionDto(educacionValida);
        verifyNoMoreInteractions(educacionRepository);
        verifyNoMoreInteractions(educacionMapper);
    }

    // ==================== TESTS NEGATIVOS ====================

    @Test
    @DisplayName("saveEducacion - Debería lanzar excepción al guardar educación con campos obligatorios nulos")
    void saveEducacion_ShouldThrowException_WhenRequiredFieldsAreNull() {
        // Arrange
        EducacionDto educacionDtoInvalido = EducacionDto.builder()
                .titulo(null)
                .fechaObtencion(null)
                .descripcion(null)
                .tipoEducacion(null)
                .build();

        // Simular que el mapper lanza excepción al mapear
        when(educacionMapper.toEducacion(educacionDtoInvalido))
                .thenThrow(new RuntimeException("Campos obligatorios nulos"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            educacionService.saveEducacion(educacionDtoInvalido);
        }, "Debería lanzar RuntimeException");

        verify(educacionMapper, times(1)).toEducacion(educacionDtoInvalido);
        verify(educacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteEducacionPorId - Debería manejar ID negativo")
    void deleteEducacionPorId_ShouldHandleNegativeId() {
        // Arrange
        Integer id = -1;
        doNothing().when(educacionRepository).deleteById(id);

        // Act
        educacionService.deleteEducacionPorId(id);

        // Assert
        verify(educacionRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("actualizarEducacionPorId - Debería manejar DTO con imagen pero sin URL")
    void actualizarEducacionPorId_ShouldHandleDtoWithImageWithoutUrl() {
        // Arrange
        Integer id = 1;
        ImagenDto imagenDtoSinUrl = ImagenDto.builder()
                .alt("Imagen sin URL")
                .build();

        EducacionDto educacionDtoConImagenSinUrl = EducacionDto.builder()
                .id(id)
                .titulo("Título")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagenDtoSinUrl)
                .build();

        when(educacionRepository.findById(id)).thenReturn(Optional.of(educacionValida));
        when(educacionRepository.save(any(Educacion.class))).thenReturn(educacionValida);
        when(educacionMapper.toEducacionDto(any(Educacion.class))).thenReturn(educacionDtoConImagenSinUrl);

        // Act
        educacionService.actualizarEducacionPorId(id, educacionDtoConImagenSinUrl);

        // Assert - Verificar que la imagen se creó con URL nula
        assertNotNull(educacionValida.getImagen(), "La imagen no debería ser nula");
        assertNull(educacionValida.getImagen().getUrl(), "La URL de la imagen debería ser nula");

        verify(educacionRepository, times(1)).findById(id);
        verify(educacionRepository, times(1)).save(educacionValida);
    }
}