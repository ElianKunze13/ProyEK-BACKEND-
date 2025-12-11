package com.example.demo.controller;

import com.example.demo.dto.HabilidadDto;
import com.example.demo.service.HabilidadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HabilidadController {

    @Autowired
    private HabilidadService habilidadService;

    @GetMapping("/todas/habilidades")
    public ResponseEntity<List<HabilidadDto>> getAllHabilidades(){
        ResponseEntity responseEntity = ResponseEntity.ok(habilidadService.getAllHabilidad());
        return responseEntity;
    }

    @PostMapping("/auth/guardar/habilidad")
    ResponseEntity<HabilidadDto> saveHabilidad(@RequestBody @Valid HabilidadDto habilidadDto) {
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(habilidadService.saveHabilidad(habilidadDto));
        return responseEntity;
    }

    @PutMapping("/auth/modificar/habilidad")
    ResponseEntity<HabilidadDto> updateExperiencia(@PathVariable Integer id, @RequestBody @Valid HabilidadDto habilidadDto) {
        try {
            HabilidadDto habilidadActualizada = habilidadService.actualizarHabilidadPorId(id, habilidadDto);
            return ResponseEntity.ok(habilidadActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/borrar/habilidad/{id}")
    ResponseEntity<Void> deleteHabilidadById(@PathVariable Integer id) {
        habilidadService.deleteHabilidadPorId(id);
        return ResponseEntity.noContent().build();
    }




}
