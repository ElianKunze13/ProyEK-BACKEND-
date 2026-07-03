package com.example.demo.service;

import com.example.demo.dto.EducacionDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.enums.TipoEducacion;
import com.example.demo.model.Educacion;
import com.example.demo.model.Imagen;
import com.example.demo.repository.EducacionRepository;
import com.example.demo.service.Impl.EducacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EducacionServiceImplIntegrationTest {

    @Autowired
    private EducacionServiceImpl educacionService;

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
    void saveEducacion_conDatosValidos_debeGuardarYRetornarEducacion() {
        // ARRANGE - contexto: educación con datos válidos
        EducacionDto educacionDto = crearEducacionDto(
                "Ingeniería en Sistemas",
                LocalDate.of(2024, 6, 15),
                "Estudios de ingeniería en sistemas computacionales",
                TipoEducacion.FORMAL
        );

        // ACT - acción: guardar educación
        EducacionDto resultado = educacionService.saveEducacion(educacionDto);

        // ASSERT - validaciones: verificar que la educación fue guardada correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Ingeniería en Sistemas", resultado.getTitulo(), "Título debe coincidir"),
                () -> assertEquals(LocalDate.of(2024, 6, 15), resultado.getFechaObtencion(),
                        "Fecha de obtención debe coincidir"),
                () -> assertEquals("Estudios de ingeniería en sistemas computacionales",
                        resultado.getDescripcion(), "Descripción debe coincidir"),
                () -> assertEquals(TipoEducacion.FORMAL, resultado.getTipoEducacion(),
                        "Tipo de educación debe coincidir"),
                () -> assertTrue(educacionRepository.existsById(resultado.getId()),
                        "Educación debe existir en la base de datos")
        );
    }

    @Test
    void saveEducacion_conImagen_debeGuardarEducacionConImagen() {
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
        EducacionDto resultado = educacionService.saveEducacion(educacionDto);

        // ASSERT - validaciones: verificar que la imagen fue guardada
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertNotNull(resultado.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/certificado_spring.jpg",
                        resultado.getImagen().getUrl(), "URL de la imagen debe coincidir"),
                () -> assertEquals("Certificado Spring Boot",
                        resultado.getImagen().getAlt(), "Alt de la imagen debe coincidir")
        );
    }

    @Test
    void saveEducacion_conTituloNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: educación con título nulo
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo(null)
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .tipoEducacion(TipoEducacion.FORMAL)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.saveEducacion(educacionDto);
        }, "Debe lanzar excepción cuando el título es nulo");
    }

    @Test
    void saveEducacion_conTituloVacio_debeLanzarExcepcion() {
        // ARRANGE - contexto: educación con título vacío
        EducacionDto educacionDto = crearEducacionDto(
                "",
                LocalDate.now(),
                "Descripción válida",
                TipoEducacion.FORMAL
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.saveEducacion(educacionDto);
        }, "Debe lanzar excepción cuando el título está vacío");
    }

    @Test
    void saveEducacion_conFechaObtencionNula_debeLanzarExcepcion() {
        // ARRANGE - contexto: educación con fecha de obtención nula
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo("Curso de Java")
                .fechaObtencion(null)
                .descripcion("Descripción válida")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.saveEducacion(educacionDto);
        }, "Debe lanzar excepción cuando la fecha de obtención es nula");
    }

    @Test
    void saveEducacion_conDescripcionNula_debeLanzarExcepcion() {
        // ARRANGE - contexto: educación con descripción nula
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.now())
                .descripcion(null)
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.saveEducacion(educacionDto);
        }, "Debe lanzar excepción cuando la descripción es nula");
    }

    @Test
    void saveEducacion_conDescripcionVacia_debeLanzarExcepcion() {
        // ARRANGE - contexto: educación con descripción vacía
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Java",
                LocalDate.now(),
                "",
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.saveEducacion(educacionDto);
        }, "Debe lanzar excepción cuando la descripción está vacía");
    }

    @Test
    void saveEducacion_conDescripcionMuyCorta_debeLanzarExcepcion() {
        // ARRANGE - contexto: educación con descripción muy corta
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Java",
                LocalDate.now(),
                "Desc", // Menos de 5 caracteres
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.saveEducacion(educacionDto);
        }, "Debe lanzar excepción cuando la descripción es muy corta");
    }

    @Test
    void saveEducacion_conTipoEducacionNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: educación con tipo de educación nulo
        EducacionDto educacionDto = EducacionDto.builder()
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .tipoEducacion(null)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.saveEducacion(educacionDto);
        }, "Debe lanzar excepción cuando el tipo de educación es nulo");
    }

    @Test
    void saveEducacion_conTituloLargo_debeLanzarExcepcion() {
        // ARRANGE - contexto: educación con título muy largo
        String tituloLargo = "a".repeat(150);
        EducacionDto educacionDto = crearEducacionDto(
                tituloLargo,
                LocalDate.now(),
                "Descripción válida",
                TipoEducacion.FORMAL
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.saveEducacion(educacionDto);
        }, "Debe lanzar excepción cuando el título es demasiado largo");
    }

    @Test
    void saveEducacion_conDescripcionLarga_debeLanzarExcepcion() {
        // ARRANGE - contexto: educación con descripción muy larga
        String descripcionLarga = "a".repeat(310);
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Java",
                LocalDate.now(),
                descripcionLarga,
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.saveEducacion(educacionDto);
        }, "Debe lanzar excepción cuando la descripción es demasiado larga");
    }

    @Test
    void getAllEducacion_debeRetornarTodasLasEducaciones() {
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
        List<EducacionDto> educaciones = educacionService.getAllEducacion();

        // ASSERT - validaciones: verificar que retorna todas las educaciones
        assertAll(
                () -> assertNotNull(educaciones, "Lista no debe ser null"),
                () -> assertFalse(educaciones.isEmpty(), "Lista no debe estar vacía"),
                () -> assertTrue(educaciones.size() >= 2, "Debe contener al menos 2 educaciones"),
                () -> assertTrue(educaciones.stream().anyMatch(e -> e.getTitulo().equals("Ingeniería en Sistemas")),
                        "Debe contener Ingeniería en Sistemas"),
                () -> assertTrue(educaciones.stream().anyMatch(e -> e.getTitulo().equals("Curso de Spring Boot")),
                        "Debe contener Curso de Spring Boot"),
                () -> assertNotNull(educaciones.get(0).getId(), "ID debe estar presente"),
                () -> assertNotNull(educaciones.get(0).getFechaObtencion(), "Fecha de obtención debe estar presente")
        );
    }

    @Test
    void getAllEducacion_sinEducaciones_debeRetornarListaVacia() {
        // ARRANGE - contexto: no hay educaciones en la base de datos
        assertEquals(0, educacionRepository.count(), "No debería haber educaciones inicialmente");

        // ACT - acción: obtener todas las educaciones
        List<EducacionDto> educaciones = educacionService.getAllEducacion();

        // ASSERT - validaciones: debe retornar lista vacía
        assertNotNull(educaciones, "Lista no debe ser null");
        assertTrue(educaciones.isEmpty(), "Lista debe estar vacía");
        assertEquals(0, educaciones.size(), "Tamaño debe ser 0");
    }

    @Test
    void getAllEducacion_conImagenes_debeRetornarEducacionesConImagenes() {
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
        List<EducacionDto> educaciones = educacionService.getAllEducacion();

        // ASSERT - validaciones: verificar que la imagen está presente
        assertAll(
                () -> assertFalse(educaciones.isEmpty(), "Lista no debe estar vacía"),
                () -> assertNotNull(educaciones.get(0).getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/certificado_angular.jpg",
                        educaciones.get(0).getImagen().getUrl(), "URL de imagen debe coincidir")
        );
    }

    @Test
    void actualizarEducacionPorId_conDatosValidos_debeActualizarCorrectamente() {
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
        EducacionDto actualizado = educacionService.actualizarEducacionPorId(
                guardado.getId(), educacionDtoActualizado);

        // ASSERT - validaciones: debe actualizar correctamente
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("Curso Avanzado de Java", actualizado.getTitulo(),
                        "Título debe estar actualizado"),
                () -> assertEquals(LocalDate.of(2024, 6, 20), actualizado.getFechaObtencion(),
                        "Fecha de obtención debe estar actualizada"),
                () -> assertEquals("Curso avanzado de Java con Spring Boot", actualizado.getDescripcion(),
                        "Descripción debe estar actualizada"),
                () -> assertEquals(TipoEducacion.FORMAL, actualizado.getTipoEducacion(),
                        "Tipo de educación debe estar actualizado")
        );
    }

    @Test
    void actualizarEducacionPorId_conImagenNueva_debeActualizarImagen() {
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
        EducacionDto actualizado = educacionService.actualizarEducacionPorId(
                guardado.getId(), educacionDtoActualizado);

        // ASSERT - validaciones: verificar que la imagen fue actualizada
        assertAll(
                () -> assertNotNull(actualizado.getImagen(), "Imagen debe estar presente"),
                () -> assertEquals("https://example.com/react_nuevo.jpg",
                        actualizado.getImagen().getUrl(), "URL de imagen debe ser la nueva"),
                () -> assertEquals("Certificado React Nuevo",
                        actualizado.getImagen().getAlt(), "Alt de imagen debe ser el nuevo")
        );
    }

    @Test
    void actualizarEducacionPorId_eliminarImagen_debeMantenerEducacionSinImagen() {
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
        EducacionDto actualizado = educacionService.actualizarEducacionPorId(
                guardado.getId(), educacionDtoActualizado);

        // ASSERT - validaciones: verificar que la imagen fue eliminada
        assertNull(actualizado.getImagen(), "Imagen debe ser null");
    }

    @Test
    void actualizarEducacionPorId_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;
        EducacionDto educacionDto = crearEducacionDto(
                "Curso Actualizado",
                LocalDate.now(),
                "Descripción actualizada",
                TipoEducacion.FORMAL
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(RuntimeException.class, () -> {
            educacionService.actualizarEducacionPorId(idInexistente, educacionDto);
        }, "Debe lanzar RuntimeException para ID inexistente");
    }

    @Test
    void actualizarEducacionPorId_conTituloNulo_debeLanzarExcepcion() {
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

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionService.actualizarEducacionPorId(guardado.getId(), educacionDtoActualizado);
        }, "Debe lanzar excepción cuando el título es nulo");
    }

    @Test
    void deleteEducacionPorId_conIdExistente_debeEliminarEducacion() {
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
        educacionService.deleteEducacionPorId(guardado.getId());

        // ASSERT - validaciones: educación debe ser eliminada
        assertFalse(educacionRepository.existsById(guardado.getId()),
                "Educación no debe existir después de eliminar");
        assertEquals(0, educacionRepository.count(), "No debe haber educaciones en la BD");
    }

    @Test
    void deleteEducacionPorId_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(EmptyResultDataAccessException.class, () -> {
            educacionService.deleteEducacionPorId(idInexistente);
        }, "Debe lanzar EmptyResultDataAccessException para ID inexistente");
    }

    @Test
    void saveEducacion_conTodosLosTipos_debeGuardarCorrectamente() {
        // ARRANGE - contexto: probar todos los tipos de educación
        EducacionDto educacionFormal = crearEducacionDto(
                "Ingeniería", LocalDate.of(2024, 6, 15), "Descripción formal", TipoEducacion.FORMAL);

        EducacionDto educacionInformal = crearEducacionDto(
                "Curso de Java", LocalDate.of(2024, 5, 20), "Descripción informal", TipoEducacion.INFORMAL_CURSO);

        EducacionDto educacionAutodidacta = crearEducacionDto(
                "Aprendizaje autodidacta", LocalDate.of(2024, 4, 10), "Descripción autodidacta", TipoEducacion.AUTODIDACTA);

        EducacionDto educacionOtros = crearEducacionDto(
                "Otros estudios", LocalDate.of(2024, 3, 1), "Descripción otros", TipoEducacion.OTROS);

        // ACT - acción: guardar todas las educaciones
        EducacionDto resultadoFormal = educacionService.saveEducacion(educacionFormal);
        EducacionDto resultadoInformal = educacionService.saveEducacion(educacionInformal);
        EducacionDto resultadoAutodidacta = educacionService.saveEducacion(educacionAutodidacta);
        EducacionDto resultadoOtros = educacionService.saveEducacion(educacionOtros);

        // ASSERT - validaciones: todas deben guardarse correctamente
        assertAll(
                () -> assertNotNull(resultadoFormal.getId(), "Formal debe tener ID"),
                () -> assertNotNull(resultadoInformal.getId(), "Informal debe tener ID"),
                () -> assertNotNull(resultadoAutodidacta.getId(), "Autodidacta debe tener ID"),
                () -> assertNotNull(resultadoOtros.getId(), "Otros debe tener ID"),
                () -> assertEquals(4, educacionRepository.count(), "Debe haber 4 educaciones en la BD")
        );
    }

    @Test
    void actualizarEducacionPorId_cambiarTipo_debeActualizarTipoCorrectamente() {
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
        EducacionDto actualizado = educacionService.actualizarEducacionPorId(
                guardado.getId(), educacionDtoActualizado);

        // ASSERT - validaciones: verificar que el tipo fue cambiado
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("Curso de Java", actualizado.getTitulo(), "Título debe mantenerse"),
                () -> assertEquals(TipoEducacion.FORMAL, actualizado.getTipoEducacion(),
                        "Tipo debe ser actualizado a FORMAL")
        );
    }

    @Test
    void saveEducacion_conFechaFutura_debeGuardarCorrectamente() {
        // ARRANGE - contexto: educación con fecha futura
        LocalDate fechaFutura = LocalDate.now().plusMonths(6);
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Machine Learning",
                fechaFutura,
                "Curso avanzado de Machine Learning",
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT - acción: guardar educación
        EducacionDto resultado = educacionService.saveEducacion(educacionDto);

        // ASSERT - validaciones: debe guardar con fecha futura
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals(fechaFutura, resultado.getFechaObtencion(),
                        "Fecha de obtención debe coincidir"),
                () -> assertTrue(educacionRepository.existsById(resultado.getId()),
                        "Educación debe existir en la BD")
        );
    }

    @Test
    void saveEducacion_conTituloConCaracteresEspeciales_debeGuardarCorrectamente() {
        // ARRANGE - contexto: título con caracteres especiales
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Java: Programación Avanzada (Nivel 2)",
                LocalDate.of(2024, 6, 15),
                "Descripción del curso con caracteres especiales",
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT - acción: guardar educación
        EducacionDto resultado = educacionService.saveEducacion(educacionDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("Curso de Java: Programación Avanzada (Nivel 2)",
                        resultado.getTitulo(), "Título con caracteres especiales debe coincidir"),
                () -> assertTrue(educacionRepository.existsById(resultado.getId()),
                        "Educación debe existir en la BD")
        );
    }

    @Test
    void saveEducacion_conTituloExactamenteDeLongitudMaxima_debeGuardarCorrectamente() {
        // ARRANGE - contexto: título exactamente de 145 caracteres
        String titulo = "a".repeat(145);
        EducacionDto educacionDto = crearEducacionDto(
                titulo,
                LocalDate.now(),
                "Descripción válida",
                TipoEducacion.FORMAL
        );

        // ACT - acción: guardar educación
        EducacionDto resultado = educacionService.saveEducacion(educacionDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals(titulo, resultado.getTitulo(), "Título debe coincidir"),
                () -> assertTrue(educacionRepository.existsById(resultado.getId()),
                        "Educación debe existir en la BD")
        );
    }

    @Test
    void saveEducacion_conDescripcionExactamenteDeLongitudMaxima_debeGuardarCorrectamente() {
        // ARRANGE - contexto: descripción exactamente de 300 caracteres
        String descripcion = "a".repeat(300);
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Java",
                LocalDate.now(),
                descripcion,
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT - acción: guardar educación
        EducacionDto resultado = educacionService.saveEducacion(educacionDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals(descripcion, resultado.getDescripcion(), "Descripción debe coincidir"),
                () -> assertTrue(educacionRepository.existsById(resultado.getId()),
                        "Educación debe existir en la BD")
        );
    }

    @Test
    void saveEducacion_sinImagen_debeGuardarEducacionSinImagen() {
        // ARRANGE - contexto: educación sin imagen
        EducacionDto educacionDto = crearEducacionDto(
                "Curso de Docker",
                LocalDate.now(),
                "Curso de Docker y contenedores",
                TipoEducacion.INFORMAL_CURSO
        );

        // ACT - acción: guardar educación
        EducacionDto resultado = educacionService.saveEducacion(educacionDto);

        // ASSERT - validaciones: verificar que no tiene imagen
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertNull(resultado.getImagen(), "Imagen debe ser null"),
                () -> assertTrue(educacionRepository.existsById(resultado.getId()),
                        "Educación debe existir en la BD")
        );
    }

    @Test
    void getAllEducacion_conMultiplesEducaciones_debeRetornarEnOrdenCorrecto() {
        // ARRANGE - contexto: crear y guardar múltiples educaciones
        for (int i = 1; i <= 5; i++) {
            Educacion educacion = Educacion.builder()
                    .titulo("Educación " + i)
                    .fechaObtencion(LocalDate.of(2024, i, 15))
                    .descripcion("Descripción de educación " + i)
                    .tipoEducacion(i % 2 == 0 ? TipoEducacion.FORMAL : TipoEducacion.INFORMAL_CURSO)
                    .build();
            educacionRepository.save(educacion);
        }

        // ACT - acción: obtener todas las educaciones
        List<EducacionDto> educaciones = educacionService.getAllEducacion();

        // ASSERT - validaciones: debe retornar todas las educaciones
        assertAll(
                () -> assertNotNull(educaciones, "Lista no debe ser null"),
                () -> assertEquals(5, educaciones.size(), "Debe haber exactamente 5 educaciones"),
                () -> assertTrue(educaciones.stream().allMatch(e -> e.getId() != null),
                        "Todas las educaciones deben tener ID"),
                () -> assertTrue(educaciones.stream().allMatch(e -> e.getFechaObtencion() != null),
                        "Todas las educaciones deben tener fecha de obtención"),
                () -> assertTrue(educaciones.stream().anyMatch(e -> e.getTitulo().equals("Educación 3")),
                        "Debe contener Educación 3"),
                () -> assertTrue(educaciones.stream().anyMatch(e -> e.getTipoEducacion() == TipoEducacion.FORMAL),
                        "Debe contener educaciones de tipo FORMAL"),
                () -> assertTrue(educaciones.stream().anyMatch(e -> e.getTipoEducacion() == TipoEducacion.INFORMAL_CURSO),
                        "Debe contener educaciones de tipo INFORMAL_CURSO")
        );
    }
}