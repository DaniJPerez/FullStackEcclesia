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
        ciudad= updateCiudadFromDTO(ciudad, ciudadDTO);
        return ciudadRepository.save(ciudad);
    }

    @Transactional
    public Ciudad update(Long id, CiudadDTO ciudadDTO){
        Ciudad ciudad = findByid(id);
        ciudad = updateCiudadFromDTO(ciudad, ciudadDTO);
        return ciudad;
    }

    @Transactional
    public  void dalete(Long id){
        Ciudad ciudad = findByid(id);
        ciudadRepository.delete(ciudad);
    }

    public Ciudad updateCiudadFromDTO(Ciudad ciudad,CiudadDTO ciudadDTO){
        ciudad.setIdCiudad(ciudadDTO.getIdCiudad());
        ciudad.setNombreCiudad(ciudadDTO.getNombreCiudad());
        return ciudad;
    }

    public CiudadDTO convertToDTO(Ciudad ciudad){
        CiudadDTO ciudadDTO = new CiudadDTO();
        ciudadDTO.setIdCiudad(ciudad.getIdCiudad());
        ciudadDTO.setNombreCiudad(ciudad.getNombreCiudad());
        return ciudadDTO;
    }


}
