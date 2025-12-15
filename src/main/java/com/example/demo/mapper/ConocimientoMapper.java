package com.example.demo.mapper;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Imagen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConocimientoMapper {

   /* ConocimientoDto toConocimientoDto(Conocimiento conocimiento);
    Conocimiento toConocimiento(ConocimientoDto conocimientoDto);
*/

    @Mapping(target = "imagenes", source = "imagenes")
    ConocimientoDto toConocimientoDto(Conocimiento conocimiento);

    @Mapping(target = "imagenes", source = "imagenes")
    Conocimiento toConocimiento(ConocimientoDto conocimientoDto);

    // Métodos para mapear imágenes si usas ImagenDto
    ImagenDto toImagenDto(Imagen imagen);
    Imagen toImagen(ImagenDto imagenDto);


}
