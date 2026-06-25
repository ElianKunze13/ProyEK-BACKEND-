package com.example.demo.repository;

import com.example.demo.enums.TipoEducacion;
import com.example.demo.model.Educacion;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase EducacionRepository
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Usa @DataJpaTest para pruebas de repositorio con JPA
 * Usa TestEntityManager para gestionar las entidades en el contexto de prueba
 */
@DataJpaTest
@ActiveProfiles("test")
class EducacionRepositoryTest {

    @Autowired
    private EducacionRepository educacionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Educacion educacionValida;
    private Educacion educacionConImagen;
    private Imagen imagen;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear usuario
        usuario = Usuario.builder()
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack con 5 años de experiencia")
                .descripcion("Apasionado por la tecnología y el desarrollo de software.")
                .rol(com.example.demo.enums.Role.USER)
                .active(true)
                .build();

        // Crear imagen
        imagen = Imagen.builder()
                .url("titulo.jpg")
                .alt("Imagen del título")
                .build();

        // Crear educación válida sin imagen
        educacionValida = Educacion.builder()
                .titulo("Ingeniería Informática")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Grado en Ingeniería Informática con especialización en software")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(usuario)
                .build();

        // Crear educación con imagen
        educacionConImagen = Educacion.builder()
                .titulo("Máster en Inteligencia Artificial")
                .fechaObtencion(LocalDate.of(2022, 9, 30))
                .descripcion("Máster oficial en IA y Machine Learning")
                .tipoEducacion(TipoEducacion.FORMAL)
                .imagen(imagen)
                .usuario(usuario)
                .build();
    }

    // ==================== TESTS DE GUARDADO ====================

    @Test
    @DisplayName("save - Debería guardar una educación correctamente")
    void save_ShouldSaveEducacionCorrectly() {
        // Act
        Educacion educacionGuardada = educacionRepository.save(educacionValida);

        // Assert
        assertNotNull(educacionGuardada, "La educación guardada no debería ser nula");
        assertNotNull(educacionGuardada.getId(), "El ID de la educación guardada no debería ser nulo");
        assertEquals("Ingeniería Informática", educacionGuardada.getTitulo(), "El título debería coincidir");
        assertEquals(LocalDate.of(2020, 6, 15), educacionGuardada.getFechaObtencion(), "La fecha de obtención debería coincidir");
        assertEquals("Grado en Ingeniería Informática con especialización en software", educacionGuardada.getDescripcion(),
                "La descripción debería coincidir");
        assertEquals(TipoEducacion.FORMAL, educacionGuardada.getTipoEducacion(), "El tipo de educación debería coincidir");
        assertNull(educacionGuardada.getImagen(), "La imagen debería ser nula");
        assertNotNull(educacionGuardada.getUsuario(), "El usuario no debería ser nulo");

        // Verificar que se guardó en la base de datos
        Educacion educacionEncontrada = entityManager.find(Educacion.class, educacionGuardada.getId());
        assertNotNull(educacionEncontrada, "La educación debería existir en la base de datos");
        assertEquals(educacionGuardada.getTitulo(), educacionEncontrada.getTitulo(), "El título debería coincidir");
    }

    @Test
    @DisplayName("save - Debería guardar una educación con imagen correctamente")
    void save_ShouldSaveEducacionWithImageCorrectly() {
        // Act
        Educacion educacionGuardada = educacionRepository.save(educacionConImagen);

        // Assert
        assertNotNull(educacionGuardada, "La educación guardada no debería ser nula");
        assertNotNull(educacionGuardada.getId(), "El ID de la educación guardada no debería ser nulo");
        assertNotNull(educacionGuardada.getImagen(), "La imagen no debería ser nula");
        assertNotNull(educacionGuardada.getImagen().getId(), "El ID de la imagen no debería ser nulo");
        assertEquals("titulo.jpg", educacionGuardada.getImagen().getUrl(), "La URL de la imagen debería coincidir");
        assertEquals("Imagen del título", educacionGuardada.getImagen().getAlt(), "El alt de la imagen debería coincidir");

        // Verificar que se guardó en la base de datos
        Educacion educacionEncontrada = entityManager.find(Educacion.class, educacionGuardada.getId());
        assertNotNull(educacionEncontrada.getImagen(), "La imagen en la BD no debería ser nula");
        assertEquals("titulo.jpg", educacionEncontrada.getImagen().getUrl(), "La URL en la BD debería coincidir");
    }

    @Test
    @DisplayName("save - Debería guardar una educación con todos los tipos de educación")
    void save_ShouldSaveEducacionWithAllTipoEducacion() {
        // Probar cada tipo de educación
        for (TipoEducacion tipo : TipoEducacion.values()) {
            // Arrange
            Educacion educacion = Educacion.builder()
                    .titulo("Educación " + tipo.name())
                    .fechaObtencion(LocalDate.now())
                    .descripcion("Descripción para " + tipo.name())
                    .tipoEducacion(tipo)
                    .usuario(usuario)
                    .build();

            // Act
            Educacion educacionGuardada = educacionRepository.save(educacion);

            // Assert
            assertNotNull(educacionGuardada, "La educación no debería ser nula para tipo: " + tipo);
            assertNotNull(educacionGuardada.getId(), "El ID no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, educacionGuardada.getTipoEducacion(), "El tipo debería coincidir para: " + tipo);
        }
    }

    @Test
    @DisplayName("save - Debería guardar educación sin usuario")
    void save_ShouldSaveEducacionWithoutUsuario() {
        // Arrange
        Educacion educacionSinUsuario = Educacion.builder()
                .titulo("Curso Independiente")
                .fechaObtencion(LocalDate.now())
                .descripcion("Curso sin usuario asociado")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .usuario(null)
                .build();

        // Act
        Educacion educacionGuardada = educacionRepository.save(educacionSinUsuario);

        // Assert
        assertNotNull(educacionGuardada, "La educación guardada no debería ser nula");
        assertNotNull(educacionGuardada.getId(), "El ID de la educación guardada no debería ser nulo");
        assertNull(educacionGuardada.getUsuario(), "El usuario debería ser nulo");

        // Verificar en la base de datos
        Educacion educacionEncontrada = entityManager.find(Educacion.class, educacionGuardada.getId());
        assertNull(educacionEncontrada.getUsuario(), "El usuario en la BD debería ser nulo");
    }

    // ==================== TESTS DE BÚSQUEDA ====================

    @Test
    @DisplayName("findById - Debería encontrar educación por ID cuando existe")
    void findById_ShouldFindEducacion_WhenIdExists() {
        // Arrange - Guardar educación en la base de datos
        Educacion educacionGuardada = entityManager.persist(educacionValida);
        entityManager.flush();

        // Act
        Optional<Educacion> educacionEncontrada = educacionRepository.findById(educacionGuardada.getId());

        // Assert
        assertTrue(educacionEncontrada.isPresent(), "La educación debería ser encontrada");
        assertEquals(educacionGuardada.getId(), educacionEncontrada.get().getId(), "El ID debería coincidir");
        assertEquals(educacionGuardada.getTitulo(), educacionEncontrada.get().getTitulo(), "El título debería coincidir");
        assertEquals(educacionGuardada.getTipoEducacion(), educacionEncontrada.get().getTipoEducacion(),
                "El tipo de educación debería coincidir");
    }

    @Test
    @DisplayName("findById - No debería encontrar educación cuando ID no existe")
    void findById_ShouldNotFindEducacion_WhenIdDoesNotExist() {
        // Act
        Optional<Educacion> educacionEncontrada = educacionRepository.findById(999);

        // Assert
        assertFalse(educacionEncontrada.isPresent(), "La educación no debería ser encontrada");
    }

    @Test
    @DisplayName("findAll - Debería encontrar todas las educaciones")
    void findAll_ShouldFindAllEducaciones() {
        // Arrange - Guardar varias educaciones en la base de datos
        entityManager.persist(educacionValida);
        entityManager.persist(educacionConImagen);

        // Crear otra educación con tipo diferente
        Educacion educacionAutodidacta = Educacion.builder()
                .titulo("Curso de Spring Boot")
                .fechaObtencion(LocalDate.of(2023, 5, 20))
                .descripcion("Curso avanzado de Spring Boot")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .usuario(usuario)
                .build();
        entityManager.persist(educacionAutodidacta);
        entityManager.flush();

        // Act
        List<Educacion> educaciones = educacionRepository.findAll();

        // Assert
        assertNotNull(educaciones, "La lista de educaciones no debería ser nula");
        assertTrue(educaciones.size() >= 3, "Debería haber al menos 3 educaciones");

        // Verificar que las educaciones guardadas están en la lista
        boolean foundFormal = educaciones.stream()
                .anyMatch(e -> e.getTipoEducacion() == TipoEducacion.FORMAL);
        boolean foundAutodidacta = educaciones.stream()
                .anyMatch(e -> e.getTipoEducacion() == TipoEducacion.AUTODIDACTA);
        assertTrue(foundFormal, "La educación formal debería estar en la lista");
        assertTrue(foundAutodidacta, "La educación autodidacta debería estar en la lista");
    }

    @Test
    @DisplayName("findAll - Debería retornar lista vacía cuando no hay educaciones")
    void findAll_ShouldReturnEmptyList_WhenNoEducacionesExist() {
        // Act
        List<Educacion> educaciones = educacionRepository.findAll();

        // Assert
        assertNotNull(educaciones, "La lista no debería ser nula");
        assertTrue(educaciones.isEmpty(), "La lista debería estar vacía");
    }

    // ==================== TESTS DE ACTUALIZACIÓN ====================

    @Test
    @DisplayName("save - Debería actualizar una educación existente")
    void save_ShouldUpdateExistingEducacion() {
        // Arrange - Guardar educación en la base de datos
        Educacion educacionGuardada = entityManager.persist(educacionValida);
        entityManager.flush();

        // Act - Actualizar educación
        educacionGuardada.setTitulo("Ingeniería Informática Actualizado");
        educacionGuardada.setDescripcion("Descripción actualizada con más detalles");
        educacionGuardada.setTipoEducacion(TipoEducacion.INFORMAL_CURSO);
        Educacion educacionActualizada = educacionRepository.save(educacionGuardada);

        // Assert
        assertNotNull(educacionActualizada, "La educación actualizada no debería ser nula");
        assertEquals(educacionGuardada.getId(), educacionActualizada.getId(), "El ID debería ser el mismo");
        assertEquals("Ingeniería Informática Actualizado", educacionActualizada.getTitulo(),
                "El título debería estar actualizado");
        assertEquals("Descripción actualizada con más detalles", educacionActualizada.getDescripcion(),
                "La descripción debería estar actualizada");
        assertEquals(TipoEducacion.INFORMAL_CURSO, educacionActualizada.getTipoEducacion(),
                "El tipo de educación debería estar actualizado");

        // Verificar en la base de datos
        Educacion educacionEncontrada = entityManager.find(Educacion.class, educacionGuardada.getId());
        assertNotNull(educacionEncontrada, "La educación debería existir en la BD");
        assertEquals("Ingeniería Informática Actualizado", educacionEncontrada.getTitulo(),
                "El título en la BD debería estar actualizado");
        assertEquals(TipoEducacion.INFORMAL_CURSO, educacionEncontrada.getTipoEducacion(),
                "El tipo en la BD debería estar actualizado");
    }

    @Test
    @DisplayName("save - Debería actualizar la imagen de una educación")
    void save_ShouldUpdateEducacionImage() {
        // Arrange - Guardar educación sin imagen en la base de datos
        Educacion educacionGuardada = entityManager.persist(educacionValida);
        entityManager.flush();

        // Crear nueva imagen
        Imagen nuevaImagen = Imagen.builder()
                .url("nuevo-certificado.jpg")
                .alt("Nuevo certificado")
                .build();

        // Act - Actualizar imagen
        educacionGuardada.setImagen(nuevaImagen);
        Educacion educacionActualizada = educacionRepository.save(educacionGuardada);

        // Assert
        assertNotNull(educacionActualizada.getImagen(), "La imagen no debería ser nula");
        assertNotNull(educacionActualizada.getImagen().getId(), "El ID de la imagen no debería ser nulo");
        assertEquals("nuevo-certificado.jpg", educacionActualizada.getImagen().getUrl(),
                "La URL de la imagen debería estar actualizada");
        assertEquals("Nuevo certificado", educacionActualizada.getImagen().getAlt(),
                "El alt de la imagen debería estar actualizado");

        // Verificar en la base de datos
        Educacion educacionEncontrada = entityManager.find(Educacion.class, educacionGuardada.getId());
        assertNotNull(educacionEncontrada.getImagen(), "La imagen en la BD no debería ser nula");
        assertEquals("nuevo-certificado.jpg", educacionEncontrada.getImagen().getUrl(),
                "La URL en la BD debería estar actualizada");
    }

    @Test
    @DisplayName("save - Debería actualizar la fecha de obtención de una educación")
    void save_ShouldUpdateEducacionFechaObtencion() {
        // Arrange - Guardar educación en la base de datos
        Educacion educacionGuardada = entityManager.persist(educacionValida);
        entityManager.flush();

        LocalDate nuevaFecha = LocalDate.of(2021, 6, 15);

        // Act - Actualizar fecha
        educacionGuardada.setFechaObtencion(nuevaFecha);
        Educacion educacionActualizada = educacionRepository.save(educacionGuardada);

        // Assert
        assertEquals(nuevaFecha, educacionActualizada.getFechaObtencion(),
                "La fecha de obtención debería estar actualizada");

        // Verificar en la base de datos
        Educacion educacionEncontrada = entityManager.find(Educacion.class, educacionGuardada.getId());
        assertEquals(nuevaFecha, educacionEncontrada.getFechaObtencion(),
                "La fecha en la BD debería estar actualizada");
    }

    // ==================== TESTS DE ELIMINACIÓN ====================

    @Test
    @DisplayName("deleteById - Debería eliminar educación por ID")
    void deleteById_ShouldDeleteEducacion() {
        // Arrange - Guardar educación en la base de datos
        Educacion educacionGuardada = entityManager.persist(educacionValida);
        entityManager.flush();
        Integer id = educacionGuardada.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Educacion.class, id), "La educación debería existir antes de eliminar");

        // Act
        educacionRepository.deleteById(id);

        // Assert - Verificar que ya no existe
        Educacion educacionEliminada = entityManager.find(Educacion.class, id);
        assertNull(educacionEliminada, "La educación no debería existir después de eliminar");
    }

    @Test
    @DisplayName("delete - Debería eliminar educación por entidad")
    void delete_ShouldDeleteEducacionEntity() {
        // Arrange - Guardar educación en la base de datos
        Educacion educacionGuardada = entityManager.persist(educacionValida);
        entityManager.flush();
        Integer id = educacionGuardada.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Educacion.class, id), "La educación debería existir antes de eliminar");

        // Act
        educacionRepository.delete(educacionGuardada);

        // Assert - Verificar que ya no existe
        Educacion educacionEliminada = entityManager.find(Educacion.class, id);
        assertNull(educacionEliminada, "La educación no debería existir después de eliminar");
    }

    @Test
    @DisplayName("deleteById - No debería lanzar excepción al eliminar ID no existente")
    void deleteById_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            educacionRepository.deleteById(999);
        }, "No debería lanzar excepción al eliminar un ID que no existe");
    }

    @Test
    @DisplayName("delete - Debería eliminar educación y su imagen en cascada")
    void delete_ShouldDeleteEducacionAndCascadeImage() {
        // Arrange - Guardar educación con imagen en la base de datos
        Educacion educacionGuardada = entityManager.persist(educacionConImagen);
        entityManager.flush();
        Integer idEducacion = educacionGuardada.getId();
        Integer idImagen = educacionGuardada.getImagen().getId();

        // Verificar que existen antes de eliminar
        assertNotNull(entityManager.find(Educacion.class, idEducacion), "La educación debería existir");
        assertNotNull(entityManager.find(Imagen.class, idImagen), "La imagen debería existir");

        // Act
        educacionRepository.delete(educacionGuardada);

        // Assert - Verificar que no existen
        assertNull(entityManager.find(Educacion.class, idEducacion), "La educación no debería existir");
        assertNull(entityManager.find(Imagen.class, idImagen), "La imagen no debería existir");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("findAll - Debería manejar educaciones con fechas extremas")
    void findAll_ShouldHandleEducacionesWithExtremeDates() {
        // Arrange - Educaciones con fechas extremas
        Educacion educacionAntigua = Educacion.builder()
                .titulo("Educación Antigua")
                .fechaObtencion(LocalDate.of(1900, 1, 1))
                .descripcion("Educación del año 1900")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(usuario)
                .build();

        Educacion educacionFutura = Educacion.builder()
                .titulo("Educación Futura")
                .fechaObtencion(LocalDate.of(2100, 12, 31))
                .descripcion("Educación del año 2100")
                .tipoEducacion(TipoEducacion.OTROS)
                .usuario(usuario)
                .build();

        entityManager.persist(educacionAntigua);
        entityManager.persist(educacionFutura);
        entityManager.flush();

        // Act
        List<Educacion> educaciones = educacionRepository.findAll();

        // Assert
        assertNotNull(educaciones, "La lista no debería ser nula");
        assertTrue(educaciones.size() >= 2, "Debería haber al menos 2 educaciones");

        boolean foundAntigua = educaciones.stream()
                .anyMatch(e -> e.getFechaObtencion().equals(LocalDate.of(1900, 1, 1)));
        boolean foundFutura = educaciones.stream()
                .anyMatch(e -> e.getFechaObtencion().equals(LocalDate.of(2100, 12, 31)));
        assertTrue(foundAntigua, "La educación antigua debería estar en la lista");
        assertTrue(foundFutura, "La educación futura debería estar en la lista");
    }

    @Test
    @DisplayName("save - Debería manejar títulos y descripciones largos")
    void save_ShouldHandleLongTitlesAndDescriptions() {
        // Arrange
        String tituloLargo = "T".repeat(140);
        String descripcionLarga = "D".repeat(295);

        Educacion educacionLarga = Educacion.builder()
                .titulo(tituloLargo)
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionLarga)
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .usuario(usuario)
                .build();

        // Act
        Educacion educacionGuardada = educacionRepository.save(educacionLarga);

        // Assert
        assertNotNull(educacionGuardada, "La educación guardada no debería ser nula");
        assertNotNull(educacionGuardada.getId(), "El ID no debería ser nulo");
        assertEquals(tituloLargo, educacionGuardada.getTitulo(), "El título largo debería mantenerse");
        assertEquals(descripcionLarga, educacionGuardada.getDescripcion(), "La descripción larga debería mantenerse");

        // Verificar en la base de datos
        Educacion educacionEncontrada = entityManager.find(Educacion.class, educacionGuardada.getId());
        assertEquals(tituloLargo, educacionEncontrada.getTitulo(), "El título largo en la BD debería mantenerse");
        assertEquals(descripcionLarga, educacionEncontrada.getDescripcion(), "La descripción larga en la BD debería mantenerse");
    }

    @Test
    @DisplayName("save - Debería manejar títulos con caracteres especiales")
    void save_ShouldHandleTitlesWithSpecialCharacters() {
        // Arrange
        String tituloEspecial = "Educación: Cursos, Talleres & Seminarios (2024)";
        String descripcionEspecial = "Descripción con acentos: áéíóú ñ y caracteres especiales !@#$%^&*()";

        Educacion educacionEspecial = Educacion.builder()
                .titulo(tituloEspecial)
                .fechaObtencion(LocalDate.now())
                .descripcion(descripcionEspecial)
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .usuario(usuario)
                .build();

        // Act
        Educacion educacionGuardada = educacionRepository.save(educacionEspecial);

        // Assert
        assertNotNull(educacionGuardada, "La educación guardada no debería ser nula");
        assertEquals(tituloEspecial, educacionGuardada.getTitulo(), "El título con caracteres especiales debería mantenerse");
        assertEquals(descripcionEspecial, educacionGuardada.getDescripcion(),
                "La descripción con caracteres especiales debería mantenerse");
    }

    // ==================== TESTS DE INTEGRACIÓN ====================

    @Test
    @DisplayName("save - Debería guardar educación con cascada a imagen")
    void save_ShouldSaveEducacionWithCascadeToImage() {
        // Act
        Educacion educacionGuardada = educacionRepository.save(educacionConImagen);

        // Assert
        assertNotNull(educacionGuardada, "La educación guardada no debería ser nula");
        assertNotNull(educacionGuardada.getId(), "El ID de la educación no debería ser nulo");

        // Verificar que la imagen se guardó en cascada
        Imagen imagenGuardada = educacionGuardada.getImagen();
        assertNotNull(imagenGuardada, "La imagen no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen no debería ser nulo");

        // Verificar en la base de datos
        Educacion educacionEncontrada = entityManager.find(Educacion.class, educacionGuardada.getId());
        assertNotNull(educacionEncontrada.getImagen(), "La imagen en la BD no debería ser nula");
        assertNotNull(educacionEncontrada.getImagen().getId(), "El ID de la imagen en la BD no debería ser nulo");
    }

    @Test
    @DisplayName("save - Debería guardar educación con usuario existente")
    void save_ShouldSaveEducacionWithExistingUsuario() {
        // Arrange - Guardar usuario primero
        Usuario usuarioGuardado = entityManager.persist(usuario);
        entityManager.flush();

        Educacion educacionConUsuario = Educacion.builder()
                .titulo("Curso de Java")
                .fechaObtencion(LocalDate.now())
                .descripcion("Curso completo de Java desde cero")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .usuario(usuarioGuardado)
                .build();

        // Act
        Educacion educacionGuardada = educacionRepository.save(educacionConUsuario);

        // Assert
        assertNotNull(educacionGuardada, "La educación guardada no debería ser nula");
        assertNotNull(educacionGuardada.getId(), "El ID no debería ser nulo");
        assertNotNull(educacionGuardada.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(usuarioGuardado.getId(), educacionGuardada.getUsuario().getId(),
                "El ID del usuario debería coincidir");

        // Verificar en la base de datos
        Educacion educacionEncontrada = entityManager.find(Educacion.class, educacionGuardada.getId());
        assertNotNull(educacionEncontrada.getUsuario(), "El usuario en la BD no debería ser nulo");
        assertEquals(usuarioGuardado.getId(), educacionEncontrada.getUsuario().getId(),
                "El ID del usuario en la BD debería coincidir");
    }

    // ==================== TESTS NEGATIVOS ====================

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar educación con campos obligatorios nulos")
    void save_ShouldThrowException_WhenRequiredFieldsAreNull() {
        // Arrange - Educación con campos obligatorios nulos
        Educacion educacionInvalida = Educacion.builder()
                .titulo(null)
                .fechaObtencion(null)
                .descripcion(null)
                .tipoEducacion(null)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionRepository.save(educacionInvalida);
        }, "Debería lanzar excepción al guardar educación con campos obligatorios nulos");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar educación con título vacío")
    void save_ShouldThrowException_WhenTituloIsEmpty() {
        // Arrange - Educación con título vacío
        Educacion educacionTituloVacio = Educacion.builder()
                .titulo("")
                .fechaObtencion(LocalDate.now())
                .descripcion("Descripción válida")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionRepository.save(educacionTituloVacio);
        }, "Debería lanzar excepción al guardar educación con título vacío");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar educación con descripción vacía")
    void save_ShouldThrowException_WhenDescripcionIsEmpty() {
        // Arrange - Educación con descripción vacía
        Educacion educacionDescripcionVacia = Educacion.builder()
                .titulo("Título válido")
                .fechaObtencion(LocalDate.now())
                .descripcion("")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            educacionRepository.save(educacionDescripcionVacia);
        }, "Debería lanzar excepción al guardar educación con descripción vacía");
    }

    @Test
    @DisplayName("findById - Debería retornar Optional vacío con ID nulo")
    void findById_ShouldReturnEmptyOptional_WithNullId() {
        // Act
        Optional<Educacion> educacionEncontrada = educacionRepository.findById(null);

        // Assert
        assertFalse(educacionEncontrada.isPresent(), "Debería retornar Optional vacío con ID nulo");
    }

    // ==================== TESTS DE MÚLTIPLES OPERACIONES ====================

    @Test
    @DisplayName("Debe manejar múltiples operaciones CRUD secuenciales")
    void shouldHandleMultipleCrudOperationsSequentially() {
        // 1. Guardar educación
        Educacion educacionGuardada = educacionRepository.save(educacionValida);
        assertNotNull(educacionGuardada.getId(), "El ID no debería ser nulo después de guardar");

        // 2. Buscar educación
        Optional<Educacion> educacionEncontrada = educacionRepository.findById(educacionGuardada.getId());
        assertTrue(educacionEncontrada.isPresent(), "La educación debería ser encontrada");

        // 3. Actualizar educación
        educacionEncontrada.get().setTitulo("Título Actualizado");
        Educacion educacionActualizada = educacionRepository.save(educacionEncontrada.get());
        assertEquals("Título Actualizado", educacionActualizada.getTitulo(),
                "El título debería estar actualizado");

        // 4. Verificar actualización
        Optional<Educacion> educacionVerificada = educacionRepository.findById(educacionGuardada.getId());
        assertTrue(educacionVerificada.isPresent(), "La educación debería existir después de actualizar");
        assertEquals("Título Actualizado", educacionVerificada.get().getTitulo(),
                "El título debería estar actualizado");

        // 5. Eliminar educación
        educacionRepository.deleteById(educacionGuardada.getId());
        Optional<Educacion> educacionEliminada = educacionRepository.findById(educacionGuardada.getId());
        assertFalse(educacionEliminada.isPresent(), "La educación no debería existir después de eliminar");
    }

    @Test
    @DisplayName("Debe manejar múltiples educaciones con diferentes tipos")
    void shouldHandleMultipleEducacionesWithDifferentTypes() {
        // Arrange - Crear educaciones de diferentes tipos
        Educacion educacionFormal = Educacion.builder()
                .titulo("Carrera Universitaria")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Carrera de grado universitario")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(usuario)
                .build();

        Educacion educacionCurso = Educacion.builder()
                .titulo("Curso de Python")
                .fechaObtencion(LocalDate.of(2023, 1, 20))
                .descripcion("Curso intensivo de Python")
                .tipoEducacion(TipoEducacion.INFORMAL_CURSO)
                .usuario(usuario)
                .build();

        Educacion educacionAutodidacta = Educacion.builder()
                .titulo("Aprendizaje Autodidacta")
                .fechaObtencion(LocalDate.of(2024, 3, 10))
                .descripcion("Aprendizaje autodidacta de programación")
                .tipoEducacion(TipoEducacion.AUTODIDACTA)
                .usuario(usuario)
                .build();

        Educacion educacionOtros = Educacion.builder()
                .titulo("Otros Estudios")
                .fechaObtencion(LocalDate.of(2022, 8, 5))
                .descripcion("Otros tipos de estudios")
                .tipoEducacion(TipoEducacion.OTROS)
                .usuario(usuario)
                .build();

        // Act - Guardar todas
        educacionRepository.save(educacionFormal);
        educacionRepository.save(educacionCurso);
        educacionRepository.save(educacionAutodidacta);
        educacionRepository.save(educacionOtros);

        // Act - Buscar todas
        List<Educacion> educaciones = educacionRepository.findAll();

        // Assert
        assertNotNull(educaciones, "La lista no debería ser nula");
        assertEquals(4, educaciones.size(), "Debería haber exactamente 4 educaciones");

        // Verificar que todos los tipos están presentes
        long countFormal = educaciones.stream()
                .filter(e -> e.getTipoEducacion() == TipoEducacion.FORMAL)
                .count();
        long countCurso = educaciones.stream()
                .filter(e -> e.getTipoEducacion() == TipoEducacion.INFORMAL_CURSO)
                .count();
        long countAutodidacta = educaciones.stream()
                .filter(e -> e.getTipoEducacion() == TipoEducacion.AUTODIDACTA)
                .count();
        long countOtros = educaciones.stream()
                .filter(e -> e.getTipoEducacion() == TipoEducacion.OTROS)
                .count();

        assertEquals(1, countFormal, "Debería haber 1 educación formal");
        assertEquals(1, countCurso, "Debería haber 1 educación de curso informal");
        assertEquals(1, countAutodidacta, "Debería haber 1 educación autodidacta");
        assertEquals(1, countOtros, "Debería haber 1 educación de otros tipos");
    }
}