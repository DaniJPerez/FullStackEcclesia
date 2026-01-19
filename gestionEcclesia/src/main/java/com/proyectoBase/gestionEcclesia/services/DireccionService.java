package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.repositories.DireccionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.proyectoBase.gestionEcclesia.DTOS.BarrioDTO;
import com.proyectoBase.gestionEcclesia.DTOS.ComunaDTO;
import com.proyectoBase.gestionEcclesia.DTOS.DireccionDTO;
import com.proyectoBase.gestionEcclesia.modele.Barrio;
import com.proyectoBase.gestionEcclesia.modele.Comuna;
import com.proyectoBase.gestionEcclesia.modele.Direccion;
import com.proyectoBase.gestionEcclesia.repositories.DireccionRepository;


@Service
@RequiredArgsConstructor
public class DireccionService {
    private final DireccionRepository direccionRepository;
    private final BarrioService barrioService;

    public Direccion findById(Long id){
        return direccionRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException());
    }

    @Transactional
    public Direccion save(DireccionDTO direccionDTO){
        Direccion direccion = new Direccion();
        direccion = updateDireccionFromDTO(direccion,direccionDTO);
        return direccionRepository.save(direccion);
    }

    @Transactional
    public Direccion update(Long id, DireccionDTO direccionDTO){
        Direccion direccion= findById(id);
        direccion = updateDireccionFromDTO(direccion,direccionDTO);
        return direccion;
    }

    Direccion updateDireccionFromDTO(Direccion direccion, DireccionDTO direccionDTO){
        direccion.setId(direccion.getId());
        direccion.setCalle(direccionDTO.getCalle());
        direccion.setBarrio(barrioService.updateBarrioFromDTO(direccion.getBarrio(),direccionDTO.getBarrioDTO()));
        return direccion;
    }

    DireccionDTO converToDTO(Direccion direccion){
        DireccionDTO direccionDTO = new DireccionDTO();
        direccionDTO.setIdDireccion(direccion.getId());
        direccionDTO.setNumero(direccionDTO.getNumero());
        direccionDTO.setCalle(direccion.getCalle());
        direccionDTO.setBarrioDTO(barrioService.convertToDTO(direccion.getBarrio()));
        return direccionDTO;
    }

}
