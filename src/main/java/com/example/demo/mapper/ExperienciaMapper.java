package com.example.demo.mapper;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.model.Experiencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImagenMapper.class})
public interface ExperienciaMapper {

    @Mapping(target = "imagenes", source = "imagenes")
    ExperienciaDto toExperienciaDto(Experiencia experiencia);

    @Mapping(target = "imagenes", source = "imagenes")
    @Mapping(target = "usuario", ignore = true)
    Experiencia toExperiencia(ExperienciaDto experienciaDto);
}