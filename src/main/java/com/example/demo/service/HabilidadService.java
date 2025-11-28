package com.example.demo.service;

import com.example.demo.dto.EducacionDto;
import com.example.demo.dto.HabilidadDto;

import java.util.List;

public interface HabilidadService {

    HabilidadDto saveHabilidad(HabilidadDto habilidadDto);

    HabilidadDto actualizarHabilidadPorId(Integer id, HabilidadDto habilidadDto);

    void deleteHabilidadPorId(Integer id);

    List<HabilidadDto> getAllHabilidad();
}
