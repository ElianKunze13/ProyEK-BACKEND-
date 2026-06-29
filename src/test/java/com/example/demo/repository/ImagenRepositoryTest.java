package com.example.demo.repository;

import com.example.demo.enums.*;
import com.example.demo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ImagenRepository
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Usa @DataJpaTest para pruebas de repositorio con JPA
 * Usa TestEntityManager para gestionar las entidades en el contexto de prueba
 */
@DataJpaTest
@ActiveProfiles("test")
class ImagenRepositoryTest {

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Imagen imagenValida;
    private Imagen imagenConRelaciones;
    private Usuario usuario;
    private Conocimiento conocimiento;
    private Educacion educacion;
    private Experiencia experiencia;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear usuario para las relaciones
        usuario = Usuario.builder()
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack con 5 años de experiencia")
                .descripcion("Apasionado por la tecnología y el desarrollo de software.")
                .rol(Role.USER)
                .active(true)
                .build();

        // Crear conocimiento para la relación
        conocimiento = Conocimiento.builder()
                .nombre("Java")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .usuario(usuario)
                .build();

        // Crear educación para la relación
        educacion = Educacion.builder()
                .titulo("Ingeniería en Sistemas")
                .fechaObtencion(LocalDate.of(2020, 6, 15))
                .descripcion("Formación universitaria en sistemas")
                .tipoEducacion(TipoEducacion.FORMAL)
                .usuario(usuario)
                .build();

        // Crear experiencia para la relación
        experiencia = Experiencia.builder()
                .titulo("Proyecto de Sistema de Gestión")
                .fechaInicioProyecto(LocalDate.of(2022, 1, 1))
                .fechaFinProyecto(LocalDate.of(2022, 12, 31))
                .descripcion("Desarrollo de un sistema de gestión empresarial")
                .link("https://github.com/proyecto-gestion")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .usuario(usuario)
                .build();

        // Crear imagen válida sin relaciones
        imagenValida = Imagen.builder()
                .url("https://ejemplo.com/imagen.jpg")
                .alt("Imagen de prueba")
                .build();

        // Crear imagen con relaciones
        imagenConRelaciones = Imagen.builder()
                .url("https://ejemplo.com/imagen-relacionada.jpg")
                .alt("Imagen con relaciones")
                .conocimiento(conocimiento)
                .educacion(educacion)
                .experiencia(experiencia)
                .usuario(usuario)
                .build();
    }

    // ==================== TESTS DE GUARDADO ====================

    @Test
    @DisplayName("save - Debería guardar una imagen correctamente")
    void save_ShouldSaveImagenCorrectly() {
        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenValida);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertEquals("https://ejemplo.com/imagen.jpg", imagenGuardada.getUrl(), "La URL debería coincidir");
        assertEquals("Imagen de prueba", imagenGuardada.getAlt(), "El alt debería coincidir");
        assertNull(imagenGuardada.getConocimiento(), "El conocimiento debería ser nulo");
        assertNull(imagenGuardada.getEducacion(), "La educación debería ser nula");
        assertNull(imagenGuardada.getExperiencia(), "La experiencia debería ser nula");
        assertNull(imagenGuardada.getUsuario(), "El usuario debería ser nulo");

        // Verificar que se guardó en la base de datos
        Imagen imagenEncontrada = entityManager.find(Imagen.class, imagenGuardada.getId());
        assertNotNull(imagenEncontrada, "La imagen debería existir en la base de datos");
        assertEquals(imagenGuardada.getUrl(), imagenEncontrada.getUrl(), "La URL debería coincidir en la BD");
    }

    @Test
    @DisplayName("save - Debería guardar una imagen con relaciones")
    void save_ShouldSaveImagenWithRelationships() {
        // Arrange - Guardar entidades relacionadas primero
        entityManager.persist(usuario);
        entityManager.persist(conocimiento);
        entityManager.persist(educacion);
        entityManager.persist(experiencia);
        entityManager.flush();

        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenConRelaciones);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertEquals("https://ejemplo.com/imagen-relacionada.jpg", imagenGuardada.getUrl(), "La URL debería coincidir");
        assertEquals("Imagen con relaciones", imagenGuardada.getAlt(), "El alt debería coincidir");

        // Verificar relaciones
        assertNotNull(imagenGuardada.getConocimiento(), "El conocimiento no debería ser nulo");
        assertEquals(conocimiento.getId(), imagenGuardada.getConocimiento().getId(), "El ID del conocimiento debería coincidir");

        assertNotNull(imagenGuardada.getEducacion(), "La educación no debería ser nula");
        assertEquals(educacion.getId(), imagenGuardada.getEducacion().getId(), "El ID de la educación debería coincidir");

        assertNotNull(imagenGuardada.getExperiencia(), "La experiencia no debería ser nula");
        assertEquals(experiencia.getId(), imagenGuardada.getExperiencia().getId(), "El ID de la experiencia debería coincidir");

        assertNotNull(imagenGuardada.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(usuario.getId(), imagenGuardada.getUsuario().getId(), "El ID del usuario debería coincidir");

        // Verificar en la base de datos
        Imagen imagenEncontrada = entityManager.find(Imagen.class, imagenGuardada.getId());
        assertNotNull(imagenEncontrada, "La imagen debería existir en la BD");
        assertNotNull(imagenEncontrada.getConocimiento(), "El conocimiento debería existir en la BD");
        assertNotNull(imagenEncontrada.getEducacion(), "La educación debería existir en la BD");
        assertNotNull(imagenEncontrada.getExperiencia(), "La experiencia debería existir en la BD");
        assertNotNull(imagenEncontrada.getUsuario(), "El usuario debería existir en la BD");
    }

    @Test
    @DisplayName("save - Debería guardar una imagen sin relaciones")
    void save_ShouldSaveImagenWithoutRelationships() {
        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenValida);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertNull(imagenGuardada.getConocimiento(), "El conocimiento debería ser nulo");
        assertNull(imagenGuardada.getEducacion(), "La educación debería ser nula");
        assertNull(imagenGuardada.getExperiencia(), "La experiencia debería ser nula");
        assertNull(imagenGuardada.getUsuario(), "El usuario debería ser nulo");
    }

    @Test
    @DisplayName("save - Debería guardar una imagen con URL larga")
    void save_ShouldSaveImagenWithLongUrl() {
        // Arrange - Crear URL larga
        String longUrl = "https://ejemplo.com/" + "a".repeat(200) + ".jpg";
        Imagen imagenConUrlLarga = Imagen.builder()
                .url(longUrl)
                .alt("Imagen con URL larga")
                .build();

        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenConUrlLarga);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertEquals(longUrl, imagenGuardada.getUrl(), "La URL larga debería coincidir");
        assertEquals("Imagen con URL larga", imagenGuardada.getAlt(), "El alt debería coincidir");
    }

    @Test
    @DisplayName("save - Debería guardar una imagen con texto alt largo")
    void save_ShouldSaveImagenWithLongAlt() {
        // Arrange - Crear alt largo
        String longAlt = "Texto alternativo muy largo " + "b".repeat(300);
        Imagen imagenConAltLargo = Imagen.builder()
                .url("https://ejemplo.com/imagen.jpg")
                .alt(longAlt)
                .build();

        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenConAltLargo);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertEquals(longAlt, imagenGuardada.getAlt(), "El alt largo debería coincidir");
    }

    // ==================== TESTS DE BÚSQUEDA ====================

    @Test
    @DisplayName("findById - Debería encontrar imagen por ID cuando existe")
    void findById_ShouldFindImagen_WhenIdExists() {
        // Arrange - Guardar imagen en la base de datos
        Imagen imagenGuardada = entityManager.persist(imagenValida);
        entityManager.flush();

        // Act
        Optional<Imagen> imagenEncontrada = imagenRepository.findById(imagenGuardada.getId());

        // Assert
        assertTrue(imagenEncontrada.isPresent(), "La imagen debería ser encontrada");
        assertEquals(imagenGuardada.getId(), imagenEncontrada.get().getId(), "El ID debería coincidir");
        assertEquals(imagenGuardada.getUrl(), imagenEncontrada.get().getUrl(), "La URL debería coincidir");
        assertEquals(imagenGuardada.getAlt(), imagenEncontrada.get().getAlt(), "El alt debería coincidir");
    }

    @Test
    @DisplayName("findById - No debería encontrar imagen cuando ID no existe")
    void findById_ShouldNotFindImagen_WhenIdDoesNotExist() {
        // Act
        Optional<Imagen> imagenEncontrada = imagenRepository.findById(999);

        // Assert
        assertFalse(imagenEncontrada.isPresent(), "La imagen no debería ser encontrada");
    }

    @Test
    @DisplayName("findById - Debería encontrar imagen con relaciones")
    void findById_ShouldFindImagenWithRelationships() {
        // Arrange - Guardar imagen con relaciones
        entityManager.persist(usuario);
        entityManager.persist(conocimiento);
        entityManager.persist(educacion);
        entityManager.persist(experiencia);
        Imagen imagenGuardada = entityManager.persist(imagenConRelaciones);
        entityManager.flush();

        // Act
        Optional<Imagen> imagenEncontrada = imagenRepository.findById(imagenGuardada.getId());

        // Assert
        assertTrue(imagenEncontrada.isPresent(), "La imagen debería ser encontrada");
        assertNotNull(imagenEncontrada.get().getConocimiento(), "El conocimiento debería estar presente");
        assertNotNull(imagenEncontrada.get().getEducacion(), "La educación debería estar presente");
        assertNotNull(imagenEncontrada.get().getExperiencia(), "La experiencia debería estar presente");
        assertNotNull(imagenEncontrada.get().getUsuario(), "El usuario debería estar presente");
    }

    @Test
    @DisplayName("findAll - Debería encontrar todas las imágenes")
    void findAll_ShouldFindAllImagenes() {
        // Arrange - Guardar varias imágenes en la base de datos
        Imagen imagen1 = Imagen.builder()
                .url("https://ejemplo.com/imagen1.jpg")
                .alt("Imagen 1")
                .build();

        Imagen imagen2 = Imagen.builder()
                .url("https://ejemplo.com/imagen2.jpg")
                .alt("Imagen 2")
                .build();

        entityManager.persist(imagen1);
        entityManager.persist(imagen2);
        entityManager.flush();

        // Act
        List<Imagen> imagenes = imagenRepository.findAll();

        // Assert
        assertNotNull(imagenes, "La lista de imágenes no debería ser nula");
        assertTrue(imagenes.size() >= 2, "Debería haber al menos 2 imágenes");

        // Verificar que las imágenes guardadas están en la lista
        boolean foundImagen1 = imagenes.stream()
                .anyMatch(i -> i.getUrl().equals("https://ejemplo.com/imagen1.jpg"));
        boolean foundImagen2 = imagenes.stream()
                .anyMatch(i -> i.getUrl().equals("https://ejemplo.com/imagen2.jpg"));
        assertTrue(foundImagen1, "La imagen 1 debería estar en la lista");
        assertTrue(foundImagen2, "La imagen 2 debería estar en la lista");
    }

    // ==================== TESTS DE ACTUALIZACIÓN ====================

    @Test
    @DisplayName("save - Debería actualizar una imagen existente")
    void save_ShouldUpdateExistingImagen() {
        // Arrange - Guardar imagen en la base de datos
        Imagen imagenGuardada = entityManager.persist(imagenValida);
        entityManager.flush();

        // Act - Actualizar imagen
        imagenGuardada.setUrl("https://ejemplo.com/imagen-actualizada.jpg");
        imagenGuardada.setAlt("Texto alternativo actualizado");
        Imagen imagenActualizada = imagenRepository.save(imagenGuardada);

        // Assert
        assertNotNull(imagenActualizada, "La imagen actualizada no debería ser nula");
        assertEquals(imagenGuardada.getId(), imagenActualizada.getId(), "El ID debería ser el mismo");
        assertEquals("https://ejemplo.com/imagen-actualizada.jpg", imagenActualizada.getUrl(), "La URL debería estar actualizada");
        assertEquals("Texto alternativo actualizado", imagenActualizada.getAlt(), "El alt debería estar actualizado");

        // Verificar en la base de datos
        Imagen imagenEncontrada = entityManager.find(Imagen.class, imagenGuardada.getId());
        assertNotNull(imagenEncontrada, "La imagen debería existir en la BD");
        assertEquals("https://ejemplo.com/imagen-actualizada.jpg", imagenEncontrada.getUrl(), "La URL en la BD debería estar actualizada");
        assertEquals("Texto alternativo actualizado", imagenEncontrada.getAlt(), "El alt en la BD debería estar actualizado");
    }

    @Test
    @DisplayName("save - Debería actualizar relaciones de una imagen")
    void save_ShouldUpdateImagenRelationships() {
        // Arrange - Guardar imagen en la base de datos
        entityManager.persist(usuario);
        Imagen imagenGuardada = entityManager.persist(imagenValida);
        entityManager.flush();

        // Crear nuevas relaciones
        Conocimiento nuevoConocimiento = Conocimiento.builder()
                .nombre("Spring Boot")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .usuario(usuario)
                .build();

        entityManager.persist(nuevoConocimiento);
        entityManager.flush();

        // Act - Actualizar relaciones
        imagenGuardada.setConocimiento(nuevoConocimiento);
        imagenGuardada.setUsuario(usuario);
        Imagen imagenActualizada = imagenRepository.save(imagenGuardada);

        // Assert
        assertNotNull(imagenActualizada.getConocimiento(), "El conocimiento no debería ser nulo");
        assertEquals(nuevoConocimiento.getId(), imagenActualizada.getConocimiento().getId(), "El ID del conocimiento debería coincidir");
        assertNotNull(imagenActualizada.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(usuario.getId(), imagenActualizada.getUsuario().getId(), "El ID del usuario debería coincidir");

        // Verificar en la base de datos
        Imagen imagenEncontrada = entityManager.find(Imagen.class, imagenGuardada.getId());
        assertNotNull(imagenEncontrada.getConocimiento(), "El conocimiento debería existir en la BD");
        assertEquals(nuevoConocimiento.getId(), imagenEncontrada.getConocimiento().getId(), "El conocimiento en la BD debería coincidir");
    }

    // ==================== TESTS DE ELIMINACIÓN ====================

    @Test
    @DisplayName("deleteById - Debería eliminar imagen por ID")
    void deleteById_ShouldDeleteImagen() {
        // Arrange - Guardar imagen en la base de datos
        Imagen imagenGuardada = entityManager.persist(imagenValida);
        entityManager.flush();
        Integer id = imagenGuardada.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Imagen.class, id), "La imagen debería existir antes de eliminar");

        // Act
        imagenRepository.deleteById(id);

        // Assert - Verificar que ya no existe
        Imagen imagenEliminada = entityManager.find(Imagen.class, id);
        assertNull(imagenEliminada, "La imagen no debería existir después de eliminar");
    }

    @Test
    @DisplayName("delete - Debería eliminar imagen por entidad")
    void delete_ShouldDeleteImagenEntity() {
        // Arrange - Guardar imagen en la base de datos
        Imagen imagenGuardada = entityManager.persist(imagenValida);
        entityManager.flush();
        Integer id = imagenGuardada.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Imagen.class, id), "La imagen debería existir antes de eliminar");

        // Act
        imagenRepository.delete(imagenGuardada);

        // Assert - Verificar que ya no existe
        Imagen imagenEliminada = entityManager.find(Imagen.class, id);
        assertNull(imagenEliminada, "La imagen no debería existir después de eliminar");
    }

    @Test
    @DisplayName("deleteById - No debería lanzar excepción al eliminar ID no existente")
    void deleteById_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            imagenRepository.deleteById(999);
        }, "No debería lanzar excepción al eliminar un ID que no existe");
    }

    @Test
    @DisplayName("deleteAll - Debería eliminar todas las imágenes")
    void deleteAll_ShouldDeleteAllImagenes() {
        // Arrange - Guardar varias imágenes
        Imagen imagen1 = Imagen.builder()
                .url("https://ejemplo.com/imagen1.jpg")
                .alt("Imagen 1")
                .build();

        Imagen imagen2 = Imagen.builder()
                .url("https://ejemplo.com/imagen2.jpg")
                .alt("Imagen 2")
                .build();

        entityManager.persist(imagen1);
        entityManager.persist(imagen2);
        entityManager.flush();

        // Verificar que existen
        List<Imagen> imagenesAntes = imagenRepository.findAll();
        assertTrue(imagenesAntes.size() >= 2, "Debería haber al menos 2 imágenes antes de eliminar");

        // Act
        imagenRepository.deleteAll();

        // Assert
        List<Imagen> imagenesDespues = imagenRepository.findAll();
        assertEquals(0, imagenesDespues.size(), "No debería haber imágenes después de eliminar todas");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("save - Debería guardar imagen con URL vacía")
    void save_ShouldSaveImagenWithEmptyUrl() {
        // Arrange
        Imagen imagenConUrlVacia = Imagen.builder()
                .url("")
                .alt("Imagen con URL vacía")
                .build();

        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenConUrlVacia);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertEquals("", imagenGuardada.getUrl(), "La URL vacía debería mantenerse");
    }

    @Test
    @DisplayName("save - Debería guardar imagen con alt vacío")
    void save_ShouldSaveImagenWithEmptyAlt() {
        // Arrange
        Imagen imagenConAltVacio = Imagen.builder()
                .url("https://ejemplo.com/imagen.jpg")
                .alt("")
                .build();

        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenConAltVacio);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertEquals("", imagenGuardada.getAlt(), "El alt vacío debería mantenerse");
    }

    @Test
    @DisplayName("save - Debería guardar imagen con caracteres especiales en URL")
    void save_ShouldSaveImagenWithSpecialCharactersInUrl() {
        // Arrange
        String urlEspecial = "https://ejemplo.com/imagen-con-espacios%20y-simbolos!@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~.jpg";
        Imagen imagenConUrlEspecial = Imagen.builder()
                .url(urlEspecial)
                .alt("Imagen con URL especial")
                .build();

        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenConUrlEspecial);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertEquals(urlEspecial, imagenGuardada.getUrl(), "La URL con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("save - Debería guardar imagen con caracteres especiales en alt")
    void save_ShouldSaveImagenWithSpecialCharactersInAlt() {
        // Arrange
        String altEspecial = "Texto alternativo con acentos: áéíóú ñü y caracteres especiales !@#$%^&*()_+-=[]{}\\|;:'\",.<>/?~";
        Imagen imagenConAltEspecial = Imagen.builder()
                .url("https://ejemplo.com/imagen.jpg")
                .alt(altEspecial)
                .build();

        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenConAltEspecial);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertEquals(altEspecial, imagenGuardada.getAlt(), "El alt con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("findById - Debería retornar Optional vacío con ID nulo")
    void findById_ShouldReturnEmptyOptional_WithNullId() {
        // Act
        Optional<Imagen> imagenEncontrada = imagenRepository.findById(null);

        // Assert
        assertFalse(imagenEncontrada.isPresent(), "Debería retornar Optional vacío con ID nulo");
    }

    // ==================== TESTS NEGATIVOS ====================

    @Test
    @DisplayName("save - Debería lanzar DataIntegrityViolationException al guardar imagen con url nula")
    void save_ShouldThrowDataIntegrityViolationException_WhenUrlIsNull() {
        // Arrange - Imagen con URL nula
        Imagen imagenSinUrl = Imagen.builder()
                .url(null)
                .alt("Imagen sin URL")
                .build();

        // Act & Assert - Debería lanzar excepción por violación de integridad de datos
        assertThrows(DataIntegrityViolationException.class, () -> {
            imagenRepository.saveAndFlush(imagenSinUrl);
        }, "Debería lanzar DataIntegrityViolationException cuando la URL es nula");
    }

    @Test
    @DisplayName("save - Debería lanzar DataIntegrityViolationException al guardar imagen con alt nulo")
    void save_ShouldThrowDataIntegrityViolationException_WhenAltIsNull() {
        // Arrange - Imagen con alt nulo
        Imagen imagenSinAlt = Imagen.builder()
                .url("https://ejemplo.com/imagen.jpg")
                .alt(null)
                .build();

        // Act & Assert - Debería lanzar excepción por violación de integridad de datos
        assertThrows(DataIntegrityViolationException.class, () -> {
            imagenRepository.saveAndFlush(imagenSinAlt);
        }, "Debería lanzar DataIntegrityViolationException cuando el alt es nulo");
    }

    @Test
    @DisplayName("save - Debería lanzar DataIntegrityViolationException al guardar imagen con url y alt nulos")
    void save_ShouldThrowDataIntegrityViolationException_WhenUrlAndAltAreNull() {
        // Arrange - Imagen con URL y alt nulos
        Imagen imagenSinDatos = Imagen.builder()
                .url(null)
                .alt(null)
                .build();

        // Act & Assert - Debería lanzar excepción por violación de integridad de datos
        assertThrows(DataIntegrityViolationException.class, () -> {
            imagenRepository.saveAndFlush(imagenSinDatos);
        }, "Debería lanzar DataIntegrityViolationException cuando URL y alt son nulos");
    }

    // ==================== TESTS DE INTEGRACIÓN CON RELACIONES ====================

    @Test
    @DisplayName("save - Debería guardar imagen y mantener relaciones bidireccionales")
    void save_ShouldSaveImagenAndMaintainBidirectionalRelationships() {
        // Arrange - Guardar entidades relacionadas
        entityManager.persist(usuario);
        entityManager.persist(conocimiento);
        entityManager.flush();

        // Crear imagen con conocimiento
        Imagen imagenConConocimiento = Imagen.builder()
                .url("https://ejemplo.com/imagen-conocimiento.jpg")
                .alt("Imagen para conocimiento")
                .conocimiento(conocimiento)
                .usuario(usuario)
                .build();

        // Act
        Imagen imagenGuardada = imagenRepository.save(imagenConConocimiento);

        // Assert
        assertNotNull(imagenGuardada, "La imagen guardada no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen guardada no debería ser nulo");
        assertNotNull(imagenGuardada.getConocimiento(), "El conocimiento no debería ser nulo");
        assertEquals(conocimiento.getId(), imagenGuardada.getConocimiento().getId(), "El ID del conocimiento debería coincidir");

        // Verificar la relación bidireccional - El conocimiento debería tener la imagen
        Conocimiento conocimientoEncontrado = entityManager.find(Conocimiento.class, conocimiento.getId());
        assertNotNull(conocimientoEncontrado.getImagen(), "La imagen en el conocimiento no debería ser nula");
        assertEquals(imagenGuardada.getId(), conocimientoEncontrado.getImagen().getId(), "El ID de la imagen en el conocimiento debería coincidir");
    }

    @Test
    @DisplayName("delete - Debería eliminar imagen y no afectar entidades relacionadas")
    void delete_ShouldDeleteImagenAndNotAffectRelatedEntities() {
        // Arrange - Guardar entidades relacionadas
        entityManager.persist(usuario);
        entityManager.persist(conocimiento);
        entityManager.persist(educacion);
        entityManager.persist(experiencia);

        Imagen imagenGuardada = entityManager.persist(imagenConRelaciones);
        entityManager.flush();

        Integer idImagen = imagenGuardada.getId();
        Integer idConocimiento = conocimiento.getId();
        Integer idEducacion = educacion.getId();
        Integer idExperiencia = experiencia.getId();
        Integer idUsuario = usuario.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Imagen.class, idImagen), "La imagen debería existir antes de eliminar");

        // Act
        imagenRepository.deleteById(idImagen);
        entityManager.flush();

        // Assert - La imagen no debería existir
        Imagen imagenEliminada = entityManager.find(Imagen.class, idImagen);
        assertNull(imagenEliminada, "La imagen no debería existir después de eliminar");

        // Las entidades relacionadas deberían seguir existiendo
        assertNotNull(entityManager.find(Conocimiento.class, idConocimiento), "El conocimiento debería seguir existiendo");
        assertNotNull(entityManager.find(Educacion.class, idEducacion), "La educación debería seguir existiendo");
        assertNotNull(entityManager.find(Experiencia.class, idExperiencia), "La experiencia debería seguir existiendo");
        assertNotNull(entityManager.find(Usuario.class, idUsuario), "El usuario debería seguir existiendo");

        // La relación en conocimiento debería ser null (si se configuró orphanRemoval o no)
        Conocimiento conocimientoActualizado = entityManager.find(Conocimiento.class, idConocimiento);
        // El conocimiento puede o no tener la imagen, depende de la configuración
        // Si la imagen se eliminó y orphanRemoval está configurado, la referencia debería ser null
        if (conocimientoActualizado.getImagen() != null) {
            // Si no se actualizó automáticamente, la referencia podría quedar huérfana
            // pero la entidad Imagen ya no existe
            assertNotEquals(idImagen, conocimientoActualizado.getImagen().getId(), "La referencia a la imagen eliminada no debería existir");
        }
    }

    // ==================== TESTS DE MÚLTIPLES OPERACIONES ====================

    @Test
    @DisplayName("Debe manejar múltiples operaciones CRUD secuenciales")
    void shouldHandleMultipleCrudOperationsSequentially() {
        // 1. Guardar imagen
        Imagen imagenGuardada = imagenRepository.save(imagenValida);
        assertNotNull(imagenGuardada.getId(), "El ID no debería ser nulo después de guardar");

        // 2. Buscar imagen
        Optional<Imagen> imagenEncontrada = imagenRepository.findById(imagenGuardada.getId());
        assertTrue(imagenEncontrada.isPresent(), "La imagen debería ser encontrada");

        // 3. Actualizar imagen
        imagenEncontrada.get().setUrl("https://ejemplo.com/imagen-actualizada.jpg");
        imagenEncontrada.get().setAlt("Alt actualizado");
        Imagen imagenActualizada = imagenRepository.save(imagenEncontrada.get());
        assertEquals("https://ejemplo.com/imagen-actualizada.jpg", imagenActualizada.getUrl(), "La URL debería estar actualizada");
        assertEquals("Alt actualizado", imagenActualizada.getAlt(), "El alt debería estar actualizado");

        // 4. Verificar actualización
        Optional<Imagen> imagenVerificada = imagenRepository.findById(imagenGuardada.getId());
        assertTrue(imagenVerificada.isPresent(), "La imagen debería existir después de actualizar");
        assertEquals("https://ejemplo.com/imagen-actualizada.jpg", imagenVerificada.get().getUrl(), "La URL debería estar actualizada");
        assertEquals("Alt actualizado", imagenVerificada.get().getAlt(), "El alt debería estar actualizado");

        // 5. Eliminar imagen
        imagenRepository.deleteById(imagenGuardada.getId());
        Optional<Imagen> imagenEliminada = imagenRepository.findById(imagenGuardada.getId());
        assertFalse(imagenEliminada.isPresent(), "La imagen no debería existir después de eliminar");
    }

    // ==================== TESTS DE MÉTODOS HEREDADOS ====================

    @Test
    @DisplayName("existsById - Debería retornar true cuando la imagen existe")
    void existsById_ShouldReturnTrue_WhenImagenExists() {
        // Arrange - Guardar imagen en la base de datos
        Imagen imagenGuardada = entityManager.persist(imagenValida);
        entityManager.flush();

        // Act
        boolean exists = imagenRepository.existsById(imagenGuardada.getId());

        // Assert
        assertTrue(exists, "Debería retornar true cuando la imagen existe");
    }

    @Test
    @DisplayName("existsById - Debería retornar false cuando la imagen no existe")
    void existsById_ShouldReturnFalse_WhenImagenDoesNotExist() {
        // Act
        boolean exists = imagenRepository.existsById(999);

        // Assert
        assertFalse(exists, "Debería retornar false cuando la imagen no existe");
    }

    @Test
    @DisplayName("count - Debería retornar el número correcto de imágenes")
    void count_ShouldReturnCorrectNumberOfImagenes() {
        // Arrange - Guardar varias imágenes
        entityManager.persist(imagenValida);
        Imagen otraImagen = Imagen.builder()
                .url("https://ejemplo.com/otra-imagen.jpg")
                .alt("Otra imagen")
                .build();
        entityManager.persist(otraImagen);
        entityManager.flush();

        // Act
        long count = imagenRepository.count();

        // Assert
        assertTrue(count >= 2, "Debería haber al menos 2 imágenes");
    }

    // ==================== TEST DE RENDIMIENTO Y VOLUMEN ====================

    @Test
    @DisplayName("save - Debería guardar múltiples imágenes secuencialmente")
    void save_ShouldSaveMultipleImagenesSequentially() {
        // Arrange - Crear múltiples imágenes
        int cantidad = 5;
        Imagen[] imagenes = new Imagen[cantidad];

        for (int i = 0; i < cantidad; i++) {
            imagenes[i] = Imagen.builder()
                    .url("https://ejemplo.com/imagen-" + i + ".jpg")
                    .alt("Imagen " + i)
                    .build();
        }

        // Act - Guardar todas las imágenes
        for (Imagen imagen : imagenes) {
            Imagen guardada = imagenRepository.save(imagen);
            assertNotNull(guardada.getId(), "La imagen " + imagen.getAlt() + " debería tener ID");
        }

        // Assert - Verificar que todas se guardaron
        List<Imagen> imagenesGuardadas = imagenRepository.findAll();
        assertTrue(imagenesGuardadas.size() >= cantidad, "Debería haber al menos " + cantidad + " imágenes");

        for (int i = 0; i < cantidad; i++) {
            final int index = i;
            boolean encontrada = imagenesGuardadas.stream()
                    .anyMatch(img -> img.getUrl().equals("https://ejemplo.com/imagen-" + index + ".jpg"));
            assertTrue(encontrada, "La imagen " + index + " debería estar en la base de datos");
        }
    }
}