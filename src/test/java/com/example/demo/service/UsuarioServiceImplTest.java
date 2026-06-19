package com.example.demo.service;

import com.example.demo.dto.ImagenDto;
import com.example.demo.dto.UsuarioDto;
import com.example.demo.enums.Role;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.Impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase UsuarioServiceImpl
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioValido;
    private UsuarioDto usuarioDtoValido;
    private Imagen imagenPerfil;
    private Imagen imagenPortada;
    private ImagenDto imagenPerfilDto;
    private ImagenDto imagenPortadaDto;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test

        // Crear imágenes
        imagenPerfil = Imagen.builder()
                .id(1)
                .url("perfil.jpg")
                .alt("Foto de perfil")
                .build();

        imagenPortada = Imagen.builder()
                .id(2)
                .url("portada.jpg")
                .alt("Foto de portada")
                .build();

        imagenPerfilDto = ImagenDto.builder()
                .id(1)
                .url("perfil.jpg")
                .alt("Foto de perfil")
                .build();

        imagenPortadaDto = ImagenDto.builder()
                .id(2)
                .url("portada.jpg")
                .alt("Foto de portada")
                .build();

        // Crear usuario válido
        usuarioValido = Usuario.builder()
                .id(1)
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

        // Crear UsuarioDto válido
        usuarioDtoValido = UsuarioDto.builder()
                .id(1)
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Desarrollador Full Stack con 5 años de experiencia")
                .descripcion("Apasionado por la tecnología y el desarrollo de software.")
                .rol(Role.USER)
                .fotoPerfil(imagenPerfilDto)
                .fotoPortada(imagenPortadaDto)
                .active(true)
                .build();
    }

    // ==================== TESTS GET BY ID ====================

    @Test
    @DisplayName("getById - Debería retornar UsuarioDto cuando el ID existe")
    void getById_ShouldReturnUsuarioDto_WhenIdExists() {
        // Arrange
        Integer id = 1;
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioValido));
        when(usuarioMapper.toDto(usuarioValido)).thenReturn(usuarioDtoValido);

        // Act
        UsuarioDto resultado = usuarioService.getById(id);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(id, resultado.getId(), "El ID debería coincidir");
        assertEquals(usuarioValido.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
        assertEquals(usuarioValido.getUsername(), resultado.getUsername(), "El username debería coincidir");

        // Verificar interacciones
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioMapper, times(1)).toDto(usuarioValido);
    }

    @Test
    @DisplayName("getById - Debería lanzar RuntimeException cuando el ID no existe")
    void getById_ShouldThrowRuntimeException_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.getById(id);
        }, "Debería lanzar RuntimeException");

        assertTrue(exception.getMessage().contains("Usuario no encontrado con id " + id),
                "El mensaje de excepción debería indicar que el usuario no fue encontrado");

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getById - Debería lanzar RuntimeException con ID nulo")
    void getById_ShouldThrowRuntimeException_WithNullId() {
        // Arrange
        Integer id = null;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            usuarioService.getById(id);
        }, "Debería lanzar RuntimeException con ID nulo");

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioMapper, never()).toDto(any());
    }

    // ==================== TESTS GET BY USERNAME ====================

    @Test
    @DisplayName("getByUsername - Debería retornar UsuarioDto cuando el username existe")
    void getByUsername_ShouldReturnUsuarioDto_WhenUsernameExists() {
        // Arrange
        String username = "juan.perez@email.com";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuarioValido));
        when(usuarioMapper.toDto(usuarioValido)).thenReturn(usuarioDtoValido);

        // Act
        UsuarioDto resultado = usuarioService.getByUsername(username);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(username, resultado.getUsername(), "El username debería coincidir");
        assertEquals(usuarioValido.getNombre(), resultado.getNombre(), "El nombre debería coincidir");

        verify(usuarioRepository, times(1)).findByUsername(username);
        verify(usuarioMapper, times(1)).toDto(usuarioValido);
    }

    @Test
    @DisplayName("getByUsername - Debería manejar ResourceNotFoundException cuando el username no existe")
    void getByUsername_ShouldHandleResourceNotFoundException_WhenUsernameDoesNotExist() {
        // Arrange
        String username = "no.existe@email.com";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        UsuarioDto resultado = usuarioService.getByUsername(username);

        // Assert - El método captura la excepción y retorna null
        assertNull(resultado, "El resultado debería ser nulo cuando el username no existe");

        verify(usuarioRepository, times(1)).findByUsername(username);
        verify(usuarioMapper, never()).toDto(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("getByUsername - Debería manejar username nulo o vacío")
    void getByUsername_ShouldHandleNullOrEmptyUsername(String username) {
        // Arrange
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        UsuarioDto resultado = usuarioService.getByUsername(username);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo para username nulo o vacío");

        verify(usuarioRepository, times(1)).findByUsername(username);
        verify(usuarioMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getByUsername - Debería manejar username con mayúsculas")
    void getByUsername_ShouldHandleUppercaseUsername() {
        // Arrange
        String username = "JUAN.PEREZ@EMAIL.COM";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuarioValido));
        when(usuarioMapper.toDto(usuarioValido)).thenReturn(usuarioDtoValido);

        // Act
        UsuarioDto resultado = usuarioService.getByUsername(username);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(usuarioValido.getNombre(), resultado.getNombre(), "El nombre debería coincidir");

        verify(usuarioRepository, times(1)).findByUsername(username);
        verify(usuarioMapper, times(1)).toDto(usuarioValido);
    }

    // ==================== TESTS CREATE ====================

    @Test
    @DisplayName("create - Debería crear un nuevo usuario correctamente")
    void create_ShouldCreateUsuarioCorrectly() {
        // Arrange
        UsuarioDto usuarioDtoCrear = UsuarioDto.builder()
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Desarrolladora Frontend con 3 años de experiencia")
                .descripcion("Especializada en Angular y React.")
                .rol(Role.ADMIN)
                .active(true)
                .build();

        Usuario usuarioParaGuardar = Usuario.builder()
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Desarrolladora Frontend con 3 años de experiencia")
                .descripcion("Especializada en Angular y React.")
                .rol(Role.ADMIN)
                .active(true)
                .build();

        Usuario usuarioGuardado = Usuario.builder()
                .id(2)
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Desarrolladora Frontend con 3 años de experiencia")
                .descripcion("Especializada en Angular y React.")
                .rol(Role.ADMIN)
                .active(true)
                .build();

        UsuarioDto usuarioDtoCreado = UsuarioDto.builder()
                .id(2)
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Desarrolladora Frontend con 3 años de experiencia")
                .descripcion("Especializada en Angular y React.")
                .rol(Role.ADMIN)
                .active(true)
                .build();

        when(usuarioMapper.toEntity(usuarioDtoCrear)).thenReturn(usuarioParaGuardar);
        when(usuarioRepository.save(usuarioParaGuardar)).thenReturn(usuarioGuardado);
        when(usuarioMapper.toDto(usuarioGuardado)).thenReturn(usuarioDtoCreado);

        // Act
        UsuarioDto resultado = usuarioService.create(usuarioDtoCrear);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.getId(), "El ID generado debería ser 2");
        assertEquals("María García", resultado.getNombre(), "El nombre debería coincidir");
        assertEquals("maria.garcia@email.com", resultado.getUsername(), "El username debería coincidir");
        assertEquals(Role.ADMIN, resultado.getRol(), "El rol debería ser ADMIN");

        verify(usuarioMapper, times(1)).toEntity(usuarioDtoCrear);
        verify(usuarioRepository, times(1)).save(usuarioParaGuardar);
        verify(usuarioMapper, times(1)).toDto(usuarioGuardado);
    }

    @Test
    @DisplayName("create - Debería crear un usuario sin imágenes")
    void create_ShouldCreateUsuarioWithoutImages() {
        // Arrange
        UsuarioDto usuarioDtoSinImagenes = UsuarioDto.builder()
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

        when(usuarioMapper.toEntity(usuarioDtoSinImagenes)).thenReturn(usuarioSinImagenes);
        when(usuarioRepository.save(usuarioSinImagenes)).thenReturn(usuarioSinImagenes);
        when(usuarioMapper.toDto(usuarioSinImagenes)).thenReturn(usuarioDtoSinImagenes);

        // Act
        UsuarioDto resultado = usuarioService.create(usuarioDtoSinImagenes);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(resultado.getFotoPortada(), "La foto de portada debería ser nula");

        verify(usuarioMapper, times(1)).toEntity(usuarioDtoSinImagenes);
        verify(usuarioRepository, times(1)).save(usuarioSinImagenes);
        verify(usuarioMapper, times(1)).toDto(usuarioSinImagenes);
    }

    @Test
    @DisplayName("create - Debería lanzar excepción cuando el DTO es nulo")
    void create_ShouldThrowException_WhenDtoIsNull() {
        // Arrange
        when(usuarioMapper.toEntity(null)).thenThrow(new NullPointerException("UsuarioDto no puede ser nulo"));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            usuarioService.create(null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(usuarioMapper, times(1)).toEntity(null);
        verify(usuarioRepository, never()).save(any());
    }

    // ==================== TESTS UPDATE ====================

    @Test
    @DisplayName("update - Debería actualizar un usuario existente correctamente")
    void update_ShouldUpdateUsuarioCorrectly() {
        // Arrange
        Integer id = 1;
        UsuarioDto usuarioDtoActualizado = UsuarioDto.builder()
                .id(id)
                .nombre("Juan Pérez Actualizado")
                .username("juan.actualizado@email.com")
                .password("newPassword123")
                .introduccion("Introducción actualizada con más de 5 caracteres")
                .descripcion("Descripción actualizada con más de 5 caracteres")
                .rol(Role.ADMIN)
                .fotoPerfil(imagenPerfilDto)
                .fotoPortada(imagenPortadaDto)
                .active(false)
                .build();

        Usuario usuarioActualizado = Usuario.builder()
                .id(id)
                .nombre("Juan Pérez Actualizado")
                .username("juan.actualizado@email.com")
                .password("newPassword123")
                .introduccion("Introducción actualizada con más de 5 caracteres")
                .descripcion("Descripción actualizada con más de 5 caracteres")
                .rol(Role.ADMIN)
                .fotoPerfil(imagenPerfil)
                .fotoPortada(imagenPortada)
                .active(false)
                .build();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioValido));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);
        when(usuarioMapper.toDto(usuarioActualizado)).thenReturn(usuarioDtoActualizado);

        // Act
        UsuarioDto resultado = usuarioService.update(id, usuarioDtoActualizado);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(id, resultado.getId(), "El ID debería coincidir");
        assertEquals("Juan Pérez Actualizado", resultado.getNombre(), "El nombre debería estar actualizado");
        assertEquals("juan.actualizado@email.com", resultado.getUsername(), "El username debería estar actualizado");
        assertEquals(Role.ADMIN, resultado.getRol(), "El rol debería ser ADMIN");
        assertFalse(resultado.isActive(), "El usuario debería estar inactivo");

        // Verificar que se actualizaron los campos
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(usuarioValido);
        verify(usuarioMapper, times(1)).toDto(usuarioActualizado);
    }

    @Test
    @DisplayName("update - Debería lanzar RuntimeException cuando el ID no existe")
    void update_ShouldThrowRuntimeException_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.update(id, usuarioDtoValido);
        }, "Debería lanzar RuntimeException");

        assertTrue(exception.getMessage().contains("Usuario no encontrado " + id),
                "El mensaje de excepción debería indicar que el usuario no fue encontrado");

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, never()).save(any());
        verify(usuarioMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("update - Debería actualizar solo los campos permitidos")
    void update_ShouldUpdateOnlyAllowedFields() {
        // Arrange
        Integer id = 1;
        UsuarioDto usuarioDtoActualizado = UsuarioDto.builder()
                .id(id)
                .nombre("Nuevo Nombre")
                .username("nuevo.email@email.com")
                .password("nuevoPassword")
                .introduccion("Nueva introducción con más de 5 caracteres")
                .descripcion("Nueva descripción con más de 5 caracteres")
                .rol(Role.ADMIN)
                .active(false)
                .build();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioValido));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);
        when(usuarioMapper.toDto(any(Usuario.class))).thenReturn(usuarioDtoActualizado);

        // Act
        usuarioService.update(id, usuarioDtoActualizado);

        // Assert - Verificar que los setters fueron llamados con los valores correctos
        assertEquals("Nuevo Nombre", usuarioValido.getNombre(), "El nombre debería estar actualizado");
        assertEquals("nuevo.email@email.com", usuarioValido.getUsername(), "El username debería estar actualizado");
        assertEquals("nuevoPassword", usuarioValido.getPassword(), "La contraseña debería estar actualizada");
        assertEquals("Nueva introducción con más de 5 caracteres", usuarioValido.getIntroduccion(),
                "La introducción debería estar actualizada");
        assertEquals("Nueva descripción con más de 5 caracteres", usuarioValido.getDescripcion(),
                "La descripción debería estar actualizada");
        assertEquals(Role.ADMIN, usuarioValido.getRol(), "El rol debería estar actualizado");
        assertFalse(usuarioValido.isActive(), "El estado active debería estar actualizado");

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(usuarioValido);
    }

    @Test
    @DisplayName("update - Debería actualizar la foto de perfil cuando se proporciona")
    void update_ShouldUpdateProfilePicture_WhenProvided() {
        // Arrange
        Integer id = 1;
        ImagenDto nuevaFotoPerfilDto = ImagenDto.builder()
                .id(3)
                .url("nuevo-perfil.jpg")
                .alt("Nueva foto de perfil")
                .build();

        UsuarioDto usuarioDtoConNuevaFoto = UsuarioDto.builder()
                .id(id)
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .fotoPerfil(nuevaFotoPerfilDto)
                .fotoPortada(imagenPortadaDto)
                .active(true)
                .build();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioValido));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);
        when(usuarioMapper.toDto(any(Usuario.class))).thenReturn(usuarioDtoConNuevaFoto);

        // Act
        usuarioService.update(id, usuarioDtoConNuevaFoto);

        // Assert - Verificar que se actualizó la foto de perfil
        assertNotNull(usuarioValido.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertEquals("nuevo-perfil.jpg", usuarioValido.getFotoPerfil().getUrl(),
                "La URL de la foto de perfil debería estar actualizada");
        assertEquals("Nueva foto de perfil", usuarioValido.getFotoPerfil().getAlt(),
                "El alt de la foto de perfil debería estar actualizado");

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(usuarioValido);
    }

    @Test
    @DisplayName("update - Debería actualizar la foto de portada cuando se proporciona")
    void update_ShouldUpdateCoverPicture_WhenProvided() {
        // Arrange
        Integer id = 1;
        ImagenDto nuevaFotoPortadaDto = ImagenDto.builder()
                .id(4)
                .url("nuevo-portada.jpg")
                .alt("Nueva foto de portada")
                .build();

        UsuarioDto usuarioDtoConNuevaFoto = UsuarioDto.builder()
                .id(id)
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .fotoPerfil(imagenPerfilDto)
                .fotoPortada(nuevaFotoPortadaDto)
                .active(true)
                .build();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioValido));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);
        when(usuarioMapper.toDto(any(Usuario.class))).thenReturn(usuarioDtoConNuevaFoto);

        // Act
        usuarioService.update(id, usuarioDtoConNuevaFoto);

        // Assert - Verificar que se actualizó la foto de portada
        assertNotNull(usuarioValido.getFotoPortada(), "La foto de portada no debería ser nula");
        assertEquals("nuevo-portada.jpg", usuarioValido.getFotoPortada().getUrl(),
                "La URL de la foto de portada debería estar actualizada");
        assertEquals("Nueva foto de portada", usuarioValido.getFotoPortada().getAlt(),
                "El alt de la foto de portada debería estar actualizado");

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(usuarioValido);
    }

    @Test
    @DisplayName("update - No debería actualizar imágenes cuando son nulas")
    void update_ShouldNotUpdateImages_WhenNull() {
        // Arrange
        Integer id = 1;
        UsuarioDto usuarioDtoSinImagenes = UsuarioDto.builder()
                .id(id)
                .nombre("Juan Pérez")
                .username("juan.perez@email.com")
                .password("password123")
                .introduccion("Introducción válida con más de 5 caracteres")
                .descripcion("Descripción válida con más de 5 caracteres")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioValido));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);
        when(usuarioMapper.toDto(any(Usuario.class))).thenReturn(usuarioDtoSinImagenes);

        // Act
        usuarioService.update(id, usuarioDtoSinImagenes);

        // Assert - Las imágenes existentes deberían mantenerse
        assertNotNull(usuarioValido.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertEquals("perfil.jpg", usuarioValido.getFotoPerfil().getUrl(),
                "La URL de la foto de perfil debería mantenerse");
        assertNotNull(usuarioValido.getFotoPortada(), "La foto de portada no debería ser nula");
        assertEquals("portada.jpg", usuarioValido.getFotoPortada().getUrl(),
                "La URL de la foto de portada debería mantenerse");

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(usuarioValido);
    }

    @Test
    @DisplayName("update - Debería manejar ID nulo - Test negativo")
    void update_ShouldHandleNullId() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            usuarioService.update(null, usuarioDtoValido);
        }, "Debería lanzar NullPointerException cuando el ID es nulo");

        verify(usuarioRepository, never()).findById(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("update - Debería manejar DTO nulo - Test negativo")
    void update_ShouldHandleNullDto() {
        // Arrange
        Integer id = 1;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            usuarioService.update(id, null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(usuarioRepository, never()).findById(any());
        verify(usuarioRepository, never()).save(any());
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("getById - Debería manejar ID en el límite de Integer (Integer.MAX_VALUE)")
    void getById_ShouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        when(usuarioRepository.findById(idMax)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            usuarioService.getById(idMax);
        }, "Debería lanzar RuntimeException con ID máximo");

        verify(usuarioRepository, times(1)).findById(idMax);
    }

    @Test
    @DisplayName("getById - Debería manejar ID en el límite de Integer (Integer.MIN_VALUE)")
    void getById_ShouldHandleMinIntegerId() {
        // Arrange
        Integer idMin = Integer.MIN_VALUE;
        when(usuarioRepository.findById(idMin)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            usuarioService.getById(idMin);
        }, "Debería lanzar RuntimeException con ID mínimo");

        verify(usuarioRepository, times(1)).findById(idMin);
    }

    @Test
    @DisplayName("getByUsername - Debería manejar username muy largo")
    void getByUsername_ShouldHandleVeryLongUsername() {
        // Arrange
        String veryLongUsername = "a".repeat(255) + "@email.com";
        when(usuarioRepository.findByUsername(veryLongUsername)).thenReturn(Optional.empty());

        // Act
        UsuarioDto resultado = usuarioService.getByUsername(veryLongUsername);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo para username largo");

        verify(usuarioRepository, times(1)).findByUsername(veryLongUsername);
        verify(usuarioMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getByUsername - Debería manejar username con caracteres especiales")
    void getByUsername_ShouldHandleSpecialCharacters() {
        // Arrange
        String specialUsername = "usuario+test@dominio.com";
        when(usuarioRepository.findByUsername(specialUsername)).thenReturn(Optional.of(usuarioValido));
        when(usuarioMapper.toDto(usuarioValido)).thenReturn(usuarioDtoValido);

        // Act
        UsuarioDto resultado = usuarioService.getByUsername(specialUsername);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");

        verify(usuarioRepository, times(1)).findByUsername(specialUsername);
        verify(usuarioMapper, times(1)).toDto(usuarioValido);
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con los mocks
        verifyNoInteractions(usuarioRepository);
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en getById")
    void verifyExpectedInteractionsInGetById() {
        // Arrange
        Integer id = 1;
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioValido));
        when(usuarioMapper.toDto(usuarioValido)).thenReturn(usuarioDtoValido);

        // Act
        usuarioService.getById(id);

        // Assert
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioMapper, times(1)).toDto(usuarioValido);
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoMoreInteractions(usuarioMapper);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en create")
    void verifyExpectedInteractionsInCreate() {
        // Arrange
        UsuarioDto usuarioDtoCrear = UsuarioDto.builder()
                .nombre("Test")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .active(true)
                .build();

        Usuario usuario = new Usuario();
        when(usuarioMapper.toEntity(usuarioDtoCrear)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toDto(usuario)).thenReturn(usuarioDtoCrear);

        // Act
        usuarioService.create(usuarioDtoCrear);

        // Assert
        verify(usuarioMapper, times(1)).toEntity(usuarioDtoCrear);
        verify(usuarioRepository, times(1)).save(usuario);
        verify(usuarioMapper, times(1)).toDto(usuario);
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoMoreInteractions(usuarioMapper);
    }

    // ==================== TESTS DE EXCEPCIONES ESPECÍFICAS ====================

    @Test
    @DisplayName("getByUsername - Debería manejar ResourceNotFoundException correctamente")
    void getByUsername_ShouldHandleResourceNotFoundExceptionCorrectly() {
        // Arrange
        String username = "no.existe@email.com";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        UsuarioDto resultado = usuarioService.getByUsername(username);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo");

        // Verificar que la excepción fue capturada y no se propagó
        verify(usuarioRepository, times(1)).findByUsername(username);
        verify(usuarioMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("update - Debería propagar RuntimeException del repositorio")
    void update_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        Integer id = 1;
        when(usuarioRepository.findById(id)).thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.update(id, usuarioDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error de base de datos", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("create - Debería propagar RuntimeException del repositorio")
    void create_ShouldPropagateRuntimeExceptionFromRepository() {
        // Arrange
        when(usuarioMapper.toEntity(usuarioDtoValido)).thenReturn(usuarioValido);
        when(usuarioRepository.save(usuarioValido)).thenThrow(new RuntimeException("Error al guardar"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.create(usuarioDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al guardar", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(usuarioMapper, times(1)).toEntity(usuarioDtoValido);
        verify(usuarioRepository, times(1)).save(usuarioValido);
    }
}