package com.example.demo.controller;

import com.example.demo.dto.EducacionDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TipoEducacion;
import com.example.demo.model.Educacion;
import com.example.demo.model.Imagen;
import com.example.demo.repository.EducacionRepository;
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
class EducacionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EducacionRepository educacionRepository;

    private EducacionDto crearEducacionDto(String titulo, LocalDate fechaObtencion, String descripcion, TipoEducacion tipo) {
        return EducacionDto.builder()
                .titulo(titulo)
                .fechaObtencion(fechaObtencion)
                .descripcion(descripcion)
                .tipoEducacion(tipo)
                .build();
    }

    private EducacionDto crearEducacionDtoConImagen(String titulo, LocalDate fechaObtencion, String descripcion,
                                                    TipoEducacion tipo, String url, String alt) {
        EducacionDto dto = crearEducacionDto(titulo, fechaObtencion, descripcion, tipo);
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
        educacionRepository.deleteAll();
    }

    @Test
    void saveEducacion_conDatosValidos_debeRetornarEducacionCreada() throws Exception {
        // ARRANGE - contexto: educación con datos válidos
        EducacionDto educacionDto = crearEducacionDto(
                "Ingeniería en Sistemas",
                LocalDate.of(2024, 6, 15),
                "Estudios de ingeniería en sistemas computacionales",
                TipoEducacion.FORMAL
        );

        // ACT - acción: guardar educación a través del endpoint
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la educación fue guardada en BD
        EducacionDto responseDto = objectMapper.readValue(responseJson, EducacionDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Ingeniería en Sistemas", responseDto.getTitulo(), "Título debe coincidir"),
                () -> assertEquals(LocalDate.of(2024, 6, 15), responseDto.getFechaObtencion(),
                        "Fecha de obtención debe coincidir"),
                () -> assertEquals("Estudios de ingeniería en sistemas computacionales",
                        responseDto.getDescripcion(), "Descripción debe coincidir"),
                () -> assertEquals(TipoEducacion.FORMAL, responseDto.getTipoEducacion(),
                        "Tipo de educación debe coincidir"),
                () -> assertTrue(educacionRepository.existsById(responseDto.getId()),
                        "Educación debe existir en la base de datos")
        );
    }

    @Test
    void saveEducacion_conImagen_debeRetornarEducacionConImagen() throws Exception {
        // ARRANGE - contexto: educación con imagen
        EducacionDto educacionDto = crearEducacionDtoConImagen(
                "Curso de Spring Boot",
                LocalDate.of(2024, 5, 20),
                "Curso avanzado de Spring Boot",
                TipoEducacion.INFORMAL_CURSO,
                "https://example.com/certificado_spring.jpg",
                "Certificado Spring Boot"
        );

        // ACT - acción: guardar educación
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la imagen fue guardada
        EducacionDto responseDto = objectMapper.readValue(responseJson, EducacionDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertNotNull(responseDto.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/certificado_spring.jpg",
                        responseDto.getImagen().getUrl(), "URL de la imagen debe coincidir"),
                () -> assertEquals("Certificado Spring Boot",
                        responseDto.getImagen().getAlt(), "Alt de la imagen debe coincidir")
        );
    }

    @Test
    void saveEducacion_conTituloNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: educación con título nulo
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo(null)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveEducacion_conFechaObtencionNula_debeRetornarError() throws Exception {
        // ARRANGE - contexto: educación con fecha de obtención nula
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo("Curso de Java")
                .fechaObtencion(null)
                .descripcion("Descripción válida")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveEducacion_conDescripcionNula_debeRetornarError() throws Exception {
        // ARRANGE - contexto: educación con descripción nula
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.now())
                .descripcion(null)
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveEducacion_conTipoEducacionNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: educación con tipo de educación nulo
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .tipoEducacion(null)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveEducacion_conTituloVacio_debeRetornarError() throws Exception {
        // ARRANGE - contexto: educación con título vacío
        EducacionDto educacionDto = crearEducacionDto(
                "",
                LocalDate.now(),
                "Descripción válida",
                TipoEducacion.FORMAL
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveEducacion_conDescripcionMuyCorta_debeRetornarError() throws Exception {
        // ARRANGE - contexto: educación con descripción muy corta
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Java",
                LocalDate.now(),
                "Desc", // Menos de 5 caracteres
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllEducaciones_debeRetornarTodasLasEducaciones() throws Exception {
        // ARRANGE - contexto: crear y guardar educaciones
        Educacion educacion1 = Educacion.builder()
                .titulo("Ingeniería en Sistemas")
                .fechaObtencion(LocalDate.of(2024, 6, 15))
                .descripcion("Estudios de ingeniería en sistemas")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        Educacion educacion2 = Educacion.builder()
                .titulo("Curso de Spring Boot")
                .fechaObtencion(LocalDate.of(2024, 5, 20))
                .descripcion("Curso avanzado de Spring Boot")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        educacionRepository.save(educacion1);
        educacionRepository.save(educacion2);

        // ACT - acción: obtener todas las educaciones
        String responseJson = mockMvc.perform(get("/api/v1/todas/educaciones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que retorna todas las educaciones
        EducacionDto[] educaciones = objectMapper.readValue(responseJson, EducacionDto[].class);

        assertAll(
                () -> assertNotNull(educaciones, "Lista no debe ser null"),
                () -> assertTrue(educaciones.length >= 2, "Debe contener al menos 2 educaciones"),
                () -> assertTrue(List.of(educaciones).stream().anyMatch(e -> e.getTitulo().equals("Ingeniería en Sistemas")),
                        "Debe contener Ingeniería en Sistemas"),
                () -> assertTrue(List.of(educaciones).stream().anyMatch(e -> e.getTitulo().equals("Curso de Spring Boot")),
                        "Debe contener Curso de Spring Boot")
        );
    }

    @Test
    void getAllEducaciones_sinEducaciones_debeRetornarListaVacia() throws Exception {
        // ARRANGE - contexto: no hay educaciones en la base de datos
        assertEquals(0, educacionRepository.count(), "No debería haber educaciones inicialmente");

        // ACT - acción: obtener todas las educaciones
        String responseJson = mockMvc.perform(get("/api/v1/todas/educaciones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe retornar lista vacía
        EducacionDto[] educaciones = objectMapper.readValue(responseJson, EducacionDto[].class);
        assertEquals(0, educaciones.length, "Lista debe estar vacía");
    }

    @Test
    void getAllEducaciones_conImagenes_debeRetornarEducacionesConImagenes() throws Exception {
        // ARRANGE - contexto: crear educación con imagen
        Educacion educacion = Educacion.builder()
                .titulo("Curso de Angular")
                .fechaObtencion(LocalDate.of(2024, 4, 10))
                .descripcion("Curso completo de Angular")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        Imagen imagen = Imagen.builder()
                .url("https://example.com/certificado_angular.jpg")
                .alt("Certificado Angular")
                .educacion(educacion)
                .build();
        educacion.setImagen(imagen);

        educacionRepository.save(educacion);

        // ACT - acción: obtener todas las educaciones
        String responseJson = mockMvc.perform(get("/api/v1/todas/educaciones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la imagen está presente
        EducacionDto[] educaciones = objectMapper.readValue(responseJson, EducacionDto[].class);

        assertAll(
                () -> assertFalse(educaciones.length == 0, "Lista no debe estar vacía"),
                () -> assertNotNull(educaciones[0].getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/certificado_angular.jpg",
                        educaciones[0].getImagen().getUrl(), "URL de imagen debe coincidir")
        );
    }

    @Test
    void updateEducacion_conDatosValidos_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - contexto: crear y guardar educación
        Educacion educacion = Educacion.builder()
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.of(2024, 3, 15))
                .descripcion("Curso básico de Java")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        Educacion guardado = educacionRepository.save(educacion);

        // Preparar datos actualizados
        EducacionDto educacionDtoActualizado = crearEducacionDto(
                "Curso Avanzado de Java",
                LocalDate.of(2024, 6, 20),
                "Curso avanzado de Java con Spring Boot",
                TipoEducacion.FORMAL
        );

        // ACT - acción: actualizar educación
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/educacion/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe actualizar correctamente
        EducacionDto educacionActualizada = objectMapper.readValue(responseJson, EducacionDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), educacionActualizada.getId(), "ID debe mantenerse"),
                () -> assertEquals("Curso Avanzado de Java", educacionActualizada.getTitulo(),
                        "Título debe estar actualizado"),
                () -> assertEquals(LocalDate.of(2024, 6, 20), educacionActualizada.getFechaObtencion(),
                        "Fecha de obtención debe estar actualizada"),
                () -> assertEquals("Curso avanzado de Java con Spring Boot", educacionActualizada.getDescripcion(),
                        "Descripción debe estar actualizada"),
                () -> assertEquals(TipoEducacion.FORMAL, educacionActualizada.getTipoEducacion(),
                        "Tipo de educación debe estar actualizado")
        );
    }

    @Test
    void updateEducacion_conImagenNueva_debeActualizarImagen() throws Exception {
        // ARRANGE - contexto: crear y guardar educación con imagen
        Educacion educacion = Educacion.builder()
                .titulo("Curso de React")
                .fechaObtencion(LocalDate.of(2024, 4, 10))
                .descripcion("Curso de React básico")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        Imagen imagenVieja = Imagen.builder()
                .url("https://example.com/react_viejo.jpg")
                .alt("Certificado React Viejo")
                .educacion(educacion)
                .build();
        educacion.setImagen(imagenVieja);

        Educacion guardado = educacionRepository.save(educacion);

        // Preparar datos con nueva imagen
        EducacionDto educacionDtoActualizado = crearEducacionDtoConImagen(
                "Curso Avanzado de React",
                LocalDate.of(2024, 7, 15),
                "Curso avanzado de React con Hooks",
                TipoEducacion.INFORMAL_CURSO,
                "https://example.com/react_nuevo.jpg",
                "Certificado React Nuevo"
        );

        // ACT - acción: actualizar educación con nueva imagen
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/educacion/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la imagen fue actualizada
        EducacionDto educacionActualizada = objectMapper.readValue(responseJson, EducacionDto.class);

        assertAll(
                () -> assertNotNull(educacionActualizada.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/react_nuevo.jpg",
                        educacionActualizada.getImagen().getUrl(), "URL de imagen debe ser la nueva"),
                () -> assertEquals("Certificado React Nuevo",
                        educacionActualizada.getImagen().getAlt(), "Alt de imagen debe ser el nuevo")
        );
    }

    @Test
    void updateEducacion_eliminarImagen_debeMantenerEducacionSinImagen() throws Exception {
        // ARRANGE - contexto: crear y guardar educación con imagen
        Educacion educacion = Educacion.builder()
                .titulo("Curso de Angular")
                .fechaObtencion(LocalDate.of(2024, 5, 20))
                .descripcion("Curso de Angular completo")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        Imagen imagen = Imagen.builder()
                .url("https://example.com/angular.jpg")
                .alt("Certificado Angular")
                .educacion(educacion)
                .build();
        educacion.setImagen(imagen);

        Educacion guardado = educacionRepository.save(educacion);

        // Preparar datos sin imagen
        EducacionDto educacionDtoActualizado = crearEducacionDto(
                "Curso Avanzado de Angular",
                LocalDate.of(2024, 8, 10),
                "Curso avanzado de Angular con TypeScript",
                TipoEducacion.FORMAL
        );

        // ACT - acción: actualizar educación sin imagen
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/educacion/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la imagen fue eliminada
        EducacionDto educacionActualizada = objectMapper.readValue(responseJson, EducacionDto.class);
        assertNull(educacionActualizada.getImagen(), "Imagen debe ser null");
    }

    @Test
    void updateEducacion_conIdInexistente_debeRetornarNotFound() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;
        EducacionDto educacionDto = crearEducacionDto(
                "Curso Actualizado",
                LocalDate.now(),
                "Descripción actualizada",
                TipoEducacion.FORMAL
        );

        // ACT & ASSERT - acción y validación: debe retornar 404 Not Found
        mockMvc.perform(put("/api/v1/auth/modificar/educacion/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEducacionById_conIdExistente_debeEliminarEducacion() throws Exception {
        // ARRANGE - contexto: crear y guardar educación
        Educacion educacion = Educacion.builder()
                .titulo("Curso de Python")
                .fechaObtencion(LocalDate.of(2024, 6, 1))
                .descripcion("Curso básico de Python")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        Educacion guardado = educacionRepository.save(educacion);
        assertTrue(educacionRepository.existsById(guardado.getId()),
                "Educación debe existir antes de eliminar");

        // ACT - acción: eliminar educación
        mockMvc.perform(delete("/api/v1/borrar/educacion/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // ASSERT - validaciones: educación debe ser eliminada
        assertFalse(educacionRepository.existsById(guardado.getId()),
                "Educación no debe existir después de eliminar");
    }

    @Test
    void deleteEducacionById_conIdInexistente_debeLanzarExcepcion() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        mockMvc.perform(delete("/api/v1/borrar/educacion/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void saveEducacion_conTodosLosTipos_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: probar todos los tipos de educación
        EducacionDto educacionFormal = crearEducacionDto(
                "Ingeniería", LocalDate.of(2024, 6, 15), "Descripción formal", TipoEducacion.FORMAL);

        EducacionDto educacionInformal = crearEducacionDto(
                "Curso de Java", LocalDate.of(2024, 5, 20), "Descripción informal", TipoEducacion.INFORMAL_CURSO);

        EducacionDto educacionAutodidacta = crearEducacionDto(
                "Aprendizaje autodidacta", LocalDate.of(2024, 4, 10), "Descripción autodidacta", TipoEducacion.AUTODIDACTA);

        EducacionDto educacionOtros = crearEducacionDto(
                "Otros estudios", LocalDate.of(2024, 3, 1), "Descripción otros", TipoEducacion.OTROS);

        // ACT & ASSERT - acción y validación: guardar todos los tipos y verificar
        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionFormal)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionInformal)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionAutodidacta)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionOtros)))
                .andExpect(status().isCreated());

        // ASSERT - validaciones: verificar que todos fueron guardados
        assertEquals(4, educacionRepository.count(), "Debe haber 4 educaciones en la BD");
    }

    @Test
    void updateEducacion_cambiarTipo_debeActualizarTipoCorrectamente() throws Exception {
        // ARRANGE - contexto: crear y guardar educación
        Educacion educacion = Educacion.builder()
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.of(2024, 3, 15))
                .descripcion("Curso de Java")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        Educacion guardado = educacionRepository.save(educacion);

        // Preparar datos cambiando el tipo
        EducacionDto educacionDtoActualizado = crearEducacionDto(
                "Curso de Java",
                LocalDate.of(2024, 6, 20),
                "Curso de Java actualizado",
                TipoEducacion.FORMAL // Cambiar de INFORMAL_CURSO a FORMAL
        );

        // ACT - acción: actualizar educación cambiando el tipo
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/educacion/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que el tipo fue cambiado
        EducacionDto educacionActualizada = objectMapper.readValue(responseJson, EducacionDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), educacionActualizada.getId(), "ID debe mantenerse"),
                () -> assertEquals("Curso de Java", educacionActualizada.getTitulo(), "Título debe mantenerse"),
                () -> assertEquals(TipoEducacion.FORMAL, educacionActualizada.getTipoEducacion(),
                        "Tipo debe ser actualizado a FORMAL")
        );
    }

    @Test
    void saveEducacion_conFechaFutura_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: educación con fecha futura
        LocalDate fechaFutura = LocalDate.now().plusMonths(6);
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Machine Learning",
                fechaFutura,
                "Curso avanzado de Machine Learning",
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT - acción: guardar educación
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe guardar con fecha futura
        EducacionDto responseDto = objectMapper.readValue(responseJson, EducacionDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertEquals(fechaFutura, responseDto.getFechaObtencion(),
                        "Fecha de obtención debe coincidir")
        );
    }

    @Test
    void saveEducacion_conTituloLargo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: educación con título muy largo
        String tituloLargo = "a".repeat(150);
        EducacionDto educacionDto = crearEducacionDto(
                tituloLargo,
                LocalDate.now(),
                "Descripción válida",
                TipoEducacion.FORMAL
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveEducacion_conDescripcionLarga_debeRetornarError() throws Exception {
        // ARRANGE - contexto: educación con descripción muy larga
        String descripcionLarga = "a".repeat(310);
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Java",
                LocalDate.now(),
                descripcionLarga,
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/educacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateEducacion_conTituloNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: crear y guardar educación
        Educacion educacion = Educacion.builder()
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.of(2024, 3, 15))
                .descripcion("Descripción válida")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        Educacion guardado = educacionRepository.save(educacion);

        // Preparar datos con título nulo
        EducacionDto educacionDtoActualizado = EducacionDto.builder()
                .titulo(null)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción actualizada")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(put("/api/v1/auth/modificar/educacion/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDtoActualizado)))
                .andExpect(status().is4xxClientError());
    }
}