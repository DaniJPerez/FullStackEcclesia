package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.GastoDTO;
import com.proyectoBase.gestionEcclesia.modele.Gasto;
import com.proyectoBase.gestionEcclesia.repositories.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GastoServices {
    private final GastoRepository gastoRepository;

    public List<Gasto> findAll() {
        return gastoRepository.findAll();
    }

    public Gasto findById(Long id) {
        return gastoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado con ID: " + id));
    }

    public Gasto saveGasto(GastoDTO gastoDTO){
        Gasto gasto = new Gasto();
        gasto = updateGastoFromDto(gasto,gastoDTO);

        return gastoRepository.save(gasto);
    }

    public Gasto updateGasto(Long id, GastoDTO gastoDTO){
        Gasto gasto = findById(id);
        gasto = updateGastoFromDto(gasto,gastoDTO);

        return gasto;
    }

    public void deleteGasto(Long id){
        Gasto gasto = findById(id);
        gastoRepository.delete(gasto);
    }

    public Gasto updateGastoFromDto(Gasto gasto,GastoDTO gastoDTO) {
        if(gasto!=null && gastoDTO!=null){
            var id = (gastoDTO.getIdGasto() != null)
                    ? gastoDTO.getIdGasto()
                    : gasto.getId();
            if(id==null)
                System.out.println("El ID del gasto es nulo al actualizar, se asignará uno nuevo al guardar");
            else
                gasto.setId(gastoDTO.getIdGasto());
            var monto = (gastoDTO.getMontoGasto() != null)
                    ? gastoDTO.getMontoGasto()
                    : (gasto.getMonto() != null ? gasto.getMonto().toString() : null);
            gasto.setMonto(
                    BigDecimal.valueOf(Long.parseLong(monto))
            );

            var fechaRegistro = (gastoDTO.getFechaGasto() != null && !gastoDTO.getFechaGasto().isEmpty())
                    ? gastoDTO.getFechaGasto()
                    : (gasto.getFechaRegistro() != null ? gasto.getFechaRegistro().toString() : null);
            if(fechaRegistro==null){
                System.out.println("La fecha de registro del gasto es nula al actualizar, se asignará la fecha actual al guardar");
                gasto.setFechaRegistro(LocalDate.now());
            }
            else
                gasto.setFechaRegistro(LocalDate.parse(fechaRegistro));

            var descripcion = (gastoDTO.getDescripcionGasto() != null && !gastoDTO.getDescripcionGasto().isEmpty())
                    ? gastoDTO.getDescripcionGasto()
                    : (gasto.getDescripcion() != null && !gasto.getDescripcion().isEmpty() ? gasto.getDescripcion() : null);
            if(descripcion==null)
                System.out.println("La descripción del gasto es nula al actualizar, No se asignará una descripción vacía al guardar");
             else
                gasto.setDescripcion(gastoDTO.getDescripcionGasto());

            return gasto;
        }
        else
            throw new IllegalArgumentException("HUBO UN ERROR \n NO SE PUDO ACTUALIZAR EL GASTO PORQUE EL GASTO O EL GASTO DTO SON NULOS");
    }

    public GastoDTO convertToDto(Gasto gasto){
        if(gasto!=null){
            GastoDTO gastoDTO = new GastoDTO();
            var id = gasto.getId() != null ? gasto.getId() : null;
            if(id==null) {
                throw new IllegalArgumentException("El ID del gasto es nulo al convertir a DTO HUBO UN ERROR AL CONVERTIR EL GASTO A DTO");
            }
            else
                gastoDTO.setIdGasto(gasto.getId());

            var monto = gasto.getMonto() != null ? gasto.getMonto().toString() : null;
            if(monto==null)
                throw new IllegalArgumentException("El monto del gasto es nulo al convertir a DTO HUBO UN ERROR AL CONVERTIR EL GASTO A DTO");
            else
                gastoDTO.setMontoGasto(String.valueOf(gasto.getMonto()));

            var fechaRegistro = gasto.getFechaRegistro() != null ? gasto.getFechaRegistro().toString() : null;
            if(fechaRegistro==null)
                throw new IllegalArgumentException("La fecha de registro del gasto es nula al convertir a DTO HUBO UN ERROR AL CONVERTIR EL GASTO A DTO");
            else
                gastoDTO.setFechaGasto(gasto.getFechaRegistro().toString());

            var descripcion = gasto.getDescripcion() != null && !gasto.getDescripcion().isEmpty() ? gasto.getDescripcion() : null;
            if(descripcion==null)
                throw new IllegalArgumentException("La descripción del gasto es nula al convertir a DTO HUBO UN ERROR AL CONVERTIR EL GASTO A DTO");
            else
                gastoDTO.setDescripcionGasto(gasto.getDescripcion());

            return gastoDTO;

        }else
            throw new IllegalArgumentException("HUBO UN ERROR \n NO SE PUDO CONVERTIR EL GASTO A DTO PORQUE EL GASTO ES NULO");
    }
}
