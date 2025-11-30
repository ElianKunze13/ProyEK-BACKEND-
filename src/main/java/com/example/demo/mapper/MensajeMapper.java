package com.example.demo.mapper;

import com.example.demo.dto.MensajeDto;
import com.example.demo.model.Mensaje;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MensajeMapper {
    MensajeMapper INSTANCE = Mappers.getMapper(MensajeMapper.class);

    Mensaje toMensaje(MensajeDto mensajeDto);

    MensajeDto toMensajeDto(Mensaje mensaje);
}
