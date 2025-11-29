package com.example.demo.service.Impl;

import com.example.demo.dto.ImagenDto;
import com.example.demo.mapper.ImagenMapper;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.service.ImagenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j // remplaza el  Sout por el log
@Service // indica que es un servicio
@RequiredArgsConstructor
public class ImagenServiceImpl implements ImagenService {

    private final ImagenRepository imagenRepository;
    private final ImagenMapper imagenMapper;

    @Override
    public ImagenDto saveImagen(ImagenDto imagenDto) {
        Imagen imagen = imagenMapper.toImagen(imagenDto);
        Imagen saveImagen = imagenRepository.save(imagen);
        return imagenMapper.toImagenDto(saveImagen);
    }


    @Override
    public void deleteImagenPorId(Integer id) {
imagenRepository.deleteById(id);
    }

    @Override
    public List<ImagenDto> getAllImagen() {
        List<Imagen> todasLasImagenes = imagenRepository.findAll();
        return todasLasImagenes.stream().map(imagenMapper::toImagenDto)
                .toList();
    }
}
