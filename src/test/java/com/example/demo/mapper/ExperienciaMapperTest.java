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
                .username("juan@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack")
                .descripcion("Apasionado por la tecnología")
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

        // Configurar comportamiento del mock de ImagenMapper
        when(imagenMapper.toImagenDto(any(Imagen.class))).thenAnswer(invocation -> {
            Imagen img = invocation.getArgument(0);
            if (img == null) return null;
            return ImagenDto.builder()
                    .id(img.getId())
                    .url(img.getUrl())
                    .alt(img.getAlt())
                    .build();
        });

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
        assertEquals(experienciaValida.getTecnologiaUsada(), resultado.getTecnologiaUsada(),
                "La tecnología usada debería coincidir");

        // Verificar que se llamó a ImagenMapper para la imagen
        verify(imagenMapper, times(1)).toImagenDto(experienciaValida.getImagen());
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear Experiencia sin imagen a ExperienciaDto")
    void toExperienciaDto_ShouldMapExperienciaWithoutImageToExperienciaDto() {
        // Arrange - Experiencia sin imagen
        Experiencia experienciaSinImagen = Experiencia.builder()
                .id(2)
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaSinImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaSinImagen.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(experienciaSinImagen.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");

        // Verificar que no se llamó a ImagenMapper para imagen nula
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear Experiencia con fechaFinProyecto nula")
    void toExperienciaDto_ShouldMapExperienciaWithNullFechaFinProyecto() {
        // Arrange - Experiencia con fecha de fin nula (proyecto en curso)
        Experiencia experienciaEnCurso = Experiencia.builder()
                .id(3)
                .titulo("Proyecto en Curso")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(imagen)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaEnCurso);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaEnCurso.getId(), resultado.getId(), "El ID debería coincidir");
        assertNull(resultado.getFechaFinProyecto(), "La fecha de fin debería ser nula");

        // Verificar que se llamó a ImagenMapper para la imagen
        verify(imagenMapper, times(1)).toImagenDto(experienciaEnCurso.getImagen());
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear Experiencia con usuario nulo")
    void toExperienciaDto_ShouldMapExperienciaWithNullUsuario() {
        // Arrange - Experiencia sin usuario
        Experiencia experienciaSinUsuario = Experiencia.builder()
                .id(4)
                .titulo("Proyecto Independiente")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-independiente")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVASCRIPT)
                .imagen(imagen)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaSinUsuario);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaSinUsuario.getId(), resultado.getId(), "El ID debería coincidir");
        assertNotNull(resultado.getImagen(), "La imagen no debería ser nula");

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagenDto(experienciaSinUsuario.getImagen());
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
        assertEquals(experienciaDtoValido.getTecnologiaUsada(), resultado.getTecnologiaUsada(),
                "La tecnología usada debería coincidir");

        // Verificar que se llamó a ImagenMapper para la imagen
        verify(imagenMapper, times(1)).toImagen(experienciaDtoValido.getImagen());
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear ExperienciaDto sin imagen a Experiencia")
    void toExperiencia_ShouldMapExperienciaDtoWithoutImageToExperiencia() {
        // Arrange - ExperienciaDto sin imagen
        ExperienciaDto experienciaDtoSinImagen = ExperienciaDto.builder()
                .id(5)
                .titulo("Proyecto sin Imagen DTO")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen-dto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.REACT)
                .imagen(null)
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoSinImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaDtoSinImagen.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(experienciaDtoSinImagen.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");

        // Verificar que no se llamó a ImagenMapper para imagen nula
        verify(imagenMapper, never()).toImagen(any());
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear ExperienciaDto con fechaFinProyecto nula")
    void toExperiencia_ShouldMapExperienciaDtoWithNullFechaFinProyecto() {
        // Arrange - ExperienciaDto con fecha de fin nula
        ExperienciaDto experienciaDtoEnCurso = ExperienciaDto.builder()
                .id(6)
                .titulo("Proyecto en Curso DTO")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null)
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso-dto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.DJANGO)
                .imagen(imagenDto)
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoEnCurso);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(experienciaDtoEnCurso.getId(), resultado.getId(), "El ID debería coincidir");
        assertNull(resultado.getFechaFinProyecto(), "La fecha de fin debería ser nula");

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagen(experienciaDtoEnCurso.getImagen());
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
        assertEquals(experienciaValida.getTitulo(), experienciaConvertida.getTitulo(), "El título debería ser el mismo");
        assertEquals(experienciaValida.getFechaInicioProyecto(), experienciaConvertida.getFechaInicioProyecto(),
                "La fecha de inicio debería ser la misma");
        assertEquals(experienciaValida.getFechaFinProyecto(), experienciaConvertida.getFechaFinProyecto(),
                "La fecha de fin debería ser la misma");
        assertEquals(experienciaValida.getDescripcion(), experienciaConvertida.getDescripcion(),
                "La descripción debería ser la misma");
        assertEquals(experienciaValida.getLink(), experienciaConvertida.getLink(), "El link debería ser el mismo");
        assertEquals(experienciaValida.getTipoExperiencia(), experienciaConvertida.getTipoExperiencia(),
                "El tipo de experiencia debería ser el mismo");
        assertEquals(experienciaValida.getTecnologiaUsada(), experienciaConvertida.getTecnologiaUsada(),
                "La tecnología usada debería ser la misma");

        // Verificar que la imagen se mapeó correctamente en ambos sentidos
        assertNotNull(experienciaConvertida.getImagen(), "La imagen no debería ser nula");
        assertEquals(experienciaValida.getImagen().getId(), experienciaConvertida.getImagen().getId(),
                "El ID de la imagen debería coincidir");
        assertEquals(experienciaValida.getImagen().getUrl(), experienciaConvertida.getImagen().getUrl(),
                "La URL de la imagen debería coincidir");
        assertEquals(experienciaValida.getImagen().getAlt(), experienciaConvertida.getImagen().getAlt(),
                "El alt de la imagen debería coincidir");
    }

    @Test
    @DisplayName("toExperienciaDto y toExperiencia - Deberían ser consistentes con objetos nulos")
    void toExperienciaDtoAndToExperiencia_ShouldBeConsistentWithNullObjects() {
        // Arrange - ExperienciaDto sin imagen
        ExperienciaDto dtoSinImagen = ExperienciaDto.builder()
                .id(10)
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(null)
                .build();

        // Act - Convertir a Experiencia y luego de vuelta a ExperienciaDto
        Experiencia experiencia = experienciaMapper.toExperiencia(dtoSinImagen);
        ExperienciaDto dtoConvertido = experienciaMapper.toExperienciaDto(experiencia);

        // Assert
        assertNotNull(experiencia, "La experiencia no debería ser nula");
        assertNotNull(dtoConvertido, "El DTO convertido no debería ser nulo");
        assertEquals(dtoSinImagen.getId(), dtoConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(dtoSinImagen.getTitulo(), dtoConvertido.getTitulo(), "El título debería ser el mismo");
        assertNull(dtoConvertido.getImagen(), "La imagen debería ser nula");
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
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(null)
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
        assertEquals(experienciaEnCurso.getTitulo(), experienciaConvertida.getTitulo(), "El título debería ser el mismo");
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
                    .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                    .imagen(imagen)
                    .usuario(null)
                    .build();

            // Act
            ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experiencia);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, resultado.getTipoExperiencia(), "El tipo de experiencia debería coincidir para: " + tipo);
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
                    .tecnologiaUsada(TecnologiaUsada.REACT)
                    .imagen(imagenDto)
                    .build();

            // Act
            Experiencia resultado = experienciaMapper.toExperiencia(dto);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, resultado.getTipoExperiencia(), "El tipo de experiencia debería coincidir para: " + tipo);
        }
    }

    @Test
    @DisplayName("toExperienciaDto - Debería mapear correctamente todas las tecnologías")
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
                    .tecnologiaUsada(tecnologia)
                    .imagen(imagen)
                    .usuario(null)
                    .build();

            // Act
            ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experiencia);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tecnología: " + tecnologia);
            assertEquals(tecnologia, resultado.getTecnologiaUsada(),
                    "La tecnología usada debería coincidir para: " + tecnologia);
        }
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear correctamente todas las tecnologías")
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
                    .tecnologiaUsada(tecnologia)
                    .imagen(imagenDto)
                    .build();

            // Act
            Experiencia resultado = experienciaMapper.toExperiencia(dto);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para tecnología: " + tecnologia);
            assertEquals(tecnologia, resultado.getTecnologiaUsada(),
                    "La tecnología usada debería coincidir para: " + tecnologia);
        }
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("toExperienciaDto - Debería manejar experiencia con campos vacíos")
    void toExperienciaDto_ShouldHandleExperienciaWithEmptyFields() {
        // Arrange - Experiencia con campos vacíos
        Experiencia experienciaVacia = Experiencia.builder()
                .id(7)
                .titulo("")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("")
                .link("")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaVacia);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getTitulo(), "El título vacío debería mantenerse");
        assertEquals("", resultado.getDescripcion(), "La descripción vacía debería mantenerse");
        assertEquals("", resultado.getLink(), "El link vacío debería mantenerse");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("toExperiencia - Debería manejar ExperienciaDto con campos vacíos")
    void toExperiencia_ShouldHandleExperienciaDtoWithEmptyFields() {
        // Arrange - ExperienciaDto con campos vacíos
        ExperienciaDto experienciaDtoVacio = ExperienciaDto.builder()
                .id(8)
                .titulo("")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("")
                .link("")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoVacio);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getTitulo(), "El título vacío debería mantenerse");
        assertEquals("", resultado.getDescripcion(), "La descripción vacía debería mantenerse");
        assertEquals("", resultado.getLink(), "El link vacío debería mantenerse");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");

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
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(null)
                .build();

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals(experienciaSinId.getTitulo(), resultado.getTitulo(), "El título debería coincidir");

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagenDto(experienciaSinId.getImagen());
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
                .tecnologiaUsada(TecnologiaUsada.REACT)
                .imagen(imagenDto)
                .build();

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals(experienciaDtoSinId.getTitulo(), resultado.getTitulo(), "El título debería coincidir");

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagen(experienciaDtoSinId.getImagen());
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que ImagenMapper se usa correctamente en toExperienciaDto")
    void verifyImagenMapperUsedCorrectlyInToExperienciaDto() {
        // Act
        experienciaMapper.toExperienciaDto(experienciaValida);

        // Assert - Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagenDto(experienciaValida.getImagen());
    }

    @Test
    @DisplayName("Verificar que ImagenMapper se usa correctamente en toExperiencia")
    void verifyImagenMapperUsedCorrectlyInToExperiencia() {
        // Act
        experienciaMapper.toExperiencia(experienciaDtoValido);

        // Assert - Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagen(experienciaDtoValido.getImagen());
    }

    @Test
    @DisplayName("Verificar que no hay interacciones no deseadas con ImagenMapper")
    void verifyNoUnexpectedInteractionsWithImagenMapper() {
        // Arrange - Experiencia sin imagen
        Experiencia experienciaSinImagen = Experiencia.builder()
                .id(9)
                .titulo("Sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida")
                .link("https://github.com/usuario/sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .usuario(null)
                .build();

        // Act
        experienciaMapper.toExperienciaDto(experienciaSinImagen);

        // Assert - Verificar que no hay interacciones
        verify(imagenMapper, never()).toImagenDto(any());
        verify(imagenMapper, never()).toImagen(any());
    }

    // ==================== TESTS CON OBJETOS COMPLEJOS ====================

    @Test
    @DisplayName("toExperienciaDto - Debería mapear experiencia con imagen completa")
    void toExperienciaDto_ShouldMapExperienciaWithFullImage() {
        // Arrange - Imagen con todos los campos
        Imagen imagenCompleta = Imagen.builder()
                .id(20)
                .url("imagen-completa.jpg")
                .alt("Imagen completa de proyecto")
                .build();

        Experiencia experienciaCompleta = Experiencia.builder()
                .id(21)
                .titulo("Proyecto Completo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción completa del proyecto")
                .link("https://github.com/usuario/proyecto-completo")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiaUsada(TecnologiaUsada.ANGULAR)
                .imagen(imagenCompleta)
                .usuario(usuario)
                .build();

        // Configurar mock para imagen completa
        when(imagenMapper.toImagenDto(imagenCompleta)).thenReturn(
                ImagenDto.builder()
                        .id(20)
                        .url("imagen-completa.jpg")
                        .alt("Imagen completa de proyecto")
                        .build()
        );

        // Act
        ExperienciaDto resultado = experienciaMapper.toExperienciaDto(experienciaCompleta);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getImagen(), "La imagen no debería ser nula");
        assertEquals(20, resultado.getImagen().getId(), "El ID de la imagen debería ser 20");
        assertEquals("imagen-completa.jpg", resultado.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Imagen completa de proyecto", resultado.getImagen().getAlt(),
                "El alt de la imagen debería coincidir");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO, resultado.getTipoExperiencia(),
                "El tipo de experiencia debería ser TRABAJO_LABORAL_COLABORATIVO");
        assertEquals(TecnologiaUsada.ANGULAR, resultado.getTecnologiaUsada(),
                "La tecnología usada debería ser ANGULAR");

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagenDto(imagenCompleta);
    }

    @Test
    @DisplayName("toExperiencia - Debería mapear ExperienciaDto con imagen completa")
    void toExperiencia_ShouldMapExperienciaDtoWithFullImage() {
        // Arrange - ImagenDto con todos los campos
        ImagenDto imagenDtoCompleta = ImagenDto.builder()
                .id(22)
                .url("imagen-completa-dto.jpg")
                .alt("Imagen DTO completa de proyecto")
                .build();

        ExperienciaDto experienciaDtoCompleta = ExperienciaDto.builder()
                .id(23)
                .titulo("Proyecto DTO Completo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 12, 31))
                .descripcion("Descripción DTO completa del proyecto")
                .link("https://github.com/usuario/proyecto-dto-completo")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE)
                .tecnologiaUsada(TecnologiaUsada.VUE)
                .imagen(imagenDtoCompleta)
                .build();

        // Configurar mock para imagenDto completa
        when(imagenMapper.toImagen(imagenDtoCompleta)).thenReturn(
                Imagen.builder()
                        .id(22)
                        .url("imagen-completa-dto.jpg")
                        .alt("Imagen DTO completa de proyecto")
                        .build()
        );

        // Act
        Experiencia resultado = experienciaMapper.toExperiencia(experienciaDtoCompleta);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getImagen(), "La imagen no debería ser nula");
        assertEquals(22, resultado.getImagen().getId(), "El ID de la imagen debería ser 22");
        assertEquals("imagen-completa-dto.jpg", resultado.getImagen().getUrl(),
                "La URL de la imagen debería coincidir");
        assertEquals("Imagen DTO completa de proyecto", resultado.getImagen().getAlt(),
                "El alt de la imagen debería coincidir");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE, resultado.getTipoExperiencia(),
                "El tipo de experiencia debería ser TRABAJO_LABORAL_FREELANCE");
        assertEquals(TecnologiaUsada.VUE, resultado.getTecnologiaUsada(),
                "La tecnología usada debería ser VUE");

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagen(imagenDtoCompleta);
    }

    @Test
    @DisplayName("toExperienciaDto - Debería manejar experiencia con fechas en el pasado")
    void toExperienciaDto_ShouldHandleExperienciaWithPastDates() {
        // Arrange - Experiencia con fechas en el pasado
        Experiencia experienciaPasada = Experiencia.builder()
                .id(24)
                .titulo("Proyecto del Pasado")
                .fechaInicioProyecto(LocalDate.of(2020, 1, 1))
                .fechaFinProyecto(LocalDate.of(2021, 12, 31))
                .descripcion("Descripción del proyecto pasado")
                .link("https://github.com/usuario/proyecto-pasado")
                .tipoExperiencia(TipoExperiencia.PRACTICA_PROFESIONAL)
                .tecnologiaUsada(TecnologiaUsada.POSTGRESQL)
                .imagen(imagen)
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

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagenDto(experienciaPasada.getImagen());
    }

    @Test
    @DisplayName("toExperiencia - Debería manejar ExperienciaDto con fechas en el futuro")
    void toExperiencia_ShouldHandleExperienciaDtoWithFutureDates() {
        // Arrange - ExperienciaDto con fechas en el futuro
        ExperienciaDto experienciaDtoFuturo = ExperienciaDto.builder()
                .id(25)
                .titulo("Proyecto del Futuro")
                .fechaInicioProyecto(LocalDate.of(2025, 1, 1))
                .fechaFinProyecto(LocalDate.of(2026, 12, 31))
                .descripcion("Descripción del proyecto futuro")
                .link("https://github.com/usuario/proyecto-futuro")
                .tipoExperiencia(TipoExperiencia.APORTE_CODIGO_ABIERTO)
                .tecnologiaUsada(TecnologiaUsada.MONGODB)
                .imagen(imagenDto)
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

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagen(experienciaDtoFuturo.getImagen());
    }
}