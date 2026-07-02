package com.example.demo.service;

import com.example.demo.dto.ImagenDto;
import com.example.demo.dto.UsuarioDto;
import com.example.demo.enums.Role;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.Impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UsuarioServiceImplIntegrationTest {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioDto crearUsuarioDto(String nombre, String username, String password,
                                       String introduccion, String descripcion, Role rol, boolean active) {
        return UsuarioDto.builder()
                .nombre(nombre)
                .username(username)
                .password(password)
                .introduccion(introduccion)
                .descripcion(descripcion)
                .rol(rol)
                .active(active)
                .build();
    }

    private UsuarioDto crearUsuarioDtoConImagenes(String nombre, String username, String password,
                                                  String introduccion, String descripcion, Role rol,
                                                  boolean active, String urlPerfil, String altPerfil,
                                                  String urlPortada, String altPortada) {
        UsuarioDto usuarioDto = crearUsuarioDto(nombre, username, password, introduccion, descripcion, rol, active);

        if (urlPerfil != null) {
            ImagenDto fotoPerfil = ImagenDto.builder()
                    .url(urlPerfil)
                    .alt(altPerfil)
                    .build();
            usuarioDto.setFotoPerfil(fotoPerfil);
        }

        if (urlPortada != null) {
            ImagenDto fotoPortada = ImagenDto.builder()
                    .url(urlPortada)
                    .alt(altPortada)
                    .build();
            usuarioDto.setFotoPortada(fotoPortada);
        }

        return usuarioDto;
    }

    @BeforeEach
    void setUp() {
        // Limpiar datos antes de cada prueba
        usuarioRepository.deleteAll();
    }

    @Test
    void create_conDatosValidos_debeGuardarYRetornarUsuario() {
        // ARRANGE - contexto: usuario con datos válidos
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Juan Pérez",
                "juan@ejemplo.com",
                "password123",
                "Introducción de Juan",
                "Descripción de Juan",
                Role.USER,
                true
        );

        // ACT - acción: crear usuario
        UsuarioDto resultado = usuarioService.create(usuarioDto);

        // ASSERT - validaciones: verificar que el usuario fue guardado correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Juan Pérez", resultado.getNombre(), "Nombre debe coincidir"),
                () -> assertEquals("juan@ejemplo.com", resultado.getUsername(), "Username debe coincidir"),
                () -> assertEquals("Introducción de Juan", resultado.getIntroduccion(),
                        "Introducción debe coincidir"),
                () -> assertEquals("Descripción de Juan", resultado.getDescripcion(),
                        "Descripción debe coincidir"),
                () -> assertEquals(Role.USER, resultado.getRol(), "Rol debe coincidir"),
                () -> assertTrue(resultado.isActive(), "Active debe ser true"),
                () -> assertTrue(usuarioRepository.existsById(resultado.getId()),
                        "Usuario debe existir en la base de datos")
        );
    }

    @Test
    void create_conUsernameDuplicado_debeLanzarExcepcion() {
        // ARRANGE - contexto: crear y guardar un usuario existente
        Usuario usuarioExistente = Usuario.builder()
                .nombre("Usuario Existente")
                .username("existente@ejemplo.com")
                .password("password123")
                .introduccion("Introducción existente")
                .descripcion("Descripción existente")
                .rol(Role.USER)
                .active(true)
                .build();
        usuarioRepository.save(usuarioExistente);

        // Crear un nuevo usuario con el mismo username
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Nuevo Usuario",
                "existente@ejemplo.com", // Mismo username
                "password456",
                "Introducción nueva",
                "Descripción nueva",
                Role.ADMIN,
                true
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción por username duplicado
        assertThrows(DataIntegrityViolationException.class, () -> {
            usuarioService.create(usuarioDto);
        }, "Debe lanzar DataIntegrityViolationException cuando el username está duplicado");

        // Verificar que solo existe el usuario original
        assertEquals(1, usuarioRepository.count(), "Solo debe existir un usuario en la BD");
    }

    @Test
    void create_conNombreNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: usuario con nombre nulo
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre(null)
                .username("juan@ejemplo.com")
                .password("password123")
                .introduccion("Introducción")
                .descripcion("Descripción")
                .rol(Role.USER)
                .active(true)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioService.create(usuarioDto);
        }, "Debe lanzar excepción cuando el nombre es nulo");
    }

    @Test
    void create_conUsernameNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: usuario con username nulo
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Juan Pérez")
                .username(null)
                .password("password123")
                .introduccion("Introducción")
                .descripcion("Descripción")
                .rol(Role.USER)
                .active(true)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioService.create(usuarioDto);
        }, "Debe lanzar excepción cuando el username es nulo");
    }

    @Test
    void create_conPasswordNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: usuario con password nulo
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Juan Pérez")
                .username("juan@ejemplo.com")
                .password(null)
                .introduccion("Introducción")
                .descripcion("Descripción")
                .rol(Role.USER)
                .active(true)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioService.create(usuarioDto);
        }, "Debe lanzar excepción cuando el password es nulo");
    }

    @Test
    void create_conIntroduccionMuyCorta_debeLanzarExcepcion() {
        // ARRANGE - contexto: usuario con introducción muy corta
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Juan Pérez",
                "juan@ejemplo.com",
                "password123",
                "Hola", // Menos de 5 caracteres
                "Descripción válida",
                Role.USER,
                true
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioService.create(usuarioDto);
        }, "Debe lanzar excepción cuando la introducción es muy corta");
    }

    @Test
    void create_conDescripcionMuyCorta_debeLanzarExcepcion() {
        // ARRANGE - contexto: usuario con descripción muy corta
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Juan Pérez",
                "juan@ejemplo.com",
                "password123",
                "Introducción válida",
                "Desc", // Menos de 5 caracteres
                Role.USER,
                true
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioService.create(usuarioDto);
        }, "Debe lanzar excepción cuando la descripción es muy corta");
    }

    @Test
    void create_conRolNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: usuario con rol nulo
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Juan Pérez")
                .username("juan@ejemplo.com")
                .password("password123")
                .introduccion("Introducción")
                .descripcion("Descripción")
                .rol(null)
                .active(true)
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioService.create(usuarioDto);
        }, "Debe lanzar excepción cuando el rol es nulo");
    }

    @Test
    void create_conEmailInvalido_debeLanzarExcepcion() {
        // ARRANGE - contexto: usuario con email inválido
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Juan Pérez",
                "email-invalido", // Sin @
                "password123",
                "Introducción válida",
                "Descripción válida",
                Role.USER,
                true
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioService.create(usuarioDto);
        }, "Debe lanzar excepción cuando el email es inválido");
    }

    @Test
    void create_conFotoPerfilYPortada_debeGuardarCorrectamente() {
        // ARRANGE - contexto: usuario con fotos de perfil y portada
        UsuarioDto usuarioDto = crearUsuarioDtoConImagenes(
                "María García",
                "maria@ejemplo.com",
                "password123",
                "Introducción de María",
                "Descripción de María",
                Role.USER,
                true,
                "https://example.com/perfil.jpg",
                "Foto de perfil de María",
                "https://example.com/portada.jpg",
                "Foto de portada de María"
        );

        // ACT - acción: crear usuario
        UsuarioDto resultado = usuarioService.create(usuarioDto);

        // ASSERT - validaciones: verificar que las imágenes fueron guardadas
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertNotNull(resultado.getFotoPerfil(), "Foto de perfil debe estar presente"),
                () -> assertEquals("https://example.com/perfil.jpg",
                        resultado.getFotoPerfil().getUrl(), "URL de perfil debe coincidir"),
                () -> assertEquals("Foto de perfil de María",
                        resultado.getFotoPerfil().getAlt(), "Alt de perfil debe coincidir"),
                () -> assertNotNull(resultado.getFotoPortada(), "Foto de portada debe estar presente"),
                () -> assertEquals("https://example.com/portada.jpg",
                        resultado.getFotoPortada().getUrl(), "URL de portada debe coincidir"),
                () -> assertEquals("Foto de portada de María",
                        resultado.getFotoPortada().getAlt(), "Alt de portada debe coincidir")
        );
    }

    @Test
    void create_conSoloFotoPerfil_debeGuardarCorrectamente() {
        // ARRANGE - contexto: usuario solo con foto de perfil
        UsuarioDto usuarioDto = crearUsuarioDtoConImagenes(
                "Carlos López",
                "carlos@ejemplo.com",
                "password123",
                "Introducción de Carlos",
                "Descripción de Carlos",
                Role.USER,
                true,
                "https://example.com/perfil_carlos.jpg",
                "Foto de perfil de Carlos",
                null,
                null
        );

        // ACT - acción: crear usuario
        UsuarioDto resultado = usuarioService.create(usuarioDto);

        // ASSERT - validaciones: verificar que solo se guardó la foto de perfil
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertNotNull(resultado.getFotoPerfil(), "Foto de perfil debe estar presente"),
                () -> assertEquals("https://example.com/perfil_carlos.jpg",
                        resultado.getFotoPerfil().getUrl(), "URL de perfil debe coincidir"),
                () -> assertNull(resultado.getFotoPortada(), "Foto de portada debe ser null")
        );
    }

    @Test
    void getById_conIdExistente_debeRetornarUsuario() {
        // ARRANGE - contexto: crear y guardar usuario
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Buscado")
                .username("buscado@ejemplo.com")
                .password("password123")
                .introduccion("Introducción del usuario")
                .descripcion("Descripción del usuario")
                .rol(Role.USER)
                .active(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        // ACT - acción: buscar usuario por ID
        UsuarioDto encontrado = usuarioService.getById(guardado.getId());

        // ASSERT - validaciones: debe encontrar el usuario correcto
        assertAll(
                () -> assertNotNull(encontrado, "Usuario no debe ser null"),
                () -> assertEquals(guardado.getId(), encontrado.getId(), "IDs deben coincidir"),
                () -> assertEquals("Usuario Buscado", encontrado.getNombre(), "Nombre debe coincidir"),
                () -> assertEquals("buscado@ejemplo.com", encontrado.getUsername(), "Username debe coincidir"),
                () -> assertEquals("Introducción del usuario", encontrado.getIntroduccion(),
                        "Introducción debe coincidir"),
                () -> assertEquals("Descripción del usuario", encontrado.getDescripcion(),
                        "Descripción debe coincidir"),
                () -> assertEquals(Role.USER, encontrado.getRol(), "Rol debe coincidir"),
                () -> assertTrue(encontrado.isActive(), "Active debe ser true")
        );
    }

    @Test
    void getById_conIdExistenteConImagenes_debeRetornarUsuarioConImagenes() {
        // ARRANGE - contexto: crear usuario con imágenes
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Con Fotos")
                .username("fotos@ejemplo.com")
                .password("password123")
                .introduccion("Introducción con fotos")
                .descripcion("Descripción con fotos")
                .rol(Role.USER)
                .active(true)
                .build();

        Imagen fotoPerfil = Imagen.builder()
                .url("https://example.com/perfil_usuario.jpg")
                .alt("Perfil del usuario")
                .usuario(usuario)
                .build();
        usuario.setFotoPerfil(fotoPerfil);

        Imagen fotoPortada = Imagen.builder()
                .url("https://example.com/portada_usuario.jpg")
                .alt("Portada del usuario")
                .usuario(usuario)
                .build();
        usuario.setFotoPortada(fotoPortada);

        Usuario guardado = usuarioRepository.save(usuario);

        // ACT - acción: buscar usuario por ID
        UsuarioDto encontrado = usuarioService.getById(guardado.getId());

        // ASSERT - validaciones: verificar que las imágenes están presentes
        assertAll(
                () -> assertNotNull(encontrado, "Usuario no debe ser null"),
                () -> assertNotNull(encontrado.getFotoPerfil(), "Foto de perfil debe estar presente"),
                () -> assertEquals("https://example.com/perfil_usuario.jpg",
                        encontrado.getFotoPerfil().getUrl(), "URL de perfil debe coincidir"),
                () -> assertNotNull(encontrado.getFotoPortada(), "Foto de portada debe estar presente"),
                () -> assertEquals("https://example.com/portada_usuario.jpg",
                        encontrado.getFotoPortada().getUrl(), "URL de portada debe coincidir")
        );
    }

    @Test
    void getById_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(RuntimeException.class, () -> {
            usuarioService.getById(idInexistente);
        }, "Debe lanzar RuntimeException para ID inexistente");
    }

    @Test
    void getByUsername_conUsernameExistente_debeRetornarUsuario() {
        // ARRANGE - contexto: crear y guardar usuario
        Usuario usuario = Usuario.builder()
                .nombre("Usuario por Username")
                .username("username@ejemplo.com")
                .password("password123")
                .introduccion("Introducción por username")
                .descripcion("Descripción por username")
                .rol(Role.ADMIN)
                .active(true)
                .build();

        usuarioRepository.save(usuario);

        // ACT - acción: buscar usuario por username
        UsuarioDto encontrado = usuarioService.getByUsername("username@ejemplo.com");

        // ASSERT - validaciones: debe encontrar el usuario correcto
        assertAll(
                () -> assertNotNull(encontrado, "Usuario no debe ser null"),
                () -> assertEquals("Usuario por Username", encontrado.getNombre(), "Nombre debe coincidir"),
                () -> assertEquals("username@ejemplo.com", encontrado.getUsername(), "Username debe coincidir"),
                () -> assertEquals("Introducción por username", encontrado.getIntroduccion(),
                        "Introducción debe coincidir"),
                () -> assertEquals("Descripción por username", encontrado.getDescripcion(),
                        "Descripción debe coincidir"),
                () -> assertEquals(Role.ADMIN, encontrado.getRol(), "Rol debe coincidir"),
                () -> assertTrue(encontrado.isActive(), "Active debe ser true")
        );
    }

    @Test
    void getByUsername_conUsernameInexistente_debeRetornarNull() {
        // ARRANGE - contexto: username que no existe
        String usernameInexistente = "inexistente@ejemplo.com";

        // ACT - acción: buscar usuario por username inexistente
        UsuarioDto encontrado = usuarioService.getByUsername(usernameInexistente);

        // ASSERT - validaciones: debe retornar null (el método catch la excepción y retorna null)
        assertNull(encontrado, "Debe retornar null para username inexistente");
    }

    @Test
    void update_conDatosValidos_debeActualizarCorrectamente() {
        // ARRANGE - contexto: crear y guardar usuario
        Usuario usuario = Usuario.builder()
                .nombre("Nombre Original")
                .username("original@ejemplo.com")
                .password("password123")
                .introduccion("Introducción original")
                .descripcion("Descripción original")
                .rol(Role.USER)
                .active(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        // Preparar datos actualizados
        UsuarioDto usuarioDtoActualizado = crearUsuarioDto(
                "Nombre Actualizado",
                "original@ejemplo.com",
                "password456",
                "Introducción actualizada",
                "Descripción actualizada",
                Role.ADMIN,
                true
        );

        // ACT - acción: actualizar usuario
        UsuarioDto actualizado = usuarioService.update(guardado.getId(), usuarioDtoActualizado);

        // ASSERT - validaciones: debe actualizar correctamente
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("Nombre Actualizado", actualizado.getNombre(), "Nombre debe estar actualizado"),
                () -> assertEquals("original@ejemplo.com", actualizado.getUsername(), "Username debe mantenerse"),
                () -> assertEquals("Introducción actualizada", actualizado.getIntroduccion(),
                        "Introducción debe estar actualizada"),
                () -> assertEquals("Descripción actualizada", actualizado.getDescripcion(),
                        "Descripción debe estar actualizada"),
                () -> assertEquals(Role.ADMIN, actualizado.getRol(), "Rol debe estar actualizado"),
                () -> assertTrue(actualizado.isActive(), "Active debe mantenerse"),
                () -> assertTrue(usuarioRepository.existsById(guardado.getId()),
                        "Usuario debe seguir existiendo en la BD")
        );
    }

    @Test
    void update_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Nombre Actualizado",
                "actualizado@ejemplo.com",
                "password456",
                "Introducción actualizada",
                "Descripción actualizada",
                Role.ADMIN,
                true
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(RuntimeException.class, () -> {
            usuarioService.update(idInexistente, usuarioDto);
        }, "Debe lanzar RuntimeException para ID inexistente");
    }

    @Test
    void update_conUsernameInvalido_debeLanzarExcepcion() {
        // ARRANGE - contexto: crear y guardar usuario
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Test")
                .username("test@ejemplo.com")
                .password("password123")
                .introduccion("Introducción test")
                .descripcion("Descripción test")
                .rol(Role.USER)
                .active(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        // Preparar datos con username inválido
        UsuarioDto usuarioDtoActualizado = crearUsuarioDto(
                "Nombre Actualizado",
                "username-invalido", // Email sin @
                "password456",
                "Introducción actualizada",
                "Descripción actualizada",
                Role.ADMIN,
                true
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioService.update(guardado.getId(), usuarioDtoActualizado);
        }, "Debe lanzar excepción cuando el username es inválido");
    }

    @Test
    void update_conFotoPerfilNueva_debeActualizarImagen() {
        // ARRANGE - contexto: crear y guardar usuario con foto de perfil
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Inicial")
                .username("inicial@ejemplo.com")
                .password("password123")
                .introduccion("Introducción inicial")
                .descripcion("Descripción inicial")
                .rol(Role.USER)
                .active(true)
                .build();

        Imagen fotoPerfilVieja = Imagen.builder()
                .url("https://example.com/perfil_viejo.jpg")
                .alt("Foto vieja")
                .usuario(usuario)
                .build();
        usuario.setFotoPerfil(fotoPerfilVieja);

        Usuario guardado = usuarioRepository.save(usuario);

        // Preparar datos con nueva foto de perfil
        UsuarioDto usuarioDtoActualizado = crearUsuarioDtoConImagenes(
                "Usuario Actualizado",
                "inicial@ejemplo.com",
                "password456",
                "Introducción actualizada",
                "Descripción actualizada",
                Role.ADMIN,
                true,
                "https://example.com/perfil_nuevo.jpg",
                "Foto nueva",
                null,
                null
        );

        // ACT - acción: actualizar usuario con nueva foto
        UsuarioDto actualizado = usuarioService.update(guardado.getId(), usuarioDtoActualizado);

        // ASSERT - validaciones: verificar que la foto fue actualizada
        assertAll(
                () -> assertNotNull(actualizado.getFotoPerfil(), "Foto de perfil debe estar presente"),
                () -> assertEquals("https://example.com/perfil_nuevo.jpg",
                        actualizado.getFotoPerfil().getUrl(), "URL de perfil debe ser la nueva"),
                () -> assertEquals("Foto nueva",
                        actualizado.getFotoPerfil().getAlt(), "Alt de perfil debe ser el nuevo")
        );
    }

    @Test
    void update_conFotoPortadaNueva_debeActualizarImagen() {
        // ARRANGE - contexto: crear y guardar usuario con foto de portada
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Inicial")
                .username("inicial@ejemplo.com")
                .password("password123")
                .introduccion("Introducción inicial")
                .descripcion("Descripción inicial")
                .rol(Role.USER)
                .active(true)
                .build();

        Imagen fotoPortadaVieja = Imagen.builder()
                .url("https://example.com/portada_vieja.jpg")
                .alt("Portada vieja")
                .usuario(usuario)
                .build();
        usuario.setFotoPortada(fotoPortadaVieja);

        Usuario guardado = usuarioRepository.save(usuario);

        // Preparar datos con nueva foto de portada
        UsuarioDto usuarioDtoActualizado = crearUsuarioDtoConImagenes(
                "Usuario Actualizado",
                "inicial@ejemplo.com",
                "password456",
                "Introducción actualizada",
                "Descripción actualizada",
                Role.ADMIN,
                true,
                null,
                null,
                "https://example.com/portada_nueva.jpg",
                "Portada nueva"
        );

        // ACT - acción: actualizar usuario con nueva foto de portada
        UsuarioDto actualizado = usuarioService.update(guardado.getId(), usuarioDtoActualizado);

        // ASSERT - validaciones: verificar que la foto de portada fue actualizada
        assertAll(
                () -> assertNotNull(actualizado.getFotoPortada(), "Foto de portada debe estar presente"),
                () -> assertEquals("https://example.com/portada_nueva.jpg",
                        actualizado.getFotoPortada().getUrl(), "URL de portada debe ser la nueva"),
                () -> assertEquals("Portada nueva",
                        actualizado.getFotoPortada().getAlt(), "Alt de portada debe ser el nuevo")
        );
    }

    @Test
    void update_eliminarFotoPerfil_debeMantenerUsuarioSinFoto() {
        // ARRANGE - contexto: crear y guardar usuario con foto de perfil
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Test")
                .username("test@ejemplo.com")
                .password("password123")
                .introduccion("Introducción test")
                .descripcion("Descripción test")
                .rol(Role.USER)
                .active(true)
                .build();

        Imagen fotoPerfil = Imagen.builder()
                .url("https://example.com/perfil_test.jpg")
                .alt("Foto test")
                .usuario(usuario)
                .build();
        usuario.setFotoPerfil(fotoPerfil);

        Usuario guardado = usuarioRepository.save(usuario);

        // Preparar datos sin foto de perfil
        UsuarioDto usuarioDtoActualizado = crearUsuarioDto(
                "Usuario Actualizado",
                "test@ejemplo.com",
                "password456",
                "Introducción actualizada",
                "Descripción actualizada",
                Role.ADMIN,
                true
        );

        // ACT - acción: actualizar usuario sin foto
        UsuarioDto actualizado = usuarioService.update(guardado.getId(), usuarioDtoActualizado);

        // ASSERT - validaciones: verificar que la foto fue eliminada
        assertNull(actualizado.getFotoPerfil(), "Foto de perfil debe ser null");
    }

    @Test
    void update_conIntroduccionMuyLarga_debeLanzarExcepcion() {
        // ARRANGE - contexto: crear y guardar usuario
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Test")
                .username("test@ejemplo.com")
                .password("password123")
                .introduccion("Introducción test")
                .descripcion("Descripción test")
                .rol(Role.USER)
                .active(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        // Preparar datos con introducción muy larga (más de 500 caracteres)
        String introduccionLarga = "a".repeat(501);
        UsuarioDto usuarioDtoActualizado = crearUsuarioDto(
                "Nombre Actualizado",
                "test@ejemplo.com",
                "password456",
                introduccionLarga,
                "Descripción actualizada",
                Role.ADMIN,
                true
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            usuarioService.update(guardado.getId(), usuarioDtoActualizado);
        }, "Debe lanzar excepción cuando la introducción es demasiado larga");
    }

    @Test
    void create_conIntroduccionExactamenteDeLongitudMaxima_debeGuardarCorrectamente() {
        // ARRANGE - contexto: usuario con introducción exactamente de 500 caracteres
        String introduccion = "a".repeat(500);
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Juan Pérez",
                "juan@ejemplo.com",
                "password123",
                introduccion,
                "Descripción válida",
                Role.USER,
                true
        );

        // ACT - acción: crear usuario
        UsuarioDto resultado = usuarioService.create(usuarioDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals(introduccion, resultado.getIntroduccion(), "Introducción debe coincidir"),
                () -> assertTrue(usuarioRepository.existsById(resultado.getId()),
                        "Usuario debe existir en la BD")
        );
    }

    @Test
    void create_conNombreConCaracteresEspeciales_debeGuardarCorrectamente() {
        // ARRANGE - contexto: usuario con caracteres especiales en el nombre
        UsuarioDto usuarioDto = crearUsuarioDto(
                "María José García-Pérez",
                "maria@ejemplo.com",
                "password123",
                "Introducción de María José",
                "Descripción de María José",
                Role.USER,
                true
        );

        // ACT - acción: crear usuario
        UsuarioDto resultado = usuarioService.create(usuarioDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("María José García-Pérez", resultado.getNombre(),
                        "Nombre con caracteres especiales debe coincidir"),
                () -> assertTrue(usuarioRepository.existsById(resultado.getId()),
                        "Usuario debe existir en la BD")
        );
    }

    @Test
    void create_conActiveFalse_debeGuardarUsuarioInactivo() {
        // ARRANGE - contexto: usuario con active = false
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Usuario Inactivo",
                "inactivo@ejemplo.com",
                "password123",
                "Introducción inactivo",
                "Descripción inactivo",
                Role.USER,
                false
        );

        // ACT - acción: crear usuario
        UsuarioDto resultado = usuarioService.create(usuarioDto);

        // ASSERT - validaciones: debe guardar usuario inactivo
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertFalse(resultado.isActive(), "Active debe ser false"),
                () -> assertTrue(usuarioRepository.existsById(resultado.getId()),
                        "Usuario debe existir en la BD")
        );
    }

    @Test
    void create_conUsernameConMayusculas_debeGuardarCorrectamente() {
        // ARRANGE - contexto: usuario con username en mayúsculas
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Usuario Test",
                "USERNAME@EJEMPLO.COM",
                "password123",
                "Introducción test",
                "Descripción test",
                Role.USER,
                true
        );

        // ACT - acción: crear usuario
        UsuarioDto resultado = usuarioService.create(usuarioDto);

        // ASSERT - validaciones: debe guardar con el username exacto
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado"),
                () -> assertEquals("USERNAME@EJEMPLO.COM", resultado.getUsername(),
                        "Username debe coincidir exactamente"),
                () -> assertTrue(usuarioRepository.existsById(resultado.getId()),
                        "Usuario debe existir en la BD")
        );
    }

    @Test
    void update_conActiveFalse_debeActualizarUsuarioInactivo() {
        // ARRANGE - contexto: crear y guardar usuario activo
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Activo")
                .username("activo@ejemplo.com")
                .password("password123")
                .introduccion("Introducción activo")
                .descripcion("Descripción activo")
                .rol(Role.USER)
                .active(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        // Preparar datos para desactivar usuario
        UsuarioDto usuarioDtoActualizado = crearUsuarioDto(
                "Usuario Desactivado",
                "activo@ejemplo.com",
                "password456",
                "Introducción desactivado",
                "Descripción desactivado",
                Role.USER,
                false
        );

        // ACT - acción: actualizar usuario para desactivarlo
        UsuarioDto actualizado = usuarioService.update(guardado.getId(), usuarioDtoActualizado);

        // ASSERT - validaciones: debe actualizar a inactivo
        assertAll(
                () -> assertEquals(guardado.getId(), actualizado.getId(), "ID debe mantenerse"),
                () -> assertFalse(actualizado.isActive(), "Active debe ser false"),
                () -> assertTrue(usuarioRepository.existsById(guardado.getId()),
                        "Usuario debe seguir existiendo en la BD")
        );
    }
}