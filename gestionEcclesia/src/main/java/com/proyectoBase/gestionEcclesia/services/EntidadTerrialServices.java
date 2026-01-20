package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.repositories.EntidadTerritorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntidadTerrialServices {
    private final EntidadTerritorialRepository entidadTerritorialRepository;

}
