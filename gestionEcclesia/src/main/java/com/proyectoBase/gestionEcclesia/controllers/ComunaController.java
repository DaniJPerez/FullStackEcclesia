package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.ComunaDTO;
import com.proyectoBase.gestionEcclesia.modele.Comuna;
import com.proyectoBase.gestionEcclesia.services.ComunaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comunas")
public class ComunaController {

    private final ComunaService comunaService;

    @PostMapping("/crear")
    public ResponseEntity<ComunaDTO> crear(@Valid @RequestBody ComunaDTO comunaDTO) {
        Comuna creada = comunaService.save(comunaDTO);
        return new ResponseEntity<>(
                comunaService.convertToDTO(creada),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaDTO> getComunaById(@PathVariable Long id) {
        Comuna comuna = comunaService.findById(id);
        return ResponseEntity.ok(
                comunaService.convertToDTO(comuna)
        );
    }

    @GetMapping("/mostraTodos")
    public ResponseEntity<List<ComunaDTO>> getAllComunas() {
        List<ComunaDTO> comunas = comunaService.findAll().stream()
                .map(comunaService::convertToDTO)
                .toList();
        return ResponseEntity.ok(comunas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComunaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ComunaDTO comunaDTO) {
        Comuna actualizada = comunaService.update(id, comunaDTO);
        return ResponseEntity.ok(
                comunaService.convertToDTO(actualizada)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comunaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
