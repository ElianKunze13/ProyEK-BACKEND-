package com.example.demo.mapper;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Imagen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConocimientoMapper {

    @Mapping(target = "imagen", source = "imagen", qualifiedByName = "toImagenDto")
    ConocimientoDto toConocimientoDto(Conocimiento conocimiento);

    @Mapping(target = "imagen", source = "imagen", qualifiedByName = "toImagen")
    @Mapping(target = "usuario", ignore = true)
    Conocimiento toConocimiento(ConocimientoDto conocimientoDto);

    // Métodos para mapear imágenes individuales
    @Named("toImagenDto")
    default ImagenDto toImagenDto(Imagen imagen) {
        if (imagen == null) return null;
        return ImagenDto.builder()
                .id(imagen.getId())
                .url(imagen.getUrl())
                .alt(imagen.getAlt())
                .build();
    }

    @Named("toImagen")
    default Imagen toImagen(ImagenDto imagenDto) {
        if (imagenDto == null) return null;
        return Imagen.builder()
                .id(imagenDto.getId())
                .url(imagenDto.getUrl())
                .alt(imagenDto.getAlt())
                .build();
    }
    /*@Mapping(target = "imagenes", source = "imagenes")
    ConocimientoDto toConocimientoDto(Conocimiento conocimiento);

    @Mapping(target = "imagenes", source = "imagenes")
    Conocimiento toConocimiento(ConocimientoDto conocimientoDto);

    // Métodos para mapear imágenes si usas ImagenDto
    ImagenDto toImagenDto(Imagen imagen);
    Imagen toImagen(ImagenDto imagenDto);*/


}
