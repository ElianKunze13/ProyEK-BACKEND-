package com.example.demo.controller;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.enums.TipoConocimiento;
import com.example.demo.service.ConocimientoService;
import jakarta.validation.Valid;
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
                conocimientoService.filtrarFrontEnd(TipoConocimiento.FRONTEND);
        return ResponseEntity.ok(conFrontend);
    }
    @GetMapping("/backend")
    public ResponseEntity<List<ConocimientoDto>> getConocimientosBackend(){
        List<ConocimientoDto> conBackend =
                conocimientoService.filtrarBackEnd(TipoConocimiento.BACKEND);
        return ResponseEntity.ok(conBackend);
    }
    @GetMapping("/baseDatos")
    public ResponseEntity<List<ConocimientoDto>> getConocimientosBD(){
        List<ConocimientoDto> conBaseDatos =
                conocimientoService.filtrarBD(TipoConocimiento.BASE_DATOS);
        return ResponseEntity.ok(conBaseDatos);
    }
    @GetMapping("/testing")
    public ResponseEntity<List<ConocimientoDto>> getConocimientosTest(){
        List<ConocimientoDto> conTesting =
                conocimientoService.filtrarTesting(TipoConocimiento.TESTING);
        return ResponseEntity.ok(conTesting);
    }
    @GetMapping("/otros/conocimientos")
    public ResponseEntity<List<ConocimientoDto>> getConocimientosVarios(){
        List<ConocimientoDto> conVarios =
                conocimientoService.filtrarOtros(TipoConocimiento.OTROS);
        return ResponseEntity.ok(conVarios);
    }

    @PostMapping("/auth/guardar/conocimiento")
    ResponseEntity<ConocimientoDto> saveConocimiento(@RequestBody @Valid ConocimientoDto conocimientoDto) {
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(conocimientoService.saveConocimiento(conocimientoDto));
        return responseEntity;
    }

    @PutMapping("/auth/modificar/conocimiento/{id}")
    ResponseEntity<ConocimientoDto> updateConocimiento(@PathVariable Integer id, @RequestBody @Valid ConocimientoDto conocimientoDto) {
        try {
            ConocimientoDto conocimientoActualizado = conocimientoService.actualizarConocimientoPorId(id, conocimientoDto);
            return ResponseEntity.ok(conocimientoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/borrar/conocimiento/{id}")
    ResponseEntity<Void> deleteConocimientoById(@PathVariable Integer id) {
        conocimientoService.deleteConocimientoById(id);
        return ResponseEntity.noContent().build();
    }


}
