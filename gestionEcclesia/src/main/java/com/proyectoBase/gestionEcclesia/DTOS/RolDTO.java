package com.proyectoBase.gestionEcclesia.DTOS;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {

    private Long id;
    
    @NotBlank(message = "El tipo de rol es obligatorio")
    private String nombreRol;
    
    @NotBlank(message = "El nombre del rol es obligatorio")
    private String descripcionRol;


}
