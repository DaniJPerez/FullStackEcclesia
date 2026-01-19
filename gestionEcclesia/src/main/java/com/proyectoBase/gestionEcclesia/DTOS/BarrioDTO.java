package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarrioDTO {
    private Long idBarrio;
    private String nombreBarrio;
    private ComunaDTO comunaDTO;
    private UsuarioDto usuarioCreador;
}
