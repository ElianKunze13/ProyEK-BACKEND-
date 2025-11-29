package com.example.demo.service.Impl;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.mapper.ConocimientoMapper;
import com.example.demo.model.Conocimiento;
import com.example.demo.repository.ConocimientoRepository;
import com.example.demo.service.ConocimientoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<ConocimientoDto> filtrarFrontEnd() {
        List<Conocimiento> conocimientos = conocimientoRepository.findByFrontEnd();
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }

    @Override
    public List<ConocimientoDto> filtrarBackEnd() {
        List<Conocimiento> conocimientos = conocimientoRepository.findByBackend();
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }

    @Override
    public List<ConocimientoDto> filtrarBD() {
        List<Conocimiento> conocimientos = conocimientoRepository.findByBD();
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }

    @Override
    public List<ConocimientoDto> filtrarTesting() {
        List<Conocimiento> conocimientos = conocimientoRepository.findByTest();
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }

    @Override
    public List<ConocimientoDto> filtrarOtros() {
        List<Conocimiento> conocimientos = conocimientoRepository.findByOtros();
        return conocimientos.stream()
                .map(conocimientoMapper::toConocimientoDto)
                .toList();
    }


    /// RECORDATORIO
    /// incluir en controller metodo especifico para modificar imagen
    @Override
    public ConocimientoDto actualizarConocimientoPorId(Integer id, ConocimientoDto conocimientoDto) {
log.info("Actualizando conocimiento con id: "+ id);
        Conocimiento conocimientoExistente = conocimientoRepository.findById(id).orElse(null);
        if (conocimientoExistente != null) {
            conocimientoExistente.setNombre(conocimientoDto.getNombre());
            conocimientoExistente.setNivel(conocimientoDto.getNivel());
            conocimientoExistente.setTipoConocimiento(conocimientoDto.getTipoConocimiento());
            Conocimiento conocimientoActualizado = conocimientoRepository.save(conocimientoExistente);
            return conocimientoMapper.toConocimientoDto(conocimientoActualizado);
        }else {
            log.warn("Conocimiento no encontrado con id: "+ id);
        return null;}
    }
}
