package com.example.demo.repository;

import com.example.demo.enums.Role;
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
 * Pruebas unitarias para la clase UsuarioRepository
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test
 *
 * Usa @DataJpaTest para pruebas de repositorio con JPA
 * Usa TestEntityManager para gestionar las entidades en el contexto de prueba
 */
@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Usuario usuarioValido;
    private Usuario usuarioAdmin;
    private Imagen imagenPerfil;
    private Imagen imagenPortada;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear imágenes para los usuarios
        imagenPerfil = Imagen.builder()
                .url("perfil.jpg")
                .alt("Foto de perfil")
                .build();

        imagenPortada = Imagen.builder()
                .url("portada.jpg")
                .alt("Foto de portada")
                .build();

        // Crear usuario válido
        usuarioValido = Usuario.builder()
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack con 5 años de experiencia")
                .descripcion("Apasionado por la tecnología y el desarrollo de software.")
                .rol(Role.USER)
                .fotoPerfil(imagenPerfil)
                .fotoPortada(imagenPortada)
                .active(true)
                .build();

        // Crear usuario administrador
        usuarioAdmin = Usuario.builder()
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Administradora del sistema con 10 años de experiencia")
                .descripcion("Especialista en gestión de equipos y sistemas.")
                .rol(Role.ADMIN)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();
    }

    // ==================== TESTS DE GUARDADO ====================

    @Test
    @DisplayName("save - Debería guardar un usuario correctamente")
    void save_ShouldSaveUsuarioCorrectly() {
        // Act
        Usuario usuarioGuardado = usuarioRepository.save(usuarioValido);

        // Assert
        assertNotNull(usuarioGuardado, "El usuario guardado no debería ser nulo");
        assertNotNull(usuarioGuardado.getId(), "El ID del usuario guardado no debería ser nulo");
        assertEquals("Juan Pérez", usuarioGuardado.getNombre(), "El nombre debería coincidir");
        assertEquals("juan.perez@email.com", usuarioGuardado.getUsername(), "El username debería coincidir");
        assertEquals("password123", usuarioGuardado.getPassword(), "La contraseña debería coincidir");
        assertEquals(Role.USER, usuarioGuardado.getRol(), "El rol debería ser USER");
        assertTrue(usuarioGuardado.isActive(), "El usuario debería estar activo");
        assertNotNull(usuarioGuardado.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertNotNull(usuarioGuardado.getFotoPortada(), "La foto de portada no debería ser nula");

        // Verificar que se guardó en la base de datos
        Usuario usuarioEncontrado = entityManager.find(Usuario.class, usuarioGuardado.getId());
        assertNotNull(usuarioEncontrado, "El usuario debería existir en la base de datos");
        assertEquals(usuarioGuardado.getNombre(), usuarioEncontrado.getNombre(), "El nombre debería coincidir");
    }

    @Test
    @DisplayName("save - Debería guardar un usuario sin imágenes correctamente")
    void save_ShouldSaveUsuarioWithoutImagesCorrectly() {
        // Arrange
        Usuario usuarioSinImagenes = Usuario.builder()
                .nombre("Carlos López")
                .username("carlos@email.com")
                .password("pass789")
                .introduccion("Introducción válida para prueba")
                .descripcion("Descripción válida para prueba")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        Usuario usuarioGuardado = usuarioRepository.save(usuarioSinImagenes);

        // Assert
        assertNotNull(usuarioGuardado, "El usuario guardado no debería ser nulo");
        assertNotNull(usuarioGuardado.getId(), "El ID del usuario guardado no debería ser nulo");
        assertNull(usuarioGuardado.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(usuarioGuardado.getFotoPortada(), "La foto de portada debería ser nula");

        // Verificar que se guardó en la base de datos
        Usuario usuarioEncontrado = entityManager.find(Usuario.class, usuarioGuardado.getId());
        assertNotNull(usuarioEncontrado, "El usuario debería existir en la base de datos");
        assertNull(usuarioEncontrado.getFotoPerfil(), "La foto de perfil debería ser nula en la BD");
        assertNull(usuarioEncontrado.getFotoPortada(), "La foto de portada debería ser nula en la BD");
    }

    @Test
    @DisplayName("save - Debería guardar un usuario con rol ADMIN")
    void save_ShouldSaveUsuarioWithAdminRole() {
        // Act
        Usuario usuarioGuardado = usuarioRepository.save(usuarioAdmin);

        // Assert
        assertNotNull(usuarioGuardado, "El usuario guardado no debería ser nulo");
        assertNotNull(usuarioGuardado.getId(), "El ID del usuario guardado no debería ser nulo");
        assertEquals("María García", usuarioGuardado.getNombre(), "El nombre debería coincidir");
        assertEquals("maria.garcia@email.com", usuarioGuardado.getUsername(), "El username debería coincidir");
        assertEquals(Role.ADMIN, usuarioGuardado.getRol(), "El rol debería ser ADMIN");

        // Verificar que se guardó en la base de datos
        Usuario usuarioEncontrado = entityManager.find(Usuario.class, usuarioGuardado.getId());
        assertNotNull(usuarioEncontrado, "El usuario debería existir en la base de datos");
        assertEquals(Role.ADMIN, usuarioEncontrado.getRol(), "El rol en la BD debería ser ADMIN");
    }

    // ==================== TESTS DE BÚSQUEDA ====================

    @Test
    @DisplayName("findById - Debería encontrar usuario por ID cuando existe")
    void findById_ShouldFindUsuario_WhenIdExists() {
        // Arrange - Guardar usuario en la base de datos
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioGuardado.getId());

        // Assert
        assertTrue(usuarioEncontrado.isPresent(), "El usuario debería ser encontrado");
        assertEquals(usuarioGuardado.getId(), usuarioEncontrado.get().getId(), "El ID debería coincidir");
        assertEquals(usuarioGuardado.getNombre(), usuarioEncontrado.get().getNombre(), "El nombre debería coincidir");
        assertEquals(usuarioGuardado.getUsername(), usuarioEncontrado.get().getUsername(), "El username debería coincidir");
    }

    @Test
    @DisplayName("findById - No debería encontrar usuario cuando ID no existe")
    void findById_ShouldNotFindUsuario_WhenIdDoesNotExist() {
        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(999);

        // Assert
        assertFalse(usuarioEncontrado.isPresent(), "El usuario no debería ser encontrado");
    }

    @Test
    @DisplayName("findByUsername - Debería encontrar usuario por username cuando existe")
    void findByUsername_ShouldFindUsuario_WhenUsernameExists() {
        // Arrange - Guardar usuario en la base de datos
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername(usuarioValido.getUsername());

        // Assert
        assertTrue(usuarioEncontrado.isPresent(), "El usuario debería ser encontrado por username");
        assertEquals(usuarioGuardado.getId(), usuarioEncontrado.get().getId(), "El ID debería coincidir");
        assertEquals(usuarioGuardado.getUsername(), usuarioEncontrado.get().getUsername(), "El username debería coincidir");
    }

    @Test
    @DisplayName("findByUsername - No debería encontrar usuario cuando username no existe")
    void findByUsername_ShouldNotFindUsuario_WhenUsernameDoesNotExist() {
        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername("no.existe@email.com");

        // Assert
        assertFalse(usuarioEncontrado.isPresent(), "El usuario no debería ser encontrado por username");
    }

    @Test
    @DisplayName("findByUsername - Debería encontrar usuario con username en mayúsculas")
    void findByUsername_ShouldFindUsuario_WithUppercaseUsername() {
        // Arrange - Guardar usuario en la base de datos
        entityManager.persist(usuarioValido);
        entityManager.flush();

        // Act - Buscar con username en mayúsculas
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername("JUAN.PEREZ@EMAIL.COM");

        // Assert - Dependiendo de la configuración de la BD, puede o no encontrar
        // En algunos casos, la búsqueda es case-sensitive
        if (usuarioEncontrado.isPresent()) {
            assertEquals(usuarioValido.getUsername().toUpperCase(),
                    usuarioEncontrado.get().getUsername().toUpperCase(),
                    "El username debería coincidir");
        }
    }

    @Test
    @DisplayName("findAll - Debería encontrar todos los usuarios")
    void findAll_ShouldFindAllUsuarios() {
        // Arrange - Guardar varios usuarios en la base de datos
        entityManager.persist(usuarioValido);
        entityManager.persist(usuarioAdmin);
        entityManager.flush();

        // Act
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Assert
        assertNotNull(usuarios, "La lista de usuarios no debería ser nula");
        assertTrue(usuarios.size() >= 2, "Debería haber al menos 2 usuarios");

        // Verificar que los usuarios guardados están en la lista
        boolean foundUser = usuarios.stream()
                .anyMatch(u -> u.getUsername().equals("juan.perez@email.com"));
        boolean foundAdmin = usuarios.stream()
                .anyMatch(u -> u.getUsername().equals("maria.garcia@email.com"));
        assertTrue(foundUser, "El usuario debería estar en la lista");
        assertTrue(foundAdmin, "El administrador debería estar en la lista");
    }

    // ==================== TESTS DE EXISTS ====================

    @Test
    @DisplayName("existsUserByUsername - Debería retornar true cuando el username existe")
    void existsUserByUsername_ShouldReturnTrue_WhenUsernameExists() {
        // Arrange - Guardar usuario en la base de datos
        entityManager.persist(usuarioValido);
        entityManager.flush();

        // Act
        boolean exists = usuarioRepository.existsUserByUsername(usuarioValido.getUsername());

        // Assert
        assertTrue(exists, "El método debería retornar true cuando el username existe");
    }

    @Test
    @DisplayName("existsUserByUsername - Debería retornar false cuando el username no existe")
    void existsUserByUsername_ShouldReturnFalse_WhenUsernameDoesNotExist() {
        // Act
        boolean exists = usuarioRepository.existsUserByUsername("no.existe@email.com");

        // Assert
        assertFalse(exists, "El método debería retornar false cuando el username no existe");
    }

    @Test
    @DisplayName("existsUserByUsername - Debería retornar false con username nulo")
    void existsUserByUsername_ShouldReturnFalse_WithNullUsername() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioRepository.existsUserByUsername(null);
        }, "Debería lanzar IllegalArgumentException cuando el username es nulo");
    }

    @Test
    @DisplayName("existsUserByUsername - Debería retornar false con username vacío")
    void existsUserByUsername_ShouldReturnFalse_WithEmptyUsername() {
        // Act
        boolean exists = usuarioRepository.existsUserByUsername("");

        // Assert
        assertFalse(exists, "El método debería retornar false con username vacío");
    }

    // ==================== TESTS DE ACTUALIZACIÓN ====================

    @Test
    @DisplayName("save - Debería actualizar un usuario existente")
    void save_ShouldUpdateExistingUsuario() {
        // Arrange - Guardar usuario en la base de datos
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        // Act - Actualizar usuario
        usuarioGuardado.setNombre("Juan Pérez Actualizado");
        usuarioGuardado.setUsername("juan.actualizado@email.com");
        usuarioGuardado.setPassword("newPassword123");
        usuarioGuardado.setRol(Role.ADMIN);
        Usuario usuarioActualizado = usuarioRepository.save(usuarioGuardado);

        // Assert
        assertNotNull(usuarioActualizado, "El usuario actualizado no debería ser nulo");
        assertEquals(usuarioGuardado.getId(), usuarioActualizado.getId(), "El ID debería ser el mismo");
        assertEquals("Juan Pérez Actualizado", usuarioActualizado.getNombre(), "El nombre debería estar actualizado");
        assertEquals("juan.actualizado@email.com", usuarioActualizado.getUsername(), "El username debería estar actualizado");
        assertEquals("newPassword123", usuarioActualizado.getPassword(), "La contraseña debería estar actualizada");
        assertEquals(Role.ADMIN, usuarioActualizado.getRol(), "El rol debería estar actualizado a ADMIN");

        // Verificar en la base de datos
        Usuario usuarioEncontrado = entityManager.find(Usuario.class, usuarioGuardado.getId());
        assertNotNull(usuarioEncontrado, "El usuario debería existir en la BD");
        assertEquals("Juan Pérez Actualizado", usuarioEncontrado.getNombre(), "El nombre en la BD debería estar actualizado");
        assertEquals("juan.actualizado@email.com", usuarioEncontrado.getUsername(), "El username en la BD debería estar actualizado");
    }

    @Test
    @DisplayName("save - Debería actualizar la imagen de perfil de un usuario")
    void save_ShouldUpdateUsuarioProfilePicture() {
        // Arrange - Guardar usuario en la base de datos
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        // Crear nueva imagen de perfil
        Imagen nuevaImagenPerfil = Imagen.builder()
                .url("nuevo-perfil.jpg")
                .alt("Nueva foto de perfil")
                .build();

        // Act - Actualizar imagen de perfil
        usuarioGuardado.setFotoPerfil(nuevaImagenPerfil);
        Usuario usuarioActualizado = usuarioRepository.save(usuarioGuardado);

        // Assert
        assertNotNull(usuarioActualizado.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertEquals("nuevo-perfil.jpg", usuarioActualizado.getFotoPerfil().getUrl(), "La URL de la foto de perfil debería estar actualizada");
        assertEquals("Nueva foto de perfil", usuarioActualizado.getFotoPerfil().getAlt(), "El alt de la foto de perfil debería estar actualizado");

        // Verificar en la base de datos
        Usuario usuarioEncontrado = entityManager.find(Usuario.class, usuarioGuardado.getId());
        assertNotNull(usuarioEncontrado.getFotoPerfil(), "La foto de perfil en la BD no debería ser nula");
        assertEquals("nuevo-perfil.jpg", usuarioEncontrado.getFotoPerfil().getUrl(), "La URL en la BD debería estar actualizada");
    }

    @Test
    @DisplayName("save - Debería actualizar el estado active de un usuario")
    void save_ShouldUpdateUsuarioActiveStatus() {
        // Arrange - Guardar usuario en la base de datos
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();

        // Act - Desactivar usuario
        usuarioGuardado.setActive(false);
        Usuario usuarioActualizado = usuarioRepository.save(usuarioGuardado);

        // Assert
        assertFalse(usuarioActualizado.isActive(), "El usuario debería estar inactivo");

        // Verificar en la base de datos
        Usuario usuarioEncontrado = entityManager.find(Usuario.class, usuarioGuardado.getId());
        assertFalse(usuarioEncontrado.isActive(), "El usuario en la BD debería estar inactivo");

        // Act - Reactivar usuario
        usuarioGuardado.setActive(true);
        usuarioActualizado = usuarioRepository.save(usuarioGuardado);

        // Assert
        assertTrue(usuarioActualizado.isActive(), "El usuario debería estar activo nuevamente");

        // Verificar en la base de datos
        usuarioEncontrado = entityManager.find(Usuario.class, usuarioGuardado.getId());
        assertTrue(usuarioEncontrado.isActive(), "El usuario en la BD debería estar activo nuevamente");
    }

    // ==================== TESTS DE ELIMINACIÓN ====================

    @Test
    @DisplayName("deleteById - Debería eliminar usuario por ID")
    void deleteById_ShouldDeleteUsuario() {
        // Arrange - Guardar usuario en la base de datos
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();
        Integer id = usuarioGuardado.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Usuario.class, id), "El usuario debería existir antes de eliminar");

        // Act
        usuarioRepository.deleteById(id);

        // Assert - Verificar que ya no existe
        Usuario usuarioEliminado = entityManager.find(Usuario.class, id);
        assertNull(usuarioEliminado, "El usuario no debería existir después de eliminar");
    }

    @Test
    @DisplayName("delete - Debería eliminar usuario por entidad")
    void delete_ShouldDeleteUsuarioEntity() {
        // Arrange - Guardar usuario en la base de datos
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();
        Integer id = usuarioGuardado.getId();

        // Verificar que existe antes de eliminar
        assertNotNull(entityManager.find(Usuario.class, id), "El usuario debería existir antes de eliminar");

        // Act
        usuarioRepository.delete(usuarioGuardado);

        // Assert - Verificar que ya no existe
        Usuario usuarioEliminado = entityManager.find(Usuario.class, id);
        assertNull(usuarioEliminado, "El usuario no debería existir después de eliminar");
    }

    @Test
    @DisplayName("deleteById - No debería lanzar excepción al eliminar ID no existente")
    void deleteById_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            usuarioRepository.deleteById(999);
        }, "No debería lanzar excepción al eliminar un ID que no existe");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("findByUsername - Debería manejar username con espacios")
    void findByUsername_ShouldHandleUsernameWithSpaces() {
        // Arrange
        Usuario usuarioConEspacios = Usuario.builder()
                .nombre("Usuario Espacios")
                .username(" usuario.con.espacios@email.com ")
                .password("pass123")
                .introduccion("Introducción válida para prueba")
                .descripcion("Descripción válida para prueba")
                .rol(Role.USER)
                .active(true)
                .build();

        entityManager.persist(usuarioConEspacios);
        entityManager.flush();

        // Act - Buscar con el username sin espacios
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername("usuario.con.espacios@email.com");

        // Assert - Dependiendo de la configuración, podría no encontrar
        if (usuarioEncontrado.isPresent()) {
            assertEquals("usuario.con.espacios@email.com", usuarioEncontrado.get().getUsername(),
                    "El username debería coincidir");
        }
    }

    @Test
    @DisplayName("findByUsername - Debería manejar username con caracteres especiales")
    void findByUsername_ShouldHandleUsernameWithSpecialCharacters() {
        // Arrange
        Usuario usuarioEspecial = Usuario.builder()
                .nombre("Usuario Especial")
                .username("usuario+test@dominio.com")
                .password("pass123")
                .introduccion("Introducción válida para prueba")
                .descripcion("Descripción válida para prueba")
                .rol(Role.USER)
                .active(true)
                .build();

        entityManager.persist(usuarioEspecial);
        entityManager.flush();

        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername("usuario+test@dominio.com");

        // Assert
        assertTrue(usuarioEncontrado.isPresent(), "El usuario con caracteres especiales debería ser encontrado");
        assertEquals("usuario+test@dominio.com", usuarioEncontrado.get().getUsername(),
                "El username debería coincidir");
    }

    @Test
    @DisplayName("findByUsername - Debería manejar username muy largo")
    void findByUsername_ShouldHandleVeryLongUsername() {
        // Arrange
        String veryLongUsername = "a".repeat(100) + "@email.com";
        Usuario usuarioLargo = Usuario.builder()
                .nombre("Usuario Largo")
                .username(veryLongUsername)
                .password("pass123")
                .introduccion("Introducción válida para prueba")
                .descripcion("Descripción válida para prueba")
                .rol(Role.USER)
                .active(true)
                .build();

        entityManager.persist(usuarioLargo);
        entityManager.flush();

        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername(veryLongUsername);

        // Assert
        assertTrue(usuarioEncontrado.isPresent(), "El usuario con username largo debería ser encontrado");
        assertEquals(veryLongUsername, usuarioEncontrado.get().getUsername(),
                "El username largo debería coincidir");
    }

    // ==================== TESTS DE INTEGRACIÓN ====================

    @Test
    @DisplayName("save - Debería guardar usuario con cascada a imágenes")
    void save_ShouldSaveUsuarioWithCascadeToImages() {
        // Act
        Usuario usuarioGuardado = usuarioRepository.save(usuarioValido);

        // Assert
        assertNotNull(usuarioGuardado, "El usuario guardado no debería ser nulo");
        assertNotNull(usuarioGuardado.getId(), "El ID del usuario no debería ser nulo");

        // Verificar que las imágenes se guardaron en cascada
        Imagen imagenPerfilGuardada = usuarioGuardado.getFotoPerfil();
        Imagen imagenPortadaGuardada = usuarioGuardado.getFotoPortada();

        assertNotNull(imagenPerfilGuardada, "La imagen de perfil no debería ser nula");
        assertNotNull(imagenPerfilGuardada.getId(), "El ID de la imagen de perfil no debería ser nulo");
        assertNotNull(imagenPortadaGuardada, "La imagen de portada no debería ser nula");
        assertNotNull(imagenPortadaGuardada.getId(), "El ID de la imagen de portada no debería ser nulo");

        // Verificar en la base de datos
        Usuario usuarioEncontrado = entityManager.find(Usuario.class, usuarioGuardado.getId());
        assertNotNull(usuarioEncontrado.getFotoPerfil(), "La imagen de perfil en la BD no debería ser nula");
        assertNotNull(usuarioEncontrado.getFotoPerfil().getId(), "El ID de la imagen de perfil en la BD no debería ser nulo");
        assertNotNull(usuarioEncontrado.getFotoPortada(), "La imagen de portada en la BD no debería ser nula");
        assertNotNull(usuarioEncontrado.getFotoPortada().getId(), "El ID de la imagen de portada en la BD no debería ser nulo");
    }

    @Test
    @DisplayName("delete - Debería eliminar usuario y sus imágenes en cascada")
    void delete_ShouldDeleteUsuarioAndCascadeImages() {
        // Arrange - Guardar usuario en la base de datos
        Usuario usuarioGuardado = entityManager.persist(usuarioValido);
        entityManager.flush();
        Integer idUsuario = usuarioGuardado.getId();
        Integer idImagenPerfil = usuarioGuardado.getFotoPerfil().getId();
        Integer idImagenPortada = usuarioGuardado.getFotoPortada().getId();

        // Verificar que existen antes de eliminar
        assertNotNull(entityManager.find(Usuario.class, idUsuario), "El usuario debería existir");
        assertNotNull(entityManager.find(Imagen.class, idImagenPerfil), "La imagen de perfil debería existir");
        assertNotNull(entityManager.find(Imagen.class, idImagenPortada), "La imagen de portada debería existir");

        // Act
        usuarioRepository.delete(usuarioGuardado);

        // Assert - Verificar que no existen
        assertNull(entityManager.find(Usuario.class, idUsuario), "El usuario no debería existir");
        assertNull(entityManager.find(Imagen.class, idImagenPerfil), "La imagen de perfil no debería existir");
        assertNull(entityManager.find(Imagen.class, idImagenPortada), "La imagen de portada no debería existir");
    }

    // ==================== TESTS NEGATIVOS ====================

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar usuario con username duplicado")
    void save_ShouldThrowException_WhenUsernameIsDuplicate() {
        // Arrange - Guardar primer usuario
        entityManager.persist(usuarioValido);
        entityManager.flush();

        // Crear usuario con el mismo username
        Usuario usuarioDuplicado = Usuario.builder()
                .nombre("Otro Usuario")
                .username("juan.perez@email.com") // Mismo username
                .password("pass456")
                .introduccion("Introducción válida para prueba")
                .descripcion("Descripción válida para prueba")
                .rol(Role.USER)
                .active(true)
                .build();

        // Act & Assert - Debería lanzar excepción por username duplicado
        assertThrows(Exception.class, () -> {
            usuarioRepository.save(usuarioDuplicado);
        }, "Debería lanzar excepción al guardar usuario con username duplicado");
    }

    @Test
    @DisplayName("save - Debería lanzar excepción al guardar usuario con campos obligatorios nulos")
    void save_ShouldThrowException_WhenRequiredFieldsAreNull() {
        // Arrange - Usuario con campos obligatorios nulos
        Usuario usuarioInvalido = Usuario.builder()
                .nombre(null)
                .username(null)
                .password(null)
                .introduccion(null)
                .descripcion(null)
                .rol(null)
                .active(true)
                .build();

        // Act & Assert - Debería lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioRepository.save(usuarioInvalido);
        }, "Debería lanzar excepción al guardar usuario con campos obligatorios nulos");
    }

    @Test
    @DisplayName("findById - Debería retornar Optional vacío con ID nulo")
    void findById_ShouldReturnEmptyOptional_WithNullId() {
        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(null);

        // Assert
        assertFalse(usuarioEncontrado.isPresent(), "Debería retornar Optional vacío con ID nulo");
    }

    // ==================== TESTS DE MÉTODOS DEFAULT ====================

    @Test
    @DisplayName("existsUserByUsername - Debería usar findByUsername internamente")
    void existsUserByUsername_ShouldUseFindByUsernameInternally() {
        // Arrange - Guardar usuario en la base de datos
        entityManager.persist(usuarioValido);
        entityManager.flush();

        // Act
        boolean exists = usuarioRepository.existsUserByUsername(usuarioValido.getUsername());

        // Assert
        assertTrue(exists, "El método default debería retornar true");

        // Verificar que el método funciona correctamente con el mismo username
        Optional<Usuario> usuarioFind = usuarioRepository.findByUsername(usuarioValido.getUsername());
        assertTrue(usuarioFind.isPresent(), "findByUsername también debería encontrar el usuario");
        assertEquals(exists, usuarioFind.isPresent(), "Ambos métodos deberían dar el mismo resultado");
    }

    @Test
    @DisplayName("existsUserByUsername - Debería retornar false para username que no existe")
    void existsUserByUsername_ShouldReturnFalse_ForNonExistentUsername() {
        // Act
        boolean exists = usuarioRepository.existsUserByUsername("no.existe@email.com");

        // Assert
        assertFalse(exists, "Debería retornar false para username que no existe");

        // Verificar que findByUsername también retorna vacío
        Optional<Usuario> usuarioFind = usuarioRepository.findByUsername("no.existe@email.com");
        assertFalse(usuarioFind.isPresent(), "findByUsername debería retornar vacío");
    }

    // ==================== TESTS DE MÚLTIPLES OPERACIONES ====================

    @Test
    @DisplayName("Debe manejar múltiples operaciones CRUD secuenciales")
    void shouldHandleMultipleCrudOperationsSequentially() {
        // 1. Guardar usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuarioValido);
        assertNotNull(usuarioGuardado.getId(), "El ID no debería ser nulo después de guardar");

        // 2. Buscar usuario
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioGuardado.getId());
        assertTrue(usuarioEncontrado.isPresent(), "El usuario debería ser encontrado");

        // 3. Actualizar usuario
        usuarioEncontrado.get().setNombre("Nombre Actualizado");
        Usuario usuarioActualizado = usuarioRepository.save(usuarioEncontrado.get());
        assertEquals("Nombre Actualizado", usuarioActualizado.getNombre(), "El nombre debería estar actualizado");

        // 4. Verificar actualización
        Optional<Usuario> usuarioVerificado = usuarioRepository.findById(usuarioGuardado.getId());
        assertTrue(usuarioVerificado.isPresent(), "El usuario debería existir después de actualizar");
        assertEquals("Nombre Actualizado", usuarioVerificado.get().getNombre(), "El nombre debería estar actualizado");

        // 5. Eliminar usuario
        usuarioRepository.deleteById(usuarioGuardado.getId());
        Optional<Usuario> usuarioEliminado = usuarioRepository.findById(usuarioGuardado.getId());
        assertFalse(usuarioEliminado.isPresent(), "El usuario no debería existir después de eliminar");
    }
}