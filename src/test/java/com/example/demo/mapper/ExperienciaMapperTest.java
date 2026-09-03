package com.example.demo.mapper;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import com.example.demo.model.Experiencia;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase ExperienciaMapper
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
@ExtendWith(MockitoExtension.class)
class ExperienciaMapperTest {

    @Mock
    private ImagenMapper imagenMapper;

    @InjectMocks
    private ExperienciaMapperImpl experienciaMapper;

    private Experiencia experienciaValida;
    private ExperienciaDto experienciaDtoValido;
    private Imagen imagen1;
    private Imagen imagen2;
    private ImagenDto imagenDto1;
    private ImagenDto imagenDto2;
    private List<Imagen> listaImagenes;
    private List<ImagenDto> listaImagenesDto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear imágenes
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

        // Crear usuario
        usuario = Usuario.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack")
                .descripcion("Apasionado por la tecnología")
                .build();

        // Crear experiencia válida con lista de imágenes y tecnologías
        experienciaValida = Experiencia.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT para gestión de usuarios")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA))
                .imagenes(listaImagenes) // 🔥 Cambiado a lista de imágenes
                .usuario(usuario)
                .build();

        // Crear ExperienciaDto válido con lista de imágenes y tecnologías
        experienciaDtoValido = ExperienciaDto.builder()
                .id(1)
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT para gestión de usuarios")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA))
                .imagenes(listaImagenesDto) // 🔥 Cambiado a lista de imágenes
                .build();

        // Configurar comportamiento del mock de ImagenMapper para toImagenDto
        when(imagenMapper.toImagenDto(any(Imagen.class))).thenAnswer(invocation -> {
            Imagen img = invocation.getArgument(0);
            if (img == null) return null;
            return ImagenDto.builder()
                    .id(img.getId())
                    .url(img.getUrl())
                    .alt(img.getAlt())
                    .build();
        });

        // Configurar comportamiento del mock de ImagenMapper para toImagen
        when(imagenMapper.toImagen(any(ImagenDto.class))).thenAnswer(invocation -> {
            ImagenDto dto = invocation.getArgument(0);
            if (dto == null) return null;
            return Imagen.builder()
                    .id(dto.getId())
                    .url(dto.getUrl())
                    .alt(dto.getAlt())
                    .build();
        });
    }

    // ==================== TESTS TO DTO ====================

    @Test
    @DisplayName("toExperienciaDto - Debería mapear Experiencia a ExperienciaDto correctamente")
    void toExperienciaDto_ShouldMapExperienciaToExperienciaDtoCorrectly() {
        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaValida);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaValida.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(experienciaValida.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertEquals(experienciaValida.getFechaInicioProyecto(), resultado.getFechaInicioProyecto(),
                "La fecha de inicio debería coincidir");
        assertEquals(experienciaValida.getFechaFinProyecto(), resultado.getFechaFinProyecto(),
                "La fecha de fin debería coincidir");
        assertEquals(experienciaValida.getDescripcion(), resultado.getDescripcion(),
                "La descripción debería coincidir");
        assertEquals(experienciaValida.getLink(), resultado.getLink(), "El link debería coincidir");
        assertEquals(experienciaValida.getTipoExperiencia(), resultado.getTipoExperiencia(),
                "El tipo de experiencia debería coincidir");

        // Verificar que la lista de tecnologías se mapea correctamente
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(experienciaValida.getTecnologiasUsadas().size(), resultado.getTecnologiasUsadas().size(),
                "El tamaño de la lista de tecnologías debería coincidir");
        assertTrue(resultado.getTecnologiasUsadas().containsAll(experienciaValida.getTecnologiasUsadas()),
                "Las tecnologías usadas deberían coincidir");

        // 🔥 Verificar que la lista de imágenes se mapea correctamente
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(experienciaValida.getImagenes().size(), resultado.getImagenes().size(),
                "El tamaño de la lista de imágenes debería coincidir");
        assertEquals(experienciaValida.getImagenes().get(0).getId(), resultado.getImagenes().get(0).getId(),
                "El ID de la primera imagen debería coincidir");
        assertEquals(experienciaValida.getImagenes().get(1).getId(), resultado.getImagenes().get(1).getId(),
                "El ID de la segunda imagen debería coincidir");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagenDto(any(Imagen.class));
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear Experiencia sin imágenes a ExperienciaDto")
    void toExperienciaDto_ShouldMapExperienciaWithoutImagesToExperienciaDto() {
        // Arrange - Experiencia sin imágenes
        Experiencia experienciaSinImagenes = Experiencia.builder()
                .id(2)
                .titulo("Proyecto sin Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(null) // Sin imágenes
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaSinImagenes);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaSinImagenes.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(experienciaSinImagenes.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertNull(resultado.getImagenes(), "La lista de imágenes debería ser nula");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA),
                "La tecnología usada debería ser JAVA");

        // Verificar que no se llamó a ImagenMapper para imágenes nulas
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear Experiencia con lista vacía de imágenes")
    void toExperienciaDto_ShouldMapExperienciaWithEmptyImagesList() {
        // Arrange - Experiencia con lista vacía de imágenes
        Experiencia experienciaConListaVacia = Experiencia.builder()
                .id(3)
                .titulo("Proyecto con Lista Vacía")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-lista-vacia")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(Collections.emptyList()) // Lista vacía
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaConListaVacia);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertTrue(resultado.getImagenes().isEmpty(), "La lista de imágenes debería estar vacía");

        // Verificar que no se llamó a ImagenMapper para lista vacía
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear Experiencia con fechaFinProyecto nula")
    void toExperienciaDto_ShouldMapExperienciaWithNullFechaFinProyecto() {
        // Arrange - Experiencia con fecha de fin nula (proyecto en curso)
        Experiencia experienciaEnCurso = Experiencia.builder()
                .id(4)
                .titulo("Proyecto en Curso")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON, TecnologiaUsada.DJANGO))
                .imagenes(listaImagenes)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaEnCurso);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaEnCurso.getId(), resultado.getId(), "El ID debería coincidir");
        assertNull(resultado.getFechaFinProyecto(), "La fecha de fin debería ser nula");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(2, resultado.getTecnologiasUsadas().size(), "Debería tener 2 tecnologías");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagenDto(any(Imagen.class));
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear Experiencia con usuario nulo")
    void toExperienciaDto_ShouldMapExperienciaWithNullUsuario() {
        // Arrange - Experiencia sin usuario
        Experiencia experienciaSinUsuario = Experiencia.builder()
                .id(5)
                .titulo("Proyecto Independiente")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-independiente")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVASCRIPT, TecnologiaUsada.TYPESCRIPT))
                .imagenes(listaImagenes)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaSinUsuario);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaSinUsuario.getId(), resultado.getId(), "El ID debería coincidir");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, resultado.getImagenes().size(), "Debería tener 2 imágenes");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertTrue(resultado.getTecnologiasUsadas().containsAll(
                        List.of(TecnologiaUsada.JAVASCRIPT, TecnologiaUsada.TYPESCRIPT)),
                "Las tecnologías usadas deberían coincidir");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagenDto(any(Imagen.class));
    }

    @Test
    @DisplayName("toExperienciaDto - Debería manejar Experiencia nula")
    void toExperienciaDto_ShouldHandleNullExperiencia() {
        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando la experiencia es nula");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagenDto(any());
    }

    // ==================== TESTS TO ENTITY ====================

    @Test
    @DisplayName("toExperiencia - Debería mapear ExperienciaDto a Experiencia correctamente")
    void toExperiencia_ShouldMapExperienciaDtoToExperienciaCorrectly() {
        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaDtoValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(experienciaDtoValido.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertEquals(experienciaDtoValido.getFechaInicioProyecto(), resultado.getFechaInicioProyecto(),
                "La fecha de inicio debería coincidir");
        assertEquals(experienciaDtoValido.getFechaFinProyecto(), resultado.getFechaFinProyecto(),
                "La fecha de fin debería coincidir");
        assertEquals(experienciaDtoValido.getDescripcion(), resultado.getDescripcion(),
                "La descripción debería coincidir");
        assertEquals(experienciaDtoValido.getLink(), resultado.getLink(), "El link debería coincidir");
        assertEquals(experienciaDtoValido.getTipoExperiencia(), resultado.getTipoExperiencia(),
                "El tipo de experiencia debería coincidir");

        // Verificar que la lista de tecnologías se mapea correctamente
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(experienciaDtoValido.getTecnologiasUsadas().size(), resultado.getTecnologiasUsadas().size(),
                "El tamaño de la lista de tecnologías debería coincidir");
        assertTrue(resultado.getTecnologiasUsadas().containsAll(experienciaDtoValido.getTecnologiasUsadas()),
                "Las tecnologías usadas deberían coincidir");

        // 🔥 Verificar que la lista de imágenes se mapea correctamente
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(experienciaDtoValido.getImagenes().size(), resultado.getImagenes().size(),
                "El tamaño de la lista de imágenes debería coincidir");
        assertEquals(experienciaDtoValido.getImagenes().get(0).getId(), resultado.getImagenes().get(0).getId(),
                "El ID de la primera imagen debería coincidir");
        assertEquals(experienciaDtoValido.getImagenes().get(1).getId(), resultado.getImagenes().get(1).getId(),
                "El ID de la segunda imagen debería coincidir");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagen(any(ImagenDto.class));
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear ExperienciaDto sin imágenes a Experiencia")
    void toExperiencia_ShouldMapExperienciaDtoWithoutImagesToExperiencia() {
        // Arrange - ExperienciaDto sin imágenes
        ExperienciaDto experienciaDtoSinImagenes = ExperienciaDto.builder()
                .id(6)
                .titulo("Proyecto sin Imágenes DTO")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagenes-dto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.REACT))
                .imagenes(null)
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoSinImagenes);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaDtoSinImagenes.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(experienciaDtoSinImagenes.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertNull(resultado.getImagenes(), "La lista de imágenes debería ser nula");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertTrue(resultado.getTecnologiasUsadas().contains(TecnologiaUsada.REACT),
                "La tecnología usada debería ser REACT");

        // Verificar que no se llamó a ImagenMapper para imágenes nulas
        verify(imagenMapper, never()).toImagen(any());
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear ExperienciaDto con lista vacía de imágenes")
    void toExperiencia_ShouldMapExperienciaDtoWithEmptyImagesList() {
        // Arrange - ExperienciaDto con lista vacía de imágenes
        ExperienciaDto experienciaDtoListaVacia = ExperienciaDto.builder()
                .id(7)
                .titulo("Proyecto DTO Lista Vacía")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-lista-vacia-dto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(Collections.emptyList())
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoListaVacia);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertTrue(resultado.getImagenes().isEmpty(), "La lista de imágenes debería estar vacía");

        // Verificar que no se llamó a ImagenMapper para lista vacía
        verify(imagenMapper, never()).toImagen(any());
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear ExperienciaDto con fechaFinProyecto nula")
    void toExperiencia_ShouldMapExperienciaDtoWithNullFechaFinProyecto() {
        // Arrange - ExperienciaDto con fecha de fin nula
        ExperienciaDto experienciaDtoEnCurso = ExperienciaDto.builder()
                .id(8)
                .titulo("Proyecto en Curso DTO")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso-dto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.DJANGO, TecnologiaUsada.PYTHON))
                .imagenes(listaImagenesDto)
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoEnCurso);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaDtoEnCurso.getId(), resultado.getId(), "El ID debería coincidir");
        assertNull(resultado.getFechaFinProyecto(), "La fecha de fin debería ser nula");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(2, resultado.getTecnologiasUsadas().size(), "Debería tener 2 tecnologías");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagen(any(ImagenDto.class));
    }

    @Test
    @DisplayName("toExperiencia - Debería manejar ExperienciaDto nulo")
    void toExperiencia_ShouldHandleNullExperienciaDto() {
        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando el ExperienciaDto es nulo");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagen(any());
    }

    // ==================== TESTS DE CONSISTENCIA ====================

    @Test
    @DisplayName("toExperienciaDto y toExperiencia - Deberían ser consistentes (round-trip)")
    void toExperienciaDtoAndToExperiencia_ShouldBeConsistent() {
        // Act - Convertir de Experiencia a ExperienciaDto y luego de vuelta a Experiencia
        ExperienciaDto dto = experienciaMapper.toExperienciaDto(experienciaValida);
        Experiencia experienciaConvertida = experienciaMapper.toExperiencia(dto);

        // Assert - Verificar que el round-trip mantiene los datos
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(experienciaConvertida, "La experiencia convertida no debería ser nula");
        assertEquals(experienciaValida.getId(), experienciaConvertida.getId(), "El ID debería ser el mismo");
        assertEquals(experienciaValida.getTitulo(), experienciaConvertida.getTitulo(),
                "El título debería ser el mismo");
        assertEquals(experienciaValida.getFechaInicioProyecto(), experienciaConvertida.getFechaInicioProyecto(),
                "La fecha de inicio debería ser la misma");
        assertEquals(experienciaValida.getFechaFinProyecto(), experienciaConvertida.getFechaFinProyecto(),
                "La fecha de fin debería ser la misma");
        assertEquals(experienciaValida.getDescripcion(), experienciaConvertida.getDescripcion(),
                "La descripción debería ser la misma");
        assertEquals(experienciaValida.getLink(), experienciaConvertida.getLink(),
                "El link debería ser el mismo");
        assertEquals(experienciaValida.getTipoExperiencia(), experienciaConvertida.getTipoExperiencia(),
                "El tipo de experiencia debería ser el mismo");

        // Verificar que la lista de tecnologías se mantiene en el round-trip
        assertNotNull(experienciaConvertida.getTecnologiasUsadas(),
                "Las tecnologías usadas no deberían ser nulas");
        assertEquals(experienciaValida.getTecnologiasUsadas().size(),
                experienciaConvertida.getTecnologiasUsadas().size(),
                "El tamaño de la lista de tecnologías debería ser el mismo");
        assertTrue(experienciaConvertida.getTecnologiasUsadas().containsAll(
                        experienciaValida.getTecnologiasUsadas()),
                "Las tecnologías usadas deberían ser las mismas");

        // 🔥 Verificar que la lista de imágenes se mantiene en el round-trip
        assertNotNull(experienciaConvertida.getImagenes(),
                "La lista de imágenes no debería ser nula");
        assertEquals(experienciaValida.getImagenes().size(),
                experienciaConvertida.getImagenes().size(),
                "El tamaño de la lista de imágenes debería ser el mismo");
        assertEquals(experienciaValida.getImagenes().get(0).getId(),
                experienciaConvertida.getImagenes().get(0).getId(),
                "El ID de la primera imagen debería ser el mismo");
        assertEquals(experienciaValida.getImagenes().get(1).getId(),
                experienciaConvertida.getImagenes().get(1).getId(),
                "El ID de la segunda imagen debería ser el mismo");
        assertEquals(experienciaValida.getImagenes().get(0).getUrl(),
                experienciaConvertida.getImagenes().get(0).getUrl(),
                "La URL de la primera imagen debería ser la misma");
        assertEquals(experienciaValida.getImagenes().get(1).getUrl(),
                experienciaConvertida.getImagenes().get(1).getUrl(),
                "La URL de la segunda imagen debería ser la misma");
    }

    @Test
    @DisplayName("toExperienciaDto y toExperiencia - Deberían ser consistentes con objetos sin imágenes")
    void toExperienciaDtoAndToExperiencia_ShouldBeConsistentWithNoImages() {
        // Arrange - ExperienciaDto sin imágenes
        ExperienciaDto dtoSinImagenes = ExperienciaDto.builder()
                .id(10)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(null)
                .build();

        // Act - Convertir a Experiencia y luego de vuelta a ExperienciaDto
        Experiencia experiencia = experienciaMapper.toExperiencia(dtoSinImagenes);
        ExperienciaDto dtoConvertido = experienciaMapper.toExperienciaDto(experiencia);

        // Assert
        assertNotNull(experiencia, "La experiencia no debería ser nula");
        assertNotNull(dtoConvertido, "El DTO convertido no debería ser nulo");
        assertEquals(dtoSinImagenes.getId(), dtoConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(dtoSinImagenes.getTitulo(), dtoConvertido.getTitulo(), "El título debería ser el mismo");
        assertNull(dtoConvertido.getImagenes(), "La lista de imágenes debería ser nula");
        assertNotNull(dtoConvertido.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertTrue(dtoConvertido.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT),
                "La tecnología usada debería ser SPRINGBOOT");
    }

    @Test
    @DisplayName("toExperienciaDto y toExperiencia - Deberían ser consistentes con fechaFinProyecto nula")
    void toExperienciaDtoAndToExperiencia_ShouldBeConsistentWithNullFechaFinProyecto() {
        // Arrange - Experiencia con fecha de fin nula
        Experiencia experienciaEnCurso = Experiencia.builder()
                .id(11)
                .titulo("Proyecto en Curso")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.PYTHON))
                .imagenes(Collections.emptyList())
                .usuario(null)
                .build();

        // Act - Convertir a DTO y luego de vuelta a Experiencia
        ExperienciaDto dto = experienciaMapper.toExperienciaDto(experienciaEnCurso);
        Experiencia experienciaConvertida = experienciaMapper.toExperiencia(dto);

        // Assert
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(experienciaConvertida, "La experiencia convertida no debería ser nula");
        assertEquals(experienciaEnCurso.getId(), experienciaConvertida.getId(), "El ID debería ser el mismo");
        assertNull(experienciaConvertida.getFechaFinProyecto(), "La fecha de fin debería ser nula");
        assertEquals(experienciaEnCurso.getTitulo(), experienciaConvertida.getTitulo(),
                "El título debería ser el mismo");
        assertNotNull(experienciaConvertida.getTecnologiasUsadas(),
                "Las tecnologías usadas no deberían ser nulas");
        assertTrue(experienciaConvertida.getTecnologiasUsadas().contains(TecnologiaUsada.PYTHON),
                "La tecnología usada debería ser PYTHON");
        assertNotNull(experienciaConvertida.getImagenes(), "La lista de imágenes no debería ser nula");
        assertTrue(experienciaConvertida.getImagenes().isEmpty(), "La lista de imágenes debería estar vacía");
    }

    @Test
    @DisplayName("toExperienciaDto y toExperiencia - Deberían ser consistentes con múltiples imágenes")
    void toExperienciaDtoAndToExperiencia_ShouldBeConsistentWithMultipleImages() {
        // Arrange - Crear una experiencia con múltiples imágenes
        Imagen img1 = Imagen.builder().id(1).url("img1.jpg").alt("Imagen 1").build();
        Imagen img2 = Imagen.builder().id(2).url("img2.jpg").alt("Imagen 2").build();
        Imagen img3 = Imagen.builder().id(3).url("img3.jpg").alt("Imagen 3").build();
        List<Imagen> imagenesMultiples = Arrays.asList(img1, img2, img3);

        Experiencia experienciaConMultiplesImagenes = Experiencia.builder()
                .id(12)
                .titulo("Proyecto con Múltiples Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/multiples-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(imagenesMultiples)
                .usuario(null)
                .build();

        // Configurar mock para múltiples imágenes
        when(imagenMapper.toImagenDto(any(Imagen.class))).thenAnswer(invocation -> {
            Imagen img = invocation.getArgument(0);
            if (img == null) return null;
            return ImagenDto.builder()
                    .id(img.getId())
                    .url(img.getUrl())
                    .alt(img.getAlt())
                    .build();
        });

        // Act - Convertir a DTO y luego de vuelta a Experiencia
        ExperienciaDto dto = experienciaMapper.toExperienciaDto(experienciaConMultiplesImagenes);
        Experiencia experienciaConvertida = experienciaMapper.toExperiencia(dto);

        // Assert
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(experienciaConvertida, "La experiencia convertida no debería ser nula");
        assertEquals(experienciaConMultiplesImagenes.getId(), experienciaConvertida.getId(),
                "El ID debería ser el mismo");
        assertNotNull(experienciaConvertida.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(3, experienciaConvertida.getImagenes().size(), "Debería tener 3 imágenes");
        assertEquals("img1.jpg", experienciaConvertida.getImagenes().get(0).getUrl());
        assertEquals("img2.jpg", experienciaConvertida.getImagenes().get(1).getUrl());
        assertEquals("img3.jpg", experienciaConvertida.getImagenes().get(2).getUrl());

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(3)).toImagenDto(any(Imagen.class));
        verify(imagenMapper, times(3)).toImagen(any(ImagenDto.class));
    }

    // ==================== TESTS CON DIFERENTES ENUMS ====================

    @Test
    @DisplayName("toExperienciaDto - Debería mapear correctamente todos los tipos de experiencia")
    void toExperienciaDto_ShouldMapAllTiposExperienciaCorrectly() {
        // Arrange - Probar todos los tipos de experiencia
        TipoExperiencia[] tipos = TipoExperiencia.values();

        for (TipoExperiencia tipo : tipos) {
            // Crear experiencia con cada tipo
            Experiencia experiencia = Experiencia.builder()
                    .id(100 + tipo.ordinal())
                    .titulo("Proyecto " + tipo.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(tipo)
                    .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                    .imagenes(listaImagenes)
                    .usuario(null)
                    .build();

            // Act
            ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experiencia);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, resultado.getTipoExperiencia(),
                    "El tipo de experiencia debería coincidir para: " + tipo);
        }
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear correctamente todos los tipos de experiencia")
    void toExperiencia_ShouldMapAllTiposExperienciaCorrectly() {
        // Arrange - Probar todos los tipos de experiencia
        TipoExperiencia[] tipos = TipoExperiencia.values();

        for (TipoExperiencia tipo : tipos) {
            // Crear ExperienciaDto con cada tipo
            ExperienciaDto dto = ExperienciaDto.builder()
                    .id(200 + tipo.ordinal())
                    .titulo("Proyecto DTO " + tipo.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(tipo)
                    .tecnologiasUsadas(List.of(TecnologiaUsada.REACT))
                    .imagenes(listaImagenesDto)
                    .build();

            // Act
            Experiencia resultado = experienciaMapper.toExperiencia(dto);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, resultado.getTipoExperiencia(),
                    "El tipo de experiencia debería coincidir para: " + tipo);
        }
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear correctamente todas las tecnologías en lista")
    void toExperienciaDto_ShouldMapAllTecnologiasUsadaCorrectly() {
        // Arrange - Probar todas las tecnologías
        TecnologiaUsada[] tecnologias = TecnologiaUsada.values();

        for (TecnologiaUsada tecnologia : tecnologias) {
            // Crear experiencia con cada tecnología
            Experiencia experiencia = Experiencia.builder()
                    .id(300 + tecnologia.ordinal())
                    .titulo("Proyecto con " + tecnologia.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiasUsadas(List.of(tecnologia))
                    .imagenes(Collections.emptyList())
                    .usuario(null)
                    .build();

            // Act
            ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experiencia);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tecnología: " + tecnologia);
            assertNotNull(resultado.getTecnologiasUsadas(),
                    "Las tecnologías usadas no deberían ser nulas para: " + tecnologia);
            assertTrue(resultado.getTecnologiasUsadas().contains(tecnologia),
                    "La tecnología usada debería coincidir para: " + tecnologia);
        }
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear correctamente múltiples tecnologías en lista")
    void toExperienciaDto_ShouldMapMultipleTecnologiasUsadaCorrectly() {
        // Arrange - Crear experiencia con múltiples tecnologías
        List<TecnologiaUsada> tecnologiasMultiples = Arrays.asList(
                TecnologiaUsada.JAVA,
                TecnologiaUsada.SPRINGBOOT,
                TecnologiaUsada.REACT,
                TecnologiaUsada.TYPESCRIPT
        );

        Experiencia experienciaMultiples = Experiencia.builder()
                .id(350)
                .titulo("Proyecto con múltiples tecnologías")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-multiples")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologiasMultiples)
                .imagenes(Collections.emptyList())
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaMultiples);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(4, resultado.getTecnologiasUsadas().size(), "Debería tener 4 tecnologías");
        assertTrue(resultado.getTecnologiasUsadas().containsAll(tecnologiasMultiples),
                "Debería contener todas las tecnologías");
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear correctamente todas las tecnologías en lista")
    void toExperiencia_ShouldMapAllTecnologiasUsadaCorrectly() {
        // Arrange - Probar todas las tecnologías
        TecnologiaUsada[] tecnologias = TecnologiaUsada.values();

        for (TecnologiaUsada tecnologia : tecnologias) {
            // Crear ExperienciaDto con cada tecnología
            ExperienciaDto dto = ExperienciaDto.builder()
                    .id(400 + tecnologia.ordinal())
                    .titulo("Proyecto DTO con " + tecnologia.name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción válida con más de 5 caracteres")
                    .link("https://github.com/usuario/proyecto")
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiasUsadas(List.of(tecnologia))
                    .imagenes(Collections.emptyList())
                    .build();

            // Act
            Experiencia resultado = experienciaMapper.toExperiencia(dto);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tecnología: " + tecnologia);
            assertNotNull(resultado.getTecnologiasUsadas(),
                    "Las tecnologías usadas no deberían ser nulas para: " + tecnologia);
            assertTrue(resultado.getTecnologiasUsadas().contains(tecnologia),
                    "La tecnología usada debería coincidir para: " + tecnologia);
        }
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear correctamente múltiples tecnologías en lista")
    void toExperiencia_ShouldMapMultipleTecnologiasUsadaCorrectly() {
        // Arrange - Crear ExperienciaDto con múltiples tecnologías
        List<TecnologiaUsada> tecnologiasMultiples = Arrays.asList(
                TecnologiaUsada.PYTHON,
                TecnologiaUsada.DJANGO,
                TecnologiaUsada.POSTGRESQL
        );

        ExperienciaDto dtoMultiples = ExperienciaDto.builder()
                .id(450)
                .titulo("Proyecto DTO con múltiples tecnologías")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-dto-multiples")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(tecnologiasMultiples)
                .imagenes(Collections.emptyList())
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(dtoMultiples);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(3, resultado.getTecnologiasUsadas().size(), "Debería tener 3 tecnologías");
        assertTrue(resultado.getTecnologiasUsadas().containsAll(tecnologiasMultiples),
                "Debería contener todas las tecnologías");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("toExperienciaDto - Debería manejar experiencia con campos vacíos")
    void toExperienciaDto_ShouldHandleExperienciaWithEmptyFields() {
        // Arrange - Experiencia con campos vacíos
        Experiencia experienciaVacia = Experiencia.builder()
                .id(9)
                .titulo("")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("")
                .link("")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of())
                .imagenes(null)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaVacia);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getTitulo(), "El título vacío debería mantenerse");
        assertEquals("", resultado.getDescripcion(), "La descripción vacía debería mantenerse");
        assertEquals("", resultado.getLink(), "El link vacío debería mantenerse");
        assertNull(resultado.getImagenes(), "La lista de imágenes debería ser nula");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertTrue(resultado.getTecnologiasUsadas().isEmpty(), "La lista de tecnologías debería estar vacía");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("toExperiencia - Debería manejar ExperienciaDto con campos vacíos")
    void toExperiencia_ShouldHandleExperienciaDtoWithEmptyFields() {
        // Arrange - ExperienciaDto con campos vacíos
        ExperienciaDto experienciaDtoVacio = ExperienciaDto.builder()
                .id(10)
                .titulo("")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("")
                .link("")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of())
                .imagenes(null)
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoVacio);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getTitulo(), "El título vacío debería mantenerse");
        assertEquals("", resultado.getDescripcion(), "La descripción vacía debería mantenerse");
        assertEquals("", resultado.getLink(), "El link vacío debería mantenerse");
        assertNull(resultado.getImagenes(), "La lista de imágenes debería ser nula");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertTrue(resultado.getTecnologiasUsadas().isEmpty(), "La lista de tecnologías debería estar vacía");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagen(any());
    }

    @Test
    @DisplayName("toExperienciaDto - Debería manejar experiencia con ID nulo")
    void toExperienciaDto_ShouldHandleExperienciaWithNullId() {
        // Arrange - Experiencia con ID nulo
        Experiencia experienciaSinId = Experiencia.builder()
                .id(null)
                .titulo("Proyecto Sin ID")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/proyecto-sin-id")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT))
                .imagenes(listaImagenes)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals(experienciaSinId.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, resultado.getImagenes().size(), "Debería tener 2 imágenes");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagenDto(any(Imagen.class));
    }

    @Test
    @DisplayName("toExperiencia - Debería manejar ExperienciaDto con ID nulo")
    void toExperiencia_ShouldHandleExperienciaDtoWithNullId() {
        // Arrange - ExperienciaDto con ID nulo
        ExperienciaDto experienciaDtoSinId = ExperienciaDto.builder()
                .id(null)
                .titulo("Proyecto Sin ID DTO")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/proyecto-sin-id-dto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.REACT))
                .imagenes(listaImagenesDto)
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals(experienciaDtoSinId.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, resultado.getImagenes().size(), "Debería tener 2 imágenes");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagen(any(ImagenDto.class));
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que ImagenMapper se usa correctamente en toExperienciaDto")
    void verifyImagenMapperUsedCorrectlyInToExperienciaDto() {
        // Act
        experienciaMapper.toExperienciaDto(experienciaValida);

        // Assert - Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagenDto(any(Imagen.class));
    }

    @Test
    @DisplayName("Verificar que ImagenMapper se usa correctamente en toExperiencia")
    void verifyImagenMapperUsedCorrectlyInToExperiencia() {
        // Act
        experienciaMapper.toExperiencia(experienciaDtoValido);

        // Assert - Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagen(any(ImagenDto.class));
    }

    @Test
    @DisplayName("Verificar que no hay interacciones no deseadas con ImagenMapper")
    void verifyNoUnexpectedInteractionsWithImagenMapper() {
        // Arrange - Experiencia sin imágenes
        Experiencia experienciaSinImagenes = Experiencia.builder()
                .id(11)
                .titulo("Sin Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(null)
                .usuario(null)
                .build();

        // Act
        experienciaMapper.toExperienciaDto(experienciaSinImagenes);

        // Assert - Verificar que no hay interacciones
        verify(imagenMapper, never()).toImagenDto(any());
        verify(imagenMapper, never()).toImagen(any());
    }

    // ==================== TESTS CON OBJETOS COMPLEJOS ====================

    @Test
    @DisplayName("toExperienciaDto - Debería mapear experiencia con imágenes completas")
    void toExperienciaDto_ShouldMapExperienciaWithFullImages() {
        // Arrange - Imágenes con todos los campos
        Imagen imagenCompleta1 = Imagen.builder()
                .id(20)
                .url("imagen-completa-1.jpg")
                .alt("Imagen completa 1 de proyecto")
                .build();

        Imagen imagenCompleta2 = Imagen.builder()
                .id(21)
                .url("imagen-completa-2.jpg")
                .alt("Imagen completa 2 de proyecto")
                .build();

        List<Imagen> imagenesCompletas = Arrays.asList(imagenCompleta1, imagenCompleta2);

        Experiencia experienciaCompleta = Experiencia.builder()
                .id(22)
                .titulo("Proyecto Completo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción completa del proyecto")
                .link("https://github.com/usuario/proyecto-completo")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiasUsadas(List.of(TecnologiaUsada.ANGULAR, TecnologiaUsada.TYPESCRIPT))
                .imagenes(imagenesCompletas)
                .usuario(usuario)
                .build();

        // Configurar mock para imágenes completas
        when(imagenMapper.toImagenDto(imagenCompleta1)).thenReturn(
                ImagenDto.builder()
                        .id(20)
                        .url("imagen-completa-1.jpg")
                        .alt("Imagen completa 1 de proyecto")
                        .build()
        );

        when(imagenMapper.toImagenDto(imagenCompleta2)).thenReturn(
                ImagenDto.builder()
                        .id(21)
                        .url("imagen-completa-2.jpg")
                        .alt("Imagen completa 2 de proyecto")
                        .build()
        );

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaCompleta);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, resultado.getImagenes().size(), "Debería tener 2 imágenes");
        assertEquals(20, resultado.getImagenes().get(0).getId(), "El ID de la primera imagen debería ser 20");
        assertEquals("imagen-completa-1.jpg", resultado.getImagenes().get(0).getUrl(),
                "La URL de la primera imagen debería coincidir");
        assertEquals(21, resultado.getImagenes().get(1).getId(), "El ID de la segunda imagen debería ser 21");
        assertEquals("imagen-completa-2.jpg", resultado.getImagenes().get(1).getUrl(),
                "La URL de la segunda imagen debería coincidir");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO, resultado.getTipoExperiencia(),
                "El tipo de experiencia debería ser TRABAJO_LABORAL_COLABORATIVO");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(2, resultado.getTecnologiasUsadas().size(), "Debería tener 2 tecnologías");
        assertTrue(resultado.getTecnologiasUsadas().containsAll(
                        List.of(TecnologiaUsada.ANGULAR, TecnologiaUsada.TYPESCRIPT)),
                "Las tecnologías usadas deberían ser ANGULAR y TYPESCRIPT");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagenDto(any(Imagen.class));
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear ExperienciaDto con imágenes completas")
    void toExperiencia_ShouldMapExperienciaDtoWithFullImages() {
        // Arrange - ImagenDto con todos los campos
        ImagenDto imagenDtoCompleta1 = ImagenDto.builder()
                .id(23)
                .url("imagen-completa-dto-1.jpg")
                .alt("Imagen DTO completa 1 de proyecto")
                .build();

        ImagenDto imagenDtoCompleta2 = ImagenDto.builder()
                .id(24)
                .url("imagen-completa-dto-2.jpg")
                .alt("Imagen DTO completa 2 de proyecto")
                .build();

        List<ImagenDto> imagenesDtoCompletas = Arrays.asList(imagenDtoCompleta1, imagenDtoCompleta2);

        ExperienciaDto experienciaDtoCompleta = ExperienciaDto.builder()
                .id(25)
                .titulo("Proyecto DTO Completo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción DTO completa del proyecto")
                .link("https://github.com/usuario/proyecto-dto-completo")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiasUsadas(List.of(TecnologiaUsada.VUE, TecnologiaUsada.TAILWIND))
                .imagenes(imagenesDtoCompletas)
                .build();

        // Configurar mock para imagenDto completa
        when(imagenMapper.toImagen(imagenDtoCompleta1)).thenReturn(
                Imagen.builder()
                        .id(23)
                        .url("imagen-completa-dto-1.jpg")
                        .alt("Imagen DTO completa 1 de proyecto")
                        .build()
        );

        when(imagenMapper.toImagen(imagenDtoCompleta2)).thenReturn(
                Imagen.builder()
                        .id(24)
                        .url("imagen-completa-dto-2.jpg")
                        .alt("Imagen DTO completa 2 de proyecto")
                        .build()
        );

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoCompleta);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, resultado.getImagenes().size(), "Debería tener 2 imágenes");
        assertEquals(23, resultado.getImagenes().get(0).getId(), "El ID de la primera imagen debería ser 23");
        assertEquals("imagen-completa-dto-1.jpg", resultado.getImagenes().get(0).getUrl(),
                "La URL de la primera imagen debería coincidir");
        assertEquals(24, resultado.getImagenes().get(1).getId(), "El ID de la segunda imagen debería ser 24");
        assertEquals("imagen-completa-dto-2.jpg", resultado.getImagenes().get(1).getUrl(),
                "La URL de la segunda imagen debería coincidir");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE, resultado.getTipoExperiencia(),
                "El tipo de experiencia debería ser TRABAJO_LABORAL_FREELANCE");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(2, resultado.getTecnologiasUsadas().size(), "Debería tener 2 tecnologías");
        assertTrue(resultado.getTecnologiasUsadas().containsAll(
                        List.of(TecnologiaUsada.VUE, TecnologiaUsada.TAILWIND)),
                "Las tecnologías usadas deberían ser VUE y TAILWIND");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagen(any(ImagenDto.class));
    }

    @Test
    @DisplayName("toExperienciaDto - Debería manejar experiencia con fechas en el pasado")
    void toExperienciaDto_ShouldHandleExperienciaWithPastDates() {
        // Arrange - Experiencia con fechas en el pasado
        Experiencia experienciaPasada = Experiencia.builder()
                .id(26)
                .titulo("Proyecto del Pasado")
                .fechaInicioProyecto(LocalDate.of(2020, 1, 1))
                .fechaFinProyecto(LocalDate.of(2021, 12, 31))
                .descripcion("Descripción del proyecto pasado")
                .link("https://github.com/usuario/proyecto-pasado")
                .tipoExperiencia(TipoExperiencia.PRACTICA_PROFESIONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.POSTGRESQL, TecnologiaUsada.JAVA))
                .imagenes(listaImagenes)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaPasada);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(LocalDate.of(2020, 1, 1), resultado.getFechaInicioProyecto(),
                "La fecha de inicio debería ser 2020-01-01");
        assertEquals(LocalDate.of(2021, 12, 31), resultado.getFechaFinProyecto(),
                "La fecha de fin debería ser 2021-12-31");
        assertEquals(TipoExperiencia.PRACTICA_PROFESIONAL, resultado.getTipoExperiencia(),
                "El tipo de experiencia debería ser PRACTICA_PROFESIONAL");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(2, resultado.getTecnologiasUsadas().size(), "Debería tener 2 tecnologías");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, resultado.getImagenes().size(), "Debería tener 2 imágenes");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagenDto(any(Imagen.class));
    }

    @Test
    @DisplayName("toExperiencia - Debería manejar ExperienciaDto con fechas en el futuro")
    void toExperiencia_ShouldHandleExperienciaDtoWithFutureDates() {
        // Arrange - ExperienciaDto con fechas en el futuro
        ExperienciaDto experienciaDtoFuturo = ExperienciaDto.builder()
                .id(27)
                .titulo("Proyecto del Futuro")
                .fechaInicioProyecto(LocalDate.of(2025, 1, 1))
                .fechaFinProyecto(LocalDate.of(2026, 12, 31))
                .descripcion("Descripción del proyecto futuro")
                .link("https://github.com/usuario/proyecto-futuro")
                .tipoExperiencia(TipoExperiencia.APORTE_CODIGO_ABIERTO)
                .tecnologiasUsadas(List.of(TecnologiaUsada.MONGODB, TecnologiaUsada.VUE))
                .imagenes(listaImagenesDto)
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoFuturo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(LocalDate.of(2025, 1, 1), resultado.getFechaInicioProyecto(),
                "La fecha de inicio debería ser 2025-01-01");
        assertEquals(LocalDate.of(2026, 12, 31), resultado.getFechaFinProyecto(),
                "La fecha de fin debería ser 2026-12-31");
        assertEquals(TipoExperiencia.APORTE_CODIGO_ABIERTO, resultado.getTipoExperiencia(),
                "El tipo de experiencia debería ser APORTE_CODIGO_ABIERTO");
        assertNotNull(resultado.getTecnologiasUsadas(), "Las tecnologías usadas no deberían ser nulas");
        assertEquals(2, resultado.getTecnologiasUsadas().size(), "Debería tener 2 tecnologías");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, resultado.getImagenes().size(), "Debería tener 2 imágenes");

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagen(any(ImagenDto.class));
    }

    @Test
    @DisplayName("toExperienciaDto - Debería manejar experiencia con imágenes que tienen campos nulos")
    void toExperienciaDto_ShouldHandleExperienciaWithImagesHavingNullFields() {
        // Arrange - Imágenes con campos nulos
        Imagen imagenConNull = Imagen.builder()
                .id(null)
                .url(null)
                .alt(null)
                .build();

        Imagen imagenConUrlNula = Imagen.builder()
                .id(30)
                .url(null)
                .alt("Alt válido")
                .build();

        List<Imagen> imagenesConNull = Arrays.asList(imagenConNull, imagenConUrlNula);

        Experiencia experienciaConImagenesNull = Experiencia.builder()
                .id(31)
                .titulo("Proyecto con imágenes nulas")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .imagenes(imagenesConNull)
                .usuario(null)
                .build();

        // Configurar mock para imágenes con campos nulos
        when(imagenMapper.toImagenDto(any(Imagen.class))).thenAnswer(invocation -> {
            Imagen img = invocation.getArgument(0);
            if (img == null) return null;
            return ImagenDto.builder()
                    .id(img.getId())
                    .url(img.getUrl())
                    .alt(img.getAlt())
                    .build();
        });

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaConImagenesNull);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getImagenes(), "La lista de imágenes no debería ser nula");
        assertEquals(2, resultado.getImagenes().size(), "Debería tener 2 imágenes");
        assertNull(resultado.getImagenes().get(0).getId(), "El ID de la primera imagen debería ser nulo");
        assertNull(resultado.getImagenes().get(0).getUrl(), "La URL de la primera imagen debería ser nula");
        assertNull(resultado.getImagenes().get(0).getAlt(), "El alt de la primera imagen debería ser nulo");
        assertEquals(30, resultado.getImagenes().get(1).getId(), "El ID de la segunda imagen debería ser 30");
        assertNull(resultado.getImagenes().get(1).getUrl(), "La URL de la segunda imagen debería ser nula");
        assertEquals("Alt válido", resultado.getImagenes().get(1).getAlt());

        // Verificar que se llamó a ImagenMapper para cada imagen
        verify(imagenMapper, times(2)).toImagenDto(any(Imagen.class));
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        ExperienciaMapper mapper = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            mapper.toExperienciaDto(null);
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }
}