package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.TipoEventoDTO;
import com.proyectoBase.gestionEcclesia.modele.TipoEvento;
import com.proyectoBase.gestionEcclesia.services.TipoEventoServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipoeventos")
@RequiredArgsConstructor
public class TipoEventoController {

    private final TipoEventoServices tipoEventoServices;

    // ==========================
    // MOSTRAR TODOS
    // ==========================
    @GetMapping("/mostratodos")
    public ResponseEntity<List<TipoEventoDTO>> getAllTipoEventos() {

        List<TipoEventoDTO> tipos = tipoEventoServices.findAll().stream()
                .map(tipoEventoServices::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tipos);
    }

    // ==========================
    // BUSCAR POR ID
    // ==========================
    @GetMapping("/buscar/{id}")
    public ResponseEntity<TipoEventoDTO> getTipoEventoById(
            @PathVariable Long id) {

        TipoEvento tipoEvento = tipoEventoServices.findById(id);
        TipoEventoDTO dto = tipoEventoServices.convertToDto(tipoEvento);

        return ResponseEntity.ok(dto);
    }

    // ==========================
    // GUARDAR
    // ==========================
    @PostMapping("/guardartipoevento")
    public ResponseEntity<TipoEventoDTO> createTipoEvento(
            @Valid @RequestBody TipoEventoDTO tipoEventoDTO) {

        TipoEvento tipoEvento = tipoEventoServices.create(tipoEventoDTO);

        return new ResponseEntity<>(
                tipoEventoServices.convertToDto(tipoEvento),
                HttpStatus.CREATED
        );
    }

    // ==========================
    // ACTUALIZAR
    // ==========================
    @PutMapping("/{id}")
    public ResponseEntity<TipoEventoDTO> updateTipoEvento(
            @PathVariable Long id,
            @Valid @RequestBody TipoEventoDTO tipoEventoDTO) {

        TipoEvento tipoEvento = tipoEventoServices.update(id, tipoEventoDTO);
        return ResponseEntity.ok(tipoEventoServices.convertToDto(tipoEvento));
    }

    // ==========================
    // ELIMINAR
    // ==========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoEvento(@PathVariable Long id) {
        tipoEventoServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
