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

    @Override
    public List<ConocimientoDto> filtrarFrontEnd(TipoConocimiento tipoConocimiento) {
        List<Conocimiento> conocimientos = conocimientoRepository.findByFrontEnd(tipoConocimiento);
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }

    @Override
    public List<ConocimientoDto> filtrarBackEnd(TipoConocimiento tipoConocimiento) {
        List<Conocimiento> conocimientos = conocimientoRepository.findByBackend(tipoConocimiento);
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }

    @Override
    public List<ConocimientoDto> filtrarBD(TipoConocimiento tipoConocimiento) {
        List<Conocimiento> conocimientos = conocimientoRepository.findByBD(tipoConocimiento);
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }

    @Override
    public List<ConocimientoDto> filtrarTesting(TipoConocimiento tipoConocimiento) {
        List<Conocimiento> conocimientos = conocimientoRepository.findByTest(tipoConocimiento);
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }

    @Override
    public List<ConocimientoDto> filtrarOtros(TipoConocimiento tipoConocimiento) {
        List<Conocimiento> conocimientos = conocimientoRepository.findByOtros(tipoConocimiento);
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }


    /// RECORDATORIO
    /// incluir metodo especifico para modificar imagen
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
        }
    }

