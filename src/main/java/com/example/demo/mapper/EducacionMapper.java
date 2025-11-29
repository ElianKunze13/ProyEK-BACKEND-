package com.example.demo.mapper;

import com.example.demo.dto.EducacionDto;
import com.example.demo.model.Educacion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EducacionMapper {
    EducacionDto toEducacionDto(Educacion educacion);
Educacion toEducacion(EducacionDto educacionDto);
}
