package com.proyectoBase.gestionEcclesia.services;

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
        updateCiudadFromDTO(ciudad, ciudadDTO);
        return ciudadRepository.save(ciudad);
    }

    @Transactional
    public Ciudad update(Long id, CiudadDTO ciudadDTO){
        Ciudad ciudad = findByid(id);
        updateCiudadFromDTO(ciudad, ciudadDTO);
        return ciudad;
    }

    @Transactional
    public  void dalete(Long id){
        Ciudad ciudad = findByid(id);
        ciudadRepository.delete(ciudad);
    }

    public Ciudad updateCiudadFromDTO(Ciudad ciudad, CiudadDTO ciudadDTO){
        if(ciudad!=null && ciudadDTO!=null){

            var id = ciudadDTO.getIdCiudad() != null
                    ? ciudadDTO.getIdCiudad()
                    : (ciudad.getIdCiudad() != null ? ciudad.getIdCiudad() : null);

            if(id==null)
                System.out.println("El ID de la ciudad es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                ciudad.setIdCiudad(id);

            var nombre = ciudadDTO.getNombreCiudad() != null
                    ? ciudadDTO.getNombreCiudad()
                    : (!ciudad.getNombreCiudad().isBlank() ? ciudad.getNombreCiudad() : null);

            if(nombre==null)
                throw new IllegalArgumentException("El nombre de la ciudad no puede ser nulo al actualizar");
            else
                ciudad.setNombreCiudad(nombre);

            if(ciudad.getEntidadTerritorial()!=null && ciudadDTO.getEntidadTerritorialDto()!=null)
                ciudad.setEntidadTerritorial(entidadTerritorialServices.updateFromDto(ciudad.getEntidadTerritorial(), ciudadDTO.getEntidadTerritorialDto()));

            return ciudad;

        }else
            throw new IllegalArgumentException("La ciudad o el DTO de la ciudad no pueden ser nulos al actualizar");
    }

    public CiudadDTO convertToDTO(Ciudad ciudad){
        if(ciudad!=null){

            CiudadDTO ciudadDTO = new CiudadDTO();

            var id = ciudad.getIdCiudad() != null ? ciudad.getIdCiudad() : null;
            if(id==null)
                System.out.println("El ID de la ciudad es nulo al convertir a DTO");
            else
                ciudadDTO.setIdCiudad(id);


            var nombre = ciudad.getNombreCiudad() != null ? ciudad.getNombreCiudad() : null;
            if(nombre==null)
                throw new IllegalArgumentException("El nombre de la ciudad no puede ser nulo al convertir");
            else
                ciudadDTO.setNombreCiudad(nombre);


            var entidadTerritorialDto = ciudad.getEntidadTerritorial() != null ?
                    entidadTerritorialServices.convertToDto(ciudad.getEntidadTerritorial()) : null;
            if(entidadTerritorialDto==null)
                throw new IllegalArgumentException("La entidad territorial no puede ser nula al convertir la ciudad a DTO");
            else
                ciudadDTO.setEntidadTerritorialDto(entidadTerritorialDto);


            return ciudadDTO;

        }else
            throw new IllegalArgumentException("La ciudad no puede ser nula al convertir a DTO");
    }


}
