package com.example.demo.service;

import com.example.demo.dto.ImagenDto;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.service.Impl.ImagenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ImagenServiceImplIntegrationTest {

    @Autowired
    private ImagenServiceImpl imagenService;

    @Autowired
    private ImagenRepository imagenRepository;

    private ImagenDto crearImagenDto(String url, String alt) {
        return ImagenDto.builder()
                .url(url)
                .alt(alt)
                .build();
    }

    @BeforeEach
    void setUp() {
        // Limpiar datos antes de cada prueba
        imagenRepository.deleteAll();
    }

    @Test
    void saveImagen_conDatosValidos_debeGuardarYRetornarImagen() {
        // ARRANGE - contexto: imagen con datos válidos
        ImagenDto imagenDto = crearImagenDto(
                "https://example.com/imagen.jpg",
                "Descripción de la imagen"
        );

        // ACT - acción: guardar imagen
        ImagenDto resultado = imagenService.saveImagen(imagenDto);

        // ASSERT - validaciones: verificar que la imagen fue guardada correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("https://example.com/imagen.jpg", resultado.getUrl(),
                        "URL debe coincidir"),
                () -> assertEquals("Descripción de la imagen", resultado.getAlt(),
                        "Alt debe coincidir"),
                () -> assertTrue(imagenRepository.existsById(resultado.getId()),
                        "Imagen debe existir en la base de datos")
        );
    }

    @Test
    void saveImagen_conUrlNula_debeLanzarExcepcion() {
        // ARRANGE - contexto: imagen con URL nula
        ImagenDto imagenDto = ImagenDto.builder()
                .url(null)
                .alt("Descripción de la imagen")
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            imagenService.saveImagen(imagenDto);
        }, "Debe lanzar excepción cuando la URL es nula");
    }

    @Test
    void saveImagen_conUrlVacia_debeLanzarExcepcion() {
        // ARRANGE - contexto: imagen con URL vacía
        ImagenDto imagenDto = crearImagenDto("", "Descripción de la imagen");

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            imagenService.saveImagen(imagenDto);
        }, "Debe lanzar excepción cuando la URL está vacía");
    }

    @Test
    void saveImagen_conAltNula_debeLanzarExcepcion() {
        // ARRANGE - contexto: imagen con alt nulo
        ImagenDto imagenDto = ImagenDto.builder()
                .url("https://example.com/imagen.jpg")
                .alt(null)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            imagenService.saveImagen(imagenDto);
        }, "Debe lanzar excepción cuando el alt es nulo");
    }

    @Test
    void saveImagen_conAltVacio_debeLanzarExcepcion() {
        // ARRANGE - contexto: imagen con alt vacío
        ImagenDto imagenDto = crearImagenDto("https://example.com/imagen.jpg", "");

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            imagenService.saveImagen(imagenDto);
        }, "Debe lanzar excepción cuando el alt está vacío");
    }

    @Test
    void saveImagen_conUrlConCaracteresEspeciales_debeGuardarCorrectamente() {
        // ARRANGE - contexto: URL con caracteres especiales
        ImagenDto imagenDto = crearImagenDto(
                "https://example.com/imagen_con_espacios_y_caracteres_especiales.jpg",
                "Descripción con caracteres especiales: áéíóú ñ"
        );

        // ACT - acción: guardar imagen
        ImagenDto resultado = imagenService.saveImagen(imagenDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("https://example.com/imagen_con_espacios_y_caracteres_especiales.jpg",
                        resultado.getUrl(), "URL debe coincidir"),
                () -> assertEquals("Descripción con caracteres especiales: áéíóú ñ",
                        resultado.getAlt(), "Alt debe coincidir"),
                () -> assertTrue(imagenRepository.existsById(resultado.getId()),
                        "Imagen debe existir en la BD")
        );
    }

    @Test
    void saveImagen_conUrlMuyLarga_debeGuardarCorrectamente() {
        // ARRANGE - contexto: URL muy larga
        String urlLarga = "https://example.com/" + "a".repeat(200) + ".jpg";
        ImagenDto imagenDto = crearImagenDto(urlLarga, "Descripción de la imagen");

        // ACT - acción: guardar imagen
        ImagenDto resultado = imagenService.saveImagen(imagenDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals(urlLarga, resultado.getUrl(), "URL debe coincidir"),
                () -> assertTrue(imagenRepository.existsById(resultado.getId()),
                        "Imagen debe existir en la BD")
        );
    }

    @Test
    void getAllImagen_debeRetornarTodasLasImagenes() {
        // ARRANGE - contexto: crear y guardar imágenes
        Imagen imagen1 = Imagen.builder()
                .url("https://example.com/imagen1.jpg")
                .alt("Imagen 1")
                .build();

        Imagen imagen2 = Imagen.builder()
                .url("https://example.com/imagen2.jpg")
                .alt("Imagen 2")
                .build();

        imagenRepository.save(imagen1);
        imagenRepository.save(imagen2);

        // ACT - acción: obtener todas las imágenes
        List<ImagenDto> imagenes = imagenService.getAllImagen();

        // ASSERT - validaciones: verificar que retorna todas las imágenes
        assertAll(
                () -> assertNotNull(imagenes, "Lista no debe ser null"),
                () -> assertFalse(imagenes.isEmpty(), "Lista no debe estar vacía"),
                () -> assertTrue(imagenes.size() >= 2, "Debe contener al menos 2 imágenes"),
                () -> assertTrue(imagenes.stream().anyMatch(i -> i.getUrl().equals("https://example.com/imagen1.jpg")),
                        "Debe contener imagen1"),
                () -> assertTrue(imagenes.stream().anyMatch(i -> i.getUrl().equals("https://example.com/imagen2.jpg")),
                        "Debe contener imagen2"),
                () -> assertNotNull(imagenes.get(0).getId(), "ID debe estar presente")
        );
    }

    @Test
    void getAllImagen_sinImagenes_debeRetornarListaVacia() {
        // ARRANGE - contexto: no hay imágenes en la base de datos
        assertEquals(0, imagenRepository.count(), "No debería haber imágenes inicialmente");

        // ACT - acción: obtener todas las imágenes
        List<ImagenDto> imagenes = imagenService.getAllImagen();

        // ASSERT - validaciones: debe retornar lista vacía
        assertNotNull(imagenes, "Lista no debe ser null");
        assertTrue(imagenes.isEmpty(), "Lista debe estar vacía");
        assertEquals(0, imagenes.size(), "Tamaño debe ser 0");
    }

    @Test
    void getAllImagen_conMultiplesImagenes_debeRetornarTodas() {
        // ARRANGE - contexto: crear y guardar múltiples imágenes
        for (int i = 1; i <= 5; i++) {
            Imagen imagen = Imagen.builder()
                    .url("https://example.com/imagen" + i + ".jpg")
                    .alt("Imagen " + i)
                    .build();
            imagenRepository.save(imagen);
        }

        // ACT - acción: obtener todas las imágenes
        List<ImagenDto> imagenes = imagenService.getAllImagen();

        // ASSERT - validaciones: debe retornar todas las imágenes
        assertAll(
                () -> assertNotNull(imagenes, "Lista no debe ser null"),
                () -> assertEquals(5, imagenes.size(), "Debe haber exactamente 5 imágenes"),
                () -> assertTrue(imagenes.stream().allMatch(i -> i.getId() != null),
                        "Todas las imágenes deben tener ID"),
                () -> assertTrue(imagenes.stream().anyMatch(i -> i.getUrl().equals("https://example.com/imagen3.jpg")),
                        "Debe contener imagen3"),
                () -> assertTrue(imagenes.stream().anyMatch(i -> i.getAlt().equals("Imagen 5")),
                        "Debe contener Imagen 5")
        );
    }

    @Test
    void deleteImagenPorId_conIdExistente_debeEliminarImagen() {
        // ARRANGE - contexto: crear y guardar imagen
        Imagen imagen = Imagen.builder()
                .url("https://example.com/imagen-eliminar.jpg")
                .alt("Imagen a eliminar")
                .build();

        Imagen guardado = imagenRepository.save(imagen);
        assertTrue(imagenRepository.existsById(guardado.getId()),
                "Imagen debe existir antes de eliminar");

        // ACT - acción: eliminar imagen
        imagenService.deleteImagenPorId(guardado.getId());

        // ASSERT - validaciones: imagen debe ser eliminada
        assertFalse(imagenRepository.existsById(guardado.getId()),
                "Imagen no debe existir después de eliminar");
        assertEquals(0, imagenRepository.count(), "No debe haber imágenes en la BD");
    }

    @Test
    void deleteImagenPorId_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(EmptyResultDataAccessException.class, () -> {
            imagenService.deleteImagenPorId(idInexistente);
        }, "Debe lanzar EmptyResultDataAccessException para ID inexistente");
    }

    @Test
    void saveImagen_conUrlYAltDuplicados_debeGuardarTodos() {
        // ARRANGE - contexto: imágenes con URL y Alt duplicados
        String url = "https://example.com/imagen-duplicada.jpg";
        String alt = "Imagen duplicada";

        // ACT - acción: guardar la misma imagen múltiples veces
        for (int i = 0; i < 3; i++) {
            ImagenDto imagenDto = crearImagenDto(url, alt);
            imagenService.saveImagen(imagenDto);
        }

        // ASSERT - validaciones: debe haber 3 imágenes con los mismos datos
        List<Imagen> imagenes = imagenRepository.findAll();
        assertEquals(3, imagenes.size(), "Debe haber 3 imágenes en la BD");

        long count = imagenes.stream()
                .filter(i -> i.getUrl().equals(url) && i.getAlt().equals(alt))
                .count();
        assertEquals(3, count, "Debe haber 3 imágenes con URL y Alt duplicados");
    }

    @Test
    void saveImagen_conUrlConProtocoloHttps_debeGuardarCorrectamente() {
        // ARRANGE - contexto: URL con protocolo HTTPS
        ImagenDto imagenDto = crearImagenDto(
                "https://secure.example.com/imagen-segura.jpg",
                "Imagen con protocolo HTTPS"
        );

        // ACT - acción: guardar imagen
        ImagenDto resultado = imagenService.saveImagen(imagenDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("https://secure.example.com/imagen-segura.jpg",
                        resultado.getUrl(), "URL debe coincidir"),
                () -> assertTrue(imagenRepository.existsById(resultado.getId()),
                        "Imagen debe existir en la BD")
        );
    }

    @Test
    void saveImagen_conUrlConProtocoloHttp_debeGuardarCorrectamente() {
        // ARRANGE - contexto: URL con protocolo HTTP
        ImagenDto imagenDto = crearImagenDto(
                "http://example.com/imagen-http.jpg",
                "Imagen con protocolo HTTP"
        );

        // ACT - acción: guardar imagen
        ImagenDto resultado = imagenService.saveImagen(imagenDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("http://example.com/imagen-http.jpg",
                        resultado.getUrl(), "URL debe coincidir"),
                () -> assertTrue(imagenRepository.existsById(resultado.getId()),
                        "Imagen debe existir en la BD")
        );
    }

    @Test
    void saveImagen_conUrlConExtensionesDiferentes_debeGuardarCorrectamente() {
        // ARRANGE - contexto: URL con diferentes extensiones
        String[] extensiones = {".jpg", ".png", ".gif", ".svg", ".webp"};

        for (String ext : extensiones) {
            ImagenDto imagenDto = crearImagenDto(
                    "https://example.com/imagen" + ext,
                    "Imagen con extensión " + ext
            );

            // ACT - acción: guardar imagen
            ImagenDto resultado = imagenService.saveImagen(imagenDto);

            // ASSERT - validaciones: debe guardar correctamente
            assertNotNull(resultado.getId(), "ID debe ser generado para extensión " + ext);
            assertTrue(imagenRepository.existsById(resultado.getId()),
                    "Imagen debe existir para extensión " + ext);
        }

        // Verificar que todas fueron guardadas
        assertEquals(extensiones.length, imagenRepository.count(),
                "Debe haber " + extensiones.length + " imágenes en la BD");
    }

    @Test
    void getAllImagen_despuesDeEliminar_debeRetornarSoloRestantes() {
        // ARRANGE - contexto: crear y guardar imágenes
        Imagen imagen1 = Imagen.builder()
                .url("https://example.com/imagen1.jpg")
                .alt("Imagen 1")
                .build();

        Imagen imagen2 = Imagen.builder()
                .url("https://example.com/imagen2.jpg")
                .alt("Imagen 2")
                .build();

        Imagen imagen3 = Imagen.builder()
                .url("https://example.com/imagen3.jpg")
                .alt("Imagen 3")
                .build();

        Imagen guardado1 = imagenRepository.save(imagen1);
        imagenRepository.save(imagen2);
        Imagen guardado3 = imagenRepository.save(imagen3);

        // ACT - acción: eliminar una imagen y obtener todas
        imagenService.deleteImagenPorId(guardado1.getId());
        List<ImagenDto> imagenes = imagenService.getAllImagen();

        // ASSERT - validaciones: solo deben quedar 2 imágenes
        assertAll(
                () -> assertEquals(2, imagenes.size(), "Debe haber 2 imágenes restantes"),
                () -> assertTrue(imagenes.stream().anyMatch(i -> i.getUrl().equals("https://example.com/imagen2.jpg")),
                        "Debe contener imagen2"),
                () -> assertTrue(imagenes.stream().anyMatch(i -> i.getUrl().equals("https://example.com/imagen3.jpg")),
                        "Debe contener imagen3"),
                () -> assertTrue(imagenes.stream().noneMatch(i -> i.getUrl().equals("https://example.com/imagen1.jpg")),
                        "No debe contener imagen1")
        );
    }

    @Test
    void saveImagen_conUrlYAltConEspaciosExtremos_debeGuardarCorrectamente() {
        // ARRANGE - contexto: URL y alt con espacios al inicio y final
        ImagenDto imagenDto = crearImagenDto(
                "  https://example.com/imagen.jpg  ",
                "  Descripción con espacios  "
        );

        // ACT - acción: guardar imagen
        ImagenDto resultado = imagenService.saveImagen(imagenDto);

        // ASSERT - validaciones: debe guardar con los espacios (no se recortan automáticamente)
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("  https://example.com/imagen.jpg  ", resultado.getUrl(),
                        "URL debe conservar los espacios"),
                () -> assertEquals("  Descripción con espacios  ", resultado.getAlt(),
                        "Alt debe conservar los espacios"),
                () -> assertTrue(imagenRepository.existsById(resultado.getId()),
                        "Imagen debe existir en la BD")
        );
    }
}