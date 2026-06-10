package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDto {
    private Integer id;
    private String path;
    private String nombreOriginal;

    //private TipoVideo tipoMime;  // ← Enum en lugar de String
}