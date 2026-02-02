package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.PaisDto;
import com.proyectoBase.gestionEcclesia.modele.Pais;
import com.proyectoBase.gestionEcclesia.services.PaisServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/paises")
@RequiredArgsConstructor
public class PaisController {

    private final PaisServices paisService;


    @GetMapping("/mostratodos")
    public ResponseEntity<List<PaisDto>> getAllPaises() {

        List<PaisDto> paises = paisService.getAllPaises().stream()
                .map(paisService::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(paises);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<PaisDto> getPaisById(@PathVariable Long id) {

        Pais pais = paisService.findByIdPais(id);
        PaisDto paisDTO = paisService.convertToDto(pais);

        return ResponseEntity.ok(paisDTO);
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<PaisDto> getPaisesByNombre(@RequestParam String nombre) {

        Pais paises = paisService.findByNombrePais(nombre);

        return ResponseEntity.ok(paisService.convertToDto(paises));
    }

    @PostMapping("/guardarpais")
    public ResponseEntity<PaisDto> createPais(
            @Valid @RequestBody PaisDto paisDTO) {

        Pais pais = paisService.savePais(paisDTO);
        return new ResponseEntity<>(paisService.convertToDto(pais), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PaisDto> updatePais(
            @PathVariable Long id,
            @Valid @RequestBody PaisDto paisDTO) {

        Pais pais = paisService.updatePais(id, paisDTO);
        return ResponseEntity.ok(paisService.convertToDto(pais));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePais(@PathVariable Long id) {
        paisService.deletePais(id);
        return ResponseEntity.noContent().build();
    }
}

