package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.BarrioDTO;
import com.proyectoBase.gestionEcclesia.modele.Barrio;
import com.proyectoBase.gestionEcclesia.services.BarrioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/barrios")
@RequiredArgsConstructor
public class BarrioController {

    private final BarrioService barrioService;

    @GetMapping
    public ResponseEntity<List<BarrioDTO>> getAllBarrios() {
        List<BarrioDTO> barrios = barrioService.findAll().stream()
                .map(barrioService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(barrios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarrioDTO> getBarrioById(@PathVariable Long id) {
        Barrio barrio = barrioService.findByid(id);
        return ResponseEntity.ok(barrioService.convertToDTO(barrio));
    }

    @PostMapping
    public ResponseEntity<BarrioDTO> createBarrio(@Valid @RequestBody BarrioDTO barrioDTO) {
        Barrio barrio = barrioService.save(barrioDTO);
        return new ResponseEntity<>(barrioService.convertToDTO(barrio), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarrioDTO> updateBarrio(@PathVariable Long id, @Valid @RequestBody BarrioDTO barrioDTO) {
        Barrio barrio = barrioService.update(id, barrioDTO);
        return ResponseEntity.ok(barrioService.convertToDTO(barrio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarrio(@PathVariable Long id) {
        barrioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

