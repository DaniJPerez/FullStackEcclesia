package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.modele.ZonaAbministrativa;
import com.proyectoBase.gestionEcclesia.repositories.ZonaAbministrativaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZonaAbministrativaServices {
    private final ZonaAbministrativaRepository repository;

    public List<ZonaAbministrativa> getAllZonasAbministrativas() {
        return repository.findAll();
    }

    public ZonaAbministrativa getZonaAbministrativaById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public ZonaAbministrativa createZonaAbministrativa(ZonaAbministrativa zonaAbministrativa) {
        return repository.save(zonaAbministrativa);
    }

    public ZonaAbministrativa updateZonaAbministrativa(Long id, ZonaAbministrativa zonaAbministrativaDetails) {
        ZonaAbministrativa zonaAbministrativa = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("Zona Abministrativa no encontrada con id: " + id));
        if (zonaAbministrativa != null) {
            zonaAbministrativa.setNombreZona(zonaAbministrativaDetails.getNombreZona());
            zonaAbministrativa.setDescripcion(zonaAbministrativaDetails.getDescripcion());
            return zonaAbministrativa;
        }
        return null;
    }




}
