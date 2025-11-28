package com.example.demo.service;

import com.example.demo.dto.EducacionDto;

import java.util.List;

public interface EducacionService {

    EducacionDto saveEducacion(EducacionDto educacionDto);

    EducacionDto actualizarEducacionPorId(Integer id, EducacionDto educacionDto);

    void deleteEducacionPorId(Integer id);

    List<EducacionDto> getAllEducacion();


}
