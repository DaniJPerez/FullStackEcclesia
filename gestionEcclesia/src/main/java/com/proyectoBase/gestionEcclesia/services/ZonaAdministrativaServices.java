package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.ZonaAdministrativaDto;
import com.proyectoBase.gestionEcclesia.modele.ZonaAdministrativa;
import com.proyectoBase.gestionEcclesia.repositories.ZonaAdministrativaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZonaAdministrativaServices {
    private final ZonaAdministrativaRepository repository;
    private final MiembroService miembroService;

    public List<ZonaAdministrativa> getAllZonasAdministrativas() {
        return repository.findAll();
    }

    public ZonaAdministrativa getZonaAdministrativaById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Zona Abministrativa no encontrada con id: " + id));
    }

    public ZonaAdministrativa createZonaAdministrativa(ZonaAdministrativaDto zonaAdministrativaDto) {
        ZonaAdministrativa zonaAdministrativa = new ZonaAdministrativa();
        updateZonaAdministrativaFromDto(zonaAdministrativa, zonaAdministrativaDto);

        return repository.save(zonaAdministrativa);
    }

    public ZonaAdministrativa updateZonaAdministrativa(Long id, ZonaAdministrativaDto zonaAdministrativaDto) {
        ZonaAdministrativa zonaAdministrativa = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("Zona Abministrativa no encontrada con id: " + id));
        updateZonaAdministrativaFromDto(zonaAdministrativa, zonaAdministrativaDto);

        return zonaAdministrativa;
    }


    public void deleteZonaAdministrativa(Long id) {
        ZonaAdministrativa zonaAdministrativa = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Zona Abministrativa no encontrada con id: " + id));
        repository.delete(zonaAdministrativa);
    }

    public ZonaAdministrativa updateZonaAdministrativaFromDto(ZonaAdministrativa zonaAdministrativa, ZonaAdministrativaDto zonaAdministrativaDto) {
        if(zonaAdministrativa!=null && zonaAdministrativaDto!=null){

            var id= (zonaAdministrativaDto.getIdZona() != null)
                    ? zonaAdministrativaDto.getIdZona()
                    : (zonaAdministrativa.getIdZona() != null ? zonaAdministrativa.getIdZona() : null);
            if(id==null)
                System.out.println("No hay una Id Expesificada se estara creando una nueva instancia de Zona Administrativa");
            else
                zonaAdministrativa.setIdZona(zonaAdministrativaDto.getIdZona());

            var nombre = (zonaAdministrativaDto.getNombreZona() != null && !zonaAdministrativaDto.getNombreZona().isBlank())
                    ? zonaAdministrativaDto.getNombreZona()
                    : (zonaAdministrativa.getNombreZona() != null && !zonaAdministrativa.getNombreZona().isBlank() ? zonaAdministrativa.getNombreZona() : null);
            if (nombre == null)
                System.out.println("El nombre de la Zona Administrativa es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                zonaAdministrativa.setNombreZona(zonaAdministrativaDto.getNombreZona());

            var descripcion = (zonaAdministrativaDto.getDescripcionZona() != null && !zonaAdministrativaDto.getDescripcionZona().isBlank())
                    ? zonaAdministrativaDto.getDescripcionZona()
                    : (zonaAdministrativa.getDescripcion() != null && !zonaAdministrativa.getDescripcion().isBlank() ? zonaAdministrativa.getDescripcion() : null);
            if (descripcion == null)
                System.out.println("La descripción de la Zona Administrativa es nula al actualizar, se asignará una nueva al guardar");
            else
                zonaAdministrativa.setDescripcion(zonaAdministrativaDto.getDescripcionZona());

            if(zonaAdministrativaDto.getResponsableZona()!=null)
                zonaAdministrativa.setLiderZona(miembroService.updateMiembroFromDTO(zonaAdministrativa.getLiderZona(), zonaAdministrativaDto.getResponsableZona()));


            return zonaAdministrativa;

        }else
            throw new IllegalArgumentException("Zona Administrativa o ZonaAdministrativaDto no pueden ser nulos al actualizar");
    }

    public ZonaAdministrativaDto convertToDto(ZonaAdministrativa zonaAdministrativa) {
        if(zonaAdministrativa!=null){

            ZonaAdministrativaDto zonaAdministrativaDto = new ZonaAdministrativaDto();

            var id= (zonaAdministrativa.getIdZona() != null)
                    ? zonaAdministrativa.getIdZona()
                    : null;
            if(id==null)
                throw new IllegalArgumentException("El ID de la Zona Administrativa es nulo al convertir a DTO, HUBO UN ERROR AL CONVERTIR LA ENTIDAD A DTO");
            else
                zonaAdministrativaDto.setIdZona(zonaAdministrativa.getIdZona());

            var nombre = (zonaAdministrativa.getNombreZona() != null && !zonaAdministrativa.getNombreZona().isBlank())
                    ? zonaAdministrativa.getNombreZona()
                    : null;
            if (nombre == null)
                throw new IllegalArgumentException("El nombre de la Zona Administrativa es nulo al convertir a DTO, HUBO UN ERROR AL CONVERTIR LA ENTIDAD A DTO");
            else
                zonaAdministrativaDto.setNombreZona(zonaAdministrativa.getNombreZona());

            var descripcion = (zonaAdministrativa.getDescripcion() != null && !zonaAdministrativa.getDescripcion().isBlank())
                    ? zonaAdministrativa.getDescripcion()
                    : null;
            if (descripcion == null)
                System.out.println("La descripción de la Zona Administrativa es nula al convertir a DTO, No se asignará una descripción vacía al guardar");
            else
                zonaAdministrativaDto.setDescripcionZona(zonaAdministrativa.getDescripcion());

            if(zonaAdministrativa.getLiderZona()!=null)
                zonaAdministrativaDto.setResponsableZona(miembroService.convertToDTO(zonaAdministrativa.getLiderZona()));

            return zonaAdministrativaDto;

        }else
            throw new IllegalArgumentException("Zona Administrativa no puede ser nula al convertir a DTO");
    }

}
