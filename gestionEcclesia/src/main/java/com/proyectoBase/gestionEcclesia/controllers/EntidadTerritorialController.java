package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.EntidadTerritorialDto;
import com.proyectoBase.gestionEcclesia.modele.EntidadTerritorial;
import com.proyectoBase.gestionEcclesia.services.EntidadTerritorialServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entidadesterritoriales")
@RequiredArgsConstructor
public class EntidadTerritorialController {

    private final EntidadTerritorialServices entidadTerritorialService;

    @GetMapping
    public ResponseEntity<List<EntidadTerritorialDto>> getAll() {
        List<EntidadTerritorialDto> lista = entidadTerritorialService.getAllEntidadTerritorials()
                .stream()
                .map(entidadTerritorialService::convertToDto)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntidadTerritorialDto> getById(@PathVariable Long id) {
        EntidadTerritorial entidad = entidadTerritorialService.findByIdEntidadTerritorial(id);
        return ResponseEntity.ok(entidadTerritorialService.convertToDto(entidad));
    }

    @PostMapping
    public ResponseEntity<EntidadTerritorialDto> create(@RequestBody EntidadTerritorialDto dto) {
        EntidadTerritorial entidad = entidadTerritorialService.save(dto);
        return ResponseEntity.status(201).body(entidadTerritorialService.convertToDto(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntidadTerritorialDto> update(@PathVariable Long id, @RequestBody EntidadTerritorialDto dto) {
        EntidadTerritorial entidad = entidadTerritorialService.update(id, dto);
        return ResponseEntity.ok(entidadTerritorialService.convertToDto(entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entidadTerritorialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
