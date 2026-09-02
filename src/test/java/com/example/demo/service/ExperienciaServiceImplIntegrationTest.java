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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas de integración para la clase ExperienciaServiceImpl
 * Verifica la interacción entre el servicio, el repositorio y el mapper
 */
@ExtendWith(MockitoExtension.class)
class ExperienciaServiceImplIntegrationTest {

    @Mock
    private ExperienciaRepository experienciaRepository;

    @Mock
    private ExperienciaMapper experienciaMapper;

    @InjectMocks
    private ExperienciaServiceImpl experienciaService;

    private Experiencia experienciaValida;
    private ExperienciaDto experienciaDtoValido;
    private Usuario usuario;
    private Imagen imagen;
    private ImagenDto imagenDto;

    @BeforeEach
    void setUp() {
        // Configuración inicial
        usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("password123")
                .build();

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

        // 🔥 Experiencia con lista de tecnologías
        experienciaValida = Experiencia.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA)) // 🔥 Cambiado a lista
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // 🔥 ExperienciaDto con lista de tecnologías
        experienciaDtoValido = ExperienciaDto.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA)) // 🔥 Cambiado a lista
                .imagen(imagenDto)
                .build();
    }

    // ==================== TESTS DE GUARDADO ====================

    @Test
    @DisplayName("saveExperiencia - Debería guardar una experiencia correctamente con integración de mapper y repository")
    void saveExperiencia_ShouldSaveExperienciaCorrectly() {
        // Arrange
        Experiencia experienciaParaGuardar = Experiencia.builder()
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA)) // 🔥 Cambiado a lista
                .imagen(imagen)
                .usuario(usuario)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA)) // 🔥 Cambiado a lista
                .imagen(imagen)
                .usuario(usuario)
                .build();

        when(experienciaMapper.toExperiencia(experienciaDtoValido)).thenReturn(experienciaParaGuardar);
        when(experienciaRepository.save(experienciaParaGuardar)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(experienciaDtoValido);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDtoValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Sistema de Gestión de Usuarios", resultado.getTitulo());

        // 🔥 Verificar tecnologías
        assertNotNull(resultado.getTecnologiasUsadas());
        assertEquals(2, resultado.getTecnologiasUsadas().size());
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT));
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));

        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoValido);
        verify(experienciaRepository, times(1)).save(experienciaParaGuardar);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaGuardada);
    }

    @Test
    @DisplayName("saveExperiencia - Debería guardar una experiencia con múltiples tecnologías")
    void saveExperiencia_ShouldSaveExperienciaWithMultipleTecnologias() {
        // Arrange
        List<TecnologiaUsada> tecnologias = Arrays.asList(
                TecnologiaUsada.JAVA,
                TecnologiaUsada.SPRINGBOOT,
                TecnologiaUsada.REACT,
                TecnologiaUsada.TYPESCRIPT
        );

        ExperienciaDto dto = ExperienciaDto.builder()
                .titulo("Proyecto Full Stack")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto full stack")
                .link("https://github.com/usuario/fullstack")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologias)
                .imagen(imagenDto)
                .build();

        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Full Stack")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto full stack")
                .link("https://github.com/usuario/fullstack")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologias)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(10)
                .titulo("Proyecto Full Stack")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto full stack")
                .link("https://github.com/usuario/fullstack")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologias)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        when(experienciaMapper.toExperiencia(dto)).thenReturn(experiencia);
        when(experienciaRepository.save(experiencia)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(dto);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(dto);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getTecnologiasUsadas());
        assertEquals(4, resultado.getTecnologiasUsadas().size());
        assertTrue(resultado.getTecnologiasUsadas().containsAll(tecnologias));

        verify(experienciaMapper, times(1)).toExperiencia(dto);
        verify(experienciaRepository, times(1)).save(experiencia);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaGuardada);
    }

    // ==================== TESTS DE ACTUALIZACIÓN ====================

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería actualizar una experiencia existente")
    void actualizarExperienciaPorId_ShouldUpdateExperiencia() {
        // Arrange
        Integer id = 1;
        ExperienciaDto dtoActualizado = ExperienciaDto.builder()
                .id(id)
                .titulo("Proyecto Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada del proyecto")
                .link("https://github.com/usuario/proyecto-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiasUsadas(List.of(TecnologiaUsada.ANGULAR, TecnologiaUsada.TYPESCRIPT)) // 🔥 Cambiado a lista
                .imagen(imagenDto)
                .build();

        Experiencia experienciaActualizada = Experiencia.builder()
                .id(id)
                .titulo("Proyecto Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada del proyecto")
                .link("https://github.com/usuario/proyecto-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiasUsadas(List.of(TecnologiaUsada.ANGULAR, TecnologiaUsada.TYPESCRIPT)) // 🔥 Cambiado a lista
                .imagen(imagen)
                .usuario(usuario)
                .build();

        when(experienciaRepository.findById(id)).thenReturn(Optional.of(experienciaValida));
        when(experienciaRepository.save(any(Experiencia.class))).thenReturn(experienciaActualizada);
        when(experienciaMapper.toExperienciaDto(experienciaActualizada)).thenReturn(dtoActualizado);

        // Act
        ExperienciaDto resultado = experienciaService.actualizarExperienciaPorId(id, dtoActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Proyecto Actualizado", resultado.getTitulo());
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE, resultado.getTipoExperiencia());

        // 🔥 Verificar tecnologías actualizadas
        assertNotNull(resultado.getTecnologiasUsadas());
        assertEquals(2, resultado.getTecnologiasUsadas().size());
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.ANGULAR));
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.TYPESCRIPT));

        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, times(1)).save(experienciaValida);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaActualizada);
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería actualizar la lista de tecnologías")
    void actualizarExperienciaPorId_ShouldUpdateTecnologiasUsadas() {
        // Arrange
        Integer id = 1;
        List<TecnologiaUsada> nuevasTecnologias = List.of(
                TecnologiaUsada.PYTHON,
                TecnologiaUsada.DJANGO,
                TecnologiaUsada.POSTGRESQL
        );

        ExperienciaDto dtoActualizado = ExperienciaDto.builder()
                .id(id)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción del proyecto")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(nuevasTecnologias)
                .imagen(imagenDto)
                .build();

        when(experienciaRepository.findById(id)).thenReturn(Optional.of(experienciaValida));
        when(experienciaRepository.save(any(Experiencia.class))).thenReturn(experienciaValida);
        when(experienciaMapper.toExperienciaDto(any(Experiencia.class))).thenReturn(dtoActualizado);

        // Act
        experienciaService.actualizarExperienciaPorId(id, dtoActualizado);

        // Assert - Verificar que las tecnologías se actualizaron
        assertNotNull(experienciaValida.getTecnologiasUsadas());
        assertEquals(3, experienciaValida.getTecnologiasUsadas().size());
        assertTrue(experienciaValida.getTecnologiasUsadas().containsAll(nuevasTecnologias));
        assertFalse(experienciaValida.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT));
        assertFalse(experienciaValida.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));

        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, times(1)).save(experienciaValida);
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería retornar null cuando la experiencia no existe")
    void actualizarExperienciaPorId_ShouldReturnNull_WhenExperienciaNotFound() {
        // Arrange
        Integer id = 999;
        when(experienciaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ExperienciaDto resultado = experienciaService.actualizarExperienciaPorId(id, experienciaDtoValido);

        // Assert
        assertNull(resultado);
        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, never()).save(any());
    }

    // ==================== TESTS DE ELIMINACIÓN ====================

    @Test
    @DisplayName("deleteExperienciaPorId - Debería eliminar una experiencia por ID")
    void deleteExperienciaPorId_ShouldDeleteExperiencia() {
        // Arrange
        Integer id = 1;
        doNothing().when(experienciaRepository).deleteById(id);

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

        // Act & Assert
        assertDoesNotThrow(() -> experienciaService.deleteExperienciaPorId(id));
        verify(experienciaRepository, times(1)).deleteById(id);
    }

    // ==================== TESTS DE OBTENCIÓN ====================

    @Test
    @DisplayName("getAllExperiencias - Debería retornar todas las experiencias")
    void getAllExperiencias_ShouldReturnAllExperiencias() {
        // Arrange
        List<Experiencia> experiencias = Arrays.asList(experienciaValida);
        List<ExperienciaDto> experienciasDto = Arrays.asList(experienciaDtoValido);

        when(experienciaRepository.findAll()).thenReturn(experiencias);
        when(experienciaMapper.toExperienciaDto(experienciaValida)).thenReturn(experienciaDtoValido);

        // Act
        List<ExperienciaDto> resultado = experienciaService.getAllExperiencias();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(experienciaDtoValido.getId(), resultado.get(0).getId());

        // 🔥 Verificar tecnologías
        assertNotNull(resultado.get(0).getTecnologiasUsadas());
        assertEquals(2, resultado.get(0).getTecnologiasUsadas().size());
        assertTrue(resultado.get(0).getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT));
        assertTrue(resultado.get(0).getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));

        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaValida);
    }

    @Test
    @DisplayName("getAllExperiencias - Debería retornar lista vacía cuando no hay experiencias")
    void getAllExperiencias_ShouldReturnEmptyList_WhenNoExperiencias() {
        // Arrange
        when(experienciaRepository.findAll()).thenReturn(List.of());

        // Act
        List<ExperienciaDto> resultado = experienciaService.getAllExperiencias();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, never()).toExperienciaDto(any());
    }

    // ==================== TESTS DE FLUJO COMPLETO ====================

    @Test
    @DisplayName("Flujo completo - Guardar, actualizar y eliminar experiencia")
    void fullFlow_SaveUpdateDeleteExperiencia() {
        // 1. Guardar
        Experiencia experienciaParaGuardar = Experiencia.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto test")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA, TecnologiaUsada.SPRINGBOOT)) // 🔥 Cambiado a lista
                .imagen(imagen)
                .usuario(usuario)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(1)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto test")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA, TecnologiaUsada.SPRINGBOOT)) // 🔥 Cambiado a lista
                .imagen(imagen)
                .usuario(usuario)
                .build();

        ExperienciaDto dtoParaGuardar = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto test")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA, TecnologiaUsada.SPRINGBOOT)) // 🔥 Cambiado a lista
                .imagen(imagenDto)
                .build();

        when(experienciaMapper.toExperiencia(dtoParaGuardar)).thenReturn(experienciaParaGuardar);
        when(experienciaRepository.save(experienciaParaGuardar)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(dtoParaGuardar);

        // Act - Guardar
        ExperienciaDto dtoGuardado = experienciaService.saveExperiencia(dtoParaGuardar);
        assertNotNull(dtoGuardado);
        assertEquals("Proyecto Test", dtoGuardado.getTitulo());
        assertTrue(dtoGuardado.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));

        // 2. Actualizar
        ExperienciaDto dtoActualizado = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada del proyecto test")
                .link("https://github.com/usuario/test-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiasUsadas(List.of(TecnologiaUsada.REACT, TecnologiaUsada.TYPESCRIPT)) // 🔥 Cambiado a lista
                .imagen(imagenDto)
                .build();

        Experiencia experienciaActualizada = Experiencia.builder()
                .id(1)
                .titulo("Proyecto Test Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada del proyecto test")
                .link("https://github.com/usuario/test-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiasUsadas(List.of(TecnologiaUsada.REACT, TecnologiaUsada.TYPESCRIPT)) // 🔥 Cambiado a lista
                .imagen(imagen)
                .usuario(usuario)
                .build();

        when(experienciaRepository.findById(1)).thenReturn(Optional.of(experienciaGuardada));
        when(experienciaRepository.save(any(Experiencia.class))).thenReturn(experienciaActualizada);
        when(experienciaMapper.toExperienciaDto(experienciaActualizada)).thenReturn(dtoActualizado);

        // Act - Actualizar
        ExperienciaDto dtoActualizadoResult = experienciaService.actualizarExperienciaPorId(1, dtoActualizado);
        assertNotNull(dtoActualizadoResult);
        assertEquals("Proyecto Test Actualizado", dtoActualizadoResult.getTitulo());
        assertTrue(dtoActualizadoResult.getTecnologiasUsadas().contains(TecnologiaUsada.REACT));
        assertTrue(dtoActualizadoResult.getTecnologiasUsadas().contains(TecnologiaUsada.TYPESCRIPT));
        assertFalse(dtoActualizadoResult.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));

        // 3. Eliminar
        doNothing().when(experienciaRepository).deleteById(1);

        // Act - Eliminar
        experienciaService.deleteExperienciaPorId(1);

        // Assert - Verificar eliminación
        verify(experienciaRepository, times(1)).deleteById(1);
    }

    // ==================== TESTS CON DIFERENTES ENUMS ====================

    @Test
    @DisplayName("saveExperiencia - Debería manejar todos los tipos de experiencia")
    void saveExperiencia_ShouldHandleAllTiposExperiencia() {
        // Arrange
        TipoExperiencia[] tipos = TipoExperiencia.values();

        for (TipoExperiencia tipo : tipos) {
            ExperienciaDto dto = ExperienciaDto.builder()
                    .titulo("Proyecto " + tipo.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(tipo)
                    .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT)) // 🔥 Cambiado a lista
                    .imagen(imagenDto)
                    .build();

            Experiencia experiencia = Experiencia.builder()
                    .titulo("Proyecto " + tipo.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(tipo)
                    .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT)) // 🔥 Cambiado a lista
                    .imagen(imagen)
                    .usuario(usuario)
                    .build();

            when(experienciaMapper.toExperiencia(dto)).thenReturn(experiencia);
            when(experienciaRepository.save(experiencia)).thenReturn(experiencia);
            when(experienciaMapper.toExperienciaDto(experiencia)).thenReturn(dto);

            // Act
            ExperienciaDto resultado = experienciaService.saveExperiencia(dto);

            // Assert
            assertNotNull(resultado);
            assertEquals(tipo, resultado.getTipoExperiencia());
            assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT));
        }
    }

    @Test
    @DisplayName("saveExperiencia - Debería manejar todas las tecnologías")
    void saveExperiencia_ShouldHandleAllTecnologias() {
        // Arrange
        TecnologiaUsada[] tecnologias = TecnologiaUsada.values();

        for (TecnologiaUsada tecnologia : tecnologias) {
            ExperienciaDto dto = ExperienciaDto.builder()
                    .titulo("Proyecto con " + tecnologia.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiasUsadas(List.of(tecnologia)) // 🔥 Cambiado a lista
                    .imagen(imagenDto)
                    .build();

            Experiencia experiencia = Experiencia.builder()
                    .titulo("Proyecto con " + tecnologia.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiasUsadas(List.of(tecnologia)) // 🔥 Cambiado a lista
                    .imagen(imagen)
                    .usuario(usuario)
                    .build();

            when(experienciaMapper.toExperiencia(dto)).thenReturn(experiencia);
            when(experienciaRepository.save(experiencia)).thenReturn(experiencia);
            when(experienciaMapper.toExperienciaDto(experiencia)).thenReturn(dto);

            // Act
            ExperienciaDto resultado = experienciaService.saveExperiencia(dto);

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.getTecnologiasUsadas().contains(tecnologia));
        }
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("saveExperiencia - Debería guardar experiencia con fechaFinProyecto nula")
    void saveExperiencia_ShouldSaveExperienciaWithNullFechaFin() {
        // Arrange
        ExperienciaDto dto = ExperienciaDto.builder()
                .titulo("Proyecto en Curso")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción del proyecto en curso")
                .link("https://github.com/usuario/proyecto-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA)) // 🔥 Cambiado a lista
                .imagen(imagenDto)
                .build();

        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto en Curso")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción del proyecto en curso")
                .link("https://github.com/usuario/proyecto-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA)) // 🔥 Cambiado a lista
                .imagen(imagen)
                .usuario(usuario)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(1)
                .titulo("Proyecto en Curso")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción del proyecto en curso")
                .link("https://github.com/usuario/proyecto-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA)) // 🔥 Cambiado a lista
                .imagen(imagen)
                .usuario(usuario)
                .build();

        when(experienciaMapper.toExperiencia(dto)).thenReturn(experiencia);
        when(experienciaRepository.save(experiencia)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(dto);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(dto);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getFechaFinProyecto());
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));
    }

    @Test
    @DisplayName("saveExperiencia - Debería guardar experiencia sin imagen")
    void saveExperiencia_ShouldSaveExperienciaWithoutImage() {
        // Arrange
        ExperienciaDto dto = ExperienciaDto.builder()
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imagen")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON)) // 🔥 Cambiado a lista
                .imagen(null)
                .build();

        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imagen")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON)) // 🔥 Cambiado a lista
                .imagen(null)
                .usuario(usuario)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(1)
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imagen")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON)) // 🔥 Cambiado a lista
                .imagen(null)
                .usuario(usuario)
                .build();

        when(experienciaMapper.toExperiencia(dto)).thenReturn(experiencia);
        when(experienciaRepository.save(experiencia)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(dto);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(dto);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getImagen());
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.PYTHON));
    }

    // ==================== TESTS DE VALIDACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar interacciones entre componentes en saveExperiencia")
    void verifyInteractionsInSaveExperiencia() {
        // Arrange
        when(experienciaMapper.toExperiencia(experienciaDtoValido)).thenReturn(experienciaValida);
        when(experienciaRepository.save(experienciaValida)).thenReturn(experienciaValida);
        when(experienciaMapper.toExperienciaDto(experienciaValida)).thenReturn(experienciaDtoValido);

        // Act
        experienciaService.saveExperiencia(experienciaDtoValido);

        // Assert - Verificar orden de interacciones
        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoValido);
        verify(experienciaRepository, times(1)).save(experienciaValida);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaValida);
        verifyNoMoreInteractions(experienciaMapper, experienciaRepository);
    }

    @Test
    @DisplayName("Verificar interacciones entre componentes en actualizarExperienciaPorId")
    void verifyInteractionsInActualizarExperienciaPorId() {
        // Arrange
        Integer id = 1;
        when(experienciaRepository.findById(id)).thenReturn(Optional.of(experienciaValida));
        when(experienciaRepository.save(experienciaValida)).thenReturn(experienciaValida);
        when(experienciaMapper.toExperienciaDto(experienciaValida)).thenReturn(experienciaDtoValido);

        // Act
        experienciaService.actualizarExperienciaPorId(id, experienciaDtoValido);

        // Assert - Verificar orden de interacciones
        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, times(1)).save(experienciaValida);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaValida);
        verifyNoMoreInteractions(experienciaMapper, experienciaRepository);
    }

    @Test
    @DisplayName("Verificar interacciones entre componentes en getAllExperiencias")
    void verifyInteractionsInGetAllExperiencias() {
        // Arrange
        List<Experiencia> experiencias = Arrays.asList(experienciaValida);
        when(experienciaRepository.findAll()).thenReturn(experiencias);
        when(experienciaMapper.toExperienciaDto(experienciaValida)).thenReturn(experienciaDtoValido);

        // Act
        experienciaService.getAllExperiencias();

        // Assert - Verificar orden de interacciones
        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaValida);
        verifyNoMoreInteractions(experienciaMapper, experienciaRepository);
    }
}