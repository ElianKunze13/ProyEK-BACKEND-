package com.example.demo.repository;

import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import com.example.demo.model.Experiencia;
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
 * Pruebas unitarias para la clase ExperienciaRepository
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Usa @DataJpaTest para pruebas de repositorio con JPA
 * Usa TestEntityManager para gestionar las entidades en el contexto de prueba
 */
@DataJpaTest
@ActiveProfiles("test")
class ExperienciaRepositoryTest {

    @Autowired
    private ExperienciaRepository experienciaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Experiencia experienciaValida;
    private Experiencia experienciaEnCurso;
    private Experiencia experienciaColaborativa;
    private Imagen imagen;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear usuario para asociar a las experiencias
        usuario = Usuario.builder()
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack con 5 años de experiencia")
                .descripcion("Apasionado por la tecnología y el desarrollo de software.")
                .active(true)
                .build();

        // Crear imagen para las experiencias
        imagen = Imagen.builder()
                .url("proyecto.jpg")
                .alt("Captura del proyecto")
                .build();

        // Crear experiencia válida
        experienciaValida = Experiencia.builder()
                .titulo("Sistema de Gestión de Usuarios")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 15))
                .fechaFinProyecto(LocalDate.of(2024, 6, 30))
                .descripcion("Desarrollo de API REST con Spring Boot y JWT para gestión de usuarios")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Crear experiencia en curso (con fechaFinProyecto nula)
        experienciaEnCurso = Experiencia.builder()
                .titulo("Proyecto en Desarrollo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(null) // Proyecto en curso
                .descripcion("Descripción del proyecto en desarrollo con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-en-curso")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Crear experiencia colaborativa
        experienciaColaborativa = Experiencia.builder()
                .titulo("App de E-commerce Colaborativa")
                .fechaInicioProyecto(LocalDate.of(2024, 2, 1))
                .fechaFinProyecto(LocalDate.of(2024, 8, 15))
                .descripcion("Desarrollo colaborativo de tienda online con carrito de compras")
                .link("https://github.com/usuario/ecommerce-colaborativo")
                .tipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_COLABORATIVO)
                .tecnologiaUsada(TecnologiaUsada.REACT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Persistir el usuario primero (necesario para la relación)
        entityManager.persist(usuario);
        entityManager.flush();
    }

    // ==================== TESTS DE GUARDADO ====================

    @Test
    @DisplayName("save - Debería guardar una experiencia correctamente")
    void save_ShouldSaveExperienciaCorrectly() {
        // Act
        Experiencia experienciaGuardada = experienciaRepository.save(experienciaValida);

        // Assert
        assertNotNull(experienciaGuardada, "La experiencia guardada no debería ser nula");
        assertNotNull(experienciaGuardada.getId(), "El ID de la experiencia guardada no debería ser nulo");
        assertEquals("Sistema de Gestión de Usuarios", experienciaGuardada.getTitulo(), "El título debería coincidir");
        assertEquals(LocalDate.of(2024, 1, 15), experienciaGuardada.getFechaInicioProyecto(),
                "La fecha de inicio debería coincidir");
        assertEquals(LocalDate.of(2024, 6, 30), experienciaGuardada.getFechaFinProyecto(),
                "La fecha de fin debería coincidir");
        assertEquals("Desarrollo de API REST con Spring Boot y JWT para gestión de usuarios",
                experienciaGuardada.getDescripcion(), "La descripción debería coincidir");
        assertEquals("https://github.com/usuario/proyecto", experienciaGuardada.getLink(),
                "El link debería coincidir");
        assertEquals(TipoExperiencia.PROYECTO_PERSONAL, experienciaGuardada.getTipoExperiencia(),
                "El tipo de experiencia debería ser PROYECTO_PERSONAL");
        assertEquals(TecnologiaUsada.SPRINGBOOT, experienciaGuardada.getTecnologiaUsada(),
                "La tecnología usada debería ser SPRINGBOOT");
        assertNotNull(experienciaGuardada.getImagen(), "La imagen no debería ser nula");
        assertNotNull(experienciaGuardada.getUsuario(), "El usuario no debería ser nulo");

        // Verificar que se guardó en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada, "La experiencia debería existir en la base de datos");
        assertEquals(experienciaGuardada.getTitulo(), experienciaEncontrada.getTitulo(),
                "El título debería coincidir en la BD");
    }

    @Test
    @DisplayName("save - Debería guardar una experiencia sin imagen correctamente")
    void save_ShouldSaveExperienciaWithoutImageCorrectly() {
        // Arrange - Experiencia sin imagen
        Experiencia experienciaSinImagen = Experiencia.builder()
                .titulo("Proyecto sin Imagen")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto sin imagen con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-sin-imagen")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Act
        Experiencia experienciaGuardada = experienciaRepository.save(experienciaSinImagen);

        // Assert
        assertNotNull(experienciaGuardada, "La experiencia guardada no debería ser nula");
        assertNotNull(experienciaGuardada.getId(), "El ID de la experiencia guardada no debería ser nulo");
        assertNull(experienciaGuardada.getImagen(), "La imagen debería ser nula");
        assertNotNull(experienciaGuardada.getUsuario(), "El usuario no debería ser nulo");

        // Verificar que se guardó en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada, "La experiencia debería existir en la BD");
        assertNull(experienciaEncontrada.getImagen(), "La imagen en la BD debería ser nula");
    }

    @Test
    @DisplayName("save - Debería guardar una experiencia con fechaFinProyecto nula (proyecto en curso)")
    void save_ShouldSaveExperienciaWithNullFechaFinProyecto() {
        // Act
        Experiencia experienciaGuardada = experienciaRepository.save(experienciaEnCurso);

        // Assert
        assertNotNull(experienciaGuardada, "La experiencia guardada no debería ser nula");
        assertNotNull(experienciaGuardada.getId(), "El ID de la experiencia guardada no debería ser nulo");
        assertNull(experienciaGuardada.getFechaFinProyecto(), "La fecha de fin debería ser nula para proyecto en curso");
        assertEquals("Proyecto en Desarrollo", experienciaGuardada.getTitulo(), "El título debería coincidir");

        // Verificar que se guardó en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada, "La experiencia debería existir en la BD");
        assertNull(experienciaEncontrada.getFechaFinProyecto(), "La fecha de fin en la BD debería ser nula");
    }

    @Test
    @DisplayName("save - Debería guardar una experiencia sin usuario asociado")
    void save_ShouldSaveExperienciaWithoutUsuario() {
        // Arrange - Experiencia sin usuario
        Experiencia experienciaSinUsuario = Experiencia.builder()
                .titulo("Proyecto Independiente")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción del proyecto independiente con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-independiente")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVASCRIPT)
                .imagen(imagen)
                .usuario(null)
                .build();

        // Act
        Experiencia experienciaGuardada = experienciaRepository.save(experienciaSinUsuario);

        // Assert
        assertNotNull(experienciaGuardada, "La experiencia guardada no debería ser nula");
        assertNotNull(experienciaGuardada.getId(), "El ID de la experiencia guardada no debería ser nulo");
        assertNull(experienciaGuardada.getUsuario(), "El usuario debería ser nulo");
        assertNotNull(experienciaGuardada.getImagen(), "La imagen no debería ser nula");

        // Verificar que se guardó en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada, "La experiencia debería existir en la BD");
        assertNull(experienciaEncontrada.getUsuario(), "El usuario en la BD debería ser nulo");
    }

    // ==================== TESTS DE BÚSQUEDA ====================

    @Test
    @DisplayName("findById - Debería encontrar experiencia por ID cuando existe")
    void findById_ShouldFindExperiencia_WhenIdExists() {
        // Arrange - Guardar experiencia en la base de datos
        Experiencia experienciaGuardada = entityManager.persist(experienciaValida);
        entityManager.flush();

        // Act
        Optional<Experiencia> experienciaEncontrada = experienciaRepository.findById(experienciaGuardada.getId());

        // Assert
        assertTrue(experienciaEncontrada.isPresent(), "La experiencia debería ser encontrada");
        assertEquals(experienciaGuardada.getId(), experienciaEncontrada.get().getId(), "El ID debería coincidir");
        assertEquals(experienciaGuardada.getTitulo(), experienciaEncontrada.get().getTitulo(),
                "El título debería coincidir");
        assertEquals(experienciaGuardada.getLink(), experienciaEncontrada.get().getLink(),
                "El link debería coincidir");
    }

    @Test
    @DisplayName("findById - No debería encontrar experiencia cuando ID no existe")
    void findById_ShouldNotFindExperiencia_WhenIdDoesNotExist() {
        // Act
        Optional<Experiencia> experienciaEncontrada = experienciaRepository.findById(999);

        // Assert
        assertFalse(experienciaEncontrada.isPresent(), "La experiencia no debería ser encontrada");
    }

    @Test
    @DisplayName("findById - Debería encontrar experiencia con fechaFinProyecto nula")
    void findById_ShouldFindExperienciaWithNullFechaFinProyecto() {
        // Arrange - Guardar experiencia en curso en la base de datos
        Experiencia experienciaGuardada = entityManager.persist(experienciaEnCurso);
        entityManager.flush();

        // Act
        Optional<Experiencia> experienciaEncontrada = experienciaRepository.findById(experienciaGuardada.getId());

        // Assert
        assertTrue(experienciaEncontrada.isPresent(), "La experiencia en curso debería ser encontrada");
        assertNull(experienciaEncontrada.get().getFechaFinProyecto(),
                "La fecha de fin debería ser nula para proyecto en curso");
        assertEquals(experienciaGuardada.getTitulo(), experienciaEncontrada.get().getTitulo(),
                "El título debería coincidir");
    }

    @Test
    @DisplayName("findAll - Debería encontrar todas las experiencias")
    void findAll_ShouldFindAllExperiencias() {
        // Arrange - Guardar varias experiencias en la base de datos
        entityManager.persist(experienciaValida);
        entityManager.persist(experienciaEnCurso);
        entityManager.persist(experienciaColaborativa);
        entityManager.flush();

        // Act
        List<Experiencia> experiencias = experienciaRepository.findAll();

        // Assert
        assertNotNull(experiencias, "La lista de experiencias no debería ser nula");
        assertTrue(experiencias.size() >= 3, "Debería haber al menos 3 experiencias");

        // Verificar que las experiencias guardadas están en la lista
        boolean foundProyectoPersonal = experiencias.stream()
                .anyMatch(e -> e.getTitulo().equals("Sistema de Gestión de Usuarios"));
        boolean foundProyectoEnCurso = experiencias.stream()
                .anyMatch(e -> e.getTitulo().equals("Proyecto en Desarrollo"));
        boolean foundProyectoColaborativo = experiencias.stream()
                .anyMatch(e -> e.getTitulo().equals("App de E-commerce Colaborativa"));

        assertTrue(foundProyectoPersonal, "La experiencia personal debería estar en la lista");
        assertTrue(foundProyectoEnCurso, "La experiencia en curso debería estar en la lista");
        assertTrue(foundProyectoColaborativo, "La experiencia colaborativa debería estar en la lista");
    }

    @Test
    @DisplayName("findAll - Debería retornar lista vacía cuando no hay experiencias")
    void findAll_ShouldReturnEmptyList_WhenNoExperienciasExist() {
        // Act
        List<Experiencia> experiencias = experienciaRepository.findAll();

        // Assert
        assertNotNull(experiencias, "La lista no debería ser nula");
        assertTrue(experiencias.isEmpty(), "La lista debería estar vacía cuando no hay experiencias");
    }

    // ==================== TESTS DE ACTUALIZACIÓN ====================

    @Test
    @DisplayName("save - Debería actualizar una experiencia existente")
    void save_ShouldUpdateExistingExperiencia() {
        // Arrange - Guardar experiencia en la base de datos
        Experiencia experienciaGuardada = entityManager.persist(experienciaValida);
        entityManager.flush();

        // Act - Actualizar experiencia
        experienciaGuardada.setTitulo("Sistema de Gestión de Usuarios Actualizado");
        experienciaGuardada.setDescripcion("Descripción actualizada con más de 5 caracteres para prueba");
        experienciaGuardada.setLink("https://github.com/usuario/proyecto-actualizado");
        experienciaGuardada.setTipoExperiencia(TipoExperiencia.TRABAJO_LABORAL_FREELANCE);
        experienciaGuardada.setTecnologiaUsada(TecnologiaUsada.ANGULAR);

        Experiencia experienciaActualizada = experienciaRepository.save(experienciaGuardada);

        // Assert
        assertNotNull(experienciaActualizada, "La experiencia actualizada no debería ser nula");
        assertEquals(experienciaGuardada.getId(), experienciaActualizada.getId(), "El ID debería ser el mismo");
        assertEquals("Sistema de Gestión de Usuarios Actualizado", experienciaActualizada.getTitulo(),
                "El título debería estar actualizado");
        assertEquals("Descripción actualizada con más de 5 caracteres para prueba",
                experienciaActualizada.getDescripcion(), "La descripción debería estar actualizada");
        assertEquals("https://github.com/usuario/proyecto-actualizado", experienciaActualizada.getLink(),
                "El link debería estar actualizado");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE, experienciaActualizada.getTipoExperiencia(),
                "El tipo de experiencia debería estar actualizado");
        assertEquals(TecnologiaUsada.ANGULAR, experienciaActualizada.getTecnologiaUsada(),
                "La tecnología usada debería estar actualizada");

        // Verificar en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada, "La experiencia debería existir en la BD");
        assertEquals("Sistema de Gestión de Usuarios Actualizado", experienciaEncontrada.getTitulo(),
                "El título en la BD debería estar actualizado");
        assertEquals(TipoExperiencia.TRABAJO_LABORAL_FREELANCE, experienciaEncontrada.getTipoExperiencia(),
                "El tipo de experiencia en la BD debería estar actualizado");
    }

    @Test
    @DisplayName("save - Debería actualizar la fecha de fin de una experiencia en curso")
    void save_ShouldUpdateFechaFinProyectoOfExperienciaEnCurso() {
        // Arrange - Guardar experiencia en curso en la base de datos
        Experiencia experienciaGuardada = entityManager.persist(experienciaEnCurso);
        entityManager.flush();

        // Verificar que la fecha de fin es nula inicialmente
        assertNull(experienciaGuardada.getFechaFinProyecto(), "La fecha de fin debería ser nula inicialmente");

        // Act - Actualizar fecha de fin
        LocalDate nuevaFechaFin = LocalDate.of(2024, 12, 31);
        experienciaGuardada.setFechaFinProyecto(nuevaFechaFin);
        Experiencia experienciaActualizada = experienciaRepository.save(experienciaGuardada);

        // Assert
        assertNotNull(experienciaActualizada, "La experiencia actualizada no debería ser nula");
        assertNotNull(experienciaActualizada.getFechaFinProyecto(), "La fecha de fin no debería ser nula");
        assertEquals(nuevaFechaFin, experienciaActualizada.getFechaFinProyecto(),
                "La fecha de fin debería estar actualizada");

        // Verificar en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada.getFechaFinProyecto(), "La fecha de fin en la BD no debería ser nula");
        assertEquals(nuevaFechaFin, experienciaEncontrada.getFechaFinProyecto(),
                "La fecha de fin en la BD debería estar actualizada");
    }

    @Test
    @DisplayName("save - Debería actualizar la imagen de una experiencia")
    void save_ShouldUpdateImagenOfExperiencia() {
        // Arrange - Guardar experiencia en la base de datos
        Experiencia experienciaGuardada = entityManager.persist(experienciaValida);
        entityManager.flush();

        // Crear nueva imagen
        Imagen nuevaImagen = Imagen.builder()
                .url("nuevo-proyecto.jpg")
                .alt("Nueva captura del proyecto")
                .build();

        // Act - Actualizar imagen
        experienciaGuardada.setImagen(nuevaImagen);
        Experiencia experienciaActualizada = experienciaRepository.save(experienciaGuardada);

        // Assert
        assertNotNull(experienciaActualizada.getImagen(), "La imagen no debería ser nula");
        assertEquals("nuevo-proyecto.jpg", experienciaActualizada.getImagen().getUrl(),
                "La URL de la imagen debería estar actualizada");
        assertEquals("Nueva captura del proyecto", experienciaActualizada.getImagen().getAlt(),
                "El alt de la imagen debería estar actualizado");

        // Verificar en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada.getImagen(), "La imagen en la BD no debería ser nula");
        assertEquals("nuevo-proyecto.jpg", experienciaEncontrada.getImagen().getUrl(),
                "La URL de la imagen en la BD debería estar actualizada");
    }

    @Test
    @DisplayName("save - Debería eliminar la imagen de una experiencia (establecer a null)")
    void save_ShouldRemoveImagenFromExperiencia() {
        // Arrange - Guardar experiencia en la base de datos
        Experiencia experienciaGuardada = entityManager.persist(experienciaValida);
        entityManager.flush();

        // Verificar que la imagen existe inicialmente
        assertNotNull(experienciaGuardada.getImagen(), "La imagen debería existir inicialmente");

        // Act - Eliminar imagen
        experienciaGuardada.setImagen(null);
        Experiencia experienciaActualizada = experienciaRepository.save(experienciaGuardada);

        // Assert
        assertNull(experienciaActualizada.getImagen(), "La imagen debería ser nula");

        // Verificar en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNull(experienciaEncontrada.getImagen(), "La imagen en la BD debería ser nula");
    }

    // ==================== TESTS DE ELIMINACIÓN ====================

    @Test
    @DisplayName("deleteById - Debería eliminar experiencia por ID")
    void deleteById_ShouldDeleteExperiencia() {
        // Arrange - Guardar experiencia en la base de datos
        Experiencia experienciaGuardada = entityManager.persist(experienciaValida);
        entityManager.flush();
        Integer id = experienciaGuardada.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Experiencia.class, id), "La experiencia debería existir antes de eliminar");

        // Act
        experienciaRepository.deleteById(id);

        // Assert - Verificar que ya no existe
        Experiencia experienciaEliminada = entityManager.find(Experiencia.class, id);
        assertNull(experienciaEliminada, "La experiencia no debería existir después de eliminar");
    }

    @Test
    @DisplayName("delete - Debería eliminar experiencia por entidad")
    void delete_ShouldDeleteExperienciaEntity() {
        // Arrange - Guardar experiencia en la base de datos
        Experiencia experienciaGuardada = entityManager.persist(experienciaValida);
        entityManager.flush();
        Integer id = experienciaGuardada.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Experiencia.class, id), "La experiencia debería existir antes de eliminar");

        // Act
        experienciaRepository.delete(experienciaGuardada);

        // Assert - Verificar que ya no existe
        Experiencia experienciaEliminada = entityManager.find(Experiencia.class, id);
        assertNull(experienciaEliminada, "La experiencia no debería existir después de eliminar");
    }

    @Test
    @DisplayName("deleteById - No debería lanzar excepción al eliminar ID no existente")
    void deleteById_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            experienciaRepository.deleteById(999);
        }, "No debería lanzar excepción al eliminar un ID que no existe");
    }

    @Test
    @DisplayName("delete - Debería eliminar experiencia en cascada con su imagen")
    void delete_ShouldDeleteExperienciaAndCascadeImage() {
        // Arrange - Guardar experiencia en la base de datos
        Experiencia experienciaGuardada = entityManager.persist(experienciaValida);
        entityManager.flush();
        Integer idExperiencia = experienciaGuardada.getId();
        Integer idImagen = experienciaGuardada.getImagen().getId();

        // Verificar que existen antes de eliminar
        assertNotNull(entityManager.find(Experiencia.class, idExperiencia), "La experiencia debería existir");
        assertNotNull(entityManager.find(Imagen.class, idImagen), "La imagen debería existir");

        // Act
        experienciaRepository.delete(experienciaGuardada);

        // Assert - Verificar que no existen
        assertNull(entityManager.find(Experiencia.class, idExperiencia), "La experiencia no debería existir");
        assertNull(entityManager.find(Imagen.class, idImagen), "La imagen no debería existir");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("save - Debería manejar experiencia con títulos largos")
    void save_ShouldHandleExperienciaWithLongTitulo() {
        // Arrange
        String tituloLargo = "A".repeat(145);
        Experiencia experienciaTituloLargo = Experiencia.builder()
                .titulo(tituloLargo)
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto-largo")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act
        Experiencia experienciaGuardada = experienciaRepository.save(experienciaTituloLargo);

        // Assert
        assertNotNull(experienciaGuardada, "La experiencia guardada no debería ser nula");
        assertNotNull(experienciaGuardada.getId(), "El ID no debería ser nulo");
        assertEquals(tituloLargo, experienciaGuardada.getTitulo(), "El título largo debería mantenerse");
        assertEquals(145, experienciaGuardada.getTitulo().length(), "El título debería tener 145 caracteres");

        // Verificar en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada, "La experiencia debería existir en la BD");
        assertEquals(tituloLargo, experienciaEncontrada.getTitulo(), "El título largo debería mantenerse en la BD");
    }

    @Test
    @DisplayName("save - Debería manejar experiencia con descripciones largas")
    void save_ShouldHandleExperienciaWithLongDescripcion() {
        // Arrange
        String descripcionLarga = "A".repeat(300);
        Experiencia experienciaDescripcionLarga = Experiencia.builder()
                .titulo("Proyecto con descripción larga")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion(descripcionLarga)
                .link("https://github.com/usuario/proyecto-descripcion-larga")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act
        Experiencia experienciaGuardada = experienciaRepository.save(experienciaDescripcionLarga);

        // Assert
        assertNotNull(experienciaGuardada, "La experiencia guardada no debería ser nula");
        assertNotNull(experienciaGuardada.getId(), "El ID no debería ser nulo");
        assertEquals(descripcionLarga, experienciaGuardada.getDescripcion(),
                "La descripción larga debería mantenerse");
        assertEquals(300, experienciaGuardada.getDescripcion().length(),
                "La descripción debería tener 300 caracteres");

        // Verificar en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada, "La experiencia debería existir en la BD");
        assertEquals(descripcionLarga, experienciaEncontrada.getDescripcion(),
                "La descripción larga debería mantenerse en la BD");
    }

    @Test
    @DisplayName("save - Debería manejar experiencia con links largos")
    void save_ShouldHandleExperienciaWithLongLink() {
        // Arrange
        String linkLargo = "https://github.com/" + "a".repeat(200) + "/proyecto";
        Experiencia experienciaLinkLargo = Experiencia.builder()
                .titulo("Proyecto con link largo")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link(linkLargo)
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act
        Experiencia experienciaGuardada = experienciaRepository.save(experienciaLinkLargo);

        // Assert
        assertNotNull(experienciaGuardada, "La experiencia guardada no debería ser nula");
        assertNotNull(experienciaGuardada.getId(), "El ID no debería ser nulo");
        assertEquals(linkLargo, experienciaGuardada.getLink(), "El link largo debería mantenerse");

        // Verificar en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada, "La experiencia debería existir en la BD");
        assertEquals(linkLargo, experienciaEncontrada.getLink(), "El link largo debería mantenerse en la BD");
    }

    @Test
    @DisplayName("findAll - Debería retornar experiencias con diferentes tipos de experiencia")
    void findAll_ShouldReturnExperienciasWithDifferentTiposExperiencia() {
        // Arrange - Guardar experiencias con diferentes tipos
        Experiencia experienciaPersonal = Experiencia.builder()
                .titulo("Proyecto Personal")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción de proyecto personal con más de 5 caracteres")
                .link("https://github.com/usuario/personal")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.JAVA)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        Experiencia experienciaOpenSource = Experiencia.builder()
                .titulo("Contribución Open Source")
                .fechaInicioProyecto(LocalDate.of(2024, 2, 1))
                .fechaFinProyecto(LocalDate.of(2024, 3, 15))
                .descripcion("Descripción de contribución open source con más de 5 caracteres")
                .link("https://github.com/usuario/open-source")
                .tipoExperiencia(TipoExperiencia.APORTE_CODIGO_ABIERTO)
                .tecnologiaUsada(TecnologiaUsada.PYTHON)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        Experiencia experienciaPractica = Experiencia.builder()
                .titulo("Práctica Profesional")
                .fechaInicioProyecto(LocalDate.of(2024, 3, 1))
                .fechaFinProyecto(LocalDate.of(2024, 5, 30))
                .descripcion("Descripción de práctica profesional con más de 5 caracteres")
                .link("https://github.com/usuario/practica")
                .tipoExperiencia(TipoExperiencia.PRACTICA_PROFESIONAL)
                .tecnologiaUsada(TecnologiaUsada.REACT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        entityManager.persist(experienciaPersonal);
        entityManager.persist(experienciaOpenSource);
        entityManager.persist(experienciaPractica);
        entityManager.flush();

        // Act
        List<Experiencia> experiencias = experienciaRepository.findAll();

        // Assert
        assertNotNull(experiencias, "La lista no debería ser nula");
        assertTrue(experiencias.size() >= 3, "Debería haber al menos 3 experiencias");

        // Verificar tipos
        boolean foundPersonal = experiencias.stream()
                .anyMatch(e -> e.getTipoExperiencia() == TipoExperiencia.PROYECTO_PERSONAL);
        boolean foundOpenSource = experiencias.stream()
                .anyMatch(e -> e.getTipoExperiencia() == TipoExperiencia.APORTE_CODIGO_ABIERTO);
        boolean foundPractica = experiencias.stream()
                .anyMatch(e -> e.getTipoExperiencia() == TipoExperiencia.PRACTICA_PROFESIONAL);

        assertTrue(foundPersonal, "Debería haber experiencia de tipo PROYECTO_PERSONAL");
        assertTrue(foundOpenSource, "Debería haber experiencia de tipo APORTE_CODIGO_ABIERTO");
        assertTrue(foundPractica, "Debería haber experiencia de tipo PRACTICA_PROFESIONAL");
    }

    @Test
    @DisplayName("findAll - Debería retornar experiencias con diferentes tecnologías")
    void findAll_ShouldReturnExperienciasWithDifferentTecnologias() {
        // Arrange - Guardar experiencias con diferentes tecnologías
        TecnologiaUsada[] tecnologias = {TecnologiaUsada.JAVA, TecnologiaUsada.PYTHON, TecnologiaUsada.REACT};

        for (int i = 0; i < tecnologias.length; i++) {
            Experiencia experiencia = Experiencia.builder()
                    .titulo("Proyecto con " + tecnologias[i].name())
                    .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                    .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                    .descripcion("Descripción para tecnología " + tecnologias[i].name() + " con más de 5 caracteres")
                    .link("https://github.com/usuario/proyecto-" + i)
                    .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                    .tecnologiaUsada(tecnologias[i])
                    .imagen(imagen)
                    .usuario(usuario)
                    .build();
            entityManager.persist(experiencia);
        }
        entityManager.flush();

        // Act
        List<Experiencia> experiencias = experienciaRepository.findAll();

        // Assert
        assertNotNull(experiencias, "La lista no debería ser nula");

        // Verificar que existen experiencias con cada tecnología
        for (TecnologiaUsada tecnologia : tecnologias) {
            boolean found = experiencias.stream()
                    .anyMatch(e -> e.getTecnologiaUsada() == tecnologia);
            assertTrue(found, "Debería haber experiencia con tecnología: " + tecnologia);
        }
    }

    // ==================== TESTS NEGATIVOS ====================

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar experiencia con campos obligatorios nulos")
    void save_ShouldThrowException_WhenRequiredFieldsAreNull() {
        // Arrange - Experiencia con campos obligatorios nulos
        Experiencia experienciaInvalida = Experiencia.builder()
                .titulo(null)
                .fechaInicioProyecto(null)
                .descripcion(null)
                .link(null)
                .tipoExperiencia(null)
                .tecnologiaUsada(null)
                .imagen(null)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaRepository.save(experienciaInvalida);
        }, "Debería lanzar excepción al guardar experiencia con campos obligatorios nulos");
    }

    @Test
    @DisplayName("findById - Debería retornar Optional vacío con ID nulo")
    void findById_ShouldReturnEmptyOptional_WithNullId() {
        // Act
        Optional<Experiencia> experienciaEncontrada = experienciaRepository.findById(null);

        // Assert
        assertFalse(experienciaEncontrada.isPresent(), "Debería retornar Optional vacío con ID nulo");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar experiencia con título vacío")
    void save_ShouldThrowException_WhenTituloIsEmpty() {
        // Arrange - Experiencia con título vacío
        Experiencia experienciaSinTitulo = Experiencia.builder()
                .titulo("")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaRepository.save(experienciaSinTitulo);
        }, "Debería lanzar excepción al guardar experiencia con título vacío");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar experiencia con descripción vacía")
    void save_ShouldThrowException_WhenDescripcionIsEmpty() {
        // Arrange - Experiencia con descripción vacía
        Experiencia experienciaSinDescripcion = Experiencia.builder()
                .titulo("Proyecto Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("")
                .link("https://github.com/usuario/proyecto")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaRepository.save(experienciaSinDescripcion);
        }, "Debería lanzar excepción al guardar experiencia con descripción vacía");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar experiencia con link vacío")
    void save_ShouldThrowException_WhenLinkIsEmpty() {
        // Arrange - Experiencia con link vacío
        Experiencia experienciaSinLink = Experiencia.builder()
                .titulo("Proyecto Válido")
                .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            experienciaRepository.save(experienciaSinLink);
        }, "Debería lanzar excepción al guardar experiencia con link vacío");
    }

    // ==================== TESTS DE MÚLTIPLES OPERACIONES ====================

    @Test
    @DisplayName("Debe manejar múltiples operaciones CRUD secuenciales")
    void shouldHandleMultipleCrudOperationsSequentially() {
        // 1. Guardar experiencia
        Experiencia experienciaGuardada = experienciaRepository.save(experienciaValida);
        assertNotNull(experienciaGuardada.getId(), "El ID no debería ser nulo después de guardar");

        // 2. Buscar experiencia
        Optional<Experiencia> experienciaEncontrada = experienciaRepository.findById(experienciaGuardada.getId());
        assertTrue(experienciaEncontrada.isPresent(), "La experiencia debería ser encontrada");

        // 3. Actualizar experiencia
        experienciaEncontrada.get().setTitulo("Título Actualizado");
        experienciaEncontrada.get().setDescripcion("Descripción actualizada con más de 5 caracteres");
        Experiencia experienciaActualizada = experienciaRepository.save(experienciaEncontrada.get());
        assertEquals("Título Actualizado", experienciaActualizada.getTitulo(), "El título debería estar actualizado");

        // 4. Verificar actualización
        Optional<Experiencia> experienciaVerificada = experienciaRepository.findById(experienciaGuardada.getId());
        assertTrue(experienciaVerificada.isPresent(), "La experiencia debería existir después de actualizar");
        assertEquals("Título Actualizado", experienciaVerificada.get().getTitulo(),
                "El título debería estar actualizado");

        // 5. Eliminar experiencia
        experienciaRepository.deleteById(experienciaGuardada.getId());
        Optional<Experiencia> experienciaEliminada = experienciaRepository.findById(experienciaGuardada.getId());
        assertFalse(experienciaEliminada.isPresent(), "La experiencia no debería existir después de eliminar");
    }

    // ==================== TESTS DE CONSISTENCIA ====================

    @Test
    @DisplayName("findAll - Debería retornar experiencias con sus relaciones cargadas correctamente")
    void findAll_ShouldReturnExperienciasWithRelationshipsLoadedCorrectly() {
        // Arrange - Guardar experiencia con todas las relaciones
        Experiencia experienciaGuardada = entityManager.persist(experienciaValida);
        entityManager.flush();

        // Act
        List<Experiencia> experiencias = experienciaRepository.findAll();

        // Assert
        assertNotNull(experiencias, "La lista no debería ser nula");
        assertFalse(experiencias.isEmpty(), "La lista no debería estar vacía");

        // Verificar que la experiencia guardada está en la lista y tiene sus relaciones
        Experiencia experienciaEncontrada = experiencias.stream()
                .filter(e -> e.getId().equals(experienciaGuardada.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(experienciaEncontrada, "La experiencia debería ser encontrada en la lista");
        assertNotNull(experienciaEncontrada.getImagen(), "La imagen no debería ser nula");
        assertEquals(imagen.getUrl(), experienciaEncontrada.getImagen().getUrl(),
                "La URL de la imagen debería coincidir");
        assertNotNull(experienciaEncontrada.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(usuario.getNombre(), experienciaEncontrada.getUsuario().getNombre(),
                "El nombre del usuario debería coincidir");
    }

    @Test
    @DisplayName("save - Debería preservar los valores de los enums correctamente")
    void save_ShouldPreserveEnumValuesCorrectly() {
        // Arrange - Experiencia con diferentes enums
        TipoExperiencia[] tipos = TipoExperiencia.values();
        TecnologiaUsada[] tecnologias = TecnologiaUsada.values();

        for (TipoExperiencia tipo : tipos) {
            for (TecnologiaUsada tecnologia : tecnologias) {
                // Act - Guardar experiencia con cada combinación
                Experiencia experiencia = Experiencia.builder()
                        .titulo("Enum Test - " + tipo.name() + " - " + tecnologia.name())
                        .fechaInicioProyecto(LocalDate.of(2024, 1, 1))
                        .fechaFinProyecto(LocalDate.of(2024, 6, 1))
                        .descripcion("Descripción para enum test con más de 5 caracteres")
                        .link("https://github.com/usuario/enum-test")
                        .tipoExperiencia(tipo)
                        .tecnologiaUsada(tecnologia)
                        .imagen(imagen)
                        .usuario(usuario)
                        .build();

                Experiencia experienciaGuardada = experienciaRepository.save(experiencia);

                // Assert
                assertNotNull(experienciaGuardada, "La experiencia guardada no debería ser nula");
                assertEquals(tipo, experienciaGuardada.getTipoExperiencia(),
                        "El tipo de experiencia debería ser " + tipo);
                assertEquals(tecnologia, experienciaGuardada.getTecnologiaUsada(),
                        "La tecnología usada debería ser " + tecnologia);
            }
        }
    }

    @Test
    @DisplayName("save - Debería manejar fechas correctamente en el límite de tiempo")
    void save_ShouldHandleDatesAtTimeBoundaries() {
        // Arrange - Experiencia con fechas en el límite
        LocalDate fechaInicio = LocalDate.of(2000, 1, 1);
        LocalDate fechaFin = LocalDate.of(2099, 12, 31);

        Experiencia experienciaLimites = Experiencia.builder()
                .titulo("Proyecto con fechas límite")
                .fechaInicioProyecto(fechaInicio)
                .fechaFinProyecto(fechaFin)
                .descripcion("Descripción válida con más de 5 caracteres")
                .link("https://github.com/usuario/fechas-limite")
                .tipoExperiencia(TipoExperiencia.PROYECTO_PERSONAL)
                .tecnologiaUsada(TecnologiaUsada.SPRINGBOOT)
                .imagen(imagen)
                .usuario(usuario)
                .build();

        // Act
        Experiencia experienciaGuardada = experienciaRepository.save(experienciaLimites);

        // Assert
        assertNotNull(experienciaGuardada, "La experiencia guardada no debería ser nula");
        assertNotNull(experienciaGuardada.getId(), "El ID no debería ser nulo");
        assertEquals(fechaInicio, experienciaGuardada.getFechaInicioProyecto(),
                "La fecha de inicio debería ser la misma");
        assertEquals(fechaFin, experienciaGuardada.getFechaFinProyecto(),
                "La fecha de fin debería ser la misma");

        // Verificar en la base de datos
        Experiencia experienciaEncontrada = entityManager.find(Experiencia.class, experienciaGuardada.getId());
        assertNotNull(experienciaEncontrada, "La experiencia debería existir en la BD");
        assertEquals(fechaInicio, experienciaEncontrada.getFechaInicioProyecto(),
                "La fecha de inicio en la BD debería ser la misma");
        assertEquals(fechaFin, experienciaEncontrada.getFechaFinProyecto(),
                "La fecha de fin en la BD debería ser la misma");
    }
}