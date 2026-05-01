package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinanzaDTO {
    private Long id;
    private String fechaInicioReporte;
    private String fechaFinalReporte;
    private String descripcionReporte;
    private String gastosTotales;
    private String ingresosTotales;
    private String egresosTotales;
    private String saldoActual;
    private String saldoAnterior;
    private String activoTotal;
    private String observaciones;
    private String fechaActualizacion;
    private List<GastoDTO> listaGastos;
    private List<ContribucionDTO> listaEntradas;

    private UsuarioDto usuarioActualizacion;
}
