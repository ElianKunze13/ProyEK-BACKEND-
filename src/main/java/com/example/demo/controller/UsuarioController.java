package com.example.demo.controller;


import com.example.demo.dto.UsuarioDto;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET /usuarios/{id}
    @GetMapping("/{id}")
    ResponseEntity<UsuarioDto> getUsuarioById(@PathVariable Integer id) {
        ResponseEntity responseEntity = ResponseEntity.ok(usuarioService.getById(id));
        return responseEntity;
    }
    @GetMapping("/username")
    ResponseEntity<UsuarioDto> getUsuarioByUsername(String username) {
        ResponseEntity responseEntity = ResponseEntity.ok(usuarioService.getByUsername(username));
        return responseEntity;
    }

    // POST /usuarios
    @PostMapping
    ResponseEntity<UsuarioDto> saveUsuario(@RequestBody UsuarioDto usuarioDto) {
        ResponseEntity responseEntity = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.create(usuarioDto));
        return responseEntity;
    }

    // PUT /usuarios/{id}
    @PutMapping("/{id}")
    ResponseEntity<UsuarioDto> updateUsuario(@PathVariable Integer id, @RequestBody UsuarioDto usuarioDto) {
        ResponseEntity responseEntity = ResponseEntity.ok(usuarioService.update(usuarioDto));
        return responseEntity;
    }

}
