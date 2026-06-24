package com.example.demo.mapper;

import com.example.demo.dto.ImagenDto;
import com.example.demo.dto.UsuarioDto;
import com.example.demo.enums.Role;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsuarioMapperTest {

    @Mock
    private ImagenMapper imagenMapper;

    @InjectMocks
    private UsuarioMapperImpl usuarioMapper;

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

        // Configurar comportamiento del mock de ImagenMapper
        when(imagenMapper.toImagenDto(any(Imagen.class))).thenAnswer(invocation -> {
            Imagen imagen = invocation.getArgument(0);
            if (imagen == null) return null;
            return ImagenDto.builder()
                    .id(imagen.getId())
                    .url(imagen.getUrl())
                    .alt(imagen.getAlt())
                    .build();
        });

        when(imagenMapper.toImagen(any(ImagenDto.class))).thenAnswer(invocation -> {
            ImagenDto dto = invocation.getArgument(0);
            if (dto == null) return null;
            return Imagen.builder()
                    .id(dto.getId())
                    .url(dto.getUrl())
                    .alt(dto.getAlt())
                    .build();
        });
    }

    // ==================== TESTS TO DTO ====================

    @Test
    @DisplayName("toDto - Debería mapear Usuario a UsuarioDto correctamente")
    void toDto_ShouldMapUsuarioToUsuarioDtoCorrectly() {
        // Act
        UsuarioDto resultado = usuarioMapper.toDto(usuarioValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(usuarioValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(usuarioValido.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
        assertEquals(usuarioValido.getUsername(), resultado.getUsername(), "El username debería coincidir");
        assertEquals(usuarioValido.getPassword(), resultado.getPassword(), "La contraseña debería coincidir");
        assertEquals(usuarioValido.getIntroduccion(), resultado.getIntroduccion(), "La introducción debería coincidir");
        assertEquals(usuarioValido.getDescripcion(), resultado.getDescripcion(), "La descripción debería coincidir");
        assertEquals(usuarioValido.getRol(), resultado.getRol(), "El rol debería coincidir");
        assertEquals(usuarioValido.isActive(), resultado.isActive(), "El estado active debería coincidir");

        // Verificar que se llamó a ImagenMapper para las imágenes
        verify(imagenMapper, times(1)).toImagenDto(usuarioValido.getFotoPerfil());
        verify(imagenMapper, times(1)).toImagenDto(usuarioValido.getFotoPortada());
    }

    @Test
    @DisplayName("toDto - Debería mapear Usuario con imágenes nulas a UsuarioDto")
    void toDto_ShouldMapUsuarioWithNullImagesToUsuarioDto() {
        // Arrange - Usuario sin imágenes
        Usuario usuarioSinImagenes = Usuario.builder()
                .id(2)
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Administradora del sistema")
                .descripcion("Especialista en gestión de equipos.")
                .rol(Role.ADMIN)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        UsuarioDto resultado = usuarioMapper.toDto(usuarioSinImagenes);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(usuarioSinImagenes.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(usuarioSinImagenes.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
        assertNull(resultado.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(resultado.getFotoPortada(), "La foto de portada debería ser nula");

        // Verificar que no se llamó a ImagenMapper para imágenes nulas
        verify(imagenMapper, never()).toImagenDto(any());
    }

    @Test
    @DisplayName("toDto - Debería mapear Usuario con solo foto de perfil")
    void toDto_ShouldMapUsuarioWithOnlyProfilePicture() {
        // Arrange - Usuario solo con foto de perfil
        Usuario usuarioSoloPerfil = Usuario.builder()
                .id(3)
                .nombre("Carlos López")
                .username("carlos@email.com")
                .password("pass789")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .fotoPerfil(imagenPerfil)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        UsuarioDto resultado = usuarioMapper.toDto(usuarioSoloPerfil);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertNull(resultado.getFotoPortada(), "La foto de portada debería ser nula");

        // Verificar que se llamó a ImagenMapper solo para la foto de perfil
        verify(imagenMapper, times(1)).toImagenDto(usuarioSoloPerfil.getFotoPerfil());
        verify(imagenMapper, never()).toImagenDto(usuarioSoloPerfil.getFotoPortada());
    }

    @Test
    @DisplayName("toDto - Debería mapear Usuario con solo foto de portada")
    void toDto_ShouldMapUsuarioWithOnlyCoverPicture() {
        // Arrange - Usuario solo con foto de portada
        Usuario usuarioSoloPortada = Usuario.builder()
                .id(4)
                .nombre("Ana Martínez")
                .username("ana@email.com")
                .password("pass101")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(imagenPortada)
                .active(true)
                .build();

        // Act
        UsuarioDto resultado = usuarioMapper.toDto(usuarioSoloPortada);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNotNull(resultado.getFotoPortada(), "La foto de portada no debería ser nula");

        // Verificar que se llamó a ImagenMapper solo para la foto de portada
        verify(imagenMapper, never()).toImagenDto(usuarioSoloPortada.getFotoPerfil());
        verify(imagenMapper, times(1)).toImagenDto(usuarioSoloPortada.getFotoPortada());
    }

    @Test
    @DisplayName("toDto - Debería manejar Usuario nulo")
    void toDto_ShouldHandleNullUsuario() {
        // Act
        UsuarioDto resultado = usuarioMapper.toDto(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando el usuario es nulo");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagenDto(any());
    }

    // ==================== TESTS TO ENTITY ====================

    @Test
    @DisplayName("toEntity - Debería mapear UsuarioDto a Usuario correctamente")
    void toEntity_ShouldMapUsuarioDtoToUsuarioCorrectly() {
        // Act
        Usuario resultado = usuarioMapper.toEntity(usuarioDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(usuarioDtoValido.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(usuarioDtoValido.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
        assertEquals(usuarioDtoValido.getUsername(), resultado.getUsername(), "El username debería coincidir");
        assertEquals(usuarioDtoValido.getPassword(), resultado.getPassword(), "La contraseña debería coincidir");
        assertEquals(usuarioDtoValido.getIntroduccion(), resultado.getIntroduccion(), "La introducción debería coincidir");
        assertEquals(usuarioDtoValido.getDescripcion(), resultado.getDescripcion(), "La descripción debería coincidir");
        assertEquals(usuarioDtoValido.getRol(), resultado.getRol(), "El rol debería coincidir");
        assertEquals(usuarioDtoValido.isActive(), resultado.isActive(), "El estado active debería coincidir");

        // Verificar que se llamó a ImagenMapper para las imágenes
        verify(imagenMapper, times(1)).toImagen(usuarioDtoValido.getFotoPerfil());
        verify(imagenMapper, times(1)).toImagen(usuarioDtoValido.getFotoPortada());
    }

    @Test
    @DisplayName("toEntity - Debería mapear UsuarioDto con imágenes nulas a Usuario")
    void toEntity_ShouldMapUsuarioDtoWithNullImagesToUsuario() {
        // Arrange - UsuarioDto sin imágenes
        UsuarioDto usuarioDtoSinImagenes = UsuarioDto.builder()
                .id(2)
                .nombre("María García")
                .username("maria.garcia@email.com")
                .password("password456")
                .introduccion("Administradora del sistema")
                .descripcion("Especialista en gestión de equipos.")
                .rol(Role.ADMIN)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        Usuario resultado = usuarioMapper.toEntity(usuarioDtoSinImagenes);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(usuarioDtoSinImagenes.getId(), resultado.getId(), "El ID debería coincidir");
        assertEquals(usuarioDtoSinImagenes.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
        assertNull(resultado.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(resultado.getFotoPortada(), "La foto de portada debería ser nula");

        // Verificar que no se llamó a ImagenMapper para imágenes nulas
        verify(imagenMapper, never()).toImagen(any());
    }

    @Test
    @DisplayName("toEntity - Debería mapear UsuarioDto con solo foto de perfil")
    void toEntity_ShouldMapUsuarioDtoWithOnlyProfilePicture() {
        // Arrange - UsuarioDto solo con foto de perfil
        UsuarioDto usuarioDtoSoloPerfil = UsuarioDto.builder()
                .id(3)
                .nombre("Carlos López")
                .username("carlos@email.com")
                .password("pass789")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .fotoPerfil(imagenPerfilDto)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        Usuario resultado = usuarioMapper.toEntity(usuarioDtoSoloPerfil);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertNull(resultado.getFotoPortada(), "La foto de portada debería ser nula");

        // Verificar que se llamó a ImagenMapper solo para la foto de perfil
        verify(imagenMapper, times(1)).toImagen(usuarioDtoSoloPerfil.getFotoPerfil());
        verify(imagenMapper, never()).toImagen(usuarioDtoSoloPerfil.getFotoPortada());
    }

    @Test
    @DisplayName("toEntity - Debería mapear UsuarioDto con solo foto de portada")
    void toEntity_ShouldMapUsuarioDtoWithOnlyCoverPicture() {
        // Arrange - UsuarioDto solo con foto de portada
        UsuarioDto usuarioDtoSoloPortada = UsuarioDto.builder()
                .id(4)
                .nombre("Ana Martínez")
                .username("ana@email.com")
                .password("pass101")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(imagenPortadaDto)
                .active(true)
                .build();

        // Act
        Usuario resultado = usuarioMapper.toEntity(usuarioDtoSoloPortada);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNotNull(resultado.getFotoPortada(), "La foto de portada no debería ser nula");

        // Verificar que se llamó a ImagenMapper solo para la foto de portada
        verify(imagenMapper, never()).toImagen(usuarioDtoSoloPortada.getFotoPerfil());
        verify(imagenMapper, times(1)).toImagen(usuarioDtoSoloPortada.getFotoPortada());
    }

    @Test
    @DisplayName("toEntity - Debería manejar UsuarioDto nulo")
    void toEntity_ShouldHandleNullUsuarioDto() {
        // Act
        Usuario resultado = usuarioMapper.toEntity(null);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando el UsuarioDto es nulo");

        // Verificar que no se llamó a ImagenMapper
        verify(imagenMapper, never()).toImagen(any());
    }

    // ==================== TESTS DE CONSISTENCIA ====================

    @Test
    @DisplayName("toDto y toEntity - Deberían ser consistentes (round-trip)")
    void toDtoAndToEntity_ShouldBeConsistent() {
        // Act - Convertir de Usuario a UsuarioDto y luego de vuelta a Usuario
        UsuarioDto dto = usuarioMapper.toDto(usuarioValido);
        Usuario usuarioConvertido = usuarioMapper.toEntity(dto);

        // Assert - Verificar que el round-trip mantiene los datos
        assertNotNull(dto, "El DTO no debería ser nulo");
        assertNotNull(usuarioConvertido, "El usuario convertido no debería ser nulo");
        assertEquals(usuarioValido.getId(), usuarioConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(usuarioValido.getNombre(), usuarioConvertido.getNombre(), "El nombre debería ser el mismo");
        assertEquals(usuarioValido.getUsername(), usuarioConvertido.getUsername(), "El username debería ser el mismo");
        assertEquals(usuarioValido.getPassword(), usuarioConvertido.getPassword(), "La contraseña debería ser la misma");
        assertEquals(usuarioValido.getRol(), usuarioConvertido.getRol(), "El rol debería ser el mismo");
        assertEquals(usuarioValido.isActive(), usuarioConvertido.isActive(), "El estado active debería ser el mismo");

        // Verificar que las imágenes se mapearon correctamente en ambos sentidos
        assertNotNull(usuarioConvertido.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertEquals(usuarioValido.getFotoPerfil().getId(), usuarioConvertido.getFotoPerfil().getId(),
                "El ID de la foto de perfil debería coincidir");
        assertNotNull(usuarioConvertido.getFotoPortada(), "La foto de portada no debería ser nula");
        assertEquals(usuarioValido.getFotoPortada().getId(), usuarioConvertido.getFotoPortada().getId(),
                "El ID de la foto de portada debería coincidir");
    }

    @Test
    @DisplayName("toDto y toEntity - Deberían ser consistentes con objetos nulos")
    void toDtoAndToEntity_ShouldBeConsistentWithNullObjects() {
        // Arrange - UsuarioDto sin imágenes
        UsuarioDto dtoSinImagenes = UsuarioDto.builder()
                .id(10)
                .nombre("Usuario Test")
                .username("test@email.com")
                .password("pass123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act - Convertir a Usuario y luego de vuelta a UsuarioDto
        Usuario usuario = usuarioMapper.toEntity(dtoSinImagenes);
        UsuarioDto dtoConvertido = usuarioMapper.toDto(usuario);

        // Assert
        assertNotNull(usuario, "El usuario no debería ser nulo");
        assertNotNull(dtoConvertido, "El DTO convertido no debería ser nulo");
        assertEquals(dtoSinImagenes.getId(), dtoConvertido.getId(), "El ID debería ser el mismo");
        assertEquals(dtoSinImagenes.getNombre(), dtoConvertido.getNombre(), "El nombre debería ser el mismo");
        assertNull(dtoConvertido.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(dtoConvertido.getFotoPortada(), "La foto de portada debería ser nula");
    }

    // ==================== TESTS CON DIFERENTES ROLES ====================

    @Test
    @DisplayName("toDto - Debería mapear correctamente usuario con rol ADMIN")
    void toDto_ShouldMapUsuarioWithAdminRoleCorrectly() {
        // Arrange - Usuario con rol ADMIN
        Usuario usuarioAdmin = Usuario.builder()
                .id(5)
                .nombre("Admin User")
                .username("admin@email.com")
                .password("adminPass")
                .introduccion("Introducción del administrador")
                .descripcion("Descripción del administrador")
                .rol(Role.ADMIN)
                .fotoPerfil(imagenPerfil)
                .fotoPortada(imagenPortada)
                .active(true)
                .build();

        // Act
        UsuarioDto resultado = usuarioMapper.toDto(usuarioAdmin);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(Role.ADMIN, resultado.getRol(), "El rol debería ser ADMIN");
        assertEquals(usuarioAdmin.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
    }

    @Test
    @DisplayName("toEntity - Debería mapear correctamente UsuarioDto con rol ADMIN")
    void toEntity_ShouldMapUsuarioDtoWithAdminRoleCorrectly() {
        // Arrange - UsuarioDto con rol ADMIN
        UsuarioDto usuarioDtoAdmin = UsuarioDto.builder()
                .id(6)
                .nombre("Admin User")
                .username("admin@email.com")
                .password("adminPass")
                .introduccion("Introducción del administrador")
                .descripcion("Descripción del administrador")
                .rol(Role.ADMIN)
                .fotoPerfil(imagenPerfilDto)
                .fotoPortada(imagenPortadaDto)
                .active(true)
                .build();

        // Act
        Usuario resultado = usuarioMapper.toEntity(usuarioDtoAdmin);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(Role.ADMIN, resultado.getRol(), "El rol debería ser ADMIN");
        assertEquals(usuarioDtoAdmin.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
    }

    // ==================== TESTS DE CASOS BORDE ====================

    @Test
    @DisplayName("toDto - Debería manejar usuario con campos vacíos")
    void toDto_ShouldHandleUsuarioWithEmptyFields() {
        // Arrange - Usuario con campos vacíos
        Usuario usuarioVacio = Usuario.builder()
                .id(7)
                .nombre("")
                .username("")
                .password("")
                .introduccion("")
                .descripcion("")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        UsuarioDto resultado = usuarioMapper.toDto(usuarioVacio);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getNombre(), "El nombre vacío debería mantenerse");
        assertEquals("", resultado.getUsername(), "El username vacío debería mantenerse");
        assertEquals("", resultado.getPassword(), "La contraseña vacía debería mantenerse");
        assertNull(resultado.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(resultado.getFotoPortada(), "La foto de portada debería ser nula");
    }

    @Test
    @DisplayName("toEntity - Debería manejar UsuarioDto con campos vacíos")
    void toEntity_ShouldHandleUsuarioDtoWithEmptyFields() {
        // Arrange - UsuarioDto con campos vacíos
        UsuarioDto usuarioDtoVacio = UsuarioDto.builder()
                .id(8)
                .nombre("")
                .username("")
                .password("")
                .introduccion("")
                .descripcion("")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        Usuario resultado = usuarioMapper.toEntity(usuarioDtoVacio);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("", resultado.getNombre(), "El nombre vacío debería mantenerse");
        assertEquals("", resultado.getUsername(), "El username vacío debería mantenerse");
        assertEquals("", resultado.getPassword(), "La contraseña vacía debería mantenerse");
        assertNull(resultado.getFotoPerfil(), "La foto de perfil debería ser nula");
        assertNull(resultado.getFotoPortada(), "La foto de portada debería ser nula");
    }

    @Test
    @DisplayName("toDto - Debería manejar usuario con ID nulo")
    void toDto_ShouldHandleUsuarioWithNullId() {
        // Arrange - Usuario con ID nulo
        Usuario usuarioSinId = Usuario.builder()
                .id(null)
                .nombre("Usuario Sin ID")
                .username("sinid@email.com")
                .password("pass123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        UsuarioDto resultado = usuarioMapper.toDto(usuarioSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals(usuarioSinId.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
    }

    @Test
    @DisplayName("toEntity - Debería manejar UsuarioDto con ID nulo")
    void toEntity_ShouldHandleUsuarioDtoWithNullId() {
        // Arrange - UsuarioDto con ID nulo
        UsuarioDto usuarioDtoSinId = UsuarioDto.builder()
                .id(null)
                .nombre("Usuario Sin ID")
                .username("sinid@email.com")
                .password("pass123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        Usuario resultado = usuarioMapper.toEntity(usuarioDtoSinId);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNull(resultado.getId(), "El ID debería ser nulo");
        assertEquals(usuarioDtoSinId.getNombre(), resultado.getNombre(), "El nombre debería coincidir");
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que ImagenMapper se usa correctamente en toDto")
    void verifyImagenMapperUsedCorrectlyInToDto() {
        // Act
        usuarioMapper.toDto(usuarioValido);

        // Assert - Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagenDto(usuarioValido.getFotoPerfil());
        verify(imagenMapper, times(1)).toImagenDto(usuarioValido.getFotoPortada());
    }

    @Test
    @DisplayName("Verificar que ImagenMapper se usa correctamente en toEntity")
    void verifyImagenMapperUsedCorrectlyInToEntity() {
        // Act
        usuarioMapper.toEntity(usuarioDtoValido);

        // Assert - Verificar que se llamó a ImagenMapper
        verify(imagenMapper, times(1)).toImagen(usuarioDtoValido.getFotoPerfil());
        verify(imagenMapper, times(1)).toImagen(usuarioDtoValido.getFotoPortada());
    }

    @Test
    @DisplayName("Verificar que no hay interacciones no deseadas con ImagenMapper")
    void verifyNoUnexpectedInteractionsWithImagenMapper() {
        // Arrange - Usuario sin imágenes
        Usuario usuarioSinImagenes = Usuario.builder()
                .id(9)
                .nombre("Sin Imágenes")
                .username("sinimagenes@email.com")
                .password("pass123")
                .introduccion("Introducción válida")
                .descripcion("Descripción válida")
                .rol(Role.USER)
                .fotoPerfil(null)
                .fotoPortada(null)
                .active(true)
                .build();

        // Act
        usuarioMapper.toDto(usuarioSinImagenes);

        // Assert - Verificar que no hay interacciones
        verify(imagenMapper, never()).toImagenDto(any());
        verify(imagenMapper, never()).toImagen(any());
    }

    // ==================== TESTS CON OBJETOS COMPLEJOS ====================

    @Test
    @DisplayName("toDto - Debería mapear usuario con imágenes que tienen todos los campos")
    void toDto_ShouldMapUsuarioWithFullImages() {
        // Arrange - Imágenes con todos los campos
        Imagen imagenCompletaPerfil = Imagen.builder()
                .id(10)
                .url("completa-perfil.jpg")
                .alt("Imagen de perfil completa")
                .build();

        Imagen imagenCompletaPortada = Imagen.builder()
                .id(11)
                .url("completa-portada.jpg")
                .alt("Imagen de portada completa")
                .build();

        Usuario usuarioCompleto = Usuario.builder()
                .id(12)
                .nombre("Usuario Completo")
                .username("completo@email.com")
                .password("passCompleta")
                .introduccion("Introducción completa")
                .descripcion("Descripción completa")
                .rol(Role.ADMIN)
                .fotoPerfil(imagenCompletaPerfil)
                .fotoPortada(imagenCompletaPortada)
                .active(false)
                .build();

        // Configurar mock para imágenes completas
        when(imagenMapper.toImagenDto(imagenCompletaPerfil)).thenReturn(
                ImagenDto.builder()
                        .id(10)
                        .url("completa-perfil.jpg")
                        .alt("Imagen de perfil completa")
                        .build()
        );
        when(imagenMapper.toImagenDto(imagenCompletaPortada)).thenReturn(
                ImagenDto.builder()
                        .id(11)
                        .url("completa-portada.jpg")
                        .alt("Imagen de portada completa")
                        .build()
        );

        // Act
        UsuarioDto resultado = usuarioMapper.toDto(usuarioCompleto);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertEquals(10, resultado.getFotoPerfil().getId(), "El ID de la foto de perfil debería ser 10");
        assertEquals("completa-perfil.jpg", resultado.getFotoPerfil().getUrl(), "La URL de la foto de perfil debería coincidir");
        assertEquals("Imagen de perfil completa", resultado.getFotoPerfil().getAlt(), "El alt de la foto de perfil debería coincidir");
        assertNotNull(resultado.getFotoPortada(), "La foto de portada no debería ser nula");
        assertEquals(11, resultado.getFotoPortada().getId(), "El ID de la foto de portada debería ser 11");
        assertEquals("completa-portada.jpg", resultado.getFotoPortada().getUrl(), "La URL de la foto de portada debería coincidir");
        assertEquals("Imagen de portada completa", resultado.getFotoPortada().getAlt(), "El alt de la foto de portada debería coincidir");
        assertFalse(resultado.isActive(), "El usuario debería estar inactivo");

        // Verificar que se llamó a ImagenMapper para ambas imágenes
        verify(imagenMapper, times(1)).toImagenDto(imagenCompletaPerfil);
        verify(imagenMapper, times(1)).toImagenDto(imagenCompletaPortada);
    }

    @Test
    @DisplayName("toEntity - Debería mapear UsuarioDto con imágenes que tienen todos los campos")
    void toEntity_ShouldMapUsuarioDtoWithFullImages() {
        // Arrange - Imágenes DTO con todos los campos
        ImagenDto imagenDtoCompletaPerfil = ImagenDto.builder()
                .id(13)
                .url("completa-perfil-dto.jpg")
                .alt("Imagen de perfil DTO completa")
                .build();

        ImagenDto imagenDtoCompletaPortada = ImagenDto.builder()
                .id(14)
                .url("completa-portada-dto.jpg")
                .alt("Imagen de portada DTO completa")
                .build();

        UsuarioDto usuarioDtoCompleto = UsuarioDto.builder()
                .id(15)
                .nombre("Usuario DTO Completo")
                .username("dto.completo@email.com")
                .password("passDtoCompleta")
                .introduccion("Introducción DTO completa")
                .descripcion("Descripción DTO completa")
                .rol(Role.ADMIN)
                .fotoPerfil(imagenDtoCompletaPerfil)
                .fotoPortada(imagenDtoCompletaPortada)
                .active(false)
                .build();

        // Configurar mock para imágenes DTO completas
        when(imagenMapper.toImagen(imagenDtoCompletaPerfil)).thenReturn(
                Imagen.builder()
                        .id(13)
                        .url("completa-perfil-dto.jpg")
                        .alt("Imagen de perfil DTO completa")
                        .build()
        );
        when(imagenMapper.toImagen(imagenDtoCompletaPortada)).thenReturn(
                Imagen.builder()
                        .id(14)
                        .url("completa-portada-dto.jpg")
                        .alt("Imagen de portada DTO completa")
                        .build()
        );

        // Act
        Usuario resultado = usuarioMapper.toEntity(usuarioDtoCompleto);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertNotNull(resultado.getFotoPerfil(), "La foto de perfil no debería ser nula");
        assertEquals(13, resultado.getFotoPerfil().getId(), "El ID de la foto de perfil debería ser 13");
        assertEquals("completa-perfil-dto.jpg", resultado.getFotoPerfil().getUrl(), "La URL de la foto de perfil debería coincidir");
        assertEquals("Imagen de perfil DTO completa", resultado.getFotoPerfil().getAlt(), "El alt de la foto de perfil debería coincidir");
        assertNotNull(resultado.getFotoPortada(), "La foto de portada no debería ser nula");
        assertEquals(14, resultado.getFotoPortada().getId(), "El ID de la foto de portada debería ser 14");
        assertEquals("completa-portada-dto.jpg", resultado.getFotoPortada().getUrl(), "La URL de la foto de portada debería coincidir");
        assertEquals("Imagen de portada DTO completa", resultado.getFotoPortada().getAlt(), "El alt de la foto de portada debería coincidir");
        assertFalse(resultado.isActive(), "El usuario debería estar inactivo");

        // Verificar que se llamó a ImagenMapper para ambas imágenes
        verify(imagenMapper, times(1)).toImagen(imagenDtoCompletaPerfil);
        verify(imagenMapper, times(1)).toImagen(imagenDtoCompletaPortada);
    }
}