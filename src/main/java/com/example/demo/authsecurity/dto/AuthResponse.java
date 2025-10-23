package edu.ies63.prog2.mascotas.authsecurity.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
  private String token;
}
