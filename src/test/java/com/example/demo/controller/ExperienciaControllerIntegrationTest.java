package com.example.demo.controller;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import com.example.demo.model.Experiencia;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ExperienciaRepository;
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

    private ExperienciaDto crearExperienciaDto(String titulo, LocalDate fechaInicio, LocalDate fechaFin,
                                               String descripcion, String link, TipoExperiencia tipo,
                                               TecnologiaUsada tecnologia) {
        return ExperienciaDto.builder()
                .titulo(titulo)
                .fechaInicioProyecto(fechaInicio)
                .fechaFinProyecto(fechaFin)
                .descripcion(descripcion)
                .link(link)
                .tipoExperiencia(tipo)
                .tecnologiaUsada(tecnologia)
                .build();
    }

    private ExperienciaDto crearExperienciaDtoConImagen(String titulo, LocalDate fechaInicio, LocalDate fechaFin,
                                                        String descripcion, String link, TipoExperiencia tipo,
                                                        TecnologiaUsada tecnologia, String url, String alt) {
        ExperienciaDto dto = crearExperienciaDto(titulo, fechaInicio, fechaFin, descripcion, link, tipo, tecnologia);
        if (url != null) {
            ImagenDto imagen = ImagenDto.builder()
                    .url(url)
                    .alt(alt)
                    .build();
            dto.setImagen(imagen);
        }
        return dto;
    }

    @BeforeEach
    void setUp() {
        // Limpiar datos antes de cada prueba
        experienciaRepository.deleteAll();
    }

    @Test
    void saveExperiencia_conDatosValidos_debeRetornarExperienciaCreada() throws Exception {
        // ARRANGE - contexto: experiencia con datos válidos
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Portfolio Personal",
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 6, 30),
                "Desarrollo de portfolio personal con Angular y Spring Boot",
                "https://github.com/mi-portfolio",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.ANGULAR
        );

        // ACT - acción: guardar experiencia a través del endpoint
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la experiencia fue guardada en BD
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Portfolio Personal", responseDto.getTitulo(), "Título debe coincidir"),
                () -> assertEquals(LocalDate.of(2024, 1, 15), responseDto.getFechaInicioProyecto(),
                        "Fecha de inicio debe coincidir"),
                () -> assertEquals(LocalDate.of(2024, 6, 30), responseDto.getFechaFinProyecto(),
                        "Fecha de fin debe coincidir"),
                () -> assertEquals("Desarrollo de portfolio personal con Angular y Spring Boot",
                        responseDto.getDescripcion(), "Descripción debe coincidir"),
                () -> assertEquals("https://github.com/mi-portfolio", responseDto.getLink(),
                        "Link debe coincidir"),
                () -> assertEquals(TipoExperiencia.PROYECTO_PERSONAL, responseDto.getTipoExperiencia(),
                        "Tipo de experiencia debe coincidir"),
                () -> assertEquals(TecnologiaUsada.ANGULAR, responseDto.getTecnologiaUsada(),
                        "Tecnología usada debe coincidir"),
                () -> assertTrue(experienciaRepository.existsById(responseDto.getId()),
                        "Experiencia debe existir en la base de datos")
        );
    }

    @Test
    void saveExperiencia_conProyectoEnCurso_fechaFinNull_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: experiencia con fecha de fin null (proyecto en curso)
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto en Curso",
                LocalDate.of(2024, 1, 15),
                null, // Proyecto en curso
                "Descripción del proyecto en curso",
                "https://github.com/proyecto-curso",
                TipoExperiencia.PROYECTO_PERSONAL,
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

        // ASSERT - validaciones: verificar que se guardó con fecha fin null
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertEquals("Proyecto en Curso", responseDto.getTitulo(), "Título debe coincidir"),
                () -> assertNull(responseDto.getFechaFinProyecto(), "Fecha de fin debe ser null"),
                () -> assertTrue(experienciaRepository.existsById(responseDto.getId()),
                        "Experiencia debe existir en la BD")
        );
    }

    @Test
    void saveExperiencia_conImagen_debeRetornarExperienciaConImagen() throws Exception {
        // ARRANGE - contexto: experiencia con imagen
        ExperienciaDto experienciaDto = crearExperienciaDtoConImagen(
                "App de Tareas",
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 4, 30),
                "Aplicación de gestión de tareas con React y Node.js",
                "https://github.com/app-tareas",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.REACT,
                "https://example.com/app-tareas.jpg",
                "Captura de la aplicación de tareas"
        );

        // ACT - acción: guardar experiencia
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la imagen fue guardada
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertNotNull(responseDto.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/app-tareas.jpg",
                        responseDto.getImagen().getUrl(), "URL de la imagen debe coincidir"),
                () -> assertEquals("Captura de la aplicación de tareas",
                        responseDto.getImagen().getAlt(), "Alt de la imagen debe coincidir")
        );
    }

    @Test
    void saveExperiencia_conTituloNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: experiencia con título nulo
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo(null)
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conTituloVacio_debeRetornarError() throws Exception {
        // ARRANGE - contexto: experiencia con título vacío
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "",
                LocalDate.now(),
                LocalDate.now(),
                "Descripción válida",
                "https://github.com/test",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conFechaInicioNula_debeRetornarError() throws Exception {
        // ARRANGE - contexto: experiencia con fecha de inicio nula
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(null)
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conDescripcionNula_debeRetornarError() throws Exception {
        // ARRANGE - contexto: experiencia con descripción nula
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion(null)
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conDescripcionVacia_debeRetornarError() throws Exception {
        // ARRANGE - contexto: experiencia con descripción vacía
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto Test",
                LocalDate.now(),
                LocalDate.now(),
                "",
                "https://github.com/test",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conLinkNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: experiencia con link nulo
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link(null)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conLinkVacio_debeRetornarError() throws Exception {
        // ARRANGE - contexto: experiencia con link vacío
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto Test",
                LocalDate.now(),
                LocalDate.now(),
                "Descripción válida",
                "",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conTipoExperienciaNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: experiencia con tipo de experiencia nulo
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(null)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveExperiencia_conTecnologiaNula_debeRetornarError() throws Exception {
        // ARRANGE - contexto: experiencia con tecnología nula
        ExperienciaDto experienciaDto = ExperienciaDto.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(null)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllExperiencias_debeRetornarTodasLasExperiencias() throws Exception {
        // ARRANGE - contexto: crear y guardar experiencias
        Experiencia experiencia1 = Experiencia.builder()
                .titulo("Portfolio Personal")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de portfolio personal")
                .link("https://github.com/portfolio1")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.ANGULAR)
                .build();

        Experiencia experiencia2 = Experiencia.builder()
                .titulo("App de Tareas")
                .fechaInicioProyecto(LocalDate.of(2024, 2, 1))
                .fechaFinProyecto(LocalDate.of(2024, 4, 30))
                .descripcion("Aplicación de gestión de tareas")
                .link("https://github.com/app-tareas")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.REACT)
                .build();

        experienciaRepository.save(experiencia1);
        experienciaRepository.save(experiencia2);

        // ACT - acción: obtener todas las experiencias
        String responseJson = mockMvc.perform(get("/api/v1/todas/experiencias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que retorna todas las experiencias
        ExperienciaDto[] experiencias = objectMapper.readValue(responseJson, ExperienciaDto[].class);

        assertAll(
                () -> assertNotNull(experiencias, "Lista no debe ser null"),
                () -> assertTrue(experiencias.length >= 2, "Debe contener al menos 2 experiencias"),
                () -> assertTrue(List.of(experiencias).stream().anyMatch(e -> e.getTitulo().equals("Portfolio Personal")),
                        "Debe contener Portfolio Personal"),
                () -> assertTrue(List.of(experiencias).stream().anyMatch(e -> e.getTitulo().equals("App de Tareas")),
                        "Debe contener App de Tareas")
        );
    }

    @Test
    void getAllExperiencias_sinExperiencias_debeRetornarListaVacia() throws Exception {
        // ARRANGE - contexto: no hay experiencias en la base de datos
        assertEquals(0, experienciaRepository.count(), "No debería haber experiencias inicialmente");

        // ACT - acción: obtener todas las experiencias
        String responseJson = mockMvc.perform(get("/api/v1/todas/experiencias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe retornar lista vacía
        ExperienciaDto[] experiencias = objectMapper.readValue(responseJson, ExperienciaDto[].class);
        assertEquals(0, experiencias.length, "Lista debe estar vacía");
    }

    @Test
    void getAllExperiencias_conImagenes_debeRetornarExperienciasConImagenes() throws Exception {
        // ARRANGE - contexto: crear experiencia con imagen
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto con Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 3, 1))
                .fechaFinProyecto(LocalDate.of(2024, 5, 15))
                .descripcion("Proyecto con imagen representativa")
                .link("https://github.com/proyecto-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        Imagen imagen = Imagen.builder()
                .url("https://example.com/proyecto.jpg")
                .alt("Imagen del proyecto")
                .experiencia(experiencia)
                .build();
        experiencia.setImagen(imagen);

        experienciaRepository.save(experiencia);

        // ACT - acción: obtener todas las experiencias
        String responseJson = mockMvc.perform(get("/api/v1/todas/experiencias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la imagen está presente
        ExperienciaDto[] experiencias = objectMapper.readValue(responseJson, ExperienciaDto[].class);

        assertAll(
                () -> assertFalse(experiencias.length == 0, "Lista no debe estar vacía"),
                () -> assertNotNull(experiencias[0].getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/proyecto.jpg",
                        experiencias[0].getImagen().getUrl(), "URL de imagen debe coincidir")
        );
    }

    @Test
    void updateExperiencia_conDatosValidos_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - contexto: crear y guardar experiencia
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Original")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción original")
                .link("https://github.com/original")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
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
                TecnologiaUsada.SPRINGBOOT
        );

        // ACT - acción: actualizar experiencia
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe actualizar correctamente
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), experienciaActualizada.getId(), "ID debe mantenerse"),
                () -> assertEquals("Proyecto Actualizado", experienciaActualizada.getTitulo(),
                        "Título debe estar actualizado"),
                () -> assertEquals(LocalDate.of(2024, 2, 1), experienciaActualizada.getFechaInicioProyecto(),
                        "Fecha de inicio debe estar actualizada"),
                () -> assertEquals(LocalDate.of(2024, 5, 31), experienciaActualizada.getFechaFinProyecto(),
                        "Fecha de fin debe estar actualizada"),
                () -> assertEquals("Descripción actualizada del proyecto", experienciaActualizada.getDescripcion(),
                        "Descripción debe estar actualizada"),
                () -> assertEquals("https://github.com/actualizado", experienciaActualizada.getLink(),
                        "Link debe estar actualizado"),
                () -> assertEquals(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO,
                        experienciaActualizada.getTipoExperiencia(), "Tipo de experiencia debe estar actualizado"),
                () -> assertEquals(TecnologiaUsada.SPRINGBOOT, experienciaActualizada.getTecnologiaUsada(),
                        "Tecnología usada debe estar actualizada")
        );
    }

    @Test
    void updateExperiencia_conIdInexistente_debeRetornarNotFound() throws Exception {
        // ARRANGE - contexto: ID que no existe
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

        // ACT & ASSERT - acción y validación: debe retornar 404 Not Found
        mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateExperiencia_cambiarTipoYTecnologia_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - contexto: crear y guardar experiencia
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Original")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción original")
                .link("https://github.com/original")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        Experiencia guardado = experienciaRepository.save(experiencia);

        // Preparar datos cambiando tipo y tecnología
        ExperienciaDto experienciaDtoActualizado = crearExperienciaDto(
                "Proyecto Original",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                "Descripción original",
                "https://github.com/original",
                TipoExperiencia.TRABAJO_LABORAL_FREELANCE,
                TecnologiaUsada.PYTHON
        );

        // ACT - acción: actualizar experiencia
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que tipo y tecnología fueron cambiados
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), experienciaActualizada.getId(), "ID debe mantenerse"),
                () -> assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE,
                        experienciaActualizada.getTipoExperiencia(), "Tipo debe ser actualizado"),
                () -> assertEquals(TecnologiaUsada.PYTHON, experienciaActualizada.getTecnologiaUsada(),
                        "Tecnología debe ser actualizada")
        );
    }

    @Test
    void deleteExperienciaById_conIdExistente_debeEliminarExperiencia() throws Exception {
        // ARRANGE - contexto: crear y guardar experiencia
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto a Eliminar")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Proyecto para eliminar")
                .link("https://github.com/eliminar")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        Experiencia guardado = experienciaRepository.save(experiencia);
        assertTrue(experienciaRepository.existsById(guardado.getId()),
                "Experiencia debe existir antes de eliminar");

        // ACT - acción: eliminar experiencia
        mockMvc.perform(delete("/api/v1/borrar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // ASSERT - validaciones: experiencia debe ser eliminada
        assertFalse(experienciaRepository.existsById(guardado.getId()),
                "Experiencia no debe existir después de eliminar");
    }

    @Test
    void deleteExperienciaById_conIdInexistente_debeLanzarExcepcion() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        mockMvc.perform(delete("/api/v1/borrar/experiencia/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void saveExperiencia_conTodasLasTecnologias_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: probar diferentes tecnologías
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

            // ACT - acción: guardar experiencia
            mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(experienciaDto)))
                    .andExpect(status().isCreated());
        }

        // ASSERT - validaciones: verificar que todas fueron guardadas
        assertEquals(tecnologias.length, experienciaRepository.count(),
                "Debe haber " + tecnologias.length + " experiencias en la BD");
    }

    @Test
    void saveExperiencia_conTodosLosTipos_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: probar todos los tipos de experiencia
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

        // ACT & ASSERT - acción y validación: guardar todos los tipos y verificar
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

        // ASSERT - validaciones: verificar que todos fueron guardados
        assertEquals(5, experienciaRepository.count(), "Debe haber 5 experiencias en la BD");
    }

    @Test
    void updateExperiencia_convertirAProyectoEnCurso_fechaFinNull() throws Exception {
        // ARRANGE - contexto: crear y guardar experiencia con fecha de fin
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Original")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción original")
                .link("https://github.com/original")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        Experiencia guardado = experienciaRepository.save(experiencia);

        // Preparar datos con fecha fin null (proyecto en curso)
        ExperienciaDto experienciaDtoActualizado = crearExperienciaDto(
                "Proyecto Original",
                LocalDate.of(2024, 1, 1),
                null, // Proyecto en curso
                "Descripción original",
                "https://github.com/original",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT - acción: actualizar experiencia a proyecto en curso
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/experiencia/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que fecha fin es null
        ExperienciaDto experienciaActualizada = objectMapper.readValue(responseJson, ExperienciaDto.class);
        assertNull(experienciaActualizada.getFechaFinProyecto(), "Fecha de fin debe ser null para proyecto en curso");
    }

    @Test
    void saveExperiencia_conTituloConCaracteresEspeciales_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: título con caracteres especiales
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto: Desarrollo Web (Full-Stack)",
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                "Descripción del proyecto con caracteres especiales",
                "https://github.com/proyecto-especial",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVASCRIPT
        );

        // ACT - acción: guardar experiencia
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe guardar correctamente
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertEquals("Proyecto: Desarrollo Web (Full-Stack)", responseDto.getTitulo(),
                        "Título con caracteres especiales debe coincidir"),
                () -> assertTrue(experienciaRepository.existsById(responseDto.getId()),
                        "Experiencia debe existir en la BD")
        );
    }

    @Test
    void saveExperiencia_conTituloExactamenteDeLongitudMaxima_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: título exactamente de 145 caracteres
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

        // ACT - acción: guardar experiencia
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/experiencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(experienciaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe guardar correctamente
        ExperienciaDto responseDto = objectMapper.readValue(responseJson, ExperienciaDto.class);
        assertEquals(titulo, responseDto.getTitulo(), "Título debe coincidir");
    }
}