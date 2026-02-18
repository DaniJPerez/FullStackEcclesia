package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.IglesiaDTO;
import com.proyectoBase.gestionEcclesia.modele.*;
import com.proyectoBase.gestionEcclesia.repositories.IglesiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IglesiaServices {
    private final IglesiaRepository iglesiaRepository;
    private final EntidadTerritorialServices entidadTerritorialServices;
    private final DireccionService direccionService;
    private final MiembroService miembroService;
    private final RecursoEconomicoService recursoEconomicoService;
    private final ZonaAdministrativaServices zonaAdministrativaServices;


    public List<Iglesia> findAll(){
        return iglesiaRepository.findAll();
    }

    public Iglesia findById(Long id){
        return iglesiaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Iglesia no encontrada con ID: "+id));
    }

    public Iglesia saveIglesia(IglesiaDTO iglesiaDTO){

        Iglesia iglesia = new Iglesia();
        iglesia = updateIglesiaFromDTO(iglesia, iglesiaDTO);

        return iglesiaRepository.save(iglesia);
    }

    public Iglesia updateIglesia(Long id, IglesiaDTO iglesiaDTO){
        Iglesia iglesia = findById(id);
        iglesia = updateIglesiaFromDTO(iglesia, iglesiaDTO);

        return iglesia;
    }

    public void deleteIglesia(Long id){
        Iglesia iglesia = findById(id);
        iglesiaRepository.delete(iglesia);
    }

    public Iglesia updateIglesiaFromDTO(Iglesia iglesia, IglesiaDTO iglesiaDTO){

        if(iglesiaDTO.getIdIglesia() != null && iglesia!= null){

            var id= (iglesiaDTO.getIdIglesia() != null)
                    ? iglesiaDTO.getIdIglesia()
                    : iglesia.getIdIglesia();
            if(id==null)
                System.out.println("El ID de la iglesia es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                iglesia.setIdIglesia(id);

            var nombre = (iglesiaDTO.getNombreIglesia() != null)
                    ? iglesiaDTO.getNombreIglesia()
                    : iglesia.getNombreIglesia();
            if(nombre==null)
                throw new IllegalArgumentException("El nombre de la iglesia no puede ser nulo al actualizar");
            else
                iglesia.setNombreIglesia(nombre);

            if(Objects.nonNull(iglesiaDTO.getNumeroIglesia()))
                iglesia.setNumeroIglesiaZona(iglesiaDTO.getNumeroIglesia());
            else
                System.out.println("El numero de la iglesia es nulo al actualizar, se asignará uno nuevo al guardar");

            var telefono = (iglesiaDTO.getTelefonoIglesia()!= null && !iglesiaDTO.getTelefonoIglesia().isEmpty())
                    ? iglesiaDTO.getTelefonoIglesia()
                    : iglesia.getTelefonoContacto();
            if(telefono==null)
                System.out.println("El telefono de la iglesia es nulo al actualizar, No se asignará uno nuevo al guardar");
            else
                iglesia.setTelefonoContacto(iglesiaDTO.getTelefonoIglesia());

            iglesia.setPastor(miembroService.updateMiembroFromDTO(iglesia.getPastor(), iglesiaDTO.getPastorIglesia()));

            iglesia.setDireccion(direccionService.updateDireccionFromDTO(iglesia.getDireccion(), iglesiaDTO.getDireccion()));

            ZonaAdministrativa zona = zonaAdministrativaServices.updateZonaAdministrativaFromDto(
                    iglesia.getZonaAdministrativa(),
                    iglesiaDTO.getZonaAbministrativa()
            );

            if(zona!=null)
                iglesia.setZonaAdministrativa(zona);
            else
                System.out.println("La zona administrativa de la iglesia es nula al actualizar la info de Iglesia, No se asignará una nueva zona administrativa al guardar");

            List<Persona> miembros = iglesiaDTO.getMiembros().stream()
                    .map(personaDto->{
                        return miembroService.updateMiembroFromDTO(new Persona(), personaDto);
                    }).toList();

            iglesia.setMiembros(miembros);

            List<RecursoEconomico> recursos = iglesiaDTO.getRecursosEconomicos().stream()
                    .map(recursoDto->{
                        return recursoEconomicoService.updateRecursoEconomicoFromDTO(new RecursoEconomico(), recursoDto);
                    }).toList();

            recursos.forEach(recursoEconomico -> {
                iglesia.getRecursos().add(recursoEconomico);
            });

            return iglesia;
        }else
            throw new IllegalArgumentException("El ID de la iglesia es nulo al actualizar, no se puede actualizar una iglesia sin ID");

    }

    public IglesiaDTO convertToDTO(Iglesia iglesia){
        if(iglesia!=null){

            IglesiaDTO iglesiaDTO = new IglesiaDTO();

            var id = iglesia.getIdIglesia() != null ? iglesia.getIdIglesia() : null;
            if(id==null)
                throw new IllegalArgumentException("El ID de la iglesia es nulo al convertir a DTO");
            else
                 iglesiaDTO.setIdIglesia(id);

            var nombre = iglesia.getNombreIglesia() != null ? iglesia.getNombreIglesia() : null;
            if(nombre==null)
                System.out.println("El nombre de la iglesia es nulo al convertir a DTO, se asignará un valor por defecto");
            else
                iglesiaDTO.setNombreIglesia(nombre);


            if(Objects.isNull(iglesia.getNumeroIglesiaZona()))
                System.out.println("El número de la iglesia es nulo al convertir a DTO, se asignará un valor por defecto");
            else
                iglesiaDTO.setNumeroIglesia(iglesia.getNumeroIglesiaZona());

            var telefono = iglesia.getTelefonoContacto() != null ? iglesia.getTelefonoContacto() : null;
            if(telefono!=null)
                iglesiaDTO.setTelefonoIglesia(telefono);
            else
                iglesiaDTO.setPastorIglesia(miembroService.convertToDTO(iglesia.getPastor()));

            iglesiaDTO.setDireccion(direccionService.converToDTO(iglesia.getDireccion()));

            iglesiaDTO.setZonaAbministrativa(zonaAdministrativaServices.convertToDto(iglesia.getZonaAdministrativa()));

            List<Persona> miembros = iglesia.getMiembros();
            List<Recurso> recursos = iglesia.getRecursos();

            iglesiaDTO.setMiembros(
                    miembros.stream()
                            .map(persona -> miembroService.convertToDTO(persona))
                            .toList()
            );

            iglesiaDTO.setRecursosEconomicos(
                    recursos.stream()
                            .map(recurso -> recursoEconomicoService.convertToDTO((RecursoEconomico) recurso))
                            .toList()
            );

            return iglesiaDTO;
        }else{
            throw new IllegalArgumentException("¡¡¡ E R R O R !!! \n No se pudo convertir a DTO, la instancia de iglesia es nula");
        }

    }
}
