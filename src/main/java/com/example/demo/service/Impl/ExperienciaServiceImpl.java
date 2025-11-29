package com.example.demo.service.Impl;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.mapper.ExperienciaMapper;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Educacion;
import com.example.demo.model.Experiencia;
import com.example.demo.repository.ExperienciaRepository;
import com.example.demo.service.ExperienciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j // remplaza el  Sout
@Service // indica que es un servicio
@RequiredArgsConstructor
public class ExperienciaServiceImpl implements ExperienciaService {

    private final ExperienciaRepository experienciaRepository;
    private final ExperienciaMapper experienciaMapper;

    @Override
    public ExperienciaDto saveExperiencia(ExperienciaDto experienciaDto) {
        Experiencia experiencia = experienciaMapper.toExperiencia(experienciaDto);
        Experiencia saveExperiencia = experienciaRepository.save(experiencia);
        return experienciaMapper.toExperienciaDto(saveExperiencia);
    }


    @Override
    public ExperienciaDto actualizarExperienciaPorId(Integer id, ExperienciaDto experienciaDto) {
        log.info("Actualizando experiencia con id: " + id);
        Experiencia experienciaExistente = experienciaRepository.findById(id).orElse(null);
        if (experienciaExistente != null) {
            experienciaExistente.setTitulo(experienciaDto.getTitulo());
            experienciaExistente.setFechaHora(experienciaDto.getFechaHora());
            experienciaExistente.setDescripcion(experienciaDto.getDescripcion());
            experienciaExistente.setTipoExperiencia(experienciaDto.getTipoExperiencia());
            experienciaExistente.setTecnologiaUsada(experienciaDto.getTecnologiaUsada());

            Experiencia experienciaActualizada = experienciaRepository.save(experienciaExistente);
            return experienciaMapper.toExperienciaDto(experienciaActualizada);
        } else {
            log.warn("Experiencia no encontrado con id: " + id);
            return null;
        }
    }

    @Override
    public void deleteExperienciaporId(Integer id) {
        experienciaRepository.deleteById(id);
    }

    @Override
    public List<ExperienciaDto> getAllExperiencias() {
        List<Experiencia> todasLasExperiencias = experienciaRepository.findAll();
        return todasLasExperiencias.stream()
                .map(experienciaMapper::toExperienciaDto)
                .toList();
    }
}
