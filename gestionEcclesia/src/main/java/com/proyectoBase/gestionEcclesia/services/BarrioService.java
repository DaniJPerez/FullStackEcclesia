package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.repositories.BarrioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.proyectoBase.gestionEcclesia.DTOS.BarrioDTO;
import com.proyectoBase.gestionEcclesia.modele.Barrio;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarrioService {
    private final BarrioRepository barrioRepository;
    private final ComunaService comunaService;

    public List<Barrio> findAll(){
        return barrioRepository.findAll();
    }

    public Barrio findByid(Long id){
        return barrioRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Barrio no encontrado"));
    }

    @Transactional
    public Barrio save(BarrioDTO barrioDTO){
        Barrio barrio = new Barrio();
        barrio = updateBarrioFromDTO(barrio,barrioDTO);
        return barrioRepository.save(barrio);
    }

    @Transactional
    public Barrio update(Long id, BarrioDTO barrioDTO){
        Barrio barrio = findByid(id);
        barrio = updateBarrioFromDTO(barrio, barrioDTO);
        return barrio;
    }

    @Transactional
    public void delete(Long id){
        Barrio barrio = findByid(id);
        barrioRepository.delete(barrio);
    }

    //accesivilidad solamente desde el paquete
    public Barrio updateBarrioFromDTO(Barrio barrio, BarrioDTO barrioDTO){
        var id = (barrioDTO != null && barrioDTO.getIdBarrio() != null)
                ? barrioDTO.getIdBarrio()
                : (barrio != null ? barrio.getId() : null);

        if(id!=null)
            barrio.setId(id);
        else
            System.out.println("El ID del barrio es nulo al actualizar, se asignará uno nuevo al guardar");

        var nombre = (barrioDTO != null && barrioDTO.getNombreBarrio() != null)
                ? barrioDTO.getNombreBarrio()
                : (barrio != null ? barrio.getNombre() : null);
        if(nombre!=null)
            barrio.setNombre(nombre);
        else
            throw new IllegalArgumentException("El nombre del barrio no puede ser nulo al actualizar");

        barrio.setComuna(comunaService.updateComunaFromDTO(barrio.getComuna(), barrioDTO.getComunaDTO()));


        return barrio;
    }

    public BarrioDTO convertToDTO(Barrio barrio){

        BarrioDTO barrioDTO = new BarrioDTO();

        var id = barrio.getId() != null ? barrio.getId() : null;
        if(id==null)
            throw new IllegalArgumentException("¡¡¡ P R E C A U C I O N !!! \n El ID del barrio no puede ser nulo al convertir a DTO");
        else
            barrioDTO.setIdBarrio(id);

        var nombre = barrio.getNombre()!=null ? barrio.getNombre():null;
        if(nombre==null)
            throw new IllegalArgumentException("El nombre del barrio no puede ser nulo al convertir a DTO");
        else
            barrioDTO.setNombreBarrio(nombre);

        barrioDTO.setComunaDTO(comunaService.convertToDTO(barrio.getComuna()));

        return barrioDTO;
    }


}
