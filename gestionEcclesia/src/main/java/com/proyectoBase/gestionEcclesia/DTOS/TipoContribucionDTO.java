package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TipoContribucionDTO {
    private Long idTipoContribucion;
    private String nombreTipoContribucion;
    private String descripcionTipoContribucion;
}
