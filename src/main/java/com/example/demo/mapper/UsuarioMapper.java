package com.example.demo.mapper;

import com.example.demo.dto.UsuarioDto;
import com.example.demo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring",uses = {ImagenMapper.class})
public interface UsuarioMapper {

    @Mapping(target = "fotoPerfil", source = "fotoPerfil")
    UsuarioDto toDto(Usuario usuario);

    @Mapping(target = "fotoPerfil", source = "fotoPerfil")
    Usuario toEntity(UsuarioDto usuarioDto);


  /*

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
    }*/

}