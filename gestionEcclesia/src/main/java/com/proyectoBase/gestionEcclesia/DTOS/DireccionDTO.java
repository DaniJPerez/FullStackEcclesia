package com.proyectoBase.gestionEcclesia.DTOS;

import com.proyectoBase.gestionEcclesia.modele.Barrio;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO {
    private Long idDireccion;
    private String calle;
    private String numero;
    private BarrioDTO barrioDTO;
    private UsuarioDto usuarioCreador;

}
