package com.example.demo.service;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import com.example.demo.mapper.ExperienciaMapper;
import com.example.demo.model.Experiencia;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ExperienciaRepository;
import com.example.demo.service.Impl.ExperienciaServiceImpl;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase ExperienciaServiceImpl
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class ExperienciaServiceImplTest {

    @Mock
    private ExperienciaRepository experienciaRepository;

    @Mock
    private ExperienciaMapper experienciaMapper;

    @InjectMocks
    private ExperienciaServiceImpl experienciaService;

    private Experiencia experienciaValida;
    private ExperienciaDto experienciaDtoValido;
    private Experiencia experienciaEnCurso;
    private ExperienciaDto experienciaDtoEnCurso;
    private Imagen imagen;
    private ImagenDto imagenDto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear imagen
        imagen = Imagen.builder()
                .id(1)
                .url("proyecto.jpg")
                .alt("Captura del proyecto")
                .build();

        imagenDto = ImagenDto.builder()
                .id(1)
                .url("proyecto.jpg")
                .alt("Captura del proyecto")
                .build();

        // Crear usuario
        usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack con 5 años de experiencia")
                .descripcion("Apasionado por la tecnología y el desarrollo de software.")
                .build();

        // Crear experiencia válida
        experienciaValida = Experiencia.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT para gestión de usuarios")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Crear ExperienciaDto válido
        experienciaDtoValido = ExperienciaDto.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT para gestión de usuarios")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagenDto)
                .build();

        // Crear experiencia en curso (con fechaFinProyecto nula)
        experienciaEnCurso = Experiencia.builder()
                .id(2)
                .titulo("Proyecto en Desarrollo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción del proyecto en desarrollo con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Crear ExperienciaDto en curso
        experienciaDtoEnCurso = ExperienciaDto.builder()
                .id(2)
                .titulo("Proyecto en Desarrollo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción del proyecto en desarrollo con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .build();
    }

    // ==================== TESTS SAVE EXPERIENCIA ====================

    @Test
    @DisplayName("saveExperiencia - Debería guardar una experiencia correctamente")
    void saveExperiencia_ShouldSaveExperienciaCorrectly() {
        // Arrange
        Experiencia experienciaParaGuardar = Experiencia.builder()
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT para gestión de usuarios")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT para gestión de usuarios")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        when(experienciaMapper.toExperiencia(experienciaDtoValido)).thenReturn(experienciaParaGuardar);
        when(experienciaRepository.save(experienciaParaGuardar)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(experienciaDtoValido);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1, resultado.getId(), "El ID generado debería ser 1");
        assertEquals("Sistema de Gestión de Usuarios", resultado.getTitulo(), "El título debería coincidir");
        assertEquals("https://github.com/usuario/proyecto", resultado.getLink(), "El link debería coincidir");
        assertEquals(TipoExperiencia.PROYECTO_PERSONAL, resultado.getTipoExperiencia(),
                "El tipo de experiencia debería ser PROYECTO_PERSONAL");
        assertEquals(TecnologiaUsada.SPRINGBOOT, resultado.getTecnologiaUsada(),
                "La tecnología usada debería ser SPRINGBOOT");

        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoValido);
        verify(experienciaRepository, times(1)).save(experienciaParaGuardar);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaGuardada);
    }

    @Test
    @DisplayName("saveExperiencia - Debería guardar una experiencia sin imagen correctamente")
    void saveExperiencia_ShouldSaveExperienciaWithoutImageCorrectly() {
        // Arrange
        ExperienciaDto experienciaDtoSinImagen = ExperienciaDto.builder()
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imagen con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(null)
                .build();

        Experiencia experienciaSinImagen = Experiencia.builder()
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imagen con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(null)
                .usuario(null)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(3)
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imagen con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(null)
                .usuario(null)
                .build();

        when(experienciaMapper.toExperiencia(experienciaDtoSinImagen)).thenReturn(experienciaSinImagen);
        when(experienciaRepository.save(experienciaSinImagen)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(experienciaDtoSinImagen);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDtoSinImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");
        assertEquals("Proyecto sin Imagen", resultado.getTitulo(), "El título debería coincidir");

        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoSinImagen);
        verify(experienciaRepository, times(1)).save(experienciaSinImagen);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaGuardada);
    }

    @Test
    @DisplayName("saveExperiencia - Debería guardar una experiencia en curso (con fechaFinProyecto nula)")
    void saveExperiencia_ShouldSaveExperienciaEnCurso() {
        // Arrange
        Experiencia experienciaParaGuardar = Experiencia.builder()
                .titulo("Proyecto en Desarrollo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción del proyecto en desarrollo con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .usuario(null)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(2)
                .titulo("Proyecto en Desarrollo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción del proyecto en desarrollo con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .usuario(null)
                .build();

        when(experienciaMapper.toExperiencia(experienciaDtoEnCurso)).thenReturn(experienciaParaGuardar);
        when(experienciaRepository.save(experienciaParaGuardar)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(experienciaDtoEnCurso);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDtoEnCurso);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getFechaFinProyecto(), "La fecha de fin debería ser nula para proyecto en curso");
        assertEquals("Proyecto en Desarrollo", resultado.getTitulo(), "El título debería coincidir");

        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoEnCurso);
        verify(experienciaRepository, times(1)).save(experienciaParaGuardar);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaGuardada);
    }

    @Test
    @DisplayName("saveExperiencia - Debería lanzar excepción cuando el DTO es nulo")
    void saveExperiencia_ShouldThrowException_WhenDtoIsNull() {
        // Arrange
        when(experienciaMapper.toExperiencia(null)).thenThrow(new NullPointerException("ExperienciaDto no puede ser nulo"));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            experienciaService.saveExperiencia(null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(experienciaMapper, times(1)).toExperiencia(null);
        verify(experienciaRepository, never()).save(any());
    }

    // ==================== TESTS ACTUALIZAR EXPERIENCIA ====================

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería actualizar una experiencia existente correctamente")
    void actualizarExperienciaPorId_ShouldUpdateExperienciaCorrectly() {
        // Arrange
        Integer id = 1;
        ExperienciaDto experienciaDtoActualizado = ExperienciaDto.builder()
                .id(id)
                .titulo("Sistema de Gestión de Usuarios Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada con más de 5 caracteres para prueba")
                .link("https://github.com/usuario/proyecto-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiaUsada(TecnologiaUsada.ANGULAR)
                .imagen(imagenDto)
                .build();

        Experiencia experienciaActualizada = Experiencia.builder()
                .id(id)
                .titulo("Sistema de Gestión de Usuarios Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada con más de 5 caracteres para prueba")
                .link("https://github.com/usuario/proyecto-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiaUsada(TecnologiaUsada.ANGULAR)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        when(experienciaRepository.findById(id)).thenReturn(Optional.of(experienciaValida));
        when(experienciaRepository.save(any(Experiencia.class))).thenReturn(experienciaActualizada);
        when(experienciaMapper.toExperienciaDto(experienciaActualizada)).thenReturn(experienciaDtoActualizado);

        // Act
        ExperienciaDto resultado = experienciaService.actualizarExperienciaPorId(id, experienciaDtoActualizado);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(id, resultado.getId(), "El ID debería coincidir");
        assertEquals("Sistema de Gestión de Usuarios Actualizado", resultado.getTitulo(),
                "El título debería estar actualizado");
        assertEquals("Descripción actualizada con más de 5 caracteres para prueba", resultado.getDescripcion(),
                "La descripción debería estar actualizada");
        assertEquals("https://github.com/usuario/proyecto-actualizado", resultado.getLink(),
                "El link debería estar actualizado");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE, resultado.getTipoExperiencia(),
                "El tipo de experiencia debería estar actualizado");
        assertEquals(TecnologiaUsada.ANGULAR, resultado.getTecnologiaUsada(),
                "La tecnología usada debería estar actualizada");

        // Verificar que se actualizaron los campos en la entidad existente
        assertEquals("Sistema de Gestión de Usuarios Actualizado", experienciaValida.getTitulo(),
                "El título en la entidad debería estar actualizado");
        assertEquals(LocalDate.of(2024, 12, 31), experienciaValida.getFechaFinProyecto(),
                "La fecha de fin en la entidad debería estar actualizada");

        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, times(1)).save(experienciaValida);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaActualizada);
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería actualizar una experiencia y establecer fechaFinProyecto a null")
    void actualizarExperienciaPorId_ShouldSetFechaFinProyectoToNull() {
        // Arrange
        Integer id = 1;
        ExperienciaDto experienciaDtoActualizado = ExperienciaDto.builder()
                .id(id)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(null) // Ahora es proyecto en curso
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagenDto)
                .build();

        when(experienciaRepository.findById(id)).thenReturn(Optional.of(experienciaValida));
        when(experienciaRepository.save(any(Experiencia.class))).thenReturn(experienciaValida);
        when(experienciaMapper.toExperienciaDto(any(Experiencia.class))).thenReturn(experienciaDtoActualizado);

        // Act
        experienciaService.actualizarExperienciaPorId(id, experienciaDtoActualizado);

        // Assert - Verificar que se estableció fechaFinProyecto a null
        assertNull(experienciaValida.getFechaFinProyecto(),
                "La fecha de fin debería ser null después de la actualización");

        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, times(1)).save(experienciaValida);
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería actualizar solo los campos permitidos")
    void actualizarExperienciaPorId_ShouldUpdateOnlyAllowedFields() {
        // Arrange
        Integer id = 1;
        ExperienciaDto experienciaDtoActualizado = ExperienciaDto.builder()
                .id(id)
                .titulo("Nuevo Título")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Nueva descripción con más de 5 caracteres")
                .link("https://github.com/usuario/nuevo-link")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiaUsada(TecnologiaUsada.REACT)
                .imagen(imagenDto)
                .build();

        when(experienciaRepository.findById(id)).thenReturn(Optional.of(experienciaValida));
        when(experienciaRepository.save(any(Experiencia.class))).thenReturn(experienciaValida);
        when(experienciaMapper.toExperienciaDto(any(Experiencia.class))).thenReturn(experienciaDtoActualizado);

        // Act
        experienciaService.actualizarExperienciaPorId(id, experienciaDtoActualizado);

        // Assert - Verificar que los setters fueron llamados con los valores correctos
        assertEquals("Nuevo Título", experienciaValida.getTitulo(), "El título debería estar actualizado");
        assertEquals(LocalDate.of(2024, 12, 31), experienciaValida.getFechaFinProyecto(),
                "La fecha de fin debería estar actualizada");
        assertEquals("Nueva descripción con más de 5 caracteres", experienciaValida.getDescripcion(),
                "La descripción debería estar actualizada");
        assertEquals("https://github.com/usuario/nuevo-link", experienciaValida.getLink(),
                "El link debería estar actualizado");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO, experienciaValida.getTipoExperiencia(),
                "El tipo de experiencia debería estar actualizado");
        assertEquals(TecnologiaUsada.REACT, experienciaValida.getTecnologiaUsada(),
                "La tecnología usada debería estar actualizada");

        // Verificar que el ID no se modifica
        assertEquals(1, experienciaValida.getId(), "El ID no debería cambiar");

        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, times(1)).save(experienciaValida);
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería retornar null cuando la experiencia no existe")
    void actualizarExperienciaPorId_ShouldReturnNull_WhenExperienciaDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(experienciaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ExperienciaDto resultado = experienciaService.actualizarExperienciaPorId(id, experienciaDtoValido);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando la experiencia no existe");

        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, never()).save(any());
        verify(experienciaMapper, never()).toExperienciaDto(any());
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería manejar ID nulo - Test negativo")
    void actualizarExperienciaPorId_ShouldHandleNullId() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            experienciaService.actualizarExperienciaPorId(null, experienciaDtoValido);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(experienciaRepository, never()).findById(any());
        verify(experienciaRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería manejar DTO nulo - Test negativo")
    void actualizarExperienciaPorId_ShouldHandleNullDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            experienciaService.actualizarExperienciaPorId(id, null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(experienciaRepository, never()).findById(any());
        verify(experienciaRepository, never()).save(any());
    }

    // ==================== TESTS DELETE EXPERIENCIA ====================

    @Test
    @DisplayName("deleteExperienciaPorId - Debería eliminar una experiencia correctamente")
    void deleteExperienciaPorId_ShouldDeleteExperienciaCorrectly() {
        // Arrange
        Integer id = 1;

        // Act
        experienciaService.deleteExperienciaPorId(id);

        // Assert
        verify(experienciaRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteExperienciaPorId - No debería lanzar excepción al eliminar ID no existente")
    void deleteExperienciaPorId_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        doNothing().when(experienciaRepository).deleteById(id);

        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            experienciaService.deleteExperienciaPorId(id);
        }, "No debería lanzar excepción al eliminar un ID que no existe");

        verify(experienciaRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteExperienciaPorId - Debería manejar ID nulo - Test negativo")
    void deleteExperienciaPorId_ShouldHandleNullId() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            experienciaService.deleteExperienciaPorId(null);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(experienciaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteExperienciaPorId - Debería propagar RuntimeException del repositorio")
    void deleteExperienciaPorId_ShouldPropagateRuntimeException() {
        // Arrange
        Integer id = 1;
        doThrow(new RuntimeException("Error al eliminar")).when(experienciaRepository).deleteById(id);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            experienciaService.deleteExperienciaPorId(id);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al eliminar", exception.getMessage(), "El mensaje de excepción debería ser el mismo");

        verify(experienciaRepository, times(1)).deleteById(id);
    }

    // ==================== TESTS GET ALL EXPERIENCIAS ====================

    @Test
    @DisplayName("getAllExperiencias - Debería retornar todas las experiencias")
    void getAllExperiencias_ShouldReturnAllExperiencias() {
        // Arrange
        List<Experiencia> experiencias = Arrays.asList(experienciaValida, experienciaEnCurso);
        List<ExperienciaDto> experienciasDto = Arrays.asList(experienciaDtoValido, experienciaDtoEnCurso);

        when(experienciaRepository.findAll()).thenReturn(experiencias);
        when(experienciaMapper.toExperienciaDto(experienciaValida)).thenReturn(experienciaDtoValido);
        when(experienciaMapper.toExperienciaDto(experienciaEnCurso)).thenReturn(experienciaDtoEnCurso);

        // Act
        List<ExperienciaDto> resultado = experienciaService.getAllExperiencias();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.size(), "Debería haber 2 experiencias");

        // Verificar que los elementos se mapearon correctamente
        assertEquals(experienciaDtoValido.getId(), resultado.get(0).getId(), "El ID del primer elemento debería coincidir");
        assertEquals(experienciaDtoEnCurso.getId(), resultado.get(1).getId(), "El ID del segundo elemento debería coincidir");
        assertEquals(experienciaDtoValido.getTitulo(), resultado.get(0).getTitulo(), "El título del primer elemento debería coincidir");
        assertEquals(experienciaDtoEnCurso.getTitulo(), resultado.get(1).getTitulo(), "El título del segundo elemento debería coincidir");

        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaValida);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaEnCurso);
    }

    @Test
    @DisplayName("getAllExperiencias - Debería retornar lista vacía cuando no hay experiencias")
    void getAllExperiencias_ShouldReturnEmptyList_WhenNoExperienciasExist() {
        // Arrange
        when(experienciaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ExperienciaDto> resultado = experienciaService.getAllExperiencias();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía cuando no hay experiencias");

        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, never()).toExperienciaDto(any());
    }

    @Test
    @DisplayName("getAllExperiencias - Debería manejar experiencias con imagen nula correctamente")
    void getAllExperiencias_ShouldHandleExperienciasWithNullImage() {
        // Arrange
        Experiencia experienciaSinImagen = Experiencia.builder()
                .id(3)
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imagen con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(null)
                .usuario(null)
                .build();

        ExperienciaDto experienciaDtoSinImagen = ExperienciaDto.builder()
                .id(3)
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imagen con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(null)
                .build();

        List<Experiencia> experiencias = Arrays.asList(experienciaValida, experienciaSinImagen);
        List<ExperienciaDto> experienciasDto = Arrays.asList(experienciaDtoValido, experienciaDtoSinImagen);

        when(experienciaRepository.findAll()).thenReturn(experiencias);
        when(experienciaMapper.toExperienciaDto(experienciaValida)).thenReturn(experienciaDtoValido);
        when(experienciaMapper.toExperienciaDto(experienciaSinImagen)).thenReturn(experienciaDtoSinImagen);

        // Act
        List<ExperienciaDto> resultado = experienciaService.getAllExperiencias();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.size(), "Debería haber 2 experiencias");
        assertNull(resultado.get(1).getImagen(), "La imagen del segundo elemento debería ser nula");

        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaValida);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaSinImagen);
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("saveExperiencia - Debería manejar títulos largos")
    void saveExperiencia_ShouldHandleLongTitulo() {
        // Arrange
        String tituloLargo = "A".repeat(145);
        ExperienciaDto experienciaDtoTituloLargo = ExperienciaDto.builder()
                .titulo(tituloLargo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-largo")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagenDto)
                .build();

        Experiencia experienciaTituloLargo = Experiencia.builder()
                .titulo(tituloLargo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-largo")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(null)
                .build();

        when(experienciaMapper.toExperiencia(experienciaDtoTituloLargo)).thenReturn(experienciaTituloLargo);
        when(experienciaRepository.save(experienciaTituloLargo)).thenReturn(experienciaTituloLargo);
        when(experienciaMapper.toExperienciaDto(experienciaTituloLargo)).thenReturn(experienciaDtoTituloLargo);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDtoTituloLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(tituloLargo, resultado.getTitulo(), "El título largo debería mantenerse");
        assertEquals(145, resultado.getTitulo().length(), "El título debería tener 145 caracteres");

        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoTituloLargo);
        verify(experienciaRepository, times(1)).save(experienciaTituloLargo);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaTituloLargo);
    }

    @Test
    @DisplayName("saveExperiencia - Debería manejar descripciones largas")
    void saveExperiencia_ShouldHandleLongDescripcion() {
        // Arrange
        String descripcionLarga = "A".repeat(300);
        ExperienciaDto experienciaDtoDescripcionLarga = ExperienciaDto.builder()
                .titulo("Proyecto con descripción larga")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(descripcionLarga)
                .link("https://github.com/usuario/proyecto-descripcion-larga")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagenDto)
                .build();

        Experiencia experienciaDescripcionLarga = Experiencia.builder()
                .titulo("Proyecto con descripción larga")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(descripcionLarga)
                .link("https://github.com/usuario/proyecto-descripcion-larga")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(null)
                .build();

        when(experienciaMapper.toExperiencia(experienciaDtoDescripcionLarga)).thenReturn(experienciaDescripcionLarga);
        when(experienciaRepository.save(experienciaDescripcionLarga)).thenReturn(experienciaDescripcionLarga);
        when(experienciaMapper.toExperienciaDto(experienciaDescripcionLarga)).thenReturn(experienciaDtoDescripcionLarga);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDtoDescripcionLarga);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(descripcionLarga, resultado.getDescripcion(), "La descripción larga debería mantenerse");
        assertEquals(300, resultado.getDescripcion().length(), "La descripción debería tener 300 caracteres");

        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoDescripcionLarga);
        verify(experienciaRepository, times(1)).save(experienciaDescripcionLarga);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaDescripcionLarga);
    }

    @Test
    @DisplayName("saveExperiencia - Debería manejar links largos")
    void saveExperiencia_ShouldHandleLongLink() {
        // Arrange
        String linkLargo = "https://github.com/" + "a".repeat(200) + "/proyecto";
        ExperienciaDto experienciaDtoLinkLargo = ExperienciaDto.builder()
                .titulo("Proyecto con link largo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link(linkLargo)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagenDto)
                .build();

        Experiencia experienciaLinkLargo = Experiencia.builder()
                .titulo("Proyecto con link largo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link(linkLargo)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(null)
                .build();

        when(experienciaMapper.toExperiencia(experienciaDtoLinkLargo)).thenReturn(experienciaLinkLargo);
        when(experienciaRepository.save(experienciaLinkLargo)).thenReturn(experienciaLinkLargo);
        when(experienciaMapper.toExperienciaDto(experienciaLinkLargo)).thenReturn(experienciaDtoLinkLargo);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDtoLinkLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(linkLargo, resultado.getLink(), "El link largo debería mantenerse");

        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoLinkLargo);
        verify(experienciaRepository, times(1)).save(experienciaLinkLargo);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaLinkLargo);
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con los mocks
        verifyNoInteractions(experienciaRepository);
        verifyNoInteractions(experienciaMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en saveExperiencia")
    void verifyExpectedInteractionsInSaveExperiencia() {
        // Arrange
        when(experienciaMapper.toExperiencia(experienciaDtoValido)).thenReturn(experienciaValida);
        when(experienciaRepository.save(experienciaValida)).thenReturn(experienciaValida);
        when(experienciaMapper.toExperienciaDto(experienciaValida)).thenReturn(experienciaDtoValido);

        // Act
        experienciaService.saveExperiencia(experienciaDtoValido);

        // Assert
        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoValido);
        verify(experienciaRepository, times(1)).save(experienciaValida);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaValida);
        verifyNoMoreInteractions(experienciaRepository);
        verifyNoMoreInteractions(experienciaMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en getAllExperiencias")
    void verifyExpectedInteractionsInGetAllExperiencias() {
        // Arrange
        List<Experiencia> experiencias = Arrays.asList(experienciaValida);
        when(experienciaRepository.findAll()).thenReturn(experiencias);
        when(experienciaMapper.toExperienciaDto(experienciaValida)).thenReturn(experienciaDtoValido);

        // Act
        experienciaService.getAllExperiencias();

        // Assert
        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaValida);
        verifyNoMoreInteractions(experienciaRepository);
        verifyNoMoreInteractions(experienciaMapper);
    }

    // ==================== TESTS DE EXCEPCIONES ====================

    @Test
    @DisplayName("saveExperiencia - Debería propagar RuntimeException del repositorio")
    void saveExperiencia_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        when(experienciaMapper.toExperiencia(experienciaDtoValido)).thenReturn(experienciaValida);
        when(experienciaRepository.save(experienciaValida)).thenThrow(new RuntimeException("Error al guardar"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            experienciaService.saveExperiencia(experienciaDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al guardar", exception.getMessage(), "El mensaje de excepción debería ser el mismo");

        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoValido);
        verify(experienciaRepository, times(1)).save(experienciaValida);
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería propagar RuntimeException del repositorio al guardar")
    void actualizarExperienciaPorId_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        Integer id = 1;
        when(experienciaRepository.findById(id)).thenReturn(Optional.of(experienciaValida));
        when(experienciaRepository.save(experienciaValida)).thenThrow(new RuntimeException("Error al actualizar"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            experienciaService.actualizarExperienciaPorId(id, experienciaDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al actualizar", exception.getMessage(), "El mensaje de excepción debería ser el mismo");

        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, times(1)).save(experienciaValida);
    }

    @Test
    @DisplayName("getAllExperiencias - Debería propagar RuntimeException del repositorio")
    void getAllExperiencias_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        when(experienciaRepository.findAll()).thenThrow(new RuntimeException("Error al obtener experiencias"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            experienciaService.getAllExperiencias();
        }, "Debería propagar RuntimeException");

        assertEquals("Error al obtener experiencias", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, never()).toExperienciaDto(any());
    }

    // ==================== TESTS CON DIFERENTES ENUMS ====================

    @Test
    @DisplayName("saveExperiencia - Debería manejar todos los tipos de experiencia")
    void saveExperiencia_ShouldHandleAllTiposExperiencia() {
        // Arrange - Probar todos los tipos de experiencia
        TipoExperiencia[] tipos = TipoExperiencia.values();

        for (TipoExperiencia tipo : tipos) {
            ExperienciaDto dto = ExperienciaDto.builder()
                    .titulo("Proyecto " + tipo.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(tipo)
                    .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                    .imagen(imagenDto)
                    .build();

            Experiencia experiencia = Experiencia.builder()
                    .titulo("Proyecto " + tipo.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(tipo)
                    .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                    .imagen(imagen)
                    .usuario(null)
                    .build();

            when(experienciaMapper.toExperiencia(dto)).thenReturn(experiencia);
            when(experienciaRepository.save(experiencia)).thenReturn(experiencia);
            when(experienciaMapper.toExperienciaDto(experiencia)).thenReturn(dto);

            // Act
            ExperienciaDto resultado = experienciaService.saveExperiencia(dto);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, resultado.getTipoExperiencia(), "El tipo de experiencia debería coincidir para: " + tipo);
        }
    }

    @Test
    @DisplayName("saveExperiencia - Debería manejar todas las tecnologías")
    void saveExperiencia_ShouldHandleAllTecnologiasUsada() {
        // Arrange - Probar todas las tecnologías
        TecnologiaUsada[] tecnologias = TecnologiaUsada.values();

        for (TecnologiaUsada tecnologia : tecnologias) {
            ExperienciaDto dto = ExperienciaDto.builder()
                    .titulo("Proyecto con " + tecnologia.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiaUsada(tecnologia)
                    .imagen(imagenDto)
                    .build();

            Experiencia experiencia = Experiencia.builder()
                    .titulo("Proyecto con " + tecnologia.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiaUsada(tecnologia)
                    .imagen(imagen)
                    .usuario(null)
                    .build();

            when(experienciaMapper.toExperiencia(dto)).thenReturn(experiencia);
            when(experienciaRepository.save(experiencia)).thenReturn(experiencia);
            when(experienciaMapper.toExperienciaDto(experiencia)).thenReturn(dto);

            // Act
            ExperienciaDto resultado = experienciaService.saveExperiencia(dto);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tecnología: " + tecnologia);
            assertEquals(tecnologia, resultado.getTecnologiaUsada(),
                    "La tecnología usada debería coincidir para: " + tecnologia);
        }
    }
}