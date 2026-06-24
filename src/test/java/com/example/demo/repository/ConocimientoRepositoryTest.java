package com.example.demo.repository;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoConocimiento;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ConocimientoRepository
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Usa @DataJpaTest para pruebas de repositorio con JPA
 * Usa TestEntityManager para gestionar las entidades en el contexto de prueba
 */
@DataJpaTest
@ActiveProfiles("test")
class ConocimientoRepositoryTest {

    @Autowired
    private ConocimientoRepository conocimientoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Conocimiento conocimientoFrontend;
    private Conocimiento conocimientoBackend;
    private Conocimiento conocimientoBaseDatos;
    private Conocimiento conocimientoTesting;
    private Conocimiento conocimientoIA;
    private Conocimiento conocimientoPrototipo;
    private Conocimiento conocimientoOtros;
    private Imagen imagen;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear usuario para asociar a los conocimientos
        usuario = Usuario.builder()
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack con 5 años de experiencia")
                .descripcion("Apasionado por la tecnología y el desarrollo de software.")
                .active(true)
                .build();

        // Crear imagen para los conocimientos
        imagen = Imagen.builder()
                .url("conocimiento.jpg")
                .alt("Logo de tecnología")
                .build();

        // Crear conocimiento de FRONTEND
        conocimientoFrontend = Conocimiento.builder()
                .nombre("Angular")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(Imagen.builder()
                        .url("angular.jpg")
                        .alt("Logo de Angular")
                        .build())
                .usuario(usuario)
                .build();

        // Crear conocimiento de BACKEND
        conocimientoBackend = Conocimiento.builder()
                .nombre("Spring Boot")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .imagen(Imagen.builder()
                        .url("springboot.jpg")
                        .alt("Logo de Spring Boot")
                        .build())
                .usuario(usuario)
                .build();

        // Crear conocimiento de BASE_DATOS
        conocimientoBaseDatos = Conocimiento.builder()
                .nombre("PostgreSQL")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BASE_DATOS)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Crear conocimiento de TESTING
        conocimientoTesting = Conocimiento.builder()
                .nombre("JUnit")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.TESTING)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Crear conocimiento de IA
        conocimientoIA = Conocimiento.builder()
                .nombre("Machine Learning")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.IA)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Crear conocimiento de PROTOTIPO
        conocimientoPrototipo = Conocimiento.builder()
                .nombre("Figma")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.PROTOTIPO)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Crear conocimiento de OTROS
        conocimientoOtros = Conocimiento.builder()
                .nombre("Docker")
                .nivel(Nivel.ALTO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .imagen(null)
                .usuario(usuario)
                .build();
    }

    // ==================== TESTS DE GUARDADO ====================

    @Test
    @DisplayName("save - Debería guardar un conocimiento correctamente")
    void save_ShouldSaveConocimientoCorrectly() {
        // Act
        Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimientoFrontend);

        // Assert
        assertNotNull(conocimientoGuardado, "El conocimiento guardado no debería ser nulo");
        assertNotNull(conocimientoGuardado.getId(), "El ID del conocimiento guardado no debería ser nulo");
        assertEquals("Angular", conocimientoGuardado.getNombre(), "El nombre debería coincidir");
        assertEquals(Nivel.AVANZADO, conocimientoGuardado.getNivel(), "El nivel debería coincidir");
        assertEquals(TipoConocimiento.FRONTEND, conocimientoGuardado.getTipoConocimiento(),
                "El tipo de conocimiento debería coincidir");
        assertNotNull(conocimientoGuardado.getImagen(), "La imagen no debería ser nula");
        assertEquals("angular.jpg", conocimientoGuardado.getImagen().getUrl(),
                "La URL de la imagen debería coincidir");
        assertNotNull(conocimientoGuardado.getUsuario(), "El usuario no debería ser nulo");

        // Verificar que se guardó en la base de datos
        Conocimiento conocimientoEncontrado = entityManager.find(Conocimiento.class,
                conocimientoGuardado.getId());
        assertNotNull(conocimientoEncontrado, "El conocimiento debería existir en la base de datos");
        assertEquals(conocimientoGuardado.getNombre(), conocimientoEncontrado.getNombre(),
                "El nombre debería coincidir");
    }

    @Test
    @DisplayName("save - Debería guardar un conocimiento sin imagen correctamente")
    void save_ShouldSaveConocimientoWithoutImagenCorrectly() {
        // Arrange
        Conocimiento conocimientoSinImagen = Conocimiento.builder()
                .nombre("HTML")
                .nivel(Nivel.PRINCIPIANTE_BASICO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Act
        Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimientoSinImagen);

        // Assert
        assertNotNull(conocimientoGuardado, "El conocimiento guardado no debería ser nulo");
        assertNotNull(conocimientoGuardado.getId(), "El ID del conocimiento guardado no debería ser nulo");
        assertNull(conocimientoGuardado.getImagen(), "La imagen debería ser nula");

        // Verificar que se guardó en la base de datos
        Conocimiento conocimientoEncontrado = entityManager.find(Conocimiento.class,
                conocimientoGuardado.getId());
        assertNotNull(conocimientoEncontrado, "El conocimiento debería existir en la base de datos");
        assertNull(conocimientoEncontrado.getImagen(), "La imagen debería ser nula en la BD");
    }

    @Test
    @DisplayName("save - Debería guardar un conocimiento con todos los niveles disponibles")
    void save_ShouldSaveConocimientoWithAllNiveles() {
        // Arrange - Probar todos los niveles
        Nivel[] niveles = Nivel.values();

        for (Nivel nivel : niveles) {
            // Arrange
            Conocimiento conocimiento = Conocimiento.builder()
                    .nombre("Conocimiento con nivel: " + nivel)
                    .nivel(nivel)
                    .tipoConocimiento(TipoConocimiento.OTROS)
                    .usuario(usuario)
                    .build();

            // Act
            Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimiento);

            // Assert
            assertNotNull(conocimientoGuardado, "El conocimiento no debería ser nulo para nivel: " + nivel);
            assertNotNull(conocimientoGuardado.getId(), "El ID no debería ser nulo para nivel: " + nivel);
            assertEquals(nivel, conocimientoGuardado.getNivel(),
                    "El nivel debería ser: " + nivel);
        }
    }

    @Test
    @DisplayName("save - Debería guardar un conocimiento con todos los tipos disponibles")
    void save_ShouldSaveConocimientoWithAllTiposConocimiento() {
        // Arrange - Probar todos los tipos de conocimiento
        TipoConocimiento[] tipos = TipoConocimiento.values();

        for (TipoConocimiento tipo : tipos) {
            // Arrange
            Conocimiento conocimiento = Conocimiento.builder()
                    .nombre("Conocimiento tipo: " + tipo)
                    .nivel(Nivel.INTERMEDIO)
                    .tipoConocimiento(tipo)
                    .usuario(usuario)
                    .build();

            // Act
            Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimiento);

            // Assert
            assertNotNull(conocimientoGuardado,
                    "El conocimiento no debería ser nulo para tipo: " + tipo);
            assertNotNull(conocimientoGuardado.getId(),
                    "El ID no debería ser nulo para tipo: " + tipo);
            assertEquals(tipo, conocimientoGuardado.getTipoConocimiento(),
                    "El tipo de conocimiento debería ser: " + tipo);
        }
    }

    // ==================== TESTS DE BÚSQUEDA ====================

    @Test
    @DisplayName("findById - Debería encontrar conocimiento por ID cuando existe")
    void findById_ShouldFindConocimiento_WhenIdExists() {
        // Arrange - Guardar conocimiento en la base de datos
        Conocimiento conocimientoGuardado = entityManager.persist(conocimientoFrontend);
        entityManager.flush();

        // Act
        Optional<Conocimiento> conocimientoEncontrado = conocimientoRepository
                .findById(conocimientoGuardado.getId());

        // Assert
        assertTrue(conocimientoEncontrado.isPresent(), "El conocimiento debería ser encontrado");
        assertEquals(conocimientoGuardado.getId(), conocimientoEncontrado.get().getId(),
                "El ID debería coincidir");
        assertEquals(conocimientoGuardado.getNombre(), conocimientoEncontrado.get().getNombre(),
                "El nombre debería coincidir");
        assertEquals(conocimientoGuardado.getTipoConocimiento(),
                conocimientoEncontrado.get().getTipoConocimiento(),
                "El tipo de conocimiento debería coincidir");
    }

    @Test
    @DisplayName("findById - No debería encontrar conocimiento cuando ID no existe")
    void findById_ShouldNotFindConocimiento_WhenIdDoesNotExist() {
        // Act
        Optional<Conocimiento> conocimientoEncontrado = conocimientoRepository.findById(999);

        // Assert
        assertFalse(conocimientoEncontrado.isPresent(), "El conocimiento no debería ser encontrado");
    }

    @Test
    @DisplayName("findAll - Debería encontrar todos los conocimientos")
    void findAll_ShouldFindAllConocimientos() {
        // Arrange - Guardar varios conocimientos en la base de datos
        entityManager.persist(conocimientoFrontend);
        entityManager.persist(conocimientoBackend);
        entityManager.persist(conocimientoBaseDatos);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientos = conocimientoRepository.findAll();

        // Assert
        assertNotNull(conocimientos, "La lista de conocimientos no debería ser nula");
        assertTrue(conocimientos.size() >= 3, "Debería haber al menos 3 conocimientos");

        // Verificar que los conocimientos guardados están en la lista
        boolean foundFrontend = conocimientos.stream()
                .anyMatch(c -> c.getNombre().equals("Angular"));
        boolean foundBackend = conocimientos.stream()
                .anyMatch(c -> c.getNombre().equals("Spring Boot"));
        boolean foundBD = conocimientos.stream()
                .anyMatch(c -> c.getNombre().equals("PostgreSQL"));

        assertTrue(foundFrontend, "El conocimiento de frontend debería estar en la lista");
        assertTrue(foundBackend, "El conocimiento de backend debería estar en la lista");
        assertTrue(foundBD, "El conocimiento de base de datos debería estar en la lista");
    }

    @Test
    @DisplayName("findAll - Debería retornar lista vacía cuando no hay conocimientos")
    void findAll_ShouldReturnEmptyList_WhenNoConocimientos() {
        // Act
        List<Conocimiento> conocimientos = conocimientoRepository.findAll();

        // Assert
        assertNotNull(conocimientos, "La lista no debería ser nula");
        assertTrue(conocimientos.isEmpty(), "La lista debería estar vacía");
    }

    // ==================== TESTS DE BÚSQUEDA POR TIPO ====================

    @Test
    @DisplayName("findByTipoConocimiento - Debería encontrar conocimientos por tipo FRONTEND")
    void findByTipoConocimiento_ShouldFindFrontendConocimientos() {
        // Arrange - Guardar conocimientos en la base de datos
        entityManager.persist(conocimientoFrontend);
        entityManager.persist(conocimientoBackend);
        entityManager.persist(conocimientoBaseDatos);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientosFrontend = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.FRONTEND);

        // Assert
        assertNotNull(conocimientosFrontend, "La lista no debería ser nula");
        assertFalse(conocimientosFrontend.isEmpty(), "La lista no debería estar vacía");
        assertTrue(conocimientosFrontend.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.FRONTEND),
                "Todos los conocimientos deberían ser de tipo FRONTEND");
        assertTrue(conocimientosFrontend.stream()
                        .anyMatch(c -> c.getNombre().equals("Angular")),
                "Debería incluir el conocimiento Angular");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería encontrar conocimientos por tipo BACKEND")
    void findByTipoConocimiento_ShouldFindBackendConocimientos() {
        // Arrange - Guardar conocimientos en la base de datos
        entityManager.persist(conocimientoFrontend);
        entityManager.persist(conocimientoBackend);
        entityManager.persist(conocimientoBaseDatos);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientosBackend = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.BACKEND);

        // Assert
        assertNotNull(conocimientosBackend, "La lista no debería ser nula");
        assertFalse(conocimientosBackend.isEmpty(), "La lista no debería estar vacía");
        assertTrue(conocimientosBackend.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.BACKEND),
                "Todos los conocimientos deberían ser de tipo BACKEND");
        assertTrue(conocimientosBackend.stream()
                        .anyMatch(c -> c.getNombre().equals("Spring Boot")),
                "Debería incluir el conocimiento Spring Boot");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería encontrar conocimientos por tipo BASE_DATOS")
    void findByTipoConocimiento_ShouldFindBaseDatosConocimientos() {
        // Arrange - Guardar conocimientos en la base de datos
        entityManager.persist(conocimientoFrontend);
        entityManager.persist(conocimientoBackend);
        entityManager.persist(conocimientoBaseDatos);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientosBD = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.BASE_DATOS);

        // Assert
        assertNotNull(conocimientosBD, "La lista no debería ser nula");
        assertFalse(conocimientosBD.isEmpty(), "La lista no debería estar vacía");
        assertTrue(conocimientosBD.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.BASE_DATOS),
                "Todos los conocimientos deberían ser de tipo BASE_DATOS");
        assertTrue(conocimientosBD.stream()
                        .anyMatch(c -> c.getNombre().equals("PostgreSQL")),
                "Debería incluir el conocimiento PostgreSQL");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería encontrar conocimientos por tipo TESTING")
    void findByTipoConocimiento_ShouldFindTestingConocimientos() {
        // Arrange - Guardar conocimientos en la base de datos
        entityManager.persist(conocimientoTesting);
        entityManager.persist(conocimientoFrontend);
        entityManager.persist(conocimientoBackend);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientosTesting = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.TESTING);

        // Assert
        assertNotNull(conocimientosTesting, "La lista no debería ser nula");
        assertFalse(conocimientosTesting.isEmpty(), "La lista no debería estar vacía");
        assertTrue(conocimientosTesting.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.TESTING),
                "Todos los conocimientos deberían ser de tipo TESTING");
        assertTrue(conocimientosTesting.stream()
                        .anyMatch(c -> c.getNombre().equals("JUnit")),
                "Debería incluir el conocimiento JUnit");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería encontrar conocimientos por tipo IA")
    void findByTipoConocimiento_ShouldFindIAConocimientos() {
        // Arrange - Guardar conocimientos en la base de datos
        entityManager.persist(conocimientoIA);
        entityManager.persist(conocimientoFrontend);
        entityManager.persist(conocimientoBackend);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientosIA = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.IA);

        // Assert
        assertNotNull(conocimientosIA, "La lista no debería ser nula");
        assertFalse(conocimientosIA.isEmpty(), "La lista no debería estar vacía");
        assertTrue(conocimientosIA.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.IA),
                "Todos los conocimientos deberían ser de tipo IA");
        assertTrue(conocimientosIA.stream()
                        .anyMatch(c -> c.getNombre().equals("Machine Learning")),
                "Debería incluir el conocimiento Machine Learning");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería encontrar conocimientos por tipo PROTOTIPO")
    void findByTipoConocimiento_ShouldFindPrototipoConocimientos() {
        // Arrange - Guardar conocimientos en la base de datos
        entityManager.persist(conocimientoPrototipo);
        entityManager.persist(conocimientoFrontend);
        entityManager.persist(conocimientoBackend);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientosPrototipo = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.PROTOTIPO);

        // Assert
        assertNotNull(conocimientosPrototipo, "La lista no debería ser nula");
        assertFalse(conocimientosPrototipo.isEmpty(), "La lista no debería estar vacía");
        assertTrue(conocimientosPrototipo.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.PROTOTIPO),
                "Todos los conocimientos deberían ser de tipo PROTOTIPO");
        assertTrue(conocimientosPrototipo.stream()
                        .anyMatch(c -> c.getNombre().equals("Figma")),
                "Debería incluir el conocimiento Figma");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería encontrar conocimientos por tipo OTROS")
    void findByTipoConocimiento_ShouldFindOtrosConocimientos() {
        // Arrange - Guardar conocimientos en la base de datos
        entityManager.persist(conocimientoOtros);
        entityManager.persist(conocimientoFrontend);
        entityManager.persist(conocimientoBackend);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientosOtros = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.OTROS);

        // Assert
        assertNotNull(conocimientosOtros, "La lista no debería ser nula");
        assertFalse(conocimientosOtros.isEmpty(), "La lista no debería estar vacía");
        assertTrue(conocimientosOtros.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.OTROS),
                "Todos los conocimientos deberían ser de tipo OTROS");
        assertTrue(conocimientosOtros.stream()
                        .anyMatch(c -> c.getNombre().equals("Docker")),
                "Debería incluir el conocimiento Docker");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería retornar lista vacía cuando no hay conocimientos del tipo")
    void findByTipoConocimiento_ShouldReturnEmptyList_WhenNoConocimientosOfType() {
        // Arrange - Guardar solo conocimientos FRONTEND
        entityManager.persist(conocimientoFrontend);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientosBackend = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.BACKEND);

        // Assert
        assertNotNull(conocimientosBackend, "La lista no debería ser nula");
        assertTrue(conocimientosBackend.isEmpty(), "La lista debería estar vacía");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería manejar correctamente múltiples conocimientos del mismo tipo")
    void findByTipoConocimiento_ShouldHandleMultipleConocimientosOfSameType() {
        // Arrange - Guardar múltiples conocimientos FRONTEND
        Conocimiento angular = Conocimiento.builder()
                .nombre("Angular")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .usuario(usuario)
                .build();

        Conocimiento react = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .usuario(usuario)
                .build();

        Conocimiento vue = Conocimiento.builder()
                .nombre("Vue.js")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .usuario(usuario)
                .build();

        entityManager.persist(angular);
        entityManager.persist(react);
        entityManager.persist(vue);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientosFrontend = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.FRONTEND);

        // Assert
        assertNotNull(conocimientosFrontend, "La lista no debería ser nula");
        assertEquals(3, conocimientosFrontend.size(), "Debería haber 3 conocimientos FRONTEND");
        assertTrue(conocimientosFrontend.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.FRONTEND),
                "Todos deberían ser de tipo FRONTEND");
    }

    // ==================== TESTS DE ACTUALIZACIÓN ====================

    @Test
    @DisplayName("save - Debería actualizar un conocimiento existente")
    void save_ShouldUpdateExistingConocimiento() {
        // Arrange - Guardar conocimiento en la base de datos
        Conocimiento conocimientoGuardado = entityManager.persist(conocimientoFrontend);
        entityManager.flush();

        // Act - Actualizar conocimiento
        conocimientoGuardado.setNombre("Angular 15");
        conocimientoGuardado.setNivel(Nivel.ALTO);
        conocimientoGuardado.setTipoConocimiento(TipoConocimiento.FRONTEND);
        Conocimiento conocimientoActualizado = conocimientoRepository.save(conocimientoGuardado);

        // Assert
        assertNotNull(conocimientoActualizado, "El conocimiento actualizado no debería ser nulo");
        assertEquals(conocimientoGuardado.getId(), conocimientoActualizado.getId(),
                "El ID debería ser el mismo");
        assertEquals("Angular 15", conocimientoActualizado.getNombre(),
                "El nombre debería estar actualizado");
        assertEquals(Nivel.ALTO, conocimientoActualizado.getNivel(),
                "El nivel debería estar actualizado");

        // Verificar en la base de datos
        Conocimiento conocimientoEncontrado = entityManager.find(Conocimiento.class,
                conocimientoGuardado.getId());
        assertNotNull(conocimientoEncontrado, "El conocimiento debería existir en la BD");
        assertEquals("Angular 15", conocimientoEncontrado.getNombre(),
                "El nombre en la BD debería estar actualizado");
        assertEquals(Nivel.ALTO, conocimientoEncontrado.getNivel(),
                "El nivel en la BD debería estar actualizado");
    }

    @Test
    @DisplayName("save - Debería actualizar la imagen de un conocimiento")
    void save_ShouldUpdateConocimientoImagen() {
        // Arrange - Guardar conocimiento en la base de datos
        Conocimiento conocimientoGuardado = entityManager.persist(conocimientoFrontend);
        entityManager.flush();

        // Crear nueva imagen
        Imagen nuevaImagen = Imagen.builder()
                .url("angular-nuevo.jpg")
                .alt("Logo de Angular actualizado")
                .build();

        // Act - Actualizar imagen
        conocimientoGuardado.setImagen(nuevaImagen);
        Conocimiento conocimientoActualizado = conocimientoRepository.save(conocimientoGuardado);

        // Assert
        assertNotNull(conocimientoActualizado.getImagen(), "La imagen no debería ser nula");
        assertEquals("angular-nuevo.jpg", conocimientoActualizado.getImagen().getUrl(),
                "La URL de la imagen debería estar actualizada");
        assertEquals("Logo de Angular actualizado", conocimientoActualizado.getImagen().getAlt(),
                "El alt de la imagen debería estar actualizado");

        // Verificar en la base de datos
        Conocimiento conocimientoEncontrado = entityManager.find(Conocimiento.class,
                conocimientoGuardado.getId());
        assertNotNull(conocimientoEncontrado.getImagen(), "La imagen en la BD no debería ser nula");
        assertEquals("angular-nuevo.jpg", conocimientoEncontrado.getImagen().getUrl(),
                "La URL en la BD debería estar actualizada");
    }

    @Test
    @DisplayName("save - Debería eliminar la imagen de un conocimiento al setearla a null")
    void save_ShouldRemoveConocimientoImagen_WhenSetToNull() {
        // Arrange - Guardar conocimiento con imagen en la base de datos
        Conocimiento conocimientoGuardado = entityManager.persist(conocimientoFrontend);
        entityManager.flush();
        Integer idImagen = conocimientoGuardado.getImagen().getId();

        // Verificar que la imagen existe
        assertNotNull(entityManager.find(Imagen.class, idImagen),
                "La imagen debería existir antes de eliminarla");

        // Act - Eliminar imagen
        conocimientoGuardado.setImagen(null);
        Conocimiento conocimientoActualizado = conocimientoRepository.save(conocimientoGuardado);

        // Assert
        assertNull(conocimientoActualizado.getImagen(), "La imagen debería ser nula");

        // Verificar en la base de datos - La imagen debería ser eliminada en cascada
        Imagen imagenEncontrada = entityManager.find(Imagen.class, idImagen);
        assertNull(imagenEncontrada, "La imagen debería ser eliminada en cascada");
    }

    @Test
    @DisplayName("save - Debería actualizar el usuario asociado a un conocimiento")
    void save_ShouldUpdateConocimientoUsuario() {
        // Arrange - Guardar conocimiento en la base de datos
        Conocimiento conocimientoGuardado = entityManager.persist(conocimientoFrontend);
        entityManager.flush();

        // Crear nuevo usuario
        Usuario nuevoUsuario = Usuario.builder()
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Administradora del sistema")
                .descripcion("Especialista en gestión")
                .active(true)
                .build();

        entityManager.persist(nuevoUsuario);
        entityManager.flush();

        // Act - Actualizar usuario
        conocimientoGuardado.setUsuario(nuevoUsuario);
        Conocimiento conocimientoActualizado = conocimientoRepository.save(conocimientoGuardado);

        // Assert
        assertNotNull(conocimientoActualizado.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(nuevoUsuario.getId(), conocimientoActualizado.getUsuario().getId(),
                "El ID del usuario debería ser el nuevo");
        assertEquals("María García", conocimientoActualizado.getUsuario().getNombre(),
                "El nombre del usuario debería ser el nuevo");
    }

    // ==================== TESTS DE ELIMINACIÓN ====================

    @Test
    @DisplayName("deleteById - Debería eliminar conocimiento por ID")
    void deleteById_ShouldDeleteConocimiento() {
        // Arrange - Guardar conocimiento en la base de datos
        Conocimiento conocimientoGuardado = entityManager.persist(conocimientoFrontend);
        entityManager.flush();
        Integer id = conocimientoGuardado.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Conocimiento.class, id),
                "El conocimiento debería existir antes de eliminar");

        // Act
        conocimientoRepository.deleteById(id);

        // Assert - Verificar que ya no existe
        Conocimiento conocimientoEliminado = entityManager.find(Conocimiento.class, id);
        assertNull(conocimientoEliminado, "El conocimiento no debería existir después de eliminar");
    }

    @Test
    @DisplayName("delete - Debería eliminar conocimiento por entidad")
    void delete_ShouldDeleteConocimientoEntity() {
        // Arrange - Guardar conocimiento en la base de datos
        Conocimiento conocimientoGuardado = entityManager.persist(conocimientoFrontend);
        entityManager.flush();
        Integer id = conocimientoGuardado.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Conocimiento.class, id),
                "El conocimiento debería existir antes de eliminar");

        // Act
        conocimientoRepository.delete(conocimientoGuardado);

        // Assert - Verificar que ya no existe
        Conocimiento conocimientoEliminado = entityManager.find(Conocimiento.class, id);
        assertNull(conocimientoEliminado, "El conocimiento no debería existir después de eliminar");
    }

    @Test
    @DisplayName("deleteById - No debería lanzar excepción al eliminar ID no existente")
    void deleteById_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            conocimientoRepository.deleteById(999);
        }, "No debería lanzar excepción al eliminar un ID que no existe");
    }

    @Test
    @DisplayName("delete - Debería eliminar conocimiento y su imagen en cascada")
    void delete_ShouldDeleteConocimientoAndCascadeImagen() {
        // Arrange - Guardar conocimiento con imagen en la base de datos
        Conocimiento conocimientoGuardado = entityManager.persist(conocimientoFrontend);
        entityManager.flush();
        Integer idConocimiento = conocimientoGuardado.getId();
        Integer idImagen = conocimientoGuardado.getImagen().getId();

        // Verificar que existen antes de eliminar
        assertNotNull(entityManager.find(Conocimiento.class, idConocimiento),
                "El conocimiento debería existir");
        assertNotNull(entityManager.find(Imagen.class, idImagen),
                "La imagen debería existir");

        // Act
        conocimientoRepository.delete(conocimientoGuardado);

        // Assert - Verificar que no existen
        assertNull(entityManager.find(Conocimiento.class, idConocimiento),
                "El conocimiento no debería existir");
        assertNull(entityManager.find(Imagen.class, idImagen),
                "La imagen no debería existir");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("save - Debería manejar nombre con caracteres especiales")
    void save_ShouldHandleNombreWithSpecialCharacters() {
        // Arrange
        Conocimiento conocimientoEspecial = Conocimiento.builder()
                .nombre("C# - .NET Core (Framework 8)")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .usuario(usuario)
                .build();

        // Act
        Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimientoEspecial);

        // Assert
        assertNotNull(conocimientoGuardado, "El conocimiento no debería ser nulo");
        assertNotNull(conocimientoGuardado.getId(), "El ID no debería ser nulo");
        assertEquals("C# - .NET Core (Framework 8)", conocimientoGuardado.getNombre(),
                "El nombre con caracteres especiales debería mantenerse");
    }

    @Test
    @DisplayName("save - Debería manejar nombre con acentos y ñ")
    void save_ShouldHandleNombreWithAccentsAndEnie() {
        // Arrange
        Conocimiento conocimientoAcentuado = Conocimiento.builder()
                .nombre("Java Spring Boot - Configuración avanzada")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .usuario(usuario)
                .build();

        // Act
        Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimientoAcentuado);

        // Assert
        assertNotNull(conocimientoGuardado, "El conocimiento no debería ser nulo");
        assertNotNull(conocimientoGuardado.getId(), "El ID no debería ser nulo");
        assertEquals("Java Spring Boot - Configuración avanzada",
                conocimientoGuardado.getNombre(),
                "El nombre con acentos debería mantenerse");
    }

    @Test
    @DisplayName("save - Debería manejar nombre en el límite de longitud (3 caracteres)")
    void save_ShouldHandleNombreAtMinimumLength() {
        // Arrange - Nombre exactamente de 3 caracteres
        Conocimiento conocimientoMinimo = Conocimiento.builder()
                .nombre("PHP")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .usuario(usuario)
                .build();

        // Act
        Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimientoMinimo);

        // Assert
        assertNotNull(conocimientoGuardado, "El conocimiento no debería ser nulo");
        assertNotNull(conocimientoGuardado.getId(), "El ID no debería ser nulo");
        assertEquals("PHP", conocimientoGuardado.getNombre(),
                "El nombre debería tener exactamente 3 caracteres");
        assertEquals(3, conocimientoGuardado.getNombre().length(),
                "El nombre debería tener 3 caracteres");
    }

    @Test
    @DisplayName("save - Debería manejar nombre en el límite de longitud (145 caracteres)")
    void save_ShouldHandleNombreAtMaximumLength() {
        // Arrange - Nombre exactamente de 145 caracteres
        String nombreMaximo = "A".repeat(145);
        Conocimiento conocimientoMaximo = Conocimiento.builder()
                .nombre(nombreMaximo)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .usuario(usuario)
                .build();

        // Act
        Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimientoMaximo);

        // Assert
        assertNotNull(conocimientoGuardado, "El conocimiento no debería ser nulo");
        assertNotNull(conocimientoGuardado.getId(), "El ID no debería ser nulo");
        assertEquals(nombreMaximo, conocimientoGuardado.getNombre(),
                "El nombre debería tener exactamente 145 caracteres");
        assertEquals(145, conocimientoGuardado.getNombre().length(),
                "El nombre debería tener 145 caracteres");
    }

    @Test
    @DisplayName("findById - Debería retornar Optional vacío con ID nulo")
    void findById_ShouldReturnEmptyOptional_WithNullId() {
        // Act
        Optional<Conocimiento> conocimientoEncontrado = conocimientoRepository.findById(null);

        // Assert
        assertFalse(conocimientoEncontrado.isPresent(),
                "Debería retornar Optional vacío con ID nulo");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería manejar null como parámetro")
    void findByTipoConocimiento_ShouldHandleNullParameter() {
        // Act
        List<Conocimiento> conocimientos = conocimientoRepository
                .findByTipoConocimiento(null);

        // Assert
        assertNotNull(conocimientos, "La lista no debería ser nula");
        assertTrue(conocimientos.isEmpty(), "La lista debería estar vacía");
    }

    // ==================== TESTS DE INTEGRACIÓN ====================

    @Test
    @DisplayName("save - Debería guardar conocimiento con cascada a imagen")
    void save_ShouldSaveConocimientoWithCascadeToImagen() {
        // Act
        Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimientoFrontend);

        // Assert
        assertNotNull(conocimientoGuardado, "El conocimiento guardado no debería ser nulo");
        assertNotNull(conocimientoGuardado.getId(), "El ID del conocimiento no debería ser nulo");

        // Verificar que la imagen se guardó en cascada
        Imagen imagenGuardada = conocimientoGuardado.getImagen();
        assertNotNull(imagenGuardada, "La imagen no debería ser nula");
        assertNotNull(imagenGuardada.getId(), "El ID de la imagen no debería ser nulo");
        assertEquals("angular.jpg", imagenGuardada.getUrl(),
                "La URL de la imagen debería coincidir");

        // Verificar en la base de datos
        Conocimiento conocimientoEncontrado = entityManager.find(Conocimiento.class,
                conocimientoGuardado.getId());
        assertNotNull(conocimientoEncontrado.getImagen(),
                "La imagen en la BD no debería ser nula");
        assertNotNull(conocimientoEncontrado.getImagen().getId(),
                "El ID de la imagen en la BD no debería ser nulo");
    }

    @Test
    @DisplayName("save - Debería guardar conocimiento asociado a usuario")
    void save_ShouldSaveConocimientoAssociatedToUsuario() {
        // Arrange - Guardar usuario primero
        Usuario usuarioGuardado = entityManager.persist(usuario);
        entityManager.flush();

        // Crear conocimiento asociado al usuario
        Conocimiento conocimiento = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .usuario(usuarioGuardado)
                .build();

        // Act
        Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimiento);

        // Assert
        assertNotNull(conocimientoGuardado, "El conocimiento no debería ser nulo");
        assertNotNull(conocimientoGuardado.getId(), "El ID no debería ser nulo");
        assertNotNull(conocimientoGuardado.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(usuarioGuardado.getId(), conocimientoGuardado.getUsuario().getId(),
                "El usuario debería ser el mismo");
        assertEquals(usuarioGuardado.getNombre(), conocimientoGuardado.getUsuario().getNombre(),
                "El nombre del usuario debería coincidir");
    }

    @Test
    @DisplayName("findByTipoConocimiento - Debería retornar conocimientos con sus relaciones cargadas")
    void findByTipoConocimiento_ShouldReturnConocimientosWithRelationships() {
        // Arrange - Guardar conocimiento con imagen y usuario
        entityManager.persist(usuario);
        entityManager.persist(conocimientoFrontend);
        entityManager.flush();

        // Act
        List<Conocimiento> conocimientos = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.FRONTEND);

        // Assert
        assertNotNull(conocimientos, "La lista no debería ser nula");
        assertFalse(conocimientos.isEmpty(), "La lista no debería estar vacía");

        Conocimiento conocimientoEncontrado = conocimientos.get(0);
        assertNotNull(conocimientoEncontrado.getImagen(),
                "La imagen debería estar cargada");
        assertNotNull(conocimientoEncontrado.getUsuario(),
                "El usuario debería estar cargado");
        assertEquals("angular.jpg", conocimientoEncontrado.getImagen().getUrl(),
                "La URL de la imagen debería ser correcta");
        assertEquals("Juan Pérez", conocimientoEncontrado.getUsuario().getNombre(),
                "El nombre del usuario debería ser correcto");
    }

    // ==================== TESTS NEGATIVOS ====================

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar conocimiento con nombre nulo")
    void save_ShouldThrowException_WhenNombreIsNull() {
        // Arrange - Conocimiento con nombre nulo
        Conocimiento conocimientoInvalido = Conocimiento.builder()
                .nombre(null)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoRepository.save(conocimientoInvalido);
        }, "Debería lanzar excepción al guardar conocimiento con nombre nulo");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar conocimiento con nivel nulo")
    void save_ShouldThrowException_WhenNivelIsNull() {
        // Arrange - Conocimiento con nivel nulo
        Conocimiento conocimientoInvalido = Conocimiento.builder()
                .nombre("Java")
                .nivel(null)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoRepository.save(conocimientoInvalido);
        }, "Debería lanzar excepción al guardar conocimiento con nivel nulo");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar conocimiento con tipoConocimiento nulo")
    void save_ShouldThrowException_WhenTipoConocimientoIsNull() {
        // Arrange - Conocimiento con tipoConocimiento nulo
        Conocimiento conocimientoInvalido = Conocimiento.builder()
                .nombre("Python")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(null)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoRepository.save(conocimientoInvalido);
        }, "Debería lanzar excepción al guardar conocimiento con tipoConocimiento nulo");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar conocimiento con nombre muy corto")
    void save_ShouldThrowException_WhenNombreIsTooShort() {
        // Arrange - Conocimiento con nombre muy corto (2 caracteres)
        Conocimiento conocimientoInvalido = Conocimiento.builder()
                .nombre("Ja")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.BACKEND)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoRepository.save(conocimientoInvalido);
        }, "Debería lanzar excepción al guardar conocimiento con nombre muy corto");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar conocimiento con nombre muy largo")
    void save_ShouldThrowException_WhenNombreIsTooLong() {
        // Arrange - Conocimiento con nombre muy largo (más de 145 caracteres)
        String nombreLargo = "A".repeat(150);
        Conocimiento conocimientoInvalido = Conocimiento.builder()
                .nombre(nombreLargo)
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.OTROS)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            conocimientoRepository.save(conocimientoInvalido);
        }, "Debería lanzar excepción al guardar conocimiento con nombre muy largo");
    }

    // ==================== TESTS DE MÚLTIPLES OPERACIONES ====================

    @Test
    @DisplayName("Debe manejar múltiples operaciones CRUD secuenciales")
    void shouldHandleMultipleCrudOperationsSequentially() {
        // 1. Guardar conocimiento
        Conocimiento conocimientoGuardado = conocimientoRepository.save(conocimientoFrontend);
        assertNotNull(conocimientoGuardado.getId(), "El ID no debería ser nulo después de guardar");

        // 2. Buscar conocimiento
        Optional<Conocimiento> conocimientoEncontrado = conocimientoRepository
                .findById(conocimientoGuardado.getId());
        assertTrue(conocimientoEncontrado.isPresent(), "El conocimiento debería ser encontrado");

        // 3. Actualizar conocimiento
        conocimientoEncontrado.get().setNombre("Angular 15");
        Conocimiento conocimientoActualizado = conocimientoRepository
                .save(conocimientoEncontrado.get());
        assertEquals("Angular 15", conocimientoActualizado.getNombre(),
                "El nombre debería estar actualizado");

        // 4. Verificar actualización
        Optional<Conocimiento> conocimientoVerificado = conocimientoRepository
                .findById(conocimientoGuardado.getId());
        assertTrue(conocimientoVerificado.isPresent(),
                "El conocimiento debería existir después de actualizar");
        assertEquals("Angular 15", conocimientoVerificado.get().getNombre(),
                "El nombre debería estar actualizado");

        // 5. Eliminar conocimiento
        conocimientoRepository.deleteById(conocimientoGuardado.getId());
        Optional<Conocimiento> conocimientoEliminado = conocimientoRepository
                .findById(conocimientoGuardado.getId());
        assertFalse(conocimientoEliminado.isPresent(),
                "El conocimiento no debería existir después de eliminar");
    }

    @Test
    @DisplayName("Debe manejar múltiples conocimientos del mismo tipo correctamente")
    void shouldHandleMultipleConocimientosOfSameTypeCorrectly() {
        // Arrange - Guardar múltiples conocimientos del mismo tipo
        Conocimiento angular = Conocimiento.builder()
                .nombre("Angular")
                .nivel(Nivel.AVANZADO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .usuario(usuario)
                .build();

        Conocimiento react = Conocimiento.builder()
                .nombre("React")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .usuario(usuario)
                .build();

        Conocimiento vue = Conocimiento.builder()
                .nombre("Vue.js")
                .nivel(Nivel.INTERMEDIO)
                .tipoConocimiento(TipoConocimiento.FRONTEND)
                .usuario(usuario)
                .build();

        conocimientoRepository.save(angular);
        conocimientoRepository.save(react);
        conocimientoRepository.save(vue);

        // Act
        List<Conocimiento> conocimientosFrontend = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.FRONTEND);

        // Assert
        assertNotNull(conocimientosFrontend, "La lista no debería ser nula");
        assertEquals(3, conocimientosFrontend.size(), "Debería haber 3 conocimientos FRONTEND");

        // Verificar que todos son del tipo correcto
        assertTrue(conocimientosFrontend.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.FRONTEND),
                "Todos deberían ser de tipo FRONTEND");

        // Verificar que todos tienen nombre
        assertTrue(conocimientosFrontend.stream()
                        .anyMatch(c -> c.getNombre().equals("Angular")),
                "Debería incluir Angular");
        assertTrue(conocimientosFrontend.stream()
                        .anyMatch(c -> c.getNombre().equals("React")),
                "Debería incluir React");
        assertTrue(conocimientosFrontend.stream()
                        .anyMatch(c -> c.getNombre().equals("Vue.js")),
                "Debería incluir Vue.js");
    }

    // ==================== TEST DE CONCURRENCIA SIMULADA ====================

    @Test
    @DisplayName("findByTipoConocimiento - Debería funcionar correctamente con múltiples tipos")
    void findByTipoConocimiento_ShouldWorkCorrectlyWithMultipleTypes() {
        // Arrange - Guardar conocimientos de diferentes tipos
        entityManager.persist(conocimientoFrontend);
        entityManager.persist(conocimientoBackend);
        entityManager.persist(conocimientoBaseDatos);
        entityManager.persist(conocimientoTesting);
        entityManager.persist(conocimientoIA);
        entityManager.flush();

        // Act - Buscar por cada tipo
        List<Conocimiento> frontendList = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.FRONTEND);
        List<Conocimiento> backendList = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.BACKEND);
        List<Conocimiento> bdList = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.BASE_DATOS);
        List<Conocimiento> testingList = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.TESTING);
        List<Conocimiento> iaList = conocimientoRepository
                .findByTipoConocimiento(TipoConocimiento.IA);

        // Assert
        // Verificar que cada lista contiene solo conocimientos del tipo correcto
        assertTrue(frontendList.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.FRONTEND),
                "Todos los conocimientos deberían ser FRONTEND");
        assertTrue(backendList.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.BACKEND),
                "Todos los conocimientos deberían ser BACKEND");
        assertTrue(bdList.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.BASE_DATOS),
                "Todos los conocimientos deberían ser BASE_DATOS");
        assertTrue(testingList.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.TESTING),
                "Todos los conocimientos deberían ser TESTING");
        assertTrue(iaList.stream()
                        .allMatch(c -> c.getTipoConocimiento() == TipoConocimiento.IA),
                "Todos los conocimientos deberían ser IA");

        // Verificar que cada lista tiene el conocimiento correcto
        assertTrue(frontendList.stream().anyMatch(c -> c.getNombre().equals("Angular")),
                "Frontend debería incluir Angular");
        assertTrue(backendList.stream().anyMatch(c -> c.getNombre().equals("Spring Boot")),
                "Backend debería incluir Spring Boot");
        assertTrue(bdList.stream().anyMatch(c -> c.getNombre().equals("PostgreSQL")),
                "Base de datos debería incluir PostgreSQL");
        assertTrue(testingList.stream().anyMatch(c -> c.getNombre().equals("JUnit")),
                "Testing debería incluir JUnit");
        assertTrue(iaList.stream().anyMatch(c -> c.getNombre().equals("Machine Learning")),
                "IA debería incluir Machine Learning");
    }
}