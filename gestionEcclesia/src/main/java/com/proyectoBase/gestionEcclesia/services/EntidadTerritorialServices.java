package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.EntidadTerritorialDto;
import com.proyectoBase.gestionEcclesia.modele.EntidadTerritorial;
import com.proyectoBase.gestionEcclesia.repositories.EntidadTerritorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntidadTerritorialServices {
    private final EntidadTerritorialRepository entidadTerritorialRepository;
    private final PaisServices paisServices;

    public List<EntidadTerritorial> getAllEntidadTerritorials(){
        return entidadTerritorialRepository.findAll();
    }

    public EntidadTerritorial findByIdEntidadTerritorial(Long idEntidadTerritorial){
        return entidadTerritorialRepository.findById(idEntidadTerritorial).orElseThrow(()-> new IllegalArgumentException("Entidad Territorial no encontrada con ID: " + idEntidadTerritorial));
    }

    public EntidadTerritorial save(EntidadTerritorialDto entidadTerritorialDTo){
        EntidadTerritorial entidadTerritorial = new EntidadTerritorial();
        entidadTerritorial = updateFromDto( entidadTerritorial, entidadTerritorialDTo);

        return entidadTerritorialRepository.save(entidadTerritorial);
    }

    public EntidadTerritorial update(Long id, EntidadTerritorialDto entidadTerritorialDTo){
        EntidadTerritorial entidadTerritorial = findByIdEntidadTerritorial(id);
        entidadTerritorial = updateFromDto(entidadTerritorial, entidadTerritorialDTo);

        return entidadTerritorial;
    }

    public void delete(Long idEntidadTerritorial){
        EntidadTerritorial entidadTerritorial = findByIdEntidadTerritorial(idEntidadTerritorial);
        entidadTerritorialRepository.delete(entidadTerritorial);
    }

    public EntidadTerritorial updateFromDto( EntidadTerritorial entidadTerritorial, EntidadTerritorialDto entidadTerritorialDto){
        var id = (entidadTerritorialDto != null && entidadTerritorialDto.getIdEntidad() != null)
                ? entidadTerritorialDto.getIdEntidad()
                : (entidadTerritorial != null ? entidadTerritorial.getIdEntidadTerritorial() : null);
        if(id==null)
            System.out.println("El ID de la entidad territorial es nulo al actualizar, se asignará uno nuevo al guardar");
        else
            entidadTerritorial.setIdEntidadTerritorial(entidadTerritorialDto.getIdEntidad());

        var nombre = (entidadTerritorialDto!= null && entidadTerritorialDto.getNombreEntidad() != null && !entidadTerritorialDto.getNombreEntidad().isEmpty())
                ? entidadTerritorialDto.getNombreEntidad()
                : (entidadTerritorial != null ? entidadTerritorial.getNombreEntidadTerritorial() : null);
        if(nombre==null)
            throw new IllegalArgumentException("El nombre de la entidad territorial no puede ser nulo al actualizar");
        else
            entidadTerritorial.setNombreEntidadTerritorial(nombre);

        entidadTerritorial.setPais(paisServices.findByIdPais(entidadTerritorialDto.getPais().getIdPais()));

        return entidadTerritorial;
    }

    public EntidadTerritorialDto convertToDto(EntidadTerritorial entidadTerritorial){
        EntidadTerritorialDto entidadTerritorialDto = new EntidadTerritorialDto();

        var id = entidadTerritorial != null || entidadTerritorial.getIdEntidadTerritorial() != null ? entidadTerritorial.getIdEntidadTerritorial() : null;
        if(id==null)
            throw new IllegalArgumentException("El ID de la entidad territorial es nulo al convertir a DTO");
        else
            entidadTerritorialDto.setIdEntidad(id);

        var nombre = entidadTerritorial != null || entidadTerritorial.getNombreEntidadTerritorial() != null ? entidadTerritorial.getNombreEntidadTerritorial() : null;
        if(nombre==null)
            throw new IllegalArgumentException("El nombre de la entidad territorial es nulo al convertir a DTO");
        else
            entidadTerritorialDto.setNombreEntidad(entidadTerritorial.getNombreEntidadTerritorial());

        entidadTerritorialDto.setPais(paisServices.convertToDto(entidadTerritorial.getPais()));

        return entidadTerritorialDto;
    }
}
