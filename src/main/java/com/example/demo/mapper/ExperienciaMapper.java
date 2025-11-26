package com.example.demo.mapper;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.model.Experiencia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExperienciaMapper {

ExperienciaDto toExperienciaDto(Experiencia experiencia);
Experiencia toExperiencia(ExperienciaDto experienciaDto);

}
