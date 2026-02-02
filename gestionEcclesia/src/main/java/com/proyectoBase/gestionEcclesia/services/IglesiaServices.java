package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.IglesiaDTO;
import com.proyectoBase.gestionEcclesia.modele.*;
import com.proyectoBase.gestionEcclesia.repositories.IglesiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        iglesia.setIdIglesia(iglesia.getIdIglesia());
        iglesia.setNombreIglesia(iglesiaDTO.getNombreIglesia());
        iglesia.setNumeroIglesiaZona(iglesiaDTO.getNumeroIglesia());
        iglesia.setTelefonoContacto(iglesiaDTO.getTelefonoIglesia());
        iglesia.setPastor(miembroService.updateMiembroFromDTO(iglesia.getPastor(), iglesiaDTO.getPastorIglesia()));
        iglesia.setDireccion(direccionService.updateDireccionFromDTO(iglesia.getDireccion(), iglesiaDTO.getDireccion()));
        ZonaAdministrativa zona = zonaAdministrativaServices.updateZonaAdministrativaFromDto(
                iglesia.getZonaAdministrativa(),
                iglesiaDTO.getZonaAbministrativa()
        );

        iglesia.setZonaAdministrativa(zona);

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

    }

    public IglesiaDTO convertToDTO(Iglesia iglesia){

        IglesiaDTO iglesiaDTO = new IglesiaDTO();
        iglesiaDTO.setIdIglesia(iglesia.getIdIglesia());
        iglesiaDTO.setNombreIglesia(iglesia.getNombreIglesia());
        iglesiaDTO.setNumeroIglesia(iglesia.getNumeroIglesiaZona());
        iglesiaDTO.setTelefonoIglesia(iglesia.getTelefonoContacto());
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

    }
}
