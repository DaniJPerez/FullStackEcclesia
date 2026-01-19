package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.RecursoEconomicoDTO;
import com.proyectoBase.gestionEcclesia.modele.RecursoEconomico;
import com.proyectoBase.gestionEcclesia.services.RecursoEconomicoService;
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
@RequestMapping("/api/recursos-economicos")
@RequiredArgsConstructor
public class RecursoEconomicoController {

    private final RecursoEconomicoService recursoEconomicoService;

    @GetMapping
    public ResponseEntity<List<RecursoEconomicoDTO>> getAllRecursosEconomicos() {
        List<RecursoEconomicoDTO> recursos = recursoEconomicoService.findAll().stream()
                .map(recursoEconomicoService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoEconomicoDTO> getRecursoEconomicoById(@PathVariable Long id) {
        RecursoEconomico recursoEconomico = recursoEconomicoService.findById(id);
        return ResponseEntity.ok(recursoEconomicoService.convertToDTO(recursoEconomico));
    }

    /*@GetMapping("/entre-fechas")
    public ResponseEntity<List<RecursoEconomicoDTO>> getRecursosEconomicosEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<RecursoEconomicoDTO> recursos = recursoEconomicoService.findByFechas(inicio, fin).stream()
                .map(recursoEconomicoService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }*/

    @PostMapping
    public ResponseEntity<RecursoEconomicoDTO> createRecursoEconomico(@Valid @RequestBody RecursoEconomicoDTO recursoEconomicoDTO) {
        RecursoEconomico recursoEconomico = recursoEconomicoService.save(recursoEconomicoDTO);
        return new ResponseEntity<>(recursoEconomicoService.convertToDTO(recursoEconomico), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoEconomicoDTO> updateRecursoEconomico(@PathVariable Long id, @Valid @RequestBody RecursoEconomicoDTO recursoEconomicoDTO) {
        RecursoEconomico recursoEconomico = recursoEconomicoService.update(id, recursoEconomicoDTO);
        return ResponseEntity.ok(recursoEconomicoService.convertToDTO(recursoEconomico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecursoEconomico(@PathVariable Long id) {
        recursoEconomicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
