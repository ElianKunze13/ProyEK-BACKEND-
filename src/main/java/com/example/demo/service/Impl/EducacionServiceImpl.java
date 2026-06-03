package com.example.demo.service.Impl;

import com.example.demo.dto.EducacionDto;
import com.example.demo.mapper.EducacionMapper;
import com.example.demo.model.Educacion;
import com.example.demo.model.Imagen;
import com.example.demo.repository.EducacionRepository;
import com.example.demo.service.EducacionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j // remplaza el  Sout
@Service // indica que es un servicio
@RequiredArgsConstructor
public class EducacionServiceImpl implements EducacionService {
    private final EducacionRepository educacionRepository;
    private final EducacionMapper educacionMapper;

    @Override
    public EducacionDto saveEducacion(EducacionDto educacionDto) {
        Educacion educacion = educacionMapper.toEducacion(educacionDto);
        Educacion saveEducacion = educacionRepository.save(educacion);
        return educacionMapper.toEducacionDto(saveEducacion);
    }

    /// RECORDATORIO
    /// incluir en controller metodo especifico para modificar imagen
    /// usar definicion de Conocimiento como guia 
    
    @Override
    @Transactional
    public EducacionDto actualizarEducacionPorId(Integer id, EducacionDto educacionDto) {
        log.info("Actualizando educación con id: {}", id);

        Educacion educacion = educacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Educación no encontrada con id " + id));

        // Actualizar campos básicos
        educacion.setTitulo(educacionDto.getTitulo());
        educacion.setDescripcion(educacionDto.getDescripcion());
        educacion.setTipoEducacion(educacionDto.getTipoEducacion());

        // Actualizar imagen (ahora es una sola imagen)
        if (educacionDto.getImagen() != null) {
            if (educacion.getImagen() == null) {
                // Crear nueva imagen
                Imagen nuevaImagen = Imagen.builder()
                        .url(educacionDto.getImagen().getUrl())
                        .alt(educacionDto.getImagen().getAlt())
                        .educacion(educacion)
                        .build();
                educacion.setImagen(nuevaImagen);
            } else {
                // Actualizar imagen existente
                educacion.getImagen().setUrl(educacionDto.getImagen().getUrl());
                educacion.getImagen().setAlt(educacionDto.getImagen().getAlt());
            }
        } else if (educacionDto.getImagen() == null && educacion.getImagen() != null) {
            // Si se envía null y había imagen, eliminar la imagen
            educacion.setImagen(null);
        }

        Educacion educacionActualizada = educacionRepository.save(educacion);
        return educacionMapper.toEducacionDto(educacionActualizada);
    }

    @Override
    public void deleteEducacionPorId(Integer id) {
        educacionRepository.deleteById(id);
    }

    @Override
    public List<EducacionDto> getAllEducacion() {
        List<Educacion> todasLasEducaciones = educacionRepository.findAll();
        return todasLasEducaciones.stream()
                .map(educacionMapper::toEducacionDto)
                .toList();

    }
}
