package com.example.demo.authsecurity.service;


import com.example.demo.authsecurity.dto.AuthRequest;
import com.example.demo.authsecurity.dto.AuthResponse;
import com.example.demo.dto.UsuarioDto;

public interface AuthService {
  AuthResponse register(UsuarioDto request);

  AuthResponse authenticate(AuthRequest request);


}