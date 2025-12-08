package com.example.demo.dto;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoEducacion;
import com.example.demo.model.Imagen;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducacionDto {

    private Integer id;

    private String titulo;

    private String descripcion;

    private TipoEducacion tipoEducacion;

    private Nivel nivel;

    private List<Imagen> imagenes = new ArrayList<>();
}
