package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.RecursoEconomicoDTO;
import com.proyectoBase.gestionEcclesia.modele.RecursoEconomico;
import com.proyectoBase.gestionEcclesia.repositories.ContribucionRepository;
import com.proyectoBase.gestionEcclesia.repositories.RecursoEconomicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoEconomicoService {

    private final RecursoEconomicoRepository recursoEconomicoRepository;
    private final ContribucionRepository contribucionRepository;

    public List<RecursoEconomico> findAll() {
        return recursoEconomicoRepository.findAll();
    }

    public RecursoEconomico findById(Long id) {
        return recursoEconomicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso económico no encontrado con ID: " + id));
    }

  /* public List<RecursoEconomico> findByFechas(LocalDate inicio, LocalDate fin) {

       List<RecursoEconomico>recursoEconomicos=contribucionRepository.findByFechaContribucionBetween(inicio, fin).stream().filter(
                c -> c instanceof ContribucionEconomica)
                .map(c -> (ContribucionEconomica)c)
                .collect(Collectors.toList());
    }*/

    @Transactional
    public RecursoEconomico save(RecursoEconomicoDTO recursoEconomicoDTO) {
        RecursoEconomico recursoEconomico = new RecursoEconomico();
        recursoEconomico = updateRecursoEconomicoFromDTO(recursoEconomico, recursoEconomicoDTO);
        return recursoEconomicoRepository.save(recursoEconomico);
    }

    @Transactional
    public RecursoEconomico update(Long id, RecursoEconomicoDTO recursoEconomicoDTO) {
        RecursoEconomico recursoEconomico = findById(id);
        recursoEconomico= updateRecursoEconomicoFromDTO(recursoEconomico, recursoEconomicoDTO);
        return recursoEconomicoRepository.save(recursoEconomico);
    }

    @Transactional
    public void delete(Long id) {
        RecursoEconomico recursoEconomico = findById(id);
        recursoEconomicoRepository.delete(recursoEconomico);
    }

    public RecursoEconomico updateRecursoEconomicoFromDTO(RecursoEconomico recursoEconomico, RecursoEconomicoDTO recursoEconomicoDTO) {
        recursoEconomico.setDescripcionRecurso(recursoEconomicoDTO.getDescripcion());
        recursoEconomico.setFechaAdquisicion(recursoEconomicoDTO.getFecha());
        recursoEconomico.setValorRecurso(recursoEconomicoDTO.getMonto());
        recursoEconomico.setDescripcionRecurso(recursoEconomicoDTO.getObservaciones());
        return recursoEconomico;
    }

    public RecursoEconomicoDTO convertToDTO(RecursoEconomico recursoEconomico) {
        return new RecursoEconomicoDTO(
            recursoEconomico.getIdRecurso(),
            recursoEconomico.getDescripcionRecurso(),
            recursoEconomico.getFechaAdquisicion(),
            recursoEconomico.getValorRecurso(),
            recursoEconomico.getDescripcionRecurso()
        );
    }
}
