package com.example.demo.mapper;

import com.example.demo.dto.ImagenDto;
import com.example.demo.dto.UsuarioDto;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDto toDto(Usuario usuario);
    Usuario toEntity(UsuarioDto usuarioDto);


}