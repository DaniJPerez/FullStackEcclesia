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
    private final MiembroService miembroService;

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
        updateRecursoEconomicoFromDTO(recursoEconomico, recursoEconomicoDTO);
        return recursoEconomicoRepository.save(recursoEconomico);
    }

    @Transactional
    public RecursoEconomico update(Long id, RecursoEconomicoDTO recursoEconomicoDTO) {
        RecursoEconomico recursoEconomico = findById(id);
        updateRecursoEconomicoFromDTO(recursoEconomico, recursoEconomicoDTO);
        return recursoEconomicoRepository.save(recursoEconomico);
    }

    @Transactional
    public void delete(Long id) {
        RecursoEconomico recursoEconomico = findById(id);
        recursoEconomicoRepository.delete(recursoEconomico);
    }

    public RecursoEconomico updateRecursoEconomicoFromDTO(RecursoEconomico recursoEconomico, RecursoEconomicoDTO recursoEconomicoDTO) {
        if(recursoEconomico!=null && recursoEconomicoDTO!=null){

            var id = (recursoEconomicoDTO.getId() != null)
                    ? recursoEconomicoDTO.getId()
                    : recursoEconomico.getIdRecurso();
            if(id==null)
                System.out.println("El ID del recurso económico es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                recursoEconomico.setIdRecurso(id);


            var descripcion = (recursoEconomicoDTO.getDescripcion() !=null)
                    ? recursoEconomicoDTO.getDescripcion()
                    : recursoEconomico.getDescripcionRecurso();
            if(descripcion==null)
                System.out.println("No se proporcionó una descripción para el recurso económico, se mantendrá la descripción actual o se asignará una nueva al guardar");
            else
                recursoEconomico.setDescripcionRecurso(descripcion);

            var fechaAdquisicion = (recursoEconomicoDTO.getFecha() != null)
                    ? recursoEconomicoDTO.getFecha()
                    : recursoEconomico.getFechaAdquisicion();
            if(fechaAdquisicion==null)
                System.out.println("No se proporcionó una fecha de adquisición para el recurso económico, se mantendrá la fecha actual o se asignará la fecha actual al guardar");
            else
                recursoEconomico.setFechaAdquisicion(fechaAdquisicion);

            var valorRecurso = (recursoEconomicoDTO.getMonto() != null)
                    ? recursoEconomicoDTO.getMonto()
                    : recursoEconomico.getValorRecurso();
            if(valorRecurso==null)
                System.out.println("No se proporcionó un valor para el recurso económico, se mantendrá el valor actual o se asignará un nuevo valor al guardar");
            else
                recursoEconomico.setValorRecurso(valorRecurso);
            if(recursoEconomico.getDonante()!=null && recursoEconomicoDTO.getMiembroDonante()!=null)
                recursoEconomico.setDonante(miembroService.updateMiembroFromDTO(recursoEconomico.getDonante(), recursoEconomicoDTO.getMiembroDonante()));

            return recursoEconomico;

        }else
            throw new IllegalArgumentException("El recurso económico o el DTO proporcionado es nulo. No se puede actualizar el recurso económico.");
    }

    public RecursoEconomicoDTO convertToDTO(RecursoEconomico recursoEconomico) {
        if(recursoEconomico!=null){
            RecursoEconomicoDTO recursoEconomicoDTO = new RecursoEconomicoDTO();

            var id = recursoEconomico.getIdRecurso() != null ? recursoEconomico.getIdRecurso() : null;
            if(id==null)
                throw new IllegalArgumentException("El ID del recurso económico es nulo al convertir a DTO, HUBO UN ERROR AL CONVERTIR LA ENTIDAD A DTO");
            else
                recursoEconomicoDTO.setId(id);

            var descripcion = recursoEconomico.getDescripcionRecurso() != null ? recursoEconomico.getDescripcionRecurso() : null;
            if(descripcion==null)
                System.out.println("La descripción del recurso económico es nula al convertir a DTO, No se asignará una descripción vacía al guardar");
            else
                recursoEconomicoDTO.setDescripcion(descripcion);

            var fechaAdquisicion = recursoEconomico.getFechaAdquisicion() != null ? recursoEconomico.getFechaAdquisicion() : null;
            if(fechaAdquisicion==null)
                System.out.println("La fecha de adquisición del recurso económico es nula al convertir a DTO, No se asignará la fecha actual al guardar");
            else
                recursoEconomicoDTO.setFecha(fechaAdquisicion);

            var valorRecurso = recursoEconomico.getValorRecurso() != null ? recursoEconomico.getValorRecurso() : null;
            if(valorRecurso==null)
                System.out.println("El valor del recurso económico es nulo al convertir a DTO, No se asignará un nuevo valor al guardar");
            else
                recursoEconomicoDTO.setMonto(valorRecurso);

            var observacion = recursoEconomico.getDescripcionRecurso() != null ? recursoEconomico.getDescripcionRecurso() : null;
            if(observacion==null)
                System.out.println("La observación del recurso económico es nula al convertir a DTO, No se asignará una nueva observación al guardar");
            else
                recursoEconomicoDTO.setObservaciones(observacion);

            return recursoEconomicoDTO;
        }else
            throw new IllegalArgumentException("El recurso económico proporcionado es nulo. No se puede convertir a DTO.");
    }
}
