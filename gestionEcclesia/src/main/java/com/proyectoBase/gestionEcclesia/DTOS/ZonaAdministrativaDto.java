package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ZonaAdministrativaDto {
    public Long idZona;
    public String nombreZona;
    public String descripcionZona;
    public MiembroDTO responsableZona;
    public UsuarioDto usuarioCreador;

}
