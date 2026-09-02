package com.example.demo.model;


import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoExperiencia;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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


    @Column(name = "fechaInicioProyecto")
    @NotNull
    private LocalDate fechaInicioProyecto;

    ///modificar fechaFinProyecto para que pueda ser null o un valor especial
    /// en caso de proyectos en curso
    @Column(name = "fechaFinProyecto")
    private LocalDate fechaFinProyecto;

    /// incluir aporte personal especifico en caso de ser un proyecto colaborativo
    @NotNull
    @NotEmpty
    @Size(min = 5, max = 300, message = "La descripción debe tener entre 5 y 301 caracteres")
    private String descripcion;

    /// INCLUIR LINK A PROYECTO ((GITHUB, LINKEDIN, PORTFOLIO))
    @NotNull
    @NotEmpty
    @Size(min = 5, max = 300, message = "Link debe tener entre 5 y 301 caracteres")
    private String link;

    ///tipoExperiencia definida como tags
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoExperiencia tipoExperiencia;

    // tecnologiausada debe cambiarse y redefinirse como lista,
    // para poder incluir varias tecnologias usadas en un proyecto
    @NotNull
    @Enumerated(EnumType.STRING)
    private TecnologiaUsada tecnologiaUsada;

    /// para imagen representativa del proyecto
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "imagen_id", referencedColumnName = "id")
    @JsonManagedReference
    private Imagen imagen;

    /// relacion con usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
