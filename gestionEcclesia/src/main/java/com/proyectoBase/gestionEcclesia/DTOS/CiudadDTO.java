package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CiudadDTO {
    private Long idCiudad;
    private String nombreCiudad;
    private UsuarioDto usuarioCreador;
}
