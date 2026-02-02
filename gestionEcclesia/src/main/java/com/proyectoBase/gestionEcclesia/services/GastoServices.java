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
        gasto.setId(gastoDTO.getIdGasto());
        gasto.setMonto(
                BigDecimal.valueOf(Long.parseLong(gastoDTO.getMontoGasto()))
        );

        gasto.setFechaRegistro(LocalDate.parse(gastoDTO.getFechaGasto()));

        gasto.setDescripcion(gastoDTO.getDescripcionGasto());

        return gasto;
    }

    public GastoDTO convertToDto(Gasto gasto){
        GastoDTO gastoDTO = new GastoDTO();
        gastoDTO.setIdGasto(gasto.getId());
        gastoDTO.setMontoGasto(String.valueOf(gasto.getMonto()));
        gastoDTO.setFechaGasto(gasto.getFechaRegistro().toString());
        gastoDTO.setDescripcionGasto(gasto.getDescripcion());

        return gastoDTO;
    }
}
