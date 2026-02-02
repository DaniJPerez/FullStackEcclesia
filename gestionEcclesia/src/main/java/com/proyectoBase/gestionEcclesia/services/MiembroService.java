package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.AsistenciaEventoDto;
import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.modele.*;
import com.proyectoBase.gestionEcclesia.repositories.MiembroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MiembroService {

    private final MiembroRepository miembroRepository;
    private final RolService rolService;
    private final DireccionService direccionService;
    private final EventoService eventoService;

    public List<Persona> findAll() {
        return miembroRepository.findAll();
    }

    public Persona findById(Long id) {
        return miembroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Miembro no encontrado con ID: " + id));
    }

    public List<Persona> findByPrimerNombre(String nombre) {
        return miembroRepository.findByPrimerNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public Persona save(MiembroDTO miembroDTO) {
        Persona persona = new Persona();
        updateMiembroFromDTO(persona, miembroDTO);
        return miembroRepository.save(persona);
    }

    @Transactional
    public Persona update(Long id, MiembroDTO miembroDTO) {
        Persona persona = findById(id);
        updateMiembroFromDTO(persona, miembroDTO);
        return miembroRepository.save(persona);
    }

    @Transactional
    public void delete(Long id) {
        Persona persona = findById(id);
        miembroRepository.delete(persona);
    }

    public Persona updateMiembroFromDTO(Persona persona, MiembroDTO miembroDTO) {
        persona.setPrimerNombre(miembroDTO.getNombre());
        persona.setSegundoNombre(miembroDTO.getNombre2());
        persona.setPrimerApellido(miembroDTO.getApellido());
        persona.setSegundoApellido(miembroDTO.getApellido2());
        persona.setFechaNacimiento(miembroDTO.getFechaNacimiento());
        persona.setEstadoCivil(Persona.convertirStringAEstadoCivil(miembroDTO.getEstadoCivil()));
        persona.setCorreo(miembroDTO.getCorreo());
        persona.setTelefono(miembroDTO.getTelefono());
        persona.setSexo(Persona.convertirStringASexo(miembroDTO.getSexo()));

        // Manejar la dirección
        if (miembroDTO.getDireccion() != null) {
            persona.setDireccion(direccionService
                    .updateDireccionFromDTO(persona.getDireccion(), miembroDTO.getDireccion()));
        }
        // Manejar roles
        if (persona.getRol() != null && miembroDTO.getRolDTO() != null) {
            Rol rol = new Rol();
            persona.setRol(rolService.convertUpdateFromDto(rol,miembroDTO.getRolDTO()));
        }
        return persona;
    }

    public MiembroDTO convertToDTO(Persona persona) {
        MiembroDTO dto = new MiembroDTO();
        dto.setId(persona.getNumeroIdentificacion());
        dto.setNombre(persona.getPrimerNombre());
        dto.setNombre2(persona.getSegundoNombre());
        dto.setApellido(persona.getPrimerApellido());
        dto.setApellido2(persona.getSegundoApellido());
        dto.setFechaNacimiento(persona.getFechaNacimiento());
        dto.setEstadoCivil(persona.getEstadoCivil().name());
        dto.setCorreo(persona.getCorreo());
        dto.setTelefono(persona.getTelefono());
        dto.setSexo(persona.getSexo().name());
        
        // Convertir dirección
        if (persona.getDireccion() != null) {
            direccionService.converToDTO(persona.getDireccion());
        }
        
        // Convertir Roles
        if(persona.getRol() != null){
            dto.setRolDTO(rolService.convertToDTO(persona.getRol()));
        }

        return dto;
    }


}
