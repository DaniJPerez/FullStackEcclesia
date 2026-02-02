package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.IglesiaDTO;
import com.proyectoBase.gestionEcclesia.modele.Iglesia;
import com.proyectoBase.gestionEcclesia.services.IglesiaServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/iglesias")
@RequiredArgsConstructor
public class IglesiaController {

    private final IglesiaServices iglesiaService;

    @GetMapping("/mostraTodos")
    public ResponseEntity<List<IglesiaDTO>> getAllIglesias() {
        List<IglesiaDTO> iglesias = iglesiaService.findAll().stream()
                .map(iglesiaService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(iglesias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IglesiaDTO> getIglesiaById(@PathVariable Long id) {
        Iglesia iglesia = iglesiaService.findById(id);
        return ResponseEntity.ok(iglesiaService.convertToDTO(iglesia));
    }

    @PostMapping("/crear")
    public ResponseEntity<IglesiaDTO> createIglesia(@Valid @RequestBody IglesiaDTO iglesiaDTO) {
        Iglesia iglesia = iglesiaService.saveIglesia(iglesiaDTO);
        return new ResponseEntity<>(iglesiaService.convertToDTO(iglesia), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IglesiaDTO> updateIglesia(@PathVariable Long id, @Valid @RequestBody IglesiaDTO iglesiaDTO) {
        Iglesia iglesia = iglesiaService.updateIglesia(id, iglesiaDTO);
        return ResponseEntity.ok(iglesiaService.convertToDTO(iglesia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIglesia(@PathVariable Long id) {
        iglesiaService.deleteIglesia(id);
        return ResponseEntity.noContent().build();
    }
}

