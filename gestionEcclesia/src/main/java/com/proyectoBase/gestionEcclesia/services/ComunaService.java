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

        var id = comuna.getId() != null || comunaDTO.getIdComuna() !=null ? comunaDTO.getIdComuna(): null ;
        comuna.setId(id);
        if(id==null)
            System.out.println("No hay una Id Expesificada se estara creando una nueva instancia de comuna");


        var nombre = comunaDTO.getNombreComuna() != null || comuna.getNombre() !=null ? comuna.getNombre() : null;
        comuna.setNombre(nombre);
        if(nombre==null)
            System.out.println("No hay un nombre Expesificado");


        var descripcion = comunaDTO.getDescripcionComuna() != null || comuna.getDescripcion() !=null ? comunaDTO.getDescripcionComuna() : null;
        comuna.setDescripcion(descripcion);
        if (descripcion==null)
            System.out.println("No hay una descripcion Expesificada");

        
        comuna.setCiudad(ciudadService.updateCiudadFromDTO(comuna.getCiudad(),comunaDTO.getCiudadDTO()));

        return comuna;
    }

    public ComunaDTO convertToDTO(Comuna comuna){
        ComunaDTO comunaDTO= new ComunaDTO();
        var id = comuna.getId()!=null? comuna.getId():null;
        comunaDTO.setIdComuna(id);
        if(id==null)
            throw new IllegalArgumentException("¡¡¡ P R E C A U C I O N !!! \n : No hay una Id Expesificada en instancia de comunaDTO");

        var nombre = comuna.getNombre() != null ? comuna.getNombre() : null;
        comunaDTO.setNombreComuna(nombre);
        if(nombre==null)
            throw new IllegalArgumentException("¡¡¡ P R E C A U C I O N !!! \n : No hay un nombre Expesificado en instancia de comunaDTO");

        var descripcion = comuna.getDescripcion() != null ? comuna.getDescripcion() : null;
        if(descripcion!=null)
            comunaDTO.setDescripcionComuna(descripcion);

        var ciudadDTO = ciudadService.convertToDTO(comuna.getCiudad());

        comunaDTO.setCiudadDTO(ciudadDTO);

        return comunaDTO;
    }

}
