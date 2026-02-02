package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.ZonaAdministrativaDto;
import com.proyectoBase.gestionEcclesia.modele.ZonaAdministrativa;
import com.proyectoBase.gestionEcclesia.services.ZonaAdministrativaServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/zonasadministrativas")
@RequiredArgsConstructor
public class ZonaAdministrativaController {

    private final ZonaAdministrativaServices zonaAdministrativaServices;

    // ==========================
    // MOSTRAR TODOS
    // ==========================
    @GetMapping("/mostratodos")
    public ResponseEntity<List<ZonaAdministrativaDto>> getAllZonasAdministrativas() {

        List<ZonaAdministrativaDto> zonas = zonaAdministrativaServices.getAllZonasAdministrativas().stream()
                .map(zonaAdministrativaServices::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(zonas);
    }

    // ==========================
    // BUSCAR POR ID
    // ==========================
    @GetMapping("/buscar/{id}")
    public ResponseEntity<ZonaAdministrativaDto> getZonaAdministrativaById(
            @PathVariable Long id) {

        ZonaAdministrativa zona = zonaAdministrativaServices.getZonaAdministrativaById(id);
        return ResponseEntity.ok(zonaAdministrativaServices.convertToDto(zona));
    }

    // ==========================
    // GUARDAR
    // ==========================

    @PostMapping("/guardarzonaadministrativa")
    public ResponseEntity<ZonaAdministrativaDto> createZonaAdministrativa(
            @Valid @RequestBody ZonaAdministrativaDto zonaAdministrativaDTO) {

        ZonaAdministrativa zona = zonaAdministrativaServices.createZonaAdministrativa(zonaAdministrativaDTO);

        return new ResponseEntity<>(
                zonaAdministrativaServices.convertToDto(zona),
                HttpStatus.CREATED
        );
    }

    // ==========================
    // ACTUALIZAR
    // ==========================
    @PutMapping("/{id}")
    public ResponseEntity<ZonaAdministrativaDto> updateZonaAdministrativa(
            @PathVariable Long id,
            @Valid @RequestBody ZonaAdministrativaDto zonaAdministrativaDTO) {

        ZonaAdministrativa zona = zonaAdministrativaServices.updateZonaAdministrativa(id, zonaAdministrativaDTO);
        return ResponseEntity.ok(zonaAdministrativaServices.convertToDto(zona));
    }

    // ==========================
    // ELIMINAR
    // ==========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZonaAdministrativa(
            @PathVariable Long id) {

        zonaAdministrativaServices.deleteZonaAdministrativa(id);
        return ResponseEntity.noContent().build();
    }
}


