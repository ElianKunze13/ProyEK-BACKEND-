package com.example.demo.service.Impl;

import com.example.demo.dto.EducacionDto;
import com.example.demo.mapper.EducacionMapper;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Educacion;
import com.example.demo.model.Imagen;
import com.example.demo.repository.EducacionRepository;
import com.example.demo.service.EducacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public EducacionDto actualizarEducacionPorId(Integer id, EducacionDto educacionDto) {
        log.info("Actualizando educaion con id: " + id);
        Educacion educacion = educacionRepository.findById(id).orElse(null);

        if (educacion != null) {
            educacion.setTitulo(educacionDto.getTitulo());
            //educacion.setFechaObtencion(educacionDto.getFechaObtencion());
            educacion.setDescripcion(educacionDto.getDescripcion());
            educacion.setTipoEducacion(educacionDto.getTipoEducacion());

            // metodo especifico para actualizar fotos
            if (educacionDto.getImagenes() != null && !educacionDto.getImagenes().isEmpty()) {
                educacion.getImagenes().clear();//borra las fotos
                List<Imagen> nuevaImagen = educacionDto.getImagenes().stream()
                        .map(imagenDto -> {
                            Imagen imagen = new Imagen();
                            imagen.setUrl(imagenDto.getUrl());//establece url nueva
                            imagen.setAlt(imagenDto.getAlt());//establece alt nueva
                            imagen.setEducacion(educacion); // Establecer relación bidireccional
                            return imagen;
                        })
                        .collect(Collectors.toList());
                educacion.getImagenes().addAll(nuevaImagen);
            }


            Educacion educacionActualizada = educacionRepository.save(educacion);
            return educacionMapper.toEducacionDto(educacionActualizada);
        } else {
            log.warn("Educacion no encontrado con id: " + id);
            return null;
        }
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
