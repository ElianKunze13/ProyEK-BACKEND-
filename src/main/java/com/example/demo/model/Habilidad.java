package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "Habilidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habilidad {

    /// habilidades serian capacidades blandas
    /// (comunicacion, trabajo en equipo, adaptabilidad, rapidez de aprendisaje, trabajo bajo presion)
    /// va en seccion SOBRE MI

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Size(min = 3, max = 145, message = "Nombre debe tener entre 3 y 20 caracteres")
    private String nombre;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
