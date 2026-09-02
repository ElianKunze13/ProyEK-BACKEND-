package com.example.demo.dto;

import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienciaDto {

    private Integer id;
    private String titulo;
    private LocalDate fechaInicioProyecto;
    private LocalDate fechaFinProyecto;
    private String descripcion;
    private String link;
    private ImagenDto imagen;
    private TipoExperiencia tipoExperiencia;

    // 🔥 CAMBIO PRINCIPAL: de Enum a List<Enum>
    private List<TecnologiaUsada> tecnologiasUsadas;
}