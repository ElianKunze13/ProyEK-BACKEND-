package com.example.demo.controller;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.service.ConocimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ConocimientoController {

    @Autowired
    private ConocimientoService conocimientoService;

    @GetMapping("/frontend")
    public ResponseEntity<List<ConocimientoDto>> getConocimientosFrontend(){
        List<ConocimientoDto> conFrontend =
                conocimientoService.filtrarFrontEnd();
        return ResponseEntity.ok(conFrontend);
    }
    @GetMapping("/backend")
    public ResponseEntity<List<ConocimientoDto>> getConocimientosBackend(){
        List<ConocimientoDto> conBackend =
                conocimientoService.filtrarBackEnd();
        return ResponseEntity.ok(conBackend);
    }
    @GetMapping("/baseDatos")
    public ResponseEntity<List<ConocimientoDto>> getConocimientosBD(){
        List<ConocimientoDto> conBaseDatos =
                conocimientoService.filtrarBD();
        return ResponseEntity.ok(conBaseDatos);
    }
    @GetMapping("/testing")
    public ResponseEntity<List<ConocimientoDto>> getConocimientosTest(){
        List<ConocimientoDto> conTesting =
                conocimientoService.filtrarTesting();
        return ResponseEntity.ok(conTesting);
    }
    @GetMapping("/otros")
    public ResponseEntity<List<ConocimientoDto>> getConocimientosVarios(){
        List<ConocimientoDto> conVarios =
                conocimientoService.filtrarOtros();
        return ResponseEntity.ok(conVarios);
    }
    @DeleteMapping("/borrandoPorId")
    /*ResponseEntity<String> deleteConocimientoById(@RequestParam Integer id) {
        return new ResponseEntity<>("Conocimiento borrrado", HttpStatus.NO_CONTENT);
    }*/
    ResponseEntity deleteConocimientoById(Integer id){
        conocimientoService.deleteConocimientoById(id);
        return ResponseEntity.noContent().build();
    }


}
