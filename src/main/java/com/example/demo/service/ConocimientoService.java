package com.example.demo.service;

import com.example.demo.dto.ConocimientoDto;


import java.util.List;

public interface ConocimientoService {

    ConocimientoDto saveConocimiento(ConocimientoDto conocimientoDto);

    void deleteConocimientoById(Integer id);

    //definir metodos para traer segun tipo de conocimiento
    List<ConocimientoDto> filtrarFrontEnd ();
    List<ConocimientoDto> filtrarBackEnd();
    List<ConocimientoDto> filtrarBD();
    List<ConocimientoDto> filtrarTesting();
    List<ConocimientoDto> filtrarOtros();

    ConocimientoDto actualizarConocimientoPorId(Integer id, ConocimientoDto conocimientoDto);
}
