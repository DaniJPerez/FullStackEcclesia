package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.ContribucionDTO;
import com.proyectoBase.gestionEcclesia.DTOS.EventoDTO;
import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.modele.Contribucion;
import com.proyectoBase.gestionEcclesia.modele.Evento;
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
public class ContribucionService {

    private final ContribucionRepository contribucionRepository;
    private final MiembroService miembroService;
    private final EventoService eventoService;

    public List<Contribucion> findAll() {
        return contribucionRepository.findAll();
    }

    public Contribucion findById(Long id) {
        return contribucionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contribución no encontrada con ID: " + id));
    }

    public List<Contribucion> findByMiembro(Long miembroId) {
        Persona persona = miembroService.findById(miembroId);
        return contribucionRepository.findByPersona(persona);
    }

    public List<Contribucion> findByEvento(Long eventoId) {
        Evento evento = eventoService.findById(eventoId);
        return contribucionRepository.findByEvento(evento);
    }

    public List<Contribucion> findByFechas(LocalDate inicio, LocalDate fin) {
        return contribucionRepository.findByFechaContribucionBetween(inicio, fin);
    }

    public List<Contribucion> findByEvento(EventoDTO eventoDTO) {
        Evento evento = eventoService.findById(eventoDTO.getId());
        return contribucionRepository.findByEvento(evento);
    }


    @Transactional
    public Contribucion save(ContribucionDTO contribucionDTO) {
        Contribucion contribucion = new Contribucion();
        updateContribucionFromDTO(contribucion, contribucionDTO);
        return contribucionRepository.save(contribucion);
    }

    @Transactional
    public Contribucion update(Long id, ContribucionDTO contribucionDTO) {
        Contribucion contribucion = findById(id);
        updateContribucionFromDTO(contribucion, contribucionDTO);
        return contribucion;
    }

    @Transactional
    public void delete(Long id) {
        Contribucion contribucion = findById(id);
        contribucionRepository.delete(contribucion);
    }

    private void updateContribucionFromDTO(Contribucion contribucion, ContribucionDTO contribucionDTO) {
      contribucion.setId(contribucionDTO.getId());
      contribucion.setPersona(miembroService.updateMiembroFromDTO(contribucion.getPersona(), contribucionDTO.getMiembro()));
      contribucion.setFechaContribucion(contribucionDTO.getFechaContribucion());
      contribucion.setMonto(contribucionDTO.getMonto());

    }

    public ContribucionDTO convertToDTO(Contribucion contribucion) {
        ContribucionDTO contribucionDTO = new ContribucionDTO();

        // Asegúrate de que estos métodos estén definidos en la clase base o usa instanceof
        contribucionDTO.setId(contribucion.getId());
        contribucionDTO.setFechaContribucion(contribucion.getFechaContribucion());
        contribucionDTO.setObservaciones(contribucion.getDescripcion());

        MiembroDTO miembroDTO = miembroService.convertToDTO(contribucion.getPersona());
        contribucionDTO.setMiembro(miembroDTO);
        contribucionDTO.setMonto(contribucion.getMonto());
        return contribucionDTO;
    }

    public MiembroDTO agregarContribucionAMiembroDto(MiembroDTO miembroDTO){
        Persona persona = miembroService.findById(miembroDTO.getId());
        List<Contribucion> contribuciones = contribucionRepository.findByPersona(persona);

        List<ContribucionDTO> contribucionDTOs = contribuciones.stream()
                .map(this::convertToDTO)
                .toList();
        miembroDTO.setContribuciones(contribucionDTOs);

        return miembroDTO;
    }

    public EventoDTO agregarContribucionAEventoDto(EventoDTO eventoDTO){
        Evento evento = eventoService.findById(eventoDTO.getId());
        List<Contribucion> contribuciones = contribucionRepository.findByEvento(evento);

        List<ContribucionDTO> contribucionDTOs = contribuciones.stream()
                .map(this::convertToDTO)
                .toList();
        eventoDTO.setContribuciones(contribucionDTOs);

        return eventoDTO;
    }
}
