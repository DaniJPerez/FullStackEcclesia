package com.proyectoBase.gestionEcclesia.DTOS;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoFisicoDTO {
    private Long id;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;
    
    @NotNull(message = "El valor unitario estimado es obligatorio")
    @Positive(message = "El valor unitario estimado debe ser positivo")
    private BigDecimal valorUnitarioEstimado;

    private MiembroDTO miembroDonante;
    
    private String observaciones;

    private DireccionDTO direccionDTO;

    private UsuarioDto usuarioCreador;
}
