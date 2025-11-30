package com.example.demo.service;

import com.example.demo.dto.MensajeDto;

import java.util.List;

public interface MensajeService {
    MensajeDto enviarMensaje(MensajeDto mensajeDto);

    List<MensajeDto> getAllMensajes();

    void deleteMensaje(Integer id);

    MensajeDto getMensajeById(Integer id);
}
