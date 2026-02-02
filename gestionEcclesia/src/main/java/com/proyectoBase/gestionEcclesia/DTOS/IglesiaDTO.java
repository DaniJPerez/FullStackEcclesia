package com.proyectoBase.gestionEcclesia.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IglesiaDTO {
    private Long idIglesia;

    private String nombreIglesia;

    private String telefonoIglesia;

    private String emailIglesia;

    private short numeroIglesia;

    private MiembroDTO pastorIglesia;

    private EntidadTerritorialDto entidadTerritotial;

    private ZonaAdministrativaDto zonaAbministrativa;

    private DireccionDTO direccion;

    private List<MiembroDTO> miembros;

    private List<RecursoEconomicoDTO> recursosEconomicos;

    private UsuarioDto usuarioCreador;

}
