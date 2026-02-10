package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.modele.EntidadTerritorial;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.proyectoBase.gestionEcclesia.DTOS.CiudadDTO;
import com.proyectoBase.gestionEcclesia.modele.Ciudad;
import com.proyectoBase.gestionEcclesia.repositories.CiudadRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CiudadService {
    private final CiudadRepository ciudadRepository;
    private final EntidadTerritorialServices entidadTerritorialServices;

    public List<Ciudad> getAllCiudades(){
        return ciudadRepository.findAll();
    }

    public Ciudad findByid(Long id){
        return ciudadRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("ciudad no encontrada"));
    }

    @Transactional
    public Ciudad save(CiudadDTO ciudadDTO){
        Ciudad ciudad = new Ciudad();
        /*ciudad=*/ updateCiudadFromDTO(ciudad, ciudadDTO);
        return ciudadRepository.save(ciudad);
    }

    @Transactional
    public Ciudad update(Long id, CiudadDTO ciudadDTO){
        Ciudad ciudad = findByid(id);
        /*ciudad =*/ updateCiudadFromDTO(ciudad, ciudadDTO);
        return ciudad;
    }

    @Transactional
    public  void dalete(Long id){
        Ciudad ciudad = findByid(id);
        ciudadRepository.delete(ciudad);
    }

    public Ciudad updateCiudadFromDTO(Ciudad ciudad, CiudadDTO ciudadDTO){
        var id = ciudadDTO != null || ciudadDTO.getIdCiudad() != null ? ciudadDTO.getIdCiudad() : null;
        ciudad.setIdCiudad(id);
        if(id==null)
            System.out.println("El ID de la ciudad es nulo al actualizar, se asignará uno nuevo al guardar");


        var nombre = ciudadDTO != null || ciudadDTO.getNombreCiudad() != null ? ciudadDTO.getNombreCiudad() : null;
        ciudad.setNombreCiudad(nombre);
        if(nombre==null)
            throw new IllegalArgumentException("El nombre de la ciudad no puede ser nulo al actualizar");

        var entidadTerritorial = ciudad.getEntidadTerritorial()!=null || ciudadDTO.getEntidadTerritorialDto()!= null ?
                entidadTerritorialServices.updateFromDto(
                        ciudad.getEntidadTerritorial(),
                        ciudadDTO.getEntidadTerritorialDto()
                ) : null;

        if(entidadTerritorial==null)
            throw new IllegalArgumentException("La entidad territorial no puede ser nula al actualizar la ciudad");

        ciudad.setEntidadTerritorial(entidadTerritorial);

        return ciudad;
    }

    public CiudadDTO convertToDTO(Ciudad ciudad){
        CiudadDTO ciudadDTO = new CiudadDTO();

        var id = ciudad.getIdCiudad() != null ? ciudad.getIdCiudad() : null;
        ciudadDTO.setIdCiudad(id);
        if(id==null)
            System.out.println("El ID de la ciudad es nulo al convertir a DTO");


        var nombre = ciudad.getNombreCiudad() != null ? ciudad.getNombreCiudad() : null;
        ciudadDTO.setNombreCiudad(nombre);
        if(nombre==null)
            throw new IllegalArgumentException("El nombre de la ciudad no puede ser nulo al convertir");


        var entidadTerritorialDto = ciudad.getEntidadTerritorial() != null ?
                entidadTerritorialServices.convertToDto(ciudad.getEntidadTerritorial()) : null;
        if(entidadTerritorialDto==null)
            throw new IllegalArgumentException("La entidad territorial no puede ser nula al convertir la ciudad a DTO");
        ciudadDTO.setEntidadTerritorialDto(entidadTerritorialDto);


        return ciudadDTO;
    }


}
