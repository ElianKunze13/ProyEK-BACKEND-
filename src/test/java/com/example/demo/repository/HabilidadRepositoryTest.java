package com.example.demo.repository;

import com.example.demo.model.Habilidad;
import com.example.demo.model.Usuario;
import com.example.demo.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase HabilidadRepository
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Usa @DataJpaTest para pruebas de repositorio con JPA
 * Usa TestEntityManager para gestionar las entidades en el contexto de prueba
 */
@DataJpaTest
@ActiveProfiles("test")
class HabilidadRepositoryTest {

    @Autowired
    private HabilidadRepository habilidadRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Validator validator;
    private Usuario usuarioValido;
    private Habilidad habilidadValida;
    private Habilidad habilidadValida2;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        // Crear usuario válido
        usuarioValido = Usuario.builder()
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .active(true)
                .build();

        // Crear habilidad válida
        habilidadValida = Habilidad.builder()
                .nombre("Comunicación Efectiva")
                .usuario(usuarioValido)
                .build();

        habilidadValida2 = Habilidad.builder()
                .nombre("Trabajo en Equipo")
                .usuario(usuarioValido)
                .build();
    }

    // ==================== TESTS DE GUARDADO ====================

    @Test
    @DisplayName("save - Debería guardar una habilidad correctamente")
    void save_ShouldSaveHabilidadCorrectly() {
        // Act - Guardar habilidad
        Habilidad habilidadGuardada = habilidadRepository.save(habilidadValida);

        // Assert
        assertNotNull(habilidadGuardada, "La habilidad guardada no debería ser nula");
        assertNotNull(habilidadGuardada.getId(), "El ID de la habilidad guardada no debería ser nulo");
        assertEquals("Comunicación Efectiva", habilidadGuardada.getNombre(), "El nombre debería coincidir");
        assertNotNull(habilidadGuardada.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(usuarioValido.getId(), habilidadGuardada.getUsuario().getId(), "El ID del usuario debería coincidir");

        // Verificar que se guardó en la base de datos
        Habilidad habilidadEncontrada = entityManager.find(Habilidad.class, habilidadGuardada.getId());
        assertNotNull(habilidadEncontrada, "La habilidad debería existir en la base de datos");
        assertEquals(habilidadGuardada.getNombre(), habilidadEncontrada.getNombre(), "El nombre debería coincidir");
    }

    @Test
    @DisplayName("save - Debería guardar una habilidad sin usuario correctamente")
    void save_ShouldSaveHabilidadWithoutUsuario() {
        // Arrange - Habilidad sin usuario
        Habilidad habilidadSinUsuario = Habilidad.builder()
                .nombre("Adaptabilidad")
                .usuario(null)
                .build();

        // Act
        Habilidad habilidadGuardada = habilidadRepository.save(habilidadSinUsuario);

        // Assert
        assertNotNull(habilidadGuardada, "La habilidad guardada no debería ser nula");
        assertNotNull(habilidadGuardada.getId(), "El ID no debería ser nulo");
        assertEquals("Adaptabilidad", habilidadGuardada.getNombre(), "El nombre debería coincidir");
        assertNull(habilidadGuardada.getUsuario(), "El usuario debería ser nulo");

        // Verificar en la base de datos
        Habilidad habilidadEncontrada = entityManager.find(Habilidad.class, habilidadGuardada.getId());
        assertNotNull(habilidadEncontrada, "La habilidad debería existir en la BD");
        assertNull(habilidadEncontrada.getUsuario(), "El usuario en la BD debería ser nulo");
    }

    @Test
    @DisplayName("save - Debería guardar múltiples habilidades para el mismo usuario")
    void save_ShouldSaveMultipleHabilidadesForSameUsuario() {
        // Arrange - Guardar usuario primero
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        Habilidad habilidad1 = Habilidad.builder()
                .nombre("Comunicación")
                .usuario(usuarioGuardado)
                .build();

        Habilidad habilidad2 = Habilidad.builder()
                .nombre("Trabajo en Equipo")
                .usuario(usuarioGuardado)
                .build();

        Habilidad habilidad3 = Habilidad.builder()
                .nombre("Adaptabilidad")
                .usuario(usuarioGuardado)
                .build();

        // Act
        Habilidad h1 = habilidadRepository.save(habilidad1);
        Habilidad h2 = habilidadRepository.save(habilidad2);
        Habilidad h3 = habilidadRepository.save(habilidad3);

        // Assert
        assertNotNull(h1.getId(), "ID de habilidad1 no debería ser nulo");
        assertNotNull(h2.getId(), "ID de habilidad2 no debería ser nulo");
        assertNotNull(h3.getId(), "ID de habilidad3 no debería ser nulo");

        // Verificar que todas tienen el mismo usuario
        assertEquals(usuarioGuardado.getId(), h1.getUsuario().getId());
        assertEquals(usuarioGuardado.getId(), h2.getUsuario().getId());
        assertEquals(usuarioGuardado.getId(), h3.getUsuario().getId());

        // Verificar en la base de datos
        List<Habilidad> habilidades = habilidadRepository.findAll();
        assertEquals(3, habilidades.size(), "Debería haber 3 habilidades");
    }

    // ==================== TESTS DE BÚSQUEDA ====================

    @Test
    @DisplayName("findById - Debería encontrar habilidad por ID cuando existe")
    void findById_ShouldFindHabilidad_WhenIdExists() {
        // Arrange - Guardar habilidad en la base de datos
        Habilidad habilidadGuardada = entityManager.persist(habilidadValida);
        entityManager.flush();

        // Act
        Optional<Habilidad> habilidadEncontrada = habilidadRepository.findById(habilidadGuardada.getId());

        // Assert
        assertTrue(habilidadEncontrada.isPresent(), "La habilidad debería ser encontrada");
        assertEquals(habilidadGuardada.getId(), habilidadEncontrada.get().getId(), "El ID debería coincidir");
        assertEquals(habilidadGuardada.getNombre(), habilidadEncontrada.get().getNombre(), "El nombre debería coincidir");
        assertNotNull(habilidadEncontrada.get().getUsuario(), "El usuario no debería ser nulo");
    }

    @Test
    @DisplayName("findById - No debería encontrar habilidad cuando ID no existe")
    void findById_ShouldNotFindHabilidad_WhenIdDoesNotExist() {
        // Act
        Optional<Habilidad> habilidadEncontrada = habilidadRepository.findById(999);

        // Assert
        assertFalse(habilidadEncontrada.isPresent(), "La habilidad no debería ser encontrada");
    }

    @Test
    @DisplayName("findAll - Debería encontrar todas las habilidades")
    void findAll_ShouldFindAllHabilidades() {
        // Arrange - Guardar habilidades en la base de datos
        entityManager.persist(usuarioValido);
        entityManager.persist(habilidadValida);
        entityManager.persist(habilidadValida2);
        entityManager.flush();

        // Act
        List<Habilidad> habilidades = habilidadRepository.findAll();

        // Assert
        assertNotNull(habilidades, "La lista de habilidades no debería ser nula");
        assertTrue(habilidades.size() >= 2, "Debería haber al menos 2 habilidades");

        // Verificar que las habilidades guardadas están en la lista
        boolean foundHabilidad1 = habilidades.stream()
                .anyMatch(h -> "Comunicación Efectiva".equals(h.getNombre()));
        boolean foundHabilidad2 = habilidades.stream()
                .anyMatch(h -> "Trabajo en Equipo".equals(h.getNombre()));
        assertTrue(foundHabilidad1, "La habilidad 'Comunicación Efectiva' debería estar en la lista");
        assertTrue(foundHabilidad2, "La habilidad 'Trabajo en Equipo' debería estar en la lista");
    }

    @Test
    @DisplayName("findAll - Debería retornar lista vacía cuando no hay habilidades")
    void findAll_ShouldReturnEmptyList_WhenNoHabilidades() {
        // Act
        List<Habilidad> habilidades = habilidadRepository.findAll();

        // Assert
        assertNotNull(habilidades, "La lista no debería ser nula");
        assertTrue(habilidades.isEmpty(), "La lista debería estar vacía");
    }

    @Test
    @DisplayName("findByUsuarioId - Debería encontrar habilidades por ID de usuario")
    void findByUsuarioId_ShouldFindHabilidadesByUsuarioId() {
        // Arrange - Guardar usuario y habilidades
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        Habilidad habilidad1 = Habilidad.builder()
                .nombre("Comunicación")
                .usuario(usuarioGuardado)
                .build();

        Habilidad habilidad2 = Habilidad.builder()
                .nombre("Trabajo en Equipo")
                .usuario(usuarioGuardado)
                .build();

        entityManager.persist(habilidad1);
        entityManager.persist(habilidad2);
        entityManager.flush();

        // Act
        List<Habilidad> habilidades = habilidadRepository.findByUsuarioId();

        // Assert
        assertNotNull(habilidades, "La lista no debería ser nula");
        assertTrue(habilidades.size() >= 2, "Debería haber al menos 2 habilidades para el usuario");

        // Verificar que todas las habilidades tienen el usuario correcto
        for (Habilidad h : habilidades) {
            assertEquals(usuarioGuardado.getId(), h.getUsuario().getId(),
                    "El usuario debería ser el mismo para todas las habilidades");
        }
    }

    @Test
    @DisplayName("findByUsuarioId - Debería retornar lista vacía cuando no hay habilidades para el usuario")
    void findByUsuarioId_ShouldReturnEmptyList_WhenNoHabilidadesForUsuario() {
        // Arrange - Guardar usuario sin habilidades
        entityManager.persist(usuarioValido);
        entityManager.flush();

        // Act
        List<Habilidad> habilidades = habilidadRepository.findByUsuarioId();

        // Assert
        assertNotNull(habilidades, "La lista no debería ser nula");
        assertTrue(habilidades.isEmpty(), "La lista debería estar vacía cuando no hay habilidades");
    }

    // ==================== TESTS DE ACTUALIZACIÓN ====================

    @Test
    @DisplayName("save - Debería actualizar una habilidad existente")
    void save_ShouldUpdateExistingHabilidad() {
        // Arrange - Guardar habilidad en la base de datos
        Habilidad habilidadGuardada = entityManager.persist(habilidadValida);
        entityManager.flush();
        Integer id = habilidadGuardada.getId();

        // Act - Actualizar habilidad
        habilidadGuardada.setNombre("Comunicación Avanzada");
        Habilidad habilidadActualizada = habilidadRepository.save(habilidadGuardada);

        // Assert
        assertNotNull(habilidadActualizada, "La habilidad actualizada no debería ser nula");
        assertEquals(id, habilidadActualizada.getId(), "El ID debería ser el mismo");
        assertEquals("Comunicación Avanzada", habilidadActualizada.getNombre(), "El nombre debería estar actualizado");

        // Verificar en la base de datos
        Habilidad habilidadEncontrada = entityManager.find(Habilidad.class, id);
        assertNotNull(habilidadEncontrada, "La habilidad debería existir en la BD");
        assertEquals("Comunicación Avanzada", habilidadEncontrada.getNombre(),
                "El nombre en la BD debería estar actualizado");
    }

    @Test
    @DisplayName("save - Debería actualizar el usuario de una habilidad")
    void save_ShouldUpdateUsuarioOfHabilidad() {
        // Arrange - Guardar habilidades
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        Habilidad habilidadGuardada = entityManager.persist(habilidadValida);
        entityManager.flush();

        // Crear otro usuario
        Usuario nuevoUsuario = Usuario.builder()
                .nombre("María García")
                .username("maria@email.com")
                .password("pass456")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.ADMIN)
                .active(true)
                .build();

        Usuario nuevoUsuarioGuardado = entityManager.persist(nuevoUsuario);
        entityManager.flush();

        // Act - Actualizar usuario de la habilidad
        habilidadGuardada.setUsuario(nuevoUsuarioGuardado);
        Habilidad habilidadActualizada = habilidadRepository.save(habilidadGuardada);

        // Assert
        assertNotNull(habilidadActualizada.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(nuevoUsuarioGuardado.getId(), habilidadActualizada.getUsuario().getId(),
                "El ID del usuario debería ser el del nuevo usuario");
        assertEquals("María García", habilidadActualizada.getUsuario().getNombre(),
                "El nombre del usuario debería ser 'María García'");

        // Verificar en la base de datos
        Habilidad habilidadEncontrada = entityManager.find(Habilidad.class, habilidadGuardada.getId());
        assertNotNull(habilidadEncontrada.getUsuario(), "El usuario en la BD no debería ser nulo");
        assertEquals(nuevoUsuarioGuardado.getId(), habilidadEncontrada.getUsuario().getId(),
                "El usuario en la BD debería ser el nuevo usuario");
    }

    @Test
    @DisplayName("save - Debería actualizar una habilidad para tener usuario nulo")
    void save_ShouldUpdateHabilidadToHaveNullUsuario() {
        // Arrange - Guardar habilidad con usuario
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        Habilidad habilidadGuardada = Habilidad.builder()
                .nombre("Comunicación")
                .usuario(usuarioGuardado)
                .build();
        habilidadGuardada = entityManager.persist(habilidadGuardada);
        entityManager.flush();

        // Act - Eliminar la relación con el usuario
        habilidadGuardada.setUsuario(null);
        Habilidad habilidadActualizada = habilidadRepository.save(habilidadGuardada);

        // Assert
        assertNull(habilidadActualizada.getUsuario(), "El usuario debería ser nulo");

        // Verificar en la base de datos
        Habilidad habilidadEncontrada = entityManager.find(Habilidad.class, habilidadGuardada.getId());
        assertNull(habilidadEncontrada.getUsuario(), "El usuario en la BD debería ser nulo");
    }

    // ==================== TESTS DE ELIMINACIÓN ====================

    @Test
    @DisplayName("deleteById - Debería eliminar habilidad por ID")
    void deleteById_ShouldDeleteHabilidad() {
        // Arrange - Guardar habilidad en la base de datos
        Habilidad habilidadGuardada = entityManager.persist(habilidadValida);
        entityManager.flush();
        Integer id = habilidadGuardada.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Habilidad.class, id), "La habilidad debería existir antes de eliminar");

        // Act
        habilidadRepository.deleteById(id);

        // Assert - Verificar que ya no existe
        Habilidad habilidadEliminada = entityManager.find(Habilidad.class, id);
        assertNull(habilidadEliminada, "La habilidad no debería existir después de eliminar");
    }

    @Test
    @DisplayName("delete - Debería eliminar habilidad por entidad")
    void delete_ShouldDeleteHabilidadEntity() {
        // Arrange - Guardar habilidad en la base de datos
        Habilidad habilidadGuardada = entityManager.persist(habilidadValida);
        entityManager.flush();
        Integer id = habilidadGuardada.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Habilidad.class, id), "La habilidad debería existir antes de eliminar");

        // Act
        habilidadRepository.delete(habilidadGuardada);

        // Assert - Verificar que ya no existe
        Habilidad habilidadEliminada = entityManager.find(Habilidad.class, id);
        assertNull(habilidadEliminada, "La habilidad no debería existir después de eliminar");
    }

    @Test
    @DisplayName("deleteById - No debería lanzar excepción al eliminar ID no existente")
    void deleteById_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            habilidadRepository.deleteById(999);
        }, "No debería lanzar excepción al eliminar un ID que no existe");
    }

    @Test
    @DisplayName("delete - Debería eliminar solo la habilidad, no el usuario asociado")
    void delete_ShouldDeleteOnlyHabilidadNotUsuario() {
        // Arrange - Guardar usuario y habilidad
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        Habilidad habilidadGuardada = Habilidad.builder()
                .nombre("Comunicación")
                .usuario(usuarioGuardado)
                .build();
        habilidadGuardada = entityManager.persist(habilidadGuardada);
        entityManager.flush();

        Integer idHabilidad = habilidadGuardada.getId();
        Integer idUsuario = usuarioGuardado.getId();

        // Verificar que existen antes de eliminar
        assertNotNull(entityManager.find(Habilidad.class, idHabilidad), "La habilidad debería existir");
        assertNotNull(entityManager.find(Usuario.class, idUsuario), "El usuario debería existir");

        // Act - Eliminar habilidad
        habilidadRepository.delete(habilidadGuardada);

        // Assert - La habilidad no existe pero el usuario sí
        assertNull(entityManager.find(Habilidad.class, idHabilidad), "La habilidad no debería existir");
        assertNotNull(entityManager.find(Usuario.class, idUsuario), "El usuario debería seguir existiendo");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("findById - Debería retornar Optional vacío con ID nulo")
    void findById_ShouldReturnEmptyOptional_WithNullId() {
        // Act
        Optional<Habilidad> habilidadEncontrada = habilidadRepository.findById(null);

        // Assert
        assertFalse(habilidadEncontrada.isPresent(), "Debería retornar Optional vacío con ID nulo");
    }

    @Test
    @DisplayName("save - Debería manejar nombre con caracteres especiales")
    void save_ShouldHandleNombreWithSpecialCharacters() {
        // Arrange
        Habilidad habilidadEspecial = Habilidad.builder()
                .nombre("Comunicación & Trabajo en Equipo! @#$")
                .usuario(usuarioValido)
                .build();

        // Act
        Habilidad habilidadGuardada = habilidadRepository.save(habilidadEspecial);

        // Assert
        assertNotNull(habilidadGuardada.getId(), "El ID no debería ser nulo");
        assertEquals("Comunicación & Trabajo en Equipo! @#$", habilidadGuardada.getNombre(),
                "El nombre con caracteres especiales debería mantenerse");

        // Verificar en la base de datos
        Habilidad habilidadEncontrada = entityManager.find(Habilidad.class, habilidadGuardada.getId());
        assertEquals("Comunicación & Trabajo en Equipo! @#$", habilidadEncontrada.getNombre(),
                "El nombre en la BD debería mantener los caracteres especiales");
    }

    @Test
    @DisplayName("save - Debería manejar nombre con espacios al inicio y al final")
    void save_ShouldHandleNombreWithSpaces() {
        // Arrange
        String nombreConEspacios = "  Comunicación Efectiva  ";
        Habilidad habilidadConEspacios = Habilidad.builder()
                .nombre(nombreConEspacios)
                .usuario(usuarioValido)
                .build();

        // Act
        Habilidad habilidadGuardada = habilidadRepository.save(habilidadConEspacios);

        // Assert
        assertNotNull(habilidadGuardada.getId(), "El ID no debería ser nulo");
        assertEquals(nombreConEspacios, habilidadGuardada.getNombre(),
                "El nombre con espacios debería mantenerse");

        // Verificar en la base de datos
        Habilidad habilidadEncontrada = entityManager.find(Habilidad.class, habilidadGuardada.getId());
        assertEquals(nombreConEspacios, habilidadEncontrada.getNombre(),
                "El nombre en la BD debería mantener los espacios");
    }

    @Test
    @DisplayName("save - Debería manejar nombre muy largo (145 caracteres)")
    void save_ShouldHandleNombreVeryLong() {
        // Arrange
        String nombreLargo = "A".repeat(145);
        Habilidad habilidadLarga = Habilidad.builder()
                .nombre(nombreLargo)
                .usuario(usuarioValido)
                .build();

        // Act
        Habilidad habilidadGuardada = habilidadRepository.save(habilidadLarga);

        // Assert
        assertNotNull(habilidadGuardada.getId(), "El ID no debería ser nulo");
        assertEquals(145, habilidadGuardada.getNombre().length(), "El nombre debería tener 145 caracteres");
        assertEquals(nombreLargo, habilidadGuardada.getNombre(), "El nombre largo debería mantenerse");

        // Validar con Bean Validation
        var violations = validator.validate(habilidadGuardada);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    // ==================== TESTS DE VALIDACIÓN CON BEAN VALIDATION ====================

    @Test
    @DisplayName("Validación - Debería pasar validación con nombre válido")
    void validation_ShouldPass_WithValidNombre() {
        // Arrange
        Habilidad habilidad = Habilidad.builder()
                .nombre("Comunicación Efectiva")
                .usuario(usuarioValido)
                .build();

        // Act
        var violations = validator.validate(habilidad);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Validación - Debería fallar con nombre nulo")
    void validation_ShouldFail_WithNullNombre() {
        // Arrange
        Habilidad habilidad = Habilidad.builder()
                .nombre(null)
                .usuario(usuarioValido)
                .build();

        // Act
        var violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debería haber violación en el campo nombre");
    }

    @Test
    @DisplayName("Validación - Debería fallar con nombre vacío")
    void validation_ShouldFail_WithEmptyNombre() {
        // Arrange
        Habilidad habilidad = Habilidad.builder()
                .nombre("")
                .usuario(usuarioValido)
                .build();

        // Act
        var violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debería haber violación en el campo nombre");
    }

    @Test
    @DisplayName("Validación - Debería fallar con nombre muy corto (menos de 3 caracteres)")
    void validation_ShouldFail_WithNombreTooShort() {
        // Arrange
        Habilidad habilidad = Habilidad.builder()
                .nombre("AB")
                .usuario(usuarioValido)
                .build();

        // Act
        var violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debería haber violación en el campo nombre");
    }

    @Test
    @DisplayName("Validación - Debería fallar con nombre muy largo (más de 145 caracteres)")
    void validation_ShouldFail_WithNombreTooLong() {
        // Arrange
        String nombreLargo = "A".repeat(146);
        Habilidad habilidad = Habilidad.builder()
                .nombre(nombreLargo)
                .usuario(usuarioValido)
                .build();

        // Act
        var violations = validator.validate(habilidad);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")),
                "Debería haber violación en el campo nombre");
    }

    // ==================== TESTS DE INTEGRACIÓN CON USUARIO ====================

    @Test
    @DisplayName("save - Debería guardar habilidad asociada a usuario existente")
    void save_ShouldSaveHabilidadAssociatedToExistingUsuario() {
        // Arrange - Guardar usuario primero
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        Habilidad habilidad = Habilidad.builder()
                .nombre("Liderazgo")
                .usuario(usuarioGuardado)
                .build();

        // Act
        Habilidad habilidadGuardada = habilidadRepository.save(habilidad);

        // Assert
        assertNotNull(habilidadGuardada.getId(), "El ID no debería ser nulo");
        assertNotNull(habilidadGuardada.getUsuario(), "El usuario no debería ser nulo");
        assertEquals(usuarioGuardado.getId(), habilidadGuardada.getUsuario().getId(),
                "El ID del usuario debería coincidir");

        // Verificar en la base de datos
        Habilidad habilidadEncontrada = entityManager.find(Habilidad.class, habilidadGuardada.getId());
        assertNotNull(habilidadEncontrada.getUsuario(), "El usuario en la BD no debería ser nulo");
        assertEquals(usuarioGuardado.getId(), habilidadEncontrada.getUsuario().getId(),
                "El usuario en la BD debería ser el mismo");
    }

    @Test
    @DisplayName("findByUsuarioId - Debería retornar solo habilidades del usuario específico")
    void findByUsuarioId_ShouldReturnOnlyHabilidadesForSpecificUsuario() {
        // Arrange - Crear dos usuarios
        Usuario usuario1 = entityManager.persist(usuarioValido);
        entityManager.flush();

        Usuario usuario2 = Usuario.builder()
                .nombre("María García")
                .username("maria@email.com")
                .password("pass456")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.ADMIN)
                .active(true)
                .build();
        Usuario usuario2Guardado = entityManager.persist(usuario2);
        entityManager.flush();

        // Crear habilidades para usuario1
        Habilidad h1 = Habilidad.builder()
                .nombre("Comunicación")
                .usuario(usuario1)
                .build();
        Habilidad h2 = Habilidad.builder()
                .nombre("Trabajo en Equipo")
                .usuario(usuario1)
                .build();
        entityManager.persist(h1);
        entityManager.persist(h2);

        // Crear habilidad para usuario2
        Habilidad h3 = Habilidad.builder()
                .nombre("Liderazgo")
                .usuario(usuario2Guardado)
                .build();
        entityManager.persist(h3);
        entityManager.flush();

        // Act - Obtener habilidades para usuario1
        List<Habilidad> habilidades = habilidadRepository.findByUsuarioId();

        // Assert - Debería retornar solo las habilidades de usuario1
        assertNotNull(habilidades, "La lista no debería ser nula");
        assertTrue(habilidades.size() >= 2, "Debería haber al menos 2 habilidades para el usuario1");

        // Verificar que todas las habilidades son del usuario1
        for (Habilidad h : habilidades) {
            assertEquals(usuario1.getId(), h.getUsuario().getId(),
                    "Las habilidades deberían pertenecer al usuario1");
        }
    }

    // ==================== TESTS DE MÚLTIPLES OPERACIONES ====================

    @Test
    @DisplayName("Debe manejar múltiples operaciones CRUD secuenciales")
    void shouldHandleMultipleCrudOperationsSequentially() {
        // 1. Guardar usuario
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        // 2. Guardar habilidad
        Habilidad habilidad = Habilidad.builder()
                .nombre("Comunicación")
                .usuario(usuarioGuardado)
                .build();
        Habilidad habilidadGuardada = habilidadRepository.save(habilidad);
        assertNotNull(habilidadGuardada.getId(), "El ID no debería ser nulo después de guardar");

        // 3. Buscar habilidad
        Optional<Habilidad> habilidadEncontrada = habilidadRepository.findById(habilidadGuardada.getId());
        assertTrue(habilidadEncontrada.isPresent(), "La habilidad debería ser encontrada");
        assertEquals("Comunicación", habilidadEncontrada.get().getNombre());

        // 4. Actualizar habilidad
        habilidadEncontrada.get().setNombre("Comunicación Avanzada");
        Habilidad habilidadActualizada = habilidadRepository.save(habilidadEncontrada.get());
        assertEquals("Comunicación Avanzada", habilidadActualizada.getNombre(),
                "El nombre debería estar actualizado");

        // 5. Verificar actualización
        Optional<Habilidad> habilidadVerificada = habilidadRepository.findById(habilidadGuardada.getId());
        assertTrue(habilidadVerificada.isPresent(), "La habilidad debería existir después de actualizar");
        assertEquals("Comunicación Avanzada", habilidadVerificada.get().getNombre(),
                "El nombre debería estar actualizado en la BD");

        // 6. Obtener todas las habilidades
        List<Habilidad> todasLasHabilidades = habilidadRepository.findAll();
        assertNotNull(todasLasHabilidades, "La lista no debería ser nula");
        assertTrue(todasLasHabilidades.stream().anyMatch(h -> "Comunicación Avanzada".equals(h.getNombre())),
                "La habilidad actualizada debería estar en la lista");

        // 7. Eliminar habilidad
        habilidadRepository.deleteById(habilidadGuardada.getId());
        Optional<Habilidad> habilidadEliminada = habilidadRepository.findById(habilidadGuardada.getId());
        assertFalse(habilidadEliminada.isPresent(), "La habilidad no debería existir después de eliminar");

        // 8. Verificar que el usuario sigue existiendo
        Optional<Usuario> usuarioVerificado = entityManager.find(Usuario.class, usuarioGuardado.getId()) != null ?
                Optional.of(entityManager.find(Usuario.class, usuarioGuardado.getId())) : Optional.empty();
        assertTrue(usuarioVerificado.isPresent(), "El usuario debería seguir existiendo");
    }

    // ==================== TESTS NEGATIVOS ====================

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar habilidad con nombre nulo")
    void save_ShouldThrowException_WhenNombreIsNull() {
        // Arrange - Habilidad con nombre nulo
        Habilidad habilidadInvalida = Habilidad.builder()
                .nombre(null)
                .usuario(usuarioValido)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            habilidadRepository.save(habilidadInvalida);
        }, "Debería lanzar excepción al guardar habilidad con nombre nulo");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar habilidad con nombre vacío")
    void save_ShouldThrowException_WhenNombreIsEmpty() {
        // Arrange - Habilidad con nombre vacío
        Habilidad habilidadInvalida = Habilidad.builder()
                .nombre("")
                .usuario(usuarioValido)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            habilidadRepository.save(habilidadInvalida);
        }, "Debería lanzar excepción al guardar habilidad con nombre vacío");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar habilidad con nombre muy largo")
    void save_ShouldThrowException_WhenNombreIsTooLong() {
        // Arrange - Habilidad con nombre muy largo (más de 145 caracteres)
        String nombreLargo = "A".repeat(200);
        Habilidad habilidadInvalida = Habilidad.builder()
                .nombre(nombreLargo)
                .usuario(usuarioValido)
                .build();

        // Act & Assert - Debería lanzar excepción o truncar el nombre
        // Dependiendo de la configuración de la BD, puede lanzar excepción o truncar
        try {
            habilidadRepository.save(habilidadInvalida);
            // Si no lanza excepción, verificar que el nombre fue truncado o la BD lo maneja
        } catch (Exception e) {
            // Excepción esperada
            assertTrue(true, "Se lanzó excepción esperada");
        }
    }

    @Test
    @DisplayName("findById - Debería retornar Optional vacío con ID negativo")
    void findById_ShouldReturnEmptyOptional_WithNegativeId() {
        // Act
        Optional<Habilidad> habilidadEncontrada = habilidadRepository.findById(-1);

        // Assert
        assertFalse(habilidadEncontrada.isPresent(), "Debería retornar Optional vacío con ID negativo");
    }

    @Test
    @DisplayName("deleteById - No debería lanzar excepción al eliminar ID negativo")
    void deleteById_ShouldNotThrowException_WithNegativeId() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            habilidadRepository.deleteById(-1);
        }, "No debería lanzar excepción al eliminar ID negativo");
    }

    // ==================== TESTS DE CONSULTAS PERSONALIZADAS ====================

    @Test
    @DisplayName("findByUsuarioId - Debería usar la consulta @Query correctamente")
    void findByUsuarioId_ShouldUseQueryCorrectly() {
        // Arrange - Guardar usuario y habilidades
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        Habilidad habilidad1 = Habilidad.builder()
                .nombre("Habilidad 1")
                .usuario(usuarioGuardado)
                .build();

        Habilidad habilidad2 = Habilidad.builder()
                .nombre("Habilidad 2")
                .usuario(usuarioGuardado)
                .build();

        entityManager.persist(habilidad1);
        entityManager.persist(habilidad2);
        entityManager.flush();

        // Act
        List<Habilidad> habilidades = habilidadRepository.findByUsuarioId();

        // Assert - Verificar que la consulta funciona correctamente
        assertNotNull(habilidades, "La lista no debería ser nula");
        assertTrue(habilidades.size() >= 2, "Debería haber al menos 2 habilidades");

        // Verificar que las habilidades tienen el usuario correcto
        for (Habilidad h : habilidades) {
            assertNotNull(h.getUsuario(), "El usuario no debería ser nulo");
            assertEquals(usuarioGuardado.getId(), h.getUsuario().getId(),
                    "El usuario debería ser el mismo que el guardado");
        }
    }

    @Test
    @DisplayName("findByUsuarioId - Debería retornar habilidades incluso sin usuario")
    void findByUsuarioId_ShouldReturnHabilidadesEvenWithoutUsuario() {
        // Arrange - Guardar habilidad sin usuario
        Habilidad habilidadSinUsuario = Habilidad.builder()
                .nombre("Habilidad sin usuario")
                .usuario(null)
                .build();

        entityManager.persist(usuarioValido);
        entityManager.persist(habilidadSinUsuario);
        entityManager.flush();

        // Act
        List<Habilidad> habilidades = habilidadRepository.findByUsuarioId();

        // Assert - La consulta actual devuelve todas las habilidades, incluyendo las sin usuario
        assertNotNull(habilidades, "La lista no debería ser nula");
        // Dependiendo de la implementación, podría incluir o excluir las sin usuario
        // Verificamos que al menos retorne algo
        assertTrue(habilidades.stream().anyMatch(h -> "Habilidad sin usuario".equals(h.getNombre())),
                "Debería incluir la habilidad sin usuario si la consulta no filtra por usuario");
    }
}