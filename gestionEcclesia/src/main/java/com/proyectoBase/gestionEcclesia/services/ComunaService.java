package com.proyectoBase.gestionEcclesia.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyectoBase.gestionEcclesia.DTOS.ComunaDTO;
import com.proyectoBase.gestionEcclesia.modele.Comuna;
import com.proyectoBase.gestionEcclesia.repositories.ComunaRepository;

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
        updateComunaFromDTO(comuna, comunaDTO);
        return comuna;
    }

    @Transactional
    public void delete (Long id){
        Comuna comuna = findById(id);
        comunaRepository.delete(comuna);
    }


     public Comuna updateComunaFromDTO(Comuna comuna, ComunaDTO comunaDTO){

        if(comuna!=null && comunaDTO!=null){

            var id = comunaDTO.getIdComuna() != null
                    ? comunaDTO.getIdComuna()
                    : (comuna.getId() != null ? comuna.getId() : null);
            if(id==null)
                System.out.println("No hay una Id Expesificada se estara creando una nueva instancia de comuna");
            else
                comuna.setId(id);

            var nombre = comunaDTO.getNombreComuna() != null
                    ? comunaDTO.getNombreComuna()
                    : (comuna.getNombre() != null ? comuna.getNombre() : null);
            if(nombre==null)
                System.out.println("No hay un nombre Expesificado");
            else
                comuna.setNombre(nombre);

            var descripcion = comunaDTO.getDescripcionComuna() != null
                    ? comunaDTO.getDescripcionComuna()
                    : (comuna.getDescripcion() != null ? comuna.getDescripcion() : null);
            if (descripcion==null)
                System.out.println("No hay una descripcion Expesificada");
            else
                comuna.setDescripcion(descripcion);

            if(comuna.getCiudad()!=null && comunaDTO.getCiudadDTO()!=null)
                comuna.setCiudad(ciudadService.updateCiudadFromDTO(comuna.getCiudad(),comunaDTO.getCiudadDTO()));

            return comuna;

        }else
            throw new IllegalArgumentException("El objeto comuna o comunaDTO es nulo al actualizar");
    }

    public ComunaDTO convertToDTO(Comuna comuna){
        if(comuna!=null){

            ComunaDTO comunaDTO= new ComunaDTO();

            var id = comuna.getId()!=null? comuna.getId():null;
            if(id==null)
                throw new IllegalArgumentException("¡¡¡ P R E C A U C I O N !!! \n : No hay una Id Expesificada en instancia de comunaDTO");
            else
                comunaDTO.setIdComuna(id);


            var nombre = comuna.getNombre() != null ? comuna.getNombre() : null;
            if(nombre==null)
                throw new IllegalArgumentException("¡¡¡ P R E C A U C I O N !!! \n : No hay un nombre Expesificado en instancia de comunaDTO");
            else
                comunaDTO.setNombreComuna(nombre);


            var descripcion = comuna.getDescripcion() != null && !comuna.getDescripcion().isBlank() ? comuna.getDescripcion() : null;
            if(descripcion==null)
                System.out.println("¡¡¡ P R E C A U C I O N !!! \n : No hay una descripcion Expesificada en instancia de comunaDTO");
            else
                comunaDTO.setDescripcionComuna(descripcion);

            var ciudadDTO = ciudadService.convertToDTO(comuna.getCiudad());

            comunaDTO.setCiudadDTO(ciudadDTO);

            return comunaDTO;

        }else
            throw new IllegalArgumentException("El objeto comuna es nulo al convertir a DTO");
    }

}
