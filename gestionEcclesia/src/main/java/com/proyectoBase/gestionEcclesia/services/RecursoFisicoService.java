package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.RecursoFisicoDTO;
import com.proyectoBase.gestionEcclesia.modele.RecursoFisico;
import com.proyectoBase.gestionEcclesia.repositories.RecursoFisicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecursoFisicoService {

    private final RecursoFisicoRepository recursoFisicoRepository;
    private final DireccionService direccionService;

    public List<RecursoFisico> findAll() {
        return recursoFisicoRepository.findAll();
    }

    public RecursoFisico findById(Long id) {
        return recursoFisicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso físico no encontrado con ID: " + id));
    }

    public List<RecursoFisico> findByDescripcion(String descripcion) {
        return recursoFisicoRepository.findByCategoriaContainingIgnoreCase(descripcion);
    }

    @Transactional
    public RecursoFisico save(RecursoFisicoDTO recursoFisicoDTO) {
        RecursoFisico recursoFisico = new RecursoFisico();
        updateRecursoFisicoFromDTO(recursoFisico, recursoFisicoDTO);
        return recursoFisicoRepository.save(recursoFisico);
    }

    @Transactional
    public RecursoFisico update(Long id, RecursoFisicoDTO recursoFisicoDTO) {
        RecursoFisico recursoFisico = findById(id);
        updateRecursoFisicoFromDTO(recursoFisico, recursoFisicoDTO);
        return recursoFisicoRepository.save(recursoFisico);
    }

    @Transactional
    public void delete(Long id) {
        RecursoFisico recursoFisico = findById(id);
        recursoFisicoRepository.delete(recursoFisico);
    }

    private RecursoFisico updateRecursoFisicoFromDTO(RecursoFisico recursoFisico, RecursoFisicoDTO recursoFisicoDTO) {

        if(recursoFisico!=null && recursoFisicoDTO!=null){

            var id = (recursoFisicoDTO.getId() != null)
                        ? recursoFisicoDTO.getId()
                        : (recursoFisico.getIdRecurso() != null ? recursoFisico.getIdRecurso() : null);
            if (id == null)
                System.out.println("El ID del recurso físico es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                recursoFisico.setIdRecurso(id);

            var descripcion = (recursoFisicoDTO.getDescripcion() != null && !recursoFisicoDTO.getDescripcion().isBlank())
                        ? recursoFisicoDTO.getDescripcion()
                        : recursoFisico.getDescripcionRecurso();
            if (descripcion == null)
                System.out.println("La descripción del recurso físico es nula al actualizar, se asignará una nueva al guardar");
            else
                recursoFisico.setDescripcionRecurso(descripcion);

            var cantidad = (recursoFisicoDTO.getCantidad() != null)
                        ? recursoFisicoDTO.getCantidad()
                        : recursoFisico.getCantidad();
            if (Objects.isNull(cantidad))
                System.out.println("La cantidad del recurso físico es nula al actualizar, se asignará una nueva al guardar");
            else
                recursoFisico.setCantidad(cantidad);

            var consto = (recursoFisicoDTO.getValorUnitarioEstimado() != null)
                        ? recursoFisicoDTO.getValorUnitarioEstimado()
                        : recursoFisico.getCosto();
            if (consto == null)
                System.out.println("El costo del recurso físico es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                recursoFisico.setCosto(recursoFisicoDTO.getValorUnitarioEstimado());
            return recursoFisico;
        }else
            throw new IllegalArgumentException("El recurso físico o el DTO proporcionado para actualizar son nulos");
    }

    public RecursoFisicoDTO convertToDTO(RecursoFisico recursoFisico) {
        if(recursoFisico!=null){
            RecursoFisicoDTO recursoFisicoDTO = new RecursoFisicoDTO();

            var id = recursoFisico.getIdRecurso() != null ? recursoFisico.getIdRecurso() : null;
            if(id==null)
                throw new IllegalArgumentException("El ID del recurso físico es nulo al convertir a DTO HUBO UN ERROR AL CONVERTIR LA ENTIDAD A DTO");
            else
                recursoFisicoDTO.setId(recursoFisicoDTO.getId());

            var cantidad = Objects.nonNull(recursoFisico.getCantidad()) ? recursoFisico.getCantidad() : null;
            if(cantidad==null)
                System.out.println("La cantidad del recurso físico es nula al convertir a DTO, se asignará una nueva al guardar");
            else
                recursoFisicoDTO.setCantidad(recursoFisico.getCantidad());

            var descripcion = recursoFisico.getDescripcionRecurso() != null ? recursoFisico.getDescripcionRecurso() : null;
            if(descripcion==null)
                System.out.println("La descripción del recurso físico es nula al convertir a DTO, se asignará una nueva al guardar");
            else
                recursoFisicoDTO.setDescripcion(recursoFisico.getDescripcionRecurso());

            var costo = recursoFisico.getCosto() != null ? recursoFisico.getCosto() : null;
            if(costo==null)
                System.out.println("El costo del recurso físico es nulo al convertir a DTO, se asignará uno nuevo al guardar");
            else
                recursoFisicoDTO.setValorUnitarioEstimado(recursoFisico.getCosto());

            if(recursoFisico.getDireccion()!=null)
                recursoFisicoDTO.setDireccionDTO(direccionService.converToDTO(recursoFisico.getDireccion()));
            else
                System.out.println("!!! P R E C A U C I O N ¡¡¡¡ El recurso físico no tiene una dirección asociada al convertir a DTO");

            return recursoFisicoDTO;

        }else
            throw new IllegalArgumentException("El recurso físico proporcionado para convertir a DTO es nulo");
    }
}
