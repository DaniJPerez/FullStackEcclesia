package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.AsistenciaEventoDto;
import com.proyectoBase.gestionEcclesia.DTOS.ContribucionDTO;
import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.modele.Contribucion;
import com.proyectoBase.gestionEcclesia.modele.Persona;
import com.proyectoBase.gestionEcclesia.services.AsistenciaEventoService;
import com.proyectoBase.gestionEcclesia.services.ContribucionService;
import com.proyectoBase.gestionEcclesia.services.MiembroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/miembros")
@RequiredArgsConstructor
public class MiembroController {

    private final MiembroService miembroService;
    private final AsistenciaEventoService asistenciaEventoService;
    private final ContribucionService contribucionService;

    @GetMapping("/mostratodos")
    public ResponseEntity<List<MiembroDTO>> getAllMiembros() {
        List<MiembroDTO> miembros = miembroService.findAll().stream()
                .map(miembroService::convertToDTO)
                .map(miembroDTO -> {
                    asistenciaEventoService.agregarAsistenciaAMiembroDto(miembroDTO);
                    return miembroDTO;
                })
                .map(miembroDTO -> {
                    contribucionService.agregarContribucionAMiembroDto(miembroDTO);
                    return miembroDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(miembros);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<MiembroDTO> getMiembroById(@PathVariable Long id) {
        Persona persona = miembroService.findById(id);
        persona = asistenciaEventoService.agregarAsistenciaMiembro(persona);

        MiembroDTO miembroDTO = miembroService.convertToDTO(persona);
        asistenciaEventoService.agregarAsistenciaAMiembroDto(miembroDTO);

        List<Contribucion> contribuciones = contribucionService.findByMiembro(id);
        List<ContribucionDTO> contribucionesDto= contribuciones.stream()
                .map(contribucionService::convertToDTO)
                .toList();

        miembroDTO.setContribuciones(contribucionesDto);

        return ResponseEntity.ok(miembroDTO);
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<MiembroDTO>> getMiembrosByNombre(@RequestParam String nombre) {
        List<MiembroDTO> miembros = miembroService.findByPrimerNombre(nombre).stream()
                .map(miembroService::convertToDTO)
                .map(miembroDTO -> {
                    asistenciaEventoService.agregarAsistenciaAMiembroDto(miembroDTO);
                    return miembroDTO;
                })
                .map(miembroDTO -> {
                    contribucionService.agregarContribucionAMiembroDto(miembroDTO);
                    return miembroDTO;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(miembros);
    }

    @PostMapping("/guardarmiembro")
    public ResponseEntity<MiembroDTO> createMiembro(@Valid @RequestBody MiembroDTO miembroDTO) {
        Persona persona = miembroService.save(miembroDTO);
        return new ResponseEntity<>(miembroService.convertToDTO(persona), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiembroDTO> updateMiembro(@PathVariable Long id, @Valid @RequestBody MiembroDTO miembroDTO) {
        Persona persona = miembroService.update(id, miembroDTO);
        return ResponseEntity.ok(miembroService.convertToDTO(persona));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMiembro(@PathVariable Long id) {
        miembroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
