package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.FinanzaDTO;
import com.proyectoBase.gestionEcclesia.config.UsuarioContextUtil;
import com.proyectoBase.gestionEcclesia.modele.Contribucion;
import com.proyectoBase.gestionEcclesia.modele.Finanza;
import com.proyectoBase.gestionEcclesia.modele.Gasto;
import com.proyectoBase.gestionEcclesia.repositories.ContribucionRepository;
import com.proyectoBase.gestionEcclesia.repositories.FinanzaRepository;
import com.proyectoBase.gestionEcclesia.repositories.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanzaServices {

    private final FinanzaRepository finanzaRepository;
    private final ContribucionRepository contribucionRepository;
    private final GastoRepository gastoRepository;
    private final ContribucionService contribucionService;
    private final GastoServices gastoServices;
    private final UsuarioContextUtil usuarioContextUtil;

    public List<Finanza> findAll() {
        return finanzaRepository.findAll();
    }

    public Finanza findById(Long id) {
        return finanzaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Finanza no encontrada con ID: " + id));
    }

    public List<Finanza> findByRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin)  {

        return finanzaRepository.findByRangoFechas(fechaInicio, fechaFin);
    }



    @Transactional
    public Finanza saveFinanza(FinanzaDTO finanzaDTO) {
        Finanza finanza = new Finanza();
        updateFinanzaFromDTO(finanza, finanzaDTO);
        finanza.setUsuarioRegistro(usuarioContextUtil.getUsuarioAutenticado());
        return finanzaRepository.save(finanza);
    }

    @Transactional
    public Finanza updateFinanza(Long id, FinanzaDTO finanzaDTO) {
        Finanza finanza = findById(id);
        updateFinanzaFromDTO(finanza, finanzaDTO);
        return finanzaRepository.save(finanza);
    }

    @Transactional
    public void deleteFinanza(Long id) {
        Finanza finanza = findById(id);
        finanzaRepository.delete(finanza);
    }

    public Finanza updateFinanzaFromDTO(Finanza finanza, FinanzaDTO finanzaDTO) {
        if (finanza != null && finanzaDTO != null) {

            var fechaInicialReporte = finanzaDTO.getFechaFinalReporte() != null
                    ? LocalDate.parse(finanzaDTO.getFechaInicioReporte())
                    : finanza.getFechaInicialReporte() != null ? finanza.getFechaInicialReporte() : null;
            if (fechaInicialReporte == null)
                throw new IllegalArgumentException("La fecha inicial del reporte no puede ser nula");
            else
                finanza.setFechaInicialReporte(fechaInicialReporte);

            var fechaFinalReporte = finanzaDTO.getFechaFinalReporte() != null
                    ? LocalDate.parse(finanzaDTO.getFechaFinalReporte())
                    : finanza.getFechaFinalReporte() != null ? finanza.getFechaFinalReporte() : null;
            if (fechaFinalReporte == null)
                throw new IllegalArgumentException("La fecha final del reporte no puede ser nula");
            else
                finanza.setFechaFinalReporte(fechaFinalReporte);

            if (finanzaDTO.getGastosTotales() != null) {
                var gastosTotales = Double.parseDouble(finanzaDTO.getGastosTotales());
                finanza.setGastosTotales(BigDecimal.valueOf(gastosTotales));
            }

            if (finanzaDTO.getIngresosTotales() != null) {
                var ingresosTotales = Double.parseDouble(finanzaDTO.getIngresosTotales());
                finanza.setIngresosTotales(BigDecimal.valueOf(ingresosTotales));
            }

            if (finanzaDTO.getEgresosTotales() != null) {
                var egresosTotales = Double.parseDouble(finanzaDTO.getEgresosTotales());
                finanza.setEgresosTotales(BigDecimal.valueOf(egresosTotales));
            }

            if (finanzaDTO.getSaldoActual() != null) {
                var saldoActual = Double.parseDouble(finanzaDTO.getSaldoActual());
                finanza.setSaldoActual(BigDecimal.valueOf(saldoActual));
            }

            if (finanzaDTO.getSaldoAnterior() != null){
                var saldoAnterior = Double.parseDouble(finanzaDTO.getSaldoAnterior());
                finanza.setSaldoAnterior(BigDecimal.valueOf(saldoAnterior));
            }

            if (finanzaDTO.getActivoTotal() != null){
                var activoTotal = Double.parseDouble(finanzaDTO.getActivoTotal());
                finanza.setActivoTotal(BigDecimal.valueOf(activoTotal));
            }

            if (finanzaDTO.getObservaciones() != null && !finanzaDTO.getObservaciones().isBlank())
                finanza.setObservaciones(finanzaDTO.getObservaciones());


            finanza.setFechaActualizacion(LocalDate.now());


            // Asociar contribuciones
            if (finanzaDTO.getListaEntradas() != null && !finanzaDTO.getListaEntradas().isEmpty()) {
               List<Contribucion> contribuciones = finanzaDTO.getListaEntradas().stream()
                        .map(c -> contribucionRepository.findById(c.getId())
                                .orElseThrow(() -> new IllegalArgumentException("Contribución no encontrada con ID: " + c.getId())))
                        .collect(Collectors.toList());
                finanza.setListaEntradas(contribuciones);
            }

            // Asociar gastos
            if (finanzaDTO.getListaGastos() != null && !finanzaDTO.getListaGastos().isEmpty()) {
                List<Gasto> gastos = finanzaDTO.getListaGastos().stream()
                        .map(g -> gastoRepository.findById(g.getIdGasto())
                                .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado con ID: " + g.getIdGasto())))
                        .collect(Collectors.toList());
                finanza.setListaGastos(gastos);
            }

            calcularFinanza(finanza);

            return finanza;
        } else
            throw new IllegalArgumentException("La finanza o el DTO proporcionado es nulo");
    }

    public FinanzaDTO convertToDTO(Finanza finanza) {
        if (finanza != null) {
            FinanzaDTO finanzaDTO = new FinanzaDTO();

            var id = finanza.getId() != null ? finanza.getId() : null;
            if (id == null)
                throw new IllegalArgumentException("El ID de la finanza es nulo al convertir a DTO");
            else
                finanzaDTO.setId(id);


            var fechaInicialReporte = finanza.getFechaInicialReporte() != null ? finanza.getFechaInicialReporte().toString() : null;
            if (fechaInicialReporte == null)
                System.out.println("La fecha inicial del reporte es nula al convertir a DTO, se asignará un valor por defecto");
            else{
                finanzaDTO.setFechaInicioReporte(fechaInicialReporte);
            }

            var fechaFinalReporte = finanza.getFechaFinalReporte() != null ? finanza.getFechaFinalReporte().toString() : null;
            if(fechaFinalReporte == null)
                System.out.println("La fecha final del reporte es nula al convertir a DTO, se asignará un valor por defecto");
            else
                finanzaDTO.setFechaFinalReporte(fechaFinalReporte);

            var fechaActualizacion = finanza.getFechaActualizacion() != null ? finanza.getFechaActualizacion().toString() : null;
            if(fechaActualizacion == null)
                System.out.println("La fecha de actualización del reporte es nula al convertir a DTO, se asignará un valor por defecto");
            else
                finanzaDTO.setFechaActualizacion(fechaActualizacion);

            var observaciones = finanza.getObservaciones() != null && !finanza.getObservaciones().isBlank() ? finanza.getObservaciones() : null;
            if(observaciones==null)
                System.out.println("Las observaciones del reporte son nulas al convertir a DTO, se asignará un valor por defecto");
            else
                finanzaDTO.setObservaciones(observaciones);

            var gastosTotales = finanza.getGastosTotales() != null ? finanza.getGastosTotales().toString() : null;
            if(gastosTotales==null)
                System.out.println("Los gastos totales del reporte son nulos al convertir a DTO, se asignará un valor por defecto");
            else
                finanzaDTO.setGastosTotales(gastosTotales);

            var ingresosTotales = finanza.getIngresosTotales() != null ? finanza.getIngresosTotales().toString() : null;
            if(ingresosTotales==null)
                System.out.println("Los ingresos totales del reporte son nulos al convertir a DTO, se asignará un valor por defecto");
            else
                finanzaDTO.setIngresosTotales(ingresosTotales);

            var egresosTotales = finanza.getEgresosTotales() != null ? finanza.getEgresosTotales().toString() : null;
            if (egresosTotales==null)
                System.out.println("Los egresos totales del reporte son nulos al convertir a DTO, se asignará un valor por defecto");
            else
                finanzaDTO.setEgresosTotales(egresosTotales);

            var saldoActual = finanza.getSaldoActual() != null ? finanza.getSaldoActual().toString() : null;
            if (saldoActual==null)
                System.out.println("El saldo actual del reporte es nulo al convertir a DTO, se asignará un valor por defecto");
            else
                finanzaDTO.setSaldoActual(saldoActual);

            var saldoAnterior = finanza.getSaldoAnterior() != null ? finanza.getSaldoAnterior().toString() : null;
            if (saldoAnterior==null)
                System.out.println("El saldo anterior del reporte es nulo al convertir a DTO, se asignará un valor por defecto");
            else
                finanzaDTO.setSaldoAnterior(saldoAnterior);

            var activoTotal = finanza.getActivoTotal() != null ? finanza.getActivoTotal().toString() : null;
            if (activoTotal==null)
                System.out.println("El activo total del reporte es nulo al convertir a DTO, se asignará un valor por defecto");
            else
                finanzaDTO.setActivoTotal(activoTotal);


            if (finanza.getListaEntradas() != null)
                finanzaDTO.setListaEntradas(finanza.getListaEntradas().stream()
                        .map(contribucionService::convertToDTO)
                        .collect(Collectors.toList()));


            if (finanza.getListaGastos() != null)
                finanzaDTO.setListaGastos(finanza.getListaGastos().stream()
                        .map(gastoServices::convertToDto)
                        .collect(Collectors.toList()));


            return finanzaDTO;

        } else
            throw new IllegalArgumentException("La finanza proporcionada es nula. No se puede convertir a DTO.");
    }

    public Finanza calcularFinanza(Finanza finanza) {
        if (finanza != null) {

            BigDecimal totalContribuciones = finanza.getListaEntradas() != null
                    ? finanza.getListaEntradas().stream()
                    .map(Contribucion::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    : BigDecimal.ZERO;

            BigDecimal totalGastos = finanza.getListaGastos() != null
                    ? finanza.getListaGastos().stream()
                    .map(Gasto::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    : BigDecimal.ZERO;


            var saldoAnterior = finanza.getSaldoActual()!= null ? finanza.getSaldoActual() : BigDecimal.ZERO;
            var saldoActual = saldoAnterior.add(totalContribuciones).subtract(totalGastos);
            finanza.setSaldoActual(saldoActual);

            return finanza;
        } else
            throw new IllegalArgumentException("La finanza proporcionada es nula. No se puede calcular.");

    }

}
