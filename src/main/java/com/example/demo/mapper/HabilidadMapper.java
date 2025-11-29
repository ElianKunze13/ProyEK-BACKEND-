package com.example.demo.mapper;

import com.example.demo.dto.HabilidadDto;
import com.example.demo.model.Habilidad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HabilidadMapper {

    HabilidadDto toHabilidadDto(Habilidad habilidad);
    Habilidad toHabilidad(HabilidadDto habilidadDto);

}
