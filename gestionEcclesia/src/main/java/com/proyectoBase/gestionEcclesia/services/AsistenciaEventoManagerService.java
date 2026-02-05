package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.AsistenciaEventoDto;
import com.proyectoBase.gestionEcclesia.DTOS.EventoDTO;
import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.modele.AsistenciaEvento;
import com.proyectoBase.gestionEcclesia.modele.Evento;
import com.proyectoBase.gestionEcclesia.modele.Persona;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

//Esta clase sirve como gestionador como intermediario entre el servicio de asistencia y los demas servicios de eventos y miembros
public class AsistenciaEventoManagerService{
    private final MiembroService miembroService;
    private final EventoService eventoService;

    @Transactional
    public AsistenciaEvento agregarAsistenciaMiembroYEvento(AsistenciaEventoDto asistenciaEventoDto){
        AsistenciaEvento asistenciaEvento = new AsistenciaEvento();

        var miembro = miembroService.findById(asistenciaEventoDto.getMiembro().getId());
        var evento = eventoService.findById(asistenciaEventoDto.getEvento().getId());

        if(miembro!=null && evento!=null){
            miembro.agregarAsistencia(asistenciaEvento);
            evento.agregarAsistencia(asistenciaEvento);
        }
        return asistenciaEvento;

    }

    @Transactional
    public Persona buscarPersona(Long id){
        return miembroService.findById(id);
    }

    @Transactional
    public Evento buscarEvento(Long id){
        return eventoService.findById(id);
    }

    @Transactional
    public AsistenciaEvento agregarAsistenciasByMiembroAndEvento(AsistenciaEvento asistenciaEvento){

        var miembro = miembroService.findById(asistenciaEvento.getPersona().getNumeroIdentificacion());
        var evento = eventoService.findById(asistenciaEvento.getEvento().getId());

        if(miembro!=null && evento!=null){
            miembro.agregarAsistencia(asistenciaEvento);
            evento.agregarAsistencia(asistenciaEvento);
        }
        return asistenciaEvento;
    }

    @Transactional
    public AsistenciaEvento agregarAsistenciasByMiembroAndEvento( Long idMiembro, Long idEvento){
        AsistenciaEvento asistenciaEvento = new AsistenciaEvento();

        var miembro = miembroService.findById(idMiembro);
        var evento = eventoService.findById(idEvento);

        if(miembro!=null && evento!=null){
            miembro.agregarAsistencia(asistenciaEvento);
            evento.agregarAsistencia(asistenciaEvento);
        }
        
        return asistenciaEvento;
    }

    @Transactional
    public AsistenciaEvento eliminarAsistenciaMiembro(Long idMiembro, Long idEvento){
        AsistenciaEvento asistenciaEvento = new AsistenciaEvento();

        var miembro = miembroService.findById(idMiembro);
        var evento = eventoService.findById(idEvento);

        if(miembro!=null && evento!=null){
            miembro.agregarAsistencia(asistenciaEvento);
            evento.agregarAsistencia(asistenciaEvento);
        }

        return asistenciaEvento;

    }

    @Transactional
    public AsistenciaEvento actualizarAsistenciaMiembro(AsistenciaEvento asistenciaEvento){

        var miembro = miembroService.findById(asistenciaEvento.getPersona().getNumeroIdentificacion());
        var evento = eventoService.findById(asistenciaEvento.getEvento().getId());

        if(miembro!=null && evento!=null){
            miembro.agregarAsistencia(asistenciaEvento);
            evento.agregarAsistencia(asistenciaEvento);
        }

        return asistenciaEvento;
    }

    @Transactional
    public AsistenciaEventoDto convertirADto(AsistenciaEvento asistenciaEvento){
        AsistenciaEventoDto asistenciaEventoDto = new AsistenciaEventoDto();

        var miembroDto = miembroService.convertToDTO(asistenciaEvento.getPersona());

        var eventoDto = eventoService.convertToDTO(asistenciaEvento.getEvento());



        asistenciaEventoDto.setMiembro(miembroDto);
        asistenciaEventoDto.setEvento(eventoDto);

        return asistenciaEventoDto;
    }

    @Transactional
    public AsistenciaEvento convertirAEntidad(AsistenciaEventoDto asistenciaEventoDto){
        AsistenciaEvento asistenciaEvento = new AsistenciaEvento();

        var miembro = miembroService.findById(asistenciaEventoDto.getMiembro().getId());

        var evento = eventoService.findById(asistenciaEventoDto.getEvento().getId());

        if(miembro!=null && evento!=null){
            miembro.agregarAsistencia(asistenciaEvento);
            evento.agregarAsistencia(asistenciaEvento);
        }

        return asistenciaEvento;
    }

    @Transactional
    public Persona combertirMiembroAEntidad(MiembroDTO miembroDTO){
        Persona persona = miembroService.findById(miembroDTO.getId());
        return persona;
    }

    @Transactional
    public Evento combertirMiembroAEntidad(EventoDTO eventoDTO){
        Evento evento = eventoService.findById(eventoDTO.getId());
        return evento;
    }

}
