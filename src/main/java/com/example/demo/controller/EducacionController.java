package com.example.demo.controller;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.EducacionDto;
import com.example.demo.model.Educacion;
import com.example.demo.service.EducacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EducacionController {

    @Autowired
    private EducacionService educacionService;


    @GetMapping("/todas/educaciones")
    public ResponseEntity<List<EducacionDto>> getAllEducaciones(){
        ResponseEntity responseEntity = ResponseEntity.ok(educacionService.getAllEducacion());
        return responseEntity;
    }

    @PostMapping("/auth/guardar/educacion")
    ResponseEntity<EducacionDto> saveEducacion(@RequestBody @Valid EducacionDto educacionDto) {
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(educacionService.saveEducacion(educacionDto));
        return responseEntity;
    }

    @PutMapping("/auth/modificar/educacion")
    ResponseEntity<EducacionDto> updateEducacion(@PathVariable Integer id, @RequestBody @Valid EducacionDto educacionDto) {
        try {
            EducacionDto educacionActualizada = educacionService.actualizarEducacionPorId(id, educacionDto);
            return ResponseEntity.ok(educacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/borrar/educacion/{id}")
    ResponseEntity<Void> deleteEducacionById(@PathVariable Integer id) {
        educacionService.deleteEducacionPorId(id);
        return ResponseEntity.noContent().build();
    }
}
