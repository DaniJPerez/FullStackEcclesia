package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.modele.*;
import com.proyectoBase.gestionEcclesia.repositories.MiembroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MiembroService {

    private final MiembroRepository miembroRepository;
    private final RolService rolService;
    private final DireccionService direccionService;

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
        if(persona!=null && miembroDTO != null && miembroDTO.getId() != null && persona.getNumeroIdentificacion().equals(miembroDTO.getId())){


            var primerNombre = miembroDTO.getNombre()!=null && !miembroDTO.getNombre().isBlank() ? miembroDTO.getNombre() : persona.getPrimerNombre();
            if(primerNombre!=null)
                persona.setPrimerNombre(primerNombre);
            else
                System.out.println("El primer nombre es nulo o está en blanco en el DTO, se mantendrá el valor actual");

            var segundoNombre = miembroDTO.getNombre2()!=null && !miembroDTO.getNombre2().isBlank() ? miembroDTO.getNombre2() : persona.getSegundoNombre();
            if(segundoNombre!=null)
                persona.setSegundoNombre(segundoNombre);
            else
                System.out.println("El segundo nombre es nulo o está en blanco en el DTO, se mantendrá el valor actual");


            var primerApellido = miembroDTO.getApellido()!=null && !miembroDTO.getApellido().isBlank() ? miembroDTO.getApellido() : persona.getPrimerApellido();
            if(primerApellido!=null)
                persona.setPrimerApellido(primerApellido);
            else
                System.out.println("El primer apellido es nulo o está en blanco en el DTO, se mantendrá el valor actual");


            var segundoApellido = miembroDTO.getApellido2()!=null && !miembroDTO.getApellido2().isBlank() ? miembroDTO.getApellido2() : persona.getSegundoApellido();
            if(segundoApellido!=null)
                persona.setSegundoApellido(segundoApellido);
            else
                System.out.println("El segundo apellido es nulo o está en blanco en el DTO, se mantendrá el valor actual");


            var fechaNacimiento = miembroDTO.getFechaNacimiento()!=null ? miembroDTO.getFechaNacimiento() : persona.getFechaNacimiento();
            if(fechaNacimiento!=null)
                persona.setFechaNacimiento(fechaNacimiento);
            else
                System.out.println("La fecha de nacimiento es nula en el DTO, se mantendrá el valor actual");


            var estadoCivil = miembroDTO.getEstadoCivil()!=null && !miembroDTO.getEstadoCivil().isBlank() ? Persona.convertirStringAEstadoCivil(miembroDTO.getEstadoCivil()) : persona.getEstadoCivil();
            if(estadoCivil!=null)
                persona.setEstadoCivil(estadoCivil);
            else
                System.out.println("El estado civil es nulo o está en blanco en el DTO, se mantendrá el valor actual");


            var correo = miembroDTO.getCorreo()!=null && !miembroDTO.getCorreo().isBlank() ? miembroDTO.getCorreo() : persona.getCorreo();
            if(correo!=null)
                persona.setCorreo(correo);
            else
                System.out.println("El correo es nulo o está en blanco en el DTO, se mantendrá el valor actual");


            var telefono = miembroDTO.getTelefono()!=null && !miembroDTO.getTelefono().isBlank() ? miembroDTO.getTelefono() : persona.getTelefono();
            if(telefono!=null)
                persona.setTelefono(telefono);
            else
                System.out.println("El teléfono es nulo o está en blanco en el DTO, se mantendrá el valor actual");


            var sexo = miembroDTO.getSexo()!=null && !miembroDTO.getSexo().isBlank() ? Persona.convertirStringASexo(miembroDTO.getSexo()) : persona.getSexo();
            if(sexo!=null)
                persona.setSexo(sexo);
            else
                System.out.println("El sexo es nulo o está en blanco en el DTO, se mantendrá el valor actual");

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

        }else
            throw new IllegalArgumentException("El ID del miembro en el DTO no coincide con el ID del miembro a actualizar o es nulo");
    }

    public MiembroDTO convertToDTO(Persona persona) {
        if(persona!=null){

            MiembroDTO dto = new MiembroDTO();

            var id = persona.getNumeroIdentificacion() != null ? persona.getNumeroIdentificacion() : null;
            if(id==null)
                throw new IllegalArgumentException("El ID del miembro es nulo al convertir a DTO");
            else
                dto.setId(id);

            var primerNombre = persona.getPrimerNombre() != null ? persona.getPrimerNombre() : null;
            if(primerNombre==null)
                throw new IllegalArgumentException("El primer nombre del miembro es nulo al convertir a DTO, no se puede convertir un miembro sin primer nombre a DTO");
            else
                dto.setNombre(primerNombre);

            var segundoNombre = persona.getSegundoNombre() != null ? persona.getSegundoNombre() : null;
            if(segundoNombre==null)
                System.out.println("¡¡¡ W A R N I N G !!! \n El segundo nombre del miembro es nulo al convertir a DTO");
            else
                dto.setNombre2(segundoNombre);

            var primerApellido = persona.getPrimerApellido() != null ? persona.getPrimerApellido() : null;
            if(primerApellido==null)
                throw new IllegalArgumentException("¡¡¡ W A R N I N G !!! \n El primer apellido del miembro es nulo al convertir a DTO, no se puede convertir un miembro sin primer apellido a DTO");
            else
                dto.setApellido(primerApellido);

            var segundoApellido = persona.getSegundoApellido() != null ? persona.getSegundoApellido() : null;
            if(segundoApellido==null)
                System.out.println("¡¡¡ W A R N I N G !!! \n El segundo apellido del miembro es nulo al convertir a DTO");
            else
                dto.setApellido2(segundoApellido);

            var fechaNacimiento = persona.getFechaNacimiento() != null ? persona.getFechaNacimiento() : null;
            if(fechaNacimiento==null)
                System.out.println("¡¡¡ W A R N I N G !!! \n La fecha de nacimiento del miembro es nula al convertir a DTO");
            else
                dto.setFechaNacimiento(fechaNacimiento);

            var estadoCivil = persona.getEstadoCivil() != null ? persona.getEstadoCivil().name() : null;
            if(estadoCivil==null)
                System.out.println("¡¡¡ W A R N I N G !!! \n El estado civil del miembro es nulo al convertir a DTO");
            else
                dto.setEstadoCivil(estadoCivil);

            var correo = persona.getCorreo() != null ? persona.getCorreo() : null;
            if(correo==null)
                System.out.println("El correo del miembro es nulo al convertir a DTO");
            else
                dto.setCorreo(correo);

            var telefono = persona.getTelefono()!=null && !persona.getTelefono().isBlank()? persona.getTelefono() :null;
            if(telefono==null)
                System.out.println("¡¡¡ W A R N I N G !!! \n El teléfono del miembro es nulo o está en blanco al convertir a DTO");
            else
                dto.setTelefono(telefono);

            var sexo = persona.getSexo() != null ? persona.getSexo().name() : null;
            if(sexo==null)
               throw new IllegalArgumentException("El sexo del miembro es nulo al convertir a DTO, no se puede convertir un miembro sin sexo a DTO");
            else
                dto.setSexo(sexo);

            // Convertir dirección
            if (persona.getDireccion() != null) {
                direccionService.converToDTO(persona.getDireccion());
            }

            // Convertir Roles
            if(persona.getRol() != null){
                dto.setRolDTO(rolService.convertToDTO(persona.getRol()));
            }

                return dto;
        }else
            throw new IllegalArgumentException("El miembro es nulo, no se puede convertir a DTO y Enviar");
    }


}
