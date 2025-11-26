package com.example.demo.service.Impl;

import com.example.demo.dto.UsuarioDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UsuarioMapper;
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
    public UsuarioDto update(UsuarioDto usuarioDto) {
        return null;
    }



}
