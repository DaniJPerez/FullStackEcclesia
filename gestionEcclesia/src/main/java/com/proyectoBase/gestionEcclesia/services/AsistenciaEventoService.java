package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.AsistenciaEventoDto;
import com.proyectoBase.gestionEcclesia.DTOS.EventoDTO;
import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.modele.AsistenciaEvento;
import com.proyectoBase.gestionEcclesia.modele.Evento;
import com.proyectoBase.gestionEcclesia.modele.Persona;
import com.proyectoBase.gestionEcclesia.repositories.AsistenciaEventoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaEventoService {

    private final AsistenciaEventoManagerService asistenciaEventoManagerService;
    private final AsistenciaEventoRepository asistenciaEventoRepository;

    public List<AsistenciaEvento> getAllAsistencias() {
        return asistenciaEventoRepository.findAll();
    }

    public AsistenciaEvento getAsistenciaById(Long id) {
        return asistenciaEventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada con id " + id));
    }

    public List<AsistenciaEvento> getAsistenciasByMiembroId(Long miembroId) {
        try{

        }catch (IllegalArgumentException ex){
            throw new EntityNotFoundException("Miembro no encontrado con id " + miembroId + ex.getMessage());
        }
        return asistenciaEventoRepository.findByPersona_NumeroIdentificacion(miembroId);
    }

    public List<AsistenciaEvento> getAsistenciasByEventoId(Long eventoId) {
        return asistenciaEventoRepository.findByEvento_Id(eventoId);
    }



    @Transactional
    public AsistenciaEvento save(AsistenciaEventoDto asistenciaEventoDto) {

        AsistenciaEvento asistenciaEvento = new AsistenciaEvento();
        asistenciaEvento = updateAsistenciaEventoFromDto(asistenciaEvento, asistenciaEventoDto);

        var asistenciaExistente = comprobarAsistenciaEvento(
                asistenciaEvento.getPersona(),
                asistenciaEvento.getEvento()
        );

        if (asistenciaExistente) {
            throw new IllegalArgumentException("La asistencia ya existe para este miembro y evento");
        }

        asistenciaEvento = asistenciaEventoManagerService.agregarAsistenciaMiembroYEvento(asistenciaEventoDto);

        return asistenciaEventoRepository.save(asistenciaEvento);
    }

    /*@Transactional
    public AsistenciaEvento saveAsistenciaEvento(AsistenciaEventoDto asistenciaEventoDto){
        AsistenciaEvento asistenciaEvento = new AsistenciaEvento();
        asistenciaEvento.setIdAsistente(asistenciaEventoDto.getIdAsistencia());
        asistenciaEvento.setMiembro(miembroService.updateMiembroFromDTO(asistenciaEvento.getMiembro(),asistenciaEventoDto.getMiembro()));
        asistenciaEvento.setEvento(eventoService.updateEventoFromDTO(asistenciaEvento.getEvento(),asistenciaEventoDto.getEvento()));
        asistenciaEvento.setFechaAsistencia(asistenciaEventoDto.getFechaAsistencia());
        return asistenciaEventoRepository.save(asistenciaEvento);
    }*/

    @Transactional
    public AsistenciaEvento update(Long id, AsistenciaEventoDto asistenciaEventoDto) {
        AsistenciaEvento asistenciaEvento = getAsistenciaById(id);

        //usa el intermediario para actualizar la asistencia dentro de las otras tablas relacionadas con asistenciaEvento
        asistenciaEventoManagerService.actualizarAsistenciaMiembro(asistenciaEvento);
    /// Por Acá quede con las actializaciones
        if(asistenciaEventoDto.getFechaAsistencia()!= null)
            asistenciaEvento.setFechaAsistencia(asistenciaEventoDto.getFechaAsistencia());

        if(asistenciaEventoDto.getEstadoAsistencia()!= null )
            asistenciaEvento.setAsistencia(Boolean.parseBoolean(asistenciaEventoDto
                    .getEstadoAsistencia()
                    .toUpperCase()));

        return asistenciaEvento;

    }

    @Transactional
    public void delete(Long id) {
        AsistenciaEvento asistenciaEvento = getAsistenciaById(id);
        var idmiembro = asistenciaEvento.getPersona().getNumeroIdentificacion();
        var idevento = asistenciaEvento.getEvento().getId();
        //usa el intermediario para eliminar la asistencia dentro de las otras tablas relacionadas
        asistenciaEventoManagerService.eliminarAsistenciaMiembro(idmiembro, idevento);

        asistenciaEventoRepository.delete(asistenciaEvento);
    }

    //AGREGAR EL PROCEDIMIENTO EN LA BASE DE DATOS PARA PODER INCLUIRLO
    /*@Transactional
    public List<AsistenciaEvento> buscarAsistenciaPorFecha(Long idmiembro, Date fecha1, Date fecha2){
        return asistenciaEventoRepository.findMiembroByAsistenciaEntreFechas(idmiembro, fecha1,fecha2);
    }*/
    //@Transactional
   //public List<AsistenciaEvento> buscarAsistenciaDeMiembroPorEvento(Long idMiembro, Long idEvento){
   //    return asistenciaEventoRepository.findBymiembroForIdEventoId(idMiembro,idEvento);
    //}

    @Transactional
    public List<AsistenciaEvento> buscarAsistenciaEvento(Long idEvento){
        return asistenciaEventoRepository.findByEvento_Id(idEvento);
    }

    @Transactional
    public void eliminarAsistencias(@PathVariable Long idMiembro, @PathVariable Long idEvento){
            AsistenciaEvento asistenciaEvento = new AsistenciaEvento();
            asistenciaEventoManagerService.agregarAsistenciasByMiembroAndEvento(asistenciaEvento);

            var existeAsistencia= this.comprobarAsistenciaEvento(
                    asistenciaEvento.getPersona(),
                    asistenciaEvento.getEvento()
            );

            if (!existeAsistencia) {
                asistenciaEventoRepository.delete(asistenciaEvento);
            } else {
                throw new EntityNotFoundException("Asistencia no encontrada para el miembro y evento proporcionados.");
            }
            //usa el intermediario para eliminar la asistencia dentro de las otras tablas relacionadas
            asistenciaEvento = asistenciaEventoManagerService.eliminarAsistenciaMiembro(idMiembro, idEvento);

            asistenciaEventoRepository.delete(asistenciaEvento);

    }

    public AsistenciaEvento updateAsistenciaEventoFromDto(AsistenciaEvento asistenciaEvento,AsistenciaEventoDto asistenciaEventoDto) {

        asistenciaEvento.setIdAsistenciaEvento(asistenciaEventoDto.getIdAsistencia());
        asistenciaEvento= asistenciaEventoManagerService
                        .convertirAEntidad(asistenciaEventoDto);

        asistenciaEvento.setFechaAsistencia(asistenciaEventoDto.getFechaAsistencia());

        return asistenciaEvento;
    }


    public AsistenciaEventoDto convertToDto(AsistenciaEvento asistenciaEvento) {
        AsistenciaEventoDto asistenciaEventoDto = new AsistenciaEventoDto();
        asistenciaEventoDto.setIdAsistencia(asistenciaEvento.getIdAsistenciaEvento());
        //Usar el manager para convertir las entidades relacionadas a AsistenciaEvento a dto
        asistenciaEventoManagerService.convertirADto(asistenciaEvento);

        asistenciaEventoDto.setFechaAsistencia(asistenciaEvento.getFechaAsistencia());
        return asistenciaEventoDto;
    }

    public Persona agregarAsistenciaMiembro(Persona persona){
        List<AsistenciaEvento> asistenciasEventos = asistenciaEventoRepository.findByPersona(persona);

        asistenciasEventos.stream().map(asistenciaEvento -> {
            persona.agregarAsistencia(asistenciaEvento);
            return asistenciaEvento;
        });

        return persona;

    }

    public Evento agregarAsistenciaAEvento(Evento evento){
        List<AsistenciaEvento> asistenciasEventos = asistenciaEventoRepository.findByEvento(evento);

        asistenciasEventos.stream().map(asistenciaEvento -> {
            evento.agregarAsistencia(asistenciaEvento);
            return asistenciaEvento;
        });

        return evento;

    }

    public MiembroDTO agregarAsistenciaAMiembroDto(MiembroDTO miembroDTO){

        Persona persona = asistenciaEventoManagerService.buscarPersona(miembroDTO.getId());

        List<AsistenciaEvento> asistenciasEventos = asistenciaEventoRepository.findByPersona(persona);

        List<AsistenciaEventoDto> asistenciasDTO = asistenciasEventos.stream()
                .map(asistenciaEvento -> {
                    AsistenciaEventoDto asistenciaEventoDto = new AsistenciaEventoDto();

                    asistenciaEventoDto = convertToDto(asistenciaEvento);

                    return asistenciaEventoDto;

                }
        ).toList();

        miembroDTO.setAsistenciaEvento(asistenciasDTO);

        return miembroDTO;

    }

    public EventoDTO agregarAsistenciaAEventoDto(EventoDTO eventoDTO){

        Evento evento = asistenciaEventoManagerService.buscarEvento(eventoDTO.getId());

        List<AsistenciaEvento> asistenciasEventos = asistenciaEventoRepository.findByEvento(evento);

        List<AsistenciaEventoDto> asistenciasDTO = asistenciasEventos.stream()
                .map(asistenciaEvento -> {
                    AsistenciaEventoDto asistenciaEventoDto = new AsistenciaEventoDto();

                    asistenciaEventoDto = convertToDto(asistenciaEvento);

                    return asistenciaEventoDto;

                }
        ).toList();

        eventoDTO.setAsistentesIds(asistenciasDTO);

        return eventoDTO;

    }


    public boolean comprobarAsistenciaEvento(Persona miembro, Evento evento){
        AsistenciaEvento comprovarAsistencia = asistenciaEventoRepository.findByPersonaAndEvento(miembro, evento);
        var asistenciaExistente = (comprovarAsistencia != null);
        return asistenciaExistente;

    }

}
