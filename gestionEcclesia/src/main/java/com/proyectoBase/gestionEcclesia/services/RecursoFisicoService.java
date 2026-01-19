package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.RecursoFisicoDTO;
import com.proyectoBase.gestionEcclesia.modele.RecursoFisico;
import com.proyectoBase.gestionEcclesia.repositories.RecursoFisicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private void updateRecursoFisicoFromDTO(RecursoFisico recursoFisico, RecursoFisicoDTO recursoFisicoDTO) {
        recursoFisico.setDescripcionRecurso(recursoFisicoDTO.getDescripcion());
        recursoFisico.setCantidad(recursoFisicoDTO.getCantidad());
        recursoFisico.setCosto(recursoFisicoDTO.getValorUnitarioEstimado());
        recursoFisico.setDescripcionRecurso(recursoFisicoDTO.getObservaciones());
    }

    public RecursoFisicoDTO convertToDTO(RecursoFisico recursoFisico) {
        RecursoFisicoDTO recursoFisicoDTO = new RecursoFisicoDTO();
        recursoFisicoDTO.setId(recursoFisicoDTO.getId());
        recursoFisicoDTO.setCantidad(recursoFisico.getCantidad());
        recursoFisicoDTO.setDescripcion(recursoFisico.getDescripcionRecurso());
        recursoFisicoDTO.setDireccionDTO(direccionService.converToDTO(recursoFisico.getDireccion()));
        return recursoFisicoDTO;
    }
}
