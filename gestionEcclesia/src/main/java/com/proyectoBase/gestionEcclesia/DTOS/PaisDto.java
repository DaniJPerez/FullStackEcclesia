package com.proyectoBase.gestionEcclesia.DTOS;

import com.proyectoBase.gestionEcclesia.modele.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaisDto {
    private Long idPais;
    private String nombrePais;
    private String descripcion;
    private Usuario usuarioCreador;
}
