package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IglesiaDTO {
    private String idIglesia;

    private String nombreIglesia;

    private String direccionIglesia;

    private String telefonoIglesia;

    private String emailIglesia;

    private MiembroDTO pastorIglesia;

    private EntidadTerritotialDto entidadTerritotial;

    private ZonaAbministrativaDto zonaAbministrativa;

    private List<MiembroDTO> miembros;

    private List<RecursoFisicoDTO> recursosFisicos;

    private UsuarioDto usuarioCreador;

}
