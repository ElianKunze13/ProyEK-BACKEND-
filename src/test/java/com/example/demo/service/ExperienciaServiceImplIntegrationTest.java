package com.example.demo.service;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import com.example.demo.model.Experiencia;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ExperienciaRepository;
import com.example.demo.service.Impl.ExperienciaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ExperienciaServiceImplIntegrationTest {

    @Autowired
    private ExperienciaServiceImpl experienciaService;

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
    void saveExperiencia_conDatosValidos_debeGuardarYRetornarExperiencia() {
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

        // ACT - acción: guardar experiencia
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDto);

        // ASSERT - validaciones: verificar que la experiencia fue guardada correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Portfolio Personal", resultado.getTitulo(), "Título debe coincidir"),
                () -> assertEquals(LocalDate.of(2024, 1, 15), resultado.getFechaInicioProyecto(),
                        "Fecha de inicio debe coincidir"),
                () -> assertEquals(LocalDate.of(2024, 6, 30), resultado.getFechaFinProyecto(),
                        "Fecha de fin debe coincidir"),
                () -> assertEquals("Desarrollo de portfolio personal con Angular y Spring Boot",
                        resultado.getDescripcion(), "Descripción debe coincidir"),
                () -> assertEquals("https://github.com/mi-portfolio", resultado.getLink(),
                        "Link debe coincidir"),
                () -> assertEquals(TipoExperiencia.PROYECTO_PERSONAL, resultado.getTipoExperiencia(),
                        "Tipo de experiencia debe coincidir"),
                () -> assertEquals(TecnologiaUsada.ANGULAR, resultado.getTecnologiaUsada(),
                        "Tecnología usada debe coincidir"),
                () -> assertTrue(experienciaRepository.existsById(resultado.getId()),
                        "Experiencia debe existir en la base de datos")
        );
    }

    @Test
    void saveExperiencia_conProyectoEnCurso_fechaFinNull_debeGuardarCorrectamente() {
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
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDto);

        // ASSERT - validaciones: verificar que se guardó con fecha fin null
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("Proyecto en Curso", resultado.getTitulo(), "Título debe coincidir"),
                () -> assertNull(resultado.getFechaFinProyecto(), "Fecha de fin debe ser null"),
                () -> assertTrue(experienciaRepository.existsById(resultado.getId()),
                        "Experiencia debe existir en la BD")
        );
    }

    @Test
    void saveExperiencia_conImagen_debeGuardarExperienciaConImagen() {
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
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDto);

        // ASSERT - validaciones: verificar que la imagen fue guardada
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertNotNull(resultado.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/app-tareas.jpg",
                        resultado.getImagen().getUrl(), "URL de la imagen debe coincidir"),
                () -> assertEquals("Captura de la aplicación de tareas",
                        resultado.getImagen().getAlt(), "Alt de la imagen debe coincidir")
        );
    }

    @Test
    void saveExperiencia_conTituloNulo_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando el título es nulo");
    }

    @Test
    void saveExperiencia_conTituloVacio_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando el título está vacío");
    }

    @Test
    void saveExperiencia_conFechaInicioNula_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando la fecha de inicio es nula");
    }

    @Test
    void saveExperiencia_conDescripcionNula_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando la descripción es nula");
    }

    @Test
    void saveExperiencia_conDescripcionVacia_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando la descripción está vacía");
    }

    @Test
    void saveExperiencia_conDescripcionMuyCorta_debeLanzarExcepcion() {
        // ARRANGE - contexto: experiencia con descripción muy corta
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto Test",
                LocalDate.now(),
                LocalDate.now(),
                "Desc", // Menos de 5 caracteres
                "https://github.com/test",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando la descripción es muy corta");
    }

    @Test
    void saveExperiencia_conLinkNulo_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando el link es nulo");
    }

    @Test
    void saveExperiencia_conLinkVacio_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando el link está vacío");
    }

    @Test
    void saveExperiencia_conTipoExperienciaNulo_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando el tipo de experiencia es nulo");
    }

    @Test
    void saveExperiencia_conTecnologiaNula_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando la tecnología usada es nula");
    }

    @Test
    void saveExperiencia_conTituloLargo_debeLanzarExcepcion() {
        // ARRANGE - contexto: experiencia con título muy largo
        String tituloLargo = "a".repeat(150);
        ExperienciaDto experienciaDto = crearExperienciaDto(
                tituloLargo,
                LocalDate.now(),
                LocalDate.now(),
                "Descripción válida",
                "https://github.com/test",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando el título es demasiado largo");
    }

    @Test
    void saveExperiencia_conDescripcionLarga_debeLanzarExcepcion() {
        // ARRANGE - contexto: experiencia con descripción muy larga
        String descripcionLarga = "a".repeat(310);
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto Test",
                LocalDate.now(),
                LocalDate.now(),
                descripcionLarga,
                "https://github.com/test",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.saveExperiencia(experienciaDto);
        }, "Debe lanzar excepción cuando la descripción es demasiado larga");
    }

    @Test
    void getAllExperiencias_debeRetornarTodasLasExperiencias() {
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
        List<ExperienciaDto> experiencias = experienciaService.getAllExperiencias();

        // ASSERT - validaciones: verificar que retorna todas las experiencias
        assertAll(
                () -> assertNotNull(experiencias, "Lista no debe ser null"),
                () -> assertFalse(experiencias.isEmpty(), "Lista no debe estar vacía"),
                () -> assertTrue(experiencias.size() >= 2, "Debe contener al menos 2 experiencias"),
                () -> assertTrue(experiencias.stream().anyMatch(e -> e.getTitulo().equals("Portfolio Personal")),
                        "Debe contener Portfolio Personal"),
                () -> assertTrue(experiencias.stream().anyMatch(e -> e.getTitulo().equals("App de Tareas")),
                        "Debe contener App de Tareas"),
                () -> assertNotNull(experiencias.get(0).getId(), "ID debe estar presente"),
                () -> assertNotNull(experiencias.get(0).getFechaInicioProyecto(),
                        "Fecha de inicio debe estar presente")
        );
    }

    @Test
    void getAllExperiencias_sinExperiencias_debeRetornarListaVacia() {
        // ARRANGE - contexto: no hay experiencias en la base de datos
        assertEquals(0, experienciaRepository.count(), "No debería haber experiencias inicialmente");

        // ACT - acción: obtener todas las experiencias
        List<ExperienciaDto> experiencias = experienciaService.getAllExperiencias();

        // ASSERT - validaciones: debe retornar lista vacía
        assertNotNull(experiencias, "Lista no debe ser null");
        assertTrue(experiencias.isEmpty(), "Lista debe estar vacía");
        assertEquals(0, experiencias.size(), "Tamaño debe ser 0");
    }

    @Test
    void getAllExperiencias_conImagenes_debeRetornarExperienciasConImagenes() {
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
        List<ExperienciaDto> experiencias = experienciaService.getAllExperiencias();

        // ASSERT - validaciones: verificar que la imagen está presente
        assertAll(
                () -> assertFalse(experiencias.isEmpty(), "Lista no debe estar vacía"),
                () -> assertNotNull(experiencias.get(0).getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/proyecto.jpg",
                        experiencias.get(0).getImagen().getUrl(), "URL de imagen debe coincidir")
        );
    }

    @Test
    void actualizarExperienciaPorId_conDatosValidos_debeActualizarCorrectamente() {
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
        ExperienciaDto actualizado = experienciaService.actualizarExperienciaPorId(
                guardado.getId(), experienciaDtoActualizado);

        // ASSERT - validaciones: debe actualizar correctamente
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("Proyecto Actualizado", actualizado.getTitulo(),
                        "Título debe estar actualizado"),
                () -> assertEquals(LocalDate.of(2024, 2, 1), actualizado.getFechaInicioProyecto(),
                        "Fecha de inicio debe estar actualizada"),
                () -> assertEquals(LocalDate.of(2024, 5, 31), actualizado.getFechaFinProyecto(),
                        "Fecha de fin debe estar actualizada"),
                () -> assertEquals("Descripción actualizada del proyecto", actualizado.getDescripcion(),
                        "Descripción debe estar actualizada"),
                () -> assertEquals("https://github.com/actualizado", actualizado.getLink(),
                        "Link debe estar actualizado"),
                () -> assertEquals(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO,
                        actualizado.getTipoExperiencia(), "Tipo de experiencia debe estar actualizado"),
                () -> assertEquals(TecnologiaUsada.SPRINGBOOT, actualizado.getTecnologiaUsada(),
                        "Tecnología usada debe estar actualizada")
        );
    }

    @Test
    void actualizarExperienciaPorId_convertirAProyectoEnCurso_fechaFinNull() {
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
        ExperienciaDto actualizado = experienciaService.actualizarExperienciaPorId(
                guardado.getId(), experienciaDtoActualizado);

        // ASSERT - validaciones: verificar que fecha fin es null
        assertNull(actualizado.getFechaFinProyecto(), "Fecha de fin debe ser null para proyecto en curso");
    }

    @Test
    void actualizarExperienciaPorId_cambiarTipoYTecnologia_debeActualizarCorrectamente() {
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
        ExperienciaDto actualizado = experienciaService.actualizarExperienciaPorId(
                guardado.getId(), experienciaDtoActualizado);

        // ASSERT - validaciones: verificar que tipo y tecnología fueron cambiados
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE,
                        actualizado.getTipoExperiencia(), "Tipo debe ser actualizado"),
                () -> assertEquals(TecnologiaUsada.PYTHON, actualizado.getTecnologiaUsada(),
                        "Tecnología debe ser actualizada")
        );
    }

    @Test
    void actualizarExperienciaPorId_conIdInexistente_debeRetornarNull() {
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

        // ACT - acción: actualizar experiencia con ID inexistente
        ExperienciaDto actualizado = experienciaService.actualizarExperienciaPorId(
                idInexistente, experienciaDto);

        // ASSERT - validaciones: debe retornar null
        assertNull(actualizado, "Debe retornar null para ID inexistente");
    }

    @Test
    void actualizarExperienciaPorId_conTituloNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: crear y guardar experiencia
        Experiencia experiencia = Experiencia.builder()
                .titulo("Proyecto Test")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 31))
                .descripcion("Descripción válida")
                .link("https://github.com/test")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .build();

        Experiencia guardado = experienciaRepository.save(experiencia);

        // Preparar datos con título nulo
        ExperienciaDto experienciaDtoActualizado = ExperienciaDto.builder()
                .titulo(null)
                .fechaInicioProyecto(LocalDate.now())
                .fechaFinProyecto(LocalDate.now())
                .descripcion("Descripción actualizada")
                .link("https://github.com/actualizado")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaService.actualizarExperienciaPorId(guardado.getId(), experienciaDtoActualizado);
        }, "Debe lanzar excepción cuando el título es nulo");
    }

    @Test
    void deleteExperienciaPorId_conIdExistente_debeEliminarExperiencia() {
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
        experienciaService.deleteExperienciaPorId(guardado.getId());

        // ASSERT - validaciones: experiencia debe ser eliminada
        assertFalse(experienciaRepository.existsById(guardado.getId()),
                "Experiencia no debe existir después de eliminar");
        assertEquals(0, experienciaRepository.count(), "No debe haber experiencias en la BD");
    }

    @Test
    void deleteExperienciaPorId_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(EmptyResultDataAccessException.class, () -> {
            experienciaService.deleteExperienciaPorId(idInexistente);
        }, "Debe lanzar EmptyResultDataAccessException para ID inexistente");
    }

    @Test
    void saveExperiencia_conTodasLasTecnologias_debeGuardarCorrectamente() {
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
            experienciaService.saveExperiencia(experienciaDto);
        }

        // ASSERT - validaciones: verificar que todas fueron guardadas
        assertEquals(tecnologias.length, experienciaRepository.count(),
                "Debe haber " + tecnologias.length + " experiencias en la BD");
    }

    @Test
    void saveExperiencia_conTodosLosTipos_debeGuardarCorrectamente() {
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

        // ACT - acción: guardar todas las experiencias
        ExperienciaDto resultadoPersonal = experienciaService.saveExperiencia(experienciaPersonal);
        ExperienciaDto resultadoLaboral = experienciaService.saveExperiencia(experienciaLaboral);
        ExperienciaDto resultadoOpenSource = experienciaService.saveExperiencia(experienciaOpenSource);
        ExperienciaDto resultadoPractica = experienciaService.saveExperiencia(experienciaPractica);
        ExperienciaDto resultadoFreelance = experienciaService.saveExperiencia(experienciaFreelance);

        // ASSERT - validaciones: todas deben guardarse correctamente
        assertAll(
                () -> assertNotNull(resultadoPersonal.getId(), "Personal debe tener ID"),
                () -> assertNotNull(resultadoLaboral.getId(), "Laboral debe tener ID"),
                () -> assertNotNull(resultadoOpenSource.getId(), "Open Source debe tener ID"),
                () -> assertNotNull(resultadoPractica.getId(), "Práctica debe tener ID"),
                () -> assertNotNull(resultadoFreelance.getId(), "Freelance debe tener ID"),
                () -> assertEquals(5, experienciaRepository.count(), "Debe haber 5 experiencias en la BD")
        );
    }

    @Test
    void saveExperiencia_conTituloExactamenteDeLongitudMaxima_debeGuardarCorrectamente() {
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
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals(titulo, resultado.getTitulo(), "Título debe coincidir"),
                () -> assertTrue(experienciaRepository.existsById(resultado.getId()),
                        "Experiencia debe existir en la BD")
        );
    }

    @Test
    void saveExperiencia_conTituloConCaracteresEspeciales_debeGuardarCorrectamente() {
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
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("Proyecto: Desarrollo Web (Full-Stack)", resultado.getTitulo(),
                        "Título con caracteres especiales debe coincidir"),
                () -> assertTrue(experienciaRepository.existsById(resultado.getId()),
                        "Experiencia debe existir en la BD")
        );
    }

    @Test
    void getAllExperiencias_conMultiplesExperiencias_debeRetornarEnOrdenCorrecto() {
        // ARRANGE - contexto: crear y guardar múltiples experiencias
        for (int i = 1; i <= 5; i++) {
            Experiencia experiencia = Experiencia.builder()
                    .titulo("Experiencia " + i)
                    .fechaInicioProyecto(LocalDate.of(2024, i, 1))
                    .fechaFinProyecto(LocalDate.of(2024, i + 2, 28))
                    .descripcion("Descripción de experiencia " + i)
                    .link("https://github.com/experiencia" + i)
                    .tipoExperiencia(i % 2 == 0 ? TipoExperiencia.PROYECTO_PERSONAL : TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                    .tecnologiaUsada(i % 2 == 0 ? TecnologiaUsada.ANGULAR : TecnologiaUsada.REACT)
                    .build();
            experienciaRepository.save(experiencia);
        }

        // ACT - acción: obtener todas las experiencias
        List<ExperienciaDto> experiencias = experienciaService.getAllExperiencias();

        // ASSERT - validaciones: debe retornar todas las experiencias
        assertAll(
                () -> assertNotNull(experiencias, "Lista no debe ser null"),
                () -> assertEquals(5, experiencias.size(), "Debe haber exactamente 5 experiencias"),
                () -> assertTrue(experiencias.stream().allMatch(e -> e.getId() != null),
                        "Todas las experiencias deben tener ID"),
                () -> assertTrue(experiencias.stream().allMatch(e -> e.getFechaInicioProyecto() != null),
                        "Todas las experiencias deben tener fecha de inicio"),
                () -> assertTrue(experiencias.stream().anyMatch(e -> e.getTitulo().equals("Experiencia 3")),
                        "Debe contener Experiencia 3"),
                () -> assertTrue(experiencias.stream().anyMatch(e -> e.getTipoExperiencia() == TipoExperiencia.PROYECTO_PERSONAL),
                        "Debe contener experiencias de tipo PROYECTO_PERSONAL"),
                () -> assertTrue(experiencias.stream().anyMatch(e -> e.getTipoExperiencia() == TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO),
                        "Debe contener experiencias de tipo TRABAJO_LABORAL_COLABORATIVO"),
                () -> assertTrue(experiencias.stream().anyMatch(e -> e.getTecnologiaUsada() == TecnologiaUsada.ANGULAR),
                        "Debe contener experiencias con tecnología Angular"),
                () -> assertTrue(experiencias.stream().anyMatch(e -> e.getTecnologiaUsada() == TecnologiaUsada.REACT),
                        "Debe contener experiencias con tecnología React")
        );
    }

    @Test
    void saveExperiencia_sinImagen_debeGuardarExperienciaSinImagen() {
        // ARRANGE - contexto: experiencia sin imagen
        ExperienciaDto experienciaDto = crearExperienciaDto(
                "Proyecto sin Imagen",
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                "Descripción del proyecto sin imagen",
                "https://github.com/sin-imagen",
                TipoExperiencia.PROYECTO_PERSONAL,
                TecnologiaUsada.JAVA
        );

        // ACT - acción: guardar experiencia
        ExperienciaDto resultado = experienciaService.saveExperiencia(experienciaDto);

        // ASSERT - validaciones: verificar que no tiene imagen
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertNull(resultado.getImagen(), "Imagen debe ser null"),
                () -> assertTrue(experienciaRepository.existsById(resultado.getId()),
                        "Experiencia debe existir en la BD")
        );
    }
}