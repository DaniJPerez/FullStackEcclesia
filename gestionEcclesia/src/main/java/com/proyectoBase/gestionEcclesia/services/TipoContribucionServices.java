package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.TipoContribucionDTO;
import com.proyectoBase.gestionEcclesia.modele.TipoContribucion;
import com.proyectoBase.gestionEcclesia.repositories.TipoContribucionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoContribucionServices {
    private final TipoContribucionRepository repository;

    public List<TipoContribucion> getAllTipoContribuciones() {
        return repository.findAll();
    }

    public TipoContribucion getTipoContribucionById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tipo Contribucion no encontrada con id: " + id));
    }

    public TipoContribucion createTipoContribucion(TipoContribucionDTO tipoContribucionDTO) {
        TipoContribucion tipoContribucion = new TipoContribucion();
        updateTipoContribucionFromDto(tipoContribucion, tipoContribucionDTO);

        return repository.save(tipoContribucion);
    }

    public TipoContribucion updateTipoContribucion(Long id, TipoContribucionDTO tipoContribucionDTO) {
        TipoContribucion tipoContribucion = getTipoContribucionById(id);
        updateTipoContribucionFromDto(tipoContribucion, tipoContribucionDTO);

        return tipoContribucion;
    }

    public void deleteTipoContribucion(Long id) {
        TipoContribucion tipoContribucion = getTipoContribucionById(id);
        repository.delete(tipoContribucion);
    }

    public TipoContribucion updateTipoContribucionFromDto(TipoContribucion tipoContribucion, TipoContribucionDTO tipoContribucionDto) {
        if(tipoContribucion!=null && tipoContribucionDto!=null){
        var id = (tipoContribucionDto.getIdTipoContribucion() != null)
                ? tipoContribucionDto.getIdTipoContribucion()
                : (tipoContribucion.getId() != null ? tipoContribucion.getId() : null);
        if(id==null)
            System.out.println("El ID del tipo de contribucion es nulo al actualizar, se asignará uno nuevo al guardar");
        else
            tipoContribucion.setId(tipoContribucionDto.getIdTipoContribucion());

        var nombre = (tipoContribucionDto.getNombreTipoContribucion() != null)
                ? tipoContribucionDto.getNombreTipoContribucion()
                : tipoContribucion.getNombre()!=null && !tipoContribucion.getNombre().isBlank() ? tipoContribucion.getNombre() : null;
        if (nombre == null)
            System.out.println("El nombre del tipo de contribucion es nulo al actualizar, se asignará uno nuevo al guardar");
        else
            tipoContribucion.setNombre(tipoContribucion.getNombre());

        var descripcion = (tipoContribucionDto.getDescripcionTipoContribucion() != null && !tipoContribucionDto.getDescripcionTipoContribucion().isBlank())
                ? tipoContribucionDto.getDescripcionTipoContribucion()
                : tipoContribucion.getDescripcion()!=null && !tipoContribucion.getDescripcion().isBlank() ? tipoContribucion.getDescripcion() : null;
        if (descripcion == null)
            System.out.println("La descripción del tipo de contribucion es nula al actualizar, se asignará una nueva al guardar");
        else
            tipoContribucion.setDescripcion(tipoContribucionDto.getDescripcionTipoContribucion());
        return tipoContribucion;
        }else
            throw new IllegalArgumentException("El objeto TipoContribucion o TipoContribucionDTO es nulo al actualizar");
    }

    public TipoContribucionDTO convertToDto(TipoContribucion tipoContribucion) {
        if(tipoContribucion!=null){

            TipoContribucionDTO tipoContribucionDto = new TipoContribucionDTO();

            var id = (tipoContribucion.getId() != null)
                    ? tipoContribucion.getId()
                    : null;
            if(id==null)
                throw new IllegalArgumentException("El ID del tipo de contribucion es nulo al convertir a DTO, HUBO UN ERROR AL CONVERTIR LA ENTIDAD A DTO");
            else
                tipoContribucionDto.setIdTipoContribucion(tipoContribucion.getId());

            var nombre = tipoContribucion.getNombre() != null ? tipoContribucion.getNombre() : null;
            if(nombre==null)
                throw new IllegalArgumentException("El nombre del tipo de contribucion es nulo al convertir a DTO, HUBO UN ERROR AL CONVERTIR LA ENTIDAD A DTO");
            else
                tipoContribucionDto.setNombreTipoContribucion(tipoContribucion.getNombre());

            var descripcion = tipoContribucion.getDescripcion() != null ? tipoContribucion.getDescripcion() : null;
            if(descripcion==null)
                System.out.println("La descripción del tipo de contribucion es nula al convertir a DTO, No se asignará una descripción vacía al guardar");
            else
                tipoContribucionDto.setDescripcionTipoContribucion(tipoContribucion.getDescripcion());

            return tipoContribucionDto;

        }else
            throw new IllegalArgumentException("El objeto TipoContribucion es nulo al convertir a DTO");
    }

}
