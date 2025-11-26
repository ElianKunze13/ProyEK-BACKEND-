package com.example.demo.mapper;

import com.example.demo.dto.ImagenDto;
import com.example.demo.model.Imagen;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImagenMapper {

    ImagenDto ToImagenDto(Imagen Imagen);
    Imagen toImagen(ImagenDto imagenDto);
}
