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
import java.util.List;

@Entity(name = "Experiencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experiencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Size(min = 3, max = 145, message = "Nombre debe tener entre 3 y 50 caracteres")
    private String titulo;

    @Column(name = "fechaInicioProyecto")
    @NotNull
    private LocalDate fechaInicioProyecto;

    @Column(name = "fechaFinProyecto")
    private LocalDate fechaFinProyecto;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 300, message = "La descripción debe tener entre 5 y 301 caracteres")
    private String descripcion;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 300, message = "Link debe tener entre 5 y 301 caracteres")
    private String link;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoExperiencia tipoExperiencia;

    @NotNull
    @ElementCollection(targetClass = TecnologiaUsada.class)
    @CollectionTable(name = "experiencia_tecnologias",
            joinColumns = @JoinColumn(name = "experiencia_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tecnologia")
    private List<TecnologiaUsada> tecnologiasUsadas;

    // CAMBIAR de imagen a List<imagenes>
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "imagen_id", referencedColumnName = "id")
    @JsonManagedReference
    private Imagen imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}