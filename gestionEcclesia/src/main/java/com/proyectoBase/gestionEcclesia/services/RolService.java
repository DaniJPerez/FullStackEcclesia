package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.RolDTO;
import com.proyectoBase.gestionEcclesia.modele.Rol;
import com.proyectoBase.gestionEcclesia.repositories.RolRepository;
import jakarta.persistence.EntityNotFoundException;
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
        convertUpdateFromDto(rol, rolDTO);
        return rol;
    }

    @Transactional
    public void delete(Long id) {
        Rol rol = findById(id);
        rolRepository.delete(rol);
    }


    public Rol convertUpdateFromDto(Rol rol,RolDTO rolDTO) {
        if(rol!=null && rolDTO!=null){
            var id = (rolDTO.getId() != null)
                    ? rolDTO.getId()
                    : (rol.getIdRol() != null ? rol.getIdRol() : null);
            if (id == null)
                System.out.println("El ID del rol es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                rol.setIdRol(rolDTO.getId());

            var descripcion = (rolDTO.getDescripcionRol() != null && !rolDTO.getDescripcionRol().isBlank())
                    ? rolDTO.getDescripcionRol()
                    : (rol.getDescripcionRol() != null ? rol.getDescripcionRol() : null);
            if (descripcion == null)
                System.out.println("La descripción del rol es nula al actualizar, se asignará una nueva al guardar");
            else
                rol.setDescripcionRol(rolDTO.getDescripcionRol());

            var nombre = (rolDTO.getNombreRol() != null && !rolDTO.getNombreRol().isBlank())
                    ? rolDTO.getNombreRol()
                    : (rol.getNombreRol() != null ? rol.getNombreRol() : null);
            if (nombre == null)
                System.out.println("El nombre del rol es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                rol.setNombreRol(rolDTO.getNombreRol());

            return rol;

        }else
            throw new IllegalArgumentException("El rol o el DTO proporcionado es nulo, no se puede actualizar el rol");
    }

    public RolDTO convertToDTO(Rol rol) {
        if(rol!=null){
            RolDTO rolDTO= new RolDTO();
            var id = (rolDTO.getId() != null)
                    ? rolDTO.getId()
                    : (rol.getIdRol() != null ? rol.getIdRol() : null);
            if (id == null)
                System.out.println("El ID del rol es nulo al convertir a DTO, se asignará uno nuevo al guardar");
            else
                rolDTO.setId(rol.getIdRol());

            var descripcion = (rolDTO.getDescripcionRol() != null && !rolDTO.getDescripcionRol().isBlank())
                    ? rolDTO.getDescripcionRol()
                    : (rol.getDescripcionRol() != null ? rol.getDescripcionRol() : null);
            if (descripcion == null)
                System.out.println("La descripción del rol es nula al convertir a DTO, se asignará una nueva al guardar");
            else
                rolDTO.setDescripcionRol(rol.getDescripcionRol());

            var nombre = (rolDTO.getNombreRol() != null && !rolDTO.getNombreRol().isBlank())
                    ? rolDTO.getNombreRol()
                    : (rol.getNombreRol() != null ? rol.getNombreRol() : null);
            if (nombre == null)
                System.out.println("El nombre del rol es nulo al convertir a DTO, se asignará uno nuevo al guardar");
            else
                rolDTO.setNombreRol(rol.getNombreRol());

            return rolDTO;
        }else
            throw new IllegalArgumentException("El rol proporcionado es nulo, no se puede convertir a DTO");

    }
}
