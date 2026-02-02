package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.DireccionDTO;
import com.proyectoBase.gestionEcclesia.modele.Direccion;
import com.proyectoBase.gestionEcclesia.services.DireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> getAllDirecciones() {
        List<DireccionDTO> direcciones = direccionService.findAll().stream()
                .map(direccionService::converToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(direcciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> getDireccionById(@PathVariable Long id) {
        Direccion direccion = direccionService.findById(id);
        return ResponseEntity.ok(direccionService.converToDTO(direccion));
    }

    @PostMapping
    public ResponseEntity<DireccionDTO> createDireccion(@Valid @RequestBody DireccionDTO direccionDTO) {
        Direccion direccion = direccionService.save(direccionDTO);
        return new ResponseEntity<>(direccionService.converToDTO(direccion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionDTO> updateDireccion(@PathVariable Long id, @Valid @RequestBody DireccionDTO direccionDTO) {
        Direccion direccion = direccionService.update(id, direccionDTO);
        return ResponseEntity.ok(direccionService.converToDTO(direccion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDireccion(@PathVariable Long id) {
        direccionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

