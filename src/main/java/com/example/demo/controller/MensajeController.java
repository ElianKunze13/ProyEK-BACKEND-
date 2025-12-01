package com.example.demo.controller;

import com.example.demo.dto.MensajeDto;
import com.example.demo.service.MensajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @PostMapping("/guardar/contacto")
    public ResponseEntity<?> enviarMensajeContacto(@Valid @RequestBody MensajeDto mensajeDto) {
        try {
            MensajeDto mensajeEnviado = mensajeService.enviarMensaje(mensajeDto);

            return ResponseEntity.ok(Map.of(
                    "estado", "success",
                    "mensaje", "Mensaje enviado exitosamente. Te contactaré pronto.",
                    "data", mensajeEnviado
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "estado", "error",
                    "mensaje", "Error al enviar el mensaje. Por favor intenta nuevamente."
            ));
        }
    }

    @GetMapping("/allMensajes")
    public ResponseEntity<List<MensajeDto>> getAllMensajes() {
        List<MensajeDto> mensajes = mensajeService.getAllMensajes();
        return ResponseEntity.ok(mensajes);
    }

    @DeleteMapping("/borrar/mensajes/{id}")
    public ResponseEntity<Void> deleteMensaje(@PathVariable Integer id) {
        mensajeService.deleteMensaje(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/obtenerMensaje/{id}")
    public ResponseEntity<MensajeDto> getMensajeById(@PathVariable Integer id) {
        MensajeDto mensaje = mensajeService.getMensajeById(id);
        return mensaje != null ? ResponseEntity.ok(mensaje) : ResponseEntity.notFound().build();
    }
}
