package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.AsistenciaEventoDto;
import com.proyectoBase.gestionEcclesia.DTOS.ContribucionDTO;
import com.proyectoBase.gestionEcclesia.DTOS.EventoDTO;
import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.modele.*;
import com.proyectoBase.gestionEcclesia.repositories.AsistenciaEventoRepository;
import com.proyectoBase.gestionEcclesia.repositories.EventoRepository;
import com.proyectoBase.gestionEcclesia.repositories.MiembroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//nombrar la clase para que sea expecifico a una clase hija. o en todo caso implementar una clase generica que maneje ambos datos
public class EventoService {

    private final EventoRepository eventoRepository;
    private final DireccionService direccionService;

    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    public Evento findById(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));
    }

    public List<Evento> findEventosFuturos() {
        return eventoRepository.findByFechaInicioAfter(LocalDateTime.now());
    }

    public List<Evento> findEventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        return eventoRepository.findByFechaInicioBetween(inicio, fin);
    }

    @Transactional
    public Evento save(EventoDTO eventoDTO) {
        Evento evento = new Evento();
        evento =updateEventoFromDTO(evento, eventoDTO);
        return eventoRepository.save(evento);
    }

    @Transactional
    public Evento update(EventoDTO eventoDTO) {
        Evento evento = findById(eventoDTO.getId());
        updateEventoFromDTO(evento, eventoDTO);
        return evento;
    }

    @Transactional
    public void delete(Long id) {
        Evento evento = findById(id);
        eventoRepository.delete(evento);
    }

    public Evento updateEventoFromDTO(Evento evento, EventoDTO eventoDTO) {
        evento.setNombre(eventoDTO.getNombre());
        evento.setFechaInicio(eventoDTO.getFechaInicio());
        evento.setFechaFin(eventoDTO.getFechaFinalizacion());

        evento.setAccesibilidadEvento(parseAccesivilidad(
                eventoDTO.getAccesibilidad()
                ));

        evento.setActivo(
                Boolean.parseBoolean(eventoDTO.getEstadoEvento()
                ));

        //se debe realizar el manejo de usuario de registro para la gestion de datos de auditoria
        //evento.setUsuarioRegirstro(usuarioServices.findById(eventoDTO.getIdUsuarioRegistro()));

        evento.setDescripcion(eventoDTO.getDescripcion());
        evento.setFechaInicio(eventoDTO.getFechaInicio());
        evento.setFechaFin(eventoDTO.getFechaFinalizacion());

        evento.setDireccion(direccionService.updateDireccionFromDTO(
                evento.getDireccion(),eventoDTO.getDireccionDTO()
                ));

        return evento;
    }
//Terminar
    public EventoDTO convertToDTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setId(evento.getId());
        dto.setNombre(evento.getNombre());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFechaInicio(evento.getFechaInicio());
        dto.setFechaFinalizacion(evento.getFechaFin());
        dto.setDireccionDTO(direccionService.converToDTO(evento.getDireccion()));
        dto.setAccesibilidad(evento.getAccesibilidadEvento() != null ? evento.getAccesibilidadEvento().name() : null);

        return dto;
    }

    private TipoAccesibilidadEvento parseAccesivilidad(String value) {
        if (value == null) return null;
        try {
            return TipoAccesibilidadEvento.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor inválido para accesibilidad: " + value, e);
        }
    }
}
