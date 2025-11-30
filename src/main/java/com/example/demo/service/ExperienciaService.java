package com.example.demo.service;

import com.example.demo.dto.ExperienciaDto;

import java.util.List;

public interface ExperienciaService {

    ExperienciaDto saveExperiencia(ExperienciaDto experienciaDto);

    ExperienciaDto actualizarExperienciaPorId(Integer id, ExperienciaDto experienciaDto);

    void deleteExperienciaPorId(Integer id);

    List<ExperienciaDto> getAllExperiencias();


}
