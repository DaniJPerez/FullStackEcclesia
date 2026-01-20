package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntidadTerritotialDto {
    private Long idEntidad;
    private String nombreEntidad;
    private String descripcionEntidad;
    private PaisDto pais;
    private UsuarioDto usuarioCreador;
}
