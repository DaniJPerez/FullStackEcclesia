package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ComunaDTO {
    private Long idComuna;
    private String descripcionComuna;
    private CiudadDTO ciudadDTO;
    private UsuarioDto usuarioCreador;

}
