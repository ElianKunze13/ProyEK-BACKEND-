package com.example.demo.mapper;

import com.example.demo.dto.ImagenDto;
import com.example.demo.dto.UsuarioDto;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {ImagenMapper.class})
public interface UsuarioMapper {

    @Mapping(target = "fotoUsuario", source = "fotoUsuario")
    UsuarioDto toDto(Usuario usuario);

    @Mapping(target = "fotoUsuario", source = "fotoUsuario")
    Usuario toEntity(UsuarioDto usuarioDto);


    // Método por defecto para mapear lista de Imagen a ImagenDto
    default List<ImagenDto> mapFotoUsuario(List<Imagen> fotoUsuario) {
        if (fotoUsuario == null) {
            return new ArrayList<>();
        }
        return fotoUsuario.stream()
                .map(imagen -> {
                    ImagenDto dto = new ImagenDto();
                    dto.setUrl(imagen.getUrl());
                    dto.setAlt(imagen.getAlt());
                    // Mapea otros campos si existen
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Método por defecto para mapear lista de ImagenDto a Imagen
    default List<Imagen> mapFotoUsuarioEntity(List<ImagenDto> fotoUsuarioDto) {
        if (fotoUsuarioDto == null) {
            return new ArrayList<>();
        }
        return fotoUsuarioDto.stream()
                .map(dto -> {
                    Imagen imagen = new Imagen();
                    imagen.setUrl(dto.getUrl());
                    imagen.setAlt(dto.getAlt());
                    // Mapea otros campos si existen
                    return imagen;
                })
                .collect(Collectors.toList());
    }

}