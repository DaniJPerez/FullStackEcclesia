package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.RecursoFisicoDTO;
import com.proyectoBase.gestionEcclesia.modele.RecursoFisico;
import com.proyectoBase.gestionEcclesia.services.RecursoFisicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recursos-fisicos")
@RequiredArgsConstructor
public class RecursoFisicoController {

    private final RecursoFisicoService recursoFisicoService;

    @GetMapping
    public ResponseEntity<List<RecursoFisicoDTO>> getAllRecursosFisicos() {
        List<RecursoFisicoDTO> recursos = recursoFisicoService.findAll().stream()
                .map(recursoFisicoService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoFisicoDTO> getRecursoFisicoById(@PathVariable Long id) {
        RecursoFisico recursoFisico = recursoFisicoService.findById(id);
        return ResponseEntity.ok(recursoFisicoService.convertToDTO(recursoFisico));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RecursoFisicoDTO>> getRecursosFisicosByDescripcion(@RequestParam String descripcion) {
        List<RecursoFisicoDTO> recursos = recursoFisicoService.findByDescripcion(descripcion).stream()
                .map(recursoFisicoService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    @PostMapping
    public ResponseEntity<RecursoFisicoDTO> createRecursoFisico(@Valid @RequestBody RecursoFisicoDTO recursoFisicoDTO) {
        RecursoFisico recursoFisico = recursoFisicoService.save(recursoFisicoDTO);
        return new ResponseEntity<>(recursoFisicoService.convertToDTO(recursoFisico), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoFisicoDTO> updateRecursoFisico(@PathVariable Long id, @Valid @RequestBody RecursoFisicoDTO recursoFisicoDTO) {
        RecursoFisico recursoFisico = recursoFisicoService.update(id, recursoFisicoDTO);
        return ResponseEntity.ok(recursoFisicoService.convertToDTO(recursoFisico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecursoFisico(@PathVariable Long id) {
        recursoFisicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
