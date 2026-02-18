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

    @Transactional
    private Contribucion updateContribucionFromDTO(Contribucion contribucion, ContribucionDTO contribucionDTO) {

         var id = (contribucionDTO != null && contribucionDTO.getId() != null&& contribucionDTO.getId().toString().isEmpty())
                 ? contribucionDTO.getId()
                 : (contribucion != null ? contribucion.getId() : null);
             if (id == null)
                 System.out.println("No hay una Id Expesificada se estara creando una nueva instancia");
             else
                 contribucion.setId(id);

         var fechaContribucion = (contribucionDTO != null && contribucionDTO.getFechaContribucion() != null && !contribucionDTO.getFechaContribucion().toString().isEmpty())
                 ? contribucionDTO.getFechaContribucion()
                 : (contribucion != null ? contribucion.getFechaContribucion() : null);
             if(fechaContribucion==null)
                 System.out.println("No hay una fecha de contribucion Expesificada");
             else
                 contribucion.setFechaContribucion(fechaContribucion);

         contribucion.setPersona(miembroService.updateMiembroFromDTO(contribucion.getPersona(), contribucionDTO.getMiembro()));

         var monto = contribucionDTO.getMonto() != null || contribucion.getMonto() != null ? contribucionDTO.getMonto() : null;
           if(monto==null)
               throw new IllegalArgumentException("No hay un monto Expesificado");
           else
               contribucion.setMonto(monto);

         var descripcion = (contribucionDTO != null && contribucionDTO.getObservaciones() != null && !contribucionDTO.getObservaciones().isEmpty())
                           ? contribucionDTO.getObservaciones()
                           : (contribucion != null && contribucion.getDescripcion() !=null && !contribucion.getDescripcion().isEmpty()
                           ? contribucion.getDescripcion() : null);
           if(descripcion==null)
               System.out.println("No hay una descripcion Expesificada");
           else
               contribucion.setDescripcion(descripcion);

         return contribucion;

    }

    public ContribucionDTO convertToDTO(Contribucion contribucion) {
        ContribucionDTO contribucionDTO = new ContribucionDTO();

        // Asegúrate de que estos métodos estén definidos en la clase base o usa instanceof
        var id = contribucion.getId() != null ? contribucion.getId() : null;
            if(id==null)
                throw new IllegalArgumentException("¡¡¡ P R E C A U C I O N !!! \n : No hay una Id Expesificada en instancia de contribucion");
            else
                contribucionDTO.setId(id);

        var fechaContribucion = contribucion.getFechaContribucion() != null ? contribucion.getFechaContribucion() : null;
            if(fechaContribucion==null)
                throw new IllegalArgumentException("¡¡¡ P R E C A U C I O N !!! \n : No hay una fecha de contribucion Expesificada en instancia de contribucion");
            else
                contribucionDTO.setFechaContribucion(contribucion.getFechaContribucion());

        var observaciones= contribucion.getDescripcion()!=null ? contribucion.getDescripcion(): null;
            if(observaciones==null)
                System.out.println("¡¡¡ P R E C A U C I O N !!! \n : No hay una descripcion Expesificada en instancia de contribucion");
            else
                contribucionDTO.setObservaciones(contribucion.getDescripcion());

        MiembroDTO miembroDTO = miembroService.convertToDTO(contribucion.getPersona());
        contribucionDTO.setMiembro(miembroDTO);

        var monto = contribucion.getMonto() != null ? contribucion.getMonto() : null;
            if(monto==null)
                throw new IllegalArgumentException("¡¡¡ P R E C A U C I O N !!! \n : No hay un monto Expesificado en instancia de contribucion");
            else
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
