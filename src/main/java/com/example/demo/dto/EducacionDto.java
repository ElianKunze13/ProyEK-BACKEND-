package com.example.demo.dto;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoEducacion;
import com.example.demo.model.Imagen;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducacionDto {

    private Integer id;

    private String titulo;

    //private LocalDate fechaObtencion;

    private String descripcion;

    private TipoEducacion tipoEducacion;


    private List<Imagen> imagenes = new ArrayList<>();
}
