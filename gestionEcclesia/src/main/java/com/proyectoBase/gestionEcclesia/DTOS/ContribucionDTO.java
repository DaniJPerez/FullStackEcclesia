package com.proyectoBase.gestionEcclesia.DTOS;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContribucionDTO {
    private Long id;
    
    @NotNull(message = "La fecha de contribución es obligatoria")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate fechaContribucion;
    
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;
    
    private String observaciones;
    
    @NotNull(message = "El ID del miembro es obligatorio")
    private MiembroDTO miembro;
    private UsuarioDto usuarioCreador;
}
