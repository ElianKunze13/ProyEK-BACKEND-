package com.example.demo.controller;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoConocimiento;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ConocimientoRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ConocimientoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConocimientoRepository conocimientoRepository;

    private ConocimientoDto crearConocimientoDto(String nombre, Nivel nivel, TipoConocimiento tipo) {
        return ConocimientoDto.builder()
                .nombre(nombre)
                .nivel(nivel)
                .tipoConocimiento(tipo)
                .build();
    }

    private ConocimientoDto crearConocimientoDtoConImagen(String nombre, Nivel nivel, TipoConocimiento tipo,
                                                          String url, String alt) {
        ConocimientoDto dto = crearConocimientoDto(nombre, nivel, tipo);
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
        conocimientoRepository.deleteAll();
    }

    @Test
    void saveConocimiento_conDatosValidos_debeRetornarConocimientoCreado() throws Exception {
        // ARRANGE - contexto: conocimiento con datos válidos
        ConocimientoDto conocimientoDto = crearConocimientoDto(
                "Spring Boot",
                Nivel.INTERMEDIO,
                TipoConocimiento.BACKEND
        );

        // ACT - acción: guardar conocimiento a través del endpoint
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que el conocimiento fue guardado en BD
        ConocimientoDto responseDto = objectMapper.readValue(responseJson, ConocimientoDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Spring Boot", responseDto.getNombre(), "Nombre debe coincidir"),
                () -> assertEquals(Nivel.INTERMEDIO, responseDto.getNivel(), "Nivel debe coincidir"),
                () -> assertEquals(TipoConocimiento.BACKEND, responseDto.getTipoConocimiento(),
                        "Tipo de conocimiento debe coincidir"),
                () -> assertTrue(conocimientoRepository.existsById(responseDto.getId()),
                        "Conocimiento debe existir en la base de datos")
        );
    }

    @Test
    void saveConocimiento_conImagen_debeRetornarConocimientoConImagen() throws Exception {
        // ARRANGE - contexto: conocimiento con imagen
        ConocimientoDto conocimientoDto = crearConocimientoDtoConImagen(
                "Angular",
                Nivel.ALTO,
                TipoConocimiento.FRONTEND,
                "https://example.com/angular.png",
                "Logo de Angular"
        );

        // ACT - acción: guardar conocimiento
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la imagen fue guardada
        ConocimientoDto responseDto = objectMapper.readValue(responseJson, ConocimientoDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertNotNull(responseDto.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/angular.png", responseDto.getImagen().getUrl(),
                        "URL de la imagen debe coincidir"),
                () -> assertEquals("Logo de Angular", responseDto.getImagen().getAlt(),
                        "Alt de la imagen debe coincidir")
        );
    }

    @Test
    void saveConocimiento_conNombreNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: conocimiento con nombre nulo
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .nombre(null)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveConocimiento_conNivelNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: conocimiento con nivel nulo
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .nombre("Spring Boot")
                .nivel(null)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveConocimiento_conTipoConocimientoNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: conocimiento con tipo de conocimiento nulo
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .nombre("Spring Boot")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(null)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllConocimientos_debeRetornarTodosLosConocimientos() throws Exception {
        // ARRANGE - contexto: crear y guardar conocimientos
        Conocimiento conocimiento1 = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        Conocimiento conocimiento2 = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        conocimientoRepository.save(conocimiento1);
        conocimientoRepository.save(conocimiento2);

        // ACT - acción: obtener todos los conocimientos
        String responseJson = mockMvc.perform(get("/api/v1/todos/educaciones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que retorna todos los conocimientos
        ConocimientoDto[] conocimientos = objectMapper.readValue(responseJson, ConocimientoDto[].class);

        assertAll(
                () -> assertNotNull(conocimientos, "Lista no debe ser null"),
                () -> assertTrue(conocimientos.length >= 2, "Debe contener al menos 2 conocimientos"),
                () -> assertTrue(List.of(conocimientos).stream().anyMatch(c -> c.getNombre().equals("Java")),
                        "Debe contener Java"),
                () -> assertTrue(List.of(conocimientos).stream().anyMatch(c -> c.getNombre().equals("React")),
                        "Debe contener React")
        );
    }

    @Test
    void getAllConocimientos_sinConocimientos_debeRetornarListaVacia() throws Exception {
        // ARRANGE - contexto: no hay conocimientos en la base de datos
        assertEquals(0, conocimientoRepository.count(), "No debería haber conocimientos inicialmente");

        // ACT - acción: obtener todos los conocimientos
        String responseJson = mockMvc.perform(get("/api/v1/todos/educaciones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe retornar lista vacía
        ConocimientoDto[] conocimientos = objectMapper.readValue(responseJson, ConocimientoDto[].class);
        assertEquals(0, conocimientos.length, "Lista debe estar vacía");
    }

    @Test
    void getConocimientosByTipo_conTipoExistente_debeRetornarConocimientosFiltrados() throws Exception {
        // ARRANGE - contexto: crear conocimientos de diferentes tipos
        Conocimiento conocimiento1 = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        Conocimiento conocimiento2 = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        Conocimiento conocimiento3 = Conocimiento.builder()
                .nombre("Spring Boot")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        conocimientoRepository.save(conocimiento1);
        conocimientoRepository.save(conocimiento2);
        conocimientoRepository.save(conocimiento3);

        // ACT - acción: filtrar por tipo BACKEND
        String responseJson = mockMvc.perform(get("/api/v1/conocimientos/tipo/{tipo}", TipoConocimiento.BACKEND)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe retornar solo los conocimientos BACKEND
        ConocimientoDto[] conocimientos = objectMapper.readValue(responseJson, ConocimientoDto[].class);

        assertAll(
                () -> assertNotNull(conocimientos, "Lista no debe ser null"),
                () -> assertEquals(2, conocimientos.length, "Debe haber 2 conocimientos BACKEND"),
                () -> assertTrue(List.of(conocimientos).stream().allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.BACKEND),
                        "Todos deben ser de tipo BACKEND"),
                () -> assertTrue(List.of(conocimientos).stream().anyMatch(c -> c.getNombre().equals("Java")),
                        "Debe contener Java"),
                () -> assertTrue(List.of(conocimientos).stream().anyMatch(c -> c.getNombre().equals("Spring Boot")),
                        "Debe contener Spring Boot")
        );
    }

    @Test
    void getConocimientosByTipo_conTipoSinConocimientos_debeRetornarListaVacia() throws Exception {
        // ARRANGE - contexto: crear conocimientos de un solo tipo
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        conocimientoRepository.save(conocimiento);

        // ACT - acción: filtrar por tipo FRONTEND (que no existe)
        String responseJson = mockMvc.perform(get("/api/v1/conocimientos/tipo/{tipo}", TipoConocimiento.FRONTEND)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe retornar lista vacía
        ConocimientoDto[] conocimientos = objectMapper.readValue(responseJson, ConocimientoDto[].class);
        assertEquals(0, conocimientos.length, "Lista debe estar vacía");
    }

    @Test
    void getConocimientosByTipoParam_conTipoExistente_debeRetornarConocimientosFiltrados() throws Exception {
        // ARRANGE - contexto: crear conocimientos de diferentes tipos
        Conocimiento conocimiento1 = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        Conocimiento conocimiento2 = Conocimiento.builder()
                .nombre("Angular")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        Conocimiento conocimiento3 = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        conocimientoRepository.save(conocimiento1);
        conocimientoRepository.save(conocimiento2);
        conocimientoRepository.save(conocimiento3);

        // ACT - acción: filtrar por tipo FRONTEND usando Query Param
        String responseJson = mockMvc.perform(get("/api/v1/conocimientos/filtrar")
                        .param("tipo", TipoConocimiento.FRONTEND.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe retornar solo los conocimientos FRONTEND
        ConocimientoDto[] conocimientos = objectMapper.readValue(responseJson, ConocimientoDto[].class);

        assertAll(
                () -> assertNotNull(conocimientos, "Lista no debe ser null"),
                () -> assertEquals(2, conocimientos.length, "Debe haber 2 conocimientos FRONTEND"),
                () -> assertTrue(List.of(conocimientos).stream().allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.FRONTEND),
                        "Todos deben ser de tipo FRONTEND"),
                () -> assertTrue(List.of(conocimientos).stream().anyMatch(c -> c.getNombre().equals("React")),
                        "Debe contener React"),
                () -> assertTrue(List.of(conocimientos).stream().anyMatch(c -> c.getNombre().equals("Angular")),
                        "Debe contener Angular")
        );
    }

    @Test
    void updateConocimiento_conDatosValidos_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - contexto: crear y guardar conocimiento
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        Conocimiento guardado = conocimientoRepository.save(conocimiento);

        // Preparar datos actualizados
        ConocimientoDto conocimientoDtoActualizado = crearConocimientoDto(
                "Java Advanced",
                Nivel.ALTO,
                TipoConocimiento.BACKEND
        );

        // ACT - acción: actualizar conocimiento
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/conocimiento/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe actualizar correctamente
        ConocimientoDto conocimientoActualizado = objectMapper.readValue(responseJson, ConocimientoDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), conocimientoActualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("Java Advanced", conocimientoActualizado.getNombre(),
                        "Nombre debe estar actualizado"),
                () -> assertEquals(Nivel.ALTO, conocimientoActualizado.getNivel(), "Nivel debe estar actualizado"),
                () -> assertEquals(TipoConocimiento.BACKEND, conocimientoActualizado.getTipoConocimiento(),
                        "Tipo de conocimiento debe mantenerse")
        );
    }

    @Test
    void updateConocimiento_conImagenNueva_debeActualizarImagen() throws Exception {
        // ARRANGE - contexto: crear y guardar conocimiento con imagen
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        Imagen imagenVieja = Imagen.builder()
                .url("https://example.com/react_viejo.png")
                .alt("Logo React Viejo")
                .conocimiento(conocimiento)
                .build();
        conocimiento.setImagen(imagenVieja);

        Conocimiento guardado = conocimientoRepository.save(conocimiento);

        // Preparar datos con nueva imagen
        ConocimientoDto conocimientoDtoActualizado = crearConocimientoDtoConImagen(
                "React Advanced",
                Nivel.ALTO,
                TipoConocimiento.FRONTEND,
                "https://example.com/react_nuevo.png",
                "Logo React Nuevo"
        );

        // ACT - acción: actualizar conocimiento con nueva imagen
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/conocimiento/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la imagen fue actualizada
        ConocimientoDto conocimientoActualizado = objectMapper.readValue(responseJson, ConocimientoDto.class);

        assertAll(
                () -> assertNotNull(conocimientoActualizado.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/react_nuevo.png",
                        conocimientoActualizado.getImagen().getUrl(), "URL de imagen debe ser la nueva"),
                () -> assertEquals("Logo React Nuevo",
                        conocimientoActualizado.getImagen().getAlt(), "Alt de imagen debe ser el nuevo")
        );
    }

    @Test
    void updateConocimiento_eliminarImagen_debeMantenerConocimientoSinImagen() throws Exception {
        // ARRANGE - contexto: crear y guardar conocimiento con imagen
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Angular")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        Imagen imagen = Imagen.builder()
                .url("https://example.com/angular.png")
                .alt("Logo Angular")
                .conocimiento(conocimiento)
                .build();
        conocimiento.setImagen(imagen);

        Conocimiento guardado = conocimientoRepository.save(conocimiento);

        // Preparar datos sin imagen
        ConocimientoDto conocimientoDtoActualizado = crearConocimientoDto(
                "Angular Advanced",
                Nivel.ALTO,
                TipoConocimiento.FRONTEND
        );

        // ACT - acción: actualizar conocimiento sin imagen
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/conocimiento/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la imagen fue eliminada
        ConocimientoDto conocimientoActualizado = objectMapper.readValue(responseJson, ConocimientoDto.class);
        assertNull(conocimientoActualizado.getImagen(), "Imagen debe ser null");
    }

    @Test
    void updateConocimiento_conIdInexistente_debeRetornarNotFound() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;
        ConocimientoDto conocimientoDto = crearConocimientoDto(
                "Java Advanced",
                Nivel.ALTO,
                TipoConocimiento.BACKEND
        );

        // ACT & ASSERT - acción y validación: debe retornar 404 Not Found
        mockMvc.perform(put("/api/v1/auth/modificar/conocimiento/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteConocimientoById_conIdExistente_debeEliminarConocimiento() throws Exception {
        // ARRANGE - contexto: crear y guardar conocimiento
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Python")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        Conocimiento guardado = conocimientoRepository.save(conocimiento);
        assertTrue(conocimientoRepository.existsById(guardado.getId()),
                "Conocimiento debe existir antes de eliminar");

        // ACT - acción: eliminar conocimiento
        mockMvc.perform(delete("/api/v1/borrar/conocimiento/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // ASSERT - validaciones: conocimiento debe ser eliminado
        assertFalse(conocimientoRepository.existsById(guardado.getId()),
                "Conocimiento no debe existir después de eliminar");
    }

    @Test
    void deleteConocimientoById_conIdInexistente_debeLanzarExcepcion() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        mockMvc.perform(delete("/api/v1/borrar/conocimiento/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void saveConocimiento_conTodosLosTipos_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: probar todos los tipos de conocimiento
        ConocimientoDto conocimientoFrontend = crearConocimientoDto(
                "React", Nivel.ALTO, TipoConocimiento.FRONTEND);

        ConocimientoDto conocimientoBackend = crearConocimientoDto(
                "Spring Boot", Nivel.INTERMEDIO, TipoConocimiento.BACKEND);

        ConocimientoDto conocimientoBD = crearConocimientoDto(
                "PostgreSQL", Nivel.INTERMEDIO, TipoConocimiento.BASE_DATOS);

        ConocimientoDto conocimientoTesting = crearConocimientoDto(
                "JUnit", Nivel.INTERMEDIO, TipoConocimiento.TESTING);

        ConocimientoDto conocimientoIA = crearConocimientoDto(
                "TensorFlow", Nivel.PRINCIPIANTE_BASICO, TipoConocimiento.IA);

        ConocimientoDto conocimientoPrototipo = crearConocimientoDto(
                "Figma", Nivel.INTERMEDIO, TipoConocimiento.PROTOTIPO);

        ConocimientoDto conocimientoOtros = crearConocimientoDto(
                "Git", Nivel.INTERMEDIO, TipoConocimiento.OTROS);

        // ACT & ASSERT - acción y validación: guardar todos los conocimientos y verificar
        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoFrontend)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoBackend)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoBD)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoTesting)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoIA)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoPrototipo)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoOtros)))
                .andExpect(status().isCreated());

        // ASSERT - validaciones: verificar que todos fueron guardados
        assertEquals(7, conocimientoRepository.count(), "Debe haber 7 conocimientos en la BD");

        // Verificar que cada tipo existe
        List<Conocimiento> frontendList = conocimientoRepository.findByTipoConocimiento(TipoConocimiento.FRONTEND);
        List<Conocimiento> backendList = conocimientoRepository.findByTipoConocimiento(TipoConocimiento.BACKEND);
        List<Conocimiento> bdList = conocimientoRepository.findByTipoConocimiento(TipoConocimiento.BASE_DATOS);
        List<Conocimiento> testingList = conocimientoRepository.findByTipoConocimiento(TipoConocimiento.TESTING);
        List<Conocimiento> iaList = conocimientoRepository.findByTipoConocimiento(TipoConocimiento.IA);
        List<Conocimiento> prototipoList = conocimientoRepository.findByTipoConocimiento(TipoConocimiento.PROTOTIPO);
        List<Conocimiento> otrosList = conocimientoRepository.findByTipoConocimiento(TipoConocimiento.OTROS);

        assertAll(
                () -> assertEquals(1, frontendList.size(), "Debe haber 1 conocimiento FRONTEND"),
                () -> assertEquals(1, backendList.size(), "Debe haber 1 conocimiento BACKEND"),
                () -> assertEquals(1, bdList.size(), "Debe haber 1 conocimiento BASE_DATOS"),
                () -> assertEquals(1, testingList.size(), "Debe haber 1 conocimiento TESTING"),
                () -> assertEquals(1, iaList.size(), "Debe haber 1 conocimiento IA"),
                () -> assertEquals(1, prototipoList.size(), "Debe haber 1 conocimiento PROTOTIPO"),
                () -> assertEquals(1, otrosList.size(), "Debe haber 1 conocimiento OTROS")
        );
    }

    @Test
    void getConocimientosByTipo_conTodosLosTipos_debeRetornarCorrectamente() throws Exception {
        // ARRANGE - contexto: crear conocimientos de todos los tipos
        TipoConocimiento[] tipos = TipoConocimiento.values();
        for (TipoConocimiento tipo : tipos) {
            Conocimiento conocimiento = Conocimiento.builder()
                    .nombre(tipo.name() + " Test")
                    .nivel(Nivel.INTERMEDIO)
                    .tipoConocimiento(tipo)
                    .build();
            conocimientoRepository.save(conocimiento);
        }

        // ACT & ASSERT - acción y validación: filtrar por cada tipo
        for (TipoConocimiento tipo : tipos) {
            String responseJson = mockMvc.perform(get("/api/v1/conocimientos/tipo/{tipo}", tipo)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            ConocimientoDto[] conocimientos = objectMapper.readValue(responseJson, ConocimientoDto[].class);
            assertEquals(1, conocimientos.length, "Debe haber exactamente 1 conocimiento de tipo " + tipo);
            assertEquals(tipo.name() + " Test", conocimientos[0].getNombre(),
                    "El nombre debe coincidir con el tipo");
        }
    }

    @Test
    void updateConocimiento_conNombreVacio_debeRetornarError() throws Exception {
        // ARRANGE - contexto: crear y guardar conocimiento
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        Conocimiento guardado = conocimientoRepository.save(conocimiento);

        // Preparar datos con nombre vacío
        ConocimientoDto conocimientoDtoActualizado = crearConocimientoDto(
                "", // Nombre vacío
                Nivel.ALTO,
                TipoConocimiento.BACKEND
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(put("/api/v1/auth/modificar/conocimiento/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDtoActualizado)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveConocimiento_conNombreLargo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: conocimiento con nombre muy largo
        String nombreLargo = "a".repeat(150);
        ConocimientoDto conocimientoDto = crearConocimientoDto(
                nombreLargo,
                Nivel.INTERMEDIO,
                TipoConocimiento.BACKEND
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/conocimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conocimientoDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getConocimientosByTipo_conTipoInvalido_debeRetornarError() throws Exception {
        // ARRANGE - contexto: tipo inválido
        String tipoInvalido = "INVALIDO";

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(get("/api/v1/conocimientos/tipo/{tipo}", tipoInvalido)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}