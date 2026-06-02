package com.example.demo.service.Impl;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.EducacionDto;
import com.example.demo.enums.TipoConocimiento;
import com.example.demo.mapper.ConocimientoMapper;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Educacion;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ConocimientoRepository;
import com.example.demo.service.ConocimientoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j // remplaza el  Sout
@Service // indica que es un servicio
@RequiredArgsConstructor
public class ConocimientoServiceImpl implements ConocimientoService {

    private final ConocimientoRepository conocimientoRepository; // inyecta el repositorio
    private final ConocimientoMapper conocimientoMapper;

    @Override
    @Transactional
    public ConocimientoDto saveConocimiento(ConocimientoDto conocimientoDto) {
        log.info("Guardando nuevo conocimiento: {}", conocimientoDto.getNombre());
        Conocimiento conocimiento = conocimientoMapper.toConocimiento(conocimientoDto);

        // Establecer la relación bidireccional si hay imagen
        if (conocimiento.getImagen() != null) {
            conocimiento.getImagen().setConocimiento(conocimiento);
        }

        Conocimiento saveConocimiento = conocimientoRepository.save(conocimiento);
        return conocimientoMapper.toConocimientoDto(saveConocimiento);
    }

    @Override
    public void deleteConocimientoById(Integer id) {
        log.info("Eliminando conocimiento con ID: {}", id);
        conocimientoRepository.deleteById(id);
        log.info("Conocimiento eliminado exitosamente");
    }

    @Override
    public List<ConocimientoDto> getAllConocimientos() {
        List<Conocimiento> todasLosConocimientos = conocimientoRepository.findAll();
        return todasLosConocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConocimientoDto> filtrarPorTipo(TipoConocimiento tipoConocimiento) {
        List<Conocimiento> conocimientos = conocimientoRepository.findByTipoConocimiento(tipoConocimiento);
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConocimientoDto actualizarConocimientoPorId(Integer id, ConocimientoDto conocimientoDto) {
        log.info("Actualizando conocimiento con id: {}", id);

        Conocimiento conocimiento = conocimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conocimiento no encontrado con id " + id));

        // Actualizar campos básicos
        conocimiento.setNombre(conocimientoDto.getNombre());
        conocimiento.setNivel(conocimientoDto.getNivel());
        conocimiento.setTipoConocimiento(conocimientoDto.getTipoConocimiento());

        // Actualizar imagen (ahora es una sola imagen)
        if (conocimientoDto.getImagen() != null) {
            if (conocimiento.getImagen() == null) {
                // Crear nueva imagen
                Imagen nuevaImagen = Imagen.builder()
                        .url(conocimientoDto.getImagen().getUrl())
                        .alt(conocimientoDto.getImagen().getAlt())
                        .conocimiento(conocimiento)
                        .build();
                conocimiento.setImagen(nuevaImagen);
            } else {
                // Actualizar imagen existente
                conocimiento.getImagen().setUrl(conocimientoDto.getImagen().getUrl());
                conocimiento.getImagen().setAlt(conocimientoDto.getImagen().getAlt());
            }
        } else if (conocimientoDto.getImagen() == null && conocimiento.getImagen() != null) {
            // Si se envía null y había imagen, eliminar la imagen
            conocimiento.setImagen(null);
        }

        Conocimiento conocimientoActualizado = conocimientoRepository.save(conocimiento);
        return conocimientoMapper.toConocimientoDto(conocimientoActualizado);
    }
   /* @Override
    @Transactional
    public ConocimientoDto saveConocimiento(ConocimientoDto conocimientoDto) {
        Conocimiento conocimiento = conocimientoMapper.toConocimiento(conocimientoDto);
        Conocimiento saveConocimiento = conocimientoRepository.save(conocimiento);
        return conocimientoMapper.toConocimientoDto(saveConocimiento);
    }

    @Override
    public void deleteConocimientoById(Integer id) {
        log.info("Eliminando conocimiento con ID: {}", id);
        conocimientoRepository.deleteById(id);
        log.info("Conocimiento eliminado exitosamente");

    }
    @Override
    public List<ConocimientoDto> getAllConocimientos() {
        List<Conocimiento> todasLosConocimientos = conocimientoRepository.findAll();
        return todasLosConocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();

    }

    //metodo unico para traer lista segun conocimiento pasado
    @Override
    public List<ConocimientoDto> filtrarPorTipo(TipoConocimiento tipoConocimiento) {
        List<Conocimiento> conocimientos = conocimientoRepository.findByTipoConocimiento(tipoConocimiento);
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }



    @Override
    public ConocimientoDto actualizarConocimientoPorId(Integer id, ConocimientoDto conocimientoDto) {

        log.info("Actualizando conocimiento con id: "+ id);
        Conocimiento conocimiento = conocimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campania no encontrada con id " + id));

            conocimiento.setNombre(conocimientoDto.getNombre());
            conocimiento.setNivel(conocimientoDto.getNivel());
            conocimiento.setTipoConocimiento(conocimientoDto.getTipoConocimiento());

            // metodo especifico para actualizar fotos
            if (conocimientoDto.getImagenes() != null && !conocimientoDto.getImagenes().isEmpty()) {
                conocimiento.getImagenes().clear();//borra las fotos
                List<Imagen> nuevaImagen = conocimientoDto.getImagenes().stream()
                        .map(imagenDto -> {
                           Imagen imagen = new Imagen();
                            imagen.setUrl(imagenDto.getUrl());//establece url nueva
                            imagen.setAlt(imagenDto.getAlt());//establece alt nueva
                            imagen.setConocimiento(conocimiento); // Establecer relación bidireccional
                            return imagen;
                        })
                        .collect(Collectors.toList());
                conocimiento.getImagenes().addAll(nuevaImagen);
            }

            Conocimiento conocimientoActualizado = conocimientoRepository.save(conocimiento);
            return conocimientoMapper.toConocimientoDto(conocimientoActualizado);
        }*/

    }

