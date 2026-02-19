package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.TipoEventoDTO;
import com.proyectoBase.gestionEcclesia.modele.TipoEvento;
import com.proyectoBase.gestionEcclesia.repositories.TipoEventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TipoEventoServices {
    private final TipoEventoRepository repository;

    public List<TipoEvento> findAll() {
        return repository.findAll();
    }

    public TipoEvento findById(Long id) {
        TipoEvento tipoEvento = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TipoEvento no encontrado: " + id));
        return tipoEvento;
    }

    public TipoEvento create(TipoEventoDTO tipoEventoDTO) {
        TipoEvento tipoEvento = new TipoEvento();
        updateTipoEventoFromDto(tipoEvento, tipoEventoDTO);

        System.out.println("Verificar que verdaderamente llega el objeto tipoEvento antes de guardarlo: " + tipoEvento);

        return repository.save(tipoEvento);
    }

    public TipoEvento update(Long id, TipoEventoDTO tipoEventoDTO) {
        TipoEvento tipoEvento = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TipoEvento no encontrado: " + id));

        updateTipoEventoFromDto(tipoEvento, tipoEventoDTO);

       return tipoEvento;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public TipoEvento updateTipoEventoFromDto(TipoEvento tipoEvento, TipoEventoDTO tipoEventoDTO){
        if(tipoEvento!=null && tipoEventoDTO!=null){

            var id = (tipoEventoDTO.getIdTipoEvento() != null)
                    ? tipoEventoDTO.getIdTipoEvento()
                    : (tipoEvento.getId() != null ? tipoEvento.getId() : null);
            if (id == null)
                System.out.println("El ID del tipoEvento es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                tipoEvento.setId(tipoEventoDTO.getIdTipoEvento());

            var nombre = (tipoEventoDTO.getNombreTipoEvento() != null)
                    ? tipoEventoDTO.getNombreTipoEvento()
                    : tipoEvento.getNombre();
            if (nombre == null)
                System.out.println("El nombre del tipoEvento es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                tipoEvento.setNombre(tipoEventoDTO.getNombreTipoEvento());

            return tipoEvento;
        }else
            throw new IllegalArgumentException("El objeto TipoEvento o TipoEventoDTO es nulo al actualizar");
    }

    public TipoEventoDTO convertToDto(TipoEvento tipoEvento){
        TipoEventoDTO dto = new TipoEventoDTO();
        dto.setIdTipoEvento(tipoEvento.getId());
        dto.setNombreTipoEvento(tipoEvento.getNombre());

        return dto;
    }
}
