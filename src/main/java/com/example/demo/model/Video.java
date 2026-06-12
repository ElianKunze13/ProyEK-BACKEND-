package com.example.demo.model;

import com.example.demo.enums.TipoVideo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "video")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "path", nullable = false, length = 500)
    private String path;  // Ruta física donde se almacena el video

    @Column(name = "nombre_original", nullable = false, length = 255)
    private String nombreOriginal;  // Nombre original del archivo subido

   /* @Column(name = "tipo_mime")
    @Enumerated(EnumType.STRING)  // ← Guardar como STRING en BD
    @NotNull
    private TipoVideo tipoMime;  // ← Usar Enum en lugar de String
*/
    // Relación con Usuario
    @OneToOne(mappedBy = "videoPresentacion")
    @JsonBackReference
    private Usuario usuario;
}