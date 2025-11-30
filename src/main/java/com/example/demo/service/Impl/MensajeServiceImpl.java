package com.example.demo.service.Impl;

import com.example.demo.dto.MensajeDto;
import com.example.demo.mapper.MensajeMapper;
import com.example.demo.model.Mensaje;
import com.example.demo.repository.MensajeRepository;
import com.example.demo.service.MensajeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j // remplaza el  Sout
@Service // indica que es un servicio
@RequiredArgsConstructor
public class MensajeServiceImpl implements MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private MensajeMapper mensajeMapper;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public MensajeDto enviarMensaje(MensajeDto mensajeDto) {
        log.info("Enviando mensaje de contacto de: {}", mensajeDto.getEmail());

        // Convertir DTO a Entidad y guardar
        Mensaje mensaje = mensajeMapper.toMensaje(mensajeDto);
        Mensaje mensajeGuardado = mensajeRepository.save(mensaje);

        // Enviar email de notificación
        emailService.enviarMensajeContacto(mensajeGuardado);

        log.info("Mensaje enviado exitosamente. ID: {}", mensajeGuardado.getId());
        return mensajeMapper.toMensajeDto(mensajeGuardado);
    }

    @Override
    public List<MensajeDto> getAllMensajes() {
        List<Mensaje> mensajes = mensajeRepository.findAll();
        return mensajes.stream()
                .map(mensajeMapper::toMensajeDto)
                .toList();
    }

    @Override
    public void deleteMensaje(Integer id) {
        log.info("Eliminando mensaje con ID: {}", id);
        mensajeRepository.deleteById(id);
    }

    @Override
    public MensajeDto getMensajeById(Integer id) {
        return mensajeRepository.findById(id)
                .map(mensajeMapper::toMensajeDto)
                .orElse(null);
    }
}
