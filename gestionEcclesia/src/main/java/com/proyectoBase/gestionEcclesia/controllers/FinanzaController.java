package com.proyectoBase.gestionEcclesia.controllers;

import com.proyectoBase.gestionEcclesia.DTOS.ContribucionDTO;
import com.proyectoBase.gestionEcclesia.DTOS.FinanzaDTO;
import com.proyectoBase.gestionEcclesia.DTOS.GastoDTO;
import com.proyectoBase.gestionEcclesia.modele.Contribucion;
import com.proyectoBase.gestionEcclesia.modele.Finanza;
import com.proyectoBase.gestionEcclesia.modele.Gasto;
import com.proyectoBase.gestionEcclesia.services.FinanzaServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finanzas")
@RequiredArgsConstructor
public class FinanzaController {

    private final FinanzaServices finanzaServices;
    private final ContribucionController contribucionController;
    private final GastoController gastoController;

    @GetMapping("/mostrarTodos")
    public ResponseEntity<List<FinanzaDTO>> getAllFinanzas() {
        List<FinanzaDTO> finanzas = finanzaServices.findAll().stream()
                .map(finanzaServices::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(finanzas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinanzaDTO> getFinanzaById(@PathVariable Long id) {
        Finanza finanza = finanzaServices.findById(id);
        return ResponseEntity.ok(finanzaServices.convertToDTO(finanza));
    }

    @GetMapping("/reporte")
    public ResponseEntity<List<FinanzaDTO>> getFinanzasPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<FinanzaDTO> finanzas = finanzaServices.findByRangoFechas(fechaInicio, fechaFin).stream()
                .map(finanzaServices::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(finanzas);
    }

    @GetMapping("/reporteEntreFechas")
    public ResponseEntity<FinanzaDTO> generarReporteEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<GastoDTO> gastos = gastoController.getGastosEntreFechas(fechaInicio, fechaFin).getBody();

        List<ContribucionDTO> contricuciones = contribucionController.getContribucionesEntreFechas(fechaInicio, fechaFin).getBody();

        FinanzaDTO finanzaDTO = new FinanzaDTO();
        finanzaDTO.setFechaInicioReporte(fechaInicio.toString());
        finanzaDTO.setFechaFinalReporte(fechaFin.toString());
        finanzaDTO.setListaGastos(gastos);
        finanzaDTO.setListaEntradas(contricuciones);

        Finanza finanza = new Finanza();

        finanzaServices.updateFinanzaFromDTO(finanza, finanzaDTO);

        var envioFinanza = finanzaServices.convertToDTO(finanza);

        return ResponseEntity.ok(envioFinanza);
    }

    @PostMapping("/crear")
    public ResponseEntity<FinanzaDTO> createFinanza(@Valid @RequestBody FinanzaDTO finanzaDTO) {
        Finanza finanza = finanzaServices.saveFinanza(finanzaDTO);
        return new ResponseEntity<>(finanzaServices.convertToDTO(finanza), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinanzaDTO> updateFinanza(@PathVariable Long id, @Valid @RequestBody FinanzaDTO finanzaDTO) {
        Finanza finanza = finanzaServices.updateFinanza(id, finanzaDTO);
        return ResponseEntity.ok(finanzaServices.convertToDTO(finanza));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinanza(@PathVariable Long id) {
        finanzaServices.deleteFinanza(id);
        return ResponseEntity.noContent().build();
    }
}
