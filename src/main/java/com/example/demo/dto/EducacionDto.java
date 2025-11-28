package com.example.demo.dto;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoEducacion;
import com.example.demo.model.Imagen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducacionDto {

    private Integer id;

    private String descripcion;

    private TipoEducacion tipoEducacion;

    private Nivel nivel;

    private List<Imagen> imagenes = new ArrayList<>();
}
