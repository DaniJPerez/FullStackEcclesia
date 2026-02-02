package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.GastoDTO;
import com.proyectoBase.gestionEcclesia.modele.Gasto;
import com.proyectoBase.gestionEcclesia.services.GastoServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoServices gastoServices;

    // ==========================
    // MOSTRAR TODOS
    // ==========================
    @GetMapping("/mostratodos")
    public ResponseEntity<List<GastoDTO>> getAllGastos() {

        List<GastoDTO> gastos = gastoServices.findAll().stream()
                .map(gastoServices::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(gastos);
    }

    // ==========================
    // BUSCAR POR ID
    // ==========================
    @GetMapping("/buscar/{id}")
    public ResponseEntity<GastoDTO> getGastoById(
            @PathVariable Long id) {

        Gasto gasto = gastoServices.findById(id);
        GastoDTO dto = gastoServices.convertToDto(gasto);

        return ResponseEntity.ok(dto);
    }

    // ==========================
    // GUARDAR
    // ==========================
    @PostMapping("/guardargasto")
    public ResponseEntity<GastoDTO> createGasto(
            @Valid @RequestBody GastoDTO gastoDTO) {

        Gasto gasto = gastoServices.saveGasto(gastoDTO);

        return new ResponseEntity<>(
                gastoServices.convertToDto(gasto),
                HttpStatus.CREATED
        );
    }

    // ==========================
    // ACTUALIZAR
    // ==========================
    @PutMapping("/{id}")
    public ResponseEntity<GastoDTO> updateGasto(
            @PathVariable Long id,
            @Valid @RequestBody GastoDTO gastoDTO) {

        Gasto gasto = gastoServices.updateGasto(id, gastoDTO);
        return ResponseEntity.ok(gastoServices.convertToDto(gasto));
    }

    // ==========================
    // ELIMINAR
    // ==========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGasto(@PathVariable Long id) {

        gastoServices.deleteGasto(id);
        return ResponseEntity.noContent().build();
    }
}
