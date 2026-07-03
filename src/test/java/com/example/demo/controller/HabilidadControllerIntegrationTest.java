package com.example.demo.controller;

import com.example.demo.dto.HabilidadDto;
import com.example.demo.model.Habilidad;
import com.example.demo.repository.HabilidadRepository;
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
class HabilidadControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HabilidadRepository habilidadRepository;

    private HabilidadDto crearHabilidadDto(String nombre) {
        return HabilidadDto.builder()
                .nombre(nombre)
                .build();
    }

    @BeforeEach
    void setUp() {
        // Limpiar datos antes de cada prueba
        habilidadRepository.deleteAll();
    }

    @Test
    void saveHabilidad_conDatosValidos_debeRetornarHabilidadCreada() throws Exception {
        // ARRANGE - contexto: habilidad con datos válidos
        HabilidadDto habilidadDto = crearHabilidadDto("Comunicación Efectiva");

        // ACT - acción: guardar habilidad a través del endpoint
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/habilidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la habilidad fue guardada en BD
        HabilidadDto responseDto = objectMapper.readValue(responseJson, HabilidadDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Comunicación Efectiva", responseDto.getNombre(), "Nombre debe coincidir"),
                () -> assertTrue(habilidadRepository.existsById(responseDto.getId()),
                        "Habilidad debe existir en la base de datos")
        );
    }

    @Test
    void saveHabilidad_conNombreNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: habilidad con nombre nulo
        HabilidadDto habilidadDto = HabilidadDto.builder()
                .nombre(null)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/habilidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveHabilidad_conNombreVacio_debeRetornarError() throws Exception {
        // ARRANGE - contexto: habilidad con nombre vacío
        HabilidadDto habilidadDto = crearHabilidadDto("");

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/habilidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveHabilidad_conNombreMuyCorto_debeRetornarError() throws Exception {
        // ARRANGE - contexto: habilidad con nombre muy corto
        HabilidadDto habilidadDto = crearHabilidadDto("Co"); // Menos de 3 caracteres

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/habilidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveHabilidad_conNombreLargo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: habilidad con nombre muy largo
        String nombreLargo = "a".repeat(150);
        HabilidadDto habilidadDto = crearHabilidadDto(nombreLargo);

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/auth/guardar/habilidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveHabilidad_conNombreConCaracteresEspeciales_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: habilidad con caracteres especiales
        HabilidadDto habilidadDto = crearHabilidadDto("Comunicación & Trabajo en Equipo");

        // ACT - acción: guardar habilidad
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/habilidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe guardar correctamente
        HabilidadDto responseDto = objectMapper.readValue(responseJson, HabilidadDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertEquals("Comunicación & Trabajo en Equipo", responseDto.getNombre(),
                        "Nombre con caracteres especiales debe coincidir"),
                () -> assertTrue(habilidadRepository.existsById(responseDto.getId()),
                        "Habilidad debe existir en la BD")
        );
    }

    @Test
    void getAllHabilidades_debeRetornarTodasLasHabilidades() throws Exception {
        // ARRANGE - contexto: crear y guardar habilidades
        Habilidad habilidad1 = Habilidad.builder()
                .nombre("Comunicación Efectiva")
                .build();

        Habilidad habilidad2 = Habilidad.builder()
                .nombre("Trabajo en Equipo")
                .build();

        habilidadRepository.save(habilidad1);
        habilidadRepository.save(habilidad2);

        // ACT - acción: obtener todas las habilidades
        String responseJson = mockMvc.perform(get("/api/v1/todas/habilidades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que retorna todas las habilidades
        HabilidadDto[] habilidades = objectMapper.readValue(responseJson, HabilidadDto[].class);

        assertAll(
                () -> assertNotNull(habilidades, "Lista no debe ser null"),
                () -> assertTrue(habilidades.length >= 2, "Debe contener al menos 2 habilidades"),
                () -> assertTrue(List.of(habilidades).stream().anyMatch(h -> h.getNombre().equals("Comunicación Efectiva")),
                        "Debe contener Comunicación Efectiva"),
                () -> assertTrue(List.of(habilidades).stream().anyMatch(h -> h.getNombre().equals("Trabajo en Equipo")),
                        "Debe contener Trabajo en Equipo")
        );
    }

    @Test
    void getAllHabilidades_sinHabilidades_debeRetornarListaVacia() throws Exception {
        // ARRANGE - contexto: no hay habilidades en la base de datos
        assertEquals(0, habilidadRepository.count(), "No debería haber habilidades inicialmente");

        // ACT - acción: obtener todas las habilidades
        String responseJson = mockMvc.perform(get("/api/v1/todas/habilidades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe retornar lista vacía
        HabilidadDto[] habilidades = objectMapper.readValue(responseJson, HabilidadDto[].class);
        assertEquals(0, habilidades.length, "Lista debe estar vacía");
    }

    @Test
    void updateHabilidad_conDatosValidos_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Comunicación")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);

        // Preparar datos actualizados
        HabilidadDto habilidadDtoActualizado = crearHabilidadDto("Comunicación Asertiva");

        // ACT - acción: actualizar habilidad
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/habilidad/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe actualizar correctamente
        HabilidadDto habilidadActualizada = objectMapper.readValue(responseJson, HabilidadDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), habilidadActualizada.getId(), "ID debe mantenerse"),
                () -> assertEquals("Comunicación Asertiva", habilidadActualizada.getNombre(),
                        "Nombre debe estar actualizado")
        );
    }

    @Test
    void updateHabilidad_conIdInexistente_debeRetornarNotFound() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;
        HabilidadDto habilidadDto = crearHabilidadDto("Habilidad Actualizada");

        // ACT & ASSERT - acción y validación: debe retornar 404 Not Found
        mockMvc.perform(put("/api/v1/auth/modificar/habilidad/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateHabilidad_conNombreNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Comunicación")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);

        // Preparar datos con nombre nulo
        HabilidadDto habilidadDtoActualizado = HabilidadDto.builder()
                .nombre(null)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(put("/api/v1/auth/modificar/habilidad/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDtoActualizado)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateHabilidad_conNombreVacio_debeRetornarError() throws Exception {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Comunicación")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);

        // Preparar datos con nombre vacío
        HabilidadDto habilidadDtoActualizado = crearHabilidadDto("");

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(put("/api/v1/auth/modificar/habilidad/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDtoActualizado)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteHabilidadById_conIdExistente_debeEliminarHabilidad() throws Exception {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Habilidad a Eliminar")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);
        assertTrue(habilidadRepository.existsById(guardado.getId()),
                "Habilidad debe existir antes de eliminar");

        // ACT - acción: eliminar habilidad
        mockMvc.perform(delete("/api/v1/borrar/habilidad/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // ASSERT - validaciones: habilidad debe ser eliminada
        assertFalse(habilidadRepository.existsById(guardado.getId()),
                "Habilidad no debe existir después de eliminar");
    }

    @Test
    void deleteHabilidadById_conIdInexistente_debeLanzarExcepcion() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        mockMvc.perform(delete("/api/v1/borrar/habilidad/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void saveHabilidad_conMultiplesHabilidades_debeGuardarTodasCorrectamente() throws Exception {
        // ARRANGE - contexto: múltiples habilidades
        String[] habilidades = {
                "Comunicación Efectiva",
                "Trabajo en Equipo",
                "Adaptabilidad",
                "Rápido Aprendizaje",
                "Trabajo Bajo Presión"
        };

        // ACT - acción: guardar todas las habilidades
        for (String nombre : habilidades) {
            HabilidadDto habilidadDto = crearHabilidadDto(nombre);
            mockMvc.perform(post("/api/v1/auth/guardar/habilidad")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(habilidadDto)))
                    .andExpect(status().isCreated());
        }

        // ASSERT - validaciones: verificar que todas fueron guardadas
        assertEquals(habilidades.length, habilidadRepository.count(),
                "Debe haber " + habilidades.length + " habilidades en la BD");

        // Verificar que cada una existe
        for (String nombre : habilidades) {
            List<Habilidad> encontradas = habilidadRepository.findAll().stream()
                    .filter(h -> h.getNombre().equals(nombre))
                    .toList();
            assertEquals(1, encontradas.size(), "Debe existir exactamente una habilidad con nombre: " + nombre);
        }
    }

    @Test
    void saveHabilidad_conNombreExactamenteDeLongitudMinima_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: nombre exactamente de 3 caracteres (mínimo permitido)
        String nombre = "ABC";
        HabilidadDto habilidadDto = crearHabilidadDto(nombre);

        // ACT - acción: guardar habilidad
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/habilidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe guardar correctamente
        HabilidadDto responseDto = objectMapper.readValue(responseJson, HabilidadDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertEquals(nombre, responseDto.getNombre(), "Nombre debe coincidir"),
                () -> assertTrue(habilidadRepository.existsById(responseDto.getId()),
                        "Habilidad debe existir en la BD")
        );
    }

    @Test
    void saveHabilidad_conNombreExactamenteDeLongitudMaxima_debeGuardarCorrectamente() throws Exception {
        // ARRANGE - contexto: nombre exactamente de 145 caracteres (máximo permitido)
        String nombre = "a".repeat(145);
        HabilidadDto habilidadDto = crearHabilidadDto(nombre);

        // ACT - acción: guardar habilidad
        String responseJson = mockMvc.perform(post("/api/v1/auth/guardar/habilidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe guardar correctamente
        HabilidadDto responseDto = objectMapper.readValue(responseJson, HabilidadDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertEquals(nombre, responseDto.getNombre(), "Nombre debe coincidir"),
                () -> assertTrue(habilidadRepository.existsById(responseDto.getId()),
                        "Habilidad debe existir en la BD")
        );
    }

    @Test
    void updateHabilidad_cambiarNombreCompletamente_debeActualizarCorrectamente() throws Exception {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Habilidad Original")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);

        // Preparar datos con nombre completamente diferente
        HabilidadDto habilidadDtoActualizado = crearHabilidadDto("Nueva Habilidad");

        // ACT - acción: actualizar habilidad
        String responseJson = mockMvc.perform(put("/api/v1/auth/modificar/habilidad/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habilidadDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe actualizar correctamente
        HabilidadDto habilidadActualizada = objectMapper.readValue(responseJson, HabilidadDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), habilidadActualizada.getId(), "ID debe mantenerse"),
                () -> assertEquals("Nueva Habilidad", habilidadActualizada.getNombre(),
                        "Nombre debe estar completamente actualizado")
        );
    }

    @Test
    void getAllHabilidades_conMuchasHabilidades_debeRetornarTodas() throws Exception {
        // ARRANGE - contexto: crear muchas habilidades
        int cantidad = 20;
        for (int i = 1; i <= cantidad; i++) {
            Habilidad habilidad = Habilidad.builder()
                    .nombre("Habilidad " + i)
                    .build();
            habilidadRepository.save(habilidad);
        }

        // ACT - acción: obtener todas las habilidades
        String responseJson = mockMvc.perform(get("/api/v1/todas/habilidades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe retornar todas
        HabilidadDto[] habilidades = objectMapper.readValue(responseJson, HabilidadDto[].class);
        assertEquals(cantidad, habilidades.length, "Debe haber " + cantidad + " habilidades");
    }
}