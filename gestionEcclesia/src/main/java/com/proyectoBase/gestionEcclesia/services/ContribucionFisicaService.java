package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.ContribucionDTO;
import com.proyectoBase.gestionEcclesia.modele.Contribucion;
import com.proyectoBase.gestionEcclesia.modele.TipoContribucion;
import com.proyectoBase.gestionEcclesia.modele.Persona;
import com.proyectoBase.gestionEcclesia.repositories.ContribucionRepository;
import com.proyectoBase.gestionEcclesia.repositories.MiembroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContribucionFisicaService {
  /*  //AL MOMENTO DE REQUERIRLA REALIZAR LAS RESPECTIVAS ENTIDADES Y AGREGARLAS EN ESTA CLASE SEGUN SU NECESIDAD
    private final ContribucionRepository contribucionRepository;
    private final MiembroRepository miembroRepository;

    public List<TipoContribucion> findAll() {
        return contribucionRepository.findAll();
    }

    public TipoContribucion findById(Long id) {
        return contribucionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contribución no encontrada con ID: " + id));
    }

    public List<TipoContribucion> findByMiembro(Long miembroId) {
        Persona persona = miembroRepository.findById(miembroId)
                .orElseThrow(() -> new EntityNotFoundException("Miembro no encontrado con ID: " + miembroId));
        return contribucionRepository.findByMiembro(persona);
    }

    public List<TipoContribucion> findByFechas(LocalDate inicio, LocalDate fin) {
        return contribucionRepository.findByFechaContribucionBetween(inicio, fin);
    }

    @Transactional
    public TipoContribucion save(ContribucionDTO contribucionDTO) {
        TipoContribucion tipoContribucion = new Contribucion();
        updateContribucionFromDTO(tipoContribucion, contribucionDTO);
        return contribucionRepository.save(tipoContribucion);
    }

    @Transactional
    public TipoContribucion update(Long id, ContribucionDTO contribucionDTO) {
        TipoContribucion tipoContribucion = findById(id);
        updateContribucionFromDTO(tipoContribucion, contribucionDTO);
        return contribucionRepository.save(tipoContribucion);
    }

    @Transactional
    public void delete(Long id) {
        TipoContribucion tipoContribucion = findById(id);
        contribucionRepository.delete(tipoContribucion);
    }

    private void updateContribucionFromDTO(Contribucion Contribucion, ContribucionDTO contribucionDTO) {
        tipoContribucion.setFechaContribucion(contribucionDTO.getFechaContribucion());
        //pasar monto en la clase hija usando la instancia del super
        tipoContribucion.mapSpecificFieldsFromDTO(contribucionDTO);
        //lo mismo pero de otra forma, sin usando metodos esta misma clase
       /* if (contribucion instanceof ContribucionEconomica eco) {
            BigDecimal monto = eco.getMonto();
            // usar monto...accediendo al valor de la clase hija
            ((ContribucionEconomica) contribucion).setMonto(contribucionDTO.getMonto());
        }

        tipoContribucion.setObservacionContribucion(contribucionDTO.getObservaciones());

        // Manejar miembro
        if (contribucionDTO.getMiembro().getId() != null) {
            Persona persona = miembroRepository.findById(contribucionDTO.getMiembro().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Miembro no encontrado con ID: " + contribucionDTO.getMiembro().getId()));
            tipoContribucion.setPersona(persona);
        }
    }

    public ContribucionDTO convertToDTO(Contribucion contribucion) {
        ContribucionDTO dto = new ContribucionDTO();
        dto.setId(contribucion.getIdContribucion());
        dto.setFechaContribucion(contribucion.getFechaContribucion());
        dto.setMonto(contribucion.getMonto());
        dto.setObservaciones(contribucion.getObservacionContribucion());

        if (contribucion.getPersona() != null) {
            dto.getMiembro().setId(contribucion.getPersona().getNumeroIdentificacion());
        }

        return dto;
    }
    */
}
