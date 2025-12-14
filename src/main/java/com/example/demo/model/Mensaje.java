package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "Mensaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensaje {

    /// va en seccion CONTACTOS

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String nombreUsuario;

    @NonNull
    @Size(min= 10, max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String email;

    @Size(max = 300, message = "El mensaje no puede exceder 1000 caracteres")
    private String mensaje;

    @Column(name = "fecha_creacion")
    //atributo fechaCeacion debe ser fecha actual
    private LocalDate fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDate.now();
    }
}
