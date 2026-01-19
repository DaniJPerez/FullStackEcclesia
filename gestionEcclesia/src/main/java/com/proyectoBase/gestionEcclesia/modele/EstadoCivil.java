package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.Embeddable;

@Embeddable
public enum EstadoCivil {
    SOLTERO,
    CASADO,
    VIUDO,
    DIVORCIADO,
    UNION_LIBRE
}
