package com.example.demo.service.Impl;

import com.example.demo.dto.UsuarioDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.model.Imagen;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;// inyecta el repositorio
    private final UsuarioMapper usuarioMapper;// inyecta el mapper


    /// definir que hace cada metodo

    @Override
    public UsuarioDto getById(Integer id) {
        log.info("Buscando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));
        // Forzar la carga de las imágenes si son LAZY
       /* if (usuario.getFotoUsuario() != null) {
            usuario.getFotoUsuario().size(); // Esto fuerza la carga de la colección
        }*/
        return usuarioMapper.toDto(usuario);

    }

    @Override
    public UsuarioDto getByUsername(String username) {
        Usuario usuario = null;
        try {
            usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return usuarioMapper.toDto(usuario);
    }

    @Override
    public UsuarioDto create(UsuarioDto usuarioDto) {
        log.info("Creando nuevo usuario");
        Usuario usuario = usuarioMapper.toEntity(usuarioDto);
        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(saved);
    }

    @Override
    public UsuarioDto update(Integer id, UsuarioDto usuarioDto) {
        log.info("Actualizando usuario con id: "+ id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado " + id)); // lanza un error de usuario no encontrado

        // Actualizamos solo campos permitidos
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setUsername(usuarioDto.getUsername());
        usuario.setPassword(usuarioDto.getPassword());
        usuario.setActive(usuarioDto.isActive());
        usuario.setIntroduccion(usuarioDto.getIntroduccion());
        usuario.setDescripcion(usuarioDto.getDescripcion());

        // metodo especifico para actualizar fotos
       /* if (usuarioDto.getFotoUsuario() != null && !usuarioDto.getFotoUsuario().isEmpty()) {
            usuario.getFotoUsuario().clear();//borra las fotos
            List<Imagen> nuevaImagen = usuarioDto.getFotoUsuario().stream()
                    .map(imagenDto -> {
                        Imagen imagen = new Imagen();
                        imagen.setUrl(imagenDto.getUrl());//establece url nueva
                        imagen.setAlt(imagenDto.getAlt());//establece alt nueva
                        imagen.setUsuario(usuario); // Establecer relación bidireccional
                        return imagen;
                    })
                    .collect(Collectors.toList());
            usuario.getFotoUsuario().addAll(nuevaImagen);
        }*/
        // Manejar la foto INDIVIDUAL
        if (usuarioDto.getFotoPerfil() != null) {
            Imagen imagen = new Imagen();
            imagen.setUrl(usuarioDto.getFotoPerfil().getUrl());
            imagen.setAlt(usuarioDto.getFotoPerfil().getAlt());
            imagen.setUsuario(usuario); // Establecer relación bidireccional
            usuario.setFotoPerfil(imagen);
        }
        if (usuarioDto.getFotoPortada() != null) {
            Imagen imagen = new Imagen();
            imagen.setUrl(usuarioDto.getFotoPortada().getUrl());
            imagen.setAlt(usuarioDto.getFotoPortada().getAlt());
            imagen.setUsuario(usuario); // Establecer relación bidireccional
            usuario.setFotoPortada(imagen);
        }

        Usuario updated = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(updated);


    }



}
