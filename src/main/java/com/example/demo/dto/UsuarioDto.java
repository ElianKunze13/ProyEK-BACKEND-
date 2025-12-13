package com.example.demo.dto;

import com.example.demo.enums.Role;
import com.example.demo.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {

   
    private Integer id;

    @NotNull(message = "Nombre no puede ser nulo")
    @Size(min = 3, max = 145, message = "Nombre debe tener entre 3 y 145 caracteres")
    private String nombre;


    @Email(message = "Debe ser un email válido")
    @NotNull(message = "Username/Email no puede ser nulo")
    private String username;

    @NotNull
    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Rol no puede ser nulo")
    private Role rol;


    @NotNull
    @Size(min=5, max=500, message = "La descripción debe tener entre 5 y 301 caracteres")
    private String descripcion;

    private List<ImagenDto> fotoUsuario = new ArrayList<>();
    //private String fotoPerfilUrl; // Solo la URL de la foto principal


    private  boolean active=true;


}
