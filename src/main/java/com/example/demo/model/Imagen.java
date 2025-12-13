package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conocimiento_id")
    @JsonIgnore  // Evita que se serialice conocimiento
    private Conocimiento conocimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "educacion_id")
    @JsonIgnore  // Evita que se serialice educacion
    private Educacion educacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @JsonIgnore  // Evita que se serialice usuario
    private Usuario usuario;


}
