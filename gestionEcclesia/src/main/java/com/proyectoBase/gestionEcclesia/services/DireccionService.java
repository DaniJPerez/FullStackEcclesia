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
        direccion = updateDireccionFromDTO(direccion,direccionDTO);
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

        var id = (direccionDTO != null && direccionDTO.getIdDireccion() != null)
                ? direccionDTO.getIdDireccion()
                : (direccion != null ? direccion.getId() : null);

        if(id==null)
            System.out.println("El ID de la dirección es nulo al actualizar, se asignará uno nuevo al guardar");
        else
            direccion.setId(id);

        var calle = (direccionDTO != null && direccionDTO.getCalle() != null)
                ? direccionDTO.getCalle() :
                (direccion!=null && direccion.getCalle()!=null && !direccion.getCalle().isEmpty())
                ? direccion.getCalle() : null;

        if(calle==null)
            throw new IllegalArgumentException("La calle de la dirección no puede ser nula al actualizar");
        else
            direccion.setCalle(calle);

        var numero = (direccionDTO != null && direccionDTO.getNumero() != null&& !direccionDTO.getNumero().isEmpty())
                ? direccionDTO.getNumero() :
                (direccion!=null && direccion.getNumero()!=null && !direccion.getNumero().isEmpty())
                ? direccion.getNumero() : null;


        direccion.setBarrio(barrioService.updateBarrioFromDTO(direccion.getBarrio(),direccionDTO.getBarrioDTO()));


        return direccion;
    }

    @Transactional
    public DireccionDTO converToDTO(Direccion direccion){

        DireccionDTO direccionDTO = new DireccionDTO();

        var id = direccion != null && direccion.getId() != null ? direccion.getId() : null;
        if(id==null)
            throw new IllegalArgumentException("El ID de la dirección es nulo al convertir a DTO");
        else
            direccionDTO.setIdDireccion(direccion.getId());


        var numero = direccion!=null &&direccion.getNumero() != null && !direccion.getNumero().isEmpty() ? direccion.getNumero() : null;
        if(numero==null)
            throw new IllegalArgumentException("El número de la dirección no puede ser nulo al convertir a DTO");
        else
            direccionDTO.setNumero(numero);


        var calle = direccion != null && direccion.getCalle() != null ? direccion.getCalle() : null;
        if(calle==null)
            throw new IllegalArgumentException("La calle de la dirección no puede ser nula al convertir a DTO");
        else
            direccionDTO.setCalle(calle);


        direccionDTO.setBarrioDTO(barrioService.convertToDTO(direccion.getBarrio()));

        return direccionDTO;
    }

}
