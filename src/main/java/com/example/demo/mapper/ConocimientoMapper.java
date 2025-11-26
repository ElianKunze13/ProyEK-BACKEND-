package com.example.demo.mapper;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.model.Conocimiento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConocimientoMapper {
    ConocimientoDto toConocimientoDto(Conocimiento conocimiento);
    Conocimiento toConocimiento(ConocimientoDto conocimientoDto);

}
