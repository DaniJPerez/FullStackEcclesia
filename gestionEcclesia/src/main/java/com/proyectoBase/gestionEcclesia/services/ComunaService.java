package com.proyectoBase.gestionEcclesia.services;

import jakarta.persistence.Convert;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyectoBase.gestionEcclesia.DTOS.ComunaDTO;
import com.proyectoBase.gestionEcclesia.modele.Comuna;
import com.proyectoBase.gestionEcclesia.repositories.ComunaRepository;
import com.proyectoBase.gestionEcclesia.repositories.CiudadRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComunaService {
    private final ComunaRepository comunaRepository;
    private final CiudadService ciudadService;

    public Comuna findById(Long idComuna){
        return comunaRepository.findById(idComuna)
                .orElseThrow(()-> new EntityNotFoundException("La comuna no fue encontrada"));

    }

    public List<Comuna> findAll(){
        return comunaRepository.findAll();
    }

    @Transactional
    public Comuna save(ComunaDTO comunaDTO){
        Comuna comuna = new Comuna();
        comuna=updateComunaFromDTO(comuna,comunaDTO);
        return comunaRepository.save(comuna);
    }

    @Transactional
    public Comuna update(Long id, ComunaDTO comunaDTO){
        Comuna comuna = findById(id);
        comuna= updateComunaFromDTO(comuna, comunaDTO);
        return comuna;
    }

    @Transactional
    public void delete (Long id){
        Comuna comuna = findById(id);
        comunaRepository.delete(comuna);
    }


     public Comuna updateComunaFromDTO(Comuna comuna, ComunaDTO comunaDTO){
        comuna.setId(comunaDTO.getIdComuna());
        comuna.setNombre(comunaDTO.getDescripcionComuna());
        comuna.setCiudad(ciudadService.updateCiudadFromDTO(comuna.getCiudad(),comunaDTO.getCiudadDTO()));
        return comuna;
    }

    public ComunaDTO convertToDTO(Comuna comuna){
        ComunaDTO comunaDTO= new ComunaDTO();
        comunaDTO.setIdComuna(comuna.getId());
        comunaDTO.setDescripcionComuna(comuna.getNombre());
        return comunaDTO;
    }

}
