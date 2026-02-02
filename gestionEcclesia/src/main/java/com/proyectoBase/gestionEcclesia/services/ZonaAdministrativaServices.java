package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.ZonaAdministrativaDto;
import com.proyectoBase.gestionEcclesia.modele.Persona;
import com.proyectoBase.gestionEcclesia.modele.ZonaAdministrativa;
import com.proyectoBase.gestionEcclesia.repositories.ZonaAdministrativaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZonaAdministrativaServices {
    private final ZonaAdministrativaRepository repository;
    private final MiembroService miembroService;

    public List<ZonaAdministrativa> getAllZonasAdministrativas() {
        return repository.findAll();
    }

    public ZonaAdministrativa getZonaAdministrativaById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Zona Abministrativa no encontrada con id: " + id));
    }

    public ZonaAdministrativa createZonaAdministrativa(ZonaAdministrativaDto zonaAdministrativaDto) {
        ZonaAdministrativa zonaAdministrativa = new ZonaAdministrativa();
        zonaAdministrativa = updateZonaAdministrativaFromDto(zonaAdministrativa, zonaAdministrativaDto);

        return repository.save(zonaAdministrativa);
    }

    public ZonaAdministrativa updateZonaAdministrativa(Long id, ZonaAdministrativaDto zonaAdministrativaDto) {
        ZonaAdministrativa zonaAdministrativa = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("Zona Abministrativa no encontrada con id: " + id));
        zonaAdministrativa= updateZonaAdministrativaFromDto(zonaAdministrativa, zonaAdministrativaDto);

        return zonaAdministrativa;
    }


    public void deleteZonaAdministrativa(Long id) {
        ZonaAdministrativa zonaAdministrativa = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Zona Abministrativa no encontrada con id: " + id));
        repository.delete(zonaAdministrativa);
    }

    public ZonaAdministrativa updateZonaAdministrativaFromDto(ZonaAdministrativa zonaAdministrativa, ZonaAdministrativaDto zonaAdministrativaDto) {
        zonaAdministrativa.setIdZona(zonaAdministrativaDto.getIdZona());
        zonaAdministrativa.setNombreZona(zonaAdministrativaDto.getNombreZona());
        zonaAdministrativa.setDescripcion(zonaAdministrativaDto.getDescripcionZona());
        Persona miembro = miembroService.findById(zonaAdministrativaDto.getResponsableZona().getId());

        zonaAdministrativa.setLiderZona(miembro);

        return zonaAdministrativa;
    }

    public ZonaAdministrativaDto convertToDto(ZonaAdministrativa zonaAdministrativa) {
        ZonaAdministrativaDto zonaAdministrativaDto = new ZonaAdministrativaDto();
        zonaAdministrativaDto.setIdZona(zonaAdministrativa.getIdZona());
        zonaAdministrativaDto.setNombreZona(zonaAdministrativa.getNombreZona());
        zonaAdministrativaDto.setDescripcionZona(zonaAdministrativa.getDescripcion());
        zonaAdministrativaDto.setResponsableZona(miembroService.convertToDTO(zonaAdministrativa.getLiderZona()));
        return zonaAdministrativaDto;
    }

}
