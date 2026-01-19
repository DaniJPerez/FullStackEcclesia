package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.RolDTO;
import com.proyectoBase.gestionEcclesia.modele.Rol;
import com.proyectoBase.gestionEcclesia.services.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping("/mostrarTodos")
    public ResponseEntity<List<RolDTO>> getAllRoles() {
        List<RolDTO> roles = rolService.findAll().stream()
                .map(rolService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getRolById(@PathVariable Long id) {
        Rol rol = rolService.findById(id);
        return ResponseEntity.ok(rolService.convertToDTO(rol));
    }

    @PostMapping("/agregar")
    public ResponseEntity<RolDTO> createRol(@Valid @RequestBody RolDTO rolDTO) {
        Rol rol = rolService.save(rolDTO);
        return new ResponseEntity<>(rolService.convertToDTO(rol), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> updateRol(@PathVariable Long id, @Valid @RequestBody RolDTO rolDTO) {
        Rol rol = rolService.update(id, rolDTO);
        return ResponseEntity.ok(rolService.convertToDTO(rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
        rolService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
