package com.proyectoBase.gestionEcclesia.DTOS;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TipoEventoDTO {
    private Long idTipoEvento;
    private String nombreTipoEvento;
    private String descripcionTipoEvento;
}
