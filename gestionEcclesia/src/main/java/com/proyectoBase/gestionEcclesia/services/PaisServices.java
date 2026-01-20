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

    public Pais savePais(Pais pais) {
        return paisRepository.save(pais);
    }


    public void deletePais(Long idPais) {
        paisRepository.deleteById(idPais);
    }

    public Pais updatePaisFromDto(PaisDto paisDto) {
        Pais pais = findByIdPais(paisDto.getIdPais());
        pais.setNombrePais(paisDto.getNombrePais());
        pais.setDescripcion(paisDto.getDescripcion());
        return pais;
    }

    public PaisDto convertToDto(Pais pais) {
        PaisDto paisDto = new PaisDto();
        paisDto.setIdPais(pais.getId());
        paisDto.setNombrePais(pais.getNombrePais());
        paisDto.setDescripcion(pais.getDescripcion());
        return paisDto;
    }

}
