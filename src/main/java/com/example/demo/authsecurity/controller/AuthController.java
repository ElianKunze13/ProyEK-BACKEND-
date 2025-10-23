package edu.ies63.prog2.mascotas.authsecurity.controller;

import edu.ies63.prog2.mascotas.authsecurity.dto.AuthRequest;
import edu.ies63.prog2.mascotas.authsecurity.dto.AuthResponse;
import edu.ies63.prog2.mascotas.authsecurity.service.AuthService;
import edu.ies63.prog2.mascotas.dto.UsuarioDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody UsuarioDto request) {
    return ResponseEntity.ok(authService.register(request));}
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
    return ResponseEntity.ok(authService.authenticate(request));   }}
