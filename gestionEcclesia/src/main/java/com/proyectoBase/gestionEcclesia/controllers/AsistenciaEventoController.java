package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.AsistenciaEventoDto;
import com.proyectoBase.gestionEcclesia.modele.AsistenciaEvento;
import com.proyectoBase.gestionEcclesia.services.AsistenciaEventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/asistencias")
@RequiredArgsConstructor
public class AsistenciaEventoController {
    private final AsistenciaEventoService asistenciaEventoService;

    @GetMapping("/mostrartodas")
    public ResponseEntity<List<AsistenciaEventoDto>> getAllAsistencias(){
        List<AsistenciaEventoDto> asistencias= asistenciaEventoService.getAllAsistencias().stream()
                .map(asistenciaEventoService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(asistencias);
    }

    @GetMapping("/buscarporid/{id}")
    public ResponseEntity<AsistenciaEventoDto> getAsistenciaEventoById(@PathVariable Long id) {
        AsistenciaEvento asistenciaEvento = asistenciaEventoService.getAsistenciaById(id);
        return ResponseEntity.ok(asistenciaEventoService.convertToDto(asistenciaEvento));
    }

    @PostMapping("/agregarasistencia")
    public ResponseEntity<AsistenciaEventoDto> agregarAsistencia(@Valid @RequestBody AsistenciaEventoDto asistenciaEventoDto) {
        AsistenciaEvento asistenciaEvento = asistenciaEventoService.save(asistenciaEventoDto);
        return new ResponseEntity<>(asistenciaEventoService.convertToDto(asistenciaEvento), HttpStatus.CREATED);
    }

    @PutMapping("/modificarasistencia/{id}")
    public ResponseEntity<AsistenciaEventoDto> updateAsistencia(@PathVariable Long id, @Valid @RequestBody AsistenciaEventoDto asistenciaEventoDto) {
        AsistenciaEvento asistenciaEvento = asistenciaEventoService.update(id, asistenciaEventoDto);
        return ResponseEntity.ok(asistenciaEventoService.convertToDto(asistenciaEvento));
    }

    @DeleteMapping("/eliminarasistencias")
    public ResponseEntity<Void> eliminarAsistencia(@PathVariable Long idEvento, @PathVariable Long idMiembro){
            asistenciaEventoService.eliminarAsistencias(idEvento,idEvento);
            return ResponseEntity.noContent().build();
    }



}
