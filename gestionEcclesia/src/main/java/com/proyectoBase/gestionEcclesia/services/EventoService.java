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

        var id = eventoDTO!= null && eventoDTO.getId() != null && !eventoDTO.getId().toString().isEmpty() ? eventoDTO.getId() : (evento != null ? evento.getId() : null);
        if(id==null)
            System.out.println("El ID del evento es nulo al actualizar, se asignará uno nuevo al guardar");
        else
            evento.setId(id);

        var nombre = (eventoDTO!= null && eventoDTO.getNombre() != null && !eventoDTO.getNombre().isEmpty())
                ? eventoDTO.getNombre()
                : (evento != null && evento.getNombre() != null && !evento.getNombre().isEmpty())
                ? evento.getNombre() : null;
        if(nombre==null)
            throw new IllegalArgumentException("El nombre del evento no puede ser nulo al actualizar");
        else
            evento.setNombre(nombre);

        var fechaInicio = (eventoDTO!=null && eventoDTO.getFechaInicio() != null && !eventoDTO.getFechaInicio().toString().isEmpty()) ? eventoDTO.getFechaInicio()
                          : (evento != null && evento.getFechaInicio() != null ? evento.getFechaInicio() : null);
        if(fechaInicio==null)
            throw new IllegalArgumentException("La fecha de inicio del evento no puede ser nula al actualizar");
        else
            evento.setFechaInicio(eventoDTO.getFechaInicio());

        var fechaFin = (eventoDTO!=null && eventoDTO.getFechaFinalizacion() != null && !eventoDTO.getFechaFinalizacion().toString().isEmpty()
                        && !eventoDTO.getFechaFinalizacion().isBefore(eventoDTO.getFechaInicio()))
                        ? eventoDTO.getFechaFinalizacion()
                        : (evento != null && evento.getFechaFin() != null ? evento.getFechaFin() : null);
        if(fechaFin==null)
            throw new IllegalArgumentException("La fecha de finalización del evento no puede ser nula al actualizar");
        else
            evento.setFechaFin(eventoDTO.getFechaFinalizacion());

        var accesivilidad = parseAccesivilidad(eventoDTO.getAccesibilidad());
        if(accesivilidad==null)
            System.out.println("La accesibilidad del evento es nula al actualizar, se asignará una nueva al guardar");
        else
            evento.setAccesibilidadEvento(accesivilidad);
        if(Boolean.parseBoolean(eventoDTO.getEstadoEvento()))
        evento.setActivo(
                Boolean.parseBoolean(eventoDTO.getEstadoEvento()
                ));
        else
            System.out.println("El estado del evento es nulo al actualizar, se asignará uno nuevo al guardar");


        //se debe realizar el manejo de usuario de registro para la gestion de datos de auditoria
        //evento.setUsuarioRegirstro(usuarioServices.findById(eventoDTO.getIdUsuarioRegistro()));
        var descripcion = evento.getDescripcion() != null || eventoDTO.getDescripcion() != null ? eventoDTO.getDescripcion() : null;
        if(descripcion==null)
            System.out.println("La descripcion del evento es nula al actualizar, no se asignará una nueva al guardar");
        else
            evento.setDescripcion(eventoDTO.getDescripcion());

        evento.setDireccion(direccionService.updateDireccionFromDTO(
                evento.getDireccion(),eventoDTO.getDireccionDTO()
                ));

        return evento;
    }
//Terminar
    public EventoDTO convertToDTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        var id = evento.getId() != null ? evento.getId() : null;
        if(id==null)
            throw new IllegalArgumentException("El ID del evento es nulo al convertir a DTO");
        else
            dto.setId(id);

        var nombre = evento.getNombre() != null || evento.getNombre().isEmpty() ? evento.getNombre() : null;
        if(nombre==null)
            System.out.println("No hay un nombre del   E V E N T O   Expesificado  al convertir a DTO");
        else
            dto.setNombre(nombre);

        var descripcion = evento.getDescripcion() != null || evento.getDescripcion().isEmpty() ? evento.getDescripcion() : null;
        if(descripcion==null)
            System.out.println("!!! P R E C A U C I O N ¡¡¡¡  No hay una descripcion del evento Expesificado  al convertir a DTO");
        else
            dto.setDescripcion(descripcion);

        var fechaInicio = evento.getFechaInicio() != null ? evento.getFechaInicio() : null;
        if(fechaInicio==null)
            System.out.println("No hay una fecha de inicio del evento Expesificada  al convertir");
        else
            dto.setFechaInicio(fechaInicio);

        var fechaFin = evento.getFechaFin() != null ? evento.getFechaFin() : null;
        if(fechaFin==null)
            System.out.println("No hay una fecha de finalización del evento Expesificada  al convertir a DTO");
        else
            dto.setFechaFinalizacion(fechaFin);

        dto.setDireccionDTO(direccionService.converToDTO(evento.getDireccion()));

        var accesivilidad = evento.getAccesibilidadEvento() != null ? evento.getAccesibilidadEvento().name() : null;
        if(accesivilidad==null)
            System.out.println("No hay una accesibilidad del evento Expesificada  al convertir a DTO");
        else
            dto.setAccesibilidad(accesivilidad);

        var estadoEvento = !Boolean.toString(evento.isActivo()).isEmpty()? null : Boolean.toString(evento.isActivo());









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
