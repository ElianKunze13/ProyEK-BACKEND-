package com.example.demo.model;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoEducacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "Educacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Educacion {

    /// educacion serian cursos, talleres o carreras estudiadas (tecnicaturas, campamentos)
    /// va en seccion ESTUDIOS

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Size(min = 3, max = 145, message = "Nombre debe tener entre 3 y 50 caracteres")
    private String titulo;

    /*@Column(name = "fechaObtencion")
    @NotNull
    private LocalDate fechaObtencion;*/

    @NotNull
    @NotEmpty
    @Size(min=5, max=300, message = "La descripción debe tener entre 5 y 301 caracteres")
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoEducacion tipoEducacion;



    //incluir imagen de certificado o pdf
    @OneToMany(mappedBy = "educacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
