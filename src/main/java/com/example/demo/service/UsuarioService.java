package com.example.demo.service;

import com.example.demo.dto.UsuarioDto;

import java.util.List;

public interface UsuarioService {

    UsuarioDto getById(Integer id);
    UsuarioDto getByUsername(String username);

    UsuarioDto create(UsuarioDto usuarioDto);

    UsuarioDto update(Integer id, UsuarioDto usuarioDto);


}
