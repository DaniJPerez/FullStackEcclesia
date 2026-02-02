package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.RolDTO;
import com.proyectoBase.gestionEcclesia.modele.Rol;
import com.proyectoBase.gestionEcclesia.repositories.RolRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Rol findById(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
    }

    @Transactional
    public Rol save(RolDTO rolDTO) {
        Rol rol = new Rol();
        rol.setDescripcionRol(rolDTO.getDescripcionRol());
        rol.setNombreRol(rolDTO.getNombreRol());
        return rolRepository.save(rol);
    }

    @Transactional
    public Rol update(Long id, RolDTO rolDTO) {
        Rol rol = findById(id);
        rol = convertUpdateFromDto(rol, rolDTO);
        return rol;
    }

    @Transactional
    public void delete(Long id) {
        Rol rol = findById(id);
        rolRepository.delete(rol);
    }

    public RolDTO convertToDTO(Rol rol) {
        RolDTO rolDTO= new RolDTO();
        rolDTO.setId(rol.getIdRol());
        rolDTO.setDescripcionRol(rol.getDescripcionRol());
        rolDTO.setNombreRol(rol.getNombreRol());
        return rolDTO;
    }

    public Rol convertUpdateFromDto(Rol rol,RolDTO rolDTO) {
        rol.setIdRol(rolDTO.getId());
        rol.setDescripcionRol(rolDTO.getDescripcionRol());
        rol.setNombreRol(rolDTO.getNombreRol());
        return rol;
    }
}
