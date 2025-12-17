package com.example.demo.dto;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoConocimiento;
import com.example.demo.model.Imagen;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConocimientoDto {

    private Integer id;


    private String nombre;

    private Nivel nivel;

    private TipoConocimiento tipoConocimiento;

    //private List<ImagenDto> imagenes = new ArrayList<>();
    private List<ImagenDto> imagenes;

}
