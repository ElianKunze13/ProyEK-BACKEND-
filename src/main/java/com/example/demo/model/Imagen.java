package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;;
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
    @JsonIgnore  // Evita que se serialice (al crear conocimiento nuevo, atributo img conecte con
    // otros modelos que tambien poseen una imagen y por ende genera error de referencia circular)
    private Conocimiento conocimiento;



   @OneToOne(mappedBy = "imagen", fetch = FetchType.LAZY)
   @JoinColumn(name = "educacion_id")
    @JsonIgnore  // Evita que se serialice educacion
    @JsonBackReference("imagen-educacion")
    private Educacion educacion;

    @OneToOne(mappedBy = "imagen", fetch = FetchType.LAZY)
    @JoinColumn(name = "experiencia_id")
    @JsonIgnore  // Evita que se serialice educacion
    @JsonBackReference("imagen-experiencia")
    private Experiencia experiencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference("imagen-usuario")
    @JsonIgnore  // Evita que se serialice usuario
    private Usuario usuario;


}
