package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.UsuarioDto;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    // opcional: si quieres login programático puedes usar authenticationManager (más avanzado)
}
