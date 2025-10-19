package com.example.demo.model;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TipoEducacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity(name = "Educacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Educacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotEmpty
    @Size(min=5, max=300, message = "La descripción debe tener entre 5 y 301 caracteres")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoEducacion tipoEducacion;

    @Enumerated(EnumType.STRING)
    private Nivel nivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
