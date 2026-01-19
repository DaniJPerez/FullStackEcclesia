package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.Embeddable;

@Embeddable
public enum TipoAccesibilidadEvento {
    PUBLICO,
    INVITADOS,
    ACCESO_RESTRIGIDO,
    PRIVADO
}
