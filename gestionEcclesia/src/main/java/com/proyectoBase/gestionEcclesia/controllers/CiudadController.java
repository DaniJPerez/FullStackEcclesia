package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.CiudadDTO;
import com.proyectoBase.gestionEcclesia.modele.Ciudad;
import com.proyectoBase.gestionEcclesia.services.CiudadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ciudades")
@RequiredArgsConstructor
public class CiudadController {

    private final CiudadService ciudadService;

    // ==========================
    // MOSTRAR TODOS
    // ==========================
    @GetMapping("/mostratodos")
    public ResponseEntity<List<CiudadDTO>> getAllCiudades() {

        List<CiudadDTO> ciudades = ciudadService.getAllCiudades().stream()
                .map(ciudadService::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ciudades);
    }

    // ==========================
    // BUSCAR POR ID
    // ==========================
    @GetMapping("/buscar/{id}")
    public ResponseEntity<CiudadDTO> getCiudadById(
            @PathVariable Long id) {

        Ciudad ciudad = ciudadService.findByid(id);
        return ResponseEntity.ok(ciudadService.convertToDTO(ciudad));
    }

    // ==========================
    // GUARDAR
    // ==========================
    @PostMapping("/guardarciudad")
    public ResponseEntity<CiudadDTO> createCiudad(
            @Valid @RequestBody CiudadDTO ciudadDTO) {

        Ciudad ciudad = ciudadService.save(ciudadDTO);

        return new ResponseEntity<>(
                ciudadService.convertToDTO(ciudad),
                HttpStatus.CREATED
        );
    }

    // ==========================
    // ACTUALIZAR
    // ==========================
    @PutMapping("/{id}")
    public ResponseEntity<CiudadDTO> updateCiudad(
            @PathVariable Long id,
            @Valid @RequestBody CiudadDTO ciudadDTO) {

        Ciudad ciudad = ciudadService.update(id, ciudadDTO);
        return ResponseEntity.ok(ciudadService.convertToDTO(ciudad));
    }

    // ==========================
    // ELIMINAR
    // ==========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCiudad(
            @PathVariable Long id) {

        ciudadService.dalete(id);
        return ResponseEntity.noContent().build();
    }
}
