package com.example.demo.service;

import com.example.demo.dto.HabilidadDto;
import com.example.demo.model.Habilidad;
import com.example.demo.repository.HabilidadRepository;
import com.example.demo.service.Impl.HabilidadServiceImpl;
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
class HabilidadServiceImplIntegrationTest {

    @Autowired
    private HabilidadServiceImpl habilidadService;

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
    void saveHabilidad_conDatosValidos_debeGuardarYRetornarHabilidad() {
        // ARRANGE - contexto: habilidad con datos válidos
        HabilidadDto habilidadDto = crearHabilidadDto("Comunicación Efectiva");

        // ACT - acción: guardar habilidad
        HabilidadDto resultado = habilidadService.saveHabilidad(habilidadDto);

        // ASSERT - validaciones: verificar que la habilidad fue guardada correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Comunicación Efectiva", resultado.getNombre(), "Nombre debe coincidir"),
                () -> assertTrue(habilidadRepository.existsById(resultado.getId()),
                        "Habilidad debe existir en la base de datos")
        );
    }

    @Test
    void saveHabilidad_conNombreNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: habilidad con nombre nulo
        HabilidadDto habilidadDto = HabilidadDto.builder()
                .nombre(null)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            habilidadService.saveHabilidad(habilidadDto);
        }, "Debe lanzar excepción cuando el nombre es nulo");
    }

    @Test
    void saveHabilidad_conNombreVacio_debeLanzarExcepcion() {
        // ARRANGE - contexto: habilidad con nombre vacío
        HabilidadDto habilidadDto = crearHabilidadDto("");

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            habilidadService.saveHabilidad(habilidadDto);
        }, "Debe lanzar excepción cuando el nombre está vacío");
    }

    @Test
    void saveHabilidad_conNombreMuyCorto_debeLanzarExcepcion() {
        // ARRANGE - contexto: habilidad con nombre muy corto
        HabilidadDto habilidadDto = crearHabilidadDto("Co"); // Menos de 3 caracteres

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            habilidadService.saveHabilidad(habilidadDto);
        }, "Debe lanzar excepción cuando el nombre es muy corto");
    }

    @Test
    void saveHabilidad_conNombreLargo_debeLanzarExcepcion() {
        // ARRANGE - contexto: habilidad con nombre muy largo
        String nombreLargo = "a".repeat(150);
        HabilidadDto habilidadDto = crearHabilidadDto(nombreLargo);

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            habilidadService.saveHabilidad(habilidadDto);
        }, "Debe lanzar excepción cuando el nombre es demasiado largo");
    }

    @Test
    void saveHabilidad_conNombreExactamenteDeLongitudMinima_debeGuardarCorrectamente() {
        // ARRANGE - contexto: nombre exactamente de 3 caracteres (mínimo permitido)
        String nombre = "ABC";
        HabilidadDto habilidadDto = crearHabilidadDto(nombre);

        // ACT - acción: guardar habilidad
        HabilidadDto resultado = habilidadService.saveHabilidad(habilidadDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals(nombre, resultado.getNombre(), "Nombre debe coincidir"),
                () -> assertTrue(habilidadRepository.existsById(resultado.getId()),
                        "Habilidad debe existir en la BD")
        );
    }

    @Test
    void saveHabilidad_conNombreExactamenteDeLongitudMaxima_debeGuardarCorrectamente() {
        // ARRANGE - contexto: nombre exactamente de 145 caracteres (máximo permitido)
        String nombre = "a".repeat(145);
        HabilidadDto habilidadDto = crearHabilidadDto(nombre);

        // ACT - acción: guardar habilidad
        HabilidadDto resultado = habilidadService.saveHabilidad(habilidadDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals(nombre, resultado.getNombre(), "Nombre debe coincidir"),
                () -> assertTrue(habilidadRepository.existsById(resultado.getId()),
                        "Habilidad debe existir en la BD")
        );
    }

    @Test
    void saveHabilidad_conNombreConCaracteresEspeciales_debeGuardarCorrectamente() {
        // ARRANGE - contexto: nombre con caracteres especiales
        HabilidadDto habilidadDto = crearHabilidadDto("Comunicación & Trabajo en Equipo");

        // ACT - acción: guardar habilidad
        HabilidadDto resultado = habilidadService.saveHabilidad(habilidadDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("Comunicación & Trabajo en Equipo", resultado.getNombre(),
                        "Nombre con caracteres especiales debe coincidir"),
                () -> assertTrue(habilidadRepository.existsById(resultado.getId()),
                        "Habilidad debe existir en la BD")
        );
    }

    @Test
    void getAllHabilidad_debeRetornarTodasLasHabilidades() {
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
        List<HabilidadDto> habilidades = habilidadService.getAllHabilidad();

        // ASSERT - validaciones: verificar que retorna todas las habilidades
        assertAll(
                () -> assertNotNull(habilidades, "Lista no debe ser null"),
                () -> assertFalse(habilidades.isEmpty(), "Lista no debe estar vacía"),
                () -> assertTrue(habilidades.size() >= 2, "Debe contener al menos 2 habilidades"),
                () -> assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Comunicación Efectiva")),
                        "Debe contener Comunicación Efectiva"),
                () -> assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Trabajo en Equipo")),
                        "Debe contener Trabajo en Equipo"),
                () -> assertNotNull(habilidades.get(0).getId(), "ID debe estar presente")
        );
    }

    @Test
    void getAllHabilidad_sinHabilidades_debeRetornarListaVacia() {
        // ARRANGE - contexto: no hay habilidades en la base de datos
        assertEquals(0, habilidadRepository.count(), "No debería haber habilidades inicialmente");

        // ACT - acción: obtener todas las habilidades
        List<HabilidadDto> habilidades = habilidadService.getAllHabilidad();

        // ASSERT - validaciones: debe retornar lista vacía
        assertNotNull(habilidades, "Lista no debe ser null");
        assertTrue(habilidades.isEmpty(), "Lista debe estar vacía");
        assertEquals(0, habilidades.size(), "Tamaño debe ser 0");
    }

    @Test
    void getAllHabilidad_conMultiplesHabilidades_debeRetornarEnOrdenCorrecto() {
        // ARRANGE - contexto: crear y guardar múltiples habilidades
        for (int i = 1; i <= 5; i++) {
            Habilidad habilidad = Habilidad.builder()
                    .nombre("Habilidad " + i)
                    .build();
            habilidadRepository.save(habilidad);
        }

        // ACT - acción: obtener todas las habilidades
        List<HabilidadDto> habilidades = habilidadService.getAllHabilidad();

        // ASSERT - validaciones: debe retornar todas las habilidades
        assertAll(
                () -> assertNotNull(habilidades, "Lista no debe ser null"),
                () -> assertEquals(5, habilidades.size(), "Debe haber exactamente 5 habilidades"),
                () -> assertTrue(habilidades.stream().allMatch(h -> h.getId() != null),
                        "Todas las habilidades deben tener ID"),
                () -> assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Habilidad 3")),
                        "Debe contener Habilidad 3"),
                () -> assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Habilidad 5")),
                        "Debe contener Habilidad 5")
        );
    }

    @Test
    void actualizarHabilidadPorId_conDatosValidos_debeActualizarCorrectamente() {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Comunicación")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);

        // Preparar datos actualizados
        HabilidadDto habilidadDtoActualizado = crearHabilidadDto("Comunicación Asertiva");

        // ACT - acción: actualizar habilidad
        HabilidadDto actualizado = habilidadService.actualizarHabilidadPorId(
                guardado.getId(), habilidadDtoActualizado);

        // ASSERT - validaciones: debe actualizar correctamente
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("Comunicación Asertiva", actualizado.getNombre(),
                        "Nombre debe estar actualizado")
        );
    }

    @Test
    void actualizarHabilidadPorId_cambiarNombreCompletamente_debeActualizarCorrectamente() {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Habilidad Original")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);

        // Preparar datos con nombre completamente diferente
        HabilidadDto habilidadDtoActualizado = crearHabilidadDto("Nueva Habilidad");

        // ACT - acción: actualizar habilidad
        HabilidadDto actualizado = habilidadService.actualizarHabilidadPorId(
                guardado.getId(), habilidadDtoActualizado);

        // ASSERT - validaciones: debe actualizar correctamente
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("Nueva Habilidad", actualizado.getNombre(),
                        "Nombre debe estar completamente actualizado")
        );
    }

    @Test
    void actualizarHabilidadPorId_conIdInexistente_debeRetornarNull() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;
        HabilidadDto habilidadDto = crearHabilidadDto("Habilidad Actualizada");

        // ACT - acción: actualizar habilidad con ID inexistente
        HabilidadDto actualizado = habilidadService.actualizarHabilidadPorId(
                idInexistente, habilidadDto);

        // ASSERT - validaciones: debe retornar null
        assertNull(actualizado, "Debe retornar null para ID inexistente");
    }

    @Test
    void actualizarHabilidadPorId_conNombreNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Comunicación")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);

        // Preparar datos con nombre nulo
        HabilidadDto habilidadDtoActualizado = HabilidadDto.builder()
                .nombre(null)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            habilidadService.actualizarHabilidadPorId(guardado.getId(), habilidadDtoActualizado);
        }, "Debe lanzar excepción cuando el nombre es nulo");
    }

    @Test
    void actualizarHabilidadPorId_conNombreVacio_debeLanzarExcepcion() {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Comunicación")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);

        // Preparar datos con nombre vacío
        HabilidadDto habilidadDtoActualizado = crearHabilidadDto("");

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            habilidadService.actualizarHabilidadPorId(guardado.getId(), habilidadDtoActualizado);
        }, "Debe lanzar excepción cuando el nombre está vacío");
    }

    @Test
    void deleteHabilidadPorId_conIdExistente_debeEliminarHabilidad() {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Habilidad a Eliminar")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);
        assertTrue(habilidadRepository.existsById(guardado.getId()),
                "Habilidad debe existir antes de eliminar");

        // ACT - acción: eliminar habilidad
        habilidadService.deleteHabilidadPorId(guardado.getId());

        // ASSERT - validaciones: habilidad debe ser eliminada
        assertFalse(habilidadRepository.existsById(guardado.getId()),
                "Habilidad no debe existir después de eliminar");
        assertEquals(0, habilidadRepository.count(), "No debe haber habilidades en la BD");
    }

    @Test
    void deleteHabilidadPorId_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(EmptyResultDataAccessException.class, () -> {
            habilidadService.deleteHabilidadPorId(idInexistente);
        }, "Debe lanzar EmptyResultDataAccessException para ID inexistente");
    }

    @Test
    void saveHabilidad_conMultiplesHabilidades_debeGuardarTodasCorrectamente() {
        // ARRANGE - contexto: múltiples habilidades
        String[] nombres = {
                "Comunicación Efectiva",
                "Trabajo en Equipo",
                "Adaptabilidad",
                "Rápido Aprendizaje",
                "Trabajo Bajo Presión"
        };

        // ACT - acción: guardar todas las habilidades
        for (String nombre : nombres) {
            HabilidadDto habilidadDto = crearHabilidadDto(nombre);
            habilidadService.saveHabilidad(habilidadDto);
        }

        // ASSERT - validaciones: verificar que todas fueron guardadas
        assertEquals(nombres.length, habilidadRepository.count(),
                "Debe haber " + nombres.length + " habilidades en la BD");

        // Verificar que cada una existe
        for (String nombre : nombres) {
            List<Habilidad> encontradas = habilidadRepository.findAll().stream()
                    .filter(h -> h.getNombre().equals(nombre))
                    .toList();
            assertEquals(1, encontradas.size(), "Debe existir exactamente una habilidad con nombre: " + nombre);
        }
    }

    @Test
    void saveHabilidad_conHabilidadesDuplicadas_debeGuardarTodas() {
        // ARRANGE - contexto: habilidades con nombres duplicados
        String nombre = "Comunicación Efectiva";

        // ACT - acción: guardar la misma habilidad múltiples veces
        for (int i = 0; i < 3; i++) {
            HabilidadDto habilidadDto = crearHabilidadDto(nombre);
            habilidadService.saveHabilidad(habilidadDto);
        }

        // ASSERT - validaciones: debe haber 3 habilidades con el mismo nombre
        List<Habilidad> habilidades = habilidadRepository.findAll();
        assertEquals(3, habilidades.size(), "Debe haber 3 habilidades en la BD");

        long count = habilidades.stream()
                .filter(h -> h.getNombre().equals(nombre))
                .count();
        assertEquals(3, count, "Debe haber 3 habilidades con el mismo nombre");
    }

    @Test
    void actualizarHabilidadPorId_conIdExistenteYNombreLargo_debeActualizarCorrectamente() {
        // ARRANGE - contexto: crear y guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Habilidad Corta")
                .build();

        Habilidad guardado = habilidadRepository.save(habilidad);

        // Preparar datos con nombre largo (pero dentro del límite)
        String nombreLargo = "Habilidad con un nombre bastante largo pero aún permitido";
        HabilidadDto habilidadDtoActualizado = crearHabilidadDto(nombreLargo);

        // ACT - acción: actualizar habilidad
        HabilidadDto actualizado = habilidadService.actualizarHabilidadPorId(
                guardado.getId(), habilidadDtoActualizado);

        // ASSERT - validaciones: debe actualizar correctamente
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals(nombreLargo, actualizado.getNombre(),
                        "Nombre debe estar actualizado")
        );
    }

    @Test
    void getAllHabilidad_despuesDeEliminar_debeRetornarSoloRestantes() {
        // ARRANGE - contexto: crear y guardar habilidades
        Habilidad habilidad1 = Habilidad.builder()
                .nombre("Habilidad 1")
                .build();

        Habilidad habilidad2 = Habilidad.builder()
                .nombre("Habilidad 2")
                .build();

        Habilidad habilidad3 = Habilidad.builder()
                .nombre("Habilidad 3")
                .build();

        Habilidad guardado1 = habilidadRepository.save(habilidad1);
        habilidadRepository.save(habilidad2);
        Habilidad guardado3 = habilidadRepository.save(habilidad3);

        // ACT - acción: eliminar una habilidad y obtener todas
        habilidadService.deleteHabilidadPorId(guardado1.getId());
        List<HabilidadDto> habilidades = habilidadService.getAllHabilidad();

        // ASSERT - validaciones: solo deben quedar 2 habilidades
        assertAll(
                () -> assertEquals(2, habilidades.size(), "Debe haber 2 habilidades restantes"),
                () -> assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Habilidad 2")),
                        "Debe contener Habilidad 2"),
                () -> assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Habilidad 3")),
                        "Debe contener Habilidad 3"),
                () -> assertTrue(habilidades.stream().noneMatch(h -> h.getNombre().equals("Habilidad 1")),
                        "No debe contener Habilidad 1")
        );
    }
}