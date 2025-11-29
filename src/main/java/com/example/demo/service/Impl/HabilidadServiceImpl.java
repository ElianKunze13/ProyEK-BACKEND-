package com.example.demo.service.Impl;

import com.example.demo.dto.HabilidadDto;
import com.example.demo.mapper.HabilidadMapper;
import com.example.demo.model.Habilidad;
import com.example.demo.repository.HabilidadRepository;
import com.example.demo.service.HabilidadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j // remplaza el  Sout
@Service // indica que es un servicio
@RequiredArgsConstructor
public class HabilidadServiceImpl implements HabilidadService {

    private final HabilidadRepository habilidadRepository;
    private final HabilidadMapper habilidadMapper;


    @Override
    public HabilidadDto saveHabilidad(HabilidadDto habilidadDto) {
        Habilidad habilidad = habilidadMapper.toHabilidad(habilidadDto);
        Habilidad saveHabilidad = habilidadRepository.save(habilidad);
        return habilidadMapper.toHabilidadDto(saveHabilidad);
    }

    @Override
    public HabilidadDto actualizarHabilidadPorId(Integer id, HabilidadDto habilidadDto) {
        log.info("Actualizando habilidad con id: " + id);
        Habilidad habilidadExistente = habilidadRepository.findById(id).orElse(null);
        if (habilidadExistente != null) {
            habilidadExistente.setNombre(habilidadDto.getNombre());


           Habilidad habilidadActualizada = habilidadRepository.save(habilidadExistente);
            return habilidadMapper.toHabilidadDto(habilidadActualizada);
        } else {
            log.warn("Habilidad no encontrado con id: " + id);
            return null;
        }
    }

    @Override
    public void deleteHabilidadPorId(Integer id) {
        habilidadRepository.deleteById(id);
    }

    @Override
    public List<HabilidadDto> getAllHabilidad() {
        List<Habilidad> todasLasHabilidades = habilidadRepository.findAll();
        return todasLasHabilidades.stream().map(habilidadMapper::toHabilidadDto)
                .toList();
    }
}
