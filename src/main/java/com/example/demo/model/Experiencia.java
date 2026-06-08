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


    /// incluir fecha de inicio
    /// EN CASO DE QUE SEA UN PROYECTO EN CURSO,
    /// LA FECHA DE FIN PUEDE SER NULL O UN VALOR ESPECIAL
    ///  private LocalDate fechaInicioProyecto;


    @Column(name = "fechaFinProyecto")
    @NotNull
    private LocalDate fechaFinProyecto;

    /// incluir aporte personal especifico en caso de ser un proyecto colaborativo
    @NotNull
    @NotEmpty
    @Size(min = 5, max = 300, message = "La descripción debe tener entre 5 y 301 caracteres")
    private String descripcion;

    /// INCLUIR LINK A PROYECTO ((GITHUB, LINKEDIN, PORTFOLIO))
    /// private String link;
    @NotNull
    @NotEmpty
    @Size(min = 5, max = 300, message = "Link debe tener entre 5 y 301 caracteres")
    private String link;

    ///incluir tecnologias y tipo de experiencia como tags
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoExperiencia tipoExperiencia;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TecnologiaUsada tecnologiaUsada;

    /// incluir captura de pantalla o imagen representativa del proyecto
    //IMPORTANTE
    //incluir relacion bidireccional con modelo Imagen
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "imagen_id", referencedColumnName = "id")
    @JsonManagedReference
    private Imagen imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
