package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.EntidadTerritorialDto;
import com.proyectoBase.gestionEcclesia.modele.EntidadTerritorial;
import com.proyectoBase.gestionEcclesia.services.EntidadTerritorialServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntidadTerritorialController {

    private final EntidadTerritorialServices entidadTerritorialService;

    public EntidadTerritorialDto crear(EntidadTerritorialDto dto) {
        EntidadTerritorial entidad = entidadTerritorialService.save(dto);
        return entidadTerritorialService.convertToDto(entidad);
    }

    public EntidadTerritorialDto obtenerPorId(Long id) {
        EntidadTerritorial entidad = entidadTerritorialService.findByIdEntidadTerritorial(id);
        return entidadTerritorialService.convertToDto(entidad);
    }

    public List<EntidadTerritorialDto> obtenerTodas() {
        return entidadTerritorialService.getAllEntidadTerritorials()
                .stream()
                .map(entidadTerritorialService::convertToDto)
                .toList();
    }

    public EntidadTerritorialDto actualizar(Long id, EntidadTerritorialDto dto) {
        EntidadTerritorial entidad = entidadTerritorialService.update(id, dto);
        return entidadTerritorialService.convertToDto(entidad);
    }

    public void eliminar(Long id) {
        entidadTerritorialService.delete(id);
    }

}
