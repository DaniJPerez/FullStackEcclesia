package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.LoginUserDTO;
import com.proyectoBase.gestionEcclesia.DTOS.UsuarioDto;
import com.proyectoBase.gestionEcclesia.modele.Usuario;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServices usuarioServices;

    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody UsuarioDto dto) {
        Usuario usuario = usuarioServices.createUsuario(dto);
        return ResponseEntity.status(201).body(usuario);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody LoginUserDTO loginDto,
                                            @RequestParam String newPassword) {
        Usuario actualizado = usuarioServices.updatePassword(loginDto, newPassword);
        if (actualizado == null) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
        return ResponseEntity.ok(actualizado);
    }
}
