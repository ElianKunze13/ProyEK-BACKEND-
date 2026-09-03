package com.example.demo.service.Impl;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.mapper.ExperienciaMapper;
import com.example.demo.model.Experiencia;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ExperienciaRepository;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.service.ExperienciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienciaServiceImpl implements ExperienciaService {

    private final ExperienciaRepository experienciaRepository;
    private final ExperienciaMapper experienciaMapper;
    private final ImagenRepository imagenRepository;

    @Override
    @Transactional
    public ExperienciaDto saveExperiencia(ExperienciaDto experienciaDto) {
        Experiencia experiencia = experienciaMapper.toExperiencia(experienciaDto);

        // Guardar primero la experiencia para tener el ID
        Experiencia savedExperiencia = experienciaRepository.save(experiencia);

        // Asociar las imágenes con la experiencia
        if (experienciaDto.getImagenes() != null && !experienciaDto.getImagenes().isEmpty()) {
            for (ImagenDto imagenDto : experienciaDto.getImagenes()) {
                Imagen imagen = new Imagen();
                imagen.setUrl(imagenDto.getUrl());
                imagen.setAlt(imagenDto.getAlt());
                imagen.setExperiencia(savedExperiencia);
                imagenRepository.save(imagen);
            }
        }

        // Recuperar la experiencia completa con sus imágenes
        Experiencia experienciaCompleta = experienciaRepository.findById(savedExperiencia.getId()).orElse(null);
        return experienciaMapper.toExperienciaDto(experienciaCompleta);
    }

    @Override
    @Transactional
    public ExperienciaDto actualizarExperienciaPorId(Integer id, ExperienciaDto experienciaDto) {
        log.info("Actualizando experiencia con id: " + id);
        Experiencia experienciaExistente = experienciaRepository.findById(id).orElse(null);

        if (experienciaExistente != null) {
            experienciaExistente.setTitulo(experienciaDto.getTitulo());
            experienciaExistente.setFechaFinProyecto(experienciaDto.getFechaFinProyecto());
            experienciaExistente.setDescripcion(experienciaDto.getDescripcion());
            experienciaExistente.setTipoExperiencia(experienciaDto.getTipoExperiencia());
            experienciaExistente.setTecnologiasUsadas(experienciaDto.getTecnologiasUsadas());

            // Gestionar la actualización de imágenes
            if (experienciaDto.getImagenes() != null) {
                // Eliminar imágenes antiguas
                experienciaExistente.getImagenes().clear();

                // Agregar nuevas imágenes
                for (ImagenDto imagenDto : experienciaDto.getImagenes()) {
                    Imagen imagen = new Imagen();
                    imagen.setUrl(imagenDto.getUrl());
                    imagen.setAlt(imagenDto.getAlt());
                    imagen.setExperiencia(experienciaExistente);
                    experienciaExistente.getImagenes().add(imagen);
                }
            }

            Experiencia experienciaActualizada = experienciaRepository.save(experienciaExistente);
            return experienciaMapper.toExperienciaDto(experienciaActualizada);
        } else {
            log.warn("Experiencia no encontrada con id: " + id);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteExperienciaPorId(Integer id) {
        // Las imágenes se eliminarán automáticamente debido a orphanRemoval = true
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