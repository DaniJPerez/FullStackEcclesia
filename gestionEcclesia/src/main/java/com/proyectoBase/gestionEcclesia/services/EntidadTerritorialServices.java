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
        entidadTerritorial = updateFromDto(entidadTerritorialDTo, entidadTerritorial);

        return entidadTerritorialRepository.save(entidadTerritorial);
    }

    public EntidadTerritorial update(Long id, EntidadTerritorialDto entidadTerritorialDTo){
        EntidadTerritorial entidadTerritorial = findByIdEntidadTerritorial(id);
        entidadTerritorial = updateFromDto(entidadTerritorialDTo, entidadTerritorial);

        return entidadTerritorial;
    }

    public void delete(Long idEntidadTerritorial){
        EntidadTerritorial entidadTerritorial = findByIdEntidadTerritorial(idEntidadTerritorial);
        entidadTerritorialRepository.delete(entidadTerritorial);
    }

    public EntidadTerritorial updateFromDto(EntidadTerritorialDto entidadTerritorialDto, EntidadTerritorial entidadTerritorial){
        entidadTerritorial.setIdEntidadTerritorial(entidadTerritorialDto.getIdEntidad());
        entidadTerritorial.setNombreEntidadTerritorial(entidadTerritorialDto.getNombreEntidad());
        entidadTerritorial.setPais(paisServices.findByIdPais(entidadTerritorialDto.getPais().getIdPais()));

        return entidadTerritorial;
    }

    public EntidadTerritorialDto convertToDto(EntidadTerritorial entidadTerritorial){
        EntidadTerritorialDto entidadTerritorialDto = new EntidadTerritorialDto();
        entidadTerritorialDto.setIdEntidad(entidadTerritorial.getIdEntidadTerritorial());
        entidadTerritorialDto.setNombreEntidad(entidadTerritorial.getNombreEntidadTerritorial());
        entidadTerritorialDto.setPais(paisServices.convertToDto(entidadTerritorial.getPais()));
        return entidadTerritorialDto;
    }
}
