package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.TipoContribucionDTO;
import com.proyectoBase.gestionEcclesia.modele.TipoContribucion;
import com.proyectoBase.gestionEcclesia.services.TipoContribucionServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipocontribuciones")
@RequiredArgsConstructor
public class TipoContribucionController {

    private final TipoContribucionServices tipoContribucionServices;

    // ==========================
    // MOSTRAR TODOS
    // ==========================
    @GetMapping("/mostratodos")
    public ResponseEntity<List<TipoContribucionDTO>> getAllTipoContribuciones() {

        List<TipoContribucionDTO> tipos = tipoContribucionServices.getAllTipoContribuciones().stream()
                .map(tipoContribucionServices::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tipos);
    }

    // ==========================
    // BUSCAR POR ID
    // ==========================
    @GetMapping("/buscar/{id}")
    public ResponseEntity<TipoContribucionDTO> getTipoContribucionById(
            @PathVariable Long id) {

        TipoContribucion tipo = tipoContribucionServices.getTipoContribucionById(id);
        TipoContribucionDTO tipoDTO = tipoContribucionServices.convertToDto(tipo);

        return ResponseEntity.ok(tipoDTO);
    }

    // ==========================
    // GUARDAR
    // ==========================
    @PostMapping("/guardartipocontribucion")
    public ResponseEntity<TipoContribucionDTO> createTipoContribucion(
            @Valid @RequestBody TipoContribucionDTO tipoContribucionDTO) {

        TipoContribucion tipo = tipoContribucionServices.createTipoContribucion(tipoContribucionDTO);
        return new ResponseEntity<>(
                tipoContribucionServices.convertToDto(tipo),
                HttpStatus.CREATED
        );
    }

    // ==========================
    // ACTUALIZAR
    // ==========================
    @PutMapping("/{id}")
    public ResponseEntity<TipoContribucionDTO> updateTipoContribucion(
            @PathVariable Long id,
            @Valid @RequestBody TipoContribucionDTO tipoContribucionDTO) {

        TipoContribucion tipo = tipoContribucionServices.updateTipoContribucion(id, tipoContribucionDTO);
        return ResponseEntity.ok(tipoContribucionServices.convertToDto(tipo));
    }

    // ==========================
    // ELIMINAR
    // ==========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoContribucion(@PathVariable Long id) {

        tipoContribucionServices.deleteTipoContribucion(id);
        return ResponseEntity.noContent().build();
    }

}
