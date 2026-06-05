package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.NacionalidadDTO;
import com.proyectoBase.gestionEcclesia.config.UsuarioContextUtil;
import com.proyectoBase.gestionEcclesia.modele.Nacionalidad;
import com.proyectoBase.gestionEcclesia.repositories.NacionalidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NacionalidadService {

    private final UsuarioContextUtil usuarioContextUtil;
    private final NacionalidadRepository nacionalidadRepository;
    private final PaisServices paisServices;

    public List<Nacionalidad> getAll() {
        return nacionalidadRepository.findAll();
    }

    public Nacionalidad findById(Long id) {
        return nacionalidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nacionalidad no encontrada con ID: " + id));
    }

    public Nacionalidad save(NacionalidadDTO dto) {
        Nacionalidad n = new Nacionalidad();
        updateFromDto(n, dto);
        n.setUsuarioRegistro(usuarioContextUtil.getUsuarioAutenticado());
        return nacionalidadRepository.save(n);
    }

    public Nacionalidad update(Long id, NacionalidadDTO dto) {
        Nacionalidad n = findById(id);
        updateFromDto(n, dto);
        return nacionalidadRepository.save(n);
    }

    public void delete(Long id) {
        nacionalidadRepository.deleteById(id);
    }

    private void updateFromDto(Nacionalidad n, NacionalidadDTO dto) {
        if (dto.getNombreNacionalidad() != null) n.setNombreNacionalidad(dto.getNombreNacionalidad());
        if (dto.getDescripcionNacionalidad() != null) n.setDescripcion(dto.getDescripcionNacionalidad());
        if (dto.getPais() != null) n.setPais(paisServices.findByIdPais(dto.getPais().getIdPais()));
    }

    public NacionalidadDTO convertToDto(Nacionalidad n) {
        NacionalidadDTO dto = new NacionalidadDTO();
        dto.setIdNacionalidad(n.getIdNacionalidad());
        dto.setNombreNacionalidad(n.getNombreNacionalidad());
        dto.setDescripcionNacionalidad(n.getDescripcion());
        if (n.getPais() != null) dto.setPais(paisServices.convertToDto(n.getPais()));
        return dto;
    }
}
