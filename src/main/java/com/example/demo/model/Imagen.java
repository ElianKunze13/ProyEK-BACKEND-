package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    // Relación OneToOne con Conocimiento
    @OneToOne(mappedBy = "imagen", fetch = FetchType.LAZY)
    @JoinColumn(name = "conocimiento_id")
    @JsonBackReference("imagen-conocimiento")
    @JsonIgnore
    private Conocimiento conocimiento;

    @OneToOne(mappedBy = "imagen", fetch = FetchType.LAZY)
    @JoinColumn(name = "educacion_id")
    @JsonIgnore
    @JsonBackReference("imagen-educacion")
    private Educacion educacion;

    // CAMBIADO: de OneToOne a ManyToOne para permitir múltiples imágenes por experiencia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiencia_id")
    @JsonIgnore
    @JsonBackReference("imagen-experiencia")
    private Experiencia experiencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference("imagen-usuario")
    @JsonIgnore
    private Usuario usuario;
}