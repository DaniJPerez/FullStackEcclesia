package com.proyectoBase.gestionEcclesia.DTOS;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Default;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GastoDTO {

    private Long idGasto;

    private String descripcionGasto;

    @NotNull(message = "El monto del gasto es obligatorio")
    private String  montoGasto;


    private String fechaGasto;

    private UsuarioDto usuarioCreador;

}
