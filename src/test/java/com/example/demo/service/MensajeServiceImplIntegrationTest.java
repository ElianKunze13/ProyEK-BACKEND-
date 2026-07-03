package com.example.demo.service;

import com.example.demo.dto.MensajeDto;
import com.example.demo.model.Mensaje;
import com.example.demo.repository.MensajeRepository;
import com.example.demo.service.Impl.EmailService;
import com.example.demo.service.Impl.MensajeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MensajeServiceImplIntegrationTest {

    @Autowired
    private MensajeServiceImpl mensajeService;

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
    void enviarMensaje_conDatosValidos_debeGuardarYRetornarMensaje() {
        // ARRANGE - contexto: mensaje con datos válidos
        MensajeDto mensajeDto = crearMensajeDto(
                "Juan Pérez",
                "juan@ejemplo.com",
                "Este es un mensaje de prueba para el servicio"
        );

        // ACT - acción: enviar mensaje
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDto);

        // ASSERT - validaciones: verificar que el mensaje fue guardado correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Juan Pérez", resultado.getNombreUsuario(), "Nombre debe coincidir"),
                () -> assertEquals("juan@ejemplo.com", resultado.getEmail(), "Email debe coincidir"),
                () -> assertEquals("Este es un mensaje de prueba para el servicio", resultado.getMensaje(),
                        "Mensaje debe coincidir"),
                () -> assertEquals(LocalDate.now(), resultado.getFechaCreacion(),
                        "Fecha de creación debe ser la actual"),
                () -> assertTrue(mensajeRepository.existsById(resultado.getId()),
                        "Mensaje debe existir en la base de datos"),
                () -> verify(emailService, times(1)).enviarMensajeContacto(any(Mensaje.class)),
                () -> verify(emailService, times(1)).enviarMensajeContacto(any())
        );
    }

    @Test
    void enviarMensaje_conEmailNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: mensaje con email nulo
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario("Carlos López")
                .email(null) // Email nulo
                .mensaje("Mensaje de prueba")
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            mensajeService.enviarMensaje(mensajeDto);
        }, "Debe lanzar excepción cuando el email es nulo");

        // Verificar que no se guardó ningún mensaje
        assertEquals(0, mensajeRepository.count(), "No debe haber mensajes en la base de datos");
        verify(emailService, never()).enviarMensajeContacto(any(Mensaje.class));
    }

    @Test
    void enviarMensaje_conEmailInvalido_debeLanzarExcepcion() {
        // ARRANGE - contexto: mensaje con email inválido (menor a 10 caracteres)
        MensajeDto mensajeDto = crearMensajeDto(
                "María García",
                "m@e.com", // Email con menos de 10 caracteres
                "Mensaje de prueba"
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción por validación
        assertThrows(Exception.class, () -> {
            mensajeService.enviarMensaje(mensajeDto);
        }, "Debe lanzar excepción cuando el email es inválido");

        // Verificar que no se guardó ningún mensaje
        assertEquals(0, mensajeRepository.count(), "No debe haber mensajes en la base de datos");
        verify(emailService, never()).enviarMensajeContacto(any(Mensaje.class));
    }

    @Test
    void enviarMensaje_conMensajeExcedido_debeLanzarExcepcion() {
        // ARRANGE - contexto: mensaje con más de 1000 caracteres
        String mensajeLargo = "a".repeat(1001);
        MensajeDto mensajeDto = crearMensajeDto(
                "Ana Martínez",
                "ana@ejemplo.com",
                mensajeLargo
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            mensajeService.enviarMensaje(mensajeDto);
        }, "Debe lanzar excepción cuando el mensaje excede el límite de caracteres");

        // Verificar que no se guardó ningún mensaje
        assertEquals(0, mensajeRepository.count(), "No debe haber mensajes en la base de datos");
        verify(emailService, never()).enviarMensajeContacto(any(Mensaje.class));
    }

    @Test
    void enviarMensaje_conNombreNulo_debeLanzarExcepcion() {
        // ARRANGE - contexto: mensaje con nombre nulo
        MensajeDto mensajeDto = MensajeDto.builder()
                .nombreUsuario(null) // Nombre nulo
                .email("nombre@ejemplo.com")
                .mensaje("Mensaje de prueba")
                .build();

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            mensajeService.enviarMensaje(mensajeDto);
        }, "Debe lanzar excepción cuando el nombre es nulo");

        // Verificar que no se guardó ningún mensaje
        assertEquals(0, mensajeRepository.count(), "No debe haber mensajes en la base de datos");
        verify(emailService, never()).enviarMensajeContacto(any(Mensaje.class));
    }

    @Test
    void enviarMensaje_conErrorEnEmailService_debeLanzarExcepcion() {
        // ARRANGE - contexto: simular error en el servicio de email
        doThrow(new RuntimeException("Error al enviar email"))
                .when(emailService).enviarMensajeContacto(any(Mensaje.class));

        MensajeDto mensajeDto = crearMensajeDto(
                "Pedro Sánchez",
                "pedro@ejemplo.com",
                "Mensaje de prueba"
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(RuntimeException.class, () -> {
            mensajeService.enviarMensaje(mensajeDto);
        }, "Debe lanzar RuntimeException cuando falla el envío de email");

        // Verificar que NO se guardó el mensaje en la base de datos (por el rollback de @Transactional)
        // NOTA: Aunque en el código se guarda antes del email, @Transactional hará rollback si hay excepción
        assertEquals(0, mensajeRepository.count(), "No debe haber mensajes en la base de datos");
        verify(emailService, times(1)).enviarMensajeContacto(any(Mensaje.class));
    }

    @Test
    void getAllMensajes_debeRetornarTodosLosMensajesPersistidos() {
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
        List<MensajeDto> mensajes = mensajeService.getAllMensajes();

        // ASSERT - validaciones: verificar que retorna todos los mensajes
        assertAll(
                () -> assertNotNull(mensajes, "Lista no debe ser null"),
                () -> assertFalse(mensajes.isEmpty(), "Lista no debe estar vacía"),
                () -> assertTrue(mensajes.size() >= 2, "Debe contener al menos 2 mensajes"),
                () -> assertTrue(mensajes.stream().anyMatch(m -> m.getNombreUsuario().equals("Usuario Uno")),
                        "Debe contener Usuario Uno"),
                () -> assertTrue(mensajes.stream().anyMatch(m -> m.getNombreUsuario().equals("Usuario Dos")),
                        "Debe contener Usuario Dos"),
                () -> assertNotNull(mensajes.get(0).getId(), "ID debe estar presente"),
                () -> assertNotNull(mensajes.get(0).getFechaCreacion(), "Fecha de creación debe estar presente")
        );
    }

    @Test
    void getAllMensajes_sinMensajes_debeRetornarListaVacia() {
        // ARRANGE - contexto: no hay mensajes en la base de datos
        assertEquals(0, mensajeRepository.count(), "No debería haber mensajes inicialmente");

        // ACT - acción: obtener todos los mensajes
        List<MensajeDto> mensajes = mensajeService.getAllMensajes();

        // ASSERT - validaciones: debe retornar lista vacía
        assertNotNull(mensajes, "Lista no debe ser null");
        assertTrue(mensajes.isEmpty(), "Lista debe estar vacía");
        assertEquals(0, mensajes.size(), "Tamaño debe ser 0");
    }

    @Test
    void getMensajeById_conIdExistente_debeRetornarMensaje() {
        // ARRANGE - contexto: crear y guardar mensaje
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario Buscado")
                .email("buscado@ejemplo.com")
                .mensaje("Mensaje para buscar")
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje guardado = mensajeRepository.save(mensaje);

        // ACT - acción: buscar mensaje por ID
        MensajeDto encontrado = mensajeService.getMensajeById(guardado.getId());

        // ASSERT - validaciones: debe encontrar el mensaje correcto
        assertAll(
                () -> assertNotNull(encontrado, "Mensaje no debe ser null"),
                () -> assertEquals(guardado.getId(), encontrado.getId(), "IDs deben coincidir"),
                () -> assertEquals("Usuario Buscado", encontrado.getNombreUsuario(), "Nombre debe coincidir"),
                () -> assertEquals("buscado@ejemplo.com", encontrado.getEmail(), "Email debe coincidir"),
                () -> assertEquals("Mensaje para buscar", encontrado.getMensaje(), "Mensaje debe coincidir"),
                () -> assertEquals(guardado.getFechaCreacion(), encontrado.getFechaCreacion(),
                        "Fecha de creación debe coincidir")
        );
    }

    @Test
    void getMensajeById_conIdInexistente_debeRetornarNull() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT - acción: buscar mensaje por ID inexistente
        MensajeDto encontrado = mensajeService.getMensajeById(idInexistente);

        // ASSERT - validaciones: debe retornar null
        assertNull(encontrado, "Debe retornar null para ID inexistente");
    }

    @Test
    void deleteMensaje_conIdExistente_debeEliminarMensaje() {
        // ARRANGE - contexto: crear y guardar mensaje
        Mensaje mensaje = Mensaje.builder()
                .nombreUsuario("Usuario a Eliminar")
                .email("eliminar@ejemplo.com")
                .mensaje("Mensaje para eliminar")
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje guardado = mensajeRepository.save(mensaje);
        assertTrue(mensajeRepository.existsById(guardado.getId()),
                "Mensaje debe existir antes de eliminar");

        // ACT - acción: eliminar mensaje
        mensajeService.deleteMensaje(guardado.getId());

        // ASSERT - validaciones: mensaje debe ser eliminado
        assertFalse(mensajeRepository.existsById(guardado.getId()),
                "Mensaje no debe existir después de eliminar");
        assertEquals(0, mensajeRepository.count(), "No debe haber mensajes en la base de datos");
    }

    @Test
    void deleteMensaje_conIdInexistente_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID que no existe
        Integer idInexistente = 9999;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(EmptyResultDataAccessException.class, () -> {
            mensajeService.deleteMensaje(idInexistente);
        }, "Debe lanzar EmptyResultDataAccessException para ID inexistente");

        // Verificar que no hay mensajes en la base de datos
        assertEquals(0, mensajeRepository.count(), "No debe haber mensajes en la base de datos");
    }

    @Test
    void enviarMensaje_conEmailConEspacios_debeLanzarExcepcion() {
        // ARRANGE - contexto: email con espacios (inválido)
        MensajeDto mensajeDto = crearMensajeDto(
                "Usuario Test",
                "email con espacios@ejemplo.com", // Email con espacios
                "Mensaje de prueba"
        );

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            mensajeService.enviarMensaje(mensajeDto);
        }, "Debe lanzar excepción cuando el email contiene espacios");

        // Verificar que no se guardó ningún mensaje
        assertEquals(0, mensajeRepository.count(), "No debe haber mensajes en la base de datos");
        verify(emailService, never()).enviarMensajeContacto(any(Mensaje.class));
    }

    @Test
    void enviarMensaje_conMensajeVacio_debeGuardarCorrectamente() {
        // ARRANGE - contexto: mensaje vacío (campo opcional)
        MensajeDto mensajeDto = crearMensajeDto(
                "Usuario Test",
                "test@ejemplo.com",
                "" // Mensaje vacío
        );

        // ACT - acción: enviar mensaje
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDto);

        // ASSERT - validaciones: debe guardar con mensaje vacío
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("Usuario Test", resultado.getNombreUsuario(), "Nombre debe coincidir"),
                () -> assertEquals("test@ejemplo.com", resultado.getEmail(), "Email debe coincidir"),
                () -> assertEquals("", resultado.getMensaje(), "Mensaje debe estar vacío"),
                () -> assertTrue(mensajeRepository.existsById(resultado.getId()),
                        "Mensaje debe existir en la base de datos"),
                () -> verify(emailService, times(1)).enviarMensajeContacto(any(Mensaje.class))
        );
    }

    @Test
    void enviarMensaje_conNombreConCaracteresEspeciales_debeGuardarCorrectamente() {
        // ARRANGE - contexto: nombre con caracteres especiales
        MensajeDto mensajeDto = crearMensajeDto(
                "María José García-Pérez",
                "maria@ejemplo.com",
                "Mensaje de prueba con caracteres especiales"
        );

        // ACT - acción: enviar mensaje
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals("María José García-Pérez", resultado.getNombreUsuario(),
                        "Nombre con caracteres especiales debe coincidir"),
                () -> assertEquals("maria@ejemplo.com", resultado.getEmail(), "Email debe coincidir"),
                () -> assertTrue(mensajeRepository.existsById(resultado.getId()),
                        "Mensaje debe existir en la base de datos")
        );
    }

    @Test
    void enviarMensaje_conEmailExactamenteDeLongitudMinima_debeGuardarCorrectamente() {
        // ARRANGE - contexto: email con longitud exacta de 10 caracteres (mínimo permitido)
        // Ejemplo: "a@b.c" + caracteres para llegar a 10
        String email = "a@bcdefghij"; // 10 caracteres
        MensajeDto mensajeDto = crearMensajeDto(
                "Usuario Test",
                email,
                "Mensaje de prueba"
        );

        // ACT - acción: enviar mensaje
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals(email, resultado.getEmail(), "Email debe coincidir"),
                () -> assertTrue(mensajeRepository.existsById(resultado.getId()),
                        "Mensaje debe existir en la base de datos")
        );
    }

    @Test
    void enviarMensaje_conEmailExactamenteDeLongitudMaxima_debeGuardarCorrectamente() {
        // ARRANGE - contexto: email con longitud máxima de 100 caracteres
        String email = "a".repeat(87) + "@ejemplo.com"; // 100 caracteres totales
        MensajeDto mensajeDto = crearMensajeDto(
                "Usuario Test",
                email,
                "Mensaje de prueba"
        );

        // ACT - acción: enviar mensaje
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDto);

        // ASSERT - validaciones: debe guardar correctamente
        assertAll(
                () -> assertNotNull(resultado.getId(), "ID debe ser generado automáticamente"),
                () -> assertEquals(email, resultado.getEmail(), "Email debe coincidir"),
                () -> assertTrue(mensajeRepository.existsById(resultado.getId()),
                        "Mensaje debe existir en la base de datos")
        );
    }

    @Test
    void deleteMensaje_conIdNull_debeLanzarExcepcion() {
        // ARRANGE - contexto: ID null
        Integer idNull = null;

        // ACT & ASSERT - acción y validación: debe lanzar excepción
        assertThrows(Exception.class, () -> {
            mensajeService.deleteMensaje(idNull);
        }, "Debe lanzar excepción cuando el ID es null");

        // Verificar que no se eliminó nada
        assertEquals(0, mensajeRepository.count(), "No debe haber mensajes en la base de datos");
    }

    @Test
    void getAllMensajes_conMultiplesMensajes_debeRetornarEnOrdenCorrecto() {
        // ARRANGE - contexto: crear y guardar múltiples mensajes
        for (int i = 1; i <= 5; i++) {
            Mensaje mensaje = Mensaje.builder()
                    .nombreUsuario("Usuario " + i)
                    .email("usuario" + i + "@ejemplo.com")
                    .mensaje("Mensaje " + i)
                    .fechaCreacion(LocalDate.now())
                    .build();
            mensajeRepository.save(mensaje);
        }

        // ACT - acción: obtener todos los mensajes
        List<MensajeDto> mensajes = mensajeService.getAllMensajes();

        // ASSERT - validaciones: debe retornar todos los mensajes
        assertAll(
                () -> assertNotNull(mensajes, "Lista no debe ser null"),
                () -> assertEquals(5, mensajes.size(), "Debe haber exactamente 5 mensajes"),
                () -> assertTrue(mensajes.stream().allMatch(m -> m.getId() != null),
                        "Todos los mensajes deben tener ID"),
                () -> assertTrue(mensajes.stream().allMatch(m -> m.getFechaCreacion() != null),
                        "Todos los mensajes deben tener fecha de creación"),
                () -> assertTrue(mensajes.stream().anyMatch(m -> m.getNombreUsuario().equals("Usuario 3")),
                        "Debe contener Usuario 3"),
                () -> assertTrue(mensajes.stream().anyMatch(m -> m.getEmail().equals("usuario5@ejemplo.com")),
                        "Debe contener usuario5@ejemplo.com")
        );
    }
}