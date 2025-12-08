package com.example.demo.mapper;

import com.example.demo.dto.EducacionDto;
import com.example.demo.dto.ImagenDto;
import com.example.demo.model.Educacion;
import com.example.demo.model.Imagen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EducacionMapper {

    EducacionDto toEducacionDto(Educacion educacion);
    Educacion toEducacion(EducacionDto educacionDto);



}
