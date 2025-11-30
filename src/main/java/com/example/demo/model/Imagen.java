package com.example.demo.model;

import jakarta.persistence.*;;
import lombok.*;

@Entity(name = "Imagen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String url;

    @NonNull
    private String alt;

    @ManyToOne
    @JoinColumn(name = "conocimiento_id")
    private Conocimiento conocimiento;

    @ManyToOne
    @JoinColumn(name = "educacion_id")
    private Educacion educacion;
}
