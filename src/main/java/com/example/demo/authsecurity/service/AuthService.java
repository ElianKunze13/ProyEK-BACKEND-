package edu.ies63.prog2.mascotas.authsecurity.service;


 import edu.ies63.prog2.mascotas.authsecurity.dto.AuthRequest;
 import edu.ies63.prog2.mascotas.authsecurity.dto.AuthResponse;
 import edu.ies63.prog2.mascotas.dto.UsuarioDto;

public interface AuthService {
  AuthResponse register(UsuarioDto request);

  AuthResponse authenticate(AuthRequest request);


}