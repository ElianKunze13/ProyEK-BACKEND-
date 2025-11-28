package com.example.demo.service;

import com.example.demo.dto.ImagenDto;

import java.util.List;

public interface ImagenService {
    ImagenDto saveImagen(ImagenDto imagenDto);

    ImagenDto actualizarImagenPorId(Integer id, ImagenDto imagenDto);

    void deleteImagenPorId(Integer id);

    List<ImagenDto> getAllImagen();
}
