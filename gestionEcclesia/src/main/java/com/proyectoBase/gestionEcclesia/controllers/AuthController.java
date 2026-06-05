package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.LoginUserDTO;
import com.proyectoBase.gestionEcclesia.DTOS.UsuarioDto;
import com.proyectoBase.gestionEcclesia.modele.Usuario;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioServices usuarioServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioDto dto) {
        return ResponseEntity.status(201).body(usuarioServices.createUsuario(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO loginDto) {
        try {
            Usuario usuario = usuarioServices.validacionIngresoUsuario(loginDto);
            return ResponseEntity.ok(usuario);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
