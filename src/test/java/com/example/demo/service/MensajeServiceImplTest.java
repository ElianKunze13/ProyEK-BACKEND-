package com.example.demo.service;

import com.example.demo.dto.MensajeDto;
import com.example.demo.mapper.MensajeMapper;
import com.example.demo.model.Mensaje;
import com.example.demo.repository.MensajeRepository;
import com.example.demo.service.Impl.EmailService;
import com.example.demo.service.Impl.MensajeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase MensajeServiceImpl
 * Principios FIRST: Fast, Independent, Repeatable, Self-Validating, Timely
 * Principios SOLID: Responsabilidad única en cada test, dependencias inyectadas mediante Mock
 */
@ExtendWith(MockitoExtension.class)
class MensajeServiceImplTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private MensajeMapper mensajeMapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private MensajeServiceImpl mensajeService;

    private Mensaje mensajeValido;
    private MensajeDto mensajeDtoValido;
    private Mensaje mensajeGuardado;
    private MensajeDto mensajeDtoGuardado;
    private Mensaje mensajeSinMensaje;
    private MensajeDto mensajeDtoSinMensaje;

    @BeforeEach
    void setUp() {
        // Arrange - Configuración inicial para cada test
        LocalDate fechaActual = LocalDate.now();

        // Crear mensaje válido
        mensajeValido = Mensaje.builder()
                .nombreUsuario("Juan Pérez")
                .email("juan.perez@email.com")
                .mensaje("Este es un mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaActual)
                .build();

        // Crear MensajeDto válido
        mensajeDtoValido = MensajeDto.builder()
                .nombreUsuario("Juan Pérez")
                .email("juan.perez@email.com")
                .mensaje("Este es un mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaActual)
                .build();

        // Crear mensaje guardado (con ID generado)
        mensajeGuardado = Mensaje.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan.perez@email.com")
                .mensaje("Este es un mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaActual)
                .build();

        // Crear MensajeDto guardado
        mensajeDtoGuardado = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Juan Pérez")
                .email("juan.perez@email.com")
                .mensaje("Este es un mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(fechaActual)
                .build();

        // Crear mensaje sin contenido (mensaje nulo)
        mensajeSinMensaje = Mensaje.builder()
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .fechaCreacion(fechaActual)
                .build();

        // Crear MensajeDto sin contenido
        mensajeDtoSinMensaje = MensajeDto.builder()
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .fechaCreacion(fechaActual)
                .build();
    }

    // ==================== TESTS ENVIAR MENSAJE ====================

    @Test
    @DisplayName("enviarMensaje - Debería enviar un mensaje correctamente - Caso feliz")
    void enviarMensaje_ShouldSendMessageCorrectly() {
        // Arrange
        when(mensajeMapper.toMensaje(mensajeDtoValido)).thenReturn(mensajeValido);
        when(mensajeRepository.save(mensajeValido)).thenReturn(mensajeGuardado);
        when(mensajeMapper.toMensajeDto(mensajeGuardado)).thenReturn(mensajeDtoGuardado);

        // Act
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDtoValido);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1, resultado.getId(), "El ID del mensaje creado debería ser 1");
        assertEquals(mensajeDtoValido.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals(mensajeDtoValido.getEmail(), resultado.getEmail(), "El email debería coincidir");
        assertEquals(mensajeDtoValido.getMensaje(), resultado.getMensaje(), "El mensaje debería coincidir");
        assertNotNull(resultado.getFechaCreacion(), "La fecha de creación no debería ser nula");

        // Verificar interacciones
        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoValido);
        verify(mensajeRepository, times(1)).save(mensajeValido);
        verify(emailService, times(1)).enviarMensajeContacto(mensajeGuardado);
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardado);
    }

    @Test
    @DisplayName("enviarMensaje - Debería enviar un mensaje sin contenido (mensaje nulo)")
    void enviarMensaje_ShouldSendMessageWithoutContent() {
        // Arrange
        MensajeDto mensajeDtoCreadoSinMensaje = MensajeDto.builder()
                .id(2)
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje mensajeGuardadoSinMensaje = Mensaje.builder()
                .id(2)
                .nombreUsuario("Carlos López")
                .email("carlos@email.com")
                .mensaje(null)
                .fechaCreacion(LocalDate.now())
                .build();

        when(mensajeMapper.toMensaje(mensajeDtoSinMensaje)).thenReturn(mensajeSinMensaje);
        when(mensajeRepository.save(mensajeSinMensaje)).thenReturn(mensajeGuardadoSinMensaje);
        when(mensajeMapper.toMensajeDto(mensajeGuardadoSinMensaje)).thenReturn(mensajeDtoCreadoSinMensaje);

        // Act
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDtoSinMensaje);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.getId(), "El ID del mensaje creado debería ser 2");
        assertEquals("Carlos López", resultado.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("carlos@email.com", resultado.getEmail(), "El email debería coincidir");
        assertNull(resultado.getMensaje(), "El mensaje debería ser nulo");

        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoSinMensaje);
        verify(mensajeRepository, times(1)).save(mensajeSinMensaje);
        verify(emailService, times(1)).enviarMensajeContacto(mensajeGuardadoSinMensaje);
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardadoSinMensaje);
    }

    @Test
    @DisplayName("enviarMensaje - Debería manejar mensaje con caracteres especiales")
    void enviarMensaje_ShouldHandleSpecialCharacters() {
        // Arrange
        String nombreEspecial = "José María ñáéíóú ÜÑ";
        String emailEspecial = "josé.maria@email.com";
        String mensajeEspecial = "Mensaje con acentos y ñ: áéíóú üñ y caracteres especiales !@#$%^&*()";

        MensajeDto mensajeDtoEspecial = MensajeDto.builder()
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .build();

        Mensaje mensajeEspecialEntity = Mensaje.builder()
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje mensajeGuardadoEspecial = Mensaje.builder()
                .id(3)
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .fechaCreacion(LocalDate.now())
                .build();

        MensajeDto mensajeDtoGuardadoEspecial = MensajeDto.builder()
                .id(3)
                .nombreUsuario(nombreEspecial)
                .email(emailEspecial)
                .mensaje(mensajeEspecial)
                .fechaCreacion(LocalDate.now())
                .build();

        when(mensajeMapper.toMensaje(mensajeDtoEspecial)).thenReturn(mensajeEspecialEntity);
        when(mensajeRepository.save(mensajeEspecialEntity)).thenReturn(mensajeGuardadoEspecial);
        when(mensajeMapper.toMensajeDto(mensajeGuardadoEspecial)).thenReturn(mensajeDtoGuardadoEspecial);

        // Act
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDtoEspecial);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreEspecial, resultado.getNombreUsuario(),
                "El nombre con caracteres especiales debería mantenerse");
        assertEquals(emailEspecial, resultado.getEmail(),
                "El email con caracteres especiales debería mantenerse");
        assertEquals(mensajeEspecial, resultado.getMensaje(),
                "El mensaje con caracteres especiales debería mantenerse");

        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoEspecial);
        verify(mensajeRepository, times(1)).save(mensajeEspecialEntity);
        verify(emailService, times(1)).enviarMensajeContacto(mensajeGuardadoEspecial);
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardadoEspecial);
    }

    @Test
    @DisplayName("enviarMensaje - Debería manejar email muy largo (100 caracteres)")
    void enviarMensaje_ShouldHandleVeryLongEmail() {
        // Arrange
        String emailLargo = "a".repeat(90) + "@email.com"; // 100 caracteres
        MensajeDto mensajeDtoEmailLargo = MensajeDto.builder()
                .nombreUsuario("Usuario Email Largo")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .build();

        Mensaje mensajeEmailLargoEntity = Mensaje.builder()
                .nombreUsuario("Usuario Email Largo")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje mensajeGuardadoEmailLargo = Mensaje.builder()
                .id(4)
                .nombreUsuario("Usuario Email Largo")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        MensajeDto mensajeDtoGuardadoEmailLargo = MensajeDto.builder()
                .id(4)
                .nombreUsuario("Usuario Email Largo")
                .email(emailLargo)
                .mensaje("Mensaje de prueba con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        when(mensajeMapper.toMensaje(mensajeDtoEmailLargo)).thenReturn(mensajeEmailLargoEntity);
        when(mensajeRepository.save(mensajeEmailLargoEntity)).thenReturn(mensajeGuardadoEmailLargo);
        when(mensajeMapper.toMensajeDto(mensajeGuardadoEmailLargo)).thenReturn(mensajeDtoGuardadoEmailLargo);

        // Act
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDtoEmailLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(emailLargo, resultado.getEmail(), "El email largo debería mantenerse");
        assertEquals(100, resultado.getEmail().length(), "El email debería tener exactamente 100 caracteres");

        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoEmailLargo);
        verify(mensajeRepository, times(1)).save(mensajeEmailLargoEntity);
        verify(emailService, times(1)).enviarMensajeContacto(mensajeGuardadoEmailLargo);
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardadoEmailLargo);
    }

    @Test
    @DisplayName("enviarMensaje - Debería manejar mensaje muy largo (1000 caracteres)")
    void enviarMensaje_ShouldHandleVeryLongMessage() {
        // Arrange
        String mensajeLargo = "a".repeat(1000);
        MensajeDto mensajeDtoMensajeLargo = MensajeDto.builder()
                .nombreUsuario("Usuario Mensaje Largo")
                .email("test@email.com")
                .mensaje(mensajeLargo)
                .build();

        Mensaje mensajeMensajeLargoEntity = Mensaje.builder()
                .nombreUsuario("Usuario Mensaje Largo")
                .email("test@email.com")
                .mensaje(mensajeLargo)
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje mensajeGuardadoMensajeLargo = Mensaje.builder()
                .id(5)
                .nombreUsuario("Usuario Mensaje Largo")
                .email("test@email.com")
                .mensaje(mensajeLargo)
                .fechaCreacion(LocalDate.now())
                .build();

        MensajeDto mensajeDtoGuardadoMensajeLargo = MensajeDto.builder()
                .id(5)
                .nombreUsuario("Usuario Mensaje Largo")
                .email("test@email.com")
                .mensaje(mensajeLargo)
                .fechaCreacion(LocalDate.now())
                .build();

        when(mensajeMapper.toMensaje(mensajeDtoMensajeLargo)).thenReturn(mensajeMensajeLargoEntity);
        when(mensajeRepository.save(mensajeMensajeLargoEntity)).thenReturn(mensajeGuardadoMensajeLargo);
        when(mensajeMapper.toMensajeDto(mensajeGuardadoMensajeLargo)).thenReturn(mensajeDtoGuardadoMensajeLargo);

        // Act
        MensajeDto resultado = mensajeService.enviarMensaje(mensajeDtoMensajeLargo);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(mensajeLargo, resultado.getMensaje(), "El mensaje largo debería mantenerse");
        assertEquals(1000, resultado.getMensaje().length(), "El mensaje debería tener exactamente 1000 caracteres");

        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoMensajeLargo);
        verify(mensajeRepository, times(1)).save(mensajeMensajeLargoEntity);
        verify(emailService, times(1)).enviarMensajeContacto(mensajeGuardadoMensajeLargo);
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardadoMensajeLargo);
    }

    @Test
    @DisplayName("enviarMensaje - Debería propagar excepción cuando el mapper falla")
    void enviarMensaje_ShouldPropagateException_WhenMapperFails() {
        // Arrange
        when(mensajeMapper.toMensaje(mensajeDtoValido)).thenThrow(new RuntimeException("Error al mapear"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mensajeService.enviarMensaje(mensajeDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al mapear", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoValido);
        verify(mensajeRepository, never()).save(any());
        verify(emailService, never()).enviarMensajeContacto(any());
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    @Test
    @DisplayName("enviarMensaje - Debería propagar excepción cuando el repositorio falla")
    void enviarMensaje_ShouldPropagateException_WhenRepositoryFails() {
        // Arrange
        when(mensajeMapper.toMensaje(mensajeDtoValido)).thenReturn(mensajeValido);
        when(mensajeRepository.save(mensajeValido)).thenThrow(new RuntimeException("Error al guardar"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mensajeService.enviarMensaje(mensajeDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al guardar", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoValido);
        verify(mensajeRepository, times(1)).save(mensajeValido);
        verify(emailService, never()).enviarMensajeContacto(any());
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    @Test
    @DisplayName("enviarMensaje - Debería propagar excepción cuando el email service falla")
    void enviarMensaje_ShouldPropagateException_WhenEmailServiceFails() {
        // Arrange
        when(mensajeMapper.toMensaje(mensajeDtoValido)).thenReturn(mensajeValido);
        when(mensajeRepository.save(mensajeValido)).thenReturn(mensajeGuardado);
        doThrow(new RuntimeException("Error al enviar email")).when(emailService).enviarMensajeContacto(mensajeGuardado);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mensajeService.enviarMensaje(mensajeDtoValido);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al enviar email", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoValido);
        verify(mensajeRepository, times(1)).save(mensajeValido);
        verify(emailService, times(1)).enviarMensajeContacto(mensajeGuardado);
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    @Test
    @DisplayName("enviarMensaje - Debería manejar DTO nulo - Test negativo")
    void enviarMensaje_ShouldHandleNullDto() {
        // Arrange
        when(mensajeMapper.toMensaje(null)).thenThrow(new NullPointerException("MensajeDto no puede ser nulo"));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            mensajeService.enviarMensaje(null);
        }, "Debería lanzar NullPointerException cuando el DTO es nulo");

        verify(mensajeMapper, times(1)).toMensaje(null);
        verify(mensajeRepository, never()).save(any());
        verify(emailService, never()).enviarMensajeContacto(any());
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    // ==================== TESTS GET ALL MENSAJES ====================

    @Test
    @DisplayName("getAllMensajes - Debería retornar todos los mensajes")
    void getAllMensajes_ShouldReturnAllMensajes() {
        // Arrange
        Mensaje mensaje1 = Mensaje.builder()
                .id(1)
                .nombreUsuario("Usuario 1")
                .email("usuario1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        Mensaje mensaje2 = Mensaje.builder()
                .id(2)
                .nombreUsuario("Usuario 2")
                .email("usuario2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        MensajeDto mensajeDto1 = MensajeDto.builder()
                .id(1)
                .nombreUsuario("Usuario 1")
                .email("usuario1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        MensajeDto mensajeDto2 = MensajeDto.builder()
                .id(2)
                .nombreUsuario("Usuario 2")
                .email("usuario2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.")
                .fechaCreacion(LocalDate.now())
                .build();

        List<Mensaje> mensajes = Arrays.asList(mensaje1, mensaje2);
        when(mensajeRepository.findAll()).thenReturn(mensajes);
        when(mensajeMapper.toMensajeDto(mensaje1)).thenReturn(mensajeDto1);
        when(mensajeMapper.toMensajeDto(mensaje2)).thenReturn(mensajeDto2);

        // Act
        List<MensajeDto> resultado = mensajeService.getAllMensajes();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.size(), "Debería haber 2 mensajes");
        assertEquals(1, resultado.get(0).getId(), "El ID del primer mensaje debería ser 1");
        assertEquals(2, resultado.get(1).getId(), "El ID del segundo mensaje debería ser 2");
        assertEquals("Usuario 1", resultado.get(0).getNombreUsuario(),
                "El nombre de usuario del primer mensaje debería coincidir");
        assertEquals("Usuario 2", resultado.get(1).getNombreUsuario(),
                "El nombre de usuario del segundo mensaje debería coincidir");

        verify(mensajeRepository, times(1)).findAll();
        verify(mensajeMapper, times(1)).toMensajeDto(mensaje1);
        verify(mensajeMapper, times(1)).toMensajeDto(mensaje2);
    }

    @Test
    @DisplayName("getAllMensajes - Debería retornar lista vacía cuando no hay mensajes")
    void getAllMensajes_ShouldReturnEmptyList_WhenNoMensajes() {
        // Arrange
        when(mensajeRepository.findAll()).thenReturn(List.of());

        // Act
        List<MensajeDto> resultado = mensajeService.getAllMensajes();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");

        verify(mensajeRepository, times(1)).findAll();
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    @Test
    @DisplayName("getAllMensajes - Debería propagar excepción del repositorio")
    void getAllMensajes_ShouldPropagateRepositoryException() {
        // Arrange
        when(mensajeRepository.findAll()).thenThrow(new RuntimeException("Error al obtener mensajes"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mensajeService.getAllMensajes();
        }, "Debería propagar RuntimeException");

        assertEquals("Error al obtener mensajes", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(mensajeRepository, times(1)).findAll();
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    // ==================== TESTS DELETE MENSAJE ====================

    @Test
    @DisplayName("deleteMensaje - Debería eliminar un mensaje correctamente")
    void deleteMensaje_ShouldDeleteMensajeCorrectly() {
        // Arrange
        Integer id = 1;
        doNothing().when(mensajeRepository).deleteById(id);

        // Act
        mensajeService.deleteMensaje(id);

        // Assert
        verify(mensajeRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteMensaje - Debería manejar ID nulo - Test negativo")
    void deleteMensaje_ShouldHandleNullId() {
        // Arrange
        Integer id = null;

        // Act & Assert - El método deleteById lanza IllegalArgumentException cuando el ID es nulo
        assertThrows(IllegalArgumentException.class, () -> {
            mensajeService.deleteMensaje(id);
        }, "Debería lanzar IllegalArgumentException cuando el ID es nulo");

        verify(mensajeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteMensaje - No debería lanzar excepción al eliminar ID inexistente")
    void deleteMensaje_ShouldNotThrowException_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        doNothing().when(mensajeRepository).deleteById(id);

        // Act & Assert
        assertDoesNotThrow(() -> {
            mensajeService.deleteMensaje(id);
        }, "No debería lanzar excepción al eliminar un ID que no existe");

        verify(mensajeRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteMensaje - Debería propagar RuntimeException del repositorio")
    void deleteMensaje_ShouldPropagateRuntimeException() {
        // Arrange
        Integer id = 1;
        doThrow(new RuntimeException("Error al eliminar mensaje")).when(mensajeRepository).deleteById(id);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mensajeService.deleteMensaje(id);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al eliminar mensaje", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(mensajeRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deleteMensaje - Debería manejar ID negativo")
    void deleteMensaje_ShouldHandleNegativeId() {
        // Arrange
        Integer idNegativo = -1;
        doNothing().when(mensajeRepository).deleteById(idNegativo);

        // Act
        mensajeService.deleteMensaje(idNegativo);

        // Assert
        verify(mensajeRepository, times(1)).deleteById(idNegativo);
    }

    @Test
    @DisplayName("deleteMensaje - Debería manejar ID con Integer.MAX_VALUE")
    void deleteMensaje_ShouldHandleMaxIntegerId() {
        // Arrange
        Integer idMax = Integer.MAX_VALUE;
        doNothing().when(mensajeRepository).deleteById(idMax);

        // Act
        mensajeService.deleteMensaje(idMax);

        // Assert
        verify(mensajeRepository, times(1)).deleteById(idMax);
    }

    // ==================== TESTS GET MENSAJE BY ID ====================

    @Test
    @DisplayName("getMensajeById - Debería retornar MensajeDto cuando el ID existe")
    void getMensajeById_ShouldReturnMensajeDto_WhenIdExists() {
        // Arrange
        Integer id = 1;
        when(mensajeRepository.findById(id)).thenReturn(Optional.of(mensajeGuardado));
        when(mensajeMapper.toMensajeDto(mensajeGuardado)).thenReturn(mensajeDtoGuardado);

        // Act
        MensajeDto resultado = mensajeService.getMensajeById(id);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(id, resultado.getId(), "El ID debería coincidir");
        assertEquals(mensajeGuardado.getNombreUsuario(), resultado.getNombreUsuario(),
                "El nombre de usuario debería coincidir");
        assertEquals(mensajeGuardado.getEmail(), resultado.getEmail(), "El email debería coincidir");
        assertEquals(mensajeGuardado.getMensaje(), resultado.getMensaje(), "El mensaje debería coincidir");

        verify(mensajeRepository, times(1)).findById(id);
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardado);
    }

    @Test
    @DisplayName("getMensajeById - Debería retornar null cuando el ID no existe")
    void getMensajeById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        Integer id = 999;
        when(mensajeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        MensajeDto resultado = mensajeService.getMensajeById(id);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo cuando el ID no existe");

        verify(mensajeRepository, times(1)).findById(id);
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    @Test
    @DisplayName("getMensajeById - Debería manejar ID nulo - Test negativo")
    void getMensajeById_ShouldHandleNullId() {
        // Arrange
        Integer id = null;
        when(mensajeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        MensajeDto resultado = mensajeService.getMensajeById(id);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo para ID nulo");

        verify(mensajeRepository, times(1)).findById(id);
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    @Test
    @DisplayName("getMensajeById - Debería propagar excepción del repositorio")
    void getMensajeById_ShouldPropagateRepositoryException() {
        // Arrange
        Integer id = 1;
        when(mensajeRepository.findById(id)).thenThrow(new RuntimeException("Error al buscar mensaje"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mensajeService.getMensajeById(id);
        }, "Debería propagar RuntimeException");

        assertEquals("Error al buscar mensaje", exception.getMessage(),
                "El mensaje de excepción debería ser el mismo");

        verify(mensajeRepository, times(1)).findById(id);
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    @Test
    @DisplayName("getMensajeById - Debería manejar ID negativo")
    void getMensajeById_ShouldHandleNegativeId() {
        // Arrange
        Integer idNegativo = -1;
        when(mensajeRepository.findById(idNegativo)).thenReturn(Optional.empty());

        // Act
        MensajeDto resultado = mensajeService.getMensajeById(idNegativo);

        // Assert
        assertNull(resultado, "El resultado debería ser nulo para ID negativo");

        verify(mensajeRepository, times(1)).findById(idNegativo);
        verify(mensajeMapper, never()).toMensajeDto(any());
    }

    // ==================== TESTS DE VERIFICACIÓN DE INTERACCIONES ====================

    @Test
    @DisplayName("Verificar que los mocks no tienen interacciones no deseadas")
    void verifyNoUnexpectedInteractions() {
        // Arrange - No configurar ningún comportamiento específico

        // Act - No ejecutar ningún método

        // Assert - Verificar que no hay interacciones con los mocks
        verifyNoInteractions(mensajeRepository);
        verifyNoInteractions(mensajeMapper);
        verifyNoInteractions(emailService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en enviarMensaje")
    void verifyExpectedInteractionsInEnviarMensaje() {
        // Arrange
        when(mensajeMapper.toMensaje(mensajeDtoValido)).thenReturn(mensajeValido);
        when(mensajeRepository.save(mensajeValido)).thenReturn(mensajeGuardado);
        when(mensajeMapper.toMensajeDto(mensajeGuardado)).thenReturn(mensajeDtoGuardado);

        // Act
        mensajeService.enviarMensaje(mensajeDtoValido);

        // Assert
        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoValido);
        verify(mensajeRepository, times(1)).save(mensajeValido);
        verify(emailService, times(1)).enviarMensajeContacto(mensajeGuardado);
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardado);
        verifyNoMoreInteractions(mensajeRepository);
        verifyNoMoreInteractions(mensajeMapper);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en getAllMensajes")
    void verifyExpectedInteractionsInGetAllMensajes() {
        // Arrange
        List<Mensaje> mensajes = Arrays.asList(mensajeGuardado);
        when(mensajeRepository.findAll()).thenReturn(mensajes);
        when(mensajeMapper.toMensajeDto(mensajeGuardado)).thenReturn(mensajeDtoGuardado);

        // Act
        mensajeService.getAllMensajes();

        // Assert
        verify(mensajeRepository, times(1)).findAll();
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardado);
        verifyNoMoreInteractions(mensajeRepository);
        verifyNoMoreInteractions(mensajeMapper);
        verifyNoInteractions(emailService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en deleteMensaje")
    void verifyExpectedInteractionsInDeleteMensaje() {
        // Arrange
        Integer id = 1;
        doNothing().when(mensajeRepository).deleteById(id);

        // Act
        mensajeService.deleteMensaje(id);

        // Assert
        verify(mensajeRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(mensajeRepository);
        verifyNoInteractions(mensajeMapper);
        verifyNoInteractions(emailService);
    }

    @Test
    @DisplayName("Verificar que los mocks tienen las interacciones esperadas en getMensajeById")
    void verifyExpectedInteractionsInGetMensajeById() {
        // Arrange
        Integer id = 1;
        when(mensajeRepository.findById(id)).thenReturn(Optional.of(mensajeGuardado));
        when(mensajeMapper.toMensajeDto(mensajeGuardado)).thenReturn(mensajeDtoGuardado);

        // Act
        mensajeService.getMensajeById(id);

        // Assert        verify(mensajeRepository, times(1)).findById(id);
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardado);
        verifyNoMoreInteractions(mensajeRepository);
        verifyNoMoreInteractions(mensajeMapper);
        verifyNoInteractions(emailService);
    }

    // ==================== TESTS DE OPERACIONES CRUD COMPLETAS ====================

    @Test
    @DisplayName("Operaciones CRUD completas - Guardar, Listar y Eliminar")
    void completeCrudOperations_ShouldWorkCorrectly() {
        // 1. Guardar mensaje
        when(mensajeMapper.toMensaje(mensajeDtoValido)).thenReturn(mensajeValido);
        when(mensajeRepository.save(mensajeValido)).thenReturn(mensajeGuardado);
        when(mensajeMapper.toMensajeDto(mensajeGuardado)).thenReturn(mensajeDtoGuardado);

        MensajeDto resultadoGuardado = mensajeService.enviarMensaje(mensajeDtoValido);
        assertNotNull(resultadoGuardado, "El mensaje guardado no debería ser nulo");
        assertEquals(1, resultadoGuardado.getId(), "El ID del mensaje guardado debería ser 1");

        // 2. Listar todos los mensajes
        List<Mensaje> mensajes = Arrays.asList(mensajeGuardado);
        when(mensajeRepository.findAll()).thenReturn(mensajes);
        when(mensajeMapper.toMensajeDto(mensajeGuardado)).thenReturn(mensajeDtoGuardado);

        List<MensajeDto> resultadoLista = mensajeService.getAllMensajes();
        assertNotNull(resultadoLista, "La lista no debería ser nula");
        assertEquals(1, resultadoLista.size(), "Debería haber 1 mensaje");
        assertEquals(1, resultadoLista.get(0).getId(), "El ID del mensaje en la lista debería ser 1");

        // 3. Obtener mensaje por ID
        when(mensajeRepository.findById(1)).thenReturn(Optional.of(mensajeGuardado));
        when(mensajeMapper.toMensajeDto(mensajeGuardado)).thenReturn(mensajeDtoGuardado);

        MensajeDto resultadoObtenido = mensajeService.getMensajeById(1);
        assertNotNull(resultadoObtenido, "El mensaje obtenido no debería ser nulo");
        assertEquals(1, resultadoObtenido.getId(), "El ID del mensaje obtenido debería ser 1");

        // 4. Eliminar mensaje
        doNothing().when(mensajeRepository).deleteById(1);
        mensajeService.deleteMensaje(1);

        // Verificar interacciones
        verify(mensajeMapper, times(1)).toMensaje(mensajeDtoValido);
        verify(mensajeRepository, times(1)).save(mensajeValido);
        verify(emailService, times(1)).enviarMensajeContacto(mensajeGuardado);
        verify(mensajeMapper, times(1)).toMensajeDto(mensajeGuardado);
        verify(mensajeRepository, times(1)).findAll();
        verify(mensajeRepository, times(1)).findById(1);
        verify(mensajeRepository, times(1)).deleteById(1);
        verify(mensajeMapper, times(3)).toMensajeDto(mensajeGuardado);
    }

    @Test
    @DisplayName("Operaciones CRUD completas - Múltiples mensajes")
    void completeCrudOperations_MultipleMensajes() {
        // Crear mensajes
        Mensaje mensaje1 = Mensaje.builder().id(1).nombreUsuario("Usuario 1").email("u1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.").fechaCreacion(LocalDate.now()).build();
        Mensaje mensaje2 = Mensaje.builder().id(2).nombreUsuario("Usuario 2").email("u2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.").fechaCreacion(LocalDate.now()).build();

        MensajeDto dto1 = MensajeDto.builder().id(1).nombreUsuario("Usuario 1").email("u1@email.com")
                .mensaje("Mensaje 1 con más de 10 caracteres.").fechaCreacion(LocalDate.now()).build();
        MensajeDto dto2 = MensajeDto.builder().id(2).nombreUsuario("Usuario 2").email("u2@email.com")
                .mensaje("Mensaje 2 con más de 10 caracteres.").fechaCreacion(LocalDate.now()).build();

        // Guardar mensaje 1
        when(mensajeMapper.toMensaje(any(MensajeDto.class))).thenReturn(mensaje1);
        when(mensajeRepository.save(mensaje1)).thenReturn(mensaje1);
        when(mensajeMapper.toMensajeDto(mensaje1)).thenReturn(dto1);
        mensajeService.enviarMensaje(dto1);

        // Guardar mensaje 2
        when(mensajeMapper.toMensaje(any(MensajeDto.class))).thenReturn(mensaje2);
        when(mensajeRepository.save(mensaje2)).thenReturn(mensaje2);
        when(mensajeMapper.toMensajeDto(mensaje2)).thenReturn(dto2);
        mensajeService.enviarMensaje(dto2);

        // Listar todos
        List<Mensaje> mensajes = Arrays.asList(mensaje1, mensaje2);
        when(mensajeRepository.findAll()).thenReturn(mensajes);
        when(mensajeMapper.toMensajeDto(mensaje1)).thenReturn(dto1);
        when(mensajeMapper.toMensajeDto(mensaje2)).thenReturn(dto2);

        List<MensajeDto> resultado = mensajeService.getAllMensajes();

        // Assert
        assertNotNull(resultado, "La lista no debería ser nula");
        assertEquals(2, resultado.size(), "Debería haber 2 mensajes");
        assertEquals(1, resultado.get(0).getId(), "El ID del primer mensaje debería ser 1");
        assertEquals(2, resultado.get(1).getId(), "El ID del segundo mensaje debería ser 2");

        // Eliminar mensaje 1
        doNothing().when(mensajeRepository).deleteById(1);
        mensajeService.deleteMensaje(1);

        verify(mensajeRepository, times(1)).deleteById(1);
        verify(mensajeRepository, times(2)).save(any(Mensaje.class));
        verify(mensajeRepository, times(1)).findAll();
        verify(mensajeMapper, times(2)).toMensaje(any(MensajeDto.class));
        verify(emailService, times(2)).enviarMensajeContacto(any(Mensaje.class));
        verify(mensajeMapper, times(4)).toMensajeDto(any(Mensaje.class));
    }
}