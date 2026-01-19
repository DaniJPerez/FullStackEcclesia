package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.ContribucionDTO;
import com.proyectoBase.gestionEcclesia.DTOS.EventoDTO;
import com.proyectoBase.gestionEcclesia.modele.Contribucion;
import com.proyectoBase.gestionEcclesia.modele.Evento;
import com.proyectoBase.gestionEcclesia.services.AsistenciaEventoService;
import com.proyectoBase.gestionEcclesia.services.ContribucionService;
import com.proyectoBase.gestionEcclesia.services.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;
    private final AsistenciaEventoService asistenciaEventoService;
    private final ContribucionService contribucionService;

    @GetMapping
    public ResponseEntity<List<EventoDTO>> getAllEventos() {
        List<EventoDTO> eventos = eventoService.findAll().stream()
                .map(eventoService::convertToDTO)
                .map(eventoDTO -> {
                    asistenciaEventoService.agregarAsistenciaAEventoDto(eventoDTO);
                    return eventoDTO;
                })
                .map(eventoDTO -> {
                    contribucionService.agregarContribucionAEventoDto(eventoDTO);
                    return eventoDTO;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> getEventoById(@PathVariable Long id) {
        Evento evento = eventoService.findById(id);
        EventoDTO eventoDTO = eventoService.convertToDTO(evento);
        asistenciaEventoService.agregarAsistenciaAEventoDto(eventoDTO);

        List<Contribucion> contribuciones = contribucionService.findByEvento(id);

        List<ContribucionDTO> contribucionDTOS = contribuciones.stream()
                .map(contribucionService::convertToDTO)
                .toList();

        eventoDTO.setContribuciones(contribucionDTOS);

        return ResponseEntity.ok(eventoDTO);
    }

    @GetMapping("/futuros")
    public ResponseEntity<List<EventoDTO>> getEventosFuturos() {
        List<EventoDTO> eventos = eventoService.findEventosFuturos().stream()
                .map(eventoService::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/entre-fechas")
    public ResponseEntity<List<EventoDTO>> getEventosEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<EventoDTO> eventos = eventoService.findEventosEntreFechas(inicio, fin).stream()
                .map(eventoService::convertToDTO)
                .map(eventoDTO -> {
                    asistenciaEventoService.agregarAsistenciaAEventoDto(eventoDTO);
                    return eventoDTO;
                })
                .map(eventoDTO->{
                    contribucionService.agregarContribucionAEventoDto(eventoDTO);
                    return eventoDTO;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventos);
    }

    @PostMapping
    public ResponseEntity<EventoDTO> createEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        Evento evento = eventoService.save(eventoDTO);
        return new ResponseEntity<>(eventoService.convertToDTO(evento), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> updateEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        Evento evento = eventoService.update(eventoDTO);
        return ResponseEntity.ok(eventoService.convertToDTO(evento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        eventoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
