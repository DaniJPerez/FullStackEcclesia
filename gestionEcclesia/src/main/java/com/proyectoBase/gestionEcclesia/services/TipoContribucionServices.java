package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.TipoContribucionDTO;
import com.proyectoBase.gestionEcclesia.modele.TipoContribucion;
import com.proyectoBase.gestionEcclesia.repositories.TipoContribucionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoContribucionServices {
    private final TipoContribucionRepository repository;

    public List<TipoContribucion> getAllTipoContribuciones() {
        return repository.findAll();
    }

    public TipoContribucion getTipoContribucionById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tipo Contribucion no encontrada con id: " + id));
    }

    public TipoContribucion createTipoContribucion(TipoContribucionDTO tipoContribucionDTO) {
        TipoContribucion tipoContribucion = new TipoContribucion();
        tipoContribucion = updateTipoContribucionFromDto(tipoContribucion, tipoContribucionDTO);

        return repository.save(tipoContribucion);
    }

    public TipoContribucion updateTipoContribucion(Long id, TipoContribucionDTO tipoContribucionDTO) {
        TipoContribucion tipoContribucion = getTipoContribucionById(id);
        tipoContribucion = updateTipoContribucionFromDto(tipoContribucion, tipoContribucionDTO);

        return tipoContribucion;
    }

    public void deleteTipoContribucion(Long id) {
        TipoContribucion tipoContribucion = getTipoContribucionById(id);
        repository.delete(tipoContribucion);
    }

    public TipoContribucion updateTipoContribucionFromDto(TipoContribucion tipoContribucion, TipoContribucionDTO tipoContribucionDto) {
        tipoContribucion.setId(tipoContribucionDto.getIdTipoContribucion());
        tipoContribucion.setNombre(tipoContribucion.getNombre());
        tipoContribucion.setDescripcion(tipoContribucionDto.getDescripcionTipoContribucion());
        return tipoContribucion;
    }

    public TipoContribucionDTO convertToDto(TipoContribucion tipoContribucion) {
        TipoContribucionDTO tipoContribucionDto = new TipoContribucionDTO();
        tipoContribucionDto.setIdTipoContribucion(tipoContribucion.getId());
        tipoContribucionDto.setNombreTipoContribucion(tipoContribucion.getNombre());
        tipoContribucionDto.setDescripcionTipoContribucion(tipoContribucion.getDescripcion());
        return tipoContribucionDto;
    }

}
