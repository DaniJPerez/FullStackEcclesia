package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginUserDTO {
    private String userName;
    private String contrasenia;
}
