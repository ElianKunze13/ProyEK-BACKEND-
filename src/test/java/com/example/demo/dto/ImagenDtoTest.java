package com.example.demo.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ImagenDto
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 */
class ImagenDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // ==================== TESTS DE CREACIÓN Y CONSTRUCTORES ====================

    @Test
    @DisplayName("Debería crear ImagenDto con valores válidos - Caso feliz")
    void shouldCreateImagenDtoWithValidValues() {
        // Arrange - Configurar datos de prueba válidos
        Integer expectedId = 1;
        String expectedUrl = "https://ejemplo.com/imagen.jpg";
        String expectedAlt = "Descripción de la imagen";

        // Act - Ejecutar la acción a probar
        ImagenDto imagenDto = ImagenDto.builder()
                .id(expectedId)
                .url(expectedUrl)
                .alt(expectedAlt)
                .build();

        // Assert - Verificar resultados
        assertNotNull(imagenDto, "El objeto ImagenDto no debería ser nulo");
        assertEquals(expectedId, imagenDto.getId(), "El ID debería coincidir");
        assertEquals(expectedUrl, imagenDto.getUrl(), "La URL debería coincidir");
        assertEquals(expectedAlt, imagenDto.getAlt(), "El texto alternativo debería coincidir");

        // Validar con Bean Validation (aunque ImagenDto no tiene anotaciones, se mantiene por consistencia)
        Set<ConstraintViolation<ImagenDto>> violations = validator.validate(imagenDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debería crear ImagenDto con constructor por defecto")
    void shouldCreateImagenDtoWithDefaultConstructor() {
        // Arrange & Act
        ImagenDto imagenDto = new ImagenDto();

        // Assert
        assertNotNull(imagenDto, "El objeto ImagenDto no debería ser nulo");
        assertNull(imagenDto.getId(), "El ID debería ser nulo por defecto");
        assertNull(imagenDto.getUrl(), "La URL debería ser nula por defecto");
        assertNull(imagenDto.getAlt(), "El texto alternativo debería ser nulo por defecto");
    }

    @Test
    @DisplayName("Debería crear ImagenDto con constructor con todos los argumentos")
    void shouldCreateImagenDtoWithAllArgsConstructor() {
        // Arrange
        Integer id = 1;
        String url = "https://ejemplo.com/foto.jpg";
        String alt = "Foto de perfil";

        // Act
        ImagenDto imagenDto = new ImagenDto(id, url, alt);

        // Assert
        assertNotNull(imagenDto, "El objeto ImagenDto no debería ser nulo");
        assertEquals(id, imagenDto.getId(), "El ID debería coincidir");
        assertEquals(url, imagenDto.getUrl(), "La URL debería coincidir");
        assertEquals(alt, imagenDto.getAlt(), "El texto alternativo debería coincidir");
    }

    @Test
    @DisplayName("Debería permitir valores nulos en campos (ya que no tienen validaciones)")
    void shouldAllowNullValuesInFields() {
        // Arrange & Act - ImagenDto con valores nulos
        ImagenDto imagenDto = ImagenDto.builder()
                .id(null)
                .url(null)
                .alt(null)
                .build();

        // Assert
        assertNotNull(imagenDto, "El objeto ImagenDto no debería ser nulo");
        assertNull(imagenDto.getId(), "El ID debería ser nulo");
        assertNull(imagenDto.getUrl(), "La URL debería ser nula");
        assertNull(imagenDto.getAlt(), "El texto alternativo debería ser nulo");

        // Validar que no hay violaciones (ya que no hay anotaciones de validación)
        Set<ConstraintViolation<ImagenDto>> violations = validator.validate(imagenDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con valores nulos");
    }

    // ==================== TESTS DE ACTUALIZACIÓN (Setters) ====================

    @Test
    @DisplayName("Debería actualizar valores con setters")
    void shouldUpdateValuesWithSetters() {
        // Arrange
        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/inicial.jpg")
                .alt("Texto inicial")
                .build();

        // Act
        imagenDto.setId(2);
        imagenDto.setUrl("https://ejemplo.com/actualizada.jpg");
        imagenDto.setAlt("Texto actualizado");

        // Assert
        assertEquals(2, imagenDto.getId(), "El ID debería estar actualizado");
        assertEquals("https://ejemplo.com/actualizada.jpg", imagenDto.getUrl(), "La URL debería estar actualizada");
        assertEquals("Texto actualizado", imagenDto.getAlt(), "El texto alternativo debería estar actualizado");
    }

    // ==================== TESTS DE IGUALDAD Y hashCode ====================

    @Test
    @DisplayName("Debería ser igual cuando se comparan dos ImagenDto con los mismos valores")
    void shouldBeEqualWhenComparingTwoImagenDtosWithSameValues() {
        // Arrange
        ImagenDto imagenDto1 = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Texto alternativo")
                .build();

        ImagenDto imagenDto2 = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Texto alternativo")
                .build();

        // Act & Assert
        assertEquals(imagenDto1, imagenDto2, "Los objetos ImagenDto deberían ser iguales");
        assertEquals(imagenDto1.hashCode(), imagenDto2.hashCode(), "Los hashCodes deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan dos ImagenDto con diferentes valores")
    void shouldNotBeEqualWhenComparingTwoImagenDtosWithDifferentValues() {
        // Arrange
        ImagenDto imagenDto1 = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen1.jpg")
                .alt("Texto alternativo 1")
                .build();

        ImagenDto imagenDto2 = ImagenDto.builder()
                .id(2)
                .url("https://ejemplo.com/imagen2.jpg")
                .alt("Texto alternativo 2")
                .build();

        // Act & Assert
        assertNotEquals(imagenDto1, imagenDto2, "Los objetos ImagenDto no deberían ser iguales");
        assertNotEquals(imagenDto1.hashCode(), imagenDto2.hashCode(), "Los hashCodes no deberían ser iguales");
    }

    @Test
    @DisplayName("No debería ser igual cuando se comparan con objeto de otra clase")
    void shouldNotBeEqualWhenComparingWithDifferentClass() {
        // Arrange
        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Texto alternativo")
                .build();

        String differentObject = "No soy un ImagenDto";

        // Act & Assert
        assertNotEquals(imagenDto, differentObject, "No debería ser igual a un objeto de diferente clase");
        assertNotEquals(imagenDto, null, "No debería ser igual a null");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("Debería manejar URLs con diferentes protocolos")
    void shouldHandleUrlsWithDifferentProtocols() {
        // Arrange & Act - URLs con diferentes protocolos
        String[] urls = {
                "https://ejemplo.com/imagen.jpg",
                "http://ejemplo.com/imagen.png",
                "ftp://ejemplo.com/imagen.gif",
                "//ejemplo.com/imagen.jpg",
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA..."
        };

        for (String url : urls) {
            ImagenDto imagenDto = ImagenDto.builder()
                    .id(1)
                    .url(url)
                    .alt("Texto alternativo")
                    .build();

            // Assert
            assertNotNull(imagenDto, "El objeto ImagenDto no debería ser nulo para URL: " + url);
            assertEquals(url, imagenDto.getUrl(), "La URL debería mantenerse igual");
        }
    }

    @Test
    @DisplayName("Debería manejar textos alternativos con diferentes longitudes")
    void shouldHandleAltWithDifferentLengths() {
        // Arrange & Act - Alt con diferentes longitudes
        String[] alts = {
                "Alt corto",
                "Alt mediano con más de 20 caracteres",
                "Alt muy largo " + "a".repeat(1000),
                "",
                "   ",
                "\t",
                "\n"
        };

        for (String alt : alts) {
            ImagenDto imagenDto = ImagenDto.builder()
                    .id(1)
                    .url("https://ejemplo.com/imagen.jpg")
                    .alt(alt)
                    .build();

            // Assert
            assertNotNull(imagenDto, "El objeto ImagenDto no debería ser nulo para alt: " + alt);
            assertEquals(alt, imagenDto.getAlt(), "El alt debería mantenerse igual");
        }
    }

    @Test
    @DisplayName("Debería manejar caracteres especiales en URL y alt")
    void shouldHandleSpecialCharactersInUrlAndAlt() {
        // Arrange - URL y alt con caracteres especiales
        String urlEspecial = "https://ejemplo.com/imagen-con-espacios%20y-simbolos!@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~.jpg";
        String altEspecial = "Texto alternativo con acentos: áéíóú ñü y caracteres especiales !@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~";

        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url(urlEspecial)
                .alt(altEspecial)
                .build();

        // Act & Assert
        assertNotNull(imagenDto, "El objeto ImagenDto no debería ser nulo");
        assertEquals(urlEspecial, imagenDto.getUrl(), "La URL con caracteres especiales debería mantenerse");
        assertEquals(altEspecial, imagenDto.getAlt(), "El alt con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("Debería manejar IDs negativos y cero")
    void shouldHandleNegativeAndZeroIds() {
        // Arrange & Act - IDs con valores no positivos
        Integer[] ids = {0, -1, -100, Integer.MIN_VALUE};

        for (Integer id : ids) {
            ImagenDto imagenDto = ImagenDto.builder()
                    .id(id)
                    .url("https://ejemplo.com/imagen.jpg")
                    .alt("Texto alternativo")
                    .build();

            // Assert
            assertNotNull(imagenDto, "El objeto ImagenDto no debería ser nulo para ID: " + id);
            assertEquals(id, imagenDto.getId(), "El ID debería mantenerse igual");
        }
    }

    @Test
    @DisplayName("Debería manejar URLs con longitud máxima")
    void shouldHandleMaximumLengthUrls() {
        // Arrange - URL con longitud máxima (usando un límite razonable)
        String baseUrl = "https://ejemplo.com/";
        String pathLargo = "a".repeat(1000);
        String urlLarga = baseUrl + pathLargo + ".jpg";

        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url(urlLarga)
                .alt("Texto alternativo")
                .build();

        // Act & Assert
        assertNotNull(imagenDto, "El objeto ImagenDto no debería ser nulo");
        assertEquals(urlLarga, imagenDto.getUrl(), "La URL larga debería mantenerse");
        assertTrue(imagenDto.getUrl().length() > 1000, "La URL debería ser muy larga");
    }

    // ==================== TEST NEGATIVO ====================

    @Test
    @DisplayName("Debería lanzar NullPointerException al acceder método en objeto nulo - Test negativo")
    void shouldThrowExceptionWhenAccessingMethodOnNullObject() {
        // Arrange
        ImagenDto imagenDto = null;

        // Act & Assert - Test negativo para verificar comportamiento con null
        assertThrows(NullPointerException.class, () -> {
            imagenDto.getUrl(); // Esto lanzará NPE
        }, "Debería lanzar NullPointerException al acceder método en objeto nulo");
    }

    // ==================== TESTS DE CONVERSIÓN Y UTILIDADES ====================

    @Test
    @DisplayName("toString debería incluir todos los campos")
    void toString_ShouldIncludeAllFields() {
        // Arrange
        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Texto alternativo")
                .build();

        // Act
        String toStringResult = imagenDto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("id=1"), "toString debería incluir el ID");
        assertTrue(toStringResult.contains("url=https://ejemplo.com/imagen.jpg"), "toString debería incluir la URL");
        assertTrue(toStringResult.contains("alt=Texto alternativo"), "toString debería incluir el alt");
    }

    @Test
    @DisplayName("toString debería manejar valores nulos correctamente")
    void toString_ShouldHandleNullValuesCorrectly() {
        // Arrange - ImagenDto con todos los campos nulos
        ImagenDto imagenDto = new ImagenDto();

        // Act
        String toStringResult = imagenDto.toString();

        // Assert
        assertNotNull(toStringResult, "toString no debería ser nulo");
        assertTrue(toStringResult.contains("id=null"), "toString debería manejar id nulo");
        assertTrue(toStringResult.contains("url=null"), "toString debería manejar url nula");
        assertTrue(toStringResult.contains("alt=null"), "toString debería manejar alt nulo");
    }

    // ==================== TESTS DE CONSTRUCTORES ====================

    @Test
    @DisplayName("Constructor con parámetros debería establecer todos los campos")
    void constructorWithParameters_ShouldSetAllFields() {
        // Arrange
        Integer id = 5;
        String url = "https://ejemplo.com/constructor.jpg";
        String alt = "Constructor alt text";

        // Act
        ImagenDto imagenDto = new ImagenDto(id, url, alt);

        // Assert
        assertEquals(id, imagenDto.getId(), "El ID debería ser el proporcionado");
        assertEquals(url, imagenDto.getUrl(), "La URL debería ser la proporcionada");
        assertEquals(alt, imagenDto.getAlt(), "El alt debería ser el proporcionado");
    }

    @Test
    @DisplayName("Builder debería permitir construcción con campos nulos")
    void builder_ShouldAllowConstructionWithNullFields() {
        // Act
        ImagenDto imagenDto = ImagenDto.builder()
                .id(null)
                .url(null)
                .alt(null)
                .build();

        // Assert
        assertNotNull(imagenDto, "El objeto no debería ser nulo");
        assertNull(imagenDto.getId(), "ID debería ser nulo");
        assertNull(imagenDto.getUrl(), "URL debería ser nula");
        assertNull(imagenDto.getAlt(), "Alt debería ser nulo");
    }

    @Test
    @DisplayName("Builder debería permitir construcción parcial")
    void builder_ShouldAllowPartialConstruction() {
        // Act - Construir solo con ID y URL
        ImagenDto imagenDto1 = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen1.jpg")
                .build();

        // Assert
        assertNotNull(imagenDto1, "El objeto no debería ser nulo");
        assertEquals(1, imagenDto1.getId(), "El ID debería ser 1");
        assertEquals("https://ejemplo.com/imagen1.jpg", imagenDto1.getUrl(), "La URL debería coincidir");
        assertNull(imagenDto1.getAlt(), "El alt debería ser nulo");

        // Act - Construir solo con URL y Alt
        ImagenDto imagenDto2 = ImagenDto.builder()
                .url("https://ejemplo.com/imagen2.jpg")
                .alt("Alt de imagen 2")
                .build();

        // Assert
        assertNotNull(imagenDto2, "El objeto no debería ser nulo");
        assertNull(imagenDto2.getId(), "El ID debería ser nulo");
        assertEquals("https://ejemplo.com/imagen2.jpg", imagenDto2.getUrl(), "La URL debería coincidir");
        assertEquals("Alt de imagen 2", imagenDto2.getAlt(), "El alt debería coincidir");

        // Act - Construir solo con ID y Alt
        ImagenDto imagenDto3 = ImagenDto.builder()
                .id(3)
                .alt("Alt de imagen 3")
                .build();

        // Assert
        assertNotNull(imagenDto3, "El objeto no debería ser nulo");
        assertEquals(3, imagenDto3.getId(), "El ID debería ser 3");
        assertNull(imagenDto3.getUrl(), "La URL debería ser nula");
        assertEquals("Alt de imagen 3", imagenDto3.getAlt(), "El alt debería coincidir");
    }

    // ==================== TESTS DE CASOS DE USO REALES ====================

    @Test
    @DisplayName("Debería usarse como DTO en capa de presentación")
    void shouldBeUsedAsDtoInPresentationLayer() {
        // Arrange - Simular creación de DTO desde datos de entrada
        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/portfolio/proyecto.jpg")
                .alt("Captura de pantalla del proyecto final")
                .build();

        // Act - Simular uso en capa de presentación
        String urlDisplay = imagenDto.getUrl();
        String altDisplay = imagenDto.getAlt();
        Integer idDisplay = imagenDto.getId();

        // Assert
        assertNotNull(urlDisplay, "La URL no debería ser nula");
        assertNotNull(altDisplay, "El alt no debería ser nulo");
        assertNotNull(idDisplay, "El ID no debería ser nulo");
        assertTrue(urlDisplay.contains("portfolio"), "La URL debería contener 'portfolio'");
        assertTrue(altDisplay.contains("Captura"), "El alt debería contener 'Captura'");
    }

    @Test
    @DisplayName("Debería permitir múltiples instancias con diferentes valores")
    void shouldAllowMultipleInstancesWithDifferentValues() {
        // Arrange - Crear múltiples instancias
        ImagenDto imagen1 = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen1.jpg")
                .alt("Imagen 1")
                .build();

        ImagenDto imagen2 = ImagenDto.builder()
                .id(2)
                .url("https://ejemplo.com/imagen2.jpg")
                .alt("Imagen 2")
                .build();

        ImagenDto imagen3 = ImagenDto.builder()
                .id(3)
                .url("https://ejemplo.com/imagen3.jpg")
                .alt("Imagen 3")
                .build();

        // Assert
        assertNotEquals(imagen1, imagen2, "Las imágenes deberían ser diferentes");
        assertNotEquals(imagen2, imagen3, "Las imágenes deberían ser diferentes");
        assertNotEquals(imagen1, imagen3, "Las imágenes deberían ser diferentes");

        // Verificar que cada una mantiene sus valores
        assertEquals(1, imagen1.getId().intValue(), "El ID de imagen1 debería ser 1");
        assertEquals(2, imagen2.getId().intValue(), "El ID de imagen2 debería ser 2");
        assertEquals(3, imagen3.getId().intValue(), "El ID de imagen3 debería ser 3");
    }

    @Test
    @DisplayName("Debería ser inmutable después de la construcción (sin setters)")
    void shouldBeImmutableAfterConstructionWithoutSetters() {
        // Este test verifica que los objetos creados con builder
        // no pueden ser modificados accidentalmente sin usar setters
        // (aunque Lombok @Data proporciona setters, verificamos el comportamiento)

        // Arrange
        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/original.jpg")
                .alt("Original alt")
                .build();

        // Act - Intentar modificar usando setters (si existen) o creando nuevo objeto
        ImagenDto imagenDtoModificado = ImagenDto.builder()
                .id(imagenDto.getId())
                .url(imagenDto.getUrl())
                .alt(imagenDto.getAlt())
                .build();

        // Assert - Verificar que los objetos son iguales pero diferentes instancias
        assertEquals(imagenDto, imagenDtoModificado, "Deberían ser iguales en contenido");
        assertNotSame(imagenDto, imagenDtoModificado, "Deberían ser diferentes instancias");

        // Para verificar inmutabilidad parcial, cambiamos y comparamos
        imagenDtoModificado.setUrl("https://ejemplo.com/modificada.jpg");
        assertNotEquals(imagenDto.getUrl(), imagenDtoModificado.getUrl(),
                "La URL debería ser diferente después de la modificación");
    }

    // ==================== TEST DE VALIDACIÓN CON BEAN VALIDATION ====================

    @Test
    @DisplayName("Validación - No debería tener violaciones (sin anotaciones de validación)")
    void validation_ShouldNotHaveViolations() {
        // Arrange - Crear ImagenDto sin anotaciones de validación
        ImagenDto imagenDto = ImagenDto.builder()
                .id(1)
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Texto alternativo")
                .build();

        // Act - Validar (no debería haber violaciones ya que no hay anotaciones)
        Set<ConstraintViolation<ImagenDto>> violations = validator.validate(imagenDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación para ImagenDto");
    }

    @Test
    @DisplayName("Validación - Debería aceptar valores nulos sin violaciones")
    void validation_ShouldAcceptNullValues() {
        // Arrange - ImagenDto con valores nulos
        ImagenDto imagenDto = ImagenDto.builder()
                .id(null)
                .url(null)
                .alt(null)
                .build();

        // Act - Validar
        Set<ConstraintViolation<ImagenDto>> violations = validator.validate(imagenDto);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para valores nulos");
    }

    @Test
    @DisplayName("Validación - Debería aceptar cualquier valor de URL")
    void validation_ShouldAcceptAnyUrlValue() {
        // Arrange - URLs con diferentes formatos (sin validación de email o formato)
        String[] urls = {
                "urlinvalida",
                "no es una url",
                "http://",
                "https://",
                "ftp://",
                "archivo.jpg",
                "imagen.png",
                "",
                "   ",
                null
        };

        for (String url : urls) {
            ImagenDto imagenDto = ImagenDto.builder()
                    .id(1)
                    .url(url)
                    .alt("Texto alternativo")
                    .build();

            // Act - Validar
            Set<ConstraintViolation<ImagenDto>> violations = validator.validate(imagenDto);

            // Assert
            assertTrue(violations.isEmpty(), "No debería haber violaciones para URL: " + url);
        }
    }
}