package com.proyectoBase.gestionEcclesia.DTOS;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AsistenciaEventoDto {

    private Long idAsistencia;

    @NotNull(message = "el evento es obligatorio")
    private EventoDTO evento;

    @NotNull(message = "el miembro es obligatorio")
    private MiembroDTO miembro;

    @DateTimeFormat(pattern="yyyy-mm-dd")
    private LocalDate fechaAsistencia;

    private String estadoAsistencia;
}
