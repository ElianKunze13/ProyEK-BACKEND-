package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeDto {
    private Integer id;

    private String nombre;

    private String email;

    private String mensaje;

    private LocalDate fechaCreacion;
}
