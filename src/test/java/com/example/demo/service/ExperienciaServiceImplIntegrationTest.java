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
import com.example.demo.repository.ImagenRepository;
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
import java.util.Collections;
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
    private ImagenRepository imagenRepository;

    @Mock
    private ExperienciaMapper experienciaMapper;

    @InjectMocks
    private ExperienciaServiceImpl experienciaService;

    private Experiencia experienciaValida;
    private ExperienciaDto experienciaDtoValido;
    private Usuario usuario;
    private Imagen imagen1;
    private Imagen imagen2;
    private ImagenDto imagenDto1;
    private ImagenDto imagenDto2;
    private List<Imagen> listaImagenes;
    private List<ImagenDto> listaImagenesDto;

    @BeforeEach
    void setUp() {
        // Configuración inicial
        usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("password123")
                .build();

        imagen1 = Imagen.builder()
                .id(1)
                .url("proyecto-principal.jpg")
                .alt("Captura principal del proyecto")
                .build();

        imagen2 = Imagen.builder()
                .id(2)
                .url("proyecto-detalle.jpg")
                .alt("Detalle del proyecto")
                .build();

        listaImagenes = Arrays.asList(imagen1, imagen2);

        imagenDto1 = ImagenDto.builder()
                .id(1)
                .url("proyecto-principal.jpg")
                .alt("Captura principal del proyecto")
                .build();

        imagenDto2 = ImagenDto.builder()
                .id(2)
                .url("proyecto-detalle.jpg")
                .alt("Detalle del proyecto")
                .build();

        listaImagenesDto = Arrays.asList(imagenDto1, imagenDto2);

        // Experiencia con lista de imágenes y tecnologías
        experienciaValida = Experiencia.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA))
                .imagenes(listaImagenes) // 🔥 Cambiado a lista de imágenes
                .usuario(usuario)
                .build();

        // ExperienciaDto con lista de imágenes y tecnologías
        experienciaDtoValido = ExperienciaDto.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA))
                .imagenes(listaImagenesDto) // 🔥 Cambiado a lista de imágenes
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA))
                .imagenes(listaImagenes) // 🔥 Cambiado a lista
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA))
                .imagenes(listaImagenes)
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

        // Verificar tecnologías
        assertNotNull(resultado.getTecnologiasUsadas());
        assertEquals(2, resultado.getTecnologiasUsadas().size());
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT));
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));

        // 🔥 Verificar lista de imágenes
        assertNotNull(resultado.getImagenes());
        assertEquals(2, resultado.getImagenes().size());
        assertEquals("proyecto-principal.jpg", resultado.getImagenes().get(0).getUrl());
        assertEquals("proyecto-detalle.jpg", resultado.getImagenes().get(1).getUrl());

        verify(experienciaMapper, times(1)).toExperiencia(experienciaDtoValido);
        verify(experienciaRepository, times(1)).save(experienciaParaGuardar);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaGuardada);
    }

    @Test
    @DisplayName("saveExperiencia - Debería guardar una experiencia sin imágenes correctamente")
    void saveExperiencia_ShouldSaveExperienciaWithoutImagesCorrectly() {
        // Arrange
        ExperienciaDto dtoSinImagenes = ExperienciaDto.builder()
                .titulo("Proyecto sin Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imágenes")
                .link("https://github.com/usuario/proyecto-sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON))
                .imagenes(null) // Sin imágenes
                .build();

        Experiencia experienciaSinImagenes = Experiencia.builder()
                .titulo("Proyecto sin Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imágenes")
                .link("https://github.com/usuario/proyecto-sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON))
                .imagenes(null)
                .usuario(usuario)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(2)
                .titulo("Proyecto sin Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imágenes")
                .link("https://github.com/usuario/proyecto-sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON))
                .imagenes(null)
                .usuario(usuario)
                .build();

        when(experienciaMapper.toExperiencia(dtoSinImagenes)).thenReturn(experienciaSinImagenes);
        when(experienciaRepository.save(experienciaSinImagenes)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(dtoSinImagenes);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(dtoSinImagenes);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getImagenes());
        assertEquals("Proyecto sin Imágenes", resultado.getTitulo());
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.PYTHON));

        verify(experienciaMapper, times(1)).toExperiencia(dtoSinImagenes);
        verify(experienciaRepository, times(1)).save(experienciaSinImagenes);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaGuardada);
    }

    @Test
    @DisplayName("saveExperiencia - Debería guardar una experiencia con lista vacía de imágenes")
    void saveExperiencia_ShouldSaveExperienciaWithEmptyImagesList() {
        // Arrange
        ExperienciaDto dtoListaVacia = ExperienciaDto.builder()
                .titulo("Proyecto con lista vacía de imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto con lista vacía de imágenes")
                .link("https://github.com/usuario/proyecto-lista-vacia")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(Collections.emptyList()) // Lista vacía
                .build();

        Experiencia experienciaListaVacia = Experiencia.builder()
                .titulo("Proyecto con lista vacía de imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto con lista vacía de imágenes")
                .link("https://github.com/usuario/proyecto-lista-vacia")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(Collections.emptyList())
                .usuario(usuario)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(3)
                .titulo("Proyecto con lista vacía de imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto con lista vacía de imágenes")
                .link("https://github.com/usuario/proyecto-lista-vacia")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(Collections.emptyList())
                .usuario(usuario)
                .build();

        when(experienciaMapper.toExperiencia(dtoListaVacia)).thenReturn(experienciaListaVacia);
        when(experienciaRepository.save(experienciaListaVacia)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(dtoListaVacia);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(dtoListaVacia);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getImagenes());
        assertTrue(resultado.getImagenes().isEmpty());
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));

        verify(experienciaMapper, times(1)).toExperiencia(dtoListaVacia);
        verify(experienciaRepository, times(1)).save(experienciaListaVacia);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaGuardada);
    }

    @Test
    @DisplayName("saveExperiencia - Debería guardar una experiencia con múltiples tecnologías e imágenes")
    void saveExperiencia_ShouldSaveExperienciaWithMultipleTecnologiasAndImagenes() {
        // Arrange
        List<TecnologiaUsada> tecnologias = Arrays.asList(
                TecnologiaUsada.JAVA,
                TecnologiaUsada.SPRINGBOOT,
                TecnologiaUsada.REACT,
                TecnologiaUsada.TYPESCRIPT
        );

        List<ImagenDto> imagenesDto = Arrays.asList(
                ImagenDto.builder().url("fullstack-ui.jpg").alt("UI del proyecto").build(),
                ImagenDto.builder().url("fullstack-backend.jpg").alt("Backend del proyecto").build(),
                ImagenDto.builder().url("fullstack-db.jpg").alt("Base de datos del proyecto").build()
        );

        ExperienciaDto dto = ExperienciaDto.builder()
                .titulo("Proyecto Full Stack")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto full stack")
                .link("https://github.com/usuario/fullstack")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologias)
                .imagenes(imagenesDto)
                .build();

        List<Imagen> imagenes = Arrays.asList(
                Imagen.builder().url("fullstack-ui.jpg").alt("UI del proyecto").build(),
                Imagen.builder().url("fullstack-backend.jpg").alt("Backend del proyecto").build(),
                Imagen.builder().url("fullstack-db.jpg").alt("Base de datos del proyecto").build()
        );

        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Full Stack")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto full stack")
                .link("https://github.com/usuario/fullstack")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologias)
                .imagenes(imagenes)
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
                .imagenes(imagenes)
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

        assertNotNull(resultado.getImagenes());
        assertEquals(3, resultado.getImagenes().size());
        assertEquals("fullstack-ui.jpg", resultado.getImagenes().get(0).getUrl());
        assertEquals("fullstack-backend.jpg", resultado.getImagenes().get(1).getUrl());
        assertEquals("fullstack-db.jpg", resultado.getImagenes().get(2).getUrl());

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

        List<ImagenDto> imagenesActualizadas = Arrays.asList(
                ImagenDto.builder().id(3).url("updated-1.jpg").alt("Imagen actualizada 1").build(),
                ImagenDto.builder().id(4).url("updated-2.jpg").alt("Imagen actualizada 2").build()
        );

        ExperienciaDto dtoActualizado = ExperienciaDto.builder()
                .id(id)
                .titulo("Proyecto Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada del proyecto")
                .link("https://github.com/usuario/proyecto-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiasUsadas(List.of(TecnologiaUsada.ANGULAR, TecnologiaUsada.TYPESCRIPT))
                .imagenes(imagenesActualizadas)
                .build();

        Experiencia experienciaActualizada = Experiencia.builder()
                .id(id)
                .titulo("Proyecto Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada del proyecto")
                .link("https://github.com/usuario/proyecto-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiasUsadas(List.of(TecnologiaUsada.ANGULAR, TecnologiaUsada.TYPESCRIPT))
                .imagenes(Arrays.asList(
                        Imagen.builder().id(3).url("updated-1.jpg").alt("Imagen actualizada 1").build(),
                        Imagen.builder().id(4).url("updated-2.jpg").alt("Imagen actualizada 2").build()
                ))
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

        // Verificar tecnologías actualizadas
        assertNotNull(resultado.getTecnologiasUsadas());
        assertEquals(2, resultado.getTecnologiasUsadas().size());
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.ANGULAR));
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.TYPESCRIPT));

        // 🔥 Verificar imágenes actualizadas
        assertNotNull(resultado.getImagenes());
        assertEquals(2, resultado.getImagenes().size());
        assertEquals("updated-1.jpg", resultado.getImagenes().get(0).getUrl());
        assertEquals("updated-2.jpg", resultado.getImagenes().get(1).getUrl());

        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, times(1)).save(experienciaValida);
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaActualizada);
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería actualizar la lista de imágenes correctamente")
    void actualizarExperienciaPorId_ShouldUpdateImagenesCorrectly() {
        // Arrange
        Integer id = 1;
        List<ImagenDto> nuevasImagenes = Arrays.asList(
                ImagenDto.builder().id(5).url("img-nueva-1.jpg").alt("Imagen nueva 1").build(),
                ImagenDto.builder().id(6).url("img-nueva-2.jpg").alt("Imagen nueva 2").build(),
                ImagenDto.builder().id(7).url("img-nueva-3.jpg").alt("Imagen nueva 3").build()
        );

        ExperienciaDto dtoActualizado = ExperienciaDto.builder()
                .id(id)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción del proyecto")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(nuevasImagenes)
                .build();

        when(experienciaRepository.findById(id)).thenReturn(Optional.of(experienciaValida));
        when(experienciaRepository.save(any(Experiencia.class))).thenReturn(experienciaValida);
        when(experienciaMapper.toExperienciaDto(any(Experiencia.class))).thenReturn(dtoActualizado);

        // Act
        experienciaService.actualizarExperienciaPorId(id, dtoActualizado);

        // Assert - Verificar que las imágenes se actualizaron
        assertNotNull(experienciaValida.getImagenes());
        assertEquals(3, experienciaValida.getImagenes().size());
        assertEquals("img-nueva-1.jpg", experienciaValida.getImagenes().get(0).getUrl());
        assertEquals("img-nueva-2.jpg", experienciaValida.getImagenes().get(1).getUrl());
        assertEquals("img-nueva-3.jpg", experienciaValida.getImagenes().get(2).getUrl());

        verify(experienciaRepository, times(1)).findById(id);
        verify(experienciaRepository, times(1)).save(experienciaValida);
    }

    @Test
    @DisplayName("actualizarExperienciaPorId - Debería actualizar la lista de tecnologías correctamente")
    void actualizarExperienciaPorId_ShouldUpdateTecnologiasUsadasCorrectly() {
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
                .imagenes(listaImagenesDto)
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
    @DisplayName("actualizarExperienciaPorId - Debería eliminar todas las imágenes de una experiencia")
    void actualizarExperienciaPorId_ShouldRemoveAllImagenesFromExperiencia() {
        // Arrange
        Integer id = 1;
        ExperienciaDto dtoSinImagenes = ExperienciaDto.builder()
                .id(id)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Descripción del proyecto")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(null) // Sin imágenes
                .build();

        when(experienciaRepository.findById(id)).thenReturn(Optional.of(experienciaValida));
        when(experienciaRepository.save(any(Experiencia.class))).thenReturn(experienciaValida);
        when(experienciaMapper.toExperienciaDto(any(Experiencia.class))).thenReturn(dtoSinImagenes);

        // Act
        experienciaService.actualizarExperienciaPorId(id, dtoSinImagenes);

        // Assert
        assertNull(experienciaValida.getImagenes());

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

        // Verificar tecnologías
        assertNotNull(resultado.get(0).getTecnologiasUsadas());
        assertEquals(2, resultado.get(0).getTecnologiasUsadas().size());
        assertTrue(resultado.get(0).getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT));
        assertTrue(resultado.get(0).getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));

        // 🔥 Verificar imágenes
        assertNotNull(resultado.get(0).getImagenes());
        assertEquals(2, resultado.get(0).getImagenes().size());
        assertEquals("proyecto-principal.jpg", resultado.get(0).getImagenes().get(0).getUrl());
        assertEquals("proyecto-detalle.jpg", resultado.get(0).getImagenes().get(1).getUrl());

        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, times(1)).toExperienciaDto(experienciaValida);
    }

    @Test
    @DisplayName("getAllExperiencias - Debería retornar lista vacía cuando no hay experiencias")
    void getAllExperiencias_ShouldReturnEmptyList_WhenNoExperiencias() {
        // Arrange
        when(experienciaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ExperienciaDto> resultado = experienciaService.getAllExperiencias();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, never()).toExperienciaDto(any());
    }

    @Test
    @DisplayName("getAllExperiencias - Debería manejar experiencias con imágenes nulas")
    void getAllExperiencias_ShouldHandleExperienciasWithNullImages() {
        // Arrange
        Experiencia experienciaSinImagenes = Experiencia.builder()
                .id(2)
                .titulo("Proyecto sin Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imágenes")
                .link("https://github.com/usuario/proyecto-sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON))
                .imagenes(null)
                .usuario(usuario)
                .build();

        ExperienciaDto dtoSinImagenes = ExperienciaDto.builder()
                .id(2)
                .titulo("Proyecto sin Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imágenes")
                .link("https://github.com/usuario/proyecto-sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON))
                .imagenes(null)
                .build();

        List<Experiencia> experiencias = Arrays.asList(experienciaValida, experienciaSinImagenes);

        when(experienciaRepository.findAll()).thenReturn(experiencias);
        when(experienciaMapper.toExperienciaDto(experienciaValida)).thenReturn(experienciaDtoValido);
        when(experienciaMapper.toExperienciaDto(experienciaSinImagenes)).thenReturn(dtoSinImagenes);

        // Act
        List<ExperienciaDto> resultado = experienciaService.getAllExperiencias();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertNotNull(resultado.get(0).getImagenes());
        assertNull(resultado.get(1).getImagenes());

        verify(experienciaRepository, times(1)).findAll();
        verify(experienciaMapper, times(2)).toExperienciaDto(any(Experiencia.class));
    }

    // ==================== TESTS DE FLUJO COMPLETO ====================

    @Test
    @DisplayName("Flujo completo - Guardar, actualizar y eliminar experiencia")
    void fullFlow_SaveUpdateDeleteExperiencia() {
        // 1. Guardar
        List<Imagen> imagenesParaGuardar = Arrays.asList(
                Imagen.builder().url("test-1.jpg").alt("Test 1").build(),
                Imagen.builder().url("test-2.jpg").alt("Test 2").build()
        );

        Experiencia experienciaParaGuardar = Experiencia.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto test")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA, TecnologiaUsada.SPRINGBOOT))
                .imagenes(imagenesParaGuardar)
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA, TecnologiaUsada.SPRINGBOOT))
                .imagenes(imagenesParaGuardar)
                .usuario(usuario)
                .build();

        List<ImagenDto> imagenesDtoParaGuardar = Arrays.asList(
                ImagenDto.builder().url("test-1.jpg").alt("Test 1").build(),
                ImagenDto.builder().url("test-2.jpg").alt("Test 2").build()
        );

        ExperienciaDto dtoParaGuardar = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto test")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA, TecnologiaUsada.SPRINGBOOT))
                .imagenes(imagenesDtoParaGuardar)
                .build();

        when(experienciaMapper.toExperiencia(dtoParaGuardar)).thenReturn(experienciaParaGuardar);
        when(experienciaRepository.save(experienciaParaGuardar)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(dtoParaGuardar);

        // Act - Guardar
        ExperienciaDto dtoGuardado = experienciaService.saveExperiencia(dtoParaGuardar);
        assertNotNull(dtoGuardado);
        assertEquals("Proyecto Test", dtoGuardado.getTitulo());
        assertTrue(dtoGuardado.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));
        assertNotNull(dtoGuardado.getImagenes());
        assertEquals(2, dtoGuardado.getImagenes().size());

        // 2. Actualizar
        List<ImagenDto> imagenesActualizadas = Arrays.asList(
                ImagenDto.builder().id(1).url("updated-test-1.jpg").alt("Updated 1").build(),
                ImagenDto.builder().id(2).url("updated-test-2.jpg").alt("Updated 2").build()
        );

        ExperienciaDto dtoActualizado = ExperienciaDto.builder()
                .id(1)
                .titulo("Proyecto Test Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada del proyecto test")
                .link("https://github.com/usuario/test-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiasUsadas(List.of(TecnologiaUsada.REACT, TecnologiaUsada.TYPESCRIPT))
                .imagenes(imagenesActualizadas)
                .build();

        List<Imagen> imagenesActualizadasEntity = Arrays.asList(
                Imagen.builder().id(1).url("updated-test-1.jpg").alt("Updated 1").build(),
                Imagen.builder().id(2).url("updated-test-2.jpg").alt("Updated 2").build()
        );

        Experiencia experienciaActualizada = Experiencia.builder()
                .id(1)
                .titulo("Proyecto Test Actualizado")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción actualizada del proyecto test")
                .link("https://github.com/usuario/test-actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiasUsadas(List.of(TecnologiaUsada.REACT, TecnologiaUsada.TYPESCRIPT))
                .imagenes(imagenesActualizadasEntity)
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
        assertNotNull(dtoActualizadoResult.getImagenes());
        assertEquals(2, dtoActualizadoResult.getImagenes().size());
        assertEquals("updated-test-1.jpg", dtoActualizadoResult.getImagenes().get(0).getUrl());

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
                    .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                    .imagenes(listaImagenesDto)
                    .build();

            Experiencia experiencia = Experiencia.builder()
                    .titulo("Proyecto " + tipo.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(tipo)
                    .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                    .imagenes(listaImagenes)
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
            assertNotNull(resultado.getImagenes());
            assertEquals(2, resultado.getImagenes().size());
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
                    .tecnologiasUsadas(List.of(tecnologia))
                    .imagenes(listaImagenesDto)
                    .build();

            Experiencia experiencia = Experiencia.builder()
                    .titulo("Proyecto con " + tecnologia.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiasUsadas(List.of(tecnologia))
                    .imagenes(listaImagenes)
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
            assertNotNull(resultado.getImagenes());
            assertEquals(2, resultado.getImagenes().size());
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(listaImagenesDto)
                .build();

        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto en Curso")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción del proyecto en curso")
                .link("https://github.com/usuario/proyecto-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(listaImagenes)
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
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(listaImagenes)
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
        assertNotNull(resultado.getImagenes());
        assertEquals(2, resultado.getImagenes().size());
    }

    @Test
    @DisplayName("saveExperiencia - Debería guardar experiencia con títulos largos")
    void saveExperiencia_ShouldSaveExperienciaWithLongTitulo() {
        // Arrange
        String tituloLargo = "A".repeat(145);
        ExperienciaDto dto = ExperienciaDto.builder()
                .titulo(tituloLargo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/proyecto-largo")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(listaImagenesDto)
                .build();

        Experiencia experiencia = Experiencia.builder()
                .titulo(tituloLargo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/proyecto-largo")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(listaImagenes)
                .usuario(usuario)
                .build();

        Experiencia experienciaGuardada = Experiencia.builder()
                .id(1)
                .titulo(tituloLargo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/proyecto-largo")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(listaImagenes)
                .usuario(usuario)
                .build();

        when(experienciaMapper.toExperiencia(dto)).thenReturn(experiencia);
        when(experienciaRepository.save(experiencia)).thenReturn(experienciaGuardada);
        when(experienciaMapper.toExperienciaDto(experienciaGuardada)).thenReturn(dto);

        // Act
        ExperienciaDto resultado = experienciaService.saveExperiencia(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(tituloLargo, resultado.getTitulo());
        assertEquals(145, resultado.getTitulo().length());
        assertNotNull(resultado.getImagenes());
        assertEquals(2, resultado.getImagenes().size());
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