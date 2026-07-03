package com.example.demo.controller;

import com.example.demo.dto.ImagenDto;
import com.example.demo.dto.UsuarioDto;
import com.example.demo.enums.Role;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void saveUsuario_conDatosValidos_debeRetornarUsuarioCreado() throws Exception {
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

        // ACT - acción: guardar usuario a través del endpoint
        String responseJson = mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que el usuario fue guardado en BD
        UsuarioDto responseDto = objectMapper.readValue(responseJson, UsuarioDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Juan Pérez", responseDto.getNombre(), "Nombre debe coincidir"),
                () -> assertEquals("juan@ejemplo.com", responseDto.getUsername(), "Username debe coincidir"),
                () -> assertEquals("Introducción de Juan", responseDto.getIntroduccion(),
                        "Introducción debe coincidir"),
                () -> assertEquals("Descripción de Juan", responseDto.getDescripcion(),
                        "Descripción debe coincidir"),
                () -> assertEquals(Role.USER, responseDto.getRol(), "Rol debe coincidir"),
                () -> assertTrue(responseDto.isActive(), "Active debe ser true"),
                () -> assertTrue(usuarioRepository.existsById(responseDto.getId()),
                        "Usuario debe existir en la base de datos")
        );
    }

    @Test
    void saveUsuario_conUsernameDuplicado_debeLanzarExcepcion() throws Exception {
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
        // Nota: El controller captura la excepción y retorna 500, pero verificamos que se lanza
        mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void saveUsuario_conDatosInvalidos_debeRetornarError() throws Exception {
        // ARRANGE - contexto: usuario con datos inválidos (nombre muy corto)
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Jo") // Nombre con menos de 3 caracteres
                .username("juan@ejemplo.com")
                .password("password123")
                .introduccion("Introducción")
                .descripcion("Descripción")
                .rol(Role.USER)
                .active(true)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error de validación
        mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveUsuario_conUsernameNulo_debeRetornarError() throws Exception {
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

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveUsuario_conPasswordNulo_debeRetornarError() throws Exception {
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

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveUsuario_conIntroduccionMuyCorta_debeRetornarError() throws Exception {
        // ARRANGE - contexto: usuario con introducción muy corta
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Juan Pérez",
                "juan@ejemplo.com",
                "password123",
                "Hola", // Introducción con menos de 5 caracteres
                "Descripción válida",
                Role.USER,
                true
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUsuarioById_conIdExistente_debeRetornarUsuario() throws Exception {
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

        // ACT - acción: obtener usuario por ID
        String responseJson = mockMvc.perform(get("/api/v1/auth/traerPor/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe encontrar el usuario correcto
        UsuarioDto usuarioEncontrado = objectMapper.readValue(responseJson, UsuarioDto.class);

        assertAll(
                () -> assertNotNull(usuarioEncontrado, "Usuario no debe ser null"),
                () -> assertEquals(guardado.getId(), usuarioEncontrado.getId(), "IDs deben coincidir"),
                () -> assertEquals("Usuario Buscado", usuarioEncontrado.getNombre(), "Nombre debe coincidir"),
                () -> assertEquals("buscado@ejemplo.com", usuarioEncontrado.getUsername(),
                        "Username debe coincidir"),
                () -> assertEquals("Introducción del usuario", usuarioEncontrado.getIntroduccion(),
                        "Introducción debe coincidir"),
                () -> assertEquals("Descripción del usuario", usuarioEncontrado.getDescripcion(),
                        "Descripción debe coincidir"),
                () -> assertEquals(Role.USER, usuarioEncontrado.getRol(), "Rol debe coincidir"),
                () -> assertTrue(usuarioEncontrado.isActive(), "Active debe ser true")
        );
    }

    @Test
    void getUsuarioById_conIdInexistente_debeLanzarExcepcion() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción (error 500)
        mockMvc.perform(get("/api/v1/auth/traerPor/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getUsuarioByUsername_conUsernameExistente_debeRetornarUsuario() throws Exception {
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

        // ACT - acción: obtener usuario por username
        String responseJson = mockMvc.perform(get("/api/v1/username/{username}", "username@ejemplo.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe encontrar el usuario correcto
        UsuarioDto usuarioEncontrado = objectMapper.readValue(responseJson, UsuarioDto.class);

        assertAll(
                () -> assertNotNull(usuarioEncontrado, "Usuario no debe ser null"),
                () -> assertEquals("Usuario por Username", usuarioEncontrado.getNombre(),
                        "Nombre debe coincidir"),
                () -> assertEquals("username@ejemplo.com", usuarioEncontrado.getUsername(),
                        "Username debe coincidir"),
                () -> assertEquals("Introducción por username", usuarioEncontrado.getIntroduccion(),
                        "Introducción debe coincidir"),
                () -> assertEquals("Descripción por username", usuarioEncontrado.getDescripcion(),
                        "Descripción debe coincidir"),
                () -> assertEquals(Role.ADMIN, usuarioEncontrado.getRol(), "Rol debe coincidir"),
                () -> assertTrue(usuarioEncontrado.isActive(), "Active debe ser true")
        );
    }

    @Test
    void getUsuarioByUsername_conUsernameInexistente_debeLanzarExcepcion() throws Exception {
        // ARRANGE - contexto: username que no existe
        String usernameInexistente = "inexistente@ejemplo.com";

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        mockMvc.perform(get("/api/v1/username/{username}", usernameInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void updateUsuario_conDatosValidos_debeActualizarCorrectamente() throws Exception {
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
        String responseJson = mockMvc.perform(put("/api/v1/usuario/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe actualizar correctamente
        UsuarioDto usuarioActualizado = objectMapper.readValue(responseJson, UsuarioDto.class);

        assertAll(
                () -> assertEquals(guardado.getId(), usuarioActualizado.getId(), "ID debe mantenerse"),
                () -> assertEquals("Nombre Actualizado", usuarioActualizado.getNombre(),
                        "Nombre debe estar actualizado"),
                () -> assertEquals("original@ejemplo.com", usuarioActualizado.getUsername(),
                        "Username debe mantenerse"),
                () -> assertEquals("Introducción actualizada", usuarioActualizado.getIntroduccion(),
                        "Introducción debe estar actualizada"),
                () -> assertEquals("Descripción actualizada", usuarioActualizado.getDescripcion(),
                        "Descripción debe estar actualizada"),
                () -> assertEquals(Role.ADMIN, usuarioActualizado.getRol(), "Rol debe estar actualizado"),
                () -> assertTrue(usuarioActualizado.isActive(), "Active debe mantenerse"),
                () -> assertTrue(usuarioRepository.existsById(guardado.getId()),
                        "Usuario debe seguir existiendo en la BD")
        );
    }

    @Test
    void updateUsuario_conIdInexistente_debeLanzarExcepcion() throws Exception {
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
        mockMvc.perform(put("/api/v1/usuario/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void updateUsuario_conUsernameInvalido_debeRetornarError() throws Exception {
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

        // Preparar datos con username inválido (email sin @)
        UsuarioDto usuarioDtoActualizado = crearUsuarioDto(
                "Nombre Actualizado",
                "username-invalido", // Username inválido
                "password456",
                "Introducción actualizada",
                "Descripción actualizada",
                Role.ADMIN,
                true
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(put("/api/v1/usuario/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDtoActualizado)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void saveUsuario_conFotoPerfilYPortada_debeGuardarCorrectamente() throws Exception {
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

        // ACT - acción: guardar usuario
        String responseJson = mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que las imágenes fueron guardadas
        UsuarioDto responseDto = objectMapper.readValue(responseJson, UsuarioDto.class);

        assertAll(
                () -> assertNotNull(responseDto.getId(), "ID debe ser generado"),
                () -> assertNotNull(responseDto.getFotoPerfil(), "Foto de perfil debe estar presente"),
                () -> assertEquals("https://example.com/perfil.jpg",
                        responseDto.getFotoPerfil().getUrl(), "URL de perfil debe coincidir"),
                () -> assertEquals("Foto de perfil de María",
                        responseDto.getFotoPerfil().getAlt(), "Alt de perfil debe coincidir"),
                () -> assertNotNull(responseDto.getFotoPortada(), "Foto de portada debe estar presente"),
                () -> assertEquals("https://example.com/portada.jpg",
                        responseDto.getFotoPortada().getUrl(), "URL de portada debe coincidir"),
                () -> assertEquals("Foto de portada de María",
                        responseDto.getFotoPortada().getAlt(), "Alt de portada debe coincidir")
        );
    }

    @Test
    void updateUsuario_conFotoPerfilNueva_debeActualizarImagen() throws Exception {
        // ARRANGE - contexto: crear y guardar usuario con foto de perfil
        UsuarioDto usuarioDtoInicial = crearUsuarioDtoConImagenes(
                "Usuario Inicial",
                "inicial@ejemplo.com",
                "password123",
                "Introducción inicial",
                "Descripción inicial",
                Role.USER,
                true,
                "https://example.com/perfil_viejo.jpg",
                "Foto vieja",
                null,
                null
        );

        String responseInicial = mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDtoInicial)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UsuarioDto usuarioGuardado = objectMapper.readValue(responseInicial, UsuarioDto.class);

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
        String responseActualizado = mockMvc.perform(put("/api/v1/usuario/{id}", usuarioGuardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDtoActualizado)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que la foto fue actualizada
        UsuarioDto usuarioActualizado = objectMapper.readValue(responseActualizado, UsuarioDto.class);

        assertAll(
                () -> assertNotNull(usuarioActualizado.getFotoPerfil(), "Foto de perfil debe estar presente"),
                () -> assertEquals("https://example.com/perfil_nuevo.jpg",
                        usuarioActualizado.getFotoPerfil().getUrl(), "URL de perfil debe ser la nueva"),
                () -> assertEquals("Foto nueva",
                        usuarioActualizado.getFotoPerfil().getAlt(), "Alt de perfil debe ser el nuevo")
        );
    }

    @Test
    void saveUsuario_conRolNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: usuario con rol nulo
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nombre("Juan Pérez")
                .username("juan@ejemplo.com")
                .password("password123")
                .introduccion("Introducción")
                .descripcion("Descripción")
                .rol(null) // Rol nulo
                .active(true)
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUsuarioById_conIdExistenteYFotos_debeRetornarUsuarioConImagenes() throws Exception {
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

        // ACT - acción: obtener usuario por ID
        String responseJson = mockMvc.perform(get("/api/v1/auth/traerPor/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que las imágenes están presentes
        UsuarioDto usuarioEncontrado = objectMapper.readValue(responseJson, UsuarioDto.class);

        assertAll(
                () -> assertNotNull(usuarioEncontrado, "Usuario no debe ser null"),
                () -> assertNotNull(usuarioEncontrado.getFotoPerfil(), "Foto de perfil debe estar presente"),
                () -> assertEquals("https://example.com/perfil_usuario.jpg",
                        usuarioEncontrado.getFotoPerfil().getUrl(), "URL de perfil debe coincidir"),
                () -> assertNotNull(usuarioEncontrado.getFotoPortada(), "Foto de portada debe estar presente"),
                () -> assertEquals("https://example.com/portada_usuario.jpg",
                        usuarioEncontrado.getFotoPortada().getUrl(), "URL de portada debe coincidir")
        );
    }

    @Test
    void saveUsuario_conEmailInvalido_debeRetornarError() throws Exception {
        // ARRANGE - contexto: usuario con email inválido
        UsuarioDto usuarioDto = crearUsuarioDto(
                "Juan Pérez",
                "email-invalido", // Email sin @
                "password123",
                "Introducción válida",
                "Descripción válida",
                Role.USER,
                true
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/guardarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().is4xxClientError());
    }
}