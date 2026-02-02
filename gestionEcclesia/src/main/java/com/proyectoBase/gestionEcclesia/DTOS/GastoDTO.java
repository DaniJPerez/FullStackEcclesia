package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GastoDTO {
    private Long idGasto;
    private String descripcionGasto;
    private String  montoGasto;
    private String fechaGasto;
    private UsuarioDto usuarioCreador;

}
