package com.example.demo.mapper;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoConocimiento;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Imagen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ConocimientoMapper
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class ConocimientoMapperTest {

    private ConocimientoMapper mapper;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        mapper = Mappers.getMapper(ConocimientoMapper.class);
    }

    // ==================== TESTS DE MAPEO CONOCIMIENTO -> CONOCIMIENTODTO ====================

    @Test
    @DisplayName("Debería mapear Conocimiento a ConocimientoDto correctamente - Caso feliz")
    void shouldMapConocimientoToConocimientoDtoSuccessfully() {
        // Arrange - Configurar datos de prueba válidos
        Imagen imagen = Imagen.builder()
                .id(1)
                .url("imagen-conocimiento.jpg")
                .alt("Logo de Java")
                .build();

        Conocimiento conocimiento = Conocimiento.builder()
                .id(10)
                .nombre("Java Spring Boot")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        // Act - Ejecutar la acción a probar
        ConocimientoDto dto = mapper.toConocimientoDto(conocimiento);

        // Assert - Verificar resultados
        assertNotNull(dto, "El ConocimientoDto no debería ser nulo");
        assertEquals(10, dto.getId(), "El ID debería coincidir");
        assertEquals("Java Spring Boot", dto.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.AVANZADO, dto.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.BACKEND, dto.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");

        // Verificar mapeo de imagen
        assertNotNull(dto.getImagen(), "La imagen no debería ser nula");
        assertEquals(1, dto.getImagen().getId(), "El ID de la imagen debería coincidir");
        assertEquals("imagen-conocimiento.jpg", dto.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Logo de Java", dto.getImagen().getAlt(), "El alt de la imagen debería coincidir");
    }

    @Test
    @DisplayName("Debería mapear Conocimiento a ConocimientoDto cuando la imagen es nula")
    void shouldMapConocimientoToConocimientoDtoWhenImagenIsNull() {
        // Arrange - Conocimiento sin imagen
        Conocimiento conocimiento = Conocimiento.builder()
                .id(11)
                .nombre("JavaScript")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        // Act
        ConocimientoDto dto = mapper.toConocimientoDto(conocimiento);

        // Assert
        assertNotNull(dto, "El ConocimientoDto no debería ser nulo");
        assertEquals(11, dto.getId(), "El ID debería coincidir");
        assertEquals("JavaScript", dto.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.INTERMEDIO, dto.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.FRONTEND, dto.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNull(dto.getImagen(), "La imagen debería ser nula");
    }

    @Test
    @DisplayName("Debería mapear Conocimiento a ConocimientoDto cuando conocimiento es nulo")
    void shouldMapNullConocimientoToNullDto() {
        // Arrange - Conocimiento nulo
        Conocimiento conocimiento = null;

        // Act
        ConocimientoDto dto = mapper.toConocimientoDto(conocimiento);

        // Assert
        assertNull(dto, "El ConocimientoDto debería ser nulo");
    }

    @Test
    @DisplayName("Debería mapear Conocimiento a ConocimientoDto con valores mínimos válidos")
    void shouldMapConocimientoToConocimientoDtoWithMinimalValues() {
        // Arrange - Conocimiento con valores mínimos
        Conocimiento conocimiento = Conocimiento.builder()
                .id(1)
                .nombre("HTML")
                .nivel(Nivel.PRINCIPIANTE_BASICO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        // Act
        ConocimientoDto dto = mapper.toConocimientoDto(conocimiento);

        // Assert
        assertNotNull(dto, "El ConocimientoDto no debería ser nulo");
        assertEquals(1, dto.getId(), "El ID debería coincidir");
        assertEquals("HTML", dto.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.PRINCIPIANTE_BASICO, dto.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.FRONTEND, dto.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNull(dto.getImagen(), "La imagen debería ser nula");
    }

    @Test
    @DisplayName("Debería mapear Conocimiento a ConocimientoDto con todos los niveles disponibles")
    void shouldMapConocimientoToConocimientoDtoWithAllNiveles() {
        // Arrange - Probar todos los niveles
        Nivel[] niveles = Nivel.values();

        for (Nivel nivel : niveles) {
            // Arrange
            Conocimiento conocimiento = Conocimiento.builder()
                    .id(1)
                    .nombre("Conocimiento con nivel: " + nivel)
                    .nivel(nivel)
                    .tipoConocimiento(TipoConocimiento.OTROS)
                    .imagen(null)
                    .build();

            // Act
            ConocimientoDto dto = mapper.toConocimientoDto(conocimiento);

            // Assert
            assertNotNull(dto, "El ConocimientoDto no debería ser nulo para nivel: " + nivel);
            assertEquals(nivel, dto.getNivel(), "El nivel debería ser: " + nivel);
        }
    }

    @Test
    @DisplayName("Debería mapear Conocimiento a ConocimientoDto con todos los tipos de conocimiento disponibles")
    void shouldMapConocimientoToConocimientoDtoWithAllTiposConocimiento() {
        // Arrange - Probar todos los tipos de conocimiento
        TipoConocimiento[] tipos = TipoConocimiento.values();

        for (TipoConocimiento tipo : tipos) {
            // Arrange
            Conocimiento conocimiento = Conocimiento.builder()
                    .id(1)
                    .nombre("Conocimiento tipo: " + tipo)
                    .nivel(Nivel.INTERMEDIO)
                    .tipoConocimiento(tipo)
                    .imagen(null)
                    .build();

            // Act
            ConocimientoDto dto = mapper.toConocimientoDto(conocimiento);

            // Assert
            assertNotNull(dto, "El ConocimientoDto no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, dto.getTipoConocimiento(), "El tipo de conocimiento debería ser: " + tipo);
        }
    }

    // ==================== TESTS DE MAPEO CONOCIMIENTODTO -> CONOCIMIENTO ====================

    @Test
    @DisplayName("Debería mapear ConocimientoDto a Conocimiento correctamente - Caso feliz")
    void shouldMapConocimientoDtoToConocimientoSuccessfully() {
        // Arrange - Configurar datos de prueba válidos
        ImagenDto imagenDto = ImagenDto.builder()
                .id(5)
                .url("imagen-dto.jpg")
                .alt("Logo de Angular")
                .build();

        ConocimientoDto dto = ConocimientoDto.builder()
                .id(20)
                .nombre("Angular")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(imagenDto)
                .build();

        // Act
        Conocimiento conocimiento = mapper.toConocimiento(dto);

        // Assert
        assertNotNull(conocimiento, "El Conocimiento no debería ser nulo");
        assertEquals(20, conocimiento.getId(), "El ID debería coincidir");
        assertEquals("Angular", conocimiento.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.ALTO, conocimiento.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.FRONTEND, conocimiento.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");

        // Verificar mapeo de imagen
        assertNotNull(conocimiento.getImagen(), "La imagen no debería ser nula");
        assertEquals(5, conocimiento.getImagen().getId(), "El ID de la imagen debería coincidir");
        assertEquals("imagen-dto.jpg", conocimiento.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Logo de Angular", conocimiento.getImagen().getAlt(), "El alt de la imagen debería coincidir");

        // Verificar que el campo usuario se ignora correctamente
        assertNull(conocimiento.getUsuario(), "El usuario debería ser nulo (ignorado)");
    }

    @Test
    @DisplayName("Debería mapear ConocimientoDto a Conocimiento cuando la imagen es nula")
    void shouldMapConocimientoDtoToConocimientoWhenImagenIsNull() {
        // Arrange - ConocimientoDto sin imagen
        ConocimientoDto dto = ConocimientoDto.builder()
                .id(21)
                .nombre("SQL")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BASE_DATOS)
                .imagen(null)
                .build();

        // Act
        Conocimiento conocimiento = mapper.toConocimiento(dto);

        // Assert
        assertNotNull(conocimiento, "El Conocimiento no debería ser nulo");
        assertEquals(21, conocimiento.getId(), "El ID debería coincidir");
        assertEquals("SQL", conocimiento.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.INTERMEDIO, conocimiento.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.BASE_DATOS, conocimiento.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNull(conocimiento.getImagen(), "La imagen debería ser nula");
        assertNull(conocimiento.getUsuario(), "El usuario debería ser nulo (ignorado)");
    }

    @Test
    @DisplayName("Debería mapear ConocimientoDto a Conocimiento cuando dto es nulo")
    void shouldMapNullConocimientoDtoToNullConocimiento() {
        // Arrange - ConocimientoDto nulo
        ConocimientoDto dto = null;

        // Act
        Conocimiento conocimiento = mapper.toConocimiento(dto);

        // Assert
        assertNull(conocimiento, "El Conocimiento debería ser nulo");
    }

    @Test
    @DisplayName("Debería mapear ConocimientoDto a Conocimiento con valores mínimos")
    void shouldMapConocimientoDtoToConocimientoWithMinimalValues() {
        // Arrange - ConocimientoDto con valores mínimos
        ConocimientoDto dto = ConocimientoDto.builder()
                .id(1)
                .nombre("CSS")
                .nivel(Nivel.PRINCIPIANTE_BASICO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        // Act
        Conocimiento conocimiento = mapper.toConocimiento(dto);

        // Assert
        assertNotNull(conocimiento, "El Conocimiento no debería ser nulo");
        assertEquals(1, conocimiento.getId(), "El ID debería coincidir");
        assertEquals("CSS", conocimiento.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.PRINCIPIANTE_BASICO, conocimiento.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.FRONTEND, conocimiento.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNull(conocimiento.getImagen(), "La imagen debería ser nula");
    }

    // ==================== TESTS DE MÉTODOS AUXILIARES PARA IMÁGENES ====================

    @Test
    @DisplayName("Debería mapear Imagen a ImagenDto correctamente - Método toImagenDto")
    void shouldMapImagenToImagenDtoCorrectly() {
        // Arrange
        Imagen imagen = Imagen.builder()
                .id(100)
                .url("imagen-test.jpg")
                .alt("Imagen de prueba")
                .build();

        // Act
        ImagenDto dto = mapper.toImagenDto(imagen);

        // Assert
        assertNotNull(dto, "El ImagenDto no debería ser nulo");
        assertEquals(100, dto.getId(), "El ID debería coincidir");
        assertEquals("imagen-test.jpg", dto.getUrl(), "La URL debería coincidir");
        assertEquals("Imagen de prueba", dto.getAlt(), "El alt debería coincidir");
    }

    @Test
    @DisplayName("Debería retornar null cuando toImagenDto recibe imagen nula")
    void shouldReturnNullWhenToImagenDtoReceivesNullImagen() {
        // Arrange
        Imagen imagen = null;

        // Act
        ImagenDto dto = mapper.toImagenDto(imagen);

        // Assert
        assertNull(dto, "El ImagenDto debería ser nulo");
    }

    @Test
    @DisplayName("Debería mapear ImagenDto a Imagen correctamente - Método toImagen")
    void shouldMapImagenDtoToImagenCorrectly() {
        // Arrange
        ImagenDto dto = ImagenDto.builder()
                .id(200)
                .url("imagen-dto-test.jpg")
                .alt("Imagen DTO de prueba")
                .build();

        // Act
        Imagen imagen = mapper.toImagen(dto);

        // Assert
        assertNotNull(imagen, "La Imagen no debería ser nula");
        assertEquals(200, imagen.getId(), "El ID debería coincidir");
        assertEquals("imagen-dto-test.jpg", imagen.getUrl(), "La URL debería coincidir");
        assertEquals("Imagen DTO de prueba", imagen.getAlt(), "El alt debería coincidir");
    }

    @Test
    @DisplayName("Debería retornar null cuando toImagen recibe ImagenDto nulo")
    void shouldReturnNullWhenToImagenReceivesNullImagenDto() {
        // Arrange
        ImagenDto dto = null;

        // Act
        Imagen imagen = mapper.toImagen(dto);

        // Assert
        assertNull(imagen, "La Imagen debería ser nula");
    }

    // ==================== TESTS DE INTEGRACIÓN ENTRE MÉTODOS ====================

    @Test
    @DisplayName("Debería mantener consistencia en el mapeo bidireccional Conocimiento <-> ConocimientoDto")
    void shouldMaintainBidirectionalMappingConsistency() {
        // Arrange - Crear conocimiento original
        Imagen imagenOriginal = Imagen.builder()
                .id(50)
                .url("imagen-original.jpg")
                .alt("Imagen original")
                .build();

        Conocimiento conocimientoOriginal = Conocimiento.builder()
                .id(30)
                .nombre("Docker")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(imagenOriginal)
                .build();

        // Act - Mapear a DTO y luego de vuelta a Conocimiento
        ConocimientoDto dto = mapper.toConocimientoDto(conocimientoOriginal);
        Conocimiento conocimientoMapeado = mapper.toConocimiento(dto);

        // Assert - Verificar que los datos se mantienen consistentes
        assertNotNull(conocimientoMapeado, "El Conocimiento mapeado no debería ser nulo");
        assertEquals(conocimientoOriginal.getId(), conocimientoMapeado.getId(), "El ID debería mantenerse");
        assertEquals(conocimientoOriginal.getNombre(), conocimientoMapeado.getNombre(), "El nombre debería mantenerse");
        assertEquals(conocimientoOriginal.getNivel(), conocimientoMapeado.getNivel(), "El nivel debería mantenerse");
        assertEquals(conocimientoOriginal.getTipoConocimiento(), conocimientoMapeado.getTipoConocimiento(),
                "El tipo de conocimiento debería mantenerse");

        // Verificar imagen
        assertNotNull(conocimientoMapeado.getImagen(), "La imagen mapeada no debería ser nula");
        assertEquals(conocimientoOriginal.getImagen().getId(), conocimientoMapeado.getImagen().getId(),
                "El ID de la imagen debería mantenerse");
        assertEquals(conocimientoOriginal.getImagen().getUrl(), conocimientoMapeado.getImagen().getUrl(),
                "La URL de la imagen debería mantenerse");
        assertEquals(conocimientoOriginal.getImagen().getAlt(), conocimientoMapeado.getImagen().getAlt(),
                "El alt de la imagen debería mantenerse");
    }

    @Test
    @DisplayName("Debería mantener consistencia en el mapeo bidireccional sin imagen")
    void shouldMaintainBidirectionalMappingConsistencyWithoutImagen() {
        // Arrange - Crear conocimiento original sin imagen
        Conocimiento conocimientoOriginal = Conocimiento.builder()
                .id(31)
                .nombre("Git")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(null)
                .build();

        // Act - Mapear a DTO y luego de vuelta a Conocimiento
        ConocimientoDto dto = mapper.toConocimientoDto(conocimientoOriginal);
        Conocimiento conocimientoMapeado = mapper.toConocimiento(dto);

        // Assert
        assertNotNull(conocimientoMapeado, "El Conocimiento mapeado no debería ser nulo");
        assertEquals(conocimientoOriginal.getId(), conocimientoMapeado.getId(), "El ID debería mantenerse");
        assertEquals(conocimientoOriginal.getNombre(), conocimientoMapeado.getNombre(), "El nombre debería mantenerse");
        assertEquals(conocimientoOriginal.getNivel(), conocimientoMapeado.getNivel(), "El nivel debería mantenerse");
        assertEquals(conocimientoOriginal.getTipoConocimiento(), conocimientoMapeado.getTipoConocimiento(),
                "El tipo de conocimiento debería mantenerse");
        assertNull(conocimientoMapeado.getImagen(), "La imagen debería ser nula");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debería mapear Conocimiento a ConocimientoDto con caracteres especiales en nombre")
    void shouldMapConocimientoToConocimientoDtoWithSpecialCharacters() {
        // Arrange - Conocimiento con caracteres especiales
        String nombreEspecial = "C# - .NET Core (Framework 8)";
        Conocimiento conocimiento = Conocimiento.builder()
                .id(40)
                .nombre(nombreEspecial)
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .build();

        // Act
        ConocimientoDto dto = mapper.toConocimientoDto(conocimiento);

        // Assert
        assertNotNull(dto, "El ConocimientoDto no debería ser nulo");
        assertEquals(nombreEspecial, dto.getNombre(), "El nombre con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Debería mapear ConocimientoDto a Conocimiento con caracteres especiales en nombre")
    void shouldMapConocimientoDtoToConocimientoWithSpecialCharacters() {
        // Arrange - ConocimientoDto con caracteres especiales
        String nombreEspecial = "Python + Django (v5.0)";
        ConocimientoDto dto = ConocimientoDto.builder()
                .id(41)
                .nombre(nombreEspecial)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .build();

        // Act
        Conocimiento conocimiento = mapper.toConocimiento(dto);

        // Assert
        assertNotNull(conocimiento, "El Conocimiento no debería ser nulo");
        assertEquals(nombreEspecial, conocimiento.getNombre(), "El nombre con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Debería manejar correctamente el campo usuario ignorado en toConocimiento")
    void shouldIgnoreUsuarioFieldInToConocimiento() {
        // Arrange - Aunque el DTO no tiene campo usuario, verificar que se ignora
        ConocimientoDto dto = ConocimientoDto.builder()
                .id(50)
                .nombre("React")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        // Act
        Conocimiento conocimiento = mapper.toConocimiento(dto);

        // Assert
        assertNotNull(conocimiento, "El Conocimiento no debería ser nulo");
        assertNull(conocimiento.getUsuario(), "El usuario debería ser null ya que está ignorado en el mapeo");
    }

    // ==================== TESTS DE IGUALDAD ====================

    @Test
    @DisplayName("Conocimiento y ConocimientoDto mapeado deberían tener mismos valores para campos mapeados")
    void mappedConocimientoAndDtoShouldHaveSameValuesForMappedFields() {
        // Arrange
        ImagenDto imagenDto = ImagenDto.builder()
                .id(60)
                .url("imagen-test.jpg")
                .alt("Imagen de prueba")
                .build();

        ConocimientoDto dto = ConocimientoDto.builder()
                .id(70)
                .nombre("Kubernetes")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(imagenDto)
                .build();

        // Act
        Conocimiento conocimiento = mapper.toConocimiento(dto);

        // Assert - Comparar valores
        assertEquals(dto.getId(), conocimiento.getId(), "El ID debería ser el mismo");
        assertEquals(dto.getNombre(), conocimiento.getNombre(), "El nombre debería ser el mismo");
        assertEquals(dto.getNivel(), conocimiento.getNivel(), "El nivel debería ser el mismo");
        assertEquals(dto.getTipoConocimiento(), conocimiento.getTipoConocimiento(),
                "El tipo de conocimiento debería ser el mismo");

        // Comparar imagen
        assertNotNull(conocimiento.getImagen(), "La imagen no debería ser nula");
        assertEquals(dto.getImagen().getId(), conocimiento.getImagen().getId(),
                "El ID de la imagen debería ser el mismo");
        assertEquals(dto.getImagen().getUrl(), conocimiento.getImagen().getUrl(),
                "La URL de la imagen debería ser la misma");
        assertEquals(dto.getImagen().getAlt(), conocimiento.getImagen().getAlt(),
                "El alt de la imagen debería ser el mismo");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        Conocimiento conocimiento = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            conocimiento.getNombre(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en Imagen nula - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullImagen() {
        // Arrange
        ImagenDto imagenDto = null;

        // Act & Assert - Test negativo
        assertThrows(NullPointerException.class, () -> {
            imagenDto.getUrl(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en ImagenDto nulo");
    }

    // ==================== TESTS DE CONSTRUCTORES ====================

    @Test
    @DisplayName("Debería crear ConocimientoDto con constructor por defecto")
    void shouldCreateConocimientoDtoWithDefaultConstructor() {
        // Arrange & Act
        ConocimientoDto dto = new ConocimientoDto();

        // Assert
        assertNotNull(dto, "El ConocimientoDto no debería ser nulo");
        assertNull(dto.getId(), "El ID debería ser nulo por defecto");
        assertNull(dto.getNombre(), "El nombre debería ser nulo por defecto");
        assertNull(dto.getNivel(), "El nivel debería ser nulo por defecto");
        assertNull(dto.getTipoConocimiento(), "El tipo de conocimiento debería ser nulo por defecto");
        assertNull(dto.getImagen(), "La imagen debería ser nula por defecto");
    }

    @Test
    @DisplayName("Debería crear ConocimientoDto con constructor con todos los argumentos")
    void shouldCreateConocimientoDtoWithAllArgsConstructor() {
        // Arrange
        Integer id = 80;
        String nombre = "TypeScript";
        Nivel nivel = Nivel.INTERMEDIO;
        TipoConocimiento tipoConocimiento = TipoConocimiento.FRONTEND;
        ImagenDto imagen = ImagenDto.builder()
                .id(90)
                .url("typescript.jpg")
                .alt("Logo TypeScript")
                .build();

        // Act
        ConocimientoDto dto = new ConocimientoDto(id, nombre, nivel, tipoConocimiento, imagen);

        // Assert
        assertNotNull(dto, "El ConocimientoDto no debería ser nulo");
        assertEquals(id, dto.getId(), "El ID debería coincidir");
        assertEquals(nombre, dto.getNombre(), "El nombre debería coincidir");
        assertEquals(nivel, dto.getNivel(), "El nivel debería coincidir");
        assertEquals(tipoConocimiento, dto.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNotNull(dto.getImagen(), "La imagen no debería ser nula");
        assertEquals(imagen.getId(), dto.getImagen().getId(), "El ID de la imagen debería coincidir");
    }

    @Test
    @DisplayName("Debería crear ConocimientoDto con constructor con todos los argumentos y imagen nula")
    void shouldCreateConocimientoDtoWithAllArgsConstructorAndNullImagen() {
        // Arrange
        Integer id = 81;
        String nombre = "PostgreSQL";
        Nivel nivel = Nivel.INTERMEDIO;
        TipoConocimiento tipoConocimiento = TipoConocimiento.BASE_DATOS;
        ImagenDto imagen = null;

        // Act
        ConocimientoDto dto = new ConocimientoDto(id, nombre, nivel, tipoConocimiento, imagen);

        // Assert
        assertNotNull(dto, "El ConocimientoDto no debería ser nulo");
        assertEquals(id, dto.getId(), "El ID debería coincidir");
        assertEquals(nombre, dto.getNombre(), "El nombre debería coincidir");
        assertEquals(nivel, dto.getNivel(), "El nivel debería coincidir");
        assertEquals(tipoConocimiento, dto.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNull(dto.getImagen(), "La imagen debería ser nula");
    }

    // ==================== TESTS DE BUILDER ====================

    @Test
    @DisplayName("Debería construir ConocimientoDto con builder correctamente")
    void shouldBuildConocimientoDtoCorrectly() {
        // Arrange & Act
        ImagenDto imagenDto = ImagenDto.builder()
                .id(100)
                .url("imagen-builder.jpg")
                .alt("Imagen construida con builder")
                .build();

        ConocimientoDto dto = ConocimientoDto.builder()
                .id(90)
                .nombre("Spring Data JPA")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagenDto)
                .build();

        // Assert
        assertNotNull(dto, "El ConocimientoDto no debería ser nulo");
        assertEquals(90, dto.getId(), "El ID debería coincidir");
        assertEquals("Spring Data JPA", dto.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.ALTO, dto.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.BACKEND, dto.getTipoConocimiento(), "El tipo de conocimiento debería coincidir");
        assertNotNull(dto.getImagen(), "La imagen no debería ser nula");
        assertEquals(100, dto.getImagen().getId(), "El ID de la imagen debería coincidir");
    }

    @Test
    @DisplayName("Debería construir Conocimiento con builder correctamente")
    void shouldBuildConocimientoCorrectly() {
        // Arrange & Act
        Imagen imagen = Imagen.builder()
                .id(200)
                .url("imagen-entity.jpg")
                .alt("Imagen de entidad")
                .build();

        Conocimiento conocimiento = Conocimiento.builder()
                .id(91)
                .nombre("Hibernate")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(imagen)
                .build();

        // Assert
        assertNotNull(conocimiento, "El Conocimiento no debería ser nulo");
        assertEquals(91, conocimiento.getId(), "El ID debería coincidir");
        assertEquals("Hibernate", conocimiento.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.AVANZADO, conocimiento.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.BACKEND, conocimiento.getTipoConocimiento(),
                "El tipo de conocimiento debería coincidir");
        assertNotNull(conocimiento.getImagen(), "La imagen no debería ser nula");
        assertEquals(200, conocimiento.getImagen().getId(), "El ID de la imagen debería coincidir");
    }

    // ==================== TESTS DE SETTERS ====================

    @Test
    @DisplayName("Debería actualizar valores de ConocimientoDto con setters")
    void shouldUpdateConocimientoDtoValuesWithSetters() {
        // Arrange
        ConocimientoDto dto = ConocimientoDto.builder()
                .id(1)
                .nombre("Nombre Inicial")
                .nivel(Nivel.PRINCIPIANTE_BASICO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        ImagenDto nuevaImagen = ImagenDto.builder()
                .id(300)
                .url("nueva-imagen.jpg")
                .alt("Nueva imagen")
                .build();

        // Act
        dto.setId(2);
        dto.setNombre("Nombre Actualizado");
        dto.setNivel(Nivel.AVANZADO);
        dto.setTipoConocimiento(TipoConocimiento.BACKEND);
        dto.setImagen(nuevaImagen);

        // Assert
        assertEquals(2, dto.getId(), "El ID debería estar actualizado");
        assertEquals("Nombre Actualizado", dto.getNombre(), "El nombre debería estar actualizado");
        assertEquals(Nivel.AVANZADO, dto.getNivel(), "El nivel debería estar actualizado");
        assertEquals(TipoConocimiento.BACKEND, dto.getTipoConocimiento(),
                "El tipo de conocimiento debería estar actualizado");
        assertNotNull(dto.getImagen(), "La imagen no debería ser nula");
        assertEquals(300, dto.getImagen().getId(), "El ID de la imagen debería estar actualizado");
        assertEquals("nueva-imagen.jpg", dto.getImagen().getUrl(), "La URL de la imagen debería estar actualizada");
    }

    @Test
    @DisplayName("Debería actualizar valores de Conocimiento con setters")
    void shouldUpdateConocimientoValuesWithSetters() {
        // Arrange
        Conocimiento conocimiento = Conocimiento.builder()
                .id(1)
                .nombre("Nombre Inicial")
                .nivel(Nivel.PRINCIPIANTE_BASICO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .build();

        Imagen nuevaImagen = Imagen.builder()
                .id(400)
                .url("nueva-imagen-entity.jpg")
                .alt("Nueva imagen entidad")
                .build();

        // Act
        conocimiento.setId(2);
        conocimiento.setNombre("Nombre Actualizado");
        conocimiento.setNivel(Nivel.AVANZADO);
        conocimiento.setTipoConocimiento(TipoConocimiento.BACKEND);
        conocimiento.setImagen(nuevaImagen);

        // Assert
        assertEquals(2, conocimiento.getId(), "El ID debería estar actualizado");
        assertEquals("Nombre Actualizado", conocimiento.getNombre(), "El nombre debería estar actualizado");
        assertEquals(Nivel.AVANZADO, conocimiento.getNivel(), "El nivel debería estar actualizado");
        assertEquals(TipoConocimiento.BACKEND, conocimiento.getTipoConocimiento(),
                "El tipo de conocimiento debería estar actualizado");
        assertNotNull(conocimiento.getImagen(), "La imagen no debería ser nula");
        assertEquals(400, conocimiento.getImagen().getId(), "El ID de la imagen debería estar actualizado");
    }

    // ==================== TESTS DE toSTRING ====================

    @Test
    @DisplayName("toString de ConocimientoDto debería incluir información relevante")
    void toString_ShouldIncludeRelevantInformationInConocimientoDto() {
        // Arrange
        ConocimientoDto dto = ConocimientoDto.builder()
                .id(100)
                .nombre("Spring Security")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(null)
                .build();

        // Act
        String toStringResult = dto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("100"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("Spring Security"), "toString debería contener el nombre");
        assertTrue(toStringResult.contains("ALTO"), "toString debería contener el nivel");
        assertTrue(toStringResult.contains("BACKEND"), "toString debería contener el tipo de conocimiento");
    }

    @Test
    @DisplayName("toString de Conocimiento debería incluir información relevante")
    void toString_ShouldIncludeRelevantInformationInConocimiento() {
        // Arrange
        Conocimiento conocimiento = Conocimiento.builder()
                .id(101)
                .nombre("JUnit")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.TESTING)
                .imagen(null)
                .build();

        // Act
        String toStringResult = conocimiento.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("101"), "toString debería contener el ID");
        assertTrue(toStringResult.contains("JUnit"), "toString debería contener el nombre");
        assertTrue(toStringResult.contains("INTERMEDIO"), "toString debería contener el nivel");
        assertTrue(toStringResult.contains("TESTING"), "toString debería contener el tipo de conocimiento");
    }

    // ==================== TESTS DE CASOS DE USO REALISTAS ====================

    @Test
    @DisplayName("Debería mapear correctamente un conocimiento con imagen para usar en API REST")
    void shouldMapKnowledgeWithImagenCorrectlyForRestApi() {
        // Arrange - Simular datos recibidos de la base de datos
        Imagen imagen = Imagen.builder()
                .id(500)
                .url("/api/imagenes/500/descargar")
                .alt("Logo de MongoDB")
                .build();

        Conocimiento conocimiento = Conocimiento.builder()
                .id(55)
                .nombre("MongoDB")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BASE_DATOS)
                .imagen(imagen)
                .build();

        // Act - Convertir a DTO para enviar al frontend
        ConocimientoDto dto = mapper.toConocimientoDto(conocimiento);

        // Assert - Verificar que el DTO tiene los datos correctos para el frontend
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertEquals(55, dto.getId(), "El ID debería ser correcto");
        assertEquals("MongoDB", dto.getNombre(), "El nombre debería ser correcto");
        assertEquals(Nivel.INTERMEDIO, dto.getNivel(), "El nivel debería ser correcto");
        assertEquals(TipoConocimiento.BASE_DATOS, dto.getTipoConocimiento(),
                "El tipo de conocimiento debería ser correcto");

        // Verificar imagen
        assertNotNull(dto.getImagen(), "La imagen en el DTO no debería ser nula");
        assertEquals(500, dto.getImagen().getId(), "El ID de la imagen debería ser correcto");
        assertEquals("/api/imagenes/500/descargar", dto.getImagen().getUrl(),
                "La URL de la imagen debería ser la correcta");
        assertEquals("Logo de MongoDB", dto.getImagen().getAlt(),
                "El alt de la imagen debería ser el correcto");
    }

    @Test
    @DisplayName("Debería mapear correctamente un DTO recibido del frontend a entidad para guardar")
    void shouldMapDtoFromFrontendToEntityCorrectlyForSaving() {
        // Arrange - Simular datos recibidos del frontend
        ImagenDto imagenDto = ImagenDto.builder()
                .id(600)
                .url("/api/imagenes/600/descargar")
                .alt("Logo de Redis")
                .build();

        ConocimientoDto dto = ConocimientoDto.builder()
                .id(56)
                .nombre("Redis")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BASE_DATOS)
                .imagen(imagenDto)
                .build();

        // Act - Convertir a entidad para guardar en base de datos
        Conocimiento conocimiento = mapper.toConocimiento(dto);

        // Assert - Verificar que la entidad tiene los datos correctos para guardar
        assertNotNull(conocimiento, "La entidad no debería ser nula");
        assertEquals(56, conocimiento.getId(), "El ID debería ser correcto");
        assertEquals("Redis", conocimiento.getNombre(), "El nombre debería ser correcto");
        assertEquals(Nivel.ALTO, conocimiento.getNivel(), "El nivel debería ser correcto");
        assertEquals(TipoConocimiento.BASE_DATOS, conocimiento.getTipoConocimiento(),
                "El tipo de conocimiento debería ser correcto");
        assertNull(conocimiento.getUsuario(), "El usuario debería ser null (ignorado)");

        // Verificar imagen
        assertNotNull(conocimiento.getImagen(), "La imagen en la entidad no debería ser nula");
        assertEquals(600, conocimiento.getImagen().getId(), "El ID de la imagen debería ser correcto");
        assertEquals("/api/imagenes/600/descargar", conocimiento.getImagen().getUrl(),
                "La URL de la imagen debería ser la correcta");
        assertEquals("Logo de Redis", conocimiento.getImagen().getAlt(),
                "El alt de la imagen debería ser el correcto");
    }
}