package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.repositories.DireccionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import com.proyectoBase.gestionEcclesia.DTOS.DireccionDTO;
import com.proyectoBase.gestionEcclesia.modele.Direccion;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DireccionService {
    private final DireccionRepository direccionRepository;
    private final BarrioService barrioService;

    public List<Direccion> findAll(){
        return direccionRepository.findAll();
    }

    public Direccion findById(Long id){
        return direccionRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException());
    }

    @Transactional
    public Direccion save(DireccionDTO direccionDTO){
        Direccion direccion = new Direccion();
        updateDireccionFromDTO(direccion,direccionDTO);
        return direccionRepository.save(direccion);
    }

    @Transactional
    public Direccion update(Long id, DireccionDTO direccionDTO){
        Direccion direccion= findById(id);
        updateDireccionFromDTO(direccion,direccionDTO);
        return direccion;
    }

    @Transactional
    public void delete(Long id){
        Direccion direccion = findById(id);
        direccionRepository.delete(direccion);
    }

    @Transactional
    public Direccion updateDireccionFromDTO(Direccion direccion, DireccionDTO direccionDTO){
        if(direccion!=null && direccionDTO!=null) {

            var id = direccionDTO.getIdDireccion() != null
                    ? direccionDTO.getIdDireccion()
                    : (direccion.getId() != null ? direccion.getId() : null);

            if(id==null)
                System.out.println("El ID de la dirección es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                direccion.setId(id);

            var calle = direccionDTO.getCalle() != null
                    ? direccionDTO.getCalle() :
                    (direccion.getCalle()!=null && !direccion.getCalle().isBlank())
                    ? direccion.getCalle() : null;

            if(calle==null)
                throw new IllegalArgumentException("La calle de la dirección no puede ser nula al actualizar");
            else
                direccion.setCalle(calle);

            var numero = direccionDTO.getNumero() != null&& !direccionDTO.getNumero().isBlank()
                    ? direccionDTO.getNumero() :
                    (direccion.getNumero()!=null && !direccion.getNumero().isEmpty())
                    ? direccion.getNumero() : null;
            if(numero==null)
                throw new IllegalArgumentException("El número de la dirección no puede ser nulo al actualizar");
            else
                direccion.setNumero(numero);

            if(direccion.getBarrio()!=null && direccionDTO.getBarrioDTO()!=null)
                direccion.setBarrio(barrioService.updateBarrioFromDTO(direccion.getBarrio(),direccionDTO.getBarrioDTO()));

            return direccion;

        }else
            throw new IllegalArgumentException("La dirección o el DTO proporcionado es nulo, no se puede actualizar la dirección");
    }

    @Transactional
    public DireccionDTO converToDTO(Direccion direccion){
        if(direccion!=null){

            DireccionDTO direccionDTO = new DireccionDTO();

            var id = direccion.getId() != null ? direccion.getId() : null;
            if(id==null)
                throw new IllegalArgumentException("El ID de la dirección es nulo al convertir a DTO");
            else
                direccionDTO.setIdDireccion(direccion.getId());


            var numero = direccion.getNumero() != null && !direccion.getNumero().isBlank() ? direccion.getNumero() : null;
            if(numero==null)
                throw new IllegalArgumentException("El número de la dirección no puede ser nulo al convertir a DTO");
            else
                direccionDTO.setNumero(numero);


            var calle = direccion.getCalle() != null && !direccion.getCalle().isBlank() ? direccion.getCalle() : null;
            if(calle==null)
                throw new IllegalArgumentException("La calle de la dirección no puede ser nula al convertir a DTO");
            else
                direccionDTO.setCalle(calle);


            direccionDTO.setBarrioDTO(barrioService.convertToDTO(direccion.getBarrio()));

            return direccionDTO;

        }else
            throw new IllegalArgumentException("La dirección proporcionada es nula, no se puede convertir a DTO");
    }

}
