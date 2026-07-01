package com.example.demo.repository;

import com.example.demo.model.Mensaje;
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
 * Pruebas unitarias para la clase MensajeRepository
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Usa @DataJpaTest para pruebas de repositorio con JPA
 * Usa TestEntityManager para gestionar las entidades en el contexto de prueba
 */
@DataJpaTest
@ActiveProfiles("test")
class MensajeRepositoryTest {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Mensaje mensajeValido;
    private Mensaje mensajeSinMensaje;
    private Mensaje mensajeConFechaManual;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear mensaje válido
        mensajeValido = Mensaje.builder()
                .nombreUsuario("Juan Pérez")
                .email("juan.perez@email.com")
                .mensaje("Este es un mensaje de prueba con más de 10 caracteres.")
                .build();

        // Crear mensaje sin contenido (mensaje opcional)
        mensajeSinMensaje = Mensaje.builder()
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .build();

        // Crear mensaje con fecha manual
        mensajeConFechaManual = Mensaje.builder()
                .nombreUsuario("María García")
                .email("maria.garcia@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(LocalDate.of(2024, 1, 1))
                .build();
    }

    // ==================== TESTS DE GUARDADO ====================

    @Test
    @DisplayName("save - Debería guardar un mensaje correctamente")
    void save_ShouldSaveMensajeCorrectly() {
        // Act
        Mensaje mensajeGuardado = mensajeRepository.save(mensajeValido);

        // Assert
        assertNotNull(mensajeGuardado, "El mensaje guardado no debería ser nulo");
        assertNotNull(mensajeGuardado.getId(), "El ID del mensaje guardado no debería ser nulo");
        assertEquals("Juan Pérez", mensajeGuardado.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("juan.perez@email.com", mensajeGuardado.getEmail(), "El email debería coincidir");
        assertEquals("Este es un mensaje de prueba con más de 10 caracteres.",
                mensajeGuardado.getMensaje(), "El mensaje debería coincidir");

        // Verificar que la fecha de creación fue establecida por @PrePersist
        assertNotNull(mensajeGuardado.getFechaCreacion(), "La fecha de creación no debería ser nula");
        assertEquals(LocalDate.now(), mensajeGuardado.getFechaCreacion(),
                "La fecha de creación debería ser la fecha actual");

        // Verificar que se guardó en la base de datos
        Mensaje mensajeEncontrado = entityManager.find(Mensaje.class, mensajeGuardado.getId());
        assertNotNull(mensajeEncontrado, "El mensaje debería existir en la base de datos");
        assertEquals(mensajeGuardado.getNombreUsuario(), mensajeEncontrado.getNombreUsuario(),
                "El nombre de usuario debería coincidir en la BD");
    }

    @Test
    @DisplayName("save - Debería guardar un mensaje sin contenido (mensaje nulo)")
    void save_ShouldSaveMensajeWithoutMessage() {
        // Act
        Mensaje mensajeGuardado = mensajeRepository.save(mensajeSinMensaje);

        // Assert
        assertNotNull(mensajeGuardado, "El mensaje guardado no debería ser nulo");
        assertNotNull(mensajeGuardado.getId(), "El ID del mensaje guardado no debería ser nulo");
        assertEquals("Carlos López", mensajeGuardado.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("carlos@email.com", mensajeGuardado.getEmail(), "El email debería coincidir");
        assertNull(mensajeGuardado.getMensaje(), "El mensaje debería ser nulo");
        assertNotNull(mensajeGuardado.getFechaCreacion(), "La fecha de creación no debería ser nula");

        // Verificar en la base de datos
        Mensaje mensajeEncontrado = entityManager.find(Mensaje.class, mensajeGuardado.getId());
        assertNotNull(mensajeEncontrado, "El mensaje debería existir en la BD");
        assertNull(mensajeEncontrado.getMensaje(), "El mensaje debería ser nulo en la BD");
    }

    @Test
    @DisplayName("save - Debería guardar un mensaje con fecha manual")
    void save_ShouldSaveMensajeWithManualDate() {
        // Act
        Mensaje mensajeGuardado = mensajeRepository.save(mensajeConFechaManual);

        // Assert
        assertNotNull(mensajeGuardado, "El mensaje guardado no debería ser nulo");
        assertNotNull(mensajeGuardado.getId(), "El ID del mensaje guardado no debería ser nulo");
        assertEquals("María García", mensajeGuardado.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("maria.garcia@email.com", mensajeGuardado.getEmail(), "El email debería coincidir");

        // Verificar que la fecha manual se mantiene (no se sobrescribe por @PrePersist si ya existe)
        assertNotNull(mensajeGuardado.getFechaCreacion(), "La fecha de creación no debería ser nula");
        assertEquals(LocalDate.of(2024, 1, 1), mensajeGuardado.getFechaCreacion(),
                "La fecha de creación debería ser la establecida manualmente");

        // Verificar en la base de datos
        Mensaje mensajeEncontrado = entityManager.find(Mensaje.class, mensajeGuardado.getId());
        assertNotNull(mensajeEncontrado, "El mensaje debería existir en la BD");
        assertEquals(LocalDate.of(2024, 1, 1), mensajeEncontrado.getFechaCreacion(),
                "La fecha en la BD debería ser la establecida manualmente");
    }

    @Test
    @DisplayName("save - Debería guardar un mensaje con email exactamente de 100 caracteres")
    void save_ShouldSaveMensajeWithEmailAtMaxLength() {
        // Arrange
        String emailMaximo = "a".repeat(90) + "@email.com"; // 100 caracteres
        Mensaje mensajeEmailLargo = Mensaje.builder()
                .nombreUsuario("Usuario Test")
                .email(emailMaximo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act
        Mensaje mensajeGuardado = mensajeRepository.save(mensajeEmailLargo);

        // Assert
        assertNotNull(mensajeGuardado, "El mensaje guardado no debería ser nulo");
        assertNotNull(mensajeGuardado.getId(), "El ID del mensaje guardado no debería ser nulo");
        assertEquals(emailMaximo, mensajeGuardado.getEmail(), "El email largo debería mantenerse");
        assertEquals(100, mensajeGuardado.getEmail().length(), "El email debería tener exactamente 100 caracteres");
    }

    @Test
    @DisplayName("save - Debería guardar un mensaje con mensaje exactamente de 1000 caracteres")
    void save_ShouldSaveMensajeWithMessageAtMaxLength() {
        // Arrange
        String mensajeMaximo = "a".repeat(1000);
        Mensaje mensajeMensajeLargo = Mensaje.builder()
                .nombreUsuario("Usuario Test")
                .email("test@email.com")
                .mensaje(mensajeMaximo)
                .build();

        // Act
        Mensaje mensajeGuardado = mensajeRepository.save(mensajeMensajeLargo);

        // Assert
        assertNotNull(mensajeGuardado, "El mensaje guardado no debería ser nulo");
        assertNotNull(mensajeGuardado.getId(), "El ID del mensaje guardado no debería ser nulo");
        assertEquals(mensajeMaximo, mensajeGuardado.getMensaje(), "El mensaje largo debería mantenerse");
        assertEquals(1000, mensajeGuardado.getMensaje().length(), "El mensaje debería tener exactamente 1000 caracteres");
    }

    // ==================== TESTS DE BÚSQUEDA ====================

    @Test
    @DisplayName("findById - Debería encontrar mensaje por ID cuando existe")
    void findById_ShouldFindMensaje_WhenIdExists() {
        // Arrange - Guardar mensaje en la base de datos
        Mensaje mensajeGuardado = entityManager.persist(mensajeValido);
        entityManager.flush();

        // Act
        Optional<Mensaje> mensajeEncontrado = mensajeRepository.findById(mensajeGuardado.getId());

        // Assert
        assertTrue(mensajeEncontrado.isPresent(), "El mensaje debería ser encontrado");
        assertEquals(mensajeGuardado.getId(), mensajeEncontrado.get().getId(), "El ID debería coincidir");
        assertEquals(mensajeGuardado.getNombreUsuario(), mensajeEncontrado.get().getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals(mensajeGuardado.getEmail(), mensajeEncontrado.get().getEmail(), "El email debería coincidir");
        assertEquals(mensajeGuardado.getMensaje(), mensajeEncontrado.get().getMensaje(), "El mensaje debería coincidir");
        assertNotNull(mensajeEncontrado.get().getFechaCreacion(), "La fecha de creación no debería ser nula");
    }

    @Test
    @DisplayName("findById - No debería encontrar mensaje cuando ID no existe")
    void findById_ShouldNotFindMensaje_WhenIdDoesNotExist() {
        // Act
        Optional<Mensaje> mensajeEncontrado = mensajeRepository.findById(999);

        // Assert
        assertFalse(mensajeEncontrado.isPresent(), "El mensaje no debería ser encontrado");
    }

    @Test
    @DisplayName("findAll - Debería encontrar todos los mensajes")
    void findAll_ShouldFindAllMensajes() {
        // Arrange - Guardar varios mensajes en la base de datos
        Mensaje mensaje1 = Mensaje.builder()
                .nombreUsuario("Usuario 1")
                .email("usuario1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .build();

        Mensaje mensaje2 = Mensaje.builder()
                .nombreUsuario("Usuario 2")
                .email("usuario2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .build();

        entityManager.persist(mensaje1);
        entityManager.persist(mensaje2);
        entityManager.flush();

        // Act
        List<Mensaje> mensajes = mensajeRepository.findAll();

        // Assert
        assertNotNull(mensajes, "La lista de mensajes no debería ser nula");
        assertTrue(mensajes.size() >= 2, "Debería haber al menos 2 mensajes");

        // Verificar que los mensajes guardados están en la lista
        boolean foundMensaje1 = mensajes.stream()
                .anyMatch(m -> m.getEmail().equals("usuario1@email.com"));
        boolean foundMensaje2 = mensajes.stream()
                .anyMatch(m -> m.getEmail().equals("usuario2@email.com"));
        assertTrue(foundMensaje1, "El mensaje 1 debería estar en la lista");
        assertTrue(foundMensaje2, "El mensaje 2 debería estar en la lista");
    }

    @Test
    @DisplayName("findAll - Debería retornar lista vacía cuando no hay mensajes")
    void findAll_ShouldReturnEmptyList_WhenNoMensajes() {
        // Act
        List<Mensaje> mensajes = mensajeRepository.findAll();

        // Assert
        assertNotNull(mensajes, "La lista no debería ser nula");
        assertTrue(mensajes.isEmpty(), "La lista debería estar vacía");
    }

    // ==================== TESTS DE ACTUALIZACIÓN ====================

    @Test
    @DisplayName("save - Debería actualizar un mensaje existente")
    void save_ShouldUpdateExistingMensaje() {
        // Arrange - Guardar mensaje en la base de datos
        Mensaje mensajeGuardado = entityManager.persist(mensajeValido);
        entityManager.flush();

        // Act - Actualizar mensaje
        mensajeGuardado.setNombreUsuario("Juan Pérez Actualizado");
        mensajeGuardado.setEmail("juan.actualizado@email.com");
        mensajeGuardado.setMensaje("Mensaje actualizado con más de 10 caracteres.");
        Mensaje mensajeActualizado = mensajeRepository.save(mensajeGuardado);

        // Assert
        assertNotNull(mensajeActualizado, "El mensaje actualizado no debería ser nulo");
        assertEquals(mensajeGuardado.getId(), mensajeActualizado.getId(), "El ID debería ser el mismo");
        assertEquals("Juan Pérez Actualizado", mensajeActualizado.getNombreUsuario(),
                "El nombre de usuario debería estar actualizado");
        assertEquals("juan.actualizado@email.com", mensajeActualizado.getEmail(),
                "El email debería estar actualizado");
        assertEquals("Mensaje actualizado con más de 10 caracteres.", mensajeActualizado.getMensaje(),
                "El mensaje debería estar actualizado");

        // Verificar en la base de datos
        Mensaje mensajeEncontrado = entityManager.find(Mensaje.class, mensajeGuardado.getId());
        assertNotNull(mensajeEncontrado, "El mensaje debería existir en la BD");
        assertEquals("Juan Pérez Actualizado", mensajeEncontrado.getNombreUsuario(),
                "El nombre de usuario en la BD debería estar actualizado");
        assertEquals("juan.actualizado@email.com", mensajeEncontrado.getEmail(),
                "El email en la BD debería estar actualizado");
    }

    @Test
    @DisplayName("save - Debería actualizar solo el mensaje (sin afectar otros campos)")
    void save_ShouldUpdateOnlyMessage() {
        // Arrange - Guardar mensaje en la base de datos
        Mensaje mensajeGuardado = entityManager.persist(mensajeValido);
        entityManager.flush();
        String nombreOriginal = mensajeGuardado.getNombreUsuario();
        String emailOriginal = mensajeGuardado.getEmail();

        // Act - Actualizar solo el mensaje
        mensajeGuardado.setMensaje("Nuevo mensaje actualizado con más de 10 caracteres.");
        Mensaje mensajeActualizado = mensajeRepository.save(mensajeGuardado);

        // Assert
        assertNotNull(mensajeActualizado, "El mensaje actualizado no debería ser nulo");
        assertEquals(nombreOriginal, mensajeActualizado.getNombreUsuario(),
                "El nombre de usuario no debería cambiar");
        assertEquals(emailOriginal, mensajeActualizado.getEmail(), "El email no debería cambiar");
        assertEquals("Nuevo mensaje actualizado con más de 10 caracteres.", mensajeActualizado.getMensaje(),
                "El mensaje debería estar actualizado");
    }

    @Test
    @DisplayName("save - Debería actualizar la fecha de creación")
    void save_ShouldUpdateFechaCreacion() {
        // Arrange - Guardar mensaje en la base de datos
        Mensaje mensajeGuardado = entityManager.persist(mensajeConFechaManual);
        entityManager.flush();
        LocalDate fechaOriginal = mensajeGuardado.getFechaCreacion();

        // Act - Actualizar fecha de creación
        LocalDate nuevaFecha = LocalDate.of(2025, 6, 15);
        mensajeGuardado.setFechaCreacion(nuevaFecha);
        Mensaje mensajeActualizado = mensajeRepository.save(mensajeGuardado);

        // Assert
        assertNotNull(mensajeActualizado, "El mensaje actualizado no debería ser nulo");
        assertEquals(nuevaFecha, mensajeActualizado.getFechaCreacion(),
                "La fecha de creación debería estar actualizada");
        assertNotEquals(fechaOriginal, mensajeActualizado.getFechaCreacion(),
                "La fecha de creación debería ser diferente a la original");
    }

    // ==================== TESTS DE ELIMINACIÓN ====================

    @Test
    @DisplayName("deleteById - Debería eliminar mensaje por ID")
    void deleteById_ShouldDeleteMensaje() {
        // Arrange - Guardar mensaje en la base de datos
        Mensaje mensajeGuardado = entityManager.persist(mensajeValido);
        entityManager.flush();
        Integer id = mensajeGuardado.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Mensaje.class, id), "El mensaje debería existir antes de eliminar");

        // Act
        mensajeRepository.deleteById(id);

        // Assert - Verificar que ya no existe
        Mensaje mensajeEliminado = entityManager.find(Mensaje.class, id);
        assertNull(mensajeEliminado, "El mensaje no debería existir después de eliminar");
    }

    @Test
    @DisplayName("delete - Debería eliminar mensaje por entidad")
    void delete_ShouldDeleteMensajeEntity() {
        // Arrange - Guardar mensaje en la base de datos
        Mensaje mensajeGuardado = entityManager.persist(mensajeValido);
        entityManager.flush();
        Integer id = mensajeGuardado.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Mensaje.class, id), "El mensaje debería existir antes de eliminar");

        // Act
        mensajeRepository.delete(mensajeGuardado);

        // Assert - Verificar que ya no existe
        Mensaje mensajeEliminado = entityManager.find(Mensaje.class, id);
        assertNull(mensajeEliminado, "El mensaje no debería existir después de eliminar");
    }

    @Test
    @DisplayName("deleteById - No debería lanzar excepción al eliminar ID no existente")
    void deleteById_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            mensajeRepository.deleteById(999);
        }, "No debería lanzar excepción al eliminar un ID que no existe");
    }

    @Test
    @DisplayName("deleteAll - Debería eliminar todos los mensajes")
    void deleteAll_ShouldDeleteAllMensajes() {
        // Arrange - Guardar varios mensajes
        Mensaje mensaje1 = Mensaje.builder()
                .nombreUsuario("Usuario 1")
                .email("usuario1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .build();

        Mensaje mensaje2 = Mensaje.builder()
                .nombreUsuario("Usuario 2")
                .email("usuario2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .build();

        entityManager.persist(mensaje1);
        entityManager.persist(mensaje2);
        entityManager.flush();

        // Verificar que existen
        List<Mensaje> mensajesAntes = mensajeRepository.findAll();
        assertTrue(mensajesAntes.size() >= 2, "Debería haber al menos 2 mensajes antes de eliminar");

        // Act
        mensajeRepository.deleteAll();

        // Assert
        List<Mensaje> mensajesDespues = mensajeRepository.findAll();
        assertEquals(0, mensajesDespues.size(), "No debería haber mensajes después de eliminar todos");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("save - Debería guardar mensaje con email vacío (fallará por @NonNull)")
    void save_ShouldThrowException_WhenEmailIsNull() {
        // Arrange
        Mensaje mensajeSinEmail = Mensaje.builder()
                .nombreUsuario("Usuario Test")
                .email(null)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act & Assert - Debería lanzar DataIntegrityViolationException
        assertThrows(DataIntegrityViolationException.class, () -> {
            mensajeRepository.saveAndFlush(mensajeSinEmail);
        }, "Debería lanzar DataIntegrityViolationException cuando el email es nulo");
    }

    @Test
    @DisplayName("save - Debería guardar mensaje con nombreUsuario vacío (fallará por @NonNull)")
    void save_ShouldThrowException_WhenNombreUsuarioIsNull() {
        // Arrange
        Mensaje mensajeSinNombre = Mensaje.builder()
                .nombreUsuario(null)
                .email("test@email.com")
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act & Assert - Debería lanzar DataIntegrityViolationException
        assertThrows(DataIntegrityViolationException.class, () -> {
            mensajeRepository.saveAndFlush(mensajeSinNombre);
        }, "Debería lanzar DataIntegrityViolationException cuando el nombre de usuario es nulo");
    }

    @Test
    @DisplayName("save - Debería guardar mensaje con email muy largo (más de 100 caracteres)")
    void save_ShouldThrowException_WhenEmailIsTooLong() {
        // Arrange - Email demasiado largo
        String emailLargo = "a".repeat(91) + "@email.com"; // 101 caracteres
        Mensaje mensajeEmailLargo = Mensaje.builder()
                .nombreUsuario("Usuario Test")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        // Act & Assert
        assertThrows(Exception.class, () -> {
            mensajeRepository.saveAndFlush(mensajeEmailLargo);
        }, "Debería lanzar excepción cuando el email es demasiado largo");
    }

    @Test
    @DisplayName("save - Debería guardar mensaje con mensaje muy largo (más de 1000 caracteres)")
    void save_ShouldThrowException_WhenMessageIsTooLong() {
        // Arrange - Mensaje demasiado largo
        String mensajeLargo = "a".repeat(1001);
        Mensaje mensajeMensajeLargo = Mensaje.builder()
                .nombreUsuario("Usuario Test")
                .email("test@email.com")
                .mensaje(mensajeLargo)
                .build();

        // Act & Assert
        assertThrows(Exception.class, () -> {
            mensajeRepository.saveAndFlush(mensajeMensajeLargo);
        }, "Debería lanzar excepción cuando el mensaje es demasiado largo");
    }

    @Test
    @DisplayName("findById - Debería retornar Optional vacío con ID nulo")
    void findById_ShouldReturnEmptyOptional_WithNullId() {
        // Act
        Optional<Mensaje> mensajeEncontrado = mensajeRepository.findById(null);

        // Assert
        assertFalse(mensajeEncontrado.isPresent(), "Debería retornar Optional vacío con ID nulo");
    }

    @Test
    @DisplayName("existsById - Debería retornar true cuando el mensaje existe")
    void existsById_ShouldReturnTrue_WhenMensajeExists() {
        // Arrange - Guardar mensaje en la base de datos
        Mensaje mensajeGuardado = entityManager.persist(mensajeValido);
        entityManager.flush();

        // Act
        boolean exists = mensajeRepository.existsById(mensajeGuardado.getId());

        // Assert
        assertTrue(exists, "Debería retornar true cuando el mensaje existe");
    }

    @Test
    @DisplayName("existsById - Debería retornar false cuando el mensaje no existe")
    void existsById_ShouldReturnFalse_WhenMensajeDoesNotExist() {
        // Act
        boolean exists = mensajeRepository.existsById(999);

        // Assert
        assertFalse(exists, "Debería retornar false cuando el mensaje no existe");
    }

    @Test
    @DisplayName("count - Debería retornar el número correcto de mensajes")
    void count_ShouldReturnCorrectNumberOfMensajes() {
        // Arrange - Guardar varios mensajes
        entityManager.persist(mensajeValido);
        entityManager.persist(mensajeSinMensaje);
        entityManager.flush();

        // Act
        long count = mensajeRepository.count();

        // Assert
        assertTrue(count >= 2, "Debería haber al menos 2 mensajes");
    }

    // ==================== TESTS DE @PrePersist ====================

    @Test
    @DisplayName("@PrePersist - Debería establecer fechaCreacion automáticamente")
    void prePersist_ShouldSetFechaCreacionAutomatically() {
        // Act
        Mensaje mensajeGuardado = mensajeRepository.save(mensajeValido);

        // Assert
        assertNotNull(mensajeGuardado.getFechaCreacion(), "La fecha de creación no debería ser nula");
        assertEquals(LocalDate.now(), mensajeGuardado.getFechaCreacion(),
                "La fecha de creación debería ser la fecha actual");
    }

    @Test
    @DisplayName("@PrePersist - No debería sobrescribir fechaCreacion existente")
    void prePersist_ShouldNotOverrideExistingFechaCreacion() {
        // Act - Guardar mensaje con fecha manual
        Mensaje mensajeGuardado = mensajeRepository.save(mensajeConFechaManual);

        // Assert
        assertNotNull(mensajeGuardado.getFechaCreacion(), "La fecha de creación no debería ser nula");
        assertEquals(LocalDate.of(2024, 1, 1), mensajeGuardado.getFechaCreacion(),
                "La fecha de creación debería mantener la fecha manual establecida");
    }

    // ==================== TESTS DE MÚLTIPLES OPERACIONES ====================

    @Test
    @DisplayName("Debe manejar múltiples operaciones CRUD secuenciales")
    void shouldHandleMultipleCrudOperationsSequentially() {
        // 1. Guardar mensaje
        Mensaje mensajeGuardado = mensajeRepository.save(mensajeValido);
        assertNotNull(mensajeGuardado.getId(), "El ID no debería ser nulo después de guardar");

        // 2. Buscar mensaje
        Optional<Mensaje> mensajeEncontrado = mensajeRepository.findById(mensajeGuardado.getId());
        assertTrue(mensajeEncontrado.isPresent(), "El mensaje debería ser encontrado");

        // 3. Actualizar mensaje
        mensajeEncontrado.get().setNombreUsuario("Nombre Actualizado");
        Mensaje mensajeActualizado = mensajeRepository.save(mensajeEncontrado.get());
        assertEquals("Nombre Actualizado", mensajeActualizado.getNombreUsuario(),
                "El nombre de usuario debería estar actualizado");

        // 4. Verificar actualización
        Optional<Mensaje> mensajeVerificado = mensajeRepository.findById(mensajeGuardado.getId());
        assertTrue(mensajeVerificado.isPresent(), "El mensaje debería existir después de actualizar");
        assertEquals("Nombre Actualizado", mensajeVerificado.get().getNombreUsuario(),
                "El nombre de usuario debería estar actualizado");

        // 5. Eliminar mensaje
        mensajeRepository.deleteById(mensajeGuardado.getId());
        Optional<Mensaje> mensajeEliminado = mensajeRepository.findById(mensajeGuardado.getId());
        assertFalse(mensajeEliminado.isPresent(), "El mensaje no debería existir después de eliminar");
    }

    // ==================== TESTS DE GUARDADO MÚLTIPLE ====================

    @Test
    @DisplayName("saveAll - Debería guardar múltiples mensajes")
    void saveAll_ShouldSaveMultipleMensajes() {
        // Arrange
        Mensaje mensaje1 = Mensaje.builder()
                .nombreUsuario("Usuario 1")
                .email("usuario1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .build();

        Mensaje mensaje2 = Mensaje.builder()
                .nombreUsuario("Usuario 2")
                .email("usuario2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .build();

        Mensaje mensaje3 = Mensaje.builder()
                .nombreUsuario("Usuario 3")
                .email("usuario3@email.com")
                .mensaje("Mensaje 3 con más de 10 caracteres.")
                .build();

        List<Mensaje> mensajes = List.of(mensaje1, mensaje2, mensaje3);

        // Act
        List<Mensaje> mensajesGuardados = mensajeRepository.saveAll(mensajes);

        // Assert
        assertNotNull(mensajesGuardados, "La lista de mensajes guardados no debería ser nula");
        assertEquals(3, mensajesGuardados.size(), "Deberían haberse guardado 3 mensajes");

        for (Mensaje mensaje : mensajesGuardados) {
            assertNotNull(mensaje.getId(), "El ID del mensaje no debería ser nulo");
            assertNotNull(mensaje.getFechaCreacion(), "La fecha de creación no debería ser nula");
            assertEquals(LocalDate.now(), mensaje.getFechaCreacion(),
                    "La fecha de creación debería ser la fecha actual");
        }

        // Verificar en la base de datos
        List<Mensaje> mensajesEncontrados = mensajeRepository.findAll();
        assertTrue(mensajesEncontrados.size() >= 3, "Debería haber al menos 3 mensajes en la BD");
    }

    // ==================== TESTS DE CARACTERES ESPECIALES ====================

    @Test
    @DisplayName("save - Debería guardar mensaje con caracteres especiales")
    void save_ShouldSaveMensajeWithSpecialCharacters() {
        // Arrange
        String nombreEspecial = "José María ñáéíóú ÜÑ";
        String emailEspecial = "josé.maria@email.com";
        String mensajeEspecial = "Mensaje con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";

        Mensaje mensajeEspecialChars = Mensaje.builder()
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .build();

        // Act
        Mensaje mensajeGuardado = mensajeRepository.save(mensajeEspecialChars);

        // Assert
        assertNotNull(mensajeGuardado, "El mensaje guardado no debería ser nulo");
        assertNotNull(mensajeGuardado.getId(), "El ID del mensaje guardado no debería ser nulo");
        assertEquals(nombreEspecial, mensajeGuardado.getNombreUsuario(),
                "El nombre con caracteres especiales debería mantenerse");
        assertEquals(emailEspecial, mensajeGuardado.getEmail(),
                "El email con caracteres especiales debería mantenerse");
        assertEquals(mensajeEspecial, mensajeGuardado.getMensaje(),
                "El mensaje con caracteres especiales debería mantenerse");

        // Verificar en la base de datos
        Mensaje mensajeEncontrado = entityManager.find(Mensaje.class, mensajeGuardado.getId());
        assertNotNull(mensajeEncontrado, "El mensaje debería existir en la BD");
        assertEquals(nombreEspecial, mensajeEncontrado.getNombreUsuario(),
                "El nombre con caracteres especiales debería mantenerse en la BD");
        assertEquals(emailEspecial, mensajeEncontrado.getEmail(),
                "El email con caracteres especiales debería mantenerse en la BD");
        assertEquals(mensajeEspecial, mensajeEncontrado.getMensaje(),
                "El mensaje con caracteres especiales debería mantenerse en la BD");
    }

    // ==================== TEST DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que el repositorio funciona correctamente con diferentes operaciones")
    void verifyRepositoryWorksCorrectlyWithDifferentOperations() {
        // 1. Guardar
        Mensaje guardado = mensajeRepository.save(mensajeValido);
        assertNotNull(guardado.getId());

        // 2. Buscar todos
        List<Mensaje> todos = mensajeRepository.findAll();
        assertFalse(todos.isEmpty());

        // 3. Verificar existencia
        boolean existe = mensajeRepository.existsById(guardado.getId());
        assertTrue(existe);

        // 4. Contar
        long count = mensajeRepository.count();
        assertTrue(count > 0);

        // 5. Eliminar
        mensajeRepository.deleteById(guardado.getId());
        Optional<Mensaje> eliminado = mensajeRepository.findById(guardado.getId());
        assertFalse(eliminado.isPresent());
    }
}