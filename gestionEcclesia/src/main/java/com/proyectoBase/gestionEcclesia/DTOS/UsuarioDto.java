package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioDto {
    private Long idUsuario;
    private String userName;
    private String password;
    private LocalDate fecha;
    private boolean estado;


}
