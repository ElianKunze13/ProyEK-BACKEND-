package com.example.demo.service;

import com.example.demo.dto.ConocimientoDto;
import com.example.demo.dto.EducacionDto;
import com.example.demo.enums.TipoConocimiento;


import java.util.List;

public interface ConocimientoService {

    ConocimientoDto saveConocimiento(ConocimientoDto conocimientoDto);

    void deleteConocimientoById(Integer id);
    List<ConocimientoDto> getAllConocimientos();

    // traer lista segun tipo de conocimiento
    List<ConocimientoDto> filtrarFrontEnd(TipoConocimiento tipoConocimiento);
    List<ConocimientoDto> filtrarBackEnd(TipoConocimiento tipoConocimiento);
    List<ConocimientoDto> filtrarBD(TipoConocimiento tipoConocimiento);
    List<ConocimientoDto> filtrarTesting(TipoConocimiento tipoConocimiento);
    List<ConocimientoDto> filtrarOtros(TipoConocimiento tipoConocimiento);
/// definir listas IA y PROTOTIPOS
 List<ConocimientoDto> filtrarIA(TipoConocimiento tipoConocimiento);
  List<ConocimientoDto> filtrarPrototipos(TipoConocimiento tipoConocimiento);
    ConocimientoDto actualizarConocimientoPorId(Integer id, ConocimientoDto conocimientoDto);
}
