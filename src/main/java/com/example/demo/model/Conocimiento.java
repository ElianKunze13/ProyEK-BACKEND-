package com.example.demo.model;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoConocimiento;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Conocimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conocimiento {

    /// conocimientos serian las tecnologias usadas/aprendidas
    ///(html, css, java, angular, springboot, bootstrap, tailwind)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Size(min = 3, max = 145, message = "Nombre debe tener entre 3 y 20 caracteres")
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Nivel nivel;

    //estaran agrupados en listas por su tipo (frontend, backend, test, AI)
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoConocimiento tipoConocimiento;

    //conocimiento tendra imagen de icono
    @OneToMany(mappedBy = "conocimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
