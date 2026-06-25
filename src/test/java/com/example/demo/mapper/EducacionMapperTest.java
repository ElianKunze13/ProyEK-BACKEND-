package com.example.demo.mapper;

import com.example.demo.dto.EducacionDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TipoEducacion;
import com.example.demo.model.Educacion;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase EducacionMapper
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class EducacionMapperTest {

    @Mock
    private ImagenMapper imagenMapper;

    @InjectMocks
    private EducacionMapperImpl educacionMapper;

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
                .username("juan@email.com")
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
    @DisplayName("toEducacionDto - Debería mapear Educacion a EducacionDto correctamente")
    void toEducacionDto_ShouldMapEducacionToEducacionDtoCorrectly() {
        // Act
        EducacionDto resultado = educacionMapper.toEducacionDto(educacionValida);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(educacionValida.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(educacionValida.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertEquals(educacionValida.getFechaObtencion(), resultado.getFechaObtencion(), "La fecha de obtención debería coincidir");
        assertEquals(educacionValida.getDescripcion(), resultado.getDescripcion(), "La descripción debería coincidir");
        assertEquals(educacionValida.getTipoEducacion(), resultado.getTipoEducacion(), "El tipo de educación debería coincidir");

        // Verificar que se llamó a ImagenMapper para la imagen
        verify(imagenMapper, times(1)).toImagenDto(educacionValida.getImagen());
    }

    @Test
    @DisplayName("toEducacionDto - Debería mapear Educacion con imagen nula a EducacionDto")
    void toEducacionDto_ShouldMapEducacionWithNullImageToEducacionDto() {
        // Arrange - Educación sin imagen
        Educacion educacionSinImagen = Educacion.builder()
                .id(2)
                .titulo("Máster en Inteligencia Artificial")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Máster oficial en IA y Machine Learning")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Act
        EducacionDto resultado = educacionMapper.toEducacionDto(educacionSinImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(educacionSinImagen.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(educacionSinImagen.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("toEducacionDto - Debería mapear Educacion con todos los tipos de educación")
    @ParameterizedTest
    @EnumSource(TipoEducacion.class)
    void toEducacionDto_ShouldMapEducacionWithAllTipoEducacion(TipoEducacion tipo) {
        // Arrange
        Educacion educacion = Educacion.builder()
                .id(3)
                .titulo("Educación " + tipo.name())
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción para " + tipo.name())
                .tipoEducacion(tipo)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act
        EducacionDto resultado = educacionMapper.toEducacionDto(educacion);

        // Assert
        assertNotNull(resultado);
        assertEquals(tipo, resultado.getTipoEducacion(), "El tipo de educación debería coincidir");
    }

    @Test
    @DisplayName("toEducacionDto - Debería manejar Educacion nula")
    void toEducacionDto_ShouldHandleNullEducacion() {
        // Act
        EducacionDto resultado = educacionMapper.toEducacionDto(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando la educación es nula");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagenDto(any());
    }

    // ==================== TESTS TO ENTITY ====================

    @Test
    @DisplayName("toEducacion - Debería mapear EducacionDto a Educacion correctamente")
    void toEducacion_ShouldMapEducacionDtoToEducacionCorrectly() {
        // Act
        Educacion resultado = educacionMapper.toEducacion(educacionDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(educacionDtoValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(educacionDtoValido.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertEquals(educacionDtoValido.getFechaObtencion(), resultado.getFechaObtencion(), "La fecha de obtención debería coincidir");
        assertEquals(educacionDtoValido.getDescripcion(), resultado.getDescripcion(), "La descripción debería coincidir");
        assertEquals(educacionDtoValido.getTipoEducacion(), resultado.getTipoEducacion(), "El tipo de educación debería coincidir");

        // Verificar que se llamó a ImagenMapper para la imagen
        verify(imagenMapper, times(1)).toImagen(educacionDtoValido.getImagen());
    }

    @Test
    @DisplayName("toEducacion - Debería mapear EducacionDto con imagen nula a Educacion")
    void toEducacion_ShouldMapEducacionDtoWithNullImageToEducacion() {
        // Arrange - EducacionDto sin imagen
        EducacionDto educacionDtoSinImagen = EducacionDto.builder()
                .id(2)
                .titulo("Máster en Inteligencia Artificial")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Máster oficial en IA y Machine Learning")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(null)
                .build();

        // Act
        Educacion resultado = educacionMapper.toEducacion(educacionDtoSinImagen);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(educacionDtoSinImagen.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(educacionDtoSinImagen.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagen(any());
    }

    @Test
    @DisplayName("toEducacion - Debería mapear EducacionDto con todos los tipos de educación")
    @ParameterizedTest
    @EnumSource(TipoEducacion.class)
    void toEducacion_ShouldMapEducacionDtoWithAllTipoEducacion(TipoEducacion tipo) {
        // Arrange
        EducacionDto educacionDto = EducacionDto.builder()
                .id(3)
                .titulo("Educación " + tipo.name())
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción para " + tipo.name())
                .tipoEducacion(tipo)
                .imagen(imagenDto)
                .build();

        // Act
        Educacion resultado = educacionMapper.toEducacion(educacionDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(tipo, resultado.getTipoEducacion(), "El tipo de educación debería coincidir");
    }

    @Test
    @DisplayName("toEducacion - Debería manejar EducacionDto nulo")
    void toEducacion_ShouldHandleNullEducacionDto() {
        // Act
        Educacion resultado = educacionMapper.toEducacion(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando el EducacionDto es nulo");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagen(any());
    }

    // ==================== TESTS DE CONSISTENCIA ====================

    @Test
    @DisplayName("toEducacionDto y toEducacion - Deberían ser consistentes (round-trip)")
    void toEducacionDtoAndToEducacion_ShouldBeConsistent() {
        // Act - Convertir de Educacion a EducacionDto y luego de vuelta a Educacion
        EducacionDto dto = educacionMapper.toEducacionDto(educacionValida);
        Educacion educacionConvertida = educacionMapper.toEducacion(dto);

        // Assert - Verificar que el round-trip mantiene los datos
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(educacionConvertida, "La educación convertida no debería ser nula");
        assertEquals(educacionValida.getId(), educacionConvertida.getId(), "El ID debería ser el mismo");
        assertEquals(educacionValida.getTitulo(), educacionConvertida.getTitulo(), "El título debería ser el mismo");
        assertEquals(educacionValida.getFechaObtencion(), educacionConvertida.getFechaObtencion(),
                "La fecha de obtención debería ser la misma");
        assertEquals(educacionValida.getDescripcion(), educacionConvertida.getDescripcion(),
                "La descripción debería ser la misma");
        assertEquals(educacionValida.getTipoEducacion(), educacionConvertida.getTipoEducacion(),
                "El tipo de educación debería ser el mismo");

        // Verificar que las imágenes se mapearon correctamente en ambos sentidos
        assertNotNull(educacionConvertida.getImagen(), "La imagen no debería ser nula");
        assertEquals(educacionValida.getImagen().getId(), educacionConvertida.getImagen().getId(),
                "El ID de la imagen debería coincidir");
        assertEquals(educacionValida.getImagen().getUrl(), educacionConvertida.getImagen().getUrl(),
                "La URL de la imagen debería coincidir");
        assertEquals(educacionValida.getImagen().getAlt(), educacionConvertida.getImagen().getAlt(),
                "El alt de la imagen debería coincidir");
    }

    @Test
    @DisplayName("toEducacionDto y toEducacion - Deberían ser consistentes con objetos nulos")
    void toEducacionDtoAndToEducacion_ShouldBeConsistentWithNullObjects() {
        // Arrange - EducacionDto sin imagen
        EducacionDto dtoSinImagen = EducacionDto.builder()
                .id(10)
                .titulo("Educación Test")
                .fechaObtencion(LocalDate.of(2023, 1, 1))
                .descripcion("Descripción de prueba")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .imagen(null)
                .build();

        // Act - Convertir a Educacion y luego de vuelta a EducacionDto
        Educacion educacion = educacionMapper.toEducacion(dtoSinImagen);
        EducacionDto dtoConvertido = educacionMapper.toEducacionDto(educacion);

        // Assert
        assertNotNull(educacion, "La educación no debería ser nula");
        assertNotNull(dtoConvertido, "El DTO convertido no debería ser nulo");
        assertEquals(dtoSinImagen.getId(), dtoConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(dtoSinImagen.getTitulo(), dtoConvertido.getTitulo(), "El título debería ser el mismo");
        assertNull(dtoConvertido.getImagen(), "La imagen debería ser nula");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("toEducacionDto - Debería manejar educación con campos vacíos")
    void toEducacionDto_ShouldHandleEducacionWithEmptyFields() {
        // Arrange - Educación con campos vacíos
        Educacion educacionVacia = Educacion.builder()
                .id(4)
                .titulo("")
                .fechaObtencion(LocalDate.now())
                .descripcion("")
                .tipoEducacion(TipoEducacion.OTROS)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Act
        EducacionDto resultado = educacionMapper.toEducacionDto(educacionVacia);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getTitulo(), "El título vacío debería mantenerse");
        assertEquals("", resultado.getDescripcion(), "La descripción vacía debería mantenerse");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");
    }

    @Test
    @DisplayName("toEducacion - Debería manejar EducacionDto con campos vacíos")
    void toEducacion_ShouldHandleEducacionDtoWithEmptyFields() {
        // Arrange - EducacionDto con campos vacíos
        EducacionDto educacionDtoVacia = EducacionDto.builder()
                .id(5)
                .titulo("")
                .fechaObtencion(LocalDate.now())
                .descripcion("")
                .tipoEducacion(TipoEducacion.OTROS)
                .imagen(null)
                .build();

        // Act
        Educacion resultado = educacionMapper.toEducacion(educacionDtoVacia);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getTitulo(), "El título vacío debería mantenerse");
        assertEquals("", resultado.getDescripcion(), "La descripción vacía debería mantenerse");
        assertNull(resultado.getImagen(), "La imagen debería ser nula");
    }

    @Test
    @DisplayName("toEducacionDto - Debería manejar educación con ID nulo")
    void toEducacionDto_ShouldHandleEducacionWithNullId() {
        // Arrange - Educación con ID nulo
        Educacion educacionSinId = Educacion.builder()
                .id(null)
                .titulo("Educación Sin ID")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act
        EducacionDto resultado = educacionMapper.toEducacionDto(educacionSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals(educacionSinId.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
    }

    @Test
    @DisplayName("toEducacion - Debería manejar EducacionDto con ID nulo")
    void toEducacion_ShouldHandleEducacionDtoWithNullId() {
        // Arrange - EducacionDto con ID nulo
        EducacionDto educacionDtoSinId = EducacionDto.builder()
                .id(null)
                .titulo("Educación Sin ID")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagenDto)
                .build();

        // Act
        Educacion resultado = educacionMapper.toEducacion(educacionDtoSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals(educacionDtoSinId.getTitulo(), resultado.getTitulo(), "El título debería coincidir");
    }

    @Test
    @DisplayName("toEducacionDto - Debería manejar fechas extremas")
    void toEducacionDto_ShouldHandleExtremeDates() {
        // Arrange - Fechas extremas
        LocalDate fechaAntigua = LocalDate.of(1900, 1, 1);
        LocalDate fechaFutura = LocalDate.of(2100, 12, 31);

        Educacion educacionAntigua = Educacion.builder()
                .id(6)
                .titulo("Educación Antigua")
                .fechaObtencion(fechaAntigua)
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        Educacion educacionFutura = Educacion.builder()
                .id(7)
                .titulo("Educación Futura")
                .fechaObtencion(fechaFutura)
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.OTROS)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act
        EducacionDto resultadoAntiguo = educacionMapper.toEducacionDto(educacionAntigua);
        EducacionDto resultadoFuturo = educacionMapper.toEducacionDto(educacionFutura);

        // Assert
        assertNotNull(resultadoAntiguo);
        assertEquals(fechaAntigua, resultadoAntiguo.getFechaObtencion(), "La fecha antigua debería coincidir");
        assertNotNull(resultadoFuturo);
        assertEquals(fechaFutura, resultadoFuturo.getFechaObtencion(), "La fecha futura debería coincidir");
    }

    @Test
    @DisplayName("toEducacion - Debería manejar fechas extremas")
    void toEducacion_ShouldHandleExtremeDates() {
        // Arrange - Fechas extremas
        LocalDate fechaAntigua = LocalDate.of(1900, 1, 1);
        LocalDate fechaFutura = LocalDate.of(2100, 12, 31);

        EducacionDto dtoAntiguo = EducacionDto.builder()
                .id(8)
                .titulo("Educación Antigua DTO")
                .fechaObtencion(fechaAntigua)
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagenDto)
                .build();

        EducacionDto dtoFuturo = EducacionDto.builder()
                .id(9)
                .titulo("Educación Futura DTO")
                .fechaObtencion(fechaFutura)
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.OTROS)
                .imagen(imagenDto)
                .build();

        // Act
        Educacion resultadoAntiguo = educacionMapper.toEducacion(dtoAntiguo);
        Educacion resultadoFuturo = educacionMapper.toEducacion(dtoFuturo);

        // Assert
        assertNotNull(resultadoAntiguo);
        assertEquals(fechaAntigua, resultadoAntiguo.getFechaObtencion(), "La fecha antigua debería coincidir");
        assertNotNull(resultadoFuturo);
        assertEquals(fechaFutura, resultadoFuturo.getFechaObtencion(), "La fecha futura debería coincidir");
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que ImagenMapper se usa correctamente en toEducacionDto")
    void verifyImagenMapperUsedCorrectlyInToEducacionDto() {
        // Act
        educacionMapper.toEducacionDto(educacionValida);

        // Assert - Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagenDto(educacionValida.getImagen());
    }

    @Test
    @DisplayName("Verificar que ImagenMapper se usa correctamente en toEducacion")
    void verifyImagenMapperUsedCorrectlyInToEducacion() {
        // Act
        educacionMapper.toEducacion(educacionDtoValido);

        // Assert - Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagen(educacionDtoValido.getImagen());
    }

    @Test
    @DisplayName("Verificar que no hay interacciones no deseadas con ImagenMapper")
    void verifyNoUnexpectedInteractionsWithImagenMapper() {
        // Arrange - Educación sin imagen
        Educacion educacionSinImagen = Educacion.builder()
                .id(10)
                .titulo("Sin Imagen")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Act
        educacionMapper.toEducacionDto(educacionSinImagen);

        // Assert - Verificar que no hay interacciones
        verify(imagenMapper, never()).toImagenDto(any());
        verify(imagenMapper, never()).toImagen(any());
    }

    // ==================== TESTS CON OBJETOS COMPLEJOS ====================

    @Test
    @DisplayName("toEducacionDto - Debería mapear educación con imagen completa")
    void toEducacionDto_ShouldMapEducacionWithFullImage() {
        // Arrange - Imagen con todos los campos
        Imagen imagenCompleta = Imagen.builder()
                .id(20)
                .url("completa.jpg")
                .alt("Imagen completa de educación")
                .build();

        Educacion educacionCompleta = Educacion.builder()
                .id(11)
                .titulo("Educación Completa")
                .fechaObtencion(LocalDate.of(2023, 5, 15))
                .descripcion("Descripción completa de la educación")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagenCompleta)
                .usuario(usuario)
                .build();

        // Configurar mock para imagen completa
        when(imagenMapper.toImagenDto(imagenCompleta)).thenReturn(
                ImagenDto.builder()
                        .id(20)
                        .url("completa.jpg")
                        .alt("Imagen completa de educación")
                        .build()
        );

        // Act
        EducacionDto resultado = educacionMapper.toEducacionDto(educacionCompleta);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getImagen(), "La imagen no debería ser nula");
        assertEquals(20, resultado.getImagen().getId(), "El ID de la imagen debería ser 20");
        assertEquals("completa.jpg", resultado.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Imagen completa de educación", resultado.getImagen().getAlt(), "El alt de la imagen debería coincidir");

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagenDto(imagenCompleta);
    }

    @Test
    @DisplayName("toEducacion - Debería mapear EducacionDto con imagen completa")
    void toEducacion_ShouldMapEducacionDtoWithFullImage() {
        // Arrange - Imagen DTO con todos los campos
        ImagenDto imagenDtoCompleta = ImagenDto.builder()
                .id(21)
                .url("completa-dto.jpg")
                .alt("Imagen DTO completa de educación")
                .build();

        EducacionDto educacionDtoCompleta = EducacionDto.builder()
                .id(12)
                .titulo("Educación DTO Completa")
                .fechaObtencion(LocalDate.of(2023, 6, 20))
                .descripcion("Descripción DTO completa de la educación")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .imagen(imagenDtoCompleta)
                .build();

        // Configurar mock para imagen DTO completa
        when(imagenMapper.toImagen(imagenDtoCompleta)).thenReturn(
                Imagen.builder()
                        .id(21)
                        .url("completa-dto.jpg")
                        .alt("Imagen DTO completa de educación")
                        .build()
        );

        // Act
        Educacion resultado = educacionMapper.toEducacion(educacionDtoCompleta);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getImagen(), "La imagen no debería ser nula");
        assertEquals(21, resultado.getImagen().getId(), "El ID de la imagen debería ser 21");
        assertEquals("completa-dto.jpg", resultado.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Imagen DTO completa de educación", resultado.getImagen().getAlt(), "El alt de la imagen debería coincidir");

        // Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagen(imagenDtoCompleta);
    }

    // ==================== TESTS DE COMPARACIÓN DE OBJETOS ====================

    @Test
    @DisplayName("Comparar objeto EducacionDto - Debería ser igual cuando los valores son iguales")
    void educacionDtoEquals_ShouldBeEqual_WhenSameValues() {
        // Arrange
        LocalDate fecha = LocalDate.of(2020, 6, 15);
        ImagenDto imagen = ImagenDto.builder().id(1).url("img.jpg").alt("Alt").build();

        EducacionDto dto1 = EducacionDto.builder()
                .id(1)
                .titulo("Ingeniería")
                .fechaObtencion(fecha)
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .build();

        EducacionDto dto2 = EducacionDto.builder()
                .id(1)
                .titulo("Ingeniería")
                .fechaObtencion(fecha)
                .descripcion("Descripción")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .build();

        // Act & Assert
        assertEquals(dto1, dto2, "Los objetos deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode deberían ser iguales");
        assertTrue(dto1.equals(dto2), "equals() debería retornar true");
    }

    @Test
    @DisplayName("Comparar objeto EducacionDto - No debería ser igual cuando los valores son diferentes")
    void educacionDtoEquals_ShouldNotBeEqual_WhenDifferentValues() {
        // Arrange
        EducacionDto dto1 = EducacionDto.builder()
                .id(1)
                .titulo("Ingeniería")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Descripción A")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        EducacionDto dto2 = EducacionDto.builder()
                .id(2)
                .titulo("Máster")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Descripción B")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // Act & Assert
        assertNotEquals(dto1, dto2, "Los objetos no deberían ser iguales");
        assertNotEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCode no deberían ser iguales");
        assertFalse(dto1.equals(dto2), "equals() debería retornar false");
    }

    @Test
    @DisplayName("toString de EducacionDto debería incluir información relevante")
    void educacionDtoToString_ShouldIncludeRelevantInformation() {
        // Arrange & Act
        String toStringResult = educacionDtoValido.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("1"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Ingeniería Informática"), "toString debería contener el título");
        assertTrue(toStringResult.contains("2020-06-15"), "toString debería contener la fecha");
        assertTrue(toStringResult.contains("FORMAL"), "toString debería contener el tipo");
        assertTrue(toStringResult.contains("descripcion"), "toString debería contener el campo descripcion");
    }

    @Test
    @DisplayName("Builder de EducacionDto debería crear objetos correctamente")
    void educacionDtoBuilder_ShouldCreateCorrectly() {
        // Arrange & Act
        LocalDate fecha = LocalDate.of(2023, 10, 15);
        ImagenDto imagen = ImagenDto.builder().id(5).url("test.jpg").alt("Test").build();

        EducacionDto dto = EducacionDto.builder()
                .id(10)
                .titulo("Curso de Testing")
                .fechaObtencion(fecha)
                .descripcion("Curso de pruebas unitarias con JUnit")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .imagen(imagen)
                .build();

        // Assert
        assertNotNull(dto, "El objeto no debería ser nulo");
        assertEquals(10, dto.getId(), "El ID debería ser 10");
        assertEquals("Curso de Testing", dto.getTitulo(), "El título debería coincidir");
        assertEquals(fecha, dto.getFechaObtencion(), "La fecha debería coincidir");
        assertEquals("Curso de pruebas unitarias con JUnit", dto.getDescripcion(), "La descripción debería coincidir");
        assertEquals(TipoEducacion.INFORMAL_CURSO, dto.getTipoEducacion(), "El tipo debería ser INFORMAL_CURSO");
        assertNotNull(dto.getImagen(), "La imagen no debería ser nula");
        assertEquals("test.jpg", dto.getImagen().getUrl(), "La URL de la imagen debería coincidir");
    }

    @Test
    @DisplayName("EducacionDto debería aceptar valores nulos en campos opcionales")
    void educacionDto_ShouldAcceptNullValuesInOptionalFields() {
        // Arrange & Act
        EducacionDto dto = EducacionDto.builder()
                .id(1)
                .titulo("Título")
                .fechaObtencion(LocalDate.now())
                .descripcion(null)
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(null)
                .build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getDescripcion(), "La descripción debería ser nula");
        assertNull(dto.getImagen(), "La imagen debería ser nula");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        EducacionDto educacionDto = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            educacionDto.getTitulo(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }
}