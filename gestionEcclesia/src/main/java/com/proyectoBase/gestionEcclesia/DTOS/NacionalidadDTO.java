package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NacionalidadDTO {
    private String idNacionalidad;
    private String nombreNacionalidad;
    private String descripcionNacionalidad;
    private PaisDto pais;
    private UsuarioDto usuarioCreador;
}
