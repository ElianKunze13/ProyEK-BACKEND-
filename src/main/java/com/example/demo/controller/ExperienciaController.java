package com.example.demo.controller;

import com.example.demo.dto.ExperienciaDto;
import com.example.demo.service.ExperienciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExperienciaController {

    @Autowired
    private ExperienciaService experienciaService;


    @GetMapping("/todas/experiencias")
    public ResponseEntity<List<ExperienciaDto>> getAllExperiencias(){
        ResponseEntity responseEntity = ResponseEntity.ok(experienciaService.getAllExperiencias());
        return responseEntity;
    }

    @PostMapping("/auth/guardar/experiencia")
    ResponseEntity<ExperienciaDto> saveExperiencia(@RequestBody @Valid ExperienciaDto experienciaDto) {
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(experienciaService.saveExperiencia(experienciaDto));
        return responseEntity;
    }

    @PutMapping("/auth/modificar/experiencias")
    ResponseEntity<ExperienciaDto> updateExperiencia(@PathVariable Integer id, @RequestBody @Valid ExperienciaDto experienciaDto) {
        try {
            ExperienciaDto experienciaActualizada = experienciaService.actualizarExperienciaPorId(id, experienciaDto);
            return ResponseEntity.ok(experienciaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/borrar/experiencia/{id}")
    ResponseEntity<Void> deleteExperienciaById(@PathVariable Integer id) {
        experienciaService.deleteExperienciaPorId(id);
        return ResponseEntity.noContent().build();
    }

}
