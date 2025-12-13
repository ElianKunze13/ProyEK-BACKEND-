package com.example.demo.controller;


import com.example.demo.dto.UsuarioDto;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET /usuarios/{id}
    @GetMapping("/auth/traerPor/{id}")
    ResponseEntity<UsuarioDto> getUsuarioById(@PathVariable Integer id) {
        ResponseEntity responseEntity = ResponseEntity.ok(usuarioService.getById(id));
        return responseEntity;
    }
    @GetMapping("/username/{username}")
    ResponseEntity<UsuarioDto> getUsuarioByUsername(@PathVariable String username) {

        System.out.println("Buscando usuario con username: " + username);
        UsuarioDto usuario = usuarioService.getByUsername(username);
        return ResponseEntity.ok(usuario);
    }

    // POST /usuarios
    @PostMapping("/guardarUsuario")
    ResponseEntity<UsuarioDto> saveUsuario(@RequestBody UsuarioDto usuarioDto) {
        ResponseEntity responseEntity = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.create(usuarioDto));
        return responseEntity;
    }

    // PUT /usuarios/{id}
    @PutMapping("/usuario/{id}")
    ResponseEntity<UsuarioDto> updateUsuario(@PathVariable Integer id, @RequestBody UsuarioDto usuarioDto) {
        ResponseEntity responseEntity = ResponseEntity.ok(usuarioService.update(id,usuarioDto));
        return responseEntity;
    }

}
