package com.proyectoBase.gestionEcclesia.DTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {

    private Long id;
    
    //@NotBlank(message = "El nombre del evento es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate fechaInicio;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate fechaFinalizacion;

    private String accesibilidad;

    @NotBlank(message = "El lugar es obligatorio")
    private DireccionDTO direccionDTO;

    private String estadoEvento;
    
    private List<AsistenciaEventoDto> asistentesIds;

    private List<ContribucionDTO> contribuciones;

    private UsuarioDto usuarioCreador;
}
