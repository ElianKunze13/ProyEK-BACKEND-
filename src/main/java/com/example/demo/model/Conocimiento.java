package com.example.demo.model;

import com.example.demo.enums.Nivel;
import com.example.demo.enums.TecnologiaUsada;
import com.example.demo.enums.TipoConocimiento;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    /// va en seccion HERRAMIENTAS en Frontend

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
    /// cambiar de lista de imagen a una imagen unica, ya que cada conocimiento
    ///  solo tendra un icono asociado
    /*@OneToMany(mappedBy = "conocimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Esto permite serializar las imágenes
    private List<Imagen> imagenes = new ArrayList<>();*/

    // Cambiado de OneToMany a OneToOne - cada conocimiento tiene UNA imagen
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "imagen_id", referencedColumnName = "id")
    @JsonManagedReference
    private Imagen imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
