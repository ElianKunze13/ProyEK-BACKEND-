package com.example.demo.service;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoConocimiento;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ConocimientoRepository;
import com.example.demo.service.Impl.ConocimientoServiceImpl;
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
class ConocimientoServiceImplIntegrationTest {

    @Autowired
    private ConocimientoServiceImpl conocimientoService;

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
    void saveConocimiento_conDatosValidos_debeGuardarYRetornarConocimiento() {
        // ARRANGE - contexto: conocimiento con datos válidos
        ConocimientoDto conocimientoDto = crearConocimientoDto(
                "Spring Boot",
                Nivel.INTERMEDIO,
                TipoConocimiento.BACKEND
        );

        // ACT - acción: guardar conocimiento
        ConocimientoDto resultado = conocimientoService.saveConocimiento(conocimientoDto);

        // ASSERT - validaciones: verificar que el conocimiento fue guardado correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Spring Boot", resultado.getNombre(), "Nombre debe coincidir"),
                () -> assertEquals(Nivel.INTERMEDIO, resultado.getNivel(), "Nivel debe coincidir"),
                () -> assertEquals(TipoConocimiento.BACKEND, resultado.getTipoConocimiento(),
                        "Tipo de conocimiento debe coincidir"),
                () -> assertTrue(conocimientoRepository.existsById(resultado.getId()),
                        "Conocimiento debe existir en la base de datos")
        );
    }

    @Test
    void saveConocimiento_conImagen_debeGuardarConocimientoConImagen() {
        // ARRANGE - contexto: conocimiento con imagen
        ConocimientoDto conocimientoDto = crearConocimientoDtoConImagen(
                "Angular",
                Nivel.ALTO,
                TipoConocimiento.FRONTEND,
                "https://example.com/angular.png",
                "Logo de Angular"
        );

        // ACT - acción: guardar conocimiento
        ConocimientoDto resultado = conocimientoService.saveConocimiento(conocimientoDto);

        // ASSERT - validaciones: verificar que la imagen fue guardada
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertNotNull(resultado.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/angular.png", resultado.getImagen().getUrl(),
                        "URL de la imagen debe coincidir"),
                () -> assertEquals("Logo de Angular", resultado.getImagen().getAlt(),
                        "Alt de la imagen debe coincidir")
        );
    }

    @Test
    void saveConocimiento_conNombreNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: conocimiento con nombre nulo
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .nombre(null)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoService.saveConocimiento(conocimientoDto);
        }, "Debe lanzar excepción cuando el nombre es nulo");
    }

    @Test
    void saveConocimiento_conNombreVacio_debeLanzarExcepcion() {
        // ARRANGE - contexto: conocimiento con nombre vacío
        ConocimientoDto conocimientoDto = crearConocimientoDto(
                "", // Nombre vacío
                Nivel.INTERMEDIO,
                TipoConocimiento.BACKEND
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoService.saveConocimiento(conocimientoDto);
        }, "Debe lanzar excepción cuando el nombre está vacío");
    }

    @Test
    void saveConocimiento_conNivelNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: conocimiento con nivel nulo
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .nombre("Spring Boot")
                .nivel(null)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoService.saveConocimiento(conocimientoDto);
        }, "Debe lanzar excepción cuando el nivel es nulo");
    }

    @Test
    void saveConocimiento_conTipoConocimientoNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: conocimiento con tipo de conocimiento nulo
        ConocimientoDto conocimientoDto = ConocimientoDto.builder()
                .nombre("Spring Boot")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(null)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoService.saveConocimiento(conocimientoDto);
        }, "Debe lanzar excepción cuando el tipo de conocimiento es nulo");
    }

    @Test
    void saveConocimiento_conNombreLargo_debeLanzarExcepcion() {
        // ARRANGE - contexto: conocimiento con nombre muy largo
        String nombreLargo = "a".repeat(150);
        ConocimientoDto conocimientoDto = crearConocimientoDto(
                nombreLargo,
                Nivel.INTERMEDIO,
                TipoConocimiento.BACKEND
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoService.saveConocimiento(conocimientoDto);
        }, "Debe lanzar excepción cuando el nombre es demasiado largo");
    }

    @Test
    void getAllConocimientos_debeRetornarTodosLosConocimientos() {
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
        List<ConocimientoDto> conocimientos = conocimientoService.getAllConocimientos();

        // ASSERT - validaciones: verificar que retorna todos los conocimientos
        assertAll(
                () -> assertNotNull(conocimientos, "Lista no debe ser null"),
                () -> assertFalse(conocimientos.isEmpty(), "Lista no debe estar vacía"),
                () -> assertTrue(conocimientos.size() >= 2, "Debe contener al menos 2 conocimientos"),
                () -> assertTrue(conocimientos.stream().anyMatch(c -> c.getNombre().equals("Java")),
                        "Debe contener Java"),
                () -> assertTrue(conocimientos.stream().anyMatch(c -> c.getNombre().equals("React")),
                        "Debe contener React")
        );
    }

    @Test
    void getAllConocimientos_sinConocimientos_debeRetornarListaVacia() {
        // ARRANGE - contexto: no hay conocimientos en la base de datos
        assertEquals(0, conocimientoRepository.count(), "No debería haber conocimientos inicialmente");

        // ACT - acción: obtener todos los conocimientos
        List<ConocimientoDto> conocimientos = conocimientoService.getAllConocimientos();

        // ASSERT - validaciones: debe retornar lista vacía
        assertNotNull(conocimientos, "Lista no debe ser null");
        assertTrue(conocimientos.isEmpty(), "Lista debe estar vacía");
        assertEquals(0, conocimientos.size(), "Tamaño debe ser 0");
    }

    @Test
    void getAllConocimientos_conImagenes_debeRetornarConocimientosConImagenes() {
        // ARRANGE - contexto: crear conocimiento con imagen
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Angular")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        Imagen imagen = Imagen.builder()
                .url("https://example.com/angular.png")
                .alt("Logo de Angular")
                .conocimiento(conocimiento)
                .build();
        conocimiento.setImagen(imagen);

        conocimientoRepository.save(conocimiento);

        // ACT - acción: obtener todos los conocimientos
        List<ConocimientoDto> conocimientos = conocimientoService.getAllConocimientos();

        // ASSERT - validaciones: verificar que la imagen está presente
        assertAll(
                () -> assertFalse(conocimientos.isEmpty(), "Lista no debe estar vacía"),
                () -> assertNotNull(conocimientos.get(0).getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/angular.png",
                        conocimientos.get(0).getImagen().getUrl(), "URL de imagen debe coincidir")
        );
    }

    @Test
    void filtrarPorTipo_conTipoExistente_debeRetornarConocimientosFiltrados() {
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
        List<ConocimientoDto> conocimientosBackend = conocimientoService.filtrarPorTipo(TipoConocimiento.BACKEND);

        // ASSERT - validaciones: debe retornar solo los conocimientos BACKEND
        assertAll(
                () -> assertNotNull(conocimientosBackend, "Lista no debe ser null"),
                () -> assertEquals(2, conocimientosBackend.size(), "Debe haber 2 conocimientos BACKEND"),
                () -> assertTrue(conocimientosBackend.stream().allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.BACKEND),
                        "Todos deben ser de tipo BACKEND"),
                () -> assertTrue(conocimientosBackend.stream().anyMatch(c -> c.getNombre().equals("Java")),
                        "Debe contener Java"),
                () -> assertTrue(conocimientosBackend.stream().anyMatch(c -> c.getNombre().equals("Spring Boot")),
                        "Debe contener Spring Boot")
        );
    }

    @Test
    void filtrarPorTipo_conTipoSinConocimientos_debeRetornarListaVacia() {
        // ARRANGE - contexto: crear conocimientos de un solo tipo
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        conocimientoRepository.save(conocimiento);

        // ACT - acción: filtrar por tipo FRONTEND (que no existe)
        List<ConocimientoDto> conocimientosFrontend = conocimientoService.filtrarPorTipo(TipoConocimiento.FRONTEND);

        // ASSERT - validaciones: debe retornar lista vacía
        assertNotNull(conocimientosFrontend, "Lista no debe ser null");
        assertTrue(conocimientosFrontend.isEmpty(), "Lista debe estar vacía");
    }

    @Test
    void filtrarPorTipo_conTipoFrontend_debeRetornarSoloFrontend() {
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

        // ACT - acción: filtrar por tipo FRONTEND
        List<ConocimientoDto> conocimientosFrontend = conocimientoService.filtrarPorTipo(TipoConocimiento.FRONTEND);

        // ASSERT - validaciones: debe retornar solo los conocimientos FRONTEND
        assertAll(
                () -> assertNotNull(conocimientosFrontend, "Lista no debe ser null"),
                () -> assertEquals(2, conocimientosFrontend.size(), "Debe haber 2 conocimientos FRONTEND"),
                () -> assertTrue(conocimientosFrontend.stream().allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.FRONTEND),
                        "Todos deben ser de tipo FRONTEND"),
                () -> assertTrue(conocimientosFrontend.stream().anyMatch(c -> c.getNombre().equals("React")),
                        "Debe contener React"),
                () -> assertTrue(conocimientosFrontend.stream().anyMatch(c -> c.getNombre().equals("Angular")),
                        "Debe contener Angular")
        );
    }

    @Test
    void actualizarConocimientoPorId_conDatosValidos_debeActualizarCorrectamente() {
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
        ConocimientoDto actualizado = conocimientoService.actualizarConocimientoPorId(
                guardado.getId(), conocimientoDtoActualizado);

        // ASSERT - validaciones: debe actualizar correctamente
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("Java Advanced", actualizado.getNombre(), "Nombre debe estar actualizado"),
                () -> assertEquals(Nivel.ALTO, actualizado.getNivel(), "Nivel debe estar actualizado"),
                () -> assertEquals(TipoConocimiento.BACKEND, actualizado.getTipoConocimiento(),
                        "Tipo de conocimiento debe mantenerse")
        );
    }

    @Test
    void actualizarConocimientoPorId_conImagenNueva_debeActualizarImagen() {
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
        ConocimientoDto actualizado = conocimientoService.actualizarConocimientoPorId(
                guardado.getId(), conocimientoDtoActualizado);

        // ASSERT - validaciones: verificar que la imagen fue actualizada
        assertAll(
                () -> assertNotNull(actualizado.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/react_nuevo.png",
                        actualizado.getImagen().getUrl(), "URL de imagen debe ser la nueva"),
                () -> assertEquals("Logo React Nuevo",
                        actualizado.getImagen().getAlt(), "Alt de imagen debe ser el nuevo")
        );
    }

    @Test
    void actualizarConocimientoPorId_eliminarImagen_debeMantenerConocimientoSinImagen() {
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
        ConocimientoDto actualizado = conocimientoService.actualizarConocimientoPorId(
                guardado.getId(), conocimientoDtoActualizado);

        // ASSERT - validaciones: verificar que la imagen fue eliminada
        assertNull(actualizado.getImagen(), "Imagen debe ser null");
    }

    @Test
    void actualizarConocimientoPorId_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;
        ConocimientoDto conocimientoDto = crearConocimientoDto(
                "Java Advanced",
                Nivel.ALTO,
                TipoConocimiento.BACKEND
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(RuntimeException.class, () -> {
            conocimientoService.actualizarConocimientoPorId(idInexistente, conocimientoDto);
        }, "Debe lanzar RuntimeException para ID inexistente");
    }

    @Test
    void actualizarConocimientoPorId_conNombreNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: crear y guardar conocimiento
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        Conocimiento guardado = conocimientoRepository.save(conocimiento);

        // Preparar datos con nombre nulo
        ConocimientoDto conocimientoDtoActualizado = ConocimientoDto.builder()
                .nombre(null)
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoService.actualizarConocimientoPorId(guardado.getId(), conocimientoDtoActualizado);
        }, "Debe lanzar excepción cuando el nombre es nulo");
    }

    @Test
    void deleteConocimientoById_conIdExistente_debeEliminarConocimiento() {
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
        conocimientoService.deleteConocimientoById(guardado.getId());

        // ASSERT - validaciones: conocimiento debe ser eliminado
        assertFalse(conocimientoRepository.existsById(guardado.getId()),
                "Conocimiento no debe existir después de eliminar");
        assertEquals(0, conocimientoRepository.count(), "No debe haber conocimientos en la BD");
    }

    @Test
    void deleteConocimientoById_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(EmptyResultDataAccessException.class, () -> {
            conocimientoService.deleteConocimientoById(idInexistente);
        }, "Debe lanzar EmptyResultDataAccessException para ID inexistente");
    }

    @Test
    void saveConocimiento_conTodosLosTipos_debeGuardarCorrectamente() {
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

        // ACT - acción: guardar todos los conocimientos
        ConocimientoDto resultadoFrontend = conocimientoService.saveConocimiento(conocimientoFrontend);
        ConocimientoDto resultadoBackend = conocimientoService.saveConocimiento(conocimientoBackend);
        ConocimientoDto resultadoBD = conocimientoService.saveConocimiento(conocimientoBD);
        ConocimientoDto resultadoTesting = conocimientoService.saveConocimiento(conocimientoTesting);
        ConocimientoDto resultadoIA = conocimientoService.saveConocimiento(conocimientoIA);
        ConocimientoDto resultadoPrototipo = conocimientoService.saveConocimiento(conocimientoPrototipo);
        ConocimientoDto resultadoOtros = conocimientoService.saveConocimiento(conocimientoOtros);

        // ASSERT - validaciones: todos deben guardarse correctamente
        assertAll(
                () -> assertNotNull(resultadoFrontend.getId(), "Frontend debe tener ID"),
                () -> assertNotNull(resultadoBackend.getId(), "Backend debe tener ID"),
                () -> assertNotNull(resultadoBD.getId(), "Base de datos debe tener ID"),
                () -> assertNotNull(resultadoTesting.getId(), "Testing debe tener ID"),
                () -> assertNotNull(resultadoIA.getId(), "IA debe tener ID"),
                () -> assertNotNull(resultadoPrototipo.getId(), "Prototipo debe tener ID"),
                () -> assertNotNull(resultadoOtros.getId(), "Otros debe tener ID"),
                () -> assertEquals(7, conocimientoRepository.count(), "Debe haber 7 conocimientos en la BD")
        );
    }

    @Test
    void saveConocimiento_conNombreConCaracteresEspeciales_debeGuardarCorrectamente() {
        // ARRANGE - contexto: nombre con caracteres especiales
        ConocimientoDto conocimientoDto = crearConocimientoDto(
                "C++",
                Nivel.INTERMEDIO,
                TipoConocimiento.BACKEND
        );

        // ACT - acción: guardar conocimiento
        ConocimientoDto resultado = conocimientoService.saveConocimiento(conocimientoDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("C++", resultado.getNombre(), "Nombre debe coincidir"),
                () -> assertTrue(conocimientoRepository.existsById(resultado.getId()),
                        "Conocimiento debe existir en la BD")
        );
    }

    @Test
    void filtrarPorTipo_conTipoIA_debeRetornarSoloIA() {
        // ARRANGE - contexto: crear conocimientos incluyendo IA
        Conocimiento conocimiento1 = Conocimiento.builder()
                .nombre("Machine Learning")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.IA)
                .build();

        Conocimiento conocimiento2 = Conocimiento.builder()
                .nombre("Deep Learning")
                .nivel(Nivel.PRINCIPIANTE_BASICO)
                .tipoConocimiento(TipoConocimiento.IA)
                .build();

        Conocimiento conocimiento3 = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .build();

        conocimientoRepository.save(conocimiento1);
        conocimientoRepository.save(conocimiento2);
        conocimientoRepository.save(conocimiento3);

        // ACT - acción: filtrar por tipo IA
        List<ConocimientoDto> conocimientosIA = conocimientoService.filtrarPorTipo(TipoConocimiento.IA);

        // ASSERT - validaciones: debe retornar solo los conocimientos IA
        assertAll(
                () -> assertNotNull(conocimientosIA, "Lista no debe ser null"),
                () -> assertEquals(2, conocimientosIA.size(), "Debe haber 2 conocimientos IA"),
                () -> assertTrue(conocimientosIA.stream().allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.IA),
                        "Todos deben ser de tipo IA"),
                () -> assertTrue(conocimientosIA.stream().anyMatch(c -> c.getNombre().equals("Machine Learning")),
                        "Debe contener Machine Learning")
        );
    }

    @Test
    void actualizarConocimientoPorId_cambiarTipo_debeActualizarTipoCorrectamente() {
        // ARRANGE - contexto: crear y guardar conocimiento
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .build();

        Conocimiento guardado = conocimientoRepository.save(conocimiento);

        // Preparar datos cambiando el tipo
        ConocimientoDto conocimientoDtoActualizado = crearConocimientoDto(
                "React",
                Nivel.ALTO,
                TipoConocimiento.PROTOTIPO // Cambiar de FRONTEND a PROTOTIPO
        );

        // ACT - acción: actualizar conocimiento cambiando el tipo
        ConocimientoDto actualizado = conocimientoService.actualizarConocimientoPorId(
                guardado.getId(), conocimientoDtoActualizado);

        // ASSERT - validaciones: verificar que el tipo fue cambiado
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("React", actualizado.getNombre(), "Nombre debe mantenerse"),
                () -> assertEquals(Nivel.ALTO, actualizado.getNivel(), "Nivel debe estar actualizado"),
                () -> assertEquals(TipoConocimiento.PROTOTIPO, actualizado.getTipoConocimiento(),
                        "Tipo debe ser actualizado a PROTOTIPO")
        );
    }

    @Test
    void saveConocimiento_sinImagen_debeGuardarConocimientoSinImagen() {
        // ARRANGE - contexto: conocimiento sin imagen
        ConocimientoDto conocimientoDto = crearConocimientoDto(
                "Docker",
                Nivel.INTERMEDIO,
                TipoConocimiento.OTROS
        );

        // ACT - acción: guardar conocimiento
        ConocimientoDto resultado = conocimientoService.saveConocimiento(conocimientoDto);

        // ASSERT - validaciones: verificar que no tiene imagen
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertNull(resultado.getImagen(), "Imagen debe ser null"),
                () -> assertTrue(conocimientoRepository.existsById(resultado.getId()),
                        "Conocimiento debe existir en la BD")
        );
    }
}