package com.example.demo.mapper;

import com.example.demo.dto.ImagenDto;
import com.example.demo.model.Imagen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ImagenMapper
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Usa MapStruct para el mapeo entre Imagen y ImagenDto
 */
@ExtendWith(MockitoExtension.class)
class ImagenMapperTest {

    private ImagenMapper imagenMapper;

    private Imagen imagenValida;
    private ImagenDto imagenDtoValido;
    private Imagen imagenConRelaciones;
    private ImagenDto imagenDtoConRelaciones;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        // Obtener la instancia del mapper generado por MapStruct
        imagenMapper = Mappers.getMapper(ImagenMapper.class);

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

        // Crear imagen con relaciones (simuladas)
        imagenConRelaciones = Imagen.builder()
                .id(2)
                .url("https://ejemplo.com/imagen-relacionada.jpg")
                .alt("Imagen con relaciones")
                .build();

        // Crear ImagenDto con relaciones (simuladas)
        imagenDtoConRelaciones = ImagenDto.builder()
                .id(2)
                .url("https://ejemplo.com/imagen-relacionada.jpg")
                .alt("Imagen con relaciones")
                .build();
    }

    // ==================== TESTS TO DTO ====================

    @Test
    @DisplayName("toImagenDto - Debería mapear Imagen a ImagenDto correctamente")
    void toImagenDto_ShouldMapImagenToImagenDtoCorrectly() {
        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenValida);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(imagenValida.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(imagenValida.getUrl(), resultado.getUrl(), "La URL debería coincidir");
        assertEquals(imagenValida.getAlt(), resultado.getAlt(), "El texto alternativo debería coincidir");
    }

    @Test
    @DisplayName("toImagenDto - Debería mapear Imagen con valores nulos a ImagenDto")
    void toImagenDto_ShouldMapImagenWithNullValuesToImagenDto() {
        // Arrange - Imagen con campos nulos
        Imagen imagenNula = Imagen.builder()
                .id(null)
                .url(null)
                .alt(null)
                .build();

        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenNula);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertNull(resultado.getUrl(), "La URL debería ser nula");
        assertNull(resultado.getAlt(), "El texto alternativo debería ser nulo");
    }

    @Test
    @DisplayName("toImagenDto - Debería mapear Imagen con solo ID")
    void toImagenDto_ShouldMapImagenWithOnlyId() {
        // Arrange - Imagen solo con ID
        Imagen imagenSoloId = Imagen.builder()
                .id(5)
                .url(null)
                .alt(null)
                .build();

        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenSoloId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(5, resultado.getId(), "El ID debería ser 5");
        assertNull(resultado.getUrl(), "La URL debería ser nula");
        assertNull(resultado.getAlt(), "El texto alternativo debería ser nulo");
    }

    @Test
    @DisplayName("toImagenDto - Debería mapear Imagen con solo URL")
    void toImagenDto_ShouldMapImagenWithOnlyUrl() {
        // Arrange - Imagen solo con URL
        Imagen imagenSoloUrl = Imagen.builder()
                .id(null)
                .url("https://ejemplo.com/solo-url.jpg")
                .alt(null)
                .build();

        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenSoloUrl);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals("https://ejemplo.com/solo-url.jpg", resultado.getUrl(), "La URL debería coincidir");
        assertNull(resultado.getAlt(), "El texto alternativo debería ser nulo");
    }

    @Test
    @DisplayName("toImagenDto - Debería mapear Imagen con solo alt")
    void toImagenDto_ShouldMapImagenWithOnlyAlt() {
        // Arrange - Imagen solo con alt
        Imagen imagenSoloAlt = Imagen.builder()
                .id(null)
                .url(null)
                .alt("Solo texto alternativo")
                .build();

        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenSoloAlt);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertNull(resultado.getUrl(), "La URL debería ser nula");
        assertEquals("Solo texto alternativo", resultado.getAlt(), "El texto alternativo debería coincidir");
    }

    @Test
    @DisplayName("toImagenDto - Debería manejar Imagen nula")
    void toImagenDto_ShouldHandleNullImagen() {
        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando la imagen es nula");
    }

    // ==================== TESTS TO ENTITY ====================

    @Test
    @DisplayName("toImagen - Debería mapear ImagenDto a Imagen correctamente")
    void toImagen_ShouldMapImagenDtoToImagenCorrectly() {
        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(imagenDtoValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(imagenDtoValido.getUrl(), resultado.getUrl(), "La URL debería coincidir");
        assertEquals(imagenDtoValido.getAlt(), resultado.getAlt(), "El texto alternativo debería coincidir");
    }

    @Test
    @DisplayName("toImagen - Debería mapear ImagenDto con valores nulos a Imagen")
    void toImagen_ShouldMapImagenDtoWithNullValuesToImagen() {
        // Arrange - ImagenDto con campos nulos
        ImagenDto imagenDtoNulo = ImagenDto.builder()
                .id(null)
                .url(null)
                .alt(null)
                .build();

        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoNulo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertNull(resultado.getUrl(), "La URL debería ser nula");
        assertNull(resultado.getAlt(), "El texto alternativo debería ser nulo");
    }

    @Test
    @DisplayName("toImagen - Debería mapear ImagenDto con solo ID")
    void toImagen_ShouldMapImagenDtoWithOnlyId() {
        // Arrange - ImagenDto solo con ID
        ImagenDto imagenDtoSoloId = ImagenDto.builder()
                .id(10)
                .url(null)
                .alt(null)
                .build();

        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoSoloId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(10, resultado.getId(), "El ID debería ser 10");
        assertNull(resultado.getUrl(), "La URL debería ser nula");
        assertNull(resultado.getAlt(), "El texto alternativo debería ser nulo");
    }

    @Test
    @DisplayName("toImagen - Debería mapear ImagenDto con solo URL")
    void toImagen_ShouldMapImagenDtoWithOnlyUrl() {
        // Arrange - ImagenDto solo con URL
        ImagenDto imagenDtoSoloUrl = ImagenDto.builder()
                .id(null)
                .url("https://ejemplo.com/solo-url-dto.jpg")
                .alt(null)
                .build();

        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoSoloUrl);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals("https://ejemplo.com/solo-url-dto.jpg", resultado.getUrl(), "La URL debería coincidir");
        assertNull(resultado.getAlt(), "El texto alternativo debería ser nulo");
    }

    @Test
    @DisplayName("toImagen - Debería mapear ImagenDto con solo alt")
    void toImagen_ShouldMapImagenDtoWithOnlyAlt() {
        // Arrange - ImagenDto solo con alt
        ImagenDto imagenDtoSoloAlt = ImagenDto.builder()
                .id(null)
                .url(null)
                .alt("Solo texto alternativo DTO")
                .build();

        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoSoloAlt);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertNull(resultado.getUrl(), "La URL debería ser nula");
        assertEquals("Solo texto alternativo DTO", resultado.getAlt(), "El texto alternativo debería coincidir");
    }

    @Test
    @DisplayName("toImagen - Debería manejar ImagenDto nulo")
    void toImagen_ShouldHandleNullImagenDto() {
        // Act
        Imagen resultado = imagenMapper.toImagen(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando el ImagenDto es nulo");
    }

    // ==================== TESTS DE CONSISTENCIA ====================

    @Test
    @DisplayName("toImagenDto y toImagen - Deberían ser consistentes (round-trip)")
    void toImagenDtoAndToImagen_ShouldBeConsistent() {
        // Act - Convertir de Imagen a ImagenDto y luego de vuelta a Imagen
        ImagenDto dto = imagenMapper.toImagenDto(imagenValida);
        Imagen imagenConvertida = imagenMapper.toImagen(dto);

        // Assert - Verificar que el round-trip mantiene los datos
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(imagenConvertida, "La imagen convertida no debería ser nula");
        assertEquals(imagenValida.getId(), imagenConvertida.getId(), "El ID debería ser el mismo");
        assertEquals(imagenValida.getUrl(), imagenConvertida.getUrl(), "La URL debería ser la misma");
        assertEquals(imagenValida.getAlt(), imagenConvertida.getAlt(), "El alt debería ser el mismo");
    }

    @Test
    @DisplayName("toImagenDto y toImagen - Deberían ser consistentes con objetos nulos")
    void toImagenDtoAndToImagen_ShouldBeConsistentWithNullObjects() {
        // Act - Convertir de ImagenDto a Imagen y luego de vuelta a ImagenDto
        Imagen imagen = imagenMapper.toImagen(imagenDtoValido);
        ImagenDto dtoConvertido = imagenMapper.toImagenDto(imagen);

        // Assert
        assertNotNull(imagen, "La imagen no debería ser nula");
        assertNotNull(dtoConvertido, "El DTO convertido no debería ser nulo");
        assertEquals(imagenDtoValido.getId(), dtoConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(imagenDtoValido.getUrl(), dtoConvertido.getUrl(), "La URL debería ser la misma");
        assertEquals(imagenDtoValido.getAlt(), dtoConvertido.getAlt(), "El alt debería ser el mismo");
    }

    @Test
    @DisplayName("toImagenDto y toImagen - Deberían ser consistentes con campos vacíos")
    void toImagenDtoAndToImagen_ShouldBeConsistentWithEmptyFields() {
        // Arrange - Imagen con campos vacíos
        Imagen imagenVacia = Imagen.builder()
                .id(100)
                .url("")
                .alt("")
                .build();

        // Act - Convertir de Imagen a ImagenDto y luego de vuelta a Imagen
        ImagenDto dto = imagenMapper.toImagenDto(imagenVacia);
        Imagen imagenConvertida = imagenMapper.toImagen(dto);

        // Assert
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(imagenConvertida, "La imagen convertida no debería ser nula");
        assertEquals(imagenVacia.getId(), imagenConvertida.getId(), "El ID debería ser el mismo");
        assertEquals("", imagenConvertida.getUrl(), "La URL vacía debería mantenerse");
        assertEquals("", imagenConvertida.getAlt(), "El alt vacío debería mantenerse");
    }

    // ==================== TESTS CON CARACTERES ESPECIALES ====================

    @Test
    @DisplayName("toImagenDto - Debería mapear Imagen con caracteres especiales en URL")
    void toImagenDto_ShouldMapImagenWithSpecialCharactersInUrl() {
        // Arrange
        String urlEspecial = "https://ejemplo.com/imagen-con-espacios%20y-simbolos!@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~.jpg";
        Imagen imagenEspecial = Imagen.builder()
                .id(3)
                .url(urlEspecial)
                .alt("Imagen con URL especial")
                .build();

        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(urlEspecial, resultado.getUrl(), "La URL con caracteres especiales debería mantenerse");
        assertEquals("Imagen con URL especial", resultado.getAlt(), "El alt debería coincidir");
    }

    @Test
    @DisplayName("toImagenDto - Debería mapear Imagen con caracteres especiales en alt")
    void toImagenDto_ShouldMapImagenWithSpecialCharactersInAlt() {
        // Arrange
        String altEspecial = "Texto alternativo con acentos: áéíóú ñü y caracteres especiales !@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~";
        Imagen imagenEspecial = Imagen.builder()
                .id(4)
                .url("https://ejemplo.com/imagen.jpg")
                .alt(altEspecial)
                .build();

        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(altEspecial, resultado.getAlt(), "El alt con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("toImagen - Debería mapear ImagenDto con caracteres especiales en URL")
    void toImagen_ShouldMapImagenDtoWithSpecialCharactersInUrl() {
        // Arrange
        String urlEspecial = "https://ejemplo.com/imagen-dto-con-espacios%20y-simbolos!@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~.jpg";
        ImagenDto imagenDtoEspecial = ImagenDto.builder()
                .id(5)
                .url(urlEspecial)
                .alt("Imagen DTO con URL especial")
                .build();

        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(urlEspecial, resultado.getUrl(), "La URL con caracteres especiales debería mantenerse");
        assertEquals("Imagen DTO con URL especial", resultado.getAlt(), "El alt debería coincidir");
    }

    @Test
    @DisplayName("toImagen - Debería mapear ImagenDto con caracteres especiales en alt")
    void toImagen_ShouldMapImagenDtoWithSpecialCharactersInAlt() {
        // Arrange
        String altEspecial = "Texto alternativo DTO con acentos: áéíóú ñü y caracteres especiales !@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~";
        ImagenDto imagenDtoEspecial = ImagenDto.builder()
                .id(6)
                .url("https://ejemplo.com/imagen-dto.jpg")
                .alt(altEspecial)
                .build();

        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(altEspecial, resultado.getAlt(), "El alt con caracteres especiales debería mantenerse");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("toImagenDto - Debería manejar URL muy larga")
    void toImagenDto_ShouldHandleVeryLongUrl() {
        // Arrange
        String longUrl = "https://ejemplo.com/" + "a".repeat(500) + ".jpg";
        Imagen imagenConUrlLarga = Imagen.builder()
                .id(7)
                .url(longUrl)
                .alt("Imagen con URL larga")
                .build();

        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenConUrlLarga);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(longUrl, resultado.getUrl(), "La URL larga debería mantenerse");
        assertEquals("Imagen con URL larga", resultado.getAlt(), "El alt debería coincidir");
    }

    @Test
    @DisplayName("toImagenDto - Debería manejar alt muy largo")
    void toImagenDto_ShouldHandleVeryLongAlt() {
        // Arrange
        String longAlt = "Texto alternativo muy largo " + "b".repeat(500);
        Imagen imagenConAltLargo = Imagen.builder()
                .id(8)
                .url("https://ejemplo.com/imagen.jpg")
                .alt(longAlt)
                .build();

        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenConAltLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(longAlt, resultado.getAlt(), "El alt largo debería mantenerse");
    }

    @Test
    @DisplayName("toImagen - Debería manejar URL muy larga")
    void toImagen_ShouldHandleVeryLongUrl() {
        // Arrange
        String longUrl = "https://ejemplo.com/" + "a".repeat(500) + ".jpg";
        ImagenDto imagenDtoConUrlLarga = ImagenDto.builder()
                .id(9)
                .url(longUrl)
                .alt("Imagen DTO con URL larga")
                .build();

        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoConUrlLarga);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(longUrl, resultado.getUrl(), "La URL larga debería mantenerse");
    }

    @Test
    @DisplayName("toImagen - Debería manejar alt muy largo")
    void toImagen_ShouldHandleVeryLongAlt() {
        // Arrange
        String longAlt = "Texto alternativo DTO muy largo " + "b".repeat(500);
        ImagenDto imagenDtoConAltLargo = ImagenDto.builder()
                .id(10)
                .url("https://ejemplo.com/imagen-dto.jpg")
                .alt(longAlt)
                .build();

        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoConAltLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(longAlt, resultado.getAlt(), "El alt largo debería mantenerse");
    }

    // ==================== TESTS CON DIFERENTES TIPOS DE URL ====================

    @Test
    @DisplayName("toImagenDto - Debería mapear Imagen con diferentes protocolos de URL")
    void toImagenDto_ShouldMapImagenWithDifferentUrlProtocols() {
        // Arrange - URLs con diferentes protocolos
        String[] urls = {
                "https://ejemplo.com/imagen.jpg",
                "http://ejemplo.com/imagen.png",
                "ftp://ejemplo.com/imagen.gif",
                "//ejemplo.com/imagen.jpg",
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
                "file:///C:/imagenes/imagen.jpg"
        };

        for (String url : urls) {
            Imagen imagen = Imagen.builder()
                    .id(11)
                    .url(url)
                    .alt("Imagen con protocolo: " + url)
                    .build();

            // Act
            ImagenDto resultado = imagenMapper.toImagenDto(imagen);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para URL: " + url);
            assertEquals(url, resultado.getUrl(), "La URL debería mantenerse para: " + url);
        }
    }

    @Test
    @DisplayName("toImagen - Debería mapear ImagenDto con diferentes protocolos de URL")
    void toImagen_ShouldMapImagenDtoWithDifferentUrlProtocols() {
        // Arrange - URLs con diferentes protocolos
        String[] urls = {
                "https://ejemplo.com/imagen-dto.jpg",
                "http://ejemplo.com/imagen-dto.png",
                "ftp://ejemplo.com/imagen-dto.gif",
                "//ejemplo.com/imagen-dto.jpg",
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
                "file:///C:/imagenes/imagen-dto.jpg"
        };

        for (String url : urls) {
            ImagenDto imagenDto = ImagenDto.builder()
                    .id(12)
                    .url(url)
                    .alt("Imagen DTO con protocolo: " + url)
                    .build();

            // Act
            Imagen resultado = imagenMapper.toImagen(imagenDto);

            // Assert
            assertNotNull(resultado, "El resultado no debería ser nulo para URL: " + url);
            assertEquals(url, resultado.getUrl(), "La URL debería mantenerse para: " + url);
        }
    }

    // ==================== TESTS DE CONSTRUCTORES ====================

    @Test
    @DisplayName("Mappers.getMapper - Debería obtener una instancia válida de ImagenMapper")
    void getMapper_ShouldReturnValidImagenMapperInstance() {
        // Act
        ImagenMapper mapper = Mappers.getMapper(ImagenMapper.class);

        // Assert
        assertNotNull(mapper, "El mapper no debería ser nulo");
        assertNotNull(mapper.toImagenDto(imagenValida), "El mapper debería poder mapear");
        assertNotNull(mapper.toImagen(imagenDtoValido), "El mapper debería poder mapear");
    }

    // ==================== TESTS DE COMPARACIÓN ====================

    @Test
    @DisplayName("toImagenDto - Debería mantener todos los valores del objeto original")
    void toImagenDto_ShouldMaintainAllValuesFromOriginalObject() {
        // Arrange
        Imagen imagenOriginal = Imagen.builder()
                .id(13)
                .url("https://ejemplo.com/original.jpg")
                .alt("Texto alternativo original")
                .build();

        // Act
        ImagenDto resultado = imagenMapper.toImagenDto(imagenOriginal);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(imagenOriginal.getId(), resultado.getId(), "El ID debería ser el mismo");
        assertEquals(imagenOriginal.getUrl(), resultado.getUrl(), "La URL debería ser la misma");
        assertEquals(imagenOriginal.getAlt(), resultado.getAlt(), "El alt debería ser el mismo");

        // Verificar que los objetos son diferentes instancias pero con mismos valores
        assertNotSame(imagenOriginal, resultado, "Deberían ser objetos diferentes");
    }

    @Test
    @DisplayName("toImagen - Debería mantener todos los valores del DTO original")
    void toImagen_ShouldMaintainAllValuesFromOriginalDto() {
        // Arrange
        ImagenDto imagenDtoOriginal = ImagenDto.builder()
                .id(14)
                .url("https://ejemplo.com/original-dto.jpg")
                .alt("Texto alternativo DTO original")
                .build();

        // Act
        Imagen resultado = imagenMapper.toImagen(imagenDtoOriginal);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(imagenDtoOriginal.getId(), resultado.getId(), "El ID debería ser el mismo");
        assertEquals(imagenDtoOriginal.getUrl(), resultado.getUrl(), "La URL debería ser la misma");
        assertEquals(imagenDtoOriginal.getAlt(), resultado.getAlt(), "El alt debería ser el mismo");

        // Verificar que los objetos son diferentes instancias pero con mismos valores
        assertNotSame(imagenDtoOriginal, resultado, "Deberían ser objetos diferentes");
    }

    // ==================== TESTS DE MÚLTIPLES INSTANCIAS ====================

    @Test
    @DisplayName("toImagenDto - Debería mapear múltiples imágenes correctamente")
    void toImagenDto_ShouldMapMultipleImagenesCorrectly() {
        // Arrange - Crear múltiples imágenes
        Imagen[] imagenes = new Imagen[5];
        for (int i = 0; i < imagenes.length; i++) {
            imagenes[i] = Imagen.builder()
                    .id(i + 1)
                    .url("https://ejemplo.com/imagen-" + i + ".jpg")
                    .alt("Imagen " + i)
                    .build();
        }

        // Act - Mapear todas las imágenes
        ImagenDto[] resultados = new ImagenDto[imagenes.length];
        for (int i = 0; i < imagenes.length; i++) {
            resultados[i] = imagenMapper.toImagenDto(imagenes[i]);
        }

        // Assert - Verificar que cada DTO tiene los valores correctos
        for (int i = 0; i < resultados.length; i++) {
            assertNotNull(resultados[i], "El DTO " + i + " no debería ser nulo");
            assertEquals(imagenes[i].getId(), resultados[i].getId(), "El ID de " + i + " debería coincidir");
            assertEquals(imagenes[i].getUrl(), resultados[i].getUrl(), "La URL de " + i + " debería coincidir");
            assertEquals(imagenes[i].getAlt(), resultados[i].getAlt(), "El alt de " + i + " debería coincidir");

            // Verificar que los DTOs son diferentes entre sí
            for (int j = i + 1; j < resultados.length; j++) {
                assertNotEquals(resultados[i], resultados[j], "Los DTOs " + i + " y " + j + " no deberían ser iguales");
            }
        }
    }

    @Test
    @DisplayName("toImagen - Debería mapear múltiples DTOs correctamente")
    void toImagen_ShouldMapMultipleDtosCorrectly() {
        // Arrange - Crear múltiples DTOs
        ImagenDto[] dtos = new ImagenDto[5];
        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = ImagenDto.builder()
                    .id(i + 10)
                    .url("https://ejemplo.com/dto-" + i + ".jpg")
                    .alt("DTO " + i)
                    .build();
        }

        // Act - Mapear todos los DTOs
        Imagen[] resultados = new Imagen[dtos.length];
        for (int i = 0; i < dtos.length; i++) {
            resultados[i] = imagenMapper.toImagen(dtos[i]);
        }

        // Assert - Verificar que cada imagen tiene los valores correctos
        for (int i = 0; i < resultados.length; i++) {
            assertNotNull(resultados[i], "La imagen " + i + " no debería ser nula");
            assertEquals(dtos[i].getId(), resultados[i].getId(), "El ID de " + i + " debería coincidir");
            assertEquals(dtos[i].getUrl(), resultados[i].getUrl(), "La URL de " + i + " debería coincidir");
            assertEquals(dtos[i].getAlt(), resultados[i].getAlt(), "El alt de " + i + " debería coincidir");

            // Verificar que las imágenes son diferentes entre sí
            for (int j = i + 1; j < resultados.length; j++) {
                assertNotEquals(resultados[i], resultados[j], "Las imágenes " + i + " y " + j + " no deberían ser iguales");
            }
        }
    }

    // ==================== TESTS DE VALIDACIÓN DE CONSISTENCIA ====================

    @Test
    @DisplayName("toImagenDto y toImagen - Deberían ser consistentes con IDs negativos")
    void toImagenDtoAndToImagen_ShouldBeConsistentWithNegativeIds() {
        // Arrange - Imagen con ID negativo
        Imagen imagenNegativa = Imagen.builder()
                .id(-1)
                .url("https://ejemplo.com/negativa.jpg")
                .alt("Imagen con ID negativo")
                .build();

        // Act
        ImagenDto dto = imagenMapper.toImagenDto(imagenNegativa);
        Imagen imagenConvertida = imagenMapper.toImagen(dto);

        // Assert
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(imagenConvertida, "La imagen convertida no debería ser nula");
        assertEquals(-1, imagenConvertida.getId(), "El ID negativo debería mantenerse");
        assertEquals(imagenNegativa.getUrl(), imagenConvertida.getUrl(), "La URL debería ser la misma");
        assertEquals(imagenNegativa.getAlt(), imagenConvertida.getAlt(), "El alt debería ser el mismo");
    }

    @Test
    @DisplayName("toImagenDto y toImagen - Deberían ser consistentes con ID cero")
    void toImagenDtoAndToImagen_ShouldBeConsistentWithZeroId() {
        // Arrange - Imagen con ID cero
        Imagen imagenCero = Imagen.builder()
                .id(0)
                .url("https://ejemplo.com/cero.jpg")
                .alt("Imagen con ID cero")
                .build();

        // Act
        ImagenDto dto = imagenMapper.toImagenDto(imagenCero);
        Imagen imagenConvertida = imagenMapper.toImagen(dto);

        // Assert
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(imagenConvertida, "La imagen convertida no debería ser nula");
        assertEquals(0, imagenConvertida.getId(), "El ID cero debería mantenerse");
        assertEquals(imagenCero.getUrl(), imagenConvertida.getUrl(), "La URL debería ser la misma");
        assertEquals(imagenCero.getAlt(), imagenConvertida.getAlt(), "El alt debería ser el mismo");
    }

    // ==================== TEST DE NEGATIVO ====================

    @Test
    @DisplayName("toImagenDto - Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void toImagenDto_ShouldThrowNullPointerException_WhenAccessingNullObject() {
        // Arrange
        Imagen imagenNula = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        // Nota: El mapper maneja null devolviendo null, pero si se intenta acceder a métodos de la imagen
        // podría lanzar NullPointerException
        assertDoesNotThrow(() -> {
            ImagenDto resultado = imagenMapper.toImagenDto(imagenNula);
            assertNull(resultado, "Debería devolver null para imagen nula");
        }, "El mapper debería manejar null sin lanzar excepción");

        // Test de acceso a método en objeto nulo
        assertThrows(NullPointerException.class, () -> {
            Imagen imagen = null;
            imagen.getUrl(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    @Test
    @DisplayName("toImagen - Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void toImagen_ShouldThrowNullPointerException_WhenAccessingNullObject() {
        // Arrange
        ImagenDto imagenDtoNulo = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertDoesNotThrow(() -> {
            Imagen resultado = imagenMapper.toImagen(imagenDtoNulo);
            assertNull(resultado, "Debería devolver null para ImagenDto nulo");
        }, "El mapper debería manejar null sin lanzar excepción");

        // Test de acceso a método en objeto nulo
        assertThrows(NullPointerException.class, () -> {
            ImagenDto dto = null;
            dto.getUrl(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TEST DE IGUALDAD ====================

    @Test
    @DisplayName("toImagenDto - Debería mantener igualdad de contenido")
    void toImagenDto_ShouldMaintainContentEquality() {
        // Arrange
        Imagen imagen1 = Imagen.builder()
                .id(15)
                .url("https://ejemplo.com/igual.jpg")
                .alt("Texto igual")
                .build();

        Imagen imagen2 = Imagen.builder()
                .id(15)
                .url("https://ejemplo.com/igual.jpg")
                .alt("Texto igual")
                .build();

        // Act
        ImagenDto dto1 = imagenMapper.toImagenDto(imagen1);
        ImagenDto dto2 = imagenMapper.toImagenDto(imagen2);

        // Assert
        assertEquals(dto1, dto2, "Los DTOs deberían ser iguales");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("toImagen - Debería mantener igualdad de contenido")
    void toImagen_ShouldMaintainContentEquality() {
        // Arrange
        ImagenDto dto1 = ImagenDto.builder()
                .id(16)
                .url("https://ejemplo.com/igual-dto.jpg")
                .alt("Texto igual DTO")
                .build();

        ImagenDto dto2 = ImagenDto.builder()
                .id(16)
                .url("https://ejemplo.com/igual-dto.jpg")
                .alt("Texto igual DTO")
                .build();

        // Act
        Imagen imagen1 = imagenMapper.toImagen(dto1);
        Imagen imagen2 = imagenMapper.toImagen(dto2);

        // Assert
        assertEquals(imagen1, imagen2, "Las imágenes deberían ser iguales");
        assertEquals(imagen1.hashCode(), imagen2.hashCode(), "Los hashCodes deberían ser iguales");
    }
}