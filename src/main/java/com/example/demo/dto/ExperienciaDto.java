package com.example.demo.dto;

import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienciaDto {

    private Integer id;
    private String titulo;
    private LocalDate fechaFinProyecto;
    private String descripcion;
    private String link;

    ///incluir imagen
    private ImagenDto imagen;
    private TipoExperiencia tipoExperiencia;
    private TecnologiaUsada tecnologiaUsada;

}
