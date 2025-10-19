package com.example.demo.authsecurity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {

  @Email(message = "Debe ser un email válido")
  @NotNull(message = "Username/Email no puede ser nulo")
  private String username;

  @NotNull
  @NotBlank
  @Size(min = 8, max = 10, message = "Password debe tener entre 8 y 10 caracteres")
  private String password;

}
