package com.example.demo.controller;

import com.example.demo.dto.MensajeDto;
import com.example.demo.model.Mensaje;
import com.example.demo.repository.MensajeRepository;
import com.example.demo.service.Impl.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MensajeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MensajeRepository mensajeRepository;

    @MockitoBean
    private EmailService emailService;

    private MensajeDto crearMensajeDto(String nombre, String email, String mensaje) {
        return MensajeDto.builder()
                .nombreUsuario(nombre)
                .email(email)
                .mensaje(mensaje)
                .fechaCreacion(LocalDate.now())
                .build();
    }

    @BeforeEach
    void setUp() {
        // Configurar el comportamiento del mock para que no lance excepciones
        doNothing().when(emailService).enviarMensajeContacto(any(Mensaje.class));
    }

    @Test
    void enviarMensajeContacto_conDatosValidos_debeRetornarExito() throws Exception {
        // ARRANGE - contexto: mensaje con datos válidos
        MensajeDto mensajeDto = crearMensajeDto(
                "Juan Pérez",
                "juan@ejemplo.com",
                "Este es un mensaje de prueba con más de 10 caracteres"
        );

        // ACT - acción: enviar mensaje a través del endpoint
        String responseJson = mockMvc.perform(post("/api/v1/guardar/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mensajeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("success"))
                .andExpect(jsonPath("$.mensaje").value("Mensaje enviado exitosamente. Te contactaré pronto."))
                .andExpect(jsonPath("$.data.nombreUsuario").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.email").value("juan@ejemplo.com"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que el mensaje fue guardado en BD
        MensajeDto responseDto = objectMapper.readValue(
                objectMapper.readTree(responseJson).get("data").toString(),
                MensajeDto.class
        );

        assertNotNull(responseDto.getId(), "ID debe ser generado automáticamente");
        assertTrue(mensajeRepository.existsById(responseDto.getId()),
                "Mensaje debe existir en la base de datos");
        assertEquals("Juan Pérez", responseDto.getNombreUsuario(), "Nombre debe coincidir");
        assertEquals("juan@ejemplo.com", responseDto.getEmail(), "Email debe coincidir");
        assertEquals(LocalDate.now(), responseDto.getFechaCreacion(), "Fecha de creación debe ser la actual");
    }

    @Test
    void enviarMensajeContacto_conEmailInvalido_debeRetornarError() throws Exception {
        // ARRANGE - contexto: mensaje con email inválido (menor a 10 caracteres)
        MensajeDto mensajeDto = crearMensajeDto(
                "María García",
                "m@e.com", // Email con menos de 10 caracteres
                "Este es un mensaje de prueba"
        );

        // ACT & ASSERT - acción y validación: debe retornar error de validación
        mockMvc.perform(post("/api/v1/guardar/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mensajeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value("error"))
                .andExpect(jsonPath("$.mensaje").value("Error al enviar el mensaje. Por favor intenta nuevamente."));
    }

    @Test
    void enviarMensajeContacto_conEmailNulo_debeRetornarError() throws Exception {
        // ARRANGE - contexto: mensaje con email nulo
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Carlos López")
                .email(null)
                .mensaje("Mensaje de prueba")
                .build();

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/guardar/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mensajeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value("error"))
                .andExpect(jsonPath("$.mensaje").value("Error al enviar el mensaje. Por favor intenta nuevamente."));
    }

    @Test
    void enviarMensajeContacto_conMensajeExcedido_debeRetornarError() throws Exception {
        // ARRANGE - contexto: mensaje con más de 1000 caracteres
        String mensajeLargo = "a".repeat(1001);
        MensajeDto mensajeDto = crearMensajeDto(
                "Ana Martínez",
                "ana@ejemplo.com",
                mensajeLargo
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/guardar/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mensajeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value("error"))
                .andExpect(jsonPath("$.mensaje").value("Error al enviar el mensaje. Por favor intenta nuevamente."));
    }

    @Test
    void enviarMensajeContacto_conErrorEmail_debeRetornarError() throws Exception {
        // ARRANGE - contexto: simular error en el servicio de email
        doThrow(new RuntimeException("Error al enviar email"))
                .when(emailService).enviarMensajeContacto(any(Mensaje.class));

        MensajeDto mensajeDto = crearMensajeDto(
                "Pedro Sánchez",
                "pedro@ejemplo.com",
                "Mensaje de prueba"
        );

        // ACT & ASSERT - acción y validación: debe retornar error
        mockMvc.perform(post("/api/v1/guardar/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mensajeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value("error"))
                .andExpect(jsonPath("$.mensaje").value("Error al enviar el mensaje. Por favor intenta nuevamente."));
    }

    @Test
    void getAllMensajes_debeRetornarTodosLosMensajes() throws Exception {
        // ARRANGE - contexto: crear y guardar mensajes de prueba
        Mensaje mensaje1 = Mensaje.builder()
                .nombreUsuario("Usuario Uno")
                .email("uno@ejemplo.com")
                .mensaje("Mensaje de prueba 1")
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje mensaje2 = Mensaje.builder()
                .nombreUsuario("Usuario Dos")
                .email("dos@ejemplo.com")
                .mensaje("Mensaje de prueba 2")
                .fechaCreacion(LocalDate.now())
                .build();

        mensajeRepository.save(mensaje1);
        mensajeRepository.save(mensaje2);

        // ACT - acción: obtener todos los mensajes
        String responseJson = mockMvc.perform(get("/api/v1/allMensajes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: verificar que retorna los mensajes
        MensajeDto[] mensajes = objectMapper.readValue(responseJson, MensajeDto[].class);

        assertNotNull(mensajes, "Lista no debe ser null");
        assertTrue(mensajes.length >= 2, "Debe contener al menos 2 mensajes");
        assertTrue(List.of(mensajes).stream().anyMatch(m -> m.getNombreUsuario().equals("Usuario Uno")),
                "Debe contener Usuario Uno");
        assertTrue(List.of(mensajes).stream().anyMatch(m -> m.getNombreUsuario().equals("Usuario Dos")),
                "Debe contener Usuario Dos");
    }

    @Test
    void getAllMensajes_sinMensajes_debeRetornarListaVacia() throws Exception {
        // ARRANGE - contexto: limpiar la base de datos (ya que @Transactional no limpiaría antes del test)
        // Pero como estamos usando @Transactional, los datos se limpian después del test
        // Podemos verificar que inicialmente hay 0 mensajes
        assertEquals(0, mensajeRepository.count(), "No debería haber mensajes inicialmente");

        // ACT - acción: obtener todos los mensajes
        String responseJson = mockMvc.perform(get("/api/v1/allMensajes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe retornar lista vacía
        MensajeDto[] mensajes = objectMapper.readValue(responseJson, MensajeDto[].class);
        assertEquals(0, mensajes.length, "Lista debe estar vacía");
    }

    @Test
    void getMensajeById_conIdExistente_debeRetornarMensaje() throws Exception {
        // ARRANGE - contexto: crear y guardar mensaje
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Buscado")
                .email("buscado@ejemplo.com")
                .mensaje("Mensaje para buscar")
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje guardado = mensajeRepository.save(mensaje);

        // ACT - acción: obtener mensaje por ID
        String responseJson = mockMvc.perform(get("/api/v1/obtenerMensaje/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones: debe encontrar el mensaje correcto
        MensajeDto mensajeEncontrado = objectMapper.readValue(responseJson, MensajeDto.class);

        assertNotNull(mensajeEncontrado, "Mensaje no debe ser null");
        assertEquals(guardado.getId(), mensajeEncontrado.getId(), "IDs deben coincidir");
        assertEquals("Usuario Buscado", mensajeEncontrado.getNombreUsuario(), "Nombre debe coincidir");
        assertEquals("buscado@ejemplo.com", mensajeEncontrado.getEmail(), "Email debe coincidir");
        assertEquals("Mensaje para buscar", mensajeEncontrado.getMensaje(), "Mensaje debe coincidir");
    }

    @Test
    void getMensajeById_conIdInexistente_debeRetornarNotFound() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe retornar 404 Not Found
        mockMvc.perform(get("/api/v1/obtenerMensaje/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMensaje_conIdExistente_debeEliminarMensaje() throws Exception {
        // ARRANGE - contexto: crear y guardar mensaje
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario a Eliminar")
                .email("eliminar@ejemplo.com")
                .mensaje("Mensaje para eliminar")
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje guardado = mensajeRepository.save(mensaje);
        assertTrue(mensajeRepository.existsById(guardado.getId()), "Mensaje debe existir antes de eliminar");

        // ACT - acción: eliminar mensaje
        mockMvc.perform(delete("/api/v1/borrar/mensajes/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // ASSERT - validaciones: mensaje debe ser eliminado
        assertFalse(mensajeRepository.existsById(guardado.getId()), "Mensaje no debe existir después de eliminar");
    }

    @Test
    void deleteMensaje_conIdInexistente_debeLanzarExcepcion() throws Exception {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        // Esto lanzará una excepción porque deleteById lanza EmptyResultDataAccessException
        assertThrows(Exception.class, () -> {
            mockMvc.perform(delete("/api/v1/borrar/mensajes/{id}", idInexistente)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is5xxServerError());
        });
    }

    @Test
    void getMensajeById_conIdExistentePeroMensajeConNombreLargo_debeRetornarCorrectamente() throws Exception {
        // ARRANGE - contexto: mensaje con nombre largo
        String nombreLargo = "a".repeat(150);
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario(nombreLargo)
                .email("largo@ejemplo.com")
                .mensaje("Mensaje de prueba")
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje guardado = mensajeRepository.save(mensaje);

        // ACT - acción: obtener mensaje
        String responseJson = mockMvc.perform(get("/api/v1/obtenerMensaje/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ASSERT - validaciones
        MensajeDto mensajeEncontrado = objectMapper.readValue(responseJson, MensajeDto.class);
        assertEquals(nombreLargo, mensajeEncontrado.getNombreUsuario(), "Nombre debe coincidir");
        assertEquals("largo@ejemplo.com", mensajeEncontrado.getEmail(), "Email debe coincidir");
    }
}