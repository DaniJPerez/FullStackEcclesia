package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.NacionalidadDTO;
import com.proyectoBase.gestionEcclesia.modele.Nacionalidad;
import com.proyectoBase.gestionEcclesia.services.NacionalidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nacionalidades")
@RequiredArgsConstructor
public class NacionalidadController {

    private final NacionalidadService nacionalidadService;

    @GetMapping
    public ResponseEntity<List<NacionalidadDTO>> getAll() {
        List<NacionalidadDTO> lista = nacionalidadService.getAll().stream()
                .map(nacionalidadService::convertToDto)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NacionalidadDTO> getById(@PathVariable Long id) {
        Nacionalidad n = nacionalidadService.findById(id);
        return ResponseEntity.ok(nacionalidadService.convertToDto(n));
    }

    @PostMapping
    public ResponseEntity<NacionalidadDTO> create(@RequestBody NacionalidadDTO dto) {
        Nacionalidad n = nacionalidadService.save(dto);
        return ResponseEntity.status(201).body(nacionalidadService.convertToDto(n));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NacionalidadDTO> update(@PathVariable Long id, @RequestBody NacionalidadDTO dto) {
        Nacionalidad n = nacionalidadService.update(id, dto);
        return ResponseEntity.ok(nacionalidadService.convertToDto(n));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        nacionalidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
