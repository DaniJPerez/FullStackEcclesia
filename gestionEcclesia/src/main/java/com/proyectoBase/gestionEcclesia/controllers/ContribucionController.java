package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.ContribucionDTO;
import com.proyectoBase.gestionEcclesia.modele.Contribucion;
import com.proyectoBase.gestionEcclesia.modele.TipoContribucion;
import com.proyectoBase.gestionEcclesia.services.ContribucionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contribuciones")
@RequiredArgsConstructor
public class ContribucionController {

    private final ContribucionService contribucionService;

    @GetMapping("/mostrarTodos")
    public ResponseEntity<List<ContribucionDTO>> getAllContribuciones() {
        List<ContribucionDTO> contribuciones = contribucionService.findAll().stream()
                .map(contribucionService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contribuciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContribucionDTO> getContribucionById(@PathVariable Long id) {
        Contribucion contribucion = contribucionService.findById(id);
        return ResponseEntity.ok(contribucionService.convertToDTO(contribucion));
    }

    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<ContribucionDTO>> getContribucionesByMiembro(@PathVariable Long miembroId) {
        List<ContribucionDTO> contribuciones = contribucionService.findByMiembro(miembroId).stream()
                .map(contribucionService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contribuciones);
    }

    @GetMapping("/entre-fechas")
    public ResponseEntity<List<ContribucionDTO>> getContribucionesEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
            ) {
        List<ContribucionDTO> contribuciones = contribucionService.findByFechas(inicio, fin).stream()
                .map(contribucionService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contribuciones);
    }

    @PostMapping
    public ResponseEntity<ContribucionDTO> createContribucion(@Valid @RequestBody ContribucionDTO contribucionDTO) {
        Contribucion contribucion = contribucionService.save(contribucionDTO);
        return new ResponseEntity<>(contribucionService.convertToDTO(contribucion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContribucionDTO> updateContribucion(@PathVariable Long id, @Valid @RequestBody ContribucionDTO contribucionDTO) {
        Contribucion contribucion = contribucionService.update(id, contribucionDTO);
        return ResponseEntity.ok(contribucionService.convertToDTO(contribucion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContribucion(@PathVariable Long id) {
        contribucionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
