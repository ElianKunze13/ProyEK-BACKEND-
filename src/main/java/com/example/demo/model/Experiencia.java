package com.example.demo.model;


import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "Experiencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experiencia {

    /// experiencia serian proyectos o aportes realizados (porfolio, apps, codigo abierto)
    /// va en seccion SOBRE MI

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Size(min = 3, max = 145, message = "Nombre debe tener entre 3 y 50 caracteres")
    private String titulo;

    @Column(name = "fechaYHora")
    @NotNull
    private LocalDate fechaHora;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 300, message = "La descripción debe tener entre 5 y 301 caracteres")
    private String descripcion;    // incluir en descripcion aporte personal en caso de ser un proyecto colaborativo

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoExperiencia tipoExperiencia; //incluir tecnologias y tipo de experiencia como tags

    @NotNull
    @Enumerated(EnumType.STRING)
    private TecnologiaUsada tecnologiaUsada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
