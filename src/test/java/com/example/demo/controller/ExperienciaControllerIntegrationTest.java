package com.example.demo.controller;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import com.example.demo.model.Experiencia;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ExperienciaRepository;
import com.example.demo.repository.ImagenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ExperienciaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExperienciaRepository experienciaRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    // Helper methods actualizadas para trabajar con lista de imágenes
    private ExperienciaDto crearExperienciaDto(String titulo, LocalDate fechaInicio, LocalDate fechaFin,
                                               String descripcion, String link, TipoExperiencia tipo,
                                               TecnologiaUsada... tecnologias) {
        return ExperienciaDto.builder()
                .titulo(titulo)
                .fechaInicioProyecto(fechaInicio)
                .fechaFinProyecto(fechaFin)
                .descripcion(descripcion)
                .link(link)
                .tipoExperiencia(tipo)
                .tecnologiasUsadas(Arrays.asList(tecnologias))
                .build();
    }

    private ExperienciaDto crearExperienciaDtoConImagenes(String titulo, LocalDate fechaInicio, LocalDate fechaFin,
                                                          String descripcion, String link, TipoExperiencia tipo,
                                                          List<ImagenDto> imagenes, TecnologiaUsada... tecnologias) {
        ExperienciaDto dto = crearExperienciaDto(titulo, fechaInicio, fechaFin, descripcion, link, tipo, tecnologias);
        if (imagenes != null && !imagenes.isEmpty()) {
            dto.setImagenes(imagenes);
        }
        return dto;
    }

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
        experienciaRepository.deleteAll();
    }

    // ==================== TESTS DE GUARDADO ====================

    @Test
    void saveExperiencia_conDatosValidosSinImagenes_debeRetornarExperienciaCreada() throws Exception {
        // ARRANGE - contexto: experiencia con datos válidos sin imágenes
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Portfolio Personal",
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 6, 30),
                "Desarrollo de portfolio personal con Angular y Spring Boot",
                "https://github.com/mi-portfolio",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.ANGULAR,
                TecnologiaUsada.SPRINGBOOT
        );

        // ACT - acción: guardar experiencia a través del endpoint
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Portfolio Personal", responseDto.getTitulo(), "Título debe coincidir"),
                () -> assertEquals(LocalDate.of(2024, 1, 15), responseDto.getFechaInicioProyecto()),
                () -> assertEquals(LocalDate.of(2024, 6, 30), responseDto.getFechaFinProyecto()),
                () -> assertEquals("Desarrollo de portfolio personal con Angular y Spring Boot",
                        responseDto.getDescripcion()),
                () -> assertEquals("https://github.com/mi-portfolio", responseDto.getLink()),
                () -> assertEquals(TipoExperiencia.PROYECTO_PERSONAL, responseDto.getTipoExperiencia()),
                () -> assertNotNull(responseDto.getTecnologiasUsadas()),
                () -> assertEquals(2, responseDto.getTecnologiasUsadas().size()),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.ANGULAR)),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT)),
                // Verificar que no hay imágenes
                () -> assertNull(responseDto.getImagenes(), "No debe haber imágenes"),
                () -> assertTrue(experienciaRepository.existsById(responseDto.getId()))
        );
    }

    @Test
    void saveExperiencia_conUnaImagen_debeRetornarExperienciaConImagen() throws Exception {
        // ARRANGE - contexto: experiencia con una imagen
        List<ImagenDto> imagenes = List.of(
                crearImagenDto("https://example.com/app-tareas.jpg", "Captura de la aplicación de tareas")
        );

        ExperienciaDto experienciaDto = crearExperienciaDtoConImagenes(
                "App de Tareas",
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 4, 30),
                "Aplicación de gestión de tareas con React y Node.js",
                "https://github.com/app-tareas",
                TipoExperiencia.PROYECTO_PERSONAL,
                imagenes,
                TecnologiaUsada.REACT,
                TecnologiaUsada.TYPESCRIPT
        );

        // ACT - acción: guardar experiencia
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId()),
                () -> assertNotNull(responseDto.getImagenes(), "Imágenes deben estar presentes"),
                () -> assertEquals(1, responseDto.getImagenes().size(), "Debe tener 1 imagen"),
                () -> assertEquals("https://example.com/app-tareas.jpg",
                        responseDto.getImagenes().get(0).getUrl()),
                () -> assertEquals("Captura de la aplicación de tareas",
                        responseDto.getImagenes().get(0).getAlt()),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.REACT)),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.TYPESCRIPT))
        );
    }

    @Test
    void saveExperiencia_conMultiplesImagenes_debeRetornarExperienciaConTodasLasImagenes() throws Exception {
        // ARRANGE - contexto: experiencia con múltiples imágenes
        List<ImagenDto> imagenes = Arrays.asList(
                crearImagenDto("https://example.com/proyecto-1.jpg", "Vista principal del proyecto"),
                crearImagenDto("https://example.com/proyecto-2.jpg", "Vista detallada del proyecto"),
                crearImagenDto("https://example.com/proyecto-3.jpg", "Arquitectura del proyecto")
        );

        ExperienciaDto experienciaDto = crearExperienciaDtoConImagenes(
                "Proyecto Full Stack",
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 5, 31),
                "Proyecto completo con múltiples capturas",
                "https://github.com/fullstack-project",
                TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO,
                imagenes,
                TecnologiaUsada.JAVA,
                TecnologiaUsada.SPRINGBOOT,
                TecnologiaUsada.REACT
        );

        // ACT - acción: guardar experiencia
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId()),
                () -> assertNotNull(responseDto.getImagenes()),
                () -> assertEquals(3, responseDto.getImagenes().size(), "Debe tener 3 imágenes"),
                () -> assertEquals("https://example.com/proyecto-1.jpg", responseDto.getImagenes().get(0).getUrl()),
                () -> assertEquals("https://example.com/proyecto-2.jpg", responseDto.getImagenes().get(1).getUrl()),
                () -> assertEquals("https://example.com/proyecto-3.jpg", responseDto.getImagenes().get(2).getUrl()),
                () -> assertEquals(3, responseDto.getTecnologiasUsadas().size())
        );
    }

    @Test
    void saveExperiencia_conProyectoEnCurso_fechaFinNull_debeGuardarCorrectamente() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto en Curso",
                LocalDate.of(2024, 1, 15),
                null,
                "Descripción del proyecto en curso",
                "https://github.com/proyecto-curso",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.REACT
        );

        // ACT
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId()),
                () -> assertEquals("Proyecto en Curso", responseDto.getTitulo()),
                () -> assertNull(responseDto.getFechaFinProyecto()),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.REACT)),
                () -> assertNull(responseDto.getImagenes(), "No debe haber imágenes"),
                () -> assertTrue(experienciaRepository.existsById(responseDto.getId()))
        );
    }

    // ==================== TESTS DE VALIDACIÓN (ERRORES) ====================

    @Test
    void saveExperiencia_conTituloNulo_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo(null)
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conTituloVacio_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "",
                LocalDate.now(),
                LocalDate.now(),
                "Descripción válida",
                "https://github.com/test",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conFechaInicioNula_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(null)
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conDescripcionNula_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion(null)
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conDescripcionVacia_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto Test",
                LocalDate.now(),
                LocalDate.now(),
                "",
                "https://github.com/test",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conLinkNulo_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link(null)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conLinkVacio_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto Test",
                LocalDate.now(),
                LocalDate.now(),
                "Descripción válida",
                "",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conTipoExperienciaNulo_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(null)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conTecnologiasNulas_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(null)
                .build();

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conListaVaciaDeTecnologias_debeRetornarError() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of())
                .build();

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    // ==================== TESTS DE OBTENCIÓN ====================

    @Test
    void getAllExperiencias_debeRetornarTodasLasExperiencias() throws Exception {
        // ARRANGE - crear y guardar experiencias
        Experiencia experiencia1 = Experiencia.builder()
                .titulo("Portfolio Personal")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de portfolio personal")
                .link("https://github.com/portfolio1")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.ANGULAR, TecnologiaUsada.SPRINGBOOT))
                .build();

        Experiencia experiencia2 = Experiencia.builder()
                .titulo("App de Tareas")
                .fechaInicioProyecto(LocalDate.of(2024, 2, 1))
                .fechaFinProyecto(LocalDate.of(2024, 4, 30))
                .descripcion("Aplicación de gestión de tareas")
                .link("https://github.com/app-tareas")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.REACT, TecnologiaUsada.TYPESCRIPT))
                .build();

        experienciaRepository.save(experiencia1);
        experienciaRepository.save(experiencia2);

        // ACT
        String responseJson = mockMvc.perform(get("/api/v1/todas/experiencias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto[] experiencias = objectMapper.readValue(responseJson, ExperienciaDto[].class);

        assertAll(
                () -> assertNotNull(experiencias),
                () -> assertTrue(experiencias.length >= 2),
                () -> assertTrue(List.of(experiencias).stream().anyMatch(e -> e.getTitulo().equals("Portfolio Personal"))),
                () -> assertTrue(List.of(experiencias).stream().anyMatch(e -> e.getTitulo().equals("App de Tareas"))),
                () -> {
                    ExperienciaDto found = List.of(experiencias).stream()
                            .filter(e -> e.getTitulo().equals("Portfolio Personal"))
                            .findFirst()
                            .orElse(null);
                    assertNotNull(found);
                    assertTrue(found.getTecnologiasUsadas().contains(TecnologiaUsada.ANGULAR));
                    assertTrue(found.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT));
                }
        );
    }

    @Test
    void getAllExperiencias_sinExperiencias_debeRetornarListaVacia() throws Exception {
        // ARRANGE - no hay experiencias
        assertEquals(0, experienciaRepository.count());

        // ACT
        String responseJson = mockMvc.perform(get("/api/v1/todas/experiencias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto[] experiencias = objectMapper.readValue(responseJson, ExperienciaDto[].class);
        assertEquals(0, experiencias.length);
    }

    @Test
    void getAllExperiencias_conImagenes_debeRetornarExperienciasConImagenes() throws Exception {
        // ARRANGE - crear experiencia con imágenes
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto con Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 3, 1))
                .fechaFinProyecto(LocalDate.of(2024, 5, 15))
                .descripcion("Proyecto con múltiples imágenes representativas")
                .link("https://github.com/proyecto-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.SPRINGBOOT, TecnologiaUsada.JAVA))
                .build();

        // Crear imágenes
        Imagen imagen1 = Imagen.builder()
                .url("https://example.com/proyecto-1.jpg")
                .alt("Imagen principal del proyecto")
                .experiencia(experiencia)
                .build();

        Imagen imagen2 = Imagen.builder()
                .url("https://example.com/proyecto-2.jpg")
                .alt("Imagen secundaria del proyecto")
                .experiencia(experiencia)
                .build();

        experiencia.setImagenes(Arrays.asList(imagen1, imagen2));
        experienciaRepository.save(experiencia);

        // ACT
        String responseJson = mockMvc.perform(get("/api/v1/todas/experiencias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto[] experiencias = objectMapper.readValue(responseJson, ExperienciaDto[].class);

        assertAll(
                () -> assertFalse(experiencias.length == 0),
                () -> assertNotNull(experiencias[0].getImagenes()),
                () -> assertEquals(2, experiencias[0].getImagenes().size()),
                () -> assertEquals("https://example.com/proyecto-1.jpg",
                        experiencias[0].getImagenes().get(0).getUrl()),
                () -> assertEquals("https://example.com/proyecto-2.jpg",
                        experiencias[0].getImagenes().get(1).getUrl()),
                () -> assertTrue(experiencias[0].getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT)),
                () -> assertTrue(experiencias[0].getTecnologiasUsadas().contains(TecnologiaUsada.JAVA))
        );
    }

    // ==================== TESTS DE ACTUALIZACIÓN ====================

    @Test
    void updateExperiencia_conDatosValidosSinImagenes_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - crear experiencia inicial
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Original")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción original")
                .link("https://github.com/original")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        Experiencia guardado = experienciaRepository.save(experiencia);

        // Preparar datos actualizados
        ExperienciaDto experienciaDtoActualizado = crearExperienciaDto(
                "Proyecto Actualizado",
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 5, 31),
                "Descripción actualizada del proyecto",
                "https://github.com/actualizado",
                TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO,
                TecnologiaUsada.SPRINGBOOT,
                TecnologiaUsada.REACT
        );

        // ACT
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), experienciaActualizada.getId()),
                () -> assertEquals("Proyecto Actualizado", experienciaActualizada.getTitulo()),
                () -> assertEquals(LocalDate.of(2024, 2, 1), experienciaActualizada.getFechaInicioProyecto()),
                () -> assertEquals(LocalDate.of(2024, 5, 31), experienciaActualizada.getFechaFinProyecto()),
                () -> assertEquals("Descripción actualizada del proyecto", experienciaActualizada.getDescripcion()),
                () -> assertEquals("https://github.com/actualizado", experienciaActualizada.getLink()),
                () -> assertEquals(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO,
                        experienciaActualizada.getTipoExperiencia()),
                () -> assertNotNull(experienciaActualizada.getTecnologiasUsadas()),
                () -> assertEquals(2, experienciaActualizada.getTecnologiasUsadas().size()),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT)),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.REACT)),
                () -> assertNull(experienciaActualizada.getImagenes(), "No debe haber imágenes")
        );
    }

    @Test
    void updateExperiencia_agregarImagenes_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - crear experiencia sin imágenes
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Sin Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción del proyecto")
                .link("https://github.com/sin-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        Experiencia guardado = experienciaRepository.save(experiencia);

        // Preparar datos con imágenes
        List<ImagenDto> imagenes = Arrays.asList(
                crearImagenDto("https://example.com/nueva-1.jpg", "Nueva imagen 1"),
                crearImagenDto("https://example.com/nueva-2.jpg", "Nueva imagen 2")
        );

        ExperienciaDto experienciaDtoActualizado = crearExperienciaDtoConImagenes(
                "Proyecto Sin Imágenes",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                "Descripción del proyecto",
                "https://github.com/sin-imagenes",
                TipoExperiencia.PROYECTO_PERSONAL,
                imagenes,
                TecnologiaUsada.JAVA
        );

        // ACT
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), experienciaActualizada.getId()),
                () -> assertNotNull(experienciaActualizada.getImagenes()),
                () -> assertEquals(2, experienciaActualizada.getImagenes().size()),
                () -> assertEquals("https://example.com/nueva-1.jpg",
                        experienciaActualizada.getImagenes().get(0).getUrl()),
                () -> assertEquals("https://example.com/nueva-2.jpg",
                        experienciaActualizada.getImagenes().get(1).getUrl()),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA))
        );
    }

    @Test
    void updateExperiencia_modificarImagenes_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - crear experiencia con imágenes existentes
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto con Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción del proyecto")
                .link("https://github.com/con-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        Imagen imagen1 = Imagen.builder()
                .url("https://example.com/antigua-1.jpg")
                .alt("Imagen antigua 1")
                .experiencia(experiencia)
                .build();

        Imagen imagen2 = Imagen.builder()
                .url("https://example.com/antigua-2.jpg")
                .alt("Imagen antigua 2")
                .experiencia(experiencia)
                .build();

        experiencia.setImagenes(Arrays.asList(imagen1, imagen2));
        Experiencia guardado = experienciaRepository.save(experiencia);

        // Preparar datos con nuevas imágenes (reemplazar todas)
        List<ImagenDto> imagenesNuevas = Arrays.asList(
                crearImagenDto("https://example.com/nueva-1.jpg", "Nueva imagen 1"),
                crearImagenDto("https://example.com/nueva-2.jpg", "Nueva imagen 2"),
                crearImagenDto("https://example.com/nueva-3.jpg", "Nueva imagen 3")
        );

        ExperienciaDto experienciaDtoActualizado = crearExperienciaDtoConImagenes(
                "Proyecto con Imágenes",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                "Descripción del proyecto",
                "https://github.com/con-imagenes",
                TipoExperiencia.PROYECTO_PERSONAL,
                imagenesNuevas,
                TecnologiaUsada.JAVA,
                TecnologiaUsada.SPRINGBOOT
        );

        // ACT
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), experienciaActualizada.getId()),
                () -> assertNotNull(experienciaActualizada.getImagenes()),
                () -> assertEquals(3, experienciaActualizada.getImagenes().size(),
                        "Debe tener 3 imágenes nuevas"),
                () -> assertEquals("https://example.com/nueva-1.jpg",
                        experienciaActualizada.getImagenes().get(0).getUrl()),
                () -> assertEquals("https://example.com/nueva-2.jpg",
                        experienciaActualizada.getImagenes().get(1).getUrl()),
                () -> assertEquals("https://example.com/nueva-3.jpg",
                        experienciaActualizada.getImagenes().get(2).getUrl()),
                // Verificar que las imágenes antiguas fueron reemplazadas
                () -> assertFalse(experienciaActualizada.getImagenes().stream()
                        .anyMatch(img -> img.getUrl().contains("antigua"))),
                () -> assertEquals(2, experienciaActualizada.getTecnologiasUsadas().size()),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA)),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT))
        );
    }

    @Test
    void updateExperiencia_eliminarImagenes_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - crear experiencia con imágenes
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto a Eliminar Imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción del proyecto")
                .link("https://github.com/eliminar-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        Imagen imagen1 = Imagen.builder()
                .url("https://example.com/eliminar-1.jpg")
                .alt("Imagen a eliminar 1")
                .experiencia(experiencia)
                .build();

        Imagen imagen2 = Imagen.builder()
                .url("https://example.com/eliminar-2.jpg")
                .alt("Imagen a eliminar 2")
                .experiencia(experiencia)
                .build();

        experiencia.setImagenes(Arrays.asList(imagen1, imagen2));
        Experiencia guardado = experienciaRepository.save(experiencia);

        // Verificar que hay imágenes
        assertFalse(experienciaRepository.findById(guardado.getId()).get().getImagenes().isEmpty());

        // Preparar datos sin imágenes
        ExperienciaDto experienciaDtoActualizado = crearExperienciaDto(
                "Proyecto a Eliminar Imágenes",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                "Descripción del proyecto",
                "https://github.com/eliminar-imagenes",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), experienciaActualizada.getId()),
                () -> assertNull(experienciaActualizada.getImagenes(), "No debe haber imágenes"),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA))
        );
    }

    @Test
    void updateExperiencia_conIdInexistente_debeRetornarNotFound() throws Exception {
        // ARRANGE
        Integer idInexistente = 9999;
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto Actualizado",
                LocalDate.now(),
                LocalDate.now(),
                "Descripción actualizada",
                "https://github.com/actualizado",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT
        mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateExperiencia_cambiarTipoYTecnologias_conImagenes_debeActualizarCorrectamente() throws Exception {
        // ARRANGE
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Original")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción original")
                .link("https://github.com/original")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        Experiencia guardado = experienciaRepository.save(experiencia);

        // Preparar datos cambiando tipo y tecnologías con imágenes
        List<ImagenDto> imagenes = List.of(
                crearImagenDto("https://example.com/proyecto-updated.jpg", "Imagen actualizada")
        );

        ExperienciaDto experienciaDtoActualizado = crearExperienciaDtoConImagenes(
                "Proyecto Original",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                "Descripción original",
                "https://github.com/original",
                TipoExperiencia.TRABAJO_LABORAL_FREELANCE,
                imagenes,
                TecnologiaUsada.PYTHON,
                TecnologiaUsada.DJANGO,
                TecnologiaUsada.POSTGRESQL
        );

        // ACT
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), experienciaActualizada.getId()),
                () -> assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE,
                        experienciaActualizada.getTipoExperiencia()),
                () -> assertNotNull(experienciaActualizada.getTecnologiasUsadas()),
                () -> assertEquals(3, experienciaActualizada.getTecnologiasUsadas().size()),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.PYTHON)),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.DJANGO)),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.POSTGRESQL)),
                () -> assertFalse(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA)),
                () -> assertNotNull(experienciaActualizada.getImagenes()),
                () -> assertEquals(1, experienciaActualizada.getImagenes().size()),
                () -> assertEquals("https://example.com/proyecto-updated.jpg",
                        experienciaActualizada.getImagenes().get(0).getUrl())
        );
    }

    @Test
    void updateExperiencia_convertirAProyectoEnCurso_fechaFinNull() throws Exception {
        // ARRANGE
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Original")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción original")
                .link("https://github.com/original")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        Experiencia guardado = experienciaRepository.save(experiencia);

        // Preparar datos con fecha fin null
        ExperienciaDto experienciaDtoActualizado = crearExperienciaDto(
                "Proyecto Original",
                LocalDate.of(2024, 1, 1),
                null,
                "Descripción original",
                "https://github.com/original",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);
        assertNull(experienciaActualizada.getFechaFinProyecto());
        assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));
    }

    // ==================== TESTS DE ELIMINACIÓN ====================

    @Test
    void deleteExperienciaById_conIdExistenteSinImagenes_debeEliminarExperiencia() throws Exception {
        // ARRANGE
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto a Eliminar")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Proyecto para eliminar")
                .link("https://github.com/eliminar")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        Experiencia guardado = experienciaRepository.save(experiencia);
        assertTrue(experienciaRepository.existsById(guardado.getId()));

        // ACT
        mockMvc.perform(delete("/api/v1/borrar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // ASSERT
        assertFalse(experienciaRepository.existsById(guardado.getId()));
    }

    @Test
    void deleteExperienciaById_conIdExistenteConImagenes_debeEliminarExperienciaYImagenes() throws Exception {
        // ARRANGE - crear experiencia con imágenes
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto con Imágenes a Eliminar")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Proyecto para eliminar con imágenes")
                .link("https://github.com/eliminar-imagenes")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA))
                .build();

        Imagen imagen1 = Imagen.builder()
                .url("https://example.com/eliminar-1.jpg")
                .alt("Imagen a eliminar 1")
                .experiencia(experiencia)
                .build();

        Imagen imagen2 = Imagen.builder()
                .url("https://example.com/eliminar-2.jpg")
                .alt("Imagen a eliminar 2")
                .experiencia(experiencia)
                .build();

        experiencia.setImagenes(Arrays.asList(imagen1, imagen2));
        Experiencia guardado = experienciaRepository.save(experiencia);

        // Verificar que existen imágenes
        assertEquals(2, imagenRepository.count());

        // ACT
        mockMvc.perform(delete("/api/v1/borrar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // ASSERT - verificar que la experiencia y sus imágenes fueron eliminadas
        assertAll(
                () -> assertFalse(experienciaRepository.existsById(guardado.getId())),
                () -> assertEquals(0, imagenRepository.count(),
                        "Todas las imágenes deben ser eliminadas")
        );
    }

    @Test
    void deleteExperienciaById_conIdInexistente_debeLanzarExcepcion() throws Exception {
        // ARRANGE
        Integer idInexistente = 9999;

        // ACT & ASSERT
        mockMvc.perform(delete("/api/v1/borrar/experiencia/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    void saveExperiencia_conTituloExactamenteDeLongitudMaxima_debeGuardarCorrectamente() throws Exception {
        // ARRANGE
        String titulo = "a".repeat(145);
        ExperienciaDto experienciaDto = crearExperienciaDto(
                titulo,
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                "Descripción válida",
                "https://github.com/test",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);
        assertEquals(titulo, responseDto.getTitulo());
        assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA));
    }

    @Test
    void saveExperiencia_conTituloConCaracteresEspeciales_debeGuardarCorrectamente() throws Exception {
        // ARRANGE
        List<ImagenDto> imagenes = List.of(
                crearImagenDto("https://example.com/especial.jpg", "Imagen especial")
        );

        ExperienciaDto experienciaDto = crearExperienciaDtoConImagenes(
                "Proyecto: Desarrollo Web (Full-Stack) & Más!",
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                "Descripción del proyecto con caracteres especiales",
                "https://github.com/proyecto-especial",
                TipoExperiencia.PROYECTO_PERSONAL,
                imagenes,
                TecnologiaUsada.JAVASCRIPT,
                TecnologiaUsada.TYPESCRIPT
        );

        // ACT
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId()),
                () -> assertEquals("Proyecto: Desarrollo Web (Full-Stack) & Más!", responseDto.getTitulo()),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.JAVASCRIPT)),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.TYPESCRIPT)),
                () -> assertNotNull(responseDto.getImagenes()),
                () -> assertEquals(1, responseDto.getImagenes().size()),
                () -> assertTrue(experienciaRepository.existsById(responseDto.getId()))
        );
    }

    @Test
    void saveExperiencia_conMultiplesTecnologiasYMultiplesImagenes_debeGuardarCorrectamente() throws Exception {
        // ARRANGE
        List<ImagenDto> imagenes = Arrays.asList(
                crearImagenDto("https://example.com/img1.jpg", "Imagen 1"),
                crearImagenDto("https://example.com/img2.jpg", "Imagen 2"),
                crearImagenDto("https://example.com/img3.jpg", "Imagen 3")
        );

        ExperienciaDto experienciaDto = crearExperienciaDtoConImagenes(
                "Proyecto Full Stack con múltiples tecnologías e imágenes",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 6, 30),
                "Proyecto que utiliza múltiples tecnologías y tiene múltiples imágenes",
                "https://github.com/multi-tecnologias-imagenes",
                TipoExperiencia.PROYECTO_PERSONAL,
                imagenes,
                TecnologiaUsada.JAVA,
                TecnologiaUsada.SPRINGBOOT,
                TecnologiaUsada.REACT,
                TecnologiaUsada.TYPESCRIPT,
                TecnologiaUsada.MYSQL
        );

        // ACT
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId()),
                () -> assertNotNull(responseDto.getTecnologiasUsadas()),
                () -> assertEquals(5, responseDto.getTecnologiasUsadas().size()),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA)),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT)),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.REACT)),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.TYPESCRIPT)),
                () -> assertTrue(responseDto.getTecnologiasUsadas().contains(TecnologiaUsada.MYSQL)),
                () -> assertNotNull(responseDto.getImagenes()),
                () -> assertEquals(3, responseDto.getImagenes().size()),
                () -> assertTrue(experienciaRepository.existsById(responseDto.getId()))
        );
    }

    @Test
    void saveExperiencia_conTodasLasTecnologias_debeGuardarCorrectamente() throws Exception {
        // ARRANGE
        TecnologiaUsada[] tecnologias = TecnologiaUsada.values();

        for (TecnologiaUsada tecnologia : tecnologias) {
            ExperienciaDto experienciaDto = crearExperienciaDto(
                    "Proyecto con " + tecnologia.name(),
                    LocalDate.now(),
                    LocalDate.now().plusMonths(3),
                    "Descripción del proyecto con " + tecnologia.name(),
                    "https://github.com/proyecto-" + tecnologia.name().toLowerCase(),
                    TipoExperiencia.PROYECTO_PERSONAL,
                    tecnologia
            );

            // ACT & ASSERT
            mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(experienciaDto)))
                    .andExpect(status().isCreated());
        }

        // ASSERT - verificar que todas fueron guardadas
        assertEquals(tecnologias.length, experienciaRepository.count());
    }

    @Test
    void saveExperiencia_conTodosLosTipos_debeGuardarCorrectamente() throws Exception {
        // ARRANGE
        ExperienciaDto experienciaPersonal = crearExperienciaDto(
                "Proyecto Personal", LocalDate.now(), LocalDate.now().plusMonths(1),
                "Descripción proyecto personal", "https://github.com/personal",
                TipoExperiencia.PROYECTO_PERSONAL, TecnologiaUsada.JAVA);

        ExperienciaDto experienciaLaboral = crearExperienciaDto(
                "Trabajo Colaborativo", LocalDate.now(), LocalDate.now().plusMonths(2),
                "Descripción trabajo colaborativo", "https://github.com/colaborativo",
                TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO, TecnologiaUsada.SPRINGBOOT);

        ExperienciaDto experienciaOpenSource = crearExperienciaDto(
                "Open Source", LocalDate.now(), LocalDate.now().plusMonths(3),
                "Descripción aporte open source", "https://github.com/opensource",
                TipoExperiencia.APORTE_CODIGO_ABIERTO, TecnologiaUsada.PYTHON);

        ExperienciaDto experienciaPractica = crearExperienciaDto(
                "Práctica Profesional", LocalDate.now(), LocalDate.now().plusMonths(4),
                "Descripción práctica profesional", "https://github.com/practica",
                TipoExperiencia.PRACTICA_PROFESIONAL, TecnologiaUsada.MYSQL);

        ExperienciaDto experienciaFreelance = crearExperienciaDto(
                "Freelance", LocalDate.now(), LocalDate.now().plusMonths(5),
                "Descripción trabajo freelance", "https://github.com/freelance",
                TipoExperiencia.TRABAJO_LABORAL_FREELANCE, TecnologiaUsada.REACT);

        // ACT
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaPersonal)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaLaboral)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaOpenSource)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaPractica)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaFreelance)))
                .andExpect(status().isCreated());

        // ASSERT
        assertEquals(5, experienciaRepository.count());
    }

    @Test
    void updateExperiencia_agregarYQuitarTecnologias_conImagenes_debeActualizarCorrectamente() throws Exception {
        // ARRANGE
        List<ImagenDto> imagenesIniciales = Arrays.asList(
                crearImagenDto("https://example.com/inicial-1.jpg", "Imagen inicial 1"),
                crearImagenDto("https://example.com/inicial-2.jpg", "Imagen inicial 2")
        );

        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto para actualizar tecnologías e imágenes")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción del proyecto")
                .link("https://github.com/actualizar-todo")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiasUsadas(List.of(TecnologiaUsada.JAVA, TecnologiaUsada.SPRINGBOOT))
                .build();

        // Crear y asociar imágenes
        Imagen img1 = Imagen.builder()
                .url("https://example.com/inicial-1.jpg")
                .alt("Imagen inicial 1")
                .experiencia(experiencia)
                .build();

        Imagen img2 = Imagen.builder()
                .url("https://example.com/inicial-2.jpg")
                .alt("Imagen inicial 2")
                .experiencia(experiencia)
                .build();

        experiencia.setImagenes(Arrays.asList(img1, img2));
        Experiencia guardado = experienciaRepository.save(experiencia);

        // Preparar datos con nuevas tecnologías e imágenes
        List<ImagenDto> imagenesNuevas = List.of(
                crearImagenDto("https://example.com/nueva-1.jpg", "Nueva imagen única")
        );

        ExperienciaDto experienciaDtoActualizado = crearExperienciaDtoConImagenes(
                "Proyecto para actualizar tecnologías e imágenes",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                "Descripción del proyecto",
                "https://github.com/actualizar-todo",
                TipoExperiencia.PROYECTO_PERSONAL,
                imagenesNuevas,
                TecnologiaUsada.REACT,
                TecnologiaUsada.TYPESCRIPT,
                TecnologiaUsada.PYTHON
        );

        // ACT
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), experienciaActualizada.getId()),
                () -> assertNotNull(experienciaActualizada.getTecnologiasUsadas()),
                () -> assertEquals(3, experienciaActualizada.getTecnologiasUsadas().size()),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.REACT)),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.TYPESCRIPT)),
                () -> assertTrue(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.PYTHON)),
                () -> assertFalse(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.JAVA)),
                () -> assertFalse(experienciaActualizada.getTecnologiasUsadas().contains(TecnologiaUsada.SPRINGBOOT)),
                () -> assertNotNull(experienciaActualizada.getImagenes()),
                () -> assertEquals(1, experienciaActualizada.getImagenes().size()),
                () -> assertEquals("https://example.com/nueva-1.jpg",
                        experienciaActualizada.getImagenes().get(0).getUrl()),
                // Verificar que las imágenes antiguas fueron eliminadas
                () -> assertFalse(experienciaActualizada.getImagenes().stream()
                        .anyMatch(img -> img.getUrl().contains("inicial")))
        );
    }
}