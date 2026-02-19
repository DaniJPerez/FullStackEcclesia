package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.PaisDto;
import com.proyectoBase.gestionEcclesia.modele.Pais;
import com.proyectoBase.gestionEcclesia.repositories.PaisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaisServices {
    private final PaisRepository paisRepository;

    public List<Pais> getAllPaises() {
        return paisRepository.findAll();
    }

    public Pais findByIdPais(Long idPais) {
        return paisRepository.findById(idPais).orElseThrow(() -> new IllegalArgumentException("Pais no encontrado con ID: " + idPais));
    }

    public Pais findByNombrePais(String nombrePais) {
        return paisRepository.findByNombrePais(nombrePais);
    }

    public Pais savePais(PaisDto pais) {
        Pais nuevoPais = new Pais();
        updatePaisFromDto(nuevoPais, pais);
        return paisRepository.save(nuevoPais);
    }

    public Pais updatePais(Long idPais, PaisDto paisDto) {
        Pais paisExistente = findByIdPais(idPais);
        updatePaisFromDto(paisExistente, paisDto);
        return paisRepository.save(paisExistente);
    }

    public void deletePais(Long idPais) {
        paisRepository.deleteById(idPais);
    }

    public Pais updatePaisFromDto(Pais pais,PaisDto paisDto) {
        if(pais!=null && paisDto!=null){
            var id = (paisDto.getIdPais() != null)
                    ? paisDto.getIdPais()
                    : pais.getId();

            if(id==null)
                System.out.println("El ID del pais es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                pais.setId(id);

            var nombre = (paisDto.getNombrePais() != null)
                    ? paisDto.getNombrePais()
                    : pais.getNombrePais();
            if (nombre==null)
                System.out.println("El nombre del pais es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                pais.setNombrePais(nombre);


            var descripcion = (paisDto.getDescripcion() != null)
                    ? paisDto.getDescripcion()
                    : pais.getDescripcion();
            if (descripcion==null)
                System.out.println("La descripción del pais es nula al actualizar");
            else
                pais.setDescripcion(descripcion);

            return pais;
        }else
            throw new IllegalArgumentException("HUBO UN ERROR AL CONVERTIR EL DTO A ENTIDAD, EL DTO O LA ENTIDAD SON NULOS");
    }

    public PaisDto convertToDto(Pais pais) {
        if(pais!=null){
            PaisDto paisDto = new PaisDto();

            var id = pais.getId() != null ? pais.getId() : null;
            if(id==null)
                throw new IllegalArgumentException("El ID del pais es nulo al convertir a DTO, HUBO UN ERROR AL CONVERTIR LA ENTIDAD A DTO");
            else
                 paisDto.setIdPais(id);


            var nombre = pais.getNombrePais() != null ? pais.getNombrePais() : null;
            if(nombre==null)
                throw new IllegalArgumentException("El nombre del pais es nulo al convertir a DTO, HUBO UN ERROR AL CONVERTIR LA ENTIDAD A DTO");
            else
                paisDto.setNombrePais(nombre);


            var descripcion = pais.getDescripcion() != null ? pais.getDescripcion() : null;
            if(descripcion==null)
                System.out.println("La descripción del pais es nula al convertir a DTO, No se asignará una descripción vacía al guardar");
            else
                paisDto.setDescripcion(descripcion);

            return paisDto;
        }else
            throw new IllegalArgumentException("HUBO UN ERROR AL CONVERTIR LA ENTIDAD A DTO, EL DTO O LA ENTIDAD SON NULOS");
    }

}
