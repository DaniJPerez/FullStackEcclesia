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
        nuevoPais = updatePaisFromDto(nuevoPais, pais);
        return paisRepository.save(nuevoPais);
    }

    public Pais updatePais(Long idPais, PaisDto paisDto) {
        Pais paisExistente = findByIdPais(idPais);
        paisExistente = updatePaisFromDto(paisExistente, paisDto);
        return paisRepository.save(paisExistente);
    }

    public void deletePais(Long idPais) {
        paisRepository.deleteById(idPais);
    }

    public Pais updatePaisFromDto(Pais pais,PaisDto paisDto) {
        pais.setId(paisDto.getIdPais());
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
